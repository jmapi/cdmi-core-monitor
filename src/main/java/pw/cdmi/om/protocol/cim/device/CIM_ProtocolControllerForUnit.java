package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_ProtocolControllerForUnit extends CIM_ProtocolControllerForDevice {
	private CIM_ProtocolController Antecedent;
	private CIM_LogicalDevice Dependent;
	private UnsignedInteger16 DeviceAccess;
}
