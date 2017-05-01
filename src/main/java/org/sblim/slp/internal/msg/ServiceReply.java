/**
 * (C) Copyright IBM Corp. 2007, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 1892103    2008-02-12  ebak         SLP improvements
 * 1949918    2008-04-08  raman_arora  Malformed service URL crashes SLP discovery
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp.internal.msg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceURL;

/*
 * 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Service
 * Location header (function = SrvRply = 2) |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Error
 * Code | URL Entry count |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | <URL
 * Entry 1> ... <URL Entry N> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */
/**
 * ServiceReply message
 * 
 */
public class ServiceReply extends ReplyMessage {

	private List<ServiceURL> iURLEntries;

	private List<Exception> iURLExceptions;

	/**
	 * parse
	 * 
	 * @param pHdr
	 * @param pInStr
	 * @return SLPMessage
	 * @throws ServiceLocationException
	 * @throws IOException
	 */
	public static SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
			throws ServiceLocationException, IOException {
		int errorCode = pInStr.read16();
		ArrayList<Exception> urlExceptions = new ArrayList<Exception>();
		List<ServiceURL> urlEntries = pInStr.readUrlList(urlExceptions);
		return new ServiceReply(pHdr, errorCode, urlEntries, urlExceptions);
	}

	/**
	 * Ctor.
	 * 
	 * @param pErrorCode
	 * @param pURLEntries
	 *            - list of ServiceURLs
	 */
	public ServiceReply(int pErrorCode, List<ServiceURL> pURLEntries) {
		super(SRV_RPLY, pErrorCode);
		this.iURLEntries = pURLEntries;
	}

	/**
	 * Ctor.
	 * 
	 * @param pLangTag
	 * @param pErrorCode
	 * @param pURLEntries
	 *            - list of ServiceURLs
	 * @param pURLExceptions
	 *            - list of URL Exceptions
	 */
	public ServiceReply(String pLangTag, int pErrorCode, List<ServiceURL> pURLEntries,
			List<Exception> pURLExceptions) {
		super(SRV_RPLY, pLangTag, pErrorCode);
		this.iURLEntries = pURLEntries;
		this.iURLExceptions = pURLExceptions;
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 * @param pErrorCode
	 * @param pURLEntries
	 *            - list of ServiceURLs
	 * @param pURLExceptions
	 *            - list of URL Exceptions
	 */
	public ServiceReply(MsgHeader pHeader, int pErrorCode, List<ServiceURL> pURLEntries,
			List<Exception> pURLExceptions) {
		super(pHeader, pErrorCode);
		this.iURLEntries = pURLEntries;
		this.iURLExceptions = pURLExceptions;
	}

	/**
	 * getResultIterator
	 * 
	 * @return iterator of URL Exception list
	 */
	@Override
	public Iterator<ServiceURL> getResultIterator() {
		return this.iURLEntries == null ? null : this.iURLEntries.iterator();
	}

	/**
	 * getExceptionIterator
	 * 
	 * @return iterator of URL Exception list
	 */
	@Override
	public Iterator<Exception> getExceptionIterator() {
		return this.iURLExceptions == null ? null : this.iURLExceptions.iterator();
	}

	/**
	 * getURLEntries
	 * 
	 * @return list of ServiceURLs
	 */
	public List<ServiceURL> getURLEntries() {
		return this.iURLEntries;
	}

	/**
	 * getURLExceptions
	 * 
	 * @return list of URL Exceptions
	 */
	public List<Exception> getURLExceptions() {
		return this.iURLExceptions;
	}

	/**
	 * @param pOption
	 */
	@Override
	protected boolean serializeBody(SLPOutputStream pOutStr, SerializeOption pOption) {
		return pOutStr.write16(getErrorCode()) && pOutStr.writeURLList(this.iURLEntries);
	}

}
