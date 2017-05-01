package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_DeviceConnection extends CIM_Dependency {
	private CIM_LogicalDevice Antecedent;
	private CIM_LogicalDevice Dependent;
	
	private UnsignedInteger64 NegotiatedSpeed;
	private UnsignedInteger32 NegotiatedDataWidth;
	
}
