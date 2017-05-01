package pw.cdmi.om.protocol.snmp;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public class SNMPTarget {
	private String targetIp;
	private String readCommunity;
	private String writeCommunity;
	private int port = 161;
	private int snmpVersion;
	private String user;
	private String password;
	private String contextEngineId;
	
	public String getTargetIp() {
		return targetIp;
	}
	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}
	public String getReadCommunity() {
		return readCommunity;
	}
	public void setReadCommunity(String readCommunity) {
		this.readCommunity = readCommunity;
	}
	public String getWriteCommunity() {
		return writeCommunity;
	}
	public void setWriteCommunity(String writeCommunity) {
		this.writeCommunity = writeCommunity;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getSnmpVersion() {
		return snmpVersion;
	}
	public void setSnmpVersion(int snmpVersion) {
		this.snmpVersion = snmpVersion;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getContextEngineId() {
		return contextEngineId;
	}
	public void setContextEngineId(String contextEngineId) {
		this.contextEngineId = contextEngineId;
	}
}

