/**
 * CIMExecQueryOp.java
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
 */

package org.sblim.cimclient.internal.wbem.operations;

import javax.cim.CIMObjectPath;

/**
 * CIMExecQueryOp
 * 
 */
public class CIMExecQueryOp extends CIMOperation {

	protected String iQuery;

	protected String iQueryLanguage;

	/**
	 * Ctor.
	 * 
	 * @param objectName
	 * @param query
	 * @param queryLanguage
	 */
	public CIMExecQueryOp(CIMObjectPath objectName, String query, String queryLanguage) {
		this.iMethodCall = "ExecQuery";
		this.iObjectName = objectName;
		this.iQuery = query;
		this.iQueryLanguage = queryLanguage;
	}

	/**
	 * Returns query
	 * 
	 * @return The value of query.
	 */
	public String getQuery() {
		return this.iQuery;
	}

	/**
	 * Returns queryLanguage
	 * 
	 * @return The value of queryLanguage.
	 */
	public String getQueryLanguage() {
		return this.iQueryLanguage;
	}

}
