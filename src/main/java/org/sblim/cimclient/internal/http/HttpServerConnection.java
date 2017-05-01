/**
 * (C) Copyright IBM Corp. 2005, 2013
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
 * 1422316    2005-05-08  lupusalex    Disable delayed acknowledgment
 * 1483270    2006-05-15  lupusalex    Using Several Cim Clients cause an indication problem
 * 1498130    2006-05-31  lupusalex    Selection of xml parser on a per connection basis
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1649779    2007-02-01  lupusalex    Indication listener threads freeze
 * 1660743    2007-02-15  lupusalex    SSLContext is static
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2210455    2008-10-30  blaschke-oss Enhance javadoc, fix potential null pointers
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3027392    2010-07-09  blaschke-oss Nullcheck of value previously dereferenced
 * 3206904    2011-03-11  lupusalex    Indication listener deadlock causes JVM to run out sockets
 * 3536399    2012-08-25  hellerda     Add client/listener peer authentication properties
 *    2618    2013-02-27  blaschke-oss Need to add property to disable weak cipher suites for the secure indication
 *    2642    2013-05-21  blaschke-oss Seperate properties needed for cim client and listener to filter out ciphers
 */

package org.sblim.cimclient.internal.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.util.ThreadPool;
import org.sblim.cimclient.internal.util.Util;
import org.sblim.cimclient.internal.util.WBEMConfiguration;

/**
 * Class HttpServerConnection implements the outer shell of a HTTP server. It
 * accepts incoming connections and puts them in a queue to be serviced by an
 * independent thread
 * 
 */
public class HttpServerConnection implements Runnable {

	private int iPort;

	private ServerSocket iServerSocket;

	private HttpConnectionHandler iHandler;

	private HttpConnectionDispatcher iDispatcher;

	private volatile boolean iClose = true;

	private String iServerName;

	private boolean iSsl;

	private Thread iRunner;

	private WBEMConfiguration iSessionProperties;

	private final int iTimeout;

	/**
	 * Ctor.
	 * 
	 * @param pHandler
	 *            The connection handler
	 * @param pLocalAddress
	 *            The local address to bind the port to. If null the port is
	 *            bound to all local addresses. For use on multi-homed systems
	 * @param pPort
	 *            The local port. If zero any free port will be chosen.
	 * @param pSsl
	 *            SSL secured connection ?
	 * @param pProperties
	 *            The configuration context
	 * @throws IOException
	 */
	public HttpServerConnection(HttpConnectionHandler pHandler, String pLocalAddress, int pPort,
			boolean pSsl, WBEMConfiguration pProperties) throws IOException {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		this.iPort = pPort;
		this.iHandler = pHandler;
		this.iSsl = pSsl;
		this.iServerName = pSsl ? "HTTPS Server" : "HTTP Server";
		this.iSessionProperties = (pProperties != null) ? pProperties : WBEMConfiguration
				.getGlobalConfiguration();
		SSLContext sslContext = pSsl ? HttpSocketFactory.getInstance().getServerSSLContext(
				this.iSessionProperties) : null;
		this.iServerSocket = (pLocalAddress != null && pLocalAddress.length() > 0) ? HttpSocketFactory
				.getInstance().getServerSocketFactory(sslContext).createServerSocket(pPort, 50,
						InetAddress.getByName(pLocalAddress))
				: HttpSocketFactory.getInstance().getServerSocketFactory(sslContext)
						.createServerSocket(pPort);
		if (this.iServerSocket instanceof SSLServerSocket) {
			if (this.iSessionProperties.getSslListenerPeerVerification().equalsIgnoreCase("ignore")) {
				logger.trace(Level.FINER, "Listener peer verification: ignore");
				((SSLServerSocket) this.iServerSocket).setNeedClientAuth(false);
			} else if (this.iSessionProperties.getSslListenerPeerVerification().equalsIgnoreCase(
					"accept")) {
				logger.trace(Level.FINER, "Listener peer verification: accept");
				((SSLServerSocket) this.iServerSocket).setWantClientAuth(true);
			} else {
				logger.trace(Level.FINER, "Listener peer verification: require");
				((SSLServerSocket) this.iServerSocket).setNeedClientAuth(true);
			}

			String disableCipherSuites = this.iSessionProperties
					.getSslListenerCipherSuitesToDisable();
			if (disableCipherSuites != null) {
				SSLServerSocket sslSock = (SSLServerSocket) this.iServerSocket;
				String[] currentCipherSuites = sslSock.getEnabledCipherSuites();
				String[] updatedCipherSuites = Util.getFilteredStringArray(currentCipherSuites,
						disableCipherSuites);
				sslSock.setEnabledCipherSuites(updatedCipherSuites);
				int before = currentCipherSuites.length;
				int after = updatedCipherSuites.length;
				if (before > 0 && after == 0) logger.trace(Level.WARNING,
						"All cipher suites disabled for listener!");
				else if (before > after) logger.trace(Level.FINE, "Some (" + (before - after)
						+ ") cipher suites disabled for listener");
				else if (before == after) logger.trace(Level.FINER,
						"No cipher suites disabled for listener");
			}
		}
		this.iTimeout = this.iSessionProperties.getListenerHttpTimeout();
		logger.exit();
	}

