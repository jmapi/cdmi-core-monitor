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
 * 1735693    2007-06-12  ebak         Empty VALUE.ARRAY elements are parsed as nulls
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 3023340    2010-07-02  blaschke-oss CIMObjectFactory uses # constructor instead of valueOf
 * 3027615    2010-07-12  blaschke-oss Use CLASS_ARRAY_T instead of new CIMDataType(CLASS,0)
 * 3513349    2012-03-31  blaschke-oss TCK: CIMDataType must not accept null string
 * 3572993    2012-10-01  blaschke-oss parseDouble("2.2250738585072012e-308") DoS vulnerability
 *    2693    2013-10-21  blaschke-oss ReturnValueNode allows invalid PARAMTYPE attribute
 */

package org.sblim.cimclient.internal.cimxml.sax;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.cim.CIMClass;
import javax.cim.CIMDataType;
import javax.cim.CIMDateTime;
import javax.cim.CIMDateTimeAbsolute;
import javax.cim.CIMDateTimeInterval;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sblim.cimclient.internal.cimxml.sax.node.ClassNode;
import org.sblim.cimclient.internal.cimxml.sax.node.InstanceNode;
import org.sblim.cimclient.internal.cimxml.sax.node.Node;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueArrayNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueNode;
import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.Util;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.xml.sax.SAXException;

/**
 * Class CIMObjectFactory is responsible for type String and value String
 * parsing.
 */
public class CIMObjectFactory {

	/**
	 * getEmbbeddedObjectA
	 * 
	 * @param pType
	 * @param pValueArrayNode
	 * @param pSession
	 * @return CIMInstance[], CIMClass[], String[] or null
	 * @throws SAXException
	 */
	public static Object[] getEmbeddedObjA(CIMDataType pType, ValueArrayNode pValueArrayNode,
			SAXSession pSession) throws SAXException {
		if (pValueArrayNode == null) return null;
		return getEmbeddedObjA(pType, (String[]) pValueArrayNode.getValue(), pSession);
	}

	/**
	 * getEmbeddedObjectA
	 * 
	 * @param pType
	 * @param pValueStrA
	 * @param pSession
	 * @return CIMInstance[], CIMClass[], String[] or null
	 * @throws SAXException
	 */
	public static Object[] getEmbeddedObjA(CIMDataType pType, String[] pValueStrA,
			SAXSession pSession) throws SAXException {
		embeddedObjTypeCheck(pType);
		if (pValueStrA == null || pValueStrA.length == 0) return null;
		CIMDataType type = null;
		ArrayList<Object> objAL = new ArrayList<Object>(pValueStrA.length);
		for (int i = 0; i < pValueStrA.length; i++) {
			Object obj = parseEmbeddedObj(pValueStrA[i], pSession);
			if (type == null) {
				type = getCIMObjScalarType(obj, false);
			} else {
				CIMDataType type2 = getCIMObjScalarType(obj, false);
				if (type2 != null && type != type2) throw new SAXException(
						"Embedded Object array contains both Instance and Class objects. "
								+ "This is not handled!");
			}
			objAL.add(obj);
		}
		if (type == CIMDataType.OBJECT_T) {
			return objAL.toArray(EMPTY_INST_A);
		} else if (type == CIMDataType.CLASS_T) { return objAL.toArray(EMPTY_CLASS_A); }
		return objAL.toArray(EMPTY_STR_A);
	}

	/**
	 * getEmbeddedObject
	 * 
	 * @param pType
	 * @param pValueStr
	 * @param pSession
	 * @return CIMInstance, CIMClass or null
	 * @throws SAXException
	 */
	public static Object getEmbeddedObj(CIMDataType pType, String pValueStr, SAXSession pSession)
			throws SAXException {
		embeddedObjTypeCheck(pType);
		return parseEmbeddedObj(pValueStr, pSession);
	}

	/**
	 * getEmbeddedObj
	 * 
	 * @param pType
	 * @param pValObj
	 * @param pSession
	 * @return Object CIMClass, CIMInstance, String, CIMClass[], CIMInstance[],
	 *         String[] or null
	 * @throws SAXException
	 */
	public static Object getEmbeddedObj(CIMDataType pType, Object pValObj, SAXSession pSession)
			throws SAXException {
		if (pValObj instanceof String) { return getEmbeddedObj(pType, (String) pValObj, pSession); }
		return getEmbeddedObjA(pType, (String[]) pValObj, pSession);
	}

