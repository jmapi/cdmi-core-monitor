package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_Service;
import pw.cdmi.om.protocol.cim.device.CIM_NetworkPort;

public class CIM_NetworkPortConfigurationService extends CIM_Service {
	public UnsignedInteger32 AddLANEndpoint(CIM_NetworkPort port,CIM_LANEndpoint  endpoint,String address,String lANID,String[] aliasAddresses,String[] groupAddresses){
		return null;
	}
}
