package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * "网络接口"的类定义
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibIfEntry implements MibOID {

	ifIndex("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifDescr("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifType("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifMtu("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifSpeed("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifPhysAddress("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifAdminStatus("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifOperStatus("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifLastChange("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifInOctets("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifInUcastPkts("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifInNUcastPkts("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifInDiscards("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifInErrors("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifInUnknownProtos("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifOutOctets("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifOutUcastPkts("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifOutNUcastPkts("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifOutDiscards("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifOutErrors("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifOutQLen("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ifSpecific("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.2.2.1";

	private MibIfEntry(String oid, int mode) {
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