	/**
	 * @param pType
	 * @param pAL
	 * @return Object[]
	 */
	public static Object[] getObjectArray(CIMDataType pType, ArrayList<Object> pAL) {
		createValFactoryA();
		// if (pType==null) pType=CIMDataType.STRING_T;
		ValueFactory factory = cValFactoryA[pType.getType()];
		return factory.make(pAL);
	}

	/**
	 * getObject
	 * 
	 * @param pType
	 * @param pValueStr
	 * @return Object
	 * @throws SAXException
	 */
	public static Object getObject(CIMDataType pType, String pValueStr) throws SAXException {
		if (pValueStr == null) return null;
		createValFactoryA();
		ValueFactory factory = cValFactoryA[pType.getType()];
		try {
			return factory.make(pValueStr);
		} catch (NumberFormatException e) {
			throw new SAXException(e);
		}
	}

	/**
	 * getObject
	 * 
	 * @param pType
	 * @param pValueNode
	 * @return Object
	 * @throws SAXException
	 */
	public static Object getObject(CIMDataType pType, ValueNode pValueNode) throws SAXException {
		if (pValueNode == null) return null;
		return getObject(pType, (String) pValueNode.getValue());
	}

	/**
	 * getObject
	 * 
	 * @param pType
	 * @param pValueArrayNode
	 * @return Object
	 * @throws SAXException
	 */
	public static Object getObject(CIMDataType pType, ValueArrayNode pValueArrayNode)
			throws SAXException {
		if (pValueArrayNode == null) return null;
		ArrayList<Object> objAL = new ArrayList<Object>(pValueArrayNode.size());
		for (int i = 0; i < pValueArrayNode.size(); i++)
			objAL.add(getObject(pType, (String) pValueArrayNode.elementAt(i)));
		return getObjectArray(pType, objAL);
	}

	/**
	 * getCIMObjType
	 * 
	 * @param pObj
	 * @param pNullToString
	 * @return CIMDataType OBJECT_T, CLASS_T, STRING_T
	 * @throws SAXException
	 */
	public static CIMDataType getCIMObjScalarType(Object pObj, boolean pNullToString)
			throws SAXException {
		if (pObj == null) return pNullToString ? CIMDataType.STRING_T : null;
		if (pObj instanceof CIMInstance) {
			return CIMDataType.OBJECT_T;
		} else if (pObj instanceof CIMClass) {
			return CIMDataType.CLASS_T;
		} else if (pObj instanceof String) { return CIMDataType.STRING_T; }
		throw new SAXException(pObj.getClass().getName() + " is not a CIMObject!");
	}

	/**
	 * getCIMObjType
	 * 
	 * @param pObj
	 * @return CIMDataType OBJECT_T, OBJECT_ARRAY_T
	 * @throws SAXException
	 */
	public static CIMDataType getCIMObjScalarType(Object pObj) throws SAXException {
		return getCIMObjScalarType(pObj, true);
	}

	/**
	 * getCIMObjArrayType
	 * 
	 * @param pObj
	 * @return CIMDataType: OBJECT_ARRAY_T, CLASS_T array, STRING_ARRAY_T
	 * @throws SAXException
	 */
	public static CIMDataType getCIMObjArrayType(Object pObj) throws SAXException {
		return getCIMObjArrayType(pObj, true);
	}

	/**
	 * getArrayCIMObjType
	 * 
	 * @param pObj
	 * @param pNullToString
	 * @return CIMDataType: OBJECT_ARRAY_T, CLASS_T array, STRING_ARRAY_T or
	 *         null
	 * @throws SAXException
	 */
	public static CIMDataType getCIMObjArrayType(Object pObj, boolean pNullToString)
			throws SAXException {
		if (pObj == null) return pNullToString ? CIMDataType.STRING_ARRAY_T : null;
		if (pObj instanceof CIMInstance[]) {
			return CIMDataType.OBJECT_ARRAY_T;
		} else if (pObj instanceof CIMClass[]) {
			return CIMDataType.CLASS_ARRAY_T;
		} else if (pObj instanceof String[]) { return CIMDataType.STRING_ARRAY_T; }
		throw new SAXException(pObj.getClass().getName() + " is not a CIMObject array!");
	}

