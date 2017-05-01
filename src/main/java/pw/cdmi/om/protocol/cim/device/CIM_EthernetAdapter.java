package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_EthernetAdapter extends CIM_NetworkAdapter {
	private String[] NetworkAddresses;
	private UnsignedInteger32 MaxDataSize;
	private UnsignedInteger16 Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16[] EnabledCapabilities;
	private UnsignedInteger32 SymbolErrors;
	private UnsignedInteger64 TotalPacketsTransmitted;
	private UnsignedInteger64 TotalPacketsReceived;
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
