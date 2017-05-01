package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_TokenRingAdapter extends CIM_NetworkAdapter {
	private String[] NetworkAddresses;
	private UnsignedInteger32 MaxDataSize;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16[] EnabledCapabilities;
	private UnsignedInteger32 RingStatus;
	private UnsignedInteger16 RingState;
	private UnsignedInteger16 RingOpenStatus;
	private UnsignedInteger16 RingSpeed;
	private UnsignedInteger32 BurstErrors;
	private UnsignedInteger32 ACErrors;
	private UnsignedInteger32 AbortTransErrors;
	private UnsignedInteger32 InternalErrors;
	private UnsignedInteger32 LostFrameErrors;
	private UnsignedInteger32 ReceiveCongestions;
	private UnsignedInteger32 FrameCopiedErrors;
	private UnsignedInteger32 TokenErrors;
	private UnsignedInteger32 SoftErrors;
	private UnsignedInteger32 HardErrors;
	private UnsignedInteger32 SignalLossCount;
	private UnsignedInteger32 TransmittedBeacons;
	private UnsignedInteger32 Recoverys;
	private UnsignedInteger32 LobeWires;
	private UnsignedInteger32 Removes;
	private UnsignedInteger32 Singles;
	private UnsignedInteger32 FrequencyErrors;
}
