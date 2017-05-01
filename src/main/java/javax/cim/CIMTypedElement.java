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
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2227442 	  2008-11-05  blaschke-oss Add missing serialVersionUID
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 */

package javax.cim;

import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * <code>CIMTypedElement</code> is an abstract class that represents a CIM
 * element that contains just the data type, but no value.
 */
public abstract class CIMTypedElement extends CIMElement {

	private static final long serialVersionUID = -8839964536590822815L;

	private CIMDataType iType;

	/**
	 * Constructs a <code>CIMTypedElement</code> with the given name and data
	 * type.
	 * 
	 * @param pName
	 *            Name of the element.
	 * @param pType
	 *            Data type of the element.
	 */
	public CIMTypedElement(String pName, CIMDataType pType) {
		super(pName);
		this.iType = pType;
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is a <code>CIMTypedElement</code> that represents the same name and
	 * type as this object.
	 * 
	 * @param pObj
	 *            The object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMTypedElement)) return false;
		CIMTypedElement that = (CIMTypedElement) pObj;
		if (!super.equals(that)) return false;
		return this.iType.equals(that.iType);
	}

	/**
	 * Returns the <code>CIMDataType</code> for this CIM Element.
	 * 
	 * @return <code>CIMDataType</code> of this CIM element.
	 */
	public CIMDataType getDataType() {
		return this.iType;
	}

	/**
	 * Returns a hash code value for the CIM typed element. This method is
	 * supported for the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return A hash code value for this CIM typed element.
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
		return MOF.typedElement(this, MOF.EMPTY);
	}
}
