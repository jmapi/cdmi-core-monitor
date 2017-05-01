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
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1565892    2006-11-16  lupusalex    Make SBLIM client JSR48 compliant
 * 1702832    2007-04-18  lupusalex    WBEMClientCIMXL.setCustomSocketFactory() not implemented
 * 1688273    2007-04-19  lupusalex    Full support of HTTP trailers
 * 1714902    2007-05-08  lupusalex    Threading related weak spots
 * 1815707    2007-10-30  ebak         TLS support
 * 1827728    2007-11-12  ebak         embeddedInstances: attribute EmbeddedObject not set
 * 1848607    2007-12-11  ebak         Strict EmbeddedObject types
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2372030    2008-12-01  blaschke-oss Add property to control synchronized SSL handshaking
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2846231    2009-09-23  rgummada     connection failure on CIMOM w/o user/pw
 * 2930341    2010-01-12  blaschke-oss Sync up WBEMClientConstants with JSR48 1.0.0
 * 2970881    2010-03-15  blaschke-oss Add property to control EmbeddedObject case
 * 3046073    2010-09-07  blaschke-oss Performance hit due to socket conn. creation with timeout
 * 3078280    2010-09-29  blaschke-oss Fix for a null pointer exception in 1.3.9.1
 * 3111718    2010-11-18  blaschke-oss org.sblim.cimclient SSL Code is using the wrong SSL Property
 * 3185763    2011-02-25  blaschke-oss Reliable indication support - Phase 1
 * 3195069    2011-02-28  blaschke-oss Need support to disable SSL Handshake
 * 3277928    2011-04-06  blaschke-oss CIM-XML tracing cannot be enabled in the field
 * 3206904    2011-05-03  blaschke-oss Indication listener deadlock causes JVM to run out sockets
 * 3459036    2011-12-13  blaschke-oss Linked list for RI queue not efficient for many LDs
 * 3485074    2012-02-06  blaschke-oss An Indication trace request
 * 3492214    2012-02-23  blaschke-oss Add a SenderIPAddress property indications
 * 3492224    2012-02-23  blaschke-oss Need two different timeouts for Socket connections
 * 3521157    2012-05-10  blaschke-oss JSR48 1.0.0: PROP_ENABLE_*_LOGGING is Level, not 0/1
 * 3524050    2012-06-06  blaschke-oss Improve WWW-Authenticate in HTTPClient.java
 * 3536399    2012-08-25  hellerda     Add client/listener peer authentication properties
 * 3572993    2012-10-01  blaschke-oss parseDouble("2.2250738585072012e-308") DoS vulnerability
 * 3576396    2012-10-11  blaschke-oss Improve logging of config file name
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
 *    2618    2013-02-27  blaschke-oss Need to add property to disable weak cipher suites for the secure indication
 *    2628    2013-03-26  blaschke-oss Limit size of LinkedList of CIMEvents to be dispatched
 *    2635    2013-05-16  blaschke-oss Slowloris DoS attack for CIM indication listener port
 *    2642    2013-05-21  blaschke-oss Seperate properties needed for cim client and listener to filter out ciphers
 *    2647    2013-07-01  blaschke-oss Add two ssl protocol properties for http server and client
 *    2653    2013-07-26  blaschke-oss FVT: java.lang.ExceptionInInitializerError during static init
 *    2654    2013-07-29  blaschke-oss Check jcc idle time with CIMOM keepalive timeout to avoid EOF
 *    2151    2013-08-20  blaschke-oss gzip compression not supported
 *    2711    2013-11-13  blaschke-oss LOCALNAMESPACEPATH allows 0 NAMESPACE children
 */

package org.sblim.cimclient.internal.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

import javax.net.SocketFactory;

import org.sblim.cimclient.WBEMConfigurationProperties;

/**
 * Class WBEMConfigurationFile is responsible for initializing the the
 * configuration properties from the configuration file.
 * 
 * @threading This class was designed as thread-safe
 */
public class WBEMConfiguration {

	private static WBEMConfiguration cConfiguration;

	private static String cConfigURL = null;

	private static String cConfigFullURL = null;

	private static boolean cLoadSuccessful = false;

	private static Exception cLoadException = null;

	static {
		loadGlobalConfiguration();
		cConfiguration = new WBEMConfiguration();
		cConfiguration.iIsGlobal = true;
	}

