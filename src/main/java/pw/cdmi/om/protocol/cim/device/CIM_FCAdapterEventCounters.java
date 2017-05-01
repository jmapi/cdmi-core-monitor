package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_DeviceStatisticalInformation;

public class CIM_FCAdapterEventCounters extends CIM_DeviceStatisticalInformation {
	private UnsignedInteger64 ABTSFramesReceived;
	private UnsignedInteger64 ABTSFramesSent;
	private UnsignedInteger64 FBSYsReceived;
	private UnsignedInteger64 PBSYsReceived;
	private UnsignedInteger64 PBSYsSent;
	private UnsignedInteger64 FRJTsReceived;
	private UnsignedInteger64 PRJTsReceived;
	private UnsignedInteger64 PRJTsSent;
	private UnsignedInteger64 PRLIsRejected;
	private UnsignedInteger64 PRLOsRejected;
	private UnsignedInteger64 ABTSFramesRejected;
	
	public UnsignedInteger32 ResetCounter(UnsignedInteger16 SelectedCounter){
		return null;
	}
}
