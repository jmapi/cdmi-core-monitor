/**
 * CIMSetInstanceOp.java
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

import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;

/**
 * CIMSetInstanceOp
 * 
 */
public class CIMSetInstanceOp extends CIMOperation {

	protected CIMInstance iInstance;

	protected boolean iIncludeQualifiers;

	protected String[] iPropertyList;

	/**
	 * Ctor.
	 * 
	 * @param objectName
	 * @param instance
	 * @param includeQualifiers
	 * @param propertyList
	 */
	public CIMSetInstanceOp(CIMObjectPath objectName, CIMInstance instance,
			boolean includeQualifiers, String[] propertyList) {
		this.iMethodCall = "SetInstance";
		this.iObjectName = objectName;
		this.iInstance = instance;
		this.iIncludeQualifiers = includeQualifiers;
		this.iPropertyList = propertyList;
	}

	/**
	 * Returns includeQualifiers
	 * 
	 * @return The value of includeQualifiers.
	 */
	public boolean isIncludeQualifiers() {
		return this.iIncludeQualifiers;
	}

	/**
	 * Returns instance
	 * 
	 * @return The value of instance.
	 */
	public CIMInstance getInstance() {
		return this.iInstance;
	}

	/**
	 * Returns propertyList
	 * 
	 * @return The value of propertyList.
	 */
	public String[] getPropertyList() {
		return this.iPropertyList;
	}

}
