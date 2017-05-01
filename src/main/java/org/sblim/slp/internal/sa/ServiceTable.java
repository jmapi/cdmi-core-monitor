/**
 * (C) Copyright IBM Corp. 2007, 2010
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3023349    2010-07-02  blaschke-oss SLP uses # constructor instead of valueOf
 */

package org.sblim.slp.internal.sa;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.sblim.slp.ServiceLocationAttribute;
import org.sblim.slp.ServiceType;
import org.sblim.slp.ServiceURL;
import org.sblim.slp.internal.IPv6MulticastAddressFactory;
import org.sblim.slp.internal.Net;
import org.sblim.slp.internal.SLPConfig;
import org.sblim.slp.internal.SLPDefaults;
import org.sblim.slp.internal.TRC;

/**
 * ServiceTable
 * 
 */
public class ServiceTable {

	DatagramThread iDgramThread;

	private boolean iUseV6 = Net.hasIPv6() && SLPConfig.getGlobalCfg().useIPv6();

	class AddressHashTable {

		class Counter {

			/**
			 * iValue
			 */
			public int iValue = 1;
		}

		/**
		 * AddressHash -> Counter
		 */
		private HashMap<Integer, Counter> iMap = new HashMap<Integer, Counter>();

		/**
		 * register
		 * 
		 * @param pType
		 * @throws UnknownHostException
		 * @throws IOException
		 */
		public void register(ServiceType pType) throws UnknownHostException, IOException {
			Integer hash = Integer.valueOf(IPv6MulticastAddressFactory.getSrvTypeHash(pType));
			TRC.debug("srvType:" + pType + ", hash:" + hash);
			Counter cntr = this.iMap.get(hash);
			if (cntr == null) {
				cntr = new Counter();
				this.iMap.put(hash, cntr);
				ServiceTable.this.iDgramThread.joinGroup(IPv6MulticastAddressFactory.get(
						SLPDefaults.IPV6_MULTICAST_SCOPE, hash.intValue()));
			} else {
				++cntr.iValue;
			}

		}

		/**
		 * unregister
		 * 
		 * @param pType
		 * @throws UnknownHostException
		 * @throws IOException
		 */
		public void unregister(ServiceType pType) throws UnknownHostException, IOException {
			Integer hash = Integer.valueOf(IPv6MulticastAddressFactory.getSrvTypeHash(pType));
			Counter cntr = this.iMap.get(hash);
			if (cntr == null) return;
			if (cntr.iValue <= 1) {
				this.iMap.remove(hash);
				ServiceTable.this.iDgramThread.leaveGroup(IPv6MulticastAddressFactory.get(
						SLPDefaults.IPV6_MULTICAST_SCOPE, hash.intValue()));
			} else {
				--cntr.iValue;
			}
		}

	}

	private static class ServiceEntry {

		private ServiceURL iSrvURL;

		private List<ServiceLocationAttribute> iAttribs;

		private List<String> iScopes;

		/**
		 * Ctor.
		 * 
		 * @param pSrvURL
		 * @param pAttribs
		 * @param pScopes
		 */
		public ServiceEntry(ServiceURL pSrvURL, List<ServiceLocationAttribute> pAttribs,
				List<String> pScopes) {
			set(pSrvURL, pAttribs, pScopes);
		}

		/**
		 * set
		 * 
		 * @param pSrvURL
		 * @param pAttribs
		 * @param pScopes
		 */
		public void set(ServiceURL pSrvURL, List<ServiceLocationAttribute> pAttribs,
				List<String> pScopes) {
			this.iSrvURL = pSrvURL;
			this.iAttribs = pAttribs;
			this.iScopes = pScopes;
		}

		/**
		 * getServiceURL
		 * 
		 * @return ServiceURL
		 */
		public ServiceURL getServiceURL() {
			return this.iSrvURL;
		}

		/**
		 * getServiceType
		 * 
		 * @return ServiceType
		 */
		public ServiceType getServiceType() {
			return this.iSrvURL.getServiceType();
		}

		/**
		 * getAttributes
		 * 
		 * @return List
		 */
		public List<ServiceLocationAttribute> getAttributes() {
			return this.iAttribs;
		}

		/**
		 * getScopes
		 * 
		 * @return List
		 */
		public List<String> getScopes() {
			return this.iScopes;
		}

		/**
		 * hasMatchingScope
		 * 
		 * @param pScopes
		 * @return boolean
		 */
		public boolean hasMatchingScope(List<String> pScopes) {
			if (pScopes == null) return false;
			Iterator<String> itr = pScopes.iterator();
			while (itr.hasNext())
				if (hasScope(itr.next())) return true;
			return false;
		}

		@Override
		public String toString() {
			return "url:" + this.iSrvURL + ", attribs:" + dumpList(this.iAttribs) + ", scopes:"
					+ dumpList(this.iScopes);
		}

		private boolean hasScope(String pScope) {
			return this.iScopes == null ? false : this.iScopes.contains(pScope);
		}

	}

	static class ServiceEntryList extends ArrayList<Object> {

		private static final long serialVersionUID = 1L;

		/**
		 * get
		 * 
		 * @param pSrvURL
		 * @return ServiceEntry
		 */
		public ServiceEntry get(ServiceURL pSrvURL) {
			for (int i = 0; i < size(); i++) {
				ServiceEntry entry = (ServiceEntry) get(i);
				if (pSrvURL.equals(entry.getServiceURL())) return entry;
			}
			return null;
		}

