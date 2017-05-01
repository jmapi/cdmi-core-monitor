package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_ProtocolEndpoint;

public class CIM_IPProtocolEndpoint extends CIM_ProtocolEndpoint {
	private String IPv4Address;
	private String IPv6Address;
	private String Address;
	private String SubnetMask;
	private UnsignedInteger8 PrefixLength;
	private UnsignedInteger16 AddressType;
	private UnsignedInteger16 IPVersionSupport;
	private UnsignedInteger16 ProtocolIFType = new UnsignedInteger16(4096);
	private UnsignedInteger16 AddressOrigin = new UnsignedInteger16(0);
	private UnsignedInteger16 IPv6AddressType;
	private UnsignedInteger16 IPv6SubnetPrefixLength;
}
