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
 */

package org.sblim.slp.internal.msg;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.internal.SLPConfig;
import org.sblim.slp.internal.SLPDefaults;

/**
 * SLPMessage
 * 
 */
public abstract class SLPMessage implements FunctionIDs {

	// cache it for instance lifetime, do not parse always
	private final int iMaxDatagramSize = SLPConfig.getGlobalCfg().getMTU();

	private MsgHeader iHeader;

	/**
	 * Ctor.
	 * 
	 * @param pFunctionID
	 */
	public SLPMessage(int pFunctionID) {
		this(pFunctionID, Util.getLangTag(SLPDefaults.LOCALE));
	}

	/**
	 * Ctor.
	 * 
	 * @param pFunctionID
	 * @param pLangTag
	 */
	public SLPMessage(int pFunctionID, String pLangTag) {
		this(new MsgHeader(MsgHeader.VERSION, pFunctionID, pLangTag, false, pFunctionID == SRV_REG,
				false, 0));
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 */
	public SLPMessage(MsgHeader pHeader) {
		this.iHeader = pHeader;
	}

	/**
	 * getHeader
	 * 
	 * @return MsgHeader
	 */
	public MsgHeader getHeader() {
		return this.iHeader;
	}

	/**
	 * getVersion
	 * 
	 * @return int
	 */
	public int getVersion() {
		return this.iHeader.getVersion();
	}

	/**
	 * getFunctionID
	 * 
	 * @return int
	 */
	public int getFunctionID() {
		return this.iHeader.getFunctionID();
	}

	/**
	 * getLangTag
	 * 
	 * @return String
	 */
	public String getLangTag() {
		return this.iHeader.getLangTag();
	}

	/**
	 * overflows
	 * 
	 * @return boolean
	 */
	public boolean overflows() {
		return this.iHeader.overflows();
	}

	/**
	 * fresh
	 * 
	 * @return boolean
	 */
	public boolean fresh() {
		return this.iHeader.fresh();
	}

	/**
	 * multicast
	 * 
	 * @return boolean
	 */
	public boolean multicast() {
		return this.iHeader.multicast();
	}

	/**
	 * getXID
	 * 
	 * @return int
	 */
	public int getXID() {
		return this.iHeader.getXID();
	}

	/**
	 * @see MsgHeader#setXID(int)
	 * @param pXID
	 */
	public void setXID(int pXID) {
		this.iHeader.setXID(pXID);
	}

	/**
	 * serialize
	 * 
	 * @param pSetMulticastFlag
	 * @param pDatagramLimited
	 * @param pKeepXID
	 * @return byte[]
	 * @throws ServiceLocationException
	 */
	public byte[] serialize(boolean pSetMulticastFlag, boolean pDatagramLimited, boolean pKeepXID)
			throws ServiceLocationException {
		return serialize(pSetMulticastFlag, pDatagramLimited, pKeepXID, null);
	}

	/**
	 * serialize
	 * 
	 * @param pSetMulticastFlag
	 * @param pDatagramLimited
	 * @param pKeepXID
	 * @param pOption
	 *            - passed to the serializeBody() method of the inherited
	 *            classes
	 * @return byte[]
	 * @throws ServiceLocationException
	 */
	public byte[] serialize(boolean pSetMulticastFlag, boolean pDatagramLimited, boolean pKeepXID,
			SerializeOption pOption) throws ServiceLocationException {
		SLPOutputStream bodyOutStr = new SLPOutputStream(pDatagramLimited ? this.iMaxDatagramSize
				- this.iHeader.getSize() : -1);
		boolean fit = serializeBody(bodyOutStr, pOption);
		byte[] bodyBytes = bodyOutStr.toByteArray();
		byte[] headerBytes = this.iHeader.serialize(bodyBytes.length, !fit, pSetMulticastFlag,
				pKeepXID);
		byte[] bytes = new byte[headerBytes.length + bodyBytes.length];
		System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
		System.arraycopy(bodyBytes, 0, bytes, headerBytes.length, bodyBytes.length);
		return bytes;
	}

	/**
	 * @return true if all data is written into pOStr (all data fits into size
	 *         limit)
	 */
	protected abstract boolean serializeBody(SLPOutputStream pOutStr, SerializeOption pOption)
			throws ServiceLocationException;

	@Override
	public String toString() {
		return super.toString() + " " + getXID();
	}

}
