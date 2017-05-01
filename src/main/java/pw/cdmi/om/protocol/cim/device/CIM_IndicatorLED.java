package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_IndicatorLED extends CIM_LogicalDevice {
	private UnsignedInteger16 IndicatedConditions;
	private String OtherIndicatedConditionDescription;
	private UnsignedInteger16 Color;
	private String OtherColorDescription;
	private UnsignedInteger16 ControlMode;
	private UnsignedInteger16 DefaultActivationState;
	private UnsignedInteger16 ActivationState;
	private String ControlPattern;
}
