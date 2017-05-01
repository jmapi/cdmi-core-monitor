package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_WiFiRadio extends CIM_LogicalDevice {
	private UnsignedInteger32 SignalStrength;
	private UnsignedInteger32 SignalNoise;
	private UnsignedInteger16 Channel;
	private UnsignedInteger32 Frequency;
}
