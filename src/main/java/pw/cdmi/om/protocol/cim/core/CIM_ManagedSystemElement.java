package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;

public class CIM_ManagedSystemElement extends CIM_ManagedElement{
	private Date InstallDate;
	private String Name;
	private UnsignedInteger16 OperationalStatus[];
	private String StatusDescriptions[];
	private String Status;
	private UnsignedInteger16 HealthState;
	private UnsignedInteger16 CommunicationStatus;
	private UnsignedInteger16 DetailedStatus;
	private UnsignedInteger16 OperatingStatus;
	private UnsignedInteger16 PrimaryStatus;
}
