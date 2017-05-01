package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_Controller extends CIM_LogicalDevice {
	private Date TimeOfLastReset;
	private UnsignedInteger16 ProtocolSupported;
	private UnsignedInteger32 MaxNumberControlled;
	private String ProtocolDescription;
}
