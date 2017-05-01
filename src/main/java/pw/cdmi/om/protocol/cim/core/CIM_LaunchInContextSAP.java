package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_LaunchInContextSAP extends CIM_RemoteServiceAccessPoint {
	private UnsignedInteger16 AccessContext;
	private String AccessInfo;
	private UnsignedInteger16 InfoFormat;
	private String[] ParameterName;
	private String[] ParameterDescription;
	private String[] ParameterDerivation;
	private String[] ParameterConstraints;
	private UnsignedInteger16[] ParameterType;
	private String[] SupportedFeatureName;
	private String[] SupportedFeatureDescription;
	private String[] ManagedClasses;
	private boolean ManagementIsRestricted = false;
	private String LaunchMessage;
	private UnsignedInteger16 LaunchMessageProtocolOperation = new UnsignedInteger16(3);
	
	public UnsignedInteger32 GetDerivedParametersForElement(CIM_ManagedElement Self, String[] ParameterValue){
		return null;
	}
}
