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
 * 1931621    2008-04-02  blaschke-oss CIMDateTimeAbsolute(Calendar) does not respect DST
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 2806362    2009-06-14  blaschke-oss Missing new CIMDateTimeAbsolute.getUTCOffset() method
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944826    2010-02-08  blaschke-oss getUTCOffset() incorrect if not significant field
 * 2973300    2010-03-19  blaschke-oss TCK: CIMDateTimeXXX.compareTo() does not handle null
 * 3022501    2010-06-30  blaschke-oss Possible integer overflow in getTotalUSec
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 *    2674    2013-09-26  blaschke-oss Null pointer exception in CIMDateTime(String)
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

import java.util.Calendar;

import org.sblim.cimclient.internal.cim.DTStringReader;
import org.sblim.cimclient.internal.cim.DTStringWriter;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * This class represents the datetime data type when used as a time value as
 * specified in the CIM Infrastructure specification. It is in the format
 * yyyyMMddHHmmss.SSSSSSsutc where:
 * <ul>
 * <li>yyyy - is a 4 digit year</li>
 * <li>MM - is the month</li>
 * <li>dd - is the day of the month</li>
 * <li>HH - is the hour (24 hour clock)</li>
 * <li>mm - is the minute</li>
 * <li>ss - is the second</li>
 * <li>SSSSSS - is the number of microseconds</li>
 * <li>s - is "+" or "-", indicating the sign of the UTC (Universal Coordinated
 * Time; for all intents and purposes the same as Greenwich Mean Time)</li>
 * <li>utc - is the offset from UTC in minutes (using the sign indicated by s).</li>
 * </ul>
 * For example Monday, May 25, 1998, at 1:30 PM EST would be represented as:
 * 19980525133015.000000-300. Values must be zero-padded so that the entire
 * string is always the same 25-character length. Fields which are not
 * significant must be replaced with asterisk characters.
 */
public class CIMDateTimeAbsolute extends CIMDateTime {

	private static final long serialVersionUID = 7556792806296945178l;

	private int iYear, iMonth, iDay, iHour, iMin, iSec, iUSec, iUtc;

	private boolean iUnsignificantUtc;

	private String iStr;

	/**
	 * Create a <code>CIMDateTimeAbsolute</code> object using the current
	 * Time/Date of the system.
	 */
	public CIMDateTimeAbsolute() {
		set(Calendar.getInstance());
	}

	/**
	 * Create a <code>CIMDateTimeAbsolute</code> object using a
	 * <code>Calendar</code> object.
	 * 
	 * @param pCalendar
	 *            A <code>Calendar</code> object used to initialize this object.
	 * @throws IllegalArgumentException
	 *             If <code>Calendar</code> object is <code>null</code>.
	 */
	public CIMDateTimeAbsolute(Calendar pCalendar) throws IllegalArgumentException {
		if (pCalendar == null) throw new IllegalArgumentException("Null Calendar is not allowed!");
		if (pCalendar.get(Calendar.YEAR) > 9999) throw new IllegalArgumentException(
				"The year field cannot be greater than 9999!");
		set(pCalendar);
	}

	/**
	 * Creates a <code>CIMDateTimeAbsolute</code> object using a string.
	 * 
	 * @param pDateTime
	 *            A string in the format of yyyyMMddHHmmss.SSSSSSsutc.
	 * @throws IllegalArgumentException
	 *             If string is not in the correct format or <code>null</code>.
	 */
	public CIMDateTimeAbsolute(String pDateTime) throws IllegalArgumentException {
		if (pDateTime == null) throw new IllegalArgumentException("Null DateTime is not allowed!");
		DTStringReader reader = new DTStringReader(pDateTime);
		this.iYear = reader.readAndCheck(4, "year", 0, 9999, true);
		this.iMonth = reader.readAndCheck(2, "month", 1, 12, true);
		this.iDay = reader.readAndCheck(2, "day", 1, 31, true);
		this.iHour = reader.readAndCheck(2, "hour", 0, 23, true);
		this.iMin = reader.readAndCheck(2, "minute", 0, 59, true);
		this.iSec = reader.readAndCheck(2, "second", 0, 59, true);
		reader.read('.');
		this.iUSec = reader.readAndCheck(6, "microSeconds", 0, 999999, true);
		char sign = reader.read();
		if (sign != '+' && sign != '-') {
			String msg = "Illegal character '" + sign + "' at position " + reader.getPos()
					+ "! '+' or '-' is expected.";
			throw new IllegalArgumentException(msg);
		}
		this.iUtc = reader.read(3, "utc", true);
		if (reader.isUnsignificant()) {
			this.iUnsignificantUtc = true;
		} else if (sign == '-') {
			this.iUtc = -this.iUtc;
		}
		if (reader.read() != 0) throw new IllegalArgumentException("Extra character at the end of "
				+ pDateTime + " !");
		this.iStr = pDateTime;
	}

