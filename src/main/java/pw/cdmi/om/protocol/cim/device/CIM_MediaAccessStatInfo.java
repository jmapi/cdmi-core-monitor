package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_DeviceStatisticalInformation;

public class CIM_MediaAccessStatInfo extends CIM_DeviceStatisticalInformation {
	private UnsignedInteger64 ReadOperations;
	private UnsignedInteger64 UnrecoverableReadOperations;
	private UnsignedInteger64 WriteOperations;
	private UnsignedInteger64 UnrecoverableWriteOperations;
	private UnsignedInteger64 RecoveredReadOperations;
	private UnsignedInteger64 RecoveredWriteOperations;
	private UnsignedInteger64 RecoveredSeekOperations;
	private UnsignedInteger64 UnrecoverableSeekOperations;
	
	public UnsignedInteger32 ResetCounter(UnsignedInteger16 SelectedCounter){
		return null;
	}
}
