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
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2013628    2008-07-30  rgummada     SAXException when listing classes
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;
import javax.cim.CIMQualifier;

import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.sax.CIMObjectFactory;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT QUALIFIER ((VALUE | VALUE.ARRAY)?)
 * ATTLIST QUALIFIER 
 *  %CIMName;
 *  %CIMType;              #REQUIRED
 *  %Propagated;
 *  %QualifierFlavor;
 *  xml:lang   NMTOKEN     #IMPLIED
 * </pre>
 */
public class QualifierNode extends Node {

	private String iName;

	private CIMDataType iType;

	private boolean iPropagated;

	private int iFlavor;

	// (VALUE | VALUE.ARRAY)
	// it can be built when child is available
	private CIMQualifier<Object> iQuali;

	/**
	 * Ctor.
	 */
	public QualifierNode() {
		super(QUALIFIER);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iQuali = null;
		this.iName = getCIMName(pAttribs);
		/*
		 * non-standard CIMOM can supply the type info in the sub Node
		 */
		this.iType = getCIMType(pAttribs, true);

		this.iPropagated = getPropagated(pAttribs);
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
		if (this.iQuali != null) throw new SAXException(getNodeName()
				+ " node can have only one VALUE or VALUE.ARRAY child node!");
		if (pNodeNameEnum != VALUE && pNodeNameEnum != VALUE_ARRAY) throw new SAXException(
				pNodeNameEnum + " child node is not valid for " + getNodeName() + " node!");
	}

	@Override
	public void childParsed(Node pChild) throws SAXException {
		AbstractValueNode absValNode = (AbstractValueNode) pChild;
		Object value;
		CIMDataType type;
		if (absValNode instanceof ValueArrayNode) {
			ValueArrayNode valANode = (ValueArrayNode) absValNode;
			setType(valANode);
			// create array value
			value = CIMObjectFactory.getObject(this.iType, valANode);
			// constructs array type
			type = this.iType.isArray() ? this.iType : CIMHelper.UnboundedArrayDataType(this.iType
					.getType());
		} else if (absValNode instanceof ValueNode) {
			ValueNode valNode = (ValueNode) absValNode;
			setType(valNode);
			String valueStr = (String) valNode.getValue();
			type = this.iType;
			value = CIMObjectFactory.getObject(type, valueStr);
		} else {
			type = CIMDataType.STRING_T;
			value = null;
		}
		this.iQuali = new CIMQualifier<Object>(this.iName, type, value, this.iFlavor,
				this.iPropagated);
	}

	@Override
	public void testCompletness() {
	// child node is optional, hence commented to support
	// servers that do not implement default value to qualifier node
	/*
	 * if (iQuali == null) throw new SAXException(getNodeName() + " must have a
	 * VALUE or VALUE.ARRAY child node!");
	 */
	}

	/**
	 * getQualifier
	 * 
	 * @return CIMQualifier
	 */
	public CIMQualifier<Object> getQualifier() {
		return this.iQuali;
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

}
