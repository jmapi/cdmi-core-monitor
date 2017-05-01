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
 * 1742873    2007-06-25  ebak         IPv6 ready cim-client
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.util.XMLHostStr;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT CLASSPATH (NAMESPACEPATH, CLASSNAME)
 */
public class ClassPathNode extends AbstractObjectPathNode {

	// child nodes:
	private boolean iHasNameSpacePath, iHasClassName;

	private String iLocalNameSpacePathStr;

	private XMLHostStr iHostStr;

	private String iClassNameStr;

	/**
	 * Ctor.
	 */
	public ClassPathNode() {
		super(CLASSPATH);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		// reset
		this.iHasNameSpacePath = this.iHasClassName = false;
		this.iLocalNameSpacePathStr = this.iClassNameStr = null;
		this.iHostStr = new XMLHostStr();
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
		if (pNodeNameEnum == NAMESPACEPATH) {
			if (this.iHasNameSpacePath) throw new SAXException(
					"CLASSPATH node already has a NAMESPACEPATH child node!");
		} else if (pNodeNameEnum == CLASSNAME) {
			if (this.iHasClassName) throw new SAXException(
					"CLASSPATH node already has a CLASSNAME child node!");
		} else throw new SAXException("CLASSPATH node cannot have " + pNodeNameEnum
				+ " child node!" + " It can have NAMESPACEPATH and CLASSNAME child nodes only!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof NameSpacePathNode) {
			NameSpacePathNode nsPathNode = (NameSpacePathNode) pChild;
			this.iLocalNameSpacePathStr = nsPathNode.getLocalNameSpacePath();
			this.iHostStr.set(nsPathNode.getHostStr());
			this.iHasNameSpacePath = true;
		} else {
			this.iClassNameStr = ((ClassNameNode) pChild).getClassName();
			this.iHasClassName = true;
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasNameSpacePath) throw new SAXException(
				"NAMESPACEPATH child node is mandatory for CLASSPATH node!");
		if (!this.iHasClassName) throw new SAXException(
				"CLASSNAME child node is mandatory for CLASSPATH node!");
	}

	public CIMObjectPath getCIMObjectPath() {
		/*
		 * CIMObjectPath( String scheme, String host, String port, String
		 * namespace, String objectName, CIMProperty[] keys )
		 */
		return new CIMObjectPath(this.iHostStr.getProtocol(), this.iHostStr.getHost(),
				this.iHostStr.getPort(), this.iLocalNameSpacePathStr, this.iClassNameStr, null);
	}

}
