package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_ProtocolControllerForDevice extends CIM_Dependency {
	private CIM_ProtocolController Antecedent;
	private CIM_LogicalDevice Dependent;
	private String DeviceNumber;
	private UnsignedInteger16 AccessPriority;
	private UnsignedInteger16 AccessState;
}
