package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_FlatPanel extends CIM_Display {
	private UnsignedInteger32 HorizontalResolution;
	private UnsignedInteger32 VerticalResolution;
	private UnsignedInteger16 ScanMode;
	private UnsignedInteger16 DisplayType;
	private UnsignedInteger16 LightSource;
	private boolean SupportsColor;
}
