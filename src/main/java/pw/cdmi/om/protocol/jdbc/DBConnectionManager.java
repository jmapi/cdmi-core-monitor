package pw.cdmi.om.protocol.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
*
* 描述：连接数据库
* @author wangbaijun
*
*/
public class DBConnectionManager {

	private static final Logger logger = LoggerFactory.getLogger(DBConnectionManager.class);

	private final static String url_head_oracle = "jdbc:oracle:thin:@";

	private final static String url_head_mysql = "jdbc:mysql://";

	private final static String url_head_sqlserver = "jdbc:microsoft:sqlserver://";

//	private final static String url_head_sybase = "jdbc:sybase:Tds:"; // prot 5000
	private final static String url_head_sybase = "jdbc:jtds:sybase://"; // prot 5000

	private final static String url_head_db2 = "jdbc:db2://";/*
																private final String url_tail = ":1521:orcl";*/

	private final static String Oracle = "ORACLE";

	private final static String MySQL = "MYSQL";

	private final static String SQLServer = "SQLSERVER";

	private final static String DB2 = "DB2";

	private final static String Sybase = "SYBASE";


	/**
	 * 关闭数据库连接,将Connection对象返还给连接池.
	 * @param conn 需要关闭的数据库连接
	 */
	public static final void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
			logger.error("关闭数据库连接错误", e);
		}
	}


	/**
	 * 从数据库连接池中获取一个Connection.
	 * @return instance of Connection
	 */
	public final static Connection getConnection(JDBCTarget target) {
		Connection conn = null;
		String url_head = "";
		try {
			
			switch (target.getDbType().toUpperCase()) {
			case Oracle:
				Class.forName("oracle.jdbc.driver.OracleDriver");
				url_head = url_head_oracle + target.getIp() + ":" + target.getPort() + ":" + target.getDbName();
				break;
			case MySQL:
				Class.forName("com.mysql.jdbc.Driver");
				url_head = url_head_mysql + target.getIp() + ":" + target.getPort() + "/" + target.getDbName();
				break;
			case SQLServer:
				Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
				url_head = url_head_sqlserver + target.getIp() + ":" + target.getPort() + ";DatabaseName="
						+ target.getDbName();
				break;
			case DB2:
				Class.forName("com.ibm.db2.jdbc.net.DB2Driver");
				url_head = url_head_db2 + target.getIp() + ":" + target.getPort() + "/" + target.getDbName();
				break;
			case Sybase:
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				url_head = url_head_sybase + target.getIp() + ":" + target.getPort() + "/" + target.getDbName();
				break;
			default:
				break;
			}
			
			if (!StringUtils.isEmpty(url_head)) {
				DriverManager.setLoginTimeout(60 * 8);
				conn = DriverManager.getConnection(url_head, target.getUsername(), target.getPassword());
			}
		} catch (SQLException e) {
			logger.error("获取数据库连接错误", e);
		} catch (ClassNotFoundException e) {
			logger.error("没有找到相应的数据库驱动包", e);
			e.printStackTrace();
		}

		return conn;
	}

}
