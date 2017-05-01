package pw.cdmi.om.protocol.snmp.mib;

import org.snmp4j.PDU;

public enum MibF5 implements MibOID {
	

    /** 采集内存使用情况参数 **/
    sysStatMemoryUsed("1.3.6.1.4.1.3375.2.1.1.2.1.45.0", PDU.GET),//TMM Mem Usage
    sysHostMemoryUsed("1.3.6.1.4.1.3375.2.1.7.1.2.0", PDU.GET),//Host Mem Usage
    sysHostMemoryTotal("1.3.6.1.4.1.3375.2.1.7.1.1.0",PDU.GET),//Displays amount of free memory remaining
    sysStatMemoryTotal("1.3.6.1.4.1.3375.2.1.1.2.1.44.0",PDU.GET),//The total memory available on the system.
    /** 采集活动连接数 **/
    sysStatClientCurConns("1.3.6.1.4.1.3375.2.1.1.2.1.8.0",PDU.GET), //Active client Connections
    sysStatServerCurConns("1.3.6.1.4.1.3375.2.1.1.2.1.15",PDU.GET), //Active server Connections
    sysStatPvaClientCurConns("1.3.6.1.4.1.3375.2.1.1.2.1.22",PDU.GET),//Active pva client Connections
    sysStatPvaServerCurConns("1.3.6.1.4.1.3375.2.1.1.2.1.29",PDU.GET),//Active pva server Connections
    sysClientsslStatCurConns("1.3.6.1.4.1.3375.2.1.1.2.9.2",PDU.GET),//Active ssl client Connections
    sysServersslStatCurConns("1.3.6.1.4.1.3375.2.1.1.2.10.2",PDU.GET),//Active ssl server Connections
    /** 采集新建连接数 **/
    sysTcpStatAccepts("1.3.6.1.4.1.3375.2.1.1.2.12.6.0",PDU.GET),//New Connections: Client Accepts
    sysTcpStatConnects("1.3.6.1.4.1.3375.2.1.1.2.12.8.0",PDU.GET),//New Connections: Server Connects
    sysStatClientTotConns("1.3.6.1.4.1.3375.2.1.1.2.1.7.0",PDU.GET),//New Connections: Client Connects 
    sysStatServerTotConns("1.3.6.1.4.1.3375.2.1.1.2.1.14.0",PDU.GET),//Total New Connections: Server Connects
    //供当前连接数
    ltmVirtualServStatClientCurConns("1.3.6.1.4.1.3375.2.2.10.2.3.1.12.0",PDU.GET),
    
    sysStatPvaClientTolConns("1.3.6.1.4.1.3375.2.1.1.2.1.21.0",PDU.GET),//New PVA Connections: pva client
    sysStatPvaServerTolConns("1.3.6.1.4.1.3375.2.1.1.2.1.28.0",PDU.GET),//New PVA Connections: pva server
    
    sysClientsslStatTotNativeConns("1.3.6.1.4.1.3375.2.1.1.2.9.6.0",PDU.GET),//New Client SSL Profile Connttions: SSL Client
    sysClientsslStatTotCompatConns("1.3.6.1.4.1.3375.2.1.1.2.9.9.0",PDU.GET),//New Client SSL Profile Connttions: SSL Client
    sysServersslStatTotNativeConns("1.3.6.1.4.1.3375.2.1.1.2.10.6",PDU.GET),//New Client SSL Profile Connttions: SSL Server
    sysServersslStatTotCompatConns("1.3.6.1.4.1.3375.2.1.1.2.10.9",PDU.GET),//New Client SSL Profile Connttions: SSL Server
    /** 采集流量吞吐率 **/
    sysStatClientBytesIn("1.3.6.1.4.1.3375.2.1.1.2.1.3.0",PDU.GET),//Throughput: Client Bites
    sysStatClientBytesOut("1.3.6.1.4.1.3375.2.1.1.2.1.5.0",PDU.GET),//Throughput: Client Bites
    sysStatServerBytesIn("1.3.6.1.4.1.3375.2.1.1.2.1.10.0",PDU.GET),//Throughput: Server Bites
    sysStatServerBytesOut("1.3.6.1.4.1.3375.2.1.1.2.1.12.0",PDU.GET),//Throughput: Server Bites
    sysHttpCompressionStatPrecompressBytes("1.3.6.1.4.1.3375.2.1.1.2.22.2.0",PDU.GET),
    sysHttpStatPrecompresssBytes("1.3.6.1.4.1.3375.2.1.1.2.4.21.0",PDU.GET),
    /**采集包吞吐量**/
    sysStatClientPktsIn("1.3.6.1.4.1.3375.2.1.1.2.1.2.0",PDU.GET),
    sysStatClientPktsOut("1.3.6.1.4.1.3375.2.1.1.2.1.4.0",PDU.GET),
    sysStatServerPktsIn("1.3.6.1.4.1.3375.2.1.1.2.1.9.0",PDU.GET),
    sysStatServerPktsOut("1.3.6.1.4.1.3375.2.1.1.2.1.11.0",PDU.GET),
    
