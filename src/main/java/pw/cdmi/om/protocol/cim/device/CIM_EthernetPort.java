package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_EthernetPort extends CIM_NetworkPort {
	private UnsignedInteger16 PortType;
	private String[] NetworkAddresses;
	private UnsignedInteger32 MaxDataSize;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16[] EnabledCapabilities;
	private String[] OtherEnabledCapabilities;
	private UnsignedInteger16 PVID;
	private UnsignedInteger16 PoEPowerEntityType;
	private String[] PortDiscriminator;
}
