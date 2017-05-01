package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_SCSIInterface extends CIM_ControlledBy {
	private CIM_SCSIController Antecedent;
	private UnsignedInteger32 SCSITimeouts;
	private UnsignedInteger32 SCSIRetries;
	private UnsignedInteger32 InitiatorId;
	private UnsignedInteger32 TargetId;
	private UnsignedInteger64 TargetLUN;
	private UnsignedInteger16 SCSIReservation;
	private UnsignedInteger16 SCSISignal;
	private UnsignedInteger32 MaxQueueDepth;
	private UnsignedInteger32 QueueDepthLimit;
}
