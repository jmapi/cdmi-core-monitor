/**
 * CIMGetClassOp.java
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
 * 3525150    2012-05-09  blaschke-oss Remove CIMGetClassOp.getPropertyLis
 */

package org.sblim.cimclient.internal.wbem.operations;

import javax.cim.CIMObjectPath;

/**
 * CIMGetClassOp
 * 
 */
public class CIMGetClassOp extends CIMSingleResultOperation {

	protected boolean iLocalOnly;

	protected boolean iIncludeQualifiers;

	protected boolean iIncludeClassOrigin;

	protected String[] iPropertyList;

	/**
	 * Ctor.
	 * 
	 * @param pObjectName
	 * @param pLocalOnly
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 */
	public CIMGetClassOp(CIMObjectPath pObjectName, boolean pLocalOnly, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin, String[] pPropertyList) {

		this.iMethodCall = "GetClass";
		this.iObjectName = pObjectName;
		this.iLocalOnly = pLocalOnly;
		this.iIncludeClassOrigin = pIncludeClassOrigin;
		this.iIncludeQualifiers = pIncludeQualifiers;
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
