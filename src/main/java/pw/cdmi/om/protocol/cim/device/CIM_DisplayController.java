package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_DisplayController extends CIM_Controller {
	private String Description;
	private String VideoProcessor;
	private UnsignedInteger16 VideoMemoryType;
	private String OtherVideoMemoryType;
	private UnsignedInteger32 NumberOfVideoPages;
	private UnsignedInteger32 MaxMemorySupported;
	private UnsignedInteger16[] AcceleratorCapabilities;
	private String[] CapabilityDescriptions;
	private String OtherVideoArchitecture;
	private UnsignedInteger16 VideoArchitecture;
}
