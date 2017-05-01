package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_ExtentRedundancyComponent;
import pw.cdmi.om.protocol.cim.core.CIM_StorageRedundancyGroup;

public class CIM_AggregateRedundancyComponent extends CIM_ExtentRedundancyComponent {
	CIM_StorageRedundancyGroup GroupComponent;
	CIM_AggregatePExtent PartComponent;
}
