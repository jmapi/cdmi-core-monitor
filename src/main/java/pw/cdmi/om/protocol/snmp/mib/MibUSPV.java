package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

/**
 * **********************************************************
 * 适用于Hitachi HDS USPV和VSP存储系统.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年5月19日
 ***********************************************************
 */
public enum MibUSPV implements MibOID{
	
	//Hitachi and RAID sub-tree
	hitachi("1.3.6.1.4.1.116", PDU.GETNEXT),
	system("1.3.6.1.4.1.116.3", PDU.GETNEXT),
	storage("1.3.6.1.4.1.116.3.11", PDU.GETNEXT),
	raid("1.3.6.1.4.1.116.3.11.4", PDU.GETNEXT),
	raidDummy("1.3.6.1.4.1.116.3.11.4.1", PDU.GETNEXT),
	raidRoot("1.3.6.1.4.1.116.3.11.4.1.1", PDU.GETNEXT),
	
	systemExMib("1.3.6.1.4.1.116.5", PDU.GETNEXT),
	storageExMib("1.3.6.1.4.1.116.5.11", PDU.GETNEXT),
	raidExMib("1.3.6.1.4.1.116.5.11.4", PDU.GETNEXT),
	raidExMibDummy("1.3.6.1.4.1.116.5.11.4.1", PDU.GETNEXT),
	raidExMibDummyX("1.3.6.1.4.1.116.5.11.4.2", PDU.GETNEXT),
	raidExMibRoot("1.3.6.1.4.1.116.5.11.4.1.1", PDU.GETNEXT),
	//Basic information
	raidExMibName("1.3.6.1.4.1.116.5.11.4.1.1.1.0", PDU.GET),
	raidExMibVersion("1.3.6.1.4.1.116.3.11.4.1.1.2.0", PDU.GET),//对uspv无效
	raidExMibAgentVersion("1.3.6.1.4.1.116.3.11.4.1.1.3.0", PDU.GET),//对uspv无效
	raidExMibDkcCount("1.3.6.1.4.1.116.3.11.4.1.1.4.0", PDU.GET),//对uspv无效
	//Raid list
	raidExMibRaidListTable("1.3.6.1.4.1.116.5.11.4.1.1.5", PDU.GETBULK),
	raidExMibRaidListEntry("1.3.6.1.4.1.116.3.11.4.1.1.5.1", PDU.GET),
	raidlistSerialNumber("1.3.6.1.4.1.116.3.11.4.1.1.5.1.1", PDU.GETNEXT),
	raidlistMibNickName("1.3.6.1.4.1.116.3.11.4.1.1.5.1.2", PDU.GET),
	raidlistDKCMainVersion("1.3.6.1.4.1.116.3.11.4.1.1.5.1.3", PDU.GET),
	raidlistDKCProductName("1.3.6.1.4.1.116.3.11.4.1.1.5.1.4", PDU.GET),
	//Disk controller information
	raidExMibDKCHWTable("1.3.6.1.4.1.116.5.11.4.1.1.6", PDU.GETBULK),
	raidExMibDKCHWEntry("1.3.6.1.4.1.116.3.11.4.1.1.6.1", PDU.GET),
	dkcRaidListIndexSerialNumber("1.3.6.1.4.1.116.3.11.4.1.1.6.1.1", PDU.GET),
	dkcHWProcessor("1.3.6.1.4.1.116.3.11.4.1.1.6.1.2", PDU.GET),
	dkcHWCSW("1.3.6.1.4.1.116.3.11.4.1.1.6.1.3", PDU.GET),
	dkcHWCache("1.3.6.1.4.1.116.3.11.4.1.1.6.1.4", PDU.GET),
	dkcHWSM("1.3.6.1.4.1.116.3.11.4.1.1.6.1.5", PDU.GET),
	dkcHWPS("1.3.6.1.4.1.116.3.11.4.1.1.6.1.6", PDU.GET),
	dkcHWBattery("1.3.6.1.4.1.116.3.11.4.1.1.6.1.7", PDU.GET),
	dkcHWFan("1.3.6.1.4.1.116.3.11.4.1.1.6.1.8", PDU.GET),
	dkcHWEnvironment("1.3.6.1.4.1.116.3.11.4.1.1.6.1.9", PDU.GET),
	//Disk unit information
	raidExMibDKUHWTable("1.3.6.1.4.1.116.5.11.4.1.1.7", PDU.GETBULK),
	raidExMibDKUHWEntry("1.3.6.1.4.1.116.3.11.4.1.1.7.1", PDU.GETNEXT),
	dkuRaidListIndexSerialNumber("1.3.6.1.4.1.116.3.11.4.1.1.7.1.1", PDU.GET),
	dkuHWPS("1.3.6.1.4.1.116.3.11.4.1.1.7.1.2", PDU.GET),
	dkuHWFan("1.3.6.1.4.1.116.3.11.4.1.1.7.1.3", PDU.GET),
	dkuHWEnvironment("1.3.6.1.4.1.116.3.11.4.1.1.7.1.4", PDU.GET),
	dkuHWDrive("1.3.6.1.4.1.116.3.11.4.1.1.7.1.5", PDU.GET),
	//Trap List
	raidExMibTrapListTable("1.3.6.1.4.1.116.5.11.4.1.1.8", PDU.TRAP),
	raidExMibTrapListEntry("1.3.6.1.4.1.116.5.11.4.1.1.8.1", PDU.TRAP),
	eventListIndexSerialNumber("1.3.6.1.4.1.116.5.11.4.1.1.8.1.1", PDU.TRAP),
	eventListNickname("1.3.6.1.4.1.116.5.11.4.1.1.8.1.2", PDU.TRAP),
	eventListIndexRecordNo("1.3.6.1.4.1.116.5.11.4.1.1.8.1.3", PDU.TRAP),
	eventListREFCODE("1.3.6.1.4.1.116.5.11.4.1.1.8.1.4", PDU.TRAP),
	eventListDate("1.3.6.1.4.1.116.5.11.4.1.1.8.1.5", PDU.TRAP),
	eventListTime("1.3.6.1.4.1.116.5.11.4.1.1.8.1.6", PDU.TRAP),
	eventListDescription("1.3.6.1.4.1.116.5.11.4.1.1.8.1.7", PDU.TRAP),
	//Trap definition
	eventTrapSerialNumber("1.3.6.1.4.1.116.5.11.4.2.1", PDU.TRAP),
	eventTrapNickname("1.3.6.1.4.1.116.5.11.4.2.2", PDU.TRAP),
	eventTrapREFCODE("1.3.6.1.4.1.116.5.11.4.2.3", PDU.TRAP),
	eventTrapPartsID("1.3.6.1.4.1.116.5.11.4.2.4", PDU.TRAP),
	eventTrapDate("1.3.6.1.4.1.116.5.11.4.2.5", PDU.TRAP),
	eventTrapTime("1.3.6.1.4.1.116.5.11.4.2.6", PDU.TRAP),
	eventTrapDescription("1.3.6.1.4.1.116.5.11.4.2.7", PDU.TRAP),
	