	/**
	 * getType
	 * 
	 * @param pTypeStr
	 * @return CIMDataType
	 * @throws SAXException
	 */
	public static CIMDataType getType(String pTypeStr) throws SAXException {
		if (pTypeStr == null) return null;
		createTypeStrMap();
		CIMDataType type = cTypeStrMap.get(pTypeStr);
		if (type == null && !cTypeStrMap.containsKey(pTypeStr)) throw new SAXException(pTypeStr
				+ " is invalid PARAMTYPE!");
		return type;
	}

	static final CIMInstance[] EMPTY_INST_A = new CIMInstance[0];

	static final CIMClass[] EMPTY_CLASS_A = new CIMClass[0];

	static final String[] EMPTY_STR_A = new String[0];

	static final UnsignedInteger8[] EMPTY_UINT8_A = new UnsignedInteger8[0];

	static final UnsignedInteger16[] EMPTY_UINT16_A = new UnsignedInteger16[0];

	static final UnsignedInteger32[] EMPTY_UINT32_A = new UnsignedInteger32[0];

	static final UnsignedInteger64[] EMPTY_UINT64_A = new UnsignedInteger64[0];

	static final Byte[] EMPTY_BYTE_A = new Byte[0];

	static final Short[] EMPTY_SHORT_A = new Short[0];

	static final Integer[] EMPTY_INT_A = new Integer[0];

	static final Long[] EMPTY_LONG_A = new Long[0];

	static final Float[] EMPTY_FLOAT_A = new Float[0];

	static final Double[] EMPTY_DOUBLE_A = new Double[0];

	static final Character[] EMPTY_CHAR_A = new Character[0];

	static final Boolean[] EMPTY_BOOL_A = new Boolean[0];

	static final CIMDateTime[] EMPTY_DT_A = new CIMDateTime[0];

	static final CIMObjectPath[] EMPTY_OP_A = new CIMObjectPath[0];

	private static void embeddedObjTypeCheck(CIMDataType pType) throws SAXException {
		if (pType.getType() != CIMDataType.STRING) throw new SAXException(
				"TYPE attribute should be 'string' for EmbeddedObjects!");
	}

