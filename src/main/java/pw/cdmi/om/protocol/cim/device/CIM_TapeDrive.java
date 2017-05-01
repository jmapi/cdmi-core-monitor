package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_TapeDrive extends CIM_MediaAccessDevice {
	private UnsignedInteger32 EOTWarningZoneSize;
	private UnsignedInteger32 MaxPartitionCount;
	private UnsignedInteger32 Padding;
	private UnsignedInteger64 MaxRewindTime;
}
