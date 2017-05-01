package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

public class CIM_OpaqueManagementData extends CIM_StorageExtent {
	private UnsignedInteger64 MaxSize;
	private Date LastAccessed;
	private UnsignedInteger16 WriteLimited;
	private String DataFormat;
	private UnsignedInteger64 DataSize;
}
