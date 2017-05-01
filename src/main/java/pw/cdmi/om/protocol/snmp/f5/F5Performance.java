package pw.cdmi.om.protocol.snmp.f5;

import java.io.IOException;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.om.protocol.snmp.SNMPClient;
import pw.cdmi.om.protocol.snmp.SNMPTarget;
import pw.cdmi.om.protocol.snmp.mib.MibF5;
import pw.cdmi.om.protocol.snmp.mib.MibOID;

/************************************************************
 * F5获取信息
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月16日
 ************************************************************/
public class F5Performance {

    private Map<String, Object> result;

    public F5Performance(SNMPTarget target) throws IOException {

        MibOID[] mibs = { MibF5.sysHostMemoryTotal, MibF5.sysHostMemoryUsed, MibF5.sysStatMemoryTotal,
                MibF5.sysStatMemoryUsed, MibF5.sysStatClientCurConns, MibF5.sysTcpStatAccepts,
                MibF5.sysTcpStatConnects, MibF5.sysTcpStatConnects, MibF5.sysStatClientBytesIn,
                MibF5.sysStatClientBytesOut, MibF5.sysStatServerBytesIn, MibF5.sysStatServerBytesOut,
                MibF5.sysStatClientPktsIn, MibF5.sysStatClientPktsOut, MibF5.sysStatServerPktsIn,
                MibF5.sysStatServerPktsOut, MibF5.sysStatHttpRequests, MibF5.sysHttpStatRamcacheHits, /**MibF5.ltmVirtualServStatClientCurConns,**/
                MibF5.sysHttpStatRamcacheMisses, MibF5.sysHttpStatRamcacheMissBytes, MibF5.sysHttpStatRamcacheHitBytes,
                MibF5.sysHttpStatRamcacheEvictions, MibF5.sysClientsslStatTotCompatConns,
                MibF5.sysClientsslStatTotNativeConns, MibF5.sysStatPvaServerTolConns,
                MibF5.apmAccessStatCurrentActiveSessions, MibF5.apmAccessStatCurrentPendingSessions,
                MibF5.apmGlobalConnectivityStatIngressRaw, MibF5.apmGlobalConnectivityStatEgressRaw,
                MibF5.apmGlobalConnectivityStatIngressCompressed, MibF5.apmGlobalConnectivityStatEgressCompressed,
                MibF5.apmGlobalRewriteStatClientReqBytes, MibF5.apmGlobalRewriteStatClientRespBytes,
                MibF5.apmGlobalRewriteStatServerReqBytes, MibF5.apmGlobalRewriteStatServerRespBytes,
                MibF5.apmGlobalRewriteStatClientReqs, MibF5.apmGlobalRewriteStatClientResps,
                MibF5.apmGlobalRewriteStatServerReqs, MibF5.apmGlobalRewriteStatServerResps,
                MibF5.sysStatTmTotalCycles, MibF5.sysStatTmIdleCycles, MibF5.sysStatTmSleepCycles,
                MibF5.sysHttpStatPrecompresssBytes, MibF5.sysStatServerTotConns };
        SNMPClient snmpClient = null;
        try {
            snmpClient = new SNMPClient(target);
            result = snmpClient.getMibField(mibs);
        } catch (RuntimeException e) {
            throw new IOException(e);
        } finally {
            snmpClient.close();
        }
    }

    /**
     * ---获取F5的内存使用情况
     * 
     * @param target
     * @return
     * @throws IOException
     */
    public MemoryUsed getMemoryUsed() {
        MemoryUsed memoryUsed = new MemoryUsed();
        memoryUsed.setTotalPhysMemory(Long.valueOf(result.get(MibF5.sysHostMemoryTotal.getOID()).toString()));
        memoryUsed.setoSUsedMemory(Long.valueOf(result.get(MibF5.sysHostMemoryUsed.getOID()).toString()));
        memoryUsed.settMMAllocMemory(Long.valueOf(result.get(MibF5.sysStatMemoryTotal.getOID()).toString()));
        memoryUsed.settMMUsedMemory(Long.valueOf(result.get(MibF5.sysStatMemoryUsed.getOID()).toString()));
        return memoryUsed;
    }

