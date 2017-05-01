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
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 */

package org.sblim.cimclient.internal.uri;

import org.sblim.cimclient.internal.util.MOF;

/**
 * Class BooleanValue parses and encapsulates a boolean value.
 */
public class BooleanValue extends Value {

	private boolean iValue;

	/**
	 * parse
	 * 
	 * @param pUriStr
	 * @param pThrow
	 * @return <code>Value</code> or <code>null</code> if <code>pThrow</code> is
	 *         <code>false</code> and parsing failed
	 * @throws IllegalArgumentException
	 *             if parsing failed and <code>pThrow</code> is
	 *             <code>true</code>
	 */
	public static Value parse(URIString pUriStr, boolean pThrow) throws IllegalArgumentException {
		URIString uriStr = pUriStr.deepCopy();
		boolean value;
		if (uriStr.cutStarting("true", true)) value = true;
		else if (uriStr.cutStarting("false", true)) value = false;
		else {
			if (pThrow) {
				String msg = "Boolean value not found!\n" + uriStr.markPosition();
				throw new IllegalArgumentException(msg);
			}
			return null;
		}
		// next char must be ',' or nothing
		if (uriStr.length() != 0 && uriStr.charAt(0) != ',') return null;
		pUriStr.set(uriStr);
		return new BooleanValue(value);
	}

	/**
	 * Parses an untyped boolean value.
	 * 
	 * @param pUriStr
	 * @return Value
	 */
	public static Value parse(URIString pUriStr) {
		return parse(pUriStr, false);
	}

	private BooleanValue(boolean pValue) {
		this.iValue = pValue;
	}

	/**
	 * getValue
	 * 
	 * @return the <code>boolean</code> value.
	 */
	public boolean getValue() {
		return this.iValue;
	}

	/**
	 * getBoolean
	 * 
	 * @return Boolean
	 */
	public Boolean getBoolean() {
		return Boolean.valueOf(this.iValue);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Boolean.toString(this.iValue);
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		return MOF.DT_BOOL;
	}

}
