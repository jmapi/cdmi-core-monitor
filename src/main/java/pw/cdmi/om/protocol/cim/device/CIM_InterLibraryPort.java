package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;


public class CIM_InterLibraryPort extends CIM_MediaTransferDevice {
	private Date LastAccessed;
	private UnsignedInteger64 ImportCount;
	private UnsignedInteger64 ExportCount;
	private UnsignedInteger16 Direction;
}
