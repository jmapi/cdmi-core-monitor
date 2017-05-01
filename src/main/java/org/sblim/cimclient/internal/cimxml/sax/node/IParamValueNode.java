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
 * 1686000    2007-04-20  ebak         modifyInstance() missing from WBEMClient
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 *    2680    2013-10-02  blaschke-oss IPARAMVALUE parsing broken on DOM/SAX
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMArgument;
import javax.cim.CIMDataType;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * FIXME: Why hasn't it got TYPE attribute? For VALUE and VALUE.ARRAY it would
 * be necessary.
 * 
 * <pre>
 * 
 * ELEMENT IPARAMVALUE (VALUE | VALUE.ARRAY | VALUE.REFERENCE | INSTANCENAME | CLASSNAME |
 *   QUALIFIER.DECLARATION | CLASS | INSTANCE | VALUE.NAMEDINSTANCE)?
 * ATTLIST IPARAMVALUE
 *   %CIMName;
 * </pre>
 */
public class IParamValueNode extends AbstractParamValueNode {

	private String iName;

	/**
	 * child element
	 */
	private Object iValue;

	private boolean iIsArray;

	private CIMDataType iType;

	/**
	 * Ctor.
	 */
	public IParamValueNode() {
		super(IPARAMVALUE);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iValue = null;
		this.iType = null;
		this.iName = getCIMName(pAttribs);
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { VALUE, VALUE_ARRAY, VALUE_REFERENCE,
			INSTANCENAME, CLASSNAME, QUALIFIER_DECLARATION, CLASS, INSTANCE, VALUE_NAMEDINSTANCE };

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
		/*
		 * this kind of check is not so strict (ValueNode.getValue() can return
		 * null)
		 */
		if (this.iValue != null) throw new SAXException(getNodeName()
				+ " node cannot have more than one child node!");

	}

	@Override
	public void childParsed(Node pChild) {
		this.iValue = ((ValueIf) pChild).getValue();
		this.iIsArray = pChild instanceof ArrayIf;
		if (pChild instanceof TypedIf) this.iType = ((TypedIf) pChild).getType();
		else if (pChild instanceof ObjectPathIf) this.iType = CIMDataType
				.getDataType(((ObjectPathIf) pChild).getCIMObjectPath());
		else if (pChild instanceof ValueIf) this.iType = CIMDataType.getDataType(((ValueIf) pChild)
				.getValue());
	}

	@Override
	public void testCompletness() {
	// child node is optional
	}

	@Override
	public CIMArgument<Object> getCIMArgument() {
		return new CIMArgument<Object>(this.iName, getType(), this.iValue);
	}

	/**
	 * getName
	 * 
	 * @return String
	 */
	public String getName() {
		return this.iName;
	}

	public CIMDataType getType() {
		return this.iType == null ? (this.iIsArray ? CIMDataType.STRING_ARRAY_T
				: CIMDataType.STRING_T) : this.iType;
	}

	public Object getValue() {
		return this.iValue;
	}

}
