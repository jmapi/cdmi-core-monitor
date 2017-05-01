package pw.cdmi.om.protocol.jmx;

import lombok.Data;

@Data
public class JMXTarget {
    private String host;

    private int port;

    private String username;

    private String password;
}
