package pw.cdmi.om.drive.sims;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.cim.CIMArgument;
import javax.cim.CIMClass;
import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.UnsignedInteger32;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.client.WBEMClientFactory;
import javax.wbem.listener.IndicationListener;
import javax.wbem.listener.WBEMListener;
import javax.wbem.listener.WBEMListenerFactory;

import org.sblim.cimclient.WBEMConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.mp.SnmpConstants;

import pw.cdmi.om.protocol.snmp.SNMPClient;
import pw.cdmi.om.protocol.snmp.SNMPTarget;

public class QuantumScalarSMIClient {
	private static List<String> mofiles = new ArrayList<String>();
	{
		mofiles.add("SMIS_ComputerSystem");
		mofiles.add("SMIS_ComponentCS");
		mofiles.add("SMIS_Chassis");
		mofiles.add("SMIS_Product");
		mofiles.add("SMIS_SoftwareIdentity");
		mofiles.add("SMIS_ConfigurationCapacity");
		mofiles.add("SMIS_FCPort");
		mofiles.add("SMIS_SCSIProtocolController");
		mofiles.add("SMIS_InstalledSoftwareIdentity");
		mofiles.add("SMIS_ElementConformsToProfile");
		mofiles.add("SMIS_HostedService");
		mofiles.add("SMIS_RegisteredProfile");
		mofiles.add("SMIS_RegisteredSubProfile");
		mofiles.add("SMIS_ObjectManager");
		mofiles.add("SMIS_ComputerSystemPackage");
		mofiles.add("SMIS_ProductPhysicalComponent");
		mofiles.add("SMIS_ElementCapacity");
		mofiles.add("SMIS_CommMechanismForManager");
		mofiles.add("SMIS_CIMXMLCommunicationMechanism");
		mofiles.add("SMIS_SystemDevice");
		mofiles.add("SMIS_Namespace");
		mofiles.add("SMIS_NamespaceInManager");
		mofiles.add("SMIS_Realizes");
		mofiles.add("SMIS_ChangerDevice");
		mofiles.add("SMIS_TapeDrive");
		mofiles.add("SMIS_Magazine");
		mofiles.add("SMIS_StorageMediaLocation");
		mofiles.add("SMIS_PhysicalTape");
		mofiles.add("SMIS_PhysicalMediaInLocation");
		mofiles.add("SMIS_PhysicalPackage");
		mofiles.add("SMIS_Container");
		mofiles.add("SMIS_PackageInChassis");
		mofiles.add("SMIS_ProtocolControllerForUnit");
		mofiles.add("SMIS_ProtocolControllerForPort");
		mofiles.add("SMIS_PackagedComponent");
		mofiles.add("SMIS_ElementSoftwareIdentity");
		mofiles.add("SMIS_LimitedAccessPort");
		mofiles.add("SMIS_Location");
		mofiles.add("SMIS_PhysicalElementLocation");
		mofiles.add("SMIS_RemoteServiceAccessPoint");
		mofiles.add("SMIS_HostedAccessPoint");
		mofiles.add("SMIS_SubProfileRequiresProfile");
		mofiles.add("SMIS_ObjectManagerAdapter");
		mofiles.add("SMIS_CommMechanismForObjectManagerAdapter");
		mofiles.add("SMIS_SAPAvailableForElement");
		mofiles.add("SMIS_SCSIProtocolEndpoint");
		mofiles.add("SMIS_PortImplementsEndpoint");
		mofiles.add("SMIS_DeviceServicesLocation");
		mofiles.add("SMIS_ConfigurationReportingService");
		mofiles.add("SMIS_StorageHardwareID");
		mofiles.add("SMIS_AuthorizedPrivilege");
		mofiles.add("SMIS_AuthorizedSubject");
		mofiles.add("SMIS_AuthorizedTarget");
		mofiles.add("SMIS_ProtocolControllerMaskingCapabilities");
		mofiles.add("SMIS_StorageClientSettingData");
		mofiles.add("SMIS_ElementSettingData");
		mofiles.add("SMIS_ElementCapabilities");
		mofiles.add("SMIS_SASPort");
		mofiles.add("SMIS_SPIPort");
	}
	
	private static Logger logger = LoggerFactory.getLogger(QuantumScalarSMIClient.class);

	private WBEMClient client;

	private String port;

	private String host;

	private String protocal;

	private String namespace;

	class MyListener implements IndicationListener {
		public void indicationOccured(String pIndicationURL, CIMInstance pIndication) {
			CIMInstance indicationInstance = pIndication;
			System.out.println("Received indication instance: " + indicationInstance);
		}
	}

