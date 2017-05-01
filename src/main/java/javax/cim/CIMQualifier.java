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
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.cim;

import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * This class represents a CIM qualifier as defined by the Distributed
 * Management Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM
 * Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). A qualifier provides additional information about classes,
 * associations, methods, parameters, properties, and/or references. A
 * <code>CIMQualifier</code> must have a CIM Qualifier Type. A qualifier and its
 * qualifier type must have the same name and data type. CIM Qualifiers can only
 * be applied to elements that are allowed by the scope defined by the CIM
 * Qualifier Type.
 * 
 * @param <E>
 *            Type parameter.
 * @see CIMQualifierType
 */
public class CIMQualifier<E> extends CIMValuedElement<E> {

	private static final long serialVersionUID = 3568987946093931214L;

	private int iFlavor;

	private boolean iPropagated;

	/**
	 * Constructs a CIM qualifier with the specified name, type, value, and
	 * flavors.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @param pType
	 *            The data type of the qualifier.
	 * @param pValue
	 *            The value of the qualifier.
	 * @param pFlavor
	 *            A list of override permissions. Flavors can be overridden from
	 *            the Qualifier Type definition to either restrict the
	 *            subclassing of a qualifier or to allow it. For the list of CIM
	 *            Flavors see the <code>CIMFlavor</code> class.
	 * @see CIMFlavor
	 */
	public CIMQualifier(String pName, CIMDataType pType, E pValue, int pFlavor) {
		this(pName, pType, pValue, pFlavor, false);
	}

	/**
	 * Constructs a CIM qualifier with the specified name, type, value, and
	 * flavors.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @param pType
	 *            The data type of the qualifier.
	 * @param pValue
	 *            The value of the qualifier.
	 * @param pFlavor
	 *            A list of override permissions. Flavors can be overridden from
	 *            the Qualifier Type definition to either restrict the
	 *            subclassing of a qualifier or to allow it. For the list of CIM
	 *            Flavors see the <code>CIMFlavor</code> class.
	 * @param pIsPropagated
	 *            <code>true</code> if the qualifier was propagated;
	 *            <code>false</code> otherwise.
	 * @see CIMFlavor
	 */
	public CIMQualifier(String pName, CIMDataType pType, E pValue, int pFlavor,
			boolean pIsPropagated) {
		super(pName, pType, pValue);
		this.iFlavor = pFlavor;
		this.iPropagated = pIsPropagated;
	}

	/**
	 * Compares this <code>CIMQualifier</code> against the specified
	 * <code>CIMQualifier</code>. The result is <code>true</code> if and only if
	 * the argument is not <code>null</code> and is a <code>CIMQualifier</code>
	 * that represents the same name, type and value as this
	 * <code>CIMQualifier</code>.
	 * 
	 * @param pObj
	 *            The object to compare.
	 * @return <code>true</code> if the input qualifier is equal, otherwise
	 *         <code>false</code>.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMQualifier)) return false;
		if (!super.equals(pObj)) return false;
		CIMQualifier<?> that = (CIMQualifier<?>) pObj;
		return this.iFlavor == that.iFlavor && this.iPropagated == that.iPropagated;
	}

	/**
	 * Returns the CIM flavors for this CIM qualifier.
	 * 
	 * @return A <code>BitSet</code> of CIM flavors in this CIM qualifier.
	 */
	public int getFlavor() {
		return this.iFlavor;
	}

	/**
	 * Determines if this qualifier is propagated. If the qualifier was
	 * inherited, this value will be <code>true</code>. If the qualifier was
	 * applied to the element directly, this value will be <code>false</code>.
	 * 
	 * @return <code>true</code> if this property is propagated;
	 *         <code>false</code> otherwise.
	 */
	public boolean isPropagated() {
		return this.iPropagated;
	}

	/**
	 * Returns a <code>String</code> representation of the
	 * <code>CIMQualifier</code>. This method is intended to be used only for
	 * debugging purposes, and the format of the returned string may vary
	 * between implementations. The returned string may be empty but may not be
	 * <code>null</code>.
	 * 
	 * @return A string representation of this qualifier.
	 */
	@Override
	public String toString() {
		return MOF.qualifier(this);
	}

}
