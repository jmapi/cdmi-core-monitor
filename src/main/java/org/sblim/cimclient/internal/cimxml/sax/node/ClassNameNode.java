/**
 * (C) Copyright IBM Corp. 2006, 2009
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
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cimxml.LocalPathBuilder;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT CLASSNAME EMPTY
 *   ATTLIST CLASSNAME
 *   %CIMName;
 * </pre>
 */
public class ClassNameNode extends AbstractPathNode {

	private String iClassName;

	private CIMObjectPath iLocalPath;

	/**
	 * Ctor.
	 */
	public ClassNameNode() {
		super(CLASSNAME);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iLocalPath = pSession.getDefLocalPath();
		this.iClassName = getCIMName(pAttribs);
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	/**
	 * @param pNodeNameEnum
	 */
	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		throw new SAXException("CLASSNAME node cannot have any child node!");
	}

	@Override
	public void testCompletness() {
	// no child nodes to test
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
	// nothing to do
	}

	/**
	 * getClassName
	 * 
	 * @return String
	 */
	public String getClassName() {
		return this.iClassName;
	}

	public CIMObjectPath getCIMObjectPath() {
		return LocalPathBuilder.build(this.iLocalPath, this.iClassName, null);
	}

}
