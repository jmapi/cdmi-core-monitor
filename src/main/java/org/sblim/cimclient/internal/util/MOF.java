/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 * 1565892    2006-11-06  ebak         Make SBLIM client JSR48 compliant
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 1736318    2007-06-13  lupusalex    Wrong object path in HTTP header
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2791860    2009-05-14  blaschke-oss Export instance to mof, wrong syntax
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3185824    2011-02-18  blaschke-oss Char16 definition includes whitespace
 * 3510090    2012-03-23  blaschke-oss Fix CIMObjectPath.toString() inconsistencies
 * 3529151    2012-08-22  blaschke-oss TCK: CIMInstance property APIs include keys from COP
 */

package org.sblim.cimclient.internal.util;

import java.util.Comparator;
import java.util.TreeMap;

import javax.cim.CIMClass;
import javax.cim.CIMDataType;
import javax.cim.CIMDateTime;
import javax.cim.CIMElement;
import javax.cim.CIMFlavor;
import javax.cim.CIMInstance;
import javax.cim.CIMMethod;
import javax.cim.CIMObjectPath;
import javax.cim.CIMParameter;
import javax.cim.CIMProperty;
import javax.cim.CIMQualifiedElementInterface;
import javax.cim.CIMQualifier;
import javax.cim.CIMQualifierType;
import javax.cim.CIMScope;
import javax.cim.CIMTypedElement;
import javax.cim.CIMValuedElement;

/**
 * Class MOF is responsible for helping MOF String generation.
 */
public class MOF {

	/**
	 * CLASS
	 */
	public static final String CLASS = "class";

	/**
	 * TRUE
	 */
	public static final String TRUE = "true";

	/**
	 * FALSE
	 */
	public static final String FALSE = "false";

	/**
	 * NULL
	 */
	public static final String NULL = "null";

	/**
	 * ENABLEOVERRIDE
	 */
	public static final String ENABLEOVERRIDE = "enableoverride";

	/**
	 * DISABLEOVERRIDE
	 */
	public static final String DISABLEOVERRIDE = "disableOverride";

	/**
	 * RESTRICTED
	 */
	public static final String RESTRICTED = "restricted";

	/**
	 * TOSUBCLASS
	 */
	public static final String TOSUBCLASS = "tosubclass";

	/**
	 * TRANSLATABLE
	 */
	public static final String TRANSLATABLE = "translatable";

	/**
	 * DT_BOOL
	 */
	public static final String DT_BOOL = "boolean";

	/**
	 * DT_CHAR16
	 */
	public static final String DT_CHAR16 = "char16";

	/**
	 * DT_DATETIME
	 */
	public static final String DT_DATETIME = "datetime";

	/**
	 * DT_REAL32
	 */
	public static final String DT_REAL32 = "real32";

	/**
	 * DT_REAL64
	 */
	public static final String DT_REAL64 = "real64";

	/**
	 * DT_SINT16
	 */
	public static final String DT_SINT16 = "sint16";

	/**
	 * DT_SINT32
	 */
	public static final String DT_SINT32 = "sint32";

	/**
	 * DT_SINT64
	 */
	public static final String DT_SINT64 = "sint64";

	/**
	 * DT_SINT8
	 */
	public static final String DT_SINT8 = "sint8";

	/**
	 * DT_STR
	 */
	public static final String DT_STR = "string";

	/**
	 * DT_UINT16
	 */
	public static final String DT_UINT16 = "uint16";

	/**
	 * DT_UINT32
	 */
	public static final String DT_UINT32 = "uint32";

	/**
	 * DT_UINT64
	 */
	public static final String DT_UINT64 = "uint64";

	/**
	 * DT_UINT8
	 */
	public static final String DT_UINT8 = "uint8";

	/**
	 * INVALID
	 */
	public static final String INVALID = "invalid";

	/**
	 * REF
	 */
	public static final String REF = "ref";

	/**
	 * INSTOF
	 */
	public static final String INSTOF = "instance of ";

	/**
	 * QUALIFIER
	 */
	public static final String QUALIFIER = "qualifier";

	/**
	 * SCOPE
	 */
	public static final String SCOPE = "scope";

	/**
	 * ANY
	 */
	public static final String ANY = "any";