	raideventUseracute("1.3.6.1.4.1.116.3.11.4.1.1.1", PDU.TRAP),
	raideventUserserious("1.3.6.1.4.1.116.3.11.4.1.1.2", PDU.TRAP),
	raideventUsermoderate("1.3.6.1.4.1.116.3.11.4.1.1.3", PDU.TRAP),
	raideventUserservice("1.3.6.1.4.1.116.3.11.4.1.1.4", PDU.TRAP),
	
	//Disk control device information2
	raidExMibDKCHW2("1.3.6.1.4.1.116.3.11.4.1.1.70", PDU.GETBULK),
	dkcHW2Entry("1.3.6.1.4.1.116.3.11.4.1.1.70.1", PDU.GETNEXT),
	DKC2RaidListIndexSerialNumber("1.3.6.1.4.1.116.3.11.4.1.1.70.1.1", PDU.GETBULK),
	DKCHW2Environment("1.3.6.1.4.1.116.3.11.4.1.1.70.1.2.0", PDU.GET),
	DKCHW2SVP("1.3.6.1.4.1.116.3.11.4.1.1.70.1.3.0", PDU.GETBULK),
	DKCHW2PP("1.3.6.1.4.1.116.3.11.4.1.1.70.1.4.0", PDU.GETNEXT);
	
	private String oid;

	private int mode;

	private String parent = "1.3.6.1.4.1.116.3.11.4.1";

	private MibUSPV(String oid, int mode) {
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
