/**
 * SLPConfigProperties.java
 *
 * (C) Copyright IBM Corp. 2006, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535793    2006-09-19  lupusalex    Fix&Integrate CIM&SLP configuration classes
 * 1911400    2008-03-10  blaschks-oss Source RPM file on SourceForge is broken
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2907527    2009-12-02  blaschke-oss Fix SLP properties issues
 */

package org.sblim.slp;

/**
 * The interface SLPConfigProperties holds string constants for the
 * configuration properties of the SLP package. To set a property call
 * <code>System.setProperty(name, value)</code>. Note that these properties have
 * global VM scope.
 * 
 */
public interface SLPConfigProperties {

	/**
	 * A URL string giving the location of the SLP config file. <br />
	 * <br />
	 * By default the SLP client looks for
	 * <ul>
	 * <li>file:sblim-slp-client2.properties</li>
	 * <li>file:%USER_HOME%/sblim-slp-client2.properties</li>
	 * <li>file:/etc/java/sblim-slp-client2.properties</li>
	 * <li>file:/etc/sblim-slp-client2.properties</li>
	 * </ul>
	 * The first file found will be used. The default search list is not applied
	 * if this property is set, even if the given URL does not exist.<br />
	 * <br />
	 * <i>SBLIM specific extension to RFC 2614</i>
	 */
	public static final String NET_SLP_CONFIG_URL = "net.slp.configURL";

	/**
	 * A 16 bit positive integer giving the number of seconds the DA url
	 * lifetime should exceed the discovery interval.<br />
	 * <br />
	 * Default is 900 seconds (15 minutes).<br />
	 * <br />
	 * <i>SBLIM specific extension to RFC 2614</i>
	 */
	public static final String NET_SLP_DA_ACTIVE_DISCOVERY_GRANULARITY = "net.slp.DAActiveDiscoveryGranularity";

	/**
	 * A 16 bit positive integer giving the number of seconds between DA active
	 * discovery queries.<br />
	 * <br />
	 * Default is 900 seconds (15 minutes).<br />
	 * <br />
	 * If the property is set to zero, active discovery is turned off. This is
	 * useful when the DAs available are explicitly restricted to those obtained
	 * from DHCP or the net.slp.DAAddresses property.
	 */
	public static final String NET_SLP_DA_ACTIVE_DISCOVERY_INTERVAL = "net.slp.DAActiveDiscoveryInterval";

	/**
	 * A value-list of IP addresses or DNS resolvable host names giving the
	 * SLPv2 DAs to use for statically configured UAs and SAs. Ignored by DAs
	 * (unless the DA is also an SA server). <br />
	 * <br />
	 * Default is none.<br />
	 * <br />
	 * The following grammar describes the property:
	 * <p>
	 * <code>
	 * addr-list = addr / addr "," addr-list<br />
	 * addr = fqdn / hostnumber<br />
	 * fqdn = ALPHA / ALPHA *[ anum / "-" ] anum<br />
	 * anum = ALPHA / DIGIT<br />
	 * hostnumber = 1*3DIGIT 3("." 1*3DIGIT)<br />
	 * </code>
	 * </p>
	 * An example is:<br />
	 * <p>
	 * <code>
	 * sawah,mandi,sambal
	 * </code>
	 * </p>
	 * IP addresses can be used instead of host names in networks where DNS is
	 * not deployed, but network administrators are reminded that using IP
	 * addresses will complicate machine renumbering, since the SLP
	 * configuration property files in statically configured networks will have
	 * to be changed. Similarly, if host names are used, implementors must be
	 * careful that a name service is available before SLP starts, in other
	 * words, SLP cannot be used to find the name service.<br/>
	 */
	public static final String NET_SLP_DA_ADDRESSES = "net.slp.DAAddresses";

