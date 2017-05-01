/**
 * (C) Copyright IBM Corp. 2007, 2013
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 *    2650    2013-07-18  blaschke-oss SLP opaque value handling incorrect
 */

package org.sblim.slp.internal;

import java.io.UnsupportedEncodingException;

import org.sblim.slp.ServiceLocationException;

/**
 * Convert
 * 
 */
public class Convert {

	/**
	 * ATTR_RESERVED
	 */
	public static final String ATTR_RESERVED = "(),\\!<=>~";

	/**
	 * SCOPE_RESERVED
	 */
	public static final String SCOPE_RESERVED = "(),\\!<=>~;*+";

	/**
	 * DEFAULT_RESERVED
	 */
	public static final String DEFAULT_RESERVED = ATTR_RESERVED;

	private static final byte[] EMPTY_BYTES = new byte[0];

	/**
	 * getBytes
	 * 
	 * @param pStr
	 * @return byte[]
	 */
	public static byte[] getBytes(String pStr) {
		if (pStr == null) return EMPTY_BYTES;
		try {
			return pStr.getBytes(SLPDefaults.ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding : " + SLPDefaults.ENCODING, e);
		}
	}

	/**
	 * escape
	 * 
	 * @param pStr
	 * @return String
	 */
	public static String escape(String pStr) {
		return escape(pStr, DEFAULT_RESERVED);
	}

	/**
	 * escape
	 * 
	 * @param pStr
	 * @param pReservedChars
	 * @return String
	 */
	public static String escape(String pStr, String pReservedChars) {
		if (pStr == null) return "";
		if (pReservedChars == null) return pStr;
		StringBuffer strBuf = null;
		for (int i = 0; i < pStr.length(); i++) {
			char ch = pStr.charAt(i);
			if (ch < 32 || pReservedChars.indexOf(ch) >= 0) {
				if (strBuf == null) {
					strBuf = new StringBuffer();
					if (i > 0) strBuf.append(pStr.substring(0, i));
				}
				strBuf.append(escapeChar(ch));
			} else if (strBuf != null) {
				strBuf.append(ch);
			}
		}
		return strBuf == null ? pStr : strBuf.toString();
	}

	/**
	 * unescape
	 * 
	 * @param pStr
	 * @return String
	 * @throws ServiceLocationException
	 */
	public static String unescape(String pStr) throws ServiceLocationException {
		if (pStr == null) return null;
		StringBuffer resBuf = null;
		int pos, lastPos = 0;
		while ((pos = pStr.indexOf('\\', lastPos)) >= 0) {
			char ch = unescapeChar(pStr.substring(pos));
			if (resBuf == null) resBuf = new StringBuffer();
			resBuf.append(pStr.subSequence(lastPos, pos)).append(ch);
			lastPos = pos + 3;
		}
		if (resBuf != null && lastPos < pStr.length()) resBuf.append(pStr.substring(lastPos, pStr
				.length()));
		return resBuf == null ? pStr : resBuf.toString();
	}

	/**
	 * The escape character is a backslash (UTF-8 0x5c) followed by the two
	 * hexadecimal digits of the escaped character. -> Character code is ASCII.
	 * 
	 * @param ch
	 * @return String
	 */
	private static String escapeChar(char ch) {
		int code = ch;
		String hexStr = Integer.toHexString(code).toUpperCase();
		return (hexStr.length() == 1 ? "\\0" : "\\") + hexStr;
	}

	private static char unescapeChar(String pEscSeq) throws ServiceLocationException {
		if (pEscSeq.length() < 3) throw new ServiceLocationException(
				ServiceLocationException.PARSE_ERROR,
				"Escaped character must contain 2 hex digits!\n" + "pEscSeq:" + pEscSeq);
		String hexStr = pEscSeq.substring(1, 3);
		try {
			int code = Integer.parseInt(hexStr, 16);
			return (char) code;
		} catch (NumberFormatException e) {
			throw new ServiceLocationException(ServiceLocationException.PARSE_ERROR,
					"Failed to parse hex string: " + hexStr + ", pEscSeq=" + pEscSeq + " !", e);
		}
	}

}
