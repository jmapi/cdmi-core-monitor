package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.system.CIM_VirtualSystemSettingData;

public class CIM_VirtualSystemManagementService extends CIM_Service {

	public UnsignedInteger32 AddResourceSettings(CIM_VirtualSystemSettingData AffectedConfiguration, String[] ResourceSettings, CIM_ResourceAllocationSettingData[] ResultingResourceSettings,CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 DefineSystem(String SystemSettings, String[] ResourceSettings, CIM_VirtualSystemSettingData ReferenceConfiguration, CIM_ComputerSystem ResultingSystem,CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 DestroySystem(CIM_ComputerSystem AffectedSystem, CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 ModifyResourceSettings(String[] ResourceSettings, CIM_ResourceAllocationSettingData[] ResultingResourceSettings,  CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 ModifySystemSettings(String SystemSettings,  CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 RemoveResourceSettings(CIM_ResourceAllocationSettingData[] ResourceSettings,  CIM_ConcreteJob job){
		return null;
	}
}
