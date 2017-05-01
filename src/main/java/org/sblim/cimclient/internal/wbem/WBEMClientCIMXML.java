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
 * @author : Alexander Wolf-Reber, a.wolf-reber@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-11-08  lupusalex    Make SBLIM client JSR48 compliant
 * 1688273    2007-04-16  ebak         Full support of HTTP trailers
 * 1702832    2007-04-18  lupusalex    WBEMClientCIMXL.setCustomSocketFactory() not implemented
 * 1686000    2007-04-19  ebak         modifyInstance() missing from WBEMClient
 * 1714184    2007-05-07  lupusalex    FVT: NPE on WBEMClientCIMXML.init()
 * 1715053    2007-05-08  lupusalex    FVT: No forced retry on HTTP 501/510
 * 1714853    2007-05-08  lupusalex    Inexplicit error when operation is invoked on closed client
 * 1714902    2007-05-08  lupusalex    Threading related weak spots
 * 1715482    2007-05-10  lupusalex    CIM_ERR_FAILED thrown when access denied
 * 1736318    2007-06-13  lupusalex    Wrong object path in HTTP header
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 1741654    2007-08-22  ebak         Header mismatch error on ModifyInstance
 * 1949000    2008-04-24  blaschke-oss setLocales() is empty
 * 1963762    2008-05-14  blaschke-oss connection leak in WBEMClientCIMXML
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2002599    2008-07-05  raman_arora  M-POST not supported in java-client
 * 2034342    2008-07-31  blaschke-oss HttpClient not closed on cimclient close
 * 2087975    2008-09-03  blaschke-oss can't set the pPropagated in WBEMClient.enumerateClasses()
 * 2382763    2008-12-03  blaschke-oss HTTP header field Accept-Language does not include *
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics 
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 2860081    2009-09-17  raman_arora  Pull Enumeration Feature (DOM Parser)
 * 2846231    2009-09-23  rgummada     connection failure on CIMOM w/o user/pw
 * 2865222    2009-09-29  raman_arora  enumerateQualifierTypes shouldn't require a class name
 * 2858933    2009-10-12  raman_arora  JSR48 new APIs: associatorClasses & associatorInstances
 * 2884718    2009-10-23  blaschke-oss Merge JSR48 and SBLIM client properties
 * 2878054    2009-10-25  raman_arora  Pull Enumeration Feature (PULL Parser)
 * 2888774    2009-10-29  raman_arora  support POST retry on HTTP error 505
 * 2886829    2009-11-18  raman_arora  JSR48 new APIs: referenceClasses & referenceInstances
 * 2909941    2010-01-06  blaschke-oss RequestStateChange gives wrong exception/error id
 * 2930341    2010-01-12  blaschke-oss Sync up WBEMClientConstants with JSR48 1.0.0
 * 2913938    2010-02-17  blaschke-oss Duplicate CIM requests with identical message ID
 * 2961592    2010-03-01  blaschke-oss Remove WBEMClient.setLocales() UnsupportedOperationException
 * 2964463    2010-03-08  blaschke-oss WBEMClient.initialize() throws wrong exception
 * 2942520    2010-03-08  blaschke-oss IPv6 link local address with scope_id including a dot not supported
 * 3019252    2010-06-21  blaschke-oss Methods concatenate strings using + in a loop
 * 3028518    2010-07-14  blaschke-oss Additional StringBuilder use
 * 3185833    2011-02-18  blaschke-oss missing newline when logging request/response
 * 3197423    2011-03-02  blaschke-oss Server authentication with PegasusLocalAuthInfo failing
 * 3277928    2011-04-06  blaschke-oss CIM-XML tracing cannot be enabled in the field
 * 3423064    2011-10-13  blaschke-oss Add UpdateExpiredPassword Header for Reqs from Java Client
 * 3496355    2012-03-02  blaschke-oss JSR48 1.0.0: add new WBEMClientConstants
 * 3514537    2012-04-03  blaschke-oss TCK: execQueryInstances requires boolean, not Boolean
 * 3514685    2012-04-03  blaschke-oss TCK: getProperty must return default values
 * 3515180    2012-04-05  blaschke-oss JSR48 log dir/file should handle UNIX/Win separators
 * 3516848    2012-04-11  blaschke-oss enumerateNamespaces() method to WBEMClient
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 * 3521328    2012-04-25  blaschke-oss JSR48 1.0.0: remove WBEMClient associators and references
 * 3522904    2012-05-02  blaschke-oss Add new API WBEMClientSBLIM.isActive()
 * 3521157    2012-05-10  blaschke-oss JSR48 1.0.0: PROP_ENABLE_*_LOGGING is Level, not 0/1
 * 3525914    2012-05-11  blaschke-oss TCK: SetPropertyTest.testSetProperty failing
 * 3545797    2012-07-19  blaschke-oss Support new error code of SFCB
 * 3553858    2012-08-06  blaschke-oss Append duplicate HTTP header fields instead of replace
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
 * 3601894    2013-01-23  blaschke-oss Enhance HTTP and CIM-XML tracing
 *    2614    2013-02-21  blaschke-oss Remove redundant code in transmitRequest
 *    2616    2013-02-23  blaschke-oss Add new API WBEMClientSBLIM.sendIndication()
 *    2651    2013-07-31  blaschke-oss IOException when tracing the cimxml
 *    2662    2013-09-10  blaschke-oss Need the specific SSLHandshakeException during the cim call
 *    2594    2013-11-30  blaschke-oss CR28: Support CIMErrorDescription HTTP field
 */
package org.sblim.cimclient.internal.wbem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

import javax.cim.CIMArgument;
import javax.cim.CIMClass;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMQualifierType;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.net.SocketFactory;
import javax.net.ssl.SSLHandshakeException;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.EnumerateResponse;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.RoleCredential;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sblim.cimclient.WBEMClientSBLIM;
import org.sblim.cimclient.WBEMConfigurationProperties;
import org.sblim.cimclient.internal.cim.CIMHelper;
import org.sblim.cimclient.internal.cimxml.CIMClientXML_HelperImpl;
import org.sblim.cimclient.internal.cimxml.CIMMessage;
import org.sblim.cimclient.internal.cimxml.CIMResponse;
import org.sblim.cimclient.internal.cimxml.CIMXMLParseException;
import org.sblim.cimclient.internal.cimxml.CIMXMLParserImpl;
import org.sblim.cimclient.internal.cimxml.sax.SAXHelper;
import org.sblim.cimclient.internal.http.AuthorizationHandler;
import org.sblim.cimclient.internal.http.AuthorizationInfo;
import org.sblim.cimclient.internal.http.HttpClientPool;
import org.sblim.cimclient.internal.http.HttpHeader;
import org.sblim.cimclient.internal.http.HttpHeader.HeaderEntry;
import org.sblim.cimclient.internal.http.HttpHeaderParser;
import org.sblim.cimclient.internal.http.HttpUrlConnection;
import org.sblim.cimclient.internal.http.io.DebugInputStream;
import org.sblim.cimclient.internal.http.io.TrailerException;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.logging.Messages;
import org.sblim.cimclient.internal.logging.TimeStamp;
import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.sblim.cimclient.internal.util.WBEMConfigurationDefaults;
import org.sblim.cimclient.internal.util.WBEMConstants;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The WBEMClientCIMXML is a implementation of the
 * <code>javax.wbem.client.WBEMClient</code> interface for the CIM-XML protocol
 * including the extensions of the
 * <code>org.sblim.cimclient.WBEMClientSBLIM</code> interface.
 * 
 * @see WBEMClient
 * @see WBEMClientSBLIM
 */
public class WBEMClientCIMXML implements WBEMClientSBLIM {

	private final WBEMConfiguration iConfiguration = new WBEMConfiguration(new Properties());

	private Locale[] iLocales; // final

	private HttpClientPool iHttpClientPool; // final

	private URI iUri; // final

	private AuthorizationHandler iAuthorizationHandler; // final

	private CIMClientXML_HelperImpl iXmlHelper; // final

	private volatile String iAuthorization;

	private volatile int iNsCounter = 10;

	private volatile boolean iMPostFailed = false;

	private volatile long iMPostFailTime = 0;

	private volatile long iCurrentTime = 0;

