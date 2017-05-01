package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_EnabledLogicalElement;

public class CIM_ProcessorCore extends CIM_EnabledLogicalElement {
	private String InstanceID;
	private UnsignedInteger16 CoreEnabledState;
	private UnsignedInteger16 LoadPercentage;
}
