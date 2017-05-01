package pw.cdmi.om.protocol.snmp.host;

public class MemoryUsage {
	private long total;// 总大小

	private long free;// 空闲大小

	private long used; // 已使用

	private float usage;// 内存使用率

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getFree() {
		return free;
	}

	public void setFree(long free) {
		this.free = free;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public float getUsage() {
		return usage;
	}

	public void setUsage(float usage) {
		this.usage = usage;
	}
}
