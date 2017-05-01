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
 *    2705    2013-11-11  blaschke-oss PARAMETER.ARRAY does not require TYPE attribute
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;

import org.sblim.cimclient.internal.cimxml.sax.EmbObjHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT PARAMETER.ARRAY (QUALIFIER*)
 * ATTLIST PARAMETER.ARRAY
 *   %CIMName;
 *   %CIMType;              #REQUIRED
 *   %ArraySize;
 * </pre>
 */
public class ParameterArrayNode extends AbstractParameterNode {

	private CIMDataType iType;

	private EmbObjHandler iEmbObjHandler;

	/**
	 * Ctor.
	 */
	public ParameterArrayNode() {
		super(PARAMETER_ARRAY);
	}

	@Override
	protected void specificInit(Attributes pAttribs) throws SAXException {
		if (getCIMType(pAttribs, true) == null) throw new SAXException(
				"PARAMETER.ARRAY element missing TYPE attribute!");
		this.iEmbObjHandler = EmbObjHandler.init(this.iEmbObjHandler, getNodeName(), pAttribs,
				this.iSession, this.iQualiHandler, true);
	}

	@Override
	public void testCompletness() throws SAXException {
		this.iType = this.iEmbObjHandler.getArrayType();
	}

	public CIMDataType getType() {
		return this.iType;
	}

}
