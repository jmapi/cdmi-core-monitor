/**
 * WBEMConstants.java
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
 * 1565892    2006-11-08  lupusalex    Make SBLIM client JSR48 compliant
 * 1671502    2007-02-28  lupusalex    Remove dependency from Xerces
 * 1688273    2007-04-19  lupusalex    Full support of HTTP trailers
 * 1710066    2007-04-30  lupsualex    LocalAuth fails for z/OS Pegasus
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 3525138    2012-05-09  blaschke-oss Remove WBEMConstants.PROTOCOL_CIMXML
 *    2619    2013-02-22  blaschke-oss Host should contain port when not 5988/5989
 *    2635    2013-05-16  blaschke-oss Slowloris DoS attack for CIM indication listener port
 */

package org.sblim.cimclient.internal.util;

import java.util.Locale;

/**
 * Class WBEMConstants contains global constants of the CIM client.
 */
public interface WBEMConstants {

	/**
	 * The HTTP protocol
	 */
	public static final String HTTP = "HTTP";

	/**
	 * The HTTPS protocol
	 */
	public static final String HTTPS = "HTTPS";

	/**
	 * The default (insecure) WBEM port
	 */
	public static final int DEFAULT_WBEM_PORT = 5988;

	/**
	 * The default secure WBEM port
	 */
	public static final int DEFAULT_WBEM_SECURE_PORT = 5989;

	/**
	 * The CIMOM path
	 */
	public static final String CIMOM_PATH = "/cimom";

	/**
	 * Platform default locales
	 */
	public static final Locale[] DEFAULT_LOCALES = new Locale[] { Locale.getDefault() };

	/**
	 * HTTP MPOST
	 */
	public static final String HTTP_MPOST = "M-POST";

	/**
	 * HTTP POST
	 */
	public static final String HTTP_POST = "POST";

	/**
	 * MESSAGE
	 */
	public static final String MESSAGE = "MESSAGE";

	/**
	 * TRACE
	 */
	public static final String TRACE = "TRACE";

	/**
	 * DOM
	 */
	public static final String DOM = "DOM";

	/**
	 * PULL
	 */
	public static final String PULL = "PULL";

	/**
	 * SAX
	 */
	public static final String SAX = "SAX";

	/**
	 * UTF-8
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * The minimum value of a Unicode high-surrogate code unit in the UTF-16
	 * encoding.
	 */
	public static final char UTF16_MIN_HIGH_SURROGATE = '\uD800';

	/**
	 * The minimum value of a Unicode low-surrogate code unit in the UTF-16
	 * encoding.
	 */
	public static final char UTF16_MIN_LOW_SURROGATE = '\uDC00';

	/**
	 * The maximum value of a Unicode high-surrogate code unit in the UTF-16
	 * encoding.
	 */
	public static final char UTF16_MAX_HIGH_SURROGATE = '\uDBFF';

	/**
	 * The maximum value of a Unicode low-surrogate code unit in the UTF-16
	 * encoding.
	 */
	public static final char UTF16_MAX_LOW_SURROGATE = '\uDFFF';

	/**
	 * The minimum value of a supplementary code point.
	 */
	public static final int UNICODE_MIN_SUPPLEMENTARY_CODE_POINT = 0x010000;

	/**
	 * The maximum value of a Unicode code point.
	 */
	public static final int UNICODE_MAX_CODE_POINT = 0x10ffff;

	/**
	 * Status code field in HTTP trailer
	 */
	public static final String HTTP_TRAILER_STATUS_CODE = "CIMStatusCode";

	/**
	 * Status description field in HTTP trailer
	 */
	public static final String HTTP_TRAILER_STATUS_DESCRIPTION = "CIMStatusCodeDescription";

	/**
	 * ISO-8859-1 character encoding
	 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/**
	 * System property containing the operating system's name
	 */
	public static final String OS_NAME = "os.name";

	/**
	 * Operating system name as found on z/OS
	 */
	public static final String Z_OS = "z/OS";

	/**
	 * Exception message for indication sender potential DoS
	 */
	public static final String INDICATION_DOS_EXCEPTION_MESSAGE = "Indication sender taking too long, possible DoS underway?";
}
