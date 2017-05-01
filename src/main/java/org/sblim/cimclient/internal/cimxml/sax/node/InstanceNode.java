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
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;

import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.LocalPathBuilder;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT INSTANCE (QUALIFIER*, (PROPERTY | PROPERTY.ARRAY | PROPERTY.REFERENCE)*)
 * ATTLIST INSTANCE
 *   %ClassName;
 *   xml:lang   NMTOKEN      #IMPLIED
 * </pre>
 */
public class InstanceNode extends AbstractObjectNode {

	private String iClassName;

	/**
	 * FIXME: What to do with the qualifiers? JSR48 doesn't specify qualifiers
	 * for CIMInstance!
	 */
	private QualifiedNodeHandler iQualiHandler;

	private ArrayList<CIMProperty<?>> iCIMPropAL;

	/**
	 * Ctor.
	 */
	public InstanceNode() {
		super(INSTANCE);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iLocalPath = pSession.getDefLocalPath();
		this.iQualiHandler = new QualifiedNodeHandler();
		this.iCIMPropAL = null;
		this.iClassName = getClassName(pAttribs);
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { QUALIFIER, PROPERTY, PROPERTY_ARRAY,
			PROPERTY_REFERENCE };

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		for (int i = 0; i < ALLOWED_CHILDREN.length; i++)
			if (ALLOWED_CHILDREN[i] == pNodeNameEnum) return;
		throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iQualiHandler.addQualifierNode(pChild)) return;
		if (this.iCIMPropAL == null) this.iCIMPropAL = new ArrayList<CIMProperty<?>>();
		this.iCIMPropAL.add(((AbstractPropertyNode) pChild).getCIMProperty());
	}

	@Override
	public void testCompletness() {
	// all child nodes are optional
	}

	/**
	 * getCIMInstance
	 * 
	 * @return CIMInstance
	 */
	public CIMInstance getCIMInstance() {
		return new CIMInstance(LocalPathBuilder.build(this.iLocalPath, this.iClassName, null),
				getProps());
	}

	/**
	 * getCIMInstance
	 * 
	 * @param pObjPath
	 * @return CIMInstance with the provided object path
	 */
	public CIMInstance getCIMInstance(CIMObjectPath pObjPath) {
		if (WBEMConfiguration.getGlobalConfiguration().synchronizeNumericKeyDataTypes()) return CIMHelper
				.CIMInstanceWithSynchonizedNumericKeyDataTypes(pObjPath, getProps());
		return new CIMInstance(pObjPath, getProps());
	}

	/**
	 * @see org.sblim.cimclient.internal.cimxml.sax.node.ValueIf#getValue()
	 * @return CIMInstance
	 */
	public Object getValue() {
		return getCIMInstance();
	}

	private static final CIMProperty<?>[] EMPTY_PA = new CIMProperty[0];

	private CIMProperty<?>[] getProps() {
		if (this.iCIMPropAL == null) return null;
		return this.iCIMPropAL.toArray(EMPTY_PA);
	}

}
