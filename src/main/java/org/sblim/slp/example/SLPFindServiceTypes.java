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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp.example;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import org.sblim.slp.Locator;
import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceLocationManager;

/**
 * List all service types for which at least one service is registered at one of
 * the SAs/DAs
 */
public class SLPFindServiceTypes {

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			Locator locator = ServiceLocationManager.getLocator(Locale.US);

			Vector<String> scopes = new Vector<String>();
			scopes.add("default");

			Enumeration<?> enumeration = locator.findServiceTypes("*", scopes);

			while (enumeration.hasMoreElements()) {
				System.out.println(enumeration.nextElement());
			}

		} catch (ServiceLocationException e) {
			e.printStackTrace();
		}
	}
}
