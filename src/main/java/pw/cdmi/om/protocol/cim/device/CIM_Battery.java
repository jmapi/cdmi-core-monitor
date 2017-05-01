package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_Battery extends CIM_LogicalDevice {
	private UnsignedInteger16 BatteryStatus;
	private UnsignedInteger32 TimeOnBattery;
}
