package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_HostedDependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_PortOnDevice extends CIM_HostedDependency {
	private CIM_LogicalDevice Antecedent;
	private CIM_LogicalPort Dependent;
}
