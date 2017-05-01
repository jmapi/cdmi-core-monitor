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
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 1735614    2007-06-12  ebak         Wrong ARRAYSIZE attribute handling in SAX/PULL
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2038305    2008-08-14  blaschke-oss SAXException SBLIM Java Client V2.0.7
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 *    2605    2013-03-20  buccella     SAX parser throws wrong exception
 *    2693    2013-10-21  blaschke-oss ReturnValueNode allows invalid PARAMTYPE attribute
 *    2702    2013-11-07  blaschke-oss Bad PROPERTY.ARRAY ARRAYSIZE generates NumberFormatException
 *    2706    2013-11-11  blaschke-oss Bad PARAMETER.REFARRAY ARRAYSIZE generates NumberFormatException
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;
import javax.cim.CIMFlavor;

import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.sax.CIMObjectFactory;
import org.sblim.cimclient.internal.cimxml.sax.NodeConstIf;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.util.MOF;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Class Node is the abstract base class of all parseable XML elements. It
 * contains helper functions for the implementations.
 */
public abstract class Node implements NodeConstIf {

	private String iNameEnum;

	private boolean iCompleted;

	/**
	 * Ctor.
	 * 
	 * @param pNameEnum
	 *            The name of the node which must be a String constant which is
	 *            defined inside this class (because the implementation compares
	 *            by reference).
	 */
	public Node(String pNameEnum) {
		this.iNameEnum = pNameEnum;
	}

	/**
	 * getNodeName
	 * 
	 * @return The name of the node. This name have to be one of the String
	 *         constant defined in the class.
	 */
	public String getNodeName() {
		return this.iNameEnum;
	}

	/**
	 * The SAX ContentHandler implementation calls this method after testChild()
	 * and addChild() calls. The implementation must reset it's instance unless
	 * it implements NonVolatileIf
	 * 
	 * @param pAttribs
	 * @param pSession
	 *            - stores variables which are common for the whole SAX parsing
	 *            session
	 * @throws SAXException
	 */
	public abstract void init(Attributes pAttribs, SAXSession pSession) throws SAXException;

	/**
	 * XML element's data have to be passed
	 * 
	 * @param pData
	 *            - String which is concatenated in SAX's
	 *            DefaultHandler.characters() implementation.
	 * @throws SAXException
	 */
	public abstract void parseData(String pData) throws SAXException;

	/**
	 * It have to be called by SAX's DefaultHandler implementation when it
	 * detects a new child element (startElement()).
	 * 
	 * @param pNodeNameEnum
	 *            - the name of the child element, it must be one of the String
	 *            constant defined in class Node, because the implementer
	 *            subclasses uses reference based equals comparisons (==)
	 * @throws SAXException
	 *             - It have to be thrown when the Node cannot have
	 *             pNodeNameEnum named child Node.
	 */
	public abstract void testChild(String pNodeNameEnum) throws SAXException;

	/**
	 * It have to be called by SAX's DefaultHandler.endElement(). It's task is
	 * to check that the Element is built up correctly. The implementer function
	 * can do some post processing here. testCompletness
	 * 
	 * @throws SAXException
	 *             It must be thrown when the Node is not valid.
	 */
	public abstract void testCompletness() throws SAXException;

	/**
	 * Have to be called by SAX's DefaultHandler.endElement()
	 * 
	 * @param pChild
	 * @throws SAXException
	 *             - parent Nodes can make conversions here (e.g. type string
	 *             into CIMDataType), failed operation should throw
	 *             SAXException)
	 */
	public abstract void childParsed(Node pChild) throws SAXException;

	/**
	 * completed
	 * 
	 * @return true if the parsing of the node is completed
	 */
	public boolean isCompleted() {
		return this.iCompleted;
	}

	/**
	 * Have to be called by SAX's DefaultHandler at endElement(), after calling
	 * testCompletness().
	 */
	public void setCompleted() {
		this.iCompleted = true;
	}

