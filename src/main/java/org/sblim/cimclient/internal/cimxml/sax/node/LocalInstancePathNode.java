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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;

import org.sblim.cimclient.internal.cimxml.LocalPathBuilder;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT LOCALINSTANCEPATH (LOCALNAMESPACEPATH, INSTANCENAME)
 */
public class LocalInstancePathNode extends AbstractObjectPathNode {

	// LOCALNAMESPACEPATH
	private boolean iHasLocalNameSpacePath;

	private String iNameSpaceStr;

	// INSTANCENAME
	private boolean iHasInstanceName;

	private String iClassNameStr;

	private CIMProperty<?>[] iKeys;

	// base localpath
	private CIMObjectPath iLocalPath;

	/**
	 * Ctor.
	 */
	public LocalInstancePathNode() {
		super(LOCALINSTANCEPATH);
	}

	/**
	 * @param pAttribs
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iLocalPath = pSession.getDefLocalPath();
		this.iHasLocalNameSpacePath = this.iHasInstanceName = false;
		this.iNameSpaceStr = this.iClassNameStr = null;
		this.iKeys = null;
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
			if (this.iHasLocalNameSpacePath) throw new SAXException(getNodeName()
					+ " node can have only one LOCALNAMESPACEPATH child node!");
		} else if (pNodeNameEnum == INSTANCENAME) {
			if (this.iHasInstanceName) throw new SAXException(getNodeName()
					+ " node can have only one INSTANCENAME child node!");
		} else throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof LocalNameSpacePathNode) {
			this.iHasLocalNameSpacePath = true;
			this.iNameSpaceStr = ((LocalNameSpacePathNode) pChild).getNameSpace();
		} else {
			this.iHasInstanceName = true;
			InstanceNameNode instNameNode = (InstanceNameNode) pChild;
			this.iClassNameStr = instNameNode.getClassName();
			this.iKeys = instNameNode.getKeys();
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasLocalNameSpacePath) throw new SAXException(getNodeName()
				+ " node must have a LOCALNAMESPACEPATH child node!");
		if (!this.iHasInstanceName) throw new SAXException(getNodeName()
				+ " node must have a INSTANCENAME child node!");
	}

	public CIMObjectPath getCIMObjectPath() {
		// CIMObjectPath(String objectName, String namespace, CIMProperty[]
		// keys)
		return LocalPathBuilder.build(this.iLocalPath, this.iClassNameStr, this.iNameSpaceStr,
				this.iKeys);
	}

}
