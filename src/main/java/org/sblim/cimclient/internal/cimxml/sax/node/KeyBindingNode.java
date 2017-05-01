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
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;
import javax.cim.CIMProperty;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT KEYBINDING (KEYVALUE | VALUE.REFERENCE) ATTLIST KEYBINDING %CIMName;
 */
public class KeyBindingNode extends Node {

	private String iName;

	// child element
	private boolean iHasChild;

	private CIMDataType iType;

	private Object iValue;

	/**
	 * Ctor.
	 */
	public KeyBindingNode() {
		super(KEYBINDING);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iHasChild = false;
		this.iValue = null;
		this.iName = getCIMName(pAttribs);
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
		if (this.iHasChild) throw new SAXException("KEYBINDING node can only have one child node!");
		if (pNodeNameEnum != KEYVALUE && pNodeNameEnum != VALUE_REFERENCE) throw new SAXException(
				"KEYBINDING node's child node can be KEYVALUE or VALUE_REFERENCE but not "
						+ pNodeNameEnum);
	}

	@Override
	public void childParsed(Node pChild) {
		AbstractScalarValueNode abstScalarValNode = (AbstractScalarValueNode) pChild;
		this.iType = abstScalarValNode.getType();
		this.iValue = abstScalarValNode.getValue();
		this.iHasChild = true;
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasChild) throw new SAXException(
				"KEYBINDING node must have a KEYVALUE or VALUE_REFERENCE child node!");
	}

	/**
	 * getCIMProperty
	 * 
	 * @return CIMProperty
	 */
	public CIMProperty<Object> getCIMProperty() {
		// CIMProperty(
		// String name, CIMDataType type, Object value, boolean key, boolean
		// propagated, String originClass
		// )
		return new CIMProperty<Object>(this.iName, this.iType, this.iValue, true, false, null);
	}

}
