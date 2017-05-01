/**
 * CIMXMLParserImpl.java
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
 *------------------------------------------------------------------------------- 
 * 1464860    2006-05-15  lupusalex    No default value for VALUETYPE assumed
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1547910    2006-09-05  ebak         parseIMETHODCALL() CIMObjectPath parsing problem
 * 1547908    2006-09-05  ebak         parseKEYBINDING() creates incorrect reference type
 * 1545915    2006-09-05  ebak         Wrong parsing of IMETHODCALL request
 * 1660756    2007-03-02  ebak         Embedded object support
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 1712656    2007-05-04  ebak         Correct type identification for SVC CIMOM
 * 1714878    2007-05-08  ebak         Empty string property values are parsed as nulls
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 1769504	  2007-08-15  ebak         Type identification for VALUETYPE="numeric"
 * 1783288    2007-09-10  ebak         CIMClass.isAssociation() not working for retrieved classes.
 * 1820763    2007-10-29  ebak         Supporting the EmbeddedInstance qualifier
 * 1827728    2007-11-20  ebak         rework: embeddedInstances: attribute EmbeddedObject not set
 * 1848607    2007-12-11  ebak         Strict EmbeddedObject types
 * 1944875    2008-05-29  blaschke-oss Indications with embedded objects are not accepted
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2210455    2008-10-30  blaschke-oss Enhance javadoc, fix potential null pointers
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2714989    2009-03-26  blaschke-oss Code cleanup from redundant null check et al
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 2860081    2009-09-17  raman_arora  Pull Enumeration Feature (DOM Parser)
 * 2957387    2010-03-03  blaschke-oss EmbededObject XML attribute must not be all uppercases
 * 3023143    2010-07-01  blaschke-oss CIMXMLParserImpl uses # constructor instead of valueOf
 * 3027479    2010-07-09  blaschke-oss Dead store to local variable
 * 3027615    2010-07-12  blaschke-oss Use CLASS_ARRAY_T instead of new CIMDataType(CLASS,0)
 * 3293248    2011-05-03  blaschke-oss Support for CIM_ERROR instances within ERROR
 * 3297028    2011-05-03  blaschke-oss Instances contain CIMClassProperty with DOM parser
 * 3411944    2011-09-20  blaschke-oss createJavaObject exception with hex uint
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 * 3513349    2012-03-31  blaschke-oss TCK: CIMDataType must not accept null string
 * 3466280    2012-04-23  blaschke-oss get instance failure for CIM_IndicationSubscription
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 * 3526679    2012-05-14  blaschke-oss DOM parser ignores ERROR node CODE
 * 3572993    2012-10-01  blaschke-oss parseDouble("2.2250738585072012e-308") DoS vulnerability
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
 *    2616    2013-02-23  blaschke-oss Add new API WBEMClientSBLIM.sendIndication()
 *    2636    2013-05-08  blaschke-oss Nested embedded instances cause CIMXMLParseException
 *    2640    2013-05-11  blaschke-oss Multiple CDATA parsing broken in DOM parser
 *    2668    2013-09-24  blaschke-oss Potential null pointer exception in parseCIM
 *    2669    2013-09-24  blaschke-oss Potential null pointer exception in parseMESSAGE
 *    2670    2013-09-25  blaschke-oss NAME attribute not required by DOM parser
 *    2671    2013-09-25  blaschke-oss Potential null pointer exception in parseERROR
 *    2675    2013-09-27  blaschke-oss CIMXMLParseException messages should contain element name
 *    2676    2013-09-27  blaschke-oss parseMULTI(EXP)REQ looking for wrong child elements
 *    2678    2013-09-30  blaschke-oss parseMULTI___ allows one SIMPLE___ child element
 *    2679    2013-10-01  blaschke-oss parseIMETHODCALL requires one IPARAMVALUE child element
 *    2680    2013-10-02  blaschke-oss IPARAMVALUE parsing broken on DOM/SAX
 *    2681    2013-10-02  blaschke-oss parseQUALIFIERDECLARATION does not require TYPE attribute
 *    2683    2013-10-07  blaschke-oss KEYVALUE VALUETYPE optional, "string" default
 *    2684    2013-10-07  blaschke-oss parseEXPMETHODRESPONSE has several issues
 *    2685    2013-10-07  blaschke-oss Element.getAttribute returns empty string if no attribute
 *    2686    2013-10-10  blaschke-oss parseEXPPARAMVALUE allows 2+ children, prohibits 0 
 *    2688    2013-10-10  blaschke-oss parseMETHODCALL looks for CIMName attribute instead of NAME
 *    2537    2013-10-17  blaschke-oss Add new data types for PARAMVALUE
 *    2691    2013-10-18  blaschke-oss RETURNVALUE should not require PARAMTYPE attribute
 *    2694    2013-10-25  blaschke-oss NAME attribute not required by DOM parser (part 2)
 *    2695    2013-10-25  blaschke-oss parseMETHODCALL allows LOCALCLASSPATH and LOCALINSTANCEPATH
 *    2696    2013-10-29  blaschke-oss parseIRETURNVALUE ignores VALUE and VALUE.ARRAY
 *    2699    2013-11-05  blaschke-oss parseQUALIFIER does not require TYPE attribute
 *    2700    2013-11-07  blaschke-oss PROPERTY does not require TYPE attribute
 *    2701    2013-11-07  blaschke-oss PROPERTY.ARRAY does not require TYPE attribute
 *    2702    2013-11-07  blaschke-oss Bad PROPERTY.ARRAY ARRAYSIZE generates NumberFormatException
 *    2703    2013-11-08  blaschke-oss MethodNode should not require TYPE attribute
 *    2704    2013-11-11  blaschke-oss PARAMETER does not require TYPE attribute
 *    2705    2013-11-11  blaschke-oss PARAMETER.ARRAY does not require TYPE attribute
 *    2706    2013-11-11  blaschke-oss Bad PARAMETER.REFARRAY ARRAYSIZE generates NumberFormatException
 *    2707    2013-11-12  blaschke-oss INSTANCENAME ignores KEYVALUE and VALUE.REFERENCE children
 *    2710    2013-11-13  blaschke-oss parseVALUEOBJECTWITH(LOCAL)PATH ignores (LOCAL)CLASSPATH child
 *    2711    2013-11-13  blaschke-oss LOCALNAMESPACEPATH allows 0 NAMESPACE children
 *    2713    2013-11-22  blaschke-oss Enforce loose validation of CIM-XML documents
 *    2715    2013-11-26  blaschke-oss Add VALUE.NULL support
 *    2538    2013-11-28  blaschke-oss CR14: Support new CORRELATOR element
 */

package org.sblim.cimclient.internal.cimxml;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;

import javax.cim.CIMArgument;
import javax.cim.CIMClass;
import javax.cim.CIMClassProperty;
import javax.cim.CIMDataType;
import javax.cim.CIMDateTime;
import javax.cim.CIMDateTimeAbsolute;
import javax.cim.CIMDateTimeInterval;
import javax.cim.CIMFlavor;
import javax.cim.CIMInstance;
import javax.cim.CIMMethod;
import javax.cim.CIMNamedElementInterface;
import javax.cim.CIMObjectPath;
import javax.cim.CIMParameter;
import javax.cim.CIMProperty;
import javax.cim.CIMQualifier;
import javax.cim.CIMQualifierType;
import javax.cim.CIMScope;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;
import javax.wbem.WBEMException;

import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cim.CIMQualifiedElementInterfaceImpl;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.Util;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.sblim.cimclient.internal.util.XMLHostStr;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * Class CIMXMLParserImpl is the main class of CIM-XML DOM parser.
 * 
 */
public class CIMXMLParserImpl {

	/*
	 * ebak: local object path - should be used by parseLOCALCLASSPATH(),
	 * parseLOCALINSTANCEPATH(), parseCLASSNAME(), parseINSTANCENAME(),
	 * parseCLASS() and parseINSTANCE()
	 */
	private static LocalPathBuilder cLocalPathBuilder = new LocalPathBuilder(null);

	/**
	 * setLocalObjectPath
	 * 
	 * @param pLocalOp
	 *            - empty fields of parsed objectpaths will be substituted by
	 *            fields coming from this parameter
	 */
	public static void setLocalObjectPath(CIMObjectPath pLocalOp) {
		cLocalPathBuilder = new LocalPathBuilder(pLocalOp);
	}

	/*
	 * / local object path
	 */

