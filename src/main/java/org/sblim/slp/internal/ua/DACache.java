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
 * @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp.internal.ua;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.sblim.slp.internal.SLPDefaults;
import org.sblim.slp.internal.msg.DADescriptor;

/**
 * DACache caches the discovered DA list in order to eliminate frequent DA
 * discovery network traffic.
 * 
 */
public class DACache {

	private static class ScopeEntry {

		private long iTimeOfDiscovery;

		private TreeSet<DADescriptor> iDADescriptors;

		/**
		 * Ctor.
		 * 
		 * @param pDADescriptors
		 */
		public ScopeEntry(TreeSet<DADescriptor> pDADescriptors) {
			this.iDADescriptors = pDADescriptors;
			this.iTimeOfDiscovery = getSecs();
		}

		/**
		 * valid
		 * 
		 * @return boolean
		 */
		public boolean valid() {
			return getSecs() - this.iTimeOfDiscovery <= SLPDefaults.DACACHE_TIMEOUT;
		}

		/**
		 * getDADescriptorItr
		 * 
		 * @return Iterator
		 */
		public Iterator<DADescriptor> getDADescriptorItr() {
			return this.iDADescriptors == null ? null : this.iDADescriptors.iterator();
		}

	}

	/**
	 * key: scope value: ScopeEntry
	 */
	private static TreeMap<String, ScopeEntry> cScopeMap = new TreeMap<String, ScopeEntry>();

	// TODO: handle scopes

	/**
	 * @param pScopes
	 * @return List of discoverable scope strings
	 */
	public static synchronized List<String> getDiscoverableScopeList(List<String> pScopes) {
		if (pScopes == null || pScopes.size() == 0) return null;
		List<String> scopeList = null;
		Iterator<String> itr = pScopes.iterator();
		while (itr.hasNext()) {
			String scope = itr.next();
			ScopeEntry scopeEntry = cScopeMap.get(scope);
			if (scopeEntry == null || !scopeEntry.valid()) {
				if (scopeList == null) scopeList = new ArrayList<String>();
				scopeList.add(scope);
			}
		}
		return scopeList;
	}

	/**
	 * @param pScopes
	 * @return List of DA URLs
	 */
	public static synchronized List<String> getDAList(List<String> pScopes) {
		if (cScopeMap == null) return null;
		TreeSet<String> daSet = new TreeSet<String>();
		Iterator<String> scopeItr = pScopes.iterator();
		while (scopeItr.hasNext()) {
			String scope = scopeItr.next();
			ScopeEntry scopeEntry = cScopeMap.get(scope);
			if (scopeEntry == null) continue;
			Iterator<DADescriptor> descItr = scopeEntry.getDADescriptorItr();
			if (descItr == null) continue;
			while (descItr.hasNext()) {
				daSet.add(descItr.next().getURL());
			}
		}
		return new ArrayList<String>(daSet);
	}

	/**
	 * @param pScopes
	 *            - list of discovered hosts
	 * @param pDADescriptors
	 *            - DADescriptors of the discovered DAs
	 */
	public static synchronized void setDAList(List<String> pScopes,
			List<DADescriptor> pDADescriptors) {
		if (pScopes == null || pDADescriptors == null) return;
		Iterator<String> scopeItr = pScopes.iterator();
		while (scopeItr.hasNext()) {
			String scope = scopeItr.next();
			TreeSet<DADescriptor> daDescsForScope = null;
			Iterator<DADescriptor> descItr = pDADescriptors.iterator();
			while (descItr.hasNext()) {
				DADescriptor daDesc = descItr.next();
				if (daDesc.hasScope(scope)) {
					if (daDescsForScope == null) daDescsForScope = new TreeSet<DADescriptor>();
					daDescsForScope.add(daDesc);
				}
			}
			cScopeMap.put(scope, new ScopeEntry(daDescsForScope));
		}
	}

	static long getSecs() {
		return new Date().getTime() / 1000;
	}

}
