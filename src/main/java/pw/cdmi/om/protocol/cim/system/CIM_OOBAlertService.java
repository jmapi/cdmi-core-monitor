package pw.cdmi.om.protocol.cim.system;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Service;

public class CIM_OOBAlertService extends CIM_Service {
	private UnsignedInteger16 DestinationType;
	private String OtherDestinationTypeDescription;
	private String DestinationAddress;
	private UnsignedInteger16 MessageFormat;
	private String OtherMessageFormatDescription;
	private boolean OnlySendsFixedMessage;
	private String FixedPartOfMessage;
	private boolean DestinationIsAckCapable;
	private UnsignedInteger16 RetryCount;
	private UnsignedInteger16 RetryInterval;
	private boolean PresenceHeartbeatCapable;
	private boolean EnablePresenceHeartbeats;
}