	/**
	 * Set the name of the thread
	 * 
	 * @param pName
	 *            The new value
	 */
	public void setName(String pName) {
		if (this.iRunner != null) this.iRunner.setName(pName);
	}

	/**
	 * Returns the port
	 * 
	 * @return The port
	 */
	public int getPort() {
		return this.iServerSocket.getLocalPort();
	}

	/**
	 * Returns the local ip address the socket is bound to
	 * 
	 * @return The ip address
	 * @throws UnknownHostException
	 */
	public String getLocalIp() throws UnknownHostException {
		String ip = this.iServerSocket.getInetAddress().getHostAddress();
		String localhost = InetAddress.getLocalHost().getHostAddress();
		return "0.0.0.0".equals(ip) ? localhost : ip;
	}

	/**
	 * Returns the local hostname the socket is bound to
	 * 
	 * @return The host name
	 * @throws UnknownHostException
	 */
	public String getLocalHostName() throws UnknownHostException {
		String ip = this.iServerSocket.getInetAddress().getHostName();
		String localhost = InetAddress.getLocalHost().getHostName();
		return "0.0.0.0".equals(ip) ? localhost : ip;
	}

	/**
	 * Return whether this connection is SSL secured
	 * 
	 * @return <code>true</code> if SSL is enabled, <code>false</code> otherwise
	 */
	public boolean isSSL() {
		return this.iSsl;
	}

	/**
	 * Starts a thread that waits for incoming connections
	 */
	public void start() {
		if (this.iClose) {
			this.iClose = false;
			ThreadGroup group = new ThreadGroup("CIMListener on port " + String.valueOf(this.iPort));
			this.iDispatcher = new HttpConnectionDispatcher(group, this.iHandler, new ThreadPool(
					this.iSessionProperties.getListenerMinPoolSize(), this.iSessionProperties
							.getListenerMaxPoolSize(),
					this.iSessionProperties.getListenerBacklog(), this.iSessionProperties
							.getListenerMaxIdle(), group, "Handler "), this.iSessionProperties
					.getListenerMaxQueueSize());
			this.iDispatcher.start();
			this.iRunner = new Thread(group, this, this.iServerName);
			this.iRunner.setDaemon(true);

			this.iRunner.start();
		}
	}

