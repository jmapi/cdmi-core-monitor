package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.physical.CIM_PhysicalPackage;


public class CIM_PackageAlarm extends CIM_PackageDependency {
	private CIM_AlarmDevice Antecedent;
	private CIM_PhysicalPackage Dependent;
}
