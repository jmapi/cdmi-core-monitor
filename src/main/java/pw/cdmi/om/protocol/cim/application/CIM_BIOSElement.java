package pw.cdmi.om.protocol.cim.application;

import java.util.Date;

import javax.cim.UnsignedInteger64;

public class CIM_BIOSElement extends CIM_SoftwareElement {
	private String version;
	private String Manufacturer;
	private boolean PrimaryBIOS;
	private String[] ListOfLanguages;
	private String CurrentLanguage;
	private UnsignedInteger64 LoadedStartingAddress;
	private UnsignedInteger64 LoadedEndingAddress;
	private String LoadUtilityInformation;
	private Date ReleaseDate;
	private String[] RegistryURIs;
}
