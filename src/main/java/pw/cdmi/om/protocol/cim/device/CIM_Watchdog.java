package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_Watchdog extends CIM_LogicalDevice {
	private UnsignedInteger16 MonitoredEntity;
	private String MonitoredEntityDescription;
	private UnsignedInteger32 TimeoutInterval;
	private UnsignedInteger32 TimerResolution;
	private Date TimeOfLastExpiration;
	private UnsignedInteger16 MonitoredEntityOnLastExpiration;
	private UnsignedInteger16 ActionOnExpiration;
	
	public UnsignedInteger32 KeepAlive(){
		return null;
	}
}
