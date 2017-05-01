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

import org.sblim.slp.ServiceLocationException;

/**
 * ServiceAcknowledgment message
 * 
 */
public class ServiceAcknowledgment extends ReplyMessage {

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
		return new ServiceAcknowledgment(pHdr, pInStr.read16());
	}

	/**
	 * Ctor.
	 * 
	 * @param pErrorCode
	 */
	public ServiceAcknowledgment(int pErrorCode) {
		super(SRV_ACK, pErrorCode);
	}

	/**
	 * Ctor.
	 * 
	 * @param pLangTag
	 * @param pErrorCode
	 */
	public ServiceAcknowledgment(String pLangTag, int pErrorCode) {
		super(SRV_ACK, pLangTag, pErrorCode);
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 * @param pErrorCode
	 */
	public ServiceAcknowledgment(MsgHeader pHeader, int pErrorCode) {
		super(pHeader, pErrorCode);
	}

	@Override
	public Iterator<?> getResultIterator() {
		// this message doesn't have iterable results
		return null;
	}

	/**
	 * @param pOption
	 */
	@Override
	protected boolean serializeBody(SLPOutputStream pOutStr, SerializeOption pOption) {
		return pOutStr.write16(getErrorCode());
	}

	@Override
	public Iterator<Exception> getExceptionIterator() {
		// this message doesn't have exception table
		return null;
	}

}
