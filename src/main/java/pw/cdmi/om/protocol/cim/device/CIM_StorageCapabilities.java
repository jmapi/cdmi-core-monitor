package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;

public class CIM_StorageCapabilities extends CIM_Capabilities {
	private UnsignedInteger16 ElementType;
	private boolean NoSinglePointOfFailure;
	private boolean NoSinglePointOfFailureDefault;
	private UnsignedInteger16 DataRedundancyMax;
	private UnsignedInteger16 DataRedundancyMin;
	private UnsignedInteger16 DataRedundancyDefault;
	private UnsignedInteger16 PackageRedundancyMax;
	private UnsignedInteger16 PackageRedundancyMin;
	private UnsignedInteger16 PackageRedundancyDefault;
	private UnsignedInteger16 DeltaReservationMax;
	private UnsignedInteger16 DeltaReservationMin;
	private UnsignedInteger16 DeltaReservationDefault;
	private UnsignedInteger16 ExtentStripeLengthDefault;
	private UnsignedInteger16 ParityLayoutDefault;
	private UnsignedInteger64 UserDataStripeDepthDefault;
	
	public UnsignedInteger32 CreateSetting(UnsignedInteger16 SettingType, CIM_StorageSetting NewSetting){
		return null;
	}
	
	public UnsignedInteger32 GetSupportedParityLayouts(UnsignedInteger16[] ParityLayout){
		return null;
	}
}
