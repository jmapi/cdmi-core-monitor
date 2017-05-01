package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_AllocationCapabilities extends CIM_Capabilities{

	private UnsignedInteger16 ResourceType; //ValueMap
	private String OtherResourceType;
	private String ResourceSubType;
	private UnsignedInteger16 RequestTypesSupported;//ValueMap
	private UnsignedInteger16 SharingMode;//valueMap
	private UnsignedInteger16 SupportedAddStates[];//valueMap
	private UnsignedInteger16 SupportedRemoveStates[];//ValueMap
	
}
