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
 * @author : Alexander Wolf-Reber, a.wolf-reber@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-09-26  lupusalex    Make SBLIM client JSR48 compliant
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2227442 	  2008-11-05  blaschke-oss Add missing serialVersionUID
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 3410126    2011-09-15  blaschke-oss TCK: CIM element name cannot be null
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

import java.io.Serializable;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * <code>CIMElement</code> is an abstract base class that represents a CIM
 * Element as defined by the Distributed Management Task Force (<a
 * href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>).
 */
public abstract class CIMElement extends Object implements Serializable, Comparable<CIMElement> {

	private static final long serialVersionUID = -3310480832682118922L;

	private String iName;

	/**
	 * Creates a new CIM element with the given name.
	 * 
	 * @param pName
	 *            The string for the name of the element.
	 * @throws IllegalArgumentException
	 *             If name is <code>null</code>.
	 */
	public CIMElement(String pName) {
		if (pName == null) throw new IllegalArgumentException("CIM element name cannot be null");
		this.iName = pName;
	}

	/**
	 * Compares this element name to the CIMElement passed in.
	 * 
	 * @param pObj
	 *            The CIMElement to be compared.
	 * @return A negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
	public int compareTo(CIMElement pObj) {
		if (pObj == null) return -1;
		CIMElement that = pObj;
		return this.iName.compareToIgnoreCase(that.iName);
	}

	/**
	 * Takes a CIM element and returns <code>true</code> if it is equal to this
	 * CIM element. Otherwise, it returns <code>false</code>. Useful for
	 * comparing two CIM elements, for example, to determine whether a CIM
	 * element exists in a Collection.
	 * 
	 * @param pObj
	 *            The object to be compared a CIM element.
	 * @return <code>true</code> indicates the specified CIM element equals this
	 *         CIM element, <code>false</code> indicates the CIM element does
	 *         not equal this CIM element.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (pObj instanceof CIMElement) { return this.iName
				.equalsIgnoreCase(((CIMElement) pObj).iName); }
		return false;
	}

	/**
	 * Returns a string representing the name of a CIM element instance.
	 * 
	 * @return The name of this CIM element.
	 */
	public String getName() {
		return this.iName;
	}

	/**
	 * Returns a hash code value for the CIM element. This method is supported
	 * for the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return A hash code value for this CIM element.
	 */
	@Override
	public int hashCode() {
		return this.iName.hashCode();
	}

	/**
	 * Returns a <code>String</code> representation of the
	 * <code>CIMElement</code>. This method is intended to be used only for
	 * debugging purposes. The format of the returned string may vary between
	 * implementations. The returned string may be empty but may not be
	 * <code>null</code>
	 * 
	 * @return <code>String</code> representation of this CIM element.
	 */
	@Override
	public String toString() {
		return this.iName;
	}
}
