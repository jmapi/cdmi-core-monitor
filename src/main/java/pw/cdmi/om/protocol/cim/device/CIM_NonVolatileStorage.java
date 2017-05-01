package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger64;

public class CIM_NonVolatileStorage extends CIM_Memory {
	private boolean IsWriteable;
	private boolean ApplicationWriteable;
	private UnsignedInteger64 StartAddressForApplicationWrite;
	private UnsignedInteger64 ApplicationWriteableSize;
}
