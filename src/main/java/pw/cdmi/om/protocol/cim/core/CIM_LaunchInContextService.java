package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger32;

public class CIM_LaunchInContextService extends CIM_Service {
	public UnsignedInteger32 CreateLaunchPoint(String LaunchPointSpecification, CIM_ManagedElement[] RestrictedToElement,CIM_LaunchInContextSAP LaunchPoint){
		return null;
	}
	public UnsignedInteger32 RemoveLaunchPoint(CIM_LaunchInContextSAP LaunchPoint, CIM_ManagedElement[] RestrictedToElement){
		return null;
	}
	public UnsignedInteger32 ApplyLaunchPoint(CIM_LaunchInContextSAP LaunchPoint, CIM_ManagedElement[] RestrictedToElement){
		return null;
	}
}
