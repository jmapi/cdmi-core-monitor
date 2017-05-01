package pw.cdmi.om.protocol.cim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CIMTarget {
    private String hostIp;

    private int port;

    private String protocol;        // SMI/SNMP

    private String telprot;         // http

    private String userName;

    private String password;

    private String namespace;       // spi.target
}
