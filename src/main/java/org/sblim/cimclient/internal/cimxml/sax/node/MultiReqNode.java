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
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT MULTIREQ (SIMPLEREQ, SIMPLEREQ+)
 */
public class MultiReqNode extends AbstractMessageNode {

	private ArrayList<Node> iSimpleReqAList;

	/**
	 * Ctor.
	 */
	public MultiReqNode() {
		super(MULTIREQ);
	}

	public void addChild(Node pChild) {
		if (this.iSimpleReqAList == null) this.iSimpleReqAList = new ArrayList<Node>();
		this.iSimpleReqAList.add(pChild);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		if (this.iSimpleReqAList != null) this.iSimpleReqAList.clear();
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
		if (pNodeNameEnum != SIMPLEREQ) throw new SAXException(
				"MULTIREQ node can have SIMPLEREQ child nodes only! " + pNodeNameEnum
						+ " child node is invalid!");
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iSimpleReqAList == null || this.iSimpleReqAList.size() < 2) throw new SAXException(
				"MULTIREQ node must have at least 2 SIMPLEREQ child nodes!");
	}

	/**
	 * size
	 * 
	 * @return int
	 */
	public int size() {
		return this.iSimpleReqAList == null ? 0 : this.iSimpleReqAList.size();
	}

}
