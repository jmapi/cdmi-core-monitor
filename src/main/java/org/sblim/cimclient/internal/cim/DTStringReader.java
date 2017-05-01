/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3526675    2012-05-14  blaschke-oss Unit test fails on Java 7
 */

package org.sblim.cimclient.internal.cim;

import java.io.IOException;
import java.io.StringReader;

/**
 * Class DTString helps parsing CIMDateTime Strings.
 * 
 */
public class DTStringReader {

	private String iDateTimeStr;

	private StringReader iReader;

	private int iPos = 0;

	private boolean iUnsignificant;

	/**
	 * Ctor.
	 * 
	 * @param pDateTimeStr
	 */
	public DTStringReader(String pDateTimeStr) {
		this.iDateTimeStr = pDateTimeStr;
		this.iReader = new StringReader(pDateTimeStr);
	}

	/**
	 * read
	 * 
	 * @param pLen
	 *            - number of characters to be read from the string
	 * @param pFieldName
	 *            - the name of the field which is to be read (e.g. year, month,
	 *            day ...)
	 * @param pAllowUnsignificant
	 * @return int
	 * @throws IllegalArgumentException
	 */
	public int read(int pLen, String pFieldName, boolean pAllowUnsignificant)
			throws IllegalArgumentException {
		char[] buf = new char[pLen];
		int read;
		try {
			read = this.iReader.read(buf);
			this.iPos += pLen;
		} catch (IOException e) {
			String msg = "Failed to read " + pFieldName + " field from " + this.iDateTimeStr + '!';
			throw new IllegalArgumentException(msg);
		}
		if (read != pLen) {
			String msg = "Length of " + pFieldName + " field should be " + pLen + " but only"
					+ read + " characters could be read!";
			throw new IllegalArgumentException(msg);
		}
		// Not significant check
		if (pAllowUnsignificant) {
			int cnt = 0;
			for (int i = 0; i < buf.length; i++)
				if (buf[i] == '*') ++cnt;
			if (cnt == buf.length) {
				this.iUnsignificant = true;
				return -1;
			}
		}
		this.iUnsignificant = false;
		String field = new String(buf);
		int res;
		try {
			res = Integer.parseInt(field);
		} catch (NumberFormatException e) {
			String msg = "Illegal " + pFieldName + " field \"" + field + "\" in \""
					+ this.iDateTimeStr + "\"!";
			throw new IllegalArgumentException(msg);
		}
		if (res < 0) throw new IllegalArgumentException("Negative value is not allowed for "
				+ pFieldName + " in " + this.iDateTimeStr + "!");
		// Java 7 parseInt began allowing plus sign
		if (field.indexOf('+') != -1) throw new IllegalArgumentException(
				"Plus sign is not allowed for " + pFieldName + " in " + this.iDateTimeStr + "!");
		return res;
	}

	/**
	 * readAndCheck
	 * 
	 * @param pLen
	 *            - number of digits to read
	 * @param pFieldName
	 *            - the name of the field which is to be read (e.g. year, month,
	 *            day...)
	 * @param pMin
	 *            - the allowed minimum value (-1 is always allowed as not
	 *            significant)
	 * @param pMax
	 *            - the allowed maximum value
	 * @param pAllowUnsignificant
	 * @return int
	 * @throws IllegalArgumentException
	 */
	public int readAndCheck(int pLen, String pFieldName, int pMin, int pMax,
			boolean pAllowUnsignificant) throws IllegalArgumentException {
		int val = read(pLen, pFieldName, pAllowUnsignificant);
		if (pAllowUnsignificant && val == -1) return val;
		if (val < pMin || val > pMax) throw new IllegalArgumentException(pFieldName
				+ " must be between " + pMin + " and " + pMax + ", but " + val + " was read from "
				+ this.iDateTimeStr + " !");
		return val;
	}

	/**
	 * read
	 * 
	 * @return a char, 0 if failed
	 */
	public char read() {
		try {
			int i = this.iReader.read();
			if (i > 0) {
				++this.iPos;
				return (char) i;
			}
			return 0;
		} catch (IOException e) {
			return 0;
		}
	}

	/**
	 * read - Throws an IllegalArgumentException if the read character is not c.
	 * 
	 * @param c
	 *            - contains the character which should be read from the String.
	 * @throws IllegalArgumentException
	 */
	public void read(char c) throws IllegalArgumentException {
		if (read() != c) {
			String msg = "'" + c + "' expected at position " + getPos() + " in "
					+ this.iDateTimeStr + "!";
			throw new IllegalArgumentException(msg);
		}
	}

	/**
	 * getPos
	 * 
	 * @return the position in the reader
	 */
	public int getPos() {
		return this.iPos;
	}

	/**
	 * isUnsignificant
	 * 
	 * @return boolean
	 */
	public boolean isUnsignificant() {
		return this.iUnsignificant;
	}

}
