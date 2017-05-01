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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2210455    2008-10-30  blaschke-oss Enhance javadoc, fix potential null pointers
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 * 2797696    2009-05-27  raman_arora  Input files use unsafe operations
 */

package org.sblim.slp.internal.msg;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sblim.slp.ServiceLocationAttribute;
import org.sblim.slp.ServiceType;
import org.sblim.slp.ServiceURL;
import org.sblim.slp.internal.AttributeHandler;
import org.sblim.slp.internal.Convert;
import org.sblim.slp.internal.TRC;

/**
 * SLPOutputStream helps the building of SLP message bytes
 * 
 */
public class SLPOutputStream {

	private static final int MAX_FIELD_SIZE = 65535;

	private static final byte[] EMPTY_BYTES = new byte[0];

	private ByteArrayOutputStream iOutStr = new ByteArrayOutputStream();

	private int iStreamLimit;

	/**
	 * Ctor.
	 */
	public SLPOutputStream() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStreamLimit
	 */
	public SLPOutputStream(int pStreamLimit) {
		this.iStreamLimit = pStreamLimit <= 0 ? Integer.MAX_VALUE : pStreamLimit;
	}

	/**
	 * size
	 * 
	 * @return int
	 */
	public int size() {
		return this.iOutStr.size();
	}

	/**
	 * freeSpace
	 * 
	 * @return int
	 */
	public int freeSpace() {
		return this.iStreamLimit - this.iOutStr.size();
	}

	/**
	 * toByteArray
	 * 
	 * @return byte[]
	 */
	public byte[] toByteArray() {
		return this.iOutStr.toByteArray();
	}

	/**
	 * write
	 * 
	 * @param pBytes
	 * @return boolean
	 */
	public boolean write(byte[] pBytes) {
		if (freeSpace() < pBytes.length) return false;
		writeNoChk(pBytes);
		return true;
	}

	/**
	 * write
	 * 
	 * @param pServType
	 * @return boolean
	 */
	public boolean write(ServiceType pServType) {
		if (pServType == null) return write((String) null);
		return write(pServType.toString());
	}

	/**
	 * URL_HDR_LENGTH
	 */
	public static final int URL_HDR_LENGTH = 6;

	/*
	 * 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ |
	 * Reserved | Lifetime | URL Length |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ |URL
	 * len, contd.| URL (variable length) \
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ |# of
	 * URL auths | Auth. blocks (if any) \
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 */
	/**
	 * @param pURL
	 * @return boolean
	 */
	public boolean write(ServiceURL pURL) {
		// URL strings in URL entries are not encoded
		String urlStr = pURL.toString();
		byte[] urlStrBytes = Convert.getBytes(urlStr);
		if (freeSpace() < URL_HDR_LENGTH + urlStrBytes.length) return false;
		writeNoChk8(0);
		writeNoChk16(pURL.getLifetime());
		writeNoChk16(urlStrBytes.length);
		writeNoChk(urlStrBytes);
		writeNoChk8(0);
		return true;
	}

	/**
	 * writeURLList
	 * 
	 * @param pURLList
	 * @return boolean
	 */
	@SuppressWarnings("null")
	public boolean writeURLList(List<?> pURLList) {
		int cnt = pURLList == null ? 0 : pURLList.size();
		if (cnt == 0) return write16(cnt);
		SLPOutputStream tmpStr = new SLPOutputStream();
		int i;
		for (i = 0; i < cnt; i++) {
			tmpStr.write((ServiceURL) pURLList.get(i));
			if (freeSpace() < tmpStr.size() + 2) break;
		}
		writeNoChk16(i);
		writeNoChk(tmpStr.toByteArray());
		return i == cnt;
	}

	/**
	 * writeServTypeList
	 * 
	 * @param pServTypeList
	 * @return boolean
	 */
	public boolean writeServTypeList(List<?> pServTypeList) {
		return writeServTypeList(pServTypeList == null ? null : pServTypeList.iterator());
	}

	/**
	 * writeServTypeList
	 * 
	 * @param pServTypeItr
	 * @return boolean
	 */
	public boolean writeServTypeList(Iterator<?> pServTypeItr) {
		if (pServTypeItr == null) return writeStringList((Iterator<String>) null);
		ArrayList<String> servTypeList = new ArrayList<String>();
		while (pServTypeItr.hasNext())
			servTypeList.add(((ServiceType) pServTypeItr.next()).toString());
		return writeStringList(servTypeList);
	}

	/**
	 * writeAttributeList
	 * 
	 * @param pAttrList
	 * @return boolean
	 */
	public boolean writeAttributeList(List<?> pAttrList) {
		return writeAttributeList(pAttrList == null ? null : pAttrList.iterator());
	}

	/**
	 * writeAttributeList
	 * 
	 * @param pAttrItr
	 * @return boolean
	 */
	public boolean writeAttributeList(Iterator<?> pAttrItr) {
		if (pAttrItr == null) return writeStringList((Iterator<String>) null);
		ArrayList<String> attrStrList = new ArrayList<String>();
		while (pAttrItr.hasNext())
			attrStrList.add(AttributeHandler
					.buildString((ServiceLocationAttribute) pAttrItr.next()));
		return writeStringList(attrStrList, null);
	}

