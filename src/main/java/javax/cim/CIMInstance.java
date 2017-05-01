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
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 1776114    2007-08-21  ebak         Cannot derive instance of class CIM_IndicationSubscription
 * 1855726    2008-02-11  blaschke-oss CIMInstance.deriveInstance is setting wrong CIMObjectPath
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944842    2010-02-08  blaschke-oss Missing thrown ArrayIndexOutOfBoundsException
 * 3529151    2012-08-22  blaschke-oss TCK: CIMInstance property APIs include keys from COP
 */

package javax.cim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;

import org.sblim.cimclient.internal.cim.CIMElementSorter;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.StringSorter;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:58 EST 2010
/**
 * This class represents a CIM instance as defined by the Distributed Management
 * Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure
 * Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>).
 */
public class CIMInstance extends Object implements CIMNamedElementInterface, Serializable {

	private static final long serialVersionUID = -249160087013230559L;

	private static final CIMProperty<?>[] EMPTY_PROP_A = new CIMProperty[0];

	private CIMObjectPath iObjPath;

	private CIMProperty<?>[] iProps;

	/**
	 * Constructs a <code>CIMInstance</code> object using the name and
	 * properties specified.
	 * 
	 * @param pName
	 *            The <code>CIMObjectPath</code> for this
	 *            <code>CIMInstance</code>.
	 * @param pProps
	 *            The properties for this <code>CIMInstance</code>.
	 * @throws IllegalArgumentException
	 *             If <code>pName</code> is <code>null</code> or
	 *             <code>pName.getObjectName()</code> is <code>null</code>.<br>
	 *             [OPTIONAL] - If the key property values do not match the
	 *             values in the property array. This is optional due to the
	 *             cost of the verification. Some implementations may leave it
	 *             up to the developer to ensure that the values match.
	 */
	public CIMInstance(CIMObjectPath pName, CIMProperty<?>[] pProps)
			throws IllegalArgumentException {
		if (pName == null) {
			String msg = "CIMObjectPath parameter cannot be null!";
			// TODO: tracing
			throw new IllegalArgumentException(msg);
		}
		if (pName.getObjectName() == null) {
			String msg = "ObjectName cannot be null!";
			// TODO: tracing
			throw new IllegalArgumentException(msg);
		}

		this.iObjPath = pName;
		if (pProps != null) {
			this.iProps = pProps;
			CIMElementSorter.sort(this.iProps);
		} else {
			this.iProps = new CIMProperty[0];
		}
	}

	/**
	 * Returns a </code>CIMInstance</code> with the updated
	 * <code>CIMObjectPath</code>.
	 * 
	 * @param pPath
	 *            The complete <code>CIMObjectPath</code> for this instance.
	 * @return A new <code>CIMInstance</code> with the updated
	 *         <code>CIMObjectPath</code>.
	 */
	public CIMInstance deriveInstance(CIMObjectPath pPath) {
		return new CIMInstance(pPath, this.iProps);
	}

	/**
	 * Returns a <code>CIMInstance</code> with the updated values for the
	 * properties in <code>pPropA</code>. Any new properties are ignored.
	 * 
	 * @param pPropA
	 *            The array of properties to update.
	 * @return A new instance with the updated properties.
	 */
	public CIMInstance deriveInstance(CIMProperty<?>[] pPropA) {
		if (pPropA == null || pPropA.length == 0) return this;
		CIMProperty<?>[] newPropA = new CIMProperty[getPropertyCount()];
		for (int i = 0; i < newPropA.length; i++)
			newPropA[i] = this.iProps[i];
		for (int i = 0; i < pPropA.length; i++) {
			CIMProperty<?> newProp = pPropA[i];
			int idx = CIMElementSorter.findIdx(newPropA, newProp.getName());
			if (idx < 0) continue;
			CIMProperty<?> oldProp = newPropA[idx];
			/*
			 * 1776114 -> reference type comparison shouldn't be sensitive to
			 * the referenced class name due to derivation
			 */
			if (!typesEqual(oldProp, newProp)) {
				LogAndTraceBroker.getBroker().trace(
						Level.FINE,
						"CIMInstance.deriveInstance() can update only property "
								+ "values but not property types!\n" + "original property: "
								+ oldProp + "\nnew property: " + newProp);
				continue;
			}
			newPropA[idx] = new CIMProperty<Object>(oldProp.getName(), newProp.getDataType(),
					newProp.getValue(), oldProp.isKey(), oldProp.isPropagated(), oldProp
							.getOriginClass());
		}
		return new CIMInstance(this.iObjPath, newPropA);
	}