	/**
	 * Compares the <code>CIMDateTimeAbsolute</code> object with this one. If
	 * either date has "Not Significant" fields then we can only compare the
	 * significant fields.
	 * 
	 * @param pDateTime
	 *            The <code>CIMDateTimeAbsolute</code> to be compared with this
	 *            one.
	 * @return -1, zero, or 1 as this date is less than, equal to, or greater
	 *         than the specified date.
	 * @throws IllegalArgumentException
	 *             If the object passed in is not an instance of
	 *             <code>CIMDataTimeAbsolute</code>.
	 */
	public int compareTo(CIMDateTime pDateTime) throws IllegalArgumentException {
		if (!(pDateTime instanceof CIMDateTimeAbsolute)) {
			String msg = "pDateTime must be a CIMDateTimeAbsolute instance while it is a "
					+ (pDateTime == null ? "null!" : pDateTime.getClass().getName() + " instance!");
			throw new IllegalArgumentException(msg);
		}

		CIMDateTimeAbsolute that = (CIMDateTimeAbsolute) pDateTime;
		/*
		 * Comparison: 1. building Calendars from both dates. If a field is not
		 * significant it and it's corresponding pair is cleared.
		 */
		int mask = getMask() & that.getMask();
		long thisMicros = getTotalUSec(mask);
		long thatMicros = that.getTotalUSec(mask);
		long val = thisMicros - thatMicros;
		if (val == 0) return 0;
		return val < 0 ? -1 : 1;
	}

	/**
	 * Gets the internal string representation of the date/time object.
	 * 
	 * @return The internal representation of the date/time object.
	 */
	@Override
	public String getDateTimeString() {
		if (this.iStr != null) return this.iStr;
		// yyyyMMddHHmmss.uuuuuuSutc
		DTStringWriter dTWriter = new DTStringWriter();
		dTWriter.write(4, this.iYear);
		dTWriter.write(2, this.iMonth);
		dTWriter.write(2, this.iDay);
		dTWriter.write(2, this.iHour);
		dTWriter.write(2, this.iMin);
		dTWriter.write(2, this.iSec);
		dTWriter.write('.');
		dTWriter.write(6, this.iUSec);
		if (this.iUnsignificantUtc) dTWriter.write("+***");
		else dTWriter.writeSigned(3, this.iUtc);
		return this.iStr = dTWriter.toString();
	}

	/**
	 * Returns day value of this date.
	 * 
	 * @return If day field "not significant" this returns -1, otherwise returns
	 *         day of this date.
	 */
	public int getDay() {
		return this.iDay;
	}

	/**
	 * Returns hour value of this date.
	 * 
	 * @return If hour field "not significant" this returns -1, otherwise
	 *         returns hour of this date.
	 */
	public int getHour() {
		return this.iHour;
	}

	/**
	 * Returns microsecond value of this date.
	 * 
	 * @return If microsecond field "not significant" this returns -1, otherwise
	 *         returns microseconds of this date.
	 */
	public int getMicrosecond() {
		return this.iUSec;
	}

	/**
	 * Returns minute value of this date.
	 * 
	 * @return If minute field "not significant" this returns -1, otherwise
	 *         returns minute of this date.
	 */
	public int getMinute() {
		return this.iMin;
	}

	/**
	 * Returns month value of this date.
	 * 
	 * @return If month field "not significant" this returns -1, otherwise
	 *         returns the month of this date.
	 */
	public int getMonth() {
		return this.iMonth;
	}

	/**
	 * Returns second value of this date.
	 * 
	 * @return If second field "not significant" this returns -1, otherwise
	 *         returns second of this date.
	 */
	public int getSecond() {
		return this.iSec;
	}

