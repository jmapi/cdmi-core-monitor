package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger16;

public class CIM_SoftwareIdentity extends CIM_LogicalElement {
	private String InstanceID;
	private UnsignedInteger16 MajorVersion;
	private UnsignedInteger16 MinorVersion;
	private UnsignedInteger16 RevisionNumber;
	private UnsignedInteger16 BuildNumber;
	private UnsignedInteger16 LargeBuildNumber;
	private boolean IsLargeBuildNumber = false;
	private String VersionString;
	private String[] TargetOperatingSystems;
	private String Manufacturer;
	private String[] Languages;
	private UnsignedInteger16[] Classifications;
	private String[] ClassificationDescriptions;
	private String SerialNumber;
	private String[] TargetTypes;
	private String[] IdentityInfoValue;
	private String[] IdentityInfoType;
	private Date ReleaseDate;
	private boolean IsEntity = false;
	private UnsignedInteger16 ExtendedResourceType;
	private String OtherExtendedResourceTypeDescription;
	private UnsignedInteger16 MinExtendedResourceTypeMajorVersion;
	private UnsignedInteger16 MinExtendedResourceTypeMinorVersion;
	private UnsignedInteger16 MinExtendedResourceTypeRevisionNumber;
	private UnsignedInteger16 MinExtendedResourceTypeBuildNumber;
	private UnsignedInteger16[] TargetOSTypes;
}
