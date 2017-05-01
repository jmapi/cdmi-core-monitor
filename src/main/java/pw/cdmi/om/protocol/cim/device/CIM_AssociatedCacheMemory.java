package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_AssociatedCacheMemory extends CIM_AssociatedMemory {
	private UnsignedInteger16 Level;
	private String OtherLevelDescription;
	private UnsignedInteger16 WritePolicy;
	private String OtherWritePolicyDescription;
	private String CacheType;
	private String OtherCacheTypeDescription;
	private UnsignedInteger32 LineSize;
	private UnsignedInteger16 ReplacementPolicy;
	private String OtherReplacementPolicyDescription;
	private UnsignedInteger16 ReadPolicy;
	private String OtherReadPolicyDescription;
	private UnsignedInteger32 FlushTimer;
	private UnsignedInteger16 Associativity;
	private String OtherAssociativityDescription;
}
