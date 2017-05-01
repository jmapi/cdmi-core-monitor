/**
 * CIMXMLBuilderImpl.java
 *
 * (C) Copyright IBM Corp. 2005, 2013
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------ 
 * 16627      2005-04-01  thschaef     Correct instantiation for Uint64 
 * 17459      2005-06-24  thschaef     catch null within createMethod method
 * 17459      2005-06-27  thschaef     further improvement to createMethod()
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1610046    2006-07-12  ebak         Does not escape trailing spaces KEYVALUE
 * 1631407    2007-01-11  lupusalex    VALUE.REFERENCE doesn't handle references without namespace
 * 1656285    2007-02-12  ebak         IndicationHandler does not accept non-Integer message ID
 * 1671505    2007-02-28  lupusalex    Wrong escaping of spaces in XML (Undo 1610046)
 * 1660756    2007-03-02  ebak         Embedded object support
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 1719991    2007-05-16  ebak         FVT: regression ClassCastException in EmbObjHandler
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 1820763    2007-10-29  ebak    	   Supporting the EmbeddedInstance qualifier
 * 1827728    2007-11-12  ebak         embeddedInstances: attribute EmbeddedObject not set
 * 1827728    2007-11-20  ebak         rework: embeddedInstances: attribute EmbeddedObject not set
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2087969    2008-09-03  blaschke-oss VALUE.ARRAY used in request for array of references
 * 2093708    2008-09-10  rgummada     HTTP 400 - Bad Request, CIMError: request-not-valid
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2210455    2008-10-30  blaschke-oss Enhance javadoc, fix potential null pointers
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 2849970    2009-09-03  blaschke-oss createVALUEARRAY fails to create reference array
 * 2912490    2010-01-05  rgummada     NullPointerException when invoking getInstance
 * 2957387    2010-03-03  blaschke-oss EmbededObject XML attribute must not be all uppercases
 * 2970881    2010-03-15  blaschke-oss Add property to control EmbeddedObject case
 * 3001333    2010-05-19  blaschke-oss CIMMethod class ignores propagated parameter
 * 3304058    2011-05-20  blaschke-oss Use same date format in change history
 * 3588558    2012-11-26  blaschke-oss An enhancement on Java CIM Client logging
 *    2616    2013-02-23  blaschke-oss Add new API WBEMClientSBLIM.sendIndication()
 *    2638    2013-05-09  blaschke-oss Do not build empty REFERENCECLASS
 *    2689    2013-10-10  blaschke-oss createMETHODCALL should not add PARAMTYPE attribute
 */

package org.sblim.cimclient.internal.cimxml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.cim.CIMArgument;
import javax.cim.CIMClass;
import javax.cim.CIMClassProperty;
import javax.cim.CIMDataType;
import javax.cim.CIMFlavor;
import javax.cim.CIMInstance;
import javax.cim.CIMMethod;
import javax.cim.CIMNamedElementInterface;
import javax.cim.CIMObjectPath;
import javax.cim.CIMParameter;
import javax.cim.CIMProperty;
import javax.cim.CIMQualifiedElementInterface;
import javax.cim.CIMQualifier;
import javax.cim.CIMQualifierType;
import javax.cim.CIMScope;
import javax.cim.CIMTypedElement;
import javax.cim.CIMValuedElement;
import javax.wbem.WBEMException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sblim.cimclient.internal.cim.CIMQualifiedElementInterfaceImpl;
import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Class CIMXMLBuilderImpl is the core class for building CIM-XML documents.
 */
public class CIMXMLBuilderImpl {

	private static final int MAJOR_CIM_VERSION = 2;

	private static final int MINOR_CIM_VERSION = 0;

	private static final int MAJOR_DTD_VERSION = 2;

	private static final int MINOR_DTD_VERSION = 0;

	/**
	 * createCIM
	 * 
	 * @param pDoc
	 * @return Element
	 */
	public static Element createCIM(Document pDoc) {
		// <!ELEMENT CIM (MESSAGE|DECLARATION)>
		// <!ATTLIST CIM %CIMVERSION;%DTDVERSION;>
		Element e = pDoc.createElement("CIM");
		e.setAttribute("CIMVERSION", MAJOR_CIM_VERSION + "." + MINOR_CIM_VERSION);
		// required
		e.setAttribute("DTDVERSION", MAJOR_DTD_VERSION + "." + MINOR_DTD_VERSION);
		// required
		pDoc.appendChild(e); // root element
		return e;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// Value Elements
	// ////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * createVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createVALUE(Document pDoc, Element pParentE) {
		// <! ELEMENT VALUE (#PCDATA)>
		Element e = pDoc.createElement("VALUE");
		pParentE.appendChild(e);

		return e;
	}

	/**
	 * createVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pValue
	 * @return Element
	 */
	public static Element createVALUE(Document pDoc, Element pParentE, String pValue) {
		// <! ELEMENT VALUE (#PCDATA)>
		Element e = pDoc.createElement("VALUE");
		pParentE.appendChild(e);

		Text textE = pDoc.createTextNode(pValue);
		e.appendChild(textE);
		return e;
	}

	/**
	 * createVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pValue
	 * @return Element
	 */
	public static Element createVALUE(Document pDoc, Element pParentE, boolean pValue) {
		return createVALUE(pDoc, pParentE, pValue ? MOF.TRUE : MOF.FALSE);
	}

