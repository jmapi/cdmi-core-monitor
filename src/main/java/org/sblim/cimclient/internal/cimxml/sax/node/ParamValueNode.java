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
 * 1719991    2007-05-16  ebak         FVT: regression ClassCastException in EmbObjHandler
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 *    2605    2013-03-20  buccella     SAX parser throws wrong exception
 *    2537    2013-10-17  blaschke-oss Add new data types for PARAMVALUE
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMArgument;
import javax.cim.CIMDataType;

import org.sblim.cimclient.internal.cimxml.sax.EmbObjHandler;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT PARAMVALUE (VALUE | VALUE.REFERENCE | VALUE.ARRAY | VALUE.REFARRAY |
 * CLASSNAME | INSTANCENAME | CLASS | INSTANCE | VALUE.NAMEDINSTANCE)? ATTLIST
 * PARAMVALUE %CIMName; %ParamType; #IMPLIED %EmbeddedObject; #IMPLIED - new
 */
public class ParamValueNode extends AbstractParamValueNode {

	private String iName;

	private EmbObjHandler iEmbObjHandler;

	private CIMDataType iType;

	// VALUE.xxx node
	private boolean iHasChild;

	private boolean iHasTypeValue;

	private Object iValue;

	/**
	 * Ctor.
	 */
	public ParamValueNode() {
		super(PARAMVALUE);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iEmbObjHandler = EmbObjHandler.init(this.iEmbObjHandler, getNodeName(), pAttribs,
				pSession, null, true);
		this.iHasChild = false;
		this.iHasTypeValue = false;
		this.iName = getCIMName(pAttribs);
		this.iType = null;
		this.iValue = null;
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { VALUE, VALUE_REFERENCE, VALUE_ARRAY,
			VALUE_REFARRAY, CLASSNAME, INSTANCENAME, CLASS, INSTANCE, VALUE_NAMEDINSTANCE };

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		boolean allowed = false;
		for (int i = 0; i < ALLOWED_CHILDREN.length; i++) {
			if (ALLOWED_CHILDREN[i] == pNodeNameEnum) {
				allowed = true;
				break;
			}
		}
		if (!allowed) throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
		if (this.iHasChild) throw new SAXException(getNodeName()
				+ " node cannot have more than one child node!");
		// type check
		CIMDataType rawType = this.iEmbObjHandler.getRawType();
		if (rawType != null) {
			if (pNodeNameEnum == VALUE_REFERENCE || pNodeNameEnum == VALUE_REFARRAY) {
				if (rawType.getType() != CIMDataType.REFERENCE) throw new SAXException(
						"PARAMVALUE node's PARAMTYPE attribute is not reference (" + rawType
								+ "), but a " + pNodeNameEnum + " child node is found!");
			}
		}
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof AbstractValueNode) {
			this.iEmbObjHandler.addValueNode((AbstractValueNode) pChild);
		} else {
			this.iValue = ((ValueIf) pChild).getValue();
			if (pChild instanceof TypedIf) this.iType = ((TypedIf) pChild).getType();
			else if (pChild instanceof ObjectPathIf) this.iType = CIMDataType
					.getDataType(((ObjectPathIf) pChild).getCIMObjectPath());
			else if (pChild instanceof ValueIf) this.iType = CIMDataType
					.getDataType(((ValueIf) pChild).getValue());
			this.iHasTypeValue = true;
		}
		this.iHasChild = true;
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasTypeValue) {
			// here is a type and value conversion
			this.iType = this.iEmbObjHandler.getType();
			this.iValue = this.iEmbObjHandler.getValue();
		}
	}

	public CIMDataType getType() {
		return this.iType;
	}

	/**
	 * getCIMArgument
	 * 
	 * @return CIMArgument
	 */
	@Override
	public CIMArgument<Object> getCIMArgument() {
		/*
		 * CIMArgument(String name, CIMDataType type, Object value)
		 */
		return new CIMArgument<Object>(this.iName, this.iType, this.iValue);
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ValueIf#getValue()
	 */
	public Object getValue() {
		return this.iValue;
	}

}
