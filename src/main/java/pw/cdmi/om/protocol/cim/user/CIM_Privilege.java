package pw.cdmi.om.protocol.cim.user;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;

public class CIM_Privilege extends CIM_ManagedElement {
	private String InstanceID;
	private boolean PrivilegeGranted = true;
	private UnsignedInteger16[] Activities;
	private String[] ActivityQualifiers;
	private UnsignedInteger16[] QualifierFormats;
	private boolean RepresentsAuthorizationRights = false;
	
}
