package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_BasedOn;

public class CIM_VolumeSetBasedOnPSExtent extends CIM_BasedOn {
	private CIM_ProtectedSpaceExtent Antecedent;
	private CIM_VolumeSet Dependent;
	private boolean LBAsMappedByDecrementing;
	private boolean LBAMappingIncludesCheckData;
}
