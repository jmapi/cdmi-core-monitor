package pw.cdmi.om.protocol.cim.physical;

import java.util.Date;

import javax.cim.SingleValue;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_PhysicalMedia extends CIM_PhysicalComponent {
	private UnsignedInteger64 Capacity;
	private UnsignedInteger16 MediaType;
	private String MediaDescription;
	private boolean WriteProtectOn;
	private boolean CleanerMedia;
	private SingleValue MediaSize;
	private UnsignedInteger64 MaxMounts;
	private UnsignedInteger64 MountCount;
	private boolean DualSided;
	private String[] PhysicalLabels;
	private UnsignedInteger16[] LabelStates;
	private UnsignedInteger16[] LabelFormats;
	private Date TimeOfLastMount;
	private UnsignedInteger64 TotalMountTime;
	
	public UnsignedInteger64 getCapacity() {
		return Capacity;
	}
	public UnsignedInteger16 getMediaType() {
		return MediaType;
	}
	public String getMediaDescription() {
		return MediaDescription;
	}
	public boolean isWriteProtectOn() {
		return WriteProtectOn;
	}
	public boolean isCleanerMedia() {
		return CleanerMedia;
	}
	public SingleValue getMediaSize() {
		return MediaSize;
	}
	public UnsignedInteger64 getMaxMounts() {
		return MaxMounts;
	}
	public UnsignedInteger64 getMountCount() {
		return MountCount;
	}
	public boolean isDualSided() {
		return DualSided;
	}
	public String[] getPhysicalLabels() {
		return PhysicalLabels;
	}
	public UnsignedInteger16[] getLabelStates() {
		return LabelStates;
	}
	public UnsignedInteger16[] getLabelFormats() {
		return LabelFormats;
	}
	public Date getTimeOfLastMount() {
		return TimeOfLastMount;
	}
	public UnsignedInteger64 getTotalMountTime() {
		return TotalMountTime;
	}
}
