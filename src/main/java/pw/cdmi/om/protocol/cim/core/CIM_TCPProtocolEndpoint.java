package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_TCPProtocolEndpoint extends CIM_ProtocolEndpoint {
	private UnsignedInteger32 PortNumber;
	private UnsignedInteger16 ProtocolIFType = new UnsignedInteger16(4111);
}
