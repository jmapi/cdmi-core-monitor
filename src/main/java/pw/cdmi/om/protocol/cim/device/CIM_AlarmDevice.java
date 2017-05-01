package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_AlarmDevice extends CIM_LogicalDevice {
	boolean AudibleAlarm;
	private boolean VisibleAlarm;
	private boolean MotionAlarm;
	private UnsignedInteger16 Urgency;
	private UnsignedInteger16 AlarmState;
	private boolean AudioIndicatorIsDisabled;
	private boolean VisualIndicatorIsDisabled;
	private boolean MotionIndicatorIsDisabled;
	
	public UnsignedInteger32 SetAlarmState(UnsignedInteger16 RequestedAlarmState){
		return null;
	}
	
	public UnsignedInteger32 SetAlarmIndicator(UnsignedInteger16 AudioIndicator, UnsignedInteger16 VisualIndicator, UnsignedInteger16 MotionIndicator){
		return null;
	}
	
	public UnsignedInteger32 SetUrgency(UnsignedInteger16 RequestedUrgency){
		return null;
	}
}
