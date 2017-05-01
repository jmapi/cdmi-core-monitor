package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_IEEE8021xSettings extends CIM_SettingData {
	private UnsignedInteger16 AuthenticationProtocol;
	private String RoamingIdentity;
	private String ServerCertificateName;
	private UnsignedInteger16 ServerCertificateNameComparison;
	private String Username;
	private String Password;
	private String Domain;
	private UnsignedInteger8[] ProtectedAccessCredential;
	private String PACPassword;
	private UnsignedInteger8[] PSK; 
}
