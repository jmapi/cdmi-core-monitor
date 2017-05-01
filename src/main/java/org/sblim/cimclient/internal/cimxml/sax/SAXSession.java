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
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1848607    2007-12-11  ebak         Strict EmbeddedObject types
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.cimxml.sax;

import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.util.WBEMConfiguration;

/**
 * This class is intended to store variables which are common for a whole
 * CIM-XML SAX parsing session.
 */
public class SAXSession {

	private CIMObjectPath iDefLocalPath;

	private boolean iStrictEmbObjParsing = WBEMConfiguration.getGlobalConfiguration()
			.strictEmbObjTypes();

	/**
	 * Ctor.
	 * 
	 * @param pDefLocalPath
	 */
	public SAXSession(CIMObjectPath pDefLocalPath) {
		this.iDefLocalPath = pDefLocalPath;
	}

	/**
	 * getDefLocalPath
	 * 
	 * @return CIMObjectPath
	 */
	public CIMObjectPath getDefLocalPath() {
		return this.iDefLocalPath;
	}

	/**
	 * strictEmbObjParsing
	 * 
	 * @return boolean
	 */
	public boolean strictEmbObjParsing() {
		return this.iStrictEmbObjParsing;
	}

}
