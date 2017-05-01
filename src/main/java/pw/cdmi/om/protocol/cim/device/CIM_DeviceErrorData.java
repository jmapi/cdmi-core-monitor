package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StatisticalData;

public class CIM_DeviceErrorData extends CIM_StatisticalData {
	private UnsignedInteger64 IndeterminateErrorCount;
	private UnsignedInteger64 CriticalErrorCount;
	private UnsignedInteger64 MajorErrorCount;
	private UnsignedInteger64 MinorErrorCount;
	private UnsignedInteger64 WarningCount;
	private String LastErrorCode;
	private String ErrorDescription;
}
