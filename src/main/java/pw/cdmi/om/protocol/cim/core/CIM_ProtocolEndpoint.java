package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;

public class CIM_ProtocolEndpoint extends CIM_ServiceAccessPoint {
	private String Description;
	private UnsignedInteger16[]  OperationalStatus;
	private UnsignedInteger16 EnabledState;
	private Date TimeOfLastStateChange;
	private String name;
	private String NameFormat;
	private UnsignedInteger16 ProtocolType;
	private UnsignedInteger16 ProtocolIFType;
	private String OtherTypeDescription;
}
