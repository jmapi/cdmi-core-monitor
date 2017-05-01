/**
 * (C) Copyright IBM Corp. 2006, 2010
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
 * 1565892    2006-10-12  ebak         Make SBLIM client JSR48 compliant
 * 1678807    2007-03-12  ebak         Minor CIMDateTime suggestions
 * 1849235    2008-02-11  blaschke-oss DTStringWriter.writeSigned parameter pDigits is not used
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2989367    2010-04-29  blaschke-oss CIMDateTimeInterval(long) constructor range wrong
 */

package org.sblim.cimclient.internal.cim;

import java.util.Arrays;

/**
 * Class DTStringWriter helps making CIMDateTime Strings.
 */
public class DTStringWriter {

	private StringBuffer iBuf = new StringBuffer();

	/**
	 * writeInt - writes an integer, the upper digits are filled with zeros
	 * 
	 * @param pNum
	 *            -1 means that asterisks have to be printed
	 * @param pDigits
	 */
	public void write(int pDigits, int pNum) {
		if (pDigits <= 0) return;
		if (pNum == -1) {
			char[] cA = new char[pDigits];
			Arrays.fill(cA, '*');
			this.iBuf.append(cA);
			return;
		}
		String str = Integer.toString(pNum);
		int paddingDigits = pDigits - str.length();
		if (paddingDigits > 0) {
			char[] cA = new char[paddingDigits];
			Arrays.fill(cA, '0');
			this.iBuf.append(cA).append(str);
		} else this.iBuf.append(str);
	}

	/**
	 * writeSignedInt - used for utc writing
	 * 
	 * @param pNum
	 * @param pDigits
	 */
	public void writeSigned(int pDigits, int pNum) {
		char sign;
		if (pNum < 0) {
			sign = '-';
			pNum = -pNum;
		} else sign = '+';
		write(sign);
		write(pDigits, pNum);
	}

	/**
	 * writeUSec
	 * 
	 * @param pUSec
	 * @param pUnsignificantDigits
	 */
	public void writeUSec(int pUSec, int pUnsignificantDigits) {
		int digits = 6 - pUnsignificantDigits;
		write(digits, pUSec);
		for (int i = 0; i < pUnsignificantDigits; i++)
			write('*');
	}

	/**
	 * writeChar
	 * 
	 * @param pChar
	 */
	public void write(char pChar) {
		this.iBuf.append(pChar);
	}

	/**
	 * write
	 * 
	 * @param pStr
	 */
	public void write(String pStr) {
		this.iBuf.append(pStr);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.iBuf.toString();
	}
}
