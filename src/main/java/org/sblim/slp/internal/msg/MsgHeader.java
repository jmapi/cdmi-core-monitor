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
 */

package org.sblim.slp.internal.msg;

import java.io.IOException;
import java.util.Random;

import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.internal.Convert;

/*
 * SLP Header: 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8
 * 9 0 1 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ |
 * Version | Function-ID | Length |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Length,
 * contd.|O|F|R| reserved |Next Ext Offset|
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Next
 * Extension Offset, contd.| XID |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Language
 * Tag Length | Language Tag \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * 
 * Message Type Abbreviation Function-ID
 * 
 * Service Request SrvRqst 1 Service Reply SrvRply 2 Service Registration SrvReg
 * 3 Service Deregister SrvDeReg 4 Service Acknowledge SrvAck 5 Attribute
 * Request AttrRqst 6 Attribute Reply AttrRply 7 DA Advertisement DAAdvert 8
 * Service Type Request SrvTypeRqst 9 Service Type Reply SrvTypeRply 10 SA
 * Advertisement SAAdvert 11
 * 
 * Length: @2 3 bytes length of the entire SLP message, header included.
 * 
 * Flags: @5 1 byte OVERFLOW (0x80): is set when a message's length exceeds what
 * can fit into a datagram. FRESH (0x40): is set on every new SrvReg. REQUEST
 * MCAST (0x20): is set when multicasting or broadcasting requests.
 * 
 * Reserved: @6 1 byte bits MUST be 0.
 * 
 * Next Extension Offset: @7 3 bytes is set to 0 unless extensions are used. The
 * first extension begins at 'offset' bytes, from the message's beginning. It is
 * placed after the SLP message data.
 * 
 * XID: @10 2 bytes is set to a unique value for each unique request. If the
 * request is retransmitted, the same XID is used. Replies set the XID to the
 * same value as the xid in the request. Only unsolicited DAAdverts are sent
 * with an XID of 0.
 * 
 * Language Tag Length: @12 2 bytes is the length in bytes of the Language Tag
 * field.
 * 
 * Language Tag: @14 The Language Tag in a reply MUST be the same as the
 * Language Tag in the request. This field must be encoded 1*8ALPHA *("-"
 * 1*8ALPHA).
 */

/**
 * MsgHeader
 * 
 */
public class MsgHeader implements FunctionIDs {

	/**
	 * VERSION
	 */
	public static final byte VERSION = 2;

	/**
	 * RAW_HDR_LEN
	 */
	public static final int RAW_HDR_LEN = 14;

	/**
	 * OVERFLOW
	 */
	public static final int OVERFLOW = 0x8000;

	/**
	 * FRESH
	 */
	public static final int FRESH = 0x4000;

	/**
	 * MCAST
	 */
	public static final int MCAST = 0x2000;

	/**
	 * Initialized with a random value, then it is increased by every
	 * getNewXID().
	 */
	private static int cXID = -1;

	private int iVersion, iFunctionID;

	private String iLangTag;

	private boolean iOverflow, iFresh, iMCast;

	/**
	 * XIDs SHOULD be randomly chosen to avoid duplicate XIDs in requests if UAs
	 * restart frequently. This variable is used only for to keep the XID of a
	 * parsed message. serialize() overwrites it.
	 */
	private int iXID;

	/**
	 * parse
	 * 
	 * @param pInStr
	 * @return MsgHeader
	 * @throws ServiceLocationException
	 * @throws IOException
	 */
	public static MsgHeader parse(SLPInputStream pInStr) throws ServiceLocationException,
			IOException {
		int version = pInStr.read8();
		int fnID = pInStr.read8();
		if (fnID < FIRST_ID || fnID > LAST_ID) throw new ServiceLocationException(
				ServiceLocationException.PARSE_ERROR, "functionID:" + fnID + " is not supported!");
		// int len =
		pInStr.read24(); // TODO: could be used for sanity checking
		int flags = pInStr.read16();
		pInStr.read24(); // skip extension
		int XID = pInStr.read16();
		String langTag = pInStr.readString();
		return new MsgHeader(version, fnID, langTag, (flags & OVERFLOW) > 0, (flags & FRESH) > 0,
				(flags & MCAST) > 0, XID);
	}

