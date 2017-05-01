package pw.cdmi.om.protocol.cim.system;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;
import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_SettingAssociatedToCapabilities extends CIM_Dependency {
	private CIM_Capabilities Antecedent;
	private CIM_SettingData Dependent;
	private boolean DefaultSetting = false;
}
