package pw.cdmi.om.protocol.snmp.f5;

public class NewConnections {
	private Long clientAccepts;

	private Long serverConnects;

	public Long getClientAccepts() {
		return clientAccepts;
	}

	public void setClientAccepts(Long clientAccepts) {
		this.clientAccepts = clientAccepts;
	}

	public Long getServerConnects() {
		return serverConnects;
	}

	public void setServerConnects(Long serverConnects) {
		this.serverConnects = serverConnects;
	}
}
