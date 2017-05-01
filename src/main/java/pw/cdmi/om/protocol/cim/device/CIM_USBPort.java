package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_USBPort extends CIM_LogicalPort {
	private UnsignedInteger64 Speed;
	private UnsignedInteger16 StatusInfo;
	private UnsignedInteger16 Availability;
	private UnsignedInteger16 Power;
	private boolean Suspended;
	private boolean Overcurrent;
}
