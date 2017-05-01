package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_ServiceAccessPoint;

public class CIM_PrintSAP extends CIM_ServiceAccessPoint {
	private UnsignedInteger16[] PrintProtocol;
	private String[] PrintProtocolInfo;
}