	/**
	 * ASSOCIATION
	 */
	public static final String ASSOCIATION = "association";

	/**
	 * INDICATION
	 */
	public static final String INDICATION = "indication";

	/**
	 * METHOD
	 */
	public static final String METHOD = "method";

	/**
	 * PARAMETER
	 */
	public static final String PARAMETER = "parameter";

	/**
	 * PROPERTY
	 */
	public static final String PROPERTY = "property";

	/**
	 * REFERENCE
	 */
	public static final String REFERENCE = "reference";

	/**
	 * INSTANCE
	 */
	public static final String INSTANCE = "instance";

	/**
	 * NAMESPACE
	 */
	public static final String NAMESPACE = "namespace";

	/**
	 * INDENT
	 */
	public static final String INDENT = "  ";

	/**
	 * EMPTY
	 */
	public static final String EMPTY = "";

	// TODO: formatter problem: " -> &quot; Is it necessary in <pre> section?

	/**
	 * <pre>
	 *        classDeclaration		=	[ qualifierList ] CLASS className [ superClass ]
	 *       					&quot;{&quot; *classFeature &quot;}&quot; &quot;;&quot;
	 *        classFeature			=	propertyDeclaration | methodDeclaration
	 *        
	 *        superClass				=	&quot;:&quot; className
	 * </pre>
	 * 
	 * @param pCl
	 * @param pInd
	 * @return the MOF string
	 */
	public static String classDeclaration(CIMClass pCl, String pInd) {
		// for associator classes an other function should be called ?
		StringBuffer buf = new StringBuffer();
		buf.append(qualifierList(pCl, pInd));
		buf.append(CLASS + ' ' + pCl.getName() + ' ');
		String supCl = pCl.getSuperClassName();
		if (supCl != null) buf.append(':' + supCl + ' ');
		buf.append("{\n");
		// *classFeature
		for (int i = 0; i < pCl.getPropertyCount(); i++)
			buf.append(propertyDeclaration(pCl.getProperty(i), pInd + INDENT));
		for (int i = 0; i < pCl.getMethodCount(); i++)
			buf.append(methodDeclaration(pCl.getMethod(i), pInd + INDENT));
		buf.append("};");
		return buf.toString();
	}

	/**
	 * instanceDeclaration = [ qualifierList ] INSTANCE OF className [ alias ]
	 * "{" 1*valueInitializer "}" ";"
	 * 
	 * @param pInst
	 * @param pInd
	 * @return the MOF string
	 */
	public static String instanceDeclaration(CIMInstance pInst, String pInd) {
		// TODO: Why is there qualifierList in the BNF? CIMInstance doesn't have
		// qualifiers
		// TODO: What is the alias? How to use?
		StringBuffer buf = new StringBuffer();
		buf.append(pInd + INSTOF + pInst.getClassName() + " {\n");
		// Add all properties in prop array
		for (int i = 0; i < pInst.getPropertyCount(); i++)
			buf.append(valueInitializer(pInst.getProperty(i), pInd + INDENT));
		// Add keys that are NOT also in prop array
		CIMProperty<?>[] keys = pInst.getKeys();
		for (int i = 0; i < keys.length; i++) {
			if (pInst.getProperty(keys[i].getName()) == null) {
				buf.append(valueInitializer(keys[i], pInd + INDENT));
			}
		}
		buf.append(pInd + "};");
		return buf.toString();
	}

	/**
	 * <pre>
	 *        methodDeclaration		=	[ qualifierList ] dataType methodName
	 *       							&quot;(&quot; [ parameterList ] &quot;)&quot; &quot;;&quot;
	 * </pre>
	 * 
	 * @param pMethod
	 * @param pInd
	 * @return the MOF string
	 */
	public static String methodDeclaration(CIMMethod<?> pMethod, String pInd) {
		StringBuffer buf = new StringBuffer();
		buf.append(qualifierList(pMethod, pInd));
		buf.append(pInd + dataType(pMethod) + ' ' + pMethod.getName() + '(');
		if (pMethod.getParameters().length > 0) {
			buf.append('\n');
			buf.append(parameterList(pMethod, pInd + INDENT) + '\n' + pInd);
		}
		buf.append(");\n");
		return buf.toString();
	}

