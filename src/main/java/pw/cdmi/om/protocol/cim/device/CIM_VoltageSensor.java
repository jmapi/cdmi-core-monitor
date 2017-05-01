package pw.cdmi.om.protocol.cim.device;

import javax.cim.SignedInteger32;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_VoltageSensor extends CIM_NumericSensor {
	private UnsignedInteger16 SensorType = new UnsignedInteger16(3);
	private UnsignedInteger16 BaseUnits = new UnsignedInteger16(5);
	private SignedInteger32 UnitModifier = new SignedInteger32(-3);
	private UnsignedInteger16 RateUnits = new UnsignedInteger16(0);
	private SignedInteger32 CurrentReading;
	private SignedInteger32 NominalReading;
	private SignedInteger32 NormalMax;
	private SignedInteger32 NormalMin;
	private SignedInteger32 MaxReadable;
	private SignedInteger32 MinReadable;
	private UnsignedInteger32 Resolution;
	private SignedInteger32 Tolerance;
	private SignedInteger32 Accuracy;
	private SignedInteger32 LowerThresholdNonCritical;
	private SignedInteger32 UpperThresholdNonCritical;
	private SignedInteger32 LowerThresholdCritical;
	private SignedInteger32 UpperThresholdCritical;
	private SignedInteger32 LowerThresholdFatal;
	private SignedInteger32 UpperThresholdFatal;
}
