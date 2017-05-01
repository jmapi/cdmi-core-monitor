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
 * 1686000    2007-04-20  ebak         modifyInstance() missing from WBEMClient
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2974884    2010-04-20  blaschke-oss Exception when attaching 2 CDRoms with invoke method
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cimxml.LocalPathBuilder;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT LOCALCLASSPATH (LOCALNAMESPACEPATH, CLASSNAME)
 */
public class LocalClassPathNode extends AbstractObjectPathNode {

	// LOCALNAMESPACEPATH
	private boolean iHasLocalNameSpacePath, iHasClassName;

	private String iNameSpaceStr;

	// CLASSNAME
	private String iClassNameStr;

	// ---
	private CIMObjectPath iLocalPath;

	/**
	 * Ctor.
	 */
	public LocalClassPathNode() {
		super(LOCALCLASSPATH);
	}

	/**
	 * @param pAttribs
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iLocalPath = pSession.getDefLocalPath();
		this.iHasLocalNameSpacePath = this.iHasClassName = false;
		this.iNameSpaceStr = this.iClassNameStr = null;
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
		if (pNodeNameEnum == LOCALNAMESPACEPATH) {
			if (this.iHasLocalNameSpacePath) throw new SAXException(
					"LOCALCLASSPATH node already has a LOCALNAMESPACEPATH child node!");
		} else if (pNodeNameEnum == CLASSNAME) {
			if (this.iHasClassName) throw new SAXException(
					"LOCALCLASSPATH node already has a CLASSNAME child node!");
		} else throw new SAXException("LOCALCLASSPATH node cannot have " + pNodeNameEnum
				+ " child node!"
				+ " It can have LOCALNAMESPACEPATH and CLASSNAME child nodes only!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof LocalNameSpacePathNode) {
			this.iNameSpaceStr = ((LocalNameSpacePathNode) pChild).getNameSpace();
			this.iHasLocalNameSpacePath = true;
		} else {
			this.iClassNameStr = ((ClassNameNode) pChild).getClassName();
			this.iHasClassName = true;
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasLocalNameSpacePath) throw new SAXException(
				"LOCALNAMESPACE child node is mandatory for LOCALCLASSPATH node!");
		if (!this.iHasClassName) throw new SAXException(
				"CLASSNAME child node is mandatory for LOCALCLASSPATH node!");
	}

	public CIMObjectPath getCIMObjectPath() {
		return LocalPathBuilder.build(this.iLocalPath, this.iClassNameStr, this.iNameSpaceStr);
	}

}
