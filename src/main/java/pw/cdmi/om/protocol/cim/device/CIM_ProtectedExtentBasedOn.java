package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_BasedOn;
import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_ProtectedExtentBasedOn extends CIM_BasedOn {
	private CIM_StorageExtent Antecedent;
	private CIM_StorageExtent Dependent;
	private UnsignedInteger64 StartingAddress;
	private UnsignedInteger64 NumberOfBlocks;
	private UnsignedInteger64 BlockSize;
	private UnsignedInteger64 UnitsBeforeCheckDataInterleave;
	private UnsignedInteger64 UnitsOfCheckData;
	private UnsignedInteger64 UnitsOfUserData;
}
