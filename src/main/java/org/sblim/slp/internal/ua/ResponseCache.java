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
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3019214    2010-06-21  blaschke-oss SLP equals methods assume too much
 */

package org.sblim.slp.internal.ua;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.HashSet;

/**
 * ResponseCache intends to eliminate the processing of SLP responses with the
 * same content.
 * 
 */
public class ResponseCache {

	private static class Entry {

		private byte[] iData;

		private int iHashCode;

		/**
		 * Ctor.
		 * 
		 * @param pData
		 * @param pOffset
		 * @param pLength
		 */
		public Entry(byte[] pData, int pOffset, int pLength) {
			this.iData = new byte[pLength];
			System.arraycopy(pData, pOffset, this.iData, 0, pLength);
			for (int pos = 0; pos < this.iData.length; ++pos) {
				this.iHashCode <<= 4;
				this.iHashCode += (this.iData[pos] & 0xff);
			}
		}

		/**
		 * Ctor.
		 * 
		 * @param pPacket
		 */
		public Entry(DatagramPacket pPacket) {
			this(pPacket.getData(), pPacket.getOffset(), pPacket.getLength());
		}

		@Override
		public int hashCode() {
			return this.iHashCode;
		}

		@Override
		public boolean equals(Object pObj) {
			if (pObj == null || !(pObj instanceof Entry)) return false;
			if (this == pObj) return true;
			Entry that = (Entry) pObj;
			return this.iHashCode == that.iHashCode && Arrays.equals(this.iData, that.iData);
		}

	}

	private HashSet<Entry> iResponseSet = new HashSet<Entry>();

	/**
	 * add
	 * 
	 * @param pPacket
	 */
	public void add(DatagramPacket pPacket) {
		this.iResponseSet.add(new Entry(pPacket));
	}

	/**
	 * contains
	 * 
	 * @param pPacket
	 * @return boolean
	 */
	public boolean contains(DatagramPacket pPacket) {
		return this.iResponseSet.contains(new Entry(pPacket));
	}

}
