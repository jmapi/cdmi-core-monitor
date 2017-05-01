package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_LogicalDisk extends CIM_StorageExtent {
	private UnsignedInteger16 NameFormat = new UnsignedInteger16(12);
	private UnsignedInteger16 NameNamespace = new UnsignedInteger16(8);
}
