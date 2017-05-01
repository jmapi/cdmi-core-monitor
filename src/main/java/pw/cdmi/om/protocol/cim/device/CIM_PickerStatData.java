package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalData;

public class CIM_PickerStatData extends CIM_StatisticalData {
	private UnsignedInteger64 PickSuccesses;
	private UnsignedInteger64 PickFailures;
	private UnsignedInteger64 PickRetries;
	private UnsignedInteger64 PutSuccesses;
	private UnsignedInteger64 PutFailures;
	private UnsignedInteger64 PutRetries;

}
