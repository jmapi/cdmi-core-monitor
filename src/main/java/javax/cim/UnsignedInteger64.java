/**
 * (C) Copyright IBM Corp. 2006, 2013
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
 * 1565892    2006-10-09  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 2935258    2010-01-19  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2973230    2010-03-19  blaschke-oss TCK: UnsignedInteger64.equals() does not handle null
 *    2719    2013-12-10  blaschke-oss TCK: CIM APIs should not generate NullPointerException
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

import java.math.BigInteger;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * This class represents an <code>UnsignedInteger64</code>. A
 * <code>uint64</code> data type is defined by the (<a
 * href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>).
 */
public class UnsignedInteger64 extends Number implements Comparable<UnsignedInteger64> {

	private static final long serialVersionUID = -3734165689168941119L;

	/**
	 * The maximum value for an <code>UnsignedInteger64</code>.
	 */
	public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");

	/**
	 * The minimum value for an <code>UnsignedInteger64</code>.
	 */
	public static final BigInteger MIN_VALUE = BigInteger.ZERO;

	private BigInteger iValue;

	/**
	 * Sets the value of this integer object if it falls within the range of
	 * minimum and maximum values.
	 * 
	 * @param pValue
	 *            The integer.
	 * @throws NumberFormatException
	 *             If the integer is out of range.
	 */
	private void setValue(BigInteger pValue) throws NumberFormatException {
		if (pValue.compareTo(MAX_VALUE) > 0 || pValue.compareTo(MIN_VALUE) < 0) {
			String msg = "uint64:" + pValue + " is out of range!";
			throw new NumberFormatException(msg);
		}
		this.iValue = pValue;
	}

	/**
	 * Constructs an unsigned 64-bit integer object for the specified
	 * <code>BigInteger</code>. Only the lower 64 bits are considered.
	 * 
	 * @param pValue
	 *            The <code>BigInteger</code> to be represented as an unsigned
	 *            64-bit integer.
	 * @throws NumberFormatException
	 *             If the number is out of range.
	 * @throws IllegalArgumentException
	 *             If value is <code>null</code>.
	 */
	public UnsignedInteger64(BigInteger pValue) throws NumberFormatException {
		if (pValue == null) throw new IllegalArgumentException("BigInteger value cannot be null!");
		setValue(pValue);
	}

	/**
	 * Constructs an unsigned 64-bit integer object for the specified array of
	 * bytes. Only the lower 64 bits are considered.
	 * 
	 * @param pValue
	 *            The byte array to be represented as an unsigned 64-bit
	 *            integer.
	 * @throws NumberFormatException
	 *             If the number is out of range.
	 * @throws IllegalArgumentException
	 *             If value is <code>null</code>.
	 */
	public UnsignedInteger64(byte[] pValue) throws NumberFormatException {
		if (pValue == null) throw new IllegalArgumentException("byte[] value cannot be null!");
		setValue(new BigInteger(pValue));
	}

	/**
	 * Constructs an unsigned 64-bit integer object from the specified string.
	 * Only the lower 64 bits are considered.
	 * 
	 * @param pValue
	 *            The string to be represented as an unsigned 64-bit integer.
	 * @throws NumberFormatException
	 *             If the number is out of range.
	 * @throws IllegalArgumentException
	 *             If value is <code>null</code>.
	 */
	public UnsignedInteger64(String pValue) throws NumberFormatException {
		if (pValue == null) throw new IllegalArgumentException("String value cannot be null!");
		setValue(new BigInteger(pValue));
	}

	/**
	 * Get the value as a <code>BigInteger</code>.
	 * 
	 * @return <code>BigInteger</code> representation of this object.
	 */
	public BigInteger bigIntegerValue() {
		return this.iValue;
	}

	/**
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 * 
	 * @param pOther
	 *            The Object to be compared.
	 * @return A negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 * @throws ClassCastException
	 *             If the specified object's type prevents it from being
	 *             compared to this Object.
	 * @throws IllegalArgumentException
	 *             If value is <code>null</code>.
	 */
	public int compareTo(UnsignedInteger64 pOther) {
		if (pOther == null) throw new IllegalArgumentException(
				"Other UnsignedInteger64 cannot be null!");
		UnsignedInteger64 that = pOther;
		int d = this.iValue.compareTo(that.iValue);
		if (d == 0) return 0;
		return d < 0 ? -1 : 1;
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is an <code>UnsignedInteger64</code> object that represents the same
	 * value as this object.
	 * 
	 * @param pOther
	 *            The object to compare.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pOther) {
		if (!(pOther instanceof UnsignedInteger64)) return false;
		return this.iValue.equals(((UnsignedInteger64) pOther).iValue);
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>byte</code>.
	 * 
	 * @return The <code>byte</code> value of this unsigned integer object.
	 */
	@Override
	public byte byteValue() {
		return this.iValue.byteValue();
	}

	/**
	 * Returns the value of this unsigned integer object as a
	 * <code>double</code>.
	 * 
	 * @return Value of this unsigned integer object as a <code>double</code>.
	 */
	@Override
	public double doubleValue() {
		return this.iValue.doubleValue();
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>float</code>
	 * .
	 * 
	 * @return Value of this unsigned integer object as a <code>float</code>.
	 */
	@Override
	public float floatValue() {
		return this.iValue.floatValue();
	}

	/**
	 * Computes the hash code for this unsigned integer object.
	 * 
	 * @return The integer representing the hash code for this unsigned integer
	 *         object.
	 */
	@Override
	public int hashCode() {
		return this.iValue.hashCode();
	}

	/**
	 * Returns the value of this unsigned integer object as an <code>int</code>.
	 * 
	 * @return Value of this unsigned integer object as an <code>int</code>.
	 */
	@Override
	public int intValue() {
		return this.iValue.intValue();
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>long</code>.
	 * 
	 * @return Value of this unsigned integer object as a <code>long</code>.
	 */
	@Override
	public long longValue() {
		return this.iValue.longValue();
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>short</code>
	 * .
	 * 
	 * @return Value of this unsigned integer object as a <code>short</code>.
	 */
	@Override
	public short shortValue() {
		return this.iValue.shortValue();
	}

	/**
	 * Returns the text representation of this unsigned integer object.
	 * 
	 * @return Text representation of this unsigned integer.
	 */
	@Override
	public String toString() {
		return this.iValue.toString();
	}

}
