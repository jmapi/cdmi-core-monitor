package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_ServiceServiceDependency extends CIM_ProvidesServiceToElement {
	private CIM_Service Antecedent;
	private CIM_Service Dependent;
	private UnsignedInteger16 TypeOfDependency;
	private boolean RestartService;
}
