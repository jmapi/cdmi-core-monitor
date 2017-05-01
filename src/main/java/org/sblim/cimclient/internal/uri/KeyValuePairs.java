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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.cimclient.internal.uri;

import java.util.Vector;

/**
 * key_value_pair *("," key_value_pair)
 */
public class KeyValuePairs extends Vector<Object> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7518417983119426792L;

	/**
	 * Tries to parse the key-value pairs from the passed <code>pUriStr</code>.
	 * 
	 * @param pTyped
	 * @param pUriStr
	 * @return instance of <code>UntypedKeyValuePairs</code> or
	 *         <code>null</code> if failed.
	 * @throws IllegalArgumentException
	 */
	public static KeyValuePairs parse(boolean pTyped, URIString pUriStr)
			throws IllegalArgumentException {
		// TODO: tracing TRC.log(uriStr.toString());
		URIString uriStr = pUriStr.deepCopy();
		KeyValuePairs pairs = new KeyValuePairs();
		KeyValuePair pair = null;
		while ((pair = KeyValuePair.parse(pTyped, uriStr)) != null) {
			pairs.add(pair);
			if (uriStr.length() > 0) {
				if (!uriStr.cutStarting(',')) {
					String msg = "',' expected!\n" + uriStr.markPosition();
					throw new IllegalArgumentException(msg);
				}
			}
			if (uriStr.length() == 0) break;
		}
		if (pairs.size() > 0) {
			pUriStr.set(uriStr);
			return pairs;
		}
		return null;
	}

	/**
	 * @see java.util.Vector#toString()
	 */
	@Override
	public String toString() {
		String sep = null;
		StringBuffer dstBuf = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			KeyValuePair pair = (KeyValuePair) elementAt(i);
			if (sep != null) dstBuf.append(sep);
			else sep = ",";
			dstBuf.append(pair.toString());

		}
		return dstBuf.toString();
	}
}
