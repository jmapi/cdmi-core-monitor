package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_FibreChannelAdapter extends CIM_NetworkAdapter {
	private UnsignedInteger64 MaxFrameSize;
	private UnsignedInteger16[] SupportedCOS;
	private UnsignedInteger16[] FC4TypesSupported;
	private UnsignedInteger16[] FC4VendorUniqueTypes;
	private UnsignedInteger16[] CurrentFC4Types;
	private UnsignedInteger16[] CurrentFC4VendorTypes;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger64 ReceiveBufferErrors;
	private UnsignedInteger64 ReceiveEndErrors;
	private UnsignedInteger64 ResourceAllocationTimeout;
	private UnsignedInteger64 ErrorDetectTimeout;
	private UnsignedInteger64 Class1SequencesSent;
	private UnsignedInteger64 Class2SequencesSent;
	private UnsignedInteger64 Class3SequencesSent;
	private UnsignedInteger64 Class4SequencesSent;
	private UnsignedInteger64 Class2OctetsReceived;
	private UnsignedInteger64 Class2OctetsTransmitted;
	private UnsignedInteger64 Class2FramesReceived;
	private UnsignedInteger64 Class2FramesTransmitted;
	private UnsignedInteger64 Class2DiscardFrames;
	private UnsignedInteger64 Class3OctetsReceived;
	private UnsignedInteger64 Class3OctetsTransmitted;
	private UnsignedInteger64 Class3FramesReceived;
	private UnsignedInteger64 Class3FramesTransmitted;
	private UnsignedInteger64 Class3DiscardFrames;
	private UnsignedInteger64 ParityErrors;
	private UnsignedInteger64 FrameTimeouts;
	private UnsignedInteger64 BufferCreditErrors;
	private UnsignedInteger64 EndCreditErrors;
	private UnsignedInteger64 OutOfOrderFramesReceived;
}
