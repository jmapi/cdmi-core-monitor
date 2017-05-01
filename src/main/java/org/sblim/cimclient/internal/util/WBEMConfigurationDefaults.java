/**
 * WBEMConfigurationDefaults.java
 *
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
 * 1565892    2006-11-15  lupusalex    Make SBLIM client JSR48 compliant
 * 1688273    2007-04-19  lupusalex    Full support of HTTP trailers
 * 1815707    2007-10-30  ebak         TLS support
 * 1827728    2007-11-12  ebak         embeddedInstances: attribute EmbeddedObject not set
 * 1848607    2007-12-11  ebak         Strict EmbeddedObject types
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2372030    2008-12-01  blaschke-oss Add property to control synchronized SSL handshaking
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2846231    2009-09-23  rgummada     connection failure on CIMOM w/o user/pw
 * 2884718    2009-10-23  blaschke-oss Merge JSR48 and SBLIM client properties
 * 2930341    2010-01-12  blaschke-oss Sync up WBEMClientConstants with JSR48 1.0.0
 * 2970881    2010-03-15  blaschke-oss Add property to control EmbeddedObject case
 * 3046073    2010-09-07  blaschke-oss Performance hit due to socket conn. creation with timeout
 * 3185763    2011-02-25  blaschke-oss Reliable indication support - Phase 1
 * 3195069    2011-02-28  blaschke-oss Need support to disable SSL Handshake
 * 3206904    2011-05-03  blaschke-oss Indication listener deadlock causes JVM to run out sockets
 * 3459036    2011-12-13  blaschke-oss Linked list for RI queue not efficient for many LDs
 * 3492214    2012-02-23  blaschke-oss Add a SenderIPAddress property indications
 * 3492224    2012-02-23  blaschke-oss Need two different timeouts for Socket connections
 * 3521157    2012-05-10  blaschke-oss JSR48 1.0.0: PROP_ENABLE_*_LOGGING is Level, not 0/1
 * 3536399    2012-08-25  hellerda     Add client/listener peer authentication properties
 * 3572993    2012-10-01  blaschke-oss parseDouble("2.2250738585072012e-308") DoS vulnerability
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
 *    2628    2013-03-26  blaschke-oss Limit size of LinkedList of CIMEvents to be dispatched
 *    2635    2013-05-16  blaschke-oss Slowloris DoS attack for CIM indication listener port
 *    2654    2013-07-29  blaschke-oss Check jcc idle time with CIMOM keepalive timeout to avoid EOF
 *    2151    2013-08-20  blaschke-oss gzip compression not supported
 *    2711    2013-11-13  blaschke-oss LOCALNAMESPACEPATH allows 0 NAMESPACE children
 */

package org.sblim.cimclient.internal.util;

import org.sblim.cimclient.internal.http.WwwAuthInfo;

/**
 * Interface WBEMConfigurationDefaults holds the default values for the
 * configuration properties.
 * 
 */
public interface WBEMConfigurationDefaults {

	/**
	 * CONFIG_URL
	 */
	public static final String[] CONFIG_URL = new String[] { "file:sblim-cim-client2.properties",
			"file:" + System.getProperty("user.home") + "/sblim-cim-client2.properties",
			"file:/etc/java/sblim-cim-client2.properties" };

	/**
	 * LOG_FILE_LEVEL
	 */
	public static final String LOG_FILE_LEVEL = "OFF";

	/**
	 * LOG_FILE_LOCATION
	 */
	public static final String LOG_FILE_LOCATION = "%t/cimclient_log_%g.txt";

	/**
	 * LOG_FILE_SIZE_LIMIT
	 */
	public static final String LOG_FILE_SIZE_LIMIT = "5242880";

	/**
	 * LOG_FILE_COUNT
	 */
	public static final String LOG_FILE_COUNT = "3";

	/**
	 * LOG_CONSOLE_LEVEL
	 */
	public static final String LOG_CONSOLE_LEVEL = "OFF";

