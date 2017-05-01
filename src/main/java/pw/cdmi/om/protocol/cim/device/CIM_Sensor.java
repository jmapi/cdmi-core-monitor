package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_Sensor extends CIM_LogicalDevice {
	private UnsignedInteger16 SensorType;
	private String OtherSensorTypeDescription;
	private String[] PossibleStates;
	private String CurrentState;
	private UnsignedInteger64 PollingInterval;
	private String SensorContext;
}
