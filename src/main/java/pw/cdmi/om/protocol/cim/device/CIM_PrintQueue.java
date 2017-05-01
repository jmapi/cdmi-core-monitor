package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.system.CIM_JobDestination;

public class CIM_PrintQueue extends CIM_JobDestination {
	private boolean QueueEnabled;
	private boolean QueueAccepting;
	private UnsignedInteger32 NumberOnQueue;
	private UnsignedInteger16 QueueStatus;
	private String QueueStatusInfo;
	private UnsignedInteger32 MaxJobSize;
	private UnsignedInteger32 DefaultJobPriority;
	private UnsignedInteger32 JobPriorityHigh;
	private UnsignedInteger32 JobPriorityLow;
	private String[] AvailableJobSheets;
}
