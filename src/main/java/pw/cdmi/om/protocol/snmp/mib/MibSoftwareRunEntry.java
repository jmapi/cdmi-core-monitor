package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 进程状态的类定义
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibSoftwareRunEntry implements MibOID {

	hrSWRunIndex("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	hrSWRunName("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	hrSWRunID("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	hrSWRunPath("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	hrSWRunParameters("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	hrSWRunType("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	hrSWRunStatus("1.3.6.1.2.1.1.5",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.25.4.2.1";

	private MibSoftwareRunEntry(String oid, int mode) {
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
