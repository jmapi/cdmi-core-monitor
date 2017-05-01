package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;


public class CIM_BlockStatisticsCapabilities extends CIM_StatisticsCapabilities {
	private UnsignedInteger16[] ElementTypesSupported;
	private UnsignedInteger16[] SynchronousMethodsSupported;
	private UnsignedInteger16[] AsynchronousMethodsSupported;
	private UnsignedInteger64 ClockTickInterval;
}
