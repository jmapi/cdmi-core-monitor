package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看IP数据包状态
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibIP implements MibOID {

	ipForwarding("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipDefaultTTL("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipInReceives("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipInHdrErrors("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipForwDatagrams("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipInUnknownProtos("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipInDiscards("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipInDelivers("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipOutRequests("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipOutDiscards("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipOutNoRoutes("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipReasmTimeout("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipReasmReqds("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipReasmOKs("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipReasmFails("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipFragOKs("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipFragFails("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipFragCreates("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRoutingDiscards("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.4";

	private MibIP(String oid, int mode) {
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
