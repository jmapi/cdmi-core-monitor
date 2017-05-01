package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.system.CIM_VirtualSystemSettingData;

public class CIM_VirtualEthernetSwitchSettingData extends CIM_VirtualSystemSettingData {
	private String[] VLANConnection;
	private String[] AssociatedResourcePool;
	private UnsignedInteger32 MaxNumMACAddress;
	private UnsignedInteger16 EVBMode;
	private String OtherEVBMode;
}
