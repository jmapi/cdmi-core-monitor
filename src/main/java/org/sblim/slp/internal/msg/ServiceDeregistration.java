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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp.internal.msg;

import java.io.IOException;
import java.util.List;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceURL;

/*
 * 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Service
 * Location header (function = SrvDeReg = 4) |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Length of
 * <scope-list> | <scope-list> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | URL Entry \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Length of
 * <tag-list> | <tag-list> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ The
 * <tag-list> is a <string-list> of attribute tags to deregister as defined in
 * Section 9.4. If no <tag-list> is present, the SrvDeReg deregisters the
 * service in all languages it has been registered in. If the <tag-list> is
 * present, the SrvDeReg deregisters the attributes whose tags are listed in the
 * tag spec. Services registered with Authentication Blocks MUST NOT include a
 * <tag-list> in a SrvDeReg message: A DA will respond with an
 * AUTHENTICATION_FAILED error in this case.
 */

/**
 * ServiceDeregistration message
 * 
 */
public class ServiceDeregistration extends SLPMessage {

	private List<String> iScopeList;

	private ServiceURL iURL;

	private List<String> iTagList;

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
		return new ServiceDeregistration(pHdr, pInStr.readStringList(), pInStr.readURL(), pInStr
				.readStringList());
	}

	/**
	 * Ctor.
	 * 
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pURL
	 * @param pTagList
	 */
	public ServiceDeregistration(List<String> pScopeList, ServiceURL pURL, List<String> pTagList) {
		super(SRV_DEREG);
		init(pScopeList, pURL, pTagList);
	}

	/**
	 * Ctor.
	 * 
	 * @param pLangTag
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pURL
	 * @param pTagList
	 */
	public ServiceDeregistration(String pLangTag, List<String> pScopeList, ServiceURL pURL,
			List<String> pTagList) {
		super(SRV_DEREG, pLangTag);
		init(pScopeList, pURL, pTagList);
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pURL
	 * @param pTagList
	 */
	public ServiceDeregistration(MsgHeader pHeader, List<String> pScopeList, ServiceURL pURL,
			List<String> pTagList) {
		super(pHeader);
		init(pScopeList, pURL, pTagList);
	}

	/**
	 * getServiceURL
	 * 
	 * @return ServiceURL
	 */
	public ServiceURL getServiceURL() {
		return this.iURL;
	}

	/**
	 * @param pOption
	 */
	@Override
	protected boolean serializeBody(SLPOutputStream pOutStr, SerializeOption pOption) {
		return pOutStr.writeStringList(this.iScopeList) && pOutStr.write(this.iURL)
				&& pOutStr.writeStringList(this.iTagList);
	}

	private void init(List<String> pScopeList, ServiceURL pURL, List<String> pTagList) {
		this.iScopeList = pScopeList;
		this.iURL = pURL;
		this.iTagList = pTagList;
	}

}