    /**
     * ---获取活动连接数
     * 
     * @param target
     * @return
     * @throws IOException
     */
    public ActiveConnections getActiveConnections() {
        ActiveConnections activeConnections = new ActiveConnections();
        activeConnections.setActiveConnections(Integer.parseInt(result.get(MibF5.sysStatClientCurConns.getOID())
            .toString()));
        return activeConnections;
    }

    /**
     *---获取新建连接数
     * 
     * @param target
     * @return
     * @throws IOException
     */
    public NewConnections getNewConnections() {

        NewConnections newConnections = new NewConnections();
        newConnections.setClientAccepts(Long.parseLong(result.get(MibF5.sysTcpStatAccepts.getOID()).toString()));
        newConnections.setServerConnects(Long.parseLong(result.get(MibF5.sysStatServerTotConns.getOID()).toString()));
        return newConnections;
    }

    /** 
     * ---获取流量吞吐量
     * 
     * @param target
     * @return
     * @throws IOException
     */
    public ThroughputWithBits getThroughputWithBits() {

        ThroughputWithBits throughputWithBits = new ThroughputWithBits();
        throughputWithBits.setSysStatClientBytesIn(Long.parseLong(result.get(MibF5.sysStatClientBytesIn.getOID())
            .toString()));
        throughputWithBits.setSysStatClientBytesOut(Long.parseLong(result.get(MibF5.sysStatClientBytesOut.getOID())
            .toString()));
        throughputWithBits.setSysStatServerBytesIn(Long.parseLong(result.get(MibF5.sysStatServerBytesIn.getOID())
            .toString()));
        throughputWithBits.setSysStatServerBytesOut(Long.parseLong(result.get(MibF5.sysStatServerBytesOut.getOID())
            .toString()));
        throughputWithBits.setSysHttpStatPrecompresssBytes(Long.parseLong(result.get(
            MibF5.sysHttpStatPrecompresssBytes.getOID()).toString()));

        return throughputWithBits;
    }

    /**
     * 
     * ---获取流量吞吐两
     * 
     * @param target
     * @return
     * @throws IOException
     */
    public ThroughputWithPackets getThroughputWithPackets() {
        ThroughputWithPackets throughputWithPackets = new ThroughputWithPackets();
        throughputWithPackets.setSysStatClientPktsIn(Long.parseLong(result.get(MibF5.sysStatClientPktsIn.getOID())
            .toString()));
        throughputWithPackets.setSysStatClientPktsOut(Long.parseLong(result.get(MibF5.sysStatClientPktsOut.getOID())
            .toString()));
        throughputWithPackets.setSysStatServerPktsIn(Long.parseLong(result.get(MibF5.sysStatServerPktsIn.getOID())
            .toString()));
        throughputWithPackets.setSysStatServerPktsOut(Long.parseLong(result.get(MibF5.sysStatServerPktsOut.getOID())
            .toString()));

        return throughputWithPackets;
    }

    /**
     * 
     * ---获取HTTP请求
     * 
     * @param target
     * @return
     * @throws IOException
     */
    public HTTPRequests getHttpRequests() {

        HTTPRequests httpRequests = new HTTPRequests();

        httpRequests.setHttpRequests(Long.parseLong(result.get(MibF5.sysStatHttpRequests.getOID()).toString()));
        return httpRequests;
    }

