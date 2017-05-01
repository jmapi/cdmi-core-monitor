package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_EnabledLogicalElementCapabilities;

public class CIM_ProcessorCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private UnsignedInteger16 NumberOfProcessorCores;
	private UnsignedInteger16 NumberOfHardwareThreads;
}
