package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看IP地址信息.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibIPAddrEntry implements MibOID {

	ipAdEntAddr("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipAdEntIfIndex("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipAdEntNetMask("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipAdEntBcastAddr("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipAdEntReasmMaxSize("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.4.20.1";

	private MibIPAddrEntry(String oid, int mode) {
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
