/**
 * (C) Copyright IBM Corp. 2006, 2011
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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3374012    2011-07-24  blaschke-oss Sblim client CIMObjectPath class defect for LLA format URL
 */

package org.sblim.cimclient.internal.uri;

import java.util.regex.Pattern;

/**
 * <code>
 * <b>[ userinfo "@" ] host [ ":" port ]</b><br><br>
 * userinfo = *( unreserved / pct-encoded / sub-delims / ":" )<br>
 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"<br>
 * pct-encoded = "%" HEXDIG HEXDIG<br>
 * sub-delims    = "!" / "$" / "&" / "'" / "(" / ")"
 *               / "*" / "+" / "," / ";" / "="<br>
 * ALPHA = regex([A-Za-z])<br>
 * DIGIT = regex([0-9])<br><br>
 * Zone-index = ["%" ( 1*unreserved )]
 * host        = IP-literal / IPv4address / reg-name<br>
 * IP-literal = "[" ( IPv6address / IPvFuture  ) "]"<br>
 * IPvFuture  = "v" 1*HEXDIG "." 1*( unreserved / sub-delims / ":" )<br>
 * IPv4address = dec-octet "." dec-octet "." dec-octet "." dec-octet<br>
 * reg-name    = *( unreserved / pct-encoded / sub-delims )<br><br>
 * 
 * IPv6address =   (                           6( h16 ":" ) ls32<br>
 *                /                       "::" 5( h16 ":" ) ls32<br>
 *                / [               h16 ] "::" 4( h16 ":" ) ls32<br>
 *                / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32<br>
 *                / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32<br>
 *                / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32<br>
 *                / [ *4( h16 ":" ) h16 ] "::"              ls32<br>
 *                / [ *5( h16 ":" ) h16 ] "::"              h16<br>
 *                / [ *6( h16 ":" ) h16 ] "::" ) Zone-index<br><br>
 * 
 *    ls32        = ( h16 ":" h16 ) / IPv4address<br>
 *                ; least-significant 32 bits of address<br><br>
 * 
 *    h16         = 1*4HEXDIG<br>
 *                ; 16 bits of address represented in hexadecimal<br>
 * </code>
 */
public class Authority {

	private static final String PCTENCODED = "%[0-9A-Fa-f]{2}";

	private static final String UNRESERVED = "A-Za-z0-9\\-\\._~";

	private static final String SUBDELIMS = "!\\$&'\\(\\)\\*\\+,;=";

	private static final String REGNAMEREG = "([" + UNRESERVED + SUBDELIMS + "]|" + PCTENCODED
			+ ")+";

	private static final String ZONEINDEX = "(%[" + UNRESERVED + "]+)?";

	private static final String IPV4 = "([0-9]{1,3}\\.){3}[0-9]{1,3}";

	private static final String H16 = "[0-9A-Fa-f]{1,4}";

	private static final String LS32 = "((" + H16 + ":" + H16 + ")" + "|(" + IPV4 + "))";

	private static final String IPV6 =
	// 6( h16 ":" ) ls32
	"(((" + H16 + ":){6}" + LS32 + ")|" +
	// "::" 5( h16 ":" )
			"(::(" + H16 + ":){5}" + LS32 + ")|" +
			// [ h16 ] "::" 4( h16 ":" ) ls32
			"((" + H16 + ")?::" + "(" + H16 + ":){4}" + LS32 + ")|" +
			// [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
			"(((" + H16 + ":){0,1}" + H16 + ")?::(" + H16 + ":){3}" + LS32 + ")|" +
			// [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
			"(((" + H16 + ":){0,2}" + H16 + ")?::(" + H16 + ":){2}" + LS32 + ")|" +
			// [ *3( h16 ":" ) h16 ] "::" h16 ":" ls32
			"(((" + H16 + ":){0,3}" + H16 + ")?::" + H16 + ":" + LS32 + ")|" +
			// [ *4( h16 ":" ) h16 ] "::" ls32
			"(((" + H16 + ":){0,4}" + H16 + ")?::" + LS32 + ")|" +
			// [ *5( h16 ":" ) h16 ] "::" h16
			"(((" + H16 + ":){0,5}" + H16 + ")?::" + H16 + ")|" +
			// [ *6( h16 ":" ) h16 ] "::"
			"(((" + H16 + ":){0,6}" + H16 + ")?::))" + ZONEINDEX;

	// IPvFuture = "v" 1*HEXDIG "." 1*( unreserved / sub-delims / ":" )
	private static final String IPVFUTURE = "v[0-9A-Fa-f]+\\.([" + UNRESERVED + SUBDELIMS + "]|:)+";

	private static final String IPLITERAL = "\\[(" + IPV6 + "|" + IPVFUTURE + ")\\]";

	private static Pattern USERINFOPAT = Pattern.compile("^((([" + UNRESERVED + SUBDELIMS + ":]|("
			+ PCTENCODED + "))*)@).*");

	private static Pattern HOSTPAT = Pattern.compile("^((" + IPV4 + ")|(" + REGNAMEREG + ")|("
			+ IPLITERAL + ")).*");

	private static Pattern PORTPAT = Pattern.compile("^([0-9]+).*");

	/**
	 * Parses userInfo, host and port
	 * 
	 * @param pUriStr
	 * @return the parsed Authority
	 */
	public static Authority parse(URIString pUriStr) {
		// TODO: tracing TRC.log(uriStr.toString());
		URIString uriStr = pUriStr.deepCopy();
		String userInfo;
		if (uriStr.matchAndCut(USERINFOPAT, 1)) {
			userInfo = uriStr.group(2);
		} else {
			userInfo = null;
		}
		if (!uriStr.matchAndCut(HOSTPAT, 1)) { return null; }
		String host = uriStr.group(1);
		String port = null;
		if (uriStr.cutStarting(':')) {
			if (!uriStr.matchAndCut(PORTPAT, 1)) {
				// TODO: tracing TRC.error("Port not matched while ':' has been
				// found!");
				return null;
			}
			port = uriStr.group(1);
		}
		pUriStr.set(uriStr);
		return new Authority(userInfo, host, port);
	}

	private String iUserInfo;

	private String iHost;

	private String iPort;

	private Authority(String pUserInfo, String pHost, String pPort) {
		this.iUserInfo = pUserInfo;
		this.iHost = pHost;
		this.iPort = pPort;
	}

	/**
	 * str
	 * 
	 * @return a String
	 */
	@Override
	public String toString() {
		return (this.iUserInfo == null ? "" : this.iUserInfo + "@") + this.iHost
				+ (this.iPort == null ? "" : ":" + this.iPort);
	}

	/**
	 * getUserInfo
	 * 
	 * @return the userInfo String
	 */
	public String getUserInfo() {
		return this.iUserInfo;
	}

	/**
	 * getHost
	 * 
	 * @return the host String
	 */
	public String getHost() {
		return this.iHost;
	}

	/**
	 * getPort
	 * 
	 * @return the port String
	 */
	public String getPort() {
		return this.iPort;
	}

}
