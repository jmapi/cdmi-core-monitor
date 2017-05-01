package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_ManagedElement;
import pw.cdmi.om.protocol.cim.core.CIM_Service;

public class CIM_ConfigurationReportingService extends CIM_Service {
	public UnsignedInteger32 GetClassTypes(UnsignedInteger16 InquiryType, boolean Recursive, CIM_ManagedElement Target, String[] ClassTypes){
		return null;
	}
	
	public UnsignedInteger32 GetUnitTypes(UnsignedInteger16 InquiryType, boolean Recursive, CIM_ManagedElement Target, String ClassType,UnsignedInteger16[] UnitTypes){
		return null;
	}
	
	public UnsignedInteger32 ReportCapacity(UnsignedInteger16 InquiryType, boolean Recursive, CIM_ManagedElement Target, String ClassType,UnsignedInteger16 UnitType, UnsignedInteger64 NumberOfUnits){
		return null;
	}
}
