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
 * 1804402    2007-11-10  ebak         IPv6 ready SLP - revision 4
 * 1892103    2008-02-15  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.slp.internal.sa;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceURL;
import org.sblim.slp.internal.IPv6MulticastAddressFactory;
import org.sblim.slp.internal.Net;
import org.sblim.slp.internal.SLPConfig;
import org.sblim.slp.internal.SLPDefaults;
import org.sblim.slp.internal.TRC;
import org.sblim.slp.internal.msg.AttributeReply;
import org.sblim.slp.internal.msg.AttributeRequest;
import org.sblim.slp.internal.msg.FunctionIDs;
import org.sblim.slp.internal.msg.MsgFactory;
import org.sblim.slp.internal.msg.ReplyMessage;
import org.sblim.slp.internal.msg.RequestMessage;
import org.sblim.slp.internal.msg.SLPMessage;
import org.sblim.slp.internal.msg.ServiceAcknowledgment;
import org.sblim.slp.internal.msg.ServiceDeregistration;
import org.sblim.slp.internal.msg.ServiceRegistration;
import org.sblim.slp.internal.msg.ServiceReply;
import org.sblim.slp.internal.msg.ServiceRequest;
import org.sblim.slp.internal.msg.ServiceTypeReply;

/**
 * ServiceAgent
 * 
 */
public class ServiceAgent implements FunctionIDs {

	private boolean iUseV4 = Net.hasIPv4() && SLPConfig.getGlobalCfg().useIPv4(), iUseV6 = Net
			.hasIPv6()
			&& SLPConfig.getGlobalCfg().useIPv6();

	boolean iStarted;

	private volatile boolean iSkipFirstRequest;

	private DatagramThread iDGThread = new DatagramThread(this);

	private TCPThread iTCPThread = new TCPThread(this);

	private ServiceTable iSrvTable = new ServiceTable(this.iDGThread);

	private MessageTable iMsgTable = new MessageTable();

