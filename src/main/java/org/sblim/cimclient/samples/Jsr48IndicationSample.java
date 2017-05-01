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
 * @author : Alexander Wolf-Reber, a.wolf-reber@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-14  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2315151 	  2008-11-20  rgummada     Jsr48IndicationSample does not work 
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 2956716    2010-03-02  blaschke-oss Jsr48IndicationSample hardcoded namespace
 * 3190335    2011-02-23  blaschke-oss Erroneous use of SystemName property in samples
 * 3267429    2011-04-01  blaschke-oss Samples should close client
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 */
package org.sblim.cimclient.samples;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.UnsignedInteger16;
import javax.wbem.WBEMException;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.listener.IndicationListener;
import javax.wbem.listener.WBEMListener;
import javax.wbem.listener.WBEMListenerFactory;

/**
 * Class Jsr48IndicationSample is an example for setting up an indication
 * listener with the JSR48 API. Keep in mind that this sample most likely won't
 * show any indications if you have a firewall between you and the CIMOM.
 */
public abstract class Jsr48IndicationSample {

	private static final String PROTOCOL = "http";

	private static final String LISTENER_PORT = "5999";

	private static CIMObjectPath cSubscriptionPath;

	private static CIMObjectPath cFilterPath;

	private static CIMObjectPath cDestinationPath;

	private static WBEMListener cListener;

	private static int cId = 0;

	/**
	 * Starts a listener. The JSR48 library will open a http(s) server socket
	 * and listen for incoming indications on that socket. Any indications
	 * received will be forwarded to the registered IndicationListener
	 * implementation. The sample one here just prints the indication to stdout.
	 * 
	 * @return <code>true</code> if the listener could be started,
	 *         <code>false</code> otherwise.
	 */
	public static boolean startListener() {
		try {
			cListener = WBEMListenerFactory.getListener(WBEMClientConstants.PROTOCOL_CIMXML);
			cListener.addListener(new IndicationListener() {

				public void indicationOccured(String pIndicationURL, CIMInstance pIndication) {
					System.out.println("Indication received on: " + pIndicationURL + ":");
					System.out.println(Jsr48CimSample.toMof(pIndication));
				}
			}, Integer.parseInt(LISTENER_PORT), PROTOCOL);

			return true;

		} catch (IOException e) {
			// nothing to do here
		}
		return false;
	}

	/**
	 * Constructs a CIM_ListenerDestinationCIMXML instance
	 * 
	 * @param pNamespace
	 *            The scoping namespace
	 * @param pURL
	 *            The URL of the destination
	 * @param pHost
	 *            The host name of the WBEM service's URL
	 * @return The instance
	 * @throws UnknownHostException
	 *             If "localhost" could not be resolved
	 */
	private static CIMInstance makeListenerDestination(String pNamespace, String pURL, String pHost)
			throws UnknownHostException {

		final CIMProperty<String> name = new CIMProperty<String>("Name", CIMDataType.STRING_T,
				"JSR48SampleListener" + getNextId(), true, false, null);
		final CIMProperty<String> creationClassName = new CIMProperty<String>("CreationClassName",
				CIMDataType.STRING_T, "CIM_ListenerDestinationCIMXML", true, false, null);
		final CIMProperty<String> systemName = new CIMProperty<String>("SystemName",
				CIMDataType.STRING_T, InetAddress.getByName(pHost).getHostName(), true, false, null);
		final CIMProperty<String> systemCCN = new CIMProperty<String>("SystemCreationClassName",
				CIMDataType.STRING_T, "CIM_ComputerSystem", true, false, null);
		final CIMProperty<String> destination = new CIMProperty<String>("Destination",
				CIMDataType.STRING_T, pURL, false, false, null);
		final CIMProperty<UnsignedInteger16> persistenceType = new CIMProperty<UnsignedInteger16>(
				"PersistenceType", CIMDataType.UINT16_T, new UnsignedInteger16(3), false, false,
				null);

		final CIMProperty<?>[] properties = new CIMProperty[] { name, creationClassName,
				systemName, systemCCN, destination, persistenceType };
		final CIMObjectPath path = new CIMObjectPath(null, null, null, pNamespace,
				"CIM_ListenerDestinationCIMXML", null);

		return new CIMInstance(path, properties);
	}

