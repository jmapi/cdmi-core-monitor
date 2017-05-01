package pw.cdmi.om.protocol.snmp.mib.huawei;

import org.snmp4j.PDU;

import pw.cdmi.om.protocol.snmp.mib.MibOID;
import pw.cdmi.om.protocol.snmp.mib.MibSystem;

/**
 * **********************************************************
 * 该表提供设备所有内存使用率统计信息，包括内存总量、空闲量、占用量等。
 * 
 * @author NiXiaojun
 * @version iSoc Service Platform, 2016-1-18
 ***********************************************************
 */
public enum HwMemoryDevTable implements MibOID {
    hwMemoryDevModuleIndex("1.3.6.1.4.1.2011.6.3.5.1.1.1.0.0.0",PDU.GET),    // 索引，该节点只用于扩展。对于单CPU设备，该节点取值为0。
    hwMemoryDevSize("1.3.6.1.4.1.2011.6.3.5.1.1.2.0.0.0",PDU.GET),    // 指示被管理对象的内存总量，单位是字节。
    hwMemoryDevFree("1.3.6.1.4.1.2011.6.3.5.1.1.3.0.0.0",PDU.GET),  // 指示设备上空闲内存的总量，单位是字节。
    hwMemoryDevRawSliceUsed("1.3.6.1.4.1.2011.6.3.5.1.1.4.0.0.0",PDU.GET),  // 指示设备上已占用的raw slice内存总量，单位是字节。
    hwMemoryDevLargestFree("1.3.6.1.4.1.2011.6.3.5.1.1.5.0.0.0",PDU.GET),  // 指示被管理对象上目前未被占用的最大连续字节数。
    hwMemoryDevFail("1.3.6.1.4.1.2011.6.3.5.1.1.6.0.0.0",PDU.GET),  // 指示内存分配失败的次数。缺省值为0。
    hwMemoryDevFailNoMem("1.3.6.1.4.1.2011.6.3.5.1.1.7.0.0.0",PDU.GET);  // 指示由于没有空闲内存导致的内存分配失败次数。缺省值为0。

    private String oid;

    private int mode;

    private String parent = "1.3.6.1.4.1.2011";

    private HwMemoryDevTable(String oid, int mode) {
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