	private volatile boolean iInitialized = false;

	private volatile boolean iClosed = false;

	private String iInteropNamespace = null;

	/**
	 * Ctor.
	 */
	public WBEMClientCIMXML() {
		super();
	}

	private synchronized void initializeClient(URI pUri, Subject pSubject, Locale[] pLocales)
			throws IllegalArgumentException {

		if (this.iInitialized) throw new IllegalStateException("WBEMClient is already initialized");

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		try {
			this.iHttpClientPool = new HttpClientPool(this.iConfiguration);
			this.iLocales = (pLocales != null && pLocales.length > 0) ? pLocales
					: WBEMConstants.DEFAULT_LOCALES;
			this.iUri = pUri;

			AuthorizationInfo authInfo = AuthorizationInfo.createAuthorizationInfo(
					this.iConfiguration.getHttpAuthenticationModule(), Boolean.FALSE, this.iUri
							.getHost(), this.iUri.getPort(), null, null, null);

			Principal principal = (pSubject != null && pSubject.getPrincipals() != null && !pSubject
					.getPrincipals().isEmpty()) ? (Principal) pSubject.getPrincipals().iterator()
					.next() : null;
			Object credential = (pSubject != null && pSubject.getPrivateCredentials() != null
					&& !pSubject.getPrivateCredentials().isEmpty() ? pSubject
					.getPrivateCredentials().iterator().next() : null);

			boolean defaultAuthEnabled = WBEMConfiguration.getGlobalConfiguration()
					.isDefaultAuthorizationEnabled();

			String user = (principal != null) ? principal.getName() : "";
			String password = (credential != null && credential instanceof PasswordCredential) ? new String(
					((PasswordCredential) credential).getUserPassword())
					: ((credential != null && credential instanceof RoleCredential) ? new String(
							((RoleCredential) credential).getCredential()) : "");

			if (defaultAuthEnabled && (user == null || user.length() == 0)
					&& password.length() == 0) {
				logger.trace(Level.FINER,
						"Principal and Credential not set - using default authorization!");

				user = WBEMConfiguration.getGlobalConfiguration().getDefaultPrincipal();
				password = WBEMConfiguration.getGlobalConfiguration().getDefaultCredentials();
			}

			authInfo.setCredentials(new PasswordAuthentication(user, password.toCharArray()));
			this.iAuthorizationHandler = new AuthorizationHandler();
			this.iAuthorizationHandler.addAuthorizationInfo(authInfo);

			try {
				this.iXmlHelper = new CIMClientXML_HelperImpl();
			} catch (ParserConfigurationException e) {
				logger
						.trace(Level.FINE, "Exception while instantiating CIMClientXML_HelperImpl",
								e);
				logger.message(Messages.CIM_XMLHELPER_FAILED);
				throw new RuntimeException(e);
			}

			this.iInitialized = true;

		} finally {
			logger.exit();
		}
	}

