package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_PhysicalExtent extends CIM_StorageExtent {
	private UnsignedInteger64 BlockSize;
	private UnsignedInteger64 NumberOfBlocks;
	private UnsignedInteger64 UnitsBeforeCheckDataInterleave;
	private UnsignedInteger64 UnitsOfCheckData;
	private UnsignedInteger64 UnitsOfUserData;
}