		/**
		 * remove
		 * 
		 * @param pSrvURL
		 */
		public void remove(ServiceURL pSrvURL) {
			for (int i = 0; i < size(); i++) {
				ServiceEntry entry = (ServiceEntry) get(i);
				if (pSrvURL.equals(entry.getServiceURL())) {
					remove(i);
					break;
				}
			}
		}

		/**
		 * getServiceURLs
		 * 
		 * @param pSrvType
		 * @param pScopes
		 * @return List
		 */
		public List<ServiceURL> getServiceURLs(ServiceType pSrvType, List<String> pScopes) {
			if (pSrvType == null) return null;
			List<ServiceURL> srvURLs = null;
			for (int i = 0; i < size(); i++) {
				ServiceEntry entry = (ServiceEntry) get(i);
				if (!entry.hasMatchingScope(pScopes)) continue;
				if (pSrvType.getPrincipleTypeName().equals(
						entry.getServiceType().getPrincipleTypeName())) {
					if (srvURLs == null) srvURLs = new ArrayList<ServiceURL>();
					srvURLs.add(entry.getServiceURL());
				}
			}
			return srvURLs;
		}

	}

	private ServiceEntryList iSrvEntryTable = new ServiceEntryList();

	private AddressHashTable iAddressHashTable = new AddressHashTable();

	/**
	 * Ctor.
	 * 
	 * @param pDgramThread
	 */
	public ServiceTable(DatagramThread pDgramThread) {
		this.iDgramThread = pDgramThread;
	}

	/**
	 * add
	 * 
	 * @param pSrvURL
	 * @param pAttrList
	 * @param pScopes
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public synchronized void add(ServiceURL pSrvURL, List<ServiceLocationAttribute> pAttrList,
			List<String> pScopes) throws UnknownHostException, IOException {
		if (pSrvURL == null) return;
		TRC.debug("add URL:" + pSrvURL + ", scopes:" + dumpList(pScopes));
		ServiceEntry srvEntry = this.iSrvEntryTable.get(pSrvURL);
		if (srvEntry == null) {
			this.iSrvEntryTable.add(new ServiceEntry(pSrvURL, pAttrList, pScopes));
		} else {
			srvEntry.set(pSrvURL, pAttrList, pScopes);
		}

		if (!this.iUseV6) return;
		this.iAddressHashTable.register(pSrvURL.getServiceType());
	}

	/**
	 * remove
	 * 
	 * @param pSrvURL
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public synchronized void remove(ServiceURL pSrvURL) throws UnknownHostException, IOException {
		this.iSrvEntryTable.remove(pSrvURL);

		if (!this.iUseV6) return;
		this.iAddressHashTable.unregister(pSrvURL.getServiceType());
	}

	/**
	 * getServiceURLs
	 * 
	 * @param pSrvType
	 * @param pScopes
	 * @return List ServiceURL
	 */
	public synchronized List<ServiceURL> getServiceURLs(ServiceType pSrvType, List<String> pScopes) {
		TRC.debug("getServiceURLs srvType:" + pSrvType + ", scopes:" + dumpList(pScopes));
		List<ServiceURL> list = this.iSrvEntryTable.getServiceURLs(pSrvType, pScopes);
		return list;
	}

	/**
	 * getAttributes
	 * 
	 * @param pSrvURL
	 * @param pScopes
	 * @return List ServiceLocationAttribute
	 */
	public synchronized List<ServiceLocationAttribute> getAttributes(ServiceURL pSrvURL,
			List<String> pScopes) {
		if (pSrvURL == null) return null;
		if (pSrvURL.getURLPath() == null) return getAttributes(pSrvURL.getServiceType(), pScopes);
		ServiceEntry entry = this.iSrvEntryTable.get(pSrvURL);
		return entry == null ? null : entry.getAttributes();
	}

	/**
	 * getAttributes
	 * 
	 * @param pSrvType
	 * @param pScopes
	 * @return List ServiceLocationAttribute
	 */
	public synchronized List<ServiceLocationAttribute> getAttributes(ServiceType pSrvType,
			List<String> pScopes) {
		if (pSrvType == null) return null;
		HashSet<ServiceLocationAttribute> attribs = new HashSet<ServiceLocationAttribute>();
		for (int i = 0; i < this.iSrvEntryTable.size(); i++) {
			ServiceEntry entry = (ServiceEntry) this.iSrvEntryTable.get(i);
			ServiceType srvType = entry.getServiceType();
			if (pSrvType.equals(srvType)) attribs.addAll(entry.getAttributes());
		}
		return new ArrayList<ServiceLocationAttribute>(attribs);
	}

	/**
	 * getServiceTypes
	 * 
	 * @param pScopes
	 * @return List ServiceType
	 */
	public synchronized List<ServiceType> getServiceTypes(List<String> pScopes) {
		List<ServiceType> srvTypes = null;
		for (int i = 0; i < this.iSrvEntryTable.size(); i++) {
			ServiceEntry entry = (ServiceEntry) this.iSrvEntryTable.get(i);
			if (entry.hasMatchingScope(pScopes)) {
				ServiceType srvType = entry.getServiceType();
				if (srvType == null) continue;
				if (srvTypes == null) srvTypes = new ArrayList<ServiceType>();
				srvTypes.add(srvType);
			}
		}
		return srvTypes;
	}

	static String dumpList(List<?> pList) {
		return dumpList(pList, ",");
	}

	private static String dumpList(List<?> pList, String pSep) {
		if (pList == null) return "null";
		StringBuffer buf = new StringBuffer();
		Iterator<?> itr = pList.iterator();
		boolean first = true;
		while (itr.hasNext()) {
			if (!first) buf.append(pSep);
			buf.append(itr.next().toString());
			first = false;
		}
		return buf.toString();
	}

}
