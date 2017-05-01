package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_ProtocolEndpoint;

public class CIM_LANEndpoint extends CIM_ProtocolEndpoint {
	private String LANID;
	private UnsignedInteger16 LANType;
	private String OtherLANType;
	private String MACAddress;
	private String[] AliasAddresses;
	private String[] GroupAddresses;
	private UnsignedInteger32 MaxDataSize;
}
