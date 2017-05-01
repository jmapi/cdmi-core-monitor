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
 * 1498130    2006-05-31  lupusalex    Selection of xml parser on a per connection basis
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1558663    2006-09-14  lupusalex    Support custom socket factories in client connections
 * 1573723    2006-10-09  lupusalex    Selection of JSSE provider via properties file not feasible
 * 1573723    2006-10-30  lupusalex    rework: Selection of JSSE provider via properties file not feasible
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1660743    2007-02-15  lupusalex    SSLContext is static
 * 1711092    2006-05-02  lupusalex    Some fixes/additions of log&trace messages
 * 1815707    2007-10-18  ebak         TLS support
 * 1815707    2007-10-30  ebak         rework: TLS support
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3001345    2010-05-18  blaschke-oss File handle leaks in HttpSocketFactory and LogAndTraceBroker
 * 3027618    2010-07-14  blaschke-oss Close files/readers in finally blocks
 * 3111718    2010-11-18  blaschke-oss org.sblim.cimclient SSL Code is using the wrong SSL Property
 * 3536399    2012-08-25  hellerda     Add client/listener peer authentication properties
 *    2647    2013-07-01  blaschke-oss Add two ssl protocol properties for http server and client
 */

package org.sblim.cimclient.internal.http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.logging.Messages;
import org.sblim.cimclient.internal.util.WBEMConfiguration;

/**
 * Class HttpSocketFactory manages socket factories
 * 
 */
public class HttpSocketFactory {

	private static HttpSocketFactory cInstance = new HttpSocketFactory();

	private HttpSocketFactory() {
	// empty
	}

	/**
	 * Returns the singleton instance
	 * 
	 * @return The instance
	 */
	public static HttpSocketFactory getInstance() {
		return cInstance;
	}

	/**
	 * Returns a server socket factory
	 * 
	 * @param pContext
	 *            The corresponding SSL context or <code>null</code> for
	 *            insecure connections
	 * 
	 * @return The factory
	 */
	public ServerSocketFactory getServerSocketFactory(SSLContext pContext) {
		return pContext != null ? pContext.getServerSocketFactory() : ServerSocketFactory
				.getDefault();
	}

	/**
	 * Returns a client socket factory
	 * 
	 * @param pContext
	 *            The SSL context or <code>null</code> for insecure connections
	 * @return The factory
	 */
	public SocketFactory getClientSocketFactory(SSLContext pContext) {
		return pContext != null ? pContext.getSocketFactory() : SocketFactory.getDefault();
	}

	/**
	 * Returns a SSLContext for client sockets corresponding to a given set of
	 * configuration properties
	 * 
	 * @param pProperties
	 *            The configuration to apply
	 * @return The SSL context
	 */
	public SSLContext getClientSSLContext(final WBEMConfiguration pProperties) {
		return getSSLContext(pProperties, false);
	}

	/**
	 * Returns a SSLContext for server sockets corresponding to a given set of
	 * configuration properties
	 * 
	 * @param pProperties
	 *            The configuration to apply
	 * @return The SSL context
	 */
	public SSLContext getServerSSLContext(final WBEMConfiguration pProperties) {
		return getSSLContext(pProperties, true);
	}

	/**
	 * Returns a SSLContext corresponding to a given set of configuration
	 * properties
	 * 
	 * @param pProperties
	 *            The configuration to apply
	 * @param pIsServer
	 *            <code>true</code> if a server socket context is requested,
	 *            <code>false</code> otherwise
	 * @return The SSL context
	 */
	private SSLContext getSSLContext(final WBEMConfiguration pProperties, boolean pIsServer) {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		final String provider = pIsServer ? pProperties.getSslServerSocketProvider() : pProperties
				.getSslSocketProvider();
		logger.trace(Level.FINER, "Loading JSSE provider:" + provider);

		final Provider securityProvider;

		try {
			Class<?> providerClass = Class.forName(provider);
			securityProvider = (java.security.Provider) providerClass.newInstance();
			if (Security.getProvider(securityProvider.getName()) == null) {
				Security.addProvider(securityProvider);
			}
		} catch (Exception e) {
			logger.trace(Level.FINER, "Exception while loading JSSE provider", e);
			logger.message(Messages.SSL_JSSE_PROVIDER_LOAD_FAILED, provider);
			logger.exit();
			throw new RuntimeException(e);
		}

		try {
			KeyManager[] keyManager = loadKeystore(pProperties, securityProvider, pIsServer);

			TrustManager[] trustManager = loadTruststore(pProperties, securityProvider, pIsServer);

			String sslProtocol = pIsServer ? pProperties.getSslListenerProtocol() : pProperties
					.getSslClientProtocol();

			SSLContext sslContext = SSLContext.getInstance(sslProtocol != null ? sslProtocol
					: pProperties.getSslProtocol(), securityProvider);

			sslContext.init(keyManager, trustManager, null);

			logger.exit();

			return sslContext;

		} catch (Exception e) {
			logger.trace(Level.FINER, "Exception while initializing SSL context (provider:"
					+ provider + ")", e);
			logger.message(Messages.SSL_CONTEXT_INIT_FAILED);
			logger.exit();
			return null;
		}
	}

