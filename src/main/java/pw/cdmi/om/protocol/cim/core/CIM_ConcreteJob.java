package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_ConcreteJob extends CIM_Job {
	private String InstanceID;
	private String Name;
	private UnsignedInteger16 JobState;
	private Date TimeOfLastStateChange;
	private Date TimeBeforeRemoval = new Date("00000000000500.000000:000");
	private String JobInParameters;
	private String JobOutParameters;
	private UnsignedInteger32 RequestStateChange;
	private UnsignedInteger16 RequestedState;
	private Date TimeoutPeriod;
	
	public UnsignedInteger32 GetError(String Error){
		return null;
	}
	public UnsignedInteger32 GetErrors(String Error[]){
		return null;
	}
}
