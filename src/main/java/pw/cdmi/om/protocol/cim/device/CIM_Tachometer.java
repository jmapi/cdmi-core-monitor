package pw.cdmi.om.protocol.cim.device;

import javax.cim.SignedInteger32;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_Tachometer extends CIM_NumericSensor {
	private UnsignedInteger16 SensorType = new UnsignedInteger16(5);
	private UnsignedInteger16 BaseUnits = new UnsignedInteger16(38);
	private SignedInteger32 UnitModifier = new SignedInteger32(1);
	private UnsignedInteger16 RateUnits = new UnsignedInteger16(4);
	private UnsignedInteger32 Resolution;
}
