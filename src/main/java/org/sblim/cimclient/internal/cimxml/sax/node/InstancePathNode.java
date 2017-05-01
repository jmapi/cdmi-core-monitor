/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.util.XMLHostStr;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT INSTANCEPATH (NAMESPACEPATH, INSTANCENAME)
 */
public class InstancePathNode extends AbstractObjectPathNode {

	// INSTANCEPATH
	private boolean iHasInstancePath;

	private String iLocalNameSpacePathStr;

	private XMLHostStr iHostStr;

	// INSTANCENAME
	private boolean iHasInstanceName;

	private String iClassNameStr;

	private CIMProperty<?>[] iKeys;

	/**
	 * Ctor.
	 */
	public InstancePathNode() {
		super(INSTANCEPATH);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iHasInstanceName = this.iHasInstancePath = false;
		this.iLocalNameSpacePathStr = this.iClassNameStr = null;
		this.iHostStr = new XMLHostStr();
		this.iKeys = null;
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (pNodeNameEnum == NAMESPACEPATH) {
			if (this.iHasInstancePath) throw new SAXException(
					"INSTANCEPATH node can have only one NAMESPACEPATH child node!");
		} else if (pNodeNameEnum == INSTANCENAME) {
			if (this.iHasInstanceName) throw new SAXException(
					"INSTANCEPATH node can have only one INSTANCENAME child node!");
		} else throw new SAXException("INSTANCEPATH node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof NameSpacePathNode) {
			NameSpacePathNode nsPathNode = (NameSpacePathNode) pChild;
			this.iHostStr.set(nsPathNode.getHostStr());
			this.iLocalNameSpacePathStr = nsPathNode.getLocalNameSpacePath();
			this.iHasInstancePath = true;
		} else {
			InstanceNameNode instNameNode = (InstanceNameNode) pChild;
			this.iClassNameStr = instNameNode.getClassName();
			this.iKeys = instNameNode.getKeys();
			this.iHasInstanceName = true;
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasInstancePath) throw new SAXException(
				"INSTANCEPATH node must have a NAMESPACEPATH child node!");
		if (!this.iHasInstanceName) throw new SAXException(
				"INSTANCEPATH node must have an INSTANCENAME child node!");
	}

	public CIMObjectPath getCIMObjectPath() {
		/*
		 * CIMObjectPath( String scheme, String host, String port, String
		 * namespace, String objectName, CIMProperty[] keys )
		 */
		return new CIMObjectPath(this.iHostStr.getProtocol(), this.iHostStr.getHost(),
				this.iHostStr.getPort(), this.iLocalNameSpacePathStr, this.iClassNameStr,
				this.iKeys);
	}

}