	/**
	 * Loads the global configuration from the configuration file
	 */
	public static void loadGlobalConfiguration() {

		Properties wbemProperties = new Properties(new Properties());
		try {
			cConfigURL = null;
			cLoadSuccessful = false;
			cLoadException = null;

			InputStream inputstream = getConfigURLStream();
			if (inputstream != null) {
				wbemProperties.load(inputstream);
				cLoadSuccessful = true;
			}
			for (Iterator<Entry<Object, Object>> iterator = wbemProperties.entrySet().iterator(); iterator
					.hasNext();) {
				Entry<Object, Object> entry = iterator.next();
				String key = String.valueOf(entry.getKey());
				if (key.startsWith("sblim.wbem.") || key.startsWith("javax.net.ssl.")
						|| key.startsWith("net.slp.")) {
					String value = String.valueOf(entry.getValue());
					System.setProperty(key, value);
				} else if (key.startsWith("ssl.")) {
					String value = String.valueOf(entry.getValue());
					Security.setProperty(key, value);
				} // else { LogAndTraceBroker is NOT initialized yet
				// if (LogAndTraceBroker.isLoggingStarted()) {
				// LogAndTraceBroker.getBroker().message(Messages.INVALID_KEY_IN_CONFIG_FILE,
				// key);
				// }
				// }
			}
		} catch (IOException e) {
			cLoadException = e;
		}
	}

	/**
	 * Returns the URL of the active configuration file. This is the file used
	 * to initialize the global configuration.
	 * 
	 * @return The URL of the active configuration file.
	 */
	public static String getActiveConfigURL() {
		return cConfigURL;
	}

	/**
	 * Returns the full URL of the active configuration file. This is the file
	 * used to initialize the global configuration.
	 * 
	 * @return The full URL of the active configuration file.
	 */
	public static String getActiveConfigFullURL() {
		return cConfigFullURL;
	}

	/**
	 * Returns if the configuration was successfully loaded from the
	 * configuration file
	 * 
	 * @return <code>true</code> if the configuration was successfully loaded
	 *         from the configuration file, <code>false</code> otherwise
	 */
	public static boolean isConfigurationLoadSuccessful() {
		return cLoadSuccessful;
	}

	/**
	 * Returns an input stream for the configuration file. The method looks if a
	 * configuration file URL is set in the system properties and tries the
	 * default location if not.
	 * 
	 * @return The <code>InputStream</code> of the configuration file or
	 *         <code>null</code> if none could be opened.
	 */
	private static InputStream getConfigURLStream() {

		String configURL = System.getProperty(WBEMConfigurationProperties.CONFIG_URL);

		if (configURL != null) {
			if (configURL.trim().length() > 0) {
				try {
					cConfigURL = configURL;
					URL url = new URL(configURL);
					cConfigFullURL = getFullURL(url);
					InputStream inputstream = url.openStream();
					return inputstream;
				} catch (Exception e) {
					cLoadException = e;
				}
			}
		} else {
			for (int i = 0; i < WBEMConfigurationDefaults.CONFIG_URL.length; ++i) {
				configURL = WBEMConfigurationDefaults.CONFIG_URL[i];
				try {
					cConfigURL = configURL;
					URL url = new URL(configURL);
					cConfigFullURL = getFullURL(url);
					InputStream inputstream = url.openStream();
					return inputstream;
				} catch (FileNotFoundException e) {
					// just proceed
				} catch (Exception e) {
					cLoadException = e;
				}
			}
			cConfigURL = Arrays.asList(WBEMConfigurationDefaults.CONFIG_URL).toString();
		}
		return null;
	}

	/**
	 * Returns the full URL of the parameter. Only "file:" URLs will be
	 * processed, and the full URL will be the FQDN of the file with relative
	 * paths ("." and "..") removed.
	 * 
	 * @param url
	 *            The URL
	 * @return The full URL
	 */
	private static String getFullURL(URL url) {
		if (url.getProtocol().equalsIgnoreCase("file")) {
			try {
				String path = url.getPath();
				int lastSlash = path.lastIndexOf('/');
				int lastBackslash = path.lastIndexOf('\\');
				int lastSep = lastSlash > lastBackslash ? lastSlash : lastBackslash;

				String dir, file;
				if (lastSep == -1) {
					// directory does not exist, use current directory
					dir = ".";
					file = path;
				} else if (lastSep == 0) {
					// directory does exist, is root
					dir = "/";
					file = path.substring(1);
				} else {
					// directory does exist, is not root
					dir = path.substring(0, lastSep);
					file = path.substring(lastSep + 1);
				}

				String cp = new java.io.File(dir).getCanonicalPath();
				if (cp.endsWith(System.getProperty("file.separator"))) return cp + file;
				return cp + System.getProperty("file.separator") + file;
			} catch (IOException e) {
				// ignore
			}
			return url.getPath();
		}
		return url.toString();
	}

	/**
	 * Returns the global configuration
	 * 
	 * @return The global configuration
	 */
	public static WBEMConfiguration getGlobalConfiguration() {
		return cConfiguration;
	}

	/**
	 * Returns the exception caught during configuration load, load failed.
	 * 
	 * @return The exception or <code>null</code> if none was caught.
	 */
	public static Exception getConfigurationLoadException() {
		return cLoadException;
	}

	private final ThreadLocal<Object> iLocalProperties = new InheritableThreadLocal<Object>() {

		@Override
		protected Object childValue(Object parentValue) {
			if (parentValue instanceof Properties) { return ((Properties) parentValue).clone(); }
			return super.childValue(parentValue);
		}

	};

	/**
	 * @threading Guarded by "this"
	 */
	private Properties iDomainProperties = null;

