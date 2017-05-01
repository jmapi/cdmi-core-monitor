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
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1565892    2006-12-06  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2964463    2010-03-08  blaschke-oss WBEMClient.initialize() throws wrong exception
 * 2942520    2010-03-08  blaschke-oss IPv6 link local address with scope_id including a dot not supported
 * 3513353    2012-03-30  blaschke-oss TCK: CIMDataType arrays must have length >= 1
 * 3513349    2012-03-31  blaschke-oss TCK: CIMDataType must not accept null string
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
 */

package org.sblim.cimclient.internal.cim;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

import org.sblim.cimclient.internal.util.WBEMConstants;

/**
 * Class CIMHelper provides convenience methods that are missing from the
 * official JSR48 API
 * 
 */
public abstract class CIMHelper {

	private CIMHelper() {
	// no instances
	}

	/**
	 * Creates a URI of a CIMOM from a given CIM object path, adding default
	 * port if port not parsable.
	 * 
	 * @param pPath
	 *            The CIM object path.
	 * @return The URI.
	 * @throws URISyntaxException
	 */
	public static URI createCimomUri(CIMObjectPath pPath) throws URISyntaxException {
		String scheme = pPath.getScheme();
		String host = pPath.getHost();
		int port = WBEMConstants.DEFAULT_WBEM_PORT;
		try {
			port = Integer.parseInt(pPath.getPort());
		} catch (NumberFormatException e) {
			// stuck with default port
		}
		return new URI(scheme, null, host, port, WBEMConstants.CIMOM_PATH, null, null);
	}

	/**
	 * Creates a URI of a CIMOM from a given URI, adding default port if port
	 * not specified.
	 * 
	 * @param pUri
	 *            The URI.
	 * @return The URI.
	 * @throws URISyntaxException
	 */
	public static URI createCimomUri(URI pUri) throws URISyntaxException {
		String scheme = pUri.getScheme();
		String host = pUri.getHost();
		int port = pUri.getPort();
		if (port == -1) {
			// stuck with default port
			port = WBEMConstants.DEFAULT_WBEM_PORT;
		}
		return new URI(scheme, null, host, port, WBEMConstants.CIMOM_PATH, null, null);
	}

	private static CIMDataType CIMScalarDataTypes[] = {
	/* 00 */CIMDataType.UINT8_T,
	/* 01 */CIMDataType.SINT8_T,
	/* 02 */CIMDataType.UINT16_T,
	/* 03 */CIMDataType.SINT16_T,
	/* 04 */CIMDataType.UINT32_T,
	/* 05 */CIMDataType.SINT32_T,
	/* 06 */CIMDataType.UINT64_T,
	/* 07 */CIMDataType.SINT64_T,
	/* 08 */CIMDataType.STRING_T,
	/* 09 */CIMDataType.BOOLEAN_T,
	/* 10 */CIMDataType.REAL32_T,
	/* 11 */CIMDataType.REAL64_T,
	/* 12 */CIMDataType.DATETIME_T,
	/* 13 */CIMDataType.CHAR16_T,
	/* 14 */new CIMDataType(""),
	/* 15 */CIMDataType.OBJECT_T,
	/* 16 */null,
	/* 17 */CIMDataType.CLASS_T };

	/**
	 * Returns the CIMDataType of a scalar of the specified data type. This
	 * should be used in lieu of "new CIMDataType(pType)" which is not supported
	 * by the JSR48 standard.
	 * 
	 * @param pType
	 *            Data type.
	 * @return CIMDataType corresponding to data type.
	 */
	public static CIMDataType ScalarDataType(int pType) {
		if (pType < 0 || pType >= CIMScalarDataTypes.length) return null;
		return CIMScalarDataTypes[pType];
	}

