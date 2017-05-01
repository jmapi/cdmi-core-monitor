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

public class HitachiSMIClient {
	private static List<String> mofiles = new ArrayList<String>();
	{
		mofiles.add("ArrayGroup");
		mofiles.add("AuthorizedPrivilege");
		mofiles.add("Battery");
		mofiles.add("BlockStatisticalDataFCPort");
		mofiles.add("BlockStatisticalDataStorageSystem");
		mofiles.add("BlockStatisticalDataStorageVolume");
		mofiles.add("BlockStatisticsCapabilities");
		mofiles.add("BlockStatisticsManifest");
		mofiles.add("BlockStatisticsManifestCollection");
		mofiles.add("BlockStatisticsService");
		mofiles.add("CacheMemory");
		mofiles.add("CapabilitiesOfThinProvisioningPool");
		mofiles.add("Cluster");
		mofiles.add("CIMXMLCommunicationMechanism");
		mofiles.add("CIMXMLObjectManagerAdapter");
		mofiles.add("CIMXMLSCommunicationMechanism");
		mofiles.add("CIMXMLSObjectManagerAdapter");
		mofiles.add("CompositeLDEVExtent");
		mofiles.add("ComputerSystem");
		mofiles.add("ConcreteJob");
		mofiles.add("ControllerConfigurationService");
		mofiles.add("DiskDrive");
		mofiles.add("DiskExtent");
		mofiles.add("DiskFirmware");
		mofiles.add("DiskProduct");
		mofiles.add("DKCChassis");
		mofiles.add("DKCLocation");
		mofiles.add("DKUChassis");
		mofiles.add("DKULocation");
		mofiles.add("ElementCompositionCapabilities");
		mofiles.add("ElementCompositionService");
		mofiles.add("Fan");
		mofiles.add("FCPort");
		mofiles.add("FreeSpace");
		mofiles.add("GeneratedStorageSetting");
		mofiles.add("GeneratedStorageSettingOfImportedPrimordialPool");
		mofiles.add("GeneratedStorageSettingOfPrimordialPool");
		mofiles.add("GeneratedThinProvisioningPoolSetting");
		mofiles.add("HiCommandAccessPoint");
		mofiles.add("ImportedStorageExtent");
		mofiles.add("ImportedStoragePoolPrimordial");
		mofiles.add("ImportingSCSIProtocolController");
		mofiles.add("LDEVExtent");
		mofiles.add("LunMaskPrivilegeService");
		mofiles.add("MaskingCapabilities");
		mofiles.add("Namespace");
		mofiles.add("ObjectManager");
		mofiles.add("ObjectManager");
		mofiles.add("PhysicalDisk");
		mofiles.add("PhysicalDiskMedia");
		mofiles.add("PowerSupply");
		mofiles.add("QueryCapabilities");
		mofiles.add("RedundancySet");
		mofiles.add("RegisteredProfile");
		mofiles.add("RegisteredProfileSMIS");
		mofiles.add("RegisteredSubProfile");
		mofiles.add("RemoteServiceAccessPoint");
		mofiles.add("Privilege");
		mofiles.add("SMIS_SPIPort");
		mofiles.add("SCSIArbitraryLogicalUnit");
		mofiles.add("SCSIProtocolController");
		mofiles.add("SCSIProtocolEndpoint");
		mofiles.add("SoftwareIdentity");
		mofiles.add("SoftwareElement");
		mofiles.add("StatisticsCollection");
		mofiles.add("StorageCapabilities");
		mofiles.add("StorageCapabilitiesOfImportedPrimordialPool");
		mofiles.add("StorageCapabilitiesOfPrimordialPool");
		mofiles.add("StorageClientSettingData");
		mofiles.add("StorageConfigurationCapabilities");
		mofiles.add("StorageConfigurationCapabilitiesOfThinProvisioningPool");
		mofiles.add("StorageConfigurationService");
		mofiles.add("StorageHardwareID");
		mofiles.add("StorageHardwareID");
		mofiles.add("StorageManagementSoftware");
		mofiles.add("StorageManagementSystem");
		mofiles.add("StoragePool");
		mofiles.add("StoragePoolPrimordial");
		mofiles.add("StorageProcessorCard");
		mofiles.add("StorageProcessorSystem");
		mofiles.add("StorageProduct");
		mofiles.add("StorageProtectionCapabilites");
		mofiles.add("StorageProtectionService");
		mofiles.add("StorageProtectionSetting");
		mofiles.add("StorageSetting");
		mofiles.add("StorageSettingForThinProvisioning");
		mofiles.add("StorageSettingOfImportedPrimordialPool");
		mofiles.add("StorageSettingOfPrimordialPool");
		mofiles.add("StorageSettingOfPrimordialPoolForThinProvisioning");
		mofiles.add("StorageSettingWithDiskLayout");
		mofiles.add("StorageSettingWithDiskLayoutOfPrimordialPool");
		mofiles.add("StorageSystem");
		mofiles.add("StorageVolume");
		mofiles.add("StorageVolumeSetting");
		mofiles.add("ThinProvisioningPool");
		mofiles.add("ThinProvisioningPoolSetting");
		mofiles.add("ThinProvisioningVolumeCompositeExtent");
		mofiles.add("ThinProvisioningVolumeSetting");
		mofiles.add("ThinProvisioningVolumeView");
		mofiles.add("UnconfiguredStorageVolume");
		mofiles.add("ViewCapabilities");
		mofiles.add("VolumeView");
		mofiles.add("WebAccessPoint");
		// Indication Classes
		mofiles.add("AlertIndication");
		mofiles.add("InstCreation");
		mofiles.add("InstDeletion");
		mofiles.add("InstModification");
		// Association Classes
		mofiles.add("AffectedJobElementAuthorizedPrivilege");
		mofiles.add("AffectedJobElementStoragePoolPrimordial");
		mofiles.add("AffectedJobElementSCSIProtocolController");
		mofiles.add("AffectedJobElementSCSIProtocolEndPoint");
		mofiles.add("AffectedJobElementStorageHardwareID");
		mofiles.add("AffectedJobElementStoragePool");
		mofiles.add("AffectedJobElementStorageVolume");
		mofiles.add("AffectedJobElementThinProvisioningPool");
		mofiles.add("AllocateFromStoragePool");
		mofiles.add("AllocatedFromStoragePoolPrimordial");
		mofiles.add("AllocatedFromImportedStoragePoolPrimordial");
		mofiles.add("AllocatedFromStoragePoolView");
		mofiles.add("AllocatedFromThinProvisioningPool");
		mofiles.add("AllocatedFromThinProvisioningStoragePoolView");
		mofiles.add("ArrayGroupBasedOnDiskExtent");
		mofiles.add("ArrayGroupBasedOnImportedStorageExtent");
		mofiles.add("AssociatitedBlockStatisticsManifestCollection");
		mofiles.add("AssociatedImportedStoragePoolPrimordialComponentImportedStorageExtent");
		mofiles.add("AssociatedStoragePoolComponentArrayGroup");
		mofiles.add("AssociatedStoragePoolComponentLDEVExtent");
		mofiles.add("AssociatedStoragePoolPrimordialComponentDiskExtent");
		mofiles.add("AssociatedStoragePoolRemainingFreeSpace");
		mofiles.add("AssociatedThinProvisioningPoolComponentArrayGroup");
		mofiles.add("AssociatedThinProvisioningPoolComponentCompositeLDEVExtent");
		mofiles.add("AuthorizedSubject");
		mofiles.add("AuthorizedTarget");
		mofiles.add("BlockStorageStatisticalDataStorageVolumeView");
		mofiles.add("BlockStorageStatisticalDataThinProvisioningVolumeView");
		mofiles.add("CIMXMLCommMechanismForManager");
		mofiles.add("CIMXMLCommMechanismForObjectManagerAdapter");
		mofiles.add("CIMXMLSCommMechanismForManager");
		mofiles.add("CIMXMLCommuniationMechanismForSystem");
		mofiles.add("CIMXMLSCommMechanismForObjectManagerAdapter");
		mofiles.add("CIMXMLSCommuniationMechanismForSystem");
		mofiles.add("CommMechanismForManager");
		mofiles.add("ComponentCS");
		mofiles.add("ComposedThinProvisioningPool");
		mofiles.add("CompositeLDEVExtentBasedOnLDEVExtent");
		mofiles.add("CompositeStorageExtentStorageView");
		mofiles.add("CompositeStorageExtentThinProvisioningVolumeView");
		mofiles.add("ControllerCSOwningJobElement");
		mofiles.add("DiskInStorageProduct");
		mofiles.add("DiskMediaPresent");
		mofiles.add("ConcreteIdentity");
		mofiles.add("ConcreteIdentitySystem");
		mofiles.add("DKCProcessorCardInChassis");
		mofiles.add("ElementCapabilitiesBlockStatistics");
		mofiles.add("ElementCapabilitiesElementComposition");
		mofiles.add("ElementCapabilitiesImportedStoragePoolPrimordial");
		mofiles.add("ElementCapabilitiesMasking");
		mofiles.add("ElementCapabilitiesStorageConfiguration");
		mofiles.add("ElementCapabilitiesStoragePool");
		mofiles.add("ElementCapabilitiesStoragePoolPrimordial");
		mofiles.add("ElementCapabilitiesStorageProtection");
		mofiles.add("ElementCapabilitiesThinProvisioningPool");
		mofiles.add("ElementConfigurationCapabilitiesThinProvisioningPool");
		mofiles.add("ElementCSOwningJobElement");
		mofiles.add("ElementDiskFirmware");
		mofiles.add("ElementProtectionSettingData");
		mofiles.add("ElementSettingDataStorageVolume");
		mofiles.add("ElementSettingDataThinProvisioningVolume");
		mofiles.add("ExistingSetting");
		mofiles.add("ElementStatisticalDataFCPort");
		mofiles.add("ElementStatisticalDataStorageSystem");
		mofiles.add("ElementStatisticalDataStorageVolume");
		mofiles.add("ElementStorageClientSettingDataSCSIPC");
		mofiles.add("ElementStorageClientSettingDataStorageHardwareID");
		mofiles.add("ElementStorageClientSettingDataStorageSystem");
		mofiles.add("ElementViewCapabilities");
		mofiles.add("FCPortForSCSIProtocolEndpointImplementation");
		mofiles.add("FreeSpaceBasedOnArrayGroup");
		mofiles.add("HiCommandAvailableForStorageSystem");
		mofiles.add("HostedAccessPoint");
		mofiles.add("HostedBlockStatisticsManifestCollection");
		mofiles.add("HostedBlockStatisticsService");
		mofiles.add("HostedCIMXMLAccessPoint");
		mofiles.add("HostedControllerConfigurationService");
		mofiles.add("HostedElementCompositionService");
		mofiles.add("HostedHiCommandAccessPoint");
		mofiles.add("HostedImportedStoragePoolPrimordial");
		mofiles.add("HostedLunMaskPrivilegeService");
		mofiles.add("HostedObjectManager");
		mofiles.add("HostedStatisticsCollection");
		mofiles.add("HostedStorageConfigurationService");
		mofiles.add("HostedStorageHardwareIDManagementService");
		mofiles.add("HostedStoragePool");
		mofiles.add("HostedStoragePoolPrimordial");
		mofiles.add("HostedStorageProcessorSystemSCSIProtocolEndpoint");
		mofiles.add("HostedStorageProtectionService");
		mofiles.add("HostedThinProvisioningPool");
		mofiles.add("ImportedStoragePoolPrimordialComponentImportedStorageExtent");
		mofiles.add("ImportingSCSIPCForImportedStorageExtent");
		mofiles.add("ImportingSCSIPCForFCPort");
		mofiles.add("InstalledSoftwareIdentity");
		mofiles.add("LDEVExtentBasedOnArrayGroup");
		mofiles.add("ManagedAuthorizedPrivilege");
		mofiles.add("HostedWebAccessPoint");
		mofiles.add("InstalledSoftwareElement");
		mofiles.add("ManagedPrivilege");
		mofiles.add("ManagedSCSIPC");
		mofiles.add("ManagedStorageHardwareID");
		mofiles.add("NamespaceInManager");
		mofiles.add("ObjectManagerConformsToProfile");
		mofiles.add("ObjectManagerForSystem");
		mofiles.add("ObjectManagerQueryCapabilities");
		mofiles.add("MemberOfBlockStatisticsManifestCollection");
		mofiles.add("MemberOfCluster");
		mofiles.add("MemberOfCollectionStorageProcessorSystem");
		mofiles.add("MemberOfStatisticsCollectionFCPort");
		mofiles.add("MemberOfStatisticsCollectionStorageSystem");
		mofiles.add("MemberOfStatisticsCollectionStorageVolume");
		mofiles.add("OwningJobElement");
		mofiles.add("PackagedPhysicalDiskMedia");
		mofiles.add("PhysicalDiskDKC");
		mofiles.add("PhysicalDiskDKU");
		mofiles.add("PhysicalDiskMediaRealizesDiskExtent");
		mofiles.add("PhysicalDiskRealizesDiskDrive");
		mofiles.add("PhysicalElementLocationDKC");
		mofiles.add("PhysicalElementLocationDKU");
		mofiles.add("ProductPhysicalComponentDisk");
		mofiles.add("ProductPhysicalComponentDKC");
		mofiles.add("ProductPhysicalComponentDKU");
		mofiles.add("ProfileSoftwareIdentity");
		mofiles.add("RegisteredProfileConformsToSMIS");
		mofiles.add("RegisteredSubProfileConformsToSMIS");
		mofiles.add("SCSIPCForFCPort");
		mofiles.add("SCSIPCForFCPort");
		mofiles.add("SCSIPCForStorageVolume");
		mofiles.add("SCSIProtocolEndpointAvailableForImportingProtocolController");
		mofiles.add("SCSIProtocolEndpointAvailableForProtocolController");
		mofiles.add("SMISSoftwareIdentity");
		mofiles.add("StorageCSOwningJobElement");
		mofiles.add("SroagePoolComponentArrayGroup");
		mofiles.add("StoragePoolComponentFreeSpace");
		mofiles.add("StoragePoolComponentLDEVExtent");
		mofiles.add("StoragePoolPrimordialComponentDiskExtent");
		mofiles.add("StoragePoolPrimordialComponentImportedStorageExtent");
		mofiles.add("StoragePoolComponent");
		mofiles.add("StorageProcessorSystemDeviceFCPort");
		mofiles.add("StorageProcessorSystemDeviceSCSIPC");
		mofiles.add("StorageProcessorSystemDeviceSCSIProtocolEndpoint");
		mofiles.add("StorageProcessorSystemPackage");
		mofiles.add("StorageSettingsAssociatedToCapabilities");
		mofiles.add("StorageSettingsAssociatedToCapabilitiesForThinProvisioning");
		mofiles.add("StorageSettingsAssociatedToCapabilitiesOfImportedPrimordialPool");
		mofiles.add("StorageSettingsAssociatedToCapabilitiesOfPrimordialPool");
		mofiles.add("StorageSettingsAssociatedToCapabilitiesOfPrimordialPoolForThinProvisioning");
		mofiles.add("StorageSettingsAssociatedToCapabilitiesOfThinProvisioningPool");
		mofiles.add("StorageSettingsGeneratedFromCapabilities");
		mofiles.add("StorageSettingsGeneratedFromCapabilitiesOfImportedPrimordialPool");
		mofiles.add("StorageSettingsGeneratedFromCapabilitiesOfPrimordialPool");
		mofiles.add("StorageSettingsGeneratedFromCapabilitiesOfThinProvisioningPool");
		mofiles.add("StorageSettingWithDiskLayoutAssociatedToCapabilities");
		mofiles.add("StorageSettingWithDiskLayoutAssociatedToCapabilitiesOfPrimordialPool");
		mofiles.add("StorageSynchronized");
		mofiles.add("StorageSystemConformsToProfile");
		mofiles.add("StorageSystemDeviceArbitraryLogicalUnit");
		mofiles.add("StorageSystemDeviceArrayGroup");
		mofiles.add("StorageSystemDeviceBattery");
		mofiles.add("StorageSystemDeviceCacheMemory");
		mofiles.add("StorageSystemDeviceCompositeLDEVExtent");
		mofiles.add("StorageSystemDeviceDiskDrive");
		mofiles.add("StorageSystemDeviceDiskExtent");
		mofiles.add("StorageSystemDeviceFan");
		mofiles.add("StorageSystemDeviceFreeSpace");
		mofiles.add("StorageSystemDeviceImportedStorageExtent");
		mofiles.add("StorageSystemDeviceImportingSCSIPC");
		mofiles.add("StorageSystemDeviceLDEVExtent");
		mofiles.add("StorageSystemDeviceOpenLDEV");
		mofiles.add("StorageSystemDevicePowerSupply");
		mofiles.add("StorageSystemDeviceStorageVolume");
		mofiles.add("StorageSystemDeviceThinProvisioningVolumeView");
		mofiles.add("StorageSystemDeviceViewVolumeView");
		mofiles.add("StorageSystemPackageDKC");
		mofiles.add("StorageSystemPackageDKU");
		mofiles.add("StorageVolumeArrayGroup");
		mofiles.add("StorageVolumeBasedOnCompositeLDEVExtent");
		mofiles.add("StorageVolumeBasedOnLDEVExtent");
		mofiles.add("StorageVolumeDiskExtent");
		mofiles.add("StorageVolumeSettingsAssociatedToCapabilities");
		mofiles.add("StorageVolumeBasedOnOpenLDEV");
		mofiles.add("StorageVolumeSCSIArbitraryLogicalUnit");
		mofiles.add("StorageVolumeSettingVolumeView");
		mofiles.add("StorageVolumeVolumeView");
		mofiles.add("SubProfileRequiresProfile");
		mofiles.add("SubProfileSoftwareIdentity");
		mofiles.add("ThinProvisioningCompositeStorageExtentThinProvisioningVolumeView");
		mofiles.add("ThinProvisioningPoolAllocatedFromStoragePool");
		mofiles.add("ThinProvisioningPoolAllocatedFromStoragePoolPrimordial");
		mofiles.add("ThinProvisioningPoolComponentArrayGroup");
		mofiles.add("ThinProvisioningPoolComponentCompositeLDEVExtent");
		mofiles.add("ThinProvisioningVolumeBasedOnThinProvisioningVolumeCompositeExtent");
		mofiles.add("ThinProvisioningVolumeCompositeExtentBasedOnCompositeLDEVExtent");
		mofiles.add("ThinProvisioningVolumeSettingVolumeView");
		mofiles.add("ThinProvisioningVolumeVolumeView");
		
	}

