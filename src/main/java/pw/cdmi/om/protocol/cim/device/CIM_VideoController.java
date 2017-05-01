package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_VideoController extends CIM_Controller {
	private String Description;
	private String VideoProcessor;
	private UnsignedInteger16 VideoMemoryType;
	private UnsignedInteger32 NumberOfVideoPages;
	private UnsignedInteger32 MaxMemorySupported;
	private UnsignedInteger16[] AcceleratorCapabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger32 CurrentBitsPerPixel;
	private UnsignedInteger32 CurrentHorizontalResolution;
	private UnsignedInteger32 CurrentVerticalResolution;
	private UnsignedInteger32 MaxRefreshRate;
	private UnsignedInteger32 MinRefreshRate;
	private UnsignedInteger32 CurrentRefreshRate;
	private UnsignedInteger16 CurrentScanMode;
	private UnsignedInteger32 CurrentNumberOfRows;
	private UnsignedInteger32 CurrentNumberOfColumns;
	private UnsignedInteger64 CurrentNumberOfColors;
}
