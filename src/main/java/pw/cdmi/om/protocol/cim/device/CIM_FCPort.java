package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;


public class CIM_FCPort extends CIM_NetworkPort {
	private UnsignedInteger16 PortType;
	private UnsignedInteger16[] SupportedCOS;
	private UnsignedInteger16[] ActiveCOS;
	private UnsignedInteger16[] SupportedFC4Types;
	private UnsignedInteger16[] ActiveFC4Types;
}
