package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_ChangerDevice extends CIM_MediaTransferDevice {
	private UnsignedInteger32 MaxTransitTime;
	private boolean MediaFlipSupported;
	private boolean AuditInProgress;
	private UnsignedInteger64 AuditsPerformed;
}
