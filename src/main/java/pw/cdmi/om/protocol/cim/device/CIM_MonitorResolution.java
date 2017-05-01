package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_Setting;

public class CIM_MonitorResolution extends CIM_Setting {
	private String SettingID;
	private UnsignedInteger32 HorizontalResolution;
	private UnsignedInteger32 VerticalResolution;
	private UnsignedInteger32 RefreshRate;
	private UnsignedInteger32 MinRefreshRate;
	private UnsignedInteger32 MaxRefreshRate;
	private UnsignedInteger16 ScanMode;
}
