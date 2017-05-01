package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_DeviceServiceImplementation;
import pw.cdmi.om.protocol.cim.system.CIM_WakeUpService;

public class CIM_WakeUpServiceOnNetworkPort extends CIM_DeviceServiceImplementation {
	private CIM_NetworkPort Antecedent;
	private CIM_WakeUpService Dependent;
}
