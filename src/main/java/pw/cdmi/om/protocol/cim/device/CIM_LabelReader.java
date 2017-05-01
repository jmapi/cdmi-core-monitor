package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_LabelReader extends CIM_LogicalDevice {
	private UnsignedInteger16 SupportedFormats;
	private UnsignedInteger16 Technology;
}
