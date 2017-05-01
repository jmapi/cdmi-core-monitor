package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_AssociatedAlarm extends CIM_Dependency {
	private CIM_AlarmDevice Antecedent;
	private CIM_LogicalDevice Dependent;
}
