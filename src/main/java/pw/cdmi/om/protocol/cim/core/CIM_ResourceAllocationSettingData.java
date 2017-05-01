package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_ResourceAllocationSettingData extends CIM_SettingData {
	private UnsignedInteger16 ResourceType;
	private String OtherResourceType;
	private String ResourceSubType;
	private String PoolID;
	private UnsignedInteger16 ConsumerVisibility;
	private String[] HostResource;
	private String AllocationUnits;
	private UnsignedInteger64 VirtualQuantity;
	private UnsignedInteger64 Reservation;
	private UnsignedInteger64 limit;
	private UnsignedInteger32 weight;
	private boolean AutomaticAllocation;
	private boolean AutomaticDeallocation;
	private String Parent;
	private String[] Connection;
	private String Address;
	private UnsignedInteger16 MappingBehavior;
	private String AddressOnParent;
	private String VirtualQuantityUnits = "count";
}
