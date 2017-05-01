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
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 1678807    2007-03-12  ebak         Minor CIMDateTime suggestions
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944830    2010-02-08  blaschke-oss Fix spelling of checkGranurality()
 * 2973300    2010-03-19  blaschke-oss TCK: CIMDateTimeXXX.compareTo() does not handle null
 * 2989367    2010-04-29  blaschke-oss CIMDateTimeInterval(long) constructor range wrong
 * 2989424    2010-04-29  blaschke-oss TCK: CIMDateTimeInterval constructor
 * 2992349    2010-04-29  blaschke-oss CIMDateTimeInterval hr/min/sec max is 23/59/59, not 24/60/60
 * 2994249    2010-04-30  blaschke-oss CIMDateTimeInterval(long) calculates milliseconds
 * 3018178    2010-06-18  blaschke-oss CIMDateTimeInterval clean up
 * 3026302    2010-07-07  blaschke-oss CIMDateTimeInterval uses # constructor instead of valueOf
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 *    2674    2013-09-26  blaschke-oss Null pointer exception in CIMDateTime(String)
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

import org.sblim.cimclient.internal.cim.DTStringReader;
import org.sblim.cimclient.internal.cim.DTStringWriter;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * This class represents the datetime data type when used as a time value as
 * specified by the Distributed Management Task Force (<a
 * href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). It is in the format ddddddddHHMMSS.mmmmmm:000 where:
 * <ul>
 * <li>dddddddd - is the days in interval</li>
 * <li>HH - is the hours in interval</li>
 * <li>MM - is the minutes in interval</li>
 * <li>SS - is the seconds in interval</li>
 * <li>mmmmmm - is the microseconds in interval</li>
 * </ul>
 * For example, an elapsed time of 1 day, 13 hours, 23 minutes, 12 seconds would
 * be: 00000001132312.000000:000. A UTC offset of zero is always used for
 * interval properties.<br>
 * Fields that are not significant MUST be replaced with asterisk ("*")
 * characters. Not significant fields are those that are beyond the resolution
 * of the data source. This is used to indicate the precision of the value and
 * can only be done for an adjacent set of fields, starting with the least
 * significant field (mmmmmm), and continuing to more significant fields. The
 * granularity of using asterisks is always the entire field, except for the
 * mmmmmm field where the granularity is single digits. The UTC offset field
 * MUST NOT contain asterisks. <br>
 * For example, if an interval of 1 day, 13 hours, 23 minutes, 12 seconds, and
 * 125 milliseconds was measured with a precision of 1 millisecond, the format
 * would be: 00000001132312.125***:000.
 * 
 */
public class CIMDateTimeInterval extends CIMDateTime {

	private static final long serialVersionUID = -7362881414085831668l;

	private int iDays, iHours, iMinutes, iSeconds, iUSeconds;

	private String iStr;

	private boolean iTotalMicrosCalced;

	private long iTotalMicros;

	private int iUnsignificantUSecDigits;

	private static final int MAX_DAY = 99999999, MAX_HOUR = 23, MAX_MIN = 59, MAX_SEC = 59,
			MAX_MILLISEC = 999, MAX_MICROSEC = 999999;

	private static final long MAX_INTERVAL = ((((long) MAX_DAY * 24 * 60 * 60)
			+ ((long) MAX_HOUR * 60 * 60) + ((long) MAX_MIN * 60) + MAX_SEC) * 1000)
			+ MAX_MILLISEC;

	private static final long MILLISEC_ACCURACY_DIV = 1000,
			SEC_ACCURACY_DIV = 1000 * MILLISEC_ACCURACY_DIV,
			MIN_ACCURACY_DIV = 60 * SEC_ACCURACY_DIV, HOUR_ACCURACY_DIV = 60 * MIN_ACCURACY_DIV,
			DAY_ACCURACY_DIV = 24 * HOUR_ACCURACY_DIV,
			NO_ACCURACY_DIV = 100000000 * DAY_ACCURACY_DIV;

