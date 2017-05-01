package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_EnabledLogicalElementCapabilities;

public class CIM_AlarmDeviceCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private UnsignedInteger16[] RequestedAlarmStatesSupported;
	private UnsignedInteger16[] AlarmIndicatorTypesConfigurable;
}
