package pw.cdmi.om.protocol.cim.physical;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_PhysicalElement;

public class CIM_PhysicalComponent extends CIM_PhysicalElement {
	private UnsignedInteger16 RemovalConditions = new UnsignedInteger16(2);
	private boolean Removable;
	private boolean Replaceable;
	private boolean HotSwappable;
	
	public UnsignedInteger16 getRemovalConditions() {
		return RemovalConditions;
	}
	public boolean isRemovable() {
		return Removable;
	}
	public boolean isReplaceable() {
		return Replaceable;
	}
	public boolean isHotSwappable() {
		return HotSwappable;
	}
	
	
}
