package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_CompositeExtent extends CIM_StorageExtent {
	private UnsignedInteger64 NumberOfBlocks;
	private UnsignedInteger64 ExtentStripeLength;
	private UnsignedInteger64 ExtentInterleaveDepth;
	private boolean IsConcatenated;
}