    /** 采集HTTP请求数 **/
    sysStatHttpRequests("1.3.6.1.4.1.3375.2.1.1.2.1.56.0",PDU.GET), //Http Requests:
    /** 采集CPU使用率 **/
    sysHostCpuUser("1.3.6.1.4.1.3375.2.1.7.2.2.1.3",PDU.GET),//CPU(0-n)
    sysHostCpuNice("1.3.6.1.4.1.3375.2.1.7.2.2.1.4",PDU.GET),//CPU(0-n)
    sysHostCpuSystem("1.3.6.1.4.1.3375.2.1.7.2.2.1.6",PDU.GET),////CPU(0-n) FIXME 需要检查
    sysHostCpuIdle("1.3.6.1.4.1.3375.2.1.7.2.2.1.5.0",PDU.GET),//CPU(0-n)
    sysHostCpuIrq("1.3.6.1.4.1.3375.2.1.7.2.2.1.7",PDU.GET),//CPU(0-n)
    sysHostCpuSoftirq("1.3.6.1.4.1.3375.2.1.7.2.2.1.8",PDU.GET),//CPU(0-n)
    sysHostCpuLowait("1.3.6.1.4.1.3375.2.1.7.2.2.1.9",PDU.GET),//CPU(0-n)
    
    sysStatTmTotalCycles("1.3.6.1.4.1.3375.2.1.1.2.1.41.0",PDU.GET),//CPU Usage: TMM CPU Usage
    sysStatTmIdleCycles("1.3.6.1.4.1.3375.2.1.1.2.1.42.0",PDU.GET),//CPU Usage: TMM CPU Usage
    sysStatTmSleepCycles("1.3.6.1.4.1.3375.2.1.1.2.1.43.0",PDU.GET),//CPU Usage: TMM CPU Usage
    /** 采集RAM Cache Utilization **/
    sysHttpStatRamcacheHits("1.3.6.1.4.1.3375.2.1.1.2.4.46.0",PDU.GET),// The number of RAM cache hit
    sysHttpStatRamcacheMisses("1.3.6.1.4.1.3375.2.1.1.2.4.47.0",PDU.GET),// The number of RAM cache misses excluding uncacheable data
    sysHttpStatRamcacheMissesAl("1.3.6.1.4.1.3375.2.1.1.2.4.48",PDU.TRAP),// The total number of RAM cache misses
    sysHttpStatRamcacheHitBytes("1.3.6.1.4.1.3375.2.1.1.2.4.49.0",PDU.GET),// The number of RAM cache hits in bytes
    sysHttpStatRamcacheMissBytes("1.3.6.1.4.1.3375.2.1.1.2.4.50.0",PDU.GET),// The number of RAM cache misses in bytes excluding uncacheable data
    sysHttpStatRamcacheMissBytesAll("1.3.6.1.4.1.3375.2.1.1.2.4.51",PDU.TRAP),// The total number of RAM cache misses in bytes
    sysHttpStatRamcacheSize("1.3.6.1.4.1.3375.2.1.1.2.4.52",PDU.TRAP),// The RAM cache size
    sysHttpStatRamcacheCount("1.3.6.1.4.1.3375.2.1.1.2.4.53",PDU.TRAP),// The RAM cache count
    sysHttpStatRamcacheEvictions("1.3.6.1.4.1.3375.2.1.1.2.4.54.0",PDU.GET),//  The number of RAM cache evictions
    
    sysClientsslStatNotssl("1.3.6.1.4.1.3375.2.1.1.2.9.50",PDU.TRAP),//  The total number of bad client greetings for global client SSL statistics
    sysServersslStatNotssl("1.3.6.1.4.1.3375.2.1.1.2.10.50",PDU.TRAP),//  The total number of bad client greetings for global server SSL statistics
    
    apmAccessStatCurrentActiveSessions("1.3.6.1.4.1.3375.2.6.1.4.3.0",PDU.GET),
    
    apmAccessStatCurrentPendingSessions("1.3.6.1.4.1.3375.2.6.1.4.4.0",PDU.GET),
    
    apmGlobalConnectivityStatIngressRaw("1.3.6.1.4.1.3375.2.6.1.5.5.0",PDU.GET),
    apmGlobalConnectivityStatEgressRaw("1.3.6.1.4.1.3375.2.6.1.5.6.0",PDU.GET),
    apmGlobalConnectivityStatIngressCompressed("1.3.6.1.4.1.3375.2.6.1.5.7.0",PDU.GET),
    apmGlobalConnectivityStatEgressCompressed("1.3.6.1.4.1.3375.2.6.1.5.8.0",PDU.GET),
    
    
    apmGlobalRewriteStatClientReqBytes("1.3.6.1.4.1.3375.2.6.1.6.2.0",PDU.GET),
    apmGlobalRewriteStatClientRespBytes("1.3.6.1.4.1.3375.2.6.1.6.3.0",PDU.GET),
    apmGlobalRewriteStatServerReqBytes("1.3.6.1.4.1.3375.2.6.1.6.4.0",PDU.GET),
    apmGlobalRewriteStatServerRespBytes("1.3.6.1.4.1.3375.2.6.1.6.5.0",PDU.GET),
    
    
    apmGlobalRewriteStatClientReqs("1.3.6.1.4.1.3375.2.6.1.6.6.0",PDU.GET),
    apmGlobalRewriteStatClientResps("1.3.6.1.4.1.3375.2.6.1.6.7.0",PDU.GET),
    apmGlobalRewriteStatServerReqs("1.3.6.1.4.1.3375.2.6.1.6.8.0",PDU.GET),
    apmGlobalRewriteStatServerResps("1.3.6.1.4.1.3375.2.6.1.6.9.0",PDU.GET),
    
    
    
    sysGeneralHwName("1.3.6.1.4.1.3375.2.1.3.3.1",PDU.TRAP),// The system hardware name
    sysGeneralHwNumber("1.3.6.1.4.1.3375.2.1.3.3.2",PDU.TRAP);// The system hardware model
	
	
	
	private String oid;

	private int mode;

	private String parent = "1.3.6.1.2.1.25.3.2.1";

	private MibF5(String oid, int mode) {
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
