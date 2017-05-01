/**
 * (C) Copyright IBM Corp. 2007, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.slp.internal.msg;

import java.util.Locale;

/**
 * Common handful functions can be here.
 * 
 */
public class Util {

	/**
	 * getLangTag
	 * 
	 * @param pLocale
	 * @return String
	 */
	public static String getLangTag(Locale pLocale) {
		if (pLocale == null) return null;
		String country = pLocale.getCountry();
		String lang = pLocale.getLanguage();
		return (country != null && country.length() > 0) ? lang + "-" + country : lang;
	}

}
