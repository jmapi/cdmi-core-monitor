package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_PowerSupply extends CIM_LogicalDevice {
	private boolean IsSwitchingSupply;
	private UnsignedInteger32 Range1InputVoltageLow;
	private UnsignedInteger32 Range1InputVoltageHigh;
	private UnsignedInteger32 Range1InputFrequencyLow;
	private UnsignedInteger32 Range1InputFrequencyHigh;
	private UnsignedInteger32 Range2InputVoltageLow;
	private UnsignedInteger32 Range2InputVoltageHigh;
	private UnsignedInteger32 Range2InputFrequencyLow;
	private UnsignedInteger32 Range2InputFrequencyHigh;
	private UnsignedInteger16 ActiveInputVoltage;
	private UnsignedInteger16 TypeOfRangeSwitching;
	private UnsignedInteger32 TotalOutputPower;
}
