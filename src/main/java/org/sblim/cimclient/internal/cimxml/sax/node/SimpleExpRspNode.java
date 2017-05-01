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
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMArgument;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT SIMPLEEXPRSP (EXPMETHODRESPONSE)
 * ELEMENT EXPMETHODRESPONSE (ERROR|IRETURNVALUE?)
 * </pre>
 */
public class SimpleExpRspNode extends AbstractSimpleRspNode {

	private ExpMethodResponseNode iExpMethodRspNode;

	/**
	 * Ctor.
	 */
	public SimpleExpRspNode() {
		super(SIMPLEEXPRSP);
	}

	public void addChild(Node pChild) {
		this.iExpMethodRspNode = (ExpMethodResponseNode) pChild;
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iExpMethodRspNode = null;
		// no attribs
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
		if (this.iExpMethodRspNode != null) throw new SAXException(getNodeName()
				+ " node can have only one child node!");
		if (pNodeNameEnum != EXPMETHODRESPONSE) throw new SAXException(getNodeName()
				+ " node's child node can be EXPMETHODRESPONSE only! " + pNodeNameEnum
				+ " is invalid!");
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iExpMethodRspNode == null) throw new SAXException(getNodeName()
				+ " node must have an EXPMETHODRESPONSE child node!");
	}

	@Override
	public CIMError getCIMError() {
		// can be null if it's value was read out before
		if (this.iExpMethodRspNode == null) return null;
		return this.iExpMethodRspNode.getCIMError();
	}

	@Override
	public CIMArgument<?>[] getCIMArguments() {
		// no out arguments
		return null;
	}

	public int getReturnValueCount() {
		return this.iExpMethodRspNode == null ? 0 : this.iExpMethodRspNode.getReturnValueCount();
	}

	public Object readReturnValue() {
		return this.iExpMethodRspNode == null ? null : this.iExpMethodRspNode.readReturnValue();
	}

}
