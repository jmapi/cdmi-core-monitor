package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_PrintInputTray extends CIM_PrinterElement {
	private String ElementName;
	private boolean IsDefault;
	private String LocalizedDescription;
	private UnsignedInteger32 Type;
	private String OtherTypeDescription;
	private UnsignedInteger32 CapacityUnit;
	private String OtherCapacityUnit;// MaxLen(255)
	private UnsignedInteger16 MaxCapacityBasis;
	private UnsignedInteger32 MaxCapacity;
	private UnsignedInteger16 CurrentLevelBasis;
	private UnsignedInteger32 CurrentLevel;
	private UnsignedInteger32 AvailabilityStatus;
	private String OtherAvailabilityStatus;
	private boolean NonCriticalAlertsPresent;
	private boolean CriticalAlertsPresent;
	private String MediaSizeName;
	private String MediaName;
	private UnsignedInteger16 MediaWeightBasisv;
	private UnsignedInteger32 MediaWeight;
	private String MediaType;
	private String MediaColor;
}
