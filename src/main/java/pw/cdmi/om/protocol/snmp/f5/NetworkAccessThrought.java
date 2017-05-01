package pw.cdmi.om.protocol.snmp.f5;

import lombok.Data;

@Data
public class NetworkAccessThrought {
    private Float fromClient;

    private Float fromClientCompressed;

    private Float toClient;

    private Float toClientCompressed;
}
