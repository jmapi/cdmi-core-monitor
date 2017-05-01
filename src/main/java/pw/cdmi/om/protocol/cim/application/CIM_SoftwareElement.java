package pw.cdmi.om.protocol.cim.application;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalElement;

public class CIM_SoftwareElement extends CIM_LogicalElement {
	private String name;
	private String version;
	private UnsignedInteger16 SoftwareElementState;
	private String SoftwareElementID; //Maxlen(255)
	private UnsignedInteger16 TargetOperatingSystem;
	private String OtherTargetOS;
	private String Manufacturer;
	private String BuildNumber;
	private String SerialNumber;
	private String CodeSet;
	private String IdentificationCode;
	private String LanguageEdition;
}