	private static final long[] USEC_ACCURACY_DIV_A = { 1, 10, 100, MILLISEC_ACCURACY_DIV, 10000,
			100000, SEC_ACCURACY_DIV };

	private long iAccuracyDivisor = 1;

	/**
	 * Constructs a <code>CIMDateTimeInterval</code> using the individual values
	 * of the interval (day, hours, minutes, seconds and microseconds). Any
	 * property that has a -1 will make that field "not significant" (i.e. that
	 * field has asterisks in the datetime string).
	 * 
	 * @param pDays
	 *            Number of days in the interval (-1 - 99999999).
	 * @param pHours
	 *            Number of hours in the interval (-1 - 23).
	 * @param pMinutes
	 *            Number of minutes in the interval (-1 - 59).
	 * @param pSeconds
	 *            Number of seconds in the interval (-1 - 59).
	 * @param pMicroseconds
	 *            Number of microseconds in the interval (-1 - 999999).
	 * @throws IllegalArgumentException
	 *             If any value is not valid.
	 */
	public CIMDateTimeInterval(int pDays, int pHours, int pMinutes, int pSeconds, int pMicroseconds)
			throws IllegalArgumentException {
		boolean notSignificant = checkLimits("pDays", pDays, 0, MAX_DAY, false);
		if (notSignificant) this.iAccuracyDivisor = NO_ACCURACY_DIV;
		notSignificant = checkLimits("pHours", pHours, 0, MAX_HOUR, notSignificant);
		if (notSignificant && this.iAccuracyDivisor == 1) this.iAccuracyDivisor = DAY_ACCURACY_DIV;
		notSignificant = checkLimits("pMinutes", pMinutes, 0, MAX_MIN, notSignificant);
		if (notSignificant && this.iAccuracyDivisor == 1) this.iAccuracyDivisor = HOUR_ACCURACY_DIV;
		notSignificant = checkLimits("pSeconds", pSeconds, 0, MAX_SEC, notSignificant);
		if (notSignificant && this.iAccuracyDivisor == 1) this.iAccuracyDivisor = MIN_ACCURACY_DIV;
		notSignificant = checkLimits("pMicroseconds", pMicroseconds, 0, MAX_MICROSEC,
				notSignificant);
		if (notSignificant) {
			this.iUnsignificantUSecDigits = 6;
			if (this.iAccuracyDivisor == 1) this.iAccuracyDivisor = USEC_ACCURACY_DIV_A[this.iUnsignificantUSecDigits];
		} else {
			this.iUnsignificantUSecDigits = 0;
		}

		this.iDays = pDays;
		this.iHours = pHours;
		this.iMinutes = pMinutes;
		this.iSeconds = pSeconds;
		this.iUSeconds = pMicroseconds;

		debug("CIMDateTimeInterval(" + pDays + "," + pHours + "," + pMinutes + "," + pSeconds + ","
				+ pMicroseconds + "): days=" + this.iDays + ", hours=" + this.iHours + ", mins="
				+ this.iMinutes + ", secs=" + this.iSeconds + ", usecs=" + this.iUSeconds
				+ ", acc div=" + this.iAccuracyDivisor + ", unsig digits="
				+ this.iUnsignificantUSecDigits);
	}

