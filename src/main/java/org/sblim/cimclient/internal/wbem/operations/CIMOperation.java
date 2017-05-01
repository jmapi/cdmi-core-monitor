/**
 * CIMOperation.java
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
import javax.wbem.WBEMException;

/**
 * CIMOperation
 * 
 */
public abstract class CIMOperation {

	protected CIMObjectPath iObjectName;

	protected String iNameSpace;

	protected String iMethodCall;

	protected Object iResult;

	/**
	 * Returns the object name
	 * 
	 * @return The object name
	 */
	public CIMObjectPath getObjectName() {
		return this.iObjectName;
	}

	/**
	 * Returns the namespace
	 * 
	 * @return The namespace
	 */
	public String getNameSpace() {
		return this.iNameSpace;
	}

	/**
	 * Sets the namespace
	 * 
	 * @param pNamespace
	 *            The namespace
	 */
	public void setNameSpace(String pNamespace) {
		this.iNameSpace = pNamespace;
	}

	/**
	 * Returns the method call
	 * 
	 * @return The method call
	 */
	public String getMethodCall() {
		return this.iMethodCall;
	}

	/**
	 * Returns if an (uncaught) exception occurred
	 * 
	 * @return <code>true</code> if an (uncaught) exception occurred,
	 *         <code>false</code> otherwise
	 */
	public boolean isException() {
		return (this.iResult instanceof Exception);
	}

	/**
	 * Returns the result of the operation
	 * 
	 * @return The result
	 * @throws WBEMException
	 */
	public Object getResult() throws WBEMException {
		if (this.iResult instanceof WBEMException) throw (WBEMException) this.iResult;
		return this.iResult;
	}

	/**
	 * Sets the operation result
	 * 
	 * @param pResult
	 *            The result
	 */
	public void setResult(Object pResult) {
		this.iResult = pResult;
	}
}
