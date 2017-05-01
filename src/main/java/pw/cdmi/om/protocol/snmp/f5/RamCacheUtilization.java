package pw.cdmi.om.protocol.snmp.f5;

import lombok.Data;

@Data
public class RamCacheUtilization {
    private float hitRate;

    private float byteRate;

    private float evictionRate;
}
