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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.cimclient.discovery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import javax.security.auth.Subject;
import javax.wbem.client.WBEMClient;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * Class AdvertisementCatalog implements a catalog for WBEM service
 * advertisements. In practice we usually have multiple advertisements for a
 * single service, because the DMTF mandates an advertisement per remote service
 * access point (e.g. http, https, rmi). This class tries to ease the management
 * of this by indexing services by the unique service id and therefore surfacing
 * which advertisements belong to the same service. The application might then
 * choose it's preferred communication mechanism.
 * 
 * @threading No thread synchronization provided, this is the responsibility of
 *            the caller.
 * @since 2.0.2
 */
public class AdvertisementCatalog {

	/**
	 * Interface EventListener specifies listener that are called when an
	 * advertisement is added to or removed from the catalog, expires or is
	 * renewed.
	 * 
	 */
	public static interface EventListener {

		/**
		 * Called when an advertisement is added to the catalog that has not
		 * been a member of the catalog before.
		 * 
		 * @param pEvent
		 *            The type of the event. One of the constants
		 *            <code>EVENT_ADD, EVENT_REMOVE, EVENT_EXPIRE, EVENT_RENEW</code>
		 *            in <code>AdvertisementCatalog</code>.
		 * 
		 * @param pAdvertisment
		 *            The added advertisement
		 */
		public void advertisementEvent(int pEvent, WBEMServiceAdvertisement pAdvertisment);
	}

	/**
	 * Class AdvertisementDecorator decorates a WBEMAdvertisement with state
	 * information required by the AdvertisementCatalog class
	 * 
	 * @pattern Decorator
	 * @invariant <code>iAvertisement!=null</code>
	 */
	private static class AdvertisementDecorator implements WBEMServiceAdvertisement {

		private WBEMServiceAdvertisement iAdvertisement;

		private boolean iRefresh = false;

		/**
		 * Ctor.
		 * 
		 * @param pAdvertisement
		 *            The advertisement to decorate
		 */
		protected AdvertisementDecorator(WBEMServiceAdvertisement pAdvertisement) {
			if (pAdvertisement == null) throw new IllegalArgumentException("Advertisement is null");
			this.iAdvertisement = pAdvertisement;
		}

		/**
		 * Returns advertisement
		 * 
		 * @return The value of advertisement.
		 */
		protected WBEMServiceAdvertisement getAdvertisementXXX() {
			return this.iAdvertisement;
		}

		/**
		 * Returns refresh
		 * 
		 * @return The value of refresh.
		 */
		protected boolean isRefresh() {
			return this.iRefresh;
		}

		/**
		 * Sets advertisement
		 * 
		 * @param pAdvertisement
		 *            The new value of advertisement.
		 */
		protected void setAdvertisement(WBEMServiceAdvertisement pAdvertisement) {
			this.iAdvertisement = pAdvertisement;
		}