	/**
	 * @threading Guarded by "this"
	 */
	private SocketFactory iCustomSocketFactory = null;

	private boolean iIsGlobal = false; // final

	/**
	 * Ctor.
	 */
	private WBEMConfiguration() {
		super();
	}

	/**
	 * Ctor.
	 * 
	 * @param pDomainProperties
	 *            The domain specific properties of this configuration. Domain
	 *            specific properties are treated as an overlay on the global
	 *            properties in the <code>System</code> class.
	 * @throws NullPointerException
	 *             If pDomainProperties is <code>null</code>
	 */
	public WBEMConfiguration(Properties pDomainProperties) {
		if (pDomainProperties == null) { throw new NullPointerException(
				"Domain properties shall not be null"); }
		this.iDomainProperties = pDomainProperties;
	}

	/**
	 * Returns whether this configuration is the global one
	 * 
	 * @return <code>true</code> if this is the global configuration
	 */
	public boolean isGlobal() {
		return this.iIsGlobal;
	}

	/**
	 * Returns the domain properties
	 * 
	 * @return The domain properties.
	 */
	public synchronized Properties getDomainProperties() {
		return this.iDomainProperties;
	}

	/**
	 * Sets the domain properties
	 * 
	 * @param pDomainProperties
	 *            The new value
	 */
	public synchronized void setDomainProperties(Properties pDomainProperties) {
		if (isGlobal()) throw new IllegalStateException(
				"Global configuration cannot be modified by setDomainProperties()");
		this.iDomainProperties = pDomainProperties;
	}

	/**
	 * Returns a value from the domain properties for a given name
	 * 
	 * @param pKey
	 *            The name of the property
	 * @return The value
	 */
	public synchronized String getDomainProperty(String pKey) {
		return (this.iDomainProperties != null) ? this.iDomainProperties.getProperty(pKey) : null;
	}

	/**
	 * Sets a property value of the domain properties
	 * 
	 * @param pKey
	 *            The name of property
	 * @param pValue
	 *            The new value
	 */
	public synchronized void setDomainProperty(String pKey, String pValue) {
		if (isGlobal()) throw new IllegalStateException(
				"Global configuration cannot be modified by setDomainProperty()");
		this.iDomainProperties.setProperty(pKey, pValue);
	}

	/**
	 * Returns the properties local to the current thread
	 * 
	 * @return The local properties.
	 */
	public Properties getLocalProperties() {
		return (Properties) this.iLocalProperties.get();
	}

	/**
	 * Sets the properties local to the current thread
	 * 
	 * @param pLocalProperties
	 *            The new value
	 */
	public void setLocalProperties(Properties pLocalProperties) {
		if (isGlobal()) throw new IllegalStateException(
				"Global configuration cannot be modified by setLocalProperties()");
		this.iLocalProperties.set(pLocalProperties);
	}

	/**
	 * Returns a value from the local properties for a given name
	 * 
	 * @param pKey
	 *            The name of the property
	 * @return The value
	 */
	public String getLocalProperty(String pKey) {
		Properties localProperties = (Properties) this.iLocalProperties.get();
		return (localProperties != null) ? localProperties.getProperty(pKey) : null;
	}

	/**
	 * Sets a property value of the local properties
	 * 
	 * @param pKey
	 *            The name of property
	 * @param pValue
	 *            The new value
	 */
	public void setLocalProperty(String pKey, String pValue) {
		if (isGlobal()) throw new IllegalStateException(
				"Global configuration cannot be modified by setLocalProperty()");
		Properties localProperties = (Properties) this.iLocalProperties.get();
		if (localProperties == null) {
			localProperties = new Properties();
			this.iLocalProperties.set(localProperties);
		}
		localProperties.setProperty(pKey, pValue);
	}

	private String getProperty(String pProperty, String pDefault) {
		String globalProperty = System.getProperty(pProperty, pDefault);
		String domainProperty = getDomainProperty(pProperty);
		String localProperty = getLocalProperty(pProperty);
		return (localProperty != null) ? localProperty : (domainProperty != null) ? domainProperty
				: (globalProperty != null) ? globalProperty : pDefault;
	}

	private String getSecurityProperty(String pProperty, String pDefault) {
		String globalProperty = Security.getProperty(pProperty);
		String domainProperty = getDomainProperty(pProperty);
		String localProperty = getLocalProperty(pProperty);
		return (localProperty != null) ? localProperty : (domainProperty != null) ? domainProperty
				: (globalProperty != null) ? globalProperty : pDefault;
	}

	/**
	 * Sets a custom socket factory.
	 * 
	 * @param pFactory
	 *            The factory
	 */
	public synchronized void setCustomSocketFactory(SocketFactory pFactory) {
		this.iCustomSocketFactory = pFactory;
	}

	/**
	 * Returns the custom socket factory
	 * 
	 * @return The factory if set, <code>null</code> otherwise
	 */
	public synchronized SocketFactory getCustomSocketFactory() {
		return this.iCustomSocketFactory;
	}

