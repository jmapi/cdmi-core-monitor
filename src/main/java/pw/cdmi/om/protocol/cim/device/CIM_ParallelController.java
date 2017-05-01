package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

public class CIM_ParallelController extends CIM_Controller {
	private boolean DMASupport;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16 Security;
}
