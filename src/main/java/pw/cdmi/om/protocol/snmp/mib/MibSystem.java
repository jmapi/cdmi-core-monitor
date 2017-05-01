package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看设备系统信息 TODO(对类的作用含义说明 – 可选). TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibSystem implements MibOID {

	sysDescr("1.3.6.1.2.1.1.1.0",PDU.GET),
	sysObjectID("1.3.6.1.2.1.1.2.0",PDU.GET),
	sysUpTime("1.3.6.1.2.1.1.3.0",PDU.GET),
	sysContact("1.3.6.1.2.1.1.4.0",PDU.GET),
	sysName("1.3.6.1.2.1.1.5.0",PDU.GET),
	sysLocation("1.3.6.1.2.1.1.6.0",PDU.GET), ;

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1";

	private MibSystem(String oid, int mode) {
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

	// @Override
	// public int getVariableNumber() {
	// return 0;
	// }

	@Override
	public String getParent() {
		return this.parent;
	}

	@Override
	public int size() {
		return MibSystem.values().length;
	}

	// @Override
	// public HashSet<String> getFieldOIDs() {
	// HashSet<String> oids = new HashSet<String>();
	// oids.add(this.oid);
	// return oids;
	// }

}