	/**
	 * Constructs a <code>CIMDateTimeInterval</code> using a milliseconds value.
	 * 
	 * @param pMilliseconds
	 *            Number of milliseconds in the interval (0 - 8639999999999999).
	 * @throws IllegalArgumentException
	 */
	public CIMDateTimeInterval(long pMilliseconds) throws IllegalArgumentException {
		if (pMilliseconds < 0 || pMilliseconds > MAX_INTERVAL) throw new IllegalArgumentException(
				"pMilliseconds must be between 0 and " + MAX_INTERVAL + ". " + pMilliseconds
						+ " is illegal!");
		long totalUSecs = pMilliseconds * 1000;
		long totalSecs = pMilliseconds / 1000;
		long totalMins = totalSecs / 60;
		long totalHours = totalMins / 60;
		long totalDays = totalHours / 24;

		this.iUSeconds = (int) (totalUSecs % 1000000);
		this.iSeconds = (int) (totalSecs % 60);
		this.iMinutes = (int) (totalMins % 60);
		this.iHours = (int) (totalHours % 24);
		this.iDays = (int) totalDays;

		this.iUnsignificantUSecDigits = 3;
		this.iAccuracyDivisor = USEC_ACCURACY_DIV_A[this.iUnsignificantUSecDigits];

		this.iTotalMicrosCalced = true;
		this.iTotalMicros = pMilliseconds * 1000;

		debug("CIMDateTimeInterval(" + pMilliseconds + "): days=" + this.iDays + ", hours="
				+ this.iHours + ", mins=" + this.iMinutes + ", secs=" + this.iSeconds + ", usecs="
				+ this.iUSeconds + ", acc div=" + this.iAccuracyDivisor + ", unsig digits="
				+ this.iUnsignificantUSecDigits);
	}

	/**
	 * Creates a <code>CIMDateTimeInterval</code> object using a string.
	 * 
	 * @param pIntervalString
	 *            A string in the format of ddddddddHHMMSS.mmmmmm:000.
	 * @throws IllegalArgumentException
	 *             If string is not in the correct format or <code>null</code>.
	 */
	public CIMDateTimeInterval(String pIntervalString) throws IllegalArgumentException {
		if (pIntervalString == null) throw new IllegalArgumentException(
				"Null IntervalString is not allowed!");
		if (pIntervalString.length() != LENGTH) throw new IllegalArgumentException("Length of "
				+ pIntervalString + " must be " + LENGTH + ", " + pIntervalString.length()
				+ " is not valid!");
		String intervalStr = setAccuracy(pIntervalString);
		DTStringReader dtReader = new DTStringReader(intervalStr);
		this.iDays = dtReader.readAndCheck(8, "days", 0, MAX_DAY, true);
		this.iHours = dtReader.readAndCheck(2, "hours", 0, MAX_HOUR, true);
		this.iMinutes = dtReader.readAndCheck(2, "minutes", 0, MAX_MIN, true);
		this.iSeconds = dtReader.readAndCheck(2, "seconds", 0, MAX_SEC, true);
		dtReader.read('.');
		this.iUSeconds = dtReader.readAndCheck(6, "microseconds", 0, MAX_MICROSEC, false);
		if (this.iAccuracyDivisor >= SEC_ACCURACY_DIV) this.iUSeconds = -1;
		dtReader.read(':');
		int zeros = dtReader.read(3, "zeros", false);
		if (zeros != 0) {
			String msg = "In " + pIntervalString
					+ " the last 3 characters after ':' must be zeros!";
			throw new IllegalArgumentException(msg);
		}

		debug("CIMDateTimeInterval(\"" + pIntervalString + "\"): days=" + this.iDays + ", hours="
				+ this.iHours + ", mins=" + this.iMinutes + ", secs=" + this.iSeconds + ", usecs="
				+ this.iUSeconds + ", acc div=" + this.iAccuracyDivisor + ", unsig digits="
				+ this.iUnsignificantUSecDigits);
	}

