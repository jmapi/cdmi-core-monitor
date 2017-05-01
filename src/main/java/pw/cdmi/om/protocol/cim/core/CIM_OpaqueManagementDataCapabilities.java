package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_OpaqueManagementDataCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private UnsignedInteger64 MaxAvailableStorage;
	private UnsignedInteger64 MaxReadLength;
	private UnsignedInteger64 MaxWriteLength;
	private UnsignedInteger16[] SupportedExportURISchemes;
	private UnsignedInteger16[] SupportedImportURISchemes;
	private UnsignedInteger16[] SupportedMethods;
	private boolean TruncationSupported = false;
	private UnsignedInteger16 MaxInstancesSupported = new UnsignedInteger16(0);
}
