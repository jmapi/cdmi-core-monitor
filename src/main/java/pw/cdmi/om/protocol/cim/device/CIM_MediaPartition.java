package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_MediaPartition extends CIM_StorageExtent {
	private boolean Bootable;
	private boolean Allocatable;
	private String Signature;
	private String SignatureAlgorithm;
	private String SignatureState;
	private boolean Extendable;
}
