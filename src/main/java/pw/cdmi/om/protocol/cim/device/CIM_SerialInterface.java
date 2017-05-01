package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_SerialInterface extends CIM_ControlledBy {
	private CIM_SerialController Antecedent;
	private UnsignedInteger32 NegotiatedDataWidth;
	private UnsignedInteger16 NumberOfStopBits;
	private UnsignedInteger16 ParityInfo;
	private UnsignedInteger16 FlowControlInfo;
}
