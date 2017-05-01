package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_SCSIController extends CIM_Controller {
	private UnsignedInteger16 ProtectionManagement;
	private UnsignedInteger32 MaxDataWidth;
	private UnsignedInteger64 MaxTransferRate;
	private UnsignedInteger32 ControllerTimeouts;
	private UnsignedInteger16[] SignalCapabilities;
}