	/**
	 * Returns the console log level
	 * 
	 * @return The console log level
	 */
	public Level getLogConsoleLevel() {
		return Level.parse(getProperty(WBEMConfigurationProperties.LOG_CONSOLE_LEVEL,
				WBEMConfigurationDefaults.LOG_CONSOLE_LEVEL));
	}

	/**
	 * Returns the console log type
	 * 
	 * @return The console log type
	 */
	public String getLogConsoleType() {
		return getProperty(WBEMConfigurationProperties.LOG_CONSOLE_TYPE,
				WBEMConfigurationDefaults.LOG_CONSOLE_TYPE);
	}

	/**
	 * Returns the log file level
	 * 
	 * @return The log file level
	 */
	public Level getLogFileLevel() {
		return Level.parse(getProperty(WBEMConfigurationProperties.LOG_FILE_LEVEL,
				WBEMConfigurationDefaults.LOG_FILE_LEVEL));
	}

	/**
	 * Returns the log file location
	 * 
	 * @return The log file location
	 */
	public String getLogFileLocation() {
		return getProperty(WBEMConfigurationProperties.LOG_FILE_LOCATION,
				WBEMConfigurationDefaults.LOG_FILE_LOCATION);
	}

	/**
	 * Returns the log file count
	 * 
	 * @return The log file count
	 */
	public int getLogFileCount() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LOG_FILE_COUNT,
				WBEMConfigurationDefaults.LOG_FILE_COUNT));
	}

	/**
	 * Returns the log file size limit
	 * 
	 * @return The log file size limit
	 */
	public int getLogFileSizeLimit() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LOG_FILE_SIZE_LIMIT,
				WBEMConfigurationDefaults.LOG_FILE_SIZE_LIMIT));
	}

	/**
	 * Returns the trace file level
	 * 
	 * @return The trace file level
	 */
	public Level getTraceFileLevel() {
		return Level.parse(getProperty(WBEMConfigurationProperties.TRACE_FILE_LEVEL,
				WBEMConfigurationDefaults.TRACE_FILE_LEVEL));

	}

	/**
	 * Returns the trace file location
	 * 
	 * @return The trace file location
	 */
	public String getTraceFileLocation() {
		return getProperty(WBEMConfigurationProperties.TRACE_FILE_LOCATION,
				WBEMConfigurationDefaults.TRACE_FILE_LOCATION);
	}

	/**
	 * Returns the trace file count
	 * 
	 * @return The trace file count
	 */
	public int getTraceFileCount() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.TRACE_FILE_COUNT,
				WBEMConfigurationDefaults.TRACE_FILE_COUNT));
	}

	/**
	 * Returns the trace file count
	 * 
	 * @return The trace file count
	 */
	public int getTraceFileSizeLimit() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.TRACE_FILE_SIZE_LIMIT,
				WBEMConfigurationDefaults.TRACE_FILE_SIZE_LIMIT));
	}

	/**
	 * Returns the http timeout
	 * 
	 * @return The http timeout
	 */
	public int getHttpTimeout() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.HTTP_TIMEOUT,
				WBEMConfigurationDefaults.HTTP_TIMEOUT));
	}

	/**
	 * Returns the http pool size
	 * 
	 * @return The http pool size
	 */
	public int getHttpPoolSize() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.HTTP_POOL_SIZE,
				WBEMConfigurationDefaults.HTTP_POOL_SIZE));
	}

	/**
	 * Returns the Java class name of http authentication module to use
	 * 
	 * @return The http authentication module's class name
	 */
	public String getHttpAuthenticationModule() {
		return getProperty(WBEMConfigurationProperties.HTTP_AUTHENTICATION_MODULE,
				WBEMConfigurationDefaults.HTTP_AUTHENTICATION_MODULE);
	}

	/**
	 * Returns the WWW-Authenticate info to use on first request
	 * 
	 * @return The WWW-Authenticate info
	 */
	public String getHttpWwwAuthenticateInfo() {
		return getProperty(WBEMConfigurationProperties.HTTP_WWW_AUTHENTICATE_INFO, null);
	}

	/**
	 * Returns the state of MPOST enablement
	 * 
	 * @return <code>true</code> if MPOST is enabled, <code>false</code>
	 *         otherwise
	 */
	public boolean isHttpMPost() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.HTTP_USE_MPOST,
						WBEMConfigurationDefaults.HTTP_USE_MPOST)).booleanValue();
	}

	/**
	 * Returns the state of chunking enablement
	 * 
	 * @return <code>true</code> if chunking is enabled, <code>false</code>
	 *         otherwise
	 */
	public boolean isHttpChunked() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.HTTP_USE_CHUNKING,
						WBEMConfigurationDefaults.HTTP_USE_CHUNKING)).booleanValue();
	}

	/**
	 * Returns the http version to use
	 * 
	 * @return The http version
	 */
	public String getHttpVersion() {
		return getProperty(WBEMConfigurationProperties.HTTP_VERSION,
				WBEMConfigurationDefaults.HTTP_VERSION);
	}

	/**
	 * Returns the file path of the keystore.
	 * 
	 * @return The keystore file path.
	 */
	public String getSslKeyStorePath() {
		return getProperty(WBEMConfigurationProperties.KEYSTORE_PATH, null);
	}

	/**
	 * Returns the type of the keystore.
	 * 
	 * @return The keystore type.
	 */
	public String getSslKeyStoreType() {
		return getProperty(WBEMConfigurationProperties.KEYSTORE_TYPE,
				WBEMConfigurationDefaults.KEYSTORE_TYPE);
	}

	/**
	 * Returns the password of the keystore.
	 * 
	 * @return The keystore password.
	 */
	public String getSslKeyStorePassword() {
		return getProperty(WBEMConfigurationProperties.KEYSTORE_PASSWORD, "");
	}

	/**
	 * Returns the file path of the truststore.
	 * 
	 * @return The truststore file path.
	 */
	public String getSslTrustStorePath() {
		return getProperty(WBEMConfigurationProperties.TRUSTSTORE_PATH, null);
	}

	/**
	 * Returns the type of the truststore.
	 * 
	 * @return The truststore type.
	 */
	public String getSslTrustStoreType() {
		return getProperty(WBEMConfigurationProperties.TRUSTSTORE_TYPE,
				WBEMConfigurationDefaults.TRUSTSTORE_TYPE);
	}

	/**
	 * Returns the password of the truststore.
	 * 
	 * @return The truststore password.
	 */
	public String getSslTrustStorePassword() {
		return getProperty(WBEMConfigurationProperties.TRUSTSTORE_PASSWORD, "");
	}

	/**
	 * Returns the JSSE provider to use for creating ssl client sockets
	 * 
	 * @return The JSSE provider for client sockets
	 */
	public String getSslSocketProvider() {
		Provider[] providers = Security.getProviders("SSLContext.SSL");
		return getProperty(WBEMConfigurationProperties.SSL_SOCKET_PROVIDER, providers != null
				&& providers.length > 0 ? providers[0].getClass().getName() : null);
	}

	/**
	 * Returns the JSSE provider to use for creating ssl server sockets
	 * 
	 * @return The JSSE provider for server sockets
	 */
	public String getSslServerSocketProvider() {
		Provider[] providers = Security.getProviders("SSLContext.SSL");
		return getProperty(WBEMConfigurationProperties.SSL_SERVER_SOCKET_PROVIDER,
				providers != null && providers.length > 0 ? providers[0].getClass().getName()
						: null);
	}

	/**
	 * getSslProtocol
	 * 
	 * @return The SSL protocol for SSLContext.getInstance()
	 */
	public String getSslProtocol() {
		return getSecurityProperty(WBEMConfigurationProperties.SSL_PROTOCOL,
				WBEMConfigurationDefaults.SSL_DEF_PROTOCOL);
	}

	/**
	 * getSslClientProtocol
	 * 
	 * @return The SSL protocol for SSLContext.getInstance() for client
	 */
	public String getSslClientProtocol() {
		return getProperty(WBEMConfigurationProperties.SSL_CLIENT_PROTOCOL, null);
	}

	/**
	 * getSslListenerProtocol
	 * 
	 * @return The SSL protocol for SSLContext.getInstance() for listener
	 */
	public String getSslListenerProtocol() {
		return getProperty(WBEMConfigurationProperties.SSL_LISTENER_PROTOCOL, null);
	}

	/**
	 * Returns the certificate algorithm the key manager will use
	 * 
	 * @return The key mangers algorithm
	 */
	public String getSslKeyManagerAlgorithm() {
		return getSecurityProperty(WBEMConfigurationProperties.SSL_KEYMANAGER_ALGORITHM, null);
	}

	/**
	 * Returns the certificate algorithm the trust manager will use
	 * 
	 * @return The trust mangers algorithm
	 */
	public String getSslTrustManagerAlgorithm() {
		return getSecurityProperty(WBEMConfigurationProperties.SSL_TRUSTMANAGER_ALGORITHM, null);
	}

	/**
	 * Returns whether the client will attempt to authenticate the CIMOM
	 * 
	 * @return <code>true</code> if the client will attempt to authenticate the
	 *         CIMOM by verifying the server certificate <code>false</code>
	 *         otherwise
	 */
	public boolean getSslClientPeerVerification() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.SSL_CLIENT_PEER_VERIFICATION,
						WBEMConfigurationDefaults.SSL_CLIENT_PEER_VERIFICATION)).booleanValue();
	}

	/**
	 * Returns how the listener will handle to authentication of the indication
	 * sender endpoint.
	 * 
	 * @return The listener peer verification mode
	 */
	public String getSslListenerPeerVerification() {
		return getProperty(WBEMConfigurationProperties.SSL_LISTENER_PEER_VERIFICATION,
				WBEMConfigurationDefaults.SSL_LISTENER_PEER_VERIFICATION);
	}

	/**
	 * Returns the comma-separated list of cipher suites to be disabled by
	 * client.
	 * 
	 * @return The list of cipher suites
	 */
	public String getSslClientCipherSuitesToDisable() {
		return getProperty(WBEMConfigurationProperties.SSL_CLIENT_CIPHER_SUITES_TO_DISABLE, null);
	}

	/**
	 * Returns the comma-separated list of cipher suites to be disabled by
	 * listener.
	 * 
	 * @return The list of cipher suites
	 */
	public String getSslListenerCipherSuitesToDisable() {
		return getProperty(WBEMConfigurationProperties.SSL_LISTENER_CIPHER_SUITES_TO_DISABLE, null);
	}

	/**
	 * Returns the number of retries the client will attempt when the connection
	 * was refused.
	 * 
	 * @return The number of retries
	 */
	public int getHttpConnectRetriesCount() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.HTTP_CONNECTION_RETRIES,
				WBEMConfigurationDefaults.HTTP_CONNECTION_RETRIES));
	}

	/**
	 * Returns if the content length retry feature is enabled
	 * 
	 * @return <code>true</code> if content length retry is enabled,
	 *         <code>false</code> otherwise
	 */
	public boolean isHttpContentLengthRetryEnabled() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.HTTP_ENABLE_CONTENT_LENGTH_RETRY,
						WBEMConfigurationDefaults.HTTP_ENABLE_CONTENT_LENGTH_RETRY)).booleanValue();
	}

	/**
	 * Returns the threshold for the content length retry algorithm
	 * 
	 * @return The threshold
	 */
	public int getHttpContentLengthThreshold() {
		return Integer.parseInt(getProperty(
				WBEMConfigurationProperties.HTTP_CONTENT_LENGTH_THRESHOLD,
				WBEMConfigurationDefaults.HTTP_CONTENT_LENGTH_THRESHOLD));
	}

	/**
	 * Return the XML parser to use for processing operation responses
	 * 
	 * @return The XML parser
	 */
	public String getCimXmlParser() {
		return getProperty(WBEMConfigurationProperties.CIMXML_PARSER,
				WBEMConfigurationDefaults.CIMXML_PARSER);
	}

	/**
	 * getCimXmlEmbObjBuilder
	 * 
	 * @return configuration property for the embedded object builder
	 */
	public String getCimXmlEmbObjBuilder() {
		return getProperty(WBEMConfigurationProperties.CIMXML_EMBOBJBUILDER,
				WBEMConfigurationDefaults.CIMXML_EMBOBJBUILDER);
	}

	/**
	 * strictEmbObjTypes
	 * 
	 * @return true if EmbeddedObject exactly means Embedded Class
	 */
	public boolean strictEmbObjTypes() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.CIMXML_PARSER_STRICT_EMBOBJ_TYPES,
						WBEMConfigurationDefaults.CIMXML_PARSER_STRICT_EMBOBJ_TYPES))
				.booleanValue();
	}

	/**
	 * upperCaseEmbObjEntities
	 * 
	 * @return true if "EMBEDDEDOBJECT" entity, false if "EmbeddedObject"
	 */
	public boolean upperCaseEmbObjEntities() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.CIMXML_BUILDER_UPPERCASE_EMBOBJ_ENTITIES,
						WBEMConfigurationDefaults.CIMXML_BUILDER_UPPERCASE_EMBOBJ_ENTITIES))
				.booleanValue();
	}

	/**
	 * performSslHandshake
	 * 
	 * @return true if SSL handshakes should take place
	 */
	public boolean performSslHandshake() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.PERFORM_SSL_HANDSHAKE,
						WBEMConfigurationDefaults.PERFORM_SSL_HANDSHAKE)).booleanValue();
	}

	/**
	 * synchronizedSslHandshake
	 * 
	 * @return true if SSL handshakes should be synchronized
	 */
	public boolean synchronizedSslHandshake() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.SYNCHRONIZED_SSL_HANDSHAKE,
						WBEMConfigurationDefaults.SYNCHRONIZED_SSL_HANDSHAKE)).booleanValue();
	}

	/**
	 * socketConnectWithTimeout
	 * 
	 * @return true if socket connections should be attempted with a timeout
	 */
	public boolean socketConnectWithTimeout() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.SOCKET_CONNECT_WITH_TIMEOUT,
						WBEMConfigurationDefaults.SOCKET_CONNECT_WITH_TIMEOUT)).booleanValue();
	}

	/**
	 * Returns the socket connect timeout
	 * 
	 * @return The socket connect timeout
	 */
	public int getSocketConnectTimeout() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.SOCKET_CONNECT_TIMEOUT,
				WBEMConfigurationDefaults.SOCKET_CONNECT_TIMEOUT));
	}

	/**
	 * Returns the socket idle timeout
	 * 
	 * @return The socket idle timeout
	 */
	public int getSocketIdleTimeout() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.SOCKET_IDLE_TIMEOUT,
				WBEMConfigurationDefaults.SOCKET_IDLE_TIMEOUT));
	}

	/**
	 * isDefaultAuthorizationEnabled
	 * 
	 * @return true if default authorization is enabled
	 */
	public boolean isDefaultAuthorizationEnabled() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.KEY_CREDENTIALS_DEFAULT_ENABLED,
						WBEMConfigurationDefaults.KEY_CREDENTIALS_DEFAULT_ENABLED)).booleanValue();
	}

	/**
	 * getDefaultPrincipal
	 * 
	 * @return default value of Principal
	 */
	public String getDefaultPrincipal() {
		return getProperty(WBEMConfigurationProperties.KEY_DEFAULT_PRINCIPAL,
				WBEMConfigurationDefaults.KEY_DEFAULT_PRINCIPAL);
	}

	/**
	 * getDefaultCredentials
	 * 
	 * @return default value of Credentials
	 */
	public String getDefaultCredentials() {
		return getProperty(WBEMConfigurationProperties.KEY_DEFAULT_CREDENTIAL,
				WBEMConfigurationDefaults.KEY_DEFAULT_CREDENTIAL);
	}

	/**
	 * Returns the state of CIM-XML tracing
	 * 
	 * @return <code>true</code> if tracing is enabled, <code>false</code>
	 *         otherwise
	 */
	public boolean isCimXmlTracingEnabled() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.CIMXML_TRACING,
						WBEMConfigurationDefaults.CIMXML_TRACING)).booleanValue();
	}

	/**
	 * Returns the CIM-XML trace stream to be used if application does not set
	 * one of its own
	 * 
	 * @return The trace stream
	 */
	public String getCimXmlTraceStream() {
		return getProperty(WBEMConfigurationProperties.CIMXML_TRACE_STREAM, null);
	}

	/**
	 * Returns the backlog that is tolerated before the thread pool creates an
	 * additional handler
	 * 
	 * @return The backlog
	 */
	public int getListenerBacklog() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LISTENER_BACKLOG,
				WBEMConfigurationDefaults.LISTENER_BACKLOG));
	}

	/**
	 * Returns the http timeout for indication connection handlers
	 * 
	 * @return The timeout
	 */
	public int getListenerHttpTimeout() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LISTENER_HTTP_TIMEOUT,
				WBEMConfigurationDefaults.LISTENER_HTTP_TIMEOUT));
	}

	/**
	 * Returns the http header timeout for indication connection handlers
	 * 
	 * @return The timeout
	 */
	public int getListenerHttpHeaderTimeout() {
		return Integer.parseInt(getProperty(
				WBEMConfigurationProperties.LISTENER_HTTP_HEADER_TIMEOUT,
				WBEMConfigurationDefaults.LISTENER_HTTP_HEADER_TIMEOUT));
	}

	/**
	 * Returns the maximum allowed http timeouts before IP is blocked
	 * 
	 * @return The maximum allowed timeouts
	 */
	public int getListenerMaxAllowedTimeouts() {
		return Integer.parseInt(getProperty(
				WBEMConfigurationProperties.LISTENER_HTTP_MAX_ALLOWED_TIMEOUTS,
				WBEMConfigurationDefaults.LISTENER_HTTP_MAX_ALLOWED_TIMEOUTS));
	}

	/**
	 * Returns the maximal pool size for indication connection handlers
	 * 
	 * @return The maximal pool size
	 */
	public int getListenerMaxPoolSize() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LISTENER_MAX_POOL_SIZE,
				WBEMConfigurationDefaults.LISTENER_MAX_POOL_SIZE));
	}

	/**
	 * Returns the minimal pool size for indication connection handlers
	 * 
	 * @return The minimal pool size
	 */
	public int getListenerMinPoolSize() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LISTENER_MIN_POOL_SIZE,
				WBEMConfigurationDefaults.LISTENER_MIN_POOL_SIZE));
	}

	/**
	 * Returns the maximal queue size for pending indication connections
	 * 
	 * @return The maximal queue size
	 */
	public int getListenerMaxQueueSize() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LISTENER_MAX_QUEUE_SIZE,
				WBEMConfigurationDefaults.LISTENER_MAX_QUEUE_SIZE));
	}

	/**
	 * Returns the maximal idle time that is tolerated for a connection handler
	 * 
	 * @return The maximal idle time
	 */
	public long getListenerMaxIdle() {
		return Long.parseLong(getProperty(WBEMConfigurationProperties.LISTENER_HANDLER_MAX_IDLE,
				WBEMConfigurationDefaults.LISTENER_HANDLER_MAX_IDLE));
	}

	/**
	 * Returns the maximal queue size for indications awaiting delivery
	 * 
	 * @return The maximal queue size
	 */
	public int getListenerMaxQueuedEvents() {
		return Integer.parseInt(getProperty(WBEMConfigurationProperties.LISTENER_MAX_QUEUED_EVENTS,
				WBEMConfigurationDefaults.LISTENER_MAX_QUEUED_EVENTS));
	}

	/**
	 * Returns the state of reliable indication support
	 * 
	 * @return <code>true</code> if reliable indication support is enabled,
	 *         <code>false</code> otherwise
	 */
	public boolean isReliableIndicationEnabled() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.LISTENER_ENABLE_RELIABLE_INDICATIONS,
						WBEMConfigurationDefaults.LISTENER_ENABLE_RELIABLE_INDICATIONS))
				.booleanValue();
	}

	/**
	 * Returns the default value to use for CIM_IndicationService
	 * DeliveryRetryAttempts
	 * 
	 * @return The default value
	 */
	public long getListenerDeliveryRetryAttempts() {
		return Long.parseLong(getProperty(
				WBEMConfigurationProperties.LISTENER_DELIVERY_RETRY_ATTEMPTS,
				WBEMConfigurationDefaults.LISTENER_DELIVERY_RETRY_ATTEMPTS));
	}

	/**
	 * Returns the default value to use for CIM_IndicationService
	 * DeliveryRetryInterval
	 * 
	 * @return The default value
	 */
	public long getListenerDeliveryRetryInterval() {
		return Long.parseLong(getProperty(
				WBEMConfigurationProperties.LISTENER_DELIVERY_RETRY_INTERVAL,
				WBEMConfigurationDefaults.LISTENER_DELIVERY_RETRY_INTERVAL));
	}

	/**
	 * Returns the initial capacity of the hash table used to handle reliable
	 * indications
	 * 
	 * @return The default value
	 */
	public int getReliableIndicationHashtableCapacity() {
		return Integer.parseInt(getProperty(
				WBEMConfigurationProperties.LISTENER_RELIABLE_INDICATION_HASHTABLE_CAPACITY,
				WBEMConfigurationDefaults.LISTENER_RELIABLE_INDICATION_HASHTABLE_CAPACITY));
	}

	/**
	 * Returns the filter to use for tracing of incoming indications at the FINE
	 * level
	 * 
	 * @return The indication trace filter
	 */
	public String getListenerIndicationTraceFilter() {
		return getProperty(WBEMConfigurationProperties.LISTENER_INDICATION_TRACE_FILTER, null);
	}

	/**
	 * Returns whether sender IP property should be added to indications
	 * 
	 * @return <code>true</code> if sender IP property should be added to
	 *         indications, <code>false</code> otherwise
	 */
	public boolean getListenerAddSenderIPAddress() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.LISTENER_ADD_SENDER_IP_ADDRESS,
						WBEMConfigurationDefaults.LISTENER_ADD_SENDER_IP_ADDRESS)).booleanValue();
	}

	/**
	 * Returns whether the client will attempt to verify strings passed into the
	 * <code>java.lang.Double</code> constructor or its <code>parseDouble</code>
	 * method won't hang the JVM in an infinite loop.
	 * 
	 * @return <code>true</code> if the client will attempt to verify strings
	 *         passed to <code>Double</code>, <code>false</code> otherwise
	 */
	public boolean verifyJavaLangDoubleStrings() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.VERIFY_JAVA_LANG_DOUBLE_STRINGS,
						WBEMConfigurationDefaults.VERIFY_JAVA_LANG_DOUBLE_STRINGS)).booleanValue();
	}

	/**
	 * Returns whether the client will synchronize the data types of all numeric
	 * keys in a <code>CIMInstance</code>'s <code>CIMObjectPath</code> match
	 * those of the corresponding keys within the <code>CIMInstance</code>'s
	 * <code>CIMProperty[]</code>.
	 * 
	 * @return <code>true</code> if the client will synchronize
	 *         <code>CIMInstance</code> numeric key data types,
	 *         <code>false</code> otherwise
	 */
	public boolean synchronizeNumericKeyDataTypes() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.SYNCHRONIZE_NUMERIC_KEY_DATA_TYPES,
						WBEMConfigurationDefaults.SYNCHRONIZE_NUMERIC_KEY_DATA_TYPES))
				.booleanValue();
	}

	/**
	 * Returns the state of gzip encoding enablement.
	 * 
	 * @return <code>true</code> if gzip encoding is enabled, <code>false</code>
	 *         otherwise
	 */
	public boolean isGzipEncodingEnabled() {
		return Boolean.valueOf(
				getProperty(WBEMConfigurationProperties.ENABLE_GZIP_ENCODING,
						WBEMConfigurationDefaults.ENABLE_GZIP_ENCODING)).booleanValue();
	}

	/**
	 * Returns whether the client will allow empty
	 * <code>LOCALNAMESPACEPATH</code> elements in incoming CIM-XML responses.
	 * 
	 * @return <code>true</code> if CIM-XML parser should allow empty
	 *         <code>LOCALNAMESPACEPATH</code> elements in incoming responses,
	 *         <code>false</code> otherwise
	 */
	public boolean allowEmptyLocalNameSpacePath() {
		return Boolean.valueOf(
				getProperty(
						WBEMConfigurationProperties.CIMXML_PARSER_ALLOW_EMPTY_LOCALNAMESPACEPATH,
						WBEMConfigurationDefaults.CIMXML_PARSER_ALLOW_EMPTY_LOCALNAMESPACEPATH))
				.booleanValue();
	}
}
