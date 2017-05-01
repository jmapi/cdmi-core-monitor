package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_NetworkPort extends CIM_LogicalPort {
	private UnsignedInteger64 Speed;
	private String OtherNetworkPortType;
	private UnsignedInteger16 PortNumber;
	private UnsignedInteger16 LinkTechnology;
	private String OtherLinkTechnology;
	private String PermanentAddress;
	private String[] NetworkAddresses;
	private boolean FullDuplex;
	private boolean AutoSense;
	private UnsignedInteger64 SupportedMaximumTransmissionUnit;
	private UnsignedInteger64 ActiveMaximumTransmissionUnit;
}