    /**
     * 
     * ---获取CPU空闲率
     * 
     * @param target
     * @return
     */
    public SystemCPUUsage getSystemCPUUsage() {
        SystemCPUUsage systemCPUUsage = new SystemCPUUsage();
        systemCPUUsage.setSysStatTmTotalCycles(Long.parseLong(result.get(MibF5.sysStatTmTotalCycles.getOID())
            .toString()));
        systemCPUUsage
            .setSysStatTmIdleCycles(Long.parseLong(result.get(MibF5.sysStatTmIdleCycles.getOID()).toString()));
        systemCPUUsage.setSysStatTmSleepCycles(Long.parseLong(result.get(MibF5.sysStatTmSleepCycles.getOID())
            .toString()));

        return systemCPUUsage;
    }

    /**
     * 
     * ---获取缓存利用情况
     * 
     * @return
     */
    public RamCacheUtilization getRamCacheUtilization() {
        RamCacheUtilization ramCacheUtilization = new RamCacheUtilization();
        Long sysHttpStatRamcacheHits = Long.parseLong(result.get(MibF5.sysHttpStatRamcacheHits.getOID()).toString());
        Long sysHttpStatRamcacheMisses = Long
            .parseLong(result.get(MibF5.sysHttpStatRamcacheMisses.getOID()).toString());
        Long sysHttpStatRamcacheHitBytes = Long.parseLong(result.get(MibF5.sysHttpStatRamcacheHitBytes.getOID())
            .toString());
        Long sysHttpStatRamcacheMissBytes = Long.parseLong(result.get(MibF5.sysHttpStatRamcacheMissBytes.getOID())
            .toString());
        Long sysHttpStatRamcacheEvictions = Long.parseLong(result.get(MibF5.sysHttpStatRamcacheEvictions.getOID())
            .toString());
        Float hitRate = (sysHttpStatRamcacheHits + sysHttpStatRamcacheMisses) == 0 ? 0 : ((float) sysHttpStatRamcacheHits
                / (sysHttpStatRamcacheHits + sysHttpStatRamcacheMisses) * 100);
        Float byteRate = (sysHttpStatRamcacheHitBytes + sysHttpStatRamcacheMissBytes) == 0 ? 0 : ((float) sysHttpStatRamcacheHitBytes
                / (sysHttpStatRamcacheHitBytes + sysHttpStatRamcacheMissBytes) * 100);
        Float evictionRate = (sysHttpStatRamcacheHits + sysHttpStatRamcacheMisses) == 0 ? 0 : ((float) sysHttpStatRamcacheEvictions
                / (sysHttpStatRamcacheHits + sysHttpStatRamcacheMisses) * 100);
        ramCacheUtilization.setHitRate(hitRate);
        ramCacheUtilization.setByteRate(byteRate);
        ramCacheUtilization.setEvictionRate(evictionRate);
        return ramCacheUtilization;
    }

    /**
     * 
     * SSLTransactions使用情况
     * 
     * @return
     */
    public SSLTransactions getSSLTransactions() {
        SSLTransactions sslTransactions = new SSLTransactions();
        sslTransactions.setClientsslStatTotNativeConns(Long.parseLong(result.get(
            MibF5.sysClientsslStatTotNativeConns.getOID()).toString()));
        sslTransactions.setClientsslStatTotCompatConns(Long.parseLong(result.get(
            MibF5.sysClientsslStatTotCompatConns.getOID()).toString()));
        return sslTransactions;
    }

    /**
     * ---获取主动网络连接数
     * 此处需要修改
     * @return
     */
    public ActiveNetworkAccessConnections getActiveNetworkAccessConnections() {
        ActiveNetworkAccessConnections activeNetworkAccessConnections = new ActiveNetworkAccessConnections();
        activeNetworkAccessConnections.setNaConns(Long.parseLong(result.get(
            MibF5.apmAccessStatCurrentActiveSessions.getOID()).toString()));
        return activeNetworkAccessConnections;
    }

    public NewNetworkAccessConnections getNetworkAccessConnections() {
        NewNetworkAccessConnections newNetworkAccessConnections = new NewNetworkAccessConnections();
        newNetworkAccessConnections.setNewNaConns(Float.parseFloat(result.get(
            MibF5.apmAccessStatCurrentPendingSessions.getOID()).toString()));
        return newNetworkAccessConnections;
    }

