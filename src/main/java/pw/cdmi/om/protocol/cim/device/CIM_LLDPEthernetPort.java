package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;

public class CIM_LLDPEthernetPort extends CIM_EthernetPort {
	private boolean ConfigPortVlanTxEnable;
	private boolean ConfigManVidTxEnable;
	private UnsignedInteger16 MessageTxInterval = new UnsignedInteger16(30);
	private UnsignedInteger8 MessageTxHoldMultiplier = new UnsignedInteger8((short)4);
	private UnsignedInteger8  ReinitDelay = new UnsignedInteger8((short)2);
	private UnsignedInteger16 NotificationInterval = new UnsignedInteger16(30);
	private UnsignedInteger8 TxCreditMax = new UnsignedInteger8((short)5);
	private UnsignedInteger16 MessageFastTx = new UnsignedInteger16(1);
	private UnsignedInteger8 TxFastInit = new UnsignedInteger8((short)4);
	private String[] DestMacAddress;
	private UnsignedInteger8[] PortConfigAdminStatus;
	private boolean[] PortConfigNotificationEnable;
	private boolean[] PortConfigPortDescriptionTLVTxEnable;
	private boolean[] PortConfigSystemNameTLVTxEnable;
	private boolean[] PortConfigSystemDescriptionTLVTxEnable;
	private boolean[] PortConfigSystemCapabilitiesTLVTxEnable;
	private String[] ManAddrConfigDestAddress;
	private UnsignedInteger16[] ManAddrConfigLocManAddrSubtype;
	private String[] ManAddrConfigLocManAddr;
	private boolean[] ManAddrConfigTxEnable;
	private UnsignedInteger8 LocChassisIdSubtype;
	private String LocChassisId;
	private String LocSysName;
	private String LocSysDesc;
	private UnsignedInteger8[] LocSysCapSupported; 
	private boolean[] LocSysCapEnabled;
	private UnsignedInteger8 LocPortIdSubtype;
	private String LocPortID;
	private String LocPortDesc;
	private UnsignedInteger16 LocTxSystemValue;
	private UnsignedInteger16 RemTxSystemValueEcho;
	private UnsignedInteger16 LocRxSystemValue;
	private UnsignedInteger16 RemRxSystemValueEcho;
	private UnsignedInteger16 LocFbSystemValue;
	private UnsignedInteger16 RemTxSystemValue;
	private UnsignedInteger16 LocTxSystemValueEcho;
	private UnsignedInteger16 RemRxSystemValue;
	private UnsignedInteger16 LocRxSystemValueEcho;
	private UnsignedInteger16 LocResolvedTxSystemValue;
	private UnsignedInteger16 LocResolvedRxSystemValue;
	private boolean MaximumFrameSizeTLVEnabled;
	private UnsignedInteger16[] LocManAddrSubType;
	private String[] LocManAddr;
	private UnsignedInteger16[] LocManAddrIfSubtype;
	private UnsignedInteger32[] LocManAddrIfId;
	private String[] RemLocalDestMacAddress;
	private UnsignedInteger16[] RemManAddrSubType;
	private String[] RemManAddr;
	private UnsignedInteger16[] RemManAddrIfSubtype;
	private UnsignedInteger32[] RemManAddrIfId;
	private UnsignedInteger8[] RemChassisIdSubtype;
	private String[] RemChassisId;
	private String[] RemSysName;
	private String[] RemSysDesc;
	private UnsignedInteger8[] RemSysCapSupported;
	private boolean[] RemSysCapEnabled;
	private UnsignedInteger8[] RemPortIdSubtype;
	private String[] RemPortID;
	private String[] RemPortDesc;
	
	
	
	
}
