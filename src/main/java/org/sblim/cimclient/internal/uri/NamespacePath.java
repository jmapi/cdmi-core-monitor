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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.uri;

import java.util.regex.Pattern;

/**
 * <pre>
 *     namespacePath	=	[namespaceType &quot;:&quot;] namespaceHandle
 *     namespaceType	=	(&quot;http&quot; [&quot;s&quot;]) / (&quot;cimxml.wbem&quot; [&quot;s&quot;])
 * </pre>
 */
public class NamespacePath {

	private String iNamespaceType;

	private NamespaceHandle iNamespaceHandle;

	protected NamespacePath(String pNamespaceType, NamespaceHandle pNamespaceHandle) {
		this.iNamespaceType = pNamespaceType;
		this.iNamespaceHandle = pNamespaceHandle;
	}

	/**
	 * Constructs a NamespacePath with namespace name only.
	 * 
	 * @param pNamespaceName
	 */
	public NamespacePath(String pNamespaceName) {
		this.iNamespaceHandle = new NamespaceHandle(pNamespaceName);
	}

	/**
	 * Tries to parse a namespace path from the passed <code>uriStr</code>.
	 * 
	 * @param pUriStr
	 * @return <code>NamespacePath</code> instance or <code>null</code> if
	 *         failed.
	 */
	public static NamespacePath parse(URIString pUriStr) {
		// TODO: tracing TRC.log(uriStr.toString());
		URIString uriStr = pUriStr.deepCopy();
		String namespaceType = parseNamespaceType(uriStr);
		// can be null
		if (namespaceType != null) {
			// : needed
			if (!uriStr.cutStarting(':')) return null;
		}
		NamespaceHandle namespaceHandle = NamespaceHandle.parse(uriStr);
		if (namespaceHandle == null) { return null; }
		pUriStr.set(uriStr);
		return new NamespacePath(namespaceType, namespaceHandle);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (this.iNamespaceType != null) buf.append(this.iNamespaceType + ':');
		buf.append(this.iNamespaceHandle.toString());
		return buf.toString();
	}

	/**
	 * getNamespaceType
	 * 
	 * @return the namespace type String
	 */
	public String getNamespaceType() {
		return this.iNamespaceType;
	}

	/**
	 * getNamespaceName
	 * 
	 * @return the namespace name String
	 */
	public String getNamespaceName() {
		return this.iNamespaceHandle == null ? null : this.iNamespaceHandle.getName();
	}

	/**
	 * getUserInfo
	 * 
	 * @return String
	 */
	public String getUserInfo() {
		return this.iNamespaceHandle == null ? null : this.iNamespaceHandle.getUserInfo();
	}

	/**
	 * getHost
	 * 
	 * @return String
	 */
	public String getHost() {
		return this.iNamespaceHandle == null ? null : this.iNamespaceHandle.getHost();
	}

	/**
	 * getPort
	 * 
	 * @return String
	 */
	public String getPort() {
		return this.iNamespaceHandle == null ? null : this.iNamespaceHandle.getPort();
	}

	private static final Pattern TYPE_PAT = Pattern.compile("^(http(s?)|cimxml\\.wbem(s?)).*");

	/**
	 * namespaceType = ("http" ["s"]) / ("cimxml.wbem" ["s"])
	 * 
	 * @param pUriStr
	 * @return the String containing the namespace type or <code>null</code> if
	 *         failed.
	 */
	private static String parseNamespaceType(URIString pUriStr) {
		if (!pUriStr.matchAndCut(TYPE_PAT, 1)) return null;
		return pUriStr.group(1);
	}

}
