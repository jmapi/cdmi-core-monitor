package pw.cdmi.om.protocol.snmp.f5;

import lombok.Data;

@Data
public class ThroughputWithBits {
    private Long sysStatClientBytesIn;

    private Long sysStatClientBytesOut;

    private Long sysStatServerBytesIn;

    private Long sysStatServerBytesOut;

    private Long sysHttpStatPrecompresssBytes;
}
