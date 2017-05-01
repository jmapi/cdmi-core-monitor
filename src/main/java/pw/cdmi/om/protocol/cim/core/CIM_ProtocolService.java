package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.network.CIM_IPProtocolEndpoint;

public class CIM_ProtocolService extends CIM_Service {
	private UnsignedInteger16 Protocol;
	private String OtherProtocol;
	private UnsignedInteger16 MaxConnections = new UnsignedInteger16(1);
	private UnsignedInteger16 CurrentActiveConnections;//MinValue ( 0 ), MaxValue ( 65535 )
	
	public UnsignedInteger32 ListenOnPort(UnsignedInteger32 PortNumber, CIM_IPProtocolEndpoint IPEndpoint, CIM_TCPProtocolEndpoint TCPEndpoint){
		return null;
	}
	
	public UnsignedInteger32 ListenOnPortIF(UnsignedInteger32 PortNumber, CIM_IPProtocolEndpoint IPEndpoint, CIM_TCPProtocolEndpoint TCPEndpoint, UnsignedInteger32 ProtocolIFType){
		return null;
	}
}
