package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_LaunchInContextCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private UnsignedInteger16 MaxRestrictionListSize = new UnsignedInteger16(0);
	private UnsignedInteger16 MaxLaunchPoints = new UnsignedInteger16(0);
}
