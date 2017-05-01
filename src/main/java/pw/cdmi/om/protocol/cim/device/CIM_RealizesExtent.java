package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_Realizes;
import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;
import pw.cdmi.om.protocol.cim.physical.CIM_PhysicalComponent;

public class CIM_RealizesExtent extends CIM_Realizes {
	private CIM_PhysicalComponent Antecedent;
	private CIM_StorageExtent Dependent;
	private UnsignedInteger64 StartingAddress;
}
