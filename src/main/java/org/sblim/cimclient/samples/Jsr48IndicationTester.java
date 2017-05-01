/**
 * (C) Copyright IBM Corp. 2012
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * @author : Alexander Wolf-Reber, a.wolf-reber@de.ibm.com
 * @author : Dave Heller, hellerda@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 3529066    2012-07-06  hellerda     Add Jsr48IndicationTester (initial version)
 */
package org.sblim.cimclient.samples;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.cim.CIMArgument;
import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.UnsignedInteger16;
import javax.security.auth.Subject;
import javax.wbem.WBEMException;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.client.WBEMClientFactory;
import javax.wbem.listener.WBEMListener;
import javax.wbem.listener.WBEMListenerFactory;

import org.sblim.cimclient.IndicationListenerSBLIM;
import org.sblim.cimclient.WBEMClientSBLIM;
import org.sblim.cimclient.WBEMListenerSBLIM;

/**
 * Class Jsr48IndicationTester is an interactive, command line program that
 * facilitates testing of end-to-end indication delivery between CIMOM and the
 * SBLIM Java CIM Client. The program starts an indication listener, sends a
 * subscription request to the CIMOM and reports any indications received by the
 * listener. The listener will continue to wait for new indications until a
 * keyboard interrupt is received or the timer expired. An indication can be
 * triggered via the program's trigger-mode, intended to be run from a separate
 * console. An indication may also triggered by some independent means. The
 * program is based on the Java CIM Client Jsr48IndicationSample programs.
 */
public abstract class Jsr48IndicationTester {

	private static CIMObjectPath cSubscriptionPath;

	private static CIMObjectPath cFilterPath;

	private static CIMObjectPath cDestinationPath;

	private static WBEMListener cListener;

	private static int cId = 0;

