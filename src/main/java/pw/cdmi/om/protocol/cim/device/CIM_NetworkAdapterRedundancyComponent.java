package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_ExtraCapacityGroup;
import pw.cdmi.om.protocol.cim.core.CIM_RedundancyComponent;

public class CIM_NetworkAdapterRedundancyComponent extends CIM_RedundancyComponent {
	private CIM_ExtraCapacityGroup GroupComponent;
	private CIM_NetworkAdapter PartComponent;
	private UnsignedInteger16 ScopeOfBalancing;
	private UnsignedInteger16 PrimaryAdapter;
}