	/**
	 * When a Node instance is going to be reused, this function must be called
	 * before.
	 */
	public void clearCompleted() {
		this.iCompleted = false;
	}

	/**
	 * ENTITY % CIMName "NAME CDATA #REQUIRED"
	 * 
	 * @param pAttribs
	 * @return String
	 * @throws SAXException
	 */
	public static String getCIMName(Attributes pAttribs) throws SAXException {
		String name = pAttribs.getValue("NAME");
		if (name == null) throw new SAXException("NAME attribute not found!");
		return name;
	}

	/**
	 * ENTITY % ClassName "CLASSNAME CDATA #REQUIRED"
	 * 
	 * @param pAttribs
	 * @return String
	 * @throws SAXException
	 */
	public static String getClassName(Attributes pAttribs) throws SAXException {
		String name = pAttribs.getValue("CLASSNAME");
		if (name == null) throw new SAXException("CLASSNAME attribute not found!");
		return name;
	}

	/**
	 * ENTITY % ReferenceClass "REFERENCECLASS CDATA #IMPLIED"
	 * 
	 * @param pAttribs
	 * @return String
	 */
	public static String getReferenceClass(Attributes pAttribs) {
		return pAttribs.getValue("REFERENCECLASS");
	}

	/**
	 * ENTITY % ClassOrigin "CLASSORIGIN CDATA #IMPLIED
	 * 
	 * @param pAttribs
	 * @return String
	 */
	public static String getClassOrigin(Attributes pAttribs) {
		return pAttribs.getValue("CLASSORIGIN");
	}

	/**
	 * ENTITY % Propagated "PROPAGATED (true|false) 'false'" getPropagated
	 * 
	 * @param pAttribs
	 * @return boolean
	 */
	public static boolean getPropagated(Attributes pAttribs) {
		String str = pAttribs.getValue("PROPAGATED");
		return MOF.TRUE.equalsIgnoreCase(str);
	}

	/**
	 * ENTITY % ArraySize "ARRAYSIZE CDATA #IMPLIED"
	 * 
	 * @param pAttribs
	 * @return int
	 * @throws SAXException
	 */
	public static int getArraySize(Attributes pAttribs) throws SAXException {
		String arraySizeStr = pAttribs.getValue("ARRAYSIZE");
		// 0 - unbounded size
		int size = 0;
		try {
			size = arraySizeStr == null || arraySizeStr.length() == 0 ? 0 : Integer
					.parseInt(arraySizeStr);
		} catch (NumberFormatException e) {
			throw new SAXException(arraySizeStr + " is not a valid ARRAYSIZE attribute!");
		}
		if (size < 0) throw new SAXException("ARRAYSIZE cannot be " + size + "!");
		return size;
	}

	/**
	 * ENTITY % CIMType "TYPE
	 * (boolean|string|char16|uint8|sint8|uint16|sint16|uint32
	 * |sint32|uint64|sint64|datetime|real32|real64)" getCIMType
	 * 
	 * @param pAttribs
	 * @param pOptional
	 * @return CIMDataType
	 * @throws SAXException
	 */
	public static CIMDataType getCIMType(Attributes pAttribs, boolean pOptional)
			throws SAXException {
		String typeStr = pAttribs.getValue("TYPE");
		if (typeStr == null) {
			if (pOptional) return null;
			throw new SAXException("TYPE attribute not found!");
		}
		CIMDataType type = CIMObjectFactory.getType(typeStr);
		if (type == null) throw new SAXException(typeStr + " is not a valid TYPE attribute!");
		if (type.getType() == CIMDataType.REFERENCE) throw new SAXException(
				"TYPE attribute cannot be \"reference\"!");
		// Is it array?
		// Yes if ISARRAY is true or ARRAYSIZE>-1.
		boolean isArray = hasTrueAttribute(pAttribs, "ISARRAY");
		String arraySizeStr = pAttribs.getValue("ARRAYSIZE");
		int arraySize;
		try {
			arraySize = (arraySizeStr == null || arraySizeStr.length() == 0 ? (isArray ? 0 : -1)
					: Integer.parseInt(arraySizeStr));
		} catch (NumberFormatException e) {
			throw new SAXException(arraySizeStr + " is not a valid ARRAYSIZE attribute!");
		}
		if (isArray || arraySize >= 0) {
			if (arraySize > 0) return new CIMDataType(type.getType(), arraySize);
			return CIMHelper.UnboundedArrayDataType(type.getType());
		}
		return type;
	}

