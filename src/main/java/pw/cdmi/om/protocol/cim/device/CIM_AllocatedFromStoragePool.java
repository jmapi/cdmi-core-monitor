package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalElement;

public class CIM_AllocatedFromStoragePool extends CIM_Dependency {
	private CIM_StoragePool Antecedent;
	private CIM_LogicalElement Dependent;
	private UnsignedInteger64 SpaceConsumed;
}
