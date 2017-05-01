package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_ResourcePool extends CIM_LogicalElement {
	private String InstanceID;
	private String PoolID;
	private boolean Primordial = false;
	private UnsignedInteger64 Capacity;
	private UnsignedInteger64 Reserved;
	private UnsignedInteger16 ResourceType;
	private String OtherResourceType;
	private String ResourceSubType;
	private String AllocationUnits;
	private String ConsumedResourceUnits = "count";
	private UnsignedInteger64 CurrentlyConsumedResource;
	private UnsignedInteger64 MaxConsumableResource;
}
