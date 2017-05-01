package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_Processor extends CIM_LogicalDevice {
	private String Role;
	private UnsignedInteger16 Family;
	private String OtherFamilyDescription;
	private UnsignedInteger16 UpgradeMethod;
	private UnsignedInteger32 MaxClockSpeed;
	private UnsignedInteger32 CurrentClockSpeed;
	private UnsignedInteger16 DataWidth;
	private UnsignedInteger16 AddressWidth;
	private UnsignedInteger16 LoadPercentage;
	private String Stepping;
	private String UniqueID;
	private String CPUStatus;
	private UnsignedInteger32 ExternalBusClockSpeed;
	private UnsignedInteger16[] Characteristics;
	private UnsignedInteger16[] EnabledProcessorCharacteristics;
	private UnsignedInteger16  NumberOfEnabledCores = new UnsignedInteger16(1);
}
