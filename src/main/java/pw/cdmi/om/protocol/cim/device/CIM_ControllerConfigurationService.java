package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_ConcreteJob;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;
import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;
import pw.cdmi.om.protocol.cim.core.CIM_Service;
import pw.cdmi.om.protocol.cim.user.CIM_Privilege;

public class CIM_ControllerConfigurationService extends CIM_Service {
	
	public UnsignedInteger32 CreateProtocolControllerWithPorts(String ElementName, String[] ports, UnsignedInteger16 Protocol, CIM_Privilege Privilege,CIM_ManagedElement Identity,CIM_ProtocolController  ProtocolController){
		return null;
	}
	
	public UnsignedInteger32 DeleteProtocolController(CIM_ProtocolController ProtocolController, boolean DeleteChildrenProtocolControllers, boolean DeleteUnits){
		return null;
	}
	
	public UnsignedInteger32 AttachDevice(CIM_ProtocolController ProtocolController, CIM_LogicalDevice Device, String DeviceNumber){
		return null;
	}
	
	public UnsignedInteger32 DetachDevice(CIM_ProtocolController ProtocolController, CIM_LogicalDevice Device){
		return null;
	}
	
	public UnsignedInteger32 ExposePaths(CIM_ConcreteJob job, String[] LUNames, String[] InitiatorPortIDs,  String[] TargetPortIDs, String[] DeviceNumbers, UnsignedInteger16[] DeviceAccesses, CIM_SCSIProtocolController[] ProtocolControllers ){
		return null;
	}
	
	public UnsignedInteger32 HidePaths(CIM_ConcreteJob job, String[] LUNames, String[] InitiatorPortIDs,  String[] TargetPortIDs,  CIM_SCSIProtocolController[] ProtocolControllers ){
		return null;
	}
}
