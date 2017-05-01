/**
 * SLPDefaults.java
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
 * 1535793    2006-09-14  lupusalex    Fix&Integrate CIM&SLP configuration classes
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 1911400    2008-03-10  blaschks-oss Source RPM file on SourceForge is broken
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.slp.internal;

import java.util.Locale;

import org.sblim.slp.ServiceType;

/**
 * SLPDefaults
 * 
 */
/**
 * Class SLPDefaults is responsible for
 * 
 */
public interface SLPDefaults {

	/**
	 * ALL_AUTHORITIES
	 */
	public static final String ALL_AUTHORITIES = "*";

	/**
	 * BROADCAST_ADDRESS
	 */
	public static final String BROADCAST_ADDRESS = "255.255.255.255";

	/**
	 * CONF_URLS
	 */
	public static final String[] CONF_URLS = {
			"file:sblim-slp-client2.properties",
			"file:" + System.getProperty("user.home") + System.getProperty("file.separator")
					+ "sblim-slp-client2.properties",
			"file:/etc/java/sblim-slp-client2.properties", "file:/etc/sblim-slp-client2.properties" };

	/**
	 * DA_DISCOVERY_TIMEOUTS
	 */
	public static final int DA_DISCOVERY_TIMEOUTS[] = { 200, 200, 200, 200, 300, 400 };

	/**
	 * DA_SERVICE_TYPE
	 */
	public static final ServiceType DA_SERVICE_TYPE = new ServiceType("service:directory-agent");

	/**
	 * DATAGRAM_TIMEOUTS
	 */
	public static final int DATAGRAM_TIMEOUTS[] = { 100, 200, 300 };

	/**
	 * DEFAULT_SCOPE
	 */
	public static final String DEFAULT_SCOPE = "default";

	/**
	 * ACTIVE_DISCOVERY_GRANULARITY
	 */
	public static final int ACTIVE_DISCOVERY_GRANULARITY = 900;

	/**
	 * ACTIVE_DISCOVERY_INTERVAL
	 */
	public static final int ACTIVE_DISCOVERY_INTERVAL = 900;

	/**
	 * LOCALE
	 */
	public static final Locale LOCALE = new Locale("en", "");

	/**
	 * LOG_FILE
	 */
	public static final String LOG_FILE = "slp.log";

	/**
	 * LOOPBACK_ADDRESS_V4
	 */
	public static final String LOOPBACK_ADDRESS_V4 = "127.0.0.1";

	/**
	 * LOOPBACK_ADDRESS_V6
	 */
	public static final String LOOPBACK_ADDRESS_V6 = "::1";

	/**
	 * LOOPBACK_NAME
	 */
	public static final String LOOPBACK_NAME = "localhost";

	/**
	 * MAXIMUM_RESULTS
	 */
	public static final int MAXIMUM_RESULTS = 0x7fffffff;

	/**
	 * MTU
	 */
	public static final int MTU = 1400;

	/**
	 * MULTICAST_ADDRESS
	 */
	public static final String MULTICAST_ADDRESS = "239.255.255.253";

	/**
	 * MULTICAST_MAXIMUM_WAIT
	 */
	public static final int MULTICAST_MAXIMUM_WAIT = 2000;

	/**
	 * MULTICAST_RADIUS
	 */
	public static final int MULTICAST_RADIUS = 255;

	/**
	 * MULTICAST_TIMEOUTS
	 */
	public static final int MULTICAST_TIMEOUTS[] = { 200, 200, 200, 200, 300, 400 };

	/**
	 * SA_SERVICE_TYPE
	 */
	public static final ServiceType SA_SERVICE_TYPE = new ServiceType("service:service-agent");

	/**
	 * SERVER_SOCKET_QUEUE_LENGTH
	 */
	public static final int SERVER_SOCKET_QUEUE_LENGTH = 10;

	/**
	 * SLP_PORT
	 */
	public static final int SLP_PORT = 427;

	/**
	 * USE_IPV6
	 */
	public static final boolean USE_IPV6 = true;

	/**
	 * USE_IPV4
	 */
	public static final boolean USE_IPV4 = true;

	/**
	 * SLP_VERSION
	 */
	public static final int SLP_VERSION = 2;

	/**
	 * TCP_TIMEOUT
	 */
	public static final int TCP_TIMEOUT = 20000;

	/**
	 * ENCODING
	 */
	public static final String ENCODING = "UTF-8";

	/**
	 * DA list is rediscovered if DACACHE_TIMEOUT (seconds) is elapsed.
	 */
	public static final int DACACHE_TIMEOUT = 90;

	/**
	 * IPV6_MULTICAST_SCOPE
	 */
	public static final int IPV6_MULTICAST_SCOPE = 5;

}
