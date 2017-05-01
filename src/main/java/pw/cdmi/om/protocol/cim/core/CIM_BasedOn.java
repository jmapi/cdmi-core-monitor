package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_BasedOn extends CIM_Dependency {
	private CIM_StorageExtent Antecedent;
	private CIM_StorageExtent Dependent;
	private UnsignedInteger64 StartingAddress;
	private UnsignedInteger64 EndingAddress;
	private UnsignedInteger16 OrderIndex;
}