	/**
	 * Initializes a CIM client connection to a given CIMOM. Note that the
	 * initialization will not lead to client<->CIMOM communication, the first
	 * request will be sent to the CIMOM when the first operation is executed.
	 * 
	 * @param pWbemUrl
	 *            The URL of the WBEM service (e.g.
	 *            <code>https://myhost.mydomain.com:5989</code>)
	 * @param pUser
	 *            The user name for authenticating with the WBEM service
	 * @param pPassword
	 *            The corresponding password
	 * @return A <code>WBEMClient</code> instance if connect was successful,
	 *         <code>null</code> otherwise
	 */
	private static WBEMClient connect(final URL pWbemUrl, final String pUser, final String pPassword) {
		try {
			final WBEMClient client = WBEMClientFactory
					.getClient(WBEMClientConstants.PROTOCOL_CIMXML);
			final CIMObjectPath path = new CIMObjectPath(pWbemUrl.getProtocol(),
					pWbemUrl.getHost(), String.valueOf(pWbemUrl.getPort()), null, null, null);
			final Subject subject = new Subject();
			if (pUser != null && pPassword != null) {
				subject.getPrincipals().add(new UserPrincipal(pUser));
				subject.getPrivateCredentials().add(new PasswordCredential(pPassword));
			}
			client.initialize(path, subject, new Locale[] { Locale.US });
			return client;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Starts a reliable listener. The JSR48 library will open a HTTP(S) server
	 * socket and listen for incoming indications on that socket. Any
	 * indications received will be forwarded to the registered
	 * IndicationListener implementation. The sample one here just prints the
	 * indication to stdout along with a message indicating whether the CIMOM
	 * supports reliable indications.
	 * 
	 * @param pDestURL
	 *            The URL of the destination
	 * @param pPrintInd
	 *            Flag indicating whether to print indication details
	 * 
	 * @return <code>true</code> if the listener could be started,
	 *         <code>false</code> otherwise.
	 */
	public static boolean startListener(URL pDestURL, boolean pPrintInd) {
		try {
			cListener = WBEMListenerFactory.getListener(WBEMClientConstants.PROTOCOL_CIMXML);

			// Cast WBEMListener to WBEMListenerSBLIM to get access to the
			// addListener() method that accepts properties - this method is not
			// part of the JSR48 standard, but is a SBLIM addition
			WBEMListenerSBLIM sListener = (WBEMListenerSBLIM) cListener;

			// Enable reliable indications using 2 retries at intervals of 30
			// seconds
			Properties props = new Properties();
			props.setProperty("sblim.wbem.listenerEnableReliableIndications", "true");
			props.setProperty("sblim.wbem.listenerDeliveryRetryAttempts", "2");
			props.setProperty("sblim.wbem.listenerDeliveryRetryInterval", "30");

			IndicationListenerSBLIM ilsBrief = new IndicationListenerSBLIM() {

				public void indicationOccured(String pIndicationURL, CIMInstance pIndication,
						InetAddress pSenderAddress) {
					System.out.println("Indication received on: " + pIndicationURL + ": from IP: "
							+ pSenderAddress.getHostAddress() + ": classname: "
							+ pIndication.getClassName());
				}
			};
			IndicationListenerSBLIM ilsDetail = new IndicationListenerSBLIM() {

				public void indicationOccured(String pIndicationURL, CIMInstance pIndication,
						InetAddress pSenderAddress) {
					System.out.println("Indication received on: " + pIndicationURL + ": from IP: "
							+ pSenderAddress.getHostAddress());
					try {
						URL parsedURL = new URL(pIndicationURL);
						System.out.println("The URL could be parsed, path is: "
								+ parsedURL.getPath());
					} catch (MalformedURLException e) {
						System.out.println("The URL could NOT be parsed: " + e);
					}
					System.out.println(Jsr48CimSample.toMof(pIndication));
					CIMProperty<?> context = pIndication.getProperty("SequenceContext");
					CIMProperty<?> number = pIndication.getProperty("SequenceNumber");
					System.out.println("Based on content of indication, CIMOM DOES"
							+ (context == null || number == null || context.getValue() == null
									|| number.getValue() == null ? " NOT " : " ")
							+ "support reliable indications.");
				}
			};
			System.out.printf("Added listener on port %d.\n\n", Integer.valueOf(sListener
					.addListener(pPrintInd ? ilsDetail : ilsBrief, pDestURL.getPort(), pDestURL
							.getProtocol(), null, props)));

			return true;

		} catch (IOException e) {
			// nothing to do here
		}
		return false;
	}

	/**
	 * Constructs a CIM_ListenerDestinationCIMXML instance
	 * 
	 * @param pInteropNS
	 *            The scoping namespace
	 * @param pURL
	 *            The URL of the destination
	 * @param pHost
	 *            The host name of the WBEM service's URL
	 * @return The instance
	 * @throws UnknownHostException
	 *             If "localhost" could not be resolved
	 */
	private static CIMInstance makeListenerDestination(String pInteropNS, String pURL, String pHost)
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
		final CIMObjectPath path = new CIMObjectPath(null, null, null, pInteropNS,
				"CIM_ListenerDestinationCIMXML", null);

		return new CIMInstance(path, properties);
	}

	/**
	 * Constructs a CIM_IndicationFilter instance
	 * 
	 * @param pInteropNS
	 *            The scoping namespace
	 * @param pIndicationNS
	 *            The namespace in which the indication provider is registered
	 * @param pQuery
	 *            The WQL query of the filter
	 * @param pHost
	 *            The host name of the WBEM service's URL
	 * @return The instance
	 * @throws UnknownHostException
	 *             If "localhost" could not be resolved
	 */
	private static CIMInstance makeFilter(String pInteropNS, String pIndicationNS, String pQuery,
			String pHost) throws UnknownHostException {
		final CIMProperty<String> name = new CIMProperty<String>("Name", CIMDataType.STRING_T,
				"JSR48SampleFilter" + getNextId(), true, false, null);
		final CIMProperty<String> creationClassName = new CIMProperty<String>("CreationClassName",
				CIMDataType.STRING_T, "CIM_IndicationFilter", true, false, null);
		final CIMProperty<String> systemName = new CIMProperty<String>("SystemName",
				CIMDataType.STRING_T, InetAddress.getByName(pHost).getHostName(), true, false, null);
		final CIMProperty<String> systemCCN = new CIMProperty<String>("SystemCreationClassName",
				CIMDataType.STRING_T, "CIM_ComputerSystem", true, false, null);
		final CIMProperty<String> query = new CIMProperty<String>("Query", CIMDataType.STRING_T,
				pQuery, false, false, null);
		final CIMProperty<String> queryLanguage = new CIMProperty<String>("QueryLanguage",
				CIMDataType.STRING_T, "WQL", false, false, null);
		final CIMProperty<String> sourceNameSpace = new CIMProperty<String>("SourceNamespace",
				CIMDataType.STRING_T, pIndicationNS, false, false, null);

		final CIMProperty<?>[] properties = new CIMProperty[] { name, creationClassName,
				systemName, systemCCN, query, queryLanguage, sourceNameSpace };
		final CIMObjectPath path = new CIMObjectPath(null, null, null, pInteropNS,
				"CIM_IndicationFilter", null);

		return new CIMInstance(path, properties);
	}

	/**
	 * Constructs a CIM_IndicationSubscription association
	 * 
	 * @param pInteropNS
	 *            The scoping namespace
	 * @param pDestinationPath
	 *            The path of the handler
	 * @param pFilterPath
	 *            The path of the filter
	 * @return The association instance
	 */
	private static CIMInstance makeSubscription(String pInteropNS, CIMObjectPath pDestinationPath,
			CIMObjectPath pFilterPath) {
		final CIMProperty<CIMObjectPath> name = new CIMProperty<CIMObjectPath>("Handler",
				new CIMDataType("CIM_ListenerDestinationCIMXML"), pDestinationPath, true, false,
				null);
		final CIMProperty<CIMObjectPath> creationClassName = new CIMProperty<CIMObjectPath>(
				"Filter", new CIMDataType("CIM_IndicationFilter"), pFilterPath, true, false, null);

		final CIMProperty<?>[] properties = new CIMProperty[] { name, creationClassName };
		final CIMObjectPath path = new CIMObjectPath(null, null, null, pInteropNS,
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
	 * @param pInteropNS
	 *            The scoping namespace
	 * @param pIndicationNS
	 *            The namespace in which the indication provider is registered
	 * @param pHost
	 *            The host name of the WBEM service's URL
	 * @param pDestURL
	 *            The URL of the destination
	 * @param pQuery
	 *            The WQL query string for the filter
	 * @return <code>true</code> if the subscription succeeds,
	 *         <code>false</code> otherwise
	 */
	public static boolean subscribe(WBEMClient pClient, String pInteropNS, String pIndicationNS,
			String pHost, URL pDestURL, String pQuery) {
		try {
			cDestinationPath = pClient.createInstance(makeListenerDestination(pInteropNS, pDestURL
					.getProtocol()
					+ "://" + pDestURL.getHost() + ":" + pDestURL.getPort() + "/create", pHost));
			cFilterPath = pClient.createInstance(makeFilter(pInteropNS, pIndicationNS, pQuery,
					pHost));
			cSubscriptionPath = pClient.createInstance(makeSubscription(pInteropNS,
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
	 * Print usage
	 */
	private static void printUsage() {
		System.out.println("Usage: Jsr48IndicationTester [options] provider");
		System.out.println("example: Jsr48IndicationTester -p Test_Indication");
	}

	/**
	 * Print help
	 */
	private static void printHelp() {
		System.out.println("\nOptions:");
		System.out.println("  -h,                   Show this help message and exit.");
		System.out.println("  -p,                   Print received indications to stdout.");
		System.out.println("  -v,                   Print additional debug info.");
		System.out.println("  -t,                   Trigger mode: send a request to the CIMOM");
		System.out.println("                        to trigger an indication and exit.");
		System.out.println("  --cimomUrl CIMOMURL   URL of CIMOM to connect to (host:port)");
		System.out.println("                        (default: http://localhost:5988)");
		System.out.println("  --destUrl DESTURL     URL of destination handler");
		System.out.println("                        (default: http://localhost:7000)");
		System.out
				.println("  --intNS INTEROPNS     Interop namespace name (default: root/interop)");
		System.out
				.println("  --indNS INDICATIONNS  Namespace in which the register the indication");
		System.out
				.println("                        (default is the same value as the interop namespace)");
	}

	/**
	 * Print all the elements of a URL
	 */
	private static void printURL(URL url) {
		System.out.printf("Full URL string   : %s\n", url.toString());
		System.out.printf("Protocol (scheme) : %s\n", url.getProtocol());
		System.out.printf("Authority         : %s\n", url.getAuthority());
		System.out.printf("Host              : %s\n", url.getHost());
		System.out.printf("Port              : %d\n", Integer.valueOf(url.getPort()));
		System.out.printf("UserInfo          : %s\n", url.getUserInfo());
		if (url.getUserInfo() != null) {
			String[] userInfo = url.getUserInfo().split(":");
			System.out.printf("-username         : %s\n", userInfo[0]);
			if (userInfo.length > 1) System.out.printf("-password         : %s\n", userInfo[1]);
		}
		System.out.printf("File              : %s\n", url.getFile());
		System.out.printf("Path              : %s\n", url.getPath());
		System.out.printf("Query             : %s\n", url.getQuery());
		System.out.printf("Ref               : %s\n", url.getRef());
		try {
			System.out.printf("InetAddress.getHostAddress(): %s\n", InetAddress.getByName(
					url.getHost()).getHostAddress());
			System.out.printf("InetAddress.getHostName()   : %s\n", InetAddress.getByName(
					url.getHost()).getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check that namespace is valid, apply default prefix if necessary
	 */
	private static String chkNamespace(String pNamespace) {
		int fields = pNamespace.split("/").length;
		if (fields == 1) return "root/" + pNamespace;
		else if (fields == 2) return pNamespace;
		else {
			throw new IllegalArgumentException("Invalid namespace: " + pNamespace);
		}
	}

	/**
	 * Cleanup at shutdown or keyboard interrupt (ctrl-C).
	 */
	static void cleanup(WBEMClient client, String namespace, URL destURL) {
		unsubscribe(client, namespace);
		cListener.removeListener(destURL.getPort());
		client.close();
		System.out.println("Cleaned up. Sample completed.");
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
			// Parse command line arguments
			List<String> orphanArgs = new ArrayList<String>();
			List<String> singleDashOpts = new ArrayList<String>();
			Map<String, String> doubleDashOpts = new HashMap<String, String>();

			for (int i = 0; i < args.length; i++) {
				if (args[i].charAt(0) == '-') {
					if (args[i].length() < 2) throw new IllegalArgumentException(
							"Invalid argument: " + args[i]);
					if (args[i].charAt(1) == '-') {
						if (i >= args.length - 1) throw new IllegalArgumentException(
								"Expected arg after: " + args[i]);
						if (args[i].length() < 3) throw new IllegalArgumentException(
								"Invalid argument: " + args[i]);
						doubleDashOpts.put(args[i].toLowerCase(), args[i + 1]);
						i++;
					} else {
						if (args[i].length() > 2) throw new IllegalArgumentException(
								"Invalid argument: " + args[i]);
						singleDashOpts.add(args[i]);
					}
				} else {
					orphanArgs.add(args[i]);
				}
			}

			boolean help = singleDashOpts.contains("-h") ? true : false;
			boolean printInd = singleDashOpts.contains("-p") ? true : false;
			boolean trigger = singleDashOpts.contains("-t") ? true : false;
			boolean verbose = singleDashOpts.contains("-v") ? true : false;

			if (help) {
				printUsage();
				printHelp();
				System.exit(0);
			}

			// If indication namespace not specified, use interop namespace
			final String interopNS = doubleDashOpts.containsKey("--intns") ? chkNamespace(doubleDashOpts
					.get("--intns"))
					: "root/interop";
			final String indicationNS = doubleDashOpts.containsKey("--indns") ? chkNamespace(doubleDashOpts
					.get("--indns"))
					: interopNS;

			final URL cimomURL = doubleDashOpts.containsKey("--cimomurl") ? new URL(doubleDashOpts
					.get("--cimomurl")) : new URL("http://localhost:5988");
			final URL destURL = doubleDashOpts.containsKey("--desturl") ? new URL(doubleDashOpts
					.get("--desturl")) : new URL("http://localhost:7000");

			String userInfo = cimomURL.getUserInfo();
			final String cimomUser = (userInfo != null) ? userInfo.split(":")[0] : null;
			final String cimomPasswd = (userInfo != null) ? userInfo.split(":")[1] : null;

			if (orphanArgs.isEmpty()) throw new IllegalArgumentException(
					"No indication classname provided");
			else if (orphanArgs.size() > 1) throw new IllegalArgumentException(
					"Garbled command: too many arguments");
			final String indClassName = orphanArgs.get(0);

			if (verbose) {
				System.out.println("==> Command-line parameters:");
				System.out.println("CIMOM URL: " + cimomURL.toString());
				System.out.println("Destination URL: " + destURL.toString());
				System.out.println("Interop namespace: " + interopNS);
				System.out.println("Indication namespace: " + indicationNS);
				System.out.println("Indication class: " + indClassName);
				System.out.println("==> CIMOM URL detail:");
				printURL(cimomURL);
				System.out.println("==> Destination URL detail:");
				printURL(destURL);
			}

			// Initialize client. This will not trigger any communication with
			// the CIMOM.
			final WBEMClientSBLIM client = (WBEMClientSBLIM) connect(cimomURL, cimomUser,
					cimomPasswd);

			if (client == null) {
				System.err.println("Client init failed. Probably due to invalid cl parameters.");
				printUsage();
				return;
			}

			if (trigger) {
				// SendTestIndication does not use any in/out parameters
				CIMArgument<?>[] input = new CIMArgument[0];
				CIMArgument<?>[] output = new CIMArgument[0];

				// This will trigger a TestIndication that is caught by the
				// remote listener
				Object obj = client.invokeMethod(new CIMObjectPath(null, null, null, indicationNS,
						indClassName, null), "SendTestIndication", input, output);
				if (obj.toString().equals("0")) System.out
						.println("Indication generated successfully.");
				else System.out.println("Indication not generated successfully!");
				System.exit(0);
			}

			// cleanup at shutdown or keyboard interrupt (ctrl-C)
			Runtime.getRuntime().addShutdownHook(new Thread() {

				@Override
				public void run() {
					cleanup(client, interopNS, destURL);
				}
			});

			// start the listener so that we are "on air" when the indications
			// come in
			if (startListener(destURL, printInd)) {
				System.out.println("Listener started.");
			} else {
				System.err.println("Listener startup failed. Most probably the port "
						+ destURL.getPort() + " is not available.");
				client.close();
				return;
			}

			try {
				// make the subscription. Since this is the first WBEM operation
				// called, the client will connect to the CIMOM now. If we've
				// any connectivity or authentication problems the WBEMException
				// will be thrown right in the subscribe method.
				String query = "SELECT * FROM " + indClassName;
				if (subscribe(client, interopNS, indicationNS, cimomURL.getHost(), destURL, query)) {
					System.out.println("Successfully subscribed.");
				} else {
					System.err.println("Subscription failed.");
					return;
				}
				Thread.sleep(300 * 1000);

			} finally {
				// should never get here as we have ShutdownHook to catch exit
			}

		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
			printUsage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
