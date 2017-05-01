package pw.cdmi.om.protocol.cim.device;

import javax.cim.SignedInteger32;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_NumericSensor extends CIM_Sensor {
	private UnsignedInteger16 BaseUnits;
	private SignedInteger32 UnitModifier;
	private UnsignedInteger16 RateUnits;
	private SignedInteger32 CurrentReading;
	private SignedInteger32 NominalReading;
	private SignedInteger32 NormalMax;
	private SignedInteger32 NormalMin;
	private SignedInteger32 MaxReadable;
	private SignedInteger32 MinReadable;
	private UnsignedInteger32 Resolution;
	private SignedInteger32 Tolerance;
	private SignedInteger32 Accuracy;
	private boolean IsLinear;
	private UnsignedInteger32 Hysteresis;
	private SignedInteger32 LowerThresholdNonCritical;
	private SignedInteger32 UpperThresholdNonCritical;
	private SignedInteger32 LowerThresholdCritical;
	private SignedInteger32 UpperThresholdCritical;
	private SignedInteger32 LowerThresholdFatal;
	private SignedInteger32 UpperThresholdFatal;
	private UnsignedInteger16[] SupportedThresholds;
	private UnsignedInteger16[] EnabledThresholds;
	private UnsignedInteger16[] SettableThresholds;

	public UnsignedInteger32 RestoreDefaultThresholds(){
		return null;
	}
	
	public UnsignedInteger32 GetNonLinearFactors(SignedInteger32 SensorReading, SignedInteger32 Accuracy, UnsignedInteger32 Resolution, SignedInteger32 Tolerance,UnsignedInteger32 Hysteresis){
		return null;
	}
}
