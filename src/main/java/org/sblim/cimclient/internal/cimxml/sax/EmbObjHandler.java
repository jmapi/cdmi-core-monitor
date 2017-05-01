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
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1712656    2007-05-04  ebak         Correct type identification for SVC CIMOM
 * 1719991    2007-05-16  ebak         FVT: regression ClassCastException in EmbObjHandler
 * 1820763    2007-10-29  ebak         Supporting the EmbeddedInstance qualifier
 * 1848607    2007-12-11  ebak         Strict EmbeddedObject types
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2957387    2010-03-03  blaschke-oss EmbededObject XML attribute must not be all uppercases
 * 3281781    2011-04-11  blaschke-oss fail to parse Embedded Instance parameter
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 * 3513349    2012-03-31  blaschke-oss TCK: CIMDataType must not accept null string
 *    2691    2013-10-18  blaschke-oss RETURNVALUE should not require PARAMTYPE attribute
 */

package org.sblim.cimclient.internal.cimxml.sax;

import javax.cim.CIMDataType;
import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.sax.node.AbstractValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ArrayIf;
import org.sblim.cimclient.internal.cimxml.sax.node.Node;
import org.sblim.cimclient.internal.cimxml.sax.node.QualifiedNodeHandler;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueArrayNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * EmbObjHandler helps the parsing of embedded object elements.
 */

public class EmbObjHandler {

	private String iNodeName;

	private CIMDataType iRawType;

	private boolean iHasEmbObjAttr, iHasEmbInstAttr;

	private SAXSession iSession;

	private CIMDataType iType;

	private Object iValue;

	private QualifiedNodeHandler iQualiHandler;

	private AbstractValueNode iAbsValNode;

	/**
	 * Generic initialization.
	 * 
	 * @param pHandler
	 * @param pNodeName
	 * @param pAttribs
	 * @param pSession
	 * @param pQNodeHandler
	 * @param pCheckEmbObjAttrib
	 * @return EmbObjHandler
	 * @throws SAXException
	 */
	public static EmbObjHandler init(EmbObjHandler pHandler, String pNodeName, Attributes pAttribs,
			SAXSession pSession, QualifiedNodeHandler pQNodeHandler, boolean pCheckEmbObjAttrib)
			throws SAXException {
		if (pHandler == null) pHandler = new EmbObjHandler();
		pHandler.initInst(pNodeName, pAttribs, pSession, pQNodeHandler, pCheckEmbObjAttrib);
		return pHandler;
	}

	private EmbObjHandler() {
	// init() used for instantiation
	}

	/**
	 * Generic instance initialization.
	 * 
	 * @param pNodeName
	 * @param pAttribs
	 * @param pSession
	 * @param pQNodeHandler
	 * @param pCheckEmbObjAttrib
	 * @throws SAXException
	 */
	public void initInst(String pNodeName, Attributes pAttribs, SAXSession pSession,
			QualifiedNodeHandler pQNodeHandler, boolean pCheckEmbObjAttrib) throws SAXException {
		this.iSession = pSession;
		this.iNodeName = pNodeName;
		this.iQualiHandler = pQNodeHandler;
		this.iType = null;
		this.iValue = null;
		this.iAbsValNode = null;
		this.iRawType = Node.getCIMType(pAttribs, true);
		if (this.iRawType == null) {
			this.iRawType = Node.getParamType(pAttribs);
			/*
			 * if (iRawType==null) throw new SAXException( iNodeName+" must
			 * contain a TYPE or a PARAMTYPE attribute!" );
			 */
			/*
			 * SVC CIMOM doesn't add TYPE attribute for the RETURNVALUE element,
			 * but it adds it to the VALUE element.
			 */
		}

		if (pCheckEmbObjAttrib) {
			// DSPs call for EmbeddedObject AND XML is case sensitive, BUT there
			// are probably CIMOMs out there that still use EMBEDDEDOBJECT
			String embObjStr = pAttribs.getValue("EmbeddedObject");
			if (embObjStr == null) {
				embObjStr = pAttribs.getValue("EMBEDDEDOBJECT");
			}
			if (embObjStr == null) {
				this.iHasEmbInstAttr = this.iHasEmbObjAttr = false;
			} else if ("instance".equalsIgnoreCase(embObjStr)) {
				this.iHasEmbInstAttr = true;
				this.iHasEmbObjAttr = false;
			} else if ("object".equalsIgnoreCase(embObjStr)) {
				this.iHasEmbInstAttr = false;
				this.iHasEmbObjAttr = true;
			} else {
				throw new SAXException(
						"EmbeddedObject attribute's value must be \"object\" or \"instance\". "
								+ embObjStr + " is invalid!");
			}
		} else {
			this.iHasEmbInstAttr = this.iHasEmbObjAttr = false;
		}
	}

	/**
	 * isEmbeddedObject For this function Object means CLASS or INSTANCE. Object
	 * can have other meanings at different places !!! :(
	 * 
	 * @return boolean
	 */
	private boolean isEmbeddedObject() {
		return this.iHasEmbInstAttr
				|| this.iHasEmbObjAttr
				|| (this.iQualiHandler != null && (this.iQualiHandler.isEmbeddedObject() || this.iQualiHandler
						.isEmbeddedInstance()));
	}

	/**
	 * isEmbeddedClass
	 * 
	 * @return boolean
	 */
	private boolean isEmbeddedClass() {
		return this.iHasEmbObjAttr
				|| (this.iQualiHandler != null && this.iQualiHandler.isEmbeddedObject());
	}

	/**
	 * isEmbeddedInstance
	 * 
	 * @return boolean
	 */
	private boolean isEmbeddedInstance() {
		return this.iHasEmbInstAttr
				|| (this.iQualiHandler != null && this.iQualiHandler.isEmbeddedInstance());
	}