	/**
	 * parameter
	 * 
	 * @param pParam
	 *            - CIMParameter
	 * @param pInd
	 * @return the MOF string
	 */
	public static String parameter(CIMParameter<?> pParam, String pInd) {
		return typedElement(pParam, pInd);
	}

	/**
	 * 
	 * typedElement = [ qualifierList ] (dataType|objectRef) parameterName [
	 * array ]
	 * 
	 * @param pTypedElement
	 * @param pInd
	 * @return the MOF string
	 */
	public static String typedElement(CIMTypedElement pTypedElement, String pInd) {
		return qualifierList(pTypedElement, pInd) + pInd + dataType(pTypedElement) + ' '
				+ pTypedElement.getName() + array(pTypedElement);
	}

	/**
	 * <pre>
	 *        valuedElement 		=	typedElement [ defaultValue ] &quot;;&quot;
	 *        defaultValue			=	&quot;=&quot; initializer
	 * </pre>
	 * 
	 * @param pValuedElement
	 * @param pInd
	 * @return the MOF string
	 */
	public static String valuedElement(CIMValuedElement<?> pValuedElement, String pInd) {
		StringBuffer buf = new StringBuffer(typedElement(pValuedElement, pInd));
		if (pValuedElement.getValue() != null) buf.append(defaultValue(pValuedElement, pInd));
		buf.append(';');
		return buf.toString();
	}

	/**
	 * <pre>
	 *        qualifierList			=	&quot;[&quot; qualifier *( &quot;,&quot; qualifier ) &quot;]&quot;
	 * </pre>
	 * 
	 * @param pElement
	 * @param pInd
	 * @return the MOF string +newLine if qualifiers present or empty string
	 */
	public static String qualifierList(CIMElement pElement, String pInd) {
		if (!(pElement instanceof CIMQualifiedElementInterface)) return "";
		CIMQualifiedElementInterface qualified = (CIMQualifiedElementInterface) pElement;
		if (qualified.getQualifierCount() == 0) return "";
		StringBuffer buf = new StringBuffer(pInd + '[');
		for (int i = 0; i < qualified.getQualifierCount(); i++) {
			if (i > 0) buf.append(",\n" + pInd);
			buf.append(qualifier(qualified.getQualifier(i)));
		}
		buf.append("]\n");
		return buf.toString();
	}

	/**
	 * <pre>
	 *        qualifierDeclaration	=	QUALIFIER qualifierName qualifierType scope 
	 *        							[ defaultFlavor ] &quot;;&quot;
	 * </pre>
	 * 
	 * @param pQType
	 * @return the MOF string
	 */
	public static String qualifierDeclaration(CIMQualifierType<?> pQType) {
		return QUALIFIER + ' ' + pQType.getName() + ' ' + dataType(pQType) + ' '
				+ scope(pQType.getScope()) + ' ' + flavor(pQType.getFlavor()) + ';';
	}

	/**
	 * <pre>
	 *        scope		=	&quot;,&quot; SCOPE &quot;(&quot; metaElement *( &quot;,&quot; metaElement ) &quot;)&quot;
	 *        metaElement	=	CLASS | ASSOCIATION | INDICATION | QUALIFIER
	 *       					PROPERTY | REFERENCE | METHOD | PARAMETER | ANY
	 * </pre>
	 * 
	 * @param pScopes
	 * @return the MOF string
	 */
	public static String scope(int pScopes) {
		ScopeBuffer buf = new ScopeBuffer(pScopes);
		buf.append(SCOPE + "(");
		if (pScopes == CIMScope.ANY) {
			buf.append(ANY);
		} else {
			buf.append(CIMScope.ASSOCIATION, ASSOCIATION);
			buf.append(CIMScope.CLASS, CLASS);
			buf.append(CIMScope.INDICATION, INDICATION);
			buf.append(CIMScope.METHOD, METHOD);
			buf.append(CIMScope.PARAMETER, PARAMETER);
			buf.append(CIMScope.PROPERTY, PROPERTY);
			buf.append(CIMScope.REFERENCE, REFERENCE);
		}
		buf.append(')');
		return buf.toString();
	}

