/**
 * (C) Copyright IBM Corp. 2006, 2010
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
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3023145    2010-07-02  blaschke-oss CharValue uses # constructor instead of valueOf
 */

package org.sblim.cimclient.internal.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sblim.cimclient.internal.util.MOF;

/**
 * Class CharValue parses character value.
 */
public class CharValue extends Value implements QuotedValue {

	private char iChar;

	private static final Pattern pat = Pattern.compile("\\\\x([0-9a-fA-F]+)");

	/**
	 * charValue = // example: 'a' '\x32'
	 * 
	 * @param pUriStr
	 * @param pThrow
	 * @return <code>Value</code> or <code>null</code> if parsing is failed and
	 *         <code>pThrow</code> is <code>false</code>
	 * @throws IllegalArgumentException
	 *             if parsing is failed and <code>pThrow</code> is
	 *             <code>true</code>
	 */
	public static Value parse(URIString pUriStr, boolean pThrow) throws IllegalArgumentException {
		URIString uriStr = pUriStr.deepCopy();
		if (!uriStr.cutStarting('\'')) {
			if (pThrow) {
				String msg = "Starting \"'\" is not found!\n" + uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		// find closing '
		String charStr = uriStr.removeTill('\'', true);
		if (charStr == null) {
			if (pThrow) {
				String msg = "Closing \"'\" is not found!\n" + uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		// next char must be ',' or nothing
		if (uriStr.length() != 0 && uriStr.charAt(0) != ',') {
			if (pThrow) {
				String msg = "Character should be ',' or end of string!\n" + uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		if (charStr.length() < 1) {
			if (pThrow) {
				String msg = "Empty character is unparseable!\n" + uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		if (charStr.length() == 1) {
			pUriStr.set(uriStr);
			return new CharValue(charStr.charAt(0));
		}
		Matcher m = pat.matcher(charStr);
		if (!m.matches()) {
			if (pThrow) {
				String msg = "Unparseable character string!\n" + uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		// FIXME: is it hexadecimal value?
		String hexStr = m.group(1);
		int charCode = Integer.parseInt(hexStr, 16);
		pUriStr.set(uriStr);
		return new CharValue((char) charCode);
	}

	/**
	 * Parses a char16 value.
	 * 
	 * @param pUriStr
	 * @return Value or null if parsing failed.
	 */
	public static Value parse(URIString pUriStr) {
		return parse(pUriStr, false);
	}

	private CharValue(char pChar) {
		this.iChar = pChar;
	}

	/**
	 * getChar
	 * 
	 * @return char
	 */
	public char get() {
		return this.iChar;
	}

	/**
	 * getCharacter
	 * 
	 * @return Character
	 */
	public Character getCharacter() {
		return Character.valueOf(this.iChar);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.iChar < 32) { return "\\x" + (int) this.iChar; }
		return Character.toString(this.iChar);
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.QuotedValue#toQuotedString()
	 */
	public String toQuotedString() {
		return "'" + toString() + '\'';
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		return MOF.DT_CHAR16;
	}

}