	/**
	 * A comma-separated list of parenthesized attribute/value list pairs that
	 * the DA must advertise in DAAdverts. The property must be in the SLP
	 * attribute list wire format, including escapes for reserved characters.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_DA_ATTRIBUTES = "net.slp.DAAttributes";

	/**
	 * A value-list of 32 bit integers used as timeouts, in milliseconds, to
	 * implement the multicast convergence algorithm during active DA discovery.
	 * Each value specifies the time to wait before sending the next request, or
	 * until nothing new has been learned from two successive requests.<br />
	 * <br/>
	 * Default is: <code>200,200,200,200,300,400</code>.
	 */
	public static final String NET_SLP_DA_DISCOVERY_TIMEOUTS = "net.slp.DADiscoveryTimeouts";

	/**
	 * A 32 bit integer giving the number of seconds for the DA heartbeat.
	 * Ignored if isDA is false. <br />
	 * <br />
	 * Default is 10800 seconds (3 hours). <br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_DA_HEARTBEAT = "net.slp.DAHeartbeat";

	/**
	 * A value-list of 32 bit integers used as timeouts, in milliseconds, to
	 * implement unicast datagram transmission to DAs. The nth value gives the
	 * time to block waiting for a reply on the nth try to contact the DA.<br />
	 * <br />
	 * Default is: <code>100,200,300</code>
	 */
	public static final String NET_SLP_DATAGRAM_TIMEOUTS = "net.slp.datagramTimeouts";

	/**
	 * Value-list of strings giving the IP addresses of network interfaces on
	 * which the DA or SA should listen on port 427 for multicast, unicast UDP,
	 * and TCP messages.<br />
	 * <br />
	 * Default is empty, i.e. use the default network interface.<br />
	 * <br />
	 * The grammar for this property is:
	 * <p>
	 * <code>
	 * addr-list     =  hostnumber / hostnumber "," addr-list<br />
	 * hostnumber    =  1*3DIGIT 3("." 1*3DIGIT)
	 * </code>
	 * </p>
	 * An example is:
	 * <p>
	 * <code>195.42.42.42,195.42.142.1,195.42.120.1</code>
	 * </p>
	 * The example machine has three interfaces on which the DA should listen.
	 * Note that since this property only takes IP addresses, it will need to be
	 * changed if the network is renumbered.
	 */
	public static final String NET_SLP_INTERFACES = "net.slp.interfaces";

	/**
	 * A boolean indicating if broadcast should be used instead of multicast.<br />
	 * <br />
	 * Default is <code>false</code>.
	 */
	public static final String NET_SLP_IS_BROADCAST_ONLY = "net.slp.isBroadcastOnly";

	/**
	 * A boolean indicating if the SLP server is to act as a DA. If
	 * <code>false</code>, run as a SA.<br />
	 * <br />
	 * Default is <code>false</code>.
	 */
	public static final String NET_SLP_IS_DA = "net.slp.isDA";

	/**
	 * A RFC 1766 Language Tag for the language locale. Setting this property
	 * causes the property value to become the default locale for SLP messages.
	 * This property is also used for SA and DA configuration.<br />
	 * <br />
	 * Default is <code>en</code>. <br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_LOCALE = "net.slp.locale";

	/**
	 * A 32 bit integer giving the maximum number of results to accumulate and
	 * return for a synchronous request before the timeout, or the maximum
	 * number of results to return through a callback if the request results are
	 * reported asynchronously.<br />
	 * Positive integers and -1 are legal values. If -1, indicates that all
	 * results should be returned. <br />
	 * <br />
	 * Default value is 2147483647 (2^31 - 1)<br />
	 * <i>SBLIM specific: RFC 2614 recommendation is -1.</i><br />
	 * <br />
	 * DAs and SAs always return all results that match the request. This
	 * configuration value applies only to UAs, that filter incoming results and
	 * only return as many values as net.slp.maxResults indicates.
	 */
	public static final String NET_SLP_MAX_RESULTS = "net.slp.maxResults";

