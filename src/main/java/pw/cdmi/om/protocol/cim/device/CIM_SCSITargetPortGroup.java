package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_SystemSpecificCollection;

public class CIM_SCSITargetPortGroup extends CIM_SystemSpecificCollection {
	private boolean Asymmetric = false;
	private UnsignedInteger16 CurrentLoadBalanceType = new UnsignedInteger16(7);
	private String OtherCurrentLoadBalanceType;
	private UnsignedInteger16 AutoFailbackEnabled = new UnsignedInteger16(4);
	private UnsignedInteger32 PollingRateMax;
	private UnsignedInteger32 CurrentPollingRate;
	private UnsignedInteger16 AccessState;
	private boolean ExplicitFailover;
	private UnsignedInteger16 Identifier;
	private boolean Preferred;
	private boolean SupportsLuAssignment;
}