	/**
	 * Indicates whether some other instance is equal to this one. Two
	 * <code>CIMInstances</code> are considered equal if the names are the same.
	 * This method does NOT compare each property value.
	 * 
	 * @param pObj
	 *            The object to compare.
	 * @return <code>true</code> if the specified path references the same
	 *         instance, otherwise <code>false</code>.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMInstance)) return false;
		CIMInstance that = (CIMInstance) pObj;
		return this.iObjPath.equals(that.iObjPath);
	}

	/**
	 * This method returns a new <code>CIMInstance</code> with properties
	 * filtered according to the input parameters. Inclusion of class origin and
	 * qualifiers can also be controlled.
	 * 
	 * @param pLocalOnly
	 *            Include only the properties values that were instantiated in
	 *            this instance.
	 * @param pIncludeClassOrigin
	 *            classOrigins are only included if <code>true</code>.
	 * @param pPropertyList
	 *            If the <code>pPropertyList</code> input parameter is not
	 *            <code>null</code>, the members of the array define one or more
	 *            Property names. The returned Instance does not include
	 *            elements for any Properties missing from this list. If the
	 *            <code>pPropertyList</code> input parameter is an empty array
	 *            this signifies that no Properties are included in each
	 *            returned class. If the <code>pPropertyList</code> input
	 *            parameter is <code>null</code> this specifies that all
	 *            Properties are included in each returned class. If the
	 *            <code>pPropertyList</code> contains duplicate elements or
	 *            invalid property names, they are ignored.
	 * @return <code>CIMInstance</code> matching the input filter.
	 */
	public CIMInstance filterProperties(boolean pLocalOnly, boolean pIncludeClassOrigin,
			String[] pPropertyList) {
		StringSorter.sort(pPropertyList);
		ArrayList<CIMProperty<?>> propAList = new ArrayList<CIMProperty<?>>();
		for (int i = 0; i < getPropertyCount(); i++) {
			CIMProperty<?> prop = getProperty(i);
			if (pLocalOnly && prop.isPropagated()) continue;
			if (pPropertyList != null && !StringSorter.find(pPropertyList, prop.getName())) continue;
			propAList.add(new CIMProperty<Object>(prop.getName(), prop.getDataType(), prop
					.getValue(), prop.isKey(), prop.isPropagated(), pIncludeClassOrigin ? prop
					.getOriginClass() : null));
		}
		return new CIMInstance(this.iObjPath, propAList.toArray(EMPTY_PROP_A));
	}

	/**
	 * Get the name of the class that instantiates this CIM instance.
	 * 
	 * @return Name of class that instantiates this CIM instance.
	 */
	public String getClassName() {
		return this.iObjPath.getObjectName();
	}

	/**
	 * Get the key properties for this instance.
	 * 
	 * @return An array of key properties.
	 */
	public CIMProperty<?>[] getKeys() {
		return this.iObjPath.getKeys();
	}

	/**
	 * Returns the <code>CIMObjectPath</code> that represents this instance.
	 * 
	 * @return The <code>CIMObjectPath</code> that represents this instance.
	 */
	public CIMObjectPath getObjectPath() {
		return this.iObjPath;
	}

	/**
	 * Retrieve an array of the properties for this instance.
	 * 
	 * @return An array of the CIM properties for this instance.
	 */
	public CIMProperty<?>[] getProperties() {
		return this.iProps == null ? new CIMProperty[0] : this.iProps;
	}

	/**
	 * Get a class property by index.
	 * 
	 * @param pIndex
	 *            The index of the class property to retrieve.
	 * @return The <code>CIMProperty</code> at the specified index.
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public CIMProperty<?> getProperty(int pIndex) throws ArrayIndexOutOfBoundsException {
		return this.iProps[pIndex];
	}

	/**
	 * Returns the specified property.
	 * 
	 * @param pName
	 *            The text string for the name of the property.
	 * @return The property requested or <code>null</code> if the property does
	 *         not exist.
	 */
	public CIMProperty<?> getProperty(String pName) {
		return getProperty(pName, null);
	}

	/**
	 * Returns the specified <code>CIMProperty</code>.
	 * 
	 * @param pName
	 *            The string name of the property to get.
	 * @param pOriginClass
	 *            (Optional) The string name of the class in which the property
	 *            was defined.
	 * @return <code>null</code> if the property does not exist, otherwise
	 *         returns the CIM property.
	 */
	public CIMProperty<?> getProperty(String pName, String pOriginClass) {
		CIMProperty<?> prop = (CIMProperty<?>) CIMElementSorter.find(this.iProps, pName);
		if (prop == null) return null;
		if (pOriginClass == null) return prop;
		if (pOriginClass.equalsIgnoreCase(prop.getOriginClass())) return prop;
		return null;
	}

	/**
	 * Get the number of properties defined in this <code>CIMInstance</code>.
	 * 
	 * @return The number of properties defined in the <code>CIMInstance</code>.
	 */
	public int getPropertyCount() {
		return this.iProps == null ? 0 : this.iProps.length;
	}

	/**
	 * Returns the value of a property of this CIM Instance.
	 * 
	 * @param name
	 *            The name of the property.
	 * @return The value for the specified property name or <code>null</code> if
	 *         the property does not exist.
	 */
	public Object getPropertyValue(String name) {
		CIMProperty<?> prop = getProperty(name);
		return prop == null ? null : prop.getValue();
	}

	/**
	 * Computes the hash code for this instance. The hash code will be the
	 * object path of the instance not including the host or namespace
	 * information.
	 * 
	 * @return The integer representing the hash code for this object path.
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Returns a <code>String</code> representation of the
	 * <code>CIMInstance</code>. This method is intended to be used only for
	 * debugging purposes, and the format of the returned string may vary
	 * between implementations. The returned string may be empty but may not be
	 * <code>null</code>.
	 * 
	 * @return String representation of this instance.
	 */
	@Override
	public String toString() {
		return MOF.instanceDeclaration(this, MOF.EMPTY);
	}

	/**
	 * Indicates whether the data types of the two properties are equal.
	 * 
	 * @param pProp0
	 *            First property.
	 *@param pProp1
	 *            Second property.
	 * @return <code>true</code> if the data types are the same,
	 *         <code>false</code> otherwise.
	 */
	private static boolean typesEqual(CIMProperty<?> pProp0, CIMProperty<?> pProp1) {
		CIMDataType type0 = pProp0.getDataType(), type1 = pProp0.getDataType();
		return type0.getType() == type1.getType() && type0.isArray() == type1.isArray();

	}

}
