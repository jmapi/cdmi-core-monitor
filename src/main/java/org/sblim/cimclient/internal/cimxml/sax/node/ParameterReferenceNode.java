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
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 1820763    2007-10-29  ebak         Supporting the EmbeddedInstance qualifier
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 *    2636    2013-05-08  blaschke-oss Nested embedded instances cause CIMXMLParseException
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;

import org.xml.sax.Attributes;

/**
 * <pre>
 * ELEMENT PARAMETER.REFERENCE (QUALIFIER*)
 * ATTLIST PARAMETER.REFERENCE
 *   %CIMName;
 *   %ReferenceClass;
 * </pre>
 */
public class ParameterReferenceNode extends AbstractParameterNode {

	private CIMDataType iType;

	/**
	 * Ctor.
	 */
	public ParameterReferenceNode() {
		super(PARAMETER_REFERENCE);
	}

	@Override
	protected void specificInit(Attributes pAttribs) {
		String refClass = getReferenceClass(pAttribs);
		this.iType = new CIMDataType(refClass != null ? refClass : "");
	}

	@Override
	public void testCompletness() { /* */}

	public CIMDataType getType() {
		return this.iType;
	}

}