	private static Object parseEmbeddedObj(String pValueStr, SAXSession pSession)
			throws SAXException {
		if (pValueStr == null || pValueStr.length() == 0) return null;
		XMLDefaultHandlerImpl ourHandler = new XMLDefaultHandlerImpl(pSession, true);
		// XML String of embedded Object is parsed by the SAX parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new ByteArrayInputStream(pValueStr.getBytes()), ourHandler);
		} catch (SAXException se) {
			throw se;
		} catch (Exception e) {
			throw new SAXException("Exception occurred during embedded object parsing!", e);
		}
		Node node = ourHandler.getRootNode();
		if (node instanceof InstanceNode) { return ((InstanceNode) node).getCIMInstance(); }
		if (node instanceof ClassNode) { return ((ClassNode) node).getCIMClass(); }
		throw new SAXException(node.getNodeName()
				+ " root element is unexpected for Embedded Object XML String!");
	}

	private static HashMap<String, CIMDataType> cTypeStrMap;

	private synchronized static void createTypeStrMap() {
		if (cTypeStrMap != null) return;
		cTypeStrMap = new HashMap<String, CIMDataType>();
		cTypeStrMap.put(MOF.DT_UINT8, CIMDataType.UINT8_T);
		cTypeStrMap.put(MOF.DT_UINT16, CIMDataType.UINT16_T);
		cTypeStrMap.put(MOF.DT_UINT32, CIMDataType.UINT32_T);
		cTypeStrMap.put(MOF.DT_UINT64, CIMDataType.UINT64_T);
		cTypeStrMap.put(MOF.DT_SINT8, CIMDataType.SINT8_T);
		cTypeStrMap.put(MOF.DT_SINT16, CIMDataType.SINT16_T);
		cTypeStrMap.put(MOF.DT_SINT32, CIMDataType.SINT32_T);
		cTypeStrMap.put(MOF.DT_SINT64, CIMDataType.SINT64_T);
		cTypeStrMap.put(MOF.DT_REAL32, CIMDataType.REAL32_T);
		cTypeStrMap.put(MOF.DT_REAL64, CIMDataType.REAL64_T);
		cTypeStrMap.put(MOF.DT_CHAR16, CIMDataType.CHAR16_T);
		cTypeStrMap.put(MOF.DT_STR, CIMDataType.STRING_T);
		cTypeStrMap.put(MOF.DT_BOOL, CIMDataType.BOOLEAN_T);
		cTypeStrMap.put(MOF.DT_DATETIME, CIMDataType.DATETIME_T);
		cTypeStrMap.put(MOF.REFERENCE, new CIMDataType(""));
	}

	private interface ValueFactory {

		/**
		 * make
		 * 
		 * @param pStr
		 * @return Object
		 */
		Object make(String pStr);

		/**
		 * make
		 * 
		 * @param pAL
		 * @return Object[]
		 */
		Object[] make(ArrayList<Object> pAL);
	}

	private static ValueFactory[] cValFactoryA;

	private static void putFactory(int pTypeCode, ValueFactory pFactory) {
		cValFactoryA[pTypeCode] = pFactory;
	}

	private synchronized static void createValFactoryA() {
		if (cValFactoryA != null) return;
		cValFactoryA = new ValueFactory[64];
		// unsigned integers
		putFactory(CIMDataType.UINT8, new ValueFactory() {

			public Object make(String pStr) {
				return new UnsignedInteger8(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_UINT8_A);
			}
		});
		putFactory(CIMDataType.UINT16, new ValueFactory() {

			public Object make(String pStr) {
				return new UnsignedInteger16(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_UINT16_A);
			}
		});
		putFactory(CIMDataType.UINT32, new ValueFactory() {

			public Object make(String pStr) {
				return new UnsignedInteger32(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_UINT32_A);
			}
		});
		putFactory(CIMDataType.UINT64, new ValueFactory() {

			public Object make(String pStr) {
				return new UnsignedInteger64(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_UINT64_A);
			}
		});
		// signed integers
		putFactory(CIMDataType.SINT8, new ValueFactory() {

			public Object make(String pStr) {
				return new Byte(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_BYTE_A);
			}
		});
		putFactory(CIMDataType.SINT16, new ValueFactory() {

			public Object make(String pStr) {
				return new Short(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_SHORT_A);
			}
		});
		putFactory(CIMDataType.SINT32, new ValueFactory() {

			public Object make(String pStr) {
				return new Integer(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_INT_A);
			}
		});
		putFactory(CIMDataType.SINT64, new ValueFactory() {

			public Object make(String pStr) {
				return new Long(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_LONG_A);
			}
		});
		// floats
		putFactory(CIMDataType.REAL32, new ValueFactory() {

			public Object make(String pStr) {
				return new Float(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_FLOAT_A);
			}
		});
		putFactory(CIMDataType.REAL64, new ValueFactory() {

			public Object make(String pStr) {
				if (WBEMConfiguration.getGlobalConfiguration().verifyJavaLangDoubleStrings()) {
					if (Util.isBadDoubleString(pStr)) throw new IllegalArgumentException(
							"Double value string hangs older JVMs!\n" + pStr);
				}
				return new Double(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_DOUBLE_A);
			}
		});
		// char
		putFactory(CIMDataType.CHAR16, new ValueFactory() {

			public Object make(String pStr) {
				if (pStr == null || pStr.length() == 0) throw new IllegalArgumentException(
						"Cannot make Character from empty String!");
				return Character.valueOf(pStr.charAt(0));
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_CHAR_A);
			}
		});
		// string
		putFactory(CIMDataType.STRING, new ValueFactory() {

			public Object make(String pStr) {
				return pStr;
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_STR_A);
			}
		});
		// boolean
		putFactory(CIMDataType.BOOLEAN, new ValueFactory() {

			public Object make(String pStr) {
				return Boolean.valueOf(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_BOOL_A);
			}
		});
		// datetime
		putFactory(CIMDataType.DATETIME, new ValueFactory() {

			public Object make(String pStr) {
				try {
					return new CIMDateTimeAbsolute(pStr);
				} catch (IllegalArgumentException eA) {
					try {
						return new CIMDateTimeInterval(pStr);
					} catch (IllegalArgumentException eI) {
						throw new IllegalArgumentException("CIMDataTimeAbsolute:" + eA.getMessage()
								+ "\nCIMDateTimeInterval:" + eI.getMessage());
					}
				}
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_DT_A);
			}
		});
		// reference
		putFactory(CIMDataType.REFERENCE, new ValueFactory() {

			public Object make(String pStr) {
				return new CIMObjectPath(pStr);
			}

			public Object[] make(ArrayList<Object> pAL) {
				return pAL.toArray(EMPTY_OP_A);
			}
		});
	}

}
