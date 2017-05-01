/**
 * (C) Copyright IBM Corp. 2009, 2013
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Ramandeep S Arora, IBM,  arorar@us.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
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
 * ELEMENT VALUE.INSTANCEWITHPATH (INSTANCEPATH, INSTANCE)
 * 
 * ELEMENT INSTANCEPATH (NAMESPACEPATH,INSTANCENAME)
 *    
 * ELEMENT INSTANCE (QUALIFIER*, (PROPERTY | PROPERTY.ARRAY | PROPERTY.REFERENCE)*)
 * ATTLIST INSTANCE
 *   %ClassName;
 *   xml:lang   NMTOKEN      #IMPLIED
 * </pre>
 */
public class ValueInstanceWithPathNode extends AbstractScalarValueNode {

	// INSTANCEPATH
	private CIMObjectPath iCIMInstPath;

	// INSTANCE
	private CIMInstance iCIMInstance;

	/**
	 * Ctor.
	 */
	public ValueInstanceWithPathNode() {
		super(VALUE_INSTANCEWITHPATH);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		// no attribute
		this.iCIMInstPath = null;
		this.iCIMInstance = null;
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
		if (pNodeNameEnum == INSTANCEPATH) {
			if (this.iCIMInstPath != null) throw new SAXException(
					"VALUE.INSTANCEWITHPATH node can have only one INSTANCEPATH node, but another one was found!");
		} else if (pNodeNameEnum == INSTANCE) {
			if (this.iCIMInstance != null) throw new SAXException(
					"VALUE.INSTANCEWITHPATH node can have only one INSTANCE node, but another one was found!");
		} else {
			throw new SAXException("VALUE.INSTANCEWITHPATH node cannot have " + pNodeNameEnum
					+ " child node!");
		}
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof InstancePathNode) {
			this.iCIMInstPath = ((InstancePathNode) pChild).getCIMObjectPath();
		} else {
			this.iCIMInstance = ((InstanceNode) pChild).getCIMInstance();
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMInstPath == null) throw new SAXException(
				"VALUE.INSTANCEWITHPATH node must have an INSTANCEPATH child node!");
		if (this.iCIMInstance == null) throw new SAXException(
				"VALUE.INSTANCEWITHPATH node must have an INSTANCE child node!");
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ValueIf#getValue()
	 * @return CIMInstance
	 */
	public Object getValue() {
		/*
		 * INSTANCENAME contains the key properties only, INSTANCE contains
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
