package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;
import javax.cim.UnsignedInteger8;

public class CIM_StorageExtent extends CIM_LogicalDevice {
	private UnsignedInteger16 DataOrganization;
	private String Purpose;
	private UnsignedInteger16 Access;
	private String ErrorMethodology;
	private UnsignedInteger64 BlockSize;
	private UnsignedInteger64 NumberOfBlocks;
	private UnsignedInteger64 ConsumableBlocks;
	private boolean IsBasedOnUnderlyingRedundancy;
	private boolean SequentialAccess;
	private UnsignedInteger16[] ExtentStatus;
	private boolean NoSinglePointOfFailure;
	private UnsignedInteger16 DataRedundancy;
	private UnsignedInteger16 PackageRedundancy;
	private UnsignedInteger8 DeltaReservation;
	private boolean Primordial = false;
	private String name;
	private UnsignedInteger16 NameFormat;
	private UnsignedInteger16 NameNamespace;
	private String OtherNameNamespace;
	private String OtherNameFormat;
}
