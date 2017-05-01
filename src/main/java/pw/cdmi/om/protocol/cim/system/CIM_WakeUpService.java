package pw.cdmi.om.protocol.cim.system;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger8;

import pw.cdmi.om.protocol.cim.core.CIM_Service;

public class CIM_WakeUpService extends CIM_Service {
	private UnsignedInteger16 WakeUpType;
	private String OtherWakeUpTypeDescription;
	private UnsignedInteger16 FilterType;
	private UnsignedInteger8[] FilterData;
	private String OtherFilterTypeDescription;
}
