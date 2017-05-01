package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_ConcreteJob;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;
import pw.cdmi.om.protocol.cim.core.CIM_Service;
import pw.cdmi.om.protocol.cim.core.CIM_System;

public class CIM_SharedDeviceManagementService extends CIM_Service {

	public UnsignedInteger32 ShareDevice(UnsignedInteger16 RequestedAccess, CIM_LogicalDevice Device, CIM_System System, Date TimeoutPeriod,boolean Force,CIM_ConcreteJob job){
		return null;
	}
}
