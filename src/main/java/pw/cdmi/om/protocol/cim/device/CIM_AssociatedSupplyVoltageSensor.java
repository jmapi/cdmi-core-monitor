package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

public class CIM_AssociatedSupplyVoltageSensor extends CIM_AssociatedSensor {
	private CIM_VoltageSensor Antecedent;
	private CIM_PowerSupply Dependent;
	private UnsignedInteger16 MonitoringRange;
}
