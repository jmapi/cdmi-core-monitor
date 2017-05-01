package pw.cdmi.om.protocol.cim.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pw.cdmi.om.protocol.cim.CIMCmdConfig;
import pw.cdmi.om.protocol.cim.CIMObjectParam;
import pw.cdmi.om.protocol.cim.CIMTarget;

public abstract class BaseDataParser implements IDataParser {
    Log log = LogFactory.getLog(BaseDataParser.class);

    @Override
    public void init(CIMTarget target, List<CIMCmdConfig> cmdConfigs, List<CIMObjectParam> dataParams) throws Exception {
        if (target != null) {
            log.info("设备：" + target.getHostIp() + " ,开始巡检!");
            log.info("初始化基本信息-开始");
            this.initClient(target);
            this.initCommands(cmdConfigs);
            this.initResultParam(dataParams);
            log.info("初始化基本信息-完成");
        } else {
            log.error("初始化基本信息,出错！---请检查配置数据");
            throw new Exception("初始化基本信息,出错！---请检查配置数据");
        }

    }

    /**
     * 初始化 客户端相关信息，用于后数据采集分析
     * @param ci
     * @param type
     */
    protected abstract void initClient(CIMTarget target) throws Exception;

    /**
     * 初始化命令内容
     * @param cmdConfigs 命令配置列表
     */
    protected abstract void initCommands(List<CIMCmdConfig> cmdConfigs);

    /**
     * 初始化参数内容
     * 
     * @param dataParams 参数列表
     */
    protected abstract void initResultParam(List<CIMObjectParam> dataParams);

}
