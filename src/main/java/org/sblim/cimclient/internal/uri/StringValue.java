/**
 * (C) Copyright IBM Corp. 2006, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, ebak@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-11-05  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.uri;

import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.Util;

/**
 * Class UntypedStringValue parses an untyped string value.
 */
public class StringValue extends Value implements QuotedValue {

	private static final int NORMAL = 0;

	private static final int ESCAPED = 1;

	private static final int CLOSED = 2;

	/**
	 * Factory method for parsing quoted strings.
	 * 
	 * @param pUriStr
	 * @return <code>Value</code> instance
	 * @throws IllegalArgumentException
	 *             if parsing failed
	 */
	public static Value parse(URIString pUriStr) throws IllegalArgumentException {
		URIString uriStr = pUriStr.deepCopy();
		if (uriStr.charAt(0) != '\"') {
			String msg = "Starting '\"' is missing!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		int rIdx = 1;
		StringBuffer dstBuf = new StringBuffer();
		int state = NORMAL;
		while (rIdx < uriStr.length()) {
			char ch = uriStr.charAt(rIdx++);
			if (state == NORMAL) {
				if (ch == '\\') {
					state = ESCAPED;
					continue;
				}
				if (ch == '"') {
					state = CLOSED;
					break;
				}
			} else { // skip if Escaped
				state = NORMAL;
			}
			dstBuf.append(ch);
		}
		if (state != CLOSED) {
			String msg = "Closing '\"' is missing!\n" + uriStr.markPosition(rIdx);
			throw new IllegalArgumentException(msg);
		}
		uriStr.cutStarting(rIdx);
		// next character should be ',' or nothing
		if (uriStr.length() != 0 && uriStr.charAt(0) != ',') {
			String msg = "Next character should be ',' or end of string!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		pUriStr.set(uriStr);
		return new StringValue(dstBuf.toString());
	}

	private String iStr;

	private StringValue(String pStr) {
		this.iStr = pStr;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.iStr;
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.QuotedValue#toQuotedString()
	 */
	public String toQuotedString() {
		return Util.quote(this.iStr);
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		return MOF.DT_STR;
	}

}
