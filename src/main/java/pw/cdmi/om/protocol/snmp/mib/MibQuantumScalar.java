package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

public enum MibQuantumScalar implements MibOID {
	// Library Identification
	productName("1.3.6.1.4.1.3764.1.1.10.3",PDU.GET),		// Product Name
	name("1.3.6.1.4.1.3764.1.1.200.20.90.20.1.3",PDU.GET), 	// Logical Library Name
	// Library Status and Health TODO名称未查到
	a("1.3.6.1.4.1.3764.1.1.200.20.100.10.1.2",PDU.GET),	// RAS Subsystem Status
	onlineStatus("1.3.6.1.4.1.3764.1.1.200.20.80.10.1.5",PDU.GET),	// Physical Library Online/Offline State
	readiness("1.3.6.1.4.1.3764.1.1.200.20.80.10.1.6",PDU.GET),	// Robotics Readiness
	b("1.3.6.1.4.1.3764.1.1.200.20.90.20.1.12",PDU.GET),	// Partition Online/Offline Status
	phDriveRasStatus("1.3.6.1.4.1.3764.1.1.200.20.80.110.1.31",PDU.GET),	// Drive Overall Health
	phDriveOnlineStatus("1.3.6.1.4.1.3764.1.1.200.20.80.110.1.29",PDU.GET),	// Drive Online/Offline Status
	physLibDoorStatus("1.3.6.1.4.1.3764.1.1.200.20.80.10.1.16",PDU.GET),	// Library Main Door Status
	physLibIeStationDoorStatus("1.3.6.1.4.1.3764.1.1.200.20.80.75.1.2",PDU.GET);// Import/Export Station Door Status

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.4.1.8.0.0.0.0.0.1.0.59";

	private MibQuantumScalar(String oid, int mode) {
		this.oid = oid;
		this.mode = mode;
	}

	public int getMode() {
		return this.mode;
	}

	@Override
	public String getOID() {
		return this.oid;
	}

	@Override
	public String getParent() {
		return this.parent;
	}

	@Override
	public int size() {
		return MibSystem.values().length;
	}

}
