/**
 * ServiceLocationException.java
 *
 * (C) Copyright IBM Corp. 2005, 2009
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
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.slp;

/**
 * The ServiceLocationException class is thrown by all methods when exceptional
 * conditions occur in the SLP framework. The error code property determines the
 * exact nature of the condition, and an optional message may provide more
 * information.
 */
public class ServiceLocationException extends Exception {

	private static final long serialVersionUID = 6414188770647750627L;

	/**
	 * OK
	 */
	public static final short OK = 0;

	/**
	 * There is data for the service type in the scope in the AttrRqst or
	 * SrvRqst, but not in the requested language.
	 */
	public static final short LANGUAGE_NOT_SUPPORTED = 1;

	/**
	 * The message fails to obey SLP syntax.
	 */
	public static final short PARSE_ERROR = 2;

	/**
	 * The SrvReg has problems -- e.g., a zero lifetime or an omitted Language
	 * Tag.
	 */
	public static final short INVALID_REGISTRATION = 3;

	/**
	 * The SLP message did not include a scope in its <scope-list> supported by
	 * the SA or DA.
	 */
	public static final short SCOPE_NOT_SUPPORTED = 4;

	/**
	 * The DA or SA receives a request for an unsupported SLP SPI.
	 */
	public static final short AUTHENTICATION_UNKNOWN = 5;

	/**
	 * The DA expected URL and ATTR authentication in the SrvReg and did not
	 * receive it.
	 */
	public static final short AUTHENTICATION_ABSENT = 6;

	/**
	 * The DA detected an authentication error in an Authentication block.
	 */
	public static final short AUTHENTICATION_FAILED = 7;

	/**
	 * Unsupported version number in message header.
	 */
	public static final short VERSION_NOT_SUPPORTED = 9;

	/**
	 * The DA (or SA) is too sick to respond.
	 */
	public static final short INTERNAL_ERROR = 10;

	/**
	 * UA or SA SHOULD retry, using exponential back off.
	 */
	public static final short DA_BUSY = 11;

	/**
	 * The DA (or SA) received an unknown option from the mandatory range (see
	 * section 9.1).
	 */
	public static final short OPTION_NOT_SUPPORTED = 12;

	/**
	 * The DA received a SrvReg without FRESH set, for an unregistered service
	 * or with inconsistent Service Types.
	 */
	public static final short INVALID_UPDATE = 13;

	/**
	 * The SA received an AttrRqst or SrvTypeRqst and does not support it.
	 */
	public static final short REQUEST_NOT_SUPPORTED = 14;

	/**
	 * The SA sent a SrvReg or partial SrvDereg to a DA more frequently than the
	 * DA's min-refresh-interval.
	 */
	public static final short REFRESH_REJECTED = 15;

	/**
	 * NOT_IMPLEMENTED
	 */
	public static final short NOT_IMPLEMENTED = 16;

	/**
	 * NETWORK_INIT_FAILED
	 */
	public static final short NETWORK_INIT_FAILED = 17;

	/**
	 * NETWORK_TIMED_OUT
	 */
	public static final short NETWORK_TIMED_OUT = 18;

	/**
	 * NETWORK_ERROR
	 */
	public static final short NETWORK_ERROR = 19;

	/**
	 * INTERNAL_SYSTEM_ERROR
	 */
	public static final short INTERNAL_SYSTEM_ERROR = 20;

	/**
	 * TYPE_ERROR
	 */
	public static final short TYPE_ERROR = 21;

	/**
	 * BUFFER_OVERFLOW
	 */
	public static final short BUFFER_OVERFLOW = 22;

	/**
	 * PREVIOUS_RESPONDER_OVERFLOW
	 */
	public static final short PREVIOUS_RESPONDER_OVERFLOW = 100;

	private static final String[] ERROR_MESSAGES = { "OK", "LANGUAGE_NOT_SUPPORTED", "PARSE_ERROR",
			"INVALID_REGISTRATION", "SCOPE_NOT_SUPPORTED", "AUTHENTICATION_UNKNOWN",
			"AUTHENTICATION_ABSENT", "AUTHENTICATION_FAILED", "", "VERSION_NOT_SUPPORTED",
			"INTERNAL_ERROR", "DA_BUSY", "OPTION_NOT_SUPPORTED", "INVALID_UPDATE",
			"REQUEST_NOT_SUPPORTED", "REFRESH_REJECTED", "NOT_IMPLEMENTED", "NETWORK_INIT_FAILED",
			"NETWORK_TIMED_OUT", "NETWORK_ERROR", "INTERNAL_SYSTEM_ERROR", "TYPE_ERROR",
			"BUFFER_OVERFLOW" };

	private short iErrorCode;

	/**
	 * Ctor.
	 * 
	 * @param pErrorCode
	 *            One of the ec constants in this class
	 */
	public ServiceLocationException(short pErrorCode) {
		super(ERROR_MESSAGES[pErrorCode]);
		this.iErrorCode = pErrorCode;
	}

	/**
	 * Ctor.
	 * 
	 * @param pErrorCode
	 *            One of the ec constants in this class
	 * @param pCause
	 *            The cause
	 */
	public ServiceLocationException(short pErrorCode, Throwable pCause) {
		super(ERROR_MESSAGES[pErrorCode], pCause);
		this.iErrorCode = pErrorCode;
	}

	/**
	 * Ctor.
	 * 
	 * @param pErrorCode
	 *            One of the ec constants in this class
	 * @param pMessage
	 *            A more specific message
	 * @param pCause
	 * 
	 */
	public ServiceLocationException(short pErrorCode, String pMessage, Throwable pCause) {
		super(ERROR_MESSAGES[pErrorCode] + "(" + pMessage + ")", pCause);
		this.iErrorCode = pErrorCode;
	}

	/**
	 * Ctor.
	 * 
	 * @param pErrorCode
	 *            One of the error code constants in this class
	 * @param pMessage
	 *            A more specific message
	 */
	public ServiceLocationException(short pErrorCode, String pMessage) {
		this(pErrorCode, pMessage, null);
	}

	/**
	 * Return the error code. The error code takes on one of the static field
	 * values.
	 * 
	 * @return The error code
	 */
	public short getErrorCode() {
		return this.iErrorCode;
	}

	/**
	 * Gets the message associated to this exception.
	 * 
	 * @return The message
	 */
	@Override
	public String getMessage() {
		if (getCause() == null) { return super.getMessage(); }
		return super.getMessage() + "; nested exception is: \n\t" + getCause().toString();
	}

}