	/**
	 * A 16 bit integer giving the network packet MTU, in bytes. This is the
	 * maximum size of any datagram to send, but the implementation might
	 * receive a larger datagram. The maximum size includes IP, and UDP or TCP
	 * headers.<br />
	 * <br />
	 * Default is 1400.
	 */
	public static final String NET_SLP_MTU = "net.slp.MTU";

	/**
	 * A 32 bit integer giving the maximum amount of time to perform multicast,
	 * in milliseconds.<br />
	 * <br />
	 * Default is 2000 ms.
	 */
	public static final String NET_SLP_MULTICAST_MAXIMUM_WAIT = "net.slp.multicastMaximumWait";

	/**
	 * A value-list of 32 bit integers used as timeouts, in milliseconds, to
	 * implement the multicast convergence algorithm. Each value specifies the
	 * time to wait before sending the next request, or until nothing new has
	 * been learned from two successive requests.<br />
	 * <br />
	 * Default is: 200, 200, 200, 200, 300, 400<br />
	 * <i>SBLIM specific: RFC 2614 recommendation is
	 * 3000,3000,3000,3000,3000.</i><br />
	 * <br />
	 * In a fast network the aggressive values of 1000,1250,1500,2000,4000 allow
	 * better performance.<br />
	 * <br />
	 * Note that the net.slp.DADiscoveryTimeouts property must be used for
	 * active DA discovery.
	 */
	public static final String NET_SLP_MULTICAST_TIMEOUTS = "net.slp.multicastTimeouts";

	/**
	 * A positive integer less than or equal to 255, giving the multicast TTL.<br />
	 * <br />
	 * Default is 255.
	 */
	public static final String NET_SLP_MULTICAST_TTL = "net.slp.multicastTTL";

	/**
	 * A boolean indicating whether passive DA detection should be used.<br />
	 * <br />
	 * Default is true.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_PASSIVE_DA_DETECTION = "net.slp.passiveDADetection";

	/**
	 * A 16 bit integer giving the port used for listening.<br />
	 * <br />
	 * Default is 427.
	 */
	public static final String NET_SLP_PORT = "net.slp.port";

	/**
	 * A boolean indicating whether IPv6 addresses should be used.<br />
	 * <br />
	 * Default is true.
	 */
	public static final String NET_SLP_USEIPV6 = "net.slp.useipv6";

	/**
	 * A boolean indicating whether IPv4 addresses should be used.<br />
	 * <br />
	 * Default is true.
	 */
	public static final String NET_SLP_USEIPV4 = "net.slp.useipv4";

	/**
	 * A 32 bit integer giving the maximum value for all random wait parameters,
	 * in milliseconds. <br />
	 * <br />
	 * Default is 1000 ms. <br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_RANDOM_WAIT_BOUND = "net.slp.randomWaitBound";

	/**
	 * A comma-separated list of parenthesized attribute/value list pairs that
	 * the SA must advertise in SAAdverts. The property must be in the SLP
	 * attribute list wire format, including escapes for reserved characters. <br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_SA_ATTRIBUTES = "net.slp.SAAttributes";

	/**
	 * A value-list of strings indicating the scopes that are only applied to
	 * SAs. In contradiction the "net.slp.useScopes" specifies the scope for UAs
	 * and SAs.<br />
	 * <br />
	 * <i>SBLIM specific extension to RFC 2614</i>
	 */
	public static final String NET_SLP_SAONLY_SCOPES = "net.slp.SAOnlyScopes";

