package pw.cdmi.om.protocol.snmp.mib.huawei;

import org.snmp4j.PDU;

import pw.cdmi.om.protocol.snmp.mib.MibOID;
import pw.cdmi.om.protocol.snmp.mib.MibSystem;

/**
 * **********************************************************
 * 该表描述设备CPU的使用率信息，包括CPU平均利用率和周期占用率
 * 
 * @author NiXiaojun
 * @version iSoc Service Platform, 2016-1-18
 ***********************************************************
 */
public enum HwCpuDevTable implements MibOID {
    hwCpuDevIndex("1.3.6.1.4.1.2011.6.3.4.1.1.0.0.0",PDU.GET),    // 索引，对于单CPU设备，该节点取值为0。
    hwCpuDevDuty("1.3.6.1.4.1.2011.6.3.4.1.2.0.0.0",PDU.GET),    // 表示一块单板或者一个实体的CPU的平均使用率。
    hwAvgDuty1min("1.3.6.1.4.1.2011.6.3.4.1.3.0.0.0",PDU.GET),  // 表示一块单板或者一个实体最后1分钟的CPU的平均使用率
    hwAvgDuty5min("1.3.6.1.4.1.2011.6.3.4.1.4.0.0.0",PDU.GET);  // 表示一块单板或者一个实体最后5分钟的CPU的平均使用率

    private String oid;

    private int mode;

    private String parent = "1.3.6.1.4.1.2011";

    private HwCpuDevTable(String oid, int mode) {
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
