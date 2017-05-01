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
 * 1737141    2007-06-19  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944842    2010-02-08  blaschke-oss Missing thrown ArrayIndexOutOfBoundsException
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.cim;

import org.sblim.cimclient.internal.cim.CIMQualifiedElementInterfaceImpl;
import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * This class represents a CIM Parameter. A CIM Parameter is a schema item, thus
 * it can only be part of a <code>CIMMethod</code> definition for a
 * <code>CIMClass</code>. A parameter can be used to define an input, output or
 * input/output parameter. A <code>CIMParameter</code> consists of a name, data
 * type and qualifiers. <code>CIMParameters</code> do not have values - so you
 * can not set a default value. CIM Parameters are defined by the Distributed
 * Management Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM
 * Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). To invoke a method, you would use <code>CIMArgument</code>.
 * 
 * @param <E>
 *            Type parameter.
 * 
 * @see CIMMethod
 */
public class CIMParameter<E> extends CIMTypedElement implements CIMQualifiedElementInterface {

	private static final long serialVersionUID = -4741931597423829396L;

	private CIMQualifiedElementInterfaceImpl iQualiImpl;

	/**
	 * Constructs a <code>CIMParameter</code> object using the specified name,
	 * data type and qualifiers. Takes a string for the name of an existing CIM
	 * parameter and creates a new instance of a CIM parameter, using the name
	 * and identifier of the existing CIM parameter.
	 * 
	 * @param pName
	 *            Name of this parameter.
	 * @param pType
	 *            Data type of this parameter.
	 * @param pQualifiers
	 *            Qualifiers for this parameter.
	 */
	public CIMParameter(String pName, CIMDataType pType, CIMQualifier<?>[] pQualifiers) {
		super(pName, pType);
		this.iQualiImpl = new CIMQualifiedElementInterfaceImpl(pQualifiers, false, true);
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is a <code>CIMParameter</code> object that represents the same value
	 * as this object.
	 * 
	 * @param pObj
	 *            The object to compare.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMParameter)) return false;
		if (!super.equals(pObj)) return false;
		CIMParameter<?> that = (CIMParameter<?>) pObj;
		return this.iQualiImpl.equals(that.iQualiImpl);
	}

	/**
	 * Returns a <code>CIMParameter</code> filtered as specified.
	 * 
	 * @param pIncludeQualifiers
	 *            If <code>true</code> all qualifiers are returned; otherwise no
	 *            qualifiers.
	 * @param pLocalOnly
	 *            If <code>true</code> only the qualifiers that were not
	 *            propagated will be included.
	 * @return A filtered <code>CIMParameter</code>.
	 */
	public CIMParameter<E> filter(boolean pIncludeQualifiers, boolean pLocalOnly) {
		return new CIMParameter<E>(getName(), getDataType(), pIncludeQualifiers ? this.iQualiImpl
				.getQualifiers(pLocalOnly) : null);
	}

	/**
	 * Get a qualifier by index.
	 * 
	 * @param pIndex
	 *            The index of the qualifier.
	 * @return The Qualifier at index pIndex.
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
	 * @return <code>null</code> if the qualifier does not exist, otherwise
	 *         returns the reference to the qualifier.
	 */
	public CIMQualifier<?> getQualifier(String pName) {
		return this.iQualiImpl.getQualifier(pName);
	}

	/**
	 * Get the number of qualifiers defined for this CIM Parameter.
	 * 
	 * @return The number of qualifiers.
	 */
	public int getQualifierCount() {
		return this.iQualiImpl.getQualifierCount();
	}

	/**
	 * Returns the list of qualifiers for this class.
	 * 
	 * @return Qualifiers for this class.
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
	 * Checks whether the specified qualifier is one of the qualifiers in this
	 * CIM element.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @return <code>true</code> if the qualifier exists in this CIM parameter,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifier(String pName) {
		return this.iQualiImpl.hasQualifier(pName);
	}

	/**
	 * Checks whether the specified qualifier is one of the qualifiers defined
	 * for this parameter with the specified value. This method will return
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
	 * Returns a <code>String</code> representation of the
	 * <code>CIMParameter</code>. This method is intended to be used only for
	 * debugging purposes, and the format of the returned string may vary
	 * between implementations. The returned string may be empty but may not be
	 * <code>null</code>.
	 * 
	 * @return String representation of this parameter.
	 */
	@Override
	public String toString() {
		return MOF.parameter(this, MOF.EMPTY);
	}

}
