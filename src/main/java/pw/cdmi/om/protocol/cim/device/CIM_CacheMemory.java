package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_CacheMemory extends CIM_Memory {
	private UnsignedInteger16 Level;
	private UnsignedInteger16 WritePolicy;
	private UnsignedInteger16 CacheType;
	private UnsignedInteger32 LineSize;
	private UnsignedInteger16 ReplacementPolicy;
	private UnsignedInteger16 ReadPolicy;
	private UnsignedInteger32 FlushTimer;
	private UnsignedInteger16 Associativity;
}
