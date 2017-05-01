package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_SettingsDefineCapabilities extends CIM_Component {
	private CIM_Capabilities GroupComponent;
	private CIM_SettingData PartComponent;
	private UnsignedInteger16 PropertyPolicy = new UnsignedInteger16(0);
	private UnsignedInteger16 ValueRole = new UnsignedInteger16(3);
	private UnsignedInteger16 ValueRange = new UnsignedInteger16(0);
	
}
