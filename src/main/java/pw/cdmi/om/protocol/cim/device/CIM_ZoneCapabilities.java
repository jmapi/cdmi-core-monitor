package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_Capabilities;

public class CIM_ZoneCapabilities extends CIM_Capabilities {
	private UnsignedInteger32 ZoneNameMaxLen;
	private UnsignedInteger16 ZoneNameFormat;
	private UnsignedInteger32 MaxNumZoneSets;
	private UnsignedInteger32 MaxNumZone;
	private UnsignedInteger32 MaxNumZoneMembers;
	private UnsignedInteger32 MaxNumZonesPerZoneSet;
	private UnsignedInteger32 MaxNumZoneAliases;
	private String[] OtherSupportedConnectivityMemberTypes;
	private UnsignedInteger16[] SupportedConnectivityMemberTypes;
}
