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
 * @author : Endre Bak, ebak@de.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-11-08  lupusalex    Make SBLIM client JSR48 compliant
 * 1715482    2007-05-10  lupusalex    CIM_ERR_FAILED thrown when access denied
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2912104    2009-12-10  blaschke-oss Sync up javax.wbem.* with JSR48 1.0.0
 * 2958941    2010-02-25  blaschke-oss Sync up javax.wbem.* javadoc with JSR48 1.0.0
 * 2958990    2010-02-25  blaschke-oss Remove WBEMException.CIM_ERR_TYPE_MISMATCH
 * 2959039    2010-02-25  blaschke-oss Fix WBEMException.toString() logic
 * 3490032    2012-02-21  blaschke-oss TCK: WBEMException must validate error ID
 * 3496301    2012-03-02  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.wbem;

import javax.cim.CIMInstance;

//Sync'd against JSR48 1.0.0 javadoc (build 1.6.0_18) on Thu Mar 01 12:21:26 EST 2012
/**
 * <code>WBEMException</code> is returned when there is a WBEM Operations error.
 * <code>WBEMException</code> includes the following:<br>
 * <ul>
 * <li><code>ID</code> - The <code>ID</code> of the exception</li>
 * <li><code>Description</code> - Brief description of the exception</li>
 * <li><code>CIMInstance[]</code> - Optional array of <code>CIM_Error</code>
 * instances</li>
 * </ul>
 */
public class WBEMException extends Exception {

	private static final long serialVersionUID = 1224653110826327234L;

	/**
	 * Access Denied Exception. Thrown when the principal is not authenticated
	 * or authorized.
	 */
	public static final int CIM_ERR_ACCESS_DENIED = 2;

	/**
	 * Element already exists.
	 */
	public static final int CIM_ERR_ALREADY_EXISTS = 11;

	/**
	 * Class has subclasses. The exception is thrown by the WBEM Server to
	 * disallow invalidation of the subclasses by the super class deletion.
	 * Clients must explicitly delete the subclasses first. The check for
	 * subclasses is made before the check for class instances.
	 */
	public static final int CIM_ERR_CLASS_HAS_CHILDREN = 8;

	/**
	 * Class has instances. The exception is thrown by the WBEM Server to
	 * disallow invalidation of the instances by the class deletion. Clients
	 * must explicitly delete the instances first. The check for subclasses is
	 * made before the check for class instances i.e.
	 * <code>CIM_ERR_CLASS_HAS_CHILDREN</code> is thrown before
	 * <code>CIM_ERR_CLASS_HAS_INSTANCES</code>
	 */
	public static final int CIM_ERR_CLASS_HAS_INSTANCES = 9;

	/**
	 * The WBEM Server does not support continuation on error.
	 */
	public static final int CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED = 26;

	/**
	 * General Exception. If no other error IDs match the error, this one should
	 * be returned.
	 */
	public static final int CIM_ERR_FAILED = 1;

	/**
	 * Using a filter in the enumeration is not supported by the WBEM Server.
	 */
	public static final int CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED = 25;

	/**
	 * Invalid class specified. For e.g. when one tries to add an instance for a
	 * class that does not exist. This error message uses one parameter, the
	 * invalid class name.
	 */
	public static final int CIM_ERR_INVALID_CLASS = 5;

	/**
	 * The enumeration identified by the specified context cannot be found, is
	 * in a closed state, does not exist, or is otherwise invalid.
	 */
	public static final int CIM_ERR_INVALID_ENUMERATION_CONTEXT = 21;

	/**
	 * Invalid namespace Exception. Thrown when the specified namespace does not
	 * exist.
	 */
	public static final int CIM_ERR_INVALID_NAMESPACE = 3;

	/**
	 * The specified operation timeout is not supported by the WBEM Server.
	 */
	public static final int CIM_ERR_INVALID_OPERATION_TIMEOUT = 22;

	/**
	 * Invalid parameter is passed to a method. This error message uses one
	 * parameter, the parameter which caused the exception.
	 */
	public static final int CIM_ERR_INVALID_PARAMETER = 4;

