/**
 * ServiceURL.java
 *
 * (C) Copyright IBM Corp. 2005, 2009
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
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.slp;

import java.io.Serializable;

/**
 * The ServiceURL object models the advertised SLP service URL. It can be either
 * a service: URL or a regular URL. These objects are returned from service
 * lookup requests, and describe the registered services. This class should be a
 * subclass of java.net.URL but can't since that class is final.
 */
public class ServiceURL implements Serializable {

	private static final long serialVersionUID = 8998115518853094365L;

	/**
	 * Indicates that no port information is required or was returned for this
	 * URL.
	 */
	public static final int NO_PORT = 0;

	/**
	 * Indicates that the URL has a zero lifetime. This value is never returned
	 * from the API, but can be used to create a ServiceURL object to
	 * deregister, delete attributes, or find attributes.
	 */
	public static final int LIFETIME_NONE = 0;

	/**
	 * The default URL lifetime (3 hours) in seconds.
	 */
	public static final int LIFETIME_DEFAULT = 10800;

	/**
	 * The maximum URL lifetime (about 18 hours) in seconds.
	 */
	public static final int LIFETIME_MAXIMUM = 65535;

	/**
	 * Indicates that the API implementation should continuously re-register the
	 * URL until the application exits.
	 */
	public static final int LIFETIME_PERMANENT = -1;

	static final int PORT_MAXIMUM = 65535;

	private ServiceType iServiceType = null;

	private String iTransport = null;

	private String iHost = null;

	private int iPort = 0;

	private String iURLPath = null;

	private int iLifetime = LIFETIME_DEFAULT;

	/**
	 * Construct a service URL object having the specified lifetime.
	 * 
	 * @param pServiceURL
	 *            The URL as a string. Must be either a service: URL or a valid
	 *            generic URL according to RFC 2396 [2].
	 * @param pLifetime
	 *            The service advertisement lifetime in seconds. This value may
	 *            be either between LIFETIME_NONE and LIFETIME_MAXIMUM or
	 *            LIFETIME_PERMANENT.
	 */
	public ServiceURL(String pServiceURL, int pLifetime) {

		if (pLifetime > LIFETIME_MAXIMUM || pLifetime < LIFETIME_PERMANENT) throw new IllegalArgumentException(
				"lifetime:" + pLifetime);

		for (int i = 0; i < pServiceURL.length(); i++) {
			char c = pServiceURL.charAt(i);
			if ("/:-.%_\'*()$!,+\\;@?&=[]".indexOf(c) == -1 && !Character.isLetterOrDigit(c)) { throw new IllegalArgumentException(
					"invalid character: '" + c + "' on string \"" + pServiceURL + "\""); }
		}

		parseURL(pServiceURL);

		this.iLifetime = (pLifetime == LIFETIME_PERMANENT) ? LIFETIME_MAXIMUM : pLifetime;
	}

	/**
	 * Returns the service type object representing the service type name of the
	 * URL.
	 * 
	 * @return The service type
	 */
	public ServiceType getServiceType() {
		return this.iServiceType;
	}

	/**
	 * Set the service type name to the object. Ignored if the URL is a service:
	 * URL.
	 * 
	 * @param pServicetype
	 *            The service type object.
	 */
	public void setServiceType(ServiceType pServicetype) {
		if (!this.iServiceType.isServiceURL()) this.iServiceType = pServicetype;
	}

	/**
	 * Get the network layer transport identifier. If the transport is IP, an
	 * empty string, "", is returned.
	 * 
	 * @return The NLT identifier
	 */
	public String getTransport() {
		// FIXME What the hell is it?
		return "";
	}

	/**
	 * Returns the host identifier. For IP, this will be the machine name or IP
	 * address.
	 * 
	 * @return The host
	 */
	public String getHost() {
		return this.iHost;
	}

	/**
	 * Returns the port number, if any. For non-IP transports, always returns
	 * NO_PORT.
	 * 
	 * @return The port
	 */
	public int getPort() {
		return this.iPort;
	}

	/**
	 * Returns the URL path description, if any.
	 * 
	 * @return The URL path
	 */
	public String getURLPath() {
		return this.iURLPath;
	}

