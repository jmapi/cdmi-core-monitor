/**
 * ServiceLocationAttribute.java
 *
 * (C) Copyright IBM Corp. 2005, 2013
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 1678915    2007-03-27  lupusalex    Integrated WBEM service discovery via SLP
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 2797696    2009-05-27  raman_arora  Input files use unsafe operations
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 3022519    2010-06-30  blaschke-oss ServiceLocationAttribute.equals() compares same array
 * 3022524    2010-06-30  blaschke-oss iSortedValueEntries not serializable in Serializable class
 * 3023349    2010-07-02  blaschke-oss SLP uses # constructor instead of valueOf
 *    2650    2013-07-18  blaschke-oss SLP opaque value handling incorrect
 */

package org.sblim.slp;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

import org.sblim.cimclient.GenericExts;
import org.sblim.slp.internal.AttributeHandler;
import org.sblim.slp.internal.Convert;
import org.sblim.slp.internal.SLPString;

/**
 * Service location attribute
 */
public class ServiceLocationAttribute implements Serializable {

	private static final long serialVersionUID = -6753246108754657715L;

	private Vector<Object> iValues;

	private String iId;

	/**
	 * Construct a service location attribute. Errors in the id or values vector
	 * result in an IllegalArgumentException.
	 * 
	 * @param pId
	 *            The attribute name. The String can consist of any Unicode
	 *            character.
	 * @param pValues
	 *            A Vector of one or more attribute values. Vector contents must
	 *            be uniform in type and one of Integer, String, Boolean, or
	 *            byte[]. If the attribute is a keyword attribute, then the
	 *            parameter should be null. String values can consist of any
	 *            Unicode character.
	 */
	public ServiceLocationAttribute(String pId, Vector<Object> pValues) {
		this.iId = pId;
		if (pValues != null && pValues.size() > 0) {
			this.iValues = GenericExts.cloneVector(pValues);
		}
	}

	/**
	 * Construct a service location attribute from a String.
	 * 
	 * @param pString
	 *            The string to parse
	 * @throws ServiceLocationException
	 *             When the string parsing failed
	 */
	public ServiceLocationAttribute(String pString) throws ServiceLocationException {
		if (pString == null || pString.length() == 0) throw new ServiceLocationException(
				ServiceLocationException.PARSE_ERROR,
				"Empty or null String is not good for this constructor!");

		if (pString.startsWith("(") && pString.endsWith(")")) {
			int equalPos = pString.indexOf('=');
			if (equalPos < 0) throw new ServiceLocationException(
					ServiceLocationException.PARSE_ERROR, "Missing '=' from attribute string: "
							+ pString);
			this.iId = Convert.unescape(pString.substring(1, equalPos));
			if (this.iId.length() == 0) throw new ServiceLocationException(
					ServiceLocationException.PARSE_ERROR,
					"Empty attribute ID in attribute string: " + pString);
			String valueString = pString.substring(equalPos + 1, pString.length() - 1);

			parseValueString(valueString);

		} else {
			if (pString.indexOf('(') >= 0 || pString.indexOf(')') >= 0) throw new ServiceLocationException(
					ServiceLocationException.PARSE_ERROR);
			this.iId = Convert.unescape(pString);
			this.iValues = null;
		}
	}

	/**
	 * Returns an escaped version of the id parameter, suitable for inclusion in
	 * a query. Any reserved characters as specified in [7] are escaped using
	 * UTF-8 encoding. If any characters in the tag are illegal, throws
	 * IllegalArgumentException.
	 * 
	 * @param pId
	 *            The attribute id to escape. ServiceLocationException is thrown
	 *            if any characters are illegal for an attribute tag.
	 * @return The escaped version
	 */
	public static String escapeId(String pId) {
		return Convert.escape(pId, Convert.ATTR_RESERVED);
	}