	/**
	 * Returns UTC offset value of this date.
	 * 
	 * @return UTC offset of this date.
	 */
	public int getUTCOffset() {
		return this.iUnsignificantUtc ? -1 : this.iUtc;
	}

	/**
	 * Returns year value of this Date.
	 * 
	 * @return If year field "not significant" this returns -1, otherwise
	 *         returns the year of this date.
	 */
	public int getYear() {
		return this.iYear;
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
	 * <code>CIMDateTimeAbsolute</code>. This method is intended to be used only
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

	private static final int YEAR = 1, MONTH = 2, DAY = 4, HOUR = 8, MIN = 16, SEC = 32, USEC = 64,
			UTC = 128;

	/**
	 * Get mask of this <code>CIMDateTimeAbsolute</code> where bit is set if the
	 * corresponding field is significant or clear if it is not significant.
	 * 
	 * @return Mask of significant fields in datetime object.
	 */
	private int getMask() {
		int mask = 0;
		if (this.iYear != -1) mask |= YEAR;
		if (this.iMonth != -1) mask |= MONTH;
		if (this.iDay != -1) mask |= DAY;
		if (this.iHour != -1) mask |= HOUR;
		if (this.iMin != -1) mask |= MIN;
		if (this.iSec != -1) mask |= SEC;
		if (this.iUSec != -1) mask |= USEC;
		if (!this.iUnsignificantUtc) mask |= UTC;
		return mask;
	}

	/**
	 * Get value of a field based on mask.
	 * 
	 * @param pMask
	 *            Mask of significant fields.
	 * @param pField
	 *            Field.
	 * @param pValue
	 *            Value of field.
	 * @param pInitValue
	 *            Initial value of field.
	 * @return Value of field if field significant, otherwise initial value.
	 */
	private int mask(int pMask, int pField, int pValue, int pInitValue) {
		return ((pMask & pField) > 0) ? pValue : pInitValue;
	}

	/**
	 * Get total microseconds of <code>CIMDateTimeAbsolute</code> object where
	 * only significant fields are used in calculation.
	 * 
	 * @param pMask
	 *            Mask of significant fields.
	 * @return Total microseconds of significant fields in datetime.
	 */
	private long getTotalUSec(int pMask) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, mask(pMask, YEAR, this.iYear, 0));
		cal.set(Calendar.MONTH, mask(pMask, MONTH, this.iMonth - 1, 0));
		cal.set(Calendar.DAY_OF_MONTH, mask(pMask, DAY, this.iDay, 1));
		cal.set(Calendar.HOUR_OF_DAY, mask(pMask, HOUR, this.iHour, 0));
		cal.set(Calendar.MINUTE, mask(pMask, MIN, this.iMin, 0));
		cal.set(Calendar.SECOND, mask(pMask, SEC, this.iSec, 0));
		int millis = this.iUSec / 1000, micros = this.iUSec % 1000;
		cal.set(Calendar.MILLISECOND, mask(pMask, USEC, millis, 0));
		long totalMicros = cal.getTimeInMillis() * 1000;
		if ((pMask & USEC) > 0) totalMicros += micros;
		/*
		 * UTC is added to the calculated micros
		 */
		if ((pMask & UTC) > 0) totalMicros += this.iUtc * 60000000L;
		return totalMicros;
	}

	/**
	 * Initializes this <code>CIMDateTimeAbsolute</code> object from the
	 * <code>Calendar</code> passed in.
	 * 
	 * @param pCal
	 *            Calendar object.
	 */
	private void set(Calendar pCal) {
		this.iYear = pCal.get(Calendar.YEAR);
		this.iMonth = pCal.get(Calendar.MONTH) + 1;
		this.iDay = pCal.get(Calendar.DAY_OF_MONTH);
		this.iHour = pCal.get(Calendar.HOUR_OF_DAY);
		this.iMin = pCal.get(Calendar.MINUTE);
		this.iSec = pCal.get(Calendar.SECOND);
		this.iUSec = pCal.get(Calendar.MILLISECOND) * 1000;
		if (pCal.getTimeZone().inDaylightTime(pCal.getTime())) {
			this.iUtc = (pCal.get(Calendar.ZONE_OFFSET) + pCal.get(Calendar.DST_OFFSET)) / 60000;
		} else {
			this.iUtc = pCal.get(Calendar.ZONE_OFFSET) / 60000;
		}
	}

}
