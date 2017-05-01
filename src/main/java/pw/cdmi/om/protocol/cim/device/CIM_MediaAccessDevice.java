package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_MediaAccessDevice extends CIM_LogicalDevice {
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private String ErrorMethodology;
	private String CompressionMethod;
	private UnsignedInteger32 NumberOfMediaSupported;
	private UnsignedInteger64 MaxMediaSize;
	private UnsignedInteger64 DefaultBlockSize;
	private UnsignedInteger64 MaxBlockSize;
	private UnsignedInteger64 MinBlockSize;
	private boolean NeedsCleaning;
	private boolean MediaIsLocked;
	private UnsignedInteger16 Security;
	private Date LastCleaned;
	private UnsignedInteger64 MaxAccessTime;
	private UnsignedInteger32 UncompressedDataRate;
	private UnsignedInteger64 LoadTime;
	private UnsignedInteger64 UnloadTime;
	private UnsignedInteger64 MountCount;
	private Date  TimeOfLastMount;
	private UnsignedInteger64 TotalMountTime;
	private String UnitsDescription;
	private UnsignedInteger64 MaxUnitsBeforeCleaning;
	private UnsignedInteger64 UnitsUsed;
	
	public UnsignedInteger32 LockMedia(boolean lock){
		return null;
	}
}
