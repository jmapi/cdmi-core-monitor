package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;

public class CIM_StorageReplicationCapabilities extends CIM_Capabilities {
	private UnsignedInteger16 SupportedSynchronizationType;
	private UnsignedInteger16[] SupportedAsynchronousActions;
	private UnsignedInteger16[] SupportedSynchronousActions;
	private UnsignedInteger16 InitialReplicationState;
	private UnsignedInteger16[] SupportedSpecializedElements;
	private UnsignedInteger16[] SupportedModifyOperations;
	private UnsignedInteger16 ReplicaHostAccessibility;
	private UnsignedInteger16[] HostAccessibleState;
	private boolean SpaceLimitSupported;
	private boolean SpaceReservationSupported;
	private boolean LocalMirrorSnapshotSupported;
	private boolean RemoteMirrorSnapshotSupported;
	private boolean IncrementalDeltasSupported;
	private boolean PersistentReplicasSupported;
	private boolean BidirectionalConnectionsSupported;
	private UnsignedInteger16 MaximumReplicasPerSource;
	private UnsignedInteger16 MaximumPortsPerConnection;
	private UnsignedInteger16 MaximumConnectionsPerPort;
	private UnsignedInteger16 MaximumPeerConnections;
	private UnsignedInteger16 MaximumLocalReplicationDepth = new UnsignedInteger16(1);
	private UnsignedInteger16 MaximumRemoteReplicationDepth = new UnsignedInteger16(1);
	private UnsignedInteger16 InitialSynchronizationDefault;
	private UnsignedInteger16 ReplicationPriorityDefault;
	private UnsignedInteger8 LowSpaceWarningThresholdDefault;
	private UnsignedInteger8 SpaceLimitWarningThresholdDefault;
	private UnsignedInteger16 RemoteReplicationServicePointAccess;
	private UnsignedInteger16 AlternateReplicationServicePointAccess;
	private UnsignedInteger16 DeltaReplicaPoolAccess;
	private UnsignedInteger16 RemoteBufferElementType;
	private UnsignedInteger16 RemoteBufferHost;
	private UnsignedInteger16 RemoteBufferLocation;
	private UnsignedInteger16 RemoteBufferSupported;
	private UnsignedInteger16 UseReplicationBufferDefault;
	private String PeerConnectionProtocol;
	
}
