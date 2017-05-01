/**
 * (C) Copyright IBM Corp. 2005, 2010
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
 * Flag     Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1353168  2005-11-24  fiuczy       Possible NullPointerExcection in HttpClient.streamFinished()
 * 1488924  2006-05-15  lupusalex    Intermittent connection loss
 * 1535756  2006-08-07  lupusalex    Make code warning free
 * 1565892  2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590  2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131  2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3026360  2010-07-07  blaschke-oss Handle unwritten fields
 */
package org.sblim.cimclient.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketPermission;
import java.net.URI;
import java.security.Permission;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * Class HttpUrlConnection encapsulates a http connection
 * 
 */
public class HttpUrlConnection extends HttpURLConnection {

	private boolean iConnected;

	private HttpClient iHttpClient;

	protected URI iUrl;

	private HttpClientPool iHttpClientPool;

	AuthorizationHandler iAuthHandler;

	/**
	 * Ctor.
	 * 
	 * @param pUri
	 *            The host URI
	 * @param pHttpClientPool
	 *            The client pool
	 * @param pAuthHandler
	 *            The authentication handler
	 */
	public HttpUrlConnection(URI pUri, HttpClientPool pHttpClientPool,
			AuthorizationHandler pAuthHandler) {
		super(null);
		this.iUrl = pUri;
		this.iHttpClientPool = pHttpClientPool;
		this.iAuthHandler = pAuthHandler;
		// iLogger = GlobalProperties.getLogger();
	}

	@Override
	public String toString() {
		return "HttpUrlConnection=[url=" + this.iUrl + ",PoolSize="
				+ this.iHttpClientPool.getNumberOfAvailableConnections() + "," + this.iAuthHandler
				+ "]";
	}

	@Override
	public Permission getPermission() {
		int port = this.iUrl.getPort();
		port = port < 0 ? 80 : port;
		String host = this.iUrl.getHost() + ":" + port;
		Permission permission = new SocketPermission(host, "connect");
		return permission;
	}

	@Override
	public synchronized void connect() throws IOException {
		if (!this.iConnected) getClient();
		this.iHttpClient.connect();
	}

	private synchronized void getClient() {
		if (this.iConnected) return;

		this.iHttpClient = HttpClient.getClient(this.iUrl, this.iHttpClientPool, this.iAuthHandler);
		this.iHttpClient.reset();
		this.iConnected = true;
	}

	@Override
	public synchronized void setRequestMethod(String pMethod) {
		if (!this.iConnected) getClient();
		this.iHttpClient.setRequestMethod(pMethod);
		this.method = pMethod;
	}

	@Override
	public synchronized void setRequestProperty(String key, String value) {
		if (!this.iConnected) getClient();
		this.iHttpClient.setRequestProperty(key, value);
	}

	@Override
	public synchronized void disconnect() {
		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		if (this.iConnected) {
			this.iConnected = false;
			this.iHttpClient.disconnect();
			if (this.iHttpClientPool != null) {
				this.iHttpClientPool.removeConnectionFromPool(this.iHttpClient);
				this.iHttpClient = null;
			}
		}
		logger.exit();
	}

	/**
	 * Closes the client pool
	 */
	public synchronized void close() {
		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		if (this.iHttpClientPool != null) {
			synchronized (this.iHttpClientPool) {
				this.iHttpClientPool.closePool();
				this.iHttpClientPool = null;
			}
		}
		logger.exit();
	}

	@Override
	public synchronized InputStream getInputStream() throws IOException {
		if (!this.iConnected) getClient();
		try {
			return this.iHttpClient.getInputStream();
		} catch (RuntimeException e) {
			disconnect();
			throw e;
		} catch (IOException e) {
			disconnect();
			throw e;
		}
	}

	@Override
	public synchronized OutputStream getOutputStream() throws RuntimeException {
		if (!this.iConnected) getClient();
		try {
			return this.iHttpClient.getOutputStream();
		} catch (RuntimeException e) {
			disconnect();
			throw e;
		}
	}

	@Override
	public boolean usingProxy() {
		return false;
	}

	/**
	 * Returns the http client
	 * 
	 * @return The http client
	 */
	public synchronized HttpClient getHttpClient() {
		if (!this.iConnected) getClient();
		return this.iHttpClient;
	}

	@Override
	public synchronized String getHeaderField(String name) {
		if (!this.iConnected) getClient();
		return this.iHttpClient.getHeaderField(name);
	}

	@Override
	public synchronized String getHeaderFieldKey(int index) {
		if (!this.iConnected) getClient();
		return this.iHttpClient.getHeaderFieldName(index);
	}

	@Override
	public synchronized String getHeaderField(int index) {
		if (!this.iConnected) getClient();
		return this.iHttpClient.getHeaderFieldValue(index);
	}

	@Override
	public synchronized String getRequestProperty(String key) {
		if (!this.iConnected) getClient();
		return this.iHttpClient.getRequestProperty(key);
	}

	@Override
	public synchronized String getRequestMethod() {
		if (!this.iConnected) getClient();
		return this.iHttpClient.getRequestMethod();
	}

	/**
	 * Resets the http client
	 */
	public synchronized void reset() {
		if (!this.iConnected) getClient();
		this.iHttpClient.reset();
	}

	@Override
	public synchronized int getResponseCode() throws IOException {
		if (!this.iConnected) getClient();
		return this.iHttpClient.getResponseCode();
	}

	@Override
	public synchronized String getResponseMessage() {
		if (!this.iConnected) getClient();
		return this.iHttpClient.getResponseMessage();
	}

	/**
	 * Enables/Disables the use of http 1.1
	 * 
	 * @param pUse11
	 *            If <code>true</code> http 1.1 is enabled.
	 */
	public synchronized void useHttp11(boolean pUse11) {
		if (!this.iConnected) getClient();
		this.iHttpClient.useHttp11(pUse11);
	}
}
