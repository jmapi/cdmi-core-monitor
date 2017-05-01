package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger32;

public class CIM_Product extends CIM_ManagedElement {
	private String name;
	private String IdentifyingNumber;
	private String Vendor;
	private String Version;
	private String SKUNumber;
	private Date WarrantyStartDate;
	private UnsignedInteger32 WarrantyDuration;
}
