package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;

public class CIM_StatisticsCapabilities extends CIM_Capabilities {
	private UnsignedInteger16[] ElementTypesSupported;
	private UnsignedInteger16[] SynchronousMethodsSupported;
	private UnsignedInteger16[] AsynchronousMethodsSupported;
	
}
