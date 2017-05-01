/**
 * (C) Copyright IBM Corp. 2006, 2009
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
 * 1688273    2007-04-16  ebak         Full support of HTTP trailers
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.http.io;

import javax.wbem.WBEMException;

import org.sblim.cimclient.internal.util.WBEMConstants;

/**
 * TrailerException is thrown by ChunkedInputStream when it receives a http
 * trailer which contains the following entries: CIMStatusCode,
 * CIMStatusCodeDescription. These http trailer entries are known to be used by
 * Pegasus CIMOM.
 */
public class TrailerException extends RuntimeException {

	private static final long serialVersionUID = 4355341648542585509L;

	private WBEMException iWBEMException;

	/**
	 * Ctor.
	 * 
	 * @param pException
	 *            The contained WBEMException
	 */
	public TrailerException(WBEMException pException) {
		super(WBEMConstants.HTTP_TRAILER_STATUS_CODE + ":" + pException.getID() + " "
				+ WBEMConstants.HTTP_TRAILER_STATUS_DESCRIPTION + ":" + pException.getMessage());
		this.iWBEMException = pException;
	}

	/**
	 * getWBEMException
	 * 
	 * @return WBEMException
	 */
	public WBEMException getWBEMException() {
		return this.iWBEMException;
	}

}
