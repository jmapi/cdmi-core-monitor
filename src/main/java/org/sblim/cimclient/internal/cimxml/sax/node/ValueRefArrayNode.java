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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 * 3513349    2012-03-31  blaschke-oss TCK: CIMDataType must not accept null string
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 *    2715    2013-11-26  blaschke-oss Add VALUE.NULL support
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMDataType;
import javax.cim.CIMObjectPath;

import org.sblim.cimclient.GenericExts;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT VALUE.REFARRAY (VALUE.REFERENCE|VALUE.NULL)*
 */
public class ValueRefArrayNode extends AbstractArrayValueNode {

	private ArrayList<CIMObjectPath> iCIMObjPathAL;

	/**
	 * Ctor.
	 */
	public ValueRefArrayNode() {
		super(VALUE_REFARRAY);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iCIMObjPathAL = GenericExts.initClearArrayList(this.iCIMObjPathAL);
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
		if (pNodeNameEnum != VALUE_REFERENCE && pNodeNameEnum != VALUE_NULL) throw new SAXException(
				getNodeName()
						+ " node child node can be VALUE.REFERENCE or VALUE.NULL only while it is "
						+ pNodeNameEnum + "!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iCIMObjPathAL == null) this.iCIMObjPathAL = new ArrayList<CIMObjectPath>();
		if (pChild instanceof ValueReferenceNode) this.iCIMObjPathAL
				.add(((ValueReferenceNode) pChild).getCIMObjectPath());
		else if (pChild instanceof ValueNullNode) this.iCIMObjPathAL.add(null);
	}

	@Override
	public void testCompletness() {
	// child nodes are not mandatory
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ArrayIf#elementAt(int)
	 * @return CIMObjectPath
	 */
	public Object elementAt(int pIdx) {
		return this.iCIMObjPathAL == null ? null : this.iCIMObjPathAL.get(pIdx);
	}

	public int size() {
		return this.iCIMObjPathAL == null ? 0 : this.iCIMObjPathAL.size();
	}

	public CIMDataType getType() {
		return new CIMDataType("", 0);
	}

	private static final CIMObjectPath[] EMPTY_OPA = new CIMObjectPath[0];

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ValueIf#getValue()
	 * @return CIMObjectPath[]
	 */
	public Object getValue() {
		return size() == 0 ? null : this.iCIMObjPathAL.toArray(EMPTY_OPA);
	}

}
