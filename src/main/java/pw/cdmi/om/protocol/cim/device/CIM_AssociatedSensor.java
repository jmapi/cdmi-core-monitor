package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_ManagedSystemElement;

public class CIM_AssociatedSensor extends CIM_Dependency {
	private CIM_Sensor Antecedent;
	private CIM_ManagedSystemElement Dependent;
}
