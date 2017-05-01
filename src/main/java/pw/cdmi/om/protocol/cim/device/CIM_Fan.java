package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_Fan extends CIM_CoolingDevice {
	private boolean VariableSpeed;
	private UnsignedInteger64 DesiredSpeed;
	
	public UnsignedInteger32 SetSpeed(UnsignedInteger64 DesiredSpeed){
		return null;
	}
}
