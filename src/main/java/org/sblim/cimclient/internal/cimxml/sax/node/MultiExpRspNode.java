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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT MULTIEXPRSP (SIMPLEEXPRSP, SIMPLEEXPRSP+)
 */
public class MultiExpRspNode extends AbstractMessageNode {

	private ArrayList<Node> iSimpleExpRspAList;

	/**
	 * Ctor.
	 */
	public MultiExpRspNode() {
		super(MULTIEXPRSP);
	}

	public void addChild(Node pChild) {
		if (this.iSimpleExpRspAList == null) this.iSimpleExpRspAList = new ArrayList<Node>();
		this.iSimpleExpRspAList.add(pChild);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		if (this.iSimpleExpRspAList != null) this.iSimpleExpRspAList.clear();
		// no attributes
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
		if (pNodeNameEnum != SIMPLEEXPRSP) throw new SAXException(getNodeName()
				+ " node can have SIMPLEEXPRSP child node only! " + pNodeNameEnum
				+ " child node is invalid!");
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iSimpleExpRspAList == null || this.iSimpleExpRspAList.size() < 2) throw new SAXException(
				getNodeName() + " node must have at least two SIMPLEEXPRSP child nodes!");
	}

}
