package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_SoftwareInstallationServiceCapabilities extends CIM_Capabilities {
	private UnsignedInteger16[] SupportedAsynchronousActions;
	private UnsignedInteger16[] SupportedSynchronousActions;
	private String[] SupportedTargetTypes;
	private UnsignedInteger16[] SupportedExtendedResourceTypes;
	private boolean CanAddToCollection;
	private UnsignedInteger16[] SupportedInstallOptions;
	private String[] OtherSupportedExtendedResourceTypeDescriptions;
	private UnsignedInteger16[] SupportedExtendedResourceTypesMajorVersions;
	private UnsignedInteger16[] SupportedExtendedResourceTypesMinorVersions;
	private UnsignedInteger16[] SupportedExtendedResourceTypesRevisionNumbers;
	private UnsignedInteger16[] SupportedExtendedResourceTypesBuildNumbers;
	private UnsignedInteger16[] SupportedURISchemes;
}
