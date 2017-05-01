package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;

public class CIM_PCIController extends CIM_Controller {
	private UnsignedInteger16 CommandRegister;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16 DeviceSelectTiming;
	private UnsignedInteger8 ClassCode;
	private UnsignedInteger8 CacheLineSize;
	private UnsignedInteger8 LatencyTimer;
	private UnsignedInteger16 InterruptPin;
	private UnsignedInteger32 ExpansionROMBaseAddress;
	private boolean SelfTestEnabled;
	
	public UnsignedInteger8 BISTExecution(){
		return null;
	}
}
