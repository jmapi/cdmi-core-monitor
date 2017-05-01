package pw.cdmi.om.protocol.snmp.f5;

import lombok.Data;

@Data
public class RewriteTransactionData {
    private Float fromClient;

    private Float toServer;

    private Float toClient;

    private Float fromServer;
}