	public QuantumScalarSMIClient(String root, String username, String password) {
		// String host = "http://localhost:5988/cimv2";
		URL url = null;
		try {
			url = new URL(root);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		// Create an object path using the host variable.
		this.protocal = url.getProtocol();
		this.port = String.valueOf(url.getPort());
		this.host = url.getHost();
		this.namespace = url.getPath();
		this.namespace = (this.namespace.startsWith("/")) ? this.namespace.substring(1) : this.namespace;
		CIMObjectPath cns = new CIMObjectPath(this.protocal, this.host, this.port, null, null, null);

		// Create the principal - used for authentication/authorization
		UserPrincipal up = new UserPrincipal(username);
		// Create the credential - used for authentication/authorization
		PasswordCredential pc = new PasswordCredential(password);
		// Add the principal and credential to the subject.
		Subject s = new Subject();
		s.getPrincipals().add(up);
		s.getPrivateCredentials().add(pc);
		try {
			// Create a CIM client connection using the either CIM-XML or WS-Management protocol
			client = WBEMClientFactory.getClient(WBEMClientConstants.PROTOCOL_CIMXML);
			// Set http timeout property with the value 3 minutes
			client.setProperty(WBEMConfigurationProperties.HTTP_TIMEOUT, String.valueOf(180 * 1000));
			client.initialize(cns, s, Locale.getAvailableLocales());
		} catch (WBEMException e) {
			e.printStackTrace();
		}
	}

	public void registerListener(String url) {
		// get the WBEMListener from the WBEMListenerFactory
		WBEMListener api = WBEMListenerFactory.getListener(WBEMClientConstants.PROTOCOL_CIMXML);
		// add the listener to the port. use 0 to specify any available port
		// create the filter
		String filter = "SELECT SELECT * from CIM_InstIndication WHERE sourceInstance ISA SMIS_ComputerSystem";
		CIMObjectPath op = new CIMObjectPath(url);
		try {
			int listen_port = api.addListener(new MyListener(), 0, WBEMClientConstants.PROTOCOL_CIMXML);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// get wbem client obj with specified protocol

		// CIMObjectPath op = new CIMObjectPath("http","CIM_IndicationFilter", "cimv2");
		try {
			CIMClass filterClass = client.getClass(op, false, true, false, null);
			CIMInstance filterInstance = filterClass.newInstance();
			CIMProperty<?>[] fcpArray = {
					new CIMProperty<String>("Query", CIMDataType.STRING_T, filter, false, false, null),
					new CIMProperty<String>("QueryLanguage", CIMDataType.STRING_T, "WQL", false, false, null) };
			filterInstance = filterInstance.deriveInstance(fcpArray);
			CIMObjectPath filterOP = client.createInstance(filterInstance);
			// create the listener
			op = new CIMObjectPath(this.protocal, this.host, this.port, this.namespace,
				"CIM_ListenerDestinationCIMXML", null);
			CIMProperty<?>[] cpa = { new CIMProperty<String>("Destination", CIMDataType.STRING_T, url, false, false,
				null) };
			// create new instance of listener
			CIMInstance listenerInstance = new CIMInstance(op, cpa);
			CIMObjectPath listenerOP = client.createInstance(listenerInstance);
			// create a subscription, an association between the filter and listener.
			CIMProperty<?>[] sicpArray = {
					new CIMProperty<CIMObjectPath>("Filter", new CIMDataType(filterOP.getObjectName()), filterOP, true,
						false, null),
					new CIMProperty<CIMObjectPath>("Handler", new CIMDataType(listenerOP.getObjectName()), listenerOP,
						true, false, null) };
			CIMInstance subscriptionInstance = new CIMInstance(new CIMObjectPath(this.protocal, this.host, this.port,
				this.namespace, "CIM_IndicationSubscription", sicpArray), sicpArray);
			CIMObjectPath subscriptionOP = client.createInstance(subscriptionInstance);

		} catch (WBEMException e) {
			e.printStackTrace();
		}
	}

	public void enumerateInstanceNames(String className) {
		logger.info("执行enumerateInstanceNames");
		CIMObjectPath cop = new CIMObjectPath(this.protocal, this.host, this.port, this.namespace, className, null);
		try {
			CloseableIterator<CIMObjectPath> ei = client.enumerateInstanceNames(cop);
			try {
				if (ei != null) {
					while (ei.hasNext()) {
						// Take your action to the element from next().
						Object next = ei.next();
						if (next instanceof CIMObjectPath)
							System.out.println("path:" + ((CIMObjectPath) next).toString());
						associatorInstances((CIMObjectPath) next, null, null);
					}
				}
			} catch (Exception e) {
				// Throw the WBEMException.
				throw ei.getWBEMException();
			} finally {
				// Make sure CloseableIterator.close() is called.
				ei.close();
			}
		} catch (WBEMException e) {
			e.printStackTrace();
		}
	}

	public void enumerateInstances(String className) {
		// get wbem client obj with specified protocol
		// pass the CIM class
		logger.info("执行enumerateInstances");
		CIMObjectPath cop = new CIMObjectPath(this.protocal, this.host, this.port, this.namespace, className, null);
		try {
			CloseableIterator<CIMInstance> ei = client.enumerateInstances(cop, true, true, true, null);
			try {
				if (ei != null) {
					while (ei.hasNext()) {
						// Take your action to the element from next().
						Object next = ei.next();
						if (next instanceof CIMInstance)
							System.out.println("instance:" + ((CIMInstance) next).toString());
					}
				}
			} catch (Exception e) {
				// Throw the WBEMException.
				throw ei.getWBEMException();
			} finally {
				// Make sure CloseableIterator.close() is called.
				ei.close();
			}
		} catch (WBEMException e) {
			e.printStackTrace();
		}
	}

	public void associatorNames(String className, String pAssociationClass, String resultClass) {
		logger.info("执行associatorNames");
		CIMObjectPath cop = new CIMObjectPath(this.protocal, this.host, this.port, this.namespace, className, null);
		try {
			CloseableIterator<CIMObjectPath> ei = client.associatorNames(cop, null, resultClass, null, null);
			try {
				if (ei != null) {
					while (ei.hasNext()) {
						// Take your action to the element from next().
						Object next = ei.next();
						if (next instanceof CIMObjectPath)
							System.out.println("path:" + ((CIMObjectPath) next).toString());

					}
				}
			} catch (Exception e) {
				// Throw the WBEMException.
				throw ei.getWBEMException();
			} finally {
				// Make sure CloseableIterator.close() is called.
				ei.close();
			}
		} catch (WBEMException e) {
			e.printStackTrace();
		}
	}

	public void extrinsicMethods(String CIM_classname, String methodname, Map<String, String> in,
		Map<String, String> out) {
		CIMObjectPath cop = new CIMObjectPath(this.protocal, this.host, this.port, this.namespace, CIM_classname, null);
		CIMArgument<?>[] inArgs = { new CIMArgument<String>("Destination", CIMDataType.STRING_T, null) };
		CIMArgument<?>[] outArgs = { new CIMArgument<String>("Destination", CIMDataType.STRING_T, null) };
		try {
			UnsignedInteger32 uInt32 = (UnsignedInteger32) client.invokeMethod(cop, methodname, inArgs, outArgs);
		} catch (WBEMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列举了指定的源CIM实例的关联CIM实例
	 * 
	 * @param className
	 * @param resultClass
	 */
	public void associatorInstances(CIMObjectPath pObject, String pAssociationClass, String resultClass) {
		logger.info("执行associatorInstances");
		try {
			CloseableIterator<CIMInstance> ei = client.associatorInstances(pObject, pAssociationClass, resultClass,
				null, null, false, null);
			try {
				if (ei != null) {
					while (ei.hasNext()) {
						// Take your action to the element from next().
						Object next = ei.next();
						if (next instanceof CIMInstance)
							System.out.println("instance:" + ((CIMInstance) next).toString());
					}
				}
			} catch (Exception e) {
				// Throw the WBEMException.
				throw ei.getWBEMException();
			} finally {
				// Make sure CloseableIterator.close() is called.
				ei.close();
			}
		} catch (WBEMException e) {
			System.out.println(e);
		}
	}

	/**
	 * 列举了指定的源CIM实例的关联CIM实例
	 * 
	 * @param className
	 * @param resultClass
	 */
	public void associatorInstances(String CIM_classname, String pAssociationClass, String resultClass) {
		CIMObjectPath cop = new CIMObjectPath(this.protocal, this.host, this.port, this.namespace, CIM_classname, null);
		logger.info("执行associatorInstances");
		try {
			CloseableIterator<CIMInstance> ei = client.associatorInstances(cop, pAssociationClass, resultClass, null,
				null, false, null);
			try {
				if (ei != null) {
					while (ei.hasNext()) {
						// Take your action to the element from next().
						Object next = ei.next();
						if (next instanceof CIMInstance)
							System.out.println("path:" + ((CIMInstance) next).toString());
					}
				}
			} catch (Exception e) {
				// Throw the WBEMException.
				throw ei.getWBEMException();
			} finally {
				// Make sure CloseableIterator.close() is called.
				ei.close();
			}
		} catch (WBEMException e) {
			System.out.println(e);
		}
	}

//	public void LibraryCapacity() {
//
//	}

	public static void main(String[] agx) throws IOException {

		SNMPTarget target1 = new SNMPTarget();
		target1.setTargetIp("10.6.178.29");
		target1.setPort(161);
		target1.setSnmpVersion(SnmpConstants.version2c);
		target1.setReadCommunity("public");
		SNMPClient snmp_client = new SNMPClient(target1);
		boolean isSuccess = snmp_client.testConnected();

		System.out.println("连接状态：" + isSuccess);
		String url = "http://10.6.178.29:5988/root/cimv2";
		// String url = "http://10.6.178.29:61000/cimv2";
		String username = "smiuser";
		String password = "smipassword";

		String result = "CIM_System";
		String pAssociationClass = "CIM_Service";
		String target = "SMIS_StorageMediaLocation";
		QuantumScalarSMIClient client = new QuantumScalarSMIClient(url, username, password);
		client.enumerateInstanceNames(target);
		// client.enumerateInstanceNames("CIM_ComputerSystem");
//		client.enumerateInstances(target);
		// client.enumerateInstances("CIM_ComputerSystem");
		// client.associatorNames("SMIS_ComputerSystem", pAssociationClass, result);
		client.associatorNames(target, null, null);
		// client.associatorInstances("SMIS_ComputerSystem",null, result);
		// client.associatorInstances("CIM_ComputerSystem", result);

	}

}
