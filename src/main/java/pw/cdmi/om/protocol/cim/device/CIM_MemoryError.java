package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

public class CIM_MemoryError extends CIM_StorageError {
	private UnsignedInteger64 StartingAddress;
	private UnsignedInteger16 ErrorInfo;
	private String OtherErrorDescription;
	private boolean CorrectableError;
	private Date ErrorTime;
	private UnsignedInteger16 ErrorAccess;
	private UnsignedInteger32 ErrorTransferSize;
	private UnsignedInteger8[] ErrorData;
	private UnsignedInteger16 ErrorDataOrder;
	private boolean SystemLevelAddress;
	private UnsignedInteger64 ErrorResolution;
	private UnsignedInteger8[] AdditionalErrorData;
}
