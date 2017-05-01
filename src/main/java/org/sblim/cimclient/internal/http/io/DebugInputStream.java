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
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3062747    2010-09-21  blaschke-oss SblimCIMClient does not log all CIM-XML responces.
 * 3185833    2011-02-18  blaschke-oss missing newline when logging request/response
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
 * 3601894    2013-01-23  blaschke-oss Enhance HTTP and CIM-XML tracing
 */
package org.sblim.cimclient.internal.http.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.logging.TimeStamp;

/**
 * Class DebugInputStream is for debugging purposes
 * 
 */
public class DebugInputStream extends FilterInputStream {

	private byte[] iBuf;

	private boolean iBuffered;

	private int iCur = 0;

	private int iMaxLen = 0;

	private OutputStream iStream;

	private String iOrigin;

	/**
	 * Ctor.
	 * 
	 * @param is
	 * @param os
	 */
	public DebugInputStream(InputStream is, OutputStream os) {
		this(is, os, null);
	}

	/**
	 * Ctor.
	 * 
	 * @param is
	 * @param os
	 * @param pOrigin
	 */
	public DebugInputStream(InputStream is, OutputStream os, String pOrigin) {
		super(is);
		this.iBuf = new byte[512];
		this.iBuffered = false;
		this.iStream = os;
		this.iOrigin = pOrigin == null ? "unknown" : pOrigin;
	}

	private void buffer() throws IOException {
		this.iBuffered = true;
		int total;
		try {
			while ((total = this.in.read(this.iBuf, this.iMaxLen, this.iBuf.length - this.iMaxLen)) > -1) {
				this.iMaxLen += total;
				if (this.iMaxLen == this.iBuf.length) {
					byte b[] = new byte[this.iBuf.length << 1];
					System.arraycopy(this.iBuf, 0, b, 0, this.iBuf.length);
					this.iBuf = b;
				}
			}
		} catch (TrailerException e) {
			// TrailerException indicates complete response BUT error in trailer
			writeBuffer(this.iOrigin + " begin (TrailerException occurred)");
			throw e;
		}
		writeBuffer(this.iOrigin + " begin");
	}

	private void writeBuffer(String header) throws IOException {
		StringBuilder outStr = new StringBuilder("<--- ");
		outStr.append(header);
		outStr.append(' ');
		outStr.append(TimeStamp.formatWithMillis(System.currentTimeMillis()));
		outStr.append(" ----\n");
		outStr.append(new String(this.iBuf, 0, this.iMaxLen));
		if (this.iMaxLen > 0 && this.iBuf[this.iMaxLen - 1] != '\n') outStr.append('\n');
		outStr.append("---- ");
		outStr.append(this.iOrigin);
		outStr.append(" end ----->\n");
		if (this.iStream != null) this.iStream.write(outStr.toString().getBytes());
		if (LogAndTraceBroker.getBroker().isLoggableCIMXMLTrace(Level.FINEST)) LogAndTraceBroker
				.getBroker().traceCIMXML(Level.FINEST, outStr.toString(), false);
	}

	@Override
	public synchronized int read() throws IOException {
		if (!this.iBuffered) buffer();

		if (this.iCur >= this.iMaxLen) return -1;
		return this.iBuf[this.iCur++];
	}

	@Override
	public synchronized int read(byte b[], int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
				|| ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) { return 0; }

		int c = read();
		if (c == -1) { return -1; }
		b[off] = (byte) c;

		int i = 1;
		for (; i < len; i++) {
			c = read();
			if (c == -1) {
				break;
			}
			if (b != null) {
				b[off + i] = (byte) c;
			}
		}
		return i;
	}
}