    public NetworkAccessThrought getNetworkAccessThrought() {
        NetworkAccessThrought networkAccessThrought = new NetworkAccessThrought();
        networkAccessThrought.setFromClient(Float.parseFloat(result.get(
            MibF5.apmGlobalConnectivityStatIngressRaw.getOID()).toString()));
        networkAccessThrought.setFromClientCompressed(Float.parseFloat(result.get(
            MibF5.apmGlobalConnectivityStatIngressCompressed.getOID()).toString()));
        networkAccessThrought.setToClient(Float.parseFloat(result
            .get(MibF5.apmGlobalConnectivityStatEgressRaw.getOID()).toString()));
        networkAccessThrought.setToClientCompressed(Float.parseFloat(result.get(
            MibF5.apmGlobalConnectivityStatEgressCompressed.getOID()).toString()));
        return networkAccessThrought;
    }

    public RewriteTransactions getRewriteTransactions() {
        RewriteTransactions rewriteTransactions = new RewriteTransactions();
        rewriteTransactions.setFromClient(Float.parseFloat(result
            .get(MibF5.apmGlobalRewriteStatClientReqBytes.getOID()).toString()));
        rewriteTransactions.setFromServer(Float.parseFloat(result
            .get(MibF5.apmGlobalRewriteStatServerReqBytes.getOID()).toString()));
        rewriteTransactions.setToClient(Float.parseFloat(result.get(MibF5.apmGlobalRewriteStatClientRespBytes.getOID())
            .toString()));
        rewriteTransactions.setToServer(Float.parseFloat(result.get(MibF5.apmGlobalRewriteStatServerRespBytes.getOID())
            .toString()));
        return rewriteTransactions;
    }

    public RewriteTransactionData getRewriteTransactionData() {
        RewriteTransactionData rewriteTransactionData = new RewriteTransactionData();
        rewriteTransactionData.setFromClient(Float.parseFloat(result.get(MibF5.apmGlobalRewriteStatClientReqs.getOID())
            .toString()));
        rewriteTransactionData.setFromServer(Float.parseFloat(result.get(MibF5.apmGlobalRewriteStatServerReqs.getOID())
            .toString()));
        rewriteTransactionData.setToClient(Float.parseFloat(result.get(MibF5.apmGlobalRewriteStatClientResps.getOID())
            .toString()));
        rewriteTransactionData.setToServer(Float.parseFloat(result.get(MibF5.apmGlobalRewriteStatServerResps.getOID())
            .toString()));
        return rewriteTransactionData;

    }

