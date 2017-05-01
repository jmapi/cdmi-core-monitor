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
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT VALUE.NAMEDINSTANCE (INSTANCENAME, INSTANCE)
 * 
 * ELEMENT INSTANCENAME (KEYBINDING* | KEYVALUE? | VALUE.REFERENCE?)
 * ATTLIST INSTANCENAME
 *   %ClassName;
 *   
 * ELEMENT INSTANCE (QUALIFIER*, (PROPERTY | PROPERTY.ARRAY | PROPERTY.REFERENCE)*)
 * ATTLIST INSTANCE
 *   %ClassName;
 *   xml:lang   NMTOKEN      #IMPLIED 
 * FIXME: Why INSTANCE has qualifiers? CIMInstance doesn't have!
 * FIXME: InstanceName and instance provides redundant information. Why?
 * </pre>
 */
public class ValueNamedInstanceNode extends AbstractScalarValueNode {

	// INSTANCENAME
	private CIMObjectPath iCIMInstPath;

	// INSTANCE
	private CIMInstance iCIMInstance;

	/**
	 * Ctor.
	 */
	public ValueNamedInstanceNode() {
		super(VALUE_NAMEDINSTANCE);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iCIMInstPath = null;
		this.iCIMInstance = null;
		// no attribute
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
		if (pNodeNameEnum == INSTANCENAME) {
			if (this.iCIMInstPath != null) throw new SAXException(
					"VALUE.NAMEDINSTANCE node can have only one INSTANCENAME node, but another one was found!");
		} else if (pNodeNameEnum == INSTANCE) {
			if (this.iCIMInstance != null) throw new SAXException(
					"VALUE.NAMEDINSTANCE node can have only one INSTANCE node, but another one was found!");
		} else {
			throw new SAXException("VALUE.NAMEDINSTANCE node cannot have " + pNodeNameEnum
					+ " child node!");
		}
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof InstanceNameNode) {
			this.iCIMInstPath = ((InstanceNameNode) pChild).getCIMObjectPath();
		} else {
			this.iCIMInstance = ((InstanceNode) pChild).getCIMInstance();
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMInstPath == null) throw new SAXException(
				"VALUE.NAMEDINSTANCE node must have an INSTANCENAME child node!");
		if (this.iCIMInstance == null) throw new SAXException(
				"VALUE.NAMEDINSTANCE node must have an INSTANCE child node!");
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ValueIf#getValue()
	 * @return CIMInstance
	 */
	public Object getValue() {
		// CIMObjectPath op=iInstanceNameNode.getCIMObjectPath();
		/*
		 * INSTANCENAME contains the key properties only, INSTANCE contains the
		 * non-key properties too.
		 */
		if (WBEMConfiguration.getGlobalConfiguration().synchronizeNumericKeyDataTypes()) return CIMHelper
				.CIMInstanceWithSynchonizedNumericKeyDataTypes(this.iCIMInstPath, this.iCIMInstance
						.getProperties());
		return new CIMInstance(this.iCIMInstPath, this.iCIMInstance.getProperties());
	}

	public CIMDataType getType() {
		return CIMDataType.OBJECT_T;
	}

}
