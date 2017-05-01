/**
 * (C) Copyright IBM Corp. 2011
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Dave Blaschke, IBM, blaschke@us.ibm.com  
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 3397922    2011-08-30  blaschke-oss support OctetString 
 */

package org.sblim.cimclient.internal.cim;

import javax.cim.UnsignedInteger8;

/**
 * This class represents a CIM octet string, or length-prefixed string, where
 * the length is four octets (32 bits) AND includes the four octets it occupies.
 * In other words, the length of the octet string is the number of characters in
 * the string plus four. There are three possible representations: <br>
 * <br>
 * 1) Byte array - This is an array of UnsignedInteger8 (unit8) objects. The
 * first four bytes contain the length, so the ASCII string "DEB" would be
 * represented as { 0x00, 0x00, 0x00, 0x07, 0x44, 0x45, 0x42 }<br>
 * <br>
 * 2) Hexadecimal string - This is a string of hexadecimal digits. The four
 * bytes after the initial "0x" contain the length, so the ASCII string "DEB"
 * would be represented as "0x00000007444542".<br>
 * <br>
 * 3) ASCII string<br>
 * <br>
 * One of these representations is passed into a constructor. The other
 * representations are created on demand when a get() method is invoked or when
 * the equals() method is invoked and the two octet strings have no
 * representations in common.
 */
public class CIMOctetString {

	private UnsignedInteger8 iBytes[];

	private String iASCIIString;

	private char iReplacementChar; // 0xFF indicates ASCII constructor used

	private String iHexString;

	private int iLength;

	/**
	 * Constructs a <code>CIMOctetString</code> from the given byte array.
	 * 
	 * @param pBytes
	 *            Byte array representation of octet string.
	 * @throws IllegalArgumentException
	 */
	public CIMOctetString(UnsignedInteger8 pBytes[]) throws IllegalArgumentException {
		// Minimum (empty) byte array is { 0x00 0x00 0x00 0x04 }
		if (pBytes == null || pBytes.length < 4) throw new IllegalArgumentException(
				"Array of bytes must contain at least four bytes");

		// Verify there are no null entries in byte array
		for (int i = pBytes.length - 1; i >= 0; i--)
			if (pBytes[i] == null) throw new IllegalArgumentException(
					"Array of bytes must not contain any null bytes");

		// Calculate length
		this.iLength = pBytes[3].byteValue() + (pBytes[2].byteValue() * 0x100)
				+ (pBytes[1].byteValue() * 0x10000) + (pBytes[0].byteValue() * 0x1000000);

		// Verify calculated length matches actual length
		if (this.iLength != pBytes.length) throw new IllegalArgumentException(
				"Array of bytes contains invalid length: found " + this.iLength + ", expected "
						+ pBytes.length);

		// Save byte array in new object
		this.iBytes = new UnsignedInteger8[this.iLength];
		for (int i = this.iLength - 1; i >= 0; i--)
			this.iBytes[i] = pBytes[i];
	}