    /**
     * 生成F5性能的json对象
     * 
     * @param snmpTarget
     * @return
     * @throws IOException
     */
    public String toJSONString() {
        MemoryUsed memoryUsed = getMemoryUsed();
        JSONObject memoryUsedJsonName = new JSONObject();
        JSONObject memoryUsedJsonObject = new JSONObject();
        JSONArray memoryUsedJsonArray = new JSONArray();
        memoryUsedJsonObject.put("Total Phys Memory", memoryUsed.getTotalPhysMemory());
        memoryUsedJsonArray.add(memoryUsedJsonObject);
        memoryUsedJsonObject = new JSONObject();
        memoryUsedJsonObject.put("OS Used Memory", memoryUsed.getoSUsedMemory());
        memoryUsedJsonArray.add(memoryUsedJsonObject);
        memoryUsedJsonObject = new JSONObject();
        memoryUsedJsonObject.put("TMM Alloc Memory", memoryUsed.gettMMAllocMemory());
        memoryUsedJsonArray.add(memoryUsedJsonObject);
        memoryUsedJsonObject = new JSONObject();
        memoryUsedJsonObject.put("TMM Used Memory", memoryUsed.gettMMUsedMemory());
        memoryUsedJsonArray.add(memoryUsedJsonObject);
        memoryUsedJsonName.put("name", "Memory Used");
        memoryUsedJsonName.put("content", memoryUsedJsonArray);
        SystemCPUUsage systemCPUUsage = getSystemCPUUsage();
        JSONObject systemCPUUsageJsonName = new JSONObject();
        JSONObject systemCPUUsageJsonObject = new JSONObject();
        JSONArray systemCPUUsageJsonArray = new JSONArray();
        systemCPUUsageJsonObject.put("sysStatTmTotalCycles", systemCPUUsage.getSysStatTmTotalCycles());
        systemCPUUsageJsonArray.add(systemCPUUsageJsonObject);
        systemCPUUsageJsonObject = new JSONObject();
        systemCPUUsageJsonObject.put("sysStatTmIdleCycles", systemCPUUsage.getSysStatTmIdleCycles());
        systemCPUUsageJsonArray.add(systemCPUUsageJsonObject);
        systemCPUUsageJsonObject = new JSONObject();
        systemCPUUsageJsonObject.put("sysStatTmSleepCycles", systemCPUUsage.getSysStatTmSleepCycles());
        systemCPUUsageJsonArray.add(systemCPUUsageJsonObject);
        systemCPUUsageJsonName.put("name", "System CPU Usage");
        systemCPUUsageJsonName.put("content", systemCPUUsageJsonArray);
        ActiveConnections activeConnections = getActiveConnections();
        JSONObject activeConnectionsJsonName = new JSONObject();
        JSONObject activeConnectionsJsonObject = new JSONObject();
        JSONArray activeConnectionsJsonArray = new JSONArray();
        activeConnectionsJsonObject.put("Active Connections", activeConnections.getActiveConnections());
        activeConnectionsJsonArray.add(activeConnectionsJsonObject);
        activeConnectionsJsonName.put("name", "Active Connections");
        activeConnectionsJsonName.put("content", activeConnectionsJsonArray);
        NewConnections newConnections = getNewConnections();
        JSONObject newConnectionsJsonName = new JSONObject();
        JSONObject newConnectionsJsonObject = new JSONObject();
        JSONArray newConnectionsJsonArray = new JSONArray();
        newConnectionsJsonObject.put("Client Accepts", newConnections.getClientAccepts());
        newConnectionsJsonArray.add(newConnectionsJsonObject);
        newConnectionsJsonObject = new JSONObject();
        newConnectionsJsonObject.put("Server Connects", newConnections.getServerConnects());
        newConnectionsJsonArray.add(newConnectionsJsonObject);
        newConnectionsJsonName.put("name", "New Connections");
        newConnectionsJsonName.put("content", newConnectionsJsonArray);
        ThroughputWithBits throughputWithBits = getThroughputWithBits();
        JSONObject throughtputWithBitsJsonName = new JSONObject();
        JSONObject throughtputWithBitsJsonObject = new JSONObject();
        JSONArray throughtputWithBitsJsonArray = new JSONArray();
        throughtputWithBitsJsonObject.put("sysStatClientBytesIn", throughputWithBits.getSysStatClientBytesIn());
        throughtputWithBitsJsonArray.add(throughtputWithBitsJsonObject);
        throughtputWithBitsJsonObject = new JSONObject();
        throughtputWithBitsJsonObject.put("sysStatClientBytesOut", throughputWithBits.getSysStatClientBytesOut());
        throughtputWithBitsJsonArray.add(throughtputWithBitsJsonObject);
        throughtputWithBitsJsonObject = new JSONObject();
        throughtputWithBitsJsonObject.put("sysStatServerBytesIn", throughputWithBits.getSysStatServerBytesIn());
        throughtputWithBitsJsonArray.add(throughtputWithBitsJsonObject);
        throughtputWithBitsJsonObject = new JSONObject();
        throughtputWithBitsJsonObject.put("sysStatServerBytesOut", throughputWithBits.getSysStatServerBytesOut());
        throughtputWithBitsJsonArray.add(throughtputWithBitsJsonObject);
        throughtputWithBitsJsonObject = new JSONObject();
        throughtputWithBitsJsonObject.put("sysHttpStatPrecompresssBytes",
            throughputWithBits.getSysHttpStatPrecompresssBytes());
        throughtputWithBitsJsonArray.add(throughtputWithBitsJsonObject);

        throughtputWithBitsJsonName.put("name", "Throughput(bits)");
        throughtputWithBitsJsonName.put("content", throughtputWithBitsJsonArray);
        ThroughputWithPackets throughputWithPackets = getThroughputWithPackets();
        JSONObject throughputWithPacketsJsonName = new JSONObject();
        JSONObject throughputWithPacketsJsonObject = new JSONObject();
        JSONArray throughputWithPacketsJsonArray = new JSONArray();
        throughputWithPacketsJsonObject.put("sysStatClientPktsIn", throughputWithPackets.getSysStatClientPktsIn());
        throughputWithPacketsJsonArray.add(throughputWithPacketsJsonObject);
        throughputWithPacketsJsonObject = new JSONObject();
        throughputWithPacketsJsonObject.put("sysStatClientPktsOut", throughputWithPackets.getSysStatClientPktsOut());
        throughputWithPacketsJsonArray.add(throughputWithPacketsJsonObject);
        throughputWithPacketsJsonObject = new JSONObject();
        throughputWithPacketsJsonObject.put("sysStatServerPktsIn", throughputWithPackets.getSysStatServerPktsIn());
        throughputWithPacketsJsonArray.add(throughputWithPacketsJsonObject);
        throughputWithPacketsJsonObject = new JSONObject();
        throughputWithPacketsJsonObject.put("sysStatServerPktsOut", throughputWithPackets.getSysStatServerPktsOut());
        throughputWithPacketsJsonArray.add(throughputWithPacketsJsonObject);
        throughputWithPacketsJsonName.put("name", "Throughput(packets)");
        throughputWithPacketsJsonName.put("content", throughputWithPacketsJsonArray);
        HTTPRequests httpRequests = getHttpRequests();
        JSONObject httpRequestJsonName = new JSONObject();
        JSONObject httpRequestJsonObject = new JSONObject();
        JSONArray httpRequestJsonArray = new JSONArray();
        httpRequestJsonObject.put("HTTP Requests", httpRequests.getHttpRequests());
        httpRequestJsonArray.add(httpRequestJsonObject);
        httpRequestJsonName.put("name", "HTTP Requests");
        httpRequestJsonName.put("content", httpRequestJsonArray);

        // 缓存使用情况
        RamCacheUtilization ramCacheUtilization = getRamCacheUtilization();
        JSONObject ramCacheUtilizationJsonName = new JSONObject();
        JSONObject ramCacheUtilizationJsonObject = new JSONObject();
        JSONArray ramCacheUtilizationJsonArray = new JSONArray();
        ramCacheUtilizationJsonObject.put("Hit Rate", ramCacheUtilization.getHitRate());
        ramCacheUtilizationJsonArray.add(ramCacheUtilizationJsonObject);
        ramCacheUtilizationJsonObject = new JSONObject();
        ramCacheUtilizationJsonObject.put("Byte Rate", ramCacheUtilization.getByteRate());
        ramCacheUtilizationJsonArray.add(ramCacheUtilizationJsonObject);
        ramCacheUtilizationJsonObject = new JSONObject();
        ramCacheUtilizationJsonObject.put("Eviction Rate", ramCacheUtilization.getEvictionRate());
        ramCacheUtilizationJsonArray.add(ramCacheUtilizationJsonObject);
        ramCacheUtilizationJsonName.put("name", "RAM Cache Utilization");
        ramCacheUtilizationJsonName.put("content", ramCacheUtilizationJsonArray);
        // SSL吞吐量使用情况
        SSLTransactions sslTransactions = getSSLTransactions();
        JSONObject sslTransactionsJsonName = new JSONObject();
        JSONObject sslTransactionsJsonObject = new JSONObject();
        JSONArray sslTransactionsJsonArray = new JSONArray();
        sslTransactionsJsonObject.put("sysClientsslStatTotNativeConns",
            sslTransactions.getClientsslStatTotNativeConns());
        sslTransactionsJsonArray.add(sslTransactionsJsonObject);
        sslTransactionsJsonObject = new JSONObject();
        sslTransactionsJsonObject.put("sysClientsslStatTotCompatConns",
            sslTransactions.getClientsslStatTotCompatConns());
        sslTransactionsJsonArray.add(sslTransactionsJsonObject);
        sslTransactionsJsonName.put("name", "SSL Transactions");
        sslTransactionsJsonName.put("content", sslTransactionsJsonArray);
        ActiveNetworkAccessConnections activeNetworkAccessConnections = getActiveNetworkAccessConnections();
        JSONObject activeNetworkAccessConnectionsJsonName = new JSONObject();
        JSONObject activeNetworkAccessConnectionsJsonObjec = new JSONObject();
        JSONArray activeNetworkAccessConnectionsJSONArray = new JSONArray();
        activeNetworkAccessConnectionsJsonObjec.put("naConns", activeNetworkAccessConnections.getNaConns());
        activeNetworkAccessConnectionsJSONArray.add(activeNetworkAccessConnectionsJsonObjec);
        activeNetworkAccessConnectionsJsonName.put("name", "Active Network Access Connections");
        activeNetworkAccessConnectionsJsonName.put("content", activeNetworkAccessConnectionsJSONArray);
        NewNetworkAccessConnections newNetworkAccessConnections = getNetworkAccessConnections();
        JSONObject newNetworkAccessConnectionsJsonName = new JSONObject();
        JSONObject newNetworkAccessConnectionsJsonObject = new JSONObject();
        JSONArray newNetworkAccessConnectionsJSONArray = new JSONArray();
        newNetworkAccessConnectionsJsonObject.put("newNaConns", newNetworkAccessConnections.getNewNaConns());
        newNetworkAccessConnectionsJSONArray.add(newNetworkAccessConnectionsJsonObject);
        newNetworkAccessConnectionsJsonName.put("name", "New Network Access Connections");
        newNetworkAccessConnectionsJsonName.put("content", newNetworkAccessConnectionsJSONArray);
        NetworkAccessThrought networkAccessThrought = getNetworkAccessThrought();
        JSONObject networkAccessThroughtJsonName = new JSONObject();
        JSONObject networkAccessThroughtJsonObject = new JSONObject();
        JSONArray networkAccessThroughtJSONArray = new JSONArray();
        networkAccessThroughtJsonObject.put("fromClient", networkAccessThrought.getFromClient());
        networkAccessThroughtJSONArray.add(networkAccessThroughtJsonObject);
        networkAccessThroughtJsonObject = new JSONObject();
        networkAccessThroughtJsonObject.put("fromClientComPressed", networkAccessThrought.getFromClientCompressed());
        networkAccessThroughtJSONArray.add(networkAccessThroughtJsonObject);
        networkAccessThroughtJsonObject = new JSONObject();
        networkAccessThroughtJsonObject.put("toClient", networkAccessThrought.getToClient());
        networkAccessThroughtJSONArray.add(networkAccessThroughtJsonObject);
        networkAccessThroughtJsonObject = new JSONObject();
        networkAccessThroughtJsonObject.put("toClientComPressed", networkAccessThrought.getToClientCompressed());
        networkAccessThroughtJSONArray.add(networkAccessThroughtJsonObject);
        networkAccessThroughtJsonName.put("name", "Network Access Throught");
        networkAccessThroughtJsonName.put("content", networkAccessThroughtJSONArray);
        RewriteTransactions rewriteTransactions = getRewriteTransactions();
        JSONObject rewriteTransactionsJsonName = new JSONObject();
        JSONObject rewriteTransactionsJsonObject = new JSONObject();
        JSONArray rewriteTransactionsJSONArray = new JSONArray();
        rewriteTransactionsJsonObject.put("fromClient", rewriteTransactions.getFromClient());
        rewriteTransactionsJSONArray.add(rewriteTransactionsJsonObject);
        rewriteTransactionsJsonObject = new JSONObject();
        rewriteTransactionsJsonObject.put("toClient", rewriteTransactions.getToClient());
        rewriteTransactionsJSONArray.add(rewriteTransactionsJsonObject);
        rewriteTransactionsJsonObject = new JSONObject();
        rewriteTransactionsJsonObject.put("toServer", rewriteTransactions.getToServer());
        rewriteTransactionsJSONArray.add(rewriteTransactionsJsonObject);
        rewriteTransactionsJsonObject = new JSONObject();
        rewriteTransactionsJsonObject.put("fromServer", rewriteTransactions.getFromServer());
        rewriteTransactionsJSONArray.add(rewriteTransactionsJsonObject);
        rewriteTransactionsJsonName.put("name", "Rewrite Transactions");
        rewriteTransactionsJsonName.put("content", rewriteTransactionsJSONArray);
        RewriteTransactionData rewriteTransactionData = getRewriteTransactionData();
        JSONObject rewriteTransactionDataJsonName = new JSONObject();
        JSONObject rewriteTransactionDataJsonObject = new JSONObject();
        JSONArray rewriteTransactionDataJSONArray = new JSONArray();
        rewriteTransactionDataJsonObject.put("fromClient", rewriteTransactionData.getFromClient());
        rewriteTransactionDataJSONArray.add(rewriteTransactionDataJsonObject);
        rewriteTransactionDataJsonObject = new JSONObject();
        rewriteTransactionDataJsonObject.put("toClient", rewriteTransactionData.getToClient());
        rewriteTransactionDataJSONArray.add(rewriteTransactionDataJsonObject);
        rewriteTransactionDataJsonObject = new JSONObject();
        rewriteTransactionDataJsonObject.put("toServer", rewriteTransactionData.getToServer());
        rewriteTransactionDataJSONArray.add(rewriteTransactionDataJsonObject);
        rewriteTransactionDataJsonObject = new JSONObject();
        rewriteTransactionDataJsonObject.put("fromServer", rewriteTransactionData.getFromServer());
        rewriteTransactionDataJSONArray.add(rewriteTransactionsJsonObject);
        rewriteTransactionDataJsonName.put("name", "Rewrite Transaction Data");
        rewriteTransactionDataJsonName.put("content", rewriteTransactionDataJSONArray);

        JSONArray workstationStatusJsonArray = new JSONArray();
        workstationStatusJsonArray.add(memoryUsedJsonName);
        workstationStatusJsonArray.add(systemCPUUsageJsonName);
        workstationStatusJsonArray.add(activeConnectionsJsonName);
        workstationStatusJsonArray.add(newConnectionsJsonName);
        workstationStatusJsonArray.add(throughtputWithBitsJsonName);
        workstationStatusJsonArray.add(throughputWithPacketsJsonName);
        workstationStatusJsonArray.add(httpRequestJsonName);
        workstationStatusJsonArray.add(ramCacheUtilizationJsonName);
        workstationStatusJsonArray.add(sslTransactionsJsonName);
        workstationStatusJsonArray.add(activeNetworkAccessConnectionsJsonName);
        workstationStatusJsonArray.add(newNetworkAccessConnectionsJsonName);
        workstationStatusJsonArray.add(networkAccessThroughtJsonName);
        workstationStatusJsonArray.add(rewriteTransactionsJsonName);
        workstationStatusJsonArray.add(rewriteTransactionDataJsonName);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("WorkstationStatus", workstationStatusJsonArray);
        return jsonObject.toString();
    }
}
