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
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1669225    2007-04-16  lupusalex    Ctor CIMDataType(int) shall be private
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2870455    2009-09-30  blaschke-oss Missing CLASS_ARRAY_T in CIMDataType
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 3004779    2010-06-16  blaschke-oss TCK: CIMDataType not throwing IllegalArgumentException
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 * 3513349    2012-03-31  blaschke-oss TCK: CIMDataType must not accept null string
 * 3517503    2012-04-13  blaschke-oss Missing parm in CIMDataType ctor javadoc
 * 3521131    2012-04-24  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final II
 *    2632    2013-05-02  blaschke-oss Potential Null Point Exception in CIMDataType
 */

package javax.cim;

import java.io.Serializable;

import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_02) on Sun Apr 22 15:31:53 EDT 2012
/**
 * This class represents a CIM data type as defined by the Distributed
 * Management Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM
 * Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). The specification only allows a set number of data types. This
 * class defines the mapping of CIM data types to Java objects. <br>
 * <br>
 * Note: CIM allows the fixed size of an array as part of the data type. The
 * predefined classes are unbounded. <br>
 * <br>
 * The following table shows the mapping of CIM data type to Java.<br>
 * <table align=center width=70% border=1>
 * <tr>
 * <th>CIM Data Type</th>
 * <th>Java Representation</th>
 * </tr>
 * <tr>
 * <td>uint8</td>
 * <td>UnsignedInteger8</td>
 * </tr>
 * <tr>
 * <td>sint8</td>
 * <td>Byte</td>
 * </tr>
 * <tr>
 * <td>uint16</td>
 * <td>UnsignedInteger16</td>
 * </tr>
 * <tr>
 * <td>sint16</td>
 * <td>Short</td>
 * </tr>
 * <tr>
 * <td>uint32</td>
 * <td>UnsignedInteger32</td>
 * </tr>
 * <tr>
 * <td>sint32</td>
 * <td>Integer</td>
 * </tr>
 * <tr>
 * <td>uint64</td>
 * <td>UnsignedInteger64</td>
 * </tr>
 * <tr>
 * <td>sint64</td>
 * <td>Long</td>
 * </tr>
 * <tr>
 * <td>string</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>boolean</td>
 * <td>Boolean</td>
 * </tr>
 * <tr>
 * <td>real32</td>
 * <td>Float</td>
 * </tr>
 * <tr>
 * <td>real64</td>
 * <td>Double</td>
 * </tr>
 * <tr>
 * <td>datetime</td>
 * <td>CIMDataTimeAbsolute<br>
 * CIMDataTimeInterval</td>
 * </tr>
 * <tr>
 * <td>&lt;classname&gt; ref</td>
 * <td>CIMObjectPath</td>
 * </tr>
 * <tr>
 * <td>char16</td>
 * <td>Character</td>
 * </tr>
 * </table>
 */
