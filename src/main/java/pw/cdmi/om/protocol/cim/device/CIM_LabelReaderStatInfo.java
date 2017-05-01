package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_DeviceStatisticalInformation;

public class CIM_LabelReaderStatInfo extends CIM_DeviceStatisticalInformation {
	private UnsignedInteger64 ScanSuccesses;
	private UnsignedInteger64 ScanFailures;
	private UnsignedInteger64 ScanRetries;
	
	public UnsignedInteger32 ResetCounter(UnsignedInteger16 SelectedCounter){
		return null;
	}
}
