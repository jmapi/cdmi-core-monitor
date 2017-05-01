package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_VolumeSet extends CIM_StorageVolume {
	private UnsignedInteger64 NumberOfBlocks;
	private UnsignedInteger64 PSExtentStripeLength;
	private UnsignedInteger64 PSExtentInterleaveDepth;
	private UnsignedInteger16 VolumeStatus;

}
