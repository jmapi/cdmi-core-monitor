package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

public class CIM_FCPortStatistics extends CIM_NetworkPortStatistics {
	private UnsignedInteger64 LIPCount;
	private UnsignedInteger64 NOSCount;
	private UnsignedInteger64 ErrorFrames;
	private UnsignedInteger64 DumpedFrames;
	private UnsignedInteger64 LinkFailures;
	private UnsignedInteger64 LossOfSignalCounter;
	private UnsignedInteger64 LossOfSyncCounter;
	private UnsignedInteger64 PrimitiveSeqProtocolErrCount;
	private UnsignedInteger64 CRCErrors;
	private UnsignedInteger64 InvalidTransmissionWords;
	private UnsignedInteger64 FramesTooShort;
	private UnsignedInteger64 FramesTooLong;
	private UnsignedInteger64 AddressErrors;
	private UnsignedInteger64 BufferCreditNotProvided;
	private UnsignedInteger64 BufferCreditNotReceived;
	private UnsignedInteger64 DelimiterErrors;
	private UnsignedInteger64 EncodingDisparityErrors;
	private UnsignedInteger64 LinkResetsReceived;
	private UnsignedInteger64 LinkResetsTransmitted;
	private UnsignedInteger64 MulticastFramesReceived;
	private UnsignedInteger64 MulticastFramesTransmitted;
	private UnsignedInteger64 FBSYFrames;
	private UnsignedInteger64 PBSYFrames;
	private UnsignedInteger64 FRJTFrames;
	private UnsignedInteger64 PRJTFrames;
	private UnsignedInteger64 RXClass1Frames;
	private UnsignedInteger64 TXClass1Frames;
	private UnsignedInteger64 Class1FBSY;
	private UnsignedInteger64 Class1PBSY;
	private UnsignedInteger64 Class1FRJT;
	private UnsignedInteger64 Class1PRJT;
	private UnsignedInteger64 RXClass2Frames;
	private UnsignedInteger64 TXClass2Frames;
	private UnsignedInteger64 Class2FBSY;
	private UnsignedInteger64 Class2PBSY;
	private UnsignedInteger64 Class2FRJT;
	private UnsignedInteger64 Class2PRJT;
	private UnsignedInteger64 RXClass3Frames;
	private UnsignedInteger64 TXClass3Frames;
	private UnsignedInteger64 Class3FramesDiscarded;
	private UnsignedInteger64 RXBroadcastFrames;
	private UnsignedInteger64 TXBroadcastFrames;
	
	
}
