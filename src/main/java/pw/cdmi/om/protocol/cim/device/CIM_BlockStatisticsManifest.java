package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;

public class CIM_BlockStatisticsManifest extends CIM_ManagedElement {
	private String InstanceID;
	private UnsignedInteger16 ElementType;
	private boolean IncludeStartStatisticTime = false;
	private boolean IncludeStatisticTime = false;
	private boolean IncludeTotalIOs = false;
	private boolean IncludeKBytesTransferred = false;
	private boolean IncludeIOTimeCounter = false;
	private boolean IncludeReadIOs = false;
	private boolean IncludeReadHitIOs  = false;
	private boolean IncludeReadIOTimeCounter = false;
	private boolean IncludeReadHitIOTimeCounter = false;
	private boolean IncludeKBytesRead = false;
	private boolean IncludeWriteIOs = false;
	private boolean IncludeWriteHitIOs = false;
	private boolean IncludeWriteIOTimeCounter = false;
	private boolean IncludeWriteHitIOTimeCounter = false;
	private boolean IncludeKBytesWritten = false;
	private boolean IncludeIdleTimeCounter = false;
	private boolean IncludeMaintOp = false;
	private boolean IncludeMaintTimeCounter = false;
	
}
