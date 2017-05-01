/**
 * (C) Copyright IBM Corp. 2013
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Dave Blaschke, blaschke@us.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 *    2538    2013-11-28  blaschke-oss CR14: Support new CORRELATOR element
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT CORRELATOR (VALUE)
 * ATTLIST CORRELATOR
 *   %CIMName;
 *   %CIMType; #REQUIRED
 * </pre>
 */
public class CorrelatorNode extends Node implements TypedIf, ValueIf {

	// private String iName;

	private CIMDataType iType;

	private Object iValue;

	private boolean iHasValue;

	/**
	 * Ctor.
	 */
	public CorrelatorNode() {
		super(CORRELATOR);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		/* this.iName = */getCIMName(pAttribs);
		this.iType = getCIMType(pAttribs, false);
		this.iValue = null;
		this.iHasValue = false;
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
		if (pNodeNameEnum != VALUE) throw new SAXException(pNodeNameEnum
				+ " cannot be the child node of " + getNodeName() + " node!");
		if (this.iHasValue) throw new SAXException(getNodeName()
				+ " node can have only one child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof ValueNode) this.iHasValue = true;
		this.iValue = ((ValueNode) pChild).getValue();
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasValue) throw new SAXException(getNodeName()
				+ " node must have one VALUE child node!");
	}

	public CIMDataType getType() {
		return this.iType;
	}

	public Object getValue() {
		return this.iValue;
	}

}
