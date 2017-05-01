package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_TokenRingPort extends CIM_NetworkPort {
	private String[] NetworkAddresses;
	private UnsignedInteger32 MaxDataSize;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16[] EnabledCapabilities;
	private String[] OtherEnabledCapabilities;
	private UnsignedInteger32 RingStatus;
	private UnsignedInteger16 RingState;
	private UnsignedInteger16 RingOpenStatus;
	private UnsignedInteger16 RingSpeed;
}
