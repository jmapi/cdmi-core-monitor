package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_PrintMarker extends CIM_PrinterElement {
	private boolean IsDefault;
	private UnsignedInteger32 MarkTech;
	private String OtherMarkTechDescription;// Maxlen(255)
	private UnsignedInteger32 CounterUnit;
	private String OtherCounterUnit;
	private UnsignedInteger32 LifeCount;
	private UnsignedInteger32 PowerOnCount;
	private UnsignedInteger32 ProcessColorants;
	private UnsignedInteger32 SpotColorants;
	private UnsignedInteger32 AvailabilityStatus;
	private String OtherAvailabilityStatus;
	private boolean NonCriticalAlertsPresent;
	private boolean CriticalAlertsPresent;
	private UnsignedInteger16 AddressabilityBasis;
	private UnsignedInteger32 FeedAddressability;
	private UnsignedInteger32 XFeedAddressability;
}
