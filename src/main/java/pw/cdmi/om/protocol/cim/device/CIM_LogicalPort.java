package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_LogicalPort extends CIM_LogicalDevice {
	private UnsignedInteger64 Speed;
	private UnsignedInteger64 MaxSpeed;
	private UnsignedInteger64 RequestedSpeed;
	private UnsignedInteger16 UsageRestriction;
	private UnsignedInteger16 PortType;
	private String OtherPortType;
}