	private static CIMDataType CIMArrayDataTypes[] = {
	/* 00 */CIMDataType.UINT8_ARRAY_T,
	/* 01 */CIMDataType.SINT8_ARRAY_T,
	/* 02 */CIMDataType.UINT16_ARRAY_T,
	/* 03 */CIMDataType.SINT16_ARRAY_T,
	/* 04 */CIMDataType.UINT32_ARRAY_T,
	/* 05 */CIMDataType.SINT32_ARRAY_T,
	/* 06 */CIMDataType.UINT64_ARRAY_T,
	/* 07 */CIMDataType.SINT64_ARRAY_T,
	/* 08 */CIMDataType.STRING_ARRAY_T,
	/* 09 */CIMDataType.BOOLEAN_ARRAY_T,
	/* 10 */CIMDataType.REAL32_ARRAY_T,
	/* 11 */CIMDataType.REAL64_ARRAY_T,
	/* 12 */CIMDataType.DATETIME_ARRAY_T,
	/* 13 */CIMDataType.CHAR16_ARRAY_T,
	/* 14 */new CIMDataType("", 0),
	/* 15 */CIMDataType.OBJECT_ARRAY_T,
	/* 16 */null,
	/* 17 */CIMDataType.CLASS_ARRAY_T };

	/**
	 * Returns the CIMDataType of an unbounded array of the specified data type.
	 * This should be used in lieu of "new CIMDataType(pType,0)" which is not
	 * supported by the JSR48 standard.
	 * 
	 * @param pType
	 *            Data type.
	 * @return CIMDataType corresponding to data type.
	 */
	public static CIMDataType UnboundedArrayDataType(int pType) {
		if (pType < 0 || pType >= CIMArrayDataTypes.length) return null;
		return CIMArrayDataTypes[pType];
	}

	/**
	 * CIMInstanceWithSynchonizedNumericKeyDataTypes returns a
	 * <code>CIMInstance</code> where the data types of all numeric keys in the
	 * <code>CIMObjectPath</code> match those of the corresponding keys within
	 * the <code>CIMProperty[]</code>.
	 * 
	 * The need for this conversion mechanism arises from a deficiency in the
	 * CIM-XML specs, where the TYPE (sint8, uint8, etc.) is required for
	 * PROPERTY but not for KEYVALUE. If a CIMOM sends a KEYVALUE of
	 * VALUETYPE="numeric" without TYPE, the Java CIM Client assumes a type of
	 * sint64, uint64 or real64. This can cause problems (i.e.
	 * ClassCastException) down the line if the TYPE of the corresponding
	 * PROPERTY is different.
	 * 
	 * @param pObjectPath
	 *            Instance object path.
	 * @param pProps
	 *            Instance properties.
	 * @return CIMInstance with numeric key data types synchronized.
	 */
	public static CIMInstance CIMInstanceWithSynchonizedNumericKeyDataTypes(
			CIMObjectPath pObjectPath, CIMProperty<?>[] pProps) {
		CIMInstance inst = new CIMInstance(pObjectPath, pProps);
		CIMProperty<?>[] oldKeys = inst.getKeys();
		CIMProperty<?>[] newKeys = new CIMProperty<?>[oldKeys.length];
		boolean update = false;

		for (int i = 0; i < oldKeys.length; i++) {
			CIMDataType oldType = oldKeys[i].getDataType();
			CIMProperty<?> prop = inst.getProperty(oldKeys[i].getName());
			if (oldType != null && prop != null && prop.getDataType() != null
					&& !prop.getDataType().equals(oldType) && isNumericObject(oldType)
					&& isNumericObject(prop.getDataType())) {
				update = true;
				newKeys[i] = new CIMProperty<Object>(oldKeys[i].getName(), prop.getDataType(),
						translateNumericObject(oldKeys[i].getValue(), oldType, prop.getDataType()),
						oldKeys[i].isKey(), oldKeys[i].isPropagated(), oldKeys[i].getOriginClass());
			} else {
				newKeys[i] = oldKeys[i];
			}
		}

		return (update ? inst.deriveInstance(new CIMObjectPath(pObjectPath.getScheme(), pObjectPath
				.getHost(), pObjectPath.getPort(), pObjectPath.getNamespace(), pObjectPath
				.getObjectName(), newKeys)) : inst);
	}

