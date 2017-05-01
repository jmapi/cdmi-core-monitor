/**
 * (C) Copyright IBM Corp. 2005, 2013
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag     Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 17970    2005-08-11  pineiro5     Logon from z/OS not possible
 * 1353138  2005-11-24  fiuczy       CIMObject element in HTTP header wrong encoded
 * 1535756  2006-08-07  lupusalex    Make code warning free
 * 1516242  2006-11-27  lupusalex    Support of OpenPegasus local authentication
 * 1565892  2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1688273  2007-04-19  lupusalex    Full support of HTTP trailers
 * 1715612  2007-05-09  lupusalex    FVT: Status 0 in trailer is parsed as error
 * 2003590  2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131  2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371  2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2641758  2009-02-27  blaschke-oss CIM Client does not recognize HTTP extension headers
 * 3304058  2011-05-20  blaschke-oss Use same date format in change history
 * 3553858  2012-08-06  blaschke-oss Append duplicate HTTP header fields instead of replace
 * 3601894  2013-01-23  blaschke-oss Enhance HTTP and CIM-XML tracing
 *    2635  2013-05-16  blaschke-oss Slowloris DoS attack for CIM indication listener port
 *    2718  2013-11-29  blaschke-oss Bad CIMStatusCode generates NumberFormatException
 */

package org.sblim.cimclient.internal.http;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.wbem.WBEMException;

import org.sblim.cimclient.internal.http.io.ASCIIPrintStream;
import org.sblim.cimclient.internal.http.io.TrailerException;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.logging.Messages;
import org.sblim.cimclient.internal.util.WBEMConstants;

/**
 * Class HttpHeader represents a http header block
 * 
 */
public class HttpHeader {

	private static BitSet cDontNeedEncoding;

	private static final String HEX_STR = "0123456789ABCDEF";

	private static String cDfltEncName = null;

	static {
		cDontNeedEncoding = new BitSet(256);
		int i;
		for (i = 'a'; i <= 'z'; i++) {
			cDontNeedEncoding.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++) {
			cDontNeedEncoding.set(i);
		}
		for (i = '0'; i <= '9'; i++) {
			cDontNeedEncoding.set(i);
		}
		cDontNeedEncoding.set(' ');
		cDontNeedEncoding.set('-');
		cDontNeedEncoding.set('_');
		cDontNeedEncoding.set('/');
		cDontNeedEncoding.set('.');
		cDontNeedEncoding.set('*');

	}

	private Hashtable<HeaderEntry, String> iFields = new Hashtable<HeaderEntry, String>();

	/**
	 * Ctor.
	 */
	public HttpHeader() {
	// empty
	}

	/**
	 * Ctor. Parses the header from an input stream
	 * 
	 * @param pReader
	 *            The input stream
	 * @throws IOException
	 */
	public HttpHeader(InputStream pReader) throws IOException {
		this(pReader, 0);
	}

	/**
	 * Ctor. Parses the header from an input stream
	 * 
	 * @param pReader
	 *            The input stream
	 * @param pTimeout
	 *            Maximum allowable time to read header
	 * @throws IOException
	 */
	public HttpHeader(InputStream pReader, long pTimeout) throws IOException {
		String line = null;
		long timeStart = System.currentTimeMillis();
		// TODO: this needs to be optimized!!!
		while (((line = HttpMethod.readLine(pReader)) != null) && (line.length() > 0)) {
			// get the header
			if (pTimeout > 0 && (System.currentTimeMillis() - timeStart > pTimeout)) { throw new IOException(
					WBEMConstants.INDICATION_DOS_EXCEPTION_MESSAGE); }
			try {
				int separator;
				if ((separator = line.indexOf(':')) > -1) {
					String header;
					String value;
					int headerStartIndex = 0;

					// Ignore prefix-match from HTTP extension (RFC 2774), it'll
					// look like "nn-"
					if (line.indexOf('-') == 2 && Character.isDigit(line.charAt(0))
							&& Character.isDigit(line.charAt(1))) {
						headerStartIndex = 3;
					}

					header = line.substring(headerStartIndex, separator);
					value = line.substring(separator + 1);
					// TODO validate header and value, they must not be empty
					// entries
					if (value.length() > 0 && value.startsWith(" ")) addParsedField(header, value
							.substring(1));
					else addParsedField(header, value);
				} else {
					LogAndTraceBroker.getBroker().message(Messages.HTTP_INVALID_HEADER, line);
				}
			} catch (Exception e) {
				LogAndTraceBroker.getBroker().trace(Level.FINER,
						"Exception while parsing http header", e);
				LogAndTraceBroker.getBroker().message(Messages.HTTP_INVALID_HEADER, line);
			}
		}
		return;
	}

