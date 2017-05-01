package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 接口与端口对应关系的类定义.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibInterfaceToPort implements MibOID {

	dot1dBasePort("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dBasePortIfIndex("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dBasePortCircuit("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dBasePortDelayExceededDiscards("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	dot1dBasePortMtuExceededDiscards("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.17.1.4.1";

	private MibInterfaceToPort(String oid, int mode) {
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