	/**
	 * Invalid query. This error message has two parameters, the invalid snippet
	 * of the query, and additional info with the actual error in the query.
	 */
	public static final int CIM_ERR_INVALID_QUERY = 15;

	/**
	 * The destination is invalid.
	 */
	public static final int CIM_ERR_INVALID_RESPONSE_DESTINATION = 19;

	/**
	 * The super class does not exist.
	 */
	public static final int CIM_ERR_INVALID_SUPERCLASS = 10;

	/**
	 * The method is not available.
	 */
	public static final int CIM_ERR_METHOD_NOT_AVAILABLE = 16;

	/**
	 * The method is not found.
	 */
	public static final int CIM_ERR_METHOD_NOT_FOUND = 17;

	/**
	 * The namespace is not empty.
	 */
	public static final int CIM_ERR_NAMESPACE_NOT_EMPTY = 20;

	/**
	 * The property does not exist in the class/instance being manipulated. This
	 * error message uses one parameter, the name of the property that does not
	 * exist.
	 */
	public static final int CIM_ERR_NO_SUCH_PROPERTY = 12;

	/**
	 * Element cannot be found. This error message uses one parameter, the
	 * element that cannot be found.
	 */
	public static final int CIM_ERR_NOT_FOUND = 6;

	/**
	 * The action is not supported. This can be thrown by a provider or the WBEM
	 * Server itself when it does not support a particular method.
	 */
	public static final int CIM_ERR_NOT_SUPPORTED = 7;

	/**
	 * The attempt to abandon a concurrent Pull operation on the same
	 * enumeration failed, the concurrent Pull operation proceeds normally.
	 */
	public static final int CIM_ERR_PULL_CANNOT_BE_ABANDONED = 24;

	/**
	 * The Pull operation has been abandoned due to execution of a concurrent
	 * CloseEnumeration operation on the same enumeration.
	 */
	public static final int CIM_ERR_PULL_HAS_BEEN_ABANDONED = 23;

	/**
	 * The requested query language is not recognized. This error message has
	 * one parameter, the invalid query language string.
	 */
	public static final int CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED = 14;

	/**
	 * The WBEM Server is in the process of shutting down and cannot process the
	 * operation at this time.
	 */
	public static final int CIM_ERR_SERVER_IS_SHUTTING_DOWN = 28;

	/**
	 * The WBEM Server has failed the operation based upon exceeding server
	 * limits.
	 */
	public static final int CIM_ERR_SERVER_LIMITS_EXCEEDED = 27;

	private static final String[] MESSAGES = new String[] {
	/* 00 */"SUCCESS",
	/* 01 */"CIM_ERR_FAILED",
	/* 02 */"CIM_ERR_ACCESS_DENIED",
	/* 03 */"CIM_ERR_INVALID_NAMESPACE",
	/* 04 */"CIM_ERR_INVALID_PARAMETER",
	/* 05 */"CIM_ERR_INVALID_CLASS",
	/* 06 */"CIM_ERR_NOT_FOUND",
	/* 07 */"CIM_ERR_NOT_SUPPORTED",
	/* 08 */"CIM_ERR_CLASS_HAS_CHILDREN",
	/* 09 */"CIM_ERR_CLASS_HAS_INSTANCES",
	/* 10 */"CIM_ERR_INVALID_SUPERCLASS",
	/* 11 */"CIM_ERR_ALREADY_EXISTS",
	/* 12 */"CIM_ERR_NO_SUCH_PROPERTY",
	/* 13 */null,
	/* 14 */"CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED",
	/* 15 */"CIM_ERR_INVALID_QUERY",
	/* 16 */"CIM_ERR_METHOD_NOT_AVAILABLE",
	/* 17 */"CIM_ERR_METHOD_NOT_FOUND",
	/* 18 */null,
	/* 19 */"CIM_ERR_INVALID_RESPONSE_DESTINATION",
	/* 20 */"CIM_ERR_NAMESPACE_NOT_EMPTY",
	/* 21 */"CIM_ERR_INVALID_ENUMERATION_CONTEXT",
	/* 22 */"CIM_ERR_INVALID_OPERATION_TIMEOUT",
	/* 23 */"CIM_ERR_PULL_HAS_BEEN_ABANDONED",
	/* 24 */"CIM_ERR_PULL_CANNOT_BE_ABANDONED",
	/* 25 */"CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED",
	/* 26 */"CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED",
	/* 27 */"CIM_ERR_SERVER_LIMITS_EXCEEDED",
	/* 28 */"CIM_ERR_SERVER_IS_SHUTTING_DOWN" };

