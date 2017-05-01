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
 * 1892103    2008-02-12  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.slp.internal.ua;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.internal.SLPConfig;
import org.sblim.slp.internal.TRC;
import org.sblim.slp.internal.msg.MsgFactory;
import org.sblim.slp.internal.msg.ReplyMessage;
import org.sblim.slp.internal.msg.RequestMessage;

/**
 * TCPRequester
 * 
 */
public class TCPRequester implements Runnable {

	private InetAddress iDestination;

	private Thread iThread;

	private ResultTable iResTable;

	private RequestMessage iReqMsg;

	private byte[] iRequestBytes;

	private int iPort = SLPConfig.getGlobalCfg().getPort();

	private final int iTCPTimeOut = SLPConfig.getGlobalCfg().getTCPTimeout();

	/**
	 * Ctor.
	 * 
	 * @param pResTable
	 * @param pDestination
	 * @param pReqMsg
	 * @param pAsThread
	 * @throws ServiceLocationException
	 */
	public TCPRequester(ResultTable pResTable, InetAddress pDestination, RequestMessage pReqMsg,
			boolean pAsThread) throws ServiceLocationException {
		this.iResTable = pResTable;
		this.iDestination = pDestination;
		this.iReqMsg = pReqMsg;
		this.iRequestBytes = pReqMsg.serializeWithoutResponders(false, false, true);
		// FIXME: Is it safe to omit PreviousResopnder list for TCP request?
		if (pAsThread) {
			this.iThread = new Thread(this);
			this.iThread.start();
		} else {
			this.iThread = null;
			run();
		}
	}

	/**
	 * waitFor
	 */
	public void waitFor() {
		if (this.iThread == null) return;
		try {
			this.iThread.join();
		} catch (InterruptedException e) {
			TRC.error(e);
		}
	}

	public void run() {
		Socket socket = null;
		try {
			socket = new Socket(this.iDestination, this.iPort);
			socket.setSoTimeout(this.iTCPTimeOut);
			OutputStream os = socket.getOutputStream();
			TRC.debug("sendTCP");
			os.write(this.iRequestBytes);
			os.flush();
			handleResponse(socket);
			TRC.debug("recievedOnTCP");
		} catch (Exception e) {
			TRC.error(e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					TRC.error(e);
				}
			}
		}

	}

	private void handleResponse(Socket pSocket) {
		ReplyMessage replyMsg;
		try {
			replyMsg = (ReplyMessage) MsgFactory.parse(pSocket);
		} catch (Exception e) {
			this.iResTable.addException(e);
			return;
		}
		if (this.iReqMsg.getXID() == replyMsg.getXID()
				&& this.iReqMsg.isAllowedResponseType(replyMsg)) this.iResTable
				.addResults(replyMsg);
	}

}