	/**
	 * Compares the <code>CIMDateTimeInterval</code> object with this one. If
	 * either interval has "Not Significant" fields then we only compare the
	 * significant fields.
	 * 
	 * @param pObj
	 *            The </code>CIMDateTimeInterval</code> to be compared with this
	 *            one.
	 * @return -1, zero, or 1 as this interval is less than, equal to, or
	 *         greater than the specified interval.
	 * @throws IllegalArgumentException
	 *             If the object passed in is not an instance of
	 *             <code>CIMDataTimeInterval</code>.
	 */
	public int compareTo(CIMDateTime pObj) throws IllegalArgumentException {
		if (!(pObj instanceof CIMDateTimeInterval)) {
			String msg = "This method requires a CIMDateTimeInterval instance, while it has received a "
					+ (pObj == null ? "null!" : pObj.getClass().getName() + " instance!");
			throw new IllegalArgumentException(msg);
		}
		CIMDateTimeInterval that = (CIMDateTimeInterval) pObj;
		long accuracyDivisor = Math.max(this.iAccuracyDivisor, that.iAccuracyDivisor);
		debug("this.acDiv=" + this.iAccuracyDivisor + ", that.acDiv=" + that.iAccuracyDivisor
				+ ", acDiv=" + accuracyDivisor);
		Long thisMicros = Long.valueOf(calcMicros(accuracyDivisor));
		Long thatMicros = Long.valueOf(that.calcMicros(accuracyDivisor));
		debug("thisUs=" + thisMicros + ", thatUs=" + thatMicros);
		return thisMicros.compareTo(thatMicros);
	}

	/**
	 * Gets the internal string representation of this object.
	 * 
	 * @return The internal representation of the
	 *         <code>CIMDateTimeInterval</code> object.
	 */
	@Override
	public String getDateTimeString() {
		if (this.iStr != null) return this.iStr;
		// ddddddddHHMMSS.mmmmmm:000
		DTStringWriter dtWriter = new DTStringWriter();
		dtWriter.write(8, this.iDays);
		dtWriter.write(2, this.iHours);
		dtWriter.write(2, this.iMinutes);
		dtWriter.write(2, this.iSeconds);
		dtWriter.write('.');
		dtWriter.writeUSec(this.iUSeconds / (int) this.iAccuracyDivisor,
				this.iUnsignificantUSecDigits);
		dtWriter.write(":000");
		return dtWriter.toString();
	}

	/**
	 * Returns days value of this interval.
	 * 
	 * @return If days field "not significant" this returns -1, otherwise
	 *         returns number of days in the interval.
	 */
	public int getDays() {
		return this.iDays;
	}

	/**
	 * Returns hours value of this interval.
	 * 
	 * @return If hours field "not significant" this returns -1, otherwise
	 *         returns number of hours in the interval.
	 */
	public int getHours() {
		return this.iHours;
	}

	/**
	 * Returns microseconds value of this interval.
	 * 
	 * @return If microseconds field "not significant" this returns -1,
	 *         otherwise returns number of microseconds in the interval.
	 */
	public int getMicroseconds() {
		return this.iUSeconds;
	}

	/**
	 * Returns minutes value of this interval.
	 * 
	 * @return If minutes field "not significant" this returns -1, otherwise
	 *         returns number of minutes in the interval.
	 */
	public int getMinutes() {
		return this.iMinutes;
	}

	/**
	 * Returns seconds value of this interval.
	 * 
	 * @return If seconds field "not significant" this returns -1, otherwise
	 *         returns number of seconds in the interval.
	 */
	public int getSeconds() {
		return this.iSeconds;
	}

	/**
	 * Returns the total length of the interval in milliseconds.
	 * 
	 * @return The length of the interval in milliseconds.
	 */
	public long getTotalMilliseconds() {
		return getTotalMicros() / 1000;
	}

	/**
	 * Returns the hash code for this object.
	 * 
	 * @return A hash code value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getDateTimeString().hashCode();
	}

	/**
	 * Returns a <code>String</code> representation of the
	 * <code>CIMDateTimeInterval</code>. This method is intended to be used only
	 * for debugging purposes, and the format of the returned string may vary
	 * between implementations. The returned string may be empty but may not be
	 * <code>null</code>.
	 * 
	 * @return String representation of this datetime.
	 */
	@Override
	public String toString() {
		return getDateTimeString();
	}

