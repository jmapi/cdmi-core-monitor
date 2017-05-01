/**
 * (C) Copyright IBM Corp. 2006, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, ebak@de.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.cimxml;

import javax.cim.CIMDataType;

/**
 * Class TypedValue is required for the CIM-XML DOM parser.
 * 
 */
public class TypedValue {

	private CIMDataType iType;

	private Object iValue;

	/**
	 * Ctor.
	 * 
	 * @param pType
	 * @param pValue
	 */
	public TypedValue(CIMDataType pType, Object pValue) {
		this.iType = pType;
		this.iValue = pValue;
	}

	/**
	 * getType
	 * 
	 * @return CIMDataType
	 */
	public CIMDataType getType() {
		return this.iType;
	}

	/**
	 * getValue
	 * 
	 * @return Object
	 */
	public Object getValue() {
		return this.iValue;
	}

}
