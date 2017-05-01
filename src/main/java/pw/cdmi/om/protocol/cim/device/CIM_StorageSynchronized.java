package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;
import pw.cdmi.om.protocol.cim.core.CIM_Synchronized;

public class CIM_StorageSynchronized extends CIM_Synchronized {
	private CIM_ManagedElement SystemElement;
	private CIM_ManagedElement SyncedElement;
	private UnsignedInteger16 CopyType;
	private UnsignedInteger16 ReplicaType;
	private UnsignedInteger16 SyncState;
}
