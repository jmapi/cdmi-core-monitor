package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalElement;
import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_StoragePool extends CIM_LogicalElement {
	private String InstanceID;
	private String PoolID;
	private boolean Primordial = false;
	private UnsignedInteger64 TotalManagedSpace;
	private UnsignedInteger64 RemainingManagedSpace;
	
	public UnsignedInteger32 GetSupportedSizes(UnsignedInteger16 ElementType, CIM_StorageSetting goal,UnsignedInteger64[] sizes){
		return null;
	}
	
	public UnsignedInteger32 GetSupportedSizeRange(UnsignedInteger16 ElementType, CIM_StorageSetting goal,UnsignedInteger64 MinimumVolumeSize, UnsignedInteger64 MaximumVolumeSize, UnsignedInteger64 VolumeSizeDivisor ){
		return null;
	}
	
	public UnsignedInteger32 GetAvailableExtents(CIM_StorageSetting goal, CIM_StorageExtent[]  AvailableExtents){
		return null;
	}
}
