package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;

public class CIM_StorageError extends CIM_ManagedElement {
	private String SystemCreationClassName;
	private String SystemName;
	private String DeviceCreationClassName;
	private String DeviceID;
	private UnsignedInteger64 StartingAddress;
	private UnsignedInteger64 EndingAddress;
}
