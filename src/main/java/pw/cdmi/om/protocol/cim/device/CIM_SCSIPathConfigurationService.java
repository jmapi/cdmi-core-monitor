package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;
import pw.cdmi.om.protocol.cim.core.CIM_Service;

public class CIM_SCSIPathConfigurationService extends CIM_Service {
	public UnsignedInteger32 SetTPGAccess(CIM_LogicalDevice LogicalUnit, CIM_SCSITargetPortGroup[] TargetPortGroups, UnsignedInteger16[] AccessStates){
		return null;
	}
	
	public UnsignedInteger32 SetLoadBalanceAlgorithm(CIM_LogicalDevice LogicalDevice, UnsignedInteger16 LoadBalanceAlgorithm, String OtherLoadBalanceAlgorithmDescription){
		return null;
	}
	
	public UnsignedInteger32 AssignLogicalUnitToPortGroup(CIM_LogicalDevice LogicalUnit, CIM_SCSITargetPortGroup TargetPortGroup){
		return null;
	}
	
	public UnsignedInteger32 SetOverridePath(CIM_SCSIInitiatorTargetLogicalUnitPath path){
		return null;
	}
	
	public UnsignedInteger32 CancelOverridePath(CIM_LogicalDevice LogicalUnit){
		return null;
	}
}
