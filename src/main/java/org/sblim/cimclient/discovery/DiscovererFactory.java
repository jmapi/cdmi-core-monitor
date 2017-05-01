/**
 * (C) Copyright IBM Corp. 2007, 2009
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
 * 1678915    2007-03-12  lupusalex    Integrated WBEM service discovery via SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.discovery;

import java.util.Locale;

import org.sblim.cimclient.internal.discovery.slp.DiscovererSLP;

/**
 * Class DiscovererFactory is responsible for creating concrete instances of the
 * Discoverer interface.
 * 
 * @threading This class is thread-safe.
 * @since 2.0.2
 */
public class DiscovererFactory {

	/**
	 * The Service Location Protocol (SLP)
	 */
	public static final String SLP = "SLP";

	private static final String[] cProtocols = new String[] { SLP };

	/**
	 * Returns the concrete Discoverer for a given discovery protocol.
	 * 
	 * @param pProtocol
	 *            The discovery protocol, e.g. "SLP"
	 * @return The corresponding discoverer
	 * @throws IllegalArgumentException
	 *             On unsupported protocols
	 * @pattern Factory Method
	 */
	public static Discoverer getDiscoverer(String pProtocol) throws IllegalArgumentException {
		if (SLP.equalsIgnoreCase(pProtocol)) { return new DiscovererSLP(Locale.US); }
		throw new IllegalArgumentException("Protocol " + pProtocol + " not supported.");
	}

	/**
	 * Return an array of all supported discovery protocols
	 * 
	 * @return The supported protocols
	 */
	public static String[] getSupportedProtocols() {
		return cProtocols;
	}

	private DiscovererFactory() { /**/}
}
