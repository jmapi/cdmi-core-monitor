package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;

public class CIM_ProtocolControllerMaskingCapabilities extends CIM_Capabilities {
	private UnsignedInteger16[] ValidHardwareIdTypes;
	private String[] OtherValidHardwareIDTypes;
	private UnsignedInteger16 PortsPerView = new UnsignedInteger16(2);
	private boolean ClientSelectableDeviceNumbers = true;
	private boolean AttachDeviceSupported;
	private boolean OneHardwareIDPerView = false;
	private boolean UniqueUnitNumbersPerPort = false;
	private boolean PrivilegeDeniedSupported = false;
	private boolean ProtocolControllerRequiresAuthorizedIdentity = false;
	private boolean ProtocolControllerSupportsCollections = false;
	private boolean ExposePathsSupported;
	private boolean CreateProtocolControllerSupported;
	private UnsignedInteger16 MaximumMapCount = new UnsignedInteger16(0);
	private boolean SPCAllowsNoLUs = false;
	private boolean SPCAllowsNoTargets = false;
	private boolean SPCAllowsNoInitiators = false;
	private boolean SPCSupportsDefaultViews = true;
}
