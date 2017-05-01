package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_ConcreteJob;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalElement;
import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;
import pw.cdmi.om.protocol.cim.core.CIM_Service;
import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_StorageConfigurationService extends CIM_Service {
	public UnsignedInteger32 CreateOrModifyStoragePool(String ElementName, CIM_ConcreteJob job, CIM_StorageSetting goal,UnsignedInteger64 size,String[] InPools,String[] InExtents,CIM_StoragePool pool){
		return null;
	}
	
	
	public UnsignedInteger32 CreateOrModifyElementFromStoragePool(String ElementName,UnsignedInteger16 ElementType, CIM_ConcreteJob job, CIM_ManagedElement goal,UnsignedInteger64 size,CIM_StoragePool InPool,CIM_LogicalElement TheElement){
		return null;
	}
	
	public UnsignedInteger32 DeleteStoragePool(CIM_ConcreteJob job, CIM_StoragePool pool){
		return null;
	}
	
	public UnsignedInteger32 ReturnToStoragePool(CIM_ConcreteJob job, CIM_LogicalElement TheElement){
		return null;
	}
	
	public UnsignedInteger32 CreateReplica(String ElementName, CIM_ConcreteJob job, CIM_LogicalElement SourceElement, CIM_LogicalElement TargetElement, CIM_StorageSetting TargetSettingGoal,CIM_StoragePool TargetPool, UnsignedInteger16 CopyType){
		return null;
	}
	
	public UnsignedInteger32 ModifySynchronization(UnsignedInteger16 Operation,CIM_ConcreteJob job, CIM_StorageSynchronized Synchronization){
		return null;
	}
	
	public UnsignedInteger32 AttachReplica(CIM_ConcreteJob job, CIM_ManagedElement SourceElement, CIM_ManagedElement TargetElement, UnsignedInteger16 CopyType){
		return null;
	}
	
	public UnsignedInteger32 CreateOrModifyElementFromElements(String ElementName, UnsignedInteger16 ElementType, CIM_ConcreteJob job, CIM_ManagedElement goal, UnsignedInteger64 size, CIM_StorageExtent[] InElements, CIM_LogicalElement TheElement){
		return null;
	}
}
