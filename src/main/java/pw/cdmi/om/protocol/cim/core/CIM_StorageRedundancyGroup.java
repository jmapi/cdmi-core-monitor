package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_StorageRedundancyGroup extends CIM_RedundancyGroup {
	private UnsignedInteger16 TypeOfAlgorithm;
	private UnsignedInteger16 StorageRedundancy;
	private boolean IsStriped;
	private boolean IsConcatenated;
}
