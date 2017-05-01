package pw.cdmi.om.protocol.jdbc;

import lombok.Data;

@Data
public class JDBCTarget {
    private String ip;

    private String dbType;

    private String username;

    private String password;

    private int port;

    private String dbName;
}
