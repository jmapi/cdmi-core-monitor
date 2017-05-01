package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalElement;

public class CIM_AssociatedMemory extends CIM_Dependency {
	private CIM_Memory Antecedent;
	private CIM_LogicalElement Dependent;
}
