package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Dependency;

public class CIM_LibraryExchange extends CIM_Dependency {
	private CIM_InterLibraryPort Antecedent;
	private CIM_InterLibraryPort Dependent;
	private UnsignedInteger16 CurrentlyAccessingPort;
}
