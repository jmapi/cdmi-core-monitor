/**
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
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1516242    2006-11-27  lupusalex    Support of OpenPegasus local authentication
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.cimclient.internal.http;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class AuthorizationHandler manages AdAuthorizationInfo instances
 * 
 */
public class AuthorizationHandler {

	private ArrayList<AuthorizationInfo> iAuthList = new ArrayList<AuthorizationInfo>();

	/**
	 * Adds an AuthorizationInfo to the handler
	 * 
	 * @param pAuthorizationInfo
	 *            The AuthorizationInfo to add
	 */
	public synchronized void addAuthorizationInfo(AuthorizationInfo pAuthorizationInfo) {
		this.iAuthList.add(pAuthorizationInfo);
	}

	/**
	 * Returns the corresponding AuthorizationInfo for a given set of parameters
	 * 
	 * @param pAuthorizationModule
	 *            The authorization module
	 * @param pProxy
	 *            Proxy authentication ?
	 * @param pAddr
	 *            Host address
	 * @param pPort
	 *            Host port
	 * @param pProtocol
	 *            Protocol
	 * @param pRealm
	 *            Realm
	 * @param pScheme
	 *            Scheme
	 * @return The AuthorizationInfo or <code>null</code> if none fits
	 */
	public synchronized AuthorizationInfo getAuthorizationInfo(String pAuthorizationModule,
			Boolean pProxy, String pAddr, int pPort, String pProtocol, String pRealm, String pScheme) {

		AuthorizationInfo request = AuthorizationInfo.createAuthorizationInfo(pAuthorizationModule,
				pProxy, pAddr, pPort, pProtocol, pRealm, pScheme);

		Iterator<AuthorizationInfo> iter = this.iAuthList.iterator();
		while (iter.hasNext()) {
			AuthorizationInfo authInfo = iter.next();

			if (authInfo.match(request)) return authInfo;

		}
		return null;
	}

	/**
	 * Returns the AuthorizationInfo at a given index
	 * 
	 * @param pIndex
	 *            The index
	 * @return The AuthorizationInfo
	 */
	public synchronized AuthorizationInfo getAuthorizationInfo(int pIndex) {
		return this.iAuthList.get(pIndex);
	}

	@Override
	public String toString() {
		return "AuthorizationHandler=[AuthInfoList=" + this.iAuthList + "]";
	}
}
