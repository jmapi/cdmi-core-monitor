package pw.cdmi.om.protocol.cim.physical;

import javax.cim.SingleValue;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_PhysicalElement;

public class CIM_PhysicalPackage extends CIM_PhysicalElement {
	private UnsignedInteger16 RemovalConditions = new UnsignedInteger16(2);
	private boolean Removable;
	private boolean Replaceable;
	private boolean HotSwappable;
	private SingleValue Height;
	private SingleValue Depth;
	private SingleValue Width;
	private SingleValue Weight;
	private UnsignedInteger16 PackageType;
	private String OtherPackageType;
	private String[] VendorCompatibilityStrings;
	
	public UnsignedInteger32 IsCompatible(CIM_PhysicalElement ElementToCheck){
		return null;
	}

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

	public SingleValue getHeight() {
		return Height;
	}

	public SingleValue getDepth() {
		return Depth;
	}

	public SingleValue getWidth() {
		return Width;
	}

	public SingleValue getWeight() {
		return Weight;
	}

	public UnsignedInteger16 getPackageType() {
		return PackageType;
	}

	public String getOtherPackageType() {
		return OtherPackageType;
	}

	public String[] getVendorCompatibilityStrings() {
		return VendorCompatibilityStrings;
	}

}
