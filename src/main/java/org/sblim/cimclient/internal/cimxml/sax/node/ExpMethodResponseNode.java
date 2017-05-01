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
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT EXPMETHODRESPONSE (ERROR|IRETURNVALUE?)
 * ATTLIST EXPMETHODRESPONSE
 *   %CIMName;
 * </pre>
 */
public class ExpMethodResponseNode extends Node implements ErrorIf, RetValPipeIf, NonVolatileIf {

	private String iName;

	private ErrorNode iErrorNode;

	private IReturnValueNode iRetValNode;

	/**
	 * Ctor.
	 */
	public ExpMethodResponseNode() {
		super(EXPMETHODRESPONSE);
	}

	public void addChild(Node pChild) {
		if (pChild instanceof ErrorNode) this.iErrorNode = (ErrorNode) pChild;
		else this.iRetValNode = (IReturnValueNode) pChild;
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iName = getCIMName(pAttribs);
		this.iErrorNode = null;
		this.iRetValNode = null;
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
		if (pNodeNameEnum == ERROR || pNodeNameEnum == IRETURNVALUE) {
			Node node;
			if (this.iErrorNode != null) node = this.iErrorNode;
			else if (this.iRetValNode != null) node = this.iRetValNode;
			else node = null;
			if (node != null) throw new SAXException(pNodeNameEnum + " child node is invalid for "
					+ getNodeName() + " node, since it already has a " + node.getNodeName()
					+ " child node!");
		} else throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
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
	// no mandatory child nodes
	}

	public CIMError getCIMError() {
		return this.iErrorNode == null ? null : this.iErrorNode.getCIMError();
	}

	public int getReturnValueCount() {
		return this.iRetValNode == null ? 0 : this.iRetValNode.getReturnValueCount();
	}

	public Object readReturnValue() {
		return this.iRetValNode.readReturnValue();
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
