package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_BasedOn;

public class CIM_PSExtentBasedOnPExtent extends CIM_BasedOn {
	private CIM_PhysicalExtent Antecedent;
	private CIM_ProtectedSpaceExtent Dependent;
	private UnsignedInteger64 StartingAddress;
}
