package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_SoftwareInstallationService extends CIM_Service {

	public UnsignedInteger32 CheckSoftwareIdentity(CIM_SoftwareIdentity Source, CIM_ManagedElement Target, CIM_Collection Collection, UnsignedInteger16[] InstallCharacteristics){
		return null;
	}
	
	public UnsignedInteger32 InstallFromSoftwareIdentity(CIM_ConcreteJob job, UnsignedInteger16[] InstallOptions,String[] InstallOptionsValues, CIM_SoftwareIdentity Source, CIM_ManagedElement Target, CIM_Collection Collection){
		return null;
	}
	
	public UnsignedInteger32 InstallFromURI(CIM_ConcreteJob job, String uRL, CIM_ManagedElement Target, UnsignedInteger16[] InstallOptions,String[] InstallOptionsValues){
		return null;
	}
}
