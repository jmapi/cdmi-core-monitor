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
 * 1737123    2007-06-15  ebak         Differences to JSR48 public review draft
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2973233    2010-03-19  blaschke-oss TCK: UnsignedIntegerNN.hashCode() not working
 *    2719    2013-12-10  blaschke-oss TCK: CIM APIs should not generate NullPointerException
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * This class represents an <code>UnsignedInteger32</code>. A
 * <code>uint32</code> data type is defined by the (<a
 * href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>).
 */
public class UnsignedInteger32 extends Number implements Comparable<UnsignedInteger32> {

	private static final long serialVersionUID = -8861436527534071393L;

	/**
	 * The maximum value for an <code>UnsignedInteger32</code>.
	 */
	public static final long MAX_VALUE = 4294967295l;

	/**
	 * The minimum value for an <code>UnsignedInteger32</code>.
	 */
	public static final long MIN_VALUE = 0l;

	private long iValue;

	/**
	 * Sets the value of this integer object if it falls within the range of
	 * minimum and maximum values.
	 * 
	 * @param pValue
	 *            The integer.
	 * @throws NumberFormatException
	 *             If the integer is out of range.
	 */
	private void setValue(long pValue) throws NumberFormatException {
		if (pValue > MAX_VALUE || pValue < MIN_VALUE) {
			String msg = "uint32:" + pValue + " is out of range!";
			throw new NumberFormatException(msg);
		}
		this.iValue = pValue;
	}

	/**
	 * Constructs an unsigned 32-bit integer object for the specified long
	 * value. Only the lower 32 bits are considered.
	 * 
	 * @param pValue
	 *            The long to be represented as an unsigned 32-bit integer.
	 * @throws NumberFormatException
	 *             If the number is out of range.
	 */
	public UnsignedInteger32(long pValue) throws NumberFormatException {
		setValue(pValue);
	}

	/**
	 * Constructs an unsigned 32-bit integer object for the specified string.
	 * Only the lower 32 bits are considered.
	 * 
	 * @param pValue
	 *            The string to be represented as an unsigned 32-bit integer.
	 * @throws NumberFormatException
	 *             If the number is out of range.
	 * @throws IllegalArgumentException
	 *             If value is <code>null</code>.
	 */
	public UnsignedInteger32(String pValue) throws NumberFormatException {
		if (pValue == null) throw new IllegalArgumentException("String value cannot be null!");
		setValue(Long.parseLong(pValue));
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
	public int compareTo(UnsignedInteger32 pOther) {
		if (pOther == null) throw new IllegalArgumentException(
				"Other UnsignedInteger32 cannot be null!");
		UnsignedInteger32 that = pOther;
		long d = this.iValue - that.iValue;
		if (d == 0) return 0;
		return d < 0 ? -1 : 1;
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not null and is an
	 * UnsignedInteger32 object that represents the same value as this object.
	 * 
	 * @param pObj
	 *            The object to compare.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof UnsignedInteger32)) return false;
		return this.iValue == ((UnsignedInteger32) pObj).iValue;
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>byte</code>.
	 * 
	 * @return The <code>byte</code> value of this unsigned integer object.
	 */
	@Override
	public byte byteValue() {
		return (byte) this.iValue;
	}

	/**
	 * Returns the value of this unsigned integer object as a
	 * <code>double</code>.
	 * 
	 * @return Value of this unsigned integer object as a <code>double</code>.
	 */
	@Override
	public double doubleValue() {
		return this.iValue;
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>float</code>
	 * .
	 * 
	 * @return Value of this unsigned integer object as a <code>float</code>.
	 */
	@Override
	public float floatValue() {
		return this.iValue;
	}

	/**
	 * Computes the hash code for this unsigned integer object.
	 * 
	 * @return The integer representing the hash code for this unsigned integer
	 *         object.
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(this.iValue).hashCode();
	}

	/**
	 * Returns the value of this unsigned integer object as an <code>int</code>.
	 * 
	 * @return Value of this unsigned integer object as an <code>int</code>.
	 */
	@Override
	public int intValue() {
		return (int) this.iValue;
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>long</code>.
	 * 
	 * @return Value of this unsigned integer object as a <code>long</code>.
	 */
	@Override
	public long longValue() {
		return this.iValue;
	}

	/**
	 * Returns the value of this unsigned integer object as a <code>short</code>
	 * .
	 * 
	 * @return Value of this unsigned integer object as a <code>short</code>.
	 */
	@Override
	public short shortValue() {
		return (short) this.iValue;
	}

	/**
	 * Returns the text representation of this unsigned integer object.
	 * 
	 * @return Text representation of this unsigned integer.
	 */
	@Override
	public String toString() {
		return Long.toString(this.iValue);
	}

}
