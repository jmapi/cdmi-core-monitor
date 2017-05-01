package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_SerialController extends CIM_Controller {
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger32 MaxBaudRate;
	private UnsignedInteger16 Security;
}
