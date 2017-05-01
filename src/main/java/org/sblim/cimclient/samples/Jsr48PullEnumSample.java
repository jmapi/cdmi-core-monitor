/**
 * (C) Copyright IBM Corp. 2009, 2012
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Ramandeep S Arora, IBM, arorar@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * --------------------------------------------------------------------
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 3267429    2011-04-01  blaschke-oss Samples should close client
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 */
package org.sblim.cimclient.samples;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.UnsignedInteger32;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.EnumerateResponse;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.client.WBEMClientFactory;

/**
 * Class Jsr48PullEnumSample demonstrates how to create a CIM client connection
 * and execute WBEM operations using Pull Enumeration Operations.
 */
public abstract class Jsr48PullEnumSample {

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
	 * An example showing how to get estimated count of remaining enumerations
	 * and close an opened enumeration
	 * 
	 * @param pClient
	 *            : Client to use
	 * @param pNamespace
	 *            : Scoping namespace for the operation
	 * @param pClassName
	 *            : Name of the CIM class
	 */
	public static void countNcloseEnumeration(WBEMClient pClient, String pNamespace,
			String pClassName) {

		try {
			CIMObjectPath pPath = new CIMObjectPath(null, null, null, pNamespace, pClassName, null);

			EnumerateResponse<CIMObjectPath> enumObj = pClient.enumerateInstancePaths(pPath, null,
					null, null, false, new UnsignedInteger32(2));

			// enumerationCount is not supported yet, so you will get an error
			// pClient.enumerationCount(pPath, enumObj.getContext());

			// close an open enumeration session, perform an early
			// termination of an enumeration sequence
			pClient.closeEnumeration(pPath, enumObj.getContext());

		} catch (final WBEMException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Enumerates all instance paths of a given CIM class
	 * 
	 * @param pClient
	 *            : Client to use
	 * @param pNamespace
	 *            : Scoping namespace for the operation
	 * @param pClassName
	 *            : Name of the CIM class
	 * @return A List<CIMObjectPath> containing the instance paths returned by
	 *         the enumeration
	 */
	public static List<CIMObjectPath> enumerateInstancePaths(WBEMClient pClient, String pNamespace,
			String pClassName) {
		try {

			CIMObjectPath pPath = new CIMObjectPath(null, null, null, pNamespace, pClassName, null);
			CloseableIterator<CIMObjectPath> iterator = null;

			// establishes and opens an enumeration session of the instance
			// paths of the instances of a CIM class (including instances of its
			// subclasses) in the target namespace. Optionally, it retrieves a
			// first set of instance paths

			EnumerateResponse<CIMObjectPath> enumObj = pClient.enumerateInstancePaths(pPath, null,
					null, null, false, new UnsignedInteger32(2));

			try {
				List<CIMObjectPath> result = new ArrayList<CIMObjectPath>();
				iterator = enumObj.getResponses();
				printIteratorObj(iterator, result);

				// retrieves instance paths from an open enumeration session
				// represented by an enumeration context value
				while (!enumObj.isEnd()) {
					enumObj = pClient.getInstancePaths(pPath, enumObj.getContext(),
							new UnsignedInteger32(5));
					iterator = enumObj.getResponses();
					printIteratorObj(iterator, result);
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
	 *            : Client to use
	 * @param pNamespace
	 *            : Scoping namespace for the operation
	 * @param pClassName
	 *            : The name of the CIM class
	 * @return A List<CIMInstance> containing the instance paths returned by the
	 *         enumeration
	 */
	public static List<CIMInstance> enumerateInstances(WBEMClient pClient, String pNamespace,
			String pClassName) {
		try {
			CIMObjectPath pPath = new CIMObjectPath(null, null, null, pNamespace, pClassName, null);
			CloseableIterator<CIMInstance> iterator = null;

			// establish and open an enumeration session of the instances of
			// a CIM class (including instances of its subclasses) in the target
			// namespace. Optionally, it retrieves a first set of instances.
			EnumerateResponse<CIMInstance> enumInstance = pClient.enumerateInstances(pPath, true,
					true, null, null, null, null, false, new UnsignedInteger32(1));

			try {
				List<CIMInstance> result = new ArrayList<CIMInstance>();
				iterator = enumInstance.getResponses();
				printIteratorInstance(iterator, result);

				// retrieve instances including their instance paths from
				// an open enumeration session represented by an enumeration
				// context value
				while (!enumInstance.isEnd()) {
					enumInstance = pClient.getInstancesWithPath(pPath, enumInstance.getContext(),
							new UnsignedInteger32(5));
					iterator = enumInstance.getResponses();
					printIteratorInstance(iterator, result);
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
	 *            : Client to use
	 * @param pPath
	 *            : Instance path
	 * @param pAssociationClass
	 *            : Class name of the association to traverse
	 * @param pResultClass
	 *            The class name of the CIM class to return
	 * @return A List<CIMInstance> of the instances of the result class
	 */
	public static List<CIMInstance> getAssociatorInstances(WBEMClient pClient, CIMObjectPath pPath,
			String pAssociationClass, String pResultClass) {
		try {
			CloseableIterator<CIMInstance> iterator = null;

			// establish and open an enumeration session of the instances of
			// a CIM class (including instances of its subclasses) in the target
			// namespace. Optionally, it retrieves a first set of instances.
			EnumerateResponse<CIMInstance> enumInstance = pClient.associators(pPath, null, null,
					null, null, false, null, null, null, null, false, new UnsignedInteger32(2));

			try {
				List<CIMInstance> result = new ArrayList<CIMInstance>();
				iterator = enumInstance.getResponses();
				printIteratorInstance(iterator, result);

				// retrieves instance paths from an open enumeration session
				// represented by an enumeration context value
				while (!enumInstance.isEnd()) {
					enumInstance = pClient.getInstancesWithPath(pPath, enumInstance.getContext(),
							new UnsignedInteger32(5));
					iterator = enumInstance.getResponses();
					printIteratorInstance(iterator, result);
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
	 *            : Client to use
	 * @param pPath
	 *            : Instance path
	 * @param pAssociationClass
	 *            : Class name of the association to traverse
	 * @param pResultClass
	 *            : Class name of the CIM class to return
	 * @return A List<CIMObjectPath> of the instance paths of the result class
	 */
	public static List<CIMObjectPath> getAssociatorNames(WBEMClient pClient, CIMObjectPath pPath,
			String pAssociationClass, String pResultClass) {
		try {
			CloseableIterator<CIMObjectPath> iterator = null;

			EnumerateResponse<CIMObjectPath> enumObj = pClient.associatorPaths(pPath, null, null,
					null, null, null, null, null, false, new UnsignedInteger32(3));
			try {
				List<CIMObjectPath> result = new ArrayList<CIMObjectPath>();
				iterator = enumObj.getResponses();
				printIteratorObj(iterator, result);

				// retrieves instance paths from an open enumeration session
				// represented by an enumeration context value
				while (!enumObj.isEnd()) {
					enumObj = pClient.getInstancePaths(pPath, enumObj.getContext(),
							new UnsignedInteger32(5));
					iterator = enumObj.getResponses();
					printIteratorObj(iterator, result);
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
	 * Traverses association instances that have references that refer to the
	 * instance defined in the instancePath parameter
	 * 
	 * @param pClient
	 *            : Client to use
	 * @param pPath
	 *            : Instance path
	 * @param pResultClass
	 *            : Class name of the CIM class to return
	 * @return A List<CIMInstance> of the instances of the result class
	 */
	public static List<CIMInstance> getReferenceInstances(WBEMClient pClient, CIMObjectPath pPath,
			String pResultClass) {

		try {

			CloseableIterator<CIMInstance> iterator = null;

			// establish and open an enumeration session of the instances of
			// a CIM class (including instances of its subclasses) in the target
			// namespace. Optionally, it retrieves a first set of instances.
			EnumerateResponse<CIMInstance> enumInstance = pClient.references(pPath, null, null,
					false, null, null, null, null, false, new UnsignedInteger32(2));

			try {
				List<CIMInstance> result = new ArrayList<CIMInstance>();
				iterator = enumInstance.getResponses();
				printIteratorInstance(iterator, result);

				// retrieves instance paths from an open enumeration session
				// represented by an enumeration context value
				while (!enumInstance.isEnd()) {
					enumInstance = pClient.getInstancesWithPath(pPath, enumInstance.getContext(),
							new UnsignedInteger32(5));
					iterator = enumInstance.getResponses();
					printIteratorInstance(iterator, result);
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
	 * Traverses association instance paths that have references that refer to
	 * the instance defined in the instancePath parameter
	 * 
	 * @param pClient
	 *            : Client to use
	 * @param pPath
	 *            : Instance path
	 * @param pResultClass
	 *            : Class name of the association to traverse
	 * @return A List<CIMObjectPath> of the instance paths of the traversed
	 *         association
	 */
	public static List<CIMObjectPath> getReferenceNames(WBEMClient pClient, CIMObjectPath pPath,
			String pResultClass) {
		try {
			CloseableIterator<CIMObjectPath> iterator = null;

			EnumerateResponse<CIMObjectPath> enumObj = pClient.referencePaths(pPath, null, null,
					null, null, new UnsignedInteger32(30), false, new UnsignedInteger32(3));
			try {
				List<CIMObjectPath> result = new ArrayList<CIMObjectPath>();
				iterator = enumObj.getResponses();
				printIteratorObj(iterator, result);

				// retrieves instance paths from an open enumeration session
				// represented by an enumeration context value
				while (!enumObj.isEnd()) {
					enumObj = pClient.getInstancePaths(pPath, enumObj.getContext(),
							new UnsignedInteger32(5));
					iterator = enumObj.getResponses();
					printIteratorObj(iterator, result);
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
	 * Executes a few tests emphasizing on Pull Enumeration Operations.
	 * 
	 * This should work on every CIMOM that implements SMIS Server profile and
	 * has support for Pull Operations.
	 * 
	 * @param args
	 *            A String array containing { CIMOM_URL, USER, PASSWORD,
	 *            NAMESPACE }, e.g. { "http://myserver.mydomain.com:5988",
	 *            "user", "pw", "root/interop" }
	 * 
	 *            e.g. arguments using eclipse { http://localhost:5988 user
	 *            passwd root/interop }
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Initialize client.
		// This will not trigger any communication with the CIMOM.
		final WBEMClient client = Jsr48PullEnumSample.connect(new URL(args[0]), args[1], args[2]);
		final String namespace = args[3];

		if (client == null) {
			System.err.println("Client init failed. Probably due to invalid cl parameters.");
			System.err.println("URL: " + args[0]);
			System.err.println("User: " + args[1]);
			System.err.println("Password: " + args[2]);
			System.err.println("Namespace: " + args[3]);
			return;
		}

		System.out.println("Execute first WBEM operation. If we have any connectivity "
				+ "problem with the CIMOM, we die exactly here. \n ");

		// enumerationCount and Close Enumeration example
		countNcloseEnumeration(client, namespace, CIM_REGISTERED_PROFILE);

		System.out.println("Enumerate registered profiles as instance paths :-");
		enumerateInstancePaths(client, namespace, CIM_REGISTERED_PROFILE);

		System.out.println("\nEnumerate registered profiles as full instances :-");
		final List<CIMInstance> profileInstances = enumerateInstances(client, namespace,
				CIM_REGISTERED_PROFILE);

		System.out.println("\nStep through the registered profile instances "
				+ "and look for the 'SERVER' profile  :- \n");
		if (profileInstances != null) {
			for (int i = 0; i < profileInstances.size(); ++i) {
				final CIMInstance instance = profileInstances.get(i);
				if (instance.getProperty("RegisteredName").getValue().toString().equalsIgnoreCase(
						"server")
						&& ((Number) instance.getProperty("RegisteredOrganization").getValue())
								.intValue() == 11) {

					System.out.println("Traverse reference instance paths :- ");
					getReferenceNames(client, instance.getObjectPath(),
							"CIM_ElementConformsToProfile");

					System.out.println("\nTraverse reference instances :- ");
					getReferenceInstances(client, instance.getObjectPath(),
							"CIM_ElementConformsToProfile");

					System.out.println("\nTraverse associator instance paths :- ");
					getAssociatorNames(client, instance.getObjectPath(),
							"CIM_ElementConformsToProfile", "CIM_ManagedElement");

					System.out.println("\nTraverse associator instances :- ");
					getAssociatorInstances(client, instance.getObjectPath(),
							"CIM_ElementConformsToProfile", "CIM_ManagedElement");
				} // if (instance.getProperty("RegisteredName")
			}// for loop profileInstances.size()
		}// if (profileInstances != null)
		client.close();
	} // main

	private static void printIteratorInstance(CloseableIterator<CIMInstance> iterator,
			List<CIMInstance> result) {
		int i = 0;
		while (iterator.hasNext()) {
			final CIMInstance instance = iterator.next();
			result.add(instance);
			System.out.println("Instance # " + ++i + " : " + Jsr48CimSample.toMof(instance));
		}
	}

	private static void printIteratorObj(CloseableIterator<CIMObjectPath> iterator,
			List<CIMObjectPath> result) {
		int i = 0;
		while (iterator.hasNext()) {
			final CIMObjectPath path = iterator.next();
			result.add(path);
			System.out.println("ObjectPath # " + ++i + " : " + Jsr48CimSample.toMof(path));
		}
	}
}
