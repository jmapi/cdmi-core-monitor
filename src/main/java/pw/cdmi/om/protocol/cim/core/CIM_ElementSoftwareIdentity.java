package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_ElementSoftwareIdentity extends CIM_Dependency {
	private CIM_SoftwareIdentity Antecedent;
	private CIM_ManagedElement Dependent;
	private UnsignedInteger16 UpgradeCondition;
	private String OtherUpgradeCondition;
	private UnsignedInteger16 ElementSoftwareStatus[];
}
