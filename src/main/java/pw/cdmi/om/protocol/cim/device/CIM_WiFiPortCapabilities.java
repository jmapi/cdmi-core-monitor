package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

public class CIM_WiFiPortCapabilities extends CIM_NetworkPortCapabilities {
	private UnsignedInteger16[] SupportedPortTypes;
	private String[] OtherSupportedPortTypes;
}
