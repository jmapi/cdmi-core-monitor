package pw.cdmi.om.protocol.snmp.host;

public class CpuUsage {
	private float sysCpuUsage;

	private float freeCpuUsage;

	private float userCpuUsage;

	private float otherCpuUsage;

	private float usedCpuUsage;

	public float getSysCpuUsage() {
		return sysCpuUsage;
	}

	public void setSysCpuUsage(float sysCpuUsage) {
		this.sysCpuUsage = sysCpuUsage;
	}

	public float getFreeCpuUsage() {
		return freeCpuUsage;
	}

	public void setFreeCpuUsage(float freeCpuUsage) {
		this.freeCpuUsage = freeCpuUsage;
	}

	public float getUserCpuUsage() {
		return userCpuUsage;
	}

	public void setUserCpuUsage(float userCpuUsage) {
		this.userCpuUsage = userCpuUsage;
	}

	public float getOtherCpuUsage() {
		return otherCpuUsage;
	}

	public void setOtherCpuUsage(float otherCpuUsage) {
		this.otherCpuUsage = otherCpuUsage;
	}

	public float getUsedCpuUsage() {
		return usedCpuUsage;
	}

	public void setUsedCpuUsage(float usedCpuUsage) {
		this.usedCpuUsage = usedCpuUsage;
	}

}
