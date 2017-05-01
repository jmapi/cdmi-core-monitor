package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_ProtocolEndpoint;

public class CIM_SCSIProtocolEndpoint extends CIM_ProtocolEndpoint {
	private String name;
	private UnsignedInteger16 ConnectionType;
	private UnsignedInteger16 Role;
	private UnsignedInteger32 TargetRelativePortNumber;
	private String OtherConnectionType;
}
