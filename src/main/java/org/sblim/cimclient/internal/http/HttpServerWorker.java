/**
 * (C) Copyright IBM Corp. 2005, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1498927    2006-06-01  lupusalex    Fill gaps in logging coverage
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.http;

import java.net.Socket;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.logging.Messages;

/**
 * Class HttpServerWorker forwards incoming connections to a handler
 * 
 */
public class HttpServerWorker implements Runnable {

	HttpConnectionHandler iHandler;

	Socket iSocket;

	/**
	 * Ctor.
	 * 
	 * @param pHandler
	 *            The handler
	 * @param pSocket
	 *            The socket
	 */
	public HttpServerWorker(HttpConnectionHandler pHandler, Socket pSocket) {
		this.iHandler = pHandler;
		this.iSocket = pSocket;
	}

	public void run() {
		try {
			this.iHandler.handleConnection(this.iSocket);
		} catch (Exception e) {
			LogAndTraceBroker.getBroker().message(Messages.HTTP_HANDLE_CONNECTION_FAILED, e);
		}
	}
}
