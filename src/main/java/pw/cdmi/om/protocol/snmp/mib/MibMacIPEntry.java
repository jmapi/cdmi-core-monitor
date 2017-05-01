package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibMacIPEntry implements MibOID {

	ipNetToMediaIfIndex("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipNetToMediaPhysAddress("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipNetToMediaNetAddress("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipNetToMediaType("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.4.22.1";

	private MibMacIPEntry(String oid, int mode) {
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