	/**
	 * OVERRIDABLE (true|false) 'true' -> DISABLEOVERRIDE=false TOSUBCLASS
	 * (true|false) 'true' -> RESTRICTED=false TOINSTANCE (true|false) 'false'
	 * TRANSLATABLE (true|false) 'false' -> TRANSLTE=false
	 */
	public static final int DEF_FLAVOR = 0;

	/**
	 * <pre>
	 * flavor = ENABLEOVERRIDE | DISABLEOVERRIDE | RESTRICTED | TOSUBCLASS | TRANSLATABLE
	 * </pre>
	 * 
	 * @param flavor
	 * @return the MOF string
	 */
	public static String flavor(int flavor) {
		// flavors = { DISABLEOVERRIDE, RESTRICTED, TRANSLATE }
		// !DISABLEOVERRIDE = ENABLEOVERRIDE
		// !RESTRICTED = TOSUBCLASS
		// !TRANSLATE = ?
		StringBuffer buf = new StringBuffer();
		buf.append(((flavor & CIMFlavor.DISABLEOVERRIDE) > 0 ? DISABLEOVERRIDE : ENABLEOVERRIDE));
		buf.append(' ' + ((flavor & CIMFlavor.RESTRICTED) > 0 ? RESTRICTED : TOSUBCLASS));
		if ((flavor & CIMFlavor.TRANSLATE) > 0) buf.append(' ' + TRANSLATABLE);
		return buf.toString();
	}

	/**
	 * <pre>
	 *        qualifier				=	qualifierName [ qualifierParameter ] [ &quot;:&quot; 1*flavor ]
	 * </pre>
	 * 
	 * @param pQuali
	 * @return the MOF string
	 */
	public static String qualifier(CIMQualifier<?> pQuali) {
		StringBuffer buf = new StringBuffer();
		buf.append(pQuali.getName());
		// TODO: review. Why qualifierParameter is optional?
		if (pQuali.getDataType() != null && pQuali.getValue() != null) buf
				.append(qualifierParameter(pQuali));
		// FIXME: why flavors are optional
		// if (pQuali.getFlavor() != 0)
		buf.append(':' + flavor(pQuali.getFlavor()));
		return buf.toString();
	}

	/**
	 * <pre>
	 *        propertyDeclaration	=	typedElement [ defaultValue ] &quot;;&quot;
	 *        defaultValue			=	&quot;=&quot; initializer
	 * </pre>
	 * 
	 * @param pProp
	 * @param pInd
	 * @return the MOF string
	 */
	public static String propertyDeclaration(CIMProperty<?> pProp, String pInd) {
		return valuedElement(pProp, pInd) + '\n';
	}

	/**
	 * <pre>
	 *        valueInitializer		= [ qualifierList ] ( propertyName | referenceName ) &quot;=&quot;
	 * 									initializer &quot;;&quot;
	 * </pre>
	 * 
	 * @param pProp
	 * @param pInd
	 * @return the MOF string
	 */
	public static String valueInitializer(CIMProperty<?> pProp, String pInd) {
		Object value = pProp.getValue();
		if (value != null) { return qualifierList(pProp, pInd) + pInd + pProp.getName() + " = "
				+ initializer(value, pInd) + ";\n"; }
		return "";
	}

	/**
	 * <pre>
	 * dataType = DT_UINT8 | DT_SINT8 | DT_UINT16 | DT_SINT16 | DT_UINT32 | DT_SINT32 | DT_UINT64
	 * 		| DT_SINT64 | DT_REAL32 | DT_REAL64 | DT_CHAR16 | DT_STR | DT_BOOL | DT_DATETIME
	 * </pre>
	 * 
	 * Additionally it handles reference types too.
	 * 
	 * <pre>
	 *        objectType	=	objectRef
	 *        objectRef		=	className REF
	 * </pre>
	 * 
	 * @param pType
	 * @return the MOF string
	 */
	public static String dataType(CIMDataType pType) {
		if (pType.getType() == CIMDataType.REFERENCE) return pType.getRefClassName() + ' ' + REF;
		String res = DATATYPE_MAP.get(pType);
		if (res == null) {
			String msg = "This function cannot handle typeCode: " + pType.getType() + "!";
			// TODO: tracing
			throw new IllegalArgumentException(msg);
		}
		return res;
	}