	// ddddddddHHMMSS.mmmmmm:000

	private static final int DAY_START_IDX = 0, HOUR_START_IDX = 8, MIN_START_IDX = 10,
			SEC_START_IDX = 12, DOT_IDX = 14, USEC_START_IDX = 15, UTC_START_IDX = 21, LENGTH = 25;

	/**
	 * Checks the granularity of the <code>CIMDateTimeAbsolute</code> precision.
	 * 
	 * @param pIdx
	 *            Index of first asterisk in datetime string.
	 * @param pFieldName
	 *            Name of field.
	 * @param pFieldStartIdx
	 *            Index of first character in field of datetime string.
	 * @param pNextStartIdx
	 *            Index of first character in subsequent field of datetime
	 *            string.
	 * @return <code>true</code> if field is completely unsignificant,
	 *         <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             If field is not completely significant or completely
	 *             unsignificant (mix of asterisks and digits).
	 */
	private boolean checkGranularity(int pIdx, String pFieldName, int pFieldStartIdx,
			int pNextStartIdx) throws IllegalArgumentException {
		if (pIdx > pFieldStartIdx && pIdx < pNextStartIdx) throw new IllegalArgumentException(
				"Partial unsignificant digits are not allowed for field " + pFieldName + " in "
						+ this.iStr + "!");
		return pIdx == pFieldStartIdx;
	}

	/**
	 * Find index of first asterisk within string.
	 * 
	 * @param pIvStr
	 *            Time interval string.
	 * @return Index of first asterisk or -1 if asterisk does not exist.
	 */
	private static int findStar(String pIvStr) {
		for (int i = 0; i < UTC_START_IDX; i++)
			if (pIvStr.charAt(i) == '*') return i;
		return -1;
	}

	/**
	 * Verify that all characters in the string after the starting index are
	 * asterisks.
	 * 
	 * @param pIvStr
	 *            Time interval string.
	 * @param pStartIdx
	 *            Starting index.
	 */
	private static void checkStars(String pIvStr, int pStartIdx) {
		for (int i = pStartIdx; i < UTC_START_IDX; i++) {
			char ch = pIvStr.charAt(i);
			if (i != DOT_IDX && ch != '*') throw new IllegalArgumentException("In " + pIvStr
					+ " every digit character after the first '*' character must " + "be '*', '"
					+ ch + "' is invalid!");
		}
	}

	/**
	 * Determines the accuracy (precision) of the interval string and replaces
	 * all asterisks in microsecond field with zeros.
	 * 
	 * @param pIntervalStr
	 *            Time interval string.
	 * @return Corrected time interval string with <code>iAccuracyDivisor</code>
	 *         set.
	 */
	private String setAccuracy(String pIntervalStr) throws IllegalArgumentException {
		this.iStr = pIntervalStr;
		/*
		 * String transformation. Checking the correct location of '*'
		 * characters. '*' characters in usec field are replaced into '0'
		 * characters
		 */
		int startIdx = findStar(this.iStr);
		if (startIdx < 0) return this.iStr;
		// all remaining digit fields must contain '*' characters except for utc
		checkStars(this.iStr, startIdx + 1);
		if (checkGranularity(startIdx, "Day", DAY_START_IDX, HOUR_START_IDX)) {
			this.iAccuracyDivisor = NO_ACCURACY_DIV;
		} else if (checkGranularity(startIdx, "Hour", HOUR_START_IDX, MIN_START_IDX)) {
			this.iAccuracyDivisor = DAY_ACCURACY_DIV;
		} else if (checkGranularity(startIdx, "Minute", MIN_START_IDX, SEC_START_IDX)) {
			this.iAccuracyDivisor = HOUR_ACCURACY_DIV;
		} else if (checkGranularity(startIdx, "Second", SEC_START_IDX, DOT_IDX)) {
			this.iAccuracyDivisor = MIN_ACCURACY_DIV;
		} else {
			// dealing with usec granularity
			this.iUnsignificantUSecDigits = UTC_START_IDX - startIdx;
			this.iAccuracyDivisor = USEC_ACCURACY_DIV_A[this.iUnsignificantUSecDigits];
		}
		// all '*' in usec field is replaced into '0'
		char[] buf = this.iStr.toCharArray();
		if (startIdx < USEC_START_IDX) startIdx = USEC_START_IDX;
		for (int i = startIdx; i < UTC_START_IDX; i++) {
			if (i == DOT_IDX) continue;
			if (this.iStr.charAt(i) != '*') throw new IllegalArgumentException(
					"All remaining digits must be marked as unsignificant in " + this.iStr + " !");
			buf[i] = '0';
		}
		return new String(buf);
	}

