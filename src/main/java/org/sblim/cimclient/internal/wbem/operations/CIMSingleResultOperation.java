/**
 * CIMSingleResultOperation.java
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

package org.sblim.cimclient.internal.wbem.operations;

import java.util.Vector;

import javax.wbem.WBEMException;

/**
 * CIMSingleResultOperation
 * 
 */
abstract public class CIMSingleResultOperation extends CIMOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sblim.wbem.client.operations.CIMOperation#getResult()
	 */
	@Override
	public Object getResult() throws WBEMException {
		if (this.iResult instanceof Exception) throw (WBEMException) this.iResult;
		if (this.iResult instanceof Vector && ((Vector<?>) this.iResult).size() > 0) return ((Vector<?>) this.iResult)
				.elementAt(0);
		return null;
	}
}
