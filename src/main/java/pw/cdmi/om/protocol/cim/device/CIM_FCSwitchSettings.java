package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_FCSwitchSettings extends CIM_SettingData {
	private UnsignedInteger8 PreferredDomainID;
	private boolean DomainIDLocked;
	private UnsignedInteger16 PrincipalPriority = new UnsignedInteger16(3);
}