	/**
	 * Get total microseconds of <code>CIMDateTimeInterval</code> object.
	 * 
	 * @return Total microseconds of time interval.
	 */
	private long getTotalMicros() {
		if (this.iTotalMicrosCalced) return this.iTotalMicros;
		this.iTotalMicrosCalced = true;
		// iTotalMicros = iUSeconds + 1000000 * (iSeconds + 60 * (iMinutes + 60
		// * (iHours + 24 * iDays)));
		this.iTotalMicros = this.iUSeconds > 0 ? this.iUSeconds : 0;
		if (this.iSeconds > 0) this.iTotalMicros += SEC_ACCURACY_DIV * this.iSeconds;
		if (this.iMinutes > 0) this.iTotalMicros += MIN_ACCURACY_DIV * this.iMinutes;
		if (this.iHours > 0) this.iTotalMicros += HOUR_ACCURACY_DIV * this.iHours;
		if (this.iDays > 0) this.iTotalMicros += DAY_ACCURACY_DIV * this.iDays;
		debug("days=" + this.iDays + " ,hours=" + this.iHours + " ,mins=" + this.iMinutes
				+ ", secs=" + this.iSeconds + ", usecs=" + this.iUSeconds + ", totalMicros="
				+ this.iTotalMicros);
		return this.iTotalMicros;
	}

	/**
	 * Calculates microseconds of <code>CIMDateTimeInterval</code> object taking
	 * accuracy (precision) into consideration.
	 * 
	 * @param pAccuracyDivisor
	 *            Accuracy divisor of time interval.
	 * @return Total microseconds of time interval rounded down to precision.
	 */
	private long calcMicros(long pAccuracyDivisor) {
		long remainder = getTotalMicros() % pAccuracyDivisor;
		return getTotalMicros() - remainder;
	}

	/**
	 * Checks validity of time interval field, making sure value is between
	 * minimum and maximum values and is not significant if following an
	 * unsignifcant field.
	 * 
	 * @param pName
	 *            Name of field.
	 * @param pValue
	 *            Value of field.
	 * @param pLow
	 *            Minimum value of field.
	 * @param pHigh
	 *            Maximum value of field.
	 * @param pNotSignificant
	 *            <code>true</code>if previous field not significant.
	 * @return <code>true</code> if field is not significant, <code>false</code>
	 *         otherwise.
	 * @throws IllegalArgumentException
	 */
	private static boolean checkLimits(String pName, int pValue, int pLow, int pHigh,
			boolean pNotSignificant) throws IllegalArgumentException {
		if (pValue == -1) return true;
		if (pNotSignificant) throw new IllegalArgumentException(
				"Not significant fields must be followed by not significant fields!");
		if (pValue < pLow || pValue > pHigh) throw new IllegalArgumentException(pName
				+ " must be between " + pLow + " and " + pHigh + ". " + pValue + " is invalid!");
		return false;
	}

	/**
	 * Prints debug message.
	 * 
	 * @param pMsg
	 *            Debug message.
	 */
	private static void debug(String pMsg) {
	// System.out.println(pMsg);
	}
}
