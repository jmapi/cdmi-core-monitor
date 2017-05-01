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
 * 1565892    2006-11-05  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.cimclient.internal.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class URIString is responsible for wrapping the WBEM-URI string. It has
 * methods which help in parsing.
 */
public class URIString extends Object implements CharSequence {

	/**
	 * Ctor.
	 * 
	 * @param pCharArray
	 * @param pStart
	 * @param pEnd
	 */
	public URIString(char[] pCharArray, int pStart, int pEnd) {
		if (pEnd < pStart) throw new IndexOutOfBoundsException("end:" + pEnd + " < start:" + pStart);
		if (pEnd > pCharArray.length) throw new IndexOutOfBoundsException("charArray.length:"
				+ pCharArray.length + ", start:" + pStart + ", end:" + pEnd + "\n" + "end:" + pEnd
				+ " > charArray.length:" + pCharArray.length);
		this.iCA = pCharArray;
		this.iInitStart = this.iStart = pStart;
		this.iEnd = pEnd;
	}

	/**
	 * Ctor.
	 * 
	 * @param pCharArray
	 */
	public URIString(char[] pCharArray) {
		this(pCharArray, 0, pCharArray.length);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStr
	 */
	public URIString(String pStr) {
		this(pStr.toCharArray());
	}

	/**
	 * Ctor.
	 * 
	 * @param pUriStr
	 */
	public URIString(URIString pUriStr) {
		set(pUriStr);
	}

	/**
	 * set
	 * 
	 * @param pUriStr
	 */
	public void set(URIString pUriStr) {
		this.iCA = pUriStr.iCA;
		this.iStart = pUriStr.iStart;
		this.iEnd = pUriStr.iEnd;
	}

	/**
	 * deepCopy
	 * 
	 * @return <code>URIString</code> instance
	 */
	public URIString deepCopy() {
		return new URIString(this);
	}

	/**
	 * find
	 * 
	 * @param pChar
	 * @return Position of <code>pChar</code> or -1 if not found.
	 */
	public int find(char pChar) {
		for (int i = 0; i < length(); i++)
			if (charAt(i) == pChar) return i;
		return -1;
	}

	/**
	 * @see java.lang.CharSequence#charAt(int)
	 */
	public char charAt(int pIdx) {
		return this.iCA[this.iStart + pIdx];
	}

	/**
	 * @see java.lang.CharSequence#length()
	 */
	public int length() {
		return this.iEnd - this.iStart;
	}

	/**
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	public CharSequence subSequence(int pStart, int pEnd) {
		return new URIString(this.iCA, this.iStart + pStart, this.iStart + pEnd);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new String(this.iCA, this.iStart, length());
	}

	/**
	 * toInitString
	 * 
	 * @return The String which was used for initializing this instance.
	 */
	public String toInitString() {
		return new String(this.iCA, this.iInitStart, this.iEnd - this.iInitStart);
	}

	/**
	 * getPos
	 * 
	 * @return The position of parsing.
	 */
	public int getPos() {
		return this.iStart - this.iInitStart;
	}

	/**
	 * markPosition
	 * 
	 * @return a String which marks the position of parsing.
	 */
	public String markPosition() {
		return markPosition(getPos());
	}

	/**
	 * markPosition
	 * 
	 * @param pPos
	 * @return a String which marks position pPos.
	 */
	public String markPosition(int pPos) {
		StringBuffer buf = new StringBuffer();
		buf.append(toInitString() + '\n');
		for (int i = 0; i < pPos; i++)
			buf.append(' ');
		buf.append("^\n");
		return buf.toString();
	}

	/**
	 * Returns a new string that is a substring of this string. The substring
	 * begins at the specified pBeginIdx and extends to the character at index
	 * pEndIdx - 1. Thus the length of the substring is pEndIdx-pBeginIdx.
	 * 
	 * @param pBeginIdx
	 * @param pEndIdx
	 * @return String
	 */
	public String substring(int pBeginIdx, int pEndIdx) {
		return new String(this.iCA, this.iStart + pBeginIdx, pEndIdx - pBeginIdx);
	}

	/**
	 * startsWith
	 * 
	 * @param pC
	 * @return <code>true</code> if the first character is <code>pC</code>.
	 */
	public boolean startsWith(char pC) {
		if (length() == 0) return false;
		return charAt(0) == pC;
	}

	/**
	 * Cuts out the first character.
	 */
	public void cutStarting() {
		cutStarting(1);
	}

	/**
	 * Cuts out the first character if it is <code>pC</code>.
	 * 
	 * @param pC
	 * @return <code>true</code> if cut is done.
	 */
	public boolean cutStarting(char pC) {
		if (startsWith(pC)) {
			++this.iStart;
			return true;
		}
		return false;
	}

	/**
	 * Cuts out <code>pSeq</code> from the beginning if it is there.
	 * 
	 * @param pSeq
	 * @return <code>true</code> if cut is done.
	 */
	public boolean cutStarting(CharSequence pSeq) {
		if (pSeq.length() > length()) return false;
		for (int i = 0; i < pSeq.length(); i++)
			if (charAt(i) != pSeq.charAt(i)) return false;
		this.iStart += pSeq.length();
		return true;
	}

	/**
	 * Cuts out <code>pStr</code> from the beginning if it is there.
	 * 
	 * @param pStr
	 * @param pIgnoreCase
	 * @return <code>true</code> if cut is done.
	 */
	public boolean cutStarting(String pStr, boolean pIgnoreCase) {
		if (pStr.length() > length()) return false;
		String subStr = substring(0, pStr.length());
		if (pIgnoreCase ? pStr.equalsIgnoreCase(subStr) : pStr.equals(subStr)) {
			this.iStart += pStr.length();
			return true;
		}
		return false;
	}

	/**
	 * Cuts out pChars pieces of characters from the beginning.
	 * 
	 * @param pChars
	 */
	public void cutStarting(int pChars) {
		if (pChars > length()) throw new IndexOutOfBoundsException("chars:" + pChars
				+ " > length()" + length());
		this.iStart += pChars;
	}

	/**
	 * Removes the beginning of the string till the first occurrence of pChar or
	 * removes the whole string if it doesn't contain pChar and pMustFound is
	 * false.
	 * 
	 * @param pChar
	 * @param pRemoveChar
	 *            if <code>true pChar</code> will be removed too
	 * @param pMustFound
	 * @return the removed substring or <code>null</code> if <code>pChar</code>
	 *         not found and <code>pMustFound</code> is <code>true</code>
	 */
	public String removeTill(char pChar, boolean pRemoveChar, boolean pMustFound) {
		int pos = find(pChar);
		if (pos < 0) {
			if (pMustFound) return null;
			pos = length();
			pRemoveChar = false;
		}
		String strVal = substring(0, pos);
		cutStarting(pRemoveChar ? pos + 1 : pos);
		return strVal;
	}

	/**
	 * removeTill(pChar, pRemoveChar, false);
	 * 
	 * @param pChar
	 * @param pRemoveChar
	 * @return String
	 * @see #removeTill(char, boolean, boolean)
	 */
	public String removeTill(char pChar, boolean pRemoveChar) {
		return removeTill(pChar, pRemoveChar, false);
	}

	/**
	 * removeTill(pChar, false, false);
	 * 
	 * @param pChar
	 * @return String
	 * @see #removeTill(char, boolean, boolean)
	 */
	public String removeTill(char pChar) {
		return removeTill(pChar, false);
	}

	/**
	 * Matches pattern <code>pPat</code> and cuts out the beginning till the end
	 * of matcher group <code>pGroup</code>.
	 * 
	 * @param pPat
	 * @param pGroup
	 * @return <code>true</code> if pattern is matched and cut was done.
	 */
	public boolean matchAndCut(Pattern pPat, int pGroup) {
		this.iM = pPat.matcher(new URIString(this));
		if (this.iM.matches()) {
			this.iStart += this.iM.end(pGroup);
			return true;
		}
		return false;
	}

	/**
	 * @param pI
	 * @return pI'th matcher group
	 */
	public String group(int pI) {
		return this.iM.group(pI);
	}

	private char[] iCA;

	private int iInitStart, iStart, iEnd;

	private Matcher iM;

}