	private int iErrorID;

	private CIMInstance[] iCimErrors;

	/**
	 * Constructs a new exception using the specified ID. The detailed message
	 * will be <code>null</code>.
	 * 
	 * @param pID
	 *            The Error ID to use.
	 * @throws IllegalArgumentException
	 *             If the pID does not match one of the predefined messages.
	 */
	public WBEMException(int pID) {
		this(pID, null, null, null);
	}

	/**
	 * Constructs a new exception using the specified ID and detailed message.
	 * 
	 * @param pID
	 *            The error ID.
	 * @param pMessage
	 *            The detailed message.
	 * @throws IllegalArgumentException
	 *             If the pID does not match one of the predefined messages.
	 */
	public WBEMException(int pID, String pMessage) {
		this(pID, pMessage, null, null);
	}

	/**
	 * Constructs a new exception using the specified ID, detailed message and
	 * CIM_Error instances.
	 * 
	 * @param pID
	 *            The error ID.
	 * @param pMessage
	 *            The detailed message.
	 * @param pErrors
	 *            Array of CIM_Error instances.
	 * @throws IllegalArgumentException
	 *             If the pID does not match one of the predefined messages.
	 */
	public WBEMException(int pID, String pMessage, CIMInstance[] pErrors) {
		this(pID, pMessage, pErrors, null);
	}

	/**
	 * Constructs a new exception using the specified ID, detailed message,
	 * CIM_Error instances and cause.
	 * 
	 * @param pID
	 *            The error ID.
	 * @param pMessage
	 *            The detailed message.
	 * @param pErrors
	 *            Array of CIM_Error instances.
	 * @param pCause
	 *            <code>Throwable</code> cause.
	 * @throws IllegalArgumentException
	 *             If the pID does not match one of the predefined messages.
	 */
	public WBEMException(int pID, String pMessage, CIMInstance[] pErrors, Throwable pCause) {
		super(pMessage, pCause);
		if (!isValidID(pID)) throw new IllegalArgumentException("Invalid error ID!");
		this.iErrorID = pID;
		this.iCimErrors = pErrors;
	}

	/**
	 * Constructs a new exception using the specified detailed message. The
	 * <code>ID</code> will be <code>CIM_ERR_FAILED</code>.
	 * 
	 * @param pMessage
	 *            The detailed message.
	 */
	public WBEMException(String pMessage) {
		this(CIM_ERR_FAILED, pMessage, null, null);
	}

	/**
	 * Get the CIM Error Instances.
	 * 
	 * @return Any CIM Error instances associated with this exception;
	 *         <code>null</code> if none.
	 */
	public CIMInstance[] getCIMErrors() {
		return this.iCimErrors;
	}

	/**
	 * Returns the ID of the error.
	 * 
	 * @return The ID of the error.
	 */
	public int getID() {
		return this.iErrorID;
	}

	/**
	 * Prints out the ID and the optional detailed message.
	 * 
	 * @return A <code>String</code> representation of the exception.
	 */
	@Override
	public String toString() {
		return "WBEMException: " + getCIMMessage()
				+ (super.getMessage() != null ? " (" + super.getMessage() + ")" : "");
	}

	/**
	 * Returns a string representation of the exception ID.
	 * 
	 * @return A <code>String</code> representation of the exception ID.
	 */
	private String getCIMMessage() {
		return isValidID(this.iErrorID) ? MESSAGES[this.iErrorID] : String.valueOf(this.iErrorID);
	}

	/**
	 * Returns validity of error ID.
	 * 
	 * @param pID
	 *            The error ID.
	 * 
	 * @return <code>true</code> if error ID is valid, <code>false</code>
	 *         otherwise.
	 */
	private boolean isValidID(int pID) {
		return pID >= 0 && pID < MESSAGES.length && MESSAGES[pID] != null;
	}
}
