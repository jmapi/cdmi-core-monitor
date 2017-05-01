package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_IsSpare extends CIM_Dependency {
	private CIM_ManagedElement Antecedent;
	private CIM_RedundancySet Dependent;
	private UnsignedInteger16 SpareStatus;
	private UnsignedInteger16 FailoverSupported;
}