	private static Logger logger = LoggerFactory.getLogger(HitachiSMIClient.class);

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

	public HitachiSMIClient(String root, String username, String password) {
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

	// public void LibraryCapacity() {
	//
	// }

	public static void main(String[] agx) throws IOException {

		SNMPTarget target1 = new SNMPTarget();
		target1.setTargetIp("10.6.178.14");
		target1.setPort(161);
		target1.setSnmpVersion(SnmpConstants.version2c);
		target1.setReadCommunity("public");
		SNMPClient snmp_client = new SNMPClient(target1);
		boolean isSuccess = snmp_client.testConnected();

		System.out.println("连接状态：" + isSuccess);
		String url = "http://10.6.178.14:5988/root/hitachi/dm";
		// String url = "http://10.6.178.29:61000/cimv2";
		String username = "smiuser";
		String password = "smipassword";

		String result = "CIM_System";
		String pAssociationClass = "CIM_Service";
		String target = "SMIS_StorageMediaLocation";
		HitachiSMIClient client = new HitachiSMIClient(url, username, password);
		client.enumerateInstanceNames(target);
		// client.enumerateInstanceNames("CIM_ComputerSystem");
		// client.enumerateInstances(target);
		// client.enumerateInstances("CIM_ComputerSystem");
		// client.associatorNames("SMIS_ComputerSystem", pAssociationClass, result);
		client.associatorNames(target, null, null);
		// client.associatorInstances("SMIS_ComputerSystem",null, result);
		// client.associatorInstances("CIM_ComputerSystem", result);

	}

}
