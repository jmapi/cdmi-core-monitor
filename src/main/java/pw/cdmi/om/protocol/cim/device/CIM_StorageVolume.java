package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_StorageVolume extends CIM_StorageExtent {
	private String name;
	private UnsignedInteger16 NameFormat;
	private UnsignedInteger16 NameNamespace;
}
