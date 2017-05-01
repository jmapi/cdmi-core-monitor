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
 * 1737123    2007-06-15  ebak         Differences to JSR48 public review draft
 * 1783288    2007-09-10  ebak         CIMClass.isAssociation() not working for retrieved classes.
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944842    2010-02-08  blaschke-oss Missing thrown ArrayIndexOutOfBoundsException
 * 2975917    2010-03-24  blaschke-oss TCK: CIMClass.getProperty() does not handle null property
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3496301    2012-03-02  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final
 * 3500619    2012-03-16  blaschke-oss JSR48 1.0.0: CIMClass association/key clean up
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 *    2719    2013-12-10  blaschke-oss TCK: CIM APIs should not generate NullPointerException
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import org.sblim.cimclient.internal.cim.CIMElementSorter;
import org.sblim.cimclient.internal.cim.CIMQualifiedElementInterfaceImpl;
import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * This class represents a CIM class as defined by the Distributed Management
 * Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure
 * Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). A <code>CIMClass</code> has the following attributes:
 * <ul>
 * <li>an object path describing the location and name of the class</li>
 * <li>superclass name (can be <code>null</code> if no superclass)</li>
 * <li>an array of qualifiers for the class</li>
 * <li>an array of properties</li>
 * <li>an array of methods</li>
 * </ul>
 */
