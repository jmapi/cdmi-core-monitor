/**
 * (C) Copyright IBM Corp. 2007, 2011
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
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 */

package org.sblim.slp.internal;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * debug -> FINEST info -> INFO warning -> WARNING error -> SEVERE
 * 
 */
public class TRC {

	private static PrintStream cOut;

	private static Level cLevel = SLPConfig.getGlobalCfg().getTraceLevel();

	private static LogAndTraceBroker cLogger = LogAndTraceBroker.getBroker();

	private static Pattern[] cDenyPatterns;

	private static Pattern[] cAllowPatterns;

	private static final String NAME = new TRC().getClass().getName();

	/**
	 * setLevel
	 * 
	 * @param pLevel
	 */
	public static void setLevel(Level pLevel) {
		cLevel = pLevel;
	}

	/**
	 * setConsolLog
	 * 
	 * @param pOutStr
	 */
	public static void setOutput(OutputStream pOutStr) {
		setOutput(new PrintStream(pOutStr));
	}

	/**
	 * setOutputStream
	 * 
	 * @param pOutStr
	 */
	public static void setOutput(PrintStream pOutStr) {
		cOut = pOutStr;
	}

	/**
	 * setPatterns
	 * 
	 * @param pDenyPatterns
	 * @param pAllowPatterns
	 */
	public static void setPatterns(String[] pDenyPatterns, String[] pAllowPatterns) {
		cDenyPatterns = compile(pDenyPatterns);
		cAllowPatterns = compile(pAllowPatterns);
	}

	private static Pattern[] compile(String[] pStrs) {
		if (pStrs == null) return null;
		Pattern[] patterns = new Pattern[pStrs.length];
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = Pattern.compile(pStrs[i]);
		}
		return patterns;
	}

	/**
	 * debug
	 * 
	 * @param pMsg
	 */
	public static void debug(String pMsg) {
		debug(pMsg, null);
	}

	/**
	 * debug
	 * 
	 * @param pMsg
	 * @param pEx
	 */
	public static void debug(String pMsg, Exception pEx) {
		if (cLevel.intValue() > Level.FINEST.intValue()) return;
		trace(Level.FINEST, pMsg, pEx);
	}

	/**
	 * info
	 * 
	 * @param pMsg
	 */
	public static void info(String pMsg) {
		info(pMsg, null);
	}

	/**
	 * info
	 * 
	 * @param pMsg
	 * @param pEx
	 */
	public static void info(String pMsg, Exception pEx) {
		if (cLevel.intValue() > Level.INFO.intValue()) return;
		trace(Level.INFO, pMsg, pEx);
	}

	/**
	 * warning
	 * 
	 * @param pMsg
	 */
	public static void warning(String pMsg) {
		warning(pMsg, null);
	}

	/**
	 * warning
	 * 
	 * @param pMsg
	 * @param pEx
	 */
	public static void warning(String pMsg, Exception pEx) {
		if (cLevel.intValue() > Level.WARNING.intValue()) return;
		trace(Level.WARNING, pMsg, pEx);
	}

	/**
	 * error
	 * 
	 * @param pMsg
	 */
	public static void error(String pMsg) {
		error(pMsg, null);
	}

	/**
	 * error
	 * 
	 * @param pEx
	 */
	public static void error(Exception pEx) {
		error(null, pEx);
	}

	/**
	 * error
	 * 
	 * @param pMsg
	 * @param pEx
	 */
	public static void error(String pMsg, Exception pEx) {
		if (cLevel.intValue() > Level.SEVERE.intValue()) return;
		trace(Level.SEVERE, pMsg, pEx);
	}

	private static boolean hasMatch(Pattern[] pPatterns, String pStr) {
		if (pPatterns == null) return false;
		for (int i = 0; i < pPatterns.length; i++) {
			Pattern pattern = pPatterns[i];
			Matcher m = pattern.matcher(pStr);
			if (m.matches()) return true;
		}
		return false;
	}

	private static boolean deny(String pFnStr) {
		return hasMatch(cDenyPatterns, pFnStr);
	}

	private static boolean allow(String pFnStr) {
		return hasMatch(cAllowPatterns, pFnStr);
	}

	private static void trace(Level pLevel, String pMsg, Exception pEx) {
		StackTraceElement location = getLocation();
		String fnStr = getFunctionStr(location);
		if (deny(fnStr) && !allow(fnStr)) return;
		StringBuffer buf = new StringBuffer('[' + pLevel.toString() + ' '
				+ Thread.currentThread().getName() + ' ' + getDate() + ' '
				+ getLocationStr(location) + "]\n");
		if (pMsg != null) buf.append(pMsg + '\n');
		if (pEx != null) {
			StringWriter writer = new StringWriter();
			pEx.printStackTrace(new PrintWriter(writer));
			buf.append(writer.toString() + '\n');
		}
		synchronized (TRC.class) {
			if (cOut != null) {
				cOut.println(buf.toString());
				System.out.flush();
			} else {
				cLogger.trace(pLevel, buf.toString());
			}
		}
	}

	private static StackTraceElement getLocation() {
		Throwable thr = new Throwable();
		StackTraceElement[] elements = thr.getStackTrace();
		StackTraceElement e = null;
		for (int i = 0; i < elements.length; i++) {
			e = elements[i];
			if (!e.getClassName().equals(NAME)) return e;
		}
		return null;
	}

	private static String getFunctionStr(StackTraceElement pSTE) {
		return pSTE.getClassName() + '.' + pSTE.getMethodName();
	}

	private static String getLocationStr(StackTraceElement pSTE) {
		return getFunctionStr(pSTE) + '(' + pSTE.getFileName() + ':' + pSTE.getLineNumber() + ')';
	}

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
	 * getDate
	 * 
	 * @return String
	 */
	private static String getDate() {
		long millis = new Date().getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return Integer.toString(cal.get(Calendar.YEAR)) + '.' + pad(2, cal.get(Calendar.MONTH) + 1)
				+ '.' + pad(2, cal.get(Calendar.DAY_OF_MONTH)) + ' '
				+ pad(2, cal.get(Calendar.HOUR_OF_DAY)) + ':' + pad(2, cal.get(Calendar.MINUTE))
				+ ':' + pad(2, cal.get(Calendar.SECOND)) + ' '
				+ pad(3, cal.get(Calendar.MILLISECOND));
	}

}