	/**
	 * main
	 * 
	 * @param pArgs
	 * @throws IOException
	 */
	public static void main(String[] pArgs) throws IOException {
		if (pArgs != null && pArgs.length >= 1) {
			int port = Integer.parseInt(pArgs[0]);
			SLPConfig.getGlobalCfg().setPort(port);
		}
		ServiceAgent srvAgent = new ServiceAgent();
		srvAgent.start();
		TRC.debug("starting idle loop");
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// lofasz
			}
		}
	}

	/**
	 * setSkipFirstRequest
	 * 
	 * @param pValue
	 */
	public void setSkipFirstRequest(boolean pValue) {
		this.iSkipFirstRequest = pValue;
	}

	/**
	 * start
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		if (this.iStarted) return;
		// launch receivers
		TRC.debug("start receivers");
		if (this.iUseV4 || this.iUseV6) {
			this.iTCPThread.start();
			this.iDGThread.start();
			TRC.debug("wait4 TCP init");
			this.iTCPThread.wait4init();
			TRC.debug("wait4 Datagram init");
			this.iDGThread.wait4init();
		}
		this.iStarted = true;
		TRC.debug("receivers started");
		// join multicast groups
		if (this.iUseV4) this.iDGThread.joinGroup(SLPConfig.getMulticastAddress());
		if (this.iUseV6) {
			this.iDGThread.joinGroup(IPv6MulticastAddressFactory
					.getSrvLocAddress(SLPDefaults.IPV6_MULTICAST_SCOPE));
		}
	}

	/**
	 * stop
	 */
	public void stop() {
		// stop the receivers
		if (this.iUseV4 || this.iUseV6) {
			this.iTCPThread.stop();
			this.iDGThread.stop();
		}
		this.iStarted = false;

	}

	/**
	 * processMessage
	 * 
	 * @param pDGSock
	 * @param pPacket
	 */
	public void processMessage(DatagramSocket pDGSock, DatagramPacket pPacket) {
		byte[] reply;
		SLPMessage msg;
		try {
			msg = MsgFactory.parse(pPacket);
		} catch (Exception e) {
			// no response for unparseable messages
			TRC.debug("Message parsing error!", e);
			return;
		}

		try {
			TRC.debug("processing: " + msg);
			reply = this.iMsgTable.getResponse(pPacket.getAddress(), msg);
			if (reply == null) {
				SLPMessage replyMsg = makeReply(msg);
				if (replyMsg != null) {
					TRC.debug("response : " + replyMsg);
					reply = replyMsg.serialize(false, true, true);
					this.iMsgTable.addResponse(pPacket.getAddress(), msg, reply);
					// debugging: do not reply to the first request
					if (this.iSkipFirstRequest) {
						TRC.debug("refusing response");
						return;
					}
				}
			} else {
				TRC.debug("cached response");
			}
		} catch (ServiceLocationException e) {
			TRC.debug(e.toString(), e);
			reply = makeErrorReply(msg, e); // known XID
		}
		if (reply != null) {
			try {
				TRC.debug("sending response");
				pDGSock.send(new DatagramPacket(reply, reply.length, pPacket.getAddress(), pPacket
						.getPort()));
			} catch (IOException e) {
				TRC.error(e);
			}
		}
	}

	/**
	 * processMessage
	 * 
	 * @param pStreamSock
	 */
	public void processMessage(Socket pStreamSock) {
		byte[] reply;
		SLPMessage msg;
		try {
			msg = MsgFactory.parse(pStreamSock.getInputStream());
		} catch (Exception e) {
			// no response for unparseable messages
			TRC.debug("Message parsing error!", e);
			return;
		}
		try {
			SLPMessage replyMsg = makeReply(msg);
			reply = replyMsg == null ? null : replyMsg.serialize(false, false, true);
		} catch (ServiceLocationException e) {
			reply = makeErrorReply(msg, e);
		}
		if (reply != null) {
			try {
				OutputStream os = pStreamSock.getOutputStream();
				os.write(reply);
				os.flush();
			} catch (IOException e) {
				TRC.error(e);
			}
		}
		try {
			pStreamSock.close();
		} catch (IOException e) {
			TRC.error(e);
		}
	}

	/*
	 * XID is set to a unique value for each unique request. If the request is
	 * retransmitted, the same XID is used. Replies set the XID to the same
	 * value as the xid in the request. Only unsolicited DAAdverts are sent with
	 * an XID of 0.
	 */
	private SLPMessage makeReply(SLPMessage pRequest) throws ServiceLocationException {
		try {
			SLPMessage msg = pRequest instanceof RequestMessage ? makeReply4Request((RequestMessage) pRequest)
					: makeReply4Others(pRequest);
			if (msg != null) msg.setXID(pRequest.getXID());
			return msg;
		} catch (UnknownHostException e) {
			TRC.error(e);
			throw new ServiceLocationException(ServiceLocationException.NETWORK_ERROR);
		} catch (IOException e) {
			TRC.error(e);
			throw new ServiceLocationException(ServiceLocationException.NETWORK_ERROR);
		}

	}

	private ReplyMessage makeReply4Request(RequestMessage pRequest) {
		ReplyMessage replyMsg;
		List<String> scopes = pRequest.getScopeList();
		switch (pRequest.getFunctionID()) {
			case SRV_RQST:
				ServiceRequest srvRqst = (ServiceRequest) pRequest;
				if (SLPDefaults.DA_SERVICE_TYPE.equals(srvRqst.getServiceType())) return null;
				List<ServiceURL> urlList = this.iSrvTable.getServiceURLs(srvRqst.getServiceType(),
						scopes);
				TRC.debug("srvReply : " + urlList);
				replyMsg = new ServiceReply(0, urlList);
				break;
			case ATTR_RQST:
				AttributeRequest attrRqst = (AttributeRequest) pRequest;
				replyMsg = new AttributeReply(0, this.iSrvTable.getAttributes(attrRqst
						.getServiceURL(), scopes));
				break;
			case SRV_TYPE_RQST:
				// ServiceTypeRequest srvTypeRqst =
				// (ServiceTypeRequest)pRequest;
				replyMsg = new ServiceTypeReply(0, this.iSrvTable.getServiceTypes(scopes));
				break;
			default:
				return null;
		}
		return replyMsg;
	}

	private SLPMessage makeReply4Others(SLPMessage pRequest) throws IOException {
		switch (pRequest.getFunctionID()) {
			case SRV_REG:
				ServiceRegistration srvReg = (ServiceRegistration) pRequest;
				this.iSrvTable.add(srvReg.getServiceURL(), srvReg.getAttributeList(), srvReg
						.getScopeList());
				return new ServiceAcknowledgment(0);
			case SRV_DEREG:
				ServiceDeregistration srvDereg = (ServiceDeregistration) pRequest;
				this.iSrvTable.remove(srvDereg.getServiceURL());
				return new ServiceAcknowledgment(0);
			default:
				// FIXME maybe returning an error message is better
				return null;
		}
	}

	private static byte[] makeErrorReply(SLPMessage pRequest, ServiceLocationException pE) {
		return makeErrorReply(pRequest, pE.getErrorCode());
	}

	/*
	 * Error reply: SRV_REG, SRV_DEREG -> SRV_ACK SRV_RQST -> SRV_RSP ATTR_RQST
	 * -> ATTR_RPLY SRV_TYPE_RQST -> SRV_TYPE_RPLY
	 */

	private static byte[] makeErrorReply(SLPMessage pRequest, int pErrorCode) {
		ReplyMessage replyMsg;
		switch (pRequest.getFunctionID()) {
			case SRV_RQST:
				replyMsg = new ServiceReply(pErrorCode, null);
				break;
			case ATTR_RQST:
				replyMsg = new AttributeReply(pErrorCode, null);
				break;
			case SRV_TYPE_RQST:
				replyMsg = new ServiceTypeReply(pErrorCode, null);
				break;
			case SRV_REG:
			case SRV_DEREG:
				replyMsg = new ServiceAcknowledgment(pErrorCode);
				break;
			default:
				return null;
		}
		replyMsg.setXID(pRequest.getXID());
		try {
			return replyMsg.serialize(false, true, true);
		} catch (ServiceLocationException se) {
			TRC.error(se);
			return null;
		}
	}

}
