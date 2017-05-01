package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_RedundancySet extends CIM_SystemSpecificCollection {
	private UnsignedInteger16 RedundancyStatus;
	private UnsignedInteger16[] TypeOfSet;
	private UnsignedInteger32 MinNumberNeeded;
	private UnsignedInteger32 MaxNumberSupported;
	private String VendorIdentifyingInfo;
	private String[] OtherTypeOfSet;
	private UnsignedInteger16 LoadBalanceAlgorithm = new UnsignedInteger16(2);
	private String OtherLoadBalanceAlgorithm;
	
	public UnsignedInteger32 Failover(CIM_ManagedElement FailoverFrom, CIM_ManagedElement FailoverTo){
		return null;
	}
}
