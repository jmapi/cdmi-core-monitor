package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_AggregatePExtent extends CIM_StorageExtent {
	private UnsignedInteger64 NumberOfBlocks;
	private  UnsignedInteger64 BlocksOfCheckData;
}