	/**
	 * createVALUEARRAY
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createVALUEARRAY(Document pDoc, Element pParentE) {
		// <! ELEMENT VALUE.ARRAY (VALUE*)>
		Element e = pDoc.createElement("VALUE.ARRAY");
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createVALUEREFERENCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createVALUEREFERENCE(Document pDoc, Element pParentE) {
		// <!ELEMENT VALUE.REFERENCE
		// (CLASSPATH|LOCALCLASSPATH|CLASSNAME|INSTANCEPATH|LOCALINSTANCEPATH|INSTANCENAME)>
		Element e = pDoc.createElement("VALUE.REFERENCE");
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createVALUEREFARRAY
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createVALUEREFARRAY(Document pDoc, Element pParentE) {
		// <! ELEMENT VALUE.REFARRAY (VALUE.REFERENCE*)>
		Element e = pDoc.createElement("VALUE.REFARRAY");
		pParentE.appendChild(e);
		return e;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// Properties elements
	// ////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * createPROPERTY
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @param pType
	 * @return Element
	 */
	public static Element createPROPERTY(Document pDoc, Element pParentE, String pName, String pType) {
		// <!ELEMENT PROPERTY (QUALIFIER*,VALUE?)>
		// <!ATTLIST PROPERTY
		// %CIMName;
		// %CIMType; #REQUIRED
		// %ClassOrigin;
		// %Propagated;>

		Element e = pDoc.createElement("PROPERTY");
		if (pName != null) e.setAttribute("NAME", pName);
		if (pType != null) e.setAttribute("TYPE", pType);
		// TODO: ClassOrigin, Propagated
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createPROPERTYARRAY
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @param pType
	 * @return Element
	 */
	public static Element createPROPERTYARRAY(Document pDoc, Element pParentE, String pName,
			String pType) {
		// <!ELEMENT PROPERTY.ARRAY (QUALIFIER*,VALUE.ARRAY?)>
		// <!ATTLIST PROPERTY.ARRAY
		// %CIMName;
		// %CIMType; #REQUIRED
		// %ArraySize;
		// %ClassOrigin;
		// %Propagated;>

		Element e = pDoc.createElement("PROPERTY.ARRAY");
		if (pName != null) e.setAttribute("NAME", pName);
		if (pType != null) e.setAttribute("TYPE", pType);
		// TODO: ClassOrigin, Propagated
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createPROPERTYREFERENCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @param pReferenceclass
	 * @return Element
	 */
	public static Element createPROPERTYREFERENCE(Document pDoc, Element pParentE, String pName,
			String pReferenceclass) {
		// <!ELEMENT PROPERTY.REFERENCE (QUALIFIER*,VALUE.REFERENCE?)>
		// <!ATTLIST PROPERTY.REFERENCE
		// %CIMName;
		// %ReferenceClass;
		// %ClassOrigin;
		// %Propagated;>

		Element e = pDoc.createElement("PROPERTY.REFERENCE");
		if (pName != null) e.setAttribute("NAME", pName);
		if (pReferenceclass != null && pReferenceclass.length() > 0) e.setAttribute(
				"REFERENCECLASS", pReferenceclass);
		pParentE.appendChild(e);
		return e;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// Naming and Location elements
	// ////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * createNAMESPACE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createNAMESPACE(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT NAMESPACE EMPTY>
		// <!ATTLIST NAMESPACE %NAME;>
		Element e = pDoc.createElement("NAMESPACE");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createLOCALINSTANCEPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createLOCALINSTANCEPATH(Document pDoc, Element pParentE) {
		// <!ELEMENT INSTANCEPATH (NAMESPACEPATH,INSTANCENAME)>
		Element e = pDoc.createElement("LOCALINSTANCEPATH");
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createCLASSNAME
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createCLASSNAME(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT CLASSNAME EMPTY>
		// <!ATTLIST CLASSNAME %NAME;>
		Element e = pDoc.createElement("CLASSNAME");
		if (pName != null) {
			e.setAttribute("NAME", pName);

			pParentE.appendChild(e);
		}
		return e;
	}

	/**
	 * createCLASS
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @param pSuperClass
	 * @return Element
	 */
	public static Element createCLASS(Document pDoc, Element pParentE, String pName,
			String pSuperClass) {
		// <!ELEMENT CLASSNAME EMPTY>
		// <!ATTLIST CLASSNAME %NAME;>
		Element e = pDoc.createElement("CLASS");
		if (pName != null) {
			e.setAttribute("NAME", pName);

		}
		if (pSuperClass != null && pSuperClass.length() > 0) {
			e.setAttribute("SUPERCLASS", pSuperClass);

		}
		if (pParentE != null) pParentE.appendChild(e);
		return e;
	}

	/**
	 * createINSTANCENAME
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pClassName
	 * @return Element
	 */
	public static Element createINSTANCENAME(Document pDoc, Element pParentE, String pClassName) {
		// <!ELEMENT INSTANCENAME (KEYBINDING*|KEYVALUE?|VALUE.REFERNCE?)>
		// <!ATTLIST INSTANCENAME %CLASSNAME;>
		Element e = pDoc.createElement("INSTANCENAME");
		if (pClassName != null) {
			e.setAttribute("CLASSNAME", pClassName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createKEYBINDING
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createKEYBINDING(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT KEYBINDING (KEYVALUE|VALUE.REFERENCE) >
		// <!ATTLIST KEYBINDING %NAME;>
		Element e = pDoc.createElement("KEYBINDING");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	private static final Pattern NUM_PAT = Pattern.compile("^[su]int(8|16|32|64)$",
			Pattern.CASE_INSENSITIVE);

	/**
	 * <!ATTLIST KEYVALUE VALUETYPE (string|boolean|numeric) 'string')>
	 * getValueTypeStr
	 * 
	 * @param pTypeStr
	 * @return String
	 */
	private static String getValueTypeStr(String pTypeStr) {

		if (pTypeStr == null || MOF.DT_STR.equalsIgnoreCase(pTypeStr)) return MOF.DT_STR;
		if (MOF.DT_BOOL.equalsIgnoreCase(pTypeStr)) return MOF.DT_BOOL;
		Matcher m = NUM_PAT.matcher(pTypeStr);
		if (m.matches()) return "numeric";
		return MOF.DT_STR;
	}

	/**
	 * createKEYVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pValueType
	 * @param pValue
	 * @return KEYVALUE
	 */
	public static Element createKEYVALUE(Document pDoc, Element pParentE, String pValueType,
			String pValue) {
		/*
		 * <!ELEMENT KEYVALUE (#PCDATA)> <!ATTLIST KEYVALUE VALUETYPE
		 * (string|boolean|numeric) 'string') %CIMType; #IMPLIED>
		 */
		if (pValueType == null) pValueType = "string";
		Element e = pDoc.createElement("KEYVALUE");
		// FIXME: ebak: TYPE attrib is more exact, easier to handle
		e.setAttribute("TYPE", pValueType);
		// ebak: maybe old CIMOMs won't understand the TYPE attrib
		e.setAttribute("VALUETYPE", getValueTypeStr(pValueType));
		pParentE.appendChild(e);
		Text valueE = pDoc.createTextNode(pValue);
		e.appendChild(valueE);
		return e;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// Object Definition Elements
	// ////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * createINSTANCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pClassName
	 * @return Element
	 */
	public static Element createINSTANCE(Document pDoc, Element pParentE, String pClassName) {
		// <!ELEMENT INSTANCE
		// (QUALIFIER*,(PROPERTY|PROPERTY.ARRAY|PROPERTY.REFERENCE)*)>
		// <!ATTLIST INSTANCE %ClassName;>

		Element e = pDoc.createElement("INSTANCE");
		if (pClassName != null) {
			e.setAttribute("CLASSNAME", pClassName);
		}
		if (pParentE != null) pParentE.appendChild(e);
		return e;
	}

	/**
	 * createQUALIFIER
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @param pType
	 * @return Element
	 */
	public static Element createQUALIFIER(Document pDoc, Element pParentE, String pName,
			String pType) {
		// <!ELEMENT QUALIFIER (VALUE|VALUE.ARRAY)>
		// <!ATTLIST QUALIFIER
		// %CIMName;
		// %CIMType; #REQUIRED
		// %Propagated;
		// %QualifierFlavor;>

		Element e = pDoc.createElement("QUALIFIER");
		if (pName != null) {
			e.setAttribute("NAME", pName);
			// TODO: add additional attributes "propagated", "qualifierFlavod"
		}
		if (pType != null) e.setAttribute("TYPE", pType);
		pParentE.appendChild(e);
		return e;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// Message Elements
	// ///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * createMESSAGE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pId
	 * @param pProtocolVersion
	 * @return Element
	 */
	public static Element createMESSAGE(Document pDoc, Element pParentE, String pId,
			String pProtocolVersion) {
		// <!ELEMENT MESSAGE
		// (SIMPLEREQ|MULTIREQ|SIMPLERSP|MULTIRSP|SIMPLEEXPREQ|MULTIEXPREQ|SIMPLEEXPRSP|MULTIEXPRSP)>
		// <!ATTLIST MESSAGE %ID;%PROTOCOLVERSION;>
		Element e = pDoc.createElement("MESSAGE");
		e.setAttribute("ID", pId); // required
		e.setAttribute("PROTOCOLVERSION", pProtocolVersion); // required
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createSIMPLEREQ
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createSIMPLEREQ(Document pDoc, Element pParentE) {
		// <!ELEMENT SIMPLEREQ (METHODCALL|IMETHODCALL)>
		Element e = pDoc.createElement("SIMPLEREQ");
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createSIMPLEREQ
	 * 
	 * @param pDoc
	 * @return Element
	 */
	public static Element createSIMPLEREQ(Document pDoc) {
		// <!ELEMENT SIMPLEREQ (METHODCALL|IMETHODCALL)>
		Element e = pDoc.createElement("SIMPLEREQ");
		return e;
	}

	/**
	 * createMULTIREQ
	 * 
	 * @param pDoc
	 * @return Element
	 */
	public static Element createMULTIREQ(Document pDoc) {
		// <!ELEMENT MULTIREQ (SIMPLEREQ|SIMPLEREQ+)>
		Element e = pDoc.createElement("MULTIREQ");
		return e;
	}

	/**
	 * createMETHODCALL
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createMETHODCALL(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT METHODCALL
		// ((LOCALCLASSPATH|LOCALINSTANCEPATH),PARAMVALUE*)>
		// <!ATTLIST METHODCALL %NAME;>
		Element e = pDoc.createElement("METHODCALL");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createPARAMVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pArg
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createPARAMVALUE(Document pDoc, Element pParentE, CIMArgument<?> pArg)
			throws WBEMException {
		// <!ELEMENT PARAMVALUE
		// (VALUE|VALUE.REFERENCE|VALUE.ARRAY|VALUE.REFARRAY)?>)
		// <!ATTLIST PARAMTYPE %NAME;%PARAMTYPE%>
		if (pArg == null) return null;
		Element e = pDoc.createElement("PARAMVALUE");
		EmbObjBuilder embObjBuilder = new EmbObjBuilder(pDoc, pArg);
		if (pArg.getName() != null) {
			e.setAttribute("NAME", pArg.getName());
		}
		e.setAttribute("PARAMTYPE", embObjBuilder.getTypeStr());
		embObjBuilder.addSign(e);
		embObjBuilder.addValue(e);
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createSIMPLERSP
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createSIMPLERSP(Document pDoc, Element pParentE) {
		// <!ELEMENT SIMPLERSP (METHODRESPONSE|IMETHODRESPONSE)>
		Element e = pDoc.createElement("SIMPLERSP");

		if (pParentE != null) pParentE.appendChild(e);
		return e;
	}

	/**
	 * createSIMPLEEXPRSP
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createSIMPLEEXPRSP(Document pDoc, Element pParentE) {
		// <!ELEMENT SIMPLEEXPRSP (EXPMETHODRESPONSE) >
		Element e = pDoc.createElement("SIMPLEEXPRSP");

		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createMETHODRESPONSE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createMETHODRESPONSE(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT METHODRESPONSE (ERROR|(RETURNVALUE?,PARAMVALUE*))>
		// <!ATTLIST METHODRESPONSE
		// %CIMName;>

		// %CIMName;>
		Element e = pDoc.createElement("METHODRESPONSE");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createIMETHODRESPONSE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createIMETHODRESPONSE(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT IMETHODRESPONSE (ERROR|IRETURNVALUE?)>
		// <!ATTLIST IMETHODRESPONSE
		// %CIMName;>
		// %CIMName;>
		Element e = pDoc.createElement("IMETHODRESPONSE");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createEXPMETHODRESPONSE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createEXPMETHODRESPONSE(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT EXPMETHODRESPONSE (ERROR|IRETURNVALUE?)>
		// <!ATTLIST EXPMETHODRESPONSE
		// %CIMName;>
		Element e = pDoc.createElement("EXPMETHODRESPONSE");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createIRETURNVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @return Element
	 */
	public static Element createIRETURNVALUE(Document pDoc, Element pParentE) {
		// <!ELEMENT IRETURNVALUE
		// (CLASSNAME*|INSTANCENAME*|VALUE*|VALUE.OBJECTWITHPATH*|VALUE.OBJECTWITHLOCALPATH*
		// VALUE.OBJECT*|OBJECTPATH*|QUALIFIER.DECLARATION*|VALUE.ARRAY?|VALUE.REFERENCE?|
		// CLASS*|INSTANCE*|VALUE.NAMEDINSTANCE*)>
		Element e = pDoc.createElement("IRETURNVALUE");
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * <pre>
	 * !ELEMENT RETURNVALUE (VALUE | VALUE.REFERENCE)
	 * !ATTLIST RETURNVALUE
	 * %ParamType;       #IMPLIED
	 * </pre>
	 * 
	 * createRETURNVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pValue
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createRETURNVALUE(Document pDoc, Element pParentE, Object pValue)
			throws WBEMException {
		Element retValE = pDoc.createElement("RETURNVALUE");
		CIMDataType type = CIMDataType.getDataType(pValue);
		retValE.setAttribute("PARAMTYPE", getTypeStr(type));
		createVALUE(pDoc, retValE, pValue);
		pParentE.appendChild(retValE);
		return retValE;
	}

	/**
	 * createIMETHODCALL
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createIMETHODCALL(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT IMETHODCALL (LOCALNAMESPACEPATH,IPARAMVALUE*)>
		// <!ATTLIST IMETHODCALL %NAME;>
		Element e = pDoc.createElement("IMETHODCALL");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createIPARAMVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createIPARAMVALUE(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT PARAMVALUE
		// (VALUE|VALUE.REFERENCE|VALUE.ARRAY|VALUE.NAMEDINSTANCE
		// |CLASSNAME|INSTANCENAME|QUALIFIER.DECLARATION|CLASS|INSTANCE|?>)
		// <!ATTLIST PARAMTYPE %NAME;>
		Element e = pDoc.createElement("IPARAMVALUE");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createERROR
	 * 
	 * @param doc
	 * @param parentE
	 * @param error
	 * @return Element
	 */
	public static Element createERROR(Document doc, Element parentE, CIMError error) {
		// <!ELEMENT ERROR EMPTY>
		// <!ATTLIST ERROR
		// CODE CDATA #REQUIRED
		// DESCRIPTION CDATA #IMPLIED>
		Element e = doc.createElement("ERROR");
		int code;
		if ((code = error.getCode()) > 0) {
			e.setAttribute("CODE", Integer.toString(code));
		}
		String description = error.getDescription();
		if (description != null) {
			e.setAttribute("DESCRIPTION", description);
		}

		parentE.appendChild(e);
		return e;
	}

	/**
	 * ENTITY % QualifierFlavor "OVERRIDABLE (true|false) 'true' TOSUBCLASS
	 * (true|false) 'true' TOINSTANCE (true|false) 'false' TRANSLATABLE
	 * (true|false) 'false'"
	 * 
	 * @param pElement
	 * @param pFlavors
	 */
	private static void setFlavors(Element pElement, int pFlavors) {
		if ((pFlavors & CIMFlavor.TRANSLATE) > 0) {
			pElement.setAttribute("TRANSLATABLE", MOF.TRUE);
		}
		if ((pFlavors & CIMFlavor.DISABLEOVERRIDE) > 0) {
			pElement.setAttribute("OVERRIDABLE", MOF.FALSE);
		}
		if ((pFlavors & CIMFlavor.RESTRICTED) > 0) {
			pElement.setAttribute("TOSUBCLASS", MOF.FALSE);
		}
		/*
		 * if ((pFlavors & CIMFlavor.RESTRICTED)>0) {
		 * pElement.setAttribute("TOSUBCLASS", "true"); }
		 */
	}

	/**
	 * createQUALIFIER_DECLARATION
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pQualifierType
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createQUALIFIER_DECLARATION(Document pDoc, Element pParentE,
			CIMQualifierType<?> pQualifierType) throws WBEMException {
		// <!ELEMENT QUALIFIER.DECLARATION (SCOPE?,(VALUE|VALUE.ARRAY)?)>
		// <!ATTLIST QUALIFIER.DECLARATION
		// %CIMName;
		// %CIMType; #REQUIRED
		// ISARRAY (true|false) #IMPLIED
		// %ArraySize;
		// %QualifierFlavor;>

		String pValueTypeStr = getTypeStr(pQualifierType.getDataType());
		Element qualifierdeclarationE = pDoc.createElement("QUALIFIER.DECLARATION");
		qualifierdeclarationE.setAttribute("NAME", pQualifierType.getName());
		qualifierdeclarationE.setAttribute("TYPE", pValueTypeStr);
		qualifierdeclarationE.setAttribute("ISARRAY",
				pQualifierType.getDataType().isArray() ? MOF.TRUE : MOF.FALSE);

		setFlavors(qualifierdeclarationE, pQualifierType.getFlavor());

		int scopes = pQualifierType.getScope();
		if (scopes > 0) {
			Element scopeE = pDoc.createElement("SCOPE");
			if ((scopes & CIMScope.CLASS) > 0) scopeE.setAttribute("CLASS", MOF.TRUE);
			if ((scopes & CIMScope.ASSOCIATION) > 0) scopeE.setAttribute("ASSOCIATION", MOF.TRUE);
			if ((scopes & CIMScope.REFERENCE) > 0) scopeE.setAttribute("REFERENCE", MOF.TRUE);
			if ((scopes & CIMScope.PROPERTY) > 0) scopeE.setAttribute("PROPERTY", MOF.TRUE);
			if ((scopes & CIMScope.METHOD) > 0) scopeE.setAttribute("METHOD", MOF.TRUE);
			if ((scopes & CIMScope.PARAMETER) > 0) scopeE.setAttribute("PARAMETER", MOF.TRUE);
			if ((scopes & CIMScope.INDICATION) > 0) scopeE.setAttribute("INDICATION", MOF.TRUE);
			qualifierdeclarationE.appendChild(scopeE);
		}

		createVALUE(pDoc, qualifierdeclarationE, pQualifierType.getValue());

		pParentE.appendChild(qualifierdeclarationE);
		return qualifierdeclarationE;
	}

	/**
	 * createQUALIFIER
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pQualifier
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createQUALIFIER(Document pDoc, Element pParentE,
			CIMQualifier<?> pQualifier) throws WBEMException {
		// <!ELEMENT QUALIFIER (VALUE|VALUE.ARRAY)>
		// <!ATTLIST QUALIFIER
		// %CIMName;
		// %CIMType; #REQUIRED
		// %Propagated;
		// %QualifierFlavor;>

		Object value = pQualifier.getValue();
		if (value == null) return null;

		Element qualifierE = createQUALIFIER(pDoc, pParentE, pQualifier.getName(),
				getTypeStr(pQualifier.getDataType()));

		if (pQualifier.isPropagated()) {
			qualifierE.setAttribute("PROPAGATED", "true");
		}
		setFlavors(qualifierE, pQualifier.getFlavor());

		createVALUE(pDoc, qualifierE, value);

		pParentE.appendChild(qualifierE);
		return qualifierE;
	}

	/**
	 * createQUALIFIERS
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pQualifiersA
	 * @throws WBEMException
	 */
	public static void createQUALIFIERS(Document pDoc, Element pParentE,
			CIMQualifier<?>[] pQualifiersA) throws WBEMException {
		if (pQualifiersA == null) return;
		for (int i = 0; i < pQualifiersA.length; i++) {
			createQUALIFIER(pDoc, pParentE, pQualifiersA[i]);
		}
	}

	/**
	 * createPROPERTIES
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pProperties
	 * @throws WBEMException
	 */
	public static void createPROPERTIES(Document pDoc, Element pParentE,
			CIMProperty<?>[] pProperties) throws WBEMException {
		if (pProperties == null) return;
		for (int i = 0; i < pProperties.length; i++) {
			createPROPERTY(pDoc, pParentE, pProperties[i]);
		}
	}

	private static final CIMQualifiedElementInterfaceImpl KEYQUALIFIERS_IMPL = new CIMQualifiedElementInterfaceImpl(
			null, true);

	static final CIMQualifier<Boolean> EMB_OBJ_QUALI = new CIMQualifier<Boolean>("EmbeddedObject",
			CIMDataType.BOOLEAN_T, Boolean.TRUE, CIMFlavor.DISABLEOVERRIDE);

	// ebak: embedded object: CLASS_T or INSTANCE_T?

	/**
	 * isCIMObject
	 * 
	 * @param typeCode
	 * @return boolean
	 */
	public static boolean isCIMObject(int typeCode) {
		return typeCode == CIMDataType.CLASS || typeCode == CIMDataType.OBJECT;
	}

	/**
	 * isCIMObject
	 * 
	 * @param pType
	 * @return boolean
	 */
	public static boolean isCIMObject(CIMDataType pType) {
		return pType == null ? false : isCIMObject(pType.getType());
	}

	/**
	 * getEmbObjTypeStr
	 * 
	 * @param pType
	 * @return String
	 */
	public static String getEmbObjTypeStr(CIMDataType pType) {
		return isCIMObject(pType) ? MOF.DT_STR : getTypeStr(pType);
	}

	private static Document getDoc() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			return docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * cimObjectToXMLString - for embedded object support
	 * 
	 * @param pObj
	 * @return String
	 * @throws WBEMException
	 */
	public static String cimObjectToXMLString(Object pObj) throws WBEMException {
		if (pObj == null) return null;
		Document doc;
		Element e;
		if (pObj instanceof CIMClass) {
			doc = getDoc();
			e = createCLASS(doc, null, (CIMClass) pObj);
		} else if (pObj instanceof CIMInstance) {
			doc = getDoc();
			e = createINSTANCE(doc, null, (CIMInstance) pObj);
		} else {
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, pObj.getClass().getName()
					+ " parameter is not suitable for this method!");
		}
		doc.appendChild(e);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			CimXmlSerializer.serialize(os, doc.getDocumentElement(), false);
		} catch (IOException ex) {
			throw new WBEMException(WBEMException.CIM_ERR_FAILED,
					"XML serialization failed with IOException!", null, ex);
		}
		return os.toString();
	}

	/**
	 * cimObjectArrayToXMLString - for embedded object support
	 * 
	 * @param pObj
	 * @return String[]
	 * @throws WBEMException
	 */
	public static String[] cimObjectArrayToXMLString(Object pObj) throws WBEMException {
		if (pObj == null) return null;
		if (!(pObj instanceof Object[])) throw new WBEMException(WBEMException.CIM_ERR_FAILED,
				"Object[] parameter is required for this method!");
		Object[] objA = (Object[]) pObj;
		String[] strA = new String[objA.length];
		// here we don't deal with the consistency check of the Object[]
		for (int i = 0; i < objA.length; i++)
			strA[i] = cimObjectToXMLString(objA[i]);
		return strA;
	}

	static boolean embObjQualified(CIMQualifiedElementInterface pQualid) {
		return pQualid.hasQualifier("EmbeddedObject") || pQualid.hasQualifier("EmbeddedInstance");
	}

	static class EmbObjBuilder {

		private static final int ATTRIB_ONLY = 0, EO_QUALI = 1, EO_AND_EI_QUALI = 2;

		private int iSignMethod;

		private Document iDoc;

		private CIMTypedElement iTypedE;

		private boolean iXMLQualified;

		private static final boolean iUpperCaseEmbObjEntities = WBEMConfiguration
				.getGlobalConfiguration().upperCaseEmbObjEntities();

		/**
		 * Ctor.
		 * 
		 * @param pDoc
		 * @param pTypedE
		 */
		public EmbObjBuilder(Document pDoc, CIMTypedElement pTypedE) {
			this(pDoc, pTypedE, false);
		}

		/**
		 * Ctor.
		 * 
		 * @param pDoc
		 * @param pTypedE
		 * @param pXMLQualified
		 *            - e.g. CIMProperty is not qualified in JSR48, but
		 *            qualified in CIM-XML
		 */
		public EmbObjBuilder(Document pDoc, CIMTypedElement pTypedE, boolean pXMLQualified) {
			this.iDoc = pDoc;
			this.iTypedE = pTypedE;
			this.iXMLQualified = pXMLQualified;
			String builderCfg = WBEMConfiguration.getGlobalConfiguration().getCimXmlEmbObjBuilder();
			if ("EmbObjQuali".equalsIgnoreCase(builderCfg)) {
				this.iSignMethod = EO_QUALI;
			} else if ("EmbObjAndEmbInstQuali".equalsIgnoreCase(builderCfg)) {
				this.iSignMethod = EO_AND_EI_QUALI;
			} else {
				this.iSignMethod = ATTRIB_ONLY;
			}
		}

		/**
		 * getTypeStr
		 * 
		 * @return String
		 */
		public String getTypeStr() {
			return getEmbObjTypeStr(this.iTypedE.getDataType());
		}

		/**
		 * isArray
		 * 
		 * @return String
		 */
		public boolean isArray() {
			return this.iTypedE.getDataType().isArray();
		}

		private Object getValue() {
			if (!(this.iTypedE instanceof CIMValuedElement)) return null;
			return ((CIMValuedElement<?>) this.iTypedE).getValue();

		}

		/**
		 * addSign
		 * 
		 * @param pElement
		 * @throws WBEMException
		 */
		public void addSign(Element pElement) throws WBEMException {
			if (!isCIMObject(this.iTypedE.getDataType())) return;
			CIMQualifiedElementInterface qualified;
			if (this.iTypedE instanceof CIMQualifiedElementInterface) {
				qualified = (CIMQualifiedElementInterface) this.iTypedE;
				if (embObjQualified(qualified)) return;
			} else {
				qualified = null;
			}
			if (this.iSignMethod == ATTRIB_ONLY || (qualified == null && !this.iXMLQualified)) {
				pElement.setAttribute(iUpperCaseEmbObjEntities ? "EMBEDDEDOBJECT"
						: "EmbeddedObject",
						this.iTypedE.getDataType().getType() == CIMDataType.OBJECT ? "instance"
								: "object");
			} else {
				if (this.iSignMethod == EO_AND_EI_QUALI) {
					addEmbObjOrEmbInstQuali(pElement);
				} else { // EO_QUALI
					createQUALIFIER(this.iDoc, pElement, EMB_OBJ_QUALI);
				}
			}
		}

		private void addEmbObjOrEmbInstQuali(Element pElement) throws WBEMException {
			CIMQualifier<?> signQuali;
			if (this.iTypedE.getDataType().getType() == CIMDataType.OBJECT) {
				CIMInstance inst;
				if (isArray()) {
					CIMInstance[] instA = (CIMInstance[]) getValue();
					inst = instA == null ? null : instA[0];
				} else {
					inst = (CIMInstance) getValue();
				}
				String className = inst == null ? "" : inst.getClassName();
				signQuali = new CIMQualifier<String>("EmbeddedInstance", CIMDataType.STRING_T,
						className, CIMFlavor.DISABLEOVERRIDE);
			} else { // class
				signQuali = EMB_OBJ_QUALI;
			}
			createQUALIFIER(this.iDoc, pElement, signQuali);
		}

		/**
		 * addValue
		 * 
		 * @param pElement
		 * @throws WBEMException
		 */
		public void addValue(Element pElement) throws WBEMException {
			Object value = getValue();
			if (value == null) return;
			if (isCIMObject(this.iTypedE.getDataType())) {
				if (isArray()) {
					value = cimObjectArrayToXMLString(value);
				} else {
					value = cimObjectToXMLString(value);
				}
			}
			createVALUE(this.iDoc, pElement, value);
		}

	}

	/**
	 * createPROPERTY
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pProperty
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createPROPERTY(Document pDoc, Element pParentE, CIMProperty<?> pProperty)
			throws WBEMException {
		CIMDataType propType = pProperty.getDataType();
		Element propertyE;
		EmbObjBuilder embObjBuilder = new EmbObjBuilder(pDoc, pProperty, true);
		String typeStr = embObjBuilder.getTypeStr();
		if (propType.isArray()) {
			propertyE = createPROPERTYARRAY(pDoc, pParentE, pProperty.getName(), typeStr);
		} else if (propType.getType() == CIMDataType.REFERENCE) {
			propertyE = createPROPERTYREFERENCE(pDoc, pParentE, pProperty.getName(), propType
					.getRefClassName());
		} else {
			propertyE = createPROPERTY(pDoc, pParentE, pProperty.getName(), typeStr);
		}

		String classorigin = pProperty.getOriginClass();
		if (classorigin != null && classorigin.length() > 0) propertyE.setAttribute("CLASSORIGIN",
				classorigin);

		if (pProperty.isPropagated()) propertyE.setAttribute("PROPAGATED", MOF.TRUE);
		// FIXME: here key qualifier should be added if the property is a key
		embObjBuilder.addSign(propertyE);
		if (pProperty instanceof CIMClassProperty) {
			createQUALIFIERS(pDoc, propertyE, ((CIMClassProperty<?>) pProperty).getQualifiers());
		} else { // CIMProperty
			if (pProperty.isKey()) createQUALIFIERS(pDoc, propertyE, KEYQUALIFIERS_IMPL
					.getQualifiers());
		}
		embObjBuilder.addValue(propertyE);
		return propertyE;
	}

	/**
	 * createVALUEARRAY
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pValA
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createVALUEARRAY(Document pDoc, Element pParentE, Object[] pValA)
			throws WBEMException {
		Element valuearrayE = (pValA != null && pValA.length > 0 && (pValA[0] instanceof CIMObjectPath || pValA[0] instanceof CIMInstance)) ? createVALUEREFARRAY(
				pDoc, pParentE)
				: createVALUEARRAY(pDoc, pParentE);

		if (pValA != null) for (int i = 0; i < pValA.length; i++) {
			createVALUE(pDoc, valuearrayE, pValA[i]);
		}
		pParentE.appendChild(valuearrayE);
		return valuearrayE;
	}

	/**
	 * createVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pArgValue
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createVALUE(Document pDoc, Element pParentE, Object pArgValue)
			throws WBEMException {
		if (pArgValue == null) return null;

		Element valueE = null;
		if (pArgValue instanceof Object[]) {
			valueE = createVALUEARRAY(pDoc, pParentE, (Object[]) pArgValue);
		} else {
			CIMDataType type = CIMDataType.getDataType(pArgValue);
			if (type != null && type.getType() == CIMDataType.REFERENCE) {
				valueE = createVALUEREFERENCE(pDoc, pParentE);

				CIMObjectPath op = (CIMObjectPath) pArgValue;
				CIMProperty<?>[] keys = op.getKeys();
				if (op.getHost() == null || "".equals(op.getHost())) {
					if (op.getNamespace() == null || "".equals(op.getNamespace())) {
						if (keys.length > 0) {
							createINSTANCENAME(pDoc, valueE, op);
						} else {
							createCLASSNAME(pDoc, valueE, op.getObjectName());
						}
					} else {
						if (keys.length > 0) {
							createLOCALINSTANCEPATH(pDoc, valueE, op);
						} else {
							createLOCALCLASSPATH(pDoc, valueE, op);
						}
					}
				} else {
					if (keys.length > 0) {
						createINSTANCEPATH(pDoc, valueE, op);
					} else {
						createCLASSPATH(pDoc, valueE, op);
					}
				}
				// createOBJECTPATH(doc, valueE, (CIMObjectPath) obj);
			} else {
				if (pArgValue instanceof CIMInstance) {
					valueE = createVALUEREFERENCE(pDoc, pParentE);
					CIMObjectPath cop = ((CIMInstance) pArgValue).getObjectPath();
					createINSTANCENAME(pDoc, valueE, cop);
				} else if (pArgValue instanceof CIMClass) {
					valueE = createVALUE(pDoc, pParentE);
					createCLASS(pDoc, valueE, (CIMClass) pArgValue);
				} else {
					createVALUE(pDoc, pParentE, pArgValue.toString());
				}
			}
		}

		return valueE;
	}

	/**
	 * createINSTANCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pInstance
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createINSTANCE(Document pDoc, Element pParentE, CIMInstance pInstance)
			throws WBEMException {
		// <!ELEMENT INSTANCE
		// (QUALIFIER*,(PROPERTY|PROPERTY.ARRAY|PROPERTY.REFERENCE)*)>
		// <!ATTLIST INSTANCE
		// %ClassName;>

		String className = pInstance.getObjectPath().getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_FAILED,
				"null class name");

		Element instanceE = createINSTANCE(pDoc, pParentE, className);

		// FIXME: in JSR48 CIMInstance doesn't have qualifiers
		// createQUALIFIERS(pDoc, instanceE, pInstance.getQualifiers());
		createPROPERTIES(pDoc, instanceE, pInstance.getProperties());

		if (pParentE != null) pParentE.appendChild(instanceE);

		return instanceE;
	}

	/**
	 * createOBJECTPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createOBJECTPATH(Document pDoc, Element pParentE, CIMObjectPath pPath)
			throws WBEMException {
		Element objectpathE = pDoc.createElement("OBJECTPATH");

		CIMProperty<?>[] keys = pPath.getKeys();
		if (keys.length > 0) {
			createINSTANCEPATH(pDoc, objectpathE, pPath);
		} else {
			createCLASSPATH(pDoc, objectpathE, pPath);
		}

		pParentE.appendChild(objectpathE);
		return objectpathE;
	}

	/**
	 * createOBJECTNAME
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createOBJECTNAME(Document pDoc, Element pParentE, CIMObjectPath pPath)
			throws WBEMException {

		CIMProperty<?>[] keys = pPath.getKeys();
		if (keys.length > 0) { return createINSTANCENAME(pDoc, pParentE, pPath); }
		if (pPath.getObjectName() == null) throw new WBEMException(
				WBEMException.CIM_ERR_INVALID_PARAMETER, "null class name");
		return createCLASSNAME(pDoc, pParentE, pPath.getObjectName());
	}

	/**
	 * createLOCALINSTANCEPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createLOCALINSTANCEPATH(Document pDoc, Element pParentE,
			CIMObjectPath pPath) throws WBEMException {
		Element localinstancepathE = pDoc.createElement("LOCALINSTANCEPATH");

		createLOCALNAMESPACEPATH(pDoc, localinstancepathE, pPath);
		createINSTANCENAME(pDoc, localinstancepathE, pPath);

		pParentE.appendChild(localinstancepathE);
		return localinstancepathE;
	}

	/**
	 * createLOCALCLASSPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createLOCALCLASSPATH(Document pDoc, Element pParentE, CIMObjectPath pPath)
			throws WBEMException {
		Element localinstancepathE = pDoc.createElement("LOCALCLASSPATH");

		createLOCALNAMESPACEPATH(pDoc, localinstancepathE, pPath);

		if (pPath.getObjectName() == null) throw new WBEMException(
				WBEMException.CIM_ERR_INVALID_PARAMETER, "null class name");
		createCLASSNAME(pDoc, localinstancepathE, pPath.getObjectName());

		pParentE.appendChild(localinstancepathE);
		return localinstancepathE;
	}

	/**
	 * createLOCALOBJECTPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createLOCALOBJECTPATH(Document pDoc, Element pParentE, CIMObjectPath pPath)
			throws WBEMException {

		CIMProperty<?>[] keys = pPath.getKeys();
		if (keys.length > 0) { return createLOCALINSTANCEPATH(pDoc, pParentE, pPath); }
		return createLOCALCLASSPATH(pDoc, pParentE, pPath);
	}

	/**
	 * createVALUEREFERENCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createVALUEREFERENCE(Document pDoc, Element pParentE, CIMObjectPath pPath)
			throws WBEMException {
		Element objectpathE = pDoc.createElement("VALUE.REFERENCE");

		String ns = pPath.getNamespace();
		if (pPath.getHost() == null || "".equals(pPath.getHost())) {
			if (pPath.getNamespace() == null || "".equals(ns)) {
				createINSTANCENAME(pDoc, objectpathE, pPath);
			} else {
				createLOCALOBJECTPATH(pDoc, objectpathE, pPath);
			}
		} else {
			CIMProperty<?>[] keys = pPath.getKeys();
			if (keys.length > 0) {
				createINSTANCEPATH(pDoc, objectpathE, pPath);
			} else {
				createCLASSPATH(pDoc, objectpathE, pPath);
			}
		}

		pParentE.appendChild(objectpathE);
		return objectpathE;
	}

	/**
	 * createINSTANCENAME
	 * 
	 * @param doc
	 * @param parentE
	 * @param instanceOP
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createINSTANCENAME(Document doc, Element parentE, CIMObjectPath instanceOP)
			throws WBEMException {

		Element instancenameE = doc.createElement("INSTANCENAME");
		String classname = instanceOP.getObjectName();
		if (classname != null) {
			instancenameE.setAttribute("CLASSNAME", classname);
		}

		CIMProperty<?>[] keysA = instanceOP.getKeys();
		for (int ii = 0; ii < keysA.length; ii++) {
			CIMProperty<?> prop = keysA[ii];
			String propName = prop.getName();
			CIMDataType propType = prop.getDataType();
			Object propValue = prop.getValue();
			// if (_pValue == null) TODO what happened when a KeyBinding has a
			// null property value?
			/*
			 * ebak: test support for #1666336 EnumInstance has attributes
			 * filled from others <!ELEMENT INSTANCENAME (KEYBINDING* |
			 * KEYVALUE? | VALUE.REFERENCE?)> Handling direct KEYVALUE and
			 * VALUE.REFERENCE children is difficult, KEYBINDING can wrap them
			 * -> dropping out KEYVALUE? | VALUE.REFERENCE?.
			 * 
			 * <!ELEMENT KEYBINDING (KEYVALUE | VALUE.REFERENCE)> <!ATTLIST
			 * KEYBINDING %CIMName; >
			 * 
			 * <!ELEMENT KEYVALUE (#PCDATA)> <!ATTLIST KEYVALUE VALUETYPE
			 * (string | boolean | numeric) "string" %CIMType; #IMPLIED> - non
			 * reference values can be null
			 * 
			 * <!ELEMENT VALUE.REFERENCE (CLASSPATH | LOCALCLASSPATH | CLASSNAME
			 * | INSTANCEPATH | LOCALINSTANCEPATH | INSTANCENAME)> - reference
			 * value shouldn't be null
			 */
			if (propType == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER,
					"Type of property or key cannot be a null! " + propName + " in ObjectPath "
							+ instanceOP.toString() + " has null type."); }

			if (propValue == null) {
				if (propType.getType() == CIMDataType.REFERENCE) { throw new WBEMException(
						WBEMException.CIM_ERR_INVALID_PARAMETER,
						"Value of reference cannot be null! " + propName + " in ObjectPath "
								+ instanceOP.toString() + " has null value."); }
			}

			Element keybindingE = createKEYBINDING(doc, instancenameE, propName);
			if (propType.getType() == CIMDataType.REFERENCE) {
				CIMObjectPath refOP = (CIMObjectPath) propValue;

				createVALUEREFERENCE(doc, keybindingE, refOP);
			} else {
				String valueStr = propValue == null ? null : propValue.toString();
				String valueTypeStr = getTypeStr(propType);
				createKEYVALUE(doc, keybindingE, valueTypeStr, valueStr);
			}
		}

		parentE.appendChild(instancenameE);
		return instancenameE;
	}

	/**
	 * createCLASSPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 */
	public static Element createCLASSPATH(Document pDoc, Element pParentE, CIMObjectPath pPath) {
		Element classpathE = pDoc.createElement("CLASSPATH");

		// ebak: NAMESPACEPATH is necessary
		createNAMESPACEPATH(pDoc, classpathE, pPath);
		createCLASSNAME(pDoc, classpathE, pPath.getObjectName());

		pParentE.appendChild(classpathE);
		return classpathE;
	}

	/**
	 * createPARAMETERS
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pParameters
	 * @throws WBEMException
	 */
	public static void createPARAMETERS(Document pDoc, Element pParentE,
			CIMParameter<?>[] pParameters) throws WBEMException {
		if (pParameters == null) return;
		for (int i = 0; i < pParameters.length; i++) {
			createPARAMETER(pDoc, pParentE, pParameters[i]);
		}
	}

	/**
	 * createPARAMETER
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pParameter
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createPARAMETER(Document pDoc, Element pParentE,
			CIMParameter<?> pParameter) throws WBEMException {
		Element parameterE;
		CIMDataType type = pParameter.getDataType();
		int typeCode = type.getType();
		EmbObjBuilder embObjBuilder = new EmbObjBuilder(pDoc, pParameter);
		String typeStr = embObjBuilder.getTypeStr();
		if (type.isArray()) {
			if (typeCode == CIMDataType.REFERENCE) {
				parameterE = pDoc.createElement("PARAMETER.REFARRAY");
				String refclass = type.getRefClassName();
				if (refclass != null && refclass.length() > 0) parameterE.setAttribute(
						"REFERENCECLASS", type.getRefClassName());
			} else {
				parameterE = pDoc.createElement("PARAMETER.ARRAY");
				parameterE.setAttribute("TYPE", typeStr);
			}
		} else {
			if (typeCode == CIMDataType.REFERENCE) {
				parameterE = pDoc.createElement("PARAMETER.REFERENCE");
				String refclass = type.getRefClassName();
				if (refclass != null && refclass.length() > 0) parameterE.setAttribute(
						"REFERENCECLASS", type.getRefClassName());
			} else {
				parameterE = pDoc.createElement("PARAMETER");
				parameterE.setAttribute("TYPE", typeStr);
			}
		}
		parameterE.setAttribute("NAME", pParameter.getName());
		embObjBuilder.addSign(parameterE);
		createQUALIFIERS(pDoc, parameterE, pParameter.getQualifiers());

		pParentE.appendChild(parameterE);
		return parameterE;
	}

	/**
	 * createMETHODS
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pMethods
	 * @param pClassName
	 * @throws WBEMException
	 */
	public static void createMETHODS(Document pDoc, Element pParentE, CIMMethod<?>[] pMethods,
			String pClassName) throws WBEMException {
		for (int i = 0; i < pMethods.length; i++) {
			createMETHOD(pDoc, pParentE, pMethods[i], pClassName);
		}
	}

	/**
	 * createMETHOD
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pMethod
	 * @param pClassName
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createMETHOD(Document pDoc, Element pParentE, CIMMethod<?> pMethod,
			String pClassName) throws WBEMException {
		Element methodE = pDoc.createElement("METHOD");

		methodE.setAttribute("NAME", pMethod.getName());
		EmbObjBuilder embObjBuilder = new EmbObjBuilder(pDoc, pMethod);
		String typeStr = embObjBuilder.getTypeStr();
		methodE.setAttribute("TYPE", typeStr);
		String classorigin = pMethod.getOriginClass();

		if (classorigin != null && classorigin.length() != 0) methodE.setAttribute("CLASSORIGIN",
				classorigin);

		// 17459
		if (pMethod.isPropagated()) {
			methodE.setAttribute("PROPAGATED", MOF.TRUE);
		} else {
			methodE.setAttribute("PROPAGATED", MOF.FALSE);
		}
		// ebak: embedded object support
		embObjBuilder.addSign(methodE);

		createQUALIFIERS(pDoc, methodE, pMethod.getQualifiers());
		createPARAMETERS(pDoc, methodE, pMethod.getParameters());

		pParentE.appendChild(methodE);
		return methodE;
	}

	/*
	 * private static String elementStr(Element pE) { try {
	 * ByteArrayOutputStream os = new ByteArrayOutputStream();
	 * CimXmlSerializer.serialize(os, pE, true); return new
	 * String(os.toByteArray()); } catch (RuntimeException e) { throw e; } catch
	 * (Exception e) { throw new RuntimeException(e); } }
	 */

	/**
	 * createCLASS
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pClass
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createCLASS(Document pDoc, Element pParentE, CIMClass pClass)
			throws WBEMException {

		Element classE = createCLASS(pDoc, pParentE, pClass.getName(), pClass.getSuperClassName());

		createQUALIFIERS(pDoc, classE, pClass.getQualifiers());
		createPROPERTIES(pDoc, classE, pClass.getProperties());
		createMETHODS(pDoc, classE, pClass.getMethods(), pClass.getName());

		// parentE.appendChild(classE);
		return classE;
	}

	/**
	 * createHOST
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pHost
	 * @return Element
	 */
	public static Element createHOST(Document pDoc, Element pParentE, String pHost) {
		Element hostE = pDoc.createElement("HOST");

		Text hostT = pDoc.createTextNode(pHost);
		hostE.appendChild(hostT);

		pParentE.appendChild(hostE);
		return hostE;
	}

	/**
	 * createNAMESPACEPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 */
	public static Element createNAMESPACEPATH(Document pDoc, Element pParentE, CIMObjectPath pPath) {
		Element namespacepathE = pDoc.createElement("NAMESPACEPATH");

		String host = pPath.getHost();
		if (host == null) {
			// try {
			// host = InetAddress.getLocalHost().getHostAddress();
			// } catch (Exception e) {
			// throw new CIMXMLBuilderException("Error while resolving
			// hostname");
			// }
			host = "unassigned-hostname";
		}
		createHOST(pDoc, namespacepathE, host);
		createLOCALNAMESPACEPATH(pDoc, namespacepathE, pPath);

		pParentE.appendChild(namespacepathE);
		return namespacepathE;
	}

	/**
	 * createINSTANCEPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createINSTANCEPATH(Document pDoc, Element pParentE, CIMObjectPath pPath)
			throws WBEMException {
		Element instancepathE = pDoc.createElement("INSTANCEPATH");

		createNAMESPACEPATH(pDoc, instancepathE, pPath);
		createINSTANCENAME(pDoc, instancepathE, pPath);
		pParentE.appendChild(instancepathE);
		return instancepathE;
	}

	/**
	 * createVALUENAMEDINSTANCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pPath
	 * @param pInst
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createVALUENAMEDINSTANCE(Document pDoc, Element pParentE,
			CIMObjectPath pPath, CIMInstance pInst) throws WBEMException {
		Element valuenamedinstanceE = pDoc.createElement("VALUE.NAMEDINSTANCE");
		createINSTANCENAME(pDoc, valuenamedinstanceE, pPath);
		createINSTANCE(pDoc, valuenamedinstanceE, pInst);

		pParentE.appendChild(valuenamedinstanceE);
		return valuenamedinstanceE;
	}

	/**
	 * createVALUENAMEDINSTANCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pInst
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createVALUENAMEDINSTANCE(Document pDoc, Element pParentE,
			CIMInstance pInst) throws WBEMException {
		Element valuenamedinstanceE = pDoc.createElement("VALUE.NAMEDINSTANCE");
		createINSTANCENAME(pDoc, valuenamedinstanceE, pInst.getObjectPath());
		createINSTANCE(pDoc, valuenamedinstanceE, pInst);

		pParentE.appendChild(valuenamedinstanceE);
		return valuenamedinstanceE;
	}

	private static CIMObjectPath changeNameSpace(CIMNamedElementInterface pNamedElement,
			String pNameSpace) {
		CIMObjectPath path = pNamedElement.getObjectPath();
		return new CIMObjectPath(path.getScheme(), path.getHost(), path.getPort(), pNameSpace, path
				.getObjectName(), path.getKeys());
	}

	private static CIMClass changeClassNameSpace(CIMClass pClass, String pNameSpace) {
		CIMObjectPath newOp = changeNameSpace(pClass, pNameSpace);
		return new CIMClass(newOp, pClass.getSuperClassName(), pClass.getQualifiers(), pClass
				.getProperties(), pClass.getMethods(), pClass.isAssociation(), pClass.isKeyed());
	}

	private static CIMInstance changeInstanceNameSpace(CIMInstance pInst, String pNameSpace) {
		CIMObjectPath newOp = changeNameSpace(pInst, pNameSpace);
		return new CIMInstance(newOp, pInst.getProperties());
	}

	/**
	 * createVALUEOBJECTWITHPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pObj
	 * @param pNameSpace
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createVALUEOBJECTWITHPATH(Document pDoc, Element pParentE, Object pObj,
			String pNameSpace) throws WBEMException {
		Element valueobjectwithpathE = pDoc.createElement("VALUE.OBJECTWITHPATH");
		if (pObj instanceof CIMClass) {
			CIMClass clazz = (CIMClass) pObj;

			if (clazz.getObjectPath().getNamespace() == null
					|| clazz.getObjectPath().getNamespace().length() == 0) {
				// ebak: changing the namespace in clazz's objectPath
				// clazz.getObjectPath().setNameSpace(pNameSpace);
				clazz = changeClassNameSpace(clazz, pNameSpace);
			}

			createCLASSPATH(pDoc, valueobjectwithpathE, clazz.getObjectPath());
			createCLASS(pDoc, valueobjectwithpathE, clazz);
		} else if (pObj instanceof CIMInstance) {
			CIMInstance inst = (CIMInstance) pObj;
			if (inst.getObjectPath().getNamespace() == null
					|| inst.getObjectPath().getNamespace().length() == 0) {
				// ebak: changing the namespace ins inst's objectPath
				// inst.getObjectPath().setNameSpace(pNameSpace);
				inst = changeInstanceNameSpace(inst, pNameSpace);
			}
			createINSTANCEPATH(pDoc, valueobjectwithpathE, inst.getObjectPath());
			createINSTANCE(pDoc, valueobjectwithpathE, inst);
		}

		pParentE.appendChild(valueobjectwithpathE);
		return valueobjectwithpathE;
	}

	/**
	 * createVALUEOBJECTWITHLOCALPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pObj
	 * @param pNameSpace
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createVALUEOBJECTWITHLOCALPATH(Document pDoc, Element pParentE,
			Object pObj, String pNameSpace) throws WBEMException {
		Element valueobjectwithpathE = pDoc.createElement("VALUE.OBJECTWITHLOCALPATH");
		if (pObj instanceof CIMClass) {
			CIMClass clazz = (CIMClass) pObj;

			if (clazz.getObjectPath().getNamespace() == null
					|| clazz.getObjectPath().getNamespace().length() == 0) {
				// ebak: changing clazz's objectPath
				// clazz.getObjectPath().setNameSpace(pNameSpace);
				clazz = changeClassNameSpace(clazz, pNameSpace);
			}
			createLOCALCLASSPATH(pDoc, valueobjectwithpathE, clazz.getObjectPath());
			createCLASS(pDoc, valueobjectwithpathE, clazz);
		} else if (pObj instanceof CIMInstance) {
			CIMInstance inst = (CIMInstance) pObj;
			if (inst.getObjectPath().getNamespace() == null
					|| inst.getObjectPath().getNamespace().length() == 0) {
				// inst.getObjectPath().setNameSpace(pNameSpace);
				inst = changeInstanceNameSpace(inst, pNameSpace);
			}
			createLOCALINSTANCEPATH(pDoc, valueobjectwithpathE, inst.getObjectPath());
			createINSTANCE(pDoc, valueobjectwithpathE, inst);
		}

		pParentE.appendChild(valueobjectwithpathE);
		return valueobjectwithpathE;
	}

	/**
	 * createIRETURNVALUE_ERROR
	 * 
	 * @param doc
	 * @param parentE
	 * @param error
	 * @return Element
	 */
	public static Element createIRETURNVALUE_ERROR(Document doc, Element parentE, CIMError error) {
		Element ireturnvalueE = doc.createElement("IRETURNVALUE");

		parentE.appendChild(ireturnvalueE);
		return ireturnvalueE;
	}

	/**
	 * createIRETURNVALUE_GETINSTANCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pInst
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createIRETURNVALUE_GETINSTANCE(Document pDoc, Element pParentE,
			CIMInstance pInst) throws WBEMException {
		Element ireturnvalueE = pDoc.createElement("IRETURNVALUE");
		createINSTANCENAME(pDoc, ireturnvalueE, pInst.getObjectPath());

		return ireturnvalueE;
	}

	/**
	 * createIRETURNVALUE_ASSOCIATORS_NAMES
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pResultSet
	 * @return Element
	 * @throws Exception
	 */
	public static Element createIRETURNVALUE_ASSOCIATORS_NAMES(Document pDoc, Element pParentE,
			CIMObjectPath[] pResultSet) throws Exception {
		Element ireturnvalueE = pDoc.createElement("IRETURNVALUE");

		if (pResultSet != null) {
			for (int i = 0; i < pResultSet.length; i++) {
				CIMObjectPath path = pResultSet[i];

				if (path.getHost() == null || "".equals(path.getHost())) createLOCALOBJECTPATH(
						pDoc, ireturnvalueE, path);
				else createOBJECTPATH(pDoc, ireturnvalueE, path);
			}
		}
		pParentE.appendChild(ireturnvalueE);
		return ireturnvalueE;
	}

	/**
	 * createIRETURNVALUE_ASSOCIATORS
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pResultSet
	 * @param pNameSpace
	 * @return Element
	 * @throws Exception
	 */
	public static Element createIRETURNVALUE_ASSOCIATORS(Document pDoc, Element pParentE,
			Object[] pResultSet, String pNameSpace) throws Exception {
		Element ireturnvalueE = pDoc.createElement("IRETURNVALUE");
		if (pResultSet != null) {
			for (int i = 0; i < pResultSet.length; i++) {
				Object obj = pResultSet[i];
				CIMObjectPath op = null;
				if (obj instanceof CIMClass) {
					op = ((CIMClass) obj).getObjectPath();
				} else if (obj instanceof CIMInstance) {
					op = ((CIMInstance) obj).getObjectPath();
				} else {
					throw new WBEMException(WBEMException.CIM_ERR_FAILED,
							"object in result set neither class nor instance!");
				}
				if (op.getHost() == null || "".equals(op.getHost())) {
					createVALUEOBJECTWITHLOCALPATH(pDoc, ireturnvalueE, obj, pNameSpace);
				} else {
					createVALUEOBJECTWITHPATH(pDoc, ireturnvalueE, obj, pNameSpace);
				}
			}
		}
		pParentE.appendChild(ireturnvalueE);
		return ireturnvalueE;
	}

	/**
	 * createIRETURNVALUE_ENUMERATE_INSTANCENAME
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pResultSet
	 * @param pNameSpace
	 * @return Element
	 * @throws Exception
	 */
	public static Element createIRETURNVALUE_ENUMERATE_INSTANCENAME(Document pDoc,
			Element pParentE, Object[] pResultSet, String pNameSpace) throws Exception {
		Element ireturnvalueE = pDoc.createElement("IRETURNVALUE");
		if (pResultSet != null) {
			for (int i = 0; i < pResultSet.length; i++) {
				Object obj = pResultSet[i];
				CIMObjectPath op = null;
				if (obj instanceof CIMClass) {
					op = ((CIMClass) obj).getObjectPath();
				} else if (obj instanceof CIMInstance) {
					op = ((CIMInstance) obj).getObjectPath();
				} else {
					throw new WBEMException(WBEMException.CIM_ERR_FAILED,
							"object in result set neither class nor instance!");
				}
				if (op.getHost() == null || "".equals(op.getHost())) {
					createVALUEOBJECTWITHLOCALPATH(pDoc, ireturnvalueE, obj, pNameSpace);
				} else {
					createVALUEOBJECTWITHPATH(pDoc, ireturnvalueE, obj, pNameSpace);
				}
			}
		}
		pParentE.appendChild(ireturnvalueE);
		return ireturnvalueE;
	}

	/**
	 * createIRETURNVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pResultSet
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createIRETURNVALUE(Document pDoc, Element pParentE, Object[] pResultSet)
			throws WBEMException {
		Element ireturnvalueE = pDoc.createElement("IRETURNVALUE");
		if (pResultSet != null && pResultSet.length > 0) {
			Object obj = pResultSet[0];
			if (obj instanceof CIMClass) {
				for (int i = 0; i < pResultSet.length; i++) {
					CIMClass clazz = (CIMClass) pResultSet[i];
					Element classnameE = pDoc.createElement("CLASSNAME");
					ireturnvalueE.appendChild(classnameE);
					if (clazz.getName() != null) classnameE.setAttribute("NAME", clazz.getName());
				}
			} else if (obj instanceof CIMInstance) {
				for (int i = 0; i < pResultSet.length; i++) {
					CIMInstance inst = (CIMInstance) pResultSet[i];
					createVALUENAMEDINSTANCE(pDoc, ireturnvalueE, inst);
				}
			}
		}
		pParentE.appendChild(ireturnvalueE);
		return ireturnvalueE;
	}

	/**
	 * createIRETURNVALUE_ENUMERATE_CLASSNAME
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pResultSet
	 * @return Element
	 */
	public static Element createIRETURNVALUE_ENUMERATE_CLASSNAME(Document pDoc, Element pParentE,
			CIMClass[] pResultSet) {
		Element ireturnvalueE = pDoc.createElement("IRETURNVALUE");
		if (pResultSet != null && pResultSet.length > 0) {
			for (int i = 0; i < pResultSet.length; i++) {
				CIMClass clazz = pResultSet[i];

				Element classnameE = pDoc.createElement("CLASSNAME");
				ireturnvalueE.appendChild(classnameE);
				if (clazz.getName() != null) classnameE.setAttribute("NAME", clazz.getName());
			}
		}
		pParentE.appendChild(ireturnvalueE);
		return ireturnvalueE;
	}

	/**
	 * createIndication_response
	 * 
	 * @param doc
	 * @param ID
	 * @param error
	 * @return Element
	 */
	// ebak: [ 1656285 ] IndicationHandler does not accept non-Integer message
	// ID
	public static Element createIndication_response(Document doc, String ID, CIMError error) {

		// xmlBuilder.create XML
		Element cimE = createCIM(doc);
		Element messageE = createMESSAGE(doc, cimE, ID, "1.0");
		Element simpleexprspE = createSIMPLEEXPRSP(doc, messageE);
		Element expmethodresponseE = createEXPMETHODRESPONSE(doc, simpleexprspE, "ExportIndication");
		if (error == null) {
			createIRETURNVALUE(doc, expmethodresponseE);
		} else {
			createERROR(doc, expmethodresponseE, error);
		}
		// Element
		return cimE;
	}

	/**
	 * createIRETURNVALUE_ENUMERATE_INSTANCE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pResultSet
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element createIRETURNVALUE_ENUMERATE_INSTANCE(Document pDoc, Element pParentE,
			CIMInstance[] pResultSet) throws WBEMException {
		Element ireturnvalueE = pDoc.createElement("IRETURNVALUE");
		if (pResultSet != null && pResultSet.length > 0) {
			for (int i = 0; i < pResultSet.length; i++) {
				createVALUENAMEDINSTANCE(pDoc, ireturnvalueE, pResultSet[i]);
			}
		}
		pParentE.appendChild(ireturnvalueE);
		return ireturnvalueE;
	}

	/**
	 * getTypeStr
	 * 
	 * @param pType
	 * @return String
	 */
	public static String getTypeStr(CIMDataType pType) {
		if (pType == null) return "string";
		if (pType.getType() == CIMDataType.REFERENCE) return MOF.REFERENCE;
		return MOF.dataType(pType);
	}

	/**
	 * getOpTypeStr
	 * 
	 * @param pType
	 * @return String
	 */
	public static String getOpTypeStr(CIMDataType pType) {
		return getTypeStr(pType);
	}

	private static final Pattern NAMESPACE_SPLIT_PATTERN = Pattern.compile("/+");

	/**
	 * createLOCALNAMESPACEPATH
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createLOCALNAMESPACEPATH(Document pDoc, Element pParentE,
			CIMObjectPath pName) {
		if (pName == null) return null;
		// TODO: name(ObjectPath) should not be null, should an exception be
		// thrown?
		// This assumes that the NameSpace does not consist exclusively of
		// empties like "////"
		Element localnamespacepathE = pDoc.createElement("LOCALNAMESPACEPATH");
		String nameSpace = pName.getNamespace();
		if (nameSpace != null) {
			String[] nsA = NAMESPACE_SPLIT_PATTERN.split(nameSpace);
			for (int i = 0; i < nsA.length; i++)
				if (nsA[i] != null && nsA[i].length() > 0) createNAMESPACE(pDoc,
						localnamespacepathE, nsA[i]);
		}
		pParentE.appendChild(localnamespacepathE);

		return localnamespacepathE;
	}

	/**
	 * createSIMPLEEXPREQ
	 * 
	 * @param pDoc
	 * @return Element
	 */
	public static Element createSIMPLEEXPREQ(Document pDoc) {
		// <!ELEMENT SIMPLEEXPREQ (EXPMETHODCALL)>
		Element e = pDoc.createElement("SIMPLEEXPREQ");
		return e;
	}

	/**
	 * createEXPMETHODCALL
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createEXPMETHODCALL(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT EXPMETHODCALL (EXPPARAMVALUE*)>
		// <!ATTLIST EXPMETHODCALL %NAME>
		Element e = pDoc.createElement("EXPMETHODCALL");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}

	/**
	 * createEXPPARAMVALUE
	 * 
	 * @param pDoc
	 * @param pParentE
	 * @param pName
	 * @return Element
	 */
	public static Element createEXPPARAMVALUE(Document pDoc, Element pParentE, String pName) {
		// <!ELEMENT EXPPARAMVALUE (INSTANCE? | VALUE? | METHODRESPONSE? |
		// IMETHODRESPONSE?)>
		// <!ATTLIST EXPPARAMVALUE %NAME>
		Element e = pDoc.createElement("EXPPARAMVALUE");
		if (pName != null) {
			e.setAttribute("NAME", pName);
		}
		pParentE.appendChild(e);
		return e;
	}
}
