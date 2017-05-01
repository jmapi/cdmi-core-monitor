/**
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
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.http;

import java.io.IOException;

/**
 * Class HttpException represents HTTP related problems
 * 
 */
public class HttpException extends IOException {

	private static final long serialVersionUID = 934925248736106630L;

	int iStatus;

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, null, null)</code>
	 */
	public HttpException() {
		this(-1, null, null);
	}

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, reason, null)</code>
	 * 
	 * @param reason
	 *            The reason
	 */
	public HttpException(String reason) {
		this(-1, reason, null);
	}

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, reason, null)</code>
	 * 
	 * @param status
	 *            The status
	 * @param reason
	 *            The reason
	 */
	public HttpException(int status, String reason) {
		this(status, reason, null);
	}

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, reason, null)</code>
	 * 
	 * @param status
	 *            The status
	 * @param reason
	 *            The reason
	 * @param cimError
	 *            The CIM error
	 */
	public HttpException(int status, String reason, String cimError) {
		super(reason);
		this.iStatus = status;
	}

	/**
	 * Returns the status
	 * 
	 * @return The status
	 */
	public int getStatus() {
		return this.iStatus;
	}

	@Override
	public String toString() {
		return super.toString() + "(status:" + this.iStatus + ")";
	}
}
