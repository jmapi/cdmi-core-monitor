package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_DeviceStatisticalInformation;

public class CIM_PickerStatInfo extends CIM_DeviceStatisticalInformation {
	private UnsignedInteger64 PickSuccesses;
	private UnsignedInteger64 PickFailures;
	private UnsignedInteger64 PickRetries;
	private UnsignedInteger64 PutSuccesses;
	private UnsignedInteger64 PutFailures;
	private UnsignedInteger64 PutRetries;

	public UnsignedInteger32 ResetCounter(UnsignedInteger16 SelectedCounter){
		return null;
	}
}