public class CIMClass extends CIMElement implements CIMQualifiedElementInterface,
		CIMNamedElementInterface {

	private static final long serialVersionUID = -5634561913210025100L;

	private CIMQualifiedElementInterfaceImpl iQualiImpl;

	private CIMObjectPath iObjPath;

	private String iSuperClass;

	private CIMClassProperty<?>[] iProps;

	private CIMMethod<?>[] iMethods;

	private boolean iIsKeyed;

	private boolean iIsAssoc;

	/**
	 * Creates and instantiates a Java object representing a CIM Class. This
	 * method may or may not validate the <code>pIsAssociation</code> and
	 * <code>pIsKeyed</code> parameters. If an invalid value is supplied (i.e.
	 * the class is an association, but the <code>pIsAssociation</code> was set
	 * to <code>false</code>), it may or may not be corrected.
	 * 
	 * @param pPath
	 *            Object Name of the CIM class.
	 * @param pSuperClass
	 *            Name of the superclass.
	 * @param pQualifiers
	 *            List of qualifiers of the CIM class.
	 * @param pProperties
	 *            List of properties of the CIM class.
	 * @param pMethods
	 *            List of methods of the CIM class.
	 * @param pIsAssociation
	 *            <code>true</code> if the CIM class is an Association,
	 *            <code>false</code> otherwise.
	 * @param pIsKeyed
	 *            <code>true</code> if the CIM class has Keys,
	 *            <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             If pPath is null.
	 * 
	 */
	public CIMClass(CIMObjectPath pPath, String pSuperClass, CIMQualifier<?>[] pQualifiers,
			CIMClassProperty<?>[] pProperties, CIMMethod<?>[] pMethods, boolean pIsAssociation,
			boolean pIsKeyed) {
		this(pPath, pSuperClass, pQualifiers, pProperties, pMethods);
		// if (this.isAssociation() != pIsAssociation) {
		// throw new IllegalArgumentException(
		// "pIsAssociation does not match pQualifiers! pIsAssociation is " +
		// pIsAssociation
		// + " while pQualifiers "
		// + (this.isAssociation() ? "contains" : "does not contain")
		// + " qualifier with association in class " +
		// this.iObjPath.getObjectName());
		// }
		// if (this.isKeyed() != pIsKeyed) {
		// throw new IllegalArgumentException(
		// "pIsKeyed does not match pProperties! pIsKeyed is " + pIsKeyed
		// + " while pProperties "
		// + (this.isKeyed() ? "contains" : "does not contain")
		// + " property with key qualifier in class " +
		// this.iObjPath.getObjectName());
		// }
	}

	/**
	 * Creates and instantiates a Java object representing a CIM Class. This
	 * constructor will inspect the class to determine if it is an association
	 * or has keys.
	 * 
	 * @param pName
	 *            Name of the CIM class.
	 * @param pSuperClass
	 *            Name of the superclass.
	 * @param pQualifiers
	 *            List of qualifiers of the CIM class.
	 * @param pProperties
	 *            List of properties of the CIM class.
	 * @param pMethods
	 *            List of methods of the CIM class.
	 * @throws IllegalArgumentException
	 *             If pName is null.
	 */
	public CIMClass(String pName, String pSuperClass, CIMQualifier<?>[] pQualifiers,
			CIMClassProperty<?>[] pProperties, CIMMethod<?>[] pMethods) {
		this(new CIMObjectPath(null, null, null, null, pName, null), pSuperClass, pQualifiers,
				pProperties, pMethods);
	}

	/**
	 * This private constructor does the actual work for the two public
	 * constructors and sets the iIsAssoc and iIsKeyed instance variables based
	 * on whether there is qualifier with name="Association" and value=true and
	 * a property with a qualifier with name="Key" and value=true, respectively.
	 */
	private CIMClass(CIMObjectPath pPath, String pSuperClass, CIMQualifier<?>[] pQualifiers,
			CIMClassProperty<?>[] pProperties, CIMMethod<?>[] pMethods) {
		super(pPath == null ? null : pPath.getObjectName());
		this.iObjPath = pPath;
		this.iSuperClass = pSuperClass;
		this.iQualiImpl = new CIMQualifiedElementInterfaceImpl(pQualifiers);
		this.iProps = (CIMClassProperty[]) CIMElementSorter.sort(pProperties);
		this.iMethods = (CIMMethod[]) CIMElementSorter.sort(pMethods);
		this.iIsAssoc = this.iQualiImpl.hasQualifierValue("Association", Boolean.TRUE);
		this.iIsKeyed = hasKey(pProperties);
	}

	/**
	 * Indicates whether the specified <code>CIMClass</code> is equal to this
	 * <code>CIMClass</code>.
	 * 
	 * @param pObj
	 *            The <code>CIMClass</code> object with which to compare.
	 * @return <code>true</code> if this object is the same as the
	 *         <code>pObj</code> argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMClass)) return false;
		CIMClass that = (CIMClass) pObj;
		if (!super.equals(that)) return false;
		if (this.iSuperClass == null ? that.iSuperClass != null : !this.iSuperClass
				.equalsIgnoreCase(that.iSuperClass)) return false;
		if (!this.iQualiImpl.equals(that.iQualiImpl) || !Arrays.equals(this.iProps, that.iProps)
				|| !Arrays.equals(this.iMethods, that.iMethods) || this.iIsAssoc != that.iIsAssoc
				|| isKeyed() != that.isKeyed()) return false;
		return true;
	}

	/**
	 * This method returns a new <code>CIMClass</code> with properties filtered
	 * according to the input parameters. Inclusion of class origin and
	 * qualifiers can also be controlled. Methods will not be included in the
	 * class returned.
	 * 
	 * @param pLocalOnly
	 *            If <code>true</code> only the elements defined in this class
	 *            are included; otherwise all elements are included.
	 * @param pIncludeQualifiers
	 *            If <code>true</code> qualifiers are included on all elements;
	 *            otherwise no qualifiers are included.
	 * @param pIncludeClassOrigin
	 *            If <code>true</code>, the ClassOrigin attribute is included.
	 * @param pPropertyList
	 *            If the <code>PropertyList</code> input parameter is not
	 *            <code>null</code>, the members of the array define one or more
	 *            Property names. The <code>CIMClass</code> returned does not
	 *            include elements for any Properties missing from this list. If
	 *            the <code>PropertyList</code> input parameter is an empty
	 *            array this signifies that no Properties are included in the
	 *            class returned. If the <code>PropertyList</code> input
	 *            parameter is <b><code>null</code></b> this specifies that all
	 *            Properties are included in the class returned. If the
	 *            <code>PropertyList</code> contains duplicate elements or
	 *            invalid property names, they are ignored.
	 * @return <code>CIMClass</code> matching the requested criteria.
	 */
	public CIMClass filterProperties(boolean pLocalOnly, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin, String[] pPropertyList) {
		ArrayList<CIMClassProperty<?>> newPropAList = new ArrayList<CIMClassProperty<?>>();
		// place pPropertyList into a set, for easy and fast search
		TreeSet<String> set;
		if (pPropertyList != null) {
			set = new TreeSet<String>();
			for (int i = 0; i < pPropertyList.length; i++)
				set.add(pPropertyList[i].toUpperCase());
		} else set = null;
		for (int i = 0; i < getPropertyCount(); i++) {
			CIMClassProperty<?> refProp = getProperty(i);
			if (pLocalOnly && refProp.isPropagated()) continue;
			if (set == null || set.contains(refProp.getName().toUpperCase())) newPropAList
					.add(new CIMClassProperty<Object>(refProp.getName(), refProp.getDataType(),
							refProp.getValue(),
							pIncludeQualifiers ? refProp.getQualifiers() : null,
							pIncludeQualifiers ? refProp.isKey() : false, // FIXME:
							// Should it depend on pIncludeQualifiers?
							refProp.isPropagated(), pIncludeClassOrigin ? refProp.getOriginClass()
									: null));
		}
		return new CIMClass(getObjectPath(), getSuperClassName(), getQualifiers(), newPropAList
				.toArray(new CIMClassProperty[0]), null, isAssociation(), isKeyed());
	}

	/**
	 * Returns a list of key properties for this CIM class.
	 * 
	 * @return The list of CIM properties that are keys for this CIM class.
	 */
	public CIMClassProperty<?>[] getKeys() {
		ArrayList<CIMClassProperty<?>> keyAList = new ArrayList<CIMClassProperty<?>>();
		for (int i = 0; i < getPropertyCount(); i++) {
			CIMClassProperty<?> prop = getProperty(i);
			if (prop.isKey()) keyAList.add(prop);
		}
		return keyAList.toArray(new CIMClassProperty[0]);
	}

	/**
	 * Get a method by index.
	 * 
	 * @param pIndex
	 *            The index of the method to retrieve.
	 * @return The <code>CIMMethod</code> at the specified index.
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public CIMMethod<?> getMethod(int pIndex) throws ArrayIndexOutOfBoundsException {
		return this.iMethods[pIndex];
	}

	/**
	 * Returns the specified CIM method in this CIM class.
	 * 
	 * @param pName
	 *            The string name of the method to retrieve. The name may be
	 *            specified in the form <code>"originClass.methodName"</code>.
	 * @return The CIM method specified or <code>null</code> if the method does
	 *         not exist.
	 */

	public CIMMethod<?> getMethod(String pName) {
		return getMethod(pName, null);
	}

	/**
	 * Returns the CIM method specified by its name and optionally, its origin
	 * class. The origin class is the class in which the method is defined.
	 * 
	 * @param pName
	 *            The string name of the method to get.
	 * @param pOriginClass
	 *            (Optional) The class in which the method was defined.
	 * @return The CIM method specified or <code>null</code> if the method does
	 *         not exist.
	 */
	public CIMMethod<?> getMethod(String pName, String pOriginClass) {
		CIMMethod<?> method = (CIMMethod<?>) CIMElementSorter.find(this.iMethods, pName);
		if (method == null) return null;
		if (pOriginClass == null || pOriginClass.equalsIgnoreCase(method.getOriginClass())) return method;
		return null;
	}

	/**
	 * Get the number of methods defined in this CIM class.
	 * 
	 * @return The number of methods defined in the CIM class.
	 */
	public int getMethodCount() {
		return this.iMethods == null ? 0 : this.iMethods.length;
	}

	/**
	 * Get the CIM methods defined in this CIM class.
	 * 
	 * @return The methods in this CIM class.
	 */
	public CIMMethod<?>[] getMethods() {
		return this.iMethods != null ? this.iMethods : new CIMMethod[0];
	}

	/**
	 * This method returns the <code>CIMObjectPath</code> that represents this
	 * CIM class.
	 * 
	 * @return The <code>CIMObjectPath</code> that represents this CIM class.
	 */
	public CIMObjectPath getObjectPath() {
		return this.iObjPath;
	}

	/**
	 * Get the properties defined for this CIM class.
	 * 
	 * @return The properties for this class.
	 */
	public CIMClassProperty<?>[] getProperties() {
		return this.iProps != null ? this.iProps : new CIMClassProperty[0];
	}

	/**
	 * Get a class property by index.
	 * 
	 * @param pIndex
	 *            The index of the class property to retrieve.
	 * @return The <code>CIMClassProperty</code> at the specified index.
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public CIMClassProperty<?> getProperty(int pIndex) throws ArrayIndexOutOfBoundsException {
		return this.iProps[pIndex];
	}

	/**
	 * Gets the specified property.
	 * 
	 * @param pName
	 *            The text string for the name of the property.
	 * @return The property requested or <code>null</code> if the property does
	 *         not exist.
	 */
	public CIMClassProperty<?> getProperty(String pName) {
		return (CIMClassProperty<?>) CIMElementSorter.find(this.iProps, pName);
	}

	/**
	 * Gets the specified property.
	 * 
	 * @param pName
	 *            The string name of the property to get.
	 * @param pOriginClass
	 *            (Optional) The string name of the class in which the property
	 *            was defined.
	 * @return The property requested or <code>null</code> if the property does
	 *         not exist.
	 */
	public CIMClassProperty<?> getProperty(String pName, String pOriginClass) {
		CIMClassProperty<?> prop = (CIMClassProperty<?>) CIMElementSorter.find(this.iProps, pName);
		if (prop == null || pOriginClass == null) return prop;
		return pOriginClass.equalsIgnoreCase(prop.getOriginClass()) ? prop : null;
	}

	/**
	 * Get the number of properties defined in this <code>CIMClass</code>.
	 * 
	 * @return The number of properties defined in the <code>CIMClass</code>.
	 */
	public int getPropertyCount() {
		return this.iProps != null ? this.iProps.length : 0;
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
	 * Get the number of qualifiers defined in this CIM class.
	 * 
	 * @return The number of qualifiers defined in the CIM class.
	 */
	public int getQualifierCount() {
		return this.iQualiImpl.getQualifierCount();
	}

	/**
	 * Returns the list of qualifiers for the CIM class.
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
	 * Gets the name of the parent of this CIM class.
	 * 
	 * @return The name of the parent class.
	 */
	public String getSuperClassName() {
		return this.iSuperClass;
	}

	/**
	 * Checks whether the specified qualifier is one of the qualifiers in this
	 * CIM class.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @return <code>true</code> if the qualifier exists in this CIM class,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifier(String pName) {
		return this.iQualiImpl.hasQualifier(pName);
	}

	/**
	 * Checks whether the specified qualifier is one of the qualifiers defined
	 * for this class with the specified value. This method will return
	 * <code>false</code> if the qualifier is not applied or if the value does
	 * not match.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @param pValue
	 *            The value to be tested.
	 * @return <code>true</code> if the qualifier exists and has the value,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifierValue(String pName, Object pValue) {
		return this.iQualiImpl.hasQualifierValue(pName, pValue);
	}

	/**
	 * Identifies whether or not this CIM class is an association. An
	 * association is a relationship between two or more classes or instances of
	 * two or more classes. The properties of an association class include
	 * references, or pointers, to the two or more instances. All CIM classes
	 * can be included in one or more associations.
	 * 
	 * @return <code>true</code> if this CIM class is an association; otherwise,
	 *         <code>false</code>.
	 */
	public boolean isAssociation() {
		return this.iIsAssoc;
	}

	/**
	 * Identifies whether or not this class is keyed. Only keyed classes can
	 * have instances. Returns <code>true</code> if this CIM class has one or
	 * more key properties. Otherwise, returns <code>false</code>.
	 * 
	 * @return <code>true</code> if this CIM class has a key property, otherwise
	 *         returns <code>false</code>.
	 */
	public boolean isKeyed() {
		return this.iIsKeyed;
	}

	/**
	 * Returns a new CIM instance initialized with the default CIM properties,
	 * values and name of this CIM class.
	 * 
	 * @return A CIM instance of this CIM class.
	 */
	public CIMInstance newInstance() {
		return new CIMInstance(this.iObjPath, this.iProps);
	}

	/**
	 * Returns a <code>String</code> representation of the CIM class. This
	 * method is intended to be used only for debugging purposes, and the format
	 * of the returned string may vary between implementations. The returned
	 * string may be empty but may not be <code>null</code>.
	 * 
	 * @return A <code>String</code> representation of this CIM class.
	 */
	@Override
	public String toString() {
		return MOF.classDeclaration(this, MOF.EMPTY);
	}

	/**
	 * Checks whether an array of properties contains a key.
	 * 
	 * @param pProps
	 *            Array of properties.
	 * @return <code>true</code> if at least one of the class properties is a
	 *         key, <code>false</code> otherwise.
	 */
	private static boolean hasKey(CIMClassProperty<?>[] pProps) {
		if (pProps == null) return false;
		for (int i = 0; i < pProps.length; i++)
			if (pProps[i].isKey()) return true;
		return false;
	}

}
