package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_SCSIMultipathSettings extends CIM_SettingData {
	private boolean Asymmetric = false;
	private UnsignedInteger16 CurrentLoadBalanceType = new UnsignedInteger16(7);
	private String OtherCurrentLoadBalanceType;
	private UnsignedInteger16 AutoFailbackEnabled = new UnsignedInteger16(4);
	private UnsignedInteger32 PollingRateMax;
	private UnsignedInteger32 CurrentPollingRate;
}
