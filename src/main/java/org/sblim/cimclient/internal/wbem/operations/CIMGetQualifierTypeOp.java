/**
 * CIMGetQualifierTypeOp.java
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
 * CIMGetQualifierTypeOp
 * 
 */
public class CIMGetQualifierTypeOp extends CIMSingleResultOperation {

	protected String iQualifierType;

	/**
	 * Ctor.
	 * 
	 * @param pObjectName
	 * @param pQualifierType
	 */
	public CIMGetQualifierTypeOp(CIMObjectPath pObjectName, String pQualifierType) {
		this.iMethodCall = "GetQualifierType";
		this.iObjectName = pObjectName;
		this.iQualifierType = pQualifierType;
	}

	/**
	 * Returns qualifierType
	 * 
	 * @return The value of qualifierType.
	 */
	public String getQualifierType() {
		return this.iQualifierType;
	}

}
