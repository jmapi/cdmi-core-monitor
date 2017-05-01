package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger64;

public class CIM_ServiceAffectsElementWithQuota extends CIM_ServiceAffectsElement {
	private UnsignedInteger64 Quota;
	private UnsignedInteger64 QuotaUsed;
	private String QuotaUnits = "byte";
}