	/**
	 * @param pTypedElement
	 * @return the MOF string
	 * @see #dataType(CIMDataType)
	 */
	public static String dataType(CIMTypedElement pTypedElement) {
		return dataType(pTypedElement.getDataType());
	}

	/**
	 * objectHandle
	 * 
	 * @param pPath
	 * @return the Untyped MOF String
	 */
	public static String objectHandle(CIMObjectPath pPath) {
		return objectHandle(pPath, false, false);
	}

	/**
	 * objectHandle
	 * 
	 * @param pPath
	 *            The path
	 * @param pTyped
	 *            If true the URI is typed
	 * @param pLocal
	 *            If true the path is local (omitting scheme, host and port)
	 * @return the URI string
	 */
	public static String objectHandle(CIMObjectPath pPath, boolean pTyped, boolean pLocal) {
		StringBuffer buf = new StringBuffer();
		if (!pLocal) {
			if (pPath.getScheme() != null) buf.append(pPath.getScheme() + ':');
			if (pPath.getHost() != null) buf.append("//" + pPath.getHost());
			if (pPath.getPort() != null) buf.append(':' + pPath.getPort());
		}
		if (pPath.getNamespace() != null) {
			if (!pLocal) buf.append('/');
			buf.append(pPath.getNamespace());
		}
		if (pTyped) buf.append(pathType(pPath));
		else if (pPath.getObjectName() != null && buf.length() > 0) {
			buf.append(':');
		}
		if (pPath.getObjectName() != null) {
			buf.append(pPath.getObjectName() + keyValuePairs(pPath.getKeys(), pTyped, EMPTY));
		}
		return buf.toString();
	}

	/**
	 * objectHandleAsRef
	 * 
	 * @param pPath
	 * @return the Untyped MOF String
	 */
	public static String objectHandleAsRef(CIMObjectPath pPath) {
		return objectHandleAsRef(pPath, false);
	}

	/**
	 * objectHandleAsRef
	 * 
	 * @param pPath
	 * @param pTyped
	 * @return the MOF String
	 */
	public static String objectHandleAsRef(CIMObjectPath pPath, boolean pTyped) {
		StringBuffer buf = new StringBuffer();
		if (pPath.getNamespace() != null) buf.append(pPath.getNamespace());
		if (pTyped) buf.append(pathType(pPath));
		if (pPath.getObjectName() != null) {
			if (!pTyped && pPath.getNamespace() != null) buf.append(':');
			buf.append(pPath.getObjectName());
			buf.append(keyValuePairs(pPath.getKeys(), pTyped, EMPTY));
		}
		return buf.toString();
	}

	/**
	 * <pre>
	 * constantValue = integerValue | realValue | charValue | stringValue | booleanValue | nullValue
	 * // | dateTimeValue | objectHandle
	 * </pre>
	 * 
	 * @param pValuedElement
	 * @param pInd
	 * @return the MOF string
	 */
	public static String constantValue(CIMValuedElement<?> pValuedElement, String pInd) {
		return constantValue(pValuedElement.getValue(), pInd);
	}

	/**
	 * constantValue
	 * 
	 * @param pObj
	 * @param pTyped
	 * @param pInd
	 * @return the MOF string
	 * @see #constantValue(CIMValuedElement, String)
	 */
	public static String constantValue(Object pObj, boolean pTyped, String pInd) {
		String valStr;
		if (pObj == null) {
			valStr = NULL;
		} else if (pObj instanceof CIMObjectPath) {
			valStr = Util.quote(objectHandleAsRef((CIMObjectPath) pObj, pTyped));
		} else if (pObj instanceof String) {
			valStr = Util.quote(pObj.toString());
		} else if (pObj instanceof CIMDateTime) {
			valStr = Util.quote(pObj.toString());
		} else if (pObj instanceof Character) {
			valStr = charValue((Character) pObj);
		} else if (pObj instanceof Boolean) {
			valStr = ((Boolean) pObj).booleanValue() ? TRUE : FALSE;
		} else if (pObj instanceof CIMInstance) {
			valStr = '\n' + instanceDeclaration((CIMInstance) pObj, pInd) + '\n';
		} else if (pObj instanceof CIMClass) {
			valStr = '\n' + classDeclaration((CIMClass) pObj, pInd) + '\n' + pInd;
		} else {
			valStr = pObj.toString();
		}
		if (pTyped) {
			String typeStr;
			if (pObj instanceof CIMObjectPath) {
				typeStr = REFERENCE;
			} else {
				CIMDataType type = CIMDataType.getDataType(pObj);
				typeStr = dataType(type);
			}
			return "(" + typeStr + ")" + valStr;
		}
		return valStr;
	}

