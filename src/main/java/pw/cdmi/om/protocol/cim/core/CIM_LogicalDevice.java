package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_LogicalDevice extends CIM_EnabledLogicalElement {

	private String SystemCreationClassName;
	private String SystemName;
	private String CreationClassName;
	private String DeviceID;
	private boolean PowerManagementSupported;
	private UnsignedInteger16[] PowerManagementCapabilities;
	private UnsignedInteger16 Availability;
	private UnsignedInteger16 StatusInfo;
	private UnsignedInteger32 LastErrorCode;
	private String ErrorDescription;
	private boolean ErrorCleared;
	private String[] OtherIdentifyingInfo;
	private UnsignedInteger64 PowerOnHours;
	private UnsignedInteger64 TotalPowerOnHours;
	private String[] IdentifyingDescriptions;
	private UnsignedInteger16[] AdditionalAvailability;
	private UnsignedInteger64 MaxQuiesceTime;
	
	
	@Override
	public UnsignedInteger32 RequestStateChange(UnsignedInteger16 RequestedState, CIM_ConcreteJob Job,
		Date TimeoutPeriod) {
		// TODO Auto-generated method stub
		return null;
	}

	public UnsignedInteger32 SetPowerState(UnsignedInteger16 PowerState, Date Time){
		return null;
	}
	
	public UnsignedInteger32 Reset(){
		return null;
	}
	
	public UnsignedInteger32 EnableDevice(boolean Enabled){
		return null;
	}
	
	public UnsignedInteger32 OnlineDevice(boolean Online){
		return null;
	}
	
	public UnsignedInteger32 QuiesceDevice(boolean Quiesce){
		return null;
	}
	
	public UnsignedInteger32 SaveProperties(){
		return null;
	}
	
	public UnsignedInteger32 RestoreProperties(){
		return null;
	}
}