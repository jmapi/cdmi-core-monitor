package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.physical.CIM_PhysicalConnector;

public class CIM_AdapterActiveConnection extends CIM_Dependency {
	CIM_PhysicalConnector Antecedent;
	CIM_NetworkAdapter Dependent;
}
