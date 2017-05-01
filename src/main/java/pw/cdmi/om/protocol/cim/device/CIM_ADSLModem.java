package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;


public class CIM_ADSLModem extends CIM_DSLModem {
	UnsignedInteger32 NoiseMargin;
	UnsignedInteger32 LineAttenuation;
	UnsignedInteger32 LineState;
	UnsignedInteger32 TotalOutputPower;
	UnsignedInteger32 MaxDataRate;
}
