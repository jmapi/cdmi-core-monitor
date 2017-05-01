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
 */

package org.sblim.slp.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.sblim.slp.ServiceType;
import org.sblim.slp.internal.msg.AttributeRequest;
import org.sblim.slp.internal.msg.SLPMessage;
import org.sblim.slp.internal.msg.ServiceRequest;
import org.sblim.slp.internal.msg.ServiceTypeRequest;

/**
 * <pre>
 * SVRLOC group-id : FF0X:0:0:0:0:0:0:116
 *  - SRVTYPERQST Service Type Request
 *  - ATTRRQST Attribute Request
 *  - UA sends too
 *  
 * SVRLOC-DA group-id : FF0X:0:0:0:0:0:0:123
 *  - SRVRQST for the &quot;service:directory-agent&quot; service type
 *  - UA sends too
 * 
 * 1000 - 13FF: SRVRQST : FF0X:0:0:0:0:0:1:....
 *  - the ID is the hashcode of the Service Type string used in the SrvRqst.
 *  - UA sends too
 * </pre>
 */
public class IPv6MulticastAddressFactory {

	private static final byte[] SRVLOC = { (byte) 0xff, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			(byte) 0x16 };

	private static final byte[] SRVLOC_DA = { (byte) 0xff, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, (byte) 0x23 };

	private static final byte[] SRV_RQST = { (byte) 0xff, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
			0 };

	/**
	 * getSrvLocAddress
	 * 
	 * @param pScope
	 * @return InetAddress
	 * @throws UnknownHostException
	 */
	public static InetAddress getSrvLocAddress(int pScope) throws UnknownHostException {
		SRVLOC[1] = (byte) (pScope & 0xff);
		return InetAddress.getByAddress(SRVLOC);
	}

	/**
	 * get
	 * 
	 * @param pScope
	 * @param pMsg
	 * @return InetAddress
	 * @throws UnknownHostException
	 */
	public static InetAddress get(int pScope, SLPMessage pMsg) throws UnknownHostException {
		if (pMsg instanceof ServiceTypeRequest || pMsg instanceof AttributeRequest) { return getSrvLocAddress(pScope); }
		if (pMsg instanceof ServiceRequest) {
			ServiceRequest srvRqst = (ServiceRequest) pMsg;
			return get(pScope, srvRqst.getServiceType());
		}
		throw new IllegalArgumentException("Cannot determine IPv6 multicast address for "
				+ pMsg.getClass().getName() + " !");
	}

	/**
	 * get
	 * 
	 * @param pScope
	 * @param pSrvType
	 * @return InetAddress
	 * @throws UnknownHostException
	 */
	public static InetAddress get(int pScope, ServiceType pSrvType) throws UnknownHostException {
		if (pSrvType.equals(SLPDefaults.DA_SERVICE_TYPE)) {
			SRVLOC_DA[1] = (byte) (pScope & 0xff);
			return InetAddress.getByAddress(SRVLOC_DA);
		}
		return get(pScope, getSrvTypeHash(pSrvType));
	}

	/**
	 * get
	 * 
	 * @param pScope
	 * @param pSrvHash
	 * @return InetAddress
	 * @throws UnknownHostException
	 */
	public static InetAddress get(int pScope, int pSrvHash) throws UnknownHostException {
		byte scope = (byte) (pScope & 0xff);
		SRV_RQST[1] = scope;
		SRV_RQST[14] = (byte) ((pSrvHash >> 8) & 0xff);
		SRV_RQST[15] = (byte) (pSrvHash & 0xff);
		return InetAddress.getByAddress(SRV_RQST);
	}

	/**
	 * <pre>
	 *  unsigned long slp_hash(const char *pc, unsigned int len) {
	 *    unsigned long h = 0;
	 *    while (len-- != 0) {
	 *        h *= 33;
	 *        h += *pc++;
	 *    }
	 *    return (0x3FF &amp; h); // round to a range of 0-1023
	 *   }
	 * </pre>
	 * 
	 * @param pServiceType
	 * @return int
	 */
	public static int getSrvTypeHash(ServiceType pServiceType) {
		if (pServiceType == null) return 0x1000;
		String typeStr = pServiceType.toString();
		int hash = 0;
		for (int i = 0; i < typeStr.length(); i++) {
			hash *= 33;
			hash += typeStr.charAt(i);
		}
		return (0x3ff & hash) + 0x1000;
	}

}
