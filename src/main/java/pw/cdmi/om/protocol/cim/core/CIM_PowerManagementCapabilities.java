package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_PowerManagementCapabilities extends CIM_Capabilities {
	private UnsignedInteger16[] PowerCapabilities;
	private String[] OtherPowerCapabilitiesDescriptions;
	private UnsignedInteger16[] PowerStatesSupported;
	private UnsignedInteger16[] PowerChangeCapabilities;
	private String OtherPowerChangeCapabilities;
	private UnsignedInteger16[] RequestedPowerStatesSupported;
}
