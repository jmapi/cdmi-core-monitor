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

/*
 * 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Service
 * Location header (function = SrvTypeRqst = 9) |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * PRList | <PRList> String \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * Naming Authority | <Naming Authority String> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * <scope-list> | <scope-list> String \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */
/**
 * ServiceTypeRequest message
 * 
 */
public class ServiceTypeRequest extends RequestMessage {

	private String iNamingAuth;

	private static final int[] ALLOWED_RSPS = { SRV_TYPE_RPLY };

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
		return new ServiceTypeRequest(pHdr, pInStr.readStringSet(), // prevResponderSet
				pInStr.readString(), // naming authority
				pInStr.readStringList() // scope list
		);
	}

	/**
	 * Ctor.
	 * 
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pNamingAuth
	 * @param pScopeList
	 *            - set of scope strings
	 */
	public ServiceTypeRequest(SortedSet<String> pPrevResponderSet, String pNamingAuth,
			List<String> pScopeList) {
		super(SRV_TYPE_RQST, pPrevResponderSet, pScopeList);
		init(pNamingAuth);
	}

	/**
	 * Ctor.
	 * 
	 * @param pLangTag
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pNamingAuth
	 * @param pScopeList
	 *            - set of scope strings
	 */
	public ServiceTypeRequest(String pLangTag, SortedSet<String> pPrevResponderSet,
			String pNamingAuth, List<String> pScopeList) {
		super(SRV_TYPE_RQST, pLangTag, pPrevResponderSet, pScopeList);
		init(pNamingAuth);
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pNamingAuth
	 * @param pScopeList
	 *            - set of scope strings
	 */
	public ServiceTypeRequest(MsgHeader pHeader, SortedSet<String> pPrevResponderSet,
			String pNamingAuth, List<String> pScopeList) {
		super(pHeader, pPrevResponderSet, pScopeList);
		init(pNamingAuth);
	}

	@Override
	protected boolean serializeRequestBody(SLPOutputStream pOutStr) {
		return pOutStr.write(this.iNamingAuth) && pOutStr.writeStringList(getScopeList());
	}

	@Override
	protected int[] getAllowedResponseIDs() {
		return ALLOWED_RSPS;
	}

	private void init(String pNamingAuth) {
		this.iNamingAuth = pNamingAuth;
	}

}
