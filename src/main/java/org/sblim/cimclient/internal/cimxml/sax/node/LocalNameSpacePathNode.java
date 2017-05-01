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
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 *    2711    2013-11-13  blaschke-oss LOCALNAMESPACEPATH allows 0 NAMESPACE children
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT LOCALNAMESPACEPATH (NAMESPACE+)
 */
public class LocalNameSpacePathNode extends AbstractPathNode {

	private StringBuffer iNameSpaceStrBuf;

	private String iNameSpaceStr;

	private CIMObjectPath iNameSpacePath;

	private CIMObjectPath iLocalPath;

	/**
	 * Ctor.
	 */
	public LocalNameSpacePathNode() {
		super(LOCALNAMESPACEPATH);
	}

	/**
	 * @param pAttribs
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iLocalPath = pSession.getDefLocalPath();
		this.iNameSpaceStrBuf = null;
		this.iNameSpaceStr = null;
		this.iNameSpacePath = null;
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
		if (pNodeNameEnum != NAMESPACE) throw new SAXException(getNodeName()
				+ " node can have NAMESPACE child node only! " + pNodeNameEnum
				+ " child node is invalid!");
	}

	@Override
	public void childParsed(Node pChild) {
		String nsStr = ((NameSpaceNode) pChild).getNameSpace();
		if (this.iNameSpaceStrBuf == null) {
			this.iNameSpaceStrBuf = new StringBuffer(nsStr);
		} else {
			this.iNameSpaceStrBuf.append('/' + nsStr);
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iNameSpaceStrBuf == null) {
			if (WBEMConfiguration.getGlobalConfiguration().allowEmptyLocalNameSpacePath()
					&& this.iLocalPath != null && this.iLocalPath.getNamespace() != null) return;
			throw new SAXException(getNodeName()
					+ " node must have at least one NAMESPACE child node!");
		}
	}

	/**
	 * getNameSpace
	 * 
	 * @return String
	 */
	public String getNameSpace() {
		if (this.iNameSpaceStr != null) return this.iNameSpaceStr;
		return this.iNameSpaceStr = (this.iNameSpaceStrBuf == null ? this.iLocalPath.getNamespace()
				: this.iNameSpaceStrBuf.toString());
	}

	public CIMObjectPath getCIMObjectPath() {
		if (this.iNameSpacePath != null) return this.iNameSpacePath;
		return this.iNameSpacePath = new CIMObjectPath(null, null, null, getNameSpace(), null, null);
	}

}
