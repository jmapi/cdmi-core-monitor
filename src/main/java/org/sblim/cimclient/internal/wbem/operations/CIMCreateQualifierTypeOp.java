/**
 * CIMCreateQualifierTypeOp.java
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
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 */

package org.sblim.cimclient.internal.wbem.operations;

import javax.cim.CIMObjectPath;
import javax.cim.CIMQualifierType;

/**
 * CIMCreateQualifierTypeOp
 * 
 */
public class CIMCreateQualifierTypeOp extends CIMOperation {

	protected CIMQualifierType<?> iQualifierType;

	/**
	 * Ctor.
	 * 
	 * @param pObjectName
	 * @param pQualifierType
	 */
	public CIMCreateQualifierTypeOp(CIMObjectPath pObjectName, CIMQualifierType<?> pQualifierType) {
		this.iMethodCall = "CreateQualifier";
		this.iObjectName = pObjectName;
		this.iQualifierType = pQualifierType;
	}

	/**
	 * Returns the qualifier type
	 * 
	 * @return The qualifier type
	 */
	public CIMQualifierType<?> getQualifierType() {
		return this.iQualifierType;
	}

}
