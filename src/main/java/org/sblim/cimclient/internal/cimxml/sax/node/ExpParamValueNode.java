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
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 *    2687    2013-10-10  blaschke-oss ExpParamValueNode allows VALUE, (I)METHODRESPONSE children
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT EXPPARAMVALUE (INSTANCE?)
 * ATTLIST EXPPARAMVALUE
 *   %CIMName;
 * </pre>
 */
public class ExpParamValueNode extends Node implements NonVolatileIf {

	private String iName;

	private Node iChildNode;

	/**
	 * Ctor.
	 */
	public ExpParamValueNode() {
		super(EXPPARAMVALUE);
	}

	public void addChild(Node pChild) {
		this.iChildNode = pChild;
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iName = getCIMName(pAttribs);
		this.iChildNode = null;
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (this.iChildNode != null) throw new SAXException(getNodeName()
				+ " node can have only one child node!");
		if (pNodeNameEnum != INSTANCE) throw new SAXException(getNodeName() + " node cannot have "
				+ pNodeNameEnum + " child node!");
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
	// nothing to do yet
	}

	@Override
	public void testCompletness() {
	// child node is optional
	}

	/**
	 * getName
	 * 
	 * @return String
	 */
	public String getName() {
		return this.iName;
	}

}
