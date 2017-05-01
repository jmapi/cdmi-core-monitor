package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_WiFiPort extends CIM_NetworkPort {
	private UnsignedInteger64 Speed;
	private UnsignedInteger64 MaxSpeed;
	private UnsignedInteger16 PortType;
	private String PermanentAddress;
	private String[] NetworkAddresses;
}