	private TrustManager[] loadTruststore(final WBEMConfiguration pProperties,
			final Provider pSecurityProvider, boolean pIsServer) {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		final TrustManager[] trustAll = new X509TrustManager[] { new AllTrustManager() };
		final TrustManager[] trustNone = new X509TrustManager[] { new NoTrustManager() };
		TrustManager[] trustManager = trustNone;

		final String truststorePath = pProperties.getSslTrustStorePath();
		final char[] truststorePassword = pProperties.getSslTrustStorePassword().toCharArray();
		final String truststoreType = pProperties.getSslTrustStoreType();
		final String trustManagerAlgorithm = pProperties.getSslTrustManagerAlgorithm();
		final boolean clientPeerVerification = pProperties.getSslClientPeerVerification();
		final String listenerPeerVerification = pProperties.getSslListenerPeerVerification();

		logger.trace(Level.FINER, "Using SSL truststore \"" + truststorePath + "\" ("
				+ truststoreType + "/" + trustManagerAlgorithm + ")");

		if (pIsServer && listenerPeerVerification.equalsIgnoreCase("ignore")
				|| (!pIsServer && !clientPeerVerification)) {
			trustManager = trustAll;
			if (truststorePath == null || truststorePath.trim().length() == 0) {
				logger.trace(Level.FINER, "Peer verification disabled for "
						+ (pIsServer ? "Listener" : "Client"));
			} else {
				logger.message(Messages.SSL_TRUSTSTORE_INACTIVE);
			}
		} else {
			if (truststorePath == null || truststorePath.trim().length() == 0) {
				logger.trace(Level.FINER, "Peer verification enabled for "
						+ (pIsServer ? "Listener" : "Client") + " but no truststore specified!");
				logger.message(Messages.SSL_TRUSTSTORE_NULL);
			} else {
				logger.trace(Level.FINER, "Peer verification enabled for "
						+ (pIsServer ? "Listener" : "Client"));
				FileInputStream fis = null;
				try {
					final KeyStore trustStore = KeyStore.getInstance(truststoreType);
					fis = new FileInputStream(truststorePath);
					trustStore.load(fis, truststorePassword);
					final TrustManagerFactory trustManagerFactory = TrustManagerFactory
							.getInstance(trustManagerAlgorithm, pSecurityProvider);
					trustManagerFactory.init(trustStore);
					trustManager = trustManagerFactory.getTrustManagers();
					logger.trace(Level.FINER, "Truststore successfully loaded for "
							+ (pIsServer ? "Listener" : "Client"));
				} catch (FileNotFoundException e) {
					logger.trace(Level.FINER, "Exception while loading truststore", e);
					logger.message(Messages.SSL_TRUSTSTORE_NOT_FOUND, truststorePath);
				} catch (IOException e) {
					logger.trace(Level.FINER, "Exception while loading truststore", e);
					logger.message(Messages.SSL_TRUSTSTORE_NOT_READABLE, truststorePath);
				} catch (NoSuchAlgorithmException e) {
					logger.trace(Level.FINER, "Exception while loading truststore", e);
					logger
							.message(Messages.SSL_TRUSTSTORE_INVALID_ALGORITHM,
									trustManagerAlgorithm);
				} catch (CertificateException e) {
					logger.trace(Level.FINER, "Exception while loading truststore", e);
					logger.message(Messages.SSL_TRUSTSTORE_INVALID_CERT, truststorePath);
				} catch (KeyStoreException e) {
					logger.trace(Level.FINER, "Exception while loading truststore", e);
					logger.message(Messages.SSL_TRUSTSTORE_INVALID, truststoreType);
				} catch (Exception e) {
					logger.trace(Level.FINER, "Exception while loading truststore", e);
					logger.message(Messages.SSL_TRUSTSTORE_OTHER, truststorePath);
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							logger.trace(Level.FINER, "Exception while closing truststore", e);
						}
					}
				}
			}
		}

		if (trustManager == trustAll) {
			logger.message(Messages.SSL_TRUSTSTORE_FALLBACK);
		} else if (trustManager == trustNone) {
			logger.message(Messages.SSL_TRUSTSTORE_FALLBACK_NOTRUST);
		} else {
			logger.message(Messages.SSL_TRUSTSTORE_ACTIVE);
		}

		logger.exit();
		return trustManager;
	}

	private KeyManager[] loadKeystore(final WBEMConfiguration pProperties,
			final Provider pSecurityProvider, boolean pIsServer) {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		final KeyManager[] noKeys = new KeyManager[] { new EmptyKeyManager() };
		KeyManager[] keyManager = noKeys;

		final String keystorePath = pProperties.getSslKeyStorePath();
		final char[] keystorePassword = pProperties.getSslKeyStorePassword().toCharArray();
		final String keystoreType = pProperties.getSslKeyStoreType();
		final String keyManagerAlgorithm = pProperties.getSslKeyManagerAlgorithm();

		logger.trace(Level.FINER, "Using SSL keystore \"" + keystorePath + "\" (" + keystoreType
				+ "/" + keyManagerAlgorithm + ")");

		if (keystorePath == null || keystorePath.trim().length() == 0) {
			logger.trace(Level.FINER, "Keystore not specified for "
					+ (pIsServer ? "Listener" : "Client"));
			logger.message(Messages.SSL_KEYSTORE_NULL);
		} else {
			logger.trace(Level.FINER, "Keystore specified and activated for "
					+ (pIsServer ? "Listener" : "Client"));
			FileInputStream fis = null;
			try {
				final KeyStore keystore = KeyStore.getInstance(keystoreType);
				fis = new FileInputStream(keystorePath);
				keystore.load(fis, keystorePassword);

				final KeyManagerFactory keymanagerfactory = KeyManagerFactory.getInstance(
						keyManagerAlgorithm, pSecurityProvider);
				keymanagerfactory.init(keystore, keystorePassword);
				keyManager = keymanagerfactory.getKeyManagers();
				logger.trace(Level.FINER, "Keystore successfully loaded for "
						+ (pIsServer ? "Listener" : "Client"));
			} catch (FileNotFoundException e) {
				logger.trace(Level.FINER, "Exception while loading keystore", e);
				logger.message(Messages.SSL_KEYSTORE_NOT_FOUND, keystorePath);
			} catch (IOException e) {
				logger.trace(Level.FINER, "Exception while loading keystore", e);
				logger.message(Messages.SSL_KEYSTORE_NOT_READABLE, keystorePath);
			} catch (NoSuchAlgorithmException e) {
				logger.trace(Level.FINER, "Exception while loading keystore", e);
				logger.message(Messages.SSL_KEYSTORE_INVALID_ALGORITHM, keyManagerAlgorithm);
			} catch (CertificateException e) {
				logger.trace(Level.FINER, "Exception while loading keystore", e);
				logger.message(Messages.SSL_KEYSTORE_INVALID_CERT, keystorePath);
			} catch (UnrecoverableKeyException e) {
				logger.trace(Level.FINER, "Exception while loading keystore", e);
				logger.message(Messages.SSL_KEYSTORE_UNRECOVERABLE_KEY, keystorePath);
			} catch (KeyStoreException e) {
				logger.trace(Level.FINER, "Exception while loading keystore", e);
				logger.message(Messages.SSL_KEYSTORE_INVALID, keystoreType);
			} catch (Exception e) {
				logger.trace(Level.FINER, "Exception while loading keystore", e);
				logger.message(Messages.SSL_KEYSTORE_OTHER, keystorePath);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						logger.trace(Level.FINER, "Exception while closing keystore", e);
					}
				}
			}
		}

		if (keyManager == noKeys) {
			logger.message(Messages.SSL_KEYSTORE_FALLBACK);
		}

		logger.exit();
		return keyManager;
	}

}

