package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;
import pw.cdmi.om.protocol.cim.physical.CIM_PhysicalMedia;

public class CIM_RealizedOnSide extends CIM_RealizesExtent {
	private CIM_PhysicalMedia Antecedent;
	private CIM_StorageExtent Dependent;
	private UnsignedInteger16 side;
}
