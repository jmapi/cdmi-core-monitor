package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_HostedDependency;
import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_SharingDependency extends CIM_HostedDependency {
	private CIM_LogicalDevice Antecedent;
	private CIM_LogicalDevice Dependent;
	private UnsignedInteger16 CurrentAccess;
	private String OtherCurrentAccess;
}
