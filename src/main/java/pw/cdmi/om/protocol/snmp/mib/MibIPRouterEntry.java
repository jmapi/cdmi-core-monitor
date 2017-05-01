package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看路由信息.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibIPRouterEntry implements MibOID {
	
	ipRouteDest("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteIfIndex("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteMetric1("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteMetric2("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteMetric3("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteMetric4("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteNextHop("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteType("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteProto("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteAge("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteMask("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteMetric5("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	ipRouteInfo("1.3.6.1.2.1.1.4",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.4.21.1";

	private MibIPRouterEntry(String oid, int mode) {
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