	public void run() {
		while (!this.iClose) {
			try {
				Socket socket = this.iServerSocket.accept();
				try {
					socket.setTcpNoDelay(true);
					socket.setSoTimeout(this.iTimeout);
				} catch (IOException e) {
					LogAndTraceBroker.getBroker().trace(Level.FINE,
							"Exception while adjusting socket options", e);
				}
				boolean dispatched = this.iDispatcher.dispatch(socket);
				if (!dispatched) {
					MessageWriter writer = new MessageWriter(socket.getOutputStream(), false, false);
					try {
						writer.setMethod(new HttpServerMethod(1, 1, 503,
								"Service temporarily overloaded"));
						writer.getHeader().addField("Retry-After", "10");
						writer.close();
					} catch (IOException e) {
						LogAndTraceBroker.getBroker().trace(Level.FINE,
								"Exception while sending HTTP 503", e);
					} finally {
						socket.close();
					}
					LogAndTraceBroker
							.getBroker()
							.trace(Level.FINE,
									"HttpServerConnection failed to dispatch incoming connection, sent 503");
				} else {
					LogAndTraceBroker.getBroker().trace(Level.FINE,
							"HttpServerConnection dispatched incoming connection");
				}
			} catch (Throwable t) {
				if (t instanceof SocketException && this.iClose) {
					break;
				}
				try {
					LogAndTraceBroker.getBroker().trace(Level.FINE,
							"Exception while waiting for incoming http connections");
				} catch (Throwable t2) {
					// just give up
				}
			}
		}

		// shutdown

		try {
			LogAndTraceBroker.getBroker().trace(Level.FINE,
					"Shutting down CIMListener on port " + this.iPort);
		} catch (Throwable t) {
			// do nothing
		}
		try {
			// give the handlers a chance to process all already accepted
			// connections before complete shutdown
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// just ignore
		}
		try {
			this.iDispatcher.close();
		} catch (Exception e) {
			LogAndTraceBroker.getBroker().trace(Level.FINE,
					"Exception while closing http connection dispatcher", e);
		}
		this.iDispatcher = null;
		this.iRunner = null;
	}

	/**
	 * Closes the socket and shuts down the listening threads
	 */
	public void close() {
		if (!this.iClose) {
			this.iClose = true;
			try {
				this.iServerSocket.close();
				this.iServerSocket = null;
			} catch (Exception e) {
				LogAndTraceBroker.getBroker().trace(Level.FINE,
						"Exception while closing server socket", e);
			}
		}
	}

	/**
	 * Class HttpConnectionDispatcher is responsible for dispatching the
	 * incoming connections to the handlers. It doesn't execute the handler
	 * directly but creates a runnable that is submitted to a thread pool which
	 * takes care of execution.
	 * 
	 */
	private static class HttpConnectionDispatcher extends Thread {

		private BlockingQueue<Socket> iConnectionPool;

		private volatile boolean iAlive = true;

		private HttpConnectionHandler iHandler;

		private ThreadPool iThreadPool;

		/**
		 * Ctor.
		 * 
		 * @param pGroup
		 *            The thread group to use for this thread and it's children
		 * @param pHandler
		 *            The connection handler
		 * @param pPool
		 *            The thread pool
		 * @param pQueueSize
		 *            The fixed capacity for the queue of pending connections
		 */
		public HttpConnectionDispatcher(ThreadGroup pGroup, HttpConnectionHandler pHandler,
				ThreadPool pPool, int pQueueSize) {
			super(pGroup, "Connection Dispatcher");
			setDaemon(true);
			this.iConnectionPool = new ArrayBlockingQueue<Socket>(pQueueSize > 0 ? pQueueSize : 1);
			this.iHandler = pHandler;
			this.iThreadPool = pPool;
		}

		/**
		 * Dispatches a connection
		 * 
		 * @param pSocket
		 *            The socket of the connection
		 * @return true if dispatch was successful
		 */
		public boolean dispatch(Socket pSocket) {
			try {
				return this.iConnectionPool.offer(pSocket, 20, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				/* */
			}
			return false;
		}

		/**
		 * Gets the next pending connection
		 * 
		 * @return The socket of the connection
		 */
		public Socket getConnection() {
			try {
				return this.iConnectionPool.poll(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// just ignore it, it is expected
			}
			return null;
		}

		@Override
		public void run() {
			while (this.iAlive) {
				try {
					Socket socket = getConnection();
					if (socket != null) {
						this.iThreadPool.execute(new HttpServerWorker(this.iHandler, socket));
					}
				} catch (Throwable t) {
					try {
						LogAndTraceBroker.getBroker().trace(Level.FINE,
								"Exception while submitting worker to thread pool", t);
					} catch (Throwable t1) {
						// forget it
					}
				}
			}
			try {
				this.iHandler.close();
			} catch (Exception e) {
				LogAndTraceBroker.getBroker().trace(Level.FINE,
						"Exception while closing http connection handler", e);
			}
			try {
				this.iThreadPool.shutdown();
			} catch (Exception e) {
				LogAndTraceBroker.getBroker().trace(Level.FINE,
						"Exception during shut down of thread pool", e);
			}
		}

		/**
		 * Closes the dispatcher
		 */
		public void close() {
			this.iAlive = false;
		}
	}
}
