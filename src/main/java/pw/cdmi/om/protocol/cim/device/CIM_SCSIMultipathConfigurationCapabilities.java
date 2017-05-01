package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;

public class CIM_SCSIMultipathConfigurationCapabilities extends CIM_Capabilities {
	private UnsignedInteger16[] SupportedLoadBalanceTypes;
	private String[] OtherSupportedLoadBalanceAlgorithmNames;
	private String[] OtherSupportedLoadBalanceVendorNames;
	private boolean CanSetTPGAccess = false;
	private boolean CanOverridePaths = false;
	private boolean ExposesPathDeviceFiles;
	private String DeviceNameFilespace;
	private boolean OnlySupportsSpecifiedProducts = false;
	private UnsignedInteger32 MaximumWeight;
	private UnsignedInteger16 AutofailbackSupport;
	private boolean AutoFailbackEnabled;
	private UnsignedInteger32 PollingRateMax;
	private UnsignedInteger32 CurrentPollingRate;
	private UnsignedInteger16 DefaultLoadBalanceType;
}
