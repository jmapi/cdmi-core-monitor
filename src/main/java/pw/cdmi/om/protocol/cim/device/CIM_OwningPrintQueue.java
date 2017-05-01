package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.system.CIM_JobDestinationJobs;

public class CIM_OwningPrintQueue extends CIM_JobDestinationJobs {
	private CIM_PrintQueue Antecedent;
	private CIM_PrintJob Dependent;
	private UnsignedInteger32 QueuePosition;
}
