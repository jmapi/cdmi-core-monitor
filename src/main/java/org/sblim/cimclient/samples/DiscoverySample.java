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
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1678915    2007-03-27  lupusalex    Integrated WBEM service discovery via SLP
 * 1729361    2007-06-04  lupusalex    Multicast discovery is broken in DiscovererSLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */
package org.sblim.cimclient.samples;

import java.util.Arrays;

import org.sblim.cimclient.discovery.AdvertisementCatalog;
import org.sblim.cimclient.discovery.Discoverer;
import org.sblim.cimclient.discovery.DiscovererFactory;
import org.sblim.cimclient.discovery.WBEMProtocol;
import org.sblim.cimclient.discovery.WBEMServiceAdvertisement;

/**
 * This class is an example how to use the high-level discovery features of the
 * SBLIM CIM Client for Java.
 */
public class DiscoverySample {

	/**
	 * Executes the samples. Will discover WBEM services on the given DAs and
	 * show some management features of the AdvertisementCatalog class.
	 * 
	 * @param args
	 *            An array of DA addresses or null for local subnet discovery
	 */
	public static void main(String[] args) {
		String host = "http://10.6.178.29:61000";
		Discoverer discoverer = DiscovererFactory.getDiscoverer(DiscovererFactory.SLP);

		String[] directories = discoverer.findDirectoryServices();

		if (directories.length == 0) {
			System.out.println("No directory or service agents found on local subnet. Exiting ...");
			return;
		}

		System.out.println("Directories: " + Arrays.asList(directories).toString());
		WBEMServiceAdvertisement[] advertisements = discoverer.findWbemServices(directories);
		System.out.println("Discovered services:");
		for (int i = 0; i < advertisements.length; ++i) {
			System.out.println(advertisements[i]);
		}
		System.out.println();
		AdvertisementCatalog catalog = new AdvertisementCatalog();
		catalog.addAdvertisements(advertisements);
		System.out.println("Catalog: " + catalog.toString());
		System.out.println("Known ids: " + Arrays.asList(catalog.getKnownIds()));
		System.out.println("Advertisements of first id: "
				+ Arrays.asList(catalog.getAdvertisementsById(catalog.getKnownIds()[0])));
		WBEMProtocol[] protocols = new WBEMProtocol[3];
		protocols[0] = new WBEMProtocol("rmi", "cim-xml");
		protocols[1] = new WBEMProtocol("http", "ws-man");
		protocols[2] = new WBEMProtocol("http", "cim-xml");
		System.out.println("Best fitting ad of first id (http/cim-xml): "
				+ catalog.getAdvertisement(catalog.getKnownIds()[0], protocols));
		protocols[0] = new WBEMProtocol("https", "cim-xml");
		System.out.println("Best fitting ad of first id (https/cim-xml): "
				+ catalog.getAdvertisement(catalog.getKnownIds()[0], protocols));
		System.out.println();
		catalog
				.refreshAdvertisements(new String[] { "1.1.1.1" },
						new WBEMServiceAdvertisement[] {});
		System.out.println("Catalog: " + catalog.toString());
		WBEMServiceAdvertisement[] advs = catalog.getAdvertisementsById(catalog.getKnownIds()[0]);
		catalog.refreshAdvertisements(new String[] { advs[0].getDirectory() }, advs);
		System.out.println("Catalog: " + catalog.toString());
		catalog.refreshAdvertisements(new String[] { advs[0].getDirectory() },
				new WBEMServiceAdvertisement[] {});
		System.out.println("Catalog: " + catalog.toString());
	}
}
