package pw.cdmi.om.protocol.cim.physical;

import javax.cim.SingleValue;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;


public class CIM_StorageMediaLocation extends CIM_PackageLocation {
	private UnsignedInteger16 LocationType;
	private UnsignedInteger16[] MediaTypesSupported;
	private SingleValue[] MediaSizesSupported;
	private UnsignedInteger32 MediaCapacity;
	private String[] TypeDescriptions;
	
	public UnsignedInteger16 getLocationType() {
		return LocationType;
	}
	public UnsignedInteger16[] getMediaTypesSupported() {
		return MediaTypesSupported;
	}
	public SingleValue[] getMediaSizesSupported() {
		return MediaSizesSupported;
	}
	public UnsignedInteger32 getMediaCapacity() {
		return MediaCapacity;
	}
	public String[] getTypeDescriptions() {
		return TypeDescriptions;
	}
}
