/**
 * HttpClient.java
 *
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
 * 13799      2004-12-07  thschaef     Fix chunking support 
 * 17620      2005-06-29  thschaef     eliminate ASCIIPrintStream1 in import statement
 * 17970      2005-08-11  pineiro5     Logon from z/OS not possible
 * 1353168    2005-11-24  fiuczy       Possible NullPointerExcection in HttpClient.streamFinished()
 * 1422316    2006-05-15  lupusalex    Disable delayed acknowledgement
 * 1486379    2006-05-29  lupusalex    CIM client retries twice when HTTP/1.1 401 is returned
 * 1498130    2006-05-31  lupusalex    Selection of xml parser on a per connection basis
 * 1516244    2006-07-10  ebak         GCJ support
 * 1536711    2006-08-15  lupusalex    NullPointerException causes client call to never return
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565091    2006-10-17  ebak         ssl handshake exception
 * 1516242    2006-11-27  lupusalex    Support of OpenPegasus local authentication
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1604329    2006-12-18  lupusalex    Fix OpenPegasus auth module
 * 1620526    2007-01-08  lupusalex    Socket Leak in HTTPClient.getResponseCode()
 * 1627832    2007-01-08  lupuslaex    Incorrect retry behaviour on HTTP 401
 * 1637546    2007-01-27  lupusalex    CIMEnumerationImpl has faulty close function
 * 1647148    2007-01-29  lupusalex    HttpClient.resetSocket() doesn't set socket timeout
 * 1647159    2007-01-29  lupusalex    HttpClientPool runs out of HttpClients
 * 1649595    2007-02-01  lupusalex    No chunkig requested
 * 1660743    2007-02-15  lupusalex    SSLContext is static
 * 1702832    2007-04-18  lupusalex    WBEMClientCIMXL.setCustomSocketFactory() not implemented
 * 1715511    2007-05-09  lupusalex    FVT: Wrong HTTP header values 
 * 1892046    2008-02-13  blaschke-oss Basic/digest authentication problem for Japanese users
 * 1931216    2008-04-01  blaschke-oss In HTTPClient need to get status before closing connection
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2372030    2008-12-01  blaschke-oss Add property to control synchronized SSL handshaking
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2714989    2009-03-26  blaschke-oss Code cleanup from redundant null check et al
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2817962    2009-08-05  blaschke-oss socket creation connects w/o a timeout
 * 2994776    2010-05-05  blaschke-oss http 401 gives CIM_ERR_FAILED instead of CIM_ERR_ACCESS_DENIED
 * 2997865    2010-05-07  blaschke-oss Infinite loop in HttpClient
 * 3004762    2010-06-16  blaschke-oss HTTPClient infinite loop for HTTP 407
 * 3022554    2010-06-30  blaschke-oss Flushing socket ignores skip() return code
 * 3046073    2010-09-07  blaschke-oss Performance hit due to socket conn. creation with timeout
 * 3195069    2011-02-28  blaschke-oss Need support to disable SSL Handshake
 * 3235440    2011-03-22  blaschke-oss NullPointerException when socket factory returns null
 * 3323310    2011-06-20  blaschke-oss Need the ability to override certain Global Properties
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3444912    2011-11-29  blaschke-oss Client delay during SSL handshake
 * 3492224    2012-02-23  blaschke-oss Need two different timeouts for Socket connections
 * 3504304    2012-03-14  blaschke-oss Rename socket timeout variables
 * 3523918    2012-05-06  blaschke-oss "java.io.IOException: Unexpected EOF" returned as HTTP 401
 * 3524050    2012-06-06  blaschke-oss Improve WWW-Authenticate in HTTPClient.java
 * 3557283    2012-11-05  blaschke-oss Print full response when get EOF from CIMOM
 * 3601894    2013-01-23  blaschke-oss Enhance HTTP and CIM-XML tracing
 *    2619    2013-02-22  blaschke-oss Host should contain port when not 5988/5989
 *    2621    2013-02-23  blaschke-oss Not all chunked input has trailers
 *    2618    2013-02-27  blaschke-oss Need to add property to disable weak cipher suites for the secure indication
 *    2642    2013-05-21  blaschke-oss Seperate properties needed for cim client and listener to filter out ciphers
 *    2654    2013-07-29  blaschke-oss Check jcc idle time with CIMOM keepalive timeout to avoid EOF
 *    2655    2013-08-14  blaschke-oss Content-length must be ignored when Transfer-encoding present
 *    2151    2013-08-20  blaschke-oss gzip compression not supported
 *    2709    2013-11-13  blaschke-oss Lower the level of the EOF message to FINE
 */
package org.sblim.cimclient.internal.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;

import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.sblim.cimclient.WBEMConfigurationProperties;
import org.sblim.cimclient.internal.http.HttpHeader.HeaderEntry;
import org.sblim.cimclient.internal.http.io.ASCIIPrintStream;
import org.sblim.cimclient.internal.http.io.BoundedInputStream;
import org.sblim.cimclient.internal.http.io.ChunkedInputStream;
import org.sblim.cimclient.internal.http.io.KeepAliveInputStream;
import org.sblim.cimclient.internal.http.io.PersistentInputStream;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.logging.Messages;
import org.sblim.cimclient.internal.util.WBEMConstants;

