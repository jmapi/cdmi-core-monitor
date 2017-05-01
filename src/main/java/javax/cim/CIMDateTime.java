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
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2227442 	  2008-11-05  blaschke-oss Add missing serialVersionUID
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 3496301    2012-03-02  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final
 */

package javax.cim;

import java.io.Serializable;

//Sync'd against JSR48 1.0.0 javadoc (build 1.6.0_18) on Thu Mar 01 12:21:26 EST 2012
/**
 * This abstract class represents a CIM Datetime data type as defined by the
 * Distributed Management Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM
 * Infrastructure Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). It is in the format yyyyMMddHHmmss.SSSSSSsutc where:
 * <ul>
 * <li>yyyy - is a 4 digit year</li>
 * <li>MM - is the month</li>
 * <li>dd - is the day of the month</li>
 * <li>HH - is the hour (24 hour clock)</li>
 * <li>mm - is the minute</li>
 * <li>ss - is the second</li>
 * <li>SSSSSS - is the number of microseconds</li>
 * <li>s - is "+" or "-", indicating the sign of the UTC (Universal Coordinated
 * Time; for all intents and purposes the same as Greenwich Mean Time)
 * correction field, or a ":". In the case of a ":" the value is interpreted as
 * a time interval, and yyyyMM are interpreted as days.</li>
 * <li>utc - is the offset from UTC in minutes (using the sign indicated by s).
 * It is ignored for a time interval.</li>
 * </ul>
 * 
 * For example, the absolute datetime for Monday, May 25, 1998, at 1:30 PM EST
 * would be represented as: 19980525133015.000000-300. Values must be
 * zero-padded so that the entire string is always the same 25-character length.
 * Fields which are not significant must be replaced with asterisk characters.
 * Similarly, intervals use the same format, except that the interpretation of
 * the fields is based on elapsed time. <br>
 * For example, the interval datetime for an elapsed time of 1 day, 13 hours, 23
 * minutes, 12 seconds would be: 00000001132312.000000:000. A UTC offset of zero
 * is always used for interval properties.
 */
public abstract class CIMDateTime extends Object implements Serializable, Comparable<CIMDateTime> {

	private static final long serialVersionUID = 3424668043014662166L;

	/**
	 * Creates a <code>CIMDateTime</code> object using a string.
	 * 
	 * @param pDateString
	 *            A string in the format of yyyyMMddHHmmss.SSSSSSsutc.
	 * @throws IllegalArgumentException
	 *             If string is not in the correct format.
	 */
	public CIMDateTime(String pDateString) throws IllegalArgumentException {
	// FIXME: what to do here?
	}

	protected CIMDateTime() {
	// FIXME: what to do here?
	}

	/**
	 * Determines whether the <code>CIMDateTime</code> that is passed in is
	 * equal to the current <code>CIMDateTime</code> object.
	 * 
	 * @param pObj
	 *            The CIMDateTime object to compare to.
	 * @return <code>true</code> if this CIMDateTime object is equal to the one
	 *         that was passed in, otherwise <code>false</code>.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMDateTime)) return false;
		CIMDateTime that = (CIMDateTime) pObj;
		return getDateTimeString().equals(that.getDateTimeString());
	}

	/**
	 * Gets the internal string representation of this object.
	 * 
	 * @return The internal representation of the <code>CIMDateTime</code>
	 *         object.
	 */
	public abstract String getDateTimeString();

	/**
	 * Returns the hash code for this object.
	 * 
	 * @return A hash code value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();
}
