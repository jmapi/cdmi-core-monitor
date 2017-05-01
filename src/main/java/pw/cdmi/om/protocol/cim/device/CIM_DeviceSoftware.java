package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.application.CIM_SoftwareElement;
import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_DeviceSoftware extends CIM_Dependency {
	private CIM_SoftwareElement Antecedent;
	private CIM_LogicalDevice Dependent;
	private UnsignedInteger16 Purpose;
	private String PurposeDescription;
	private boolean LoadedOnDevice;
	private boolean UpgradeableOnDevice;
}
