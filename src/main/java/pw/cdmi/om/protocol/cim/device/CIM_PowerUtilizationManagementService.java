package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_ConcreteJob;
import pw.cdmi.om.protocol.cim.core.CIM_Service;

public class CIM_PowerUtilizationManagementService extends CIM_Service {
	private UnsignedInteger16 PowerUtilizationMode = new UnsignedInteger16(2);
	
	public UnsignedInteger32 ApplyPowerAllocationSettings(String PowerAllocationSettings, CIM_ConcreteJob job){
		return null;
	}
}
