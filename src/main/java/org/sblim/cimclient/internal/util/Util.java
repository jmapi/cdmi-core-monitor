/**
 * (C) Copyright IBM Corp. 2006, 2013
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
 * 1565892    2006-10-18  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3572993    2012-10-01  blaschke-oss parseDouble("2.2250738585072012e-308") DoS vulnerability
 *    2618    2013-02-27  blaschke-oss Need to add property to disable weak cipher suites for the secure indication
 */

package org.sblim.cimclient.internal.util;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Class Util is responsible for storing commonly used static methods.
 */
public class Util {

	/**
	 * Quotes the passed string.
	 * 
	 * @param pStr
	 * @return the quoted string
	 */
	public static String quote(String pStr) {
		StringBuffer dstBuf = new StringBuffer();
		dstBuf.append('"');
		for (int i = 0; i < pStr.length(); i++) {
			char ch = pStr.charAt(i);
			if (ch == '\\' || ch == '"') dstBuf.append('\\');
			dstBuf.append(ch);
		}
		dstBuf.append('"');
		return dstBuf.toString();
	}

	/*
	 * Sun bug 4421494 identifies a range of <code>java.lang.Double</code>
	 * values that will hang the JVM due to an error in
	 * <code>FloatingDecimal.doubleValue()</code> that results in an infinite
	 * loop. The range is defined as (<code>lowBadDouble</code>,
	 * <code>hiBadDouble</code>).
	 */
	private static final BigDecimal lowBadDouble = new BigDecimal(
			"2.225073858507201136057409796709131975934E-308");

	private static final BigDecimal hiBadDouble = new BigDecimal(
			"2.225073858507201259573821257020768020078E-308");

	/**
	 * isBadDoubleString checks if passed string could hang JVM.
	 * 
	 * @param s
	 *            A string to be converted to a Double.
	 * @return <code>true</code> if double is in range of bad values,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isBadDoubleString(String s) {
		BigDecimal val = new BigDecimal(s);
		BigDecimal min = val.min(lowBadDouble);
		BigDecimal max = val.max(hiBadDouble);

		// Do not use string if min < value < max
		return (min.compareTo(val) < 0 && max.compareTo(val) > 0);
	}

	/**
	 * Filters any elements listed in <code>pIgnoreElements</code> out of
	 * <code>pArray</code> and returns the updated array. For example, if
	 * <code>pArray = {"A", "B", "C", "D", "E", "F", "G"}</code> and
	 * <code>pIgnoreElements = "D,E,B"</code> then this method returns
	 * <code>{"A", "C", "F", "G"}</code>.
	 * 
	 * @param pArray
	 *            Original string array.
	 * @param pIgnoreElements
	 *            Comma-separated list of array elements to ignore.
	 * @return Updated string array.
	 */
	public static String[] getFilteredStringArray(String[] pArray, String pIgnoreElements) {
		int i, j;

		if (pArray == null || pArray.length == 0 || pIgnoreElements == null
				|| pIgnoreElements.length() == 0) return pArray;

		Vector<String> vecIgnore = new Vector<String>();
		StringTokenizer strtok = new StringTokenizer(pIgnoreElements, ",");
		while (strtok.hasMoreElements()) {
			String str = ((String) strtok.nextElement()).trim();
			if (str.length() > 0) vecIgnore.add(str);
		}

		if (vecIgnore.size() == 0) return pArray;

		Vector<String> vecNew = new Vector<String>();
		for (i = 0; i < pArray.length; i++) {
			for (j = 0; j < vecIgnore.size(); j++)
				if (pArray[i].equalsIgnoreCase(vecIgnore.elementAt(j))) break;
			if (j >= vecIgnore.size()) vecNew.add(pArray[i]);
		}

		return vecNew.toArray(new String[0]);
	}
}
