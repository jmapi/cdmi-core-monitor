package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_NetworkPortCapabilities extends CIM_LogicalPortCapabilities {
	private boolean SpeedConfigurable = false;
	private UnsignedInteger64[] PortSpeedsSupported;
	private boolean NetworkIDsConfigurable;
	private UnsignedInteger16 NetworkIDsFormat;
	private UnsignedInteger16[] LinkTechnologiesSupported;
}