	/**
	 * LOG_CONSOLE_TYPE
	 */
	public static final String LOG_CONSOLE_TYPE = WBEMConstants.MESSAGE;

	/**
	 * TRACE_FILE_LEVEL
	 */
	public static final String TRACE_FILE_LEVEL = "OFF";

	/**
	 * TRACE_FILE_LOCATION
	 */
	public static final String TRACE_FILE_LOCATION = "%t/cimclient_trace_%g.txt";

	/**
	 * TRACE_FILE_SIZE_LIMIT
	 */
	public static final String TRACE_FILE_SIZE_LIMIT = "1000000";

	/**
	 * TRACE_FILE_COUNT
	 */
	public static final String TRACE_FILE_COUNT = "5";

	/**
	 * HTTP_TIMEOUT
	 */
	public static final String HTTP_TIMEOUT = "0";

	/**
	 * HTTP_POOL_SIZE
	 */
	public static final String HTTP_POOL_SIZE = "16";

	/**
	 * HTTP_AUTHENTICATION_MODULE
	 */
	public static final String HTTP_AUTHENTICATION_MODULE = WwwAuthInfo.class.getName();

	/**
	 * HTTP_USE_MPOST
	 */
	public static final String HTTP_USE_MPOST = "true";

	/**
	 * HTTP_USE_CHUNKING
	 */
	public static final String HTTP_USE_CHUNKING = "true";

	/**
	 * HTTP_VERSION
	 */
	public static final String HTTP_VERSION = "1.1";

	/**
	 * KEYSTORE_TYPE
	 */
	public static final String KEYSTORE_TYPE = "JKS";

	/**
	 * TRUSTSTORE_TYPE
	 */
	public static final String TRUSTSTORE_TYPE = "JKS";

	/**
	 * HTTP_CONNECTION_RETRIES
	 */
	public static final String HTTP_CONNECTION_RETRIES = "0";

	/**
	 * HTTP_ENABLE_CONTENT_LENGTH_RETRY
	 */
	public static final String HTTP_ENABLE_CONTENT_LENGTH_RETRY = "false";

	/**
	 * HTTP_CONTENT_LENGTH_THRESHOLD
	 */
	public static final String HTTP_CONTENT_LENGTH_THRESHOLD = "50";

	/**
	 * CIMXML_PARSER
	 */
	public static final String CIMXML_PARSER = WBEMConstants.SAX;

	/**
	 * CIMXML_TRACING
	 */
	public static final String CIMXML_TRACING = "false";

	/**
	 * CIMXML_EMBOBJBUILDER
	 */
	public static final String CIMXML_EMBOBJBUILDER = "AttribOnly";

	/**
	 * CIMXML_PARSER_STRICT_EMBOBJ_TYPES
	 */
	public static final String CIMXML_PARSER_STRICT_EMBOBJ_TYPES = "true";

	/**
	 * CIMXML_BUILDER_UPPERCASE_EMBOBJ_ENTITIES
	 */
	public static final String CIMXML_BUILDER_UPPERCASE_EMBOBJ_ENTITIES = "true";

	/**
	 * PERFORM_SSL_HANDSHAKE
	 */
	public static final String PERFORM_SSL_HANDSHAKE = "true";

	/**
	 * SYNCHRONIZED_SSL_HANDSHAKE
	 */
	public static final String SYNCHRONIZED_SSL_HANDSHAKE = "false";

	/**
	 * SOCKET_CONNECT_WITH_TIMEOUT
	 */
	public static final String SOCKET_CONNECT_WITH_TIMEOUT = "true";

	/**
	 * SOCKET_CONNECT_TIMEOUT
	 */
	public static final String SOCKET_CONNECT_TIMEOUT = "0";

	/**
	 * SOCKET_IDLE_TIMEOUT
	 */
	public static final String SOCKET_IDLE_TIMEOUT = "0";

	/**
	 * KEY_CREDENTIALS_DEFAULT_ENABLED
	 */
	public static final String KEY_CREDENTIALS_DEFAULT_ENABLED = "false";

