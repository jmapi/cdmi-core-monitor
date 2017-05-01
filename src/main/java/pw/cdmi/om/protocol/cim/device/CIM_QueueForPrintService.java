package pw.cdmi.om.protocol.cim.device;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;

public class CIM_QueueForPrintService extends CIM_Dependency {
	private CIM_PrintQueue Antecedent;
	private CIM_PrintService Dependent;
	private boolean QueueAcceptingFromService;
}