/**
 * Class HttpClient implements a HTTP client
 * 
 */
public class HttpClient implements HandshakeCompletedListener {

	private static class HostPortPair {

		String iHost;

		/**
		 * Ctor.
		 * 
		 * @param url
		 *            The url
		 */
		public HostPortPair(URI url) {
			this.iHost = url.getScheme().toLowerCase() + ':' + url.getHost().toLowerCase() + ':'
					+ url.getPort();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof HostPortPair)) return false;

			return this.iHost.equals(((HostPortPair) o).iHost);
		}

		@Override
		public int hashCode() {
			return this.iHost.hashCode();
		}

		@Override
		public String toString() {
			return "HostPortPair=[+" + this.iHost + "]";
		}
	}

	private static class GetProperty implements PrivilegedAction<Object> {

		String iPropertyName;

		GetProperty(String propertyName) {
			this.iPropertyName = propertyName;
		}

		public Object run() {
			return System.getProperty(this.iPropertyName);
		}
	}

	private static String iEncoding;

	static {
		try {
			iEncoding = (String) AccessController.doPrivileged(new PrivilegedAction<Object>() {

				public Object run() {
					return System.getProperty("file.encoding", "ISO8859_1");
				}
			});
			if (!isASCIISuperset(iEncoding)) iEncoding = "ISO8859_1";
		} catch (Exception exception) {
			iEncoding = "ISO8859_1";
		}
	}

	/**
	 * Converts a byte array to a String of hex digits
	 * 
	 * @param digest
	 *            The byte array
	 * @return The hex string
	 */
	public static String convertToHexString(byte[] digest) {
		char hexDigit[] = "0123456789abcdef".toCharArray();

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int b = digest[i];
			buf.append(hexDigit[(b >> 4) & 0xF]);
			buf.append(hexDigit[(b) & 0xF]);
		}
		return buf.toString();
	}

	/**
	 * Returns a client from a http client pool
	 * 
	 * @param url
	 *            The url to connect to
	 * @param clientPool
	 *            The client pool
	 * @param auth_handler
	 *            The authentication handler to use
	 * @return A http client from the pool
	 */
	public static HttpClient getClient(URI url, HttpClientPool clientPool,
			AuthorizationHandler auth_handler) {

		return clientPool.retrieveAvailableConnectionFromPool(url, auth_handler);
	}

	protected static String dequote(String str) {
		int len = str.length();
		if (len > 1 && str.charAt(0) == '\"' && str.charAt(len - 1) == '\"') return str.substring(
				1, len - 1);
		return str;
	}

	protected static void handleRsp(String authInfo, AuthorizationInfo prevAuthInfo)
			throws IOException {
		if (authInfo != null) {
			HttpHeader params = HttpHeader.parse(authInfo);

			String nonce = params.getField("nextnonce");
			if (nonce != null) {
				prevAuthInfo.setNonce(nonce);
				prevAuthInfo.setNc(0);
			} else {
				nonce = prevAuthInfo.getNonce();
			}
			String qop = params.getField("qop");
			if (qop != null) {
				if (!"auth".equalsIgnoreCase(qop) && !"auth-int".equalsIgnoreCase(qop)) {
					// TODO
					throw new IOException(
							"Authentication Digest with integrity check not supported");
				}
				byte[] rspauth;
				String rspauthStr = dequote(params.getField("rspauth"));
				if (rspauthStr != null) {
					rspauth = parseHex(rspauthStr);

					String cnonce = dequote(params.getField("cnonce"));
					if (cnonce != null && !cnonce.equals(prevAuthInfo.getCnonce())) { throw new IOException(
							"Digest authentication: Invalid nonce counter"); }
					String ncStr = params.getField("nc");
					if (ncStr != null) {
						try {
							long nc = Long.parseLong(ncStr, 16);
							if (nc != prevAuthInfo.getNc()) { throw new IOException(); }
						} catch (Exception e) {
							throw new IOException("Digest authentication: Invalid nonce counter");
						}
					}

					String HA1, HA2;
					MessageDigest md5;
					try {
						md5 = MessageDigest.getInstance("MD5");
						md5.reset();
						byte[] bytes = prevAuthInfo.getA1().getBytes("UTF-8");
						md5.update(bytes);
						HA1 = convertToHexString(md5.digest());
						if ("MD5-sess".equalsIgnoreCase(params.getField("algorithm"))) {
							md5.reset();
							md5.update((HA1 + ":" + nonce + ":" + cnonce).getBytes("UTF-8"));
							HA1 = convertToHexString(md5.digest());
						}

						HA2 = ":" + prevAuthInfo.getURI();
						if ("auth-int".equalsIgnoreCase(qop)) {
							md5.reset();
							md5.update(new byte[] {});
							HA2 += ":" + convertToHexString(md5.digest());
						}
						md5.reset();
						md5.update(HA2.getBytes("UTF-8"));
						HA2 = convertToHexString(md5.digest());

						md5.reset();
						md5.update((HA1 + ":" + nonce + ":" + ncStr + ":" + cnonce + ":" + qop
								+ ":" + HA2).getBytes("UTF-8"));
						String hsh = convertToHexString(md5.digest());
						byte[] hash = parseHex(hsh);

						if (!Arrays.equals(hash, rspauth)) throw new IOException(
								"Digest Authentication failed!");

					} catch (NoSuchAlgorithmException e1) {
						throw new IOException(
								"Unable to validate Authentication response: NoSuchAlgorithmException");
					}
				}
			} else {
				// TODO compute md5 of the entity-body
			}
		}
	}

	protected static byte[] parseHex(String hex) {
		byte[] value = new byte[hex.length() >> 1];
		int n = 0;
		for (int i = 0; i < value.length; i++) {
			value[i] = (byte) (0xff & Integer.parseInt(hex.substring(n, n + 1), 16));
			n += 2;
		}
		return value;
	}

	private static boolean isASCIISuperset(String charset) throws Exception {
		String asciiSuperSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'();/?:@&=+$,";
		byte abyte0[] = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72,
				73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99,
				100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115,
				116, 117, 118, 119, 120, 121, 122, 45, 95, 46, 33, 126, 42, 39, 40, 41, 59, 47, 63,
				58, 64, 38, 61, 43, 36, 44 };
		byte convertedArray[] = asciiSuperSet.getBytes(charset);
		return Arrays.equals(convertedArray, abyte0);
	}

	private boolean iConnected = false;

	private HttpClientPool iHttpClientPool;

	private AuthorizationHandler iAuth_handler;

	private SSLSession iSession;

	private InputStream iIStream;

	private boolean iUseHttp11 = true;

	private boolean iKeepAlive = true;

	private HttpClientMethod iMethod;

	private OutputStream iOStream;

	private AuthorizationInfo iPrevAuthInfo;

	private AuthorizationInfo iPrevProxy;

	private HttpHeader iRequestHeaders = new HttpHeader();

	private String iRequestMethod = "POST";

	private boolean iReset = true;

	private HttpClientMethod iResponse;

	private HttpHeader iResponseHeaders = new HttpHeader();

	private InputStream iServerInput;

	private ByteArrayOutputStream iServerOutput;

	private Socket iSocket;

	private URI iUrl;

	private long iPreviousResponseTime = -1;

	/**
	 * Ctor.
	 * 
	 * @param url
	 *            The url to connect to
	 * @param clientPool
	 *            The associated client pool
	 * @param auth_handler
	 *            The authentication handler
	 */
	public HttpClient(URI url, HttpClientPool clientPool, AuthorizationHandler auth_handler) {
		this.iUrl = url;
		this.iAuth_handler = auth_handler;
		this.iHttpClientPool = clientPool;
	}

	/**
	 * Connects to the http server
	 * 
	 * @throws IOException
	 */
	public void connect() throws IOException {
		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		try {
			this.iReset = true;
			this.iResponse = null;
			this.iConnected = true;
			this.iServerOutput = null;
			resetSocket();
		} finally {
			logger.exit();
		}
	}

	/**
	 * Disconnects the session
	 */
	public synchronized void disconnect() {
		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		this.iConnected = false;
		if (this.iSocket != null) {
			try {
				this.iSocket.close();
			} catch (IOException e) {
				logger.trace(Level.FINE, "Unexpected problem closing http socket", e);
			}
			this.iSocket = null;
			this.iServerInput = null;
			this.iReset = true;
			this.iResponse = null;
		}
		logger.exit();
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.iSocket.close();
		} catch (IOException e) {
			// bad luck
		} finally {
			super.finalize();
		}
	}

	/**
	 * Returns the http header field value for a given index
	 * 
	 * @param index
	 *            The index
	 * @return The header field value
	 */
	public synchronized String getHeaderFieldValue(int index) {

		if (index < 0) throw new IllegalArgumentException();
		if (index == 0) return this.iResponse.toString();

		Iterator<Entry<HeaderEntry, String>> iterator = this.iResponseHeaders.iterator();
		while (iterator.hasNext() && --index >= 0) {
			Entry<HeaderEntry, String> entry = iterator.next();
			if (index == 0) return entry.getValue().toString();
		}
		return null;
	}

	/**
	 * Returns the http header field for a given name
	 * 
	 * @param name
	 *            The name
	 * @return The header field
	 */
	public synchronized String getHeaderField(String name) {
		return this.iResponseHeaders.getField(name);
	}

	/**
	 * Return the http header field name for a given index
	 * 
	 * @param index
	 *            The index
	 * @return The name
	 */
	public synchronized String getHeaderFieldName(int index) {
		if (index < 0) throw new IllegalArgumentException();
		if (index == 0) return null;

		Iterator<Entry<HeaderEntry, String>> iterator = this.iResponseHeaders.iterator();
		while (iterator.hasNext() && --index >= 0) {
			Entry<HeaderEntry, String> entry = iterator.next();
			if (index == 0) return entry.getKey().toString();
		}
		return null;
	}

	/**
	 * Returns the input stream of this http connection
	 * 
	 * @return The input stream
	 * @throws IOException
	 */
	public synchronized InputStream getInputStream() throws IOException {
		if (getResponseCode() < 500 && this.iResponse != null && this.iServerInput != null) return this.iServerInput;

		throw new IOException("Failed to open an input stream from server: HTTPResponse "
				+ getResponseCode());
	}

	/**
	 * Returns the output stream of this http connection
	 * 
	 * @return The output stream
	 */
	public synchronized OutputStream getOutputStream() {
		if (this.iServerOutput == null) {
			this.iServerOutput = new ByteArrayOutputStream();
		}
		return this.iServerOutput;
	}

	/**
	 * Returns the request method
	 * 
	 * @return The request method
	 */
	public String getRequestMethod() {
		return this.iRequestMethod;
	}

	/**
	 * Returns the request property for a given key
	 * 
	 * @param key
	 *            The key
	 * @return The property
	 */
	public String getRequestProperty(String key) {
		return this.iRequestHeaders.getField(key);
	}

	/**
	 * Returns the response code
	 * 
	 * @return The response code
	 * @throws IOException
	 */
	public synchronized int getResponseCode() throws IOException {
		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		try {
			long ResponseTime = -1;
			Exception delayedException = null;
			if (this.iReset && this.iResponse == null) {
				boolean authFailed = false;
				int IoRetry = 1;
				int AuthentificationRetry = 1;
				do {
					logger.trace(Level.FINER, "Attempting http request (retry counters:" + IoRetry
							+ "/" + AuthentificationRetry + ")");
					long RequestTime = System.currentTimeMillis();

					if (this.iPreviousResponseTime != -1) {
						long time = RequestTime - this.iPreviousResponseTime;
						long maxTime = this.iHttpClientPool.getConfigurationContext()
								.getSocketIdleTimeout();
						if (maxTime > 0 && time > maxTime) {
							logger.trace(Level.FINER, "Closing socket after " + time
									+ "ms of idle time");

							if (this.iSocket != null && !this.iSocket.isClosed()) {
								try {
									this.iSocket.close();
								} catch (IOException e) {
									logger.trace(Level.FINER,
											"Exception caught while closing socket", e);
								}
							}
							this.iSocket = null;
							this.iReset = true;
							this.iResponse = null;
						}
					}

					ResponseTime = -1;
					resetSocket();
					this.iReset = false;
					try {
						ASCIIPrintStream out = (ASCIIPrintStream) this.iOStream;
						if (out == null) throw new IOException("could not open output stream");

						String file = this.iUrl.getPath();
						if (file == null || file.length() == 0) file = "/";
						String query = this.iUrl.getQuery();
						if (query != null) file = file + '?' + query;

						this.iMethod = new HttpClientMethod(this.iRequestMethod, this.iUrl
								.getPath(), 1, this.iUseHttp11 ? 1 : 0);
						logger.trace(Level.FINER, "HTTP Operation= " + this.iMethod);

						this.iMethod.write(out);

						StringBuilder hostField = new StringBuilder(this.iUrl.getHost());
						if (this.iUrl.getPort() > 0
								&& ((WBEMConstants.HTTPS.equalsIgnoreCase(this.iUrl.getScheme()) && this.iUrl
										.getPort() != WBEMConstants.DEFAULT_WBEM_SECURE_PORT) || (WBEMConstants.HTTP
										.equalsIgnoreCase(this.iUrl.getScheme()) && this.iUrl
										.getPort() != WBEMConstants.DEFAULT_WBEM_PORT))) {
							hostField.append(':');
							hostField.append(this.iUrl.getPort());
						}
						this.iRequestHeaders.addField("Host", hostField.toString());

						if (this.iServerOutput != null) this.iRequestHeaders.addField(
								"Content-length", "" + this.iServerOutput.size());
						else this.iRequestHeaders.addField("Content-length", "0");
						if (this.iHttpClientPool.getConfigurationContext().isHttpChunked()) {
							this.iRequestHeaders.addField("TE", "trailers");
						}
						this.iRequestHeaders.addField("Connection", "Keep-alive");

						if (this.iPrevAuthInfo == null) {
							AuthorizationInfo authInfo = this.iAuth_handler.getAuthorizationInfo(0);
							String authenticate = this.iHttpClientPool.getConfigurationContext()
									.getHttpWwwAuthenticateInfo();

							if (authInfo.isSentOnFirstRequest()) {
								this.iRequestHeaders.addField(authInfo.getHeaderFieldName(),
										authInfo.toString());
							} else if (authenticate != null) {
								try {
									this.iPrevAuthInfo = getAuthentication(false,
											this.iPrevAuthInfo, authenticate);
									if (this.iPrevAuthInfo != null) {
										this.iRequestHeaders.addField(this.iPrevAuthInfo
												.getHeaderFieldName(), this.iPrevAuthInfo
												.toString());
									}
								} catch (NoSuchAlgorithmException e) {
									logger.trace(Level.FINER, "Unable to find digest algorithm", e);
								} catch (IllegalArgumentException e) {
									logger
											.trace(
													Level.FINER,
													WBEMConfigurationProperties.HTTP_WWW_AUTHENTICATE_INFO
															+ " did not contain WWW-Authenticate information",
													e);
								} catch (HttpParseException e) {
									logger
											.trace(
													Level.FINER,
													WBEMConfigurationProperties.HTTP_WWW_AUTHENTICATE_INFO
															+ " did not contain valid WWW-Authenticate information",
													e);
								}
							}
						} else {
							this.iRequestHeaders.addField(this.iPrevAuthInfo.getHeaderFieldName(),
									this.iPrevAuthInfo.toString());
						}

						if (this.iPrevProxy != null) this.iRequestHeaders.addField(
								"Proxy-authorization", this.iPrevProxy.toString());

						boolean isGzipped = false;
						if (this.iHttpClientPool.getConfigurationContext().isGzipEncodingEnabled()) {
							isGzipped = true;
							this.iRequestHeaders.addField("Accept-Encoding", "gzip,identity;q=0.5");
						}

						this.iRequestHeaders.write(out);

						logger.trace(Level.FINER, "Request HTTP Headers= " + this.iRequestHeaders);

						if (out.checkError() != null) {
							delayedException = out.checkError();
							logger.trace(Level.FINER,
									"Exception caught while writing to the http output stream.",
									delayedException);
							if (this.iSocket != null && !this.iSocket.isClosed()) {
								try {
									this.iSocket.close();
								} catch (IOException e) {
									logger.trace(Level.FINER,
											"Exception caught while closing socket", e);
								}
							}
							this.iSocket = null;
							this.iReset = true;
							this.iResponse = null;
							--IoRetry;
							continue;
						}
						if (this.iServerOutput != null) {
							this.iServerOutput.writeTo(out);
						}
						out.flush();

						// byte[] header = new byte[8];
						// istream.mark(8);
						// int totalRead;
						// int k;
						// for (totalRead = 0; totalRead < 8; totalRead += k) {
						// k = istream.read(header, totalRead, 8 - totalRead);
						// if (k < 0)
						// break;
						// }
						//				
						// if (header[0] != 72 // HTTP1.
						// || header[1] != 84
						// || header[2] != 84
						// || header[3] != 80
						// || header[4] != 47
						// || header[5] != 49
						// || header[6] != 46
						// || totalRead != 8) {
						// retry = 0;
						// throw new IOException("Unexpected end of file from
						// server. Header does not match HTTP header: "+new
						// String(header));
						// }
						//						
						// istream.reset();

						this.iResponse = new HttpClientMethod(this.iIStream);
						logger.trace(Level.FINER, "HTTP Response= " + this.iResponse);
						ResponseTime = System.currentTimeMillis();

						this.iResponseHeaders = new HttpHeader(this.iIStream);
						logger.trace(Level.FINER, "Response HTTP Headers= "
								+ this.iResponseHeaders.toString());
						this.iKeepAlive = false;
						if ("Keep-alive".equalsIgnoreCase(this.iResponseHeaders
								.getField("Connection"))
								|| (this.iResponse.getMajorVersion() == 1 && this.iResponse
										.getMinorVersion() == 1)
								|| this.iResponseHeaders.getField("Keep-alive") != null) {
							this.iKeepAlive = true;
						}

						this.iServerInput = new PersistentInputStream(this.iIStream);
						// String keepAliveHdr =
						// iResponseHeaders.getField("Keep-Alive");

						String contentLength = this.iResponseHeaders.getField("Content-length");
						long length = -1;
						try {
							if (contentLength != null && contentLength.length() > 0) length = Long
									.parseLong(contentLength);
						} catch (Exception e) {
							logger.trace(Level.FINER,
									"Exception while parsing the content length of http response",
									e);
						}
						this.iKeepAlive = (length >= 0 || this.iResponse.getStatus() == 304 || this.iResponse
								.getStatus() == 204);

						if (isGzipped) {
							String contentEncoding = this.iResponseHeaders
									.getField("Content-Encoding");
							if (contentEncoding != null
									&& contentEncoding.toLowerCase().contains("gzip")) {
								length = -1; // ignore Content-length
								this.iServerInput = new GZIPInputStream(this.iServerInput);
							}
						}

						String transferEncoding = this.iResponseHeaders
								.getField("Transfer-encoding");
						if (transferEncoding != null) {
							length = -1; // ignore Content-length
							if (transferEncoding.toLowerCase().endsWith("chunked")) {
								this.iServerInput = new ChunkedInputStream(this.iServerInput,
										this.iResponseHeaders.getField("Trailer"), "Response");
								this.iKeepAlive = true;
							}
						}

						if (length >= 0) this.iServerInput = new BoundedInputStream(
								this.iServerInput, length);

						logger.trace(Level.FINER, "KeepAlive="
								+ (this.iKeepAlive ? "true" : "false"));

						if (this.iKeepAlive) {
							this.iServerInput = new KeepAliveInputStream(this.iServerInput, this);
						}

						switch (this.iResponse.getStatus()) {
							case 100: {
								continue;
							}
							case HttpURLConnection.HTTP_OK:
								String authInfo = this.iResponseHeaders
										.getField("Authentication-Info");
								handleRsp(authInfo, this.iPrevAuthInfo);

								authInfo = this.iResponseHeaders.getField("Authentication-Proxy");
								handleRsp(authInfo, this.iPrevProxy);

								if (this.iServerOutput != null) this.iServerOutput = null;

								this.iPreviousResponseTime = ResponseTime;
								return HttpURLConnection.HTTP_OK;

							case HttpURLConnection.HTTP_UNAUTHORIZED:
								--AuthentificationRetry;
								String authenticate = this.iResponseHeaders
										.getField("WWW-Authenticate");
								try {
									this.iPrevAuthInfo = getAuthentication(false,
											this.iPrevAuthInfo, authenticate);
									if (this.iPrevAuthInfo != null) {
										this.iRequestHeaders.addField(this.iPrevAuthInfo
												.getHeaderFieldName(), this.iPrevAuthInfo
												.toString());
									}
								} catch (NoSuchAlgorithmException e) {
									logger.trace(Level.FINER, "Unable to find digest algorithm", e);
								} catch (IllegalArgumentException e) {
									logger
											.trace(
													Level.FINER,
													"HTTP 401 response did not contain WWW-Authenticate information",
													e);
								} catch (HttpParseException e) {
									logger
											.trace(
													Level.FINER,
													"HTTP 401 response did not contain valid WWW-Authenticate information",
													e);
								}

								if (!authFailed) {
									authFailed = true;
									logger
											.trace(Level.FINER,
													"Authorization failed, retrying with authorization info.");
								}
								if (this.iPrevAuthInfo != null && this.iPrevAuthInfo.isKeptAlive()) {
									this.iKeepAlive = true;
								}
								break;
							case HttpURLConnection.HTTP_PROXY_AUTH:
								--AuthentificationRetry;
								logger.message(Messages.HTTP_PROXY_AUTH_UNSUPPORTED, this.iUrl);

								// TODO implement http proxy authentication
								/*
								 * authenticate =
								 * responseHeaders.getField("Proxy-Authenticate"
								 * ); prevProxy = getAuthentication(true,
								 * prevProxy, authenticate, this); if
								 * (prevAuthInfo != null)
								 * requestHeaders.addField
								 * ("Proxy-Authorization",
								 * prevProxy.toString());
								 * 
								 * while ((total = serverInput.available()) > 0)
								 * { serverInput.skip(total); } break;
								 */
								break;
							default:
								int status = this.iResponse.getStatus();
								if (!this.iKeepAlive) closeConnection();
								else this.iServerInput.close();
								this.iPreviousResponseTime = ResponseTime;
								return status;
						}
					} catch (SocketTimeoutException e) {
						throw e;
					} catch (IOException e) {
						logger.message(Messages.HTTP_CONNECTION_FAILED, new Object[] { this.iUrl,
								e.getMessage() });
						StringBuilder msg = new StringBuilder("Http connection failed ");
						if (ResponseTime != -1) {
							msg.append("after");
							msg.append(ResponseTime - RequestTime);
							msg.append(" milliseconds");
						} else {
							msg.append("before response received");
						}
						if (this.iPreviousResponseTime != -1) {
							msg.append(", ");
							msg.append(System.currentTimeMillis() - this.iPreviousResponseTime);
							msg.append(" milliseconds after previous response on same socket");
						}
						logger.trace(Level.FINE, msg.toString(), e);
						delayedException = e;
						if (this.iSocket != null && !this.iSocket.isClosed()) {
							try {
								this.iSocket.close();
							} catch (IOException e2) {
								logger.trace(Level.FINER, "Exception caught while closing socket",
										e2);
							}
						}
						this.iSocket = null;
						this.iReset = true;
						this.iResponse = null;
						--IoRetry;
					}

				} while (AuthentificationRetry >= 0 && IoRetry >= 0);
			}

			if (this.iResponse != null) {
				logger.trace(Level.FINER, "http response code=" + this.iResponse.getStatus());
				if (ResponseTime != -1) this.iPreviousResponseTime = ResponseTime;
				return this.iResponse.getStatus();
			}
			throw (IOException) (delayedException != null ? delayedException : new Exception(
					"Unable to get response after maximum retries"));
		} finally {
			logger.exit();
		}
	}

	/**
	 * Returns the response message
	 * 
	 * @return The response message
	 */
	public String getResponseMessage() {
		if (this.iResponse != null) return this.iResponse.getResponseMessage();
		return null;
	}

	public void handshakeCompleted(HandshakeCompletedEvent event) {
		LogAndTraceBroker.getBroker().trace(Level.FINER, "Http handshake completed.");
		this.iSession = event.getSession();
	}

	/**
	 * Resets state
	 */
	public void reset() {
		this.iRequestHeaders.clear();
		this.iResponseHeaders.clear();
		this.iResponse = null;
		this.iReset = true;
	}

	/**
	 * Sets the request method
	 * 
	 * @param method
	 *            The request method
	 */
	public void setRequestMethod(String method) {
		this.iRequestMethod = method;
	}

	/**
	 * Sets the request property
	 * 
	 * @param key
	 *            The property name
	 * @param value
	 *            The value
	 */
	public void setRequestProperty(String key, String value) {
		this.iRequestHeaders.addField(key, value);
	}

	/**
	 * Releases the client and returns it to the pool
	 */
	public void streamFinished() {
		streamFinished(true);
	}

	/**
	 * Releases the client and returns it to the pool
	 * 
	 * @param keep
	 *            if <code>true</code> return to the pool, if <code>false</code>
	 *            drop.
	 */
	public void streamFinished(boolean keep) {

		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		HostPortPair hpp = new HostPortPair(this.iUrl);
		if (keep) { // TODO configurable from property file
			logger.trace(Level.FINER, "Adding http client to pool (" + hpp + ")");
			this.iHttpClientPool.returnAvailableConnectionToPool(this);
		} else {
			logger.trace(Level.FINER, "Disconnectiong http client (" + hpp + ")");
			this.iHttpClientPool.removeConnectionFromPool(this);
			disconnect();
		}
		logger.exit();
	}

	/**
	 * Enables/Disables use of http 1.1
	 * 
	 * @param bool
	 *            if <code>true</code> http 1.1 is enabled.
	 */
	public void useHttp11(boolean bool) {
		this.iUseHttp11 = bool;
	}

	/**
	 * Returns if a proxy is used
	 * 
	 * @return <code>true</code> if a proxy is used
	 */
	public boolean usingProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	protected AuthorizationInfo getAuthentication(boolean proxy, AuthorizationInfo prevAuthInfo,
			String authenticate) throws HttpParseException, NoSuchAlgorithmException {
		Challenge[] challenges = Challenge.parseChallenge(authenticate);

		// AuthorizationHandler auth_handler =
		// AuthorizationHandler.getInstance();
		int cntr = 0;
		prevAuthInfo = null;
		while (cntr < challenges.length) {
			Challenge challenge = challenges[cntr];
			cntr++;
			// if (challenge.getScheme().equalsIgnoreCase("Digest")) {
			// HttpHeader headers = challenge.getParams();
			// String stale = headers.getField("stale");
			// }
			prevAuthInfo = this.iAuth_handler.getAuthorizationInfo(this.iHttpClientPool
					.getConfigurationContext().getHttpAuthenticationModule(), proxy ? Boolean.TRUE
					: Boolean.FALSE, this.iUrl.getHost(), this.iUrl.getPort(), this.iUrl
					.getScheme(), challenge.getRealm(), challenge.getScheme());

			if (prevAuthInfo != null) {
				prevAuthInfo.updateAuthenticationInfo(challenge, authenticate, this.iUrl,
						this.iRequestMethod);
				return prevAuthInfo;
			}
		}
		return null;
	}

	private void closeConnection() {
		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		if (this.iSocket != null) {
			try {
				this.iSocket.close();
			} catch (IOException e) {
				logger.trace(Level.FINER, "Exception while closing the socket", e);
			}
			this.iSocket = null;
			this.iServerInput = null;
			// response = null;
		}
		logger.exit();
	}

	private String[] parseProperty(String propertyName) {
		String s = (String) AccessController.doPrivileged(new GetProperty(propertyName));
		String as[];
		if (s == null || s.length() == 0) {
			as = null;
		} else {
			Vector<Object> vector = new Vector<Object>();
			for (StringTokenizer stringtokenizer = new StringTokenizer(s, ","); stringtokenizer
					.hasMoreElements(); vector.addElement(stringtokenizer.nextElement())) {
				// add each token to vector
			}
			as = new String[vector.size()];
			for (int i1 = 0; i1 < as.length; i1++)
				as[i1] = (String) vector.elementAt(i1);
		}
		return as;
	}

	private void resetSocket() throws IOException {
		LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		if (!this.iKeepAlive) {
			logger.trace(Level.FINER, "KeepAlive=false, closing http connection...");
			closeConnection();
		}

		int httpTimeout = this.iHttpClientPool.getConfigurationContext().getHttpTimeout();
		logger.trace(Level.FINER, "Setting http timeout=" + httpTimeout);

		if (this.iSocket == null) {
			// Determine whether we need to connect with a timeout or not
			boolean socketConnectWithTimeout = this.iHttpClientPool.getConfigurationContext()
					.socketConnectWithTimeout();
			logger.trace(Level.FINER, "Socket=null, creating http socket "
					+ (socketConnectWithTimeout ? "with" : "without") + " timeout.");

			// On Java 5+ InetSocketAddress(String,int) constructor will call
			// any security manager's checkConnect method
			if (!socketConnectWithTimeout) {
				SecurityManager sm = System.getSecurityManager();
				if (sm != null) {
					sm.checkConnect(this.iUrl.getHost(), this.iUrl.getPort());
				}
			}

			SocketFactory factory = this.iHttpClientPool.getConfigurationContext()
					.getCustomSocketFactory();
			if (factory == null) {
				factory = HttpSocketFactory
						.getInstance()
						.getClientSocketFactory(
								WBEMConstants.HTTPS.equalsIgnoreCase(this.iUrl.getScheme()) ? this.iHttpClientPool
										.getSslContext()
										: null);
				if (factory == null) {
					logger.message(Messages.HTTP_NO_SOCKET_FACTORY, this.iUrl.getScheme());
					throw new IllegalStateException("Unable to load socket factory:"
							+ this.iUrl.getScheme());
				}
			}
			logger.trace(Level.FINER, "Creating new http for url " + this.iUrl.toString());
			if (socketConnectWithTimeout) {
				int connectTimeout = this.iHttpClientPool.getConfigurationContext()
						.getSocketConnectTimeout();
				logger.trace(Level.FINER, "Setting socket connect timeout=" + connectTimeout);

				if (factory instanceof SSLSocketFactory) {
					Socket sock = new Socket();
					sock.connect(new InetSocketAddress(this.iUrl.getHost(), this.iUrl.getPort()),
							connectTimeout);
					this.iSocket = ((SSLSocketFactory) factory).createSocket(sock, this.iUrl
							.getHost(), this.iUrl.getPort(), true);

				} else {
					this.iSocket = factory.createSocket();
					if (this.iSocket != null) this.iSocket.connect(new InetSocketAddress(this.iUrl
							.getHost(), this.iUrl.getPort()), connectTimeout);
				}
			} else {
				this.iSocket = factory.createSocket(this.iUrl.getHost(), this.iUrl.getPort());
			}
			if (this.iSocket == null) {
				logger.trace(Level.WARNING, "Socket factory " + factory.getClass().getName()
						+ " returned null socket");
				throw new IOException("Socket factory did not create socket");
			}

			this.iPreviousResponseTime = -1;
			this.iSocket.setTcpNoDelay(true);
			this.iSocket.setKeepAlive(true);
			this.iSocket.setSoTimeout(httpTimeout);

			if (this.iSocket instanceof SSLSocket) {
				// Determine whether we need to perform SSL handshake or not
				boolean performHandshake = this.iHttpClientPool.getConfigurationContext()
						.performSslHandshake();
				logger.trace(Level.FINER, "SSL socket created, handshake "
						+ (performHandshake ? "will" : "will not") + " be performed.");

				if (performHandshake) {
					SSLSocket sk = (SSLSocket) this.iSocket;

					String protocols[] = parseProperty("https.protocols");
					if (protocols != null) {
						logger.trace(Level.FINER,
								"Setting SSLSocket.setEnabledProtocols() from \"https.protocols\"="
										+ String.valueOf(Arrays.asList(protocols)));
						sk.setEnabledProtocols(protocols);
					}

					String ciphersuites[] = parseProperty("https.cipherSuites");
					if (ciphersuites != null) {
						logger.trace(Level.FINER,
								"Setting SSLSocket.setEnableCipheSuites() from \"https.cipherSuites\"="
										+ String.valueOf(Arrays.asList(ciphersuites)));
						sk.setEnabledCipherSuites(ciphersuites);
					}

					String disableCipherSuites = this.iHttpClientPool.getConfigurationContext()
							.getSslClientCipherSuitesToDisable();
					if (disableCipherSuites != null) {
						sk.setEnabledCipherSuites(this.iHttpClientPool.getUpdatedCipherSuites(sk
								.getEnabledCipherSuites(), disableCipherSuites));
					}

					// Determine whether we need to perform synchronized SSL
					// handshake or not
					boolean synchronizedHandshake = this.iHttpClientPool.getConfigurationContext()
							.synchronizedSslHandshake();
					logger.trace(Level.FINER, "Starting "
							+ (synchronizedHandshake ? "synchronized" : "unsynchronized")
							+ " http handshake.");

					sk.addHandshakeCompletedListener(this);
					if (synchronizedHandshake) {
						synchronized (SSLSocket.class) {
							sk.startHandshake();
						}
					} else {
						sk.startHandshake();
					}
				}
			}

			this.iIStream = new BufferedInputStream(this.iSocket.getInputStream());
			this.iOStream = new ASCIIPrintStream(new BufferedOutputStream(this.iSocket
					.getOutputStream(), 1024), false, iEncoding);
			this.iServerInput = null;
		} else {
			if (this.iServerInput != null && !(this.iServerInput instanceof KeepAliveInputStream)) {
				logger.trace(Level.FINER, "Socket!=null, flushing the stream...");
				long totalBytes = 0;
				long total;
				while ((total = this.iServerInput.available()) > 0) {
					total = this.iServerInput.skip(total);
					if (total >= 0) totalBytes += total;
				}
				logger.trace(Level.FINER, "total bytes on the stream=" + totalBytes);
			}
			this.iSocket.setSoTimeout(httpTimeout);
		}
	}

	/**
	 * Returns connected
	 * 
	 * @return The value of connected.
	 */
	public boolean isConnected() {
		return this.iConnected;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append('[');
		result.append("URI=");
		result.append(this.iUrl);
		result.append(", ");
		result.append(this.iConnected ? "connected" : "not connected");
		result.append(", ");
		result.append(this.iKeepAlive ? "kept alive" : "not kept alive");
		result.append(", ");
		result.append(this.iUseHttp11 ? "HTTP 1.1" : "HTTP 1.0");
		result.append(", ");
		result.append("Method=");
		result.append(this.iMethod);
		result.append(", ");
		result.append("Request Method=");
		result.append(this.iRequestMethod);
		result.append(", ");
		result.append("Protocol=");
		result.append(this.iSession != null ? this.iSession.getProtocol() : "http");
		result.append(", ");
		result.append("CipherSuite=");
		result.append(this.iSession != null ? this.iSession.getCipherSuite() : "n/a");
		result.append(']');
		return result.toString();
	}
}