	/**
	 * parseCIM
	 * 
	 * @param pCimE
	 * @return CIMMessage
	 * @throws CIMXMLParseException
	 */
	public static CIMMessage parseCIM(Element pCimE) throws CIMXMLParseException {
		// <!ELEMENT CIM (MESSAGE|DECLARATION)>
		// <!ATTLIST CIM %CIMVERSION;%DTDVERSION;>
		Attr cim_cimversionA = (Attr) searchAttribute(pCimE, "CIMVERSION");
		if (cim_cimversionA == null) throw new CIMXMLParseException(
				"CIM element missing CIMVERSION attribute!");
		String cimversion = cim_cimversionA.getNodeValue();

		Attr cim_dtdversionA = (Attr) searchAttribute(pCimE, "DTDVERSION");
		if (cim_dtdversionA == null) throw new CIMXMLParseException(
				"CIM element missing DTDVERSION attribute!");
		String dtdversion = cim_dtdversionA.getNodeValue();

		// MESSAGE
		Element messageA[] = searchNodes(pCimE, "MESSAGE", 0, 1, false);
		if (messageA != null) {
			CIMMessage message = parseMESSAGE(cimversion, dtdversion, messageA[0]);
			message.setCIMVersion(cimversion);
			message.setDTDVersion(dtdversion);
			return message;
		}

		// DECLARATION
		if (searchNodes(pCimE, "DECLARATION", 0, 1, false) != null) throw new CIMXMLParseException(
				"DECLARATION element not supported!");

		throw new CIMXMLParseException("CIM element missing required child element!");
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// Value Elements
	// ////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * parseVALUE - supports the non-standard TYPE attribute
	 * 
	 * @param pValueE
	 * @return TypedValue, type is null if no TYPE attribute was found, the
	 *         value is always String, the caller method have to convert it.
	 * @throws CIMXMLParseException
	 */
	public static TypedValue parseVALUE(Element pValueE) throws CIMXMLParseException {
		// <! ELEMENT VALUE (#PCDATA)>

		String typeStr = attribute(pValueE, "TYPE");
		CIMDataType type = typeStr == null ? null : parseScalarTypeStr(typeStr);
		// ebak: empty VALUE element is parsed as empty String
		NodeList list = pValueE.getChildNodes();
		StringBuilder valueStr = new StringBuilder();
		for (int i = 0; i < list.getLength(); i++) {
			Text t = (Text) list.item(i);
			if (t != null) {
				String nodeValue = t.getNodeValue();
				if (nodeValue != null) valueStr.append(nodeValue);
			}
		}
		return new TypedValue(type, valueStr.toString());
	}

	/**
	 * parseVALUEARRAY - supports the non-standard TYPE attribute
	 * 
	 * @param pValueArrayE
	 * @return TypedValue, type is null if no TYPE attribute was found, the
	 *         value is always String[], the caller method have to convert it.
	 * @throws CIMXMLParseException
	 */
	public static TypedValue parseVALUEARRAY(Element pValueArrayE) throws CIMXMLParseException {
		// <! ELEMENT VALUE.ARRAY (VALUE|VALUE.NULL)*>
		String typeStr = attribute(pValueArrayE, "TYPE");
		CIMDataType type = typeStr == null ? null : parseArrayTypeStr(typeStr);

		// Process node list here, order of VALUE/VALUE.NULL IS important
		NodeList nl = pValueArrayE.getChildNodes();
		if (nl == null || nl.getLength() == 0) return new TypedValue(type, new String[0]);
		Vector<String> resStringV = new Vector<String>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n instanceof Text && "".equalsIgnoreCase(n.getNodeValue().trim())) continue;
			String name = n.getNodeName();
			if (name.equals("VALUE")) {
				resStringV.add((String) parseVALUE((Element) n).getValue());
			} else if (name.equals("VALUE.NULL")) {
				resStringV.add(null);
			} else if (NODENAME_HASH.containsKey(name)) { throw new CIMXMLParseException(
					"VALUE.ARRAY element contains invalid child element " + name + "!"); }
		}
		return new TypedValue(type, resStringV.toArray(new String[0]));
	}

	/**
	 * parseVALUEREFERENCE
	 * 
	 * @param pValuereferenceE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseVALUEREFERENCE(Element pValuereferenceE)
			throws CIMXMLParseException {
		// <!ELEMENT VALUE.REFERENCE
		// (CLASSPATH|LOCALCLASSPATH|CLASSNAME|INSTANCEPATH|LOCALINSTANCEPATH|INSTANCENAME)>

		// CLASSPATH
		Element classpathA[] = searchNodes(pValuereferenceE, "CLASSPATH", 0, 1, false);
		if (classpathA != null) {
			CIMObjectPath op = parseCLASSPATH(classpathA[0]);
			return op;
		}

		// LOCALCLASSPATH
		Element localclasspathA[] = searchNodes(pValuereferenceE, "LOCALCLASSPATH", 0, 1, false);
		if (localclasspathA != null) {
			CIMObjectPath op = parseLOCALCLASSPATH(localclasspathA[0]);
			return op;
		}

		// CLASSNAME
		Element classnameA[] = searchNodes(pValuereferenceE, "CLASSNAME", 0, 1, false);
		if (classnameA != null) {
			CIMObjectPath op = parseCLASSNAME(classnameA[0]);
			if (op != null && op.getNamespace() != null) {
				// LocalPathBuilder includes default namespace in CLASSNAME
				// elements, needs to be stripped
				op = new CIMObjectPath(op.getScheme(), op.getHost(), op.getPort(), null, op
						.getObjectName(), op.getKeys(), op.getXmlSchemaName());
			}
			return op;
		}

		// INSTANCEPATH
		Element instancepathA[] = searchNodes(pValuereferenceE, "INSTANCEPATH", 0, 1, false);
		if (instancepathA != null) {
			CIMObjectPath op = parseINSTANCEPATH(instancepathA[0]);
			return op;
		}

		// LOCALINSTANCEPATH
		Element localinstancepathA[] = searchNodes(pValuereferenceE, "LOCALINSTANCEPATH", 0, 1,
				false);
		if (localinstancepathA != null) {
			CIMObjectPath op = parseLOCALINSTANCEPATH(localinstancepathA[0]);
			return op;
		}

		// INSTANCENAME
		Element instancenameA[] = searchNodes(pValuereferenceE, "INSTANCENAME", 0, 1, false);
		if (instancenameA != null) {
			CIMObjectPath op = parseINSTANCENAME(instancenameA[0]);
			if (op != null && op.getNamespace() != null) {
				// LocalPathBuilder includes default namespace in INSTANCENAME
				// elements, needs to be stripped
				op = new CIMObjectPath(op.getScheme(), op.getHost(), op.getPort(), null, op
						.getObjectName(), op.getKeys(), op.getXmlSchemaName());
			}
			return op;
		}

		throw new CIMXMLParseException("VALUE.REFERENCE element missing required child element!");
	}

	/**
	 * parseVALUEREFARRAY
	 * 
	 * @param pValueRefArrayE
	 * @return CIMObjectPath[]
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath[] parseVALUEREFARRAY(Element pValueRefArrayE)
			throws CIMXMLParseException {
		// <! ELEMENT VALUE.REFARRAY (VALUE.REFERENCE|VALUE.NULL)*>

		// Process node list here, order of VALUE.REFERENCE/VALUE.NULL IS
		// important
		NodeList nl = pValueRefArrayE.getChildNodes();
		if (nl == null || nl.getLength() == 0) return new CIMObjectPath[0];
		Vector<CIMObjectPath> resObjectPathV = new Vector<CIMObjectPath>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n instanceof Text && "".equalsIgnoreCase(n.getNodeValue().trim())) continue;
			String name = n.getNodeName();
			if (name.equals("VALUE.REFERENCE")) {
				resObjectPathV.add(parseVALUEREFERENCE((Element) n));
			} else if (name.equals("VALUE.NULL")) {
				resObjectPathV.add(null);
			} else if (NODENAME_HASH.containsKey(name)) { throw new CIMXMLParseException(
					"VALUE.REFARRAY element contains invalid child element " + name + "!"); }
		}
		return resObjectPathV.toArray(new CIMObjectPath[0]);
	}

	/**
	 * parseVALUEOBJECT
	 * 
	 * @param pValueObjectE
	 * @return CIMNamedElementInterface (CIMClass|CIMInstance)
	 * @throws CIMXMLParseException
	 */
	public static CIMNamedElementInterface parseVALUEOBJECT(Element pValueObjectE)
			throws CIMXMLParseException {
		// <! ELEMENT VALUE.OBJECT (CLASS|INSTANCE)>

		// CLASS
		Element classA[] = searchNodes(pValueObjectE, "CLASS", 0, 1, false);
		if (classA != null) {
			CIMClass obj = parseCLASS(classA[0]);
			return obj;
		}

		// INSTANCE
		Element instanceA[] = searchNodes(pValueObjectE, "INSTANCE", 0, 1, false);
		if (instanceA != null) {
			CIMInstance obj = parseINSTANCE(instanceA[0]);
			return obj;
		}

		throw new CIMXMLParseException("VALUE.OBJECT element missing required child element!");
	}

	private static final String nodesVALUENAMEDINSTANCE[] = { "INSTANCENAME", "INSTANCE" };

	/**
	 * parseVALUENAMEDINSTANCE
	 * 
	 * @param pValueNamedInstanceE
	 * @return CIMInstance
	 * @throws CIMXMLParseException
	 */
	public static CIMInstance parseVALUENAMEDINSTANCE(Element pValueNamedInstanceE)
			throws CIMXMLParseException {
		// <! ELEMENT VALUE.NAMEDINSTANCE (INSTANCENAME,INSTANCE)>

		// INSTANCENAME
		Element instancenameA[] = searchNodes(pValueNamedInstanceE, "INSTANCENAME", 1, 1, true);
		if (instancenameA == null) { throw new CIMXMLParseException(
				"VALUE.NAMEDINSTANCE element missing INSTANCENAME child element!"); }
		CIMObjectPath op = parseINSTANCENAME(instancenameA[0]);

		// INSTANCE
		Element instanceA[] = searchNodes(pValueNamedInstanceE, "INSTANCE", 1, 1, true);
		if (instanceA == null) { throw new CIMXMLParseException(
				"VALUE.NAMEDINSTANCE element missing INSTANCE child element!"); }
		CIMInstance inst = parseINSTANCE(instanceA[0], op); // BB mod
		checkOtherNodes(pValueNamedInstanceE, nodesVALUENAMEDINSTANCE);
		return inst;
	}

	private static final String nodesVALUEINSTANCEWITHPATH[] = { "INSTANCEPATH", "INSTANCE" };

	/**
	 * parseVALUEINSTANCEWITHPATH
	 * 
	 * @param pValueNamedInstanceE
	 * @return CIMInstance
	 * @throws CIMXMLParseException
	 */
	public static CIMInstance parseVALUEINSTANCEWITHPATH(Element pValueNamedInstanceE)
			throws CIMXMLParseException {
		// <! ELEMENT VALUE.INSTANCEWITHPATH (INSTANCEPATH,INSTANCE)>

		// INSTANCEPATH
		Element instancepathA[] = searchNodes(pValueNamedInstanceE, "INSTANCEPATH", 1, 1, true);
		if (instancepathA == null) { throw new CIMXMLParseException(
				"VALUE.INSTANCEWITHPATH element missing INSTANCEPATH child element!"); }
		CIMObjectPath op = parseINSTANCEPATH(instancepathA[0]);

		// INSTANCE
		Element instanceA[] = searchNodes(pValueNamedInstanceE, "INSTANCE", 1, 1, true);
		if (instanceA == null) { throw new CIMXMLParseException(
				"VALUE.INSTANCEWITHPATH element missing INSTANCE child element!"); }
		CIMInstance inst = parseINSTANCE(instanceA[0], op); // BB mod
		checkOtherNodes(pValueNamedInstanceE, nodesVALUEINSTANCEWITHPATH);
		return inst;
	}

	private static final String nodesVALUENAMEDOBJECTi[] = { "INSTANCENAME", "INSTANCE" };

	/**
	 * parseVALUENAMEDOBJECT
	 * 
	 * @param pValueNamedObjectE
	 * @return CIMNamedElementInterface
	 * @throws CIMXMLParseException
	 */
	public static CIMNamedElementInterface parseVALUENAMEDOBJECT(Element pValueNamedObjectE)
			throws CIMXMLParseException {
		// <! ELEMENT VALUE.NAMEDOBJECT (CLASS|(INSTANCENAME,INSTANCE))>

		// CLASS
		Element classA[] = searchNodes(pValueNamedObjectE, "CLASS", 0, 1, false);
		if (classA != null) {
			CIMClass obj = parseCLASS(classA[0]);
			// checkOtherNodes(pValueNamedObjectE, nodesVALUENAMEDOBJECTc);
			return obj;
		}

		// INSTANCENAME
		Element instancenameA[] = searchNodes(pValueNamedObjectE, "INSTANCENAME", 0, 1, true);
		if (instancenameA != null) {
			CIMObjectPath op = parseINSTANCENAME(instancenameA[0]);

			// INSTANCE
			Element instanceA[] = searchNodes(pValueNamedObjectE, "INSTANCE", 0, 1, true);
			if (instanceA == null) { throw new CIMXMLParseException(
					"VALUE.NAMEDOBJECT element missing INSTANCE child element!"); }
			CIMInstance inst = parseINSTANCE(instanceA[0], op); // BB mod
			checkOtherNodes(pValueNamedObjectE, nodesVALUENAMEDOBJECTi);
			return inst;
		}

		throw new CIMXMLParseException("VALUE.NAMEDOBJECT element missing required child element!");
	}

	private static final String nodesVALUEOBJECTWITHPATHcls[] = { "CLASSPATH", "CLASS" };

	private static final String nodesVALUEOBJECTWITHPATHins[] = { "INSTANCEPATH", "INSTANCE" };

	/**
	 * parseVALUEOBJECTWITHPATH
	 * 
	 * @param pValueObjectWithPathE
	 * @return CIMNamedElementInterface
	 * @throws CIMXMLParseException
	 */
	public static CIMNamedElementInterface parseVALUEOBJECTWITHPATH(Element pValueObjectWithPathE)
			throws CIMXMLParseException {
		// <! ELEMENT VALUE.OBJECTWITHPATH
		// ((CLASSPATH,CLASS)|(INSTANCEPATH,INSTANCE))>

		// CLASSPATH
		Element classpathA[] = searchNodes(pValueObjectWithPathE, "CLASSPATH", 0, 1, true);
		if (classpathA != null) {
			CIMObjectPath op = parseCLASSPATH(classpathA[0]);

			// CLASS
			Element classA[] = searchNodes(pValueObjectWithPathE, "CLASS", 0, 1, true);
			if (classA == null) { throw new CIMXMLParseException(
					"VALUE.OBJECTWITHPATH element missing CLASS child element!"); }

			CIMClass obj = parseCLASS(classA[0], op);
			checkOtherNodes(pValueObjectWithPathE, nodesVALUEOBJECTWITHPATHcls);
			return obj;
		}

		// INSTANCEPATH
		Element instancepathA[] = searchNodes(pValueObjectWithPathE, "INSTANCEPATH", 0, 1, true);
		if (instancepathA != null) {
			CIMObjectPath op = parseINSTANCEPATH(instancepathA[0]);

			// INSTANCE
			Element instanceA[] = searchNodes(pValueObjectWithPathE, "INSTANCE", 0, 1, true);
			if (instanceA == null) { throw new CIMXMLParseException(
					"VALUE.OBJECTWITHPATH element missing INSTANCE child element!"); }
			CIMInstance inst = parseINSTANCE(instanceA[0], op); // BB mod
			checkOtherNodes(pValueObjectWithPathE, nodesVALUEOBJECTWITHPATHins);
			return inst;
		}

		throw new CIMXMLParseException(
				"VALUE.OBJECTWITHPATH element missing required child element!");
	}

	private static final String nodesVALUEOBJECTWITHLOCALPATHcls[] = { "LOCALCLASSPATH", "CLASS" };

	private static final String nodesVALUEOBJECTWITHLOCALPATHins[] = { "LOCALINSTANCEPATH",
			"INSTANCE" };

	/**
	 * parseVALUEOBJECTWITHLOCALPATH
	 * 
	 * @param pValueObjectWithLocalPathE
	 * @return CIMNamedElementInterface
	 * @throws CIMXMLParseException
	 */
	public static CIMNamedElementInterface parseVALUEOBJECTWITHLOCALPATH(
			Element pValueObjectWithLocalPathE) throws CIMXMLParseException {
		// <! ELEMENT VALUE.OBJECTWITHLOCALPATH
		// ((LOCALCLASSPATH,CLASS)|(LOCALINSTANCEPATH,INSTANCE))>

		// LOCALCLASSPATH
		Element localclasspathA[] = searchNodes(pValueObjectWithLocalPathE, "LOCALCLASSPATH", 0, 1,
				true);
		if (localclasspathA != null) {
			CIMObjectPath op = parseLOCALCLASSPATH(localclasspathA[0]);

			// CLASS
			Element classA[] = searchNodes(pValueObjectWithLocalPathE, "CLASS", 0, 1, true);
			if (classA == null) { throw new CIMXMLParseException(
					"VALUE.OBJECTWITHLOCALPATH element missing CLASS child element!"); }

			CIMClass obj = parseCLASS(classA[0], op);
			checkOtherNodes(pValueObjectWithLocalPathE, nodesVALUEOBJECTWITHLOCALPATHcls);
			return obj;
		}

		// LOCALINSTANCEPATH
		Element localinstancepathA[] = searchNodes(pValueObjectWithLocalPathE, "LOCALINSTANCEPATH",
				0, 1, true);
		if (localinstancepathA != null) {
			CIMObjectPath op = parseLOCALINSTANCEPATH(localinstancepathA[0]);

			// INSTANCE
			Element instanceA[] = searchNodes(pValueObjectWithLocalPathE, "INSTANCE", 0, 1, true);
			if (instanceA == null) { throw new CIMXMLParseException(
					"VALUE.OBJECTWITHLOCALPATH element missing INSTANCE child element!"); }
			CIMInstance inst = parseINSTANCE(instanceA[0], op); // BB mod
			checkOtherNodes(pValueObjectWithLocalPathE, nodesVALUEOBJECTWITHLOCALPATHins);
			return inst;
		}

		throw new CIMXMLParseException(
				"VALUE.OBJECTWITHLOCALPATH element missing required child element!");
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// Naming and Location elements
	// ////////////////////////////////////////////////////////////////////////////////////////

	private static final String nodesNAMESPACEPATH[] = { "HOST", "LOCALNAMESPACEPATH" };

	/**
	 * parseNAMESPACEPATH
	 * 
	 * @param pNameSpacePathE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseNAMESPACEPATH(Element pNameSpacePathE)
			throws CIMXMLParseException {
		// <!ELEMENT NAMESPACEPATH (HOST,LOCALNAMESPACEPATH)>
		// HOST
		Element hostA[] = searchNodes(pNameSpacePathE, "HOST", 1, 1, true);
		if (hostA == null) { throw new CIMXMLParseException(
				"NAMESPACEPATH element missing HOST child element!"); }
		XMLHostStr xmlHostStr = new XMLHostStr(parseHOST(hostA[0]));
		// LOCALNAMESPACE
		Element localnamespacepathA[] = searchNodes(pNameSpacePathE, "LOCALNAMESPACEPATH", 1, 1,
				true);
		if (localnamespacepathA == null) { throw new CIMXMLParseException(
				"NAMESPACEPATH element missing LOCALNAMESPACEPATH child element!"); }
		String nameSpace = parseLOCALNAMESPACEPATH(localnamespacepathA[0]);
		/*
		 * CIMObjectPath( String scheme, String host, String port, String
		 * namespace, String objectName, CIMProperty[] keys )
		 */
		checkOtherNodes(pNameSpacePathE, nodesNAMESPACEPATH);
		return new CIMObjectPath(xmlHostStr.getProtocol(), xmlHostStr.getHost(), xmlHostStr
				.getPort(), nameSpace, null, null);
	}

	/**
	 * parseLOCALNAMESPACEPATH
	 * 
	 * @param pLocalNameSpaceE
	 * @return String
	 * @throws CIMXMLParseException
	 */
	public static String parseLOCALNAMESPACEPATH(Element pLocalNameSpaceE)
			throws CIMXMLParseException {
		// <!ELEMENT LOCALNAMESPACE (NAMESPACE+))>

		Element[] nameSpaceElementA = searchNodes(pLocalNameSpaceE, "NAMESPACE", 1,
				Integer.MAX_VALUE, false);
		if (nameSpaceElementA == null) {
			if (WBEMConfiguration.getGlobalConfiguration().allowEmptyLocalNameSpacePath()
					&& cLocalPathBuilder != null) return cLocalPathBuilder.getBasePath()
					.getNamespace();
			throw new CIMXMLParseException(
					"LOCALNAMESPACEPATH element missing NAMESPACE child element!");
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nameSpaceElementA.length; i++) {
			Element namespaceE = nameSpaceElementA[i];
			String s = parseNAMESPACE(namespaceE);
			if (i > 0) sb.append("/" + s);
			else sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * parseHOST
	 * 
	 * @param pHostE
	 * @return String
	 */
	public static String parseHOST(Element pHostE) {
		// <!ELEMENT HOST (#PCDATA)>

		Text valueT = (Text) searchFirstChild(pHostE);
		String host = valueT.getNodeValue();
		return host;
	}

	private static final String nodesNAMESPACE[] = {};

	/**
	 * parseNAMESPACE
	 * 
	 * @param pNameSpaceE
	 * @return String
	 * @throws CIMXMLParseException
	 */
	public static String parseNAMESPACE(Element pNameSpaceE) throws CIMXMLParseException {
		// <!ELEMENT NAMESPACE EMPTY>
		// <!ATTLIST NAMESPACE %NAME;>

		Attr namespace_nameA = (Attr) searchAttribute(pNameSpaceE, "NAME");
		if (namespace_nameA == null) throw new CIMXMLParseException(
				"NAMESPACE element missing NAME attribute!");
		String n = namespace_nameA.getValue();
		checkOtherNodes(pNameSpaceE, nodesNAMESPACE);
		return n;
	}

	private static final String nodesCLASSPATH[] = { "NAMESPACEPATH", "CLASSNAME" };

	/**
	 * parseCLASSPATH
	 * 
	 * @param pClassPathE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseCLASSPATH(Element pClassPathE) throws CIMXMLParseException {
		// <!ELEMENT CLASSPATH (NAMESPACEPATH,CLASSNAME)>

		// NAMESPACEPATH
		Element namespacepathA[] = searchNodes(pClassPathE, "NAMESPACEPATH", 1, 1, true);
		if (namespacepathA == null) { throw new CIMXMLParseException(
				"CLASSPATH element missing NAMESPACEPATH child element!"); }
		CIMObjectPath nsPath = parseNAMESPACEPATH(namespacepathA[0]);
		// CLASSNAME
		Element classnameA[] = searchNodes(pClassPathE, "CLASSNAME", 1, 1, true);
		if (classnameA == null) { throw new CIMXMLParseException(
				"CLASSPATH element missing CLASSNAME child element!"); }
		String className = parseClassName(classnameA[0]);
		/*
		 * CIMObjectPath( String scheme, String host, String port, String
		 * namespace, String objectName, CIMProperty[] keys )
		 */
		checkOtherNodes(pClassPathE, nodesCLASSPATH);
		return new CIMObjectPath(nsPath.getScheme(), nsPath.getHost(), nsPath.getPort(), nsPath
				.getNamespace(), className, null);
	}

	private static final String nodesLOCALCLASSPATH[] = { "LOCALNAMESPACEPATH", "CLASSNAME" };

	/**
	 * parseLOCALCLASSPATH
	 * 
	 * @param pClassPathE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseLOCALCLASSPATH(Element pClassPathE)
			throws CIMXMLParseException {
		// <!ELEMENT LOCALCLASSPATH (LOCALNAMESPACEPATH,CLASSNAME)>

		// NAMESPACEPATH
		Element localnamespacepathA[] = searchNodes(pClassPathE, "LOCALNAMESPACEPATH", 1, 1, true);
		if (localnamespacepathA == null) { throw new CIMXMLParseException(
				"LOCALCLASSPATH element missing LOCALNAMESPACEPATH child element!"); }
		String nameSpace = parseLOCALNAMESPACEPATH(localnamespacepathA[0]);

		// CLASSNAME
		Element classnameA[] = searchNodes(pClassPathE, "CLASSNAME", 1, 1, true);
		if (classnameA == null) { throw new CIMXMLParseException(
				"LOCALCLASSPATH element missing CLASSNAME child element!"); }
		CIMObjectPath op = parseCLASSNAME(classnameA[0]);
		checkOtherNodes(pClassPathE, nodesLOCALCLASSPATH);
		return cLocalPathBuilder.build(op.getObjectName(), nameSpace);
	}

	private static final String nodesCLASSNAME[] = {};

	/**
	 * parseClassName
	 * 
	 * @param pClassNameE
	 * @return String
	 * @throws CIMXMLParseException
	 */
	public static String parseClassName(Element pClassNameE) throws CIMXMLParseException {
		// <!ELEMENT CLASSNAME EMPTY>
		// <!ATTLIST CLASSNAME %NAME;>
		Attr classname_nameA = (Attr) searchAttribute(pClassNameE, "NAME");
		if (classname_nameA == null) throw new CIMXMLParseException(
				"CLASSNAME element missing NAME attribute!");
		checkOtherNodes(pClassNameE, nodesCLASSNAME);
		return classname_nameA.getNodeValue();
	}

	/**
	 * parseCLASSNAME
	 * 
	 * @param pClassNameE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseCLASSNAME(Element pClassNameE) throws CIMXMLParseException {
		return cLocalPathBuilder.build(parseClassName(pClassNameE), null);
	}

	private static final String nodesINSTANCEPATH[] = { "NAMESPACEPATH", "INSTANCENAME" };

	/**
	 * parseINSTANCEPATH
	 * 
	 * @param pInstancePathE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseINSTANCEPATH(Element pInstancePathE)
			throws CIMXMLParseException {
		// <!ELEMENT INSTANCEPATH (NAMESPACEPATH,INSTANCENAME)>

		// NAMESPACEPATH
		Element namespacepathA[] = searchNodes(pInstancePathE, "NAMESPACEPATH", 1, 1, true);
		if (namespacepathA == null) { throw new CIMXMLParseException(
				"INSTANCEPATH element missing NAMESPACEPATH child element!"); }
		CIMObjectPath nsPath = parseNAMESPACEPATH(namespacepathA[0]);
		// INSTANCENAME
		Element instancenameA[] = searchNodes(pInstancePathE, "INSTANCENAME", 1, 1, true);
		if (instancenameA == null) { throw new CIMXMLParseException(
				"INSTANCEPATH element missing INSTANCENAME child element!"); }
		CIMObjectPath op = parseINSTANCENAME(instancenameA[0]);
		// ebak: change host and namespace
		checkOtherNodes(pInstancePathE, nodesINSTANCEPATH);
		return new CIMObjectPath(nsPath.getScheme(), nsPath.getHost(), nsPath.getPort(), nsPath
				.getNamespace(), op.getObjectName(), op.getKeys());
	}

	private static final String nodesLOCALINSTANCEPATH[] = { "LOCALNAMESPACEPATH", "INSTANCENAME" };

	/**
	 * parseLOCALINSTANCEPATH
	 * 
	 * @param pLocalInstancePathE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseLOCALINSTANCEPATH(Element pLocalInstancePathE)
			throws CIMXMLParseException {
		// <!ELEMENT LOCALINSTANCEPATH (LOCALNAMESPACEPATH,INSTANCENAME)>

		// LOCALNAMESPACEPATH
		Element localnamespacepathA[] = searchNodes(pLocalInstancePathE, "LOCALNAMESPACEPATH", 1,
				1, true);
		if (localnamespacepathA == null) { throw new CIMXMLParseException(
				"LOCALINSTANCEPATH element missing LOCALNAMESPACEPATH child element!"); }
		String nameSpace = parseLOCALNAMESPACEPATH(localnamespacepathA[0]);

		// INSTANCENAME
		Element instancenameA[] = searchNodes(pLocalInstancePathE, "INSTANCENAME", 1, 1, true);
		if (instancenameA == null) { throw new CIMXMLParseException(
				"LOCALINSTANCEPATH element missing INSTANCENAME child element!"); }
		CIMObjectPath op = parseINSTANCENAME(instancenameA[0]);
		/*
		 * CIMObjectPath(String objectName, String namespace, CIMProperty[]
		 * keys)
		 */
		checkOtherNodes(pLocalInstancePathE, nodesLOCALINSTANCEPATH);
		return cLocalPathBuilder.build(op.getObjectName(), nameSpace, op.getKeys());
	}

	private static final String nodesINSTANCENAME[] = { "KEYBINDING", "KEYVALUE", "VALUE.REFERENCE" };

	/**
	 * parseINSTANCENAME
	 * 
	 * @param pInstanceNameE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseINSTANCENAME(Element pInstanceNameE)
			throws CIMXMLParseException {
		// <!ELEMENT INSTANCENAME (KEYBINDING*|KEYVALUE?|VALUE.REFERENCE?)>
		// <!ATTLIST INSTANCENAME %CLASSNAME;>
		Attr instance_classnameA = (Attr) searchAttribute(pInstanceNameE, "CLASSNAME");
		if (instance_classnameA == null) throw new CIMXMLParseException(
				"INSTANCENAME element missing CLASSNAME attribute!");
		String opClassName = instance_classnameA.getNodeValue();

		// KEYBINDING
		Element[] keyBindingElementA = searchNodes(pInstanceNameE, "KEYBINDING", 0,
				Integer.MAX_VALUE, false);
		if (keyBindingElementA != null) {
			CIMProperty<?>[] keys = new CIMProperty[keyBindingElementA.length];
			for (int i = 0; i < keyBindingElementA.length; i++) {
				Element keybindingE = keyBindingElementA[i];
				keys[i] = parseKEYBINDING(keybindingE);
			}
			return cLocalPathBuilder.build(opClassName, null, keys);
		}

		// KEYVALUE
		Element keyvalueA[] = searchNodes(pInstanceNameE, "KEYVALUE", 0, 1, false);
		if (keyvalueA != null) {
			CIMProperty<?>[] keys = new CIMProperty[1];
			TypedValue propTypedVal = parseKEYVALUE(keyvalueA[0]);
			keys[0] = new CIMProperty<Object>("", propTypedVal.getType(), propTypedVal.getValue(),
					true, false, null);
			return cLocalPathBuilder.build(opClassName, null, keys);
		}

		// VALUE.REFERENCE
		Element valuereferenceA[] = searchNodes(pInstanceNameE, "VALUE.REFERENCE", 0, 1, false);
		if (valuereferenceA != null) {
			CIMProperty<?>[] keys = new CIMProperty[1];
			CIMObjectPath op = parseVALUEREFERENCE(valuereferenceA[0]);
			keys[0] = new CIMProperty<CIMObjectPath>("", new CIMDataType(op.getObjectName()), op,
					true, false, null);
			return cLocalPathBuilder.build(opClassName, null, keys);
		}

		checkOtherNodes(pInstanceNameE, nodesINSTANCENAME);

		return new CIMObjectPath(null, null, null, null, opClassName, null);
	}

	/**
	 * parseOBJECTPATH
	 * 
	 * @param pObjectPathE
	 * @return CIMObjectPath
	 * @throws CIMXMLParseException
	 */
	public static CIMObjectPath parseOBJECTPATH(Element pObjectPathE) throws CIMXMLParseException {
		// <!ELEMENT OBJECTPATH (INSTANCEPATH|CLASSPATH) >

		// INSTANCEPATH
		Element instancepathA[] = searchNodes(pObjectPathE, "INSTANCEPATH", 0, 1, false);
		if (instancepathA != null) {
			CIMObjectPath op = parseINSTANCEPATH(instancepathA[0]);
			return op;
		}

		// CLASSPATH
		Element classpathA[] = searchNodes(pObjectPathE, "CLASSPATH", 0, 1, false);
		if (classpathA != null) {
			CIMObjectPath op = parseCLASSPATH(classpathA[0]);
			return op;
		}

		throw new CIMXMLParseException("OBJECTPATH element missing required child element!");
	}

	/**
	 * parseKEYBINDING
	 * 
	 * @param pKeyBindingE
	 * @return CIMProperty
	 * @throws CIMXMLParseException
	 */
	public static CIMProperty<?> parseKEYBINDING(Element pKeyBindingE) throws CIMXMLParseException {
		// <!ELEMENT KEYBINDING (KEYVALUE|VALUE.REFERENCE) >
		// <!ATTLIST KEYBINDING %NAME;>

		Attr keybinding_nameA = (Attr) searchAttribute(pKeyBindingE, "NAME");
		if (keybinding_nameA == null) throw new CIMXMLParseException(
				"KEYBINDING element missing NAME attribute!");
		String propName = keybinding_nameA.getValue();

		// KEYVALUE
		Element keyvalueA[] = searchNodes(pKeyBindingE, "KEYVALUE", 0, 1, false);
		if (keyvalueA != null) {
			TypedValue propTypedVal = parseKEYVALUE(keyvalueA[0]);
			return new CIMProperty<Object>(propName, propTypedVal.getType(), propTypedVal
					.getValue(), true, false, null);
		}

		// VALUE.REFERENCE
		Element valuereferenceA[] = searchNodes(pKeyBindingE, "VALUE.REFERENCE", 0, 1, false);
		if (valuereferenceA != null) {
			CIMObjectPath op = parseVALUEREFERENCE(valuereferenceA[0]);
			return new CIMProperty<CIMObjectPath>(propName, new CIMDataType(op.getObjectName()),
					op, true, false, null);
		}

		throw new CIMXMLParseException("KEYBINDING element missing required child element!");
	}

	private static final TreeMap<String, CIMDataType> TYPESTR_MAP = new TreeMap<String, CIMDataType>(
			new Comparator<Object>() {

				public int compare(Object pO1, Object pO2) {
					return ((String) pO1).compareToIgnoreCase((String) pO2);
				}

			});

	static {
		TYPESTR_MAP.put(MOF.DT_UINT8, CIMDataType.UINT8_T);
		TYPESTR_MAP.put(MOF.DT_UINT16, CIMDataType.UINT16_T);
		TYPESTR_MAP.put(MOF.DT_UINT32, CIMDataType.UINT32_T);
		TYPESTR_MAP.put(MOF.DT_UINT64, CIMDataType.UINT64_T);
		TYPESTR_MAP.put(MOF.DT_SINT8, CIMDataType.SINT8_T);
		TYPESTR_MAP.put(MOF.DT_SINT16, CIMDataType.SINT16_T);
		TYPESTR_MAP.put(MOF.DT_SINT32, CIMDataType.SINT32_T);
		TYPESTR_MAP.put(MOF.DT_SINT64, CIMDataType.SINT64_T);
		TYPESTR_MAP.put(MOF.DT_REAL32, CIMDataType.REAL32_T);
		TYPESTR_MAP.put(MOF.DT_REAL64, CIMDataType.REAL64_T);
		TYPESTR_MAP.put(MOF.DT_CHAR16, CIMDataType.CHAR16_T);
		TYPESTR_MAP.put(MOF.DT_STR, CIMDataType.STRING_T);
		TYPESTR_MAP.put(MOF.DT_BOOL, CIMDataType.BOOLEAN_T);
		TYPESTR_MAP.put(MOF.DT_DATETIME, CIMDataType.DATETIME_T);
		// FIXME: ebak: What to do with those types which are not specified by
		// MOF's BNF?
		TYPESTR_MAP.put(MOF.INVALID, CIMDataType.INVALID_T);
		// FIXME: ebak: What is the string representation of OBJECT?
		TYPESTR_MAP.put("object", CIMDataType.OBJECT_T);
		TYPESTR_MAP.put(MOF.CLASS, CIMDataType.CLASS_T);
		TYPESTR_MAP.put(MOF.REFERENCE, new CIMDataType(""));
	}

	/**
	 * parseScalarTypeStr
	 * 
	 * @param pTypeStr
	 * @return CIMDataType
	 * @throws CIMXMLParseException
	 */
	public static CIMDataType parseScalarTypeStr(String pTypeStr) throws CIMXMLParseException {
		return parseTypeStr(pTypeStr, false);
	}

	/**
	 * parseArrayTypeStr
	 * 
	 * @param pTypeStr
	 * @return CIMDataType
	 * @throws CIMXMLParseException
	 */
	public static CIMDataType parseArrayTypeStr(String pTypeStr) throws CIMXMLParseException {
		return parseTypeStr(pTypeStr, true);
	}

	/**
	 * parseTypeStr
	 * 
	 * @param pTypeStr
	 * @param pArray
	 * @return CIMDataType
	 * @throws CIMXMLParseException
	 */
	public static CIMDataType parseTypeStr(String pTypeStr, boolean pArray)
			throws CIMXMLParseException {
		if (pTypeStr == null) return pArray ? CIMDataType.STRING_ARRAY_T : CIMDataType.STRING_T;
		CIMDataType type = TYPESTR_MAP.get(pTypeStr);
		if (type == null) throw new CIMXMLParseException("Unknown TYPE string:" + pTypeStr);
		if (pArray) {
			if (type.getType() == CIMDataType.REFERENCE) return new CIMDataType(type
					.getRefClassName(), 0);
			return CIMHelper.UnboundedArrayDataType(type.getType());
		}
		return type;
	}

	/**
	 * parseKEYVALUE
	 * 
	 * @param pKeyValueE
	 * @return TypedValue
	 * @throws CIMXMLParseException
	 */
	public static TypedValue parseKEYVALUE(Element pKeyValueE) throws CIMXMLParseException {
		/*
		 * <!ELEMENT KEYVALUE (#PCDATA)> <!ATTLIST KEYVALUE VALUETYPE
		 * (string|boolean|numeric) 'string') %CIMType; #IMPLIED>
		 */
		// ebak: if TYPE attribute is included there is no need to deal with
		// VALUETYPE attribute
		String typeStr = attribute(pKeyValueE, "TYPE");

		Text valueT = (Text) searchFirstChild(pKeyValueE);
		String valueStr = valueT == null ? null : valueT.getNodeValue();

		if (typeStr == null) {
			String valueTypeStr = attribute(pKeyValueE, "VALUETYPE");
			if (valueTypeStr == null) valueTypeStr = "string";
			ValueTypeHandler vtHandler = new ValueTypeHandler(valueTypeStr, valueStr);
			return new TypedValue(vtHandler.getType(), vtHandler.getValue());
		}
		Object value = valueStr == null ? "" : createJavaObject(typeStr, valueStr);
		return new TypedValue(parseScalarTypeStr(typeStr), value);
	}

	/**
	 * Class ValueTypeHandler determines the value and type of a KEYVALUE XML
	 * element, when the VALUETYPE attribute is provided instead of the exact
	 * TYPE attribute. There is a very similar code section in the PULL and SAX
	 * parser, but that section wasn't made common since in ideal case the DOM
	 * parser will be removed.
	 */
	private static class ValueTypeHandler {

		private CIMDataType iType;

		private Object iValue;

		/**
		 * 
		 * Ctor.
		 * 
		 * @param pValueTypeStr
		 * @param pValueStr
		 * @throws CIMXMLParseException
		 */
		public ValueTypeHandler(String pValueTypeStr, String pValueStr) throws CIMXMLParseException {

			if (pValueTypeStr == null) throw new CIMXMLParseException(
					"KEYVALUE element missing VALUETYPE attribute!");

			if (pValueTypeStr.equals("numeric")) {
				if (!setUInt64(pValueStr) && !setSInt64(pValueStr) && !setReal64(pValueStr)) throw new CIMXMLParseException(
						"Unparseable \"number\" value: " + pValueStr + " !");
			} else if (pValueTypeStr.equals(MOF.DT_STR)) {
				if (!setDTAbsolute(pValueStr) && !setDTInterval(pValueStr)) {
					this.iValue = pValueStr;
					this.iType = CIMDataType.STRING_T;
				}
			} else if (pValueTypeStr.equals(MOF.DT_BOOL)) {
				if (!setBoolean(pValueStr)) throw new CIMXMLParseException(
						"Unparseable \"boolean\" value: " + pValueStr + " !");
			} else {
				throw new CIMXMLParseException("KEYVALUE element's VALUETYPE attribute must be "
						+ MOF.DT_STR + ", " + MOF.DT_BOOL + " or numeric! " + pValueStr
						+ " is not allowed!");
			}
		}

		/**
		 * 
		 * getType
		 * 
		 * @return CIMDataType
		 */
		public CIMDataType getType() {
			return this.iType;
		}

		/**
		 * 
		 * getValue
		 * 
		 * @return Object
		 */
		public Object getValue() {
			return this.iValue;
		}

		private boolean setUInt64(String pValue) {
			try {
				this.iValue = new UnsignedInteger64(pValue);
			} catch (NumberFormatException e) {
				return false;
			}
			this.iType = CIMDataType.UINT64_T;
			return true;
		}

		private boolean setSInt64(String pValue) {
			try {
				this.iValue = new Long(pValue);
			} catch (NumberFormatException e) {
				return false;
			}
			this.iType = CIMDataType.SINT64_T;
			return true;
		}

		private boolean setReal64(String pValue) {
			try {
				if (WBEMConfiguration.getGlobalConfiguration().verifyJavaLangDoubleStrings()) {
					if (Util.isBadDoubleString(pValue)) return false;
				}
				this.iValue = new Double(pValue);
			} catch (NumberFormatException e) {
				return false;
			}
			this.iType = CIMDataType.REAL64_T;
			return true;
		}

		private boolean setBoolean(String pValue) {
			this.iValue = Boolean.valueOf(pValue);
			this.iType = CIMDataType.BOOLEAN_T;
			return true;
		}

		private boolean setDTAbsolute(String pValue) {
			try {
				this.iValue = new CIMDateTimeAbsolute(pValue);
			} catch (IllegalArgumentException e) {
				return false;
			}
			this.iType = CIMDataType.DATETIME_T;
			return true;
		}

		private boolean setDTInterval(String pValue) {
			try {
				this.iValue = new CIMDateTimeInterval(pValue);
			} catch (IllegalArgumentException e) {
				return false;
			}
			this.iType = CIMDataType.DATETIME_T;
			return true;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// Object Definition Elements
	// ////////////////////////////////////////////////////////////////////////////////////////

	private static String attribute(Element pElement, String pName) {
		String attrib = pElement.getAttribute(pName);
		if (attrib == null || attrib.length() == 0) return null;
		return attrib;
	}

	private static final String[] nodesCLASS = { "QUALIFIER", "PROPERTY", "PROPERTY.ARRAY",
			"PROPERTY.REFERENCE", "METHOD" };

	/**
	 * parseCLASS
	 * 
	 * @param pClassE
	 * @return CIMClass
	 * @throws CIMXMLParseException
	 */
	public static CIMClass parseCLASS(Element pClassE) throws CIMXMLParseException {
		return parseCLASS(pClassE, null);
	}

	/**
	 * parseCLASS
	 * 
	 * @param pClassE
	 * @param pObjectPath
	 * @return CIMClass
	 * @throws CIMXMLParseException
	 */
	public static CIMClass parseCLASS(Element pClassE, CIMObjectPath pObjectPath)
			throws CIMXMLParseException {
		// <!ELEMENT CLASS (QUALIFIER*,
		// (PROPERTY|PROPERTY.ARRAY|PROPERTY.REFERENCE)*,METHOD*)>
		// <!ATTLIST CLASS %NAME;%SUPERCLASS;>

		Attr class_nameA = (Attr) searchAttribute(pClassE, "NAME");
		if (class_nameA == null) throw new CIMXMLParseException(
				"CLASS element missing NAME attribute!");
		String name = class_nameA.getNodeValue();

		// Attr superclass_nameA = (Attr)searchAttribute(classE, "SUPERCLASS");
		// String supername = superclass_nameA.getNodeValue();

		String superClass = attribute(pClassE, "SUPERCLASS");

		// QUALIFIER
		CIMQualifier<?>[] qualis = parseQUALIFIERS(pClassE);

		// PROPERTY
		CIMClassProperty<?>[] props = parseCLASSPROPERTIES(pClassE);

		// METHOD
		Element[] methodElementA = searchNodes(pClassE, "METHOD", 0, Integer.MAX_VALUE, true);
		CIMMethod<?>[] methods;
		if (methodElementA != null) {
			methods = new CIMMethod[methodElementA.length];
			for (int i = 0; i < methodElementA.length; i++) {
				Element methodE = methodElementA[i];
				methods[i] = parseMETHOD(methodE);
			}
		} else {
			methods = null;
		}

		/*
		 * CIMClass( String name, String superclass, CIMQualifier[] qualifiers,
		 * CIMClassProperty[] props, CIMMethod[] methods )
		 * 
		 * return new CIMClass( name, superClass, qualis, props, methods );
		 * 
		 * This constructor can provide localPath info. CIMClass( CIMObjectPath
		 * path, String superclass, CIMQualifier[] qualifiers,
		 * CIMClassProperty[] props, CIMMethod[] pMethods, boolean
		 * pIsAssociation, boolean pIsKeyed )
		 */
		checkOtherNodes(pClassE, nodesCLASS);

		return new CIMClass(
				pObjectPath == null ? cLocalPathBuilder.build(name, null) : pObjectPath,
				superClass, qualis, props, methods, hasAssocQuali(qualis), hasKeyProp(props));
	}

	private static boolean hasAssocQuali(CIMQualifier<?>[] pQualis) {
		if (pQualis == null) return false;
		for (int i = 0; i < pQualis.length; i++) {
			CIMQualifier<?> quali = pQualis[i];
			if ("ASSOCIATION".equalsIgnoreCase(quali.getName())
					&& Boolean.TRUE.equals(quali.getValue())) return true;
		}
		return false;
	}

	private static boolean hasKeyProp(CIMProperty<?>[] pProps) {
		if (pProps == null) return false;
		for (int i = 0; i < pProps.length; i++)
			if (pProps[i].isKey()) return true;
		return false;
	}

	private static CIMParameter<?>[] parseParameters(Element pMethodE) throws CIMXMLParseException {
		// PARAMETER
		Vector<CIMParameter<Object>> paramV = new Vector<CIMParameter<Object>>();
		Element[] paramElementA = searchNodes(pMethodE, "PARAMETER");
		if (paramElementA != null) {
			for (int i = 0; i < paramElementA.length; i++) {
				Element parameterE = paramElementA[i];
				CIMParameter<Object> p = parsePARAMETER(parameterE);
				paramV.add(p);
			}
		}

		Element[] paramRefElementA = searchNodes(pMethodE, "PARAMETER.REFERENCE");
		if (paramRefElementA != null) {
			for (int i = 0; i < paramRefElementA.length; i++) {
				Element parameterE = paramRefElementA[i];
				CIMParameter<Object> p = parsePARAMETERREFERENCE(parameterE);
				paramV.add(p);
			}
		}

		Element[] paramArrayElementA = searchNodes(pMethodE, "PARAMETER.ARRAY");
		if (paramArrayElementA != null) {
			for (int i = 0; i < paramArrayElementA.length; i++) {
				Element parameterE = paramArrayElementA[i];
				CIMParameter<Object> p = parsePARAMETERARRAY(parameterE);
				paramV.add(p);
			}
		}

		Element[] paramRefArrayElementA = searchNodes(pMethodE, "PARAMETER.REFARRAY");
		if (paramRefArrayElementA != null) {
			for (int i = 0; i < paramRefArrayElementA.length; i++) {
				Element parameterE = paramRefArrayElementA[i];
				CIMParameter<Object> p = parsePARAMETERREFARRAY(parameterE);
				paramV.add(p);
			}
		}

		return paramV.toArray(new CIMParameter[0]);
	}

	private static final String nodesMETHOD[] = { "QUALIFIER", "PARAMETER", "PARAMETER.REFERENCE",
			"PARAMETER.ARRAY", "PARAMETER.REFARRAY" };

	/**
	 * parseMETHOD
	 * 
	 * @param pMethodE
	 * @return CIMMethod
	 * @throws CIMXMLParseException
	 */
	public static CIMMethod<Object> parseMETHOD(Element pMethodE) throws CIMXMLParseException {
		// <!ELEMENT METHOD
		// (QUALIFIER*,(PARAMETER|PARAMETER.REFERENCE|PARAMETER.ARRAY|PARAMETER.REFARRAY)*)>
		// <!ATTLIST METHOD
		// %CIMName;
		// %CIMType; #IMPLIED
		// %ClassOrigin;
		// %Propagated;>

		String name = attribute(pMethodE, "NAME");
		if (name == null) throw new CIMXMLParseException("METHOD element missing NAME attribute!");
		EmbObjHandler embObjHandler = new EmbObjHandler(pMethodE);
		CIMDataType type = embObjHandler.getType();
		if (type != null && type.isArray()) throw new CIMXMLParseException(
				"Method's type cannot be Array!");
		String classOrigin = attribute(pMethodE, "CLASSORIGIN");
		String propagatedStr = pMethodE.getAttribute("PROPAGATED");
		boolean propagated = "true".equalsIgnoreCase(propagatedStr);

		checkOtherNodes(pMethodE, nodesMETHOD);

		// PARAMETERS
		CIMParameter<?>[] params = parseParameters(pMethodE);
		// CIMMethod(String name, CIMDataType type, CIMQualifier[] qualifiers,
		// CIMParameter[] parameters, boolean propagated, String originClass)
		return new CIMMethod<Object>(name, type, embObjHandler.getQualifiers(), params, propagated,
				classOrigin);
	}

	private static final String nodesPARAMETER[] = { "QUALIFIER" };

	/**
	 * parsePARAMETER
	 * 
	 * @param pParamE
	 * @return CIMParameter
	 * @throws CIMXMLParseException
	 */
	public static CIMParameter<Object> parsePARAMETER(Element pParamE) throws CIMXMLParseException {
		String name = attribute(pParamE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"PARAMETER element missing NAME attribute!");
		if (attribute(pParamE, "TYPE") == null) throw new CIMXMLParseException(
				"PARAMETER element missing TYPE attribute!");
		checkOtherNodes(pParamE, nodesPARAMETER);
		EmbObjHandler iEmbObjHandler = new EmbObjHandler(pParamE);
		// CIMParameter(String name, CIMDataType type, CIMQualifier[]
		// qualifiers)
		return new CIMParameter<Object>(name, iEmbObjHandler.getType(), iEmbObjHandler
				.getQualifiers());
	}

	private static final String nodesPARAMETERREFERENCE[] = { "QUALIFIER" };

	/**
	 * parsePARAMETERREFERENCE
	 * 
	 * @param pParamE
	 * @return CIMParameter
	 * @throws CIMXMLParseException
	 */
	public static CIMParameter<Object> parsePARAMETERREFERENCE(Element pParamE)
			throws CIMXMLParseException {
		String name = attribute(pParamE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"PARAMETER.REFERENCE element missing NAME attribute!");
		String referenceClass = attribute(pParamE, "REFERENCECLASS");
		CIMDataType type = new CIMDataType(referenceClass != null ? referenceClass : "");
		checkOtherNodes(pParamE, nodesPARAMETERREFERENCE);
		// QUALIFIER
		CIMQualifier<?>[] qualis = parseQUALIFIERS(pParamE);
		return new CIMParameter<Object>(name, type, qualis);
	}

	private static final String nodesPARAMETERARRAY[] = { "QUALIFIER" };

	/**
	 * parsePARAMETERARRAY
	 * 
	 * @param pParamE
	 * @return CIMParameter
	 * @throws CIMXMLParseException
	 */
	public static CIMParameter<Object> parsePARAMETERARRAY(Element pParamE)
			throws CIMXMLParseException {
		String name = attribute(pParamE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"PARAMETER.ARRAY element missing NAME attribute!");
		if (attribute(pParamE, "TYPE") == null) throw new CIMXMLParseException(
				"PARAMETER.ARRAY element missing TYPE attribute!");
		String arraySizeStr = pParamE.getAttribute("ARRAYSIZE");
		try {
			if (arraySizeStr.length() > 0) Integer.parseInt(arraySizeStr);
		} catch (NumberFormatException e) {
			throw new CIMXMLParseException(arraySizeStr
					+ " is not a valid ARRAYSIZE attribute for PARAMETER.ARRAY!");
		}
		checkOtherNodes(pParamE, nodesPARAMETERARRAY);
		EmbObjHandler iEmbObjHandler = new EmbObjHandler(pParamE);
		return new CIMParameter<Object>(name, iEmbObjHandler.getArrayType(), iEmbObjHandler
				.getQualifiers());
	}

	private static final String nodesPARAMETERREFARRAY[] = { "QUALIFIER" };

	/**
	 * parsePARAMETERREFARRAY
	 * 
	 * @param pParamE
	 * @return CIMParameter
	 * @throws CIMXMLParseException
	 */
	public static CIMParameter<Object> parsePARAMETERREFARRAY(Element pParamE)
			throws CIMXMLParseException {
		String name = attribute(pParamE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"PARAMETER.REFARRAY element missing NAME attribute!");

		String referenceClass = attribute(pParamE, "REFERENCECLASS");

		int arraySize = 0; // unlimited
		String arraySizeStr = pParamE.getAttribute("ARRAYSIZE");
		try {
			if (arraySizeStr.length() > 0) arraySize = Integer.parseInt(arraySizeStr);
		} catch (NumberFormatException e) {
			throw new CIMXMLParseException(arraySizeStr
					+ " is not a valid ARRAYSIZE attribute for PARAMETER.REFARRAY!");
		}

		CIMDataType type = new CIMDataType((referenceClass != null) ? referenceClass : "",
				arraySize);

		// QUALIFIER
		checkOtherNodes(pParamE, nodesPARAMETERREFARRAY);
		CIMQualifier<?>[] qualis = parseQUALIFIERS(pParamE);
		return new CIMParameter<Object>(name, type, qualis);
	}

	/**
	 * parseINSTANCE
	 * 
	 * @param pInstanceE
	 * @return CIMInstance
	 * @throws CIMXMLParseException
	 */
	public static CIMInstance parseINSTANCE(Element pInstanceE) throws CIMXMLParseException {
		return parseINSTANCE(pInstanceE, null);
	}

	private static final String[] nodesINSTANCE = { "QUALIFIER", "PROPERTY", "PROPERTY.ARRAY",
			"PROPERTY.REFERENCE" };

	/**
	 * parseINSTANCE
	 * 
	 * @param pInstanceE
	 * @param pObjPath
	 * @return CIMInstance
	 * @throws CIMXMLParseException
	 */
	public static CIMInstance parseINSTANCE(Element pInstanceE, CIMObjectPath pObjPath)
			throws CIMXMLParseException {
		// <!ELEMENT INSTANCE (QUALIFIER*,
		// (PROPERTY|PROPERTY.ARRAY|PROPERTY.REFERENCE)*)>
		// <!ATTLIST INSTANCE %CLASSNAME;>

		// BB mod CIMInstance inst = new CIMInstance();
		String className = attribute(pInstanceE, "CLASSNAME");
		if (className == null) throw new CIMXMLParseException(
				"INSTANCE element missing CLASSNAME attribute!");
		// QUALIFIER
		// FIXME: in JSR48 CIMInstance doesn't have qualifiers
		// CIMQualifier[] qualis = parseQUALIFIERS(pInstanceE);
		// PROPERTY
		CIMProperty<?>[] props = parsePROPERTIES(pInstanceE);

		checkOtherNodes(pInstanceE, nodesINSTANCE);

		if (pObjPath == null) return new CIMInstance(cLocalPathBuilder.build(className, null),
				props);
		if (WBEMConfiguration.getGlobalConfiguration().synchronizeNumericKeyDataTypes()) return CIMHelper
				.CIMInstanceWithSynchonizedNumericKeyDataTypes(pObjPath, props);
		return new CIMInstance(pObjPath, props);
	}

	/**
	 * ebak: Access to enclosing method parseQUALIFIERS(Element) from the type
	 * CIMXMLParserImpl is emulated by a synthetic accessor method. Increasing
	 * its visibility will improve your performance
	 * 
	 * @param pElement
	 * @return CIMQualifier[]
	 * @throws CIMXMLParseException
	 */
	public static CIMQualifier<?>[] parseQUALIFIERS(Element pElement) throws CIMXMLParseException {
		Element[] qualiElementA = searchNodes(pElement, "QUALIFIER");
		CIMQualifier<?>[] qualis;
		if (qualiElementA != null) {
			qualis = new CIMQualifier[qualiElementA.length];
			for (int i = 0; i < qualiElementA.length; i++) {
				Element qualifierE = qualiElementA[i];
				qualis[i] = parseQUALIFIER(qualifierE);
			}
		} else qualis = null;
		return qualis;
	}

	/**
	 * <pre>
	 *  ENTITY % QualifierFlavor &quot;
	 *  OVERRIDABLE    (true|false)  'true'
	 *  TOSUBCLASS     (true|false)  'true'
	 *  TOINSTANCE     (true|false)  'false'
	 *  TRANSLATABLE   (true|false)  'false'&quot;
	 * </pre>
	 * 
	 * @param pElement
	 * @return int - CIMFlavor bit mixture
	 */
	private static int parseFLAVORS(Element pElement) {
		int flavors = 0;
		if (!getBoolAttribute(pElement, "OVERRIDABLE", true)) flavors |= CIMFlavor.DISABLEOVERRIDE;
		if (!getBoolAttribute(pElement, "TOSUBCLASS", true)) flavors |= CIMFlavor.RESTRICTED;
		if (getBoolAttribute(pElement, "TRANSLATABLE", false)) flavors |= CIMFlavor.TRANSLATE;
		return flavors;
	}

	private static TypedValue parseSingleValue(Element pElement) throws CIMXMLParseException {
		return parseSingleValue(pElement, VALUE | VALUEA);
	}

	private static final int VALUE = 1, VALUEA = 2, VALUEREF = 4, VALUEREFA = 8;

	private static TypedValue parseSingleValue(Element pElement, int pMask)
			throws CIMXMLParseException {
		boolean foundSingleValue = false;
		String typeStr = attribute(pElement, "TYPE");
		// ebak: there was an ESS fix in the base implementation
		if (typeStr == null) typeStr = attribute(pElement, "PARAMTYPE");
		CIMDataType type = null;
		Object value = null;
		if ((pMask & VALUE) > 0) {
			Element valueE = (Element) searchFirstNode(pElement, "VALUE");
			if (valueE != null) {
				TypedValue tVal = parseVALUE(valueE);
				String valueStr = (String) tVal.getValue();
				type = typeStr == null ? tVal.getType() : parseScalarTypeStr(typeStr);
				value = createJavaObject(type == null ? null : type.toString(), valueStr);
				foundSingleValue = true;
			}
		}
		if (!foundSingleValue && (pMask & VALUEREF) > 0) {
			// VALUE.REFERENCE
			Element valuereferenceE = (Element) searchFirstNode(pElement, "VALUE.REFERENCE");
			if (valuereferenceE != null) {
				CIMObjectPath op = parseVALUEREFERENCE(valuereferenceE);
				value = op;
				type = new CIMDataType(op.getObjectName());
				foundSingleValue = true;
			}
		}
		if (!foundSingleValue && (pMask & VALUEA) > 0) {
			// VALUE.ARRAY
			Element valuearrayE = (Element) searchFirstNode(pElement, "VALUE.ARRAY");
			if (valuearrayE != null) {
				TypedValue tValA = parseVALUEARRAY(valuearrayE);
				type = typeStr == null ? tValA.getType() : parseArrayTypeStr(typeStr);
				String[] valStrA = (String[]) tValA.getValue();
				if (valStrA != null) {
					Object[] values = new Object[valStrA.length];
					for (int i = 0; i < valStrA.length; i++) {
						values[i] = createJavaObject(type == null ? null : type.toString(),
								valStrA[i]);
					}
					value = values;
				}
				foundSingleValue = true;
			}
		}
		if (!foundSingleValue && (pMask & VALUEREFA) > 0) {
			// VALUE.REFARRAY
			Element valueRefArrayE = (Element) searchFirstNode(pElement, "VALUE.REFARRAY");
			if (valueRefArrayE != null) {
				type = new CIMDataType("", 0);
				CIMObjectPath[] opA = parseVALUEREFARRAY(valueRefArrayE);
				value = opA;
				foundSingleValue = true;
			}
		}
		if (!foundSingleValue) {
			if (value instanceof Object[]) type = CIMDataType.STRING_ARRAY_T;
			else if (value != null) type = CIMDataType.STRING_T;
			else type = CIMDataType.STRING_T; // /throw new
			// CIMXMLParseException("null
			// type with null value!");
		}
		return new TypedValue(type, value);
	}

	private static final String nodesQUALIFIER[] = { "VALUE", "VALUE.ARRAY" };

	/**
	 * parseQUALIFIER
	 * 
	 * @param pQualifierE
	 * @return CIMQualifier
	 * @throws CIMXMLParseException
	 */
	public static CIMQualifier<?> parseQUALIFIER(Element pQualifierE) throws CIMXMLParseException {
		// <!ELEMENT QUALIFIER (VALUE|VALUE.ARRAY)>
		// <!ATTLIST QUALIFIER %NAME;%TYPE;%PROPAGATED;%QUALIFIERFLAVOR;>

		String name = attribute(pQualifierE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"QUALIFIER element missing NAME attribute!");
		String typeStr = attribute(pQualifierE, "TYPE");
		if (typeStr == null && !hasTypeAttrsInNodes(pQualifierE)) throw new CIMXMLParseException(
				"QUALIFIER element missing TYPE attribute!");
		boolean propagated = MOF.TRUE.equalsIgnoreCase(pQualifierE.getAttribute("PROPAGATED"));
		// FLAVORS
		int flavors = parseFLAVORS(pQualifierE);
		// VALUE
		if (searchNodes(pQualifierE, "VALUE", 0, 1, false) != null
				|| searchNodes(pQualifierE, "VALUE.ARRAY", 0, 1, false) != null) {
			TypedValue typedValue = parseSingleValue(pQualifierE);
			if (typedValue.getType() == null) throw new CIMXMLParseException(
					"Qualifier's type is null!");
			// CIMQualifier(String pName, CIMDataType pType, Object pValue, int
			// pFlavor)
			return new CIMQualifier<Object>(name, typedValue.getType(), typedValue.getValue(),
					flavors, propagated);
		}
		checkOtherNodes(pQualifierE, nodesQUALIFIER);

		CIMDataType type = parseScalarTypeStr(typeStr);
		return new CIMQualifier<Object>(name, type != null ? type : CIMDataType.STRING_T, null,
				flavors, propagated);
	}

	/**
	 * parseQUALIFIERDECLARATION
	 * 
	 * @param pQualifierTypeE
	 * @return CIMQualifierType
	 * @throws CIMXMLParseException
	 */
	public static CIMQualifierType<Object> parseQUALIFIERDECLARATION(Element pQualifierTypeE)
			throws CIMXMLParseException {
		// <!ELEMENT QUALIFIER.DECLARATION (SCOPE?,(VALUE|VALUE.ARRAY)?)>
		// <!ATTLIST QUALIFIER.DECLARATION
		// %CIMName;
		// %CIMType; #REQUIRED
		// ISARRAY (true|false) #IMPLIED
		// %ArraySize;
		// %QualifierFlavor;>

		String name = attribute(pQualifierTypeE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"QUALIFIER.DECLARATION element missing NAME attribute!");
		String type = attribute(pQualifierTypeE, "TYPE");
		if (type == null && !hasTypeAttrsInNodes(pQualifierTypeE)) throw new CIMXMLParseException(
				"QUALIFIER.DECLARATION element missing TYPE attribute!");

		// SCOPES
		// ebak: there should be only 1 scope node
		Element scopeElementA[] = searchNodes(pQualifierTypeE, "SCOPE", 0, 1, true);
		int scopes = scopeElementA == null ? 0 : parseSCOPES(scopeElementA[0]);
		// QUALIFIERS
		// CIMQualifier[] qualis = parseQUALIFIERS(pQualifierTypeE);

		CIMDataType qdType;
		Object qdValue;

		String nodes[] = null;

		if (searchNodes(pQualifierTypeE, "VALUE", 0, 1, true) != null) {
			nodes = new String[] { "SCOPE", "VALUE" };
		} else if (searchNodes(pQualifierTypeE, "VALUE.ARRAY", 0, 1, true) != null) {
			nodes = new String[] { "SCOPE", "VALUE.ARRAY" };
		}

		// VALUE
		if (nodes != null) {
			TypedValue typedValue = parseSingleValue(pQualifierTypeE);
			qdType = typedValue.getType();
			qdValue = typedValue.getValue();
			boolean isArray = hasTrueAttribute(pQualifierTypeE, "ISARRAY");
			String arraySizeStr = attribute(pQualifierTypeE, "ARRAYSIZE");
			try {
				int arraySize = (arraySizeStr == null ? (isArray ? 0 : -1) : Integer
						.parseInt(arraySizeStr));
				if (isArray || arraySize >= 0) {
					qdType = (arraySize > 0) ? new CIMDataType(qdType.getType(), arraySize)
							: CIMHelper.UnboundedArrayDataType(qdType.getType());
				}
			} catch (NumberFormatException e) {
				throw new CIMXMLParseException(arraySizeStr
						+ " is not a valid ARRAYSIZE attribute for QUALIFIER.DECLARATION!");
			}
		} else {
			nodes = new String[] { "SCOPE" };
			qdType = parseScalarTypeStr(type);
			if (qdType == null) qdType = CIMDataType.STRING_T;
			qdValue = null;
		}
		checkOtherNodes(pQualifierTypeE, nodes);

		// FIXME: ebak: what about the flavors?
		return new CIMQualifierType<Object>(new CIMObjectPath(null, null, null, null, name, null),
				qdType, qdValue, scopes, 0);
	}

	private static boolean hasTrueAttribute(Element pElement, String pName) {
		String valueStr = pElement.getAttribute(pName);
		return MOF.TRUE.equalsIgnoreCase(valueStr);
	}

	private static boolean getBoolAttribute(Element pElement, String pName, boolean pDefVal) {
		String val = pElement.getAttribute(pName);
		if (MOF.TRUE.equalsIgnoreCase(val)) return true;
		if (MOF.FALSE.equalsIgnoreCase(val)) return false;
		return pDefVal;
	}

	/**
	 * parseSCOPES
	 * 
	 * @param pScopeE
	 * @return int
	 */
	public static int parseSCOPES(Element pScopeE) {
		int scopes = 0;
		if (hasTrueAttribute(pScopeE, "CLASS")) scopes |= CIMScope.CLASS;
		if (hasTrueAttribute(pScopeE, "ASSOCIATION")) scopes |= CIMScope.ASSOCIATION;
		if (hasTrueAttribute(pScopeE, "REFERENCE")) scopes |= CIMScope.REFERENCE;
		if (hasTrueAttribute(pScopeE, "PROPERTY")) scopes |= CIMScope.PROPERTY;
		if (hasTrueAttribute(pScopeE, "METHOD")) scopes |= CIMScope.METHOD;
		if (hasTrueAttribute(pScopeE, "PARAMETER")) scopes |= CIMScope.PARAMETER;
		if (hasTrueAttribute(pScopeE, "INDICATION")) scopes |= CIMScope.INDICATION;
		return scopes;
	}

	private static Vector<CIMClassProperty<?>> parseClassPropsToVec(Element pElement)
			throws CIMXMLParseException {
		Element[] propElementA = searchNodes(pElement, "PROPERTY");
		Vector<CIMClassProperty<?>> propVec = new Vector<CIMClassProperty<?>>();
		if (propElementA != null) {
			for (int i = 0; i < propElementA.length; i++) {
				Element propertyE = propElementA[i];
				propVec.add(parseCLASSPROPERTY(propertyE));

			}
		}

		// PROPERTY.ARRAY
		Element[] propArrayElementA = searchNodes(pElement, "PROPERTY.ARRAY");
		if (propArrayElementA != null) {
			for (int i = 0; i < propArrayElementA.length; i++) {
				Element propertyarrayE = propArrayElementA[i];
				propVec.add(parseCLASSPROPERTYARRAY(propertyarrayE));
			}
		}

		// PROPERTY.REFERENCE
		Element[] propRefElementA = searchNodes(pElement, "PROPERTY.REFERENCE");
		if (propRefElementA != null) {
			for (int i = 0; i < propRefElementA.length; i++) {
				Element propertyreferenceE = propRefElementA[i];
				propVec.add(parseCLASSPROPERTYREFERENCE(propertyreferenceE));
			}
		}
		return propVec;
	}

	/**
	 * parsePROPERTIES
	 * 
	 * @param pElement
	 * @return CIMProperty[]
	 * @throws CIMXMLParseException
	 */
	public static CIMProperty<?>[] parsePROPERTIES(Element pElement) throws CIMXMLParseException {
		Vector<CIMClassProperty<?>> classPropVec = parseClassPropsToVec(pElement);
		// The following does not convert Vector<CIMClassProperty> to
		// CIMProperty[], it is still CIMClassProperty[]!!! You can treat it
		// like a CIMProperty (property.getValue(), etc.) but (property
		// instanceof CIMProperty) will return false!
		// return classPropVec.toArray(new CIMProperty[0]);

		int arraySize = classPropVec.size();
		CIMProperty<?>[] retA = new CIMProperty[arraySize];
		for (int i = 0; i < arraySize; i++) {
			CIMClassProperty<?> prop = classPropVec.get(i);
			retA[i] = new CIMProperty<Object>(prop.getName(), prop.getDataType(), prop.getValue(),
					prop.isKey(), prop.isPropagated(), prop.getOriginClass());
		}
		return retA;
	}

	/**
	 * parseCLASSPROPERTIES
	 * 
	 * @param pElement
	 * @return CIMClassProperty[]
	 * @throws CIMXMLParseException
	 */
	public static CIMClassProperty<?>[] parseCLASSPROPERTIES(Element pElement)
			throws CIMXMLParseException {
		Vector<CIMClassProperty<?>> classPropVec = parseClassPropsToVec(pElement);
		return classPropVec.toArray(new CIMClassProperty[0]);
	}

	/**
	 * parsePROPERTY
	 * 
	 * @param pPropertyE
	 * @return CIMProperty
	 * @throws CIMXMLParseException
	 */
	public static CIMProperty<?> parsePROPERTY(Element pPropertyE) throws CIMXMLParseException {
		return parseCLASSPROPERTY(pPropertyE);
	}

	private static class EmbObjHandler {

		private CIMQualifier<?>[] iQualiA;

		private boolean iHasEmbObjAttr, iHasEmbInstAttr, iHasEmbObjQuali, iHasEmbInstQuali, iKeyed;

		private CIMDataType iRawType, iType;

		private Object iRawValue, iValue;

		private Element iElement;

		private boolean iStrictParsing = WBEMConfiguration.getGlobalConfiguration()
				.strictEmbObjTypes();

		/**
		 * Ctor.
		 * 
		 * @param pElement
		 * @throws CIMXMLParseException
		 */
		public EmbObjHandler(Element pElement) throws CIMXMLParseException {
			this.iElement = pElement;
			handleQualis();
			handleAttribs();
			Element valueE = (Element) searchFirstNode(pElement, "VALUE");
			if (valueE != null) {
				TypedValue tv = parseVALUE(valueE);
				if (this.iRawType == null) this.iRawType = tv.getType();
				if (this.iRawType == null) this.iRawType = CIMDataType.STRING_T;
				this.iRawValue = tv.getValue();
			} else {
				valueE = (Element) searchFirstNode(pElement, "VALUE.ARRAY");
				if (valueE != null) {
					TypedValue tv = parseVALUEARRAY(valueE);
					if (this.iRawType == null) this.iRawType = tv.getType();
					if (this.iRawType == null) this.iRawType = CIMDataType.STRING_ARRAY_T;
					this.iRawValue = tv.getValue();
				}
			}
		}

		private void handleQualis() throws CIMXMLParseException {
			this.iQualiA = parseQUALIFIERS(this.iElement);
			if (this.iQualiA != null) {
				for (int idx = 0; idx < this.iQualiA.length; idx++) {
					String qualiName = this.iQualiA[idx].getName();
					if ("EmbeddedObject".equalsIgnoreCase(qualiName)) {
						this.iHasEmbObjQuali = true;
					} else if ("EMBEDDEDINSTANCE".equalsIgnoreCase(qualiName)) {
						this.iHasEmbInstQuali = true;
					} else if ("KEY".equalsIgnoreCase(qualiName)) {
						this.iKeyed = true;
					}
					if ((this.iHasEmbObjQuali || this.iHasEmbInstQuali) && this.iKeyed) return;
				}
			}
		}

		private void handleAttribs() throws CIMXMLParseException {
			String typeStr = this.iElement.getAttribute("TYPE");
			if (typeStr == null || typeStr.length() == 0) {
				typeStr = this.iElement.getAttribute("PARAMTYPE");
				if (typeStr == null || typeStr.length() == 0) {
					/*
					 * throw new CIMXMLParseException( iElement.getNodeName()+"
					 * must contain a TYPE or PARAMTYPE attribute!" );
					 */
					/*
					 * ebak: Workaround for SVC's TYPE attribute in VALUE and
					 * VALUE.ARRAY element.
					 */
					typeStr = null;
				}
			}
			// ebak: iRawType can be filled from contained VALUE or VALUE.ARRAY
			// element (SVC)
			if (typeStr != null) this.iRawType = parseScalarTypeStr(typeStr);

			// DSPs call for EmbeddedObject AND XML is case sensitive, BUT there
			// are probably CIMOMs out there that still use EMBEDDEDOBJECT
			String embObjAttrStr = this.iElement.getAttribute("EmbeddedObject");
			if (embObjAttrStr == null || embObjAttrStr.length() == 0) {
				embObjAttrStr = this.iElement.getAttribute("EMBEDDEDOBJECT");
			}
			if (embObjAttrStr == null || embObjAttrStr.length() == 0) {
				this.iHasEmbObjAttr = this.iHasEmbInstAttr = false;
			} else if ("object".equalsIgnoreCase(embObjAttrStr)) {
				this.iHasEmbObjAttr = true;
				this.iHasEmbInstAttr = false;
			} else if ("instance".equalsIgnoreCase(embObjAttrStr)) {
				this.iHasEmbObjAttr = false;
				this.iHasEmbInstAttr = true;
			} else throw new CIMXMLParseException("EmbeddedObject attribute cannot contain \""
					+ embObjAttrStr + "\" value!");
		}

		/**
		 * getQualifiers
		 * 
		 * @return CIMQualifier[]
		 * @throws CIMXMLParseException
		 */
		public CIMQualifier<?>[] getQualifiers() throws CIMXMLParseException {
			transform();
			CIMQualifiedElementInterfaceImpl qualiImpl = new CIMQualifiedElementInterfaceImpl(
					this.iQualiA, isKeyed(), this.iType == null
							|| this.iType.getType() == CIMDataType.STRING);
			return qualiImpl.getQualifiers();
		}

		/**
		 * isKeyed
		 * 
		 * @return boolean
		 */
		public boolean isKeyed() {
			return this.iKeyed;
		}

		/**
		 * isEmbeddedObject
		 * 
		 * @return boolean
		 */
		private boolean isEmbeddedObject() {
			return this.iHasEmbObjAttr || this.iHasEmbInstAttr || this.iHasEmbObjQuali
					|| this.iHasEmbInstQuali;
		}

		/**
		 * isEmbeddedClass
		 * 
		 * @return boolean
		 */
		private boolean isEmbeddedClass() {
			return this.iHasEmbObjAttr || this.iHasEmbObjQuali;
		}

		/**
		 * isEmbeddedInstance
		 * 
		 * @return boolean
		 */
		private boolean isEmbeddedInstance() {
			return this.iHasEmbInstAttr || this.iHasEmbInstQuali;
		}

		/**
		 * getType
		 * 
		 * @return CIMDataType
		 * @throws CIMXMLParseException
		 */
		public CIMDataType getType() throws CIMXMLParseException {
			transform();
			return this.iType;
		}

		/**
		 * getArrayType
		 * 
		 * @return CIMDataType
		 * @throws CIMXMLParseException
		 */
		public CIMDataType getArrayType() throws CIMXMLParseException {
			transform();
			return this.iType.isArray() ? this.iType : CIMHelper.UnboundedArrayDataType(this.iType
					.getType());
		}

		/**
		 * getValue
		 * 
		 * @return Object
		 * @throws CIMXMLParseException
		 */
		public Object getValue() throws CIMXMLParseException {
			transform();
			return this.iValue;
		}

		private void transform() throws CIMXMLParseException {
			if (this.iType != null) return;
			if (this.iRawValue == null) {
				if (isEmbeddedObject()) {
					if (this.iRawType != CIMDataType.STRING_T) throw new CIMXMLParseException(
							"Embedded Object CIM-XML element's type must be string. "
									+ this.iRawType + " is invalid!");
					if (this.iStrictParsing) {
						/*
						 * In case of strict parsing Object means CIMClass,
						 * Instance means CIMInstance. Pegasus 2.7.0 seems to
						 * handle it this way.
						 */
						this.iType = isEmbeddedInstance() ? CIMDataType.OBJECT_T
								: CIMDataType.CLASS_T;
					} else {
						/*
						 * for valueless EmbeddedObject="object" the type is
						 * undeterminable since Pegasus's CIMObject can contain
						 * both CIMClass and CIMInstance
						 */
						this.iType = isEmbeddedInstance() ? CIMDataType.OBJECT_T
								: CIMDataType.STRING_T;
					}
				} else {
					this.iType = this.iRawType;
				}
				this.iValue = null;
			} else {
				if (isEmbeddedObject()) {
					transformEmbObj();
				} else {
					transformNormObj();
				}
			}
		}

		private void transformEmbObj() throws CIMXMLParseException {
			if (this.iRawValue instanceof String) {
				this.iValue = parseXmlStr((String) this.iRawValue);
				this.iType = getCIMObjType(this.iValue);
			} else {
				this.iValue = parseXmlStrA((String[]) this.iRawValue);
				this.iType = getCIMObjAType((Object[]) this.iValue);
			}
			if (isEmbeddedInstance() && this.iType.getType() != CIMDataType.OBJECT) throw new CIMXMLParseException(
					this.iElement.getNodeName()
							+ " element is an EmbeddedInstance with non INSTANCE value. "
							+ "It's not valid!");
			if (isEmbeddedClass() && this.iType.getType() != CIMDataType.CLASS
					&& this.iType.getType() != CIMDataType.OBJECT) throw new CIMXMLParseException(
					this.iElement.getNodeName()
							+ " element is an EmbeddedObject with non CLASS/INSTANCE value. It's not valid!");
		}

		private void transformNormObj() throws CIMXMLParseException {
			if (this.iRawValue instanceof String) {
				this.iType = this.iRawType;
				this.iValue = createJavaObject(this.iType == null ? null : this.iType.toString(),
						(String) this.iRawValue);
			} else {
				String[] rawValueA = (String[]) this.iRawValue;
				String typeStr = this.iRawType.toString();
				Object[] objA = new Object[rawValueA.length];
				for (int i = 0; i < objA.length; i++)
					objA[i] = createJavaObject(typeStr, rawValueA[i]);
				this.iType = CIMHelper.UnboundedArrayDataType(this.iRawType.getType());
				this.iValue = objA;
			}
		}

		/**
		 * parseXmlStr
		 * 
		 * @param pXmlStr
		 * @return CIMClass or CIMInstance
		 * @throws CIMXMLParseException
		 */
		public static Object parseXmlStr(String pXmlStr) throws CIMXMLParseException {
			// ebak: workaround for null array elements -> parse empty EmbObj
			// string as null
			if (pXmlStr == null || pXmlStr.length() == 0) return null;
			try {
				CIMClientXML_HelperImpl builder = new CIMClientXML_HelperImpl();
				Document doc = builder.parse(new InputSource(new StringReader(pXmlStr)));
				return parseObject(doc.getDocumentElement());
			} catch (CIMXMLParseException e) {
				throw e;
			} catch (Exception e) {
				throw new CIMXMLParseException(e.getMessage() + "\npXmlStr=" + pXmlStr);
			}
		}

		/**
		 * parseXmlStrA
		 * 
		 * @param pXmlStrA
		 * @return Object[]
		 * @throws CIMXMLParseException
		 */
		public static Object[] parseXmlStrA(String[] pXmlStrA) throws CIMXMLParseException {
			if (pXmlStrA == null || pXmlStrA.length == 0) return null;
			Object[] objA = new Object[pXmlStrA.length];
			for (int i = 0; i < objA.length; i++) {
				String xmlStr = pXmlStrA[i];
				objA[i] = xmlStr == null ? null : parseXmlStr(xmlStr);
			}
			return objA;
		}

		/**
		 * getCIMObjType
		 * 
		 * @param pCIMObj
		 * @return CIMDataType
		 * @throws CIMXMLParseException
		 */
		public static CIMDataType getCIMObjType(Object pCIMObj) throws CIMXMLParseException {
			if (pCIMObj == null) throw new CIMXMLParseException(
					"cannot have null CIM object! (CIMClass or CIMInstance)");
			if (pCIMObj instanceof CIMInstance) return CIMDataType.OBJECT_T;
			if (pCIMObj instanceof CIMClass) return CIMDataType.CLASS_T;
			throw new CIMXMLParseException(pCIMObj.getClass().getName()
					+ " is not a CIM object! (CIMClass or CIMInstance)");
		}

		/**
		 * getCIMObjAType
		 * 
		 * @param pCIMObjA
		 * @return CIMDataType
		 * @throws CIMXMLParseException
		 */
		public static CIMDataType getCIMObjAType(Object[] pCIMObjA) throws CIMXMLParseException {
			if (pCIMObjA == null || pCIMObjA.length == 0) return CIMDataType.STRING_ARRAY_T;
			CIMDataType type = null;
			for (int i = 0; i < pCIMObjA.length; i++) {
				if (pCIMObjA[i] == null) continue;
				CIMDataType currType = getCIMObjType(pCIMObjA[i]);
				if (type == null) {
					type = currType;
				} else if (type != currType) { throw new CIMXMLParseException(
						"Embedded Object arrays with different types are not supported"); }
			}
			if (type == CIMDataType.OBJECT_T) return CIMDataType.OBJECT_ARRAY_T;
			if (type == CIMDataType.CLASS_T) return CIMDataType.CLASS_ARRAY_T;
			return CIMDataType.STRING_ARRAY_T;
		}

	}

	private static final String nodesPROPERTY[] = { "QUALIFIER", "VALUE" };

	/**
	 * parseCLASSPROPERTY
	 * 
	 * @param pPropertyE
	 * @return CIMClassProperty
	 * @throws CIMXMLParseException
	 */
	public static CIMClassProperty<Object> parseCLASSPROPERTY(Element pPropertyE)
			throws CIMXMLParseException {
		// <!ELEMENT PROPERTY (QUALIFIER*,VALUE?)>
		// <!ATTLIST PROPERTY %NAME;%TYPE;%CLASSORIGIN;%PROPAGATED;>

		Attr property_nameA = (Attr) searchAttribute(pPropertyE, "NAME");
		if (property_nameA == null) throw new CIMXMLParseException(
				"PROPERTY element missing NAME attribute!");
		String name = property_nameA.getNodeValue();
		if (attribute(pPropertyE, "TYPE") == null && !hasTypeAttrsInNodes(pPropertyE)) throw new CIMXMLParseException(
				"PROPERTY element missing TYPE attribute!");

		String classOrigin = pPropertyE.getAttribute("CLASSORIGIN");
		if (classOrigin != null && classOrigin.length() == 0) classOrigin = null;

		String propagatedStr = pPropertyE.getAttribute("PROPAGATED");
		boolean propagated = MOF.TRUE.equalsIgnoreCase(propagatedStr);

		// only QUALIFIER(s) and 0/1 VALUE
		searchNodes(pPropertyE, "VALUE", 0, 1, true);
		checkOtherNodes(pPropertyE, nodesPROPERTY);

		// QUALIFIER
		// ebak: EmbeddedObject support
		EmbObjHandler embObjHandler = new EmbObjHandler(pPropertyE);
		/*
		 * CIMClassProperty(String pName, CIMDataType pType, Object pValue,
		 * CIMQualifier[] pQualifiers, boolean pKey, boolean propagated, String
		 * originClass)
		 */
		return new CIMClassProperty<Object>(name, embObjHandler.getType(),
				embObjHandler.getValue(), embObjHandler.getQualifiers(), embObjHandler.isKeyed(),
				propagated, classOrigin);
	}

	/**
	 * parsePROPERTYARRAY
	 * 
	 * @param pPropertyArrayE
	 * @return CIMProperty
	 * @throws CIMXMLParseException
	 */
	public static CIMProperty<Object> parsePROPERTYARRAY(Element pPropertyArrayE)
			throws CIMXMLParseException {
		return parseCLASSPROPERTYARRAY(pPropertyArrayE);
	}

	private static final String nodesPROPERTYARRAY[] = { "QUALIFIER", "VALUE.ARRAY" };

	/**
	 * parseCLASSPROPERTYARRAY
	 * 
	 * @param pPropArrayE
	 * @return CIMClassProperty
	 * @throws CIMXMLParseException
	 */
	public static CIMClassProperty<Object> parseCLASSPROPERTYARRAY(Element pPropArrayE)
			throws CIMXMLParseException {
		// <!ELEMENT PROPERTY.ARRAY (QUALIFIER*,VALUE.ARRAY?)>
		// <!ATTLIST PROPERTY.ARRAY
		// %NAME;%TYPE;%ARRAYSIZE;%CLASSORIGIN;%PROPAGATED;>

		String name = attribute(pPropArrayE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"PROPERTY.ARRAY element missing NAME attribute!");
		if (attribute(pPropArrayE, "TYPE") == null && !hasTypeAttrsInNodes(pPropArrayE)) throw new CIMXMLParseException(
				"PROPERTY.ARRAY element missing TYPE attribute!");

		String valueArraysizeStr = pPropArrayE.getAttribute("ARRAYSIZE");
		try {
			if (valueArraysizeStr.length() > 0) Integer.parseInt(valueArraysizeStr);
		} catch (NumberFormatException e) {
			throw new CIMXMLParseException(valueArraysizeStr
					+ " is not a valid ARRAYSIZE attribute for VALUE.ARRAY!");
		}

		String classOrigin = pPropArrayE.getAttribute("CLASSORIGIN");
		if (classOrigin != null && classOrigin.length() == 0) classOrigin = null;

		String valuePropagatedStr = pPropArrayE.getAttribute("PROPAGATED");
		boolean propagated = (valuePropagatedStr != null && valuePropagatedStr
				.equalsIgnoreCase(MOF.TRUE));

		// only QUALIFIER(s) and 0/1 VALUE.ARRAY
		searchNodes(pPropArrayE, "VALUE.ARRAY", 0, 1, true);
		checkOtherNodes(pPropArrayE, nodesPROPERTYARRAY);

		// QUALIFIER
		// ebak: EmbeddedObject support
		EmbObjHandler embObjHandler = new EmbObjHandler(pPropArrayE);
		return new CIMClassProperty<Object>(name, embObjHandler.getArrayType(), embObjHandler
				.getValue(), embObjHandler.getQualifiers(), embObjHandler.isKeyed(), propagated,
				classOrigin);
	}

	/**
	 * parsePROPERTYREFERENCE
	 * 
	 * @param pPropRefE
	 * @return CIMProperty
	 * @throws CIMXMLParseException
	 */
	public static CIMProperty<Object> parsePROPERTYREFERENCE(Element pPropRefE)
			throws CIMXMLParseException {
		return parseCLASSPROPERTYREFERENCE(pPropRefE);
	}

	private static final String nodesPROPERTYREFERENCE[] = { "QUALIFIER", "VALUE.REFERENCE" };

	/**
	 * parseCLASSPROPERTYREFERENCE
	 * 
	 * @param pPropRefE
	 * @return CIMClassProperty
	 * @throws CIMXMLParseException
	 */
	public static CIMClassProperty<Object> parseCLASSPROPERTYREFERENCE(Element pPropRefE)
			throws CIMXMLParseException {
		// <!ELEMENT PROPERTY.REFERENCE (QUALIFIER*,VALUE.REFERENCE?)>
		// <!ATTLIST PROPERTY.REFERENCE
		// %NAME;%REFERENCECLASS;%CLASSORIGIN;%PROPAGATED;>

		String name = attribute(pPropRefE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"PROPERTY.REFERENCE element missing NAME attribute!");

		String classOrigin = pPropRefE.getAttribute("CLASSORIGIN");
		if (classOrigin != null && classOrigin.length() == 0) classOrigin = null;

		String referenceClass = pPropRefE.getAttribute("REFERENCECLASS");

		String propagatedStr = pPropRefE.getAttribute("PROPAGATED");
		boolean propagated = MOF.TRUE.equalsIgnoreCase(propagatedStr);

		// QUALIFIER
		CIMQualifier<?>[] qualis = parseQUALIFIERS(pPropRefE);

		CIMDataType type = new CIMDataType(referenceClass != null ? referenceClass : "");
		// VALUE.REFERENCE
		Element valueRefA[] = searchNodes(pPropRefE, "VALUE.REFERENCE", 0, 1, true);
		Object value = valueRefA != null ? parseVALUEREFERENCE(valueRefA[0]) : null;

		// return a CIMProperty without a CIMValue
		checkOtherNodes(pPropRefE, nodesPROPERTYREFERENCE);
		CIMQualifiedElementInterfaceImpl qualiImpl = new CIMQualifiedElementInterfaceImpl(qualis);
		return new CIMClassProperty<Object>(name, type, value, qualis, qualiImpl.isKeyed(),
				propagated, classOrigin);
	}

	/**
	 * parseMESSAGE
	 * 
	 * @param pCimVersion
	 * @param pDtdVersion
	 * @param pMessageE
	 * @return CIMMessage
	 * @throws CIMXMLParseException
	 */
	public static CIMMessage parseMESSAGE(String pCimVersion, String pDtdVersion, Element pMessageE)
			throws CIMXMLParseException {
		// <!ELEMENT MESSAGE
		// (SIMPLEREQ|MULTIREQ|SIMPLERSP|MULTIRSP|SIMPLEEXPREQ|MULTIEXPREQ|SIMPLEEXPRSP|MULTIEXPRSP)>
		// <!ATTLIST MESSAGE %ID;%PROTOCOLVERSION;>

		Attr idA = (Attr) searchAttribute(pMessageE, "ID");
		if (idA == null) throw new CIMXMLParseException("MESSAGE element missing ID attribute!");
		String id = idA.getNodeValue();

		Attr protocolA = (Attr) searchAttribute(pMessageE, "PROTOCOLVERSION");
		if (protocolA == null) throw new CIMXMLParseException(
				"MESSAGE element missing PROTOCOLVERSION attribute!");
		// TODO
		if (pCimVersion.equals("2.0") && pDtdVersion.equals("2.0")) {

			// Attr message_idA = (Attr)searchAttribute(messageE, "ID");
			// String messadeID = message_idA.getNodeValue();

			// Attr message_protocolversionA = (Attr)searchAttribute(messageE,
			// "PROTOCOLVERSION");
			// String protocolversion = message_protocolversionA.getNodeValue();

			// SIMPLERSP
			Element simplerspA[] = searchNodes(pMessageE, "SIMPLERSP", 0, 1, false);
			if (simplerspA != null) {
				CIMResponse response = parseSIMPLERSP(simplerspA[0]);
				response.setMethod("SIMPLERSP");
				response.setId(id);
				return response;
			}
			Element multirspA[] = searchNodes(pMessageE, "MULTIRSP", 0, 1, false);
			if (multirspA != null) {
				CIMResponse response = parseMULTIRSP(multirspA[0]);
				response.setMethod("MULTIRSP");
				response.setId(id);
				return response;
			}
			// SIMPLEEXPREQ
			Element simpleexpreqA[] = searchNodes(pMessageE, "SIMPLEEXPREQ", 0, 1, false);
			if (simpleexpreqA != null) {
				CIMRequest request = parseSIMPLEEXPREQ(simpleexpreqA[0]);
				request.setMethod("SIMPLEEXPREQ");
				request.setId(id);
				return request;
			}

			// MULTIEXPREQ
			Element multiexpreqA[] = searchNodes(pMessageE, "MULTIEXPREQ", 0, 1, false);
			if (multiexpreqA != null) {
				CIMRequest request = parseMULTIEXPREQ(multiexpreqA[0]);
				request.setMethod("MULTIEXPREQ");
				request.setId(id);
				return request;
			}
			// SIMPLEEXPRSP
			Element simpleexprspA[] = searchNodes(pMessageE, "SIMPLEEXPRSP", 0, 1, false);
			if (simpleexprspA != null) {
				CIMResponse response = parseSIMPLEEXPRSP(simpleexprspA[0]);
				response.setMethod("SIMPLEEXPRSP");
				response.setId(id);
				return response;
			}
			// MULTIEXPRSP
			Element multiexprspA[] = searchNodes(pMessageE, "MULTIEXPRSP", 0, 1, false);
			if (multiexprspA != null) {
				CIMResponse response = parseMULTIEXPRSP(multiexprspA[0]);
				response.setMethod("MULTIEXPRSP");
				response.setId(id);
				return response;
			}

			// SIMPLEREQ
			Element simplereqA[] = searchNodes(pMessageE, "SIMPLEREQ", 0, 1, false);
			if (simplereqA != null) {
				CIMRequest request = parseSIMPLEREQ(simplereqA[0]);
				request.setMethod("SIMPLEREQ");
				request.setId(id);
				return request;
			}

			// MULTIREQ
			Element multireqA[] = searchNodes(pMessageE, "MULTIREQ", 0, 1, false);
			if (multireqA != null) {
				CIMRequest request = parseMULTIREQ(multireqA[0]);
				request.setMethod("MULTIREQ");
				request.setId(id);
				return request;
			}
			throw new CIMXMLParseException("MESSAGE element missing required child element!");
		}
		// TODO, look for the specific error message in the spec
		throw new CIMXMLParseException("DTD not supported");
	}

	private static final String nodesPARAMVALUE[] = { "VALUE", "VALUE.REFERENCE", "VALUE.ARRAY",
			"VALUE.REFARRAY", "CLASSNAME", "INSTANCENAME", "CLASS", "INSTANCE",
			"VALUE.NAMEDINSTANCE" };

	/**
	 * parsePARAMVALUE
	 * 
	 * @param pParamValueE
	 * @return CIMArgument
	 * @throws CIMXMLParseException
	 */
	public static CIMArgument<Object> parsePARAMVALUE(Element pParamValueE)
			throws CIMXMLParseException {
		// <!ELEMENT PARAMVALUE
		// (VALUE|VALUE.REFERENCE|VALUE.ARRAY|VALUE.REFARRAYCLASSNAME|
		// INSTANCENAME|CLASS|INSTANCE|VALUE.NAMEDINSTANCE)?>)
		// <!ATTLIST PARAMTYPE %NAME;%PARAMTYPE%>
		String name = attribute(pParamValueE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"PARAMVALUE element missing NAME attribute!");
		// FIXME: ebak: The base implementation didn't contain VALUE.REFARRAY
		// handling. Why?
		// ebak: embedded object support, different parsing for VALUE and
		// VALUE.ARRAY sub elements
		if (searchNodes(pParamValueE, "VALUE.REFERENCE", 0, 1, false) != null
				|| searchNodes(pParamValueE, "VALUE.REFARRAY", 0, 1, false) != null) {
			TypedValue typedValue = parseSingleValue(pParamValueE, VALUEREF | VALUEREFA);
			CIMDataType type = typedValue.getType();
			Object value = typedValue.getValue();
			if (type == null) throw new CIMXMLParseException("PARAMVALUE element type is null!");
			return new CIMArgument<Object>(name, type, value);
		}
		if (searchNodes(pParamValueE, "VALUE", 0, 1, false) != null
				|| searchNodes(pParamValueE, "VALUE.ARRAY", 0, 1, false) != null
				|| !hasNodes(pParamValueE)) {
			EmbObjHandler embObjHandler = new EmbObjHandler(pParamValueE);
			return new CIMArgument<Object>(name, embObjHandler.getType(), embObjHandler.getValue());
		}

		// CLASSNAME
		Element classNameA[] = searchNodes(pParamValueE, "CLASSNAME", 0, 1, false);
		if (classNameA != null) {
			CIMObjectPath op = parseCLASSNAME(classNameA[0]);
			CIMDataType type = new CIMDataType(op.getObjectName());
			return new CIMArgument<Object>(name, type, op);
		}

		// INSTANCENAME
		Element instNameA[] = searchNodes(pParamValueE, "INSTANCENAME", 0, 1, false);
		if (instNameA != null) {
			CIMObjectPath op = parseINSTANCENAME(instNameA[0]);
			CIMDataType type = new CIMDataType(op.getObjectName());
			return new CIMArgument<Object>(name, type, op);
		}

		// CLASS
		Element classA[] = searchNodes(pParamValueE, "CLASS", 0, 1, false);
		if (classA != null) {
			CIMClass cl = parseCLASS(classA[0]);
			return new CIMArgument<Object>(name, CIMDataType.CLASS_T, cl);
		}

		// INSTANCE
		Element instanceA[] = searchNodes(pParamValueE, "INSTANCE", 0, 1, false);
		if (instanceA != null) {
			CIMInstance inst = parseINSTANCE(instanceA[0]);
			return new CIMArgument<Object>(name, CIMDataType.OBJECT_T, inst);
		}

		// VALUE.NAMEDINSTANCE
		Element valuenamedisntanceA[] = searchNodes(pParamValueE, "VALUE.NAMEDINSTANCE", 0, 1,
				false);
		if (valuenamedisntanceA != null) {
			CIMInstance inst = parseVALUENAMEDINSTANCE(valuenamedisntanceA[0]);
			return new CIMArgument<Object>(name, CIMDataType.OBJECT_T, inst);
		}
		checkOtherNodes(pParamValueE, nodesPARAMVALUE);
		return new CIMArgument<Object>(name, CIMDataType.STRING_T, null);
	}

	private static final String nodesIPARAMVALUE[] = { "VALUE", "VALUE.ARRAY", "VALUE.REFERENCE",
			"CLASSNAME", "INSTANCENAME", "QUALIFIER.DECLARATION", "CLASS", "INSTANCE",
			"VALUE.NAMEDINSTANCE" };

	/**
	 * parseIPARAMVALUE
	 * 
	 * @param pParamValueE
	 * @return CIMArgument
	 * @throws CIMXMLParseException
	 */
	public static CIMArgument<Object> parseIPARAMVALUE(Element pParamValueE)
			throws CIMXMLParseException {
		// <!ELEMENT IPARAMVALUE
		// (VALUE|VALUE.ARRAY|VALUE.REFERENCE|CLASSNAME|INSTANCENAME|QUALIFIER.DECLARATION|
		// CLASS|INSTANCE|VALUE.NAMEDINSTANCE)?>
		// <!ATTLIST IPARAMVALUE
		// %CIMName;>

		String name = attribute(pParamValueE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"IPARAMVALUE element missing NAME attribute!");
		// this can parse VALUE, VALUE.ARRAY and VALUE.REFERENCE
		if (searchNodes(pParamValueE, "VALUE", 0, 1, false) != null
				|| searchNodes(pParamValueE, "VALUE.ARRAY", 0, 1, false) != null
				|| searchNodes(pParamValueE, "VALUE.REFERENCE", 0, 1, false) != null) {
			TypedValue typedValue = parseSingleValue(pParamValueE, VALUE | VALUEA | VALUEREF);
			if (typedValue.getType() != null) { return new CIMArgument<Object>(name, typedValue
					.getType(), typedValue.getValue()); }
		}
		// we can have different types too which cannot be handled by
		// parseSingleValue()

		// INSTANCENAME
		Element instNameA[] = searchNodes(pParamValueE, "INSTANCENAME", 0, 1, false);
		if (instNameA != null) {
			CIMObjectPath op = parseINSTANCENAME(instNameA[0]);
			CIMDataType type = new CIMDataType(op.getObjectName());
			return new CIMArgument<Object>(name, type, op);
		}

		// CLASSNAME
		Element classNameA[] = searchNodes(pParamValueE, "CLASSNAME", 0, 1, false);
		if (classNameA != null) {
			CIMObjectPath op = parseCLASSNAME(classNameA[0]);
			CIMDataType type = new CIMDataType(op.getObjectName());
			return new CIMArgument<Object>(name, type, op);
		}

		// QUALIFIER.DECLARATION
		Element qualiDeclarationA[] = searchNodes(pParamValueE, "QUALIFIER.DECLARATION", 0, 1,
				false);
		if (qualiDeclarationA != null) {
			CIMQualifierType<Object> qualiType = parseQUALIFIERDECLARATION(qualiDeclarationA[0]);
			// FIXME: ebak: Does it surely have to be mapped to type:
			// REFERENCE????
			return new CIMArgument<Object>(name, new CIMDataType(qualiType.getName()), qualiType);
		}

		// CLASS
		Element classA[] = searchNodes(pParamValueE, "CLASS", 0, 1, false);
		if (classA != null) {
			CIMClass cl = parseCLASS(classA[0]);
			return new CIMArgument<Object>(name, CIMDataType.CLASS_T, cl);
		}

		// INSTANCE
		Element instanceA[] = searchNodes(pParamValueE, "INSTANCE", 0, 1, false);
		if (instanceA != null) {
			CIMInstance inst = parseINSTANCE(instanceA[0]);
			return new CIMArgument<Object>(name, CIMDataType.OBJECT_T, inst);
		}

		// VALUE.NAMEDINSTANCE
		Element valuenamedisntanceA[] = searchNodes(pParamValueE, "VALUE.NAMEDINSTANCE", 0, 1,
				false);
		if (valuenamedisntanceA != null) {
			CIMInstance inst = parseVALUENAMEDINSTANCE(valuenamedisntanceA[0]);
			return new CIMArgument<Object>(name, CIMDataType.OBJECT_T, inst);
		}

		checkOtherNodes(pParamValueE, nodesIPARAMVALUE);
		return new CIMArgument<Object>(name, CIMDataType.STRING_T, null);
	}

	/**
	 * parseSIMPLERSP
	 * 
	 * @param pSimpleRspE
	 * @return CIMResponse
	 * @throws CIMXMLParseException
	 */
	public static CIMResponse parseSIMPLERSP(Element pSimpleRspE) throws CIMXMLParseException {
		// <!ELEMENT SIMPLERSP (METHODRESPONSE|IMETHODRESPONSE)>

		// METHODRESPONSE
		Element methodresponseA[] = searchNodes(pSimpleRspE, "METHODRESPONSE", 0, 1, false);
		if (methodresponseA != null) {

		return parseMETHODRESPONSE(methodresponseA[0]); }

		// IMETHODRESPONSE
		Element imethodresponseA[] = searchNodes(pSimpleRspE, "IMETHODRESPONSE", 0, 1, false);
		if (imethodresponseA != null) { return parseIMETHODRESPONSE(imethodresponseA[0]); }

		throw new CIMXMLParseException("SIMPLERSP element missing required child element!");
	}

	/**
	 * parseMULTIRSP
	 * 
	 * @param pSimpleRspE
	 * @return CIMResponse
	 * @throws CIMXMLParseException
	 */
	public static CIMResponse parseMULTIRSP(Element pSimpleRspE) throws CIMXMLParseException {
		// <!ELEMENT MULTIRSP (SIMPLERSP,SIMPLERSP+)>

		Element[] multiRespElementA = searchNodes(pSimpleRspE, "SIMPLERSP", 2, Integer.MAX_VALUE,
				false);
		if (multiRespElementA != null) {
			CIMResponse multiRsp = new CIMResponse();
			for (int i = 0; i < multiRespElementA.length; i++) {
				Element methodresponseE = multiRespElementA[i];
				CIMResponse rsp = parseSIMPLERSP(methodresponseE);
				rsp.setMethod("SIMPLERSP");
				multiRsp.addResponse(rsp);
			}
			return multiRsp;
		}

		throw new CIMXMLParseException("MULTIRSP element missing SIMPLERSP child element!");
	}

	private static final String nodesSIMPLEREQ[] = { "CORRELATOR", "METHODCALL", "IMETHODCALL" };

	/**
	 * parseSIMPLEREQ
	 * 
	 * @param pSimpleReqE
	 * @return CIMRequest
	 * @throws CIMXMLParseException
	 */
	public static CIMRequest parseSIMPLEREQ(Element pSimpleReqE) throws CIMXMLParseException {
		// <!ELEMENT SIMPLEREQ (CORRELATOR*, (METHODCALL | IMETHODCALL))>
		CIMRequest request = null;

		// METHODCALL
		Element methodcallA[] = searchNodes(pSimpleReqE, "METHODCALL", 0, 1, true);
		if (methodcallA != null) {
			request = parseMETHODCALL(methodcallA[0]);
		}

		// IMETHODCALL
		Element imethodcallA[] = searchNodes(pSimpleReqE, "IMETHODCALL", 0, 1, true);
		if (imethodcallA != null) {
			if (request != null) { throw new CIMXMLParseException(
					"SIMPLEREQ element cannot have METHODCALL and IMETHODCALL child elements!"); }
			request = parseIMETHODCALL(imethodcallA[0]);
		}

		if (request == null) throw new CIMXMLParseException(
				"SIMPLEREQ element missing required child element!");

		// CORRELATOR
		Element[] correlatorA = searchNodes(pSimpleReqE, "CORRELATOR", 0, Integer.MAX_VALUE, true);
		if (correlatorA != null) {
			for (int i = 0; i < correlatorA.length; i++)
				// TODO: return to WBEMClient API if JSR48 changes
				parseCORRELATOR(correlatorA[i]);
		}

		checkOtherNodes(pSimpleReqE, nodesSIMPLEREQ);
		return request;
	}

	/**
	 * parseMULTIREQ
	 * 
	 * @param pMultiReqE
	 * @return CIMRequest
	 * @throws CIMXMLParseException
	 */
	public static CIMRequest parseMULTIREQ(Element pMultiReqE) throws CIMXMLParseException {
		// <!ELEMENT MULTIREQ (SIMPLEREQ,SIMPLEREQ+)>

		Element[] methodReqElementA = searchNodes(pMultiReqE, "SIMPLEREQ", 2, Integer.MAX_VALUE,
				false);
		if (methodReqElementA != null) {
			CIMRequest multiReq = new CIMRequest();
			for (int i = 0; i < methodReqElementA.length; i++) {
				Element methodrequestE = methodReqElementA[i];
				CIMRequest req = parseSIMPLEREQ(methodrequestE);
				req.setMethod("SIMPLEREQ");

				multiReq.addRequest(req);
			}
			return multiReq;
		}
		throw new CIMXMLParseException("MULTIREQ element missing SIMPLEREQ child element!");
	}

	private static final String nodesMETHODCALL[] = { "LOCALCLASSPATH", "LOCALINSTANCEPATH",
			"PARAMVALUE" };

	/**
	 * parseMETHODCALL
	 * 
	 * @param pMethodCallE
	 * @return CIMRequest
	 * @throws CIMXMLParseException
	 */
	public static CIMRequest parseMETHODCALL(Element pMethodCallE) throws CIMXMLParseException {
		// <!ELEMENT METHODCALL
		// ((LOCALCLASSPATH|LOCALINSTANCEPATH),PARAMVALUE*)>
		// <!ATTLIST METHODCALL
		// %CIMName;>

		CIMRequest request = new CIMRequest();
		String methodname = attribute(pMethodCallE, "NAME");
		if (methodname == null) throw new CIMXMLParseException(
				"METHODCALL element missing NAME attribute!");
		request.setMethodName(methodname);

		// EXPMETHODCALL
		boolean localclasspathFound = false;
		Element localclasspathA[] = searchNodes(pMethodCallE, "LOCALCLASSPATH", 0, 1, true);
		if (localclasspathA != null) {
			CIMObjectPath path = parseLOCALCLASSPATH(localclasspathA[0]);

			request.setObjectPath(path);
			localclasspathFound = true;
		}

		Element localinstancepathA[] = searchNodes(pMethodCallE, "LOCALINSTANCEPATH", 0, 1, true);
		if (localinstancepathA != null) {
			if (localclasspathFound) throw new CIMXMLParseException(
					"METHODCALL element cannot have both LOCALCLASSPATH and LOCALINSTANCEPATH child elements!");

			CIMObjectPath path = parseLOCALINSTANCEPATH(localinstancepathA[0]);

			request.setObjectPath(path);
		} else {
			if (!localclasspathFound) throw new CIMXMLParseException(
					"METHODCALL element missing required child element!");
		}

		Element[] paramValueElementA = searchNodes(pMethodCallE, "PARAMVALUE", 0,
				Integer.MAX_VALUE, true);

		if (paramValueElementA != null) {
			CIMArgument<?>[] argA = new CIMArgument[paramValueElementA.length];
			for (int i = 0; i < paramValueElementA.length; i++) {
				Element paramvalueE = paramValueElementA[i];
				argA[i] = parsePARAMVALUE(paramvalueE);
			}
			request.addParamValue(argA);
		}

		checkOtherNodes(pMethodCallE, nodesMETHODCALL);
		return request;
	}

	private static final String nodesIMETHODCALL[] = { "LOCALNAMESPACEPATH", "IPARAMVALUE" };

	/**
	 * parseIMETHODCALL
	 * 
	 * @param pIMethodCallE
	 * @return CIMRequest
	 * @throws CIMXMLParseException
	 */
	public static CIMRequest parseIMETHODCALL(Element pIMethodCallE) throws CIMXMLParseException {
		// <!ELEMENT IMETHODCALL (LOCALNAMESPACEPATH,IPARAMVALUE*)>
		// <!ATTLIST IMETHODCALL
		// %CIMName;>

		CIMRequest request = new CIMRequest();
		String methodname = attribute(pIMethodCallE, "NAME"); // ebak:
		// CIMName->NAME
		if (methodname == null) throw new CIMXMLParseException(
				"IMETHODCALL element missing NAME attribute!");
		request.setMethodName(methodname);

		// METHODCALL
		Element localnamespacepathA[] = searchNodes(pIMethodCallE, "LOCALNAMESPACEPATH", 1, 1, true);
		if (localnamespacepathA != null) {
			String nameSpace = parseLOCALNAMESPACEPATH(localnamespacepathA[0]);
			request.setNameSpace(nameSpace);
		}

		Element[] iParamValElementA = searchNodes(pIMethodCallE, "IPARAMVALUE", 0,
				Integer.MAX_VALUE, true); // ebak:
		// PARAMVALUE->IPARAMVALUE

		if (iParamValElementA != null) {
			CIMArgument<?>[] argA = new CIMArgument[iParamValElementA.length];
			for (int i = 0; i < iParamValElementA.length; i++) {
				Element paramvalueE = iParamValElementA[i];
				CIMArgument<Object> arg = parseIPARAMVALUE(paramvalueE);
				/*
				 * ebak: local nameSpacePath should be added to those reference
				 * arguments which don't contain namespace
				 */
				Object value = arg.getValue();
				if (value instanceof CIMObjectPath) {
					CIMObjectPath op = (CIMObjectPath) value;
					if (op.getNamespace() == null || op.getNamespace().length() == 0) {
						arg = new CIMArgument<Object>(arg.getName(), arg.getDataType(),
						// CIMObjectPath(String scheme, String host, String
								// port, String namespace, String objectName,
								// CIMProperty[] keys)
								new CIMObjectPath(op.getScheme(), op.getHost(), op.getPort(),
										request.getNameSpace(), op.getObjectName(), op.getKeys()));
					}
				}
				argA[i] = arg;
			}
			request.addParamValue(argA);
		}

		checkOtherNodes(pIMethodCallE, nodesIMETHODCALL);
		return request;
	}

	private static final String nodesSIMPLEEXPREQ[] = { "CORRELATOR", "EXPMETHODCALL" };

	/**
	 * parseSIMPLEEXPREQ
	 * 
	 * @param pSimpleExpReqE
	 * @return CIMRequest
	 * @throws CIMXMLParseException
	 */
	public static CIMRequest parseSIMPLEEXPREQ(Element pSimpleExpReqE) throws CIMXMLParseException {
		// <!ELEMENT SIMPLEEXPREQ (CORRELATOR*, EXPMETHODCALL)>
		CIMRequest request = null;

		// EXPMETHODCALL
		Element[] expmethodcallA = searchNodes(pSimpleExpReqE, "EXPMETHODCALL", 1, 1, true);
		if (expmethodcallA != null) {
			request = parseEXPMETHODCALL(expmethodcallA[0]);
		} else {
			throw new CIMXMLParseException(
					"SIMPLEEXPREQ element missing EXPMETHODCALL child element!");
		}

		// CORRELATOR
		Element[] correlatorA = searchNodes(pSimpleExpReqE, "CORRELATOR", 0, Integer.MAX_VALUE,
				true);
		if (correlatorA != null) {
			for (int i = 0; i < correlatorA.length; i++)
				// TODO: return to WBEMClient API if JSR48 changes
				parseCORRELATOR(correlatorA[i]);
		}

		checkOtherNodes(pSimpleExpReqE, nodesSIMPLEEXPREQ);
		return request;
	}

	/**
	 * parseMULTIEXPREQ
	 * 
	 * @param pMultiExpReqE
	 * @return CIMRequest
	 * @throws CIMXMLParseException
	 */
	public static CIMRequest parseMULTIEXPREQ(Element pMultiExpReqE) throws CIMXMLParseException {
		// <!ELEMENT SIMPLEEXPREQ (METHODRESPONSE)>

		Element[] methodReqElementA = searchNodes(pMultiExpReqE, "SIMPLEEXPREQ", 2,
				Integer.MAX_VALUE, false);
		if (methodReqElementA != null) {
			CIMRequest multiReq = new CIMRequest();
			for (int i = 0; i < methodReqElementA.length; i++) {
				Element methodrequestE = methodReqElementA[i];
				CIMRequest req = parseSIMPLEEXPREQ(methodrequestE);
				req.setMethod("SIMPLEEXPREQ");

				multiReq.addRequest(req);
			}
			return multiReq;
		}
		throw new CIMXMLParseException("MULTIEXPREQ element missing SIMPLEEXPREQ child element!");
	}

	private static final String nodesEXPMETHODCALL[] = { "EXPPARAMVALUE" };

	/**
	 * parseEXPMETHODCALL
	 * 
	 * @param pExpMethodCallE
	 * @return CIMRequest
	 * @throws CIMXMLParseException
	 */
	public static CIMRequest parseEXPMETHODCALL(Element pExpMethodCallE)
			throws CIMXMLParseException {
		// <!ELEMENT EXPMETHODCALL (EXPPARAMVALUE*)>

		// EXPMETHODCALL
		CIMRequest request = new CIMRequest();
		String methodname = attribute(pExpMethodCallE, "NAME");
		if (methodname == null) throw new CIMXMLParseException(
				"EXPMETHODCALL element missing NAME attribute!");
		request.setMethodName(methodname);

		Element[] paramValElementA = searchNodes(pExpMethodCallE, "EXPPARAMVALUE", 0,
				Integer.MAX_VALUE, false);
		Vector<CIMInstance> v = new Vector<CIMInstance>();
		if (paramValElementA != null) {
			for (int i = 0; i < paramValElementA.length; i++) {
				Element expparamvalueE = paramValElementA[i];
				CIMInstance inst = parseEXPPARAMVALUE(expparamvalueE);
				v.add(inst);
			}
		}
		request.addParamValue(v);
		checkOtherNodes(pExpMethodCallE, nodesEXPMETHODCALL);
		return request;
	}

	private static final String nodesEXPPARAMVALUE[] = { "INSTANCE" };

	/**
	 * parseEXPPARAMVALUE
	 * 
	 * @param pExpParamValueE
	 * @return CIMInstance
	 * @throws CIMXMLParseException
	 */
	public static CIMInstance parseEXPPARAMVALUE(Element pExpParamValueE)
			throws CIMXMLParseException {
		// <!ELEMENT EXPPARAMVALUE (INSTANCE?)>
		// <!ATTLIST EXPPARAMVALUE
		// %CIMName;>
		// INSTANCE
		if (attribute(pExpParamValueE, "NAME") == null) throw new CIMXMLParseException(
				"EXPPARAMVALUE element missing NAME attribute!");

		Element[] instanceA = searchNodes(pExpParamValueE, "INSTANCE", 0, 1, false);
		if (instanceA != null) {
			CIMInstance inst = parseINSTANCE(instanceA[0]);
			return inst;
		}

		checkOtherNodes(pExpParamValueE, nodesEXPPARAMVALUE);

		return null;
	}

	private static final String nodesMETHODRESPONSE[] = { "ERROR", "RETURNVALUE", "PARAMVALUE" };

	/**
	 * parseMETHODRESPONSE
	 * 
	 * @param pMethodResponseE
	 * @return CIMResponse
	 * @throws CIMXMLParseException
	 */
	public static CIMResponse parseMETHODRESPONSE(Element pMethodResponseE)
			throws CIMXMLParseException {
		// <!ELEMENT METHODRESPONSE (ERROR|(RETURNVALUE?,PARAMVALUE*))>
		if (attribute(pMethodResponseE, "NAME") == null) throw new CIMXMLParseException(
				"METHODRESPONSE element missing NAME attribute!");

		CIMResponse response = new CIMResponse();

		// ERROR
		Element errorA[] = searchNodes(pMethodResponseE, "ERROR", 0, 1, false);
		if (errorA != null) {
			WBEMException exception = parseERROR(errorA[0]);
			response.setError(exception);
			return response;
		}

		// RETURNVALUE
		Element[] retValElementA = searchNodes(pMethodResponseE, "RETURNVALUE", 0, 1, true);
		if (retValElementA != null) {
			Vector<Object> v = new Vector<Object>();
			for (int i = 0; i < retValElementA.length; i++) {
				Element returnvalueE = retValElementA[i];
				v.add(parseRETURNVALUE(returnvalueE));
			}
			response.setReturnValue(v);
		}

		// PARAMVALUE
		Element[] paramValElementA = searchNodes(pMethodResponseE, "PARAMVALUE", 0,
				Integer.MAX_VALUE, true);
		if (paramValElementA != null) {
			Vector<Object> v = new Vector<Object>();
			for (int i = 0; i < paramValElementA.length; i++) {
				Element paramvalueE = paramValElementA[i];
				CIMArgument<?> arg = parsePARAMVALUE(paramvalueE);
				v.add(arg);
			}
			response.addParamValue(v);
		}

		// PARAMVALUE (ESS fix)
		if (retValElementA != null) {
			for (int i = 0; i < retValElementA.length; i++) {
				Element retValE = retValElementA[i];

				paramValElementA = searchNodes(retValE, "PARAMVALUE");
				if (paramValElementA != null) {
					Vector<CIMArgument<Object>> v = new Vector<CIMArgument<Object>>();
					for (int j = 0; j < paramValElementA.length; j++) {
						Element paramvalueE = paramValElementA[j];
						CIMArgument<Object> arg = parsePARAMVALUE(paramvalueE);
						v.add(arg);
					}
					response.addParamValue(v);
				}
			}
		}
		checkOtherNodes(pMethodResponseE, nodesMETHODRESPONSE);

		return response;
	}

	private static final String nodesIMETHODRESPONSE[] = { "ERROR", "IRETURNVALUE", "PARAMVALUE" };

	/**
	 * parseIMETHODRESPONSE
	 * 
	 * @param pIMethodResponseE
	 * @return CIMResponse
	 * @throws CIMXMLParseException
	 */
	public static CIMResponse parseIMETHODRESPONSE(Element pIMethodResponseE)
			throws CIMXMLParseException {
		// <!ELEMENT IMETHODRESPONSE (ERROR|(IRETURNVALUE?, PARAMVALUE*))>
		if (attribute(pIMethodResponseE, "NAME") == null) throw new CIMXMLParseException(
				"IMETHODRESPONSE element missing NAME attribute!");

		CIMResponse response = new CIMResponse();
		// ERROR
		Element[] errorA = searchNodes(pIMethodResponseE, "ERROR", 0, 1, false);
		if (errorA != null) {
			WBEMException exception = parseERROR(errorA[0]);
			response.setError(exception);
			return response;
		}

		// IRETURNVALUE
		Element[] retValElementA = searchNodes(pIMethodResponseE, "IRETURNVALUE", 0, 1, true);
		if (retValElementA != null) {
			for (int i = 0; i < retValElementA.length; i++) {
				Element ireturnvalueE = retValElementA[i];

				Vector<Object> rtnV = parseIRETURNVALUE(ireturnvalueE);
				response.setReturnValue(rtnV);
			}
		}

		// PARAMVALUE
		Element[] paramValElementA = searchNodes(pIMethodResponseE, "PARAMVALUE", 0,
				Integer.MAX_VALUE, true);
		if (paramValElementA != null) {
			Vector<Object> v = new Vector<Object>();
			for (int i = 0; i < paramValElementA.length; i++) {
				Element paramvalueE = paramValElementA[i];
				CIMArgument<?> arg = parsePARAMVALUE(paramvalueE);
				v.add(arg);
			}
			response.addParamValue(v);
		}
		checkOtherNodes(pIMethodResponseE, nodesIMETHODRESPONSE);

		return response;
	}

	private static final String nodesERROR[] = { "INSTANCE" };

	/**
	 * parseERROR
	 * 
	 * @param pErrorE
	 * @return WBEMException
	 * @throws CIMXMLParseException
	 */
	public static WBEMException parseERROR(Element pErrorE) throws CIMXMLParseException {
		// <!ELEMENT ERROR (INSTANCE*)>
		// <!ATTLSIT ERROR %CODE;%DESCRIPTION;>

		Attr error_codeA = (Attr) searchAttribute(pErrorE, "CODE");
		if (error_codeA == null) throw new CIMXMLParseException(
				"ERROR element missing CODE attribute!");
		String code = error_codeA.getNodeValue();
		int errorCode = 0;
		try {
			if (code.length() > 0) errorCode = Integer.parseInt(code);
		} catch (Exception e) {
			LogAndTraceBroker.getBroker().trace(Level.WARNING,
					"exception while parsing error code from XML", e);
			errorCode = WBEMException.CIM_ERR_FAILED;
		}
		Attr error_descriptionA = (Attr) searchAttribute(pErrorE, "DESCRIPTION");
		String description = "";
		if (error_descriptionA != null) {
			description = error_descriptionA.getNodeValue();
		}

		Vector<Object> rtnV = new Vector<Object>();

		// INSTANCE
		Element[] instElementA = searchNodes(pErrorE, "INSTANCE", 0, Integer.MAX_VALUE, false);
		if (instElementA != null) {
			for (int i = 0; i < instElementA.length; i++) {
				Element instanceE = instElementA[i];
				CIMInstance inst = parseINSTANCE(instanceE);
				rtnV.add(inst);
			}
		}
		checkOtherNodes(pErrorE, nodesERROR);

		// throw new CIMException(CIMException.getErrorName(errorCode),
		// description.substring(description.indexOf(':')+1));
		if (!rtnV.isEmpty()) return new WBEMException(errorCode, "ErrorCode:" + errorCode
				+ " description:" + description, rtnV.toArray(new CIMInstance[0]));
		return new WBEMException(errorCode, "ErrorCode:" + errorCode + " description:"
				+ description);
	}

	private static final String nodesRETURNVALUE[] = { "VALUE", "VALUE.REFERENCE" };

	/**
	 * parseRETURNVALUE
	 * 
	 * @param pRetValE
	 * @return Object
	 * @throws CIMXMLParseException
	 */
	public static Object parseRETURNVALUE(Element pRetValE) throws CIMXMLParseException {
		// <!ELEMENT RETURNVALUE (VALUE|VALUE.REFERENCE)?>
		// <!ATTLSIT RETURNVALUE %PARAMTYPE;>
		// ebak: embedded object support: different parsing of VALUE sub element
		checkOtherNodes(pRetValE, nodesRETURNVALUE);
		if (searchNodes(pRetValE, "VALUE", 0, 1, false) != null) {
			EmbObjHandler embObjHandler = new EmbObjHandler(pRetValE);
			return embObjHandler.getValue();
		}
		if (searchNodes(pRetValE, "VALUE.REFERENCE", 0, 1, false) != null) {
			TypedValue typedVal = parseSingleValue(pRetValE, VALUEREF);
			Object value = typedVal.getValue();
			return value;
		}
		checkOtherNodes(pRetValE, nodesRETURNVALUE);
		return null;
	}

	private static final String nodesIRETURNVALUE[] = { "CLASSNAME", "INSTANCENAME", "VALUE",
			"VALUE.OBJECTWITHPATH", "VALUE.OBJECTWITHLOCALPATH", "VALUE.OBJECT", "OBJECTPATH",
			"QUALIFIER.DECLARATION", "VALUE.ARRAY", "VALUE.REFERENCE", "CLASS", "INSTANCE",
			"INSTANCEPATH", "VALUE.NAMEDINSTANCE" };

	/**
	 * parseIRETURNVALUE
	 * 
	 * @param pIRetValE
	 * @return Vector
	 * @throws CIMXMLParseException
	 */
	public static Vector<Object> parseIRETURNVALUE(Element pIRetValE) throws CIMXMLParseException {
		// <!ELEMENT IRETURNVALUE (CLASSNAME*|INSTANCENAME*|INSTANCEPATH*
		// |VALUE*|VALUE.OBJECTWITHPATH*|VALUE.OBJECTWITHLOCALPATH*|VALUE.OBJECT*
		// |OBJECTPATH*|QUALIFIER.DECLARATION*|VALUE.ARRAY|VALUE.REFERENCE*
		// |CLASS*|INSTANCE*|VALUE.NAMEDINSTANCE*|VALUE.INSTANCEWITHPATH)>

		Vector<Object> rtnV = new Vector<Object>();

		// CLASS
		Element[] classElementA = searchNodes(pIRetValE, "CLASS", 0, Integer.MAX_VALUE, false);
		if (classElementA != null) {
			for (int i = 0; i < classElementA.length; i++) {
				Element classE = classElementA[i];
				CIMClass c = parseCLASS(classE);
				rtnV.add(c);
			}
			return rtnV;
		}

		// INSTANCE
		Element[] instElementA = searchNodes(pIRetValE, "INSTANCE", 0, Integer.MAX_VALUE, false);
		if (instElementA != null) {
			for (int i = 0; i < instElementA.length; i++) {
				Element instanceE = instElementA[i];
				CIMInstance inst = parseINSTANCE(instanceE);
				rtnV.add(inst);
			}
			return rtnV;
		}

		// CLASSNAME
		Element[] classNameElementA = searchNodes(pIRetValE, "CLASSNAME", 0, Integer.MAX_VALUE,
				false);
		if (classNameElementA != null) {
			for (int i = 0; i < classNameElementA.length; i++) {
				Element classnameE = classNameElementA[i];
				CIMObjectPath op = parseCLASSNAME(classnameE);
				rtnV.add(op);
			}
			return rtnV;
		}

		// INSTANCENAME
		Element[] instNameElementA = searchNodes(pIRetValE, "INSTANCENAME", 0, Integer.MAX_VALUE,
				false);
		if (instNameElementA != null) {
			for (int i = 0; i < instNameElementA.length; i++) {
				Element instancenameE = instNameElementA[i];
				CIMObjectPath op = parseINSTANCENAME(instancenameE);
				rtnV.add(op);
			}
			return rtnV;
		}

		// INSTANCEPATH
		Element[] instpathElementA = searchNodes(pIRetValE, "INSTANCEPATH", 0, Integer.MAX_VALUE,
				false);
		if (instpathElementA != null) {
			for (int i = 0; i < instpathElementA.length; i++) {
				Element instancePathE = instpathElementA[i];
				CIMObjectPath op = parseINSTANCEPATH(instancePathE);
				rtnV.add(op);
			}
			return rtnV;
		}

		// OBJECTPATH
		Element[] objPathElementA = searchNodes(pIRetValE, "OBJECTPATH", 0, Integer.MAX_VALUE,
				false);
		if (objPathElementA != null) {
			for (int i = 0; i < objPathElementA.length; i++) {
				Element objectpathE = objPathElementA[i];
				CIMObjectPath op = parseOBJECTPATH(objectpathE);
				rtnV.add(op);
			}
			return rtnV;
		}

		// VALUE
		Element[] valueElementA = searchNodes(pIRetValE, "VALUE", 0, Integer.MAX_VALUE, false);
		if (valueElementA != null) {
			for (int i = 0; i < valueElementA.length; i++) {
				Element valueE = valueElementA[i];
				TypedValue tv = parseVALUE(valueE);
				rtnV.add(tv.getValue());
			}
			return rtnV;
		}

		// VALUE.ARRAY
		Element[] valueArrayElementA = searchNodes(pIRetValE, "VALUE.ARRAY", 0, 1, false);
		if (valueArrayElementA != null) {
			Element valuearrayE = valueArrayElementA[0];
			TypedValue tv = parseVALUEARRAY(valuearrayE);
			rtnV.add(tv.getValue());
			return rtnV;
		}

		// VALUE.REFERENCE
		Element[] valRefElementA = searchNodes(pIRetValE, "VALUE.REFERENCE", 0, 1, false);
		if (valRefElementA != null) {
			Element valuereferenceE = valRefElementA[0];
			CIMObjectPath op = parseVALUEREFERENCE(valuereferenceE);
			rtnV.add(op);
			return rtnV;
		}

		// VALUE.OBJECT
		Element[] valObjElementA = searchNodes(pIRetValE, "VALUE.OBJECT", 0, Integer.MAX_VALUE,
				false);
		if (valObjElementA != null) {
			for (int i = 0; i < valObjElementA.length; i++) {
				Element valueobjectE = valObjElementA[i];
				CIMNamedElementInterface obj = parseVALUEOBJECT(valueobjectE);
				rtnV.add(obj);
			}
			return rtnV;
		}

		// VALUE.NAMEDINSTANCE
		Element[] valNamedInstElementA = searchNodes(pIRetValE, "VALUE.NAMEDINSTANCE", 0,
				Integer.MAX_VALUE, false);
		if (valNamedInstElementA != null) {
			for (int i = 0; i < valNamedInstElementA.length; i++) {
				Element valuenamedisntanceE = valNamedInstElementA[i];
				CIMInstance inst = parseVALUENAMEDINSTANCE(valuenamedisntanceE);
				rtnV.add(inst);
			}
			return rtnV;
		}

		// VALUE.INSTANCEWITHPATH
		Element[] valInstWithPathElementA = searchNodes(pIRetValE, "VALUE.INSTANCEWITHPATH", 0,
				Integer.MAX_VALUE, false);
		if (valInstWithPathElementA != null) {
			for (int i = 0; i < valInstWithPathElementA.length; i++) {
				Element valueinstancewithpathE = valInstWithPathElementA[i];
				CIMInstance inst = parseVALUEINSTANCEWITHPATH(valueinstancewithpathE);
				rtnV.add(inst);
			}
			return rtnV;
		}

		// VALUE.OBJECTWITHPATH
		Element[] valObjWithPathElementA = searchNodes(pIRetValE, "VALUE.OBJECTWITHPATH", 0,
				Integer.MAX_VALUE, false);
		if (valObjWithPathElementA != null) {
			for (int i = 0; i < valObjWithPathElementA.length; i++) {
				Element valueobjectwithpathE = valObjWithPathElementA[i];
				CIMNamedElementInterface namedIF = parseVALUEOBJECTWITHPATH(valueobjectwithpathE);
				rtnV.add(namedIF);
			}
			return rtnV;
		}

		// VALUE.OBJECTWITHLOCALPATH
		Element[] valObjWithLocalPathElementA = searchNodes(pIRetValE, "VALUE.OBJECTWITHLOCALPATH",
				0, Integer.MAX_VALUE, false);
		if (valObjWithLocalPathElementA != null) {
			for (int i = 0; i < valObjWithLocalPathElementA.length; i++) {
				Element valueobjectwithlocalpathE = valObjWithLocalPathElementA[i];
				CIMNamedElementInterface namedIF = parseVALUEOBJECTWITHLOCALPATH(valueobjectwithlocalpathE);
				rtnV.add(namedIF);
			}
			return rtnV;
		}

		// QUALIFIER.DECLARATION
		Element[] qualiDeclElementA = searchNodes(pIRetValE, "QUALIFIER.DECLARATION", 0,
				Integer.MAX_VALUE, false);
		if (qualiDeclElementA != null) {
			for (int i = 0; i < qualiDeclElementA.length; i++) {
				Element qualifierdeclarationE = qualiDeclElementA[i];
				CIMQualifierType<Object> o = parseQUALIFIERDECLARATION(qualifierdeclarationE);
				rtnV.add(o);
			}
			return rtnV;
		}
		checkOtherNodes(pIRetValE, nodesIRETURNVALUE);
		return rtnV;
	}

	/**
	 * parseObject
	 * 
	 * @param pRootE
	 * @return Object
	 * @throws CIMXMLParseException
	 */
	public static Object parseObject(Element pRootE) throws CIMXMLParseException {
		Object o = null;
		String nodeName = pRootE.getNodeName();
		if (nodeName.equalsIgnoreCase("INSTANCE")) {
			o = parseINSTANCE(pRootE);
		} else if (nodeName.equalsIgnoreCase("VALUE.NAMEDINSTANCE")) {
			o = parseVALUENAMEDINSTANCE(pRootE);
		} else if (nodeName.equalsIgnoreCase("VALUE.NAMEDOBJECT")) {
			o = parseVALUENAMEDOBJECT(pRootE);
		} else if (nodeName.equalsIgnoreCase("VALUE.OBJECTWITHPATH")) {
			o = parseVALUEOBJECTWITHPATH(pRootE);
		} else if (nodeName.equalsIgnoreCase("VALUE.OBJECTWITHLOCALPATH")) {
			o = parseVALUEOBJECTWITHLOCALPATH(pRootE);
		} else if (nodeName.equalsIgnoreCase("CLASS")) {
			o = parseCLASS(pRootE);
		} else if (nodeName.equalsIgnoreCase("CLASSPATH")) {
			o = parseCLASSPATH(pRootE);
		} else if (nodeName.equalsIgnoreCase("LOCALCLASSPATH")) {
			o = parseLOCALCLASSPATH(pRootE);
		} else if (nodeName.equalsIgnoreCase("OBJECTPATH")) {
			o = parseOBJECTPATH(pRootE);
		} else if (nodeName.equalsIgnoreCase("CLASSNAME")) {
			o = parseCLASSNAME(pRootE);
		} else if (nodeName.equalsIgnoreCase("INSTANCEPATH")) {
			o = parseINSTANCEPATH(pRootE);
		} else if (nodeName.equalsIgnoreCase("LOCALINSTANCEPATH")) {
			o = parseLOCALINSTANCEPATH(pRootE);
		} else if (nodeName.equalsIgnoreCase("INSTANCENAME")) {
			o = parseINSTANCENAME(pRootE);
		} else if (nodeName.equalsIgnoreCase("QUALIFIER")) {
			o = parseQUALIFIER(pRootE);
		} else if (nodeName.equalsIgnoreCase("PROPERTY")) {
			o = parsePROPERTY(pRootE);
		} else if (nodeName.equalsIgnoreCase("PROPERTY.ARRAY")) {
			o = parsePROPERTYARRAY(pRootE);
		} else if (nodeName.equalsIgnoreCase("PROPERTY.REFERENCE")) {
			o = parsePROPERTYREFERENCE(pRootE);
		} else if (nodeName.equalsIgnoreCase("METHOD")) {
			o = parseMETHOD(pRootE);
		} else if (nodeName.equalsIgnoreCase("PARAMETER")) {
			o = parsePARAMETER(pRootE);
		} else if (nodeName.equalsIgnoreCase("PARAMETER.REFERENCE")) {
			o = parsePARAMETERREFERENCE(pRootE);
		} else if (nodeName.equalsIgnoreCase("PARAMETER.ARRAY")) {
			o = parsePARAMETERARRAY(pRootE);
		} else if (nodeName.equalsIgnoreCase("PARAMETER.REFARRAY")) {
			o = parsePARAMETERREFARRAY(pRootE);
		}
		return o;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * hasNodes
	 * 
	 * @param pParentE
	 * @return boolean
	 */
	private static boolean hasNodes(Element pParentE) {
		NodeList nl = pParentE.getChildNodes();
		if (nl == null || nl.getLength() == 0) return false;
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (!(n instanceof Text) || !("".equalsIgnoreCase(n.getNodeValue().trim()))) return true;
		}
		return false;
	}

	/**
	 * hasTypeAttrsInNodes
	 * 
	 * SVC CIMOM sends typed CIM-XML elements in a non-standard way. The TYPE
	 * attribute is included in the VALUE or VALUE.ARRAY element not in the
	 * enclosing element as the standard says.
	 * 
	 * @param pParentE
	 * @return boolean
	 */
	private static boolean hasTypeAttrsInNodes(Element pParentE) {
		NodeList nl = pParentE.getChildNodes();
		if (nl == null || nl.getLength() == 0) return false;
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			String name = n.getNodeName();
			if ("VALUE".equalsIgnoreCase(name) || "VALUE.ARRAY".equalsIgnoreCase(name)) {
				NamedNodeMap nm = n.getAttributes();
				if (nm != null
						&& (nm.getNamedItem("TYPE") != null || nm.getNamedItem("PARAMTYPE") != null)) return true;
			}
		}
		return false;
	}

	private static final HashMap<String, String> NODENAME_HASH = new HashMap<String, String>();

	private static void initNodeNameHash(String[] pEnumA) {
		for (int i = 0; i < pEnumA.length; i++)
			NODENAME_HASH.put(pEnumA[i], pEnumA[i]);
	}

	static {
		initNodeNameHash(new String[] { "CIM", "DECLARATION", "DECLGROUP", "DECLGROUP.WITHNAME",
				"DECLGROUP.WITHPATH", "QUALIFIER.DECLARATION", "SCOPE", "VALUE", "VALUE.ARRAY",
				"VALUE.REFERENCE", "VALUE.REFARRAY", "VALUE.OBJECT", "VALUE.NAMEDINSTANCE",
				"VALUE.NAMEDOBJECT", "VALUE.OBJECTWITHLOCALPATH", "VALUE.OBJECTWITHPATH",
				"VALUE.NULL", "VALUE.INSTANCEWITHPATH", "NAMESPACEPATH", "LOCALNAMESPACEPATH",
				"HOST", "NAMESPACE", "CLASSPATH", "LOCALCLASSPATH", "CLASSNAME", "INSTANCEPATH",
				"LOCALINSTANCEPATH", "INSTANCENAME", "OBJECTPATH", "KEYBINDING", "KEYVALUE",
				"CLASS", "INSTANCE", "QUALIFIER", "PROPERTY", "PROPERTY.ARRAY",
				"PROPERTY.REFERENCE", "METHOD", "PARAMETER", "PARAMETER.REFERENCE",
				"PARAMETER.ARRAY", "PARAMETER.REFARRAY", "MESSAGE", "MULTIREQ", "MULTIEXPREQ",
				"SIMPLEREQ", "SIMPLEEXPREQ", "IMETHODCALL", "METHODCALL", "EXPMETHODCALL",
				"PARAMVALUE", "IPARAMVALUE", "EXPPARAMVALUE", "MULTIRSP", "MULTIEXPRSP",
				"SIMPLERSP", "SIMPLEEXPRSP", "METHODRESPONSE", "EXPMETHODRESPONSE",
				"IMETHODRESPONSE", "ERROR", "RETURNVALUE", "IRETURNVALUE", "CORRELATOR" });
	}

	/**
	 * checkOtherNodes
	 * 
	 * @param pParentE
	 * @param pAllowedChildNodes
	 */
	private static void checkOtherNodes(Element pParentE, String[] pAllowedChildNodes)
			throws CIMXMLParseException {
		NodeList nl = pParentE.getChildNodes();
		if (nl == null || nl.getLength() == 0) return;
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n instanceof Text && "".equalsIgnoreCase(n.getNodeValue().trim())) continue;
			boolean found = false;
			String name = n.getNodeName().toUpperCase();
			for (int j = 0; j < pAllowedChildNodes.length; j++) {
				if (pAllowedChildNodes[j].equalsIgnoreCase(name)) {
					found = true;
					break;
				}
			}
			if (!found && NODENAME_HASH.containsKey(name)) throw new CIMXMLParseException(pParentE
					.getNodeName()
					+ " element contains invalid child element " + name + "!");
		}
	}

	/**
	 * searchNodes
	 * 
	 * @param pParentE
	 * @param pTagName
	 * @return Element[]
	 */
	public static Element[] searchNodes(Element pParentE, String pTagName) {
		// return all of child nodes immediately below the parent
		// return null if not found
		NodeList nl = pParentE.getChildNodes();
		if (nl == null || nl.getLength() == 0) return null;
		Vector<Node> resElementV = new Vector<Node>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeName().equals(pTagName)) {
				resElementV.add(n);
			}
		}
		return resElementV.toArray(new Element[0]);
	}

	/**
	 * searchNodes
	 * 
	 * @param pParentE
	 * @param pTagName
	 * @param pMin
	 * @param pMax
	 * @param pAllowOtherNodes
	 * @return Element[]
	 * @throws CIMXMLParseException
	 */
	public static Element[] searchNodes(Element pParentE, String pTagName, int pMin, int pMax,
			boolean pAllowOtherNodes) throws CIMXMLParseException {
		// return all child nodes immediately below parent, null if none found

		NodeList nl = pParentE.getChildNodes();
		if (nl == null || nl.getLength() == 0) {
			if (pMin > 0) throw new CIMXMLParseException(pParentE.getNodeName()
					+ " element must have at least " + pMin + " " + pTagName + " child element(s)!");
			return null;
		}

		String otherNode = null;
		Vector<Node> resElementV = new Vector<Node>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			String name = n.getNodeName().toUpperCase();
			if (name.equals(pTagName)) {
				resElementV.add(n);
			} else if (NODENAME_HASH.containsKey(name)) {
				otherNode = name;
			}
		}

		if (resElementV.size() < pMin) throw new CIMXMLParseException(pParentE.getNodeName()
				+ " element must have at least " + pMin + " " + pTagName + " child element(s)!");
		if (resElementV.size() > pMax) throw new CIMXMLParseException(pParentE.getNodeName()
				+ " element can have no more than " + pMax + " " + pTagName + " child element(s)!");
		if (resElementV.size() > 0 && !pAllowOtherNodes && otherNode != null) throw new CIMXMLParseException(
				pParentE.getNodeName() + " element cannot have " + otherNode
						+ " child element(s) when it already has " + pTagName + " element(s)!");
		if (resElementV.size() == 0) return null;

		return resElementV.toArray(new Element[0]);
	}

	/**
	 * searchFirstNode
	 * 
	 * @param pParentE
	 * @param pTagName
	 * @return Node
	 */
	public static Node searchFirstNode(Element pParentE, String pTagName) {
		// return the first node which matches to the specific name
		// return null if not found
		NodeList nl = pParentE.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeName().equals(pTagName)) { return n; }
		}
		return null;
	}

	/**
	 * searchAttribute
	 * 
	 * @param pParentN
	 * @param pAttrName
	 * @return Node
	 */
	public static Node searchAttribute(Node pParentN, String pAttrName) {
		// return the attribute node with the specific name
		NamedNodeMap nnm = pParentN.getAttributes();
		return nnm.getNamedItem(pAttrName);
	}

	/**
	 * searchFirstChild
	 * 
	 * @param pParentE
	 * @return Node
	 */
	public static Node searchFirstChild(Element pParentE) {
		// return the first node which matches to the specific name
		// return null if not found
		return pParentE.getFirstChild();
	}

	/**
	 * createJavaObject
	 * 
	 * @param pTypeStr
	 * @param pValue
	 * @return Object
	 * @throws CIMXMLParseException
	 */
	public static Object createJavaObject(String pTypeStr, String pValue)
			throws CIMXMLParseException {
		// return a java object with the specific type
		if (pTypeStr == null) pTypeStr = MOF.DT_STR;
		if (MOF.NULL.equalsIgnoreCase(pTypeStr)) return null;
		Object o = null;
		CIMDataType cimType = parseTypeStr(pTypeStr, false);
		int radix = 10;

		if (pTypeStr.toLowerCase().startsWith("sint") || pTypeStr.toLowerCase().startsWith("uint")) {
			pValue = pValue.toLowerCase();
			if (pValue.startsWith("0x") || pValue.startsWith("+0x") || pValue.startsWith("-0x")) {
				radix = 16;
				if (pValue.startsWith("-")) pValue = "-" + pValue.substring(3);
				else pValue = pValue.substring(pValue.indexOf('x') + 1);
			}
		}

		switch (cimType.getType()) {
			case CIMDataType.UINT8:
				o = new UnsignedInteger8(Short.parseShort(pValue, radix));
				break;
			case CIMDataType.UINT16:
				o = new UnsignedInteger16(Integer.parseInt(pValue, radix));
				break;
			case CIMDataType.UINT32:
				o = new UnsignedInteger32(Long.parseLong(pValue, radix));
				break;
			case CIMDataType.UINT64:
				o = new UnsignedInteger64(new BigInteger(pValue, radix));
				break;
			case CIMDataType.SINT8:
				o = Byte.valueOf(pValue, radix);
				break;
			case CIMDataType.SINT16:
				o = Short.valueOf(pValue, radix);
				break;
			case CIMDataType.SINT32:
				o = Integer.valueOf(pValue, radix);
				break;
			case CIMDataType.SINT64:
				o = Long.valueOf(pValue, radix);
				break;
			case CIMDataType.STRING:
				o = pValue;
				break;
			case CIMDataType.BOOLEAN:
				o = Boolean.valueOf(pValue);
				break;
			case CIMDataType.REAL32:
				o = new Float(pValue);
				break;
			case CIMDataType.REAL64:
				if (WBEMConfiguration.getGlobalConfiguration().verifyJavaLangDoubleStrings()) {
					if (Util.isBadDoubleString(pValue)) throw new IllegalArgumentException(
							"Double value string hangs older JVMs!\n" + pValue);
				}
				o = new Double(pValue);
				break;
			case CIMDataType.DATETIME:
				o = getDateTime(pValue);
				break;
			case CIMDataType.REFERENCE:
				o = new CIMObjectPath(pValue);
				break;
			case CIMDataType.CHAR16:
				o = Character.valueOf(pValue.charAt(0));
				break;
			// case CIMDataType.OBJECT: o = new CIMInstance(); break; //TODO
			// case CIMDataType.CLASS: o = new CIMClass(value); break; //TODO
		}
		return o;
	}

	private static CIMDateTime getDateTime(String pValue) throws CIMXMLParseException {
		try {
			return new CIMDateTimeAbsolute(pValue);
		} catch (IllegalArgumentException eAbs) {
			try {
				return new CIMDateTimeInterval(pValue);
			} catch (IllegalArgumentException eInt) {
				throw new CIMXMLParseException("Failed to parse dateTime string: " + pValue + "!\n"
						+ "CIMDateTimeAbsolute parsing error:\n" + eAbs.getMessage() + "\n"
						+ "CIMDateTimeInterval parsing error:\n" + eInt.getMessage());
			}
		}
	}

	/**
	 * parseSIMPLEEXPRSP
	 * 
	 * @param pSimpleExpRspE
	 * @return CIMResponse
	 * @throws CIMXMLParseException
	 */
	public static CIMResponse parseSIMPLEEXPRSP(Element pSimpleExpRspE) throws CIMXMLParseException {
		// <!ELEMENT SIMPLEEXPRSP (EXPMETHODRESPONSE)>

		// EXPMETHODRESPONSE
		Element[] expmethodresponseA = searchNodes(pSimpleExpRspE, "EXPMETHODRESPONSE", 1, 1, false);
		if (expmethodresponseA != null) return parseEXPMETHODRESPONSE(expmethodresponseA[0]);

		throw new CIMXMLParseException(
				"SIMPLEEXPRSP element missing EXPMETHODRESPONSE child element!");
	}

	/**
	 * parseMULTIEXPRSP
	 * 
	 * @param pMultiExpRspE
	 * @return CIMResponse
	 * @throws CIMXMLParseException
	 */
	public static CIMResponse parseMULTIEXPRSP(Element pMultiExpRspE) throws CIMXMLParseException {
		// <!ELEMENT MULTIEXPRSP (SIMPLEEXPRSP,SIMPLEEXPRSP+)>

		Element[] multiExpRespElementA = searchNodes(pMultiExpRspE, "SIMPLEEXPRSP", 2,
				Integer.MAX_VALUE, false);
		if (multiExpRespElementA != null) {
			CIMResponse multiExpRsp = new CIMResponse();
			for (int i = 0; i < multiExpRespElementA.length; i++) {
				Element methodresponseE = multiExpRespElementA[i];
				CIMResponse rsp = parseSIMPLEEXPRSP(methodresponseE);
				rsp.setMethod("SIMPLEEXPRSP");
				multiExpRsp.addResponse(rsp);
			}
			return multiExpRsp;
		}

		throw new CIMXMLParseException("MULTIEXPRSP element missing SIMPLEEXPRSP child element!");
	}

	private static final String[] nodesEXPMETHODRESPONSE = { "ERROR", "IRETURNVALUE" };

	/**
	 * parseEXPMETHODRESPONSE
	 * 
	 * @param pExpMethodResponseE
	 * @return CIMResponse
	 * @throws CIMXMLParseException
	 */
	public static CIMResponse parseEXPMETHODRESPONSE(Element pExpMethodResponseE)
			throws CIMXMLParseException {
		// <!ELEMENT EXPMETHODRESPONSE (ERROR | IRETURNVALUE?)>

		CIMResponse response = new CIMResponse();

		if (attribute(pExpMethodResponseE, "NAME") == null) throw new CIMXMLParseException(
				"EXPMETHODRESPONSE element missing NAME attribute!");

		// ERROR
		Element[] errorA = searchNodes(pExpMethodResponseE, "ERROR", 0, 1, false);
		if (errorA != null) {
			WBEMException exception = parseERROR(errorA[0]);
			response.setError(exception);
			return response;
		}

		// RETURNVALUE
		Element[] retValElementA = searchNodes(pExpMethodResponseE, "IRETURNVALUE", 0, 1, false);
		if (retValElementA != null) {
			Vector<Object> v = new Vector<Object>();
			Element returnvalueE = retValElementA[0];
			v.add(parseIRETURNVALUE(returnvalueE));
			response.setReturnValue(v);
			return response;
		}

		checkOtherNodes(pExpMethodResponseE, nodesEXPMETHODRESPONSE);

		return response;
	}

	private static final String nodesCORRELATOR[] = { "VALUE" };

	/**
	 * parseCORRELATOR
	 * 
	 * @param pCorrelatorE
	 * @throws CIMXMLParseException
	 * */
	public static void parseCORRELATOR(Element pCorrelatorE) throws CIMXMLParseException {
		// <!ELEMENT CORRELATOR (VALUE)>
		// <!ATTLIST CORRELATOR %CIMName; %CIMType; #REQUIRED>
		String name = attribute(pCorrelatorE, "NAME");
		if (name == null) throw new CIMXMLParseException(
				"CORRELATOR element missing NAME attribute!");
		String type = attribute(pCorrelatorE, "TYPE");
		if (type == null) throw new CIMXMLParseException(
				"CORRELATOR element missing TYPE attribute!");

		// VALUE
		Element[] valueA = searchNodes(pCorrelatorE, "VALUE", 1, 1, false);
		if (valueA != null) {
			// TypedValue tVal = parseVALUE(valueA[0]);
		}

		checkOtherNodes(pCorrelatorE, nodesCORRELATOR);
	}
}
