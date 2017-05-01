package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 设备主MAC信息的类定义.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibFirstMAC implements MibOID {

	dot1dBaseBridgeAddress("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dBaseNumPorts("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dBaseType("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.17.1";

	private MibFirstMAC(String oid, int mode) {
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
