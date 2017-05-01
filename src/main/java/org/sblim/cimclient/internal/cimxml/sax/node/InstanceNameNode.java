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
 *    2707    2013-11-12  blaschke-oss INSTANCENAME ignores KEYVALUE and VALUE.REFERENCE children
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;

import org.sblim.cimclient.internal.cimxml.LocalPathBuilder;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * perhaps it's a documentation mistake: ELEMENT INSTANCENAME (KEYBINDING* | KEYVALUE? | VALUE.REFERENCE?)
 * ELEMENT INSTANCENAME (KEYBINDING*)
 * ATTLIST INSTANCENAME
 *  %ClassName;
 * </pre>
 */
public class InstanceNameNode extends AbstractPathNode {

	private String iClassName;

	private ArrayList<CIMProperty<?>> iCIMPropAL;

	private CIMObjectPath iLocalPath;

	private String iNodeName;

	/**
	 * Ctor.
	 */
	public InstanceNameNode() {
		super(INSTANCENAME);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iLocalPath = pSession.getDefLocalPath();
		if (this.iCIMPropAL != null) this.iCIMPropAL.clear();
		this.iClassName = getClassName(pAttribs);
		this.iNodeName = null;
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { KEYBINDING, KEYVALUE, VALUE_REFERENCE };

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		for (int i = 0; i < ALLOWED_CHILDREN.length; i++)
			if (ALLOWED_CHILDREN[i] == pNodeNameEnum) {
				if (this.iNodeName != null && this.iNodeName != pNodeNameEnum) throw new SAXException(
						getNodeName() + " node cannot have " + pNodeNameEnum
								+ " child node, it already has a " + this.iNodeName + "!");
				if (pNodeNameEnum != KEYBINDING) {
					if (this.iNodeName != null) throw new SAXException(getNodeName()
							+ " node can have only one " + pNodeNameEnum + " child node!");
				}
				this.iNodeName = pNodeNameEnum;
				return;
			}
		throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iCIMPropAL == null) this.iCIMPropAL = new ArrayList<CIMProperty<?>>();
		if (pChild instanceof KeyBindingNode) this.iCIMPropAL.add(((KeyBindingNode) pChild)
				.getCIMProperty());
		else this.iCIMPropAL.add(new CIMProperty<Object>("", ((AbstractScalarValueNode) pChild)
				.getType(), ((AbstractScalarValueNode) pChild).getValue(), true, false, null));
	}

	@Override
	public void testCompletness() {
	// no mandatory child nodes
	}

	/**
	 * getClassName
	 * 
	 * @return String
	 */
	public String getClassName() {
		return this.iClassName;
	}

	private static final CIMProperty<?>[] EMPTY_PA = new CIMProperty[0];

	/**
	 * getKeys
	 * 
	 * @return CIMProperty[]
	 */
	public CIMProperty<?>[] getKeys() {
		if (this.iCIMPropAL == null || this.iCIMPropAL.size() == 0) return null;
		return this.iCIMPropAL.toArray(EMPTY_PA);
	}

	public CIMObjectPath getCIMObjectPath() {
		/*
		 * CIMObjectPath(String objectName, String namespace, CIMProperty[]
		 * keys)
		 */
		return LocalPathBuilder.build(this.iLocalPath, this.iClassName, null, getKeys());
	}

}
