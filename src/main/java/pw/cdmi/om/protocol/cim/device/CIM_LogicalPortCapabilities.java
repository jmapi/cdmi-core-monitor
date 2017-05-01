package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_EnabledLogicalElementCapabilities;

public class CIM_LogicalPortCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private UnsignedInteger64[] RequestedSpeedsSupported;
	private boolean AutoSenseSpeedConfigurable;
}