	/**
	 * # of AttrAuths |(if present) Attribute Authentication Blocks...
	 * 
	 * @param pAuthBlockList
	 * @return boolean
	 */
	public boolean writeAuthBlockList(List<?> pAuthBlockList) {
		int cnt = pAuthBlockList == null ? 0 : pAuthBlockList.size();
		if (cnt != 0) TRC
				.error("Handling of non empty authentication block list is not implemented!");
		return write8(0);
	}

	/**
	 * write
	 * 
	 * @param pStr
	 * @return boolean
	 */
	public boolean write(String pStr) {
		return write(pStr, Convert.DEFAULT_RESERVED);
	}

	/**
	 * write
	 * 
	 * @param pStr
	 * @param pReservedChars
	 * @return boolean
	 */
	public boolean write(String pStr, String pReservedChars) {
		byte[] bytes = pStr == null ? EMPTY_BYTES : Convert.getBytes(Convert.escape(pStr,
				pReservedChars));
		if (bytes.length > MAX_FIELD_SIZE) return false;
		if (freeSpace() < bytes.length + 2) return false;
		writeNoChk16(bytes.length);
		writeNoChk(bytes);
		return true;
	}

	/**
	 * writeStringList
	 * 
	 * @param pStrList
	 * @return boolean
	 */
	public boolean writeStringList(List<String> pStrList) {
		return writeStringList(pStrList == null ? null : pStrList.iterator());
	}

	/**
	 * writeStringList
	 * 
	 * @param pStrListItr
	 * @return boolean
	 */
	public boolean writeStringList(Iterator<String> pStrListItr) {
		return writeStringList(pStrListItr, Convert.DEFAULT_RESERVED);
	}

	/**
	 * writeStringList
	 * 
	 * @param pStrList
	 * @param pReservedChars
	 * @return boolean
	 */
	public boolean writeStringList(List<String> pStrList, String pReservedChars) {
		return writeStringList(pStrList == null ? null : pStrList.iterator(), pReservedChars);
	}

	/**
	 * writeStringList
	 * 
	 * @param pStrListItr
	 * @param pReservedChars
	 * @return true if all list items are written to the stream, otherwise false
	 */
	public boolean writeStringList(Iterator<String> pStrListItr, String pReservedChars) {
		ByteArrayOutputStream listByteStr = new ByteArrayOutputStream();
		boolean first = true;
		boolean allWritten = true;
		if (pStrListItr != null) while (pStrListItr.hasNext()) {
			StringBuffer strBuf = new StringBuffer();
			String listItemStr = Convert.escape(pStrListItr.next(), pReservedChars);
			if (first) {
				first = false;
			} else {
				strBuf.append(',');
			}
			strBuf.append(listItemStr);
			byte[] listItemBytes = Convert.getBytes(strBuf.toString());
			int currentSize = listByteStr.size() + listItemBytes.length;
			if (currentSize > MAX_FIELD_SIZE || currentSize + 2 > freeSpace()) {
				allWritten = false;
				break;
			}
			listByteStr.write(listItemBytes, 0, listItemBytes.length);
		}
		byte[] listBytes = listByteStr.toByteArray();
		writeNoChk16(listBytes.length);
		writeNoChk(listBytes);
		return allWritten;
	}

	/**
	 * write8
	 * 
	 * @param pValue
	 * @return boolean
	 */
	public boolean write8(int pValue) {
		if (freeSpace() < 1) return false;
		writeNoChk8(pValue);
		return true;
	}

	/**
	 * write16
	 * 
	 * @param pValue
	 * @return boolean
	 */
	public boolean write16(int pValue) {
		if (freeSpace() < 2) return false;
		writeNoChk16(pValue);
		return true;
	}

	/**
	 * write24
	 * 
	 * @param pValue
	 * @return boolean
	 */
	public boolean write24(int pValue) {
		if (freeSpace() < 3) return false;
		writeNoChk24(pValue);
		return true;
	}

	/**
	 * write32
	 * 
	 * @param pValue
	 * @return boolean
	 */
	public boolean write32(long pValue) {
		if (freeSpace() < 4) return false;
		writeNoChk32(pValue);
		return true;
	}

	/**
	 * writeNoChk
	 * 
	 * @param pBytes
	 */
	public void writeNoChk(byte[] pBytes) {
		this.iOutStr.write(pBytes, 0, pBytes.length);
	}

	/**
	 * writeNoChk8
	 * 
	 * @param pValue
	 */
	public void writeNoChk8(int pValue) {
		this.iOutStr.write(pValue);
	}

	/**
	 * writeNoChk16
	 * 
	 * @param pValue
	 */
	public void writeNoChk16(int pValue) {
		this.iOutStr.write((pValue >> 8) & 0xff);
		this.iOutStr.write((pValue & 0xff));
	}

	/**
	 * writeNoChk24
	 * 
	 * @param pValue
	 */
	public void writeNoChk24(int pValue) {
		this.iOutStr.write((pValue >> 16) & 0xff);
		writeNoChk16(pValue);
	}

	/**
	 * writeNoChk32
	 * 
	 * @param pValue
	 */
	public void writeNoChk32(long pValue) {
		this.iOutStr.write((int) ((pValue >> 24) & 0xff));
		writeNoChk24((int) pValue);
	}
}
