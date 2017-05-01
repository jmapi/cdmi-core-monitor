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
 * 1565892    2006-10-09  ebak         Make SBLIM client JSR48 compliant
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 1783288    2007-09-10  ebak         CIMClass.isAssociation() not working for retrieved classes.
 * 1796339    2007-10-01  ebak         Serializable interface missing from internal componentry
 * 1963102    2008-06-26  rgummada     NullPointerException when getting qualifiers
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2620505    2009-02-24  rgummada     EmbeddedObject qualifier is missing from CIMClass
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 2975885    2010-03-24  blaschke-oss TCK: CIMXXX.hasQualifierValue(null,null) returns true
 * 3001680    2010-05-18  blaschke-oss CIMQualifierElementInterfaceImpl changes qualifiers
 * 3023095    2010-07-01  blaschke-oss CIMQualifiedElementInterfaceImpl equals/hashCode issue
 * 3154232    2011-01-13  blaschke-oss EmbeddedObject misspelled in javadoc
 */

package org.sblim.cimclient.internal.cim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.cim.CIMDataType;
import javax.cim.CIMFlavor;
import javax.cim.CIMQualifiedElementInterface;
import javax.cim.CIMQualifier;

/**
 * Class CIMQualifiedElementInterfaceImpl is responsible for implementing the
 * functionality of javax.cim.CIMQualifiedElementInterface
 */
