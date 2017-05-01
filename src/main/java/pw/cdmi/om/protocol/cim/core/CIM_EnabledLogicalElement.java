package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_EnabledLogicalElement extends CIM_LogicalElement {
	private UnsignedInteger16 EnabledState = new UnsignedInteger16(5);
	private String OtherEnabledState;
	private UnsignedInteger16 RequestedState = new UnsignedInteger16(12);
	private UnsignedInteger16 EnabledDefault = new UnsignedInteger16(2);
	private Date TimeOfLastStateChange;
	private UnsignedInteger16 AvailableRequestedStates[];
	private UnsignedInteger16 TransitioningToState = new UnsignedInteger16(12);
	
	public UnsignedInteger32 RequestStateChange(UnsignedInteger16 RequestedState, CIM_ConcreteJob Job, Date TimeoutPeriod){
		return null;
	}
}
