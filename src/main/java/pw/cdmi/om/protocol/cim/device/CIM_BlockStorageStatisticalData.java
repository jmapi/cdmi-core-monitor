package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalData;

public class CIM_BlockStorageStatisticalData extends CIM_StatisticalData {
	private UnsignedInteger16 ElementType;
	private UnsignedInteger64 TotalIOs;
	private UnsignedInteger64 KBytesTransferred;
	private UnsignedInteger64 KBytesWritten;
	private UnsignedInteger64 IOTimeCounter;
	private UnsignedInteger64 ReadIOs;
	private UnsignedInteger64 ReadHitIOs;
	private UnsignedInteger64 ReadIOTimeCounter;
	private UnsignedInteger64 ReadHitIOTimeCounter;
	private UnsignedInteger64 KBytesRead;
	private UnsignedInteger64 WriteIOs;
	private UnsignedInteger64 WriteHitIOs;
	private UnsignedInteger64 WriteIOTimeCounter;
	private UnsignedInteger64 WriteHitIOTimeCounter;
	private UnsignedInteger64 IdleTimeCounter;
	private UnsignedInteger64 MaintOp;
	private UnsignedInteger64 MaintTimeCounter;
	
	
}
