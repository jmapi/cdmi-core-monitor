package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_DeviceStatisticalInformation;

public class CIM_FibrePortEventCounters extends CIM_DeviceStatisticalInformation {
	private UnsignedInteger64 PLOGIsReceived;
	private UnsignedInteger64 PLOGIsSent;
	private UnsignedInteger64 EOFAbortsReceived;
	private UnsignedInteger64 EOFAbortsTransmitted;
	private UnsignedInteger64 PLOGOsReceived;
	private UnsignedInteger64 PLOGOsSent;
	private UnsignedInteger64 PLOGIsRejected;
	private UnsignedInteger64 PLOGOsRejected;
	
	public UnsignedInteger32 ResetCounter(UnsignedInteger16 SelectedCounter){
		return null;
	}
}