	/**
	 * Constructs a CIM_IndicationFilter instance
	 * 
	 * @param pNamespace
	 *            The scoping namespace
	 * @param pQuery
	 *            The WQL query of the filter
	 * @param pHost
	 *            The host name of the WBEM service's URL
	 * @return The instance
	 * @throws UnknownHostException
	 *             If "localhost" could not be resolved
	 */
	private static CIMInstance makeFilter(String pNamespace, String pQuery, String pHost)
			throws UnknownHostException {
		final CIMProperty<String> name = new CIMProperty<String>("Name", CIMDataType.STRING_T,
				"JSR48SampleFilter" + getNextId(), true, false, null);
		final CIMProperty<String> creationClassName = new CIMProperty<String>("CreationClassName",
				CIMDataType.STRING_T, "CIM_IndicationFilter", true, false, null);
		final CIMProperty<String> systemName = new CIMProperty<String>("SystemName",
				CIMDataType.STRING_T, InetAddress.getByName(pHost).getHostName(), true, false, null);
		final CIMProperty<String> systemCCN = new CIMProperty<String>("SystemCreationClassName",
				CIMDataType.STRING_T, "CIM_ComputerSystem", true, false, null);
		final CIMProperty<String> destination = new CIMProperty<String>("Query",
				CIMDataType.STRING_T, pQuery, false, false, null);
		final CIMProperty<String> persistenceType = new CIMProperty<String>("QueryLanguage",
				CIMDataType.STRING_T, "WQL", false, false, null);
		final CIMProperty<String> sourceNameSpace = new CIMProperty<String>("SourceNamespace",
				CIMDataType.STRING_T, pNamespace, false, false, null);

		final CIMProperty<?>[] properties = new CIMProperty[] { name, creationClassName,
				systemName, systemCCN, destination, persistenceType, sourceNameSpace };
		final CIMObjectPath path = new CIMObjectPath(null, null, null, pNamespace,
				"CIM_IndicationFilter", null);

		return new CIMInstance(path, properties);
	}

	/**
	 * Constructs a CIM_IndicationSubscription association
	 * 
	 * @param pNamespace
	 *            The scoping namespace
	 * @param pDestinationPath
	 *            The path of the handler
	 * @param pFilterPath
	 *            The path of the filter
	 * @return The association instance
	 */
	private static CIMInstance makeSubscription(String pNamespace, CIMObjectPath pDestinationPath,
			CIMObjectPath pFilterPath) {
		final CIMProperty<CIMObjectPath> name = new CIMProperty<CIMObjectPath>("Handler",
				new CIMDataType("CIM_ListenerDestinationCIMXML"), pDestinationPath, true, false,
				null);
		final CIMProperty<CIMObjectPath> creationClassName = new CIMProperty<CIMObjectPath>(
				"Filter", new CIMDataType("CIM_IndicationFilter"), pFilterPath, true, false, null);

		final CIMProperty<?>[] properties = new CIMProperty[] { name, creationClassName };
		final CIMObjectPath path = new CIMObjectPath(null, null, null, pNamespace,
				"CIM_IndicationSubscription", null);

		return new CIMInstance(path, properties);
	}

	/**
	 * Returns a monotonically increasing sequence of integers on subsequent
	 * calls
	 * 
	 * @return An integer
	 */
	private static String getNextId() {
		return String.valueOf(++cId);
	}

