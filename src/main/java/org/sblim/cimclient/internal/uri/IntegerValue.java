/**
 * (C) Copyright IBM Corp. 2006, 2011
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
 * 3194680    2011-02-28  blaschke-oss Error in numeric keys
 */

package org.sblim.cimclient.internal.uri;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class IntegerValue parses and encapsulates an integer value.
 * 
 */
public class IntegerValue extends Value {

	private BigInteger iBigValue;

	private boolean iTyped;

	private int iBitWidth;

	private boolean iSigned;

	/**
	 * <pre>
	 *      integerValue 		=	binaryValue | octalValue | decimalValue | hexValue
	 *      binaryValue		=	[ &quot;+&quot; | &quot;-&quot; ] 1*binaryDigit ( &quot;b&quot; | &quot;B&quot; )
	 *      octalValue		=	[ &quot;+&quot; | &quot;-&quot; ] &quot;0&quot; 1*octalDigit
	 *      decimalValue		=	[ &quot;+&quot; | &quot;-&quot; ] ( positiveDecimalDigit *decimalDigit | &quot;0&quot; )
	 *      hexValue			=	[ &quot;+&quot; | &quot;-&quot; ] ( &quot;0x&quot; | &quot;0X&quot; ) 1*hexDigit
	 * </pre>
	 * 
	 * @param pUriStr
	 * @param pTyped
	 *            - if <code>true</code> pSigned and pBitWidth will be
	 *            considered otherwise not
	 * @param pSigned
	 * @param pBitWidth
	 * @param pThrow
	 * @return a <code>Value</code> or null if parsing is failed and pThrow is
	 *         false
	 * @throws IllegalArgumentException
	 *             if parsing is failed and pThrow is true
	 */
	private static Value parse(URIString pUriStr, boolean pTyped, boolean pSigned, int pBitWidth,
			boolean pThrow) throws IllegalArgumentException {
		URIString savedUriStr = pUriStr.deepCopy();
		// get the substring till the next ',' or end of uriStr
		String valStr = pUriStr.removeTill(',');
		if (valStr == null) {
			pUriStr.set(savedUriStr);
			if (pThrow) {
				String msg = "Empty value!\n" + pUriStr.markPosition();
				pUriStr.set(savedUriStr);
				throw new IllegalArgumentException(msg);
			}
			return null;
		}

		if (valStr.compareTo("0") == 0) {
			// Special case "0" to avoid changing patterns below ("0" does not
			// match any of the patterns, so produces null IntegerValue)
			return make(valStr, 10, pTyped, pSigned, pBitWidth);
		}
		IntegerValue val;
		Matcher m = DEC_PAT.matcher(valStr);
		if (m.matches()) {
			val = make(valStr, 10, pTyped, pSigned, pBitWidth);
			if (val != null) return val;
			pUriStr.set(savedUriStr);
			if (pThrow) {
				String msg = "Failed to parse decimal value!\n" + pUriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		m = HEX_PAT.matcher(valStr);
		if (m.matches()) {
			String sign = m.group(1);
			String str = sign == null ? m.group(2) : sign + m.group(2);
			val = make(str, 16, pTyped, pSigned, pBitWidth);
			if (val != null) return val;
			pUriStr.set(savedUriStr);
			if (pThrow) {
				String msg = "Failed to parse hexadecimal value!\n" + pUriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		m = OCT_PAT.matcher(valStr);
		if (m.matches()) {
			val = make(valStr, 8, pTyped, pSigned, pBitWidth);
			if (val != null) return val;
			pUriStr.set(savedUriStr);
			if (pThrow) {
				String msg = "Failed to parse octal value!\n" + pUriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		m = BIN_PAT.matcher(valStr);
		if (m.matches()) {
			val = make(m.group(1), 2, pTyped, pSigned, pBitWidth);
			if (val != null) return val;
			pUriStr.set(savedUriStr);
			if (pThrow) {
				String msg = "Failed to parse binary value!\n" + pUriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		pUriStr.set(savedUriStr);
		if (pThrow) {
			String msg = "Failed to parse integer value!\n" + pUriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		return null;
	}

	/**
	 * Parses an untyped integer value.
	 * 
	 * @param pUriStr
	 * @return a <code>Value</code>
	 */
	public static Value parse(URIString pUriStr) {
		return parse(pUriStr, false, false, 0, false);
	}

	/**
	 * parseUnsigned
	 * 
	 * @param pUriStr
	 * @param pBitWidth
	 * @return Value
	 * @throws IllegalArgumentException
	 *             if parsing failed.
	 */
	public static Value parseUnsigned(URIString pUriStr, int pBitWidth)
			throws IllegalArgumentException {
		return parse(pUriStr, true, false, pBitWidth, true);
	}

	/**
	 * parseSigned
	 * 
	 * @param pUriStr
	 * @param pBitWidth
	 * @return Value
	 * @throws IllegalArgumentException
	 *             if parsing failed.
	 */
	public static Value parseSigned(URIString pUriStr, int pBitWidth)
			throws IllegalArgumentException {
		return parse(pUriStr, true, true, pBitWidth, true);
	}

	private static final Pattern BIN_PAT = Pattern.compile("^((?:\\+|-)?[01]+)[bB]$"),
			OCT_PAT = Pattern.compile("^(?:\\+|-)?0[0-7]+$"), DEC_PAT = Pattern
					.compile("^(?:\\+|-)?[1-9][0-9]*$"), HEX_PAT = Pattern
					.compile("^(\\+|-)?0[xX]([0-9a-fA-F]+)$");

	private IntegerValue(String pStrVal, int pRadix, boolean pTyped, boolean pSigned, int pBitWidth) {
		this.iBigValue = new BigInteger(pStrVal, pRadix);
		this.iTyped = pTyped;
		this.iSigned = pSigned;
		this.iBitWidth = pBitWidth;
	}

	private static IntegerValue make(String pStrVal, int pRadix, boolean pTyped, boolean pSigned,
			int pBitWidth) {
		IntegerValue val = new IntegerValue(pStrVal, pRadix, pTyped, pSigned, pBitWidth);
		if (pTyped) {
			if (!pSigned && val.isNegative()) {
				// TODO: tracing!
				// parsed integer is negative but it should be an unsigned
				// integer!
				return null;
			}
			if (val.bitLength() > val.getBitWidth()) {
				// TODO: tracing!
				// parsed integer cannot be fitted to the passed bitWidth!
				return null;
			}
		}
		return val;
	}

	/**
	 * byteValue
	 * 
	 * @return byte
	 */
	public byte byteValue() {
		return this.iBigValue.byteValue();
	}

	/**
	 * shortValue
	 * 
	 * @return short
	 */
	public short shortValue() {
		return this.iBigValue.shortValue();
	}

	/**
	 * intValue
	 * 
	 * @return int
	 */
	public int intValue() {
		return this.iBigValue.intValue();
	}

	/**
	 * longValue
	 * 
	 * @return long
	 */
	public long longValue() {
		return this.iBigValue.longValue();
	}

	/**
	 * bigIntValue
	 * 
	 * @return BigInteger
	 */
	public BigInteger bigIntValue() {
		return this.iBigValue;
	}

	/**
	 * isNegative
	 * 
	 * @return <code>true</code> if the number is negative
	 */
	public boolean isNegative() {
		return this.iBigValue.compareTo(BigInteger.ZERO) < 0;
	}

	/**
	 * isSigned
	 * 
	 * @return <code>true</code> if the number is signed integer
	 */
	public boolean isSigned() {
		if (this.iTyped) return this.iSigned;
		return isNegative();
	}

	/**
	 * bitLength
	 * 
	 * @return the number of bits which is required for storing this integer
	 *         value.
	 */
	public int bitLength() {
		int len = this.iBigValue.bitLength();
		if (isSigned()) ++len;
		return len;
	}

	/**
	 * getBitWidth
	 * 
	 * @return 8, 16, 32 or 64
	 */
	public int getBitWidth() {
		if (this.iTyped) return this.iBitWidth;
		int rawWidth = bitLength();
		if (rawWidth <= 8) return 8;
		if (rawWidth <= 16) return 16;
		if (rawWidth <= 32) return 32;
		return 64;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.iBigValue.toString();
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		return isSigned() ? "sint" : "uint" + getBitWidth();
	}

}
