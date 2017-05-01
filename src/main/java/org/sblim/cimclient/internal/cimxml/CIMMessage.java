/**
 * CIMMessage.java
 *
 * (C) Copyright IBM Corp. 2005, 2009
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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.cimclient.internal.cimxml;

import java.util.Hashtable;

import org.w3c.dom.Document;

/**
 * Class CIMMessage is used by the CIM-XML DOM parser.
 */
public class CIMMessage {

	protected Document iDoc;

	protected Hashtable<?, ?> iElements;

	protected String iCimVersion;

	protected String iDtdVersion;

	protected String iId;

	protected String iProtocolVersion;

	protected String iMethod;

	protected boolean iIsCIMExport = false;

	protected boolean iIsSimple = false;

	protected boolean iIsRequest = false;

	protected CIMMessage() { /**/}

	/**
	 * Ctor.
	 * 
	 * @param pCimVersion
	 * @param pDtdVersion
	 * @param pId
	 * @param pMethod
	 */
	public CIMMessage(String pCimVersion, String pDtdVersion, String pId, String pMethod) {
		this.iCimVersion = pCimVersion;
		this.iDtdVersion = pDtdVersion;
		this.iId = pId;
		this.iMethod = pMethod;
	}

	/**
	 * getCIMVersion
	 * 
	 * @return String
	 */
	public String getCIMVersion() {
		return this.iCimVersion;
	}

	/**
	 * getDTDVersion
	 * 
	 * @return String
	 */
	public String getDTDVersion() {
		return this.iDtdVersion;
	}

	/**
	 * isCIMOperation
	 * 
	 * @return String
	 */
	public boolean isCIMOperation() {
		return !this.iIsCIMExport;
	}

	/**
	 * isCIMExport
	 * 
	 * @return String
	 */
	public boolean isCIMExport() {
		return this.iIsCIMExport;
	}

	/**
	 * setId
	 * 
	 * @param pId
	 */
	public void setId(String pId) {
		this.iId = pId;
	}

	/**
	 * setMethod
	 * 
	 * @param pMethod
	 */
	public void setMethod(String pMethod) {

		this.iMethod = pMethod;
		this.iIsCIMExport = (pMethod.toUpperCase().endsWith("EXPREQ") || pMethod.toUpperCase()
				.endsWith("EXPRSP"));
		this.iIsRequest = (pMethod.toUpperCase().endsWith("REQ"));
		this.iIsSimple = pMethod.toUpperCase().startsWith("SIMPLE");
	}

	/**
	 * setCIMVersion
	 * 
	 * @param pCimVersion
	 */
	public void setCIMVersion(String pCimVersion) {
		this.iCimVersion = pCimVersion;
	}

	/**
	 * setDTDVersion
	 * 
	 * @param pDtdVersion
	 */
	public void setDTDVersion(String pDtdVersion) {
		this.iDtdVersion = pDtdVersion;
	}

	/**
	 * setIsRequest
	 * 
	 * @param pValue
	 */
	public void setIsRequest(boolean pValue) {
		this.iIsRequest = pValue;
	}

	/**
	 * getId
	 * 
	 * @return String
	 */
	public String getId() {
		return this.iId;
	}

	/**
	 * getProtocolVersion
	 * 
	 * @return String
	 */
	public String getProtocolVersion() {
		return this.iProtocolVersion;
	}
}
