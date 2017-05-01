package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_System;

public class CIM_StorageLibrary extends CIM_System {
	private UnsignedInteger16[] Capabilities;
	private boolean Overfilled;
	private boolean AuditNeeded;
	private boolean AuditInProgress;
	private UnsignedInteger64 MaxAuditTime;
	private boolean Automated;
	private boolean RoboticsEnabled;

	public UnsignedInteger32 EnableRobotics(boolean enable){
		return null;
	}
}
