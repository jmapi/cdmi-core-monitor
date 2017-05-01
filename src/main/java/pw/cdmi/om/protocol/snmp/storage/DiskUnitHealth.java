package pw.cdmi.om.protocol.snmp.storage;

public class DiskUnitHealth {
	private int dkuRaidListIndexSerialNumber;
	private int dkuHWPS;
	private int dkuHWFan;
	private int dkuHWEnvironment;
	private int dkuHWDrive;
	
	public int getDkuRaidListIndexSerialNumber() {
		return dkuRaidListIndexSerialNumber;
	}
	public int getDkuHWPS() {
		return dkuHWPS;
	}
	public int getDkuHWFan() {
		return dkuHWFan;
	}
	public int getDkuHWEnvironment() {
		return dkuHWEnvironment;
	}
	public int getDkuHWDrive() {
		return dkuHWDrive;
	}
	public void setDkuRaidListIndexSerialNumber(int dkuRaidListIndexSerialNumber) {
		this.dkuRaidListIndexSerialNumber = dkuRaidListIndexSerialNumber;
	}
	public void setDkuHWPS(int dkuHWPS) {
		this.dkuHWPS = dkuHWPS;
	}
	public void setDkuHWFan(int dkuHWFan) {
		this.dkuHWFan = dkuHWFan;
	}
	public void setDkuHWEnvironment(int dkuHWEnvironment) {
		this.dkuHWEnvironment = dkuHWEnvironment;
	}
	public void setDkuHWDrive(int dkuHWDrive) {
		this.dkuHWDrive = dkuHWDrive;
	}
	
	
}
