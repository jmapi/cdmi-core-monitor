package pw.cdmi.om.protocol.ssh;

import lombok.Data;

@Data
public class SSHTarget {
    private String ip;

    private String username;

    private String password;

    private int port;

}
