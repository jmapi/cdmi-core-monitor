package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

public class CIM_Unimodem extends CIM_POTSModem {
	private UnsignedInteger16 CallSetupResultCode;
	private UnsignedInteger16 MultiMediaMode;
	private UnsignedInteger16 DTEDCEInterfaceMode;
	private UnsignedInteger16 TransmitCarrierNegotiation;
	private UnsignedInteger16 ReceiveCarrierNegotiation;
	private UnsignedInteger32 InitialTransmitCarrierDataRate;
	private UnsignedInteger32 InitialReceiveCarrierDataRate;
	private UnsignedInteger8 CarrierRetrainsRequested;
	private UnsignedInteger8 CarrierRetrainsGranted;
	private UnsignedInteger32 FinalTransmitCarrierDataRate;
	private UnsignedInteger32 FinalReceiveCarrierDataRate;
	private UnsignedInteger16 TerminationCause;
	private String UnimodemRevision;
	private UnsignedInteger32 EstimatedNoiseLevel;
	private UnsignedInteger32 NormalizedMeanSquaredError;
	private UnsignedInteger8 TemporaryCarrierLossEventCount;
	private UnsignedInteger8 CarrierRenegotiationEventCount;
	private UnsignedInteger16 ErrorControlFrameSize;
	private UnsignedInteger8 ErrorControlLinkTimeouts;
	private UnsignedInteger8 ErrorControlLinkNAKs;
	private UnsignedInteger16 TransmitFlowControl;
	private UnsignedInteger16 ReceiveFlowControl;
	private UnsignedInteger64 TransmitCharsSentFromDTE;
	private UnsignedInteger64 ReceiveCharsSentToDTE;
	private UnsignedInteger64 TransmitCharsLost;
	private UnsignedInteger64 ReceiveCharsLost;
	private UnsignedInteger64 TransmitIFrameCount;
	private UnsignedInteger64 ReceiveIFrameCount;
	private UnsignedInteger64 TransmitIFrameErrorCount;
	private UnsignedInteger64 ReceivedIFrameErrorCount;
	private UnsignedInteger8 CallWaitingEventCount;
}
