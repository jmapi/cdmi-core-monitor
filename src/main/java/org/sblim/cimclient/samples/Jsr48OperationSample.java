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
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 2963502    2010-03-04  blaschke-oss Add XML tracing to sample code
 * 3190335    2011-02-23  blaschke-oss Erroneous use of SystemName property in samples
 * 3267429    2011-04-01  blaschke-oss Samples should close client
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
 */
package org.sblim.cimclient.samples;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.cim.CIMClass;
import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.UnsignedInteger16;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.client.WBEMClientFactory;

/**
 * Class Jsr48OperationSample demonstrates how to create a CIM client connection
 * and execute WBEM operations.
 * 
 */
public abstract class Jsr48OperationSample {

	private static final String CIM_REGISTERED_PROFILE = "CIM_RegisteredProfile";

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
	public static WBEMClient connect(final URL pWbemUrl, final String pUser, final String pPassword) {
		try {
			final WBEMClient client = WBEMClientFactory
					.getClient(WBEMClientConstants.PROTOCOL_CIMXML);
			final CIMObjectPath path = new CIMObjectPath(pWbemUrl.getProtocol(),
					pWbemUrl.getHost(), String.valueOf(pWbemUrl.getPort()), null, null, null);
			final Subject subject = new Subject();
			subject.getPrincipals().add(new UserPrincipal(pUser));
			subject.getPrivateCredentials().add(new PasswordCredential(pPassword));
			client.initialize(path, subject, new Locale[] { Locale.US });
			return client;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Enumerates all instances of a given CIM class
	 * 
	 * @param pClient
	 *            The client to use
	 * @param pNamespace
	 *            The scoping namespace for the operation
	 * @param pClassName
	 *            The name of the CIM class
	 * @return A List<CIMObjectPath> containing the instance paths returned by
	 *         the enumeration
	 */
	public static List<CIMObjectPath> enumerateInstanceNames(WBEMClient pClient, String pNamespace,
			String pClassName) {
		try {
			final CloseableIterator<CIMObjectPath> iterator = pClient
					.enumerateInstanceNames(new CIMObjectPath(null, null, null, pNamespace,
							pClassName, null));
			try {
				final List<CIMObjectPath> result = new ArrayList<CIMObjectPath>();
				while (iterator.hasNext()) {
					final CIMObjectPath path = iterator.next();
					result.add(path);
					System.out.println(Jsr48CimSample.toMof(path));
				}
				return result;
			} finally {
				iterator.close();
			}
		} catch (final WBEMException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Enumerates all instances of a given CIM class
	 * 
	 * @param pClient
	 *            The client to use
	 * @param pNamespace
	 *            The scoping namespace for the operation
	 * @param pClassName
	 *            The name of the CIM class
	 * @return A List<CIMInstance> containing the instance paths returned by the
	 *         enumeration
	 */
	public static List<CIMInstance> enumerateInstances(WBEMClient pClient, String pNamespace,
			String pClassName) {
		try {
			final CloseableIterator<CIMInstance> iterator = pClient.enumerateInstances(
					new CIMObjectPath(null, null, null, pNamespace, pClassName, null), true, false,
					false, null);
			try {
				final List<CIMInstance> result = new ArrayList<CIMInstance>();
				while (iterator.hasNext()) {
					final CIMInstance instance = iterator.next();
					result.add(instance);
					System.out.println(Jsr48CimSample.toMof(instance));
				}
				return result;
			} finally {
				iterator.close();
			}
		} catch (final WBEMException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retrieves a CIM instance for a given instance path
	 * 
	 * @param pClient
	 *            The client to use
	 * @param pPath
	 *            The instance path
	 * @return The instance
	 */
	public static CIMInstance getInstance(WBEMClient pClient, CIMObjectPath pPath) {
		try {
			final CIMInstance instance = pClient.getInstance(pPath, false, false, null);
			System.out.println(Jsr48CimSample.toMof(instance));
			return instance;
		} catch (final WBEMException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Traverses an association from a given instance path. Returns the
	 * association itself.
	 * 
	 * @param pClient
	 *            The client to use
	 * @param pPath
	 *            The instance path
	 * @param pResultClass
	 *            The class name of the association to traverse
	 * @return A List<CIMObjectPath> of the instance paths of the traversed
	 *         association
	 */
	public static List<CIMObjectPath> getReferenceNames(WBEMClient pClient, CIMObjectPath pPath,
			String pResultClass) {
		try {
			final CloseableIterator<CIMObjectPath> iterator = pClient.referenceNames(pPath,
					pResultClass, null);
			try {
				final List<CIMObjectPath> result = new ArrayList<CIMObjectPath>();
				while (iterator.hasNext()) {
					final CIMObjectPath element = iterator.next();
					result.add(element);
					System.out.println(Jsr48CimSample.toMof(element));
				}
				return result;
			} finally {
				iterator.close();
			}
		} catch (final WBEMException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Traverses an association from a given instance path to a given result
	 * class.
	 * 
	 * @param pClient
	 *            The client to use
	 * @param pPath
	 *            The instance path
	 * @param pAssociationClass
	 *            The class name of the association to traverse
	 * @param pResultClass
	 *            The class name of the CIM class to return
	 * @return A List<CIMObjectPath> of the instance paths of the result class
	 */
	public static List<CIMObjectPath> getAssociatorNames(WBEMClient pClient, CIMObjectPath pPath,
			String pAssociationClass, String pResultClass) {
		try {
			final CloseableIterator<CIMObjectPath> associators = pClient.associatorNames(pPath,
					pAssociationClass, pResultClass, null, null);
			try {
				final List<CIMObjectPath> result = new ArrayList<CIMObjectPath>();
				while (associators.hasNext()) {
					final CIMObjectPath element = associators.next();
					result.add(element);
					System.out.println(Jsr48CimSample.toMof(element));
				}
				return result;
			} finally {
				associators.close();
			}
		} catch (final WBEMException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a dummy instance in the given namespace.
	 * 
	 * @param pClient
	 *            The client to use
	 * @param pNamespace
	 *            The namespace
	 * @param pHost
	 *            The host name of the WBEM service's URL
	 * @return The path of the created instance
	 * @throws WBEMException
	 */
	public static CIMObjectPath createInstance(WBEMClient pClient, String pNamespace, String pHost)
			throws WBEMException {

		try {

			CIMProperty<String> name = new CIMProperty<String>("Name", CIMDataType.STRING_T,
					"TestÄöüListener", true, false, null);
			CIMProperty<String> creationClassName = new CIMProperty<String>("CreationClassName",
					CIMDataType.STRING_T, "CIM_ListenerDestinationCIMXML", true, false, null);
			CIMProperty<String> systemName = new CIMProperty<String>("SystemName",
					CIMDataType.STRING_T, InetAddress.getByName(pHost).getHostName(), true, false,
					null);
			CIMProperty<String> systemCCN = new CIMProperty<String>("SystemCreationClassName",
					CIMDataType.STRING_T, "CIM_ComputerSystem", true, false, null);
			CIMProperty<String> destination = new CIMProperty<String>("Destination",
					CIMDataType.STRING_T, "http://www.xyz.org:5990/test", false, false, null);
			CIMProperty<UnsignedInteger16> persistenceType = new CIMProperty<UnsignedInteger16>(
					"PersistenceType", CIMDataType.UINT16_T, new UnsignedInteger16(3), false,
					false, null);

			CIMProperty<?>[] properties = new CIMProperty[] { name, creationClassName, systemName,
					systemCCN, destination, persistenceType };
			CIMObjectPath path = new CIMObjectPath(null, null, null, pNamespace,
					"CIM_ListenerDestinationCIMXML", null);

			CIMInstance instance = new CIMInstance(path, properties);
			return pClient.createInstance(instance);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retrieves the class definition of a given CIM class
	 * 
	 * @param pClient
	 *            The client to use
	 * @param pNamespace
	 *            The scoping namespace
	 * @param pClassName
	 *            The name of the CIM class
	 * @return The CIM class definition
	 */
	public static CIMClass getClass(WBEMClient pClient, String pNamespace, String pClassName) {
		final CIMObjectPath path = new CIMObjectPath(null, null, null, pNamespace, pClassName, null);
		try {
			final CIMClass cimClass = pClient.getClass(path, false, true, true, null);
			System.out.println(Jsr48CimSample.toMof(cimClass));
			return cimClass;
		} catch (final WBEMException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Executes a few simple tests. Should work on every CIMOM that implements
	 * the SMIS Server profile.
	 * 
	 * @param args
	 *            A String array containing { CIMOM_URL, USER, PASSWORD,
	 *            NAMESPACE }, e.g. { "http://myserver.mydomain.com:5988",
	 *            "user", "pw", "root/interop" }
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Uncomment following two lines to enable writing XML trace to stdout.
		// System.setProperty("sblim.wbem.cimxmlTracing", "true");
		// LogAndTraceManager.getManager().setXmlTraceStream(System.out);

		// Uncomment following lines and corresponding removeCIMXMLTraceListener
		// call below to enable writing XML trace to stdout.
		// CIMXMLTraceListener xmlListener = new CIMXMLTraceListener() {
		// public void traceCIMXML(Level pLevel, String pMessage, boolean
		// pOutgoing) {
		// System.out.println("client" + (pOutgoing ? "->" : "<-") + "server: "
		// + pMessage);
		// }
		// };
		// LogAndTraceManager.getManager().addCIMXMLTraceListener(xmlListener);

		// Initialize client. This will not trigger any communication with
		// the CIMOM.
		final URL serverURL = new URL(args[0]);
		final WBEMClient client = Jsr48OperationSample.connect(serverURL, args[1], args[2]);
		final String namespace = args[3];

		if (client == null) {
			System.err.println("Client init failed. Probably due to invalid cl parameters.");
			System.err.println("URL: " + args[0]);
			System.err.println("User: " + args[1]);
			System.err.println("Password: " + args[2]);
			System.err.println("Namespace: " + args[3]);
			return;
		}

		// Execute first WBEM operation. If we have any connectivity problem
		// with the CIMOM, we die exactly here.
		getClass(client, namespace, CIM_REGISTERED_PROFILE);

		// Enumerate registered profiles as instance paths
		enumerateInstanceNames(client, namespace, CIM_REGISTERED_PROFILE);

		// Enumerate registered profiles as full instances
		final List<CIMInstance> profileInstances = enumerateInstances(client, namespace,
				CIM_REGISTERED_PROFILE);

		// Step through the registered profile instances and look for the
		// "SERVER" profile
		if (profileInstances != null) {
			for (int i = 0; i < profileInstances.size(); ++i) {
				final CIMInstance instance = profileInstances.get(i);
				if (instance.getProperty("RegisteredName").getValue().toString().equalsIgnoreCase(
						"server")
						&& ((Number) instance.getProperty("RegisteredOrganization").getValue())
								.intValue() == 11) {
					// Traverse association and list association paths
					getReferenceNames(client, instance.getObjectPath(),
							"CIM_ElementConformsToProfile");
					// Traverse association and list result class paths
					List<CIMObjectPath> associators = getAssociatorNames(client, instance
							.getObjectPath(), "CIM_ElementConformsToProfile", "CIM_ManagedElement");
					if (associators != null) {
						for (int j = 0; j < associators.size(); ++j) {
							// Get full instance
							getInstance(client, associators.get(0));
						}
					}
				}
			}
		}

		// create a dummy instance
		CIMObjectPath path = createInstance(client, namespace, serverURL.getHost());
		System.out.println(Jsr48CimSample.toMof(path));
		// try to retrieve the instance created before
		getInstance(client, path);
		// delete the instance again
		client.deleteInstance(path);
		System.out.println("Instance " + path + " deleted");
		// LogAndTraceManager.getManager().removeCIMXMLTraceListener(xmlListener);
		client.close();
	}
}
