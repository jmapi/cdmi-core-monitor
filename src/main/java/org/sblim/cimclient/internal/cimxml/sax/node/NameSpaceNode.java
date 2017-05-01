/**
 * (C) Copyright IBM Corp. 2006, 2013
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
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 *    2673    2013-09-26  blaschke-oss NameSpaceNode does not need testCompletness()
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT NAMESPACE EMPTY ATTRLIST NAMESPACE %CIMName;
 * 
 */
public class NameSpaceNode extends Node {

	private String iNameSpaceValue;

	/**
	 * Ctor.
	 */
	public NameSpaceNode() {
		super(NAMESPACE);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iNameSpaceValue = getCIMName(pAttribs);
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	/**
	 * @param pNodeNameEnum
	 */
	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		throw new SAXException("NAMESPACE node cannot have any child node!");
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
	// no child
	}

	@Override
	public void testCompletness() {
	// no child
	}

	/**
	 * getNameSpace
	 * 
	 * @return String
	 */
	public String getNameSpace() {
		return this.iNameSpaceValue;
	}

}
