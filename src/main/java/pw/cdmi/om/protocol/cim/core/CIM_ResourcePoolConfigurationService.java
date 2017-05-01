package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger32;

public class CIM_ResourcePoolConfigurationService extends CIM_Service {

	public UnsignedInteger32 CreateResourcePool(String ElementName, CIM_LogicalDevice[] HostResources, String ResourceType, CIM_ResourcePool pool, CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 CreateChildResourcePool(String ElementName, String[] Settings, CIM_ResourcePool[] ParentPool, CIM_ResourcePool pool, CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 DeleteResourcePool(CIM_ResourcePool pool, CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 AddResourcesToResourcePool(CIM_LogicalDevice[] HostResources, CIM_ResourcePool pool, CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 RemoveResourcesFromResourcePool(CIM_LogicalDevice[] HostResources, CIM_ResourcePool pool, CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 ChangeParentResourcePool(CIM_ResourcePool ChildPool, CIM_ResourcePool[] ParentPool,String[] Settings, CIM_ConcreteJob job){
		return null;
	}
}