	/**
	 * A comma-separated list of parenthesized attribute/value list pairs that
	 * the SA must advertise in SAAdverts. The property must be in the SLP
	 * attribute list wire format, including escapes for reserved characters.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_SECURITY_ENABLED = "net.slp.securityEnabled";

	/**
	 * A string containing a URL pointing to a document containing serialized
	 * registrations that should be processed when the DA or SA server starts
	 * up.<br />
	 * <br />
	 * Default is none.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_SERIALIZED_REG_URL = "net.slp.serializedRegUrl";

	/**
	 * A 32 bit integer giving the server socket queue length for SAs/DAs.<br />
	 * <br />
	 * Default is 10.<br />
	 * <br />
	 * <i>SBLIM specific extension to RFC 2614</i>
	 * 
	 */
	public static final String NET_SLP_SERVER_SOCKET_QUEUE_LENGTH = "net.slp.serverSocketQueueLength";

	/**
	 * A 32 bit integer giving the TCP timeout in milliseconds.<br />
	 * <br />
	 * The default is 20000 ms. <br />
	 * <br />
	 * <i>SBLIM specific extension to RFC 2614</i>
	 * 
	 */
	public static final String NET_SLP_TCPTIMEOUT = "net.slp.TCPTimeout";

	/**
	 * A boolean controlling printing of messages about traffic with DAs. <br />
	 * <br />
	 * Default is <code>false</code>.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_TRACE_DA_TRAFFIC = "net.slp.traceDATraffic";

	/**
	 * A boolean controlling printing details when a SLP message is dropped for
	 * any reason. <br />
	 * <br />
	 * Default is <code>false</code>.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_TRACE_DROP = "net.slp.traceDrop";

	/**
	 * A boolean controlling printing of details on SLP messages. The fields in
	 * all incoming messages and outgoing replies are printed. <br />
	 * <br />
	 * Default is <code>false</code>.
	 */
	public static final String NET_SLP_TRACE_MSG = "net.slp.traceMsg";

	/**
	 * A boolean controlling dumps of all registered services upon registration
	 * and deregistration. If true, the contents of the DA or SA server are
	 * dumped after a registration or deregistration occurs. <br />
	 * <br />
	 * Default is false.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_TRACE_REG = "net.slp.traceReg";

	/**
	 * A value-list of service type names. In the absence of any DAs, UAs
	 * perform SA discovery for finding scopes. These SA discovery requests may
	 * contain a request for service types as an attribute.<br />
	 * <br />
	 * The API implementation will use the service type names supplied by this
	 * property to discover only those SAs (and their scopes) which support the
	 * desired service type or types. For example, if net.slp.typeHint is set to
	 * "service:imap,service:pop3" then SA discovery requests will include the
	 * search filter:<br />
	 * <br />
	 * <code>(|(service-type=service:imap)(service-type=service:pop3))</code><br />
	 * <br />
	 * 
	 * The API library can also use unicast to contact the discovered SAs for
	 * subsequent requests for these service types, to optimize network access.<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_TYPE_HINT = "net.slp.typeHint";

	/**
	 * A value-list of strings indicating the only scopes a UA or SA is allowed
	 * to use when making requests or registering, or the scopes a DA must
	 * support.<br />
	 * If not present for the DA and SA, then in the absence of scope
	 * information from DHCP, the default scope "DEFAULT" is used. If not
	 * present for the UA, and there is no scope information available from
	 * DHCP, then the user scoping model is in force. <br />
	 * Active and passive DA discovery or SA discovery are used for scope
	 * discovery, and the scope "DEFAULT" is used if no other information is
	 * available. <br />
	 * If a DA or SA gets another scope in a request, a SCOPE_NOT_SUPPORTED
	 * error should be returned, unless the request was multicast, in which case
	 * it should be dropped. If a DA gets another scope in a registration, a
	 * SCOPE_NOT_SUPPORTED error must be returned.
	 */
	public static final String NET_SLP_USE_SCOPES = "net.slp.useScopes";

	/**
	 * Trace level. Can be ALL, INFO, WARNING, ERROR, OFF<br />
	 * <br />
	 * <i>Not evaluated by SBLIM SLP client</i>
	 */
	public static final String NET_SLP_TRC_LEVEL = "net.slp.trc.level";

}
