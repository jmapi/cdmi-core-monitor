package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_VideoHead extends CIM_LogicalDevice {
	private UnsignedInteger32 CurrentBitsPerPixel;
	private UnsignedInteger32 CurrentHorizontalResolution;
	private UnsignedInteger32 CurrentVerticalResolution;
	private UnsignedInteger32 MaxRefreshRate;
	private UnsignedInteger32 MinRefreshRate;
	private UnsignedInteger32 CurrentRefreshRate;
	private UnsignedInteger16 CurrentScanMode;
	private String OtherCurrentScanMode;
	private UnsignedInteger32 CurrentNumberOfRows;
	private UnsignedInteger32 CurrentNumberOfColumns;
	private UnsignedInteger64 CurrentNumberOfColors;
	
}
