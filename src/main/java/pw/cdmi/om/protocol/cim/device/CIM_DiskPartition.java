package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

public class CIM_DiskPartition extends CIM_MediaPartition {
	private boolean PrimaryPartition;
	private UnsignedInteger16 PartitionType;
	private UnsignedInteger16 PartitionSubtype;
}
