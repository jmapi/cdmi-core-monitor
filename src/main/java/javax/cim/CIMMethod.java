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
 * Change History
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944842    2010-02-08  blaschke-oss Missing thrown ArrayIndexOutOfBoundsException
 * 3001333    2010-05-19  blaschke-oss CIMMethod class ignores propagated parameter
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.cim;

import java.util.Arrays;

import org.sblim.cimclient.internal.cim.CIMElementSorter;
import org.sblim.cimclient.internal.cim.CIMQualifiedElementInterfaceImpl;
import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * This class represents a CIM Method as defined by the Distributed Management
 * Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure
 * Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>).
 * 
 * @param <E>
 *            Type parameter.
 */
public class CIMMethod<E> extends CIMTypedElement implements CIMQualifiedElementInterface {

	private static final long serialVersionUID = -3920536802046705977L;

	private CIMQualifiedElementInterfaceImpl iQualiImpl;

	private CIMParameter<?>[] iParams;

	private boolean iPropagated;

	private String iOriginClass;

	/**
	 * Constructs a <code>CIMMethod</code> object with the specified
	 * information.
	 * 
	 * @param pName
	 *            The name of the method.
	 * @param pType
	 *            The data type of the method.
	 * @param pQualis
	 *            The method qualifiers.
	 * @param pParams
	 *            The array of parameters for this method.
	 * @param pPropagated
	 *            Is this method propagated from the superclass.
	 * @param pOriginClass
	 *            The class this method was defined or overridden in.
	 */
	public CIMMethod(String pName, CIMDataType pType, CIMQualifier<?>[] pQualis,
			CIMParameter<?>[] pParams, boolean pPropagated, String pOriginClass) {
		super(pName, pType);
		this.iQualiImpl = new CIMQualifiedElementInterfaceImpl(pQualis, false, true);
		this.iParams = (CIMParameter[]) CIMElementSorter.sort(pParams);
		this.iPropagated = pPropagated;
		this.iOriginClass = pOriginClass;
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is a <code>CIMMethod</code> object that represents the same value as
	 * this object.
	 * 
	 * @param pObj
	 *            The object to compare.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMMethod)) return false;
		if (!super.equals(pObj)) return false;
		CIMMethod<?> that = (CIMMethod<?>) pObj;
		if (!this.iQualiImpl.equals(that.iQualiImpl)) return false;
		if (this.iPropagated != that.iPropagated) return false;
		return Arrays.equals(getParameters(), that.getParameters());
	}

	/**
	 * Returns a <code>CIMMethod</code> filtered as specified.
	 * 
	 * @param pIncludeQualifiers
	 *            If <code>true</code> all qualifiers are returned; otherwise no
	 *            qualifiers.
	 * @param pIncludeClassOrigin
	 *            If <code>true</code> the class origin is included; otherwise
	 *            no class origin is present.
	 * @return A filtered <code>CIMMethod</code>.
	 */
	public CIMMethod<E> filter(boolean pIncludeQualifiers, boolean pIncludeClassOrigin) {
		return filter(pIncludeQualifiers, pIncludeClassOrigin, false);
	}

	/**
	 * Returns a <code>CIMMethod</code> filtered as specified.
	 * 
	 * @param pIncludeQualifiers
	 *            If <code>true</code> all qualifiers are returned; otherwise no
	 *            qualifiers.
	 * @param pIncludeClassOrigin
	 *            If <code>true</code> the class origin is included; otherwise
	 *            no class origin is present
	 * @param pLocalOnly
	 *            If <code>true</code> only the qualifiers defined on this class
	 *            are included; otherwise all qualifiers are included.
	 * 
	 * @return A filtered <code>CIMMethod</code>.
	 */
	public CIMMethod<E> filter(boolean pIncludeQualifiers, boolean pIncludeClassOrigin,
			boolean pLocalOnly) {
		return new CIMMethod<E>(getName(), getDataType(), pIncludeQualifiers ? this.iQualiImpl
				.getQualifiers(pLocalOnly) : null, this.iParams, this.iPropagated,
				pIncludeClassOrigin ? this.iOriginClass : null);
	}

	/**
	 * Returns the class name in which this method was defined or overridden.
	 * 
	 * @return Name of class where this property was defined.
	 */
	public String getOriginClass() {
		return this.iOriginClass;
	}

	/**
	 * Get the parameter that matches the specified name.
	 * 
	 * @param pName
	 *            The name of the <code>CIMParameter</code> to retrieve.
	 * @return <code>CIMParameter</code> matching the name specified; otherwise
	 *         <code>null</code>.
	 */
	public CIMParameter<?> getParameter(String pName) {
		return (CIMParameter<?>) CIMElementSorter.find(this.iParams, pName);
	}

	/**
	 * Returns an array of the parameters for this method.
	 * 
	 * @return The parameters for this method.
	 */
	public CIMParameter<?>[] getParameters() {
		return this.iParams == null ? new CIMParameter[0] : this.iParams;
	}

	/**
	 * Get a qualifier by index.
	 * 
	 * @param pIndex
	 *            The index of the qualifier.
	 * @return The Qualifier at index <code>pIndex</code>.
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
	 * Get the number of qualifiers defined for this CIM Method.
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
	 * CIM method.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @return <code>true</code> if the qualifier exists in this CIM method,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifier(String pName) {
		return this.iQualiImpl.hasQualifier(pName);
	}

	/**
	 * Checks whether the specified qualifier is one of the qualifiers defined
	 * for this method with the specified value. This method will return
	 * <code>false</code> if the qualifier is not applied or if the value does
	 * not match.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @param pValue
	 *            The value to be tested.
	 * @return <code>true</code> if the qualifier exists and has this value,
	 *         otherwise false.
	 */
	public boolean hasQualifierValue(String pName, Object pValue) {
		return this.iQualiImpl.hasQualifierValue(pName, pValue);
	}

	/**
	 * Determines if this method is Propagated.
	 * 
	 * @return <code>true</code> if this method is propagated.
	 */
	public boolean isPropagated() {
		return this.iPropagated;
	}

	/**
	 * Returns a <code>String</code> representation of the
	 * <code>CIMMethod</code>. This method is intended to be used only for
	 * debugging purposes, and the format of the returned string may vary
	 * between implementations. The returned string may be empty but may not be
	 * <code>null</code>.
	 * 
	 * @return The string representation of this method.
	 */
	@Override
	public String toString() {
		return MOF.methodDeclaration(this, MOF.EMPTY);
	}

}
