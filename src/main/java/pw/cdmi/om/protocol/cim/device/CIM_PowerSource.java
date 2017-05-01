package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_PowerSource extends CIM_LogicalDevice {
	private UnsignedInteger32 RatedMaxOutputPower;
	private String OutputPowerUnits;
	private boolean IsACOutput;
}
