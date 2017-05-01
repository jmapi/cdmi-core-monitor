package pw.cdmi.om.protocol.cim.common;

import java.util.List;

import org.springframework.stereotype.Component;

import pw.cdmi.om.protocol.cim.CIMCmdConfig;
import pw.cdmi.om.protocol.cim.CIMObjectParam;
import pw.cdmi.om.protocol.cim.CIMTarget;

/**
 * 定义数据分析器接口
 * @author super_cc
 *
 */
@Component
public interface IDataParser {
    /**
     * 初始化 
     * 如：与外部建立数据通信
     */
    public void init(CIMTarget target, List<CIMCmdConfig> cmdConfigs, List<CIMObjectParam> dataParams) throws Exception;

    /**
     * 数据分析
     */
    public void analyse() throws Exception;

    /**
     * 关闭相关连接信息
     */
    public Object getResult() throws Exception;
}
