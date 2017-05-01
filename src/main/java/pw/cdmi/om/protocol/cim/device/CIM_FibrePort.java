package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;


public class CIM_FibrePort extends CIM_LogicalPort {
	private UnsignedInteger32 AddressIdentifier;
	private UnsignedInteger16[] SupportedPortTypes;
	private String[] PortTypeVersions;
	private UnsignedInteger16[] EnabledPortTypes;
	private String[] EnabledVersions;
	private UnsignedInteger16 CurrentPortType;
	private String CurrentVersion;
	private UnsignedInteger32[] AliasAddresses;
	private UnsignedInteger64 LossOfSignalCounter;
	private UnsignedInteger64 LossOfSyncCounter;
	private UnsignedInteger64 CRCErrors;
	private UnsignedInteger64 InvalidTransmissionWords;
	private UnsignedInteger64 FramesTooShort;
	private UnsignedInteger64 FramesTooLong;
	private UnsignedInteger64 ElasticityBufferUnderruns;
	private UnsignedInteger64 ElasticityBufferOverruns;
	private UnsignedInteger64 ReceiverTransmitterTimeout;
	private UnsignedInteger16 BypassedState;
	private UnsignedInteger16 ConnectedMedia;
}
