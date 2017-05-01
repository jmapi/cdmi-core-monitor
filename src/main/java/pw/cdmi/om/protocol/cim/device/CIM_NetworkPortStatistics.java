package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalData;

public class CIM_NetworkPortStatistics extends CIM_StatisticalData {
	private UnsignedInteger64 BytesTransmitted;
	private UnsignedInteger64 BytesReceived;
	private UnsignedInteger64 PacketsTransmitted;
	private UnsignedInteger64 PacketsReceived;
}
