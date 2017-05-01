package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看CPU状态.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibProcessorEntry implements MibOID {

	ssCpuUser("1.3.6.1.4.1.2021.11.9.0",PDU.GET),		// 用户CPU百分比
	ssCpuSystem("1.3.6.1.4.1.2021.11.10.0",PDU.GET), 	// 系统CPU百分比
	ssCpuIdle("1.3.6.1.4.1.2021.11.11.0",PDU.GET),	// 空闲CPU百分比
	hrDeviceIndex("1.3.6.1.2.1.1.4",PDU.GETNEXT),	//  FIXME
	hrProcessorFrwID("1.3.6.1.2.1.1.4",PDU.GETNEXT),	// FIXME
	hrProcessorLoad("1.3.6.1.2.1.25.3.3.1.2",PDU.GETBULK),
	;// CPU的当前负载，N个核就有N个负载

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.25.3.3.1";

	private MibProcessorEntry(String oid, int mode) {
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
