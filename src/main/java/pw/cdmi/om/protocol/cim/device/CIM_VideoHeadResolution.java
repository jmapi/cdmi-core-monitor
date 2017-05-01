package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_VideoHeadResolution extends CIM_SettingData {
	private UnsignedInteger32 HorizontalResolution;
	private UnsignedInteger32 VerticalResolution;
	private UnsignedInteger32 RefreshRate;
	private UnsignedInteger32 MinRefreshRate;
	private UnsignedInteger32 MaxRefreshRate;
	private UnsignedInteger16 ScanMode;
	private String OtherScanMode;
	private UnsignedInteger64 NumberOfColors;
}
