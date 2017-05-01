package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_VirtualSystemManagementCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private String[] VirtualSystemTypesSupported;
	private UnsignedInteger16[] SynchronousMethodsSupported;
	private UnsignedInteger16[] AsynchronousMethodsSupported;
	private UnsignedInteger16[] IndicationsSupported;
}
