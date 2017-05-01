/**
 * (C) Copyright IBM Corp. 2006, 2013
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
 * 1565892    2007-01-08  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3023348    2010-07-02  blaschke-oss Listener uses # constructor instead of valueOf
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3469018    2012-01-03  blaschke-oss Properties not passed to CIMIndicationHandler
 * 3477087    2012-01-23  blaschke-oss Need Access to an Indication Sender's IP Address
 * 3496385    2012-03-02  blaschke-oss JSR48 1.0.0: add WBEMListener get/setProperty
 * 3513228    2012-04-23  blaschke-oss Reliable Indications support can create lots of threads
 * 3529062    2012-05-23  blaschke-oss WBEMListenerFactory should return new instance
 * 3529065    2012-05-31  hellerda     Enable WBEMListener get/setProperty
 *    2628    2013-03-26  blaschke-oss Limit size of LinkedList of CIMEvents to be dispatched
 *    2635    2013-05-16  blaschke-oss Slowloris DoS attack for CIM indication listener port
 *    2657    2013-08-20  blaschke-oss Potential null pointer exception in handleConnection
 */

package org.sblim.cimclient;

import java.io.IOException;
import java.net.BindException;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.wbem.listener.IndicationListener;
import javax.wbem.listener.WBEMListener;
import javax.wbem.listener.WBEMListenerConstants;

import org.sblim.cimclient.internal.http.HttpConnectionHandler;
import org.sblim.cimclient.internal.http.HttpServerConnection;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.sblim.cimclient.internal.util.WBEMConstants;
import org.sblim.cimclient.internal.wbem.indications.CIMEventDispatcher;
import org.sblim.cimclient.internal.wbem.indications.CIMIndicationHandler;

/**
 * Class WBEMListenerSBLIM is the SBLIM implementation of the WBEMListener
 * interface.
 * 
 */
public class WBEMListenerSBLIM implements WBEMListener {

	/**
	 * The real implementation of a listener that starts a HTTP server and
	 * processes incoming indications
	 * 
	 */
	public class WBEMListenerImpl {

		private EventListener iIndicationListener;

		private HttpServerConnection iConnection;

		private CIMIndicationHandler iIndicationHandler;

		private HttpConnectionHandler iConnectionHandler;

		/**
		 * Ctor.
		 * 
		 * @param pLocalAddress
		 *            The local address to bind the port to. If null the port is
		 *            bound to all local addresses. For use on multi-homed
		 *            systems.
		 * @param pPort
		 *            The port to listen on. If zero any free port will be
		 *            chosen.
		 * @param pSSL
		 *            SSL secured connection?
		 * @param pIndicationListener
		 *            The indication listener to forward the incoming
		 *            indications to (an instance of IndicationListener or
		 *            IndicationListenerSBLIM).
		 * @param pProperties
		 *            The configuration.
		 * @throws IOException
		 */
		public WBEMListenerImpl(String pLocalAddress, int pPort, boolean pSSL,
				EventListener pIndicationListener, Properties pProperties) throws IOException {

			// Merge any properties passed via addListener
			if (pProperties != null) {
				for (Enumeration<Object> e = pProperties.keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
					String value = pProperties.getProperty(key);
					setProperty(key, value);
				}
			}
			WBEMConfiguration config = WBEMListenerSBLIM.this.iConfiguration;
			if (!(pIndicationListener instanceof IndicationListener)
					&& !(pIndicationListener instanceof IndicationListenerSBLIM)) throw new IllegalArgumentException(
					"Listener must be instance of IndicationListener or IndicationListenerSBLIM");
			this.iIndicationListener = pIndicationListener;
			CIMEventDispatcher eventDispatcher = new CIMEventDispatcher(this.iIndicationListener,
					config.getListenerMaxQueuedEvents());
			this.iIndicationHandler = new CIMIndicationHandler(eventDispatcher, config);
			this.iConnectionHandler = new HttpConnectionHandler(this.iIndicationHandler, config);
			this.iConnection = new HttpServerConnection(this.iConnectionHandler, pLocalAddress,
					pPort, pSSL, config);
		}

		@Override
		protected void finalize() throws Throwable {
			try {
				stop();
			} finally {
				super.finalize();
			}
		}

		/**
		 * Starts the HTTP server connection receiving the indications.
		 */
		public void start() {
			this.iConnection.start();
		}

		/**
		 * Stops the HTTP server connection receiving the indications and the
		 * indication handler.
		 */
		public void stop() {
			this.iConnection.close();
			this.iIndicationHandler.close();
		}

		/**
		 * Returns the listener we forward the indications to.
		 * 
		 * @return The listener.
		 */
		public IndicationListener getIndicationListener() {
			return (this.iIndicationListener instanceof IndicationListener) ? (IndicationListener) this.iIndicationListener
					: null;
		}

		/**
		 * Returns the listener we forward the indications to.
		 * 
		 * @return The listener.
		 */
		public IndicationListenerSBLIM getIndicationListenerSBLIM() {
			return (this.iIndicationListener instanceof IndicationListenerSBLIM) ? (IndicationListenerSBLIM) this.iIndicationListener
					: null;
		}

