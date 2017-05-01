/**
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
 *------------------------------------------------------------------------------- 
 * 13521      2004-11-26  thschaef     XML Request Composition for static method call is wrong
 * 18075      2005-08-11  pineiro5     Can not use method CIMClient.invokeMethod
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1365086 	  2006-10-25  ebak	       Possible bug in createQualifier
 * 1565892    2006-11-16  lupusalex    Make SBLIM client JSR48 compliant
 * 1610046    2006-12-18  lupusalex    Does not escape trailing spaces <KEYVALUE> 
 * 1610046    2007-01-10  lupusalex    Rework: Does not escape trailing spaces <KEYVALUE>
 * 1649611    2007-01-31  lupusalex    Interop issue: Quotes not escaped by client
 * 1671502    2007-02-28  lupusalex    Remove dependency from Xerces
 * 1660756    2007-03-02  ebak         Embedded object support
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 1715027    2007-05-08  lupusalex    Make message id random
 * 1719991    2007-05-16  ebak         FVT: regression ClassCastException in EmbObjHandler
 * 1734888    2007-06-11  ebak         Wrong reference building in METHODCALL request
 * 1827728    2007-11-12  ebak         embeddedInstances: attribute EmbeddedObject not set
 * 1827728    2007-11-20  ebak         rework: embeddedInstances: attribute EmbeddedObject not set
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 2865222    2009-09-29  raman_arora  enumerateQualifierTypes shouldn't require a class name
 * 2858933    2009-10-12  raman_arora  JSR48 new APIs: associatorClasses & associatorInstances
 * 2886829    2009-11-18  raman_arora  JSR48 new APIs: referenceClasses & referenceInstances
 * 2944219    2010-02-05  blaschke-oss Problem with pull operations using client against EMC CIMOM
 * 3027479    2010-07-09  blaschke-oss Dead store to local variable
 * 3062747    2010-09-21  blaschke-oss SblimCIMClient does not log all CIM-XML responces.
 * 3514537    2012-04-03  blaschke-oss TCK: execQueryInstances requires boolean, not Boolean
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 * 3527580    2012-05-17  blaschke-oss WBEMClient should not throw IllegalArgumentException
 * 3601894    2013-01-23  blaschke-oss Enhance HTTP and CIM-XML tracing
 *    2616    2013-02-23  blaschke-oss Add new API WBEMClientSBLIM.sendIndication()
 *    2689    2013-10-10  blaschke-oss createMETHODCALL should not add PARAMTYPE attribute
 */

package org.sblim.cimclient.internal.cimxml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.cim.CIMArgument;
import javax.cim.CIMClass;
import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMNamedElementInterface;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.CIMQualifierType;
import javax.cim.CIMValuedElement;
import javax.cim.UnsignedInteger32;
import javax.wbem.WBEMException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sblim.cimclient.internal.logging.TimeStamp;
import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.sblim.cimclient.internal.wbem.operations.CIMAssociatorNamesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMAssociatorsOp;
import org.sblim.cimclient.internal.wbem.operations.CIMCreateClassOp;
import org.sblim.cimclient.internal.wbem.operations.CIMCreateInstanceOp;
import org.sblim.cimclient.internal.wbem.operations.CIMCreateNameSpaceOp;
import org.sblim.cimclient.internal.wbem.operations.CIMCreateQualifierTypeOp;
import org.sblim.cimclient.internal.wbem.operations.CIMDeleteClassOp;
import org.sblim.cimclient.internal.wbem.operations.CIMDeleteInstanceOp;
import org.sblim.cimclient.internal.wbem.operations.CIMDeleteQualifierTypeOp;
import org.sblim.cimclient.internal.wbem.operations.CIMEnumClassNamesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMEnumClassesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMEnumInstanceNamesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMEnumInstancesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMEnumNameSpaceOp;
import org.sblim.cimclient.internal.wbem.operations.CIMEnumQualifierTypesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMExecQueryOp;
import org.sblim.cimclient.internal.wbem.operations.CIMGetClassOp;
import org.sblim.cimclient.internal.wbem.operations.CIMGetInstanceOp;
import org.sblim.cimclient.internal.wbem.operations.CIMGetPropertyOp;
import org.sblim.cimclient.internal.wbem.operations.CIMGetQualifierTypeOp;
import org.sblim.cimclient.internal.wbem.operations.CIMInvokeMethodOp;
import org.sblim.cimclient.internal.wbem.operations.CIMOperation;
import org.sblim.cimclient.internal.wbem.operations.CIMReferenceNamesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMReferencesOp;
import org.sblim.cimclient.internal.wbem.operations.CIMSetClassOp;
import org.sblim.cimclient.internal.wbem.operations.CIMSetInstanceOp;
import org.sblim.cimclient.internal.wbem.operations.CIMSetPropertyOp;
import org.sblim.cimclient.internal.wbem.operations.CIMSetQualifierTypeOp;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class CIMClientXML_HelperImpl is responsible for building CIM-XML requests
 * and responses.
 */
public class CIMClientXML_HelperImpl {

	private static class Counter {

		private int iCounter;

		protected Counter(int pCounter) {
			this.iCounter = pCounter;
		}

		protected int incrementAndGet() {
			return ++this.iCounter;
		}
	}

	private static final String VERSION = "1.0";

	private static final String ASSOCIATOR_NAMES = "AssociatorNames";

	// Pull Enumeration variables
	private static final String ASSOC_CLASS = "AssocClass";

	private static final String CLASS_NAME = "ClassName";

	private static final String CONTINUE_ON_ERROR = "ContinueOnError";

	private static final String DEEP_INHERITANCE = "DeepInheritance";

	private static final String ENUMERATION_CONTEXT = "EnumerationContext";

	private static final String FILTER_QUERY_LANGUAGE = "FilterQueryLanguage";

	private static final String FILTER_QUERY = "FilterQuery";

	private static final String INCLUDE_CLASS_ORIGIN = "IncludeClassOrigin";

	private static final String INSTANCE_NAME = "InstanceName";

	private static final String MAX_OBJECT_COUNT = "MaxObjectCount";

	private static final String PROPERTY_LIST = "PropertyList";

	private static final String OPERATION_TIMEOUT = "OperationTimeout";

	private static final String RESULT_CLASS = "ResultClass";

	private static final String RETURN_QUERY_RESULT_CLASS = "ReturnQueryResultClass";

	private static final String QUERY_RESULT_CLASS = "QueryResultClass";

	private static final String ROLE = "Role";

	private static final String RESULT_ROLE = "ResultRole";

	private static final Random RANDOM = new Random();

	private static final int MAX_ID = 1 << 20;

	private final ThreadLocal<Counter> iCurrentId = new ThreadLocal<Counter>();

	private final DocumentBuilder iBuilder;

	private static String valueStr(CIMValuedElement<?> pE) {
		Object o = pE.getValue();
		return o == null ? MOF.NULL : o.toString();
	}

