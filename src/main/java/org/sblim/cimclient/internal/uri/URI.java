/**
 * (C) Copyright IBM Corp. 2006, 2009
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
 * 1565892    2006-11-05  ebak         Make SBLIM client JSR48 compliant
 * 1723607    2007-05-22  ebak         IPv6 support in WBEM-URI strings
 * 1917309    2008-03-24  raman_arora  "/root:__NAMESPACE" not valid CIMObjectPath
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.uri;

import java.util.regex.Pattern;

import org.sblim.cimclient.internal.util.MOF;

/**
 * <pre>
 *    untypedNamespacePath	=	namespacePath
 *   
 *    untypedClassPath		=	namespacePath &quot;:&quot; className
 *    
 *    untypedInstancePath	=	namespacePath &quot;:&quot; className &quot;.&quot; key_value_pairs
 *    
 *    typedNamespacePath	=	namespacePath &quot;/(namespace)&quot;
 *   
 *    typedClassPath		=	namespacePath &quot;/(class)&quot; className
 *   
 *    typedInstancePath	= 	namespacePath &quot;/(instance)&quot; className &quot;.&quot; typed_key_value_pairs
 * </pre>
 */
public class URI {

	/**
	 * parse
	 * 
	 * @param pUri
	 * @return URI
	 * @throws IllegalArgumentException
	 *             if parsing failed.
	 */
	public static URI parse(String pUri) throws IllegalArgumentException {
		URIString uriStr = new URIString(pUri);
		NamespacePath nsPath = NamespacePath.parse(uriStr);
		if (nsPath == null) {
			String msg = "namespacePath expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		String uriType = parseUriType(uriStr);
		boolean typed = uriType != null;
		if (uriStr.length() == 0) {
			if (typed && uriType != NAMESPACE) {
				String msg = uriType + " excepted but " + NAMESPACE + " found!\n"
						+ uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return new URI(nsPath, uriType, null, null);
		}
		if (!typed && !uriStr.cutStarting(':')) {
			String msg = "':' expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		String className = parseClassName(uriStr, typed);
		if (uriStr.length() == 0) {
			if (typed && uriType != CLASS) {
				String msg = uriType + " expected but " + CLASS + " found!\n"
						+ uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return new URI(nsPath, uriType, className, null);
		}
		KeyValuePairs keyValuePairs = parseKeyValuePairs(uriStr, typed);
		return new URI(nsPath, uriType, className, keyValuePairs);
	}

	/**
	 * <pre>
	 *  referenceValue			=	[ namespaceName &quot;:&quot; ] className &quot;.&quot; 
	 * 								untyped_key_value_pairs
	 * 
	 *  typed_reference_value		=	&quot;(reference)&quot; &quot;\&quot;&quot; typedReferenceValue &quot;\&quot;&quot;
	 *  
	 *  // according to Alexander we have to support instance references only
	 *  typedReferenceValue	=	[ namespaceName ] &quot;/(instance)&quot; className &quot;.&quot;
	 *  							typed_key_value_pairs
	 * </pre>
	 * 
	 * @param pUriStr
	 * @param pTyped
	 * @return URI
	 * @throws IllegalArgumentException
	 *             if parsing failed
	 */
	public static URI parseRef(URIString pUriStr, boolean pTyped) throws IllegalArgumentException {
		URIString uriStr = pUriStr.deepCopy();
		// try to retrieve optional namespace handle
		String namespaceName = NamespaceHandle.parseNamespaceName(uriStr);
		if (pTyped) {
			if (!uriStr.cutStarting("/(instance)")) {
				namespaceName = null;
				uriStr.set(pUriStr);
			}
		} else {
			if (!uriStr.cutStarting(':')) {
				namespaceName = null;
				uriStr.set(pUriStr);
			}
		}
		// className is not typed in reference
		String className = parseClassName(uriStr, false);
		if (uriStr.length() == 0) {
			if (pTyped) return new URI(null, className, null, pTyped);
			// untyped reference can be instance reference only
			String msg = "Untyped reference can be instance reference only!\n"
					+ uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		KeyValuePairs keyValuePairs = parseKeyValuePairs(uriStr, pTyped);
		if (keyValuePairs == null) {
			String msg = "Property reference must contain key-value pairs!\n"
					+ uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		pUriStr.set(uriStr);
		return new URI(namespaceName, className, keyValuePairs, pTyped);
	}

	/**
	 * getNamespaceType
	 * 
	 * @return String or null if not set
	 */
	public String getNamespaceType() {
		return this.iNamespacePath == null ? null : this.iNamespacePath.getNamespaceType();
	}

	/**
	 * getNamespaceName
	 * 
	 * @return String or null if not set
	 */
	public String getNamespaceName() {
		return this.iNamespacePath == null ? null : this.iNamespacePath.getNamespaceName();
	}

	/**
	 * getUserInfo
	 * 
	 * @return String or null if not set
	 */
	public String getUserInfo() {
		return this.iNamespacePath == null ? null : this.iNamespacePath.getUserInfo();
	}

	/**
	 * getHost Userinfo is attached if exists.
	 * 
	 * @return String or null if not set
	 */
	public String getHost() {
		if (this.iNamespacePath == null) return null;
		String userInfo = this.iNamespacePath.getUserInfo();
		String host = this.iNamespacePath.getHost();
		return userInfo == null ? host : userInfo + '@' + host;
	}

	/**
	 * getPort
	 * 
	 * @return String or null if not set
	 */
	public String getPort() {
		return this.iNamespacePath == null ? null : this.iNamespacePath.getPort();
	}

	/**
	 * getClassName
	 * 
	 * @return String or null if not set
	 */
	public String getClassName() {
		return this.iClassName;
	}

	/**
	 * getKeyValuePairs
	 * 
	 * @return KeyValuePairs or null if not set
	 */
	public KeyValuePairs getKeyValuePairs() {
		return this.iKeyValuePairs;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (this.iNamespacePath != null) {
			if (this.iIsRef) buf.append(getNamespaceName());
			else buf.append(this.iNamespacePath.toString());
		}
		if (this.iUriType != null) buf.append("/(" + this.iUriType + ")");
		if (this.iClassName != null) {
			if (this.iNamespacePath != null && this.iUriType == null) {
				// it's not a reference value
				buf.append(':');
			}
			buf.append(this.iClassName);
			if (this.iKeyValuePairs != null) {
				buf.append('.');
				buf.append(this.iKeyValuePairs.toString());
			}
		}
		return buf.toString();
	}

	/**
	 * Constructor for URI paths.
	 * 
	 * @param pNamespacePath
	 * @param pUriType
	 * @param pClassName
	 * @param pKeyValuePairs
	 */
	private URI(NamespacePath pNamespacePath, String pUriType, String pClassName,
			KeyValuePairs pKeyValuePairs) {
		this.iNamespacePath = pNamespacePath;
		this.iUriType = pUriType;
		this.iClassName = pClassName;
		this.iKeyValuePairs = pKeyValuePairs;
		this.iIsRef = false;
	}

	/**
	 * Constructor for reference property values.
	 * 
	 * @param pNamespaceName
	 * @param pClassName
	 * @param pKeyValuePairs
	 * @param pTyped
	 */
	private URI(String pNamespaceName, String pClassName, KeyValuePairs pKeyValuePairs,
			boolean pTyped) {
		if (pNamespaceName == null && pClassName == null) {
			String msg = "pNamespaceName or pClassName must be set!";
			throw new IllegalArgumentException(msg);
		}
		if (pKeyValuePairs != null && pClassName == null) {
			String msg = "If pKeyValuePairs is set pClassName cannot be null!";
			throw new IllegalArgumentException(msg);
		}
		this.iNamespacePath = pNamespaceName == null ? null : new NamespacePath(pNamespaceName);
		if (pTyped) {
			if (pKeyValuePairs != null) this.iUriType = INSTANCE;
			else if (pClassName != null) this.iUriType = CLASS;
			else this.iUriType = NAMESPACE;
		} else this.iUriType = null;
		this.iClassName = pClassName;
		this.iKeyValuePairs = pKeyValuePairs;
		this.iIsRef = true;
	}

	private NamespacePath iNamespacePath;

	/**
	 * iUriType - if null the URI is untyped
	 */
	private String iUriType;

	private String iClassName;

	private KeyValuePairs iKeyValuePairs;

	private boolean iIsRef;

	private static final String NAMESPACE = MOF.NAMESPACE, CLASS = MOF.CLASS,
			INSTANCE = MOF.INSTANCE;

	/**
	 * uriType = "/(" ( "namespace" / "class" / "instance" ) ")"
	 * 
	 * @param pUriStr
	 * @return NAMESPACE | CLASS | INSTANCE
	 */
	private static String parseUriType(URIString pUriStr) {
		URIString uriStr = pUriStr.deepCopy();
		if (!uriStr.cutStarting("/(")) return null;
		String typeStr = uriStr.removeTill(')', true, true);
		if (typeStr == null) return null;
		if (typeStr.equalsIgnoreCase(NAMESPACE)) typeStr = NAMESPACE;
		else if (typeStr.equalsIgnoreCase(CLASS)) typeStr = CLASS;
		else if (typeStr.equalsIgnoreCase(INSTANCE)) typeStr = INSTANCE;
		else return null;
		pUriStr.set(uriStr);
		return typeStr;
	}

	/*
	 * Per DMTF DSP0004 spec: Therefore, all namespace, class and property names
	 * are identifiers composed as follows: 1. Initial identifier characters
	 * must be in set S1, where S1 = {U+005F, U+0041...U+005A, U+0061...U+007A,
	 * U+0080...U+FFEF) [This is alphabetic, plus underscore] 2. All following
	 * characters must be in set S2 where S2 = S1 union {U+0030...U+0039} [This
	 * is alphabetic, underscore, plus Arabic numerals 0 through 9.]
	 */
	private static final Pattern PAT = Pattern.compile("^([A-Za-z_]+[A-Za-z0-9_]*).*");

	/**
	 * @param pTyped
	 */
	private static String parseClassName(URIString pUriStr, boolean pTyped)
			throws IllegalArgumentException {
		if (!pUriStr.matchAndCut(PAT, 1)) {
			String msg = "className expected!\n" + pUriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		String className = pUriStr.group(1);
		return className;
	}

	private static KeyValuePairs parseKeyValuePairs(URIString pUriStr, boolean pTyped)
			throws IllegalArgumentException {
		if (!pUriStr.cutStarting('.')) {
			String msg = "'.' expected!\n" + pUriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		KeyValuePairs keyValuePairs = KeyValuePairs.parse(pTyped, pUriStr);
		if (keyValuePairs == null) {
			String msg = "keyValuePairs expected!\n" + pUriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		return keyValuePairs;
	}

}
