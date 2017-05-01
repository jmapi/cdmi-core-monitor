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
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 1742873    2007-06-25  ebak         IPv6 ready cim-client
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3194700    2011-03-01  blaschke-oss Exception thrown on extrinsic methods
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;

import org.sblim.cimclient.internal.cimxml.sax.EmbObjHandler;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT RETURNVALUE (VALUE | VALUE.REFERENCE)?
 * ATTLIST RETURNVALUE
 *   %ParamType;       #IMPLIED
 *   %EmbeddedObject;  #IMPLIED - new
 * </pre>
 */
public class ReturnValueNode extends Node implements TypedIf, ValueIf {

	private EmbObjHandler iEmbObjHandler;

	private CIMDataType iType;

	// (VALUE | VALUE.REFERENCE)
	private boolean iHasValue;

	private Object iValue;

	/**
	 * Ctor.
	 */
	public ReturnValueNode() {
		super(RETURNVALUE);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iType = null;
		this.iValue = null;
		this.iEmbObjHandler = EmbObjHandler.init(this.iEmbObjHandler, getNodeName(), pAttribs,
				pSession, null, true);
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
		if (pNodeNameEnum == VALUE || pNodeNameEnum == VALUE_REFERENCE) {
			if (this.iHasValue) throw new SAXException(getNodeName()
					+ " node can have only one child node!");
		} else throw new SAXException(pNodeNameEnum + " cannot be the child node of "
				+ getNodeName() + " node!");
	}

	@Override
	public void childParsed(Node pChild) throws SAXException {
		AbstractValueNode valNode = (AbstractValueNode) pChild;
		if (valNode instanceof ValueReferenceNode) {
			if (this.iType != null && this.iType.getType() != CIMDataType.REFERENCE) throw new SAXException(
					getNodeName() + " node's child node is VALUE.REFERENCE "
							+ "but its type based on PARAMTYPE attribute is " + this.iType + "!");
			ValueReferenceNode valRefNode = (ValueReferenceNode) valNode;
			this.iValue = valRefNode.getCIMObjectPath();
			this.iType = valRefNode.getType();
		} else { // VALUE node
			this.iEmbObjHandler.addValueNode((ValueNode) pChild);
		}
		this.iHasValue = true;
	}

	@Override
	public void testCompletness() throws SAXException {
		// DSP0203 v2.3 changed these from required to optional
		// if (!this.iHasValue) throw new SAXException(getNodeName()
		// + " node must have a VALUE or VALUE.REFERENCE child node!");
		if (this.iType == null) {
			this.iType = this.iEmbObjHandler.getType();
			this.iValue = this.iEmbObjHandler.getValue();
		}
	}

	public CIMDataType getType() {
		return this.iType;
	}

	public Object getValue() {
		return this.iValue;
	}

}