	/**
	 * Adds a header field for client output (this means duplicate header
	 * entries are replaced)
	 * 
	 * @param pName
	 *            The name of the header field
	 * @param pValue
	 *            The value
	 */
	public void addField(String pName, String pValue) {
		if (pName == null || pName.length() == 0) return;

		if (pValue != null) {
			this.iFields.put(new HeaderEntry(pName), pValue);
		} else {
			this.iFields.remove(new HeaderEntry(pName));
		}
	}

	/**
	 * Adds a header field from parsed server input (this means duplicate header
	 * entries are appended in comma-separated list as defined by RFC 2616)
	 * 
	 * @param pName
	 *            The name of the header field
	 * @param pValue
	 *            The value
	 */
	public void addParsedField(String pName, String pValue) {
		if (pName == null || pName.length() == 0) return;

		if (pValue != null) {
			String oldValue = this.iFields.put(new HeaderEntry(pName), pValue);
			if (oldValue != null) {
				// Field already exists, so append value to existing value; it
				// is done checking put() return code, instead of checking get()
				// return code prior to put(), because typical user case does
				// not include duplicate fields
				StringBuilder combinedValue = new StringBuilder(oldValue);
				combinedValue.append(',');
				combinedValue.append(pValue);
				this.iFields.put(new HeaderEntry(pName), combinedValue.toString());
			}
		} else {
			this.iFields.remove(new HeaderEntry(pName));
		}
	}

	/**
	 * Clears all header fields
	 */
	public void clear() {
		this.iFields.clear();
	}

	/**
	 * Return an iterator over the header fields
	 * 
	 * @return The iterator
	 */
	public Iterator<Entry<HeaderEntry, String>> iterator() {
		return this.iFields.entrySet().iterator();
	}

	/**
	 * Parses a line from a header block
	 * 
	 * @param pLine
	 *            The line
	 * @return The http header
	 */
	public static HttpHeader parse(String pLine) {
		int prev = 0;
		int next = 0;
		HttpHeader header = new HttpHeader();
		if (pLine != null && pLine.length() > 0) {
			next = pLine.indexOf(',');
			while (next > -1) {
				String hdr = pLine.substring(prev, next);
				int separator = hdr.indexOf('=');
				if (separator > -1) {
					String key;
					String value;
					key = hdr.substring(0, separator);
					value = hdr.substring(separator + 1);

					header.addParsedField(key, value);
				} else {
					// something goes wrong. no separator found
				}
				prev = next + 1;
				while (Character.isSpaceChar(pLine.charAt(prev)))
					prev++;
				next = pLine.indexOf(',', prev);
			}
			String hdr = pLine.substring(prev);
			int separator = hdr.indexOf('=');
			if (separator > -1) {
				header.addParsedField(hdr.substring(0, separator), hdr.substring(separator + 1));
			}
		}

		return header;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		Iterator<Entry<HeaderEntry, String>> iterator = this.iFields.entrySet().iterator();
		while (iterator.hasNext()) {
			if (i++ > 0) buf.append(',');
			Entry<HeaderEntry, String> entry = iterator.next();
			buf.append(entry.getKey().toString());
			buf.append(": ");
			buf.append(entry.getValue().toString());
		}
		return buf.toString();
	}

	/**
	 * Removes a field from the header
	 * 
	 * @param pName
	 *            The name of the field
	 */
	public void removeField(String pName) {
		this.iFields.remove(new HeaderEntry(pName));
	}

	/**
	 * Returns a field from the header
	 * 
	 * @param pName
	 *            The name of the field
	 * @return The value
	 */
	public String getField(String pName) {
		return this.iFields.get(new HeaderEntry(pName));
	}