	/**
	 * Returns the service advertisement lifetime. This will be a positive int
	 * between LIFETIME_NONE and LIFETIME_MAXIMUM.
	 * 
	 * @return The lifetime
	 */
	public int getLifetime() {
		return this.iLifetime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * Compares the object to the ServiceURL and returns true if the two are the
	 * same. Two ServiceURL objects are equal if their current service types
	 * match and they have the same host, port, transport, and URL path.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof ServiceURL)) return false;

		ServiceURL that = (ServiceURL) obj;

		return equalObjs(this.iServiceType, that.iServiceType)
				&& equalStrs(this.iTransport, that.iTransport) && equalStrs(this.iHost, that.iHost)
				&& this.iPort == that.iPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * Returns a formatted string with the URL. Overrides Object.toString(). The
	 * returned URL has the original service type or URL scheme, not the current
	 * service type.
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (this.iServiceType != null) buf.append(this.iServiceType);
		if (this.iURLPath != null) {
			if (buf.length() > 0) buf.append("://");
			buf.append(this.iURLPath);
		}
		return buf.toString();
	}

	private int iHashCode = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 * Overrides Object.hashCode(). Hashes on the current service type,
	 * transport, host, port, and URL part. !! in this case toString() must not
	 * contain the lifeTime
	 */
	@Override
	public int hashCode() {
		if (this.iHashCode == 0) {
			this.iHashCode = toString().hashCode();
		}
		return this.iHashCode;
	}

	private static final String DELIM = "://";

	/**
	 * <pre>
	 * service: URL or URL
	 * 
	 * &quot;service:&quot; srvtype &quot;://&quot; addrspec
	 * &quot;service:&quot; abstract-type &quot;:&quot; concrete-type&gt; &quot;://&quot; addrspecc
	 * 
	 * addrspesc  = ( hostName / IPv4Address / IPv6Address ) [ &quot;:&quot; port ]
	 * </pre>
	 * 
	 * @param pUrlString
	 * @throws IllegalArgumentException
	 */
	private void parseURL(String pUrlStr) throws IllegalArgumentException {
		int srvTypeEndIdx = pUrlStr.indexOf(DELIM);
		String addrStr;
		if (srvTypeEndIdx >= 0) {
			this.iServiceType = new ServiceType(pUrlStr.substring(0, srvTypeEndIdx));
			addrStr = pUrlStr.substring(srvTypeEndIdx + DELIM.length());
		} else {
			if (pUrlStr.startsWith("service:")) {
				this.iServiceType = new ServiceType(pUrlStr);
				addrStr = null;
			} else {
				addrStr = pUrlStr;
			}
		}
		if (addrStr == null) return;
		this.iURLPath = addrStr;
		if (addrStr.charAt(0) == '[') {
			parseIPv6Address(addrStr);
		} else {
			parseIPv4Address(addrStr);
		}
	}

	private void parseIPv6Address(String pAddrStr) throws IllegalArgumentException {
		int hostEndIdx = pAddrStr.indexOf(']');
		if (hostEndIdx < 0) throw new IllegalArgumentException("']' is not found for IPv6 address");
		int colonIdx = hostEndIdx + 1;
		this.iHost = pAddrStr.substring(0, colonIdx);
		if (colonIdx < pAddrStr.length()) {
			if (pAddrStr.charAt(colonIdx) != ':') throw new IllegalArgumentException(
					"':' expected in \"" + pAddrStr + "\" at position " + colonIdx + " !");
			parsePort(pAddrStr.substring(colonIdx + 1), pAddrStr);
		}
	}

	private void parseIPv4Address(String pAddrStr) {
		int colonIdx = pAddrStr.indexOf(':');
		if (colonIdx > 0) {
			this.iHost = pAddrStr.substring(0, colonIdx);
			parsePort(pAddrStr.substring(colonIdx + 1), pAddrStr);
		} else {
			this.iHost = pAddrStr;
		}
	}

	private void parsePort(String pPortStr, String pAddrStr) throws IllegalArgumentException {
		try {
			this.iPort = Integer.parseInt(pPortStr);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Port field : " + pPortStr + " in " + pAddrStr
					+ " is invalid!");
		}
	}

	private static boolean equalObjs(Object pThis, Object pThat) {
		return pThis == null ? pThat == null : pThis.equals(pThat);
	}

	private static boolean equalStrs(String pThis, String pThat) {
		return (pThis == null || pThis.length() == 0) ? (pThat == null || pThat.length() == 0)
				: pThis.equals(pThat);
	}

}
