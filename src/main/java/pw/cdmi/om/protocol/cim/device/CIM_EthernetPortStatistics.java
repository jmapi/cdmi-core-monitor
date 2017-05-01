package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_EthernetPortStatistics extends CIM_NetworkPortStatistics {
	private UnsignedInteger64 PacketsTransmitted;
	private UnsignedInteger64 PacketsReceived;
	private UnsignedInteger32 SymbolErrors;
	private UnsignedInteger32 AlignmentErrors;
	private UnsignedInteger32 FCSErrors;
	private UnsignedInteger32 SingleCollisionFrames;
	private UnsignedInteger32 MultipleCollisionFrames;
	private UnsignedInteger32 SQETestErrors;
	private UnsignedInteger32 DeferredTransmissions;
	private UnsignedInteger32 LateCollisions;
	private UnsignedInteger32 ExcessiveCollisions;
	private UnsignedInteger32 InternalMACTransmitErrors;
	private UnsignedInteger32 InternalMACReceiveErrors;
	private UnsignedInteger32 CarrierSenseErrors;
	private UnsignedInteger32 FrameTooLongs;
}
