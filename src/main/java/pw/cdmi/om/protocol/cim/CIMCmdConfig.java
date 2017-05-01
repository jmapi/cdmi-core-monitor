package pw.cdmi.om.protocol.cim;

import javax.persistence.Column;

/**
 * 用于记录CIM的命令配置信息
 * @author super_cc
 *
 */
public class CIMCmdConfig implements Comparable<CIMCmdConfig> {

    private String name;

    /**
     * 1\[]
     * 2\map<eleName, eleValue>
     * 3\list<elename>
     */
    private String outParams;

    /**
     * 类型
     */
    private String type;

    /**
     * 命令名
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 取出命令输入关键字
     * 1\[]
     * 2\map<eleName, eleValue>
     * 3\list<elename>
     */
    public String getOutParams() {
        return outParams;
    }

    /**
     * 设值命令输入关键字
     * 1\[]
     * 2\map<eleName, eleValue>
     * 3\list<elename>
     */
    public void setOutParams(String outParams) {
        this.outParams = outParams;
    }

    public CIMCmdConfig(String name, String outParams, String type) {
        super();
        this.name = name;
        this.outParams = outParams;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CIMCmdConfig() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compareTo(CIMCmdConfig o) {
        // TODO Auto-generated method stub
        return 0;
    }

}
