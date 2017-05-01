package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalData;

public class CIM_MediaAccessStatData extends CIM_StatisticalData {
	private UnsignedInteger64 ReadOperations;
	private UnsignedInteger64 UnrecoverableReadOperations;
	private UnsignedInteger64 WriteOperations;
	private UnsignedInteger64 UnrecoverableWriteOperations;
	private UnsignedInteger64 RecoveredReadOperations;
	private UnsignedInteger64 RecoveredWriteOperations;
	private UnsignedInteger64 RecoveredSeekOperations;
	private UnsignedInteger64 UnrecoverableSeekOperations;
}
