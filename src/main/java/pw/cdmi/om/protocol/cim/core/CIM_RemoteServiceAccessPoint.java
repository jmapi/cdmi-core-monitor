package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_RemoteServiceAccessPoint extends CIM_ServiceAccessPoint{
	private String AccessInfo;
	private UnsignedInteger16 InfoFormat;
	private String OtherInfoFormatDescription;
	private UnsignedInteger16 AccessContext = new UnsignedInteger16(0);
	private String OtherAccessContext;
}
