package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalData;

public class CIM_LabelReaderStatData extends CIM_StatisticalData {
	private UnsignedInteger64 ScanSuccesses;
	private UnsignedInteger64 ScanFailures;
	private UnsignedInteger64 ScanRetries;
	
}
