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

package org.sblim.slp.internal.sa;

import java.io.IOException;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.internal.TRC;

/**
 * RecieverThread
 * 
 */
public abstract class RecieverThread implements Runnable {

	private Thread iThread;

	private volatile boolean iStop;

	private boolean iInited;

	private Object iInitLock = new Object();

	protected ServiceAgent iSrvAgent;

	/**
	 * Ctor.
	 * 
	 * @param pName
	 * @param pSrvAgent
	 */
	public RecieverThread(String pName, ServiceAgent pSrvAgent) {
		this.iThread = new Thread(this, pName);
		this.iSrvAgent = pSrvAgent;
	}

	/**
	 * start
	 */
	public void start() {
		this.iThread.start();
	}

	/**
	 * wait4init
	 */
	public void wait4init() {
		synchronized (this.iInitLock) {
			try {
				if (this.iInited) return;
				this.iInitLock.wait();
				return;
			} catch (InterruptedException e) {
				TRC.error(e);
			}
		}
	}

	/**
	 * stop
	 */
	public void stop() {
		stop(true);
	}

	/**
	 * stop
	 * 
	 * @param pWait
	 */
	public void stop(boolean pWait) {
		this.iStop = true;
		if (pWait) join();
	}

	public void run() {
		// 1st init
		synchronized (this.iInitLock) {
			TRC.debug("initing");
			initialize();
			this.iInited = true;
			TRC.debug("inited");
			try {
				this.iInitLock.notifyAll();
			} catch (IllegalMonitorStateException e) {
				TRC.error(e);
			}
		}
		while (!this.iStop) {
			try {
				mainLoop();
			} catch (Exception e) {
				TRC.error(e);
				sleep(100);
				initialize();
			}
		}
		close();
		this.iStop = false;
		TRC.debug("STOPPED");
	}

	// exception of init shoud be reachable for ServiceTable

	protected abstract void init() throws ServiceLocationException, IOException;

	protected abstract void mainLoop() throws IOException;

	protected abstract void close();

	private void join() {
		try {
			this.iThread.join();
		} catch (InterruptedException e) {
			TRC.error(e);
		}
	}

	private void initialize() {
		try {
			init();
		} catch (ServiceLocationException e) {
			TRC.error(e);
		} catch (IOException e) {
			TRC.error(e);
		}
	}

	private static void sleep(int pMillis) {
		try {
			Thread.sleep(pMillis);
		} catch (InterruptedException e) {
			TRC.error(e);
		}
	}

}