	/**
	 * Ctor.
	 * 
	 * @param pHdr
	 */
	public MsgHeader(MsgHeader pHdr) {
		this(pHdr.iVersion, pHdr.iFunctionID, pHdr.iLangTag, pHdr.iOverflow, pHdr.iFresh,
				pHdr.iMCast, pHdr.iXID);
	}

	/**
	 * Ctor.
	 * 
	 * @param pVersion
	 * @param pFunctionID
	 * @param pLangTag
	 * @param pOverflow
	 * @param pFresh
	 * @param pMCast
	 * @param pXID
	 */
	public MsgHeader(int pVersion, int pFunctionID, String pLangTag, boolean pOverflow,
			boolean pFresh, boolean pMCast, int pXID) {
		this.iVersion = pVersion;
		this.iFunctionID = pFunctionID;
		this.iLangTag = pLangTag;
		this.iOverflow = pOverflow;
		this.iFresh = pFresh;
		this.iMCast = pMCast;
		this.iXID = pXID;
	}

	/**
	 * getVersion
	 * 
	 * @return int
	 */
	public int getVersion() {
		return this.iVersion;
	}

	/**
	 * getFunctionID
	 * 
	 * @return int
	 */
	public int getFunctionID() {
		return this.iFunctionID;
	}

	/**
	 * getLangTag
	 * 
	 * @return int
	 */
	public String getLangTag() {
		return this.iLangTag;
	}

	/**
	 * overflows
	 * 
	 * @return boolean
	 */
	public boolean overflows() {
		return this.iOverflow;
	}

	/**
	 * fresh
	 * 
	 * @return boolean
	 */
	public boolean fresh() {
		return this.iFresh;
	}

	/**
	 * multicast
	 * 
	 * @return boolean
	 */
	public boolean multicast() {
		return this.iMCast;
	}

	/**
	 * @return the XID which is parsed from the message. serialize() doesn't use
	 *         this value, that serializes a new XID into the stream at every
	 *         call (unless pKeepXID is set).
	 */
	public int getXID() {
		return this.iXID;
	}

	/**
	 * The response have to contain the same XID of the request. So this setter
	 * can be useful.
	 * 
	 * @param pXID
	 */
	public void setXID(int pXID) {
		this.iXID = pXID;
	}

	/**
	 * getSize
	 * 
	 * @return int
	 */
	public int getSize() {
		byte[] langBytes = getLangTagBytes();
		return RAW_HDR_LEN + langBytes.length;
	}

	/*
	 * message body have to be serialized first in order to know length and
	 * MCAST
	 */
	/**
	 * serialize
	 * 
	 * @param pBodyLength
	 * @param pOverflow
	 * @param pSetMultiCastFlag
	 * @param pKeepXID
	 * @return byte[]
	 */
	public byte[] serialize(int pBodyLength, boolean pOverflow, boolean pSetMultiCastFlag,
			boolean pKeepXID) {
		SLPOutputStream outStr = new SLPOutputStream();
		outStr.writeNoChk8(VERSION);
		outStr.writeNoChk8(this.iFunctionID);
		outStr.writeNoChk24(getSize() + pBodyLength);
		int flags = 0;
		if (pOverflow) flags |= OVERFLOW;
		if (this.iFresh) flags |= FRESH;
		if (pSetMultiCastFlag) flags |= MCAST;
		outStr.writeNoChk16(flags);
		outStr.writeNoChk24(0); // skip extension
		if (!pKeepXID) this.iXID = getNewXID();
		outStr.writeNoChk16(this.iXID);
		byte[] langTagBytes = getLangTagBytes();
		outStr.writeNoChk16(langTagBytes.length);
		outStr.writeNoChk(langTagBytes);
		return outStr.toByteArray();
	}

	private byte[] iLangTagBytes;

	private byte[] getLangTagBytes() {
		if (this.iLangTagBytes != null) return this.iLangTagBytes;
		this.iLangTagBytes = Convert.getBytes(this.iLangTag);
		return this.iLangTagBytes;
	}

	/*
	 * XID = 0 is not allowed except for some special case
	 */
	private static int getNewXID() {
		if (cXID < 0) {
			cXID = new Random().nextInt(65536);
			return cXID == 0 ? ++cXID : cXID;
		}
		++cXID;
		cXID &= 0xffff;
		if (cXID == 0) ++cXID;
		return ++cXID;
	}

}
