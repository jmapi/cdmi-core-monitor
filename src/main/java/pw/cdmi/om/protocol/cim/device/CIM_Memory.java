package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_StorageExtent;

public class CIM_Memory extends CIM_StorageExtent {
	private boolean Volatile;
	private String ErrorMethodology;
	private UnsignedInteger64 StartingAddress;
	private UnsignedInteger64 EndingAddress;
	private UnsignedInteger16 ErrorInfo;
	private String OtherErrorDescription;
	private boolean CorrectableError;
	private Date ErrorTime;
	private UnsignedInteger16 ErrorAccess;
	private UnsignedInteger32 ErrorTransferSize;
	private UnsignedInteger8[] ErrorData;
	private UnsignedInteger16[] ErrorDataOrder;
	private UnsignedInteger64 ErrorAddress;
	private boolean SystemLevelAddress;
	private UnsignedInteger64 ErrorResolution;
	private UnsignedInteger8[] AdditionalErrorData;
}
