package pw.cdmi.om.protocol.jmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.core.om.utils.TimeUtils;
import pw.cdmi.om.protocol.ssh.SSHTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JMXUtils {
    private static Logger logger = LoggerFactory.getLogger(JMXUtils.class);

    private JSch jsch;

    private Session session;

    private MBeanServerConnection connection;

    private JMXConnector connector;

    private ObjectName service;

    private String descriptionMain = "";

    private String healthMain = "good";

    // 初始化DomainMBean实体对象

    public JMXUtils() {
        try {
            service = new ObjectName(
                "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
        } catch (MalformedObjectNameException e) {
            logger.error("创建JMXClient失败,原因：" + e.getMessage());
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * 实例化与 Domain Runtime MBean Server 的连接。
     * @throws MalformedURLException 
     */
    public void connection(JMXTarget target) throws IOException {
        close();
        logger.info("开始连接weblogic的domain域。。。");
        String proto = "t3";
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";
        JMXServiceURL serviceURL = new JMXServiceURL(proto, target.getHost(), target.getPort(), jndiroot + mserver);
        Hashtable<String, String> environment = new Hashtable<String, String>();
        environment.put(Context.SECURITY_PRINCIPAL, target.getUsername());
        environment.put(Context.SECURITY_CREDENTIALS, target.getPassword());
        environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
        connector = JMXConnectorFactory.connect(serviceURL, environment);
        connection = connector.getMBeanServerConnection();
        logger.info("成功连接weblogic的domain域！！！");
    }

    /**
     * 关闭与Domain Runtime MBean Server的连接
     * @throws IOException 
     */
    public void close() throws IOException {
        if (connector != null) {
            connector.close();
        }
    }

    /**
     * 从weblogic.health.HealthState的字符串中提取HealthState的属性值
     * 
     * @param str
     * @return
     */
    public String extractionHealthState(String str) {
        String health = "";
        String[] strArray = str.split(",");
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i].contains("State:")) {
                health = strArray[i].substring(6);
                break;
            }
        }
        return health;
    }

    /**
     * 合并服务器及其下属组件的所有健康状况
     * 
     * @param healths 健康状况数组
     * @return 返回合并后的健康状况字符串
     */
    private String integrateServerHealthState(String... healths) {
        String healthState = "HEALTH_OK";
        for (String health : healths) {
            if (!"".equals(health) && !"HEALTH_OK".equals(health)) {
                if ("HEALTH_WARN".equals(health)) {
                    if ("HEALTH_OK".equals(healthState)) {
                        healthState = "HEALTH_WARN";
                    }
                } else {
                    healthState = health;
                }

            }
        }
        return healthState;
    }

    /**
     * 获取Server Runtime MBean
     * 
     * @return
     * @throws Exception
     */
    private ObjectName[] getServerRuntimes() throws Exception {
        return (ObjectName[]) connection.getAttribute(service, "ServerRuntimes");
    }

    /**
     * 获取weblogic的服务器的信息
     * 
     * @return
     * @throws Exception
     */
    public JSONArray getServerInfo() throws Exception {
        logger.info("开始获取weblogic的server的数据。。。");
        JSONArray jsonArray = new JSONArray();
        List<String> serverNames = new ArrayList<String>();
        String errorDescription = "";
        String status = "good";
        ObjectName[] serverRT = getServerRuntimes();
        // 获取运行状态下的服务器信息
        for (int i = 0; i < serverRT.length; i++) {
            JSONObject jsonObject = new JSONObject();
            String serverHealth = extractionHealthState(String.valueOf(connection.getAttribute(serverRT[i],
                "HealthState")));
            String name = String.valueOf(connection.getAttribute(serverRT[i], "Name"));
            serverNames.add(name);
            JSONObject jvmInfo = getJVMInfo(name, serverRT[i]);
            JSONObject threadInfo = getThreadPoolInfo(name, serverRT[i]);
            JSONObject jmsInfo = getJMSInfo(name, serverRT[i]);
            JSONObject jdbcInfo = getJDBCServerInfo(name, serverRT[i]);

            String healthState = integrateServerHealthState(serverHealth, jvmInfo.getString("health"),
                threadInfo.getString("health"), jmsInfo.getString("health"), jdbcInfo.getString("health"));
            jsonObject.put("serverName", name);
            jsonObject.put("serverState", String.valueOf(connection.getAttribute(serverRT[i], "State")));
            jsonObject.put("healthState", healthState);
            jsonObject.put(
                "activationTime",
                TimeUtils.formatTime(
                    new Date(Long.parseLong(String.valueOf(connection.getAttribute(serverRT[i], "ActivationTime")))))
                    .toString());
            // jsonObject.put("currentDirectory", String.valueOf(connection.getAttribute(serverRT[i],
            // "CurrentDirectory"))
            // .replace("\\", "\\\\"));
            jsonObject.put("weblogicVersion", String.valueOf(connection.getAttribute(serverRT[i], "WeblogicVersion")));
            jsonObject.put("listenAddress", String.valueOf(connection.getAttribute(serverRT[i], "ListenAddress")));
            jsonObject.put("listenPort",
                Integer.parseInt(String.valueOf(connection.getAttribute(serverRT[i], "ListenPort"))));
            jsonObject.put("jvmInfo", jvmInfo.getJSONObject("jvmData"));
            jsonObject.put("threadPool", threadInfo.getJSONObject("threadData"));
            jsonObject.put("jmsInfo", jmsInfo.getJSONObject("jmsData"));
            jsonObject.put("security", getSecurityInfo(name, serverRT[i]));
            jsonObject.put("jdbcInfo", jdbcInfo.getJSONArray("jdbcData"));

            // 将json对象添加到json数组
            jsonArray.add(jsonObject);
            if ("HEALTH_OK".equals(healthState)) {
                logger.info("服务器：" + name + "运行正常！");
            } else if ("HEALTH_WARN".equals(healthState)) {
                if ("good".equals(status)) {
                    status = "warning";
                }
                errorDescription += "[警告信息：服务器“" + name + "”上发生警告！] ";
            } else {
                status = "fatal";
                errorDescription += "[异常信息：服务器“" + name + "”上发生异常！] ";
            }
        }

        // 获取非运行状态下的服务器信息
        ObjectName domainRuntime = (ObjectName) connection.getAttribute(service, "DomainRuntime");
        ObjectName[] serverLifeCycleRuntimes = (ObjectName[]) connection.getAttribute(domainRuntime,
            "ServerLifeCycleRuntimes");
        for (ObjectName serverLifeCycleRuntime : serverLifeCycleRuntimes) {
            String name = String.valueOf(connection.getAttribute(serverLifeCycleRuntime, "Name"));
            if (!serverNames.contains(name)) {
                JSONObject jsonObject = new JSONObject();
                String state = String.valueOf(connection.getAttribute(serverLifeCycleRuntime, "State"));
                jsonObject.put("serverState", state);
                jsonObject.put("serverName", name);
                jsonObject.put("healthState", "");
                jsonArray.add(jsonObject);
            }
        }

        integrateStatusAndDescription(status, errorDescription);
        logger.info("获取weblogic的server数据完成！！！");
        return jsonArray;
    }

    /**
     * 获取weblogic每个服务器上jvm的信息
     * 
     * @param serverName
     * @param serverObjectName
     * @return
     * @throws Exception
     */
    private JSONObject getJVMInfo(String serverName, ObjectName serverObjectName) throws Exception {
        logger.info("开始获取服务器“" + serverName + "”的JVM的数据。。。");
        JSONObject jsonObject = new JSONObject();
        String health = "HEALTH_OK";
        ObjectName jvmRuntime = (ObjectName) connection.getAttribute(serverObjectName, "JVMRuntime");
        JSONObject jvmData = new JSONObject();
        Integer heapFreePercent = Integer.parseInt(String.valueOf(connection
            .getAttribute(jvmRuntime, "HeapFreePercent")));
        Long jvmFree = Long.parseLong(String.valueOf(connection.getAttribute(jvmRuntime, "HeapFreeCurrent")));
        Long jvmSize = Long.parseLong(String.valueOf(connection.getAttribute(jvmRuntime, "HeapSizeCurrent")));
        Long heapSizeMax = Long.parseLong(String.valueOf(connection.getAttribute(jvmRuntime, "HeapSizeMax")));
        jvmData.put("jvmFree", getBytesToMBytes(jvmFree) + " MB");
        jvmData.put("jvmSize", getBytesToMBytes(jvmSize) + " MB");
        jvmData.put("heapFreePercent", heapFreePercent + "%");
        jvmData.put("heapSizeMax", getBytesToMBytes(heapSizeMax) + " MB");
        jvmData.put("javaVendor", String.valueOf(connection.getAttribute(jvmRuntime, "JavaVendor")));
        jvmData.put("javaVersion", String.valueOf(connection.getAttribute(jvmRuntime, "JavaVersion")));
        jvmData.put("javaVMVendor", String.valueOf(connection.getAttribute(jvmRuntime, "JavaVMVendor")));
        jvmData.put("osName", String.valueOf(connection.getAttribute(jvmRuntime, "OSName")));
        jvmData.put("osVersion", String.valueOf(connection.getAttribute(jvmRuntime, "OSVersion")));
        if (heapFreePercent < 10) {
            String status = "warning";
            health = "HEALTH_WARN";
            String description = "[警告信息：服务器“" + serverName + "”的JVM堆的空闲不足！] ";
            integrateStatusAndDescription(status, description);
        }
        jsonObject.put("jvmData", jvmData);
        jsonObject.put("health", health);
        logger.info("获取服务器“" + serverName + "”的JVM数据完成！！！");
        return jsonObject;
    }

    /**
     * 将bytes数据计算为Mbytes的数据，并保留两位小数
     * 
     * @param size
     * @return
     */
    private String getBytesToMBytes(Long size) {
        Double mbSize = (double) (size / 1024) / 1024;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(mbSize);
    }

    /**
     * 获取weblogic每个服务器上线程池的信息
     * 
     * @param serverName
     * @param serverObjectName
     * @return
     * @throws Exception
     */
    private JSONObject getThreadPoolInfo(String serverName, ObjectName serverObjectName) throws Exception {
        logger.info("开始获取服务器“" + serverName + "”的线程池的数据。。。");
        JSONObject jsonObject = new JSONObject();
        String status = "good";
        String description = "";
        ObjectName threadRuntime = (ObjectName) connection.getAttribute(serverObjectName, "ThreadPoolRuntime");
        String health = extractionHealthState(String.valueOf(connection.getAttribute(threadRuntime, "HealthState")));
        JSONObject threadData = new JSONObject();
        threadData.put("idleThread",
            Integer.parseInt(String.valueOf(connection.getAttribute(threadRuntime, "ExecuteThreadIdleCount"))));
        threadData.put("totalThread",
            Integer.parseInt(String.valueOf(connection.getAttribute(threadRuntime, "ExecuteThreadTotalCount"))));
        threadData.put("pendingUserRequest",
            Integer.parseInt(String.valueOf(connection.getAttribute(threadRuntime, "PendingUserRequestCount"))));
        threadData.put("throughput",
            Double.parseDouble(String.valueOf(connection.getAttribute(threadRuntime, "Throughput"))));
        threadData.put("queueLength",
            Integer.parseInt(String.valueOf(connection.getAttribute(threadRuntime, "QueueLength"))));
        threadData.put("hoggingThread",
            Integer.parseInt(String.valueOf(connection.getAttribute(threadRuntime, "HoggingThreadCount"))));
        // threadData.put("stuckThread",
        // Integer.parseInt(String.valueOf(connection.getAttribute(threadRuntime, "StuckThreadCount"))));
        threadData.put("completedRequest",
            Long.parseLong(String.valueOf(connection.getAttribute(threadRuntime, "CompletedRequestCount"))));
        threadData.put("standbyThreadCount",
            Integer.parseInt(String.valueOf(connection.getAttribute(threadRuntime, "StandbyThreadCount"))));
        threadData.put("healthState", health);
        // threadData.put("activeThread", connection.getAttribute(threadRuntime, "ExecuteThreads"));
        if ("HEALTH_WARN".equals(health)) {
            status = "warning";
            description = "[警告信息：服务器“" + serverName + "”的线程池运行时发生警告！] ";
        } else if (!"HEALTH_OK".equals(health)) {
            status = "fatal";
            description = "[异常信息：服务器“" + serverName + "”的线程池运行时发生异常！] ";
        }
        integrateStatusAndDescription(status, description);
        jsonObject.put("threadData", threadData);
        jsonObject.put("health", health);
        logger.info("获取服务器“" + serverName + "”的线程池数据完成！！！");
        return jsonObject;
    }

    /**
     * 获取weblogic每个服务器上JMS的信息
     * 
     * @param serverName
     * @param serverObjectName
     * @return
     * @throws Exception
     */
    private JSONObject getJMSInfo(String serverName, ObjectName serverObjectName) throws Exception {
        logger.info("开始获取服务器“" + serverName + "”的JMS的数据。。。");
        JSONObject jsonObject = new JSONObject();
        String status = "good";
        String description = "";
        ObjectName jmsRumtime = (ObjectName) connection.getAttribute(serverObjectName, "JMSRuntime");
        JSONObject jmsData = new JSONObject();
        String health = extractionHealthState(String.valueOf(connection.getAttribute(jmsRumtime, "HealthState")));
        jmsData.put("connectionsCurrentCount",
            Long.parseLong(String.valueOf(connection.getAttribute(jmsRumtime, "ConnectionsCurrentCount"))));
        jmsData.put("connectionsHighCount",
            Long.parseLong(String.valueOf(connection.getAttribute(jmsRumtime, "ConnectionsHighCount"))));
        jmsData.put("connectionsTotalCount",
            Long.parseLong(String.valueOf(connection.getAttribute(jmsRumtime, "ConnectionsTotalCount"))));
        jmsData.put("jmsServersCurrentCount",
            Long.parseLong(String.valueOf(connection.getAttribute(jmsRumtime, "JMSServersCurrentCount"))));
        jmsData.put("jmsServersHighCount",
            Long.parseLong(String.valueOf(connection.getAttribute(jmsRumtime, "JMSServersHighCount"))));
        jmsData.put("jmsServersTotalCount",
            Long.parseLong(String.valueOf(connection.getAttribute(jmsRumtime, "JMSServersTotalCount"))));
        jmsData.put("healthState", health);
        if ("HEALTH_WARN".equals(health)) {
            status = "warning";
            description = "[警告信息：服务器“" + serverName + "”的JMS运行时发生警告！] ";
        } else if (!"HEALTH_OK".equals(health)) {
            status = "fatal";
            description = "[异常信息：服务器“" + serverName + "”的JMS运行时发生异常！] ";
        }
        integrateStatusAndDescription(status, description);
        jsonObject.put("jmsData", jmsData);
        jsonObject.put("health", health);
        logger.info("获取服务器“" + serverName + "”的JMS数据完成！！！");
        return jsonObject;
    }

    /**
     * 获取weblogic每个服务器用户及登录的安全信息
     * 
     * @param serverName
     * @param serverObjectName
     * @return
     * @throws Exception
     */
    private JSONObject getSecurityInfo(String serverName, ObjectName serverObjectName) throws Exception {
        logger.info("开始获取服务器“" + serverName + "”的登录及安全的数据。。。");
        ObjectName securityRuntime = (ObjectName) connection.getAttribute(serverObjectName, "ServerSecurityRuntime");
        String jaccEnabled = String.valueOf(connection.getAttribute(securityRuntime, "JACCEnabled"));
        ObjectName defaultRealm = (ObjectName) connection.getAttribute(securityRuntime, "DefaultRealmRuntime");
        ObjectName userLockoutManager = (ObjectName) connection.getAttribute(defaultRealm, "UserLockoutManagerRuntime");
        JSONObject securityData = new JSONObject();
        securityData.put("jaccEnabled", jaccEnabled);
        securityData.put("invalidLoginAttemptsTotalCount", Long.parseLong(String.valueOf(connection.getAttribute(
            userLockoutManager, "InvalidLoginAttemptsTotalCount"))));
        securityData.put("invalidLoginUsersHighCount",
            Long.parseLong(String.valueOf(connection.getAttribute(userLockoutManager, "InvalidLoginUsersHighCount"))));
        securityData.put("lockedUsersCurrentCount",
            Long.parseLong(String.valueOf(connection.getAttribute(userLockoutManager, "LockedUsersCurrentCount"))));
        securityData.put("loginAttemptsWhileLockedTotalCount", Long.parseLong(String.valueOf(connection.getAttribute(
            userLockoutManager, "LoginAttemptsWhileLockedTotalCount"))));
        securityData.put("unlockedUsersTotalCount",
            Long.parseLong(String.valueOf(connection.getAttribute(userLockoutManager, "UnlockedUsersTotalCount"))));
        securityData.put("userLockoutTotalCount",
            Long.parseLong(String.valueOf(connection.getAttribute(userLockoutManager, "UserLockoutTotalCount"))));
        logger.info("获取服务器“" + serverName + "”的登录及安全的数据完成！！！");
        return securityData;
    }

    /**
     * 获取服务器中JDBC连接信息
     * 
     * @param serverName
     * @param serverObjectName
     * @return
     * @throws Exception
     */
    private JSONObject getJDBCServerInfo(String serverName, ObjectName serverObjectName) throws Exception {
        logger.info("开始获取服务器“" + serverName + "”的JDBC的数据。。。");
        JSONArray jsonArray = new JSONArray();
        String status = "good";
        String description = "";
        String health = "HEALTH_OK";
        ObjectName jdbcRumtime = (ObjectName) connection.getAttribute(serverObjectName, "JDBCServiceRuntime");
        String server = String.valueOf(connection.getAttribute(jdbcRumtime, "Name"));
        ObjectName[] dataSources = (ObjectName[]) connection.getAttribute(jdbcRumtime, "JDBCDataSourceRuntimeMBeans");
        for (int i = 0; i < dataSources.length; i++) {
            Integer leakedConnectionsCount = Integer.parseInt(String.valueOf(connection.getAttribute(dataSources[i],
                "LeakedConnectionCount")));
            String name = String.valueOf(connection.getAttribute(dataSources[i], "Name"));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("名称", name);
            jsonObject.put("服务器", server);
            jsonObject.put("状态", String.valueOf(connection.getAttribute(dataSources[i], "State")));
            // jsonObject.put("启用",
            // Boolean.parseBoolean(String.valueOf(connection.getAttribute(dataSources[i], "Enabled"))));
            jsonObject.put("等待连接当前计数", Integer.parseInt(String.valueOf(connection.getAttribute(dataSources[i],
                "WaitingForConnectionCurrentCount"))));
            jsonObject.put("泄露连接计数", leakedConnectionsCount);
            jsonObject.put("当前活动连接计数", Integer.parseInt(String.valueOf(connection.getAttribute(dataSources[i],
                "ActiveConnectionsCurrentCount"))));
            jsonObject
                .put("最大活动连接计数", Integer.parseInt(String.valueOf(connection.getAttribute(dataSources[i],
                    "ActiveConnectionsHighCount"))));
            if (leakedConnectionsCount > 0) {
                status = "warning";
                health = "HEALTH_WARN";
                description += "[告警信息：服务器“" + serverName + "”的名称为“" + name + "”的JDBC连接出现泄漏连接！] ";
            }
            jsonArray.add(i, jsonObject);
        }
        integrateStatusAndDescription(status, description);
        JSONObject jdbcData = new JSONObject();
        jdbcData.put("jdbcData", jsonArray);
        jdbcData.put("health", health);
        logger.info("获取服务器“" + serverName + "”的JDBC数据完成！！！");
        return jdbcData;
    }

    /**
     * 获取Domain上部署应用的信息
     * 
     * @return
     * @throws Exception
     */
    public JSONObject getDeploymentInfo() throws Exception {
        logger.info("开始获取部署应用的数据。。。");
        JSONObject deploymentInfo = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String status = "good";
        String description = "";
        ObjectName domain = (ObjectName) connection.getAttribute(service, "DomainConfiguration");
        ObjectName[] appDeployments = (ObjectName[]) connection.getAttribute(domain, "AppDeployments");
        for (ObjectName appDeployment : appDeployments) {
            JSONObject jsonObject = new JSONObject();
            String appName = String.valueOf(connection.getAttribute(appDeployment, "ApplicationName"));
            List<String> targetNames = getAppDeploymentTargetName(appName, appDeployment);
            String health = getApplicationRuntimeHealthState(appName);
            String state = getAppDeployState(appName, targetNames);
            jsonObject.put("appName", appName);
            jsonObject.put("deploymentOrder",
                Integer.parseInt(String.valueOf(connection.getAttribute(appDeployment, "DeploymentOrder"))));
            jsonObject.put("moduleType", String.valueOf(connection.getAttribute(appDeployment, "ModuleType")));
            // jsonObject.put("备注", String.valueOf(connection.getAttribute(appDeployment, "Notes")));
            // jsonObject.put("目标", targetName);
            jsonObject.put("health", health);
            jsonObject.put("state", state);
            if ("STATE_ACTIVE".equals(state)) {
                if ("HEALTH_WARN".equals(health) && "good".equals(status)) {
                    status = "warning";
                    description += "[警告信息：名称为“" + appName + "”的应用运行时发生告警！] ";
                } else if (!"HEALTH_OK".equals(health)) {
                    status = "fatal";
                    description += "[异常信息：名称为“" + appName + "”的应用运行异常！] ";
                }
            } else if ("STATE_FAILED".equals(state)) {
                status = "fatal";
                description += "[异常信息：名称为“" + appName + "”的应用在部署或启动时失败！] ";
            }

            jsonArray.add(jsonObject);
        }
        integrateStatusAndDescription(status, description);
        deploymentInfo.put("deployment", jsonArray);
        logger.info("获取部署应用数据完成！！！");
        return deploymentInfo;
    }

    /**
     * 获取应用的部署目标的信息
     * 
     * @param appDeployment
     * @return
     * @throws Exception
     */
    private List<String> getAppDeploymentTargetName(String appName, ObjectName appDeployment) throws Exception {
        logger.info("开始获取应用“" + appName + "”的部署目标。。。");
        List<String> targetNames = new ArrayList<String>();
        ObjectName[] targets = (ObjectName[]) connection.getAttribute(appDeployment, "Targets");
        for (ObjectName target : targets) {
            Object object = connection.getAttribute(target, "Name");
            if (object != null && !"".equals(object.toString())) {
                targetNames.add(object.toString());
            }
        }
        logger.info("获取应用“" + appName + "”的部署目标完成！！！");
        return targetNames;
    }

    /**
     * 获取运行应用的健康状况
     * 
     * @param appName 应用名称
     * @param serverName 目标应用服务器名称
     * @return
     * @throws Exception
     */
    private String getApplicationRuntimeHealthState(String appName) throws Exception {
        logger.info("开始获取部署应用“" + appName + "”的健康状况。。。");
        String healthState = "";
        ObjectName[] serverRT = getServerRuntimes();
        boolean checkHealth = false;
        for (ObjectName server : serverRT) {
            // String name = String.valueOf(connection.getAttribute(server, "Name"));
            // if (serverName.equals(name)) {
            // break;
            // }
            ObjectName[] applicationRuntimes = (ObjectName[]) connection.getAttribute(server, "ApplicationRuntimes");
            for (ObjectName applicationRuntime : applicationRuntimes) {
                String appRuntimeName = String.valueOf(connection.getAttribute(applicationRuntime, "ApplicationName"));
                if (appName.equals(appRuntimeName)) {
                    healthState = extractionHealthState(String.valueOf(connection.getAttribute(applicationRuntime,
                        "HealthState")));
                    if (!"".equals(healthState)) {
                        checkHealth = true;
                        break;
                    }
                }
            }
            if (checkHealth) {
                break;
            }

        }
        logger.info("获取部署应用“" + appName + "”的健康状况完成！！！");
        return healthState;
    }

    /**
     * 获取部署应用的当前状态
     * 
     * @param applicationName 应用名称
     * @param targetName 目标服务器名称
     * @return 返回状态字符串
     * @throws Exception
     */
    private String getAppDeployState(String applicationName, List<String> targetNames) throws Exception {
        logger.info("开始获取部署应用“" + applicationName + "”的当前状态。。。");
        ObjectName domainRuntime = (ObjectName) connection.getAttribute(service, "DomainRuntime");
        ObjectName appRuntimeStateRuntime = (ObjectName) connection.getAttribute(domainRuntime,
            "AppRuntimeStateRuntime");
        String state = String.valueOf(connection.invoke(appRuntimeStateRuntime, "getCurrentState", new Object[] {
                applicationName, targetNames.get(0) }, new String[] { "java.lang.String", "java.lang.String" }));
        logger.info("获取部署应用“" + applicationName + "”的当前状态完成！！！");
        return state;
    }

    /**
     * 整合weblogic检测总状态
     * 
     * @param status 健康状态
     * @param description 异常描述
     */
    private void integrateStatusAndDescription(String status, String description) {
        if ("warning".equals(status)) {
            if ("good".equals(healthMain)) {
                healthMain = status;
            }
            descriptionMain += description;
        } else if ("fatal".equals(status)) {
            if (!"fatal".equals(healthMain)) {
                healthMain = status;
            }
            descriptionMain += description;
        }
    }

    /**
     * 获取日志文件中的异常日志信息
     * 
     * @throws Exception
     */
    public JSONObject getLogFileContent(String osType, SSHTarget target) throws Exception {
        logger.info("开始通过SSH获取异常日志信息。。。");
        JSONObject jsonObject = new JSONObject();
        String status = "good";
        String errorLog = "";
        String description = "";
        if (target == null) {
            description = "[异常信息：系统未找到该weblogic所处的主机账号信息！] ";
            errorLog = "系统未找到该weblogic所处的主机账号信息，因此无法获取日志信息！";
        } else {
            ObjectName domain = (ObjectName) connection.getAttribute(service, "DomainConfiguration");
            String domainRootDir = String.valueOf(connection.getAttribute(domain, "RootDirectory"));
            ObjectName[] serverRT = getServerRuntimes();
            StringBuffer command = new StringBuffer();
            for (ObjectName server : serverRT) {
                String serverName = String.valueOf(connection.getAttribute(server, "Name"));
                String baseDir = domainRootDir + "/servers/" + serverName + "/logs/*.log";
                String dataSourceCommand = "grep 'BEA-001129' " + baseDir;
                String memoryCommand = "grep 'java.lang.OutOfMemoryError' " + baseDir;
                String filesCommand = "grep 'Too many open files' " + baseDir;
                command.append(dataSourceCommand + ";" + memoryCommand + ";" + filesCommand + ";");
            }
            errorLog = readLogFile(osType, command.toString(), target);
            if (errorLog.length() > 0) {
                status = "warning";
                description = "[警告信息：该weblogic的日志中发现异常日志信息！] ";
                errorLog = errorLog.replace("<", "&lt").replace(">", "&gt").replaceAll("&ltbr/&gt", "<br>")
                    .replace("\"", "\\\"").replace("'", "\\\"");
            } else {
                errorLog = "未发现异常日志信息！";
            }
        }
        integrateStatusAndDescription(status, description);
        jsonObject.put("errorLog", errorLog);
        logger.info("获取异常日志信息完成！！！");
        return jsonObject;
    }

    /**
     * 从服务器上获取日志信息
     * @param policy
     * @param scheduleName
     * @param workstation
     * @return
     */
    private String readLogFile(String osType, String command, SSHTarget target) {
        StringBuilder logInfo = new StringBuilder();
        connect(target.getIp(), target.getUsername(), target.getPassword());
        BufferedReader reader = null;
        Channel channel = null;
        try {
            String lang = getOperationSystemCharset(osType);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            channel.connect();
            InputStream in = channel.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, lang));

            String line = null;
            while ((line = reader.readLine()) != null) {
                logInfo.append(line + "&ltbr/&gt");
            }
            in.close();
        } catch (Exception e) {
            logger.error("SSH读取weblogic服务器日志信息失败。引起原因：" + e);
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                channel.disconnect();
                session.disconnect();
            }

        }
        return logInfo.toString();
    }

    /**
     * 使用JSch连接服务器获取数据
     * @param user
     * @param passwd
     * @param host
     */
    private boolean connect(String host, String user, String passwd) {
        logger.info("采用SSH连接主机[ " + host + " ].");
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        boolean connected = true;
        jsch = new JSch();
        try {
            session = jsch.getSession(user, host, 22);
            session.setPassword(passwd);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(99999);
            long start = System.currentTimeMillis();
            session.connect();
            long end = System.currentTimeMillis();
            logger.info("jsch连接用时:" + (end - start));
        } catch (Exception e) {
            logger.error("通过SSH访问主机[ " + host + " ]失败." + e);
            connected = false;
        }
        return connected;
    }

    /**
     * 获取weblogic所在主机的语言环境
     * 
     * @param workstation
     * @return 返回编码类型字符串
     */
    private String getOperationSystemCharset(String osType) {
        BufferedReader reader = null;
        Channel localechannel = null;
        InputStream in = null;
        String lang = "UTF-8";
        try {
            if ("AIX".equals(osType) || "Linux".equals(osType)) {
                localechannel = session.openChannel("exec");
                ((ChannelExec) localechannel).setCommand("locale");
                localechannel.setInputStream(null);
                ((ChannelExec) localechannel).setErrStream(System.err);
                localechannel.connect();
                in = localechannel.getInputStream();
                String langLine = "UTF-8";
                reader = new BufferedReader(new InputStreamReader(in));
                if ((langLine = reader.readLine()) != null) {

                    if (langLine.toUpperCase().contains("GB")) {
                        lang = "GBK";
                    } else if (langLine.toUpperCase().contains("UTF-8")) {
                        lang = "UTF-8";
                    } else if (langLine.toUpperCase().contains("ZH_CN")) {
                        lang = "GBK";
                    } else if (langLine.toUpperCase().contains("EN_US")) {
                        if (langLine.replace(" ", "").equals("LANG=en_US")) {
                            lang = "GBK";
                        }
                    }

                }
                in.close();
                reader.close();
                localechannel.disconnect();
            }
        } catch (JSchException e) {
            logger.error("获取weblogic主机日志文件编码时，连接异常！");
        } catch (IOException e) {
            logger.error("获取weblogic主机日志文件编码时，IO异常！");
        }
        return lang;
    }
}
