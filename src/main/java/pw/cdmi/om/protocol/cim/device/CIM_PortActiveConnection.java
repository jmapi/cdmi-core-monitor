package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.physical.CIM_PhysicalConnector;

public class CIM_PortActiveConnection extends CIM_Dependency {
	private CIM_PhysicalConnector Antecedent;
	private CIM_NetworkPort Dependent;
}
