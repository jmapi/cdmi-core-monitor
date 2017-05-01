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
 * 1679534    2007-03-13  ebak         wrong ValueObjectNode.testChild()
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMClass;
import javax.cim.CIMDataType;
import javax.cim.CIMNamedElementInterface;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT VALUE.OBJECT (CLASS | INSTANCE)
 */
public class ValueObjectNode extends AbstractScalarValueNode {

	private CIMNamedElementInterface iCIMObject;

	/**
	 * Ctor.
	 */
	public ValueObjectNode() {
		super(VALUE_OBJECT);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iCIMObject = null;
		// no attributes
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
		if (this.iCIMObject != null) throw new SAXException("This " + getNodeName()
				+ " node can have only one child but an additional " + pNodeNameEnum
				+ " node found!");
		if (pNodeNameEnum != CLASS && pNodeNameEnum != INSTANCE) throw new SAXException(
				getNodeName() + " node child node can be CLASS or INSTANCE but a " + pNodeNameEnum
						+ " node was found!");
	}

	@Override
	public void childParsed(Node pChild) {
		AbstractObjectNode objNode = (AbstractObjectNode) pChild;
		if (objNode instanceof ClassNode) this.iCIMObject = ((ClassNode) objNode).getCIMClass();
		else this.iCIMObject = ((InstanceNode) objNode).getCIMInstance();
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMObject == null) throw new SAXException(
				"VALUE.OBJECT node must have a CLASS or INSTANCE child node!");
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ValueIf#getValue()
	 * @return CIMClass or CIMInstance
	 */
	public Object getValue() {
		return this.iCIMObject;
	}

	public CIMDataType getType() {
		if (this.iCIMObject instanceof CIMClass) return CIMDataType.CLASS_T;
		return CIMDataType.OBJECT_T;
	}

}
