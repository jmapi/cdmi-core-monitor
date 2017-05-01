package pw.cdmi.wmi;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.device.CIM_LogicalDisk;

public class Win32_LogicalDisk extends CIM_LogicalDisk {
	UnsignedInteger16 Access;

	UnsignedInteger16 Availability;

	UnsignedInteger64 BlockSize;

	String Caption;

	boolean Compressed;

	UnsignedInteger32 ConfigManagerErrorCode;

	boolean ConfigManagerUserConfig;

	String CreationClassName;

	String Description;

	String DeviceID;

	UnsignedInteger32 DriveType;

	boolean ErrorCleared;

	String ErrorDescription;

	String ErrorMethodology;

	String FileSystem;

	UnsignedInteger64 FreeSpace;

	Date InstallDate;

	UnsignedInteger32 LastErrorCode;

	UnsignedInteger32 MaximumComponentLength;

	UnsignedInteger32 MediaType;

	String Name;

	UnsignedInteger64 NumberOfBlocks;

	String PNPDeviceID;

	UnsignedInteger16 PowerManagementCapabilities[];

	boolean PowerManagementSupported;

	String ProviderName;

	String Purpose;

	boolean QuotasDisabled;

	boolean QuotasIncomplete;

	boolean QuotasRebuilding;

	UnsignedInteger64 Size;

	String Status;

	UnsignedInteger16 StatusInfo;

	boolean SupportsDiskQuotas;

	boolean SupportsFileBasedCompression;

	String SystemCreationClassName;

	String SystemName;

	boolean VolumeDirty;

	String VolumeName;

	String VolumeSerialNumber;
}
