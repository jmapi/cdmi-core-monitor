package pw.cdmi.om.protocol.snmp.f5;

import lombok.Data;

@Data
public class SSLTransactions {
    private Long clientsslStatTotNativeConns;

    private Long clientsslStatTotCompatConns;
}
