package pw.cdmi.om.protocol.cim.network;

import javax.cim.UnsignedInteger16;

public class CIM_WiFiEndpoint extends CIM_LANEndpoint {
	private String LANID;
	private UnsignedInteger16 ProtocolIFType = new UnsignedInteger16(71);
	private UnsignedInteger16 EncryptionMethod;
	private String OtherEncryptionMethod;
	private UnsignedInteger16 AuthenticationMethod;
	private String OtherAuthenticationMethod;
	private UnsignedInteger16 IEEE8021xAuthenticationProtocol;
	private String AccessPointAddress;
	private UnsignedInteger16 BSSType;
	private boolean Associated;
}
