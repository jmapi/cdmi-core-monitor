package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_AssociatedBattery extends CIM_Dependency {
	CIM_Battery Antecedent;
	CIM_LogicalDevice Dependent;
}
