package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看磁盘和内存存储状态.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibDiskAndMemoEntry implements MibOID {

	memTotalReal("1.3.6.1.4.1.2021.4.5.0",PDU.GET),		// Total RAM in machine
	memAvailReal("1.3.6.1.4.1.2021.4.6.0",PDU.GET), 	// Total RAM used
	hrStorageIndex("1.3.6.1.4.1.2021.4.6.0",PDU.GET), 	// FIXME
	hrStorageType("1.3.6.1.4.1.2021.4.6.0",PDU.GET), 	// FIXME
	hrStorageDescr("1.3.6.1.2.1.25.2.3.1.3",PDU.GETBULK),	// 存储设备描述
	hrStorageAllocationUnits("1.3.6.1.2.1.25.2.3.1.4",PDU.GETBULK),	// 簇的大小
	hrStorageSize("1.3.6.1.2.1.25.2.3.1.5",PDU.GETBULK),	// 簇的的数目
	hrStorageUsed("1.3.6.1.2.1.25.2.3.1.6",PDU.GETBULK), // 使用多少，跟总容量相除就是占用率
	hrStorageAllocationFailures("1.3.6.1.4.1.2021.4.6.0",PDU.GET); 	// FIXME

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.25.2.3.1";

	private MibDiskAndMemoEntry(String oid, int mode) {
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
