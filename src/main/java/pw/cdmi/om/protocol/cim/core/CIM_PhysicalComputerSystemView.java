package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.SignedInteger32;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

public class CIM_PhysicalComputerSystemView extends CIM_View {

	private String InstanceID;
	private UnsignedInteger16 EnabledState;
	private UnsignedInteger16 RequestedState;
	private UnsignedInteger16[] OperationalStatus;
	private UnsignedInteger16 HealthState;
	private boolean FRUInfoSupported;
	private String Tag;
	private String Manufacturer;
	private String Model;
	private String SKU;
	private String SerialNumber;
	private String Version;
	private String PartNumber;
	private UnsignedInteger16[] PowerUtilizationModesSupported;
	private UnsignedInteger16 PowerUtilizationMode;
	private UnsignedInteger64 PowerAllocationLimit;
	private String[] NumericSensorElementName;
	private UnsignedInteger16[] NumericSensorEnabledState;
	private UnsignedInteger16[] NumericSensorHealthState;
	private String[] NumericSensorCurrentState;
	private UnsignedInteger16[] NumericSensorPrimaryStatus;
	private UnsignedInteger16[] NumericSensorBaseUnits;
	private SignedInteger32[] NumericSensorUnitModifier;
	private UnsignedInteger16[] NumericSensorRateUnits;
	private SignedInteger32[] NumericSensorCurrentReading;
	private UnsignedInteger16[] NumericSensorSensorType;
	private String[] NumericSensorOtherSensorTypeDescription;
	private SignedInteger32[] NumericSensorUpperThresholdNonCritical;
	private SignedInteger32[] NumericSensorUpperThresholdCritical;
	private SignedInteger32[] NumericSensorUpperThresholdFatal;
	private String[] LogInstanceID;
	private UnsignedInteger64[] LogMaxNumberOfRecords;
	private UnsignedInteger64[] LogCurrentNumberOfRecords;
	private UnsignedInteger16[] LogOverWritePolicy;
	private UnsignedInteger16[] LogState;
	private String[] StructuredBootString;
	private UnsignedInteger8[] PersistentBootConfigOrder;
	private UnsignedInteger8 OneTimeBootSource;
	private UnsignedInteger16 NumberOfProcessors;
	private UnsignedInteger16 NumberOfProcessorCores;
	private UnsignedInteger16 NumberOfProcessorThreads;
	private UnsignedInteger16 ProcessorFamily;
	private UnsignedInteger32 ProcessorMaxClockSpeed;
	private UnsignedInteger64 MemoryBlockSize;
	private UnsignedInteger64 MemoryNumberOfBlocks;
	private UnsignedInteger64 MemoryConsumableBlocks;
	private UnsignedInteger16 CurrentBIOSMajorVersion;
	private UnsignedInteger16 CurrentBIOSMinorVersion;
	private UnsignedInteger16 CurrentBIOSRevisionNumber;
	private UnsignedInteger16 CurrentBIOSBuildNumber;
	private UnsignedInteger16 CurrentManagementFirmwareMajorVersion;
	private UnsignedInteger16 CurrentManagementFirmwareMinorVersion;
	private UnsignedInteger16 CurrentManagementFirmwareRevisionNumber;
	private UnsignedInteger16 CurrentManagementFirmwareBuildNumber;
	private String CurrentManagementFirmwareElementName;
	private String CurrentManagementFirmwareVersionString;
	private UnsignedInteger16 OSType;
	private String OSVersion;
	private UnsignedInteger16 OSEnabledState;
	private String CurrentBIOSVersionString;
	private UnsignedInteger16[] Dedicated;
	private String[] IdentifyingDescriptions;
	private String[] OtherDedicatedDescriptions;
	private String[] OtherIdentifyingInfo;
	private UnsignedInteger32 ProcessorCurrentClockSpeed;
	private String[] NumericSensorContext;
	private SignedInteger32[] NumericSensorLowerThresholdCritical;
	private SignedInteger32[] NumericSensorLowerThresholdFatal;
	private SignedInteger32[] NumericSensorLowerThresholdNonCritical;
	
	public UnsignedInteger32 RequestStateChange(UnsignedInteger16 RequestedState, CIM_ConcreteJob Job, Date TimeoutPeriod){
		return null;
	}
	
	public UnsignedInteger32 ClearLog(String LogInstanceID, UnsignedInteger32 InstallSoftwareFromURI, CIM_ConcreteJob Job, UnsignedInteger16[] Classifications, String URI, UnsignedInteger16[] InstallOptions,String[] InstallOptionsValues){
		return null;
	}
	
	public UnsignedInteger32 ModifyPersistentBootConfigOrder(String[] StructuredBootString, CIM_ConcreteJob Job){
		return null;
	}
	
	public UnsignedInteger32 SetOneTimeBootSource(String StructuredBootString, CIM_ConcreteJob Job){
		return null;
	}
}
