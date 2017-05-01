/**
 * SLPConfig.java
 *
 * (C) Copyright IBM Corp. 2005, 2010
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
 * ------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 1535793    2006-09-14  lupusalex    Fix&Integrate CIM&SLP configuration classes
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2414503    2008-12-10  raman_arora  parseList not returning populated list
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 3026311    2010-07-07  blaschke-oss Vacuous comparison of integer value
 * 3026360    2010-07-07  blaschke-oss Handle unwritten fields
 */

package org.sblim.slp.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.sblim.slp.SLPConfigProperties;

/**
 * SLPConfig
 * 
 */
public final class SLPConfig {

	private static InetAddress cBroadcastAddress;

	private static SLPConfig cInstance = null;

	private static InetAddress cLocalHostAddress;

	private static InetAddress cLoopBackV4;

	private static InetAddress cLoopBackV6;

	private static InetAddress cMulticastAddress;

	// SRVLOC_MulticastAddress requires scope (FF0s:0:0:0:0:0:0:116)
	private static InetAddress cSRVLOC_MulticastAddress = null;

	// SRVLOC_DA_MulticastAddress requires scope (FF0s:0:0:0:0:0:0:123)
	private static InetAddress cSRVLOC_DA_MulticastAddress = null;

	private static InetAddress getByName(String pName) {
		try {
			return InetAddress.getByName(pName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	static {
		cLoopBackV4 = getByName(SLPDefaults.LOOPBACK_ADDRESS_V4);
		cLoopBackV6 = getByName(SLPDefaults.LOOPBACK_ADDRESS_V6);
		cMulticastAddress = getByName(SLPDefaults.MULTICAST_ADDRESS);
		cBroadcastAddress = getByName(SLPDefaults.BROADCAST_ADDRESS);
		try {
			cLocalHostAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getBroadcastAddress
	 * 
	 * @return InetAddress
	 */
	public static InetAddress getBroadcastAddress() {
		return cBroadcastAddress;
	}

	/**
	 * getGlobalCfg
	 * 
	 * @return SLPConfig
	 */
	public static synchronized SLPConfig getGlobalCfg() {
		if (cInstance == null) {
			cInstance = new SLPConfig();
		}
		return cInstance;
	}

	/**
	 * getLoopbackV4
	 * 
	 * @return InetAddress
	 */
	public static InetAddress getLoopbackV4() {
		return cLoopBackV4;
	}

	/**
	 * getLoopbackV6
	 * 
	 * @return InetAddress
	 */
	public static InetAddress getLoopbackV6() {
		return cLoopBackV6;
	}

	/**
	 * getMulticastAddress
	 * 
	 * @return InetAddress
	 */
	public static InetAddress getMulticastAddress() {
		return cMulticastAddress;
	}

	/**
	 * getSRVLOC_MulticastAddress
	 * 
	 * @return InetAddress
	 */
	public static InetAddress getSRVLOC_MulticastAddress() {
		return cSRVLOC_MulticastAddress;
	}

	/**
	 * getSRVLOC_DA_MulticastAddress
	 * 
	 * @return InetAddress
	 */
	public static InetAddress getSRVLOC_DA_MulticastAddress() {
		return cSRVLOC_DA_MulticastAddress;
	}

	private static int getIntProperty(String pName, int pDefaultValue, int pMinValue, int pMaxValue) {
		int value = Integer.getInteger(pName, pDefaultValue).intValue();
		return Math.min(pMaxValue, Math.max(pMinValue, value));
	}

	private SLPConfig() {

		try {
			// try to load config class from CIM client. This will cause load of
			// the CIM client's config file.
			Class.forName("org.sblim.cimclient.internal.util.WBEMConfiguration");
		} catch (ClassNotFoundException e) {
			// nothing to do
		}

		Properties slpProperties = new Properties(new Properties());
		try {
			InputStream inputstream = getConfigURLStream();
			if (inputstream != null) {
				slpProperties.load(inputstream);
			}
			for (Iterator<Entry<Object, Object>> iterator = slpProperties.entrySet().iterator(); iterator
					.hasNext();) {
				Entry<Object, Object> entry = iterator.next();
				System
						.setProperty(String.valueOf(entry.getKey()), String.valueOf(entry
								.getValue()));
			}
		} catch (IOException e) {
			System.out.println("Error while parsing property file");
			e.printStackTrace();
		}
	}

	/**
	 * getActiveDiscoveryGranularity
	 * 
	 * @return int
	 */
	public int getActiveDiscoveryGranularity() {
		return getIntProperty(SLPConfigProperties.NET_SLP_DA_ACTIVE_DISCOVERY_GRANULARITY,
				SLPDefaults.ACTIVE_DISCOVERY_GRANULARITY, SLPLimits.MIN_DISCOVERY_GRANULARITY,
				SLPLimits.MAX_DISCOVERY_GRANULARITY);
	}

	/**
	 * getActiveDiscoveryInterval
	 * 
	 * @return int
	 */
	public int getActiveDiscoveryInterval() {
		int interval = getIntProperty(SLPConfigProperties.NET_SLP_DA_ACTIVE_DISCOVERY_INTERVAL,
				SLPDefaults.ACTIVE_DISCOVERY_INTERVAL, SLPLimits.MIN_DISCOVERY_INTERVAL,
				SLPLimits.MAX_DISCOVERY_INTERVAL); // 3mins
		if (interval > 0 && interval < 300) { return 300;
		// prevent network flooding
		}
		return interval;
	}

	/**
	 * getDADiscoveryTimeouts
	 * 
	 * @return int[]
	 */
	public int[] getDADiscoveryTimeouts() {
		return parseTimeouts(SLPConfigProperties.NET_SLP_DA_DISCOVERY_TIMEOUTS,
				SLPDefaults.DA_DISCOVERY_TIMEOUTS);
	}

	/**
	 * getDatagramTimeouts
	 * 
	 * @return int[]
	 */
	public int[] getDatagramTimeouts() {
		return parseTimeouts(SLPConfigProperties.NET_SLP_DATAGRAM_TIMEOUTS,
				SLPDefaults.DATAGRAM_TIMEOUTS);
	}

	/**
	 * getInterfaces
	 * 
	 * @return List
	 */
	@SuppressWarnings("null")
	public List<InetAddress> getInterfaces() {

		String property = System.getProperty(SLPConfigProperties.NET_SLP_INTERFACES);

		List<String> addresses = parseList(property);

		final int count = addresses == null ? 0 : addresses.size();
		List<InetAddress> interfaces = new ArrayList<InetAddress>(count);

		if (count == 0) {
			interfaces.add(cLocalHostAddress);
			interfaces.add(cLoopBackV4);
			interfaces.add(cLoopBackV6);
		} else {
			for (int i = 0; i < count; ++i) {
				String address = addresses.get(i);
				try {
					InetAddress inetAddress = InetAddress.getByName(address);
					if (!interfaces.contains(inetAddress)) {
						if (inetAddress.equals(cLocalHostAddress)) {
							interfaces.add(0, inetAddress);
						} else {
							interfaces.add(inetAddress);
						}
					}
				} catch (UnknownHostException e) {
					// TODO log this
					continue;
				}
			}
		}
		return interfaces;
	}

	/**
	 * getPort
	 * 
	 * @return int
	 */
	public int getPort() {
		return Integer.getInteger(SLPConfigProperties.NET_SLP_PORT, SLPDefaults.SLP_PORT)
				.intValue();
	}

	/**
	 * setPort
	 * 
	 * @param pPort
	 */
	public void setPort(int pPort) {
		System.setProperty(SLPConfigProperties.NET_SLP_PORT, Integer.toString(pPort));
	}

	/**
	 * getTraceLevel
	 * 
	 * @return String
	 */
	public Level getTraceLevel() {
		String str = System.getProperty(SLPConfigProperties.NET_SLP_TRC_LEVEL);
		if ("OFF".equalsIgnoreCase(str)) return Level.OFF;
		if ("ERROR".equalsIgnoreCase(str)) return Level.SEVERE;
		if ("WARNING".equalsIgnoreCase(str)) return Level.WARNING;
		if ("INFO".equalsIgnoreCase(str)) return Level.INFO;
		if ("ALL".equalsIgnoreCase(str)) return Level.FINEST;
		return Level.SEVERE;
	}

	/**
	 * setTraceLevel
	 * 
	 * @param pLevel
	 */
	public void setTraceLevel(String pLevel) {
		System.setProperty(SLPConfigProperties.NET_SLP_TRC_LEVEL, pLevel);
	}

	/**
	 * setUseIPv6
	 * 
	 * @param pValue
	 */
	public void setUseIPv6(boolean pValue) {
		System.setProperty(SLPConfigProperties.NET_SLP_USEIPV6, Boolean.toString(pValue));
	}

	/**
	 * useIPv6
	 * 
	 * @return boolean
	 */
	public boolean useIPv6() {
		return getBoolean(SLPConfigProperties.NET_SLP_USEIPV6, SLPDefaults.USE_IPV6);
	}

	/**
	 * setUseIPv4
	 * 
	 * @param pValue
	 */
	public void setUseIPv4(boolean pValue) {
		System.setProperty(SLPConfigProperties.NET_SLP_USEIPV4, Boolean.valueOf(pValue).toString());
	}

	/**
	 * useIPv4
	 * 
	 * @return boolean
	 */
	public boolean useIPv4() {
		return getBoolean(SLPConfigProperties.NET_SLP_USEIPV4, SLPDefaults.USE_IPV4);
	}

	/**
	 * getLocalHost
	 * 
	 * @return InetAddress
	 */
	public InetAddress getLocalHost() {
		List<InetAddress> interfaces = getInterfaces();
		return interfaces.get(0);
	}

	/**
	 * getMaximumResults
	 * 
	 * @return int
	 */
	public int getMaximumResults() {
		int value = Integer.getInteger(SLPConfigProperties.NET_SLP_MAX_RESULTS,
				SLPDefaults.MAXIMUM_RESULTS).intValue();
		return (value >= 1 && value < SLPDefaults.MAXIMUM_RESULTS) ? value
				: SLPDefaults.MAXIMUM_RESULTS;
	}

	/**
	 * getMTU
	 * 
	 * @return int
	 */
	public int getMTU() {
		return getIntProperty(SLPConfigProperties.NET_SLP_MTU, SLPDefaults.MTU, SLPLimits.MIN_MTU,
				SLPLimits.MAX_MTU);
	}

	/**
	 * getMulticastMaximumWait
	 * 
	 * @return int
	 */
	public int getMulticastMaximumWait() {
		return getIntProperty(SLPConfigProperties.NET_SLP_MULTICAST_MAXIMUM_WAIT,
				SLPDefaults.MULTICAST_MAXIMUM_WAIT, SLPLimits.MIN_MULTICAST_MAXIMUM_WAIT,
				SLPLimits.MAX_MULTICAST_MAXIMUM_WAIT);
	}

	/**
	 * getMulticastRadius
	 * 
	 * @return int
	 */
	public int getMulticastRadius() {
		return getIntProperty(SLPConfigProperties.NET_SLP_MULTICAST_TTL,
				SLPDefaults.MULTICAST_RADIUS, SLPLimits.MIN_MULTICAST_RADIUS,
				SLPLimits.MAX_MULTICAST_RADIUS);
	}

	/**
	 * getMulticastTimeouts
	 * 
	 * @return int[]
	 */
	public int[] getMulticastTimeouts() {
		return parseTimeouts(SLPConfigProperties.NET_SLP_MULTICAST_TIMEOUTS,
				SLPDefaults.MULTICAST_TIMEOUTS);
	}

	/**
	 * getPreconfiguredDAs
	 * 
	 * @return List <InetAddress>
	 */
	public List<InetAddress> getPreconfiguredDAs() {
		String addressString = System.getProperty(SLPConfigProperties.NET_SLP_DA_ADDRESSES, "");
		List<String> addresses = parseList(addressString);
		if (addresses == null) return null;
		List<InetAddress> result = new ArrayList<InetAddress>();
		for (Iterator<String> iter = addresses.iterator(); iter.hasNext();) {
			try {
				result.add(InetAddress.getByName(iter.next()));
			} catch (UnknownHostException e) {
				// TODO: log this
			}
		}
		return result;
	}

	/**
	 * getConfiguredScopes
	 * 
	 * @return List<String>
	 */
	public List<String> getConfiguredScopes() {
		List<String> scopes = parseList(SLPConfigProperties.NET_SLP_USE_SCOPES);
		if (scopes == null) scopes = new ArrayList<String>();
		if (scopes.size() == 0) scopes.add(SLPDefaults.DEFAULT_SCOPE);
		return scopes;
	}

	/**
	 * getSAOnlyScopes
	 * 
	 * @return List<String>
	 */
	public List<String> getSAOnlyScopes() {
		return parseList(SLPConfigProperties.NET_SLP_SAONLY_SCOPES);
	}

	/**
	 * getServerSocketQueueLength
	 * 
	 * @return int
	 */
	public int getServerSocketQueueLength() {
		return getIntProperty(SLPConfigProperties.NET_SLP_SERVER_SOCKET_QUEUE_LENGTH,
				SLPDefaults.SERVER_SOCKET_QUEUE_LENGTH, SLPLimits.MIN_SERVER_SOCKET_QUEUE_LENGTH,
				SLPLimits.MAX_SERVER_SOCKET_QUEUE_LENGTH);
	}

	/**
	 * getTCPTimeout
	 * 
	 * @return int
	 */
	public int getTCPTimeout() {
		return getIntProperty(SLPConfigProperties.NET_SLP_TCPTIMEOUT, SLPDefaults.TCP_TIMEOUT,
				SLPLimits.MIN_TCP_TIMEOUT, SLPLimits.MAX_TCP_TIMEOUT);
	}

	/**
	 * getTraceMsg
	 * 
	 * @return boolean
	 */
	public boolean getTraceMsg() {
		return Boolean.getBoolean(SLPConfigProperties.NET_SLP_TRACE_MSG);
	}

	/**
	 * isBroadcastOnly
	 * 
	 * @return boolean
	 */
	public boolean isBroadcastOnly() {
		return Boolean.getBoolean(SLPConfigProperties.NET_SLP_IS_BROADCAST_ONLY);
	}

	/**
	 * isDA
	 * 
	 * @return boolean
	 */
	public boolean isDA() {
		return Boolean.getBoolean(SLPConfigProperties.NET_SLP_IS_DA);
	}

	/**
	 * isSA
	 * 
	 * @return boolean
	 */
	public boolean isSA() {
		return !isDA();
	}

	private static boolean getBoolean(String pPropName, boolean pDefaultValue) {
		if (System.getProperty(pPropName) == null) return pDefaultValue;
		return Boolean.getBoolean(pPropName);
	}

	private InputStream getConfigURLStream() {

		String configURL = System.getProperty(SLPConfigProperties.NET_SLP_CONFIG_URL);

		if (configURL != null) {
			if (configURL.trim().length() > 0) {
				try {
					URL url = new URL(configURL);
					InputStream inputstream = url.openStream();
					return inputstream;
				} catch (MalformedURLException e) {
					// TODO log this
				} catch (IOException e) {
					// TODO LOG this... config file not found
				}
			}
			return null;
		}
		for (int i = 0; i < SLPDefaults.CONF_URLS.length; ++i) {
			configURL = SLPDefaults.CONF_URLS[i];
			try {
				URL url = new URL(configURL);
				InputStream inputstream = url.openStream();
				return inputstream;
			} catch (Exception e) {
				// Nothing to do
			}
		}
		return null;
	}

	/**
	 * Parses comma separated list.
	 * 
	 * @param pList
	 * @return List of Strings
	 */
	private static List<String> parseList(String pListStr) {
		if (pListStr == null || pListStr.length() == 0) return null;
		// TODO
		StringTokenizer tokenizer = new StringTokenizer(pListStr, ",");
		if (tokenizer.countTokens() == 0) return null;
		List<String> list = new ArrayList<String>(tokenizer.countTokens());
		while (tokenizer.hasMoreTokens()) {
			String str = tokenizer.nextToken().trim();
			if (str.length() == 0) continue;
			list.add(str);
		}
		return list.size() == 0 ? null : list;
	}

	private int[] parseTimeouts(String pPropertyName, int[] pDefaultTimeouts) {
		String value = System.getProperty(pPropertyName);

		List<String> list = parseList(value);
		if (list == null) return pDefaultTimeouts;

		int timeouts[] = new int[list.size()];
		for (int pos = 0; pos < list.size(); pos++) {
			String timeoutElem = list.get(pos);
			try {
				timeouts[pos] = Integer.parseInt(timeoutElem);
			} catch (NumberFormatException e) {
				return pDefaultTimeouts;
			}
		}
		return timeouts;
	}
}
