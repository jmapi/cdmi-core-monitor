package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看交换机端口转发信息.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibDot1dTpFdbEntry implements MibOID {

	dot1dTpFdbAddress("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dTpFdbPort("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dTpFdbStatus("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.17.4.3.1";

	private MibDot1dTpFdbEntry(String oid, int mode) {
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
