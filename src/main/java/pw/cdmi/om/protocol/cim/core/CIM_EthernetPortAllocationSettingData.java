package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

public class CIM_EthernetPortAllocationSettingData extends CIM_ResourceAllocationSettingData {
	private UnsignedInteger16 DesiredVLANEndpointMode;
	private String OtherEndpointMode;
	private UnsignedInteger16[] AllowedPriorities;
	private String[] AllowedToReceiveMACAddresses;
	private UnsignedInteger16[] AllowedToReceiveVLANs;
	private String[] AllowedToTransmitMACAddresses;
	private UnsignedInteger16[] AllowedToTransmitVLANs;
	private UnsignedInteger16 DefaultPortVID;
	private UnsignedInteger16 DefaultPriority;
	private UnsignedInteger32 GroupID;
	private UnsignedInteger32 ManagerID;
	private String NetworkPortProfileID;
	private UnsignedInteger16 NetworkPortProfileIDType;
	private String OtherNetworkPortProfileIDTypeInfo;
	private String PortCorrelationID;
	private UnsignedInteger16 PortVID;
	private boolean Promiscuous;
	private UnsignedInteger64 ReceiveBandwidthLimit;
	private UnsignedInteger64 ReceiveBandwidthReservation;
	private boolean SourceMACFilteringEnabled;
	private UnsignedInteger32 VSITypeID;
	private UnsignedInteger8 VSITypeIDVersion;
	
}
