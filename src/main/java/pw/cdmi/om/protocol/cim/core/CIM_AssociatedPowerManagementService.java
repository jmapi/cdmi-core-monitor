package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;

public class CIM_AssociatedPowerManagementService extends CIM_ServiceAvailableToElement {
	private CIM_PowerManagementService ServiceProvided;
	private UnsignedInteger16 PowerState;
	private UnsignedInteger16 OtherPowerState;
	private UnsignedInteger16 RequestedPowerState = new UnsignedInteger16(12);
	private String OtherRequestedPowerState;
	private Date PowerOnTime;
	private UnsignedInteger16 AvailableRequestedPowerStates[];
	private UnsignedInteger16 TransitioningToPowerState;
}
