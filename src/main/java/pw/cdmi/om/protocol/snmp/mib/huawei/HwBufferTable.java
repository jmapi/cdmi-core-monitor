package pw.cdmi.om.protocol.snmp.mib.huawei;

import org.snmp4j.PDU;

import pw.cdmi.om.protocol.snmp.mib.MibOID;
import pw.cdmi.om.protocol.snmp.mib.MibSystem;

/**
 * **********************************************************
 * 该表提供对设备缓冲区使用率的查询。
 * 
 * @author NiXiaojun
 * @version iSoc Service Platform, 2016-1-18
 ***********************************************************
 */
public enum HwBufferTable implements MibOID {
    hwBufferModuleIndex("1.3.6.1.4.1.2011.6.3.5.1.1.1",PDU.GET),    // 索引，该节点只用于扩展。对于单CPU设备，该节点取值为0。
    hwBufferSize("1.3.6.1.4.1.2011.6.3.5.1.1.2",PDU.GET),    // 指示缓冲区的大小，单位是字节
    hwBufferCurrentTotal("1.3.6.1.4.1.2011.6.3.5.1.1.3",PDU.GET),  // 指示当前的缓冲区总数。
    hwBufferCurrentUsed("1.3.6.1.4.1.2011.6.3.5.1.1.4",PDU.GET);  // 指示当前占用的缓冲区总数。

    private String oid;

    private int mode;

    private String parent = "1.3.6.1.4.1.2011";

    private HwBufferTable(String oid, int mode) {
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
