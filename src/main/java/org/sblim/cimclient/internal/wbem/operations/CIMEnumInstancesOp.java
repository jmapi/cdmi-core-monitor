/**
 * CIMEnumInstancesOp.java
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
 * CIMEnumInstancesOp
 * 
 */
public class CIMEnumInstancesOp extends CIMOperation {

	protected boolean iDeep;

	protected boolean iLocalOnly;

	protected boolean iIncludeQualifiers;

	protected boolean iIncludeClassOrigin;

	protected String[] iPropertyList;

	/**
	 * Ctor.
	 * 
	 * @param objectName
	 * @param deep
	 * @param localOnly
	 * @param includeQualifiers
	 * @param includeClassOrigin
	 * @param propertyList
	 */
	public CIMEnumInstancesOp(CIMObjectPath objectName, boolean deep, boolean localOnly,
			boolean includeQualifiers, boolean includeClassOrigin, String[] propertyList) {
		this.iMethodCall = "EnumerateInstances";
		this.iObjectName = objectName;
		this.iDeep = deep;
		this.iLocalOnly = localOnly;
		this.iIncludeClassOrigin = includeClassOrigin;
		this.iIncludeQualifiers = includeQualifiers;
		this.iPropertyList = propertyList;
	}

	/**
	 * Returns deep
	 * 
	 * @return The value of deep.
	 */
	public boolean isDeep() {
		return this.iDeep;
	}

	/**
	 * Returns includeClassOrigin
	 * 
	 * @return The value of includeClassOrigin.
	 */
	public boolean isIncludeClassOrigin() {
		return this.iIncludeClassOrigin;
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
	 * Returns localOnly
	 * 
	 * @return The value of localOnly.
	 */
	public boolean isLocalOnly() {
		return this.iLocalOnly;
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
