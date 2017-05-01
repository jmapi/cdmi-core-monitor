package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.SignedInteger8;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;

public class CIM_Job extends CIM_LogicalElement {
	private String JobStatus;
	private Date TimeSubmitted;
	private Date ScheduledStartTime;
	private Date StartTime;
	private Date ElapsedTime;
	private UnsignedInteger32 JobRunTimes = new UnsignedInteger32(1);
	private UnsignedInteger8 RunMonth;
	private SignedInteger8 RunDay;
	private SignedInteger8 RunDayOfWeek;
	private Date RunStartInterval;
	private UnsignedInteger16 LocalOrUtcTime;
	private Date UntilTime;
	private String Notify;
	private String Owner;
	private UnsignedInteger32 Priority;
	private UnsignedInteger16 PercentComplete;
	private boolean DeleteOnCompletion;
	private UnsignedInteger16 ErrorCode;
	private String ErrorDescription;
	private UnsignedInteger16 RecoveryAction;
	private String OtherRecoveryAction;
	public UnsignedInteger32 KillJob(boolean DeleteOnKill){
		return null;
	}
}
