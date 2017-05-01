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
 * 1714878    2007-05-08  ebak         Empty string property values are parsed as nulls
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 *    2701    2013-11-07  blaschke-oss PROPERTY.ARRAY does not require TYPE attribute
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;
import javax.cim.CIMQualifier;

import org.sblim.cimclient.internal.cimxml.sax.EmbObjHandler;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT PROPERTY.ARRAY (QUALIFIER*, VALUE.ARRAY?)
 * ATTLIST PROPERTY.ARRAY 
 *   %CIMName;
 *   %CIMType;              #REQUIRED
 *   %ArraySize;
 *   %ClassOrigin;
 *   %Propagated;
 *   %EmbeddedObject;    	#IMPLIED - new
 *   xml:lang   NMTOKEN     #IMPLIED
 * </pre>
 */
public class PropertyArrayNode extends AbstractPropertyNode {

	// VALUE.ARRAY
	// required for super.testChild()
	private boolean iHasValueArray;

	private boolean iHasTypeAttribute;

	private CIMDataType iType;

	private Object iValue;

	private EmbObjHandler iEmbObjHandler;

	/**
	 * Ctor.
	 */
	public PropertyArrayNode() {
		super(PROPERTY_ARRAY);
	}

	@Override
	protected void specificInit(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iHasTypeAttribute = (getCIMType(pAttribs, true) != null);
		this.iEmbObjHandler = EmbObjHandler.init(this.iEmbObjHandler, getNodeName(), pAttribs,
				pSession, this.iQualiHandler, true);
		this.iHasValueArray = false;
	}

	@Override
	protected void childValueNodeParsed(Node pChild) throws SAXException {
		if (!this.iHasTypeAttribute && ((ValueArrayNode) pChild).getType() == null) throw new SAXException(
				"PROPERTY.ARRAY element missing TYPE attribute!");
		this.iEmbObjHandler.addValueNode((ValueArrayNode) pChild);
		this.iHasValueArray = true;
	}

	@Override
	public void testCompletness() throws SAXException {
		/*
		 * Value and type conversion are placed here. It can throw Exception.
		 */
		this.iType = this.iEmbObjHandler.getArrayType();
		this.iValue = this.iEmbObjHandler.getValue();
	}

	public CIMDataType getType() {
		return this.iType;
	}

	@Override
	protected String getChildValueNodeNameEnum() {
		return VALUE_ARRAY;
	}

	public Object getValue() {
		// if iHasValueArray is false iObjA is null, since nulled in init()
		return this.iValue;
	}

	@Override
	protected boolean hasValueNode() {
		return this.iHasValueArray;
	}

	@Override
	protected CIMQualifier<?>[] getQualis() {
		return this.iQualiHandler.getQualis(this.iType == CIMDataType.STRING_ARRAY_T);
	}

}
