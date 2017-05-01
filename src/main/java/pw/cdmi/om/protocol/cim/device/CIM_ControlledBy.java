package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_ControlledBy extends CIM_DeviceConnection {
	private CIM_Controller Antecedent;
	private CIM_LogicalDevice Dependent;
	private UnsignedInteger16 AccessState;
	private Date TimeOfDeviceReset;
	private UnsignedInteger32 NumberOfHardResets;
	private UnsignedInteger32 NumberOfSoftResets;
	private String DeviceNumber;
	private UnsignedInteger16 AccessMode;
	private UnsignedInteger16 AccessPriority;
	
}
