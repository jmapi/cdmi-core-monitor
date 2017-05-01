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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2975989    2010-03-24  blaschke-oss TCK: CIMQualifierType constructor does not handle null
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.cim;

import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * The CIMQualifierType class represents a CIM Qualifier Type as defined by the
 * Distributed Management Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM
 * Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). A Qualifier Type supplies the definition/rules for a qualifier.
 * A qualifier must have a qualifier type. CIMQualifierType has the following
 * components:
 * <ul>
 * <li><code>Name</code> - The name of the qualifier type.</li>
 * <li><code>Data Type</code> - The data type of the qualifier type.</li>
 * <li><code>Value</code> - The default value of the qualifier type (can be
 * <code>null</code>/uninitialized)</li>
 * <li><code>Scopes</code> - The scopes applicable to this qualifier type. In
 * other words what CIM Elements can the qualifier based on this type be applied
 * to.</li>
 * <li><code>Flavors</code> - The flavors applicable to this qualifier type.
 * Flavors describe the propagation and override rules for a qualifier.</li>
 * </ul>
 * 
 * @param <E>
 *            Type parameter.
 * @see CIMScope
 * @see CIMFlavor
 * @see CIMQualifier
 */
public class CIMQualifierType<E> extends CIMValuedElement<E> implements CIMNamedElementInterface {

	private static final long serialVersionUID = -4563643521754840535L;

	private CIMObjectPath iObjPath;

	private int iScope, iFlavor;

	/**
	 * Constructs a new CIM qualifier type, using the name, type of the
	 * specified CIM qualifier type.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> of a CIM qualifier type.
	 * @param pType
	 *            The <code>CIMDataType</code> of the qualifier type.
	 * @param pValue
	 *            The default value or <code>null</code> if no default value.
	 * @param pScope
	 *            The applicable scopes for the qualifier type.
	 * @param pFlavor
	 *            The applicable flavors for the qualifier type.
	 * @throws IllegalArgumentException
	 *             If the value/data type does not match.
	 */
	public CIMQualifierType(CIMObjectPath pPath, CIMDataType pType, E pValue, int pScope,
			int pFlavor) throws IllegalArgumentException {
		super(pPath == null ? null : pPath.getObjectName(), pType, pValue);
		this.iObjPath = pPath;
		this.iScope = pScope;
		this.iFlavor = pFlavor;
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is a <code>CIMQualifierType</code> object that represents the same
	 * value as this object.
	 * 
	 * @param pObj
	 *            The object to compare.
	 * @return <code>true</code> if the specified object it is the same as this
	 *         <code>CIMQualifierType</code>. Otherwise, <code>false</code>.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMQualifierType)) return false;
		if (!super.equals(pObj)) return false;
		CIMQualifierType<?> that = (CIMQualifierType<?>) pObj;
		return this.iObjPath.equals(that.iObjPath) && this.iScope == that.iScope;
	}

	/**
	 * Returns the flavors of this qualifier type as a <code>BitSet</code>.
	 * 
	 * @return <code>BitSet</code> of flavors for this qualifier type.
	 */
	public int getFlavor() {
		return this.iFlavor;
	}

	/**
	 * Get the object path for this <code>CIMQualifierType</code>.
	 * 
	 * @return The <code>CIMObjectPath</code> that represents this qualifier
	 *         type.
	 */
	public CIMObjectPath getObjectPath() {
		return this.iObjPath;
	}

	/**
	 * Returns the scopes of this qualifier type as a bit set.
	 * 
	 * @return Bit set of CIM element scopes for which this qualifier type is
	 *         applicable.
	 */
	public int getScope() {
		return this.iScope;
	}

	/**
	 * Returns a <code>String</code> representation of the
	 * <code>CIMQualifierType</code> This method is intended to be used only for
	 * debugging purposes, and the format of the returned string may vary
	 * between implementations. The returned string may be empty but may not be
	 * <code>null</code>.
	 * 
	 * @return A string representation of this qualifier type.
	 */
	@Override
	public String toString() {
		return MOF.qualifierDeclaration(this);
	}

}
