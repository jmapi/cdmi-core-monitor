package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;

public class CIM_PCIDevice extends CIM_PCIController {
	private UnsignedInteger32[] BaseAddress;
	private UnsignedInteger16 SubsystemID;
	private UnsignedInteger16 SubsystemVendorID;
	private UnsignedInteger8 MinGrantTime;
	private UnsignedInteger8 MaxLatency;
	private UnsignedInteger8 BusNumber;
	private UnsignedInteger8 DeviceNumber;
	private UnsignedInteger8 FunctionNumber;
	private UnsignedInteger16 PCIDeviceID;
	private UnsignedInteger16 VendorID;
	private UnsignedInteger8 RevisionID;
}