	/**
	 * constantValue
	 * 
	 * @param pObj
	 * @param pInd
	 * @return the untyped MOF String
	 */
	public static String constantValue(Object pObj, String pInd) {
		return constantValue(pObj, false, pInd);
	}

	private static String pathType(CIMObjectPath pPath) {
		String typeInfo;
		if (pPath.getKeys().length > 0) {
			typeInfo = INSTANCE;
		} else if (pPath.getObjectName() != null) {
			typeInfo = CLASS;
		} else {
			typeInfo = NAMESPACE;
		}
		return "/(" + typeInfo + ")";
	}

	private static String keyValuePairs(CIMProperty<?>[] pKeys, boolean pTyped, String pInd) {
		StringBuffer buf = new StringBuffer();
		if (pKeys != null && pKeys.length > 0) {
			buf.append('.');
			for (int i = 0; i < pKeys.length; i++) {
				CIMProperty<?> key = pKeys[i];
				if (i != 0) buf.append(',');
				buf.append(key.getName() + '=' + constantValue(key.getValue(), pTyped, pInd));
			}
		}
		return buf.toString();
	}

	/**
	 * <pre>
	 *        parameterList		=	parameter *( &quot;,&quot; parameter )
	 *        parameter			=	[ qualifierList ] (dataType|objectRef) parameterName [ array ]
	 * </pre>
	 * 
	 * @param pMethod
	 * @return the MOF string
	 */
	private static String parameterList(CIMMethod<?> pMethod, String pInd) {
		StringBuffer buf = new StringBuffer();
		CIMParameter<?>[] params = pMethod.getParameters();
		for (int i = 0; i < params.length; i++) {
			if (i > 0) buf.append(",\n");
			buf.append(parameter(params[i], pInd));
		}
		return buf.toString();
	}

	private static class ScopeBuffer {

		private StringBuffer buf = new StringBuffer();

		private int iScopes;

		private int iCnt = 0;

		/**
		 * Ctor.
		 * 
		 * @param pScopes
		 */
		public ScopeBuffer(int pScopes) {
			this.iScopes = pScopes;
		}

		/**
		 * append
		 * 
		 * @param pStr
		 */
		public void append(String pStr) {
			this.buf.append(pStr);
		}

		/**
		 * append
		 * 
		 * @param pChar
		 */
		public void append(char pChar) {
			this.buf.append(pChar);
		}

		/**
		 * append
		 * 
		 * @param pMask
		 * @param pName
		 */
		public void append(int pMask, String pName) {
			if ((this.iScopes & pMask) > 0) {
				if (this.iCnt++ > 0) this.buf.append(',');
				this.buf.append(pName);
			}
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.buf.toString();
		}
	}

	/**
	 * <pre>
	 *        qualifierParameter		=	&quot;(&quot; constantValue &quot;)&quot; | arrayInitializer
	 * </pre>
	 * 
	 * @param pQuali
	 * @return the MOF string
	 */
	private static String qualifierParameter(CIMQualifier<?> pQuali) {
		return pQuali.getDataType().isArray() ? arrayInitializer(pQuali, EMPTY)
				: '(' + constantValue(pQuali, EMPTY) + ')';
	}

	private static String charValue(Character pChar) {
		char ch = pChar.charValue();
		if (ch < 32) { return "'\\x" + Integer.toString(ch, 16) + '\''; }
		return "'" + ch + '\'';
	}

	/**
	 * <pre>
	 *        arrayInitializer		=	&quot;{&quot; constantValue*( &quot;,&quot; constantValue)&quot;}&quot;
	 * </pre>
	 * 
	 * @param pValue
	 * @param pInd
	 * @return the MOF string or empty string if the array has no elements or
	 *         null
	 */
	private static String arrayInitializer(Object pValue, String pInd) {
		if (pValue == null) return "";
		Object[] vA = (Object[]) pValue;
		StringBuffer buf = new StringBuffer();
		buf.append('{');
		for (int i = 0; i < vA.length; i++) {
			if (i > 0) buf.append(", ");
			buf.append(constantValue(vA[i], pInd));
		}
		buf.append('}');
		return buf.toString();
	}

