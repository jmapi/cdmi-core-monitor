package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Service;

public class CIM_PrintService extends CIM_Service {
	private String[] PaperTypesAvailable;
	private UnsignedInteger16[] LanguagesSupported;
	private String[] MimeTypesSupported;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private String[] AvailableFilters;
	private String Description;
	private String ElementName;
}
