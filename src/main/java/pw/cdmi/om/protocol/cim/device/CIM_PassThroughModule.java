package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_PassThroughModule extends CIM_LogicalModule {
	private UnsignedInteger16[] LinkTechnologies;
	private String[] OtherLinkTechnologies;
	private boolean IsProgrammable = false;
	private UnsignedInteger16 NumberOfPorts;
	private UnsignedInteger16[] InternalPorts;
	private UnsignedInteger16[] ExternalPorts;
	
	public UnsignedInteger32 AssignPorts(boolean Mapped, UnsignedInteger16 InternalPort, UnsignedInteger16 ExternalPort){
		return null;
	}
}
