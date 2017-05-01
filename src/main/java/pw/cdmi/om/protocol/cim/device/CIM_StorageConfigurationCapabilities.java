package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;

public class CIM_StorageConfigurationCapabilities extends CIM_Capabilities {
	private UnsignedInteger16[] SupportedAsynchronousActions;
	private UnsignedInteger16[] SupportedSynchronousActions;
	private UnsignedInteger16[] SupportedStorageElementTypes;
	private UnsignedInteger16[] SupportedStoragePoolFeatures;
	private UnsignedInteger16[] SupportedStorageElementFeatures;
	private UnsignedInteger16[] SupportedCopyTypes;
	private UnsignedInteger16[] InitialReplicationState;
}
