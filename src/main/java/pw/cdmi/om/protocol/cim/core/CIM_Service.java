package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_Service extends CIM_EnabledLogicalElement {

	private String SystemCreationClassName;
	private String SystemName;
	private String CreationClassName;
	private String name;
	private String PrimaryOwnerName;
	private String PrimaryOwnerContact;
	private String StartMode;
	private String Started;
	
	@Override
	public UnsignedInteger32 RequestStateChange(UnsignedInteger16 RequestedState, CIM_ConcreteJob Job,
		Date TimeoutPeriod) {
		// TODO Auto-generated method stub
		return null;
	}

	public UnsignedInteger32 StartService(){
		return null;
	}
	
	public UnsignedInteger32 StopService(){
		return null;
	}
}
