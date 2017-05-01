package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_PrintSupply extends CIM_PrinterElement {
	private String LocalizedDescription;
	private UnsignedInteger32 Classification;
	private String OtherClassification;
	private UnsignedInteger32 Type;
	private String OtherTypeDescription;
	private UnsignedInteger32 SupplyUnit;
	private String OtherSupplyUnit;
	private UnsignedInteger16 MaxCapacityBasis;
	private UnsignedInteger32 MaxCapacity;
	private UnsignedInteger16 RemainingCapacityBasis;
	private UnsignedInteger32 RemainingCapacity;
	private UnsignedInteger32 ColorantRole;
	private String OtherColorantRole;
	private UnsignedInteger16 ColorantName;
	private String OtherColorantName;
	private UnsignedInteger32 ColorantTonality;
}
