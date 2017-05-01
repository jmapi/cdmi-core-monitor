package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

public class CIM_PCIeSwitch extends CIM_PCIDevice {
	private UnsignedInteger16 NumberOfPorts;
	private UnsignedInteger8[] SecondaryBusNumbers;
}
