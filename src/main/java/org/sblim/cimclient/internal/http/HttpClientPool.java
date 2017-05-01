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
 * 1353168    2005-11-24  fiuczy       Possible NullPointerExcection in HttpClient.streamFinished()
 * 1498130    2006-05-31  lupusalex    Selection of xml parser on a per connection basis
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1646434    2007-01-28  lupusalex    CIMClient close() invalidates all it's enumerations
 * 1647159    2007-01-29  lupusalex    HttpClientPool runs out of HttpClients
 * 1660743    2007-02-15  lupusalex    SSLContext is static
 * 1714902    2007-05-08  lupusalex    Threading related weak spots
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 *    2618    2013-02-27  blaschke-oss Need to add property to disable weak cipher suites for the secure indication
 *    2642    2013-05-21  blaschke-oss Seperate properties needed for cim client and listener to filter out ciphers
 */

package org.sblim.cimclient.internal.http;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.net.ssl.SSLContext;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.util.Util;
import org.sblim.cimclient.internal.util.WBEMConfiguration;

/**
 * Class HttpClientPool implements a pool for http client connections
 * 
 */
public class HttpClientPool {

	/**
	 * @threading Guarded by "this"
	 */
	private List<HttpClient> iAllConnections;

	/**
	 * @threading Guarded by "this"
	 */
	private List<HttpClient> iAvailableConnections;

	/**
	 * @threading Guarded by "this"
	 */
	private boolean iClosed = false;

	private final int iPoolSize;

	private final WBEMConfiguration iConfiguration;

	/**
	 * @threading Guarded by "this"
	 */
	private SSLContext iSslContext;

	private String[] iEnabledCipherSuites = null;

	/**
	 * Ctor.
	 * 
	 * @param pConfiguration
	 *            The configuration for this session. Must be non-null.
	 */
	public HttpClientPool(WBEMConfiguration pConfiguration) {
		super();
		this.iConfiguration = pConfiguration;
		this.iAllConnections = new ArrayList<HttpClient>(pConfiguration.getHttpPoolSize());
		this.iAvailableConnections = new ArrayList<HttpClient>(pConfiguration.getHttpPoolSize());
		this.iPoolSize = pConfiguration.getHttpPoolSize();
		this.iSslContext = null;
		this.iEnabledCipherSuites = null;
	}

	/**
	 * Returns the number of connections in this pool that are available/free
	 * for (re-)use.
	 * 
	 * @return number of available/free connections in pool
	 */
	public synchronized int getNumberOfAllConnections() {
		return this.iAllConnections.size();
	}

	/**
	 * Returns the number of all connections in this pool.
	 * 
	 * @return number of all connections in pool
	 */
	public synchronized int getNumberOfAvailableConnections() {
		return this.iAvailableConnections.size();
	}

	/**
	 * Returns the configuration context of this pool
	 * 
	 * @return The configuration
	 */
	public WBEMConfiguration getConfigurationContext() {
		return this.iConfiguration;
	}

	/**
	 * Returns the SSL context for the clients in this pool. The context is
	 * initialized on the first call of this method (lazy initialization).
	 * 
	 * @return The SSL context
	 */
	public synchronized SSLContext getSslContext() {
		if (this.iSslContext == null) {
			this.iSslContext = HttpSocketFactory.getInstance().getClientSSLContext(
					this.iConfiguration);
		}
		return this.iSslContext;
	}

	/**
	 * Returns the available connections of this pool for a given
	 * URI&AuthorizationHandler
	 * 
	 * @param pUri
	 *            The uri
	 * @param pHandler
	 *            The authorization handler
	 * @return A connection if one is available, <code>null</code> otherwise
	 */
	public synchronized HttpClient retrieveAvailableConnectionFromPool(URI pUri,
			AuthorizationHandler pHandler) {
		if (this.iClosed) {
			LogAndTraceBroker.getBroker().trace(Level.FINE,
					"Attempt to get client from closed http client pool,");
			throw new IllegalStateException("HttpClientPool is closed. Retrieve connection failed.");
		}
		if (getNumberOfAvailableConnections() > 0) {
			LogAndTraceBroker.getBroker().trace(
					Level.FINE,
					"Reusing client (" + pUri.toString() + ", max: " + getPoolSize() + ", current:"
							+ getNumberOfAvailableConnections());

			return this.iAvailableConnections.remove(0);
		}

		LogAndTraceBroker.getBroker().trace(
				Level.FINE,
				"New client (" + pUri.toString() + ", max: " + getPoolSize() + ", current:"
						+ getNumberOfAvailableConnections());
		HttpClient client = new HttpClient(pUri, this, pHandler);
		addConnectionToPool(client);
		return client;
	}

