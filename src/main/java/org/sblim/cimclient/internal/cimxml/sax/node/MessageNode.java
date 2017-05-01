/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT MESSAGE (SIMPLEREQ | MULTIREQ | SIMPLERSP | MULTIRSP | SIMPLEEXPREQ | MULTIEXPREQ |
 *    SIMPLEEXPRSP | MULTIEXPRSP)
 * ATTLIST MESSAGE
 *    ID CDATA #REQUIRED
 *    PROTOCOLVERSION CDATA #REQUIRED
 * </pre>
 */
public class MessageNode extends Node implements NonVolatileIf {

	private String iID;

	private String iProtocolVersion;

	private AbstractMessageNode iAbstractMsgNode;

	/**
	 * Ctor.
	 */
	public MessageNode() {
		super(MESSAGE);
	}

	public void addChild(Node pChild) {
		this.iAbstractMsgNode = (AbstractMessageNode) pChild;
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iID = pAttribs.getValue("ID");
		if (this.iID == null) throw new SAXException("ID attribute is mandatory for MESSAGE node!");
		this.iProtocolVersion = pAttribs.getValue("PROTOCOLVERSION");
		if (this.iProtocolVersion == null) throw new SAXException(
				"PROTOCOLVERSION attribute is mandatory for MESSAGE node!");
		this.iAbstractMsgNode = null;
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { SIMPLEREQ, MULTIREQ, SIMPLERSP, MULTIRSP,
			SIMPLEEXPREQ, MULTIEXPREQ, SIMPLEEXPRSP, MULTIEXPRSP };

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (this.iAbstractMsgNode != null) throw new SAXException(
				"MESSAGE node can have only one child node!");
		for (int i = 0; i < ALLOWED_CHILDREN.length; i++)
			if (pNodeNameEnum == ALLOWED_CHILDREN[i]) return;
		throw new SAXException("MESSAGE node cannot have " + pNodeNameEnum + " child node!");
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
	// nothing to do
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iAbstractMsgNode == null) throw new SAXException(
				"MESSAGE node must have a child node!");
	}

	/**
	 * getAbstractMessageNode
	 * 
	 * @return AbstractMessageNode
	 */
	public AbstractMessageNode getAbstractMessageNode() {
		return this.iAbstractMsgNode;
	}

}