	/**
	 * Returns a String containing the escaped value parameter as a string,
	 * suitable for inclusion in a query. If the parameter is a string, any
	 * reserved characters as specified in [7] are escaped using UTF-8 encoding.
	 * If the parameter is a byte array, then the escaped string begins with the
	 * nonUTF-8 sequence `\ff` and the rest of the string consists of the
	 * escaped bytes, which is the encoding for opaque. If the value parameter
	 * is a Boolean or Integer, then the returned string contains the object
	 * converted into a string. If the value is any type other than String,
	 * Integer, Boolean or byte[], an IllegalArgumentException is thrown.
	 * 
	 * @param pValue
	 *            The attribute value to be converted into a string and escaped.
	 * @return The escaped value
	 */
	public static String escapeValue(Object pValue) {
		return AttributeHandler.escapeValue(pValue);
	}

	/**
	 * Returns a cloned vector of attribute values, or null if the attribute is
	 * a keyword attribute. If the attribute is single-valued, then the vector
	 * contains only one object.
	 * 
	 * @return The value vector
	 * 
	 */
	public Vector<Object> getValues() {
		if (this.iValues != null) return GenericExts.cloneVector(this.iValues);
		return this.iValues;
	}

	/**
	 * Returns the attribute's name.
	 * 
	 * @return The name (id)
	 */
	public String getId() {
		return this.iId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * Overrides Object.equals(). Two attributes are equal if their identifiers
	 * are equal and their value vectors contain the same number of equal values
	 * as determined by the Object equals() method. Values having byte[] type
	 * are equal if the contents of all byte arrays in both attribute vectors
	 * match. Note that the SLP string matching algorithm [7] MUST NOT be used
	 * for comparing attribute identifiers or string values.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof ServiceLocationAttribute)) return false;

		ServiceLocationAttribute that = (ServiceLocationAttribute) obj;
		if (!that.getId().equalsIgnoreCase(this.iId)) return false;

		Vector<Object> thatValues = that.iValues;
		if (this.iValues == null) return thatValues == null;
		if (thatValues == null) return false;
		if (this.iValues.size() != thatValues.size()) return false;

		ValueEntry[] thisEntries = getSortedValueEntries();
		ValueEntry[] thatEntries = that.getSortedValueEntries();

		return Arrays.equals(thisEntries, thatEntries);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * Overrides Object.toString(). The string returned contains a formatted
	 * representation of the attribute, giving the attribute's id, values, and
	 * the Java type of the values. The returned string is suitable for
	 * debugging purposes, but is not in SLP wire format.
	 */
	@Override
	public String toString() {
		StringBuffer stringbuffer = new StringBuffer("(");
		stringbuffer.append(this.iId);
		if (this.iValues != null) {
			stringbuffer.append("=");
			int size = this.iValues.size();
			for (int i = 0; i < size; i++) {
				Object obj = this.iValues.elementAt(i);
				if (i > 0) {
					stringbuffer.append(",");
				}
				if (obj instanceof byte[]) obj = AttributeHandler.mkOpaqueStr((byte[]) obj);
				stringbuffer.append(obj.toString());
			}

		}
		stringbuffer.append(")");
		return stringbuffer.toString();
	}

	private int iHashCode = 0;

	private void incHashCode(int pHashCode) {
		this.iHashCode *= 31;
		this.iHashCode += pHashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 * Overrides Object.hashCode(). Hashes on the attribute's identifier.
	 */
	@Override
	public int hashCode() {
		if (this.iHashCode == 0) {
			this.iHashCode = this.iId.hashCode();
			if (this.iValues != null) {
				ValueEntry[] valueEntries = getSortedValueEntries();
				for (int i = 0; i < valueEntries.length; i++)
					incHashCode(valueEntries[i].hashCode());
			}
		}
		return this.iHashCode;
	}

	private void parseValueString(String pStr) throws ServiceLocationException {
		StringTokenizer tokenizer = new StringTokenizer(pStr, ",");
		this.iValues = new Vector<Object>();
		while (tokenizer.hasMoreElements()) {
			String valueStr = tokenizer.nextToken();
			Object value;
			try {
				int intVal = Integer.parseInt(valueStr);
				value = Integer.valueOf(intVal);
			} catch (NumberFormatException e) {
				if ("TRUE".equalsIgnoreCase(valueStr)) {
					value = Boolean.TRUE;
				} else if ("FALSE".equalsIgnoreCase(valueStr)) {
					value = Boolean.FALSE;
				} else if (valueStr.startsWith("\\FF")) {
					value = parseOpaqueStr(valueStr);
				} else {
					value = Convert.unescape(valueStr);
				}
			}
			this.iValues.add(value);
		}
	}

	private static byte[] parseOpaqueStr(String pStr) throws ServiceLocationException {
		if (pStr.length() == 3) throw new ServiceLocationException(
				ServiceLocationException.PARSE_ERROR,
				"There must be at least three characters after \\FF in opaque string!" + " pStr="
						+ pStr);

		ByteArrayOutputStream oStr = new ByteArrayOutputStream();
		int pos = 3; // skip "\\FF"
		int left;
		while ((left = pStr.length() - pos) > 0) {
			if (left < 3) throw new ServiceLocationException(ServiceLocationException.PARSE_ERROR,
					"Number of characters must be multiple of three after \\FF in opaque string!"
							+ " pStr=" + pStr);
			if (pStr.charAt(pos) != '\\') throw new ServiceLocationException(
					ServiceLocationException.PARSE_ERROR,
					"Hex value must be preceded by \\ in opaque string!" + " pStr=" + pStr);
			String hexStr = pStr.substring(pos + 1, pos + 3);
			pos += 3;
			try {
				oStr.write(Integer.parseInt(hexStr, 16));
			} catch (NumberFormatException e) {
				throw new ServiceLocationException(ServiceLocationException.PARSE_ERROR,
						"Failed to parse hex value: " + hexStr + " in opaque string: " + pStr
								+ " !");
			}
		}
		return oStr.toByteArray();
	}

	static class ValueEntry implements Comparable<ValueEntry> {

		/**
		 * iStr
		 */
		public String iStr;

		/**
		 * iValue
		 */
		public Object iValue;

		public int compareTo(ValueEntry o) {
			ValueEntry that = o;
			return this.iStr.compareTo(that.iStr);
		}

		@Override
		public boolean equals(Object pObj) {
			if (this == pObj) return true;
			if (!(pObj instanceof ValueEntry)) return false;
			ValueEntry that = (ValueEntry) pObj;
			if (this.iValue == null) return that.iValue == null;
			if (that.iValue == null) return false;
			if (!this.iValue.getClass().equals(that.iValue.getClass())) return false;
			if (this.iValue instanceof byte[]) return Arrays.equals((byte[]) this.iValue,
					(byte[]) that.iValue);
			if (this.iValue instanceof String) return this.iStr.equals(that.iStr);
			return this.iValue.equals(that.iValue);
		}

		@Override
		public int hashCode() {
			return this.iStr == null ? 1 : this.iStr.hashCode();
		}

	}

	private transient ValueEntry[] iSortedValueEntries;

	/**
	 * Used for equals check and hashCode calculation.
	 * 
	 * @param pAttrib
	 * @return attribute values in unified order, which is : values are sorted
	 *         by theirs toString().
	 */
	private ValueEntry[] getSortedValueEntries() {
		if (this.iValues == null) return null;
		if (this.iSortedValueEntries != null) return this.iSortedValueEntries;
		this.iSortedValueEntries = new ValueEntry[this.iValues.size()];
		for (int i = 0; i < this.iValues.size(); i++) {
			ValueEntry entry = new ValueEntry();
			this.iSortedValueEntries[i] = entry;
			Object value = this.iValues.get(i);
			entry.iValue = value;
			if (value == null) {
				entry.iStr = "";
			} else {
				if (value instanceof String) {
					entry.iStr = SLPString.unify((String) value);
				} else if (value instanceof byte[]) {
					entry.iStr = AttributeHandler.mkOpaqueStr((byte[]) value);
				} else {
					entry.iStr = value.toString();
				}
			}
		}
		Arrays.sort(this.iSortedValueEntries);
		return this.iSortedValueEntries;
	}

}