	/**
	 * Creates the three CIM instances necessary for making a subscription on
	 * all CIM_InstCreation indication in this namespace
	 * 
	 * @param pClient
	 *            The WBEM client to use
	 * @param pNamespace
	 *            The namespace to subscribe in
	 * @param pHost
	 *            The host name of the WBEM service's URL
	 * @return <code>true</code> if the subscription succeeds,
	 *         <code>false</code> otherwise
	 */
	public static boolean subscribe(WBEMClient pClient, String pNamespace, String pHost) {
		try {

			cDestinationPath = pClient.createInstance(makeListenerDestination(pNamespace, PROTOCOL
					+ "://" + InetAddress.getLocalHost().getHostAddress() + ":" + LISTENER_PORT
					+ "/create", pHost));
			cFilterPath = pClient.createInstance(makeFilter(pNamespace,
					"SELECT * FROM CIM_InstModification", pHost));
			cSubscriptionPath = pClient.createInstance(makeSubscription(pNamespace,
					cDestinationPath, cFilterPath));

			return true;

		} catch (WBEMException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Cleans up the instances of the subscription we've made.
	 * 
	 * @param pClient
	 *            The WBEM client to use
	 * @param pNamespace
	 *            The namespace we've subscribed in
	 */
	private static void unsubscribe(WBEMClient pClient, String pNamespace) {
		if (cSubscriptionPath != null) {
			try {
				pClient.deleteInstance(cSubscriptionPath);
			} catch (WBEMException e) {
				e.printStackTrace();
			}
		}
		if (cFilterPath != null) {
			try {
				pClient.deleteInstance(cFilterPath);
			} catch (WBEMException e) {
				e.printStackTrace();
			}
		}
		if (cDestinationPath != null) {
			try {
				pClient.deleteInstance(cDestinationPath);
			} catch (WBEMException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Runs the sample. Will start a listener, subscribe for InstCreation
	 * indications, create an instance, catch the indication and final clean up
	 * and shut down.
	 * 
	 * @param args
	 *            A String array containing { CIMOM_URL, USER, PASSWORD,
	 *            NAMESPACE }, e.g. { "http://myserver.mydomain.com:5988",
	 *            "user", "pw", "root/interop" }
	 */
	static public void main(String[] args) {
		try {
			// Initialize client. This will not trigger any communication with
			// the CIMOM.
			String host = "http://10.6.178.29:61000";
			final URL serverURL = new URL(host);
			final WBEMClient client = Jsr48OperationSample.connect(serverURL, args[1], args[2]);
			final String namespace = "root/cimv2";

			if (client == null) {
				System.err.println("Client init failed. Probably due to invalid cl parameters.");
				System.err.println("URL: " + host);
				System.err.println("User: " + args[1]);
				System.err.println("Password: " + args[2]);
				System.err.println("Namespace: " + namespace);
				return;
			}

			// start the listener so that we are "on air" when the indications
			// come in
			if (startListener()) {
				System.out.println("Listener started.");
			} else {
				System.err.println("Listener startup failed. Most probably the port "
						+ LISTENER_PORT + " is not available.");
				client.close();
				return;
			}

			try {
				// make the subscription. Since this is the first WBEM operation
				// called, the client will connect to the CIMOM now. If we've
				// any connectivity or authentication problems the WBEMException
				// will be thrown right in the subscribe method.
				if (subscribe(client, namespace, serverURL.getHost())) {
					System.out.println("Successfully subscribed.");
				} else {
					System.err.println("Subscription failed.");
					return;
				}
				// This will trigger an InstCreation indication that is caught
				// by our listener
				CIMInstance dummyInstance = makeFilter(namespace, "SELECT * FROM CIM_InstDeletion",
						serverURL.getHost());
				CIMObjectPath dummyPath = client.createInstance(dummyInstance);
				Thread.sleep(10000);
				// Clean up the instance
				client.deleteInstance(dummyPath);
			} finally {
				// clean up our subscription
				unsubscribe(client, namespace);
				// close listener
				cListener.removeListener(Integer.parseInt(LISTENER_PORT));
				client.close();
				System.out.println("Cleaned up.");
			}

			System.out.println("Sample completed.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