	/**
	 * Constructs a <code>CIMOctetString</code> from the given string.
	 * 
	 * @param pString
	 *            String representation of octet string.
	 * @param pIsHex
	 *            <code>true</code> if string is hexadecimal string,
	 *            <code>false</code> if string is ASCII string.
	 * 
	 * @throws IllegalArgumentException
	 */
	public CIMOctetString(String pString, boolean pIsHex) throws IllegalArgumentException {
		if (pString == null) throw new IllegalArgumentException("String cannot be null");

		if (pIsHex) {
			// Minimum (empty) hexadecimal string is "0x00000004"
			if (pString.length() < 10) throw new IllegalArgumentException(
					"Hexadecimal string must contain \"0x\" and at least four pairs of hex digits");

			// Verify hexadecimal string starts with "0x"
			if (pString.charAt(0) != '0' || pString.charAt(1) != 'x') throw new IllegalArgumentException(
					"Hexadecimal string must begin with \"0x\"");

			// Calculate length
			try {
				this.iLength = Integer.parseInt(pString.substring(2, 10), 16);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Hexadecimal string length could not be parsed: " + e.toString());
			}

			// Verify calculated length matches actual length
			if ((this.iLength * 2) + 2 != pString.length()) throw new IllegalArgumentException(
					"Hexadecimal string contains invalid length: found " + this.iLength
							+ ", expected " + ((pString.length() - 2 / 2)));

			// Verify remainder of hexadecimal string contains only hexadecimal
			// digits
			for (int i = pString.length() - 1; i >= 10; i--) {
				char ch = pString.charAt(i);
				if (!((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F'))) throw new IllegalArgumentException(
						"Hexadecimal string could not be parsed, invalid character \'" + ch
								+ "\' at index " + i);
			}

			// Save hexadecimal string in new object
			this.iHexString = new String(pString);
		} else {
			// Calculate length
			this.iLength = pString.length() + 4;

			// Save ASCII string in new object and indicate constructor used
			this.iASCIIString = new String(pString);
			this.iReplacementChar = 0xFF;
		}
	}

	/**
	 * Takes a CIM octet string and returns <code>true</code> if it is equal to
	 * this CIM octet string. Otherwise, it returns <code>false</code>. Two
	 * octet strings are considered equal if all of their common representations
	 * are equal. If the octet strings have no representations in common, this
	 * method will build the missing one, starting with byte array and then
	 * hexadecmial string.
	 * 
	 * NOTE: The ASCII string representation is only considered if both octet
	 * strings were constructed with an ASCII string.
	 * 
	 * @param pObj
	 *            The object to be compared a CIM element.
	 * @return <code>true</code> if the specified CIM octet string equals this
	 *         CIM octet string, <code>false</code> otherwise.
	 */
	@Override
	public synchronized boolean equals(Object pObj) {
		// Verify parameter is CIMOctetString instance
		if (!(pObj instanceof CIMOctetString)) return false;

		CIMOctetString that = (CIMOctetString) pObj;
		int numCompares = 0;

		// Verify lengths are same
		if (this.iLength != that.iLength) return false;

		// Verify byte arrays match if both non-null
		if (this.iBytes != null && that.iBytes != null) {
			for (int i = this.iLength - 1; i >= 0; i--)
				if (this.iBytes[i].byteValue() != that.iBytes[i].byteValue()) return false;
			numCompares++;
		}

		// Verify hexadecimal strings match if both non-null
		if (this.iHexString != null && that.iHexString != null) {
			if (!this.iHexString.equalsIgnoreCase(that.iHexString)) return false;
			numCompares++;
		}

		// Verify ASCII strings match if both non-null
		if (this.iASCIIString != null && that.iASCIIString != null
				&& this.iReplacementChar == that.iReplacementChar) {
			if (!this.iASCIIString.equalsIgnoreCase(that.iASCIIString)) return false;
			numCompares++;
		}

		// Octet strings equal if at least one representation equal
		if (numCompares > 0) return true;

		// At this point, the two CIMOctetString instances have no
		// representations in common - time to make one

		if (this.iBytes != null && that.iBytes == null) {
			that.getBytes();
			if (this.iBytes != null && that.iBytes != null) {
				for (int i = this.iLength - 1; i >= 0; i--)
					if (this.iBytes[i].byteValue() != that.iBytes[i].byteValue()) return false;
				numCompares++;
			}
		}

		// Octet strings equal if byte arrays equal
		if (numCompares > 0) return true;

		if (this.iBytes == null && that.iBytes != null) {
			getBytes();
			if (this.iBytes != null && that.iBytes != null) {
				for (int i = this.iLength - 1; i >= 0; i--)
					if (this.iBytes[i].byteValue() != that.iBytes[i].byteValue()) return false;
				numCompares++;
			}
		}

		// Octet strings equal if byte arrays equal
		if (numCompares > 0) return true;

		if (this.iHexString != null && that.iHexString == null) {
			that.getHexString();
			if (this.iHexString != null && that.iHexString != null) {
				if (!this.iHexString.equalsIgnoreCase(that.iHexString)) return false;
				numCompares++;
			}
		}

		// Octet strings equal if byte arrays equal
		if (numCompares > 0) return true;

		if (this.iHexString == null && that.iHexString != null) {
			getHexString();
			if (this.iHexString != null && that.iHexString != null) {
				if (!this.iHexString.equalsIgnoreCase(that.iHexString)) return false;
				numCompares++;
			}
		}

		// Octet strings equal if byte arrays equal
		if (numCompares > 0) return true;

		return false;
	}

	/**
	 * Returns ASCII string representation of octet string with non-printable
	 * characters replaced by <code>pReplacementChar</code>. If the ASCII string
	 * has not yet been created, it is created from the byte array or
	 * hexadecimal string.
	 * 
	 * @param pReplacementChar
	 *            Replacement character for non-printable characters which must
	 *            be between 0x20 and 0x7E, inclusive.
	 * @return ASCII string representation of octet string.
	 */
	public synchronized String getASCIIString(char pReplacementChar) {
		// If ASCII string constructor used, return original string
		if (this.iASCIIString != null && this.iReplacementChar == 0xFF) return this.iASCIIString;

		// Verify replacement character is printable
		if (pReplacementChar <= 0x1F || pReplacementChar >= 0x7F) throw new IllegalArgumentException(
				"Replacement character not printable");

		// If we already did this once, return previous string
		if (this.iASCIIString != null && this.iReplacementChar == pReplacementChar) return this.iASCIIString;

		// Construct new ASCII string
		StringBuilder str = new StringBuilder("");
		if (this.iBytes != null) {
			for (int i = 4; i < this.iBytes.length; i++) {
				char ch = (char) this.iBytes[i].byteValue();
				if (ch <= 0x1F || ch >= 0x7F) str.append(pReplacementChar);
				else str.append(ch);
			}
		} else /* (this.iHexString != null) */{
			for (int i = 10; i < this.iHexString.length(); i += 2) {
				char ch = (char) Integer.parseInt(this.iHexString.substring(i, i + 2), 16);
				if (ch <= 0x1F || ch >= 0x7F) str.append(pReplacementChar);
				else str.append(ch);
			}
		}

		// Save ASCII string in new object and indicate which replacement
		// character used
		this.iASCIIString = new String(str);
		this.iReplacementChar = pReplacementChar;

		return this.iASCIIString;
	}

	/**
	 * Returns byte array representation of octet string. If the byte array has
	 * not yet been created, it is created from the hexadecimal string or ASCII
	 * string.
	 * 
	 * @return Byte array representation of octet string.
	 */
	public synchronized UnsignedInteger8[] getBytes() {
		if (this.iBytes != null) return this.iBytes;

		if (this.iHexString != null) {
			convertHexStringToBytes();
		} else /* if (this.iASCIIString != null) */{
			convertASCIIStringToBytes();
		}

		return this.iBytes;
	}

	/**
	 * Returns hexadecimal string representation of octet string. If the
	 * hexadecimal string has not yet been created, it is created from the byte
	 * array or ASCII string.
	 * 
	 * @return Hexadecimal string representation of octet string.
	 */
	public synchronized String getHexString() {
		if (this.iHexString != null) return this.iHexString;

		if (this.iBytes != null) {
			convertBytesToHexString();
		} else /* if (this.iASCIIString != null) */{
			convertASCIIStringToHexString();
		}

		return this.iHexString;
	}

	/**
	 * Returns hash code value for octet string.
	 * 
	 * @return Hash code value for octet string.
	 */
	@Override
	public int hashCode() {
		return toString().toLowerCase().hashCode();
	}

	/**
	 * Returns length of octet string, where length is number of octets plus
	 * four.
	 * 
	 * @return Length of octet string.
	 */
	public int length() {
		return this.iLength;
	}

	/**
	 * Returns string representation of octet string.
	 * 
	 * @return String representation of octet string.
	 */
	@Override
	public String toString() {
		return getHexString();
	}

	private void convertBytesToHexString() {
		// Start with "0x"
		StringBuilder str = new StringBuilder("0x");

		// Append length
		String len = Integer.toHexString(this.iLength);
		for (int i = 8 - len.length(); i > 0; i--)
			str.append('0');
		str.append(len);

		// Append string
		for (int i = 4; i < this.iLength; i++) {
			String octet = Integer.toHexString(this.iBytes[i].intValue());
			if (octet.length() == 1) str.append('0');
			str.append(octet);
		}

		// Save hexadecimal string in new object
		this.iHexString = new String(str);

		// debug("convertBytesToHexString: from {" + toBytesString() + "} to \""
		// + this.iHexString + "\"");
	}

	private void convertHexStringToBytes() {
		// Save byte array in new object
		this.iBytes = new UnsignedInteger8[this.iLength];

		// Convert each octet in hexadecimal string to byte
		for (int idxByte = 0, idxStr = 2, len = this.iHexString.length(); idxStr < len; idxByte++, idxStr += 2) {
			short s;
			try {
				s = Short.parseShort(this.iHexString.substring(idxStr, idxStr + 2), 16);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Hex string length could not be parsed: "
						+ e.toString());
			}
			this.iBytes[idxByte] = new UnsignedInteger8(s);
		}

		// debug("convertHexStringToBytes: from \"" + this.iHexString +
		// "\" to {" + toBytesString() + "}");
	}

	private void convertASCIIStringToBytes() {
		// Save byte array in new object
		this.iBytes = new UnsignedInteger8[this.iLength];

		// Convert length
		this.iBytes[0] = new UnsignedInteger8((short) ((this.iLength >> 24) & 0xFF));
		this.iBytes[1] = new UnsignedInteger8((short) ((this.iLength >> 16) & 0xFF));
		this.iBytes[2] = new UnsignedInteger8((short) ((this.iLength >> 8) & 0xFF));
		this.iBytes[3] = new UnsignedInteger8((short) (this.iLength & 0xFF));

		// Convert each character in ASCII string to byte
		for (int idxStr = 0, idxByte = 4; idxStr < this.iASCIIString.length(); idxStr++, idxByte++)
			this.iBytes[idxByte] = new UnsignedInteger8((short) (this.iASCIIString.charAt(idxStr)));

		// debug("convertASCIIStringToBytes: from \"" + this.iASCIIString +
		// "\" to {" + toBytesString() + "}");
	}

	private void convertASCIIStringToHexString() {
		// Start with "0x"
		StringBuilder str = new StringBuilder("0x");

		// Append length
		String len = Integer.toHexString(this.iLength);
		for (int i = 8 - len.length(); i > 0; i--)
			str.append('0');
		str.append(len);

		// Append string
		for (int idxAsc = 0, idxHex = 10; idxAsc < this.iASCIIString.length(); idxAsc++, idxHex++) {
			String octet = Integer.toHexString((this.iASCIIString.charAt(idxAsc)));
			if (octet.length() == 1) str.append('0');
			str.append(octet);
		}

		// Save hexadecimal string in new object
		this.iHexString = new String(str);

		// debug("convertASCIIStringToHexString: from \"" + this.iASCIIString +
		// "\" to \"" + this.iHexString + "\"");
	}

	// private String toBytesString() {
	// StringBuilder str = new StringBuilder();
	//
	// for (int i = 0; i < this.iLength; i++) {
	// String octet = Integer.toHexString((this.iBytes[i].intValue()));
	// if (i > 0) str.append(' ');
	// if (octet.length() == 1) str.append('0');
	// str.append(octet);
	// }
	// return new String(str);
	// }

	// private void debug(String str) {
	// System.out.println(str);
	// }
}
