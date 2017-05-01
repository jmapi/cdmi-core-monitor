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
 *    2677    2013-09-30  blaschke-oss ObjectPathNode allows all child nodes
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT OBJECTPATH (INSTANCEPATH | CLASSPATH)
 */
public class ObjectPathNode extends AbstractPathNode {

	// (INSTANCEPATH | CLASSPATH)
	private CIMObjectPath iObjPath;

	// private AbstractObjectPathNode iChildNode;

	/**
	 * Ctor.
	 */
	public ObjectPathNode() {
		super(OBJECTPATH);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iObjPath = null;
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
		if (this.iObjPath != null) throw new SAXException(getNodeName()
				+ " node can have only one INSTANCEPATH or CLASSPATH child node!" + " Additional "
				+ pNodeNameEnum + " child node is invalid!");
		if (pNodeNameEnum != CLASSPATH && pNodeNameEnum != INSTANCEPATH) throw new SAXException(
				getNodeName() + " node child node can be CLASSPATH or INSTANCEPATH but a "
						+ pNodeNameEnum + " node was found!");
	}

	@Override
	public void childParsed(Node pChild) {
		this.iObjPath = ((AbstractObjectPathNode) pChild).getCIMObjectPath();
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iObjPath == null) throw new SAXException(getNodeName()
				+ " node must have a INSTANCEPATH or CLASSPATH child node!");
	}

	public CIMObjectPath getCIMObjectPath() {
		return this.iObjPath;
	}

}
