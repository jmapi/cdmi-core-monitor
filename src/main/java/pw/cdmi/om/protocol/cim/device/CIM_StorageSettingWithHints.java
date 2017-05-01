package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

public class CIM_StorageSettingWithHints extends CIM_StorageSetting {
	private UnsignedInteger16 DataAvailabilityHint;
	private UnsignedInteger16 AccessRandomnessHint;
	private UnsignedInteger16 AccessDirectionHint;
	private UnsignedInteger16[] AccessSizeHint;
	private UnsignedInteger16 AccessLatencyHint;
	private UnsignedInteger16 AccessBandwidthWeight;
	private UnsignedInteger16 StorageCostHint;
	private UnsignedInteger16 StorageEfficiencyHint;
}
