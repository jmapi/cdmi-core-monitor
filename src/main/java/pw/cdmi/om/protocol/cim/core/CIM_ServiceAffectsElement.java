package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_ServiceAffectsElement {
	private CIM_ManagedElement AffectedElement;
	private CIM_Service AffectingElement;
	private UnsignedInteger16[] ElementEffects;
	private String[] OtherElementEffectsDescriptions;
}