class AllTrustManager implements X509TrustManager {

	/**
	 * @param arg0
	 * @param arg1
	 */
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
		return;
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
		return;
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}

class NoTrustManager implements X509TrustManager {

	/**
	 * @param arg0
	 * @param arg1
	 * @throws CertificateException
	 */
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		throw new CertificateException();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws CertificateException
	 */
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		throw new CertificateException();
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}

class EmptyKeyManager implements X509KeyManager {

	/**
	 * @param keyType
	 * @param issuers
	 * @param socket
	 * @return String chooseClientAlias
	 */
	public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
		return null;
	}

	/**
	 * @param keyType
	 * @param issuers
	 * @param socket
	 * @return String chooseServerAlias
	 */
	public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
		return null;
	}

	/**
	 * @param alias
	 * @return X509Certificate[]
	 */
	public X509Certificate[] getCertificateChain(String alias) {
		return null;
	}

	/**
	 * @param keyType
	 * @param issuers
	 * @return String[]
	 */
	public String[] getClientAliases(String keyType, Principal[] issuers) {
		return null;
	}

	/**
	 * @param alias
	 * @return PrivateKey
	 */
	public PrivateKey getPrivateKey(String alias) {
		return null;
	}

	/**
	 * @param keyType
	 * @param issuers
	 * @return String[]
	 */
	public String[] getServerAliases(String keyType, Principal[] issuers) {
		return null;
	}
}
