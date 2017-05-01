package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;


public class CIM_PointingDevice extends CIM_UserDevice {
	private UnsignedInteger16 PointingType;
	private UnsignedInteger8 NumberOfButtons;
	private UnsignedInteger16 Handedness;
	private UnsignedInteger32 Resolution;
}
