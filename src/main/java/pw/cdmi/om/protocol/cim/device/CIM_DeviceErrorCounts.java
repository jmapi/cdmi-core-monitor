package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalInformation;

public class CIM_DeviceErrorCounts extends CIM_StatisticalInformation {
	private String SystemCreationClassName;
	private String SystemName;
	private String DeviceCreationClassName;
	private String DeviceID;
	private String name;
	private UnsignedInteger64 IndeterminateErrorCount;
	private UnsignedInteger64 CriticalErrorCount;
	private UnsignedInteger64 MajorErrorCount;
	private UnsignedInteger64 MinorErrorCount;
	private UnsignedInteger64 WarningCount;
	
	
	public UnsignedInteger32 ResetCounter(UnsignedInteger16 SelectedCounter){
		return null;
	}
}
