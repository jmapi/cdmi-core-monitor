package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_LogicalModule extends CIM_LogicalDevice {
	private UnsignedInteger16 ModuleNumber;
	private UnsignedInteger16 LogicalModuleType = new UnsignedInteger16(0);
	private String OtherLogicalModuleTypeDescription;
}
