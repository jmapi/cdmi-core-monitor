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
 * 1712656    2007-05-04  ebak         Correct type identification for SVC CIMOM
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;
import javax.cim.CIMObjectPath;
import javax.cim.CIMQualifierType;

import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.sax.CIMObjectFactory;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT QUALIFIER.DECLARATION (SCOPE?, (VALUE | VALUE.ARRAY)?)
 * ATTLIST QUALIFIER.DECLARATION 
 * 	%CIMName;               
 * 	%CIMType;               #REQUIRED
 * 	ISARRAY    (true|false) #IMPLIED
 * 	%ArraySize;
 * 	%QualifierFlavor;
 * </pre>
 */
public class QualiDeclNode extends Node implements TypedIf, ValueIf {

	// child nodes
	// SCOPE
	private boolean iHasScope;

	private int iScope;

	// VALUE
	private String iValueNodeName;

	private Object iValue;

	// attributes
	private String iName;

	private CIMDataType iType;

	private int iFlavor;

	/**
	 * Ctor.
	 */
	public QualiDeclNode() {
		super(QUALIFIER_DECLARATION);
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
		return this.iType;
	}

	/**
	 * getFlavor
	 * 
	 * @return int - CIMFlavor bitset
	 */
	public int getFlavor() {
		return this.iFlavor;
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iHasScope = false;
		this.iScope = 0;
		this.iValueNodeName = null;
		this.iValue = null;
		this.iName = getCIMName(pAttribs);
		this.iType = getCIMType(pAttribs, true);
		this.iFlavor = getQualifierFlavor(pAttribs);
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
		if (pNodeNameEnum == VALUE || pNodeNameEnum == VALUE_ARRAY) {
			if (this.iValueNodeName != null) throw new SAXException("Cannot add " + pNodeNameEnum
					+ " node, this " + getNodeName() + " node has already got a "
					+ this.iValueNodeName + " node!");
		} else if (pNodeNameEnum == SCOPE) {
			if (this.iHasScope) throw new SAXException("Cannot add " + pNodeNameEnum
					+ " node, this " + getNodeName() + " node has already got one!");
		} else throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	/**
	 * Required to handle the output XML of some non-standard CIMOMs like SVC
	 * which adds the TYPE attribute to the sub VALUE or VALUE.ARRAY XML
	 * element.
	 * 
	 * @param pTypedIf
	 * @throws SAXException
	 */
	private void setType(TypedIf pTypedIf) throws SAXException {
		if (this.iType != null) return;
		this.iType = pTypedIf.getType();
		if (this.iType == null) throw new SAXException("Unknown type for Qualifier declaration in "
				+ getNodeName() + " node!");
	}

	@Override
	public void childParsed(Node pChild) throws SAXException {
		if (pChild instanceof ScopeNode) {
			this.iHasScope = true;
			this.iScope = ((ScopeNode) pChild).getScope();
		} else {
			this.iValueNodeName = pChild.getNodeName();
			if (pChild instanceof ValueArrayNode) {
				ValueArrayNode valAChild = (ValueArrayNode) pChild;
				setType(valAChild);
				this.iValue = CIMObjectFactory.getObject(this.iType, valAChild);
				// making array type
				if (!this.iType.isArray()) this.iType = CIMHelper.UnboundedArrayDataType(this.iType
						.getType());
			} else if (pChild instanceof ValueNode) {
				ValueNode valChild = (ValueNode) pChild;
				setType(valChild);
				this.iValue = CIMObjectFactory.getObject(this.iType, valChild);
			} else {
				this.iValue = null;
			}
		}

	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iType == null) throw new SAXException("Unknown type for Qualifier declaration in "
				+ getNodeName() + " node!");
	}

	/**
	 * getCIMQualifierType
	 * 
	 * @return CIMQualifierType
	 */
	public CIMQualifierType<Object> getCIMQualifierType() {
		/*
		 * CIMQualifierType( CIMObjectPath pPath, CIMDataType pType, Object
		 * pValue, int pScope, int pFlavor )
		 */
		return new CIMQualifierType<Object>(new CIMObjectPath(null, null, null, null, this.iName,
				null), this.iType, this.iValue, this.iScope, this.iFlavor);
	}

	public Object getValue() {
		return getCIMQualifierType();
	}

}
