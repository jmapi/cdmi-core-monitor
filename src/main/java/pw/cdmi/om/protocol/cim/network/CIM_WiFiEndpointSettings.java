package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_WiFiEndpointSettings extends CIM_SettingData {
	private UnsignedInteger8 Priority;
	private String SSID;
	private UnsignedInteger16 BSSType;
	private UnsignedInteger16 EncryptionMethod;
	private String OtherEncryptionMethod;
	private UnsignedInteger16 AuthenticationMethod;
	private String OtherAuthenticationMethod;
	private String[] Keys;
	private UnsignedInteger8 KeyIndex;
	private UnsignedInteger8[] PSKValue;
	private String PSKPassPhrase;
}