	/**
	 * Add the connection to the pool. Connection is added as available
	 * connection. Use method {@link #addConnectionToPool(HttpClient)} to add
	 * the connection without being available for reuse.
	 * 
	 * @param httpClient
	 *            connection that is to be added to the pool
	 * @return true if connection was added otherwise false
	 */
	public synchronized boolean returnAvailableConnectionToPool(HttpClient httpClient) {
		if (httpClient == null) { return false; }

		if (this.iClosed) {
			this.iAllConnections.remove(httpClient);
			httpClient.disconnect();
			return false;
		}

		if (getPoolSize() > this.iAvailableConnections.size()) {
			if (!this.iAvailableConnections.contains(httpClient)) {
				// ensure that the connection exists in allConnections owned by
				// pool
				addConnectionToPool(httpClient);
				this.iAvailableConnections.add(httpClient);
				return true;
			}
		} else {
			LogAndTraceBroker.getBroker().trace(Level.FINE,
					"Http pool size reached, discarding client.");
			this.iAllConnections.remove(httpClient);
			this.iAvailableConnections.remove(httpClient);
			httpClient.disconnect();
		}
		return false; // connection was not added to pool
	}

	/**
	 * Add the connection to the pool, but does NOT add it as available
	 * connection. Use method
	 * {@link #returnAvailableConnectionToPool(HttpClient)} to also add the
	 * connection to the available connections.
	 * 
	 * @param httpClient
	 *            connection that is to be added to the pool
	 * @return true if connection was added otherwise false
	 */
	public synchronized boolean addConnectionToPool(HttpClient httpClient) {
		if (this.iClosed) {
			LogAndTraceBroker.getBroker().trace(Level.FINE,
					"Attempt to add client to closed http client pool,");
			throw new IllegalStateException("HttpClientPool is closed. Add connection failed.");
		}
		if (httpClient != null && !this.iAllConnections.contains(httpClient)) {
			// if connection is not in pool add it
			this.iAllConnections.add(httpClient);
			return true;
		}
		return false;
	}

	/**
	 * Removes a connection completely from the pool.
	 * 
	 * @param httpClient
	 *            connection that is to be removed from the pool
	 * @return true if connection was removed otherwise false
	 */
	public synchronized boolean removeConnectionFromPool(HttpClient httpClient) {
		if (httpClient != null) {
			this.iAvailableConnections.remove(httpClient);
			if (this.iAllConnections.remove(httpClient)) { return true; }
			return false; // connection was not in pool!
		}
		return false; // no connection given
	}

	/**
	 * Closes the pool. This will disconnect all clients in the pool.
	 */
	public synchronized void closePool() {
		this.iClosed = true;
		Iterator<HttpClient> iter = this.iAvailableConnections.iterator();
		while (iter.hasNext()) {
			HttpClient httpClient = iter.next();
			this.iAllConnections.remove(httpClient);
			httpClient.disconnect();
		}
		this.iAvailableConnections.clear();
	}

	@Override
	protected void finalize() throws Throwable {
		closePool();
		super.finalize();
	}

	/**
	 * Returns poolSize
	 * 
	 * @return The value of poolSize.
	 */
	public int getPoolSize() {
		return this.iPoolSize;
	}

	/**
	 * Returns updated array of cipher suites which is current cipher suites
	 * less any cipher suites listed to be disabled
	 * 
	 * NOTE: The updated array is generated only upon first invocation and then
	 * saved, effectively making this a lazy initialization of the cipher suites
	 * on a HttpClientPool basis - it has to be done here and not in WBEMClient
	 * where it belongs because socket characteristics are not known to
	 * WBEMClient
	 * 
	 * @param pCurrentCipherSuites
	 *            The currently enabled cipher suites
	 * @param pDisableCipherSuites
	 *            The list of cipher suites to be disabled
	 * @return The updated enabled cipher suites
	 */
	public synchronized String[] getUpdatedCipherSuites(String[] pCurrentCipherSuites,
			String pDisableCipherSuites) {
		if (this.iEnabledCipherSuites == null) {
			this.iEnabledCipherSuites = Util.getFilteredStringArray(pCurrentCipherSuites,
					pDisableCipherSuites);
			int before = pCurrentCipherSuites.length;
			int after = this.iEnabledCipherSuites.length;
			if (before > 0 && after == 0) LogAndTraceBroker.getBroker().trace(Level.WARNING,
					"All cipher suites disabled for client!");
			else if (before > after) LogAndTraceBroker.getBroker().trace(Level.FINE,
					"Some (" + (before - after) + ") cipher suites disabled for client");
			else if (before == after) LogAndTraceBroker.getBroker().trace(Level.FINER,
					"No cipher suites disabled for client");
		}
		return this.iEnabledCipherSuites;
	}
}