	/**
	 * @param pValuedElement
	 * @param pInd
	 * @return the MOF string or empty string if the value is null
	 * @see #arrayInitializer(Object)
	 */
	private static String arrayInitializer(CIMValuedElement<?> pValuedElement, String pInd) {
		return arrayInitializer(pValuedElement.getValue(), pInd);
	}

	/**
	 * array = "[" [positiveDecimalValue] "]"
	 * 
	 * @param pType
	 * @return the MOF string or empty string if not array type
	 */
	private static String array(CIMDataType pType) {
		if (!pType.isArray()) return "";
		if (pType.getSize() > 0) return "[" + pType.getSize() + ']';
		return "[]";
	}

	/**
	 * @param pTypedElement
	 * @return the MOF string or empty string if not array type
	 * @see #array(CIMDataType)
	 */
	private static String array(CIMTypedElement pTypedElement) {
		return array(pTypedElement.getDataType());
	}

	private static final TreeMap<CIMDataType, String> DATATYPE_MAP = new TreeMap<CIMDataType, String>(
			new Comparator<Object>() {

				public int compare(Object pO1, Object pO2) {
					CIMDataType t1 = (CIMDataType) pO1;
					CIMDataType t2 = (CIMDataType) pO2;
					return t1.getType() - t2.getType();
				}
			});

	static {
		DATATYPE_MAP.put(CIMDataType.UINT8_T, DT_UINT8);
		DATATYPE_MAP.put(CIMDataType.UINT16_T, DT_UINT16);
		DATATYPE_MAP.put(CIMDataType.UINT32_T, DT_UINT32);
		DATATYPE_MAP.put(CIMDataType.UINT64_T, DT_UINT64);
		DATATYPE_MAP.put(CIMDataType.SINT8_T, DT_SINT8);
		DATATYPE_MAP.put(CIMDataType.SINT16_T, DT_SINT16);
		DATATYPE_MAP.put(CIMDataType.SINT32_T, DT_SINT32);
		DATATYPE_MAP.put(CIMDataType.SINT64_T, DT_SINT64);
		DATATYPE_MAP.put(CIMDataType.REAL32_T, DT_REAL32);
		DATATYPE_MAP.put(CIMDataType.REAL64_T, DT_REAL64);
		DATATYPE_MAP.put(CIMDataType.CHAR16_T, DT_CHAR16);
		DATATYPE_MAP.put(CIMDataType.STRING_T, DT_STR);
		DATATYPE_MAP.put(CIMDataType.BOOLEAN_T, DT_BOOL);
		DATATYPE_MAP.put(CIMDataType.DATETIME_T, DT_DATETIME);
		// FIXME: What to do with those types which are not specified by MOF's
		// BNF?
		DATATYPE_MAP.put(CIMDataType.INVALID_T, INVALID);
		// FIXME: What is the string representation of OBJECT?
		DATATYPE_MAP.put(CIMDataType.OBJECT_T, "object");
		DATATYPE_MAP.put(CIMDataType.CLASS_T, CLASS);
	}

	/**
	 * defaultValue = "=" initializer
	 * 
	 * @param pValuedElement
	 * @param pInd
	 * @return the MOF string or empty string if the value is null
	 */
	private static String defaultValue(CIMValuedElement<?> pValuedElement, String pInd) {
		Object value = pValuedElement.getValue();
		if (value == null) return "";
		return " = " + initializer(value, pInd);
	}

	/**
	 * <pre>
	 * initializer = constantValue | arrayInitializer | referenceInitializer
	 * // referenceInitializer is handled by constantValue
	 * </pre>
	 * 
	 * @param pValue
	 * @param pInd
	 * @return the MOF string
	 */
	private static String initializer(Object pValue, String pInd) {
		if (pValue instanceof Object[]) return arrayInitializer(pValue, pInd);
		return constantValue(pValue, pInd);
	}
}
