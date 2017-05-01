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
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3293248    2011-05-03  blaschke-oss Support for CIM_ERROR instances within ERROR
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMInstance;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT ERROR (INSTANCE*)
 * ATTLIST ERROR
 *   CODE CDATA #REQUIRED
 *   DESCRIPTION CDATA #IMPLIED
 * </pre>
 */
public class ErrorNode extends Node implements ErrorIf {

	private int iCode;

	private String iDesc;

	private ArrayList<CIMInstance> iCIMInstAL;

	/**
	 * Ctor.
	 */
	public ErrorNode() {
		super(ERROR);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iCIMInstAL = null;
		String code = pAttribs.getValue("CODE");
		if (code == null) throw new SAXException(getNodeName()
				+ " node must have a CODE attribute!");
		try {
			this.iCode = Integer.parseInt(code);
		} catch (NumberFormatException e) {
			throw new SAXException("Failed to parse CODE attribute in " + getNodeName() + " node!",
					e);
		}
		this.iDesc = pAttribs.getValue("DESCRIPTION");
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
		if (pNodeNameEnum != INSTANCE) throw new SAXException(getNodeName() + " node cannot have "
				+ pNodeNameEnum + " child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iCIMInstAL == null) this.iCIMInstAL = new ArrayList<CIMInstance>();
		this.iCIMInstAL.add(((InstanceNode) pChild).getCIMInstance());
	}

	@Override
	public void testCompletness() {
	// no mandatory child nodes
	}

	private static final CIMInstance[] EMPTY_IA = new CIMInstance[0];

	public CIMError getCIMError() {
		if (this.iCIMInstAL != null) { return new CIMError(this.iCode, this.iDesc, this.iCIMInstAL
				.toArray(EMPTY_IA)); }
		return new CIMError(this.iCode, this.iDesc);
	}

}
