package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_VirtualSystemSnapshotServiceCapabilities extends CIM_Capabilities {
	private UnsignedInteger16[] SynchronousMethodsSupported;
	private UnsignedInteger16[] AynchronousMethodsSupported;
	private UnsignedInteger16[] SnapshotTypesSupported;
}
