package pw.cdmi.om.protocol.snmp.f5;

import lombok.Data;

@Data
public class ThroughputWithPackets {
    private Long sysStatClientPktsIn;

    private Long sysStatClientPktsOut;

    private Long sysStatServerPktsIn;

    private Long sysStatServerPktsOut;
}
