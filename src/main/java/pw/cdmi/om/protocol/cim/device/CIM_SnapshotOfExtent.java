package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;
import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_SnapshotOfExtent extends CIM_Dependency {
	private CIM_StorageExtent Antecedent;
	private CIM_Snapshot Dependent;
	private UnsignedInteger16 SnapshotType;
}
