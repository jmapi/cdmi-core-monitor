package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_EnabledLogicalElementCapabilities;

public class CIM_FCSwitchCapabilities extends CIM_EnabledLogicalElementCapabilities {
	private boolean DomainIDConfigureable;
	private UnsignedInteger8 MinDomainID;
	private UnsignedInteger8 MaxDomainID;
	private boolean DomainIDLockedSupported;
	private UnsignedInteger16[] PrincipalPrioritiesSupported;
}
