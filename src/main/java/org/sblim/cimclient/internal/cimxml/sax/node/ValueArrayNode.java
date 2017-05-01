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
 * 1735693    2007-06-12  ebak         Empty VALUE.ARRAY elements are parsed as nulls
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 *    2715    2013-11-26  blaschke-oss Add VALUE.NULL support
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMDataType;

import org.sblim.cimclient.GenericExts;
import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT VALUE.ARRAY (VALUE|VALUE.NULL)*<br>
 * For non-standard CIMOMs the TYPE and PARAMTYPE attributes are handled.
 */
public class ValueArrayNode extends AbstractArrayValueNode {

	// VALUE *
	private ArrayList<Object> iValueAL;

	private CIMDataType iType;

	/**
	 * Ctor.
	 */
	public ValueArrayNode() {
		super(VALUE_ARRAY);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iValueAL = GenericExts.initClearArrayList(this.iValueAL);
		/*
		 * For supporting non-standard CIMOMs TYPE and PARAMTYPE attributes are
		 * handled
		 */
		CIMDataType scalarType = getCIMType(pAttribs, true);
		if (scalarType == null) scalarType = getParamType(pAttribs);
		// make array type
		this.iType = scalarType == null ? null : CIMHelper.UnboundedArrayDataType(scalarType
				.getType());
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
		if (pNodeNameEnum != VALUE && pNodeNameEnum != VALUE_NULL) throw new SAXException(
				"Only VALUE and VALUE.NULL nodes can be added to VALUE.ARRAY nodes but "
						+ pNodeNameEnum + " found!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iValueAL == null) this.iValueAL = new ArrayList<Object>();
		if (pChild instanceof ValueNode) this.iValueAL.add(((ValueNode) pChild).getValue());
		else if (pChild instanceof ValueNullNode) this.iValueAL.add(null);
	}

	@Override
	public void testCompletness() {
	// Nothing to test, since it is OK if it doesn't have child node.
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ArrayIf#elementAt(int)
	 * @return String value of VALUE child node
	 */
	public Object elementAt(int pIdx) {
		return this.iValueAL.get(pIdx);
	}

	public int size() {
		return this.iValueAL == null ? 0 : this.iValueAL.size();
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.TypedIf#getType()
	 * @return usually null, because the type is unknown, but can return
	 *         non-null in case of non-standard CIMOM.
	 */
	public CIMDataType getType() {
		return this.iType;
	}

	private static final String[] EMPTY_SA = new String[0];

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ValueIf#getValue() If
	 *      the getType() returns non-null value, the container Node have to
	 *      convert the String into the corresponding Java object.
	 * @return String[]
	 */
	public Object getValue() {
		return this.iValueAL == null ? EMPTY_SA : this.iValueAL.toArray(EMPTY_SA);
	}

}
