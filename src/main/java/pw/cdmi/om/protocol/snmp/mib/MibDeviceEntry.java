package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/************************************************************
 * 查看设备设施状态
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public enum MibDeviceEntry implements MibOID {

	hrDeviceIndex("1.3.6.1.2.1.25.2.3.1.4",PDU.GETBULK),	//
	hrDeviceType("1.3.6.1.2.1.25.2.3.1.5",PDU.GETBULK),	//
	hrDeviceDescr("1.3.6.1.2.1.25.2.3.1.6",PDU.GETBULK), //
	hrDeviceID("1.3.6.1.2.1.25.2.3.1.6",PDU.GETBULK), //
	hrDeviceStatus("1.3.6.1.2.1.25.2.3.1.6",PDU.GETBULK), //
	hrDeviceErrors("1.3.6.1.2.1.25.2.3.1.6",PDU.GETBULK); //

	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.25.3.2.1";

	private MibDeviceEntry(String oid, int mode) {
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
