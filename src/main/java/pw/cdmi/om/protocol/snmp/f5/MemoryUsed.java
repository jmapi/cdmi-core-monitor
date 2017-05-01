package pw.cdmi.om.protocol.snmp.f5;

public class MemoryUsed {
	private long totalPhysMemory;

	private long oSUsedMemory;

	private long tMMAllocMemory;

	private long tMMUsedMemory;

	public long getTotalPhysMemory() {
		return totalPhysMemory;
	}

	public void setTotalPhysMemory(long totalPhysMemory) {
		this.totalPhysMemory = totalPhysMemory;
	}

	public long getoSUsedMemory() {
		return oSUsedMemory;
	}

	public void setoSUsedMemory(long oSUsedMemory) {
		this.oSUsedMemory = oSUsedMemory;
	}

	public long gettMMAllocMemory() {
		return tMMAllocMemory;
	}

	public void settMMAllocMemory(long tMMAllocMemory) {
		this.tMMAllocMemory = tMMAllocMemory;
	}

	public long gettMMUsedMemory() {
		return tMMUsedMemory;
	}

	public void settMMUsedMemory(long tMMUsedMemory) {
		this.tMMUsedMemory = tMMUsedMemory;
	}
}
