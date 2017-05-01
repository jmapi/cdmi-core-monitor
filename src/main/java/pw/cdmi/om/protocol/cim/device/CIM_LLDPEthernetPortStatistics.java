package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;

public class CIM_LLDPEthernetPortStatistics extends CIM_EthernetPortStatistics {
	private String[] TxDestMACAddress;
	private UnsignedInteger32[] TxPortFramesTotal;
	private UnsignedInteger32[] TxLLDPDULengthErrors;
	private String[] RxDestMACAddress;
	private UnsignedInteger32[] RxPortFramesDiscardedTotal;
	private UnsignedInteger32[] RxPortFramesErrors;
	private UnsignedInteger32[] RxPortTLVsDiscardedTotal;
	private UnsignedInteger32[] RxPortTLVsUnrecognizedTotal;
	private UnsignedInteger32[] RxPortAgeoutsTotal;
}
