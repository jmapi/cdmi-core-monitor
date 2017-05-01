package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.network.CIM_IEEE8021xSettings;
import pw.cdmi.om.protocol.cim.network.CIM_NetworkPortConfigurationService;
import pw.cdmi.om.protocol.cim.network.CIM_WiFiEndpoint;
import pw.cdmi.om.protocol.cim.network.CIM_WiFiEndpointSettings;
import pw.cdmi.om.protocol.cim.user.CIM_Credential;

public class CIM_WiFiPortConfigurationService extends CIM_NetworkPortConfigurationService {
	public UnsignedInteger32 AddWiFiSettings(CIM_WiFiEndpoint WiFiEndpoint, String WiFiEndpointSettingsInput,String IEEE8021xSettingsInput,CIM_Credential ClientCredential,CIM_Credential CACredential, CIM_WiFiEndpointSettings WiFiEndpointSettings, CIM_IEEE8021xSettings IEEE8021xSettings){
		return null;
	}
	
	public UnsignedInteger32 UpdateWiFiSettings(CIM_WiFiEndpointSettings WiFiEndpointSettings, String WiFiEndpointSettingsInput,String IEEE8021xSettingsInput,CIM_Credential ClientCredential,CIM_Credential CACredential, CIM_IEEE8021xSettings IEEE8021xSettings){
		return null;
	}
}
