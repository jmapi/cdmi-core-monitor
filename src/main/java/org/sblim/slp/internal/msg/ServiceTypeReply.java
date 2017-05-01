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
import java.util.Iterator;
import java.util.List;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceType;

/*
 * 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Service
 * Location header (function = SrvTypeRply = 10) |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Error
 * Code | length of <srvType-list> |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ |
 * <srvtype--list> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */
/**
 * ServiceTypeReply message
 * 
 */
public class ServiceTypeReply extends ReplyMessage {

	private List<ServiceType> iServTypeList;

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
		return new ServiceTypeReply(pHdr, pInStr.read16(), pInStr.readServTypeList());
	}

	/**
	 * Ctor.
	 * 
	 * @param pErrorCode
	 * @param pServTypeList
	 *            - list of ServiceTypes
	 */
	public ServiceTypeReply(int pErrorCode, List<ServiceType> pServTypeList) {
		super(SRV_TYPE_RPLY, pErrorCode);
		this.iServTypeList = pServTypeList;
	}

	/**
	 * Ctor.
	 * 
	 * @param pLangTag
	 * @param pErrorCode
	 * @param pServTypeList
	 *            - list of ServiceTypes
	 */
	public ServiceTypeReply(String pLangTag, int pErrorCode, List<ServiceType> pServTypeList) {
		super(SRV_TYPE_RPLY, pLangTag, pErrorCode);
		this.iServTypeList = pServTypeList;
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 * @param pErrorCode
	 * @param pServTypeList
	 *            - list of ServiceTypes
	 */
	public ServiceTypeReply(MsgHeader pHeader, int pErrorCode, List<ServiceType> pServTypeList) {
		super(pHeader, pErrorCode);
		this.iServTypeList = pServTypeList;
	}

	@Override
	public Iterator<ServiceType> getResultIterator() {
		return this.iServTypeList == null ? null : this.iServTypeList.iterator();
	}

	/**
	 * @param pOption
	 */
	@Override
	protected boolean serializeBody(SLPOutputStream pOutStr, SerializeOption pOption) {
		return pOutStr.write16(getErrorCode()) && pOutStr.writeServTypeList(getResultIterator());
	}

	@Override
	public Iterator<Exception> getExceptionIterator() {
		// this message doesn't have exception table
		return null;
	}

}
