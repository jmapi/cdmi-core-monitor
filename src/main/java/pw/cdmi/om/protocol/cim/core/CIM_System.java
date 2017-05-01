package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_System extends CIM_EnabledLogicalElement {

	private String CreationClassName;
	private String name;
	private String NameFormat;
	private String PrimaryOwnerName;
	private String PrimaryOwnerContact;
	private String[] Roles;
	private String[] OtherIdentifyingInfo;
	private String[] IdentifyingDescriptions;
	
	@Override
	public UnsignedInteger32 RequestStateChange(UnsignedInteger16 RequestedState, CIM_ConcreteJob Job,
		Date TimeoutPeriod) {
		// TODO Auto-generated method stub
		return null;
	}

}