	/**
	 * KEY_DEFAULT_PRINCIPAL
	 */
	public static final String KEY_DEFAULT_PRINCIPAL = "default";

	/**
	 * KEY_DEFAULT_CREDENTIAL
	 */
	public static final String KEY_DEFAULT_CREDENTIAL = "default";

	/**
	 * LISTENER_HTTP_TIMEOUT
	 */
	public static final String LISTENER_HTTP_TIMEOUT = "10000";

	/**
	 * LISTENER_HTTP_HEADER_TIMEOUT
	 */
	public static final String LISTENER_HTTP_HEADER_TIMEOUT = "30000";

	/**
	 * LISTENER_HTTP_MAX_ALLOWED_TIMEOUTS
	 */
	public static final String LISTENER_HTTP_MAX_ALLOWED_TIMEOUTS = "0";

	/**
	 * LISTENER_MAX_POOL_SIZE
	 */
	public static final String LISTENER_MAX_POOL_SIZE = "8";

	/**
	 * LISTENER_MIN_POOL_SIZE
	 */
	public static final String LISTENER_MIN_POOL_SIZE = "2";

	/**
	 * LISTENER_MAX_QUEUE_SIZE
	 */
	public static final String LISTENER_MAX_QUEUE_SIZE = "32";

	/**
	 * LISTENER_BACKLOG
	 */
	public static final String LISTENER_BACKLOG = "2";

	/**
	 * LISTENER_HANDLER_MAX_IDLE
	 */
	public static final String LISTENER_HANDLER_MAX_IDLE = "30000";

	/**
	 * LISTENER_MAX_QUEUED_EVENTS
	 */
	public static final String LISTENER_MAX_QUEUED_EVENTS = "0";

	/**
	 * LISTENER_ENABLE_RELIABLE_INDICATIONS
	 */
	public static final String LISTENER_ENABLE_RELIABLE_INDICATIONS = "false";

	/**
	 * LISTENER_DELIVERY_RETRY_ATTEMPTS
	 */
	public static final String LISTENER_DELIVERY_RETRY_ATTEMPTS = "3";

	/**
	 * LISTENER_DELIVERY_RETRY_INTERVAL
	 */
	public static final String LISTENER_DELIVERY_RETRY_INTERVAL = "20";

	/**
	 * LISTENER_RELIABLE_INDICATION_HASHTABLE_CAPACITY
	 */
	public static final String LISTENER_RELIABLE_INDICATION_HASHTABLE_CAPACITY = "0";

	/**
	 * LISTENER_ADD_SENDER_IP_ADDRESS
	 */
	public static final String LISTENER_ADD_SENDER_IP_ADDRESS = "false";

	/**
	 * SSL_DEFAULT_PROTOCOL
	 */
	public static final String SSL_DEF_PROTOCOL = "SSL";

	/**
	 * SSL_CLIENT_PEER_VERIFICATION
	 */
	public static final String SSL_CLIENT_PEER_VERIFICATION = "false";

	/**
	 * SSL_LISTENER_PEER_VERIFICATION
	 */
	public static final String SSL_LISTENER_PEER_VERIFICATION = "ignore";

	/**
	 * VERIFY_JAVA_LANG_DOUBLE_STRINGS
	 */
	public static final String VERIFY_JAVA_LANG_DOUBLE_STRINGS = "true";

	/**
	 * SYNCHRONIZE_NUMERIC_KEY_DATA_TYPES
	 */
	public static final String SYNCHRONIZE_NUMERIC_KEY_DATA_TYPES = "false";

	/**
	 * ENABLE_GZIP_ENCODING
	 */
	public static final String ENABLE_GZIP_ENCODING = "false";

	/**
	 * CIMXML_PARSER_ALLOW_EMPTY_LOCALNAMESPACEPATH
	 */
	public static final String CIMXML_PARSER_ALLOW_EMPTY_LOCALNAMESPACEPATH = "false";
}
