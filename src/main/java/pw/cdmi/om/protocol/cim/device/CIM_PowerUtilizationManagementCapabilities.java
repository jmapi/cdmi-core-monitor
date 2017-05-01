package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_EnabledLogicalElementCapabilities;

public class CIM_PowerUtilizationManagementCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private UnsignedInteger16[] PowerUtilizationModesSupported;
	private UnsignedInteger16[] SupportedMethods;
}
