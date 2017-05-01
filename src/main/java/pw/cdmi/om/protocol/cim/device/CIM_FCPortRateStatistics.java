package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalData;

public class CIM_FCPortRateStatistics extends CIM_StatisticalData {
	private UnsignedInteger64 TxFrameRate;
	private UnsignedInteger64 RxFrameRate;
	private UnsignedInteger64 MaxTxFrameRate;
	private UnsignedInteger64 MaxRxFrameRate;
	private UnsignedInteger64 TxRate;
	private UnsignedInteger64 RxRate;
	private UnsignedInteger64 PeakTxRate;
	private UnsignedInteger64 PeakRxRate;
}
