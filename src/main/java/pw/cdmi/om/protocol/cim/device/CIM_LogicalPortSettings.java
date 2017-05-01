package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_LogicalPortSettings extends CIM_SettingData {
	private UnsignedInteger64 RequestedSpeed;
	private boolean AutoSenseSpeed;
}
