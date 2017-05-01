package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_BasedOn;
import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_LogicalDiskBasedOnExtent extends CIM_BasedOn {
	private CIM_StorageExtent Antecedent;
	private CIM_LogicalDisk Dependent;
}
