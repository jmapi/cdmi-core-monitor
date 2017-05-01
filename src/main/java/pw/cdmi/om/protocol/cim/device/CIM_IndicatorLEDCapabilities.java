package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_EnabledLogicalElementCapabilities;

public class CIM_IndicatorLEDCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private UnsignedInteger16[] SupportedIndicatedConditions;
	private String[] OtherSupportedIndicatedConditionDescriptions;
	private UnsignedInteger16[] SupportedControlModes;
	private UnsignedInteger16[] SupportedColors;
	private String[] OtherSupportedColorDescriptions;
	private String[] SupportedControlPatterns;
	private UnsignedInteger16[] SupportedActivationStates;
}
