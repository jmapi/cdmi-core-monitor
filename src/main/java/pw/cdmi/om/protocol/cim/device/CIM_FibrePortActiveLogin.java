package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_FibrePortActiveLogin {
	private CIM_FibrePort LoginOriginator;
	private CIM_FibrePort LoginResponder;
	private UnsignedInteger16 NegotiatedCOS;
	private UnsignedInteger64 NegotiatedFrameSize;
	private UnsignedInteger64 NegotiatedSpeed;
	private UnsignedInteger16 ACKModel;
	private UnsignedInteger16 BufferToBufferModel;
	private UnsignedInteger64 OriginatorBufferCredit;
	private UnsignedInteger64 ResponderBufferCredit;
	private UnsignedInteger64 OriginatorEndCredit;
	private UnsignedInteger64 ResponderEndCredit;
}