	/**
	 * getCIMType(pAttribs, pOptional=false);
	 * 
	 * @param pAttribs
	 * @return CIMDataType
	 * @throws SAXException
	 */
	public static CIMDataType getCIMType(Attributes pAttribs) throws SAXException {
		return getCIMType(pAttribs, false);
	}

	/**
	 * ENTITY % ParamType "PARAMTYPE (
	 * boolean|string|char16|uint8|sint8|uint16|sint16
	 * |uint32|sint32|uint64|sint64|datetime| real32|real64|reference)
	 * 
	 * @param pAttribs
	 * @return CIMDataType
	 * @throws SAXException
	 */
	public static CIMDataType getParamType(Attributes pAttribs) throws SAXException {
		String typeStr = pAttribs.getValue("PARAMTYPE");
		return CIMObjectFactory.getType(typeStr);
	}

	/**
	 * <pre>
	 * ENTITY % QualifierFlavor &quot;
	 * OVERRIDABLE    (true|false)  'true'
	 * TOSUBCLASS     (true|false)  'true'
	 * TOINSTANCE     (true|false)  'false'
	 * TRANSLATABLE   (true|false)  'false'&quot;
	 * </pre>
	 * 
	 * @param pAttribs
	 * @return int - CIMFlavor bit mixture
	 */
	public int getQualifierFlavor(Attributes pAttribs) {
		int flavors = 0;
		if (!getBoolAttribute(pAttribs, "OVERRIDABLE", true)) flavors |= CIMFlavor.DISABLEOVERRIDE;
		if (!getBoolAttribute(pAttribs, "TOSUBCLASS", true)) flavors |= CIMFlavor.RESTRICTED;
		if (getBoolAttribute(pAttribs, "TRANSLATABLE", false)) flavors |= CIMFlavor.TRANSLATE;
		return flavors;
	}

	/**
	 * hasTrueAttribute
	 * 
	 * @param pAttribs
	 * @param pName
	 * @return boolean
	 */
	public static boolean hasTrueAttribute(Attributes pAttribs, String pName) {
		return MOF.TRUE.equalsIgnoreCase(pAttribs.getValue(pName));
	}

	/**
	 * getBoolAttribute
	 * 
	 * @param pAttribs
	 * @param pName
	 * @param pDefVal
	 * @return boolean
	 */
	public static boolean getBoolAttribute(Attributes pAttribs, String pName, boolean pDefVal) {
		String val = pAttribs.getValue(pName);
		if (MOF.TRUE.equalsIgnoreCase(val)) return true;
		if (MOF.FALSE.equalsIgnoreCase(val)) return false;
		return pDefVal;
	}

	/**
	 * duplicatedNode
	 * 
	 * @param pParsedNodeName
	 * @param pNewNodeName
	 * @throws SAXException
	 */
	public void duplicatedNode(String pParsedNodeName, String pNewNodeName) throws SAXException {
		throw new SAXException(getNodeName() + " has a " + pParsedNodeName
				+ " child node which disallows an additional " + pNewNodeName + " child node!");
	}

	/**
	 * illegalChildNodePair
	 * 
	 * @param pNodeName0
	 * @param pNodeName1
	 * @throws SAXException
	 */
	public void illegalChildNodePair(String pNodeName0, String pNodeName1) throws SAXException {
		throw new SAXException(pNodeName0 + ", " + pNodeName1 + " child node pair is illegal for "
				+ getNodeName() + " node!");
	}

}
