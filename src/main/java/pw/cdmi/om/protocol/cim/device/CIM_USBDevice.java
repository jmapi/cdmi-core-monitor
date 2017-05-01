package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_USBDevice extends CIM_LogicalDevice {
	private UnsignedInteger16 USBVersion;
	private UnsignedInteger8 ClassCode;
	private UnsignedInteger8 SubclassCode;
	private UnsignedInteger8 ProtocolCode;
	private UnsignedInteger16 USBVersionInBCD;
	private UnsignedInteger8 MaxPacketSize;
	private UnsignedInteger16 VendorID;
	private UnsignedInteger16 ProductID;
	private UnsignedInteger16 DeviceReleaseNumber;
	private String Manufacturer;
	private String Product;
	private String SerialNumber;
	private UnsignedInteger8 NumberOfConfigs;
	private UnsignedInteger8 CurrentConfigValue;
	private UnsignedInteger8[] CurrentAlternateSettings;
	private Date CommandTimeout;
	
	public UnsignedInteger32 GetDescriptor(UnsignedInteger8 RequestType, UnsignedInteger16 RequestValue, UnsignedInteger16 RequestIndex, UnsignedInteger16 RequestLength, UnsignedInteger8[] buffer){
		return null;
	}
}
