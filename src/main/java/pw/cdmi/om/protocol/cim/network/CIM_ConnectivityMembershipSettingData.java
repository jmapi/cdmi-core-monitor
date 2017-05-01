package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_ConnectivityMembershipSettingData extends CIM_SettingData {
	private UnsignedInteger16 ConnectivityMemberType;
	private String OtherConnectivityMemberType;
	private String ConnectivityMemberID;
}
