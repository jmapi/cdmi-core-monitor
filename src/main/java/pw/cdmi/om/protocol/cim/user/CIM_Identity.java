package pw.cdmi.om.protocol.cim.user;

import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;


public class CIM_Identity extends CIM_ManagedElement {
	private String InstanceID;
	private boolean CurrentlyAuthenticated = false;
}
