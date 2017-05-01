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
 * 1565892    2006-11-05  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3572993    2012-10-01  blaschke-oss parseDouble("2.2250738585072012e-308") DoS vulnerability
 */

package org.sblim.cimclient.internal.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.Util;
import org.sblim.cimclient.internal.util.WBEMConfiguration;

/**
 * Class RealValue parses and encapsulates real values.
 */
public class RealValue extends Value {

	private double iValue;

	private boolean iDoublePrec;

	private static final Pattern WHITE_PAT = Pattern.compile("^.*[\\s\\n]+.*$");

	/**
	 * realValue = [ "+" | "-" ] *decimalDigit "." 1*decimalDigit [ ( "e" | "E"
	 * ) [ "+" | "-" ] 1*decimalDigit ] parse
	 * 
	 * @param pUriStr
	 * @param pDoublePrec
	 * @return Value
	 */
	private static Value parse(URIString pUriStr, boolean pDoublePrec, boolean pThrow)
			throws IllegalArgumentException {
		URIString uriStr = pUriStr.deepCopy();
		// get the substring till the next ',' or end of pUriStr
		String strVal = uriStr.removeTill(',');
		if (strVal == null) {
			if (pThrow) {
				String msg = "Empty real value!\n" + uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		// strVal shouldn't contain white spaces
		Matcher m = WHITE_PAT.matcher(strVal);
		if (m.matches()) {
			if (pThrow) {
				String msg = "Illegal real format!\n" + pUriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		try {
			if (WBEMConfiguration.getGlobalConfiguration().verifyJavaLangDoubleStrings()) {
				if (Util.isBadDoubleString(strVal)) throw new IllegalArgumentException(
						"Double value string hangs older JVMs!\n" + pUriStr.markPosition());
			}
			double val = Double.parseDouble(strVal);
			pUriStr.set(uriStr);
			return new RealValue(val, pDoublePrec);
		} catch (NumberFormatException e) {
			if (pThrow) {
				String msg = "Illegal number format!\n" + pUriStr.markPosition()
						+ "Nested message:\n" + e.getMessage();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
	}

	/**
	 * Parses a RealValue as a double precision value.
	 * 
	 * @param pUriStr
	 * @return Value
	 */
	public static Value parse(URIString pUriStr) {
		return parse(pUriStr, true, false);
	}

	/**
	 * parseFloat
	 * 
	 * @param pUriStr
	 * @return Value
	 * @throws IllegalArgumentException
	 *             if parsing failed
	 */
	public static Value parseFloat(URIString pUriStr) throws IllegalArgumentException {
		return parse(pUriStr, false, true);
	}

	/**
	 * parseDouble
	 * 
	 * @param pUriStr
	 * @return Value
	 * @throws IllegalArgumentException
	 *             if parsing failed
	 */
	public static Value parseDouble(URIString pUriStr) throws IllegalArgumentException {
		return parse(pUriStr, true, true);
	}

	private RealValue(double pValue, boolean pDoublePrec) {
		this.iValue = pValue;
		this.iDoublePrec = pDoublePrec;
	}

	/**
	 * isDouble
	 * 
	 * @return boolean
	 */
	public boolean isDouble() {
		return this.iDoublePrec;
	}

	/**
	 * floatValue
	 * 
	 * @return float
	 */
	public float floatValue() {
		return (float) this.iValue;
	}

	/**
	 * doubleValue
	 * 
	 * @return double
	 */
	public double doubleValue() {
		return this.iValue;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Double.toString(this.iValue);
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		if (this.iDoublePrec) return MOF.DT_REAL64;
		return MOF.DT_REAL32;
	}

}