public class CIMDataType extends Object implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Boolean
	 * 
	 * @see Boolean
	 */
	public static final int BOOLEAN = 9;

	/**
	 * Boolean unbounded array data type
	 * 
	 * @see Boolean
	 */
	public static final CIMDataType BOOLEAN_ARRAY_T = new CIMDataType(BOOLEAN, true);

	/**
	 * Boolean data type
	 * 
	 * @see Boolean
	 */
	public static final CIMDataType BOOLEAN_T = new CIMDataType(BOOLEAN, false);

	/**
	 * 16-bit UCS-2 character
	 * 
	 * @see Character
	 */
	public static final int CHAR16 = 13;

	/**
	 * 16-bit UCS-2 character unbounded Array type
	 * 
	 * @see Character
	 */
	public static final CIMDataType CHAR16_ARRAY_T = new CIMDataType(CHAR16, true);

	/**
	 * 16-bit UCS-2 character data type
	 * 
	 * @see Character
	 */
	public static final CIMDataType CHAR16_T = new CIMDataType(CHAR16, false);

	/**
	 * CIMClass type
	 */
	public static final int CLASS = 17;

	/**
	 * CIMClass unbounded Array type
	 * 
	 * @see CIMClass
	 */
	public static final CIMDataType CLASS_ARRAY_T = new CIMDataType(CLASS, true);

	/**
	 * CIMClass type
	 * 
	 * @see CIMClass
	 */
	public static final CIMDataType CLASS_T = new CIMDataType(CLASS, false);

	/**
	 * A string containing the date-time
	 * 
	 * @see CIMDateTimeInterval CIMDateTimeAbsolute
	 */
	public static final int DATETIME = 12;

	/**
	 * A date-time unbounded array data type
	 * 
	 * @see CIMDateTimeInterval CIMDateTimeAbsolute
	 */
	public static final CIMDataType DATETIME_ARRAY_T = new CIMDataType(DATETIME, true);

	/**
	 * A date-time data type
	 * 
	 * @see CIMDateTimeInterval CIMDateTimeAbsolute
	 */
	public static final CIMDataType DATETIME_T = new CIMDataType(DATETIME, false);

	/**
	 * Invalid type
	 */
	public static final int INVALID = -1;

	/**
	 * Invalid data type
	 */
	public static final CIMDataType INVALID_T = new CIMDataType(INVALID, false);

	/**
	 * Null type
	 */
	public static final int NULL = 16;

	/**
	 * CIMInstance type
	 * 
	 * @see CIMInstance
	 */
	public static final int OBJECT = 15;

	/**
	 * CIMInstance unbounded Array type
	 * 
	 * @see CIMInstance
	 */
	public static final CIMDataType OBJECT_ARRAY_T = new CIMDataType(OBJECT, true);

	/**
	 * CIMInstance type data type (Note: For CIMV2, this can only be used when
	 * the property has either an EmbeddedInstance or EmbeddedObject qualifier)
	 * 
	 * @see CIMInstance
	 */
	public static final CIMDataType OBJECT_T = new CIMDataType(OBJECT, false);

	/**
	 * IEEE 4-byte floating-point
	 * 
	 * @see Float
	 */
	public static final int REAL32 = 10;

	/**
	 * IEEE 4-byte floating-point unbounded array data type
	 * 
	 * @see Float
	 */
	public static final CIMDataType REAL32_ARRAY_T = new CIMDataType(REAL32, true);

	/**
	 * IEEE 4-byte floating-point data type
	 * 
	 * @see Float
	 */
	public static final CIMDataType REAL32_T = new CIMDataType(REAL32, false);

	/**
	 * IEEE 8-byte floating-point
	 * 
	 * @see Double
	 */
	public static final int REAL64 = 11;

	/**
	 * IEEE 8-byte floating-point unbounded array data type
	 * 
	 * @see Double
	 */
	public static final CIMDataType REAL64_ARRAY_T = new CIMDataType(REAL64, true);

	/**
	 * IEEE 8-byte floating-point data type
	 * 
	 * @see Double
	 */
	public static final CIMDataType REAL64_T = new CIMDataType(REAL64, false);

	/**
	 * Reference type
	 * 
	 * @see CIMObjectPath
	 */
	public static final int REFERENCE = 14;

	/**
	 * Signed 16-bit integer
	 * 
	 * @see Short
	 */
	public static final int SINT16 = 3;

	/**
	 * Signed 16-bit integer unbounded array data type
	 * 
	 * @see Short
	 */
	public static final CIMDataType SINT16_ARRAY_T = new CIMDataType(SINT16, true);

	/**
	 * Signed 16-bit integer data type
	 * 
	 * @see Short
	 */
	public static final CIMDataType SINT16_T = new CIMDataType(SINT16, false);

	/**
	 * Signed 32-bit integer
	 * 
	 * @see Integer
	 */
	public static final int SINT32 = 5;

	/**
	 * Signed 32-bit integer unbounded array data type
	 * 
	 * @see Integer
	 */
	public static final CIMDataType SINT32_ARRAY_T = new CIMDataType(SINT32, true);

	/**
	 * Signed 32-bit integer data type
	 * 
	 * @see Integer
	 */
	public static final CIMDataType SINT32_T = new CIMDataType(SINT32, false);

	/**
	 * Signed 64-bit integer
	 */
	public static final int SINT64 = 7;

	/**
	 * Signed 64-bit integer unbounded array data type
	 * 
	 * @see Long
	 */
	public static final CIMDataType SINT64_ARRAY_T = new CIMDataType(SINT64, true);

	/**
	 * Signed 64-bit integer data type
	 * 
	 * @see Long
	 */
	public static final CIMDataType SINT64_T = new CIMDataType(SINT64, false);

	/**
	 * Signed 8-bit integer
	 * 
	 * @see Byte
	 */
	public static final int SINT8 = 1;

	/**
	 * Signed 8-bit integer unbounded array data type
	 * 
	 * @see Byte
	 */
	public static final CIMDataType SINT8_ARRAY_T = new CIMDataType(SINT8, true);

	/**
	 * Signed 8-bit integer data type
	 * 
	 * @see Byte
	 */
	public static final CIMDataType SINT8_T = new CIMDataType(SINT8, false);

	/**
	 * UCS-2 string
	 * 
	 * @see String
	 */
	public static final int STRING = 8;

	/**
	 * UCS-2 string unbounded array data type
	 * 
	 * @see String
	 */
	public static final CIMDataType STRING_ARRAY_T = new CIMDataType(STRING, true);

	/**
	 * UCS-2 string data type
	 * 
	 * @see String
	 */
	public static final CIMDataType STRING_T = new CIMDataType(STRING, false);

	/**
	 * Unsigned 16-bit integer
	 * 
	 * @see UnsignedInteger16
	 */
	public static final int UINT16 = 2;

	/**
	 * Unsigned 16-bit integer unbounded array data type
	 * 
	 * @see UnsignedInteger16
	 */
	public static final CIMDataType UINT16_ARRAY_T = new CIMDataType(UINT16, true);

	/**
	 * Unsigned 16-bit integer data type
	 * 
	 * @see UnsignedInteger16
	 */
	public static final CIMDataType UINT16_T = new CIMDataType(UINT16, false);

	/**
	 * Unsigned 32-bit integer
	 * 
	 * @see UnsignedInteger32
	 */
	public static final int UINT32 = 4;

	/**
	 * Unsigned 32-bit integer unbounded array data type
	 * 
	 * @see UnsignedInteger32
	 */
	public static final CIMDataType UINT32_ARRAY_T = new CIMDataType(UINT32, true);

	/**
	 * Unsigned 32-bit integer data type
	 * 
	 * @see UnsignedInteger32
	 */
	public static final CIMDataType UINT32_T = new CIMDataType(UINT32, false);

	/**
	 * Unsigned 64-bit integer
	 * 
	 * @see UnsignedInteger64
	 */
	public static final int UINT64 = 6;

	/**
	 * Unsigned 64-bit integer unbounded array data type
	 * 
	 * @see UnsignedInteger64
	 */
	public static final CIMDataType UINT64_ARRAY_T = new CIMDataType(UINT64, true);

	/**
	 * Unsigned 64-bit integer data type
	 * 
	 * @see UnsignedInteger64
	 */
	public static final CIMDataType UINT64_T = new CIMDataType(UINT64, false);

	/**
	 * Unsigned 8-bit integer
	 * 
	 * @see UnsignedInteger8
	 */
	public static final int UINT8 = 0;

	/**
	 * Unsigned 8-bit integer unbounded array data type
	 * 
	 * @see UnsignedInteger8
	 */
	public static final CIMDataType UINT8_ARRAY_T = new CIMDataType(UINT8, true);

	/**
	 * Unsigned 8-bit integer data type
	 * 
	 * @see UnsignedInteger8
	 */
	public static final CIMDataType UINT8_T = new CIMDataType(UINT8, false);

	private int iTypeCode;

	/**
	 * non array if <0<br>
	 * unbounded if =0<br>
	 * bounded if >0
	 */
	private int iBound;

	private String iRefClassName;

	/**
	 * Sets the data type (non-array).
	 * 
	 * @param pType
	 *            The data type of the class.
	 */
	private void setType(int pType) {
		this.iTypeCode = pType;
		this.iBound = -1;
	}

	/**
	 * Sets the data type (array).
	 * 
	 * @param pType
	 *            The data type of the class.
	 * @param Array
	 *            Array bounds or unbounded if zero.
	 * 
	 */
	private void setType(int pType, int pBound) {
		this.iTypeCode = pType;
		this.iBound = pBound;
	}

	/**
	 * Constructs a <code>CIMDataType</code>.
	 * 
	 * @param pType
	 *            The data type as defined in the CIM class.
	 * @param pIsArray
	 *            <code>true</code> if data type is unbounded array,
	 *            <code>false</code> if data type is scalar.
	 * @throws IllegalArgumentException
	 */
	private CIMDataType(int pType, boolean pIsArray) throws IllegalArgumentException {
		if (pIsArray) {
			setType(pType, 0);
		} else {
			setType(pType);
		}
	}

	/**
	 * Constructs a <code>CIMDataType</code> array object of the specified type
	 * and size. This should only be used when the size is being limited/defined
	 * as part of the data type.
	 * 
	 * @param pType
	 *            The data type as defined in the CIM class.
	 * @param pSize
	 *            The maximum number of elements in the array.
	 * @throws IllegalArgumentException
	 *             If the <code>pSize</code> value specified is not a positive
	 *             integer.
	 */
	public CIMDataType(int pType, int pSize) throws IllegalArgumentException {
		if (pSize <= 0) throw new IllegalArgumentException(
				"Maximum number of elements must be positive integer!");
		setType(pType, pSize);
	}

	/**
	 * Creates a new CIM REFERENCE data type object with the specified class
	 * reference.
	 * 
	 * @param pClassName
	 *            The CIM class reference name.
	 * @throws IllegalArgumentException
	 *             If the <code>pClassName</code> is <code>null</code>.
	 */
	public CIMDataType(String pClassName) {
		if (pClassName == null) throw new IllegalArgumentException("Class name must not be null!");
		setType(REFERENCE);
		this.iRefClassName = pClassName;
	}

	/**
	 * Creates a new CIM REFERENCE array data type object with the specified
	 * class reference.
	 * 
	 * @param pClassName
	 *            The CIM class reference name.
	 * @param pSize
	 *            The size of the array. 0 indicates the array is unbounded.
	 * @throws IllegalArgumentException
	 *             If the <code>pClassName</code> is <code>null</code>.
	 */
	public CIMDataType(String pClassName, int pSize) {
		if (pClassName == null) throw new IllegalArgumentException("Class name must not be null!");
		if (pSize < 0) throw new IllegalArgumentException(
				"Maximum number of elements cannot be negative integer!");
		setType(REFERENCE, pSize);
		this.iRefClassName = pClassName;
	}

	/**
	 * Checks that the specified <code>CIMDataType</code> is equal to this
	 * <code>CIMDataType</code>.
	 * 
	 * @param pObj
	 *            The object to compare.
	 * @return <code>true</code> if the specified object is equal to this
	 *         <code>CIMDataType</code>; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (this == pObj) return true;
		if (!(pObj instanceof CIMDataType)) return false;
		CIMDataType that = (CIMDataType) pObj;
		if (this.iRefClassName == null && that.iRefClassName != null) return false;
		boolean refMatch = (this.iRefClassName == null ? true : this.iRefClassName
				.equals(that.iRefClassName));
		return (this.iTypeCode == that.iTypeCode && isArray() == that.isArray() && refMatch);
	}

	/**
	 * Get the data type of an object.
	 * 
	 * @param pObj
	 *            The object whose data type is to be returned.
	 * @return The data type of the specified object.
	 * @throws IllegalArgumentException
	 *             If <code>pObj</code> is not a valid CIM Type.
	 */
	public static final CIMDataType getDataType(Object pObj) throws IllegalArgumentException {
		if (pObj instanceof Byte) { return SINT8_T; }
		if (pObj instanceof Byte[]) { return SINT8_ARRAY_T; }
		if (pObj instanceof Short) { return SINT16_T; }
		if (pObj instanceof Short[]) { return SINT16_ARRAY_T; }
		if (pObj instanceof Integer) { return SINT32_T; }
		if (pObj instanceof Integer[]) { return SINT32_ARRAY_T; }
		if (pObj instanceof Long) { return SINT64_T; }
		if (pObj instanceof Long[]) { return SINT64_ARRAY_T; }
		if (pObj instanceof UnsignedInteger8) { return UINT8_T; }
		if (pObj instanceof UnsignedInteger8[]) { return UINT8_ARRAY_T; }
		if (pObj instanceof UnsignedInteger16) { return UINT16_T; }
		if (pObj instanceof UnsignedInteger16[]) { return UINT16_ARRAY_T; }
		if (pObj instanceof UnsignedInteger32) { return UINT32_T; }
		if (pObj instanceof UnsignedInteger32[]) { return UINT32_ARRAY_T; }
		if (pObj instanceof UnsignedInteger64) { return UINT64_T; }
		if (pObj instanceof UnsignedInteger64[]) { return UINT64_ARRAY_T; }
		if (pObj instanceof Boolean) { return BOOLEAN_T; }
		if (pObj instanceof Boolean[]) { return BOOLEAN_ARRAY_T; }
		if (pObj instanceof Character) { return CHAR16_T; }
		if (pObj instanceof Character[]) { return CHAR16_ARRAY_T; }
		if (pObj instanceof String) { return STRING_T; }
		if (pObj instanceof String[]) { return STRING_ARRAY_T; }
		if (pObj instanceof Float) { return REAL32_T; }
		if (pObj instanceof Float[]) { return REAL32_ARRAY_T; }
		if (pObj instanceof Double) { return REAL64_T; }
		if (pObj instanceof Double[]) { return REAL64_ARRAY_T; }
		if (pObj instanceof CIMDateTime) { return DATETIME_T; }
		if (pObj instanceof CIMDateTime[]) { return DATETIME_ARRAY_T; }
		if (pObj instanceof CIMClass) { return CLASS_T; }
		if (pObj instanceof CIMClass[]) { return CLASS_ARRAY_T; }
		if (pObj instanceof CIMInstance) { return OBJECT_T; }
		if (pObj instanceof CIMInstance[]) { return OBJECT_ARRAY_T; }
		if (pObj instanceof CIMObjectPath) {
			CIMObjectPath op = (CIMObjectPath) pObj;
			return new CIMDataType(op.getObjectName());
		}
		if (pObj instanceof CIMObjectPath[]) {
			CIMObjectPath[] ops = (CIMObjectPath[]) pObj;
			for (int i = 0; i < ops.length; i++)
				if (ops[i] != null) return new CIMDataType(ops[i].getObjectName(), 0);
		}
		// TODO: tracing
		/*
		 * String msg= "Cannot determine the CIMDataType of class:"+
		 * pObj.getClass().getName()+"!";
		 */
		throw new IllegalArgumentException("Invalid CIM Type!");
	}

	/**
	 * Returns the class name of the CIM REFERENCE data type.
	 * 
	 * @return The CIM REFERENCE class name.
	 */
	public String getRefClassName() {
		return this.iRefClassName;
	}

	/**
	 * Returns the size of the maximum number of elements an array data type may
	 * hold.
	 * 
	 * @return The maximum size of the array data type.
	 */
	public int getSize() {
		return this.iBound;
	}

	/**
	 * Returns the data type.
	 * 
	 * @return The data type.
	 */
	public int getType() {
		return this.iTypeCode;
	}

	/**
	 * Checks if the data type is an array type.
	 * 
	 * @return <code>true</code> if the data type is an array type,
	 *         <code>false</code> otherwise.
	 */
	public boolean isArray() {
		return this.iBound >= 0;
	}

	/**
	 * Returns a <code>String</code> representation of the
	 * <code>CIMDataType</code>. This method is intended to be used only for
	 * debugging purposes, and the format of the returned string may vary
	 * between implementations. The returned string may be empty but may not be
	 * <code>null</code>.
	 * 
	 * @return A string representation of this data type.
	 */
	@Override
	public String toString() {
		return MOF.dataType(this);
	}
}
