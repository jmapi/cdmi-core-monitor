/**
 * ServiceLocationManager.java
 *
 * (C) Copyright IBM Corp. 2005, 2009
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
 * 1535756    2006-08-08  lupusalex    Make code warning free
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp;

import java.util.Locale;
import java.util.Vector;

import org.sblim.slp.internal.AdvertiserImpl;
import org.sblim.slp.internal.ua.LocatorImpl;

/**
 * The ServiceLocationManager manages access to the service location framework.
 * Clients obtain the Locator and Advertiser objects for UA and SA, and a Vector
 * of known scope names from the ServiceLocationManager.
 * 
 */
public class ServiceLocationManager {

	/**
	 * Returns the maximum across all DAs of the min-refresh-interval attribute.
	 * This value satisfies the advertised refresh interval bounds for all DAs,
	 * and, if used by the SA, assures that no refresh registration will be
	 * rejected. If no DA advertises a min-refresh-interval attribute, a value
	 * of 0 is returned.
	 * 
	 * <b>Not yet implemented</b>
	 * 
	 * @return The minimum refresh interval
	 * @throws ServiceLocationException
	 */
	public static int getRefreshInterval() throws ServiceLocationException {
		throw new ServiceLocationException(ServiceLocationException.NOT_IMPLEMENTED);
	}

	/**
	 * Returns an List of strings with all available scope names. The list of
	 * scopes comes from a variety of sources, see Section 2.1 for the scope
	 * discovery algorithm. There is always at least one string in the Vector,
	 * the default scope, "DEFAULT".
	 * 
	 * <b>Not yet implemented</b>
	 * 
	 * @return A Vector containing the scopes
	 * @throws ServiceLocationException
	 */
	public static Vector<?> findScopes() throws ServiceLocationException {
		throw new ServiceLocationException(ServiceLocationException.NOT_IMPLEMENTED);
	}

	/**
	 * Return a Locator object for the given language locale. If the
	 * implementation does not support UA functionality, returns null.
	 * 
	 * @param pLocale
	 *            The language locale of the Locator. The default SLP locale is
	 *            used if null.
	 * @return The Locator
	 */
	public static Locator getLocator(Locale pLocale) {
		return new LocatorImpl(pLocale);
	}

	/**
	 * Return an Advertiser object for the given language locale. If the
	 * implementation does not support SA functionality, returns null.
	 * 
	 * @param pLocale
	 *            The language locale of the Advertiser. The default SLP locale
	 *            is used if null.
	 * @return The advertiser
	 */
	public static Advertiser getAdvertiser(Locale pLocale) {
		return new AdvertiserImpl(pLocale);
	}

}
