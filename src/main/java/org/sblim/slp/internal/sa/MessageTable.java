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
 * 1892103    2008-02-12  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 3023120    2010-07-01  blaschke-oss RequestDescriptor equals/compareTo issue
 * 3023349    2010-07-02  blaschke-oss SLP uses # constructor instead of valueOf
 */

package org.sblim.slp.internal.sa;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.internal.msg.RequestMessage;
import org.sblim.slp.internal.msg.SLPMessage;

/**
 * Keeps track of datagram messages. For requests with the same XID the same
 * responses should be returned.
 * 
 */
public class MessageTable {

	private static class RequestDescriptor implements Comparable<RequestDescriptor> {

		private byte[] iSrcAddress;

		private byte[] iRequest;

		/**
		 * Ctor.
		 * 
		 * @param pSource
		 * @param pRequest
		 * @throws ServiceLocationException
		 */
		public RequestDescriptor(InetAddress pSource, SLPMessage pRequest)
				throws ServiceLocationException {
			this.iSrcAddress = pSource.getAddress();
			this.iRequest = (pRequest instanceof RequestMessage) ? ((RequestMessage) pRequest)
					.serializeWithoutResponders(false, true, true) : pRequest.serialize(false,
					true, true);
		}

		public int compareTo(RequestDescriptor pObj) {
			RequestDescriptor that = pObj;
			int cmp = compare(this.iSrcAddress, that.iSrcAddress);
			if (cmp != 0) return cmp;
			return compare(this.iRequest, that.iRequest);
		}

		private static int compare(byte[] pBytes0, byte[] pBytes1) {
			int len = Math.min(pBytes0.length, pBytes1.length);
			for (int i = 0; i < len; i++) {
				int cmp = pBytes0[i] & 0xff - pBytes1[i] & 0xff;
				if (cmp != 0) return cmp;
			}
			return pBytes0.length - pBytes1.length;
		}

		@Override
		public boolean equals(Object pObj) {
			if (!(pObj instanceof RequestDescriptor)) return false;
			RequestDescriptor that = (RequestDescriptor) pObj;
			return compare(this.iSrcAddress, that.iSrcAddress) == 0
					&& compare(this.iRequest, that.iRequest) == 0;
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(this.iSrcAddress) + Arrays.hashCode(this.iRequest);
		}
	}

	private static class TableEntry {

		private long iTime;

		private RequestDescriptor iReqDesc;

		private byte[] iResponse;

		/**
		 * Ctor.
		 * 
		 * @param pTime
		 * @param pReqKey
		 * @param pResponse
		 */
		public TableEntry(long pTime, RequestDescriptor pReqKey, byte[] pResponse) {
			this.iTime = pTime;
			this.iReqDesc = pReqKey;
			this.iResponse = pResponse;
		}

		/**
		 * getTime
		 * 
		 * @return long
		 */
		public long getTime() {
			return this.iTime;
		}

		/**
		 * setTime
		 * 
		 * @param pTime
		 */
		public void setTime(long pTime) {
			this.iTime = pTime;
		}

		/**
		 * getRequestDescriptor
		 * 
		 * @return RequestDescriptor
		 */
		public RequestDescriptor getRequestDescriptor() {
			return this.iReqDesc;
		}

		/**
		 * getResponse
		 * 
		 * @return byte[]
		 */
		public byte[] getResponse() {
			return this.iResponse;
		}

	}

	/**
	 * Remember messages for 30 seconds.
	 */
	private static final long KEEPIN = 30;

	/**
	 * Time -> TableEntry
	 */
	private SortedMap<Long, TableEntry> iTimeMap = new TreeMap<Long, TableEntry>();

	/**
	 * RequestKey -> TableEntry
	 */
	private SortedMap<RequestDescriptor, TableEntry> iReqMap = new TreeMap<RequestDescriptor, TableEntry>();

	/**
	 * getResponse
	 * 
	 * @param pSource
	 * @param pRequest
	 * @return byte[]
	 * @throws ServiceLocationException
	 */
	public synchronized byte[] getResponse(InetAddress pSource, SLPMessage pRequest)
			throws ServiceLocationException {
		long now = getSecs();
		RequestDescriptor reqDesc = new RequestDescriptor(pSource, pRequest);
		TableEntry entry = this.iReqMap.get(reqDesc);
		if (entry == null) return null;
		clean();
		updateTime(entry, now);
		return entry.getResponse();
	}

	/**
	 * addResponse
	 * 
	 * @param pSource
	 * @param pRequest
	 * @param pRespond
	 * @throws ServiceLocationException
	 */
	public synchronized void addResponse(InetAddress pSource, SLPMessage pRequest, byte[] pRespond)
			throws ServiceLocationException {
		insert(new TableEntry(getSecs(), new RequestDescriptor(pSource, pRequest), pRespond));
		clean();
	}

	private void clean() {
		long now = getSecs();
		Long timeStamp;
		while ((timeStamp = this.iTimeMap.firstKey()) != null) {
			if (now - timeStamp.longValue() < KEEPIN) break;
			TableEntry entry = this.iTimeMap.get(timeStamp);
			remove(entry);
		}
	}

	private void insert(TableEntry pEntry) {
		this.iTimeMap.put(Long.valueOf(pEntry.getTime()), pEntry);
		this.iReqMap.put(pEntry.getRequestDescriptor(), pEntry);
	}

	private void remove(TableEntry pEntry) {
		this.iTimeMap.remove(Long.valueOf(pEntry.getTime()));
		this.iReqMap.remove(pEntry.getRequestDescriptor());
	}

	private void updateTime(TableEntry pEntry, long pTime) {
		remove(pEntry);
		pEntry.setTime(pTime);
		insert(pEntry);
	}

	private static long getSecs() {
		return new Date().getTime() / 1000;
	}

}
