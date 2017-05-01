package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

public class CIM_LimitedAccessPort extends CIM_MediaTransferDevice {
	private boolean Locked;
	private boolean Extended;
	private UnsignedInteger32 ExtendTimeout;
	private Date LastExtended;
	private UnsignedInteger64 ImportCount;
	private UnsignedInteger64 ExportCount;
	private UnsignedInteger16 Direction;
}