public class CIMQualifiedElementInterfaceImpl implements CIMQualifiedElementInterface, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4533301297060752510L;

	private CIMQualifier<?>[] iQualis;

	private CIMQualifier<?>[] iLocalOnlyQualis;

	private boolean iEmbeddedObject;

	private static final CIMQualifier<Boolean> KEY = new CIMQualifier<Boolean>("Key",
			CIMDataType.BOOLEAN_T, Boolean.TRUE, CIMFlavor.DISABLEOVERRIDE);

	private static final CIMQualifier<Boolean> ASSOCIATION = new CIMQualifier<Boolean>(
			"Association", CIMDataType.BOOLEAN_T, Boolean.TRUE, CIMFlavor.DISABLEOVERRIDE);

	private static final CIMQualifier<?>[] EMPTY_QA = new CIMQualifier[0];

	/**
	 * Ctor. This constructor doesn't modify the passed qualifier list.
	 * 
	 * @param pQualifiers
	 */
	public CIMQualifiedElementInterfaceImpl(CIMQualifier<?>[] pQualifiers) {
		setQualis(pQualifiers);
	}

	/**
	 * Ctor. This constructor modifies the qualifier list according to the
	 * pIsKeyed flag.
	 * 
	 * @param pQualifiers
	 * @param pIsKeyed
	 */
	public CIMQualifiedElementInterfaceImpl(CIMQualifier<?>[] pQualifiers, boolean pIsKeyed) {
		this(pQualifiers, pIsKeyed, false);
	}

	/**
	 * Ctor. This constructor is able to not remove the EmbeddedObject
	 * qualifier. It is useful for the XML parser to parse EmbeddedObject
	 * qualified elements without values.
	 * 
	 * @param pQualifiers
	 * @param pIsKeyed
	 * @param pKeepEmbObj
	 */
	public CIMQualifiedElementInterfaceImpl(CIMQualifier<?>[] pQualifiers, boolean pIsKeyed,
			boolean pKeepEmbObj) {
		this(pQualifiers, pIsKeyed, pKeepEmbObj, false);
	}

	/**
	 * Ctor. This constructor is able to not remove the EmbeddedObject
	 * qualifier. It is useful for the XML parser to parse EmbeddedObject
	 * qualified elements without values. It also adds or removes the
	 * Association qualifier depending on the value of pIsAssociation.
	 * 
	 * @param pQualifiers
	 * @param pIsKeyed
	 * @param pKeepEmbObj
	 * @param pIsAssociation
	 */
	public CIMQualifiedElementInterfaceImpl(CIMQualifier<?>[] pQualifiers, boolean pIsKeyed,
			boolean pKeepEmbObj, boolean pIsAssociation) {
		if (pKeepEmbObj) {
			this.iQualis = (CIMQualifier[]) CIMElementSorter.sort(pQualifiers);
			this.iEmbeddedObject = CIMElementSorter.findIdx(this.iQualis, "EmbeddedObject") >= 0;
		} else {
			setQualis(pQualifiers);
		}
		setBoolQualifier(KEY, pIsKeyed);
		setBoolQualifier(ASSOCIATION, pIsAssociation);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMQualifiedElementInterfaceImpl)) return false;
		CIMQualifiedElementInterfaceImpl that = (CIMQualifiedElementInterfaceImpl) pObj;
		return Arrays.equals(getQualifiers(), that.getQualifiers());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(getQualifiers());
	}

	/**
	 * Sets the Qualifiers. If there is an EmbeddedObject qualifier,
	 * corresponding flag is set.
	 * 
	 * @param pQualiA
	 */
	private void setQualis(CIMQualifier<?>[] pQualiA) {
		pQualiA = (CIMQualifier[]) CIMElementSorter.sort(pQualiA);
		int rmIdx = CIMElementSorter.findIdx(pQualiA, "EmbeddedObject");
		if (rmIdx < 0) {
			this.iQualis = pQualiA;
			return;
		}
		this.iEmbeddedObject = true;
		this.iQualis = pQualiA;
	}

	/**
	 * if pValue is false and boolean qualifier exists it is removed.<br>
	 * if pValue is true and boolean qualifier exists, it's value is changed to
	 * true, if boolean qualifier doesn't exist it is added.
	 * 
	 * @param pQuali
	 * @param pValue
	 */
	private void setBoolQualifier(CIMQualifier<Boolean> pQuali, boolean pValue) {
		int idx = CIMElementSorter.findIdx(this.iQualis, pQuali.getName());
		if (pValue) {
			if (idx < 0) {
				// insert the qualifier
				// idx=-insertIdx-1
				// insertIdx=-idx-1;
				insertQuali(pQuali, -idx - 1);
			} else if (!pQuali.getValue().equals(this.iQualis[idx].getValue())) {
				// change the qualifier if value different
				this.iQualis[idx] = pQuali;
			}
		} else {
			if (idx > 0) {
				// remove the existing qualifier
				removeQuali(idx);
			}
		}
	}

	private void insertQuali(CIMQualifier<Boolean> pQuali, int idx) {
		int origLength = this.iQualis == null ? 0 : this.iQualis.length;
		CIMQualifier<?>[] qualis = new CIMQualifier[origLength + 1];
		int srcIdx = 0, dstIdx = 0;
		while (srcIdx < idx)
			qualis[dstIdx++] = this.iQualis[srcIdx++];
		qualis[dstIdx++] = pQuali;
		while (srcIdx < origLength)
			qualis[dstIdx++] = this.iQualis[srcIdx++];
		this.iQualis = qualis;
	}

	private void removeQuali(int idx) {
		CIMQualifier<?>[] qualis = new CIMQualifier[this.iQualis.length - 1];
		int srcIdx = 0, dstIdx = 0;
		while (srcIdx < idx)
			qualis[dstIdx++] = this.iQualis[srcIdx++];
		++srcIdx;
		while (srcIdx < this.iQualis.length)
			qualis[dstIdx++] = this.iQualis[srcIdx++];
		this.iQualis = qualis;
	}

	/*
	 * CIMQualifier( "Key", CIMDataType.BOOLEAN_T, new Boolean(true),
	 * CIMFlavor.DISABLEOVERRIDE );
	 */

	/**
	 * Returns true if the "key" Qualifier with true value presents.
	 * 
	 * @return true/false
	 */
	public boolean isKeyed() {
		return hasQualifierValue("key", Boolean.TRUE);
	}

	/**
	 * Returns true if the "EmbeddedObject" qualifier with true value presents.
	 * 
	 * @return true/false
	 */
	public boolean isEmbeddedObject() {
		return this.iEmbeddedObject;
	}

	/**
	 * @see javax.cim.CIMQualifiedElementInterface#getQualifier(int)
	 */
	public CIMQualifier<?> getQualifier(int pIndex) {
		return this.iQualis[pIndex];
	}

	/**
	 * @see javax.cim.CIMQualifiedElementInterface#getQualifier(java.lang.String)
	 */
	public CIMQualifier<?> getQualifier(String pName) {
		return (CIMQualifier<?>) CIMElementSorter.find(this.iQualis, pName);
	}

	/**
	 * @see javax.cim.CIMQualifiedElementInterface#getQualifierCount()
	 */
	public int getQualifierCount() {
		return this.iQualis == null ? 0 : this.iQualis.length;
	}

	/**
	 * @see javax.cim.CIMQualifiedElementInterface#getQualifierValue(java.lang.String)
	 */
	public Object getQualifierValue(String pName) {
		CIMQualifier<?> quali = getQualifier(pName);
		if (quali == null) return null;
		return quali.getValue();
	}

	/**
	 * @see javax.cim.CIMQualifiedElementInterface#getQualifiers()
	 */
	public CIMQualifier<?>[] getQualifiers() {
		return getQualifiers(false);
	}

	/**
	 * getQualifiers - helps filtering based on the propagated flag.
	 * 
	 * @param pLocalOnly
	 * @return CIMQualifier[]
	 */
	public CIMQualifier<?>[] getQualifiers(boolean pLocalOnly) {
		if (this.iQualis == null) return EMPTY_QA;
		if (!pLocalOnly) return this.iQualis;
		if (this.iLocalOnlyQualis == null) {
			ArrayList<CIMQualifier<?>> qualiL = new ArrayList<CIMQualifier<?>>(this.iQualis.length);
			for (int i = 0; i < this.iQualis.length; i++)
				if (!this.iQualis[i].isPropagated()) qualiL.add(this.iQualis[i]);
			this.iLocalOnlyQualis = qualiL.toArray(new CIMQualifier[qualiL.size()]);
		}
		return this.iLocalOnlyQualis;
	}

	/**
	 * @see javax.cim.CIMQualifiedElementInterface#hasQualifier(java.lang.String)
	 */
	public boolean hasQualifier(String pName) {
		return getQualifier(pName) != null;
	}

	/**
	 * @see javax.cim.CIMQualifiedElementInterface#hasQualifierValue(java.lang.String,
	 *      java.lang.Object)
	 */
	public boolean hasQualifierValue(String pName, Object pValue) {
		if (!hasQualifier(pName)) return false;
		Object value = getQualifierValue(pName);
		return value == null ? pValue == null : value.equals(pValue);
	}

}
