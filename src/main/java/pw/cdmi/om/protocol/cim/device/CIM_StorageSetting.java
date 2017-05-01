package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_StorageSetting extends CIM_SettingData {
	private boolean NoSinglePointOfFailure;
	private UnsignedInteger16 DataRedundancyMax;
	private UnsignedInteger16 DataRedundancyMin;
	private UnsignedInteger16 DataRedundancyGoal;
	private UnsignedInteger16 PackageRedundancyMax;
	private UnsignedInteger16 PackageRedundancyMin;
	private UnsignedInteger16 PackageRedundancyGoal;
	private UnsignedInteger8 DeltaReservationMax;
	private UnsignedInteger8 DeltaReservationMin;
	private UnsignedInteger8 DeltaReservationGoal;
	private UnsignedInteger16 ChangeableType;
	private UnsignedInteger16 ExtentStripeLength;
	private UnsignedInteger16 ExtentStripeLengthMin;
	private UnsignedInteger16 ExtentStripeLengthMax;
	private UnsignedInteger16 ParityLayout;
	private UnsignedInteger64 UserDataStripeDepth;
	private UnsignedInteger64 UserDataStripeDepthMin;
	private UnsignedInteger64 UserDataStripeDepthMax;
}
