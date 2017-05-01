package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_Door extends CIM_LogicalDevice {
	private boolean Open;
	private boolean Locked;
	private UnsignedInteger32 Timeout;
	private Date LastOpened;
	private UnsignedInteger16[] Capabilities;
}
