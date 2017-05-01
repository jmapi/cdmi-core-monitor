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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 *    2636    2013-05-08  blaschke-oss Nested embedded instances cause CIMXMLParseException
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;

/**
 * <pre>
 * 
 * ELEMENT PROPERTY.REFERENCE (QUALIFIER*, (VALUE.REFERENCE)?)
 * ATTLIST PROPERTY.REFERENCE
 *   %CIMName; 
 *   %ReferenceClass; 
 *   %ClassOrigin;
 *   %Propagated;
 * </pre>
 */
public class PropertyReferenceNode extends AbstractPropertyNode {

	private String iRefClassName;

	// VALUE.REFERENCE
	private boolean iHasValueRef;

	private Object iValue;

	/**
	 * Ctor.
	 */
	public PropertyReferenceNode() {
		super(PROPERTY_REFERENCE);
	}

	@Override
	protected void childValueNodeParsed(Node pChild) {
		this.iValue = ((ValueReferenceNode) pChild).getValue();
		this.iHasValueRef = true;
	}

	@Override
	protected String getChildValueNodeNameEnum() {
		return VALUE_REFERENCE;
	}

	public Object getValue() {
		return this.iHasValueRef ? this.iValue : null;
	}

	@Override
	protected boolean hasValueNode() {
		return this.iHasValueRef;
	}

	/**
	 * @param pSession
	 */
	@Override
	protected void specificInit(Attributes pAttribs, SAXSession pSession) {
		this.iHasValueRef = false;
		this.iRefClassName = getReferenceClass(pAttribs);
	}

	@Override
	public void testCompletness() {
	//
	}

	public CIMDataType getType() {
		return new CIMDataType(this.iRefClassName != null ? this.iRefClassName : "");
	}

}
