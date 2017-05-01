package pw.cdmi.om.protocol.cim.core;

public class CIM_FRU extends CIM_ManagedElement {
	private String FRUNumber;
	private String IdentifyingNumber;
	private String Vendor;
	private String Description;
	private String Name;//MaxLen ( 256 )
	private String RevisionLevel;//MaxLen ( 64 )
	private boolean CustomerReplaceable; 
}
