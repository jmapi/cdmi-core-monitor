package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_Job;

public class CIM_PrintJob extends CIM_Job {
	private String SystemCreationClassName;
	private String SystemName;
	private String QueueCreationClassName;
	private String QueueName;
	private String JobStatus;
	private String JobID;
	private String SchedulingInformation;
	private UnsignedInteger32 JobSize;
	private UnsignedInteger16 Language;
	private String[] MimeTypes;
	private String RequiredPaperType;
	private UnsignedInteger16[] Finishing;
	private UnsignedInteger32 Copies;
	private UnsignedInteger32 HorizontalResolution;
	private UnsignedInteger32 VerticalResolution;
	private String CharSet;
	private String NaturalLanguage;
	private UnsignedInteger32 NumberUp;
	private UnsignedInteger16 PrintJobStatus;
	private Date TimeCompleted;
	private String[] RequiredJobSheets;
	private String JobOrigination;
	private Date ElapsedTime;
	private String ElementName;
	private Date StartTime;
	private Date TimeSubmitted;
}
