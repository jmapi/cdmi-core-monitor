package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

public class CIM_AssociatedSupplyCurrentSensor extends CIM_AssociatedSensor {
	private CIM_CurrentSensor Antecedent;
	private CIM_PowerSupply Dependent;
	private UnsignedInteger16 MonitoringRange;
}
