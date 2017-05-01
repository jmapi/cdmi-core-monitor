package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_NetworkAdapter extends CIM_LogicalDevice {
	private String PermanentAddress;
	private String[] NetworkAddresses;
	private UnsignedInteger64 Speed;
	private UnsignedInteger64 MaxSpeed;
	private boolean FullDuplex;
	private boolean AutoSense;
	private UnsignedInteger64 OctetsTransmitted;
	private UnsignedInteger64 OctetsReceived;
}
