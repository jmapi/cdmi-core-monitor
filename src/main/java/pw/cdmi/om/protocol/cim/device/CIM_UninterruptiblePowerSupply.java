package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_UninterruptiblePowerSupply extends CIM_PowerSupply {
	private UnsignedInteger32 Range1InputVoltageLow;
	private UnsignedInteger32 Range1InputVoltageHigh;
	private UnsignedInteger32 Range2InputVoltageLow;
	private UnsignedInteger32 Range2InputVoltageHigh;
	private UnsignedInteger16 RemainingCapacityStatus;
	private UnsignedInteger32 TimeOnBackup;
	private UnsignedInteger32 EstimatedRunTime;
	private UnsignedInteger16 EstimatedChargeRemaining;
}
