/**
 * CIMRequest.java
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

import java.util.Vector;

import javax.cim.CIMObjectPath;

/**
 * Class CIMRequest is used by the CIM-XML DOM parser.
 * 
 */
public class CIMRequest extends CIMMessage {

	protected Vector<CIMRequest> iRequests = new Vector<CIMRequest>(0);

	protected Vector<Object> iParamValue = new Vector<Object>(0);

	protected String iMethodName;

	protected CIMObjectPath iPath;

	protected String iNamespace;

	/**
	 * Ctor.
	 */
	public CIMRequest() { /**/}

	/**
	 * Ctor.
	 * 
	 * @param pCimVersion
	 * @param pDtdVersion
	 * @param pId
	 * @param pMethod
	 */
	public CIMRequest(String pCimVersion, String pDtdVersion, String pId, String pMethod) {
		super(pCimVersion, pDtdVersion, pId, pMethod);
	}

	/**
	 * addParamValue
	 * 
	 * @param v
	 */
	public void addParamValue(Object v) {
		if (v instanceof Vector) this.iParamValue.addAll((Vector<?>) v);
		else this.iParamValue.add(v);
	}

	/**
	 * addRequest
	 * 
	 * @param request
	 */
	public void addRequest(CIMRequest request) {
		this.iRequests.add(request);
	}

	/**
	 * getMethodName
	 * 
	 * @return String
	 */
	public String getMethodName() {
		return this.iMethodName;
	}

	/**
	 * getNameSpace
	 * 
	 * @return String
	 */
	public String getNameSpace() {
		return this.iNamespace;
	}

	/**
	 * getObjectPath
	 * 
	 * @return String
	 */
	public CIMObjectPath getObjectPath() {
		return this.iPath;
	}

	/**
	 * getParamValue
	 * 
	 * @return String
	 */
	public Vector<Object> getParamValue() {
		return this.iParamValue;
	}

	/**
	 * setMethodName
	 * 
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.iMethodName = methodName;
	}

	/**
	 * setNameSpace
	 * 
	 * @param namespace
	 */
	public void setNameSpace(String namespace) {
		this.iNamespace = namespace;
	}

	/**
	 * setObjectPath
	 * 
	 * @param path
	 */
	public void setObjectPath(CIMObjectPath path) {
		this.iPath = path;
	}
}
