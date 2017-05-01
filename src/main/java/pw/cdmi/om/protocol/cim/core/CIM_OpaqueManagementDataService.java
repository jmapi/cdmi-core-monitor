package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.user.CIM_Identity;

public class CIM_OpaqueManagementDataService extends CIM_Service {
	private UnsignedInteger64 AvailableStorage;
	private Date LockTimeout;
	private UnsignedInteger64 MaxCreateSize;
	
	public UnsignedInteger32 AssignAccess(CIM_Identity Identity,CIM_OpaqueManagementData OpaqueManagementData, UnsignedInteger16[] Activities){
		return null;
	}

	public UnsignedInteger32 Create(UnsignedInteger64 MaxSize, String DataFormat,String ElementName, CIM_Identity Owner,CIM_StorageExtent BasedOnExtent,CIM_OpaqueManagementData OpaqueManagementData ,CIM_ConcreteJob Job){
		return null;
	}
	
	public UnsignedInteger32 ExportToURI(CIM_OpaqueManagementData OpaqueManagementData, UnsignedInteger64 Offset, UnsignedInteger64 Length){
		return null;
	}
	
	public String ExportURI(UnsignedInteger8[] LockToken,CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 ImportFromURI(CIM_OpaqueManagementData OpaqueManagementData,UnsignedInteger64 Offset,UnsignedInteger64 Length,boolean Truncate,String ImportURI, UnsignedInteger8[]  LockToken,CIM_ConcreteJob job){
		return null;
	}
	
	public UnsignedInteger32 Lock(CIM_OpaqueManagementData OpaqueManagementData, boolean Lock, UnsignedInteger8[] LockToken){
		return null;
	}
	
	public UnsignedInteger32 Read(CIM_OpaqueManagementData OpaqueManagementData, UnsignedInteger64 Offset, UnsignedInteger64 Length,UnsignedInteger8[] Data, UnsignedInteger8[] LockToken){
		return null;
	}
	
	public UnsignedInteger32 ReassignOwnership(CIM_Identity NewOwner, CIM_OpaqueManagementData OpaqueManagementData){
		return null;
	}
	
	public UnsignedInteger32 Write(CIM_OpaqueManagementData OpaqueManagementData, UnsignedInteger64 Offset, UnsignedInteger64 Length, boolean Truncate, UnsignedInteger8[] LockToken, CIM_ConcreteJob Job){
		return null;
	}
}
