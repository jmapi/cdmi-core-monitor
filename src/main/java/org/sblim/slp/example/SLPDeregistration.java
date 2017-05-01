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
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.slp.example;

import java.util.Locale;

import org.sblim.slp.Advertiser;
import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceLocationManager;
import org.sblim.slp.ServiceURL;

/**
 * Deregisters a service from the internal registry.
 */
public class SLPDeregistration {

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		ServiceURL serviceURL = new ServiceURL("service:wbem:https://myWBEMService:5989", 10);

		try {
			Advertiser slpAdvertiser = ServiceLocationManager.getAdvertiser(Locale.US);
			slpAdvertiser.deregister(serviceURL);

		} catch (ServiceLocationException e) {
			e.printStackTrace();
		}
	}
}