	/**
	 * getValue
	 * 
	 * @return Object
	 * @throws SAXException
	 */
	public Object getValue() throws SAXException {
		transform();
		return this.iValue;
	}

	/**
	 * getType
	 * 
	 * @return Object
	 * @throws SAXException
	 */
	public CIMDataType getType() throws SAXException {
		transform();
		return this.iType;
	}

	/**
	 * getRawType
	 * 
	 * @return the type which is retrieved from the XML attributes
	 */
	public CIMDataType getRawType() {
		return this.iRawType;
	}

	/**
	 * getArrayType useful e.g. for PROPERTY.ARRAY
	 * 
	 * @return CIMDataType
	 * @throws SAXException
	 */
	public CIMDataType getArrayType() throws SAXException {
		transform();
		return this.iType.isArray() ? this.iType : CIMHelper.UnboundedArrayDataType(this.iType
				.getType());
	}

	/**
	 * addValueNode
	 * 
	 * @param pValueNode
	 *            - can be ValueNode ore ValueArrayNode for Embedded Objects
	 */
	public void addValueNode(AbstractValueNode pValueNode) {
		if (isEmbeddedObject()
				&& !(pValueNode == null || pValueNode instanceof ValueNode || pValueNode instanceof ValueArrayNode)) throw new IllegalArgumentException(
				"pValueNode's type can be ValueNode or ValueArrayNode or it can be null. "
						+ pValueNode.getClass().getName() + " is an invalid type!");
		this.iAbsValNode = pValueNode;
	}

	private void transform() throws SAXException {
		if (this.iType != null) return;
		if (this.iAbsValNode == null) {
			if (isEmbeddedObject()) {
				if (this.iRawType != CIMDataType.STRING_T) throw new SAXException(
						"Embedded Object CIM-XML element's type must be string. " + this.iRawType
								+ " is invalid!");
				if (this.iSession.strictEmbObjParsing()) {
					/*
					 * Here the assumption is that Object = CLASS, Instance =
					 * INSTANCE.
					 */
					this.iType = isEmbeddedInstance() ? CIMDataType.OBJECT_T : CIMDataType.CLASS_T;
				} else {
					/*
					 * for valueless EmbeddedObject="object" the type can not be
					 * determined since Pegasus's CIMObject can contain both
					 * CIMClass and CIMInstance. Is it true for Pegasus 2.7.0
					 * too?
					 */
					this.iType = isEmbeddedInstance() ? CIMDataType.OBJECT_T : CIMDataType.STRING_T;
				}
			} else {
				this.iType = this.iRawType;
			}
			this.iValue = null;
		} else {
			setType();
			if (isEmbeddedObject()) {
				transformEmbObj();
			} else {
				transformNormObj();
			}
		}
	}

	private void transformEmbObj() throws SAXException {
		if (this.iAbsValNode instanceof ValueNode) {
			String valueStr = (String) ((ValueNode) this.iAbsValNode).getValue();
			this.iValue = CIMObjectFactory.getEmbeddedObj(this.iRawType, valueStr, this.iSession);
			this.iType = CIMObjectFactory.getCIMObjScalarType(this.iValue);
		} else { // ValueArrayNode
			this.iValue = CIMObjectFactory.getEmbeddedObjA(this.iRawType,
					(ValueArrayNode) this.iAbsValNode, this.iSession);
			this.iType = CIMObjectFactory.getCIMObjArrayType(this.iValue);
		}
		if (isEmbeddedInstance() && this.iType.getType() != CIMDataType.OBJECT) throw new SAXException(
				this.iNodeName + " element is an EmbeddedInstance with non INSTANCE value. "
						+ "It's not valid!");
		if (isEmbeddedClass() && this.iType.getType() != CIMDataType.CLASS
				&& this.iType.getType() != CIMDataType.OBJECT) throw new SAXException(
				this.iNodeName
						+ " element is an EmbeddedObject with non CLASS/INSTANCE value. It's not valid!");
	}

	private void transformNormObj() throws SAXException {
		if (this.iAbsValNode instanceof ValueNode) {
			this.iType = this.iRawType;
			this.iValue = CIMObjectFactory.getObject(this.iType, (ValueNode) this.iAbsValNode);
		} else if (this.iAbsValNode instanceof ValueArrayNode) {
			this.iType = CIMHelper.UnboundedArrayDataType(this.iRawType.getType());
			this.iValue = CIMObjectFactory.getObject(this.iRawType,
					(ValueArrayNode) this.iAbsValNode);
		} else {
			this.iValue = this.iAbsValNode.getValue();
			if (this.iAbsValNode instanceof ArrayIf) {
				if (this.iValue instanceof CIMObjectPath[]) this.iType = new CIMDataType("", 0);
			} else {
				if (this.iValue instanceof CIMObjectPath) {
					this.iType = new CIMDataType(((CIMObjectPath) this.iValue).getObjectName());
				} else {
					this.iType = this.iRawType;
				}
			}
		}
	}

	/**
	 * Required to handle the output XML of some non-standard CIMOMs like SVC
	 * which adds the TYPE attribute to the sub VALUE or VALUE.ARRAY XML
	 * element.
	 * 
	 * @throws SAXException
	 */
	private void setType() throws SAXException {
		if (this.iType != null || this.iRawType != null) return;
		this.iRawType = this.iAbsValNode.getType();
		if (this.iRawType == null) this.iRawType = (this.iAbsValNode instanceof ArrayIf ? CIMDataType.STRING_ARRAY_T
				: CIMDataType.STRING_T);
	}

}
