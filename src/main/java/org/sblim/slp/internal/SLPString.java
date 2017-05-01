/**
 * (C) Copyright IBM Corp. 2007, 2009
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
 */

package org.sblim.slp.internal;

/**
 * <pre>
 * This class provides functions for comparison of SLP message strings.
 * 
 * From RFC 2608:
 * 
 * String comparison for order and equality in SLP MUST be case
 * insensitive inside the 0x00-0x7F subrange of UTF-8 (which corresponds
 * to ASCII character encoding).  Case insensitivity SHOULD be supported
 * throughout the entire UTF-8 encoded Unicode [6] character set.
 * 
 * The case insensitivity rule applies to all string matching in SLPv2,
 * including Scope strings, SLP SPI strings, service types, attribute
 * tags and values in query handling, language tags, previous responder
 * lists.  Comparisons of URL strings, however, is case sensitive.
 * 
 * White space (SPACE, CR, LF, TAB) internal to a string value is folded
 * to a single SPACE character for the sake of string comparisons.
 * White space preceding or following a string value is ignored for the
 * purposes of string comparison.  For example, &quot;  Some String  &quot;
 * matches &quot;SOME    STRING&quot;.
 * </pre>
 */

public class SLPString {

	/**
	 * equals
	 * 
	 * @param pStr0
	 * @param pStr1
	 * @return boolean
	 */
	public static boolean equals(String pStr0, String pStr1) {
		if (pStr0 == null) return pStr1 == null;
		if (pStr1 == null) return false;
		return unify(pStr0).equals(unify(pStr1));
	}

	/**
	 * compare
	 * 
	 * @param pStr0
	 * @param pStr1
	 * @return int
	 */
	public static int compare(String pStr0, String pStr1) {
		return unify(pStr0).compareTo(unify(pStr1));
	}

	/**
	 * public for testing only.
	 * 
	 * @param pStr
	 * @return String
	 */
	public static String unify(String pStr) {
		if (pStr == null) return null;
		StringBuffer resBuf = new StringBuffer();
		boolean inSpace = false;
		for (int i = 0; i < pStr.length(); i++) {
			char ch = pStr.charAt(i);
			if (ch == ' ' || ch == '\t' || ch == 0xa || ch == 0xd) {
				if (resBuf.length() == 0) continue; // skip front white spaces
				inSpace = true;
			} else {
				if (inSpace) {
					resBuf.append(' ');
					inSpace = false;
				}
				resBuf.append(Character.toLowerCase(ch));
			}
		}
		return resBuf.toString();
	}

}
