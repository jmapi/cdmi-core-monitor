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

package org.sblim.slp.internal.msg;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.Socket;

import org.sblim.slp.ServiceLocationException;

/**
 * MsgFactory
 * 
 */
public class MsgFactory implements FunctionIDs {

	/**
	 * FactoryEntry
	 * 
	 */
	private interface FactoryEntry {

		/**
		 * parse
		 * 
		 * @param pHdr
		 * @param pInStr
		 * @return SLPMessage
		 * @throws ServiceLocationException
		 * @throws IOException
		 */
		public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
				throws ServiceLocationException, IOException;
	}

	private static FactoryEntry[] cFactoryArray;

	private static void placeFactory(int pFnID, FactoryEntry pEntry) {
		cFactoryArray[pFnID - FIRST_ID] = pEntry;
	}

	private static synchronized void createFactoryArray() {
		if (cFactoryArray != null) return;
		cFactoryArray = new FactoryEntry[LAST_ID - FIRST_ID + 1];
		for (int i = 0; i < cFactoryArray.length; i++)
			cFactoryArray[i] = null;
		placeFactory(ATTR_RPLY, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return AttributeReply.parse(pHdr, pInStr);
			}
		});
		placeFactory(ATTR_RQST, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return AttributeRequest.parse(pHdr, pInStr);
			}
		});
		placeFactory(DA_ADVERT, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return DAAdvert.parse(pHdr, pInStr);
			}
		});
		placeFactory(SA_ADVERT, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return SAAdvert.parse(pHdr, pInStr);
			}
		});
		placeFactory(SRV_ACK, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return ServiceAcknowledgment.parse(pHdr, pInStr);
			}
		});
		placeFactory(SRV_DEREG, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return ServiceDeregistration.parse(pHdr, pInStr);
			}
		});
		placeFactory(SRV_REG, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return ServiceRegistration.parse(pHdr, pInStr);
			}
		});
		placeFactory(SRV_RPLY, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return ServiceReply.parse(pHdr, pInStr);
			}
		});
		placeFactory(SRV_RQST, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return ServiceRequest.parse(pHdr, pInStr);
			}
		});
		//
		placeFactory(SRV_TYPE_RPLY, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return ServiceTypeReply.parse(pHdr, pInStr);
			}
		});
		placeFactory(SRV_TYPE_RQST, new FactoryEntry() {

			public SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
					throws ServiceLocationException, IOException {
				return ServiceTypeRequest.parse(pHdr, pInStr);
			}
		});
	}

	private static FactoryEntry getFactory(int pFnID) {
		createFactoryArray();
		return cFactoryArray[pFnID - FIRST_ID];
	}

	/**
	 * parse
	 * 
	 * @param pSock
	 * @return SLPMessage
	 * @throws ServiceLocationException
	 * @throws IOException
	 */
	public static SLPMessage parse(Socket pSock) throws ServiceLocationException, IOException {
		return parse(pSock.getInputStream());
	}

	/**
	 * parse
	 * 
	 * @param pInStr
	 * @return SLPMessage
	 * @throws ServiceLocationException
	 * @throws IOException
	 */
	public static SLPMessage parse(InputStream pInStr) throws ServiceLocationException, IOException {
		return parse(new SLPInputStream(pInStr));
	}

	/**
	 * parse
	 * 
	 * @param pPacket
	 * @return SLPMessage
	 * @throws ServiceLocationException
	 * @throws IOException
	 */
	public static SLPMessage parse(DatagramPacket pPacket) throws ServiceLocationException,
			IOException {
		return parse(new SLPInputStream(pPacket));
	}

	/**
	 * parse
	 * 
	 * @param pInStr
	 * @return SLPMessage
	 * @throws ServiceLocationException
	 * @throws IOException
	 */
	public static SLPMessage parse(SLPInputStream pInStr) throws ServiceLocationException,
			IOException {
		MsgHeader hdr = MsgHeader.parse(pInStr);
		FactoryEntry factory = getFactory(hdr.getFunctionID());
		if (factory == null) throw new ServiceLocationException(
				ServiceLocationException.NOT_IMPLEMENTED, "FunctionID=" + hdr.getFunctionID()
						+ " is not implemented!");
		return factory.parse(hdr, pInStr);
	}

}
