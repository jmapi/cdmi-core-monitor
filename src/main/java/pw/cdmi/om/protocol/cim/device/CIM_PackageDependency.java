package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;
import pw.cdmi.om.protocol.cim.physical.CIM_PhysicalPackage;

public class CIM_PackageDependency extends CIM_Dependency {
	private CIM_LogicalDevice Antecedent;
	private CIM_PhysicalPackage Dependent;
}
