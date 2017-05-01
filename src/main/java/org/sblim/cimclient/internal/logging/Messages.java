/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 * 1711092    2006-05-02  lupusalex    Some fixes/additions of log&trace messages
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3536399    2012-08-25  hellerda     Add client/listener peer authentication properties
 */

package org.sblim.cimclient.internal.logging;

/**
 * Interface Messages holds the constants for the message keys.
 * 
 */
public interface Messages {

	/**
	 * GREETING
	 */
	public String GREETING = "CIM0001I";

	/**
	 * RELEASE
	 */
	public String RELEASE = "CIM0002I";

	/**
	 * OS
	 */
	public String OS = "CIM0003I";

	/**
	 * JRE
	 */
	public String JRE = "CIM0004I";

	/**
	 * JVM
	 */
	public String JVM = "CIM0005I";

	/**
	 * CONFIGURATION_URL
	 */
	public String CONFIGURATION_URL = "CIM1001C";

	/**
	 * CONFIGURATION_LOAD_FAILED
	 */
	public String CONFIGURATION_LOAD_FAILED = "CIM1002C";

	/**
	 * EXCEPTION_DURING_CONFIGURATION_LOAD
	 */
	public String EXCEPTION_DURING_CONFIGURATION_LOAD = "CIM1003S";

	/**
	 * INVALID_KEY_IN_CONFIG_FILE
	 */
	public String INVALID_KEY_IN_CONFIG_FILE = "CIM1004W";

	/**
	 * HTTP_PROXY_AUTH_UNSUPPORTED
	 */
	public String HTTP_PROXY_AUTH_UNSUPPORTED = "CIM2001S";

	/**
	 * HTTP_CONNECTION_FAILED
	 */
	public String HTTP_CONNECTION_FAILED = "CIM2002W";

	/**
	 * HTTP_NO_SOCKET_FACTORY
	 */
	public String HTTP_NO_SOCKET_FACTORY = "CIM2003S";

	/**
	 * HTTP_AUTH_MODULE_INVALID
	 */
	public String HTTP_AUTH_MODULE_INVALID = "CIM2004S";

	/**
	 * HTTP_INVALID_HEADER
	 */
	public String HTTP_INVALID_HEADER = "CIM2005W";

	/**
	 * HTTP_HANDLE_CONNECTION_FAILED
	 */
	public String HTTP_HANDLE_CONNECTION_FAILED = "CIM2006S";

	/**
	 * HTTP_PEGASUS_LOCAL_AUTH_READ
	 */
	public String HTTP_PEGASUS_LOCAL_AUTH_READ = "CIM2007S";

	/**
	 * SSL_JSSE_PROVIDER_LOAD_FAILED
	 */
	public String SSL_JSSE_PROVIDER_LOAD_FAILED = "CIM2100S";

	/**
	 * SSL_KEYSTORE_NOT_FOUND
	 */
	public String SSL_KEYSTORE_NOT_FOUND = "CIM2101C";

	/**
	 * SSL_KEYSTORE_NOT_READABLE
	 */
	public String SSL_KEYSTORE_NOT_READABLE = "CIM2102W";

	/**
	 * SSL_KEYSTORE_INVALID_CERT
	 */
	public String SSL_KEYSTORE_INVALID_CERT = "CIM2103W";

	/**
	 * SSL_KEYSTORE_UNRECOVERABLE_KEY
	 */
	public String SSL_KEYSTORE_UNRECOVERABLE_KEY = "CIM2104W";

	/**
	 * SSL_KEYSTORE_INVALID
	 */
	public String SSL_KEYSTORE_INVALID = "CIM2105W";

	/**
	 * SSL_KEYSTORE_OTHER
	 */
	public String SSL_KEYSTORE_OTHER = "CIM2106W";

	/**
	 * SSL_KEYSTORE_FALLBACK
	 */
	public String SSL_KEYSTORE_FALLBACK = "CIM2107C";

	/**
	 * SSL_CONTEXT_INIT_FAILED
	 */
	public String SSL_CONTEXT_INIT_FAILED = "CIM2301S";

	/**
	 * SSL_KEYSTORE_INVALID_ALGORITHM
	 */
	public String SSL_KEYSTORE_INVALID_ALGORITHM = "CIM2108W";

	/**
	 * SSL_KEYSTORE_NULL
	 */
	public String SSL_KEYSTORE_NULL = "CIM2110C";

	/**
	 * SSL_TRUSTSTORE_NOT_FOUND
	 */
	public String SSL_TRUSTSTORE_NOT_FOUND = "CIM2201C";

	/**
	 * SSL_TRUSTSTORE_NOT_READABLE
	 */
	public String SSL_TRUSTSTORE_NOT_READABLE = "CIM2202W";

	/**
	 * SSL_TRUSTSTORE_INVALID_CERT
	 */
	public String SSL_TRUSTSTORE_INVALID_CERT = "CIM2203W";

	/**
	 * SSL_TRUSTSTORE_INVALID
	 */
	public String SSL_TRUSTSTORE_INVALID = "CIM2205W";

	/**
	 * SSL_TRUSTSTORE_OTHER
	 */
	public String SSL_TRUSTSTORE_OTHER = "CIM2206W";

	/**
	 * SSL_TRUSTSTORE_FALLBACK
	 */
	public String SSL_TRUSTSTORE_FALLBACK = "CIM2207C";

	/**
	 * SSL_TRUSTSTORE_INVALID_ALGORITHM
	 */
	public String SSL_TRUSTSTORE_INVALID_ALGORITHM = "CIM2208W";

	/**
	 * SSL_TRUSTSTORE_ACTIVE
	 */
	public String SSL_TRUSTSTORE_ACTIVE = "CIM2209C";

	/**
	 * SSL_TRUSTSTORE_NULL
	 */
	public String SSL_TRUSTSTORE_NULL = "CIM2210C";

	/**
	 * SSL_TRUSTSTORE_INACTIVE
	 */
	public String SSL_TRUSTSTORE_INACTIVE = "CIM2211W";

	/**
	 * SSL_TRUSTSTORE_FALLBACK_NOTRUST
	 */
	public String SSL_TRUSTSTORE_FALLBACK_NOTRUST = "CIM2212W";

	/**
	 * CIM_XMLHELPER_FAILED
	 */
	public String CIM_XMLHELPER_FAILED = "CIM3000S";

	/**
	 * TEST_MESSAGE
	 */
	public String TEST_MESSAGE = "CIM9999S";
}