	/**
	 * Ctor.
	 * 
	 * @throws ParserConfigurationException
	 */
	public CIMClientXML_HelperImpl() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		this.iBuilder = factory.newDocumentBuilder();
	}

	/**
	 * getDocumentBuilder
	 * 
	 * @return DocumentBuilder
	 */
	public DocumentBuilder getDocumentBuilder() {
		return this.iBuilder;
	}

	/**
	 * newDocument
	 * 
	 * @return Document
	 */
	public Document newDocument() {
		return this.iBuilder.newDocument();
	}

	/**
	 * parse
	 * 
	 * @param pIS
	 * @return Document
	 * @throws IOException
	 * @throws SAXException
	 */
	public Document parse(InputSource pIS) throws IOException, SAXException {
		if (pIS == null) throw new IllegalArgumentException("null input stream argument");
		return this.iBuilder.parse(pIS);
	}

	/**
	 * Serializes a given DOM document as (CIM-)XML to a given output stream
	 * 
	 * @param pOS
	 *            The output stream
	 * @param pDoc
	 *            The documents
	 * @throws IOException
	 *             Whenever something goes wrong
	 */
	public static void serialize(OutputStream pOS, Document pDoc) throws IOException {
		CimXmlSerializer.serialize(pOS, pDoc, false);
	}

	/**
	 * Serializes a given DOM document as (CIM-)XML to a given output stream.
	 * The document is pretty wrapped and indented and surrounded with markers
	 * for the begin and end.
	 * 
	 * @param pOS
	 *            The output stream
	 * @param pDoc
	 *            The documents
	 * @throws IOException
	 */
	public static void dumpDocument(OutputStream pOS, Document pDoc) throws IOException {
		dumpDocument(pOS, pDoc, null);
	}

	/**
	 * Serializes a given DOM document as (CIM-)XML to a given output stream.
	 * The document is pretty wrapped and indented and surrounded with markers
	 * for the begin and end.
	 * 
	 * @param pOS
	 *            The output stream
	 * @param pDoc
	 *            The documents
	 * @param pOrigin
	 *            The origin of the output stream (request, indication response,
	 *            etc.)
	 * @throws IOException
	 */
	public static void dumpDocument(OutputStream pOS, Document pDoc, String pOrigin)
			throws IOException {
		// debug
		if (pOS == null) { return; }
		if (pOrigin == null) pOrigin = "unknown";
		pOS.write("<--- ".getBytes());
		pOS.write(pOrigin.getBytes());
		pOS.write(" begin ".getBytes());
		pOS.write(TimeStamp.formatWithMillis(System.currentTimeMillis()).getBytes());
		pOS.write(" -----\n".getBytes());
		CimXmlSerializer.serialize(pOS, pDoc, true);
		pOS.write("\n---- ".getBytes());
		pOS.write(pOrigin.getBytes());
		pOS.write(" end ------>\n".getBytes());
	}

	/**
	 * createCIMMessage
	 * 
	 * @param pDoc
	 * @param pRequestE
	 * @return Element
	 */
	public Element createCIMMessage(Document pDoc, Element pRequestE) {
		Element cimE = CIMXMLBuilderImpl.createCIM(pDoc);
		Element messageE = CIMXMLBuilderImpl.createMESSAGE(pDoc, cimE, String.valueOf(getNextId()),
				VERSION);
		if (pRequestE != null) {
			messageE.appendChild(pRequestE);
		}
		return messageE;
	}

	/**
	 * createMultiReq
	 * 
	 * @param pDoc
	 * @return Element
	 */
	public Element createMultiReq(Document pDoc) {
		Element multireqE = CIMXMLBuilderImpl.createMULTIREQ(pDoc);
		return multireqE;
	}

	/**
	 * associatorNames_request
	 * 
	 * @param pDoc
	 * @param pObjectName
	 * @param pAssocClass
	 * @param pResultClass
	 * @param pRole
	 * @param pResultRole
	 * @return Element
	 * @throws WBEMException
	 */
	public Element associatorNames_request(Document pDoc, CIMObjectPath pObjectName,
			String pAssocClass, String pResultClass, String pRole, String pResultRole)
			throws WBEMException {

		// obtain data
		String className = pObjectName.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");
		CIMProperty<?>[] keysA = pObjectName.getKeys();

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				ASSOCIATOR_NAMES);
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pObjectName);

		Element iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");
		Element instancenameE = CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, className);
		for (int i = 0; i < keysA.length; i++) {
			CIMProperty<?> prop = keysA[i];
			String propName = prop.getName();
			// TODO: check that CIMDataType.toString() satisfies this
			String propTypeStr = prop.getDataType().toString();
			String propValueStr = valueStr(prop);
			Element keybindingE = CIMXMLBuilderImpl.createKEYBINDING(pDoc, instancenameE, propName);
			CIMXMLBuilderImpl.createKEYVALUE(pDoc, keybindingE, propTypeStr, propValueStr);
		}
		if (pAssocClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "AssocClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pAssocClass);
		}
		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}
		if (pResultRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultRole");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pResultRole);
		}

		return simplereqE;
	}

	/**
	 * associatorNames_response
	 * 
	 * @param pDoc
	 * @param pPathA
	 * @return Element
	 */
	public static Element associatorNames_response(Document pDoc, CIMObjectPath[] pPathA) {
		Element simpRspE = CIMXMLBuilderImpl.createSIMPLERSP(pDoc, null);
		Element iMethRspE = CIMXMLBuilderImpl.createIMETHODRESPONSE(pDoc, simpRspE,
				"associatorNames");
		Element iRetValE = CIMXMLBuilderImpl.createIRETURNVALUE(pDoc, iMethRspE);
		try {
			for (int i = 0; i < pPathA.length; i++) {
				CIMXMLBuilderImpl.createOBJECTPATH(pDoc, iRetValE, pPathA[i]);
			}
		} catch (WBEMException e) {
			throw new RuntimeException(e);
		}
		return simpRspE;
	}

	/**
	 * associatorInstances_request
	 * 
	 * @param pDoc
	 * @param pObjectName
	 * @param pAssocClass
	 * @param pResultClass
	 * @param pRole
	 * @param pResultRole
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element associatorInstances_request(Document pDoc, CIMObjectPath pObjectName,
			String pAssocClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException {

		String className = pObjectName.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");
		CIMProperty<?>[] keysA = pObjectName.getKeys();

		// Make sure keys are populated
		if (keysA.length == 0) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"associatorInstances requires keys for the instance to be populated");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "Associators");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pObjectName);
		Element iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");
		Element instancenameE = CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, className);
		for (int i = 0; i < keysA.length; i++) {
			CIMProperty<?> prop = keysA[i];
			String propName = prop.getName();
			String propTypeStr = prop.getDataType().toString();
			String propValueStr = valueStr(prop);

			Element keybindingE = CIMXMLBuilderImpl.createKEYBINDING(pDoc, instancenameE, propName);
			CIMXMLBuilderImpl.createKEYVALUE(pDoc, keybindingE, propTypeStr, propValueStr);
		}
		if (pAssocClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "AssocClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pAssocClass);
		}
		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}
		if (pResultRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultRole");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pResultRole);
		}

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}

		return simplereqE;
	}

	/**
	 * associatorClasses_request
	 * 
	 * @param pDoc
	 * @param pObjectName
	 * @param pAssocClass
	 * @param pResultClass
	 * @param pRole
	 * @param pResultRole
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element associatorClasses_request(Document pDoc, CIMObjectPath pObjectName,
			String pAssocClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeQualifiers, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {

		String className = pObjectName.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		// Make sure keys are not populated
		if (pObjectName.getKeys().length != 0) throw new WBEMException(
				WBEMException.CIM_ERR_INVALID_PARAMETER,
				"Keys should not be populated for associatorClasses");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "Associators");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pObjectName);
		Element iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");

		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, className);

		if (pAssocClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "AssocClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pAssocClass);
		}
		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}
		if (pResultRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultRole");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pResultRole);
		}
		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}

		return simplereqE;
	}

	/**
	 * associators_request
	 * 
	 * @param pDoc
	 * @param pObjectName
	 * @param pAssocClass
	 * @param pResultClass
	 * @param pRole
	 * @param pResultRole
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element associators_request(Document pDoc, CIMObjectPath pObjectName,
			String pAssocClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeQualifiers, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {

		// obtain data
		String className = pObjectName.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");
		CIMProperty<?>[] keysA = pObjectName.getKeys();

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "Associators");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pObjectName);
		Element iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");
		Element instancenameE = CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, className);
		for (int i = 0; i < keysA.length; i++) {
			CIMProperty<?> prop = keysA[i];
			String propName = prop.getName();
			// TODO: check that CIMDataType.toString() satisfies this
			String propTypeStr = prop.getDataType().toString();
			// CIMXMLBuilderImpl.getTypeStr(pValue.getType());
			String propValueStr = valueStr(prop);

			Element keybindingE = CIMXMLBuilderImpl.createKEYBINDING(pDoc, instancenameE, propName);
			CIMXMLBuilderImpl.createKEYVALUE(pDoc, keybindingE, propTypeStr, propValueStr);
		}
		if (pAssocClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "AssocClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pAssocClass);
		}
		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}
		if (pResultRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultRole");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pResultRole);
		}
		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList"); // BB
			// fixed
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}

		return simplereqE;
	}

	/**
	 * associators_response
	 * 
	 * @param pDoc
	 * @param pNamedElementA
	 * @return Element
	 */
	public static Element associators_response(Document pDoc,
			CIMNamedElementInterface[] pNamedElementA) {
		Element simpRspE = CIMXMLBuilderImpl.createSIMPLERSP(pDoc, null);
		Element iMethRspE = CIMXMLBuilderImpl.createIMETHODRESPONSE(pDoc, simpRspE, "associators");
		Element iRetValE = CIMXMLBuilderImpl.createIRETURNVALUE(pDoc, iMethRspE);
		try {
			for (int i = 0; i < pNamedElementA.length; i++) {
				CIMNamedElementInterface namedElement = pNamedElementA[i];
				CIMObjectPath op = namedElement.getObjectPath();
				String nameSpace = op == null ? null : op.getNamespace();
				CIMXMLBuilderImpl
						.createVALUEOBJECTWITHPATH(pDoc, iRetValE, namedElement, nameSpace);
				/*
				 * CIMXMLBuilderImpl.createCLASSPATH( pDoc, iRetValE,
				 * pClassA[i].getObjectPath() );
				 * CIMXMLBuilderImpl.createCLASS(pDoc, iRetValE, pClassA[i]);
				 */

			}
		} catch (WBEMException e) {
			throw new RuntimeException(e);
		}
		return simpRspE;
	}

	/**
	 * enumerateInstanceNames_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public Element enumerateInstanceNames_request(Document pDoc, CIMObjectPath pPath)
			throws WBEMException {
		// obtain data
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"EnumerateInstanceNames");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ClassName");
		CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, className);

		return simplereqE;
	}

	/**
	 * enumerateInstances_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pDeepInheritance
	 * @param pLocalOnly
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element enumerateInstances_request(Document pDoc, CIMObjectPath pPath,
			boolean pDeepInheritance, boolean pLocalOnly, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException {

		// obtain data
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"EnumerateInstances");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ClassName");
		CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, className);
		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "LocalOnly");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pLocalOnly);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "DeepInheritance");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pDeepInheritance);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}
		return simplereqE;
	}

	/**
	 * getInstance_request
	 * 
	 * @param pDoc
	 * @param pName
	 * @param pLocalOnly
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element getInstance_request(Document pDoc, CIMObjectPath pName, boolean pLocalOnly,
			boolean pIncludeQualifiers, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {
		// obtain data
		String className = pName.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "GetInstance");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pName);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"InstanceName");
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pName);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "LocalOnly");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pLocalOnly);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}

		return simplereqE;
	}

	/**
	 * deleteInstance_request
	 * 
	 * @param pDoc
	 * @param pName
	 * @return Element
	 * @throws WBEMException
	 */
	public Element deleteInstance_request(Document pDoc, CIMObjectPath pName) throws WBEMException {

		// obtain data
		String className = pName.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"DeleteInstance");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pName);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"InstanceName");
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pName);

		return simplereqE;
	}

	/**
	 * getClass_request
	 * 
	 * @param pDoc
	 * @param pName
	 * @param pLocalOnly
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element getClass_request(Document pDoc, CIMObjectPath pName, boolean pLocalOnly,
			boolean pIncludeQualifiers, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {

		// obtain data
		String className = pName.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "GetClass");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pName);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ClassName");
		CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, className);
		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "LocalOnly");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pLocalOnly);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}

		return simplereqE;
	}

	/**
	 * createInstance_request
	 * 
	 * @param pDoc
	 * @param pName
	 * @param pInstance
	 * @return Element
	 * @throws WBEMException
	 */
	public Element createInstance_request(Document pDoc, CIMObjectPath pName, CIMInstance pInstance)
			throws WBEMException {

		String className = pInstance.getObjectPath().getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"CreateInstance");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pName);
		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"NewInstance");

		CIMXMLBuilderImpl.createINSTANCE(pDoc, iparamvalueE, pInstance);

		return simplereqE;
	}

	/**
	 * invokeMethod_request
	 * 
	 * @param pDoc
	 * @param pLocalPath
	 * @param pMethodName
	 * @param pInArgs
	 * @return Element
	 * @throws WBEMException
	 */
	public Element invokeMethod_request(Document pDoc, CIMObjectPath pLocalPath,
			String pMethodName, CIMArgument<?>[] pInArgs) throws WBEMException {

		// obtain data
		String className = pLocalPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");
		CIMProperty<?>[] keysA = pLocalPath.getKeys();

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element methodcallE = CIMXMLBuilderImpl.createMETHODCALL(pDoc, simplereqE, pMethodName);

		// 13521
		if (keysA.length > 0) {
			Element localpathE = CIMXMLBuilderImpl.createLOCALINSTANCEPATH(pDoc, methodcallE);
			CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, localpathE, pLocalPath); // 13521
			CIMXMLBuilderImpl.createINSTANCENAME(pDoc, localpathE, pLocalPath); // 13521
		} else {
			CIMXMLBuilderImpl.createLOCALCLASSPATH(pDoc, methodcallE, pLocalPath);
		}

		buildParamValues(pDoc, methodcallE, pLocalPath, pInArgs);

		return simplereqE;
	}

	/**
	 * invokeMethod_response
	 * 
	 * @param pDoc
	 * @param pMethodName
	 * @param pLocalPath
	 * @param pRetVal
	 * @param pOutArgA
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element invokeMethod_response(Document pDoc, String pMethodName,
			CIMObjectPath pLocalPath, Object pRetVal, CIMArgument<?>[] pOutArgA)
			throws WBEMException {
		if (pMethodName == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null method name");
		Element simpleRspE = CIMXMLBuilderImpl.createSIMPLERSP(pDoc, null);
		Element methodRspE = CIMXMLBuilderImpl.createMETHODRESPONSE(pDoc, simpleRspE, pMethodName);

		CIMXMLBuilderImpl.createRETURNVALUE(pDoc, methodRspE, pRetVal);

		buildParamValues(pDoc, methodRspE, pLocalPath, pOutArgA);
		return simpleRspE;
	}

	/**
	 * @param pLocalPath
	 */
	private static void buildParamValues(Document pDoc, Element pParentE, CIMObjectPath pLocalPath,
			CIMArgument<?>[] pArgA) throws WBEMException {
		if (pArgA == null) return;
		for (int i = 0; i < pArgA.length; i++) {
			CIMArgument<?> arg = pArgA[i];
			if (arg == null) continue;
			CIMXMLBuilderImpl.createPARAMVALUE(pDoc, pParentE, arg);
		}
	}

	// public CIMResponse createIndication_request(Document doc) throws
	// CIMXMLParseException, CIMException {
	// Element rootE = doc.getDocumentElement();
	// CIMResponse response = (CIMResponse)xmlParser.parseCIM(rootE);
	// response.checkError();
	// return response;
	// // Vector v = (Vector)response.getFirstReturnValue();
	// //
	// // //TODO: Should we return the whole list of instances or just the first
	// instance?
	// // //TODO: return the whole vector of indications
	// // if (v.size() > 0)
	// // return (CIMInstance)v.elementAt(0);
	// // else
	// // return null;
	// }

	/**
	 * createIndication_response
	 * 
	 * @param error
	 * @return Document
	 */
	public Document createIndication_response(CIMError error) {

		// CIMXMLBuilderImpl.create XML
		Document doc = this.iBuilder.newDocument();
		Element cimE = CIMXMLBuilderImpl.createCIM(doc);
		Element messageE = CIMXMLBuilderImpl.createMESSAGE(doc, cimE, String.valueOf(getNextId()),
				"1.0");
		Element simpleexprspE = CIMXMLBuilderImpl.createSIMPLEEXPRSP(doc, messageE);
		Element expmethodresponseE = CIMXMLBuilderImpl.createEXPMETHODRESPONSE(doc, simpleexprspE,
				"ExportIndication");
		if (error == null) {
			CIMXMLBuilderImpl.createIRETURNVALUE(doc, expmethodresponseE);
		} else {
			CIMXMLBuilderImpl.createERROR(doc, expmethodresponseE, error);
		}
		// Element
		return doc;
	}

	/**
	 * createClass_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pClass
	 * @return Element
	 * @throws WBEMException
	 */
	public Element createClass_request(Document pDoc, CIMObjectPath pPath, CIMClass pClass)
			throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "CreateClass");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "NewClass");

		CIMXMLBuilderImpl.createCLASS(pDoc, iparamvalueE, pClass);

		return simplereqE;
	}

	/**
	 * getQualifier_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pQt
	 * @return Element
	 * @throws WBEMException
	 */
	public Element getQualifier_request(Document pDoc, CIMObjectPath pPath, String pQt)
			throws WBEMException {
		// obtain data
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl
				.createIMETHODCALL(pDoc, simplereqE, "GetQualifier");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"QualifierName");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pPath.getObjectName());

		return simplereqE;
	}

	/**
	 * createQualifierType_request : This has been replaced by
	 * setQualifierType_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pQt
	 * @return Element
	 * @throws WBEMException
	 */
	public Element createQualifierType_request(Document pDoc, CIMObjectPath pPath,
			CIMQualifierType<?> pQt) throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl
				.createIMETHODCALL(pDoc, simplereqE, "SetQualifier");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"QualifierDeclaration");
		CIMXMLBuilderImpl.createQUALIFIER_DECLARATION(pDoc, iparamvalueE, pQt);

		return simplereqE;
	}

	/**
	 * deleteClass_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public Element deleteClass_request(Document pDoc, CIMObjectPath pPath) throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "DeleteClass");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ClassName");

		CIMXMLBuilderImpl.createOBJECTNAME(pDoc, iparamvalueE, pPath);

		return simplereqE;
	}

	/**
	 * deleteQualifierType_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public Element deleteQualifierType_request(Document pDoc, CIMObjectPath pPath)
			throws WBEMException {
		// obtain data
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"DeleteQualifier");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"QualifierName");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pPath.getObjectName());
		return simplereqE;
	}

	/**
	 * enumerateClasses_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pDeepInheritance
	 * @param pLocalOnly
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @return Element
	 */
	public Element enumerateClasses_request(Document pDoc, CIMObjectPath pPath,
			boolean pDeepInheritance, boolean pLocalOnly, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin) {

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"EnumerateClasses");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE;
		if (pPath != null && pPath.getObjectName() != null
				&& pPath.getObjectName().trim().length() != 0) {
			String className = pPath.getObjectName();
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ClassName");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, className);
		}
		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "LocalOnly");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pLocalOnly);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "DeepInheritance");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pDeepInheritance);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);
		return simplereqE;
	}

	/**
	 * enumerateClasses_response
	 * 
	 * @param pDoc
	 * @param pClA
	 * @return Element
	 */
	public static Element enumerateClasses_response(Document pDoc, CIMClass[] pClA) {
		Element simpRspE = CIMXMLBuilderImpl.createSIMPLERSP(pDoc, null);
		Element iMethRspE = CIMXMLBuilderImpl.createIMETHODRESPONSE(pDoc, simpRspE,
				"enumerateClasses");
		Element iRetValE = CIMXMLBuilderImpl.createIRETURNVALUE(pDoc, iMethRspE);
		try {
			for (int i = 0; i < pClA.length; i++) {
				CIMXMLBuilderImpl.createCLASS(pDoc, iRetValE, pClA[i]);
			}
		} catch (WBEMException e) {
			throw new RuntimeException(e);
		}
		return simpRspE;
	}

	/**
	 * enumerateInstances_response
	 * 
	 * @param pDoc
	 * @param pInstA
	 * @return Element
	 */
	public static Element enumerateInstances_response(Document pDoc, CIMInstance[] pInstA) {
		Element simpRspE = CIMXMLBuilderImpl.createSIMPLERSP(pDoc, null);
		Element iMethRspE = CIMXMLBuilderImpl.createIMETHODRESPONSE(pDoc, simpRspE,
				"enumerateInstances");
		Element iRetValE = CIMXMLBuilderImpl.createIRETURNVALUE(pDoc, iMethRspE);
		try {
			for (int i = 0; i < pInstA.length; i++) {
				CIMXMLBuilderImpl.createINSTANCE(pDoc, iRetValE, pInstA[i]);
			}
		} catch (WBEMException e) {
			throw new RuntimeException(e);
		}
		return simpRspE;
	}

	/**
	 * enumerateClassNames_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pDeepInheritance
	 * @return Element
	 */
	public Element enumerateClassNames_request(Document pDoc, CIMObjectPath pPath,
			boolean pDeepInheritance) {

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"EnumerateClassNames");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE;

		if (pPath != null && pPath.getObjectName() != null
				&& pPath.getObjectName().trim().length() != 0) {
			String className = pPath.getObjectName();
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ClassName");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, className);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "DeepInheritance");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pDeepInheritance);

		return simplereqE;
	}

	/**
	 * getProperty_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pPropertyName
	 * @return Element
	 * @throws WBEMException
	 */
	public Element getProperty_request(Document pDoc, CIMObjectPath pPath, String pPropertyName)
			throws WBEMException {
		// obtain data
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "GetProperty");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"InstanceName");
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pPath);

		if (pPropertyName != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyName");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pPropertyName);
		}

		return simplereqE;
	}

	/**
	 * referenceNames_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pResultClass
	 * @param pRole
	 * @return Element
	 * @throws WBEMException
	 */
	public Element referenceNames_request(Document pDoc, CIMObjectPath pPath, String pResultClass,
			String pRole) throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"ReferenceNames");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");
		CIMXMLBuilderImpl.createOBJECTNAME(pDoc, iparamvalueE, pPath);

		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}

		return simplereqE;
	}

	/**
	 * referenceClasses_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pResultClass
	 * @param pRole
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element referenceClasses_request(Document pDoc, CIMObjectPath pPath,
			String pResultClass, String pRole, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		// Make sure keys are not populated
		if (pPath.getKeys().length != 0) throw new WBEMException(
				WBEMException.CIM_ERR_INVALID_PARAMETER,
				"Keys should not be populated for referenceClasses");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "References");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");
		// createOBJECTNAME will internally call createINSTANCENAME but as there
		// are no keys Element containing keys will not be populated
		CIMXMLBuilderImpl.createOBJECTNAME(pDoc, iparamvalueE, pPath);

		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}
		return simplereqE;
	}

	/**
	 * referenceInstances_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pResultClass
	 * @param pRole
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element referenceInstances_request(Document pDoc, CIMObjectPath pPath,
			String pResultClass, String pRole, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		// keys are required for CIMInstance
		if (pPath.getKeys().length == 0) throw new WBEMException(
				WBEMException.CIM_ERR_INVALID_PARAMETER,
				"refrenceInstances requires keys for the instance to be populated");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "References");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");
		// createOBJECTNAME will internally call createINSTANCENAME to populate
		// Element containing keys
		CIMXMLBuilderImpl.createOBJECTNAME(pDoc, iparamvalueE, pPath);

		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}
		return simplereqE;
	}

	/**
	 * references_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pResultClass
	 * @param pRole
	 * @param pIncludeQualifiers
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element references_request(Document pDoc, CIMObjectPath pPath, String pResultClass,
			String pRole, boolean pIncludeQualifiers, boolean pIncludeClassOrigin,
			String[] pPropertyList) throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "References");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE;

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ObjectName");
		CIMXMLBuilderImpl.createOBJECTNAME(pDoc, iparamvalueE, pPath);

		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "ResultClass");
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Role");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, "IncludeClassOrigin");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}

		return simplereqE;
	}

	/**
	 * setClass_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pClass
	 * @return Element
	 * @throws WBEMException
	 */
	public Element setClass_request(Document pDoc, CIMObjectPath pPath, CIMClass pClass)
			throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "ModifyClass");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"ModifiedClass");
		CIMXMLBuilderImpl.createCLASS(pDoc, iparamvalueE, pClass);

		return simplereqE;
	}

	/**
	 * setInstance_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pInstance
	 * @param pIncludeQualifiers
	 * @param pPropertyList
	 * @return Element
	 * @throws WBEMException
	 */
	public Element setInstance_request(Document pDoc, CIMObjectPath pPath, CIMInstance pInstance,
			boolean pIncludeQualifiers, String[] pPropertyList) throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"ModifyInstance");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"ModifiedInstance");
		CIMXMLBuilderImpl.createVALUENAMEDINSTANCE(pDoc, iparamvalueE, pPath, pInstance);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "IncludeQualifiers");
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeQualifiers);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyList");
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++) {
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
			}
		}

		return simplereqE;
	}

	/**
	 * setProperty_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pPropertyName
	 * @param pNewValue
	 * @return Element
	 * @throws WBEMException
	 */
	public Element setProperty_request(Document pDoc, CIMObjectPath pPath, String pPropertyName,
			Object pNewValue) throws WBEMException {
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "SetProperty");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"InstanceName");
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pPath);

		if (pPropertyName != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "PropertyName");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pPropertyName);
		}

		if (pNewValue != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "NewValue");
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pNewValue);
		}

		return simplereqE;
	}

	/**
	 * setQualifierType_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pQt
	 * @return Element
	 * @throws WBEMException
	 */
	public Element setQualifierType_request(Document pDoc, CIMObjectPath pPath,
			CIMQualifierType<?> pQt) throws WBEMException {
		// Make sure class name exists, it is required to uniquely identify
		// qualifier in namespace
		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl
				.createIMETHODCALL(pDoc, simplereqE, "SetQualifier");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"QualifierDeclaration");
		CIMXMLBuilderImpl.createQUALIFIER_DECLARATION(pDoc, iparamvalueE, pQt);

		return simplereqE;
	}

	/**
	 * enumQualifierTypes_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @return Element
	 * @throws WBEMException
	 */
	public Element enumQualifierTypes_request(Document pDoc, CIMObjectPath pPath)
			throws WBEMException {

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"EnumerateQualifiers");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		return simplereqE;
	}

	/**
	 * enumQualifierTypes_response
	 * 
	 * @param pDoc
	 * @param pQualiTypeA
	 * @return Element
	 * @throws WBEMException
	 */
	public static Element enumQualifierTypes_response(Document pDoc,
			CIMQualifierType<?>[] pQualiTypeA) throws WBEMException {
		Element simpRspE = CIMXMLBuilderImpl.createSIMPLERSP(pDoc, null);
		Element iMethRspE = CIMXMLBuilderImpl.createIMETHODRESPONSE(pDoc, simpRspE,
				"associatorNames");
		Element iRetValE = CIMXMLBuilderImpl.createIRETURNVALUE(pDoc, iMethRspE);
		for (int i = 0; i < pQualiTypeA.length; i++) {
			CIMXMLBuilderImpl.createQUALIFIER_DECLARATION(pDoc, iRetValE, pQualiTypeA[i]);
		}
		return simpRspE;
	}

	/**
	 * execQuery_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pQuery
	 * @param pQueryLanguage
	 * @return Element
	 */
	public Element execQuery_request(Document pDoc, CIMObjectPath pPath, String pQuery,
			String pQueryLanguage) {
		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE, "ExecQuery");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element querylanguageE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				"QueryLanguage");
		CIMXMLBuilderImpl.createVALUE(pDoc, querylanguageE, pQueryLanguage);

		Element queryE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, "Query");
		CIMXMLBuilderImpl.createVALUE(pDoc, queryE, pQuery);

		return simplereqE;
	}

	/**
	 * performBatchOperation_request
	 * 
	 * @param pDoc
	 * @param pOperations
	 * @return Element
	 * @throws WBEMException
	 */
	public Element performBatchOperation_request(Document pDoc, Vector<CIMOperation> pOperations)
			throws WBEMException {

		Element messageE = createCIMMessage(pDoc, null);
		if (pOperations.size() > 1) {
			Element multireqE = createMultiReq(pDoc);
			messageE.appendChild(multireqE);
			messageE = multireqE;
		}
		int i = 0;
		Iterator<CIMOperation> iter = pOperations.iterator();
		while (iter.hasNext()) {
			CIMOperation op = iter.next();
			try {
				Element requestE = null;

				if (op instanceof CIMAssociatorsOp) {
					CIMAssociatorsOp associatorsOp = (CIMAssociatorsOp) op;
					requestE = associators_request(pDoc, associatorsOp.getObjectName(),
							associatorsOp.getAssocClass(), associatorsOp.getResultClass(),
							associatorsOp.getRole(), associatorsOp.getResultRole(), associatorsOp
									.isIncludeQualifiers(), associatorsOp.isIncludeClassOrigin(),
							associatorsOp.getPropertyList());
				} else if (op instanceof CIMAssociatorNamesOp) {
					CIMAssociatorNamesOp associatorNamesOp = (CIMAssociatorNamesOp) op;
					requestE = associatorNames_request(pDoc, associatorNamesOp.getObjectName(),
							associatorNamesOp.getAssocClass(), associatorNamesOp.getResultClass(),
							associatorNamesOp.getRole(), associatorNamesOp.getResultRole());
				} else if (op instanceof CIMCreateClassOp) {
					CIMCreateClassOp createClassOp = (CIMCreateClassOp) op;
					requestE = createClass_request(pDoc, createClassOp.getObjectName(),
							createClassOp.getCimClass());
				} else if (op instanceof CIMCreateInstanceOp) {
					CIMCreateInstanceOp createInstanceOp = (CIMCreateInstanceOp) op;
					requestE = createInstance_request(pDoc, createInstanceOp.getObjectName(),
							createInstanceOp.getInstance());
				} else if (op instanceof CIMCreateNameSpaceOp) {
					CIMCreateNameSpaceOp createNameSpaceOp = (CIMCreateNameSpaceOp) op;

					String namespace = createNameSpaceOp.getNameSpace();
					int j = namespace.lastIndexOf('/');

					if (j < 0) throw new WBEMException(WBEMException.CIM_ERR_NOT_FOUND,
							"Invalid namespace. Must contain at least /");
					String parentNs = namespace.substring(0, j);
					namespace = namespace.substring(j + 1);

					/*
					 * CIMInstance inst = new CIMInstance();
					 * inst.setClassName("CIM_NameSpace"); CIMProperty prop =
					 * new CIMProperty("NameSpace"); prop.setValue(new
					 * CIMValue(namespace, CIMDataType
					 * .getPredefinedType(CIMDataType.STRING))); Vector v = new
					 * Vector(); v.add(prop); inst.setProperties(v);
					 */

					CIMInstance inst = new CIMInstance(new CIMObjectPath(null, null, null, null,
							"CIM_NameSpace", null), new CIMProperty[] { new CIMProperty<String>(
							"NameSpace", CIMDataType.STRING_T, namespace, true, false, null) });
					CIMObjectPath object = new CIMObjectPath(null, null, null, parentNs, null, null);

					requestE = createInstance_request(pDoc, object, inst);
				} else if (op instanceof CIMCreateQualifierTypeOp) {
					CIMCreateQualifierTypeOp createQualifierTypeOp = (CIMCreateQualifierTypeOp) op;
					requestE = createQualifierType_request(pDoc, createQualifierTypeOp
							.getObjectName(), createQualifierTypeOp.getQualifierType());
				} else if (op instanceof CIMDeleteClassOp) {
					CIMDeleteClassOp deleteClassOp = (CIMDeleteClassOp) op;
					requestE = deleteClass_request(pDoc, deleteClassOp.getObjectName());
				} else if (op instanceof CIMDeleteInstanceOp) {
					CIMDeleteInstanceOp deleteInstanceOp = (CIMDeleteInstanceOp) op;
					requestE = deleteClass_request(pDoc, deleteInstanceOp.getObjectName());
				} else if (op instanceof CIMDeleteQualifierTypeOp) {
					CIMDeleteQualifierTypeOp deleteQualifierTypeOp = (CIMDeleteQualifierTypeOp) op;
					requestE = deleteClass_request(pDoc, deleteQualifierTypeOp.getObjectName());
				} else if (op instanceof CIMEnumClassesOp) {
					CIMEnumClassesOp enumClassesOp = (CIMEnumClassesOp) op;
					requestE = enumerateClasses_request(pDoc, enumClassesOp.getObjectName(),
							enumClassesOp.isDeep(), enumClassesOp.isLocalOnly(), enumClassesOp
									.isIncludeQualifiers(), enumClassesOp.isIncludeClassOrigin());
				} else if (op instanceof CIMEnumClassNamesOp) {
					CIMEnumClassNamesOp enumClassNamesOp = (CIMEnumClassNamesOp) op;
					requestE = enumerateClassNames_request(pDoc, enumClassNamesOp.getObjectName(),
							enumClassNamesOp.isDeep());
				} else if (op instanceof CIMEnumInstanceNamesOp) {
					CIMEnumInstanceNamesOp enumInstanceNamesOp = (CIMEnumInstanceNamesOp) op;
					requestE = enumerateInstanceNames_request(pDoc, enumInstanceNamesOp
							.getObjectName());
				} else if (op instanceof CIMEnumInstancesOp) {
					CIMEnumInstancesOp enumInstancesOp = (CIMEnumInstancesOp) op;
					requestE = enumerateInstances_request(pDoc, enumInstancesOp.getObjectName(),
							enumInstancesOp.isDeep(), enumInstancesOp.isLocalOnly(),
							enumInstancesOp.isIncludeQualifiers(), enumInstancesOp
									.isIncludeClassOrigin(), enumInstancesOp.getPropertyList());
				} else if (op instanceof CIMEnumNameSpaceOp) {
					CIMEnumNameSpaceOp enumNameSpaceOp = (CIMEnumNameSpaceOp) op;
					// ebak: here we have to set CIMObjectPath's objectname
					// enumNameSpaceOp.getObjectName().setObjectName("CIM_NameSpace");
					CIMObjectPath objPath = enumNameSpaceOp.getObjectName();
					objPath = new CIMObjectPath(objPath.getScheme(), objPath.getHost(), objPath
							.getPort(), objPath.getNamespace(), "CIM_NameSpace", objPath.getKeys());
					requestE = enumerateInstanceNames_request(pDoc, enumNameSpaceOp.getObjectName());
				} else if (op instanceof CIMEnumQualifierTypesOp) {
					CIMEnumQualifierTypesOp enumQualifierTypesOp = (CIMEnumQualifierTypesOp) op;
					requestE = enumQualifierTypes_request(pDoc, enumQualifierTypesOp
							.getObjectName());
				} else if (op instanceof CIMExecQueryOp) {
					CIMExecQueryOp execQueryOp = (CIMExecQueryOp) op;
					requestE = execQuery_request(pDoc, execQueryOp.getObjectName(), execQueryOp
							.getQuery(), execQueryOp.getQueryLanguage());
				} else if (op instanceof CIMGetPropertyOp) {
					CIMGetPropertyOp getPropertyOp = (CIMGetPropertyOp) op;
					requestE = getInstance_request(pDoc, getPropertyOp.getObjectName(), false,
							false, false, new String[] { getPropertyOp.getPropertyName() });

				} else if (op instanceof CIMGetClassOp) {
					CIMGetClassOp getClassOp = (CIMGetClassOp) op;
					requestE = getClass_request(pDoc, getClassOp.getObjectName(), getClassOp
							.isLocalOnly(), getClassOp.isIncludeQualifiers(), getClassOp
							.isIncludeClassOrigin(), getClassOp.getPropertyList());
				} else if (op instanceof CIMGetInstanceOp) {
					CIMGetInstanceOp getInstanceOp = (CIMGetInstanceOp) op;
					requestE = getInstance_request(pDoc, getInstanceOp.getObjectName(),
							getInstanceOp.isLocalOnly(), getInstanceOp.isIncludeQualifiers(),
							getInstanceOp.isIncludeClassOrigin(), getInstanceOp.getPropertyList());
				} else if (op instanceof CIMGetQualifierTypeOp) {
					CIMGetQualifierTypeOp getQualifierTypeOp = (CIMGetQualifierTypeOp) op;
					requestE = getQualifier_request(pDoc, getQualifierTypeOp.getObjectName(),
							getQualifierTypeOp.getQualifierType());
				} else if (op instanceof CIMInvokeMethodOp) {
					CIMInvokeMethodOp invokeMethodOp = (CIMInvokeMethodOp) op;
					requestE = invokeMethod_request(pDoc, invokeMethodOp.getObjectName(),
							invokeMethodOp.getMethodCall(), invokeMethodOp.getInParams());
				} else if (op instanceof CIMReferenceNamesOp) {
					CIMReferenceNamesOp referenceNamesOp = (CIMReferenceNamesOp) op;
					requestE = referenceNames_request(pDoc, referenceNamesOp.getObjectName(),
							referenceNamesOp.getResultClass(), referenceNamesOp.getResultRole());
				} else if (op instanceof CIMReferencesOp) {
					CIMReferencesOp referencesOp = (CIMReferencesOp) op;
					requestE = references_request(pDoc, referencesOp.getObjectName(), referencesOp
							.getResultClass(), referencesOp.getRole(), referencesOp
							.isIncludeQualifiers(), referencesOp.isIncludeClassOrigin(),
							referencesOp.getPropertyList());
				} else if (op instanceof CIMSetClassOp) {
					CIMSetClassOp setClassOp = (CIMSetClassOp) op;
					requestE = setClass_request(pDoc, setClassOp.getObjectName(), setClassOp
							.getCimClass());
				} else if (op instanceof CIMSetInstanceOp) {
					CIMSetInstanceOp setInstanceOp = (CIMSetInstanceOp) op;
					requestE = setInstance_request(pDoc, setInstanceOp.getObjectName(),
							setInstanceOp.getInstance(), setInstanceOp.isIncludeQualifiers(),
							setInstanceOp.getPropertyList());
				} else if (op instanceof CIMSetPropertyOp) {
					CIMSetPropertyOp setPropertyOp = (CIMSetPropertyOp) op;
					requestE = setProperty_request(pDoc, setPropertyOp.getObjectName(),
							setPropertyOp.getPropertyName(), setPropertyOp.getCimValue());
				} else if (op instanceof CIMSetQualifierTypeOp) {
					CIMSetQualifierTypeOp setQualifierTypeOp = (CIMSetQualifierTypeOp) op;
					requestE = setQualifierType_request(pDoc, setQualifierTypeOp.getObjectName(),
							setQualifierTypeOp.getQualifierType());
				}
				if (requestE == null) throw new WBEMException(
						WBEMException.CIM_ERR_INVALID_PARAMETER, "Illegal batch operation number ("
								+ i + ") " + op.getClass());
				messageE.appendChild(requestE);
			} catch (WBEMException e) {
				throw e;
			} catch (Exception e) {
				throw new WBEMException(WBEMException.CIM_ERR_FAILED, "At batch operation (" + i
						+ ')', null, e);
			}
			i++;
		}
		return messageE;
	}

	/**
	 * Sets the message id counter to a given value. For use in units tests
	 * only.
	 * 
	 * @param pId
	 *            The new value
	 */
	public void setId(int pId) {
		this.iCurrentId.set(new Counter(pId - 1));
	}

	/**
	 * Get the next message id. If this method is called for the first time by
	 * the current thread it will choose a start value randomly. Afterwards the
	 * id is incremented by 1. Be aware that different threads will have
	 * distinct id counters.
	 * 
	 * @return The next message id
	 */
	private int getNextId() {
		if (this.iCurrentId.get() == null) {
			this.iCurrentId.set(new Counter(RANDOM.nextInt(MAX_ID)));
		}
		return this.iCurrentId.get().incrementAndGet();
	}

	/**
	 * pAssociatorPaths_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pAssocClass
	 * @param pResultClass
	 * @param pRole
	 * @param pResultRole
	 * @param pFilterQueryLanguage
	 * @param pFilterQuery
	 * @param pOperationTimeout
	 * @param pContinueOnError
	 * @param pMaxObjectCount
	 * @return Element
	 * @throws WBEMException
	 */
	public Element OpenAssociatorInstancePaths_request(Document pDoc, CIMObjectPath pPath,
			String pAssocClass, String pResultClass, String pRole, String pResultRole,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pOperationTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"OpenAssociatorInstancePaths");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				INSTANCE_NAME);
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pPath);

		if (pAssocClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, ASSOC_CLASS);
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pAssocClass);
		}
		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, RESULT_CLASS);
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, ROLE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}
		if (pResultRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, RESULT_ROLE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pResultRole);
		}
		if (pFilterQueryLanguage != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					FILTER_QUERY_LANGUAGE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQueryLanguage);
		}
		if (pFilterQuery != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, FILTER_QUERY);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQuery);
		}
		if (pOperationTimeout != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					OPERATION_TIMEOUT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pOperationTimeout);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CONTINUE_ON_ERROR);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContinueOnError);

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * OpenAssociatorInstances_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pAssocClass
	 * @param pResultClass
	 * @param pRole
	 * @param pResultRole
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @param pFilterQueryLanguage
	 * @param pFilterQuery
	 * @param pOperationTimeout
	 * @param pContinueOnError
	 * @param pMaxObjectCount
	 * @return Element OpenAssociatorInstances_request
	 * @throws WBEMException
	 */
	public Element OpenAssociatorInstances_request(Document pDoc, CIMObjectPath pPath,
			String pAssocClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeClassOrigin, String[] pPropertyList, String pFilterQueryLanguage,
			String pFilterQuery, UnsignedInteger32 pOperationTimeout, boolean pContinueOnError,
			UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();

		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"OpenAssociatorInstances");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				INSTANCE_NAME);
		// createINSTANCENAME will take care of keyBindings
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pPath);

		if (pAssocClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, ASSOC_CLASS);
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pAssocClass);
		}
		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, RESULT_CLASS);
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, ROLE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}
		if (pResultRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, RESULT_ROLE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pResultRole);
		}
		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, INCLUDE_CLASS_ORIGIN);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, PROPERTY_LIST);
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++)
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
		}
		if (pFilterQueryLanguage != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					FILTER_QUERY_LANGUAGE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQueryLanguage);
		}
		if (pFilterQuery != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, FILTER_QUERY);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQuery);
		}
		if (pOperationTimeout != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					OPERATION_TIMEOUT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pOperationTimeout);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CONTINUE_ON_ERROR);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContinueOnError);

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * OpenEnumerateInstancePaths_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pFilterQueryLanguage
	 * @param pFilterQuery
	 * @param pOperationTimeout
	 * @param pContinueOnError
	 * @param pMaxObjectCount
	 * @return Element
	 * @throws WBEMException
	 */
	public Element OpenEnumerateInstancePaths_request(Document pDoc, CIMObjectPath pPath,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pOperationTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"OpenEnumerateInstancePaths");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CLASS_NAME);
		CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, className);

		if (pFilterQueryLanguage != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					FILTER_QUERY_LANGUAGE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQueryLanguage);
		}
		if (pFilterQuery != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, FILTER_QUERY);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQuery);
		}
		if (pOperationTimeout != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					OPERATION_TIMEOUT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pOperationTimeout);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CONTINUE_ON_ERROR);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContinueOnError);

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * OpenEnumerateInstances_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pPropertyList
	 * @param pIncludeClassOrigin
	 * @param pDeepInheritance
	 * @param pFilterQueryLanguage
	 * @param pFilterQuery
	 * @param pOperationTimeout
	 * @param pContinueOnError
	 * @param pMaxObjectCount
	 * @return Element
	 * @throws WBEMException
	 */
	public Element OpenEnumerateInstances_request(Document pDoc, CIMObjectPath pPath,
			boolean pDeepInheritance, boolean pIncludeClassOrigin, String[] pPropertyList,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pOperationTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"OpenEnumerateInstances");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CLASS_NAME);
		CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, className);

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, DEEP_INHERITANCE);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pDeepInheritance);

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, INCLUDE_CLASS_ORIGIN);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, PROPERTY_LIST);
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++)
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
		}

		if (pFilterQueryLanguage != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					FILTER_QUERY_LANGUAGE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQueryLanguage);
		}
		if (pFilterQuery != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, FILTER_QUERY);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQuery);
		}
		if (pOperationTimeout != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					OPERATION_TIMEOUT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pOperationTimeout);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CONTINUE_ON_ERROR);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContinueOnError);

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * EnumerationCount_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pEnumerationContext
	 * @return Element
	 * @throws WBEMException
	 */
	public Element EnumerationCount_request(Document pDoc, CIMObjectPath pPath,
			String pEnumerationContext) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"EnumerationCount");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		if (pEnumerationContext != null) {
			Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					ENUMERATION_CONTEXT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pEnumerationContext);
		}

		return simplereqE;

	}

	/**
	 * CloseEnumeration_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pEnumerationContext
	 * @return Element
	 * @throws WBEMException
	 */
	public Element CloseEnumeration_request(Document pDoc, CIMObjectPath pPath,
			String pEnumerationContext) throws WBEMException {

		String className = pPath.getObjectName();

		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"CloseEnumeration");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		if (pEnumerationContext != null) {
			Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					ENUMERATION_CONTEXT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pEnumerationContext);
		}
		return simplereqE;
	}

	/**
	 * referencePaths_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pResultClass
	 * @param pRole
	 * @param pFilterQueryLanguage
	 * @param pFilterQuery
	 * @param pOperationTimeout
	 * @param pContinueOnError
	 * @param pMaxObjectCount
	 * @return Element referencePaths_request
	 * @throws WBEMException
	 */
	public Element OpenReferenceInstancePaths_request(Document pDoc, CIMObjectPath pPath,
			String pResultClass, String pRole, String pFilterQueryLanguage, String pFilterQuery,
			UnsignedInteger32 pOperationTimeout, boolean pContinueOnError,
			UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"OpenReferenceInstancePaths");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				INSTANCE_NAME);
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pPath);

		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, RESULT_CLASS);
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, ROLE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}
		if (pFilterQueryLanguage != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					FILTER_QUERY_LANGUAGE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQueryLanguage);
		}
		if (pFilterQuery != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, FILTER_QUERY);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQuery);
		}
		if (pOperationTimeout != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					OPERATION_TIMEOUT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pOperationTimeout);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CONTINUE_ON_ERROR);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContinueOnError);

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;

	}

	/**
	 * references_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pResultClass
	 * @param pRole
	 * @param pIncludeClassOrigin
	 * @param pPropertyList
	 * @param pFilterQueryLanguage
	 * @param pFilterQuery
	 * @param pOperationTimeout
	 * @param pContinueOnError
	 * @param pMaxObjectCount
	 * @return Element references_request
	 * @throws WBEMException
	 */
	public Element OpenReferenceInstances_request(Document pDoc, CIMObjectPath pPath,
			String pResultClass, String pRole, boolean pIncludeClassOrigin, String[] pPropertyList,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pOperationTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"OpenReferenceInstances");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				INSTANCE_NAME);
		CIMXMLBuilderImpl.createINSTANCENAME(pDoc, iparamvalueE, pPath);

		if (pResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, RESULT_CLASS);
			CIMXMLBuilderImpl.createCLASSNAME(pDoc, iparamvalueE, pResultClass);
		}
		if (pRole != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, ROLE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pRole);
		}

		iparamvalueE = CIMXMLBuilderImpl
				.createIPARAMVALUE(pDoc, imethodcallE, INCLUDE_CLASS_ORIGIN);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pIncludeClassOrigin);

		if (pPropertyList != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, PROPERTY_LIST);
			Element valuearrayE = CIMXMLBuilderImpl.createVALUEARRAY(pDoc, iparamvalueE);
			for (int i = 0; i < pPropertyList.length; i++)
				CIMXMLBuilderImpl.createVALUE(pDoc, valuearrayE, pPropertyList[i]);
		}

		if (pFilterQueryLanguage != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					FILTER_QUERY_LANGUAGE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQueryLanguage);
		}
		if (pFilterQuery != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, FILTER_QUERY);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQuery);
		}
		if (pOperationTimeout != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					OPERATION_TIMEOUT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pOperationTimeout);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CONTINUE_ON_ERROR);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContinueOnError);

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * OpenQueryInstances_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pFilterQuery
	 * @param pFilterQueryLanguage
	 * @param pReturnQueryResultClass
	 * @param pOperationTimeout
	 * @param pContinueOnError
	 * @param pMaxObjectCount
	 * @param pQueryResultClass
	 * @return Element OpenQueryInstances_request
	 * @throws WBEMException
	 */
	public Element OpenQueryInstances_request(Document pDoc, CIMObjectPath pPath,
			String pFilterQuery, String pFilterQueryLanguage, boolean pReturnQueryResultClass,
			UnsignedInteger32 pOperationTimeout, boolean pContinueOnError,
			UnsignedInteger32 pMaxObjectCount, CIMClass pQueryResultClass) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"OpenQueryInstances");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE;

		if (pFilterQuery != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, FILTER_QUERY);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQuery);
		}
		if (pFilterQueryLanguage != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					FILTER_QUERY_LANGUAGE);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pFilterQueryLanguage);
		}
		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
				RETURN_QUERY_RESULT_CLASS);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pReturnQueryResultClass);

		if (pOperationTimeout != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					OPERATION_TIMEOUT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pOperationTimeout);
		}

		iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE, CONTINUE_ON_ERROR);
		CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContinueOnError);

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}
		if (pQueryResultClass != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					QUERY_RESULT_CLASS);
			CIMXMLBuilderImpl.createCLASS(pDoc, iparamvalueE, pQueryResultClass);
		}
		return simplereqE;
	}

	/**
	 * PullInstancesWithPath_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pContext
	 * @param pMaxObjectCount
	 * @return Element PullInstancesWithPath_request
	 * @throws WBEMException
	 */
	public Element PullInstancesWithPath_request(Document pDoc, CIMObjectPath pPath,
			String pContext, UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();
		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"PullInstancesWithPath");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE;

		if (pContext != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					ENUMERATION_CONTEXT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContext);
		}

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * PullInstancePaths_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pContext
	 * @param pMaxObjectCount
	 * @return Element PullInstancePaths
	 * @throws WBEMException
	 */
	public Element PullInstancePaths_request(Document pDoc, CIMObjectPath pPath, String pContext,
			UnsignedInteger32 pMaxObjectCount) throws WBEMException {

		String className = pPath.getObjectName();

		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"PullInstancePaths");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE;

		if (pContext != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					ENUMERATION_CONTEXT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContext);
		}

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * PullInstances_request
	 * 
	 * @param pDoc
	 * @param pPath
	 * @param pContext
	 * @param pMaxObjectCount
	 * @return Element PullInstances_request
	 * @throws WBEMException
	 */
	public Element PullInstances_request(Document pDoc, CIMObjectPath pPath, String pContext,
			UnsignedInteger32 pMaxObjectCount) throws WBEMException {
		String className = pPath.getObjectName();

		if (className == null) throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER,
				"null class name");

		Element simplereqE = CIMXMLBuilderImpl.createSIMPLEREQ(pDoc);
		Element imethodcallE = CIMXMLBuilderImpl.createIMETHODCALL(pDoc, simplereqE,
				"PullInstances");
		CIMXMLBuilderImpl.createLOCALNAMESPACEPATH(pDoc, imethodcallE, pPath);

		Element iparamvalueE;

		if (pContext != null) {
			iparamvalueE = CIMXMLBuilderImpl.createIPARAMVALUE(pDoc, imethodcallE,
					ENUMERATION_CONTEXT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pContext);
		}

		if (pMaxObjectCount != null) {
			iparamvalueE = CIMXMLBuilderImpl
					.createIPARAMVALUE(pDoc, imethodcallE, MAX_OBJECT_COUNT);
			CIMXMLBuilderImpl.createVALUE(pDoc, iparamvalueE, pMaxObjectCount);
		}

		return simplereqE;
	}

	/**
	 * sendIndication_request
	 * 
	 * @param pDoc
	 * @param pIndication
	 * @return Element sendIndication_request
	 * @throws WBEMException
	 */
	public Element sendIndication_request(Document pDoc, CIMInstance pIndication)
			throws WBEMException {
		Element simpleexpreqE = CIMXMLBuilderImpl.createSIMPLEEXPREQ(pDoc);
		Element expmethodcallE = CIMXMLBuilderImpl.createEXPMETHODCALL(pDoc, simpleexpreqE,
				"ExportIndication");
		Element expparamvalueE = CIMXMLBuilderImpl.createEXPPARAMVALUE(pDoc, expmethodcallE,
				"NewIndication");

		CIMXMLBuilderImpl.createINSTANCE(pDoc, expparamvalueE, pIndication);

		return simpleexpreqE;
	}
}