	public void initialize(CIMObjectPath pName, Subject pSubject, Locale[] pLocales)
			throws IllegalArgumentException, WBEMException {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		try {
			final URI uri;

			if (pName == null || pName.getHost() == null || pName.getHost().length() == 0) { throw new IllegalArgumentException(
					"Empty host path"); }
			if (pName.getScheme() == null || pName.getScheme().length() == 0) { throw new IllegalArgumentException(
					"Empty scheme"); }
			if (!pName.getScheme().equalsIgnoreCase(WBEMConstants.HTTP)
					&& !pName.getScheme().equalsIgnoreCase(WBEMConstants.HTTPS)) { throw new IllegalArgumentException(
					"Protocol not supported"); }

			try {
				uri = CIMHelper.createCimomUri(pName);
			} catch (URISyntaxException e) {
				throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER, "Malformed URI");
			}

			initializeClient(uri, pSubject, pLocales);
		} finally {
			logger.exit();
		}
	}

	public void initialize(URI pUri, Subject pSubject, Locale[] pLocales)
			throws IllegalArgumentException, WBEMException {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		try {
			final URI uri;

			if (pUri == null || pUri.getHost() == null || pUri.getHost().length() == 0) { throw new IllegalArgumentException(
					"Empty host path"); }
			if (pUri.getScheme() == null || pUri.getScheme().length() == 0) { throw new IllegalArgumentException(
					"Empty scheme"); }
			if (!pUri.getScheme().equalsIgnoreCase(WBEMConstants.HTTP)
					&& !pUri.getScheme().equalsIgnoreCase(WBEMConstants.HTTPS)) { throw new IllegalArgumentException(
					"Protocol not supported"); }

			try {
				uri = CIMHelper.createCimomUri(pUri);
			} catch (URISyntaxException e) {
				throw new WBEMException(WBEMException.CIM_ERR_INVALID_PARAMETER, "Malformed URI");
			}

			initializeClient(uri, pSubject, pLocales);
		} finally {
			logger.exit();
		}
	}

	public Properties getProperties() {
		return this.iConfiguration.getDomainProperties();
	}

	private int getLastSeparator(String path) {
		if (path == null) return -1;

		int last = -1;
		int sepUnix = path.lastIndexOf('/');
		int sepWin = path.lastIndexOf('\\');

		if (sepUnix != -1 && sepWin != -1) {
			last = sepUnix > sepWin ? sepUnix : sepWin;
		} else if (sepUnix != -1) {
			last = sepUnix;
		} else if (sepWin != -1) {
			last = sepWin;
		}
		return last;
	}

	public String getProperty(String pKey) {
		if (pKey.startsWith("javax.wbem.")) {
			// Process JSR48 properties
			if (pKey.equals(WBEMClientConstants.PROP_ENABLE_CONSOLE_LOGGING)) {
				return this.iConfiguration.getLogConsoleLevel().getName();
			} else if (pKey.equals(WBEMClientConstants.PROP_ENABLE_FILE_LOGGING)) {
				return this.iConfiguration.getLogFileLevel().getName();
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_BYTE_LIMIT)) {
				return Integer.toString(this.iConfiguration.getLogFileSizeLimit());
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_DIR)) {
				String SblimKey = this.iConfiguration.getLogFileLocation();
				if (SblimKey == null) return null;
				int lastSep = getLastSeparator(SblimKey);
				return lastSep == -1 ? null : SblimKey.substring(0, lastSep);
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_FILENAME)) {
				String SblimKey = this.iConfiguration.getLogFileLocation();
				if (SblimKey == null) return null;
				int lastSep = getLastSeparator(SblimKey);
				return lastSep == -1 ? SblimKey : SblimKey.substring(lastSep + 1);
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_NUM_FILES)) {
				return Integer.toString(this.iConfiguration.getLogFileCount());
			} else if (pKey.equals(WBEMClientConstants.PROP_TIMEOUT)) {
				return Integer.toString(this.iConfiguration.getHttpTimeout());
			} else if (pKey.equals(WBEMClientConstants.PROPERTY_WBEM_CHUNKING)) {
				return this.iConfiguration.isHttpChunked() ? "1" : "0";
			} else if (pKey.equals(WBEMClientConstants.PROP_CLIENT_KEYSTORE)) {
				return this.iConfiguration.getSslKeyStorePath();
			} else if (pKey.equals(WBEMClientConstants.PROP_CLIENT_KEYSTORE_PASSWORD)) {
				return this.iConfiguration.getSslKeyStorePassword();
			} else if (pKey.equals(WBEMClientConstants.PROP_CLIENT_TRUSTSTORE)) {
				return this.iConfiguration.getSslTrustStorePath();
			} else {
				return null;
			}
		}
		return this.iConfiguration.getDomainProperty(pKey);

	}

	public void setProperties(Properties pProperties) {
		this.iConfiguration.setDomainProperties(pProperties);
	}

	public void setProperty(String pKey, String pValue) {
		if (pKey.startsWith("javax.wbem.")) {
			// Process JSR48 properties
			if (pKey.equals(WBEMClientConstants.PROP_ENABLE_CONSOLE_LOGGING)) {
				this.iConfiguration.setDomainProperty(
						WBEMConfigurationProperties.LOG_CONSOLE_LEVEL, pValue);
			} else if (pKey.equals(WBEMClientConstants.PROP_ENABLE_FILE_LOGGING)) {
				this.iConfiguration.setDomainProperty(WBEMConfigurationProperties.LOG_FILE_LEVEL,
						pValue);
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_BYTE_LIMIT)) {
				this.iConfiguration.setDomainProperty(
						WBEMConfigurationProperties.LOG_FILE_SIZE_LIMIT, pValue);
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_DIR)) {
				StringBuffer NewSblimLog = new StringBuffer(pValue != null ? pValue : "");
				if (NewSblimLog.length() > 0
						&& NewSblimLog.charAt(NewSblimLog.length() - 1) != File.separatorChar) {
					NewSblimLog.append(File.separator);
				}

				String CurSblimLog = this.iConfiguration
						.getDomainProperty(WBEMConfigurationProperties.LOG_FILE_LOCATION);
				if (CurSblimLog == null) {
					CurSblimLog = WBEMConfigurationDefaults.LOG_FILE_LOCATION;
				}

				int i = getLastSeparator(CurSblimLog);
				if (i == -1) {
					NewSblimLog.append(CurSblimLog);
				} else {
					NewSblimLog.append(CurSblimLog.substring(i + 1));
				}

				this.iConfiguration.setDomainProperty(
						WBEMConfigurationProperties.LOG_FILE_LOCATION, NewSblimLog.toString());
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_FILENAME)) {
				StringBuffer NewSblimLog = new StringBuffer(pValue != null ? pValue : "");

				String CurSblimLog = this.iConfiguration
						.getDomainProperty(WBEMConfigurationProperties.LOG_FILE_LOCATION);
				if (CurSblimLog != null) {
					int i = getLastSeparator(CurSblimLog);
					if (i != -1) {
						NewSblimLog.insert(0, CurSblimLog.substring(0, i + 1));
					}
				}

				this.iConfiguration.setDomainProperty(
						WBEMConfigurationProperties.LOG_FILE_LOCATION, NewSblimLog.toString());
			} else if (pKey.equals(WBEMClientConstants.PROP_LOG_NUM_FILES)) {
				this.iConfiguration.setDomainProperty(WBEMConfigurationProperties.LOG_FILE_COUNT,
						pValue);
			} else if (pKey.equals(WBEMClientConstants.PROP_TIMEOUT)) {
				this.iConfiguration.setDomainProperty(WBEMConfigurationProperties.HTTP_TIMEOUT,
						pValue);
			} else if (pKey.equals(WBEMClientConstants.PROPERTY_WBEM_CHUNKING)) {
				this.iConfiguration.setDomainProperty(
						WBEMConfigurationProperties.HTTP_USE_CHUNKING, pValue.equals("0") ? "false"
								: "true");
			} else if (pKey.equals(WBEMClientConstants.PROP_CLIENT_KEYSTORE)) {
				this.iConfiguration.setDomainProperty(WBEMConfigurationProperties.KEYSTORE_PATH,
						pValue);
			} else if (pKey.equals(WBEMClientConstants.PROP_CLIENT_KEYSTORE_PASSWORD)) {
				this.iConfiguration.setDomainProperty(
						WBEMConfigurationProperties.KEYSTORE_PASSWORD, pValue);
			} else if (pKey.equals(WBEMClientConstants.PROP_CLIENT_TRUSTSTORE)) {
				this.iConfiguration.setDomainProperty(WBEMConfigurationProperties.TRUSTSTORE_PATH,
						pValue);
			} else {
				throw new IllegalArgumentException(pKey);
			}
		} else {
			this.iConfiguration.setDomainProperty(pKey, pValue);
		}
	}

	public CloseableIterator<CIMObjectPath> associatorNames(CIMObjectPath pObjectName,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole)
			throws WBEMException {

		final String operation = "AssociatorNames";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {

			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.associatorNames_request(doc,
					pObjectName, pAssociationClass, pResultClass, pRole, pResultRole));

			InputStreamReader is = transmitRequest("AssociatorNames", hh, doc);

			CloseableIterator<CIMObjectPath> iter = getIterator(is, pObjectName);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMClass> associatorClasses(CIMObjectPath pObjectName,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeQualifiers, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {
		final String operation = "AssociatorClasses";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {

			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.associatorClasses_request(doc,
					pObjectName, pAssociationClass, pResultClass, pRole, pResultRole,
					pIncludeQualifiers, pIncludeClassOrigin, pPropertyList));

			InputStreamReader is = transmitRequest("Associators", hh, doc);

			CloseableIterator<CIMClass> iter = getIterator(is, pObjectName);

			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMInstance> associatorInstances(CIMObjectPath pObjectName,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException {
		final String operation = "AssociatorInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {

			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.associatorInstances_request(doc,
					pObjectName, pAssociationClass, pResultClass, pRole, pResultRole,
					pIncludeClassOrigin, pPropertyList));

			InputStreamReader is = transmitRequest("Associators", hh, doc);

			CloseableIterator<CIMInstance> iter = getIterator(is, pObjectName);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public synchronized void close() {
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		try {
			checkState();
			if (this.iHttpClientPool != null) {
				this.iHttpClientPool.closePool();
			}
		} finally {
			this.iClosed = true;
			logger.exit();
		}
	}

	public void createClass(CIMClass pClass) throws WBEMException {

		final String operation = "CreateClass";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {

			if (pClass == null || pClass.getObjectPath() == null
					|| pClass.getObjectPath().getNamespace() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pClass.getObjectPath().getNamespace(),
					"UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.createClass_request(doc, pClass
					.getObjectPath(), pClass));

			InputStreamReader is = transmitRequest("CreateClass", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pClass.getObjectPath());
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CIMObjectPath createInstance(CIMInstance pInstance) throws WBEMException {

		final String operation = "CreateInstance";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pInstance == null || pInstance.getObjectPath() == null
					|| pInstance.getObjectPath().getNamespace() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pInstance.getObjectPath().getNamespace(),
					"UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.createInstance_request(doc,
					pInstance.getObjectPath(), pInstance));

			InputStreamReader is = transmitRequest("CreateInstance", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pInstance.getObjectPath());
			try {
				if (iter.hasNext()) { return (CIMObjectPath) iter.next(); }
			} finally {
				iter.close();
			}

			return null;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public void deleteClass(CIMObjectPath pPath) throws WBEMException {

		final String operation = "DeleteClass";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null || pPath.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.deleteClass_request(doc, pPath));

			InputStreamReader is = transmitRequest("DeleteClass", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pPath);
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}

			return;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public void deleteInstance(CIMObjectPath pPath) throws WBEMException {

		final String operation = "DeleteInstance";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null || pPath.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper
					.deleteInstance_request(doc, pPath));

			InputStreamReader is = transmitRequest("DeleteInstance", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pPath);
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}

			return;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public void deleteQualifierType(CIMObjectPath pPath) throws WBEMException {

		final String operation = "DeleteQualifierType";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null || pPath.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.deleteQualifierType_request(doc,
					pPath));

			InputStreamReader is = transmitRequest("DeleteQualifierType", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pPath);
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}

			return;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMObjectPath> enumerateClassNames(CIMObjectPath pPath, boolean pDeep)
			throws WBEMException {

		final String operation = "EnumerateClassNames";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.enumerateClassNames_request(doc,
					pPath, pDeep));

			InputStreamReader is = transmitRequest("EnumerateClassNames", hh, doc);

			CloseableIterator<CIMObjectPath> iter = getIterator(is, pPath);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMClass> enumerateClasses(CIMObjectPath pPath, boolean pDeep,
			boolean pPropagated, boolean pIncludeQualifiers, boolean pIncludeClassOrigin)
			throws WBEMException {

		final String operation = "EnumerateClasses";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.enumerateClasses_request(doc,
					pPath, pDeep, pPropagated, pIncludeQualifiers, pIncludeClassOrigin));

			InputStreamReader is = transmitRequest("EnumerateClasses", hh, doc);

			CloseableIterator<CIMClass> iter = getIterator(is, pPath);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMObjectPath> enumerateInstanceNames(CIMObjectPath pPath)
			throws WBEMException {

		final String operation = "EnumerateInstanceNames";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null || pPath.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.enumerateInstanceNames_request(
					doc, pPath));

			InputStreamReader is = transmitRequest("EnumerateInstanceNames", hh, doc);

			CloseableIterator<CIMObjectPath> iter = getIterator(is, pPath);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMInstance> enumerateInstances(CIMObjectPath pPath, boolean pDeep,
			boolean pPropagated, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {

		final String operation = "EnumerateInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null || pPath.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.enumerateInstances_request(doc,
					pPath, pDeep, pPropagated, false, pIncludeClassOrigin, pPropertyList));

			InputStreamReader is = transmitRequest("EnumerateInstances", hh, doc);

			CloseableIterator<CIMInstance> iter = getIterator(is, pPath);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	// DSP1033 states that CIMOMs shall include an Interop Namespace and that
	// the name of this Interop Namespace shall be either "interop" (preferred)
	// or "root/interop" while OpenPegasus 2.11 and earlier implemented
	// "root/PG_InterOp" prior to adopting DSP1033.
	private static final String[] InteropNamespaces = { "interop", "root/interop",
			"root/PG_InterOp" };

	private CloseableIterator<CIMObjectPath> enumerateNamespace(String pNamespace) {
		if (pNamespace != null && pNamespace.trim().length() > 0) {
			try {
				CloseableIterator<CIMObjectPath> result = enumerateInstanceNames(new CIMObjectPath(
						null, null, null, pNamespace, "CIM_Namespace", null));
				if (result != null) return result;
			} catch (Exception e) {
				// namespace may not exist
			}
		}
		return null;
	}

	public CloseableIterator<CIMObjectPath> enumerateNamespaces(String pNamespace)
			throws WBEMException {
		CloseableIterator<CIMObjectPath> result;

		// First, try the user's specified namespace (if any)
		if (pNamespace != null && pNamespace.trim().length() > 0) {
			result = enumerateNamespace(pNamespace);
			if (result != null) return result;
		}

		// Second, try the saved interop namespace (if any)
		if (this.iInteropNamespace != null) {
			result = enumerateNamespace(this.iInteropNamespace);
			if (result != null) return result;
			this.iInteropNamespace = null;
		}

		// Finally, try the default interop namespace names
		for (int i = 0; i < InteropNamespaces.length; i++) {
			result = enumerateNamespace(InteropNamespaces[i]);
			if (result != null) {
				if (this.iInteropNamespace == null) this.iInteropNamespace = InteropNamespaces[i];
				return result;
			}
		}

		throw new WBEMException(WBEMException.CIM_ERR_FAILED,
				"Interop namespaces do not exist, CIMOM may not support DSP1033");
	}

	public CloseableIterator<CIMQualifierType<?>> enumerateQualifierTypes(CIMObjectPath pPath)
			throws WBEMException {

		final String operation = "EnumerateQualifiers";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			// enumerateQualifierTypes does not need class name, as it lists all
			// qualifiers in namespace
			if (pPath == null || pPath.getNamespace() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.enumQualifierTypes_request(doc,
					pPath));

			InputStreamReader is = transmitRequest("EnumerateQualifiers", hh, doc);

			CloseableIterator<CIMQualifierType<?>> iter = getIterator(is, pPath);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMInstance> execQuery(CIMObjectPath pPath, String pQuery,
			String pQueryLanguage) throws WBEMException {

		final String operation = "ExecQuery";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pPath == null || pPath.getNamespace() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pPath.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.execQuery_request(doc, pPath,
					pQuery, pQueryLanguage));

			InputStreamReader is = transmitRequest("ExecQuery", hh, doc);

			CloseableIterator<CIMInstance> iter = getIterator(is, pPath);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CIMClass getClass(CIMObjectPath pName, boolean pPropagated, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException {

		final String operation = "GetClass";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pName == null || pName.getNamespace() == null || pName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pName.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.getClass_request(doc, pName,
					pPropagated, pIncludeQualifiers, pIncludeClassOrigin, pPropertyList));

			InputStreamReader is = transmitRequest("GetClass", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pName);
			try {
				if (iter.hasNext()) { return (CIMClass) iter.next(); }
			} finally {
				iter.close();
			}

			return null;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CIMInstance getInstance(CIMObjectPath pName, boolean pPropagated,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException {

		final String operation = "GetInstance";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pName == null || pName.getNamespace() == null || pName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pName.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.getInstance_request(doc, pName,
					pPropagated, false, pIncludeClassOrigin, pPropertyList));

			InputStreamReader is = transmitRequest("GetInstance", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pName);
			try {
				if (iter.hasNext()) {
					CIMInstance result = (CIMInstance) iter.next();
					return new CIMInstance(pName, result.getProperties());
				}
			} finally {
				iter.close();
			}

			return null;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CIMQualifierType<?> getQualifierType(CIMObjectPath pName) throws WBEMException {

		final String operation = "GetQualifier";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pName == null || pName.getNamespace() == null || pName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pName.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.getQualifier_request(doc, pName,
					pName.getObjectName()));

			InputStreamReader is = transmitRequest("GetQualifier", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pName);
			try {
				if (iter.hasNext()) { return (CIMQualifierType<?>) iter.next(); }
			} finally {
				iter.close();
			}

			return null;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public Object invokeMethod(CIMObjectPath pName, String pMethodName,
			CIMArgument<?>[] pInputArguments, CIMArgument<?>[] pOutputArguments)
			throws WBEMException {

		final String operation = "InvokeMethod";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pName == null || pName.getNamespace() == null || pName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(MOF.objectHandle(pName, false, true),
					"UTF-8", "US-ASCII"));

			// sfcb needs pragma set for UpdateExpiredPassword call
			if (pMethodName != null && pMethodName.equalsIgnoreCase("UpdateExpiredPassword")) hh
					.addField("Pragma", "UpdateExpiredPassword");

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.invokeMethod_request(doc, pName,
					pMethodName, pInputArguments));

			InputStreamReader is = transmitRequest(pMethodName, hh, doc);

			CIMResponse response = null;
			String parser = this.iConfiguration.getCimXmlParser();
			// TODO: set the local namespace for parsers
			if (WBEMConstants.SAX.equals(parser) || WBEMConstants.PULL.equals(parser)) { return SAXHelper
					.parseInvokeMethodResponse(is, pOutputArguments, null); }
			// DOM parser
			response = getSingleResponse(is, null);
			response.checkError();
			List<Object> resultSet = response.getFirstReturnValue();

			Object rc = resultSet.size() > 0 ? resultSet.get(0) : null;

			List<Object> outParamValues = response.getParamValues();
			if (pOutputArguments != null && outParamValues != null) {
				Iterator<Object> itr = outParamValues.iterator();
				for (int i = 0; i < pOutputArguments.length; i++)
					if (itr.hasNext()) {
						pOutputArguments[i] = (CIMArgument<?>) itr.next();
					} else {
						break;
					}
			}
			return rc;
		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public boolean isActive() {
		return this.iInitialized && !this.iClosed;
	}

	public void modifyClass(CIMClass pClass) throws WBEMException {

		final String operation = "ModifyClass";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pClass == null || pClass.getObjectPath() == null
					|| pClass.getObjectPath().getNamespace() == null
					|| pClass.getObjectPath().getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pClass.getObjectPath().getNamespace(),
					"UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.setClass_request(doc, pClass
					.getObjectPath(), pClass));

			InputStreamReader is = transmitRequest("ModifyClass", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pClass.getObjectPath());
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}

			return;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public void modifyInstance(CIMInstance pInst, String[] pPropertyList) throws WBEMException {
		final String operation = "ModifyInstance";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();
		checkState();
		if (pInst == null || pInst.getObjectPath() == null
				|| pInst.getObjectPath().getNamespace() == null
				|| pInst.getObjectPath().getObjectName() == null) { throw new WBEMException(
				WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }
		final CIMObjectPath path = pInst.getObjectPath();
		try {
			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(path.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.setInstance_request(doc, path,
					pInst, true, pPropertyList));
			InputStreamReader is = transmitRequest(operation, hh, doc);

			CloseableIterator<?> iter = getIterator(is, path);
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}
			return;
		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMClass> referenceClasses(CIMObjectPath pObjectName,
			String pResultClass, String pRole, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException {

		final String operation = "ReferenceClasses";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.referenceClasses_request(doc,
					pObjectName, pResultClass, pRole, pIncludeQualifiers, pIncludeClassOrigin,
					pPropertyList));

			InputStreamReader is = transmitRequest("References", hh, doc);

			return getIterator(is, pObjectName);

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMInstance> referenceInstances(CIMObjectPath pObjectName,
			String pResultClass, String pRole, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException {

		final String operation = "ReferenceInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.referenceInstances_request(doc,
					pObjectName, pResultClass, pRole, pIncludeClassOrigin, pPropertyList));

			InputStreamReader is = transmitRequest("References", hh, doc);

			return getIterator(is, pObjectName);

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public CloseableIterator<CIMObjectPath> referenceNames(CIMObjectPath pObjectName,
			String pResultClass, String pRole) throws WBEMException {

		final String operation = "ReferenceNames";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.referenceNames_request(doc,
					pObjectName, pResultClass, pRole));

			InputStreamReader is = transmitRequest("ReferenceNames", hh, doc);

			CloseableIterator<CIMObjectPath> iter = getIterator(is, pObjectName);
			return iter;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public void setLocales(Locale[] pLocales) {
		this.iLocales = (pLocales != null && pLocales.length > 0) ? pLocales
				: WBEMConstants.DEFAULT_LOCALES;
	}

	public void setQualifierType(CIMQualifierType<?> pQualifierType) throws WBEMException {

		final String operation = "SetQualifierType";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pQualifierType == null || pQualifierType.getObjectPath() == null
					|| pQualifierType.getObjectPath().getNamespace() == null
					|| pQualifierType.getObjectPath().getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pQualifierType.getObjectPath()
					.getNamespace(), "UTF-8", "US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();
			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.setQualifierType_request(doc,
					pQualifierType.getObjectPath(), pQualifierType));

			InputStreamReader is = transmitRequest("SetQualifierType", hh, doc);

			CloseableIterator<?> iter = getIterator(is, pQualifierType.getObjectPath());
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}

			return;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public SocketFactory getCustomSocketFactory() {
		return this.iConfiguration.getCustomSocketFactory();
	}

	public void setCustomSocketFactory(SocketFactory pFactory) throws UnsupportedOperationException {
		this.iConfiguration.setCustomSocketFactory(pFactory);
	}

	private String getHttpErrorString(int pStatusCode) {
		switch (pStatusCode) {
			case 400:
				return "400 - BAD REQUEST";
			case 401:
				return "401 - UNAUTHORIZED";
			case 403:
				return "403 - FORBIDDEN";
			case 405:
				return "405 - METHOD NOT ALLOWED";
			case 407:
				return "407 - PROXY AUTHENTICATION REQUIRED";
		}
		return null;
	}

	private InputStreamReader transmitRequest(String pCimMethod, HttpHeader pHeader,
			Document pDocument) throws IOException, ProtocolException, WBEMException {
		return transmitRequestWorker(false, this.iUri, this.iHttpClientPool, pCimMethod, pHeader,
				pDocument);
	}

	/*
	 * Worker for both transmitRequest() (pIsIndication == false) and
	 * transmitIndicationRequest() (pIsIndication == true).
	 */
	private InputStreamReader transmitRequestWorker(boolean pIsIndication, URI pRecipient,
			HttpClientPool pClientPool, String pCimMethod, HttpHeader pHeader, Document pDocument)
			throws IOException, ProtocolException, WBEMException {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		HttpUrlConnection connection = null;
		int exceptionNum = WBEMException.CIM_ERR_FAILED;

		// retry HTTP MPOST only if failure is more than 24 hours old
		if (this.iMPostFailed && !pIsIndication) {
			this.iCurrentTime = System.currentTimeMillis();
			if ((this.iCurrentTime - this.iMPostFailTime) > 24 * 60 * 60 * 1000) this.iMPostFailed = false;
		}
		boolean useMPost = this.iMPostFailed || pIsIndication ? false : this.iConfiguration
				.isHttpMPost();
		int retries = this.iConfiguration.getHttpConnectRetriesCount();
		do {
			logger.trace(Level.FINE, "Attempting to connect.. number of attempts left:" + retries);

			// Disconnect existing connection to prevent object leak
			if (connection != null) {
				connection.disconnect();
			}
			connection = newConnection(pIsIndication, pRecipient, pClientPool, pCimMethod, pHeader,
					useMPost);
			try {
				logger.trace(Level.FINE, "Connecting...");
				connection.connect();
			} catch (UnknownHostException e) {
				throw new WBEMException(WBEMException.CIM_ERR_FAILED, "Unknown host", null, e);
			} catch (SocketException e) {
				throw new WBEMException(WBEMException.CIM_ERR_FAILED, "Unable to connect", null, e);
			} catch (SSLHandshakeException e) {
				throw new WBEMException(WBEMException.CIM_ERR_FAILED, "SSL handshake exception",
						null, e);
			}

			OutputStream os = connection.getOutputStream();

			if (this.iConfiguration.isCimXmlTracingEnabled()
					|| LogAndTraceBroker.getBroker().isLoggableCIMXMLTrace(Level.FINEST)) {
				OutputStream pos = new ByteArrayOutputStream();
				CIMClientXML_HelperImpl.dumpDocument(pos, pDocument,
						pIsIndication ? "indication request" : "request");
				OutputStream debugStream = LogAndTraceBroker.getBroker().getXmlTraceStream();
				if (this.iConfiguration.isCimXmlTracingEnabled() && debugStream != null) debugStream
						.write(pos.toString().getBytes());
				if (LogAndTraceBroker.getBroker().isLoggableCIMXMLTrace(Level.FINEST)) logger
						.traceCIMXML(Level.FINEST, pos.toString(), true);
			}
			CIMClientXML_HelperImpl.serialize(os, pDocument);
			os.flush();
			os.close();

			int resultCode = 200;
			try {
				resultCode = connection.getResponseCode();
			} catch (SocketException e) {
				connection.disconnect();
				throw new WBEMException(WBEMException.CIM_ERR_FAILED, "Unable to connect", null, e);
			} catch (SocketTimeoutException e) {
				connection.disconnect();
				throw new WBEMException(WBEMException.CIM_ERR_FAILED, "Connection timed out", null,
						e);
			} catch (SSLHandshakeException e) {
				connection.disconnect();
				throw new WBEMException(WBEMException.CIM_ERR_FAILED, "SSL handshake exception",
						null, e);
			}

			HttpHeader headers = parseHeaders(connection);
			String auth = connection.getRequestProperty("Authorization");
			if (auth != null) {
				this.iAuthorization = auth;
			}

			exceptionNum = WBEMException.CIM_ERR_FAILED;

			switch (resultCode) {
				case HttpURLConnection.HTTP_OK: // 200

					if (this.iConfiguration.isHttpContentLengthRetryEnabled()) {
						String contentLengthField = headers.getField("Content-length");
						if (contentLengthField != null && contentLengthField.trim().length() > 0) {
							int contentLength = Integer.parseInt(contentLengthField);
							int lengthToCheck = this.iConfiguration.getHttpContentLengthThreshold();

							if (contentLength < lengthToCheck) {
								logger.trace(Level.FINE, "Content Length below " + lengthToCheck
										+ ", retrying");
								break;
							}
						}
					}

					String charset = getCharacterSet(headers);

					InputStream is = connection.getInputStream();
					OutputStream debugStream = LogAndTraceBroker.getBroker().getXmlTraceStream();
					if ((this.iConfiguration.isCimXmlTracingEnabled() && debugStream != null)
							|| LogAndTraceBroker.getBroker().isLoggableCIMXMLTrace(Level.FINEST)) {
						is = new DebugInputStream(is, debugStream,
								pIsIndication ? "indication response" : "response");
					}

					return new InputStreamReader(is, charset);

				case HttpURLConnection.HTTP_NOT_IMPLEMENTED: // 501
					// TODO if there is an error with the default xml
					// encoder/decoder, load the correct version
					// The problem is that the CIMOM does not return the DTD
					// version, CIM version or Protocol Version
					// that is expecting
					String cimProtocolVersion = headers.getField("CIMProtocolVersion");
					if (cimProtocolVersion == null && headers.getField("CIMError") == null
							&& useMPost) {
						logger
								.trace(Level.FINER,
										"Received HTTP Error 501 - NOT IMPLEMENTED with M-POST, falling back to POST");
						this.iMPostFailTime = System.currentTimeMillis();
						useMPost = false;
						this.iMPostFailed = true;
						++retries;
						break;
					}

					logger.trace(Level.FINER,
							"Received HTTP Error 501 - NOT IMPLEMENTED, skipping retries");
					retries = 0;
					break;

				case HttpURLConnection.HTTP_BAD_REQUEST: // 400
				case HttpURLConnection.HTTP_FORBIDDEN: // 403
				case HttpURLConnection.HTTP_BAD_METHOD: // 405
					logger.trace(Level.FINER, "Received HTTP Error "
							+ getHttpErrorString(resultCode) + ", skipping retries");
					retries = 0;
					break;

				case HttpURLConnection.HTTP_UNAUTHORIZED: // 401
				case HttpURLConnection.HTTP_PROXY_AUTH: // 407
					logger.trace(Level.FINER, "Received HTTP Error "
							+ getHttpErrorString(resultCode) + ", skipping retries");
					exceptionNum = WBEMException.CIM_ERR_ACCESS_DENIED;
					retries = 0;
					break;

				default:
					if (useMPost) {
						logger.trace(Level.FINER, "Received HTTP Error " + resultCode
								+ " with M-POST, falling back to POST");
						this.iMPostFailTime = System.currentTimeMillis();
						useMPost = false;
						this.iMPostFailed = true;
						++retries;
					} else {
						logger.trace(Level.FINER, "Received HTTP Error " + resultCode
								+ ", retrying");
					}
			}

			// Log HTTP error in XML trace if we are going to retry request
			// (this means there is no response and no exception to indicate
			// result of current request, which makes it look like the client is
			// sending multiple requests without any response)
			if (retries > 0) {
				OutputStream debugStream = LogAndTraceBroker.getBroker().getXmlTraceStream();
				if ((this.iConfiguration.isCimXmlTracingEnabled() && debugStream != null)
						|| LogAndTraceBroker.getBroker().isLoggableCIMXMLTrace(Level.FINEST)) {
					StringBuilder resultStr = new StringBuilder("<--- error response begin ");
					resultStr.append(TimeStamp.formatWithMillis(System.currentTimeMillis()));
					resultStr.append(" ----\nHTTP ");
					resultStr.append(resultCode);
					resultStr.append(' ');
					resultStr.append(connection.getResponseMessage());
					resultStr.append(" (retrying request)\n---- error response end ----->\n");
					if (this.iConfiguration.isCimXmlTracingEnabled() && debugStream != null) debugStream
							.write(resultStr.toString().getBytes());
					if (LogAndTraceBroker.getBroker().isLoggableCIMXMLTrace(Level.FINEST)) logger
							.traceCIMXML(Level.FINEST, resultStr.toString(), false);
				}
			}
		} while (retries-- > 0);

		// Benchmark.stopTransportTimer();

		// Look for CIM error and description, include in exception if exists
		String errorCIM = connection.getHeaderField("CIMError");
		String errorDescriptionCIM = null;
		if (errorCIM != null) {
			logger.trace(Level.FINER, "Found CIMError field with value \"" + errorCIM + "\"");

			errorDescriptionCIM = connection.getHeaderField("CIMErrorDescription");
			if (errorDescriptionCIM != null) {
				try {
					errorDescriptionCIM = URLDecoder.decode(errorDescriptionCIM, "UTF-8");
				} catch (Exception e) {
					errorDescriptionCIM = null;
				}
				logger.trace(Level.FINER, "Found CIMErrorDescription field with value \""
						+ errorDescriptionCIM + "\"");
			}
		}

		// Look for OpenPegasus error details, decode and include in exception
		// if it exists
		String errorOP = connection.getHeaderField("PGErrorDetail");
		if (errorOP != null) {
			try {
				errorOP = URLDecoder.decode(errorOP, "UTF-8");
			} catch (Exception e) {
				errorOP = null;
			}
			logger.trace(Level.FINER, "Found PGErrorDetail field with value \"" + errorOP + "\"");
		}

		// Look for SFCB error details, decode and include in exception
		// if it exists
		String errorSFCB = connection.getHeaderField("SFCBErrorDetail");
		if (errorSFCB != null) {
			try {
				errorSFCB = URLDecoder.decode(errorSFCB, "UTF-8");
			} catch (Exception e) {
				errorSFCB = null;
			}
			logger.trace(Level.FINER, "Found SFCBErrorDetail field with value \"" + errorSFCB
					+ "\"");
		}

		// If CIMErrorDescription present, format of WBEMException is:
		//
		// HTTP StatusCode - ReasonPhrase (CIMError: "ErrorString",
		// CIMErrorDescription: "ErrorString")
		//
		// Otherwise, format of WBEMException message is:
		//
		// HTTP StatusCode - ReasonPhrase (CIMError: "ErrorString", OpenPegasus
		// Error: "ErrorString", SFCB Error: "ErrorString")
		// 
		// For example:
		// HTTP 503 - Service Unavailable (SFCB Error:
		// "Max Session Limit Exceeded")
		StringBuffer errorMsg = new StringBuffer("HTTP ");
		errorMsg.append(connection.getResponseCode());
		errorMsg.append(" - ");
		errorMsg.append(connection.getResponseMessage());
		if (errorCIM != null && errorDescriptionCIM != null) {
			errorMsg.append(" (CIMError: \"");
			errorMsg.append(errorCIM);
			errorMsg.append("\", CIMErrorDescription: \"");
			errorMsg.append(errorDescriptionCIM);
			errorMsg.append("\")");
		} else if (errorCIM != null || errorOP != null || errorSFCB != null) {
			errorMsg.append(" (");
			if (errorCIM != null) {
				errorMsg.append("CIMError: \"");
				errorMsg.append(errorCIM);
				errorMsg.append('"');
			}
			if (errorOP != null) {
				if (errorCIM != null) errorMsg.append(", ");
				errorMsg.append("OpenPegasus Error: \"");
				errorMsg.append(errorOP);
				errorMsg.append('"');
			}
			if (errorSFCB != null) {
				if (errorCIM != null || errorOP != null) errorMsg.append(", ");
				errorMsg.append("SFCB Error: \"");
				errorMsg.append(errorSFCB);
				errorMsg.append('"');
			}
			errorMsg.append(')');
		}

		connection.disconnect();

		throw new WBEMException(exceptionNum, errorMsg.toString());
	}

	private HttpUrlConnection newConnection(boolean pIsIndication, URI pRecipient,
			HttpClientPool pClientPool, String pCimMethod, HttpHeader pHeader, boolean pUseMPost) {

		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		HttpUrlConnection connection = new HttpUrlConnection(pRecipient, pClientPool,
				this.iAuthorizationHandler);
		if (pUseMPost) {
			connection.setRequestMethod(WBEMConstants.HTTP_MPOST);
		} else connection.setRequestMethod(WBEMConstants.HTTP_POST);
		connection.useHttp11("1.1".equals(this.iConfiguration.getHttpVersion()));

		String firstLocaleStr = this.iLocales[0].getLanguage();
		if (this.iLocales[0].getCountry().length() > 0) firstLocaleStr = firstLocaleStr + '-'
				+ this.iLocales[0].getCountry();
		StringBuilder restLocaleStrBld = new StringBuilder("");
		for (int i = 1; i < this.iLocales.length; i++) {
			if (this.iLocales[i] != null && this.iLocales[i].getLanguage().length() > 0) {
				restLocaleStrBld.append(", ");
				restLocaleStrBld.append(this.iLocales[i].getLanguage());
				if (this.iLocales[i].getCountry().length() > 0) {
					restLocaleStrBld.append('-');
					restLocaleStrBld.append(this.iLocales[i].getCountry());
				}
			}
		}

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("Content-type", "application/xml; charset=\"utf-8\"");
		connection.setRequestProperty("Accept", "text/html, text/xml, application/xml");
		connection.setRequestProperty("Cache-Control", "no-cache");
		connection.setRequestProperty("Content-Language", firstLocaleStr);
		connection.setRequestProperty("Accept-Language", firstLocaleStr
				+ restLocaleStrBld.toString() + ", *");
		if (this.iAuthorization != null) connection.setRequestProperty("Authorization",
				this.iAuthorization);

		String prefix = "";
		if (connection.getRequestMethod().equalsIgnoreCase(WBEMConstants.HTTP_MPOST)) {
			String ns = getNextNs();
			connection.setRequestProperty("Man", "http://www.dmtf.org/cim/mapping/http/v1.0;ns="
					+ ns);
			prefix = ns + "-";
		}
		connection.setRequestProperty(prefix + "CIMProtocolVersion", "1.0");

		if (pIsIndication) {
			try {
				connection.setRequestProperty("CIMExport", HttpHeader.encode("MethodRequest",
						"UTF-8", "US-ASCII"));
				connection.setRequestProperty("CIMExportMethod", HttpHeader.encode(
						"ExportIndication", "UTF-8", "US-ASCII"));
			} catch (UnsupportedEncodingException e) {
				logger.trace(Level.FINE, "Exception while encoding http header", e);
				connection.setRequestProperty("CIMExport", "MethodRequest");
				connection.setRequestProperty("CIMExportMethod", "ExportIndication");
			}
		} else {
			connection.setRequestProperty(prefix + "CIMOperation", "MethodCall");
			try {
				connection.setRequestProperty(prefix + "CIMMethod", HttpHeader.encode(pCimMethod,
						"UTF-8", "US-ASCII"));
			} catch (UnsupportedEncodingException e) {
				logger.trace(Level.FINE, "Exception while encoding http header", e);
				connection.setRequestProperty(prefix + "CIMMethod", pCimMethod);
			}
		}
		Iterator<Entry<HeaderEntry, String>> iter = pHeader.iterator();
		while (iter.hasNext()) {
			Entry<HeaderEntry, String> entry = iter.next();
			connection.setRequestProperty(prefix + entry.getKey().toString(), entry.getValue()
					.toString());
		}

		logger.exit();
		return connection;
	}

	private String getNextNs() {
		this.iNsCounter = (this.iNsCounter < 99) ? ++this.iNsCounter : 10;
		return String.valueOf(this.iNsCounter);
	}

	private HttpHeader parseHeaders(URLConnection pConnection) {
		String man = pConnection.getHeaderField("Man");
		String opt = pConnection.getHeaderField("Opt");
		HttpHeader headers = new HttpHeader();
		int i;
		String ns = null;

		HttpHeaderParser manOptHeader = null;
		if (man != null && man.length() > 0) manOptHeader = new HttpHeaderParser(man);
		else if (opt != null && opt.length() > 0) manOptHeader = new HttpHeaderParser(opt);
		if (manOptHeader != null) ns = manOptHeader.getValue("ns");

		if (ns != null) {
			i = 0;
			String key;
			while ((key = pConnection.getHeaderFieldKey(++i)) != null) {
				if (key.startsWith(ns + "-")) headers.addParsedField(key.substring(3), pConnection
						.getHeaderField(i));
				else headers.addParsedField(key, pConnection.getHeaderField(i));
			}
		} else {
			i = 0;
			String key;
			while ((key = pConnection.getHeaderFieldKey(++i)) != null) {
				headers.addParsedField(key, pConnection.getHeaderField(i));
			}
		}

		return headers;
	}

	private String getCharacterSet(HttpHeader pHeader) {
		String contentType = pHeader.getField("Content-type");
		String charset = "UTF-8";
		if (contentType != null && contentType.length() > 0) {
			HttpHeaderParser contentTypeHeader = new HttpHeaderParser(contentType);
			charset = contentTypeHeader.getValue("charset", charset);
		}
		return charset;
	}

	/**
	 * getIterator : get generic CloseableIterator<T>
	 * 
	 * @param <T>
	 *            : Type Parameter
	 * @param pStream
	 *            : Input Stream Reader
	 * @param pPath
	 *            : CIMObjectPath
	 * @return generic CloseableIterator<T>
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws WBEMException
	 */

	// :TODO : try to find solution of "unchecked" warning
	@SuppressWarnings("unchecked")
	private <T> CloseableIterator<T> getIterator(InputStreamReader pStream, CIMObjectPath pPath)
			throws IOException, SAXException, ParserConfigurationException, WBEMException {

		String parser = this.iConfiguration.getCimXmlParser();

		CloseableIterator<?> iter = null;

		if (WBEMConstants.SAX.equals(parser)) {
			iter = new CloseableIteratorSAX(pStream, pPath);
		} else if (WBEMConstants.PULL.equals(parser)) {
			iter = new CloseableIteratorPULL(pStream, pPath);
		} else if (WBEMConstants.DOM.equals(parser)) {
			iter = new CloseableIteratorDOM(pStream, pPath);
		} else {
			throw new IllegalArgumentException("Invalid CIM-XML parser configured (\"" + parser
					+ "\") ");
		}
		try {
			// check for CIMExceptions
			iter.hasNext();
			return (CloseableIterator<T>) iter;
		} catch (RuntimeException e) {
			iter.close();
			if (e.getCause() != null && e.getCause() instanceof WBEMException) { throw (WBEMException) e
					.getCause(); }
			throw e;
		}
	}

	private <T> EnumerateResponse<T> getEnumerateResponse(InputStreamReader pStream,
			CIMObjectPath pPath) throws IOException, SAXException, ParserConfigurationException,
			WBEMException {

		String parser = this.iConfiguration.getCimXmlParser();

		if (WBEMConstants.SAX.equals(parser)) return new EnumerateResponseSAX<T>(pStream, pPath)
				.getEnumResponse();
		else if (WBEMConstants.PULL.equals(parser)) return new EnumerateResponsePULL<T>(pStream,
				pPath).getEnumResponse();
		else if (WBEMConstants.DOM.equals(parser)) return new EnumerateResponseDOM<T>(pStream,
				pPath).getEnumResponse();

		throw new IllegalArgumentException("Invalid CIM-XML parser configured (\"" + parser
				+ "\") ");
	}

	private CIMResponse getSingleResponse(InputStreamReader pStream, CIMObjectPath pLocalPath)
			throws WBEMException {
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom;
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using builder to get DOM representation of the XML file
			dom = db.parse(new InputSource(pStream));
		} catch (TrailerException e) {
			throw e.getWBEMException();
		} catch (Exception e) {
			String msg = "Exception occurred during DOM parsing!";
			logger.trace(Level.SEVERE, msg, e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, msg, null, e);
		}
		CIMXMLParserImpl.setLocalObjectPath(pLocalPath);
		CIMMessage cimMsg;
		try {
			cimMsg = CIMXMLParserImpl.parseCIM(dom.getDocumentElement());
		} catch (CIMXMLParseException e) {
			String msg = "Exception occurred during parseCIM!";
			logger.trace(Level.SEVERE, msg, e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, msg, null, e);
		}
		if (!(cimMsg instanceof CIMResponse)) {
			String msg = "CIM message must be response!";
			logger.trace(Level.SEVERE, msg);
			throw new WBEMException(msg);
		}
		return (CIMResponse) cimMsg;
	}

	public Properties getLocalProperties() {
		return this.iConfiguration.getLocalProperties();
	}

	public void setLocalProperties(Properties pProperties) {
		this.iConfiguration.setLocalProperties(pProperties);
	}

	public void setLocalProperty(String pKey, String pValue) {
		this.iConfiguration.setLocalProperty(pKey, pValue);
	}

	private synchronized void checkState() throws IllegalStateException {
		if (this.iInitialized && !this.iClosed) return;
		String state = this.iClosed ? "closed." : "not initialized.";
		LogAndTraceBroker.getBroker().trace(Level.FINE, "Illegal state for operation: " + state);
		throw new IllegalStateException("WBEMClient is " + state);
	}

	public EnumerateResponse<CIMObjectPath> associatorPaths(CIMObjectPath pObjectName,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "OpenAssociatorInstancePaths";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper
					.OpenAssociatorInstancePaths_request(doc, pObjectName, pAssociationClass,
							pResultClass, pRole, pResultRole, pFilterQueryLanguage, pFilterQuery,
							pTimeout, pContinueOnError, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);

			EnumerateResponse<CIMObjectPath> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMInstance> associators(CIMObjectPath pObjectName,
			String pAssocClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeClassOrigin, String[] pPropertyList, String pFilterQueryLanguage,
			String pFilterQuery, UnsignedInteger32 pTimeout, boolean pContinueOnError,
			UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "OpenAssociatorInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.OpenAssociatorInstances_request(
					doc, pObjectName, pAssocClass, pResultClass, pRole, pResultRole,
					pIncludeClassOrigin, pPropertyList, pFilterQueryLanguage, pFilterQuery,
					pTimeout, pContinueOnError, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);

			EnumerateResponse<CIMInstance> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public void closeEnumeration(CIMObjectPath pObjectName, String pEnumerationContext)
			throws WBEMException {

		final String operation = "CloseEnumeration";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.CloseEnumeration_request(doc,
					pObjectName, pEnumerationContext));

			InputStreamReader is = transmitRequest(operation, hh, doc);

			CloseableIterator<CIMInstance> iter = getIterator(is, pObjectName);

			// Check for exceptions
			try {
				iter.hasNext();
			} finally {
				iter.close();
			}
		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}

	}

	public EnumerateResponse<CIMObjectPath> enumerateInstancePaths(CIMObjectPath pObjectName,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "OpenEnumerateInstancePaths";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper
					.OpenEnumerateInstancePaths_request(doc, pObjectName, pFilterQueryLanguage,
							pFilterQuery, pTimeout, pContinueOnError, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);

			EnumerateResponse<CIMObjectPath> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMInstance> enumerateInstances(CIMObjectPath pObjectName,
			boolean pDeepInheritance, boolean pIncludeClassOrigin, String[] pPropertyList,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "OpenEnumerateInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.OpenEnumerateInstances_request(
					doc, pObjectName, pDeepInheritance, pIncludeClassOrigin, pPropertyList,
					pFilterQueryLanguage, pFilterQuery, pTimeout, pContinueOnError, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			EnumerateResponse<CIMInstance> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	// not supported yet
	public UnsignedInteger64 enumerationCount(CIMObjectPath pObjectName, String pEnumerationContext)
			throws WBEMException {
		final String operation = "EnumerationCount";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.EnumerationCount_request(doc,
					pObjectName, pEnumerationContext));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			// Currently this is not supported by server. Server returns
			// error : "CIM_ERR_NOT_SUPPORTED"
			// This error is caught by parser and exception is thrown

			getEnumerateResponse(is, pObjectName);
			// Error mentioned above will go away if server starts to support
			// EnumerationCount, we need to update code i.e. remove exception
			// and return UnsignedInteger64

			// this exception will be thrown only if server starts to support
			// enumerationCount
			throw new WBEMException(operation + " is currently not supported by client");

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMInstance> execQueryInstances(CIMObjectPath pObjectName,
			String pFilterQuery, String pFilterQueryLanguage, boolean pReturnQueryResultClass,
			UnsignedInteger32 pTimeout, boolean pContinueOnError, UnsignedInteger32 pMaxObjects,
			CIMClass pQueryResultClass) throws WBEMException {

		final String operation = "OpenQueryInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.OpenQueryInstances_request(doc,
					pObjectName, pFilterQuery, pFilterQueryLanguage, pReturnQueryResultClass,
					pTimeout, pContinueOnError, pMaxObjects, pQueryResultClass));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			EnumerateResponse<CIMInstance> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMObjectPath> getInstancePaths(CIMObjectPath pObjectName,
			String pContext, UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "PullInstancePaths";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.PullInstancePaths_request(doc,
					pObjectName, pContext, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			EnumerateResponse<CIMObjectPath> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMInstance> getInstances(CIMObjectPath pObjectName, String pContext,
			UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "PullInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.PullInstances_request(doc,
					pObjectName, pContext, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			EnumerateResponse<CIMInstance> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMInstance> getInstancesWithPath(CIMObjectPath pObjectName,
			String pContext, UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "PullInstancesWithPath";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.PullInstancesWithPath_request(
					doc, pObjectName, pContext, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			EnumerateResponse<CIMInstance> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMObjectPath> referencePaths(CIMObjectPath pObjectName,
			String pResultClass, String pRole, String pFilterQueryLanguage, String pFilterQuery,
			UnsignedInteger32 pTimeout, boolean pContinueOnError, UnsignedInteger32 pMaxObjects)
			throws WBEMException {

		final String operation = "OpenReferenceInstancePaths";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper
					.OpenReferenceInstancePaths_request(doc, pObjectName, pResultClass, pRole,
							pFilterQueryLanguage, pFilterQuery, pTimeout, pContinueOnError,
							pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			EnumerateResponse<CIMObjectPath> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	public EnumerateResponse<CIMInstance> references(CIMObjectPath pObjectName,
			String pResultClass, String pRole, boolean pIncludeClassOrigin, String[] pPropertyList,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException {

		final String operation = "OpenReferenceInstances";
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {

			if (pObjectName == null || pObjectName.getNamespace() == null
					|| pObjectName.getObjectName() == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid object path"); }

			HttpHeader hh = new HttpHeader();
			hh.addField("CIMObject", HttpHeader.encode(pObjectName.getNamespace(), "UTF-8",
					"US-ASCII"));

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.OpenReferenceInstances_request(
					doc, pObjectName, pResultClass, pRole, pIncludeClassOrigin, pPropertyList,
					pFilterQueryLanguage, pFilterQuery, pTimeout, pContinueOnError, pMaxObjects));

			InputStreamReader is = transmitRequest(operation, hh, doc);
			EnumerateResponse<CIMInstance> enumResp = getEnumerateResponse(is, pObjectName);
			return enumResp;

		} catch (WBEMException e) {
			logger.trace(Level.FINE, operation + " request resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger
						.trace(Level.FINE, operation + " request resulted in CIM Error", e
								.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, operation + " request failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}

	private InputStreamReader transmitIndicationRequest(URI pRecipient, HttpClientPool pClientPool,
			HttpHeader pHeader, Document pDocument) throws IOException, ProtocolException,
			WBEMException {
		return transmitRequestWorker(true, pRecipient, pClientPool, null, pHeader, pDocument);
	}

	public boolean sendIndication(URI pRecipient, CIMInstance pIndication) throws WBEMException {
		final LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
		logger.entry();

		checkState();

		try {
			if (pRecipient == null || pRecipient.getScheme() == null
					|| pRecipient.getHost() == null || pRecipient.getPort() <= 0) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER,
					"Invalid recipient URI, must contain valid scheme://host:port"); }

			if (!pRecipient.getScheme().equalsIgnoreCase(WBEMConstants.HTTP)
					&& !pRecipient.getScheme().equalsIgnoreCase(WBEMConstants.HTTPS)) throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid scheme "
							+ pRecipient.getScheme() + ", must be http or https");

			if (pIndication == null) { throw new WBEMException(
					WBEMException.CIM_ERR_INVALID_PARAMETER, "Invalid indication, must not be null"); }

			logger.trace(Level.FINER, "Attempting to send following indication to "
					+ pRecipient.toString() + ":\n" + pIndication.toString());

			HttpHeader hh = new HttpHeader();

			Document doc = this.iXmlHelper.newDocument();

			this.iXmlHelper.createCIMMessage(doc, this.iXmlHelper.sendIndication_request(doc,
					pIndication));

			HttpClientPool indPool = new HttpClientPool(this.iConfiguration);

			InputStreamReader is = transmitIndicationRequest(pRecipient, indPool, hh, doc);

			boolean success = false;
			if (is != null) {
				CIMResponse response = getSingleResponse(is, null);
				success = response != null && response.isSuccessful();
			}

			indPool.closePool();

			return success;
		} catch (WBEMException e) {
			logger.trace(Level.FINE, "Sending indication resulted in CIM Error", e);
			throw e;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WBEMException) {
				logger.trace(Level.FINE, "Sending indication resulted in CIM Error", e.getCause());
				throw (WBEMException) e.getCause();
			}
			logger.trace(Level.FINE, "Sending indication failed", e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, null, null, e);
		} finally {
			logger.exit();
		}
	}
}
