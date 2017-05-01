/**
 * 
 */
package pw.cdmi.om.protocol.cim.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.wbem.CloseableIterator;
import javax.wbem.client.WBEMClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pw.cdmi.om.protocol.cim.CIMClientService;
import pw.cdmi.om.protocol.cim.CIMCmdConfig;
import pw.cdmi.om.protocol.cim.CIMObjectParam;
import pw.cdmi.om.protocol.cim.CIMTarget;
import pw.cdmi.om.protocol.cim.cmd.CIMCmdTotalInfo;

/**
 * CIM 数据采集分析器
 * @author super_cc
 *
 */
public abstract class CIMDataParser extends BaseDataParser {

    private Log log = LogFactory.getLog(CIMDataParser.class);

    private Map<String, CIMCmdTotalInfo> commandm = new HashMap<String, CIMCmdTotalInfo>();

    private CIMClientService cimClientService = new CIMClientService();

    private WBEMClient client;

    private CIMTarget target;

    @Override
    protected void initClient(CIMTarget target) throws Exception {
        log.debug("初始化客户端信息：" + target.getHostIp() + " 用户：" + target.getUserName());
        client = cimClientService.getClient(target);
        this.target = target;
    }

    @Override
    public void initCommands(List<CIMCmdConfig> cmdConfigs) {
        log.debug("获取命令配置--开始！");
        // 取命令信息配置
        for (CIMCmdConfig conf : cmdConfigs) {
            CIMCmdTotalInfo cmdParam = new CIMCmdTotalInfo();
            cmdParam.setCmdConfig(conf);
            commandm.put(conf.getName(), cmdParam);
        }
        log.debug("获取命令配置--完成！");
    }

    /**
     * 取结果参数配置
     * @param type
     */
    @Override
    protected void initResultParam(List<CIMObjectParam> dataParams) {
        log.debug("处理CIM 结果参数-开始");
        for (CIMObjectParam o : dataParams) {
            CIMCmdTotalInfo cmdParam = this.commandm.get(o.getObjectName());
            if (cmdParam != null)
                cmdParam.addResultParam(o);
            else
                log.error("CIM 结果参数配置有误，请检查！对象名：" + o.getObjectName());
        }
        log.debug("处理CIM 结果参数-完成");
    }

    @Override
    public void analyse() {
        Object object = this.initResultInstance();
        for (String name : this.commandm.keySet()) {
            // 生成ojectpath
            CIMObjectPath cop = new CIMObjectPath(target.getNamespace() + ":" + name);
            CIMCmdTotalInfo cmd = this.commandm.get(name);

            CloseableIterator<CIMInstance> ei = null;
            try {
                ei = client.enumerateInstances(cop, true, true, true, cmd.getCmdPropertys());

                // this.proc(name, cmd, ei);
                // 数据处理-从返回结果中取出相关数据，并进行转换为PO
                cmd.getHander().handler(ei, cmd.getCmdPropertys(), cmd.getResultConfig(), object);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ei != null)
                    ei.close();
            }
        }
    }

    @Override
    public Object getResult() {
        if (client != null)
            client.close();
        return this.getResultInstance();
    }

    /**
     * 初始化PO实体，用于消息存储
     */
    abstract protected Object initResultInstance();

    /**
     * 返回最终结果实体
     */
    abstract protected Object getResultInstance();

}
