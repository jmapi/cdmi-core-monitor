/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3496301    2012-03-02  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.cim;

//Sync'd against JSR48 1.0.0 javadoc (build 1.6.0_18) on Thu Mar 01 12:21:26 EST 2012
/**
 * This class represents a CIM Property as defined by the Distributed Management
 * Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure
 * Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). A CIM Property Object consists of a name, data type and value.
 * The CIM Property object also includes a flag to signify whether the property
 * is a key property (used as part of the name of the CIM element), a flag to
 * signify whether it was propagated from a parent class and the class origin
 * information (where the property was originally defined).
 * 
 * @param <E>
 *            Type parameter.
 */
public class CIMProperty<E> extends CIMValuedElement<E> {

	private static final long serialVersionUID = -4741931597423829396L;

	private boolean iKey, iPropagated;

	private String iOriginClass;

	/**
	 * Constructs a <code>CIMProperty</code> to be used in instances. For a
	 * <code>CIMClass</code>, <code>CIMClassProperty</code> shall be used. This
	 * can only be used for non-Key properties, non-propagated properties and
	 * when the the origin class is not needed.
	 * 
	 * @param pName
	 *            The name of the property.
	 * @param pType
	 *            The <code>CIMDataType</code> of the property.
	 * @param pValue
	 *            The value of the property.
	 */
	public CIMProperty(String pName, CIMDataType pType, E pValue) {
		this(pName, pType, pValue, false, false, null);
	}

	/**
	 * Constructs a <code>CIMProperty</code> to be used in instances. For a
	 * <code>CIMClass</code>, <code>CIMClassProperty</code> shall be used.
	 * 
	 * @param pName
	 *            The name of the property.
	 * @param pType
	 *            The <code>CIMDataType</code> of the property.
	 * @param pValue
	 *            The value of the property.
	 * @param pKey
	 *            <code>true</code> if the property is a key; otherwise
	 *            <code>false</code>.
	 * @param pPropagated
	 *            <code>true</code> if the value was propagated from the class.
	 * @param pOriginClass
	 *            The class in which this property was defined or overridden.
	 */
	public CIMProperty(String pName, CIMDataType pType, E pValue, boolean pKey,
			boolean pPropagated, String pOriginClass) {
		super(pName, pType, pValue);
		this.iKey = pKey;
		this.iPropagated = pPropagated;
		this.iOriginClass = pOriginClass;
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is a <code>CIMProperty</code> that represents the same name, type and
	 * value as this object.
	 * 
	 * @param pObj
	 *            The object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMProperty)) return false;
		if (!super.equals(pObj)) return false;
		CIMProperty<?> that = (CIMProperty<?>) pObj;
		return this.iKey == that.iKey
				&& this.iPropagated == that.iPropagated
				&& (this.iOriginClass == null ? that.iOriginClass == null : this.iOriginClass
						.equalsIgnoreCase(that.iOriginClass));
	}

	/**
	 * Returns the class in which this property was defined or overridden.
	 * 
	 * @return Name of class where this property was defined.
	 */
	public String getOriginClass() {
		return this.iOriginClass;
	}

	/**
	 * Convenience method for determining if this property is a Key.
	 * 
	 * @return <code>true</code> if this property is a key.
	 */
	public boolean isKey() {
		return this.iKey;
	}

	/**
	 * Determines if this property is Propagated. When this property is part of
	 * a class, this value designates that the class origin value is the same as
	 * the class name.
	 * 
	 * @return <code>true</code> if this property is propagated.
	 */
	public boolean isPropagated() {
		return this.iPropagated;
	}

}