		/**
		 * Returns the listener port.
		 * 
		 * @return The listener port.
		 */
		public int getListenerPort() {
			return this.iConnection.getPort();
		}

		/**
		 * Get the IPs blocked by the listener.
		 * 
		 * @return The comma-separated list of blocked IPs.
		 */
		public String getBlockedIPs() {
			return this.iConnectionHandler.getBlockedIPs();
		}

		/**
		 * Set the IPs to be blocked by the listener.
		 * 
		 * @param pIPs
		 *            The comma-separated list of blocked IPs.
		 */
		public void setBlockedIPs(String pIPs) {
			this.iConnectionHandler.setBlockedIPs(pIPs);
		}
	}

	protected final WBEMConfiguration iConfiguration = new WBEMConfiguration(new Properties());

	private Map<Integer, WBEMListenerImpl> iPortMap = new HashMap<Integer, WBEMListenerImpl>();

	/**
	 * Ctor.
	 */
	public WBEMListenerSBLIM() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.wbem.listener.WBEMListener#addListener(javax.wbem.listener.
	 * IndicationListener, int, java.lang.String)
	 */
	public int addListener(IndicationListener pListener, int pPort, String pTransport)
			throws IOException {
		return addListener((EventListener) pListener, pPort, pTransport, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.wbem.listener.WBEMListener#addListener(javax.wbem.listener.
	 * IndicationListener, int, java.lang.String, java.lang.String)
	 */
	public int addListener(IndicationListener pListener, int pPort, String pTransport,
			String pLocalAddr) throws IOException {
		return addListener((EventListener) pListener, pPort, pTransport, pLocalAddr, null);
	}

	/**
	 * Add a new listener using the specified port.
	 * 
	 * @param pListener
	 *            The Indication Listener that will be called when an indication
	 *            is received.
	 * @param pPort
	 *            The port to listen on. Use 0 to specify any available port.
	 * @param pTransport
	 *            The transport to use (e.g. HTTP or HTTPS).
	 * @param pLocalAddr
	 *            The local IP address to bind to. This is only needed in
	 *            multi-homed systems. A value of <code>null</code> will bind to
	 *            all IP addresses.
	 * @param pConfigurationProperties
	 *            The individual configuration properties for this listener.
	 * @return The port that was used.
	 * @throws IOException
	 *             This exception is thrown when binding to pPort fails.
	 */
	public int addListener(IndicationListener pListener, int pPort, String pTransport,
			String pLocalAddr, Properties pConfigurationProperties) throws IOException {

		return addListener((EventListener) pListener, pPort, pTransport, pLocalAddr,
				pConfigurationProperties);
	}

	/**
	 * Add a new listener using the specified port.
	 * 
	 * @param pListener
	 *            The SBLIM Indication Listener that will be called when an
	 *            indication is received.
	 * @param pPort
	 *            The port to listen on. Use 0 to specify any available port.
	 * @param pTransport
	 *            The transport to use (e.g. HTTP or HTTPS).
	 * @return The port that was used.
	 * @throws IOException
	 *             This exception is thrown when binding to pPort fails.
	 */
	public int addListener(IndicationListenerSBLIM pListener, int pPort, String pTransport)
			throws IOException {
		return addListener((EventListener) pListener, pPort, pTransport, null, null);
	}

	/**
	 * Add a new listener using the specified port and local address.
	 * 
	 * @param pListener
	 *            The SBLIM Indication Listener that will be called when an
	 *            indication is received.
	 * @param pPort
	 *            The port to listen on. Use 0 to specify any available port.
	 * @param pTransport
	 *            The transport to use (e.g. HTTP or HTTPS).
	 * @param pLocalAddr
	 *            The local IP address to bind to. This is only needed in
	 *            multi-homed systems. A value of <code>null</code> will bind to
	 *            all IP addresses.
	 * @return The port that was used.
	 * @throws IOException
	 *             This exception is thrown when binding to pPort fails.
	 */
	public int addListener(IndicationListenerSBLIM pListener, int pPort, String pTransport,
			String pLocalAddr) throws IOException {
		return addListener((EventListener) pListener, pPort, pTransport, pLocalAddr, null);
	}

	/**
	 * Add a new listener using the specified port, local address and
	 * properties.
	 * 
	 * @param pListener
	 *            The SBLIM Indication Listener that will be called when an
	 *            indication is received.
	 * @param pPort
	 *            The port to listen on. Use 0 to specify any available port.
	 * @param pTransport
	 *            The transport to use (e.g. HTTP or HTTPS).
	 * @param pLocalAddr
	 *            The local IP address to bind to. This is only needed in
	 *            multi-homed systems. A value of <code>null</code> will bind to
	 *            all IP addresses.
	 * @param pConfigurationProperties
	 *            The individual configuration properties for this listener.
	 * @return The port that was used.
	 * @throws IOException
	 *             This exception is thrown when binding to pPort fails.
	 */
	public int addListener(IndicationListenerSBLIM pListener, int pPort, String pTransport,
			String pLocalAddr, Properties pConfigurationProperties) throws IOException {

		return addListener((EventListener) pListener, pPort, pTransport, pLocalAddr,
				pConfigurationProperties);
	}

	/**
	 * Add a new listener using the specified port, local address and
	 * properties. This is the worker routine for all public addListener
	 * methods.
	 * 
	 * @param pListener
	 *            The indication listener (<code>IndicationListener</code> or
	 *            <code>IndicationListenerSBLIM</code>) that will be called when
	 *            an indication is received.
	 * @param pPort
	 *            The port to listen on. Use 0 to specify any available port.
	 * @param pTransport
	 *            The transport to use (e.g. HTTP or HTTPS).
	 * @param pLocalAddr
	 *            The local IP address to bind to. This is only needed in
	 *            multi-homed systems. A value of <code>null</code> will bind to
	 *            all IP addresses.
	 * @param pConfigurationProperties
	 *            The individual configuration properties for this listener.
	 * @return The port that was used.
	 * @throws IOException
	 *             This exception is thrown when binding to pPort fails.
	 */
	private synchronized int addListener(EventListener pListener, int pPort, String pTransport,
			String pLocalAddr, Properties pConfigurationProperties) throws IOException {

		if (pPort > 0 && this.iPortMap.containsKey(Integer.valueOf(pPort))) { throw new BindException(
				"Port already in use."); }
		boolean ssl;
		if (pTransport.equalsIgnoreCase(WBEMConstants.HTTP)) ssl = false;
		else if (pTransport.equalsIgnoreCase(WBEMConstants.HTTPS)) ssl = true;
		else throw new IllegalArgumentException("Unknown transport: " + pTransport
				+ "! Valid values are http and https.");

		WBEMListenerImpl listener = new WBEMListenerImpl(pLocalAddr, pPort, ssl, pListener,
				pConfigurationProperties);
		listener.start();

		this.iPortMap.put(Integer.valueOf(listener.getListenerPort()), listener);

		return listener.getListenerPort();
	}

	/**
	 * Get the IPs blocked by the listener associated with the specified port.
	 * 
	 * @param pPort
	 *            The port.
	 * @return The comma-separated list of blocked IPs.
	 */
	public String getBlockedIPs(int pPort) {
		if (pPort <= 0 || !this.iPortMap.containsKey(Integer.valueOf(pPort))) { throw new IllegalArgumentException(
				"Port not in use."); }
		WBEMListenerImpl listener = this.iPortMap.get(Integer.valueOf(pPort));
		return listener != null ? listener.getBlockedIPs() : null;
	}

	public String getProperty(String pName) {
		if (pName.startsWith("javax.wbem.")) {
			// Process JSR48 properties
			if (pName.equals(WBEMListenerConstants.PROP_LISTENER_KEYSTORE)) {
				return this.iConfiguration.getSslKeyStorePath();
			} else if (pName.equals(WBEMListenerConstants.PROP_LISTENER_KEYSTORE_PASSWORD)) {
				return this.iConfiguration.getSslKeyStorePassword();
			} else if (pName.equals(WBEMListenerConstants.PROP_LISTENER_TRUSTSTORE)) {
				return this.iConfiguration.getSslTrustStorePath();
			} else {
				return null;
			}
		}
		return this.iConfiguration.getDomainProperty(pName);
	}

	public synchronized void removeListener(int pPort) {
		WBEMListenerImpl listener = this.iPortMap.remove(Integer.valueOf(pPort));
		if (listener != null) {
			listener.stop();
		}
	}

	/**
	 * Set the IPs to be blocked by the listener associated with the specified
	 * port.
	 * 
	 * @param pPort
	 *            The port.
	 * @param pIPs
	 *            The comma-separated list of blocked IPs.
	 */
	public void setBlockedIPs(int pPort, String pIPs) {
		if (pPort <= 0 || !this.iPortMap.containsKey(Integer.valueOf(pPort))) { throw new IllegalArgumentException(
				"Port not in use."); }
		WBEMListenerImpl listener = this.iPortMap.get(Integer.valueOf(pPort));
		if (listener != null) {
			listener.setBlockedIPs(pIPs);
		}
	}

	public void setProperty(String pName, String pValue) {
		if (pName.startsWith("javax.wbem.")) {
			// Process JSR48 properties
			if (pName.equals(WBEMListenerConstants.PROP_LISTENER_KEYSTORE)) {
				this.iConfiguration.setDomainProperty(WBEMConfigurationProperties.KEYSTORE_PATH,
						pValue);
			} else if (pName.equals(WBEMListenerConstants.PROP_LISTENER_KEYSTORE_PASSWORD)) {
				this.iConfiguration.setDomainProperty(
						WBEMConfigurationProperties.KEYSTORE_PASSWORD, pValue);
			} else if (pName.equals(WBEMListenerConstants.PROP_LISTENER_TRUSTSTORE)) {
				this.iConfiguration.setDomainProperty(WBEMConfigurationProperties.TRUSTSTORE_PATH,
						pValue);
			} else {
				throw new IllegalArgumentException(pName);
			}
		} else {
			this.iConfiguration.setDomainProperty(pName, pValue);
		}
	}
}
