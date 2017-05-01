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
 */

package org.sblim.cimclient.internal.uri;

import org.sblim.cimclient.internal.util.MOF;

/**
 * This class is responsible for parsing key values.
 */
public abstract class Value {

	/**
	 * Factory method which tries to parse an untyped value.
	 * 
	 * @param pTyped
	 * @param pUriStr
	 * @return <code>Value</code> instance
	 * @throws IllegalArgumentException
	 *             if parsing failed.
	 */
	public static Value parse(boolean pTyped, URIString pUriStr) throws IllegalArgumentException {
		// TODO: tracing TRC.debug(uriStr.toString());
		if (pTyped) return parseTypedValue(pUriStr);
		URIString uriStr = pUriStr.deepCopy();
		Value value;
		try {
			StringValue strVal = (StringValue) StringValue.parse(uriStr);

			// string like value
			// is this string an instance reference?
			try {
				URI ref = URI.parseRef(new URIString(strVal.toString()), false);
				value = new ReferenceValue(ref);
			} catch (IllegalArgumentException e) {
				if ((value = DateTimeValue.parse(strVal.toString())) == null) {
					// if not dateTimeValue, it is stringvalue
					value = strVal;
				}
			}
			pUriStr.set(uriStr);
			return value;
		} catch (IllegalArgumentException e) {
			// non string like value
			if ((value = IntegerValue.parse(uriStr)) != null
					|| (value = RealValue.parse(uriStr)) != null
					|| (value = BooleanValue.parse(uriStr)) != null
					|| (value = CharValue.parse(uriStr)) != null) {
				pUriStr.set(uriStr);
				return value;
			}
			String msg = "Failed to parse untyped value!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
	}

	/**
	 * getTypeInfo
	 * 
	 * @return the type description string of the Value.
	 */
	public abstract String getTypeInfo();

	private static Value parseTypedValue(URIString pUriStr) throws IllegalArgumentException {
		URIString uriStr = pUriStr.deepCopy();
		int typeInfoPos = uriStr.getPos();
		String typeInfo = parseTypeInfo(uriStr);
		if (typeInfo == null) {
			String msg = "typeInfo expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		int valuePos = uriStr.getPos();
		Value val;
		try {
			if (typeInfo.equalsIgnoreCase(MOF.DT_STR)) {
				val = StringValue.parse(uriStr);
			} else if (typeInfo.equalsIgnoreCase(MOF.REFERENCE)) {
				val = parseTypedReference(uriStr);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_DATETIME)) {
				val = parseTypedDateTime(uriStr);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_CHAR16)) {
				val = CharValue.parse(uriStr);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_BOOL)) {
				val = BooleanValue.parse(uriStr);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_SINT8)) {
				val = IntegerValue.parseSigned(uriStr, 8);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_SINT16)) {
				val = IntegerValue.parseSigned(uriStr, 16);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_SINT32)) {
				val = IntegerValue.parseSigned(uriStr, 32);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_SINT64)) {
				val = IntegerValue.parseSigned(uriStr, 64);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_UINT8)) {
				val = IntegerValue.parseUnsigned(uriStr, 8);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_UINT16)) {
				val = IntegerValue.parseUnsigned(uriStr, 16);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_UINT32)) {
				val = IntegerValue.parseUnsigned(uriStr, 32);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_UINT64)) {
				val = IntegerValue.parseUnsigned(uriStr, 64);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_REAL32)) {
				val = RealValue.parseFloat(uriStr);
			} else if (typeInfo.equalsIgnoreCase(MOF.DT_REAL64)) {
				val = RealValue.parseDouble(uriStr);
			} else {
				val = null;
			}
		} catch (IllegalArgumentException e) {
			String msg = "Failed to parse " + typeInfo + " value!\n"
					+ uriStr.markPosition(valuePos) + "Nested message:\n" + e.getMessage();
			throw new IllegalArgumentException(msg);
		}
		if (val == null) {
			String msg = "Unknown type:" + typeInfo + "!\n" + uriStr.markPosition(typeInfoPos);
			throw new IllegalArgumentException(msg);
		}
		pUriStr.set(uriStr);
		return val;
	}

	private static Value parseTypedReference(URIString pUriStr) throws IllegalArgumentException {
		Value strVal;
		int pos = pUriStr.getPos();
		try {
			strVal = StringValue.parse(pUriStr);
		} catch (IllegalArgumentException e) {
			String msg = "Failed to retrieve typed reference string!\n" + pUriStr.markPosition()
					+ "Nested message is:\n" + e.getMessage();
			throw new IllegalArgumentException(msg);
		}
		URIString refUriStr = new URIString(strVal.toString());
		try {
			URI ref = URI.parseRef(refUriStr, true);
			return new ReferenceValue(ref);
		} catch (IllegalArgumentException e) {
			String msg = "Failed to parse typed reference value!\n" + pUriStr.markPosition(pos)
					+ "Nested message is:\n" + e.getMessage();
			throw new IllegalArgumentException(msg);
		}
	}

	private static Value parseTypedDateTime(URIString pUriStr) throws IllegalArgumentException {
		Value strVal;
		try {
			strVal = StringValue.parse(pUriStr);
		} catch (IllegalArgumentException e) {
			String msg = "Failed to retrieve typed datetime string!\n" + pUriStr.markPosition()
					+ "Nested message is:\n" + e.getMessage();
			throw new IllegalArgumentException(msg);
		}
		return DateTimeValue.parse(strVal.toString(), true);
	}

	private static String parseTypeInfo(URIString pUriStr) {
		URIString uriStr = pUriStr.deepCopy();
		if (!uriStr.cutStarting('(')) return null;
		String typeInfo = uriStr.removeTill(')', true, true);
		if (typeInfo == null) return null;
		pUriStr.set(uriStr);
		return typeInfo;
	}

}