	/**
	 * Writes a header block to a stream
	 * 
	 * @param pWriter
	 *            The stream
	 */
	public void write(ASCIIPrintStream pWriter) {

		Iterator<Entry<HeaderEntry, String>> iterator = this.iFields.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<HeaderEntry, String> entry = iterator.next();
			pWriter.print(entry.getKey().toString());
			pWriter.print(": ");
			pWriter.print(entry.getValue().toString());
			pWriter.print("\r\n");
		}
		pWriter.print("\r\n");
	}

	/**
	 * Encodes raw data
	 * 
	 * @param pData
	 *            The raw data
	 * @return The encoded data
	 */
	public static synchronized String encode(byte[] pData) {
		String str = null;
		try {
			if (cDfltEncName == null) cDfltEncName = (String) AccessController
					.doPrivileged(new GetProperty("file.encoding"));
			str = encode(pData, cDfltEncName);
		} catch (UnsupportedEncodingException e) {
			LogAndTraceBroker.getBroker().trace(Level.FINER,
					"Exception while encoding http header data", e);
		}
		return str;
	}

	/**
	 * Encodes raw data for a given character set
	 * 
	 * @param pData
	 *            The raw data
	 * @param pEnc
	 *            The character set
	 * @return The encoded data
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(byte[] pData, String pEnc) throws UnsupportedEncodingException {

		int maxBytesPerChar = 10;
		// BufferedWriter validates encoding
		ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
		new BufferedWriter(new OutputStreamWriter(buf, pEnc));
		StringBuffer out = new StringBuffer(pData.length);

		for (int i = 0; i < pData.length; i++) {
			int c = pData[i] & 0xFF;
			if (cDontNeedEncoding.get(c)) {
				if (c == ' ') {
					out.append("%20");
				} else {
					out.append((char) c);
				}
			} else {
				out.append('%');
				out.append(HEX_STR.charAt((c >> 4) & 0x0f));
				out.append(HEX_STR.charAt(c & 0x0f));
			}
		}

		return out.toString();
	}

	/**
	 * Encodes a given string for a given character set
	 * 
	 * @param pData
	 *            The source string
	 * @param pSourceEnc
	 *            The source character set
	 * @param pTargetEnc
	 *            The target character set
	 * @return The encoded string
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String pData, String pSourceEnc, String pTargetEnc)
			throws UnsupportedEncodingException {

		return encode(pData.getBytes(pSourceEnc), pTargetEnc);
	}

	/**
	 * Class HeaderEntry represents a single header field
	 * 
	 */
	public static class HeaderEntry {

		String iHeader;

		int iHashcode;

		/**
		 * Ctor.
		 * 
		 * @param pName
		 *            The name of the header field
		 */
		public HeaderEntry(String pName) {
			this.iHeader = pName;
			this.iHashcode = pName.toUpperCase().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof HeaderEntry)) return false;
			return this.iHeader.equalsIgnoreCase(((HeaderEntry) obj).iHeader);
		}

		@Override
		public String toString() {
			return this.iHeader;
		}

		@Override
		public int hashCode() {
			return this.iHashcode;
		}
	}

	/**
	 * Class GetProperty implements privileged access to system properties
	 * 
	 */
	private static class GetProperty implements PrivilegedAction<Object> {

		String iPropertyName;

		GetProperty(String propertyName) {
			this.iPropertyName = propertyName;
		}

		public Object run() {
			return System.getProperty(this.iPropertyName);
		}
	}

	/**
	 * Throws a TrailerException if it contains recognized CIM errors in http
	 * trailer entries.
	 * 
	 * @throws TrailerException
	 */
	public void examineTrailer() throws TrailerException {
		examineTrailer(null);
	}

	/**
	 * Throws a TrailerException if it contains recognized CIM errors in http
	 * trailer entries.
	 * 
	 * @param pOrigin
	 *            The origin of the trailer (response, request, etc.)
	 * @throws TrailerException
	 */
	public void examineTrailer(String pOrigin) throws TrailerException {
		Iterator<Entry<HeaderEntry, String>> itr = this.iterator();
		int code = 0, i = 0;
		String desc = null;
		StringBuilder hdrs = null;
		if (LogAndTraceBroker.getBroker().isLoggableTrace(Level.FINER)) hdrs = new StringBuilder();
		while (itr.hasNext()) {
			Entry<HeaderEntry, String> ent = itr.next();
			String keyStr = ent.getKey().toString();
			if (hdrs != null) {
				if (i++ > 0) hdrs.append(',');
				hdrs.append(keyStr);
				hdrs.append(": ");
				hdrs.append(this.getField(keyStr));
			}
			try {
				if (keyStr.equalsIgnoreCase(WBEMConstants.HTTP_TRAILER_STATUS_CODE)) {
					String valStr = URLDecoder.decode(this.getField(keyStr), WBEMConstants.UTF8);
					try {
						code = Integer.parseInt(valStr);
					} catch (NumberFormatException e) {
						String msg = new String(WBEMConstants.HTTP_TRAILER_STATUS_CODE + " \""
								+ valStr + "\" invalid, setting to CIM_ERR_FAILED");
						LogAndTraceBroker.getBroker().trace(Level.FINER, msg, e);
						code = WBEMException.CIM_ERR_FAILED;
						if (desc == null) desc = msg;
					}
				} else if (keyStr.equalsIgnoreCase(WBEMConstants.HTTP_TRAILER_STATUS_DESCRIPTION)) {
					desc = URLDecoder.decode(this.getField(keyStr), WBEMConstants.UTF8);
				}
			} catch (UnsupportedEncodingException e) {
				// if UTF-8 isn't supported we're in real trouble
				throw new Error(e);
			}
		}
		if (hdrs != null && hdrs.length() > 0) LogAndTraceBroker.getBroker().trace(
				Level.FINER,
				(pOrigin == null ? "Unknown" : pOrigin) + " HTTP Trailer Headers= "
						+ hdrs.toString());
		if (code > 0) {
			if (desc != null) { throw new TrailerException(new WBEMException(code, desc)); }
			throw new TrailerException(new WBEMException(code));
		}
	}
}
