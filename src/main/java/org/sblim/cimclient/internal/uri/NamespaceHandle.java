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
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.uri;

import java.util.regex.Pattern;

/**
 * <pre>
 *     namespaceHandle = [&quot;//&quot; authority] &quot;/&quot; [namespaceName]
 *     namespaceName   = IDENTIFIER *(&quot;/&quot;IDENTIFIER)
 * </pre>
 */
public class NamespaceHandle {

	/**
	 * Factory method which tries to build a <code>NamespaceHandle</code> from
	 * the passed <code>pUriStr</code>
	 * 
	 * @param pUriStr
	 * @return a <code>NamespaceHandle</code> or <code>null</code> in case of
	 *         failure
	 */
	public static NamespaceHandle parse(URIString pUriStr) {
		// TODO: tracing TRC.log(uriStr.toString());
		URIString uriStr = pUriStr.deepCopy();
		Authority auth;
		if (uriStr.cutStarting("//")) {
			auth = Authority.parse(uriStr);
			if (auth == null) return null;
		} else {
			auth = null;
		}
		if (!uriStr.cutStarting('/')) { return null; }
		String nsName = parseNamespaceName(uriStr);
		// namespaceName is optimal
		pUriStr.set(uriStr);
		return new NamespaceHandle(auth, nsName);
	}

	private Authority iAuth;

	private String iNsName;

	private NamespaceHandle(Authority pAuth, String pNsName) {
		this.iAuth = pAuth;
		this.iNsName = pNsName;
	}

	/**
	 * Constructs a NamespaceHandle with namespace name only.
	 * 
	 * @param pNamespaceName
	 */
	public NamespaceHandle(String pNamespaceName) {
		this.iAuth = null;
		this.iNsName = pNamespaceName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.iAuth == null ? "" : "//" + this.iAuth.toString()) + "/"
				+ (this.iNsName == null ? "" : this.iNsName);
	}

	/**
	 * getName
	 * 
	 * @return String
	 */
	public String getName() {
		return this.iNsName;
	}

	/**
	 * getUserInfo
	 * 
	 * @return String
	 */
	public String getUserInfo() {
		return this.iAuth == null ? null : this.iAuth.getUserInfo();
	}

	/**
	 * getHost
	 * 
	 * @return String
	 */
	public String getHost() {
		return this.iAuth == null ? null : this.iAuth.getHost();
	}

	/**
	 * getPort
	 * 
	 * @return String
	 */
	public String getPort() {
		return this.iAuth == null ? null : this.iAuth.getPort();
	}

	private static final String IDENTIFIER = "[A-Za-z][0-9A-Za-z\\._-]*";

	private static final Pattern NAMESPACENAME_PAT = Pattern.compile("^(" + IDENTIFIER + "(/"
			+ IDENTIFIER + ")*).*");

	/**
	 * <code>IDENTIFIER *("/"IDENTIFIER)</code>
	 * 
	 * @param pUriStr
	 * @return <code>String</code> containing the namespace name or
	 *         <code>null</code> if failed.
	 */
	public static String parseNamespaceName(URIString pUriStr) {
		if (!pUriStr.matchAndCut(NAMESPACENAME_PAT, 1)) return null;
		return pUriStr.group(1);
	}

}
