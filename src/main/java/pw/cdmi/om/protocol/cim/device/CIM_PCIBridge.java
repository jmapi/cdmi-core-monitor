package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;

public class CIM_PCIBridge extends CIM_PCIDevice {
	private UnsignedInteger16 BridgeType;
	private UnsignedInteger8 SecondaryLatencyTimer;
	private UnsignedInteger8 SubordinateBusNumber;
	private UnsignedInteger8 SecondayBusNumber;
	private UnsignedInteger8 PrimaryBusNumber;
	private UnsignedInteger16 SecondaryStatusRegister;
	private UnsignedInteger16 SecondaryBusDeviceSelectTiming;
	private UnsignedInteger8 IOLimit;
	private UnsignedInteger8 IOBase;
	private UnsignedInteger16 MemoryLimit;
	private UnsignedInteger16 MemoryBase;
	private UnsignedInteger16 PrefetchMemoryLimit;
	private UnsignedInteger16 PrefetchMemoryBase;
	private UnsignedInteger32 PrefetchLimitUpper32;
	private UnsignedInteger32 PrefetchBaseUpper32;
	private UnsignedInteger16 IOLimitUpper16;
	private UnsignedInteger16 IOBaseUpper16;
}
