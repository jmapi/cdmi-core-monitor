package pw.cdmi.om.protocol.snmp.f5;

import lombok.Data;

@Data
public class SystemCPUUsage {
    private Long sysStatTmTotalCycles;

    private Long sysStatTmIdleCycles;

    private Long sysStatTmSleepCycles;
}
