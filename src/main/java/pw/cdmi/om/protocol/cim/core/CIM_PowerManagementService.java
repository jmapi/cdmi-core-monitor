package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_PowerManagementService extends CIM_Service {
	public UnsignedInteger32[] SetPowerState(UnsignedInteger16 PowerState, CIM_ManagedElement ManagedElement, Date time){
		return null;
	}
	
	public UnsignedInteger32 RequestPowerStateChange(UnsignedInteger16 PowerState, CIM_ManagedElement ManagedElement, Date time, CIM_ConcreteJob job, Date TimeoutPeriod){
		return null;
	}
}
