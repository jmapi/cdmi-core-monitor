package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;
import pw.cdmi.om.protocol.cim.network.CIM_SCSIProtocolEndpoint;

public class CIM_SCSIInitiatorTargetLogicalUnitPath {
	private CIM_SCSIProtocolEndpoint Initiator;
	private CIM_SCSIProtocolEndpoint Target;
	private CIM_LogicalDevice LogicalUnit;
	private String OSDeviceName;
	private UnsignedInteger32 AdministrativeWeight;
	private UnsignedInteger32 State;
	private UnsignedInteger16 AdministrativeOverride;
	private UnsignedInteger16 LogicalUnitNumber;
	
}
