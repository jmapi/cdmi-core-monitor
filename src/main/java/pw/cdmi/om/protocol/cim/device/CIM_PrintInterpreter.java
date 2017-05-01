package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_PrintInterpreter extends CIM_PrinterElement {
	private String LocalizedDescription;
	private UnsignedInteger32 LangType;
	private String OtherLangTypeDescription;
	private String LangLevel;
	private UnsignedInteger16 AddressabilityBasis;
	private UnsignedInteger32 FeedAddressability;
	private UnsignedInteger32 XFeedAddressability;
	private String DefaultCharSetIn;//Maxlen(63)
	private UnsignedInteger32 AvailabilityStatus;
	private String OtherAvailabilityStatus;
	private boolean NonCriticalAlertsPresent;
	private boolean CriticalAlertsPresent;
}
