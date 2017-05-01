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
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3411879    2011-09-20  blaschke-oss TCK: CIM element value must match type
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

import java.util.Arrays;

import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * <code>CIMValuedElement</code> is a base class used by any element that
 * contains a name, type and value.
 * 
 * @param <E>
 *            Type parameter.
 */
public abstract class CIMValuedElement<E> extends CIMTypedElement {

	private static final long serialVersionUID = 4234L;

	private E iValue;

	/**
	 * Creates a new CIM element with the given name, type and value.
	 * 
	 * @param pName
	 *            The string for the name for this element.
	 * @param pType
	 *            The data type for this element.
	 * @param pValue
	 *            The value for this element. <code>null</code> is a valid
	 *            value.
	 * @throws IllegalArgumentException
	 *             If the value does not match the data type.
	 */
	protected CIMValuedElement(String pName, CIMDataType pType, E pValue) {
		super(pName, pType);

		if (pType != null && pValue != null) {
			CIMDataType valueDataType;
			try {
				valueDataType = CIMDataType.getDataType(pValue);
			} catch (IllegalArgumentException e) {
				// Value has unknown data type, cannot validate
				valueDataType = null;
			}
			if (valueDataType != null && valueDataType.getType() != pType.getType()) throw new IllegalArgumentException(
					"CIM value does not match type: " + valueDataType.getType() + " != "
							+ pType.getType());
		}

		this.iValue = pValue;
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is a <code>CIMValuedElement</code> that represents the same name,
	 * type and value as this object.
	 * 
	 * @param pObj
	 *            The object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMValuedElement)) return false;
		CIMValuedElement<?> that = (CIMValuedElement<?>) pObj;
		if (!super.equals(that)) return false;
		if (getDataType().isArray()) { return Arrays.equals((Object[]) this.iValue,
				(Object[]) that.iValue); }
		return this.iValue == null ? that.iValue == null : this.iValue.equals(that.iValue);
	}

	/**
	 * Returns the value for this CIM Element.
	 * 
	 * @return The value of the CIM Element. <code>null</code> is a valid value.
	 */
	public E getValue() {
		return this.iValue;
	}

	/**
	 * Returns a hash code value for the CIM valued element. This method is
	 * supported for the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return A hash code value for this CIM valued element.
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Returns a <code>String</code> representation of the CIM Element. This
	 * method is intended to be used only for debugging purposes, and the format
	 * of the returned string may vary between implementations. The returned
	 * string may be empty but may not be <code>null</code>.
	 * 
	 * @return String representation of this element.
	 */
	@Override
	public String toString() {
		return MOF.valuedElement(this, MOF.EMPTY);
	}

}
