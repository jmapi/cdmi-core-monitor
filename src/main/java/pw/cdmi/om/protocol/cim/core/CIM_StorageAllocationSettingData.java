package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_StorageAllocationSettingData extends CIM_ResourceAllocationSettingData {
	private UnsignedInteger64 VirtualResourceBlockSize;
	private UnsignedInteger64 VirtualQuantity;
	private String VirtualQuantityUnits = "count(fixed size block)";
	private UnsignedInteger16 Access;
	private UnsignedInteger64 HostResourceBlockSize;
	private UnsignedInteger64 Reservation;
	private UnsignedInteger64 limit;
	private UnsignedInteger64 HostExtentStartingAddress;
	private String HostExtentName;
	private UnsignedInteger16 HostExtentNameFormat;
	private String OtherHostExtentNameFormat;
	private UnsignedInteger16 HostExtentNameNamespace;
	private String OtherHostExtentNameNamespace;
}
