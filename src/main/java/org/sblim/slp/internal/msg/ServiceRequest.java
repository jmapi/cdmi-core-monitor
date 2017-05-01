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
 * 1892103    2008-02-15  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp.internal.msg;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceType;

/*
 * 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Service
 * Location header (function = SrvRqst = 1) |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * <PRList> | <PRList> String \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * <service-type> | <service-type> String \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * <scope-list> | <scope-list> String \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * predicate string | Service Request <predicate> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * <SLP SPI> string | <SLP SPI> String \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */

/*
 * The <SLP SPI> string indicates a SLP SPI that the requester has been
 * configured with. If this string is omitted, the responder does not include
 * any Authentication Blocks in its reply. If it is included, the responder MUST
 * return a reply which has an associated authentication block with the SLP SPI
 * in the SrvRqst. If no replies may be returned because the SLP SPI is not
 * supported, the responder returns an AUTHENTICATION_UNKNOWN error.
 */

/**
 * ServiceRequest message
 * 
 */
public class ServiceRequest extends RequestMessage {

	private ServiceType iServiceType;

	private String iPredicate, iSlpSpi;

	private static final int[] ALLOWED_RSPS = { SRV_RPLY, DA_ADVERT, SA_ADVERT };

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
		return new ServiceRequest(pHdr, pInStr.readStringSet(), // prevResponders
				pInStr.readServiceType(), // serviceType
				pInStr.readStringList(), // scopeList
				pInStr.readString(), // predicate
				pInStr.readString() // SlpSpi
		);
	}

	/**
	 * Ctor.
	 * 
	 * @param pLangTag
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pServiceType
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pPredicate
	 * @param pSlpSpi
	 */
	public ServiceRequest(String pLangTag, SortedSet<String> pPrevResponderSet,
			ServiceType pServiceType, List<String> pScopeList, String pPredicate, String pSlpSpi) {
		super(SRV_RQST, pLangTag, pPrevResponderSet, pScopeList);
		init(pServiceType, pPredicate, pSlpSpi);
	}

	/**
	 * Ctor.
	 * 
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pServiceType
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pPredicate
	 * @param pSlpSpi
	 */
	public ServiceRequest(SortedSet<String> pPrevResponderSet, ServiceType pServiceType,
			List<String> pScopeList, String pPredicate, String pSlpSpi) {
		super(SRV_RQST, pPrevResponderSet, pScopeList);
		init(pServiceType, pPredicate, pSlpSpi);
	}

	/**
	 * @param pHeader
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pServiceType
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pPredicate
	 *            - LDAPv3 search filter
	 * @param pSlpSpi
	 */
	public ServiceRequest(MsgHeader pHeader, SortedSet<String> pPrevResponderSet,
			ServiceType pServiceType, List<String> pScopeList, String pPredicate, String pSlpSpi) {
		super(pHeader, pPrevResponderSet, pScopeList);
		init(pServiceType, pPredicate, pSlpSpi);
	}

	/**
	 * getServiceType
	 * 
	 * @return ServiceType
	 */
	public ServiceType getServiceType() {
		return this.iServiceType;
	}

	@Override
	protected boolean serializeRequestBody(SLPOutputStream pOutStr) {
		return pOutStr.write(this.iServiceType) && pOutStr.writeStringList(getScopeList())
				&& pOutStr.write(this.iPredicate) && pOutStr.write(this.iSlpSpi);
	}

	@Override
	protected int[] getAllowedResponseIDs() {
		return ALLOWED_RSPS;
	}

	private void init(ServiceType pServiceType, String pPredicate, String pSlpSpi) {
		this.iServiceType = pServiceType;
		this.iPredicate = pPredicate;
		this.iSlpSpi = pSlpSpi;
	}

}
