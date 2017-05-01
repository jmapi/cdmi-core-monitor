package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.network.CIM_ConnectivityCollection;

public class CIM_Zone extends CIM_ConnectivityCollection {
	private String ElementName;
	private boolean Active;
	private UnsignedInteger16 ZoneType;
	private String OtherZoneTypeDescription;
	private UnsignedInteger16 ZoneSubType;
	private String OtherZoneSubTypeDescription;
}
