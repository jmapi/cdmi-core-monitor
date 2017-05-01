package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.application.CIM_BIOSElement;
import pw.cdmi.om.protocol.cim.core.CIM_Dependency;

public class CIM_BIOSLoadedInNV extends CIM_Dependency {
	private CIM_Memory Antecedent;
	private CIM_BIOSElement Dependent;
	private UnsignedInteger64 StartingAddress;
	private UnsignedInteger64 EndingAddress;
}
