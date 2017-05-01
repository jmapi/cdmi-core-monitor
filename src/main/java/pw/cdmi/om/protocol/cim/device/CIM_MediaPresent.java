package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_MediaPresent extends CIM_Dependency {
	private CIM_MediaAccessDevice Antecedent;
	private CIM_StorageExtent Dependent;
	private boolean FixedMedia;
}
