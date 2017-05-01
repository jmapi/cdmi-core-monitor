package pw.cdmi.om.protocol.cim.system;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_ComputerSystem;

public class CIM_UnitaryComputerSystem extends CIM_ComputerSystem {
	private String[] InitialLoadInfo;
	private String LastLoadInfo;
	private boolean PowerManagementSupported;
	private UnsignedInteger16 PowerState;
	private UnsignedInteger16 WakeUpType;
}
