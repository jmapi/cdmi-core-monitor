package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看TCP连接状态.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibTCPConnEntry implements MibOID {

	tcpConnState("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	tcpConnLocalAddress("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	tcpConnLocalPort("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	tcpConnRemAddress("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	tcpConnRemPort("1.3.6.1.2.1.1.5",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.6.13.1";

	private MibTCPConnEntry(String oid, int mode) {
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