		/**
		 * Sets refresh
		 * 
		 * @param pRefresh
		 *            The new value of refresh.
		 */
		protected void setRefresh(boolean pRefresh) {
			this.iRefresh = pRefresh;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object pObj) {
			return this.iAdvertisement.equals(pObj);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return this.iAdvertisement.hashCode();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#createClient(javax.security.auth.Subject,
		 *      java.util.Locale[])
		 */
		public WBEMClient createClient(Subject pSubject, Locale[] pLocales) throws Exception {
			return this.iAdvertisement.createClient(pSubject, pLocales);
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getAttribute(java.lang.String)
		 */
		public String getAttribute(String pAttributeName) {
			return this.iAdvertisement.getAttribute(pAttributeName);
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getAttributes()
		 */
		public Set<Entry<String, String>> getAttributes() {
			return this.iAdvertisement.getAttributes();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getConcreteServiceType()
		 */
		public String getConcreteServiceType() {
			return this.iAdvertisement.getConcreteServiceType();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getDirectory()
		 */
		public String getDirectory() {
			return this.iAdvertisement.getDirectory();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getInteropNamespaces()
		 */
		public String[] getInteropNamespaces() {
			return this.iAdvertisement.getInteropNamespaces();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getServiceId()
		 */
		public String getServiceId() {
			return this.iAdvertisement.getServiceId();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getServiceUrl()
		 */
		public String getServiceUrl() {
			return this.iAdvertisement.getServiceUrl();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#isExpired()
		 */
		public boolean isExpired() {
			return this.iAdvertisement.isExpired();
		}

		/**
		 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#setExpired(boolean)
		 */
		public void setExpired(boolean pExpired) {
			this.iAdvertisement.setExpired(pExpired);
		}
	}

	/**
	 * Event code when advertisement is added
	 */
	public static int EVENT_ADD = 1;

	/**
	 * Event code when advertisement is removed
	 */
	public static int EVENT_REMOVE = 2;

	/**
	 * Event code when advertisement expires
	 */
	public static int EVENT_EXPIRE = 4;

	/**
	 * Event code when advertisement is renewed
	 */
	public static int EVENT_RENEW = 8;

	private List<EventListener> iListeners = new LinkedList<EventListener>();

	private HashMap<String, List<AdvertisementDecorator>> iCatalogByDirectory = new HashMap<String, List<AdvertisementDecorator>>();

	private Map<String, Map<WBEMProtocol, WBEMServiceAdvertisement>> iCatalogById = new HashMap<String, Map<WBEMProtocol, WBEMServiceAdvertisement>>();

	/**
	 * Ctor.
	 */
	public AdvertisementCatalog() {
	/**/
	}

	/**
	 * Adds a listener for "add" events. The listener will be called whenever a
	 * advertisement is added to the catalog.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public void addEventListener(EventListener pListener) {
		this.iListeners.add(pListener);
	}

	/**
	 * Adds new advertisements to the catalog. Existing advertisements sharing
	 * concrete type, url and directory are replaced.
	 * 
	 * @param pAdvertisements
	 *            The new advertisements
	 */
	public void addAdvertisements(WBEMServiceAdvertisement[] pAdvertisements) {
		for (int i = 0; i < pAdvertisements.length; ++i) {
			String url = "";
			try {
				WBEMServiceAdvertisement advertisement = pAdvertisements[i];
				url = advertisement.getServiceUrl();
				WBEMProtocol protocol = makeProtocol(advertisement);
				String serviceId = advertisement.getAttribute(WBEMServiceAdvertisement.SERVICE_ID);
				{
					List<AdvertisementDecorator> innerList = this.iCatalogByDirectory
							.get(advertisement.getDirectory());
					if (innerList == null) {
						innerList = new ArrayList<AdvertisementDecorator>();
						this.iCatalogByDirectory.put(advertisement.getDirectory(), innerList);
					}
					AdvertisementDecorator entry = findAdvertisement(innerList, advertisement);
					if (entry != null) {
						boolean wasExpired = entry.isExpired();
						entry.setAdvertisement(advertisement);
						entry.setRefresh(false);
						entry.setExpired(false);
						if (wasExpired) {
							notifyEventListeners(EVENT_RENEW, advertisement);
						}
					} else {
						innerList.add(new AdvertisementDecorator(advertisement));
						notifyEventListeners(EVENT_ADD, advertisement);
					}
				}
				{
					Map<WBEMProtocol, WBEMServiceAdvertisement> innerMap = this.iCatalogById
							.get(serviceId);
					if (innerMap == null) {
						innerMap = new HashMap<WBEMProtocol, WBEMServiceAdvertisement>();
						this.iCatalogById.put(serviceId, innerMap);
					}
					innerMap.put(protocol, advertisement);
				}
			} catch (Exception e) {
				LogAndTraceBroker.getBroker().trace(Level.FINE,
						"Incomplete advertisement for" + url, e);
			}
		}
	}

	/**
	 * Returns the advertisement from the catalog corresponding to a given id
	 * and with the protocol preferred most.
	 * 
	 * @param pId
	 *            The service id
	 * @param pProtocols
	 *            An array containing the desired protocols in order of
	 *            preference. If a service doesn't advertise any of the given
	 *            protocols this service returns null.
	 * @return The corresponding advertisement
	 */
	public WBEMServiceAdvertisement getAdvertisement(final String pId,
			final WBEMProtocol[] pProtocols) {
		Map<WBEMProtocol, WBEMServiceAdvertisement> innerMap = this.iCatalogById.get(pId);
		if (innerMap == null) { return null; }
		for (int i = 0; i < pProtocols.length; ++i) {
			WBEMServiceAdvertisement advertisement = innerMap.get(pProtocols[i]);
			if (advertisement != null) { return advertisement; }
		}
		return null;
	}

	/**
	 * Returns the advertisements from the catalog corresponding to a given
	 * directory
	 * 
	 * @param pDirectory
	 *            The directory service
	 * @return The corresponding advertisements
	 */
	public WBEMServiceAdvertisement[] getAdvertisementsByDirectory(String pDirectory) {
		List<AdvertisementDecorator> result = this.iCatalogByDirectory.get(pDirectory);
		return (result != null ? (WBEMServiceAdvertisement[]) result
				.toArray(new WBEMServiceAdvertisement[result.size()])
				: new WBEMServiceAdvertisement[] {});
	}

	/**
	 * Returns the advertisements from the catalog corresponding to a given id
	 * 
	 * @param pId
	 *            The service id
	 * @return The corresponding advertisements
	 */
	public WBEMServiceAdvertisement[] getAdvertisementsById(String pId) {
		Map<WBEMProtocol, WBEMServiceAdvertisement> innerMap = this.iCatalogById.get(pId);
		if (innerMap == null) { return null; }
		Collection<WBEMServiceAdvertisement> advertisements = innerMap.values();
		return advertisements.toArray(new WBEMServiceAdvertisement[advertisements.size()]);
	}

	/**
	 * Returns an array of service ids known by this catalog
	 * 
	 * @return The service ids
	 */
	public String[] getKnownIds() {
		Set<String> ids = this.iCatalogById.keySet();
		return ids.toArray(new String[ids.size()]);
	}

	/**
	 * Refreshes the advertisements from a given directory. All existing
	 * advertisements from this directory are deleted first before the new ones
	 * are added.
	 * 
	 * @param pDirectory
	 *            The directory services we got the advertisements from
	 * @param pAdvertisements
	 *            The advertisements
	 */
	public void refreshAdvertisements(final String[] pDirectory,
			WBEMServiceAdvertisement[] pAdvertisements) {
		for (int i = 0; i < pDirectory.length; ++i) {
			markRefresh(pDirectory[i]);
		}
		addAdvertisements(pAdvertisements);
		for (int i = 0; i < pDirectory.length; ++i) {
			expire(pDirectory[i]);
		}
	}

	/**
	 * Removes a listener
	 * 
	 * @param pListener
	 *            The listener to remove
	 */
	public void removeEventListener(EventListener pListener) {
		this.iListeners.remove(pListener);
	}

	/**
	 * Removes the expired advertisements from the catalog.
	 * 
	 * @param pDirectory
	 *            When <code>not null</code> only the expired advertisements of
	 *            the given directory are removed. Otherwise all expired
	 *            advertisements are removed.
	 */
	public void removeExpired(String pDirectory) {

		if (pDirectory == null) {
			Iterator<String> iter = this.iCatalogByDirectory.keySet().iterator();
			while (iter.hasNext()) {
				removeExpired(iter.next());
			}
			return;
		}

		List<AdvertisementDecorator> advertisementList = this.iCatalogByDirectory.get(pDirectory);
		Iterator<AdvertisementDecorator> iter = advertisementList.iterator();
		while (iter.hasNext()) {
			AdvertisementDecorator decorator = iter.next();
			if (decorator.isExpired()) {
				iter.remove();
				notifyEventListeners(EVENT_REMOVE, decorator);
				Map<WBEMProtocol, WBEMServiceAdvertisement> innerMap = this.iCatalogById
						.get(decorator.getServiceId());
				innerMap.remove(makeProtocol(decorator));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("AdvertisementCatalog:");
		Iterator<Entry<String, Map<WBEMProtocol, WBEMServiceAdvertisement>>> outer = this.iCatalogById
				.entrySet().iterator();
		while (outer.hasNext()) {
			Entry<String, Map<WBEMProtocol, WBEMServiceAdvertisement>> entry = outer.next();
			if (entry.getValue() == null) {
				continue;
			}
			result.append("[service-id:\"");
			result.append(entry.getKey());
			result.append("\"");
			Map<WBEMProtocol, WBEMServiceAdvertisement> innerMap = entry.getValue();
			Iterator<Entry<WBEMProtocol, WBEMServiceAdvertisement>> inner = innerMap.entrySet()
					.iterator();
			while (inner.hasNext()) {
				Entry<WBEMProtocol, WBEMServiceAdvertisement> innerEntry = inner.next();
				result.append("[");
				result.append(innerEntry.getKey().toString());
				result.append("]");
			}
			result.append("]");
		}
		return result.toString();
	}

	private void expire(String pDirectory) {
		List<AdvertisementDecorator> advertisementList = this.iCatalogByDirectory.get(pDirectory);
		if (advertisementList == null) { return; }
		Iterator<AdvertisementDecorator> iter = advertisementList.iterator();
		while (iter.hasNext()) {
			AdvertisementDecorator advertisement = iter.next();
			if (advertisement.isRefresh()) {
				advertisement.setRefresh(false);
				advertisement.setExpired(true);
				notifyEventListeners(EVENT_EXPIRE, advertisement);
			}
		}
	}

	private AdvertisementDecorator findAdvertisement(List<AdvertisementDecorator> pList,
			WBEMServiceAdvertisement pAdvertisement) {
		Iterator<AdvertisementDecorator> iter = pList.iterator();
		while (iter.hasNext()) {
			AdvertisementDecorator entry = iter.next();
			if (entry.getServiceUrl().equals(pAdvertisement.getServiceUrl())) { return entry; }
		}
		return null;
	}

	private WBEMProtocol makeProtocol(WBEMServiceAdvertisement advertisement) {
		String presentation = advertisement.getAttribute(WBEMServiceAdvertisement.COMM_MECHANISM);
		if ("OTHER".equalsIgnoreCase(presentation)) {
			presentation = advertisement
					.getAttribute(WBEMServiceAdvertisement.OTHER_COMM_MECHN_DESC);
		}
		String transport = advertisement.getServiceUrl().split(":", 2)[0];
		WBEMProtocol protocol = new WBEMProtocol(transport, presentation);
		return protocol;
	}

	private void markRefresh(String pDirectory) {
		List<AdvertisementDecorator> advertisementList = this.iCatalogByDirectory.get(pDirectory);
		if (advertisementList == null) { return; }
		Iterator<AdvertisementDecorator> iter = advertisementList.iterator();
		while (iter.hasNext()) {
			AdvertisementDecorator advertisement = iter.next();
			advertisement.setRefresh(true);
		}
	}

	private void notifyEventListeners(int pEvent, WBEMServiceAdvertisement pAdvertisement) {
		Iterator<EventListener> iter = this.iListeners.iterator();
		while (iter.hasNext()) {
			iter.next().advertisementEvent(pEvent, pAdvertisement);
		}
	}

}
