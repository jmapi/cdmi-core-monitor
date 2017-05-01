package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.system.CIM_VirtualSystemSettingData;

public class CIM_VirtualSystemSnapshotService extends CIM_Service {

	public UnsignedInteger32 CreateSnapshot(CIM_ComputerSystem AffectedSystem, String SnapshotSettings, UnsignedInteger16 SnapshotType, CIM_VirtualSystemSettingData ResultingSnapshot,CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 DestroySnapshot(CIM_VirtualSystemSettingData AffectedSnapshot,CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 ApplySnapshot(CIM_VirtualSystemSettingData Snapshot,CIM_ConcreteJob job){
		return null;
	}
}