	private static boolean isNumericObject(CIMDataType type) {
		switch (type.getType()) {
			case CIMDataType.SINT8:
			case CIMDataType.SINT16:
			case CIMDataType.SINT32:
			case CIMDataType.SINT64:
			case CIMDataType.UINT8:
			case CIMDataType.UINT16:
			case CIMDataType.UINT32:
			case CIMDataType.UINT64:
			case CIMDataType.REAL32:
			case CIMDataType.REAL64:
				return true;
		}
		return false;
	}

	private static Object translateNumericObject(Object oldValue, CIMDataType oldType,
			CIMDataType newType) {
		if (oldValue == null) return null;

		int from = oldType.getType(), to = newType.getType();
		long newInt = 0;
		double newDec = 0;
		Object o = null;
		boolean useInt = true;

		switch (from) {
			case CIMDataType.SINT8:
				Byte b = (Byte) oldValue;
				newInt = b.longValue();
				break;
			case CIMDataType.SINT16:
				Short s = (Short) oldValue;
				newInt = s.longValue();
				break;
			case CIMDataType.SINT32:
				Integer i = (Integer) oldValue;
				newInt = i.longValue();
				break;
			case CIMDataType.SINT64:
				Long l = (Long) oldValue;
				newInt = l.longValue();
				break;
			case CIMDataType.UINT8:
				UnsignedInteger8 u8 = (UnsignedInteger8) oldValue;
				newInt = u8.longValue();
				break;
			case CIMDataType.UINT16:
				UnsignedInteger16 u16 = (UnsignedInteger16) oldValue;
				newInt = u16.longValue();
				break;
			case CIMDataType.UINT32:
				UnsignedInteger32 u32 = (UnsignedInteger32) oldValue;
				newInt = u32.longValue();
				break;
			case CIMDataType.UINT64:
				UnsignedInteger64 u64 = (UnsignedInteger64) oldValue;
				newInt = u64.longValue();
				break;
			case CIMDataType.REAL32:
				Float f = (Float) oldValue;
				newDec = f.doubleValue();
				useInt = false;
				break;
			case CIMDataType.REAL64:
				Double d = (Double) oldValue;
				newDec = d.doubleValue();
				useInt = false;
				break;
		}

		switch (to) {
			case CIMDataType.SINT8:
				byte b = (byte) (useInt ? newInt : newDec);
				o = new Byte(b);
				break;
			case CIMDataType.SINT16:
				short s = (short) (useInt ? newInt : newDec);
				o = new Short(s);
				break;
			case CIMDataType.SINT32:
				int i = (int) (useInt ? newInt : newDec);
				o = new Integer(i);
				break;
			case CIMDataType.SINT64:
				long l = (long) (useInt ? newInt : newDec);
				o = new Long(l);
				break;
			case CIMDataType.UINT8:
				byte u8 = (byte) (useInt ? newInt : newDec);
				o = new UnsignedInteger8(u8);
				break;
			case CIMDataType.UINT16:
				short u16 = (short) (useInt ? newInt : newDec);
				o = new UnsignedInteger16(u16);
				break;
			case CIMDataType.UINT32:
				int u32 = (int) (useInt ? newInt : newDec);
				o = new UnsignedInteger32(u32);
				break;
			case CIMDataType.UINT64:
				long u64 = (long) (useInt ? newInt : newDec);
				o = new UnsignedInteger64(BigInteger.valueOf(u64));
				break;
			case CIMDataType.REAL32:
				float f = (float) (useInt ? newInt : newDec);
				o = new Float(f);
				break;
			case CIMDataType.REAL64:
				double d = useInt ? (double) newInt : newDec;
				o = new Double(d);
				break;
		}

		return o;
	}
}
