package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_Service;

public class CIM_ZoneService extends CIM_Service {
	private UnsignedInteger16 SessionState = new UnsignedInteger16(4);
	private UnsignedInteger16 RequestedSessionState = new UnsignedInteger16(5);
	private UnsignedInteger16 DefaultZoningState;
}
