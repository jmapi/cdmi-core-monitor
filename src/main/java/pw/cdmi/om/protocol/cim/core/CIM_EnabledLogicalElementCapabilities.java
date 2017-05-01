package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_EnabledLogicalElementCapabilities extends CIM_Capabilities {
	private boolean ElementNameEditSupported;
	private UnsignedInteger16 MaxElementNameLen;
	private UnsignedInteger16 RequestedStatesSupported[];
	private String ElementNameMask;
}
