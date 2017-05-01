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

import java.util.regex.Pattern;

/**
 * key_name "=" key_value
 */
public class KeyValuePair {

	private static final Pattern KEYNAMEPAT = Pattern.compile("^([A-Za-z][0-9A-Za-z_]*).*");

	/**
	 * Tries to get an <code>KeyValuePair</code> from the passed
	 * <code>pUriStr</code>.
	 * 
	 * @param pTyped
	 * @param pUriStr
	 * @return an <code>KeyValuePair</code> or <code>null</code> if failed.
	 * @throws IllegalArgumentException
	 */
	public static KeyValuePair parse(boolean pTyped, URIString pUriStr)
			throws IllegalArgumentException {
		// TODO: tracing TRC.log(uriStr.toString());
		URIString uriStr = pUriStr.deepCopy();
		if (!uriStr.matchAndCut(KEYNAMEPAT, 1)) {
			String msg = "keyName expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		String key = uriStr.group(1);
		if (!uriStr.cutStarting('=')) {
			String msg = "'=' expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		Value value = Value.parse(pTyped, uriStr);
		if (value == null) {
			String msg = "value expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		pUriStr.set(uriStr);
		return new KeyValuePair(key, value, pTyped);
	}

	private String iKey;

	private Value iValue;

	private boolean iTyped;

	private KeyValuePair(String pKey, Value pValue, boolean pTyped) {
		this.iKey = pKey;
		this.iValue = pValue;
		this.iTyped = pTyped;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(this.iKey + '=');
		if (this.iTyped) buf.append('(' + this.iValue.getTypeInfo() + ')');
		buf.append((this.iValue instanceof QuotedValue) ? ((QuotedValue) this.iValue)
				.toQuotedString() : this.iValue.toString());
		return buf.toString();
	}

	/**
	 * getKey
	 * 
	 * @return the key String
	 */
	public String getKey() {
		return this.iKey;
	}

	/**
	 * getValue
	 * 
	 * @return the value String
	 */
	public Value getValue() {
		return this.iValue;
	}

}
