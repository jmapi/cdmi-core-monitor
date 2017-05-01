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
 * 1660756    2007-02-22  ebak         Embedded object support 
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 1737123    2007-06-15  ebak         Differences to JSR48 public review draft
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944839    2010-02-08  blaschke-oss Remove redundant toString() methods
 * 2944842    2010-02-08  blaschke-oss Missing thrown ArrayIndexOutOfBoundsException
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.cim;

import org.sblim.cimclient.internal.cim.CIMQualifiedElementInterfaceImpl;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:58 EST 2010
/**
 * This class represents a CIM property (when used for a <code>CIMClass</code>)
 * as defined by the Distributed Management Task Force (<a
 * href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). This class is to be used for all <code>CIMClass</code>
 * properties. NOTE: For instance properties, use the class
 * <code>CIMProperty</code>.
 * 
 * @param <E>
 *            Type parameter.
 * 
 */
public class CIMClassProperty<E> extends CIMProperty<E> implements CIMQualifiedElementInterface {

	private static final long serialVersionUID = -1455588144409014311L;

	private CIMQualifiedElementInterfaceImpl iQualiImpl;

	/**
	 * This method constructs an instance of <code>CIMClassProperty</code>.
	 * 
	 * @param pName
	 *            The name of the property.
	 * @param pType
	 *            The data type of the property.
	 * @param pValue
	 *            The value of the property.
	 * @param pQualifiers
	 *            The qualifiers for the property.
	 * @param pKey
	 *            <code>true</code> if the property is a key, otherwise
	 *            <code>false</code>.
	 * @param pPropagated
	 *            <code>true</code> if the property was inherited, otherwise
	 *            <code>false</code>.
	 * @param pOriginClass
	 *            The original class in which the property was defined.
	 */
	public CIMClassProperty(String pName, CIMDataType pType, E pValue,
			CIMQualifier<?>[] pQualifiers, boolean pKey, boolean pPropagated, String pOriginClass) {
		super(pName, pType, pValue, pKey | hasKey(pQualifiers), pPropagated, pOriginClass);
		this.iQualiImpl = new CIMQualifiedElementInterfaceImpl(pQualifiers, pKey, true);
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not null and is a
	 * <code>CIMClassProperty</code> that represents the same name, type and
	 * value as this object.
	 * 
	 * @param pObj
	 *            The object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMClassProperty)) return false;
		if (!super.equals(pObj)) return false;
		return this.iQualiImpl.equals(((CIMClassProperty<?>) pObj).iQualiImpl);
	}

	/**
	 * Returns a <code>CIMClassProperty</code> filtered as specified.
	 * 
	 * @param pIncludeQualifiers
	 *            If <code>true</code> all qualifiers are returned; otherwise no
	 *            qualifiers.
	 * @param pIncludeClassOrigin
	 *            If <code>true</code> the class origin is included; otherwise
	 *            no class origin is present.
	 * @return A filtered <code>CIMClassProperty</code>.
	 */
	public CIMClassProperty<E> filter(boolean pIncludeQualifiers, boolean pIncludeClassOrigin) {
		return filter(pIncludeQualifiers, pIncludeClassOrigin, false);
	}

	/**
	 * Returns a <code>CIMClassProperty</code> filtered as specified.
	 * 
	 * @param pIncludeQualifiers
	 *            If <code>true</code> all qualifiers are returned; otherwise no
	 *            qualifiers.
	 * @param pIncludeClassOrigin
	 *            If <code>true</code> the class origin is included; otherwise
	 *            no class origin is present.
	 * @param pLocalOnly
	 *            If <code>true</code> only the qualifiers that were not
	 *            propagated will be included.
	 * @return CIMClassProperty A filtered <code>CIMClassProperty</code>.
	 */
	public CIMClassProperty<E> filter(boolean pIncludeQualifiers, boolean pIncludeClassOrigin,
			boolean pLocalOnly) {
		// FIXME: should key depend on pIncludeQualifiers?
		return new CIMClassProperty<E>(getName(), getDataType(), getValue(),
				pIncludeQualifiers ? this.iQualiImpl.getQualifiers(pLocalOnly) : null, isKey(),
				isPropagated(), pIncludeClassOrigin ? getOriginClass() : null);
	}

	/**
	 * Get a qualifier by index.
	 * 
	 * @param pIndex
	 *            The index of the qualifier to retrieve.
	 * @return The qualifier at the specified index.
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public CIMQualifier<?> getQualifier(int pIndex) throws ArrayIndexOutOfBoundsException {
		return this.iQualiImpl.getQualifier(pIndex);
	}

	/**
	 * Gets a qualifier by name.
	 * 
	 * @param pName
	 *            The name of the qualifier to get.
	 * @return The qualifier requested or <code>null</code> if the qualifier
	 *         does not exist.
	 */
	public CIMQualifier<?> getQualifier(String pName) {
		return this.iQualiImpl.getQualifier(pName);
	}

	/**
	 * Get the number of qualifiers defined for this property.
	 * 
	 * @return The number of qualifiers defined for this property.
	 */
	public int getQualifierCount() {
		return this.iQualiImpl.getQualifierCount();
	}

	/**
	 * Returns the list of qualifiers for this property.
	 * 
	 * @return Qualifiers for this property.
	 */
	public CIMQualifier<?>[] getQualifiers() {
		return this.iQualiImpl.getQualifiers();
	}

	/**
	 * Gets a qualifier value by name.
	 * 
	 * @param pName
	 *            The name of the qualifier to get.
	 * @return <code>null</code> if the qualifier does not exist or value is
	 *         <code>null</code>, otherwise returns the reference to the
	 *         qualifier.
	 */
	public Object getQualifierValue(String pName) {
		return this.iQualiImpl.getQualifierValue(pName);
	}

	/**
	 * Checks whether the specified qualifier is one of the qualifiers defined
	 * for this property.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @return <code>true</code> if the qualifier exists in this property,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifier(String pName) {
		return this.iQualiImpl.hasQualifier(pName);
	}

	/**
	 * Checks whether the specified qualifier is one of the qualifiers defined
	 * for this property with the specified value. This method will return
	 * <code>false</code> if the qualifier is not applied or if the value does
	 * not match.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @param pValue
	 *            The value to be tested.
	 * @return <code>true</code> if the qualifier exists in this property,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifierValue(String pName, Object pValue) {
		return this.iQualiImpl.hasQualifierValue(pName, pValue);
	}

	/**
	 * Checks whether an array of qualifiers contains a key.
	 * 
	 * @param pQualiA
	 *            Array of qualifiers.
	 * 
	 * @return <code>true</code> if at least one of the qualifiers is a key,
	 *         <code>false</code> otherwise.
	 */
	private static boolean hasKey(CIMQualifier<?>[] pQualiA) {
		if (pQualiA == null) return false;
		Boolean trueBool = Boolean.TRUE;
		for (int i = 0; i < pQualiA.length; i++) {
			CIMQualifier<?> quali = pQualiA[i];
			if ("key".equalsIgnoreCase(quali.getName())) { return trueBool.equals(quali.getValue()); }
		}
		return false;
	}

}
