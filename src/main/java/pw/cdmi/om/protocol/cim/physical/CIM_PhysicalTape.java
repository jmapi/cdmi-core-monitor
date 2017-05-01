package pw.cdmi.om.protocol.cim.physical;

import javax.cim.SingleValue;


public class CIM_PhysicalTape extends CIM_PhysicalMedia {
	private SingleValue TapeLength;
	private boolean UnloadAnywhere;
	
	public SingleValue getTapeLength() {
		return TapeLength;
	}
	public boolean isUnloadAnywhere() {
		return UnloadAnywhere;
	}
}
