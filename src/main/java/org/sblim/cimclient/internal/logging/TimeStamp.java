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
 * @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1745282    2007-06-29  ebak         Uniform time stamps for log files
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3062747    2010-09-21  blaschke-oss SblimCIMClient does not log all CIM-XML responces.
 */

package org.sblim.cimclient.internal.logging;

import java.util.Calendar;

/**
 * Class TimeStamp is responsible for building uniform date/time strings for
 * logging and tracing.
 * 
 */
public class TimeStamp {

	private static String pad(int pDigits, int pNum) {
		String str = Integer.toString(pNum);
		int len = Math.max(pDigits, str.length());
		char[] cA = new char[len];
		int paddingDigits = pDigits - str.length();
		int dIdx = 0;
		while (dIdx < paddingDigits)
			cA[dIdx++] = '0';
		int sIdx = 0;
		while (dIdx < len)
			cA[dIdx++] = str.charAt(sIdx++);
		return new String(cA);
	}

	/**
	 * formatWorker
	 * 
	 * @param pMillis
	 *            - total milliseconds
	 * @param pIncludeMillis
	 *            - include milliseconds in String
	 * @return formatted date/time String. ( YYYY.MM.DD HH:mm:SS[.sss] )
	 */
	private static String formatWorker(long pMillis, boolean pIncludeMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(pMillis);
		StringBuilder sb = new StringBuilder(Integer.toString(cal.get(Calendar.YEAR)));
		sb.append('.');
		sb.append(pad(2, cal.get(Calendar.MONTH) + 1));
		sb.append('.');
		sb.append(pad(2, cal.get(Calendar.DAY_OF_MONTH)));
		sb.append(' ');
		sb.append(pad(2, cal.get(Calendar.HOUR_OF_DAY)));
		sb.append(':');
		sb.append(pad(2, cal.get(Calendar.MINUTE)));
		sb.append(':');
		sb.append(pad(2, cal.get(Calendar.SECOND)));
		if (pIncludeMillis) {
			sb.append('.');
			sb.append(pad(3, cal.get(Calendar.MILLISECOND)));
		}
		return sb.toString();
	}

	/**
	 * format
	 * 
	 * @param pMillis
	 *            - total milliseconds
	 * @return formatted date/time String. ( YYYY.MM.DD HH:mm:SS )
	 */
	public static String format(long pMillis) {
		return formatWorker(pMillis, false);
	}

	/**
	 * formatWithMillis
	 * 
	 * @param pMillis
	 *            - total milliseconds
	 * @return formatted date/time String. ( YYYY.MM.DD HH:mm:SS.sss )
	 */
	public static String formatWithMillis(long pMillis) {
		return formatWorker(pMillis, true);
	}
}
