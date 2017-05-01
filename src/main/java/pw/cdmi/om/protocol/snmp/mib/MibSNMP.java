package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * SNMP代理状态的类定义.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibSNMP implements MibOID {

	snmpInPkts("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	snmpOutPkts("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	snmpInBadVersions("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	snmpInBadCommunityNames("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	snmpInBadCommunityUses("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	snmpInASNParseErrs("1.3.6.1.2.1.1.4",PDU.GETNEXT),
	snmpInNoSuchNames("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInBadValues("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInReadOnlys("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInGenErrs("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInTotalReqVars("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInTotalSetVars("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInGetRequests("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInGetNexts("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInSetRequests("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInGetResponses("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpInTraps("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutTooBigs("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutNoSuchNames("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutBadValues("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutGenErrs("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutGetRequests("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutGetNexts("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutSetRequests("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutGetResponses("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpOutTraps("1.3.6.1.2.1.1.5",PDU.GETNEXT),
	snmpEnableAuthenTraps("1.3.6.1.2.1.1.5",PDU.GETNEXT);

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.11";

	private MibSNMP(String oid, int mode) {
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
