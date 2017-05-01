/**
 * CIMError.java
 *
 * (C) Copyright IBM Corp. 2005, 2012
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3526681    2012-05-14  blaschke-oss CIMError valid status codes out-of-date
 */
package org.sblim.cimclient.internal.wbem;

import javax.cim.CIMInstance;
import javax.wbem.WBEMException;

/**
 * Class CIMError is required for IndicationHandling and CIM-XML parsing.
 * 
 */
public class CIMError {

	private int iStatusCode;

	private String iDescription;

	private CIMInstance[] iInstances;

	/**
	 * Construct a CIMError object using the default status (CIM_ERR_FAIL).
	 */
	public CIMError() {
		this(1, "Failed.");
	}

	/**
	 * Construct a CIMError object from the given WBEMException.
	 * 
	 * @param pEx
	 */
	public CIMError(WBEMException pEx) {
		this(pEx.getID(), pEx.getMessage(), pEx.getCIMErrors());
	}

	/**
	 * Construct a CIMError object with the specified status code.
	 * 
	 * @param pStatus
	 *            The status code
	 */
	public CIMError(int pStatus) {
		this(pStatus, null);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStatus
	 * @param pMsg
	 */
	public CIMError(int pStatus, String pMsg) {
		this(pStatus, pMsg, null);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStatus
	 * @param pMsg
	 * @param pInstances
	 */
	public CIMError(int pStatus, String pMsg, CIMInstance[] pInstances) {
		this.iStatusCode = pStatus;
		this.iDescription = pMsg;
		this.iInstances = pInstances;
	}

	/**
	 * Gets the status code.
	 * 
	 * @return The status code
	 */
	public int getCode() {
		return this.iStatusCode;
	}

	/**
	 * Gets the description associated with this status.
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return this.iDescription;
	}

	/**
	 * getCIMInstances
	 * 
	 * @return CIMInstance[]
	 */
	public CIMInstance[] getCIMInstances() {
		return this.iInstances;
	}

	/**
	 * Specifies the status code.
	 * 
	 * @param pStatus
	 */
	public void setCode(int pStatus) {
		if (pStatus > WBEMException.CIM_ERR_SERVER_IS_SHUTTING_DOWN
				|| pStatus < WBEMException.CIM_ERR_FAILED) throw new IllegalArgumentException(
				"invalid error code");
		this.iStatusCode = pStatus;
	}

	/**
	 * Specifies the description associated to this status.
	 * 
	 * @param pDescription
	 */
	public void setDescription(String pDescription) {
		this.iDescription = pDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CIMError: " + ((this.iDescription != null) ? ("(" + this.iDescription + ")") : "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CIMError)) return false;

		CIMError that = (CIMError) o;

		if (this.iStatusCode == that.iStatusCode
				&& (this.iDescription == null ? that.iDescription == null : this.iDescription
						.equals(that.iDescription))) return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (this.iDescription != null ? this.iDescription.hashCode() : 0) << 16 + this.iStatusCode;
	}
}
