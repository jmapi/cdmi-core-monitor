package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_TapePartition extends CIM_MediaPartition {
	private UnsignedInteger16 NameFormat = new UnsignedInteger16(1);
	private UnsignedInteger16 NameNamespace = new UnsignedInteger16(1);
	private UnsignedInteger64 UnusedBlocks;
}
