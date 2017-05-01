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
 * 1729361    2007-06-04  lupusalex    Multicast discovery is broken in DiscovererSLP
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 1892103    2008-02-13  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.cimclient.internal.discovery.slp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;

import org.sblim.cimclient.discovery.Discoverer;
import org.sblim.cimclient.discovery.WBEMServiceAdvertisement;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.slp.Locator;
import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceLocationManager;
import org.sblim.slp.ServiceType;
import org.sblim.slp.ServiceURL;
import org.sblim.slp.internal.SLPDefaults;

/**
 * Class DiscovererSLP is the SLP specific implementation of the Discoverer
 * interface.
 * 
 * @since 2.0.2
 */
public class DiscovererSLP extends Object implements Discoverer {

	private static final String SERVICE_WBEM = "service:wbem";

	private static final ServiceType SERVICE_TYPE = new ServiceType(SERVICE_WBEM);

	private static final Vector<String> ATTRIBUTES = new Vector<String>();

	private Locale iLocale;

	private Vector<String> iScopes;

	/**
	 * Ctor.
	 * 
	 * @param pLocale
	 *            The locale setting to use for the Locator
	 */
	public DiscovererSLP(Locale pLocale) {
		this.iLocale = pLocale;
		this.iScopes = new Vector<String>();
		this.iScopes.add(SLPDefaults.DEFAULT_SCOPE);
	}

	/**
	 * Returns locale
	 * 
	 * @return The value of locale.
	 */
	public Locale getLocale() {
		return this.iLocale;
	}

	/**
	 * Sets locale
	 * 
	 * @param pLocale
	 *            The new value of locale.
	 */
	public void setLocale(Locale pLocale) {
		this.iLocale = pLocale;
	}

	/**
	 * Returns scopes
	 * 
	 * @return The value of scopes.
	 */
	public Vector<String> getScopes() {
		return this.iScopes;
	}

	/**
	 * Sets scopes
	 * 
	 * @param pScopes
	 *            The new value of scopes.
	 */
	public void setScopes(Vector<String> pScopes) {
		this.iScopes = pScopes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sblim.cimclient.discovery.Discoverer#findWbemServices(String[])
	 */
	public WBEMServiceAdvertisement[] findWbemServices(final String[] pDirectoryUrls) {

		List<WBEMServiceAdvertisement> advertisements = new ArrayList<WBEMServiceAdvertisement>();

		if (pDirectoryUrls != null && pDirectoryUrls.length > 0) {
			for (int i = 0; i < pDirectoryUrls.length; ++i) {
				advertisements.addAll(findWbemServices(pDirectoryUrls[i]));
			}
		} else {
			advertisements.addAll(findWbemServices((String) null));
		}

		return advertisements.toArray(new WBEMServiceAdvertisement[advertisements.size()]);
	}

	private List<WBEMServiceAdvertisement> findWbemServices(final String pDA) {
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		logger.trace(Level.FINEST, "SLP discovery started on DA " + pDA);
		try {

			Vector<InetAddress> agents;
			if (pDA != null) {
				agents = new Vector<InetAddress>();
				InetAddress address = InetAddress.getByName(pDA);
				if (address == null) { return new LinkedList<WBEMServiceAdvertisement>(); }
				agents.add(address);
			} else {
				agents = null;
			}

			final Locator locator = ServiceLocationManager.getLocator(this.iLocale);

			List<WBEMServiceAdvertisement> advertisements = new ArrayList<WBEMServiceAdvertisement>();
			Enumeration<?> serviceEnum = locator.findServices(SERVICE_TYPE, this.iScopes, "",
					agents);

			while (serviceEnum != null && serviceEnum.hasMoreElements()) {
				ServiceURL url;
				try {
					url = (ServiceURL) serviceEnum.nextElement();
				} catch (RuntimeException e) {
					logger.trace(Level.FINE, e.getMessage(), e);
					continue;
				}
				Enumeration<?> attributeEnum = locator.findAttributes(url, this.iScopes,
						ATTRIBUTES, agents);
				List<String> attributes = new ArrayList<String>();
				while (attributeEnum != null && attributeEnum.hasMoreElements()) {
					try {
						attributes.add(attributeEnum.nextElement().toString());
					} catch (RuntimeException e) {
						logger.trace(Level.FINE, e.getMessage(), e);
						continue;
					}
				}
				advertisements.add(new WBEMServiceAdvertisementSLP(pDA, url, attributes));
			}
			logger.trace(Level.FINEST, "SLP discovery completed on DA " + pDA + ". "
					+ advertisements.size() + " WBEM services found");
			logger.exit();
			return advertisements;

		} catch (ServiceLocationException e) {
			LogAndTraceBroker.getBroker().trace(Level.FINE, "SLP discovery failed with error", e);
		} catch (Exception e) {
			LogAndTraceBroker.getBroker()
					.trace(Level.FINE, "Exception during service discovery", e);
		}
		logger.exit();
		return new LinkedList<WBEMServiceAdvertisement>();
	}

	public String[] findDirectoryServices() {
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		try {
			ArrayList<String> agents = new ArrayList<String>();

			final Locator locator = ServiceLocationManager.getLocator(this.iLocale);

			Enumeration<?> serviceEnum = locator.findServices(SLPDefaults.DA_SERVICE_TYPE,
					this.iScopes, "");

			while (serviceEnum != null && serviceEnum.hasMoreElements()) {
				ServiceURL url = (ServiceURL) serviceEnum.nextElement();
				agents.add(url.getHost());
			}

			logger.trace(Level.FINER, "SLP DA discovery completed in local subnet. "
					+ agents.size() + " DAs found:" + agents.toString());
			if (agents.size() == 0) {
				serviceEnum = locator.findServices(SLPDefaults.SA_SERVICE_TYPE, this.iScopes, "");

				while (serviceEnum != null && serviceEnum.hasMoreElements()) {
					ServiceURL url = (ServiceURL) serviceEnum.nextElement();
					agents.add(url.getHost());
				}
				logger.trace(Level.FINER, "SLP SA discovery completed in local subnet. "
						+ agents.size() + " SAs found:" + agents.toString());
			}

			logger.exit();
			return agents.toArray(new String[agents.size()]);

		} catch (ServiceLocationException e) {
			LogAndTraceBroker.getBroker().trace(Level.FINE, "SLP discovery failed with error", e);
		} catch (Exception e) {
			LogAndTraceBroker.getBroker()
					.trace(Level.FINE, "Exception during service discovery", e);
		}
		logger.exit();
		return new String[0];
	}
}
