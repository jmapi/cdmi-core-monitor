package pw.cdmi.om.protocol.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import pw.cdmi.om.protocol.ssh.ShellExecutorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JDBCUtils {
    private final static Logger logger = LoggerFactory.getLogger(JDBCUtils.class);

    /**
     * 执行数据库命令
     * 
     * @param conn
     * @param command
     * @return 执行结果
     */
    public Map<String[], List<String[]>> execCmdByJDBC(JDBCTarget target, String cmd) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        // 用一个有序的Map来存储多个Select结果,确保列信息和结果信息能够匹配
        Map<String[], List<String[]>> map = new LinkedHashMap<String[], List<String[]>>();

        Connection conn = DBConnectionManager.getConnection(target);
        String[] commands = cmd.split(";");
        try {
            for (String command : commands) {

                ps = conn.prepareStatement(command);
                ps.setQueryTimeout(60 * 2);// 设置超时时间
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();

                String[] columnNames = new String[count];// 用一个数组来保存对象的列名
                List<String[]> columnValues = new ArrayList<String[]>();// 用另外一个数组来保存获取到的数值

                for (int i = 0; i < count; i++) {
                    columnNames[i] = rsmd.getColumnName(i + 1);// 第一列不是0，而是1
                }
                // statuses.add(columnNames);
                while (rs.next()) {
                    String[] result = new String[count];
                    for (int i = 0; i < count; i++) {
                        result[i] = rs.getString(i + 1); // 索引从1开始，而不是从0开始
                    }
                    columnValues.add(result);
                    // statuses.add(line);
                }
                map.put(columnNames, columnValues);
            }
        } catch (SQLException e) {
            logger.error("执行SQL时候出现异常，错误信息" + e);
            return null;
        } catch (Exception e) {
            logger.error("执行SQL时候出现异常，错误信息" + e);
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("执行SQL时候出现异常，错误码：" + e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("执行SQL时候出现异常，错误码：" + e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    logger.error("关闭conn连接出错：" + e);
                }
            }
        }
        return map;
    }

    /**
     * 特殊情况，查询结果只有两列，且设置第一列为key，第二列为value
     * @param conn
     * @param command
     * @return 执行结果
     */
    public Map<String, Object> execCmd(JDBCTarget target, String cmd) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        // 用一个有序的Map来存储多个Select结果,确保列信息和结果信息能够匹配
        Map<String, Object> map = new HashMap<String, Object>();

        Connection conn = DBConnectionManager.getConnection(target);
        String[] commands = cmd.split(";");
        try {
            for (String command : commands) {

                ps = conn.prepareStatement(command);
                ps.setQueryTimeout(60 * 2);// 设置超时时间
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                // 多少列
                int count = rsmd.getColumnCount();
                // 特定的方法，只有两列结果
                if (count == 2) {
                    while (rs.next()) {
                        map.put(rs.getString(1), rs.getString(2));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("执行SQL时候出现异常，错误信息" + e);
            return null;
        } catch (Exception e) {
            logger.error("执行SQL时候出现异常，错误信息" + e);
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("执行SQL时候出现异常，错误码：" + e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("执行SQL时候出现异常，错误码：" + e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    logger.error("关闭conn连接出错：" + e);
                }
            }
        }
        return map;
    }

    public List<String> execCmdBySqlPlus(JDBCTarget target, String cmd) {
        List<String> result = new ArrayList<String>();
        boolean isRead = false;
        Process process = null;
        InputStreamReader isr = null;
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("cmd");

        try {
            process = pb.start();
        } catch (IOException e) {
            logger.error("执行CMD出错：" + e);
        }
        InputStream is = process.getInputStream();
        try {
            isr = new InputStreamReader(is, "GBK");
        } catch (UnsupportedEncodingException e) {
            logger.error("执行CMD出错：" + e);
        }
        BufferedReader br = new BufferedReader(isr);
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
        printWriter.println("sqlplus " + target.getUsername() + "/" + target.getPassword() + "@" + target.getIp() + ":"
                + target.getPort() + "/" + target.getDbName() + "");

        printWriter.flush();
        printWriter.println("set linesize 3000;");
        printWriter.flush();
        printWriter.println("set trimout on;");
        printWriter.flush();
        printWriter.println("set colsep '#';");

        printWriter.flush();
        String[] commands = cmd.split(";");
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];
            if (i == (commands.length - 1)) {
                command = command + ";";
            }
            printWriter.println(command);

        }

        printWriter.flush();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                if (isRead && line.equals("")) {
                    break;
                }
                if (isRead) {
                    if (!line.contains("---") && !line.toUpperCase().contains("SQL>")
                            && !line.toUpperCase().contains("BREAKS") && !line.toUpperCase().contains("computes")) {
                        result.add(line);
                    }
                }
                if (line.toUpperCase().contains("SQL>") && !line.toUpperCase().contains("COLUMNS")) {
                    isRead = true;
                }

            }
        } catch (IOException e) {
            logger.error("执行SQL语句出错：" + e);
        } finally {

            try {

                br.close();
                is.close();
                isr.close();
            } catch (IOException e) {
                logger.error("关闭SQL连接出错：" + e);
            }

            process.destroy();

        }
        return result;
    }

    public List<String> execCmdBySQLClient(JDBCTarget target, String cmd, Integer timeOut) {

        List<String> result = null;
        String[] commands = cmd.split(";");
        String logincommand = "sqlplus " + target.getUsername() + "/" + target.getPassword() + " as sysdba";
        String[] dbCommand = new String[commands.length];
        for (int i = 0; i < commands.length; i++) {
            dbCommand[i] = commands[i] + ";";
        }
        String[] firstCommands = { logincommand, "set linesize 10000;", "set trimout on;", "set colsep '#';" };
        String[] newCommands = new String[dbCommand.length + 4];
        System.arraycopy(firstCommands, 0, newCommands, 0, firstCommands.length);
        System.arraycopy(dbCommand, 0, newCommands, firstCommands.length, dbCommand.length);
        logger.debug(Arrays.toString(newCommands));
        ShellExecutorUtil shell = null;

        try {
            shell = new ShellExecutorUtil(target.getIp(), target.getPort(), target.getUsername(), target.getPassword());
            result = shell.executeOracleCommands(newCommands, timeOut);
        } catch (Throwable ex) {
            logger.error("执行Shell语句失败,原因：" + ex.toString());
        } finally {
            if (shell != null) {
                try {
                    shell.disconnect();
                } catch (Throwable t) {
                    // 不处理
                    logger.debug("关闭Shell失败,原因：" + t.toString());
                }
            }
        }
        return result;
    }

    public String formatToJSON(Map<String[], List<String[]>> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        int index = 0;// 这里最多两个Select语句，一个是查询结果，一个是对查询结果的统计
        String[] headers = {};
        List<String[]> data = new ArrayList<String[]>();
        for (Map.Entry<String[], List<String[]>> entry : map.entrySet()) {
            if (index > 0) {// 将后一个统计的Data加到第一个Data中。
                String[] new_row = new String[headers.length];// 创建一个新行
                for (int j = 0; j < headers.length; j++) {
                    new_row[j] = "--";// 默认填充新行--
                    String colnum_header = headers[j];// 新行对应的列名
                    for (int k = 0; k < entry.getKey().length; k++) {
                        String compute_header = entry.getKey()[k];// 得到统计列原始名称
                        String[] header_text = compute_header.split(":");// 统计列为Total:数据列名

                        List<String[]> compute_data = entry.getValue();// 统计值只有一行，List.size()==0
                        if (header_text.length == 2 && compute_data.size() == 1) {// 按照规则一定等于2，不是2的话不存储，统计数据直接丢弃
                            if (colnum_header.equalsIgnoreCase(header_text[1].trim())) {// 统计列和数据列相同，则将新行对应的数据列中加入统计后的值
                                new_row[j] = compute_data.get(0)[k];
                            }
                        }
                    }

                }
                data.add(new_row);// 添加新行数据
            } else {
                headers = entry.getKey();
                data = entry.getValue();
            }
            index++;
        }
        List<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
        for (int i = 0; i < data.size(); i++) {
            String[] row_data = data.get(i);
            LinkedHashMap<String, String> nameStruct = new LinkedHashMap<String, String>();
            for (int j = 0; j < headers.length; j++) {
                headers[j] = headers[j].trim();
                row_data[j] = (row_data[j] == null) ? "" : row_data[j].trim();
                nameStruct.put(headers[j], row_data[j]);
            }
            list.add(nameStruct);
        }

        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            logger.error("输出JSON数据时存在错误，原因：" + e);
            return null;
        }
    }

    public String formatToTableJSON(Map<String[], List<String[]>> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        int index = 0;// 这里最多两个Select语句，一个是查询结果，一个是对查询结果的统计
        String[] headers = {};
        List<String[]> data = new ArrayList<String[]>();
        for (Map.Entry<String[], List<String[]>> entry : map.entrySet()) {
            if (index > 0) {// 将后一个统计的Data加到第一个Data中。
                String[] new_row = new String[headers.length];// 创建一个新行
                for (int j = 0; j < headers.length; j++) {
                    new_row[j] = "--";// 默认填充新行--
                    String colnum_header = headers[j];// 新行对应的列名
                    for (int k = 0; k < entry.getKey().length; k++) {
                        String compute_header = entry.getKey()[k];// 得到统计列原始名称
                        String[] header_text = compute_header.split(":");// 统计列为Total:数据列名

                        List<String[]> compute_data = entry.getValue();// 统计值只有一行，List.size()==0
                        if (header_text.length == 2 && compute_data.size() == 1) {// 按照规则一定等于2，不是2的话不存储，统计数据直接丢弃
                            if (colnum_header.equalsIgnoreCase(header_text[1].trim())) {// 统计列和数据列相同，则将新行对应的数据列中加入统计后的值
                                new_row[j] = compute_data.get(0)[k];
                            }
                        }
                    }

                }
                data.add(new_row);// 添加新行数据
            } else {
                headers = entry.getKey();
                data = entry.getValue();
            }
            index++;
        }
        List<LinkedHashMap<String, String>> columns = new ArrayList<LinkedHashMap<String, String>>();
        for (int i = 0; i < headers.length; i++) {
            LinkedHashMap<String, String> column = new LinkedHashMap<String, String>();
            column.put("field", "field" + i);
            column.put("title", headers[i]);
            columns.add(column);
        }
        List<LinkedHashMap<String, String>> tableDatas = new ArrayList<LinkedHashMap<String, String>>();
        for (int i = 0; i < data.size(); i++) {
            String[] row_data = data.get(i);
            LinkedHashMap<String, String> nameStruct = new LinkedHashMap<String, String>();
            for (int j = 0; j < headers.length; j++) {
                row_data[j] = (row_data[j] == null) ? "" : row_data[j].trim();
                nameStruct.put("field" + j, row_data[j]);
            }
            tableDatas.add(nameStruct);
        }

        final ObjectMapper mapper = new ObjectMapper();

        try {
            JSONObject table = new JSONObject();
            table.put("columns", mapper.writeValueAsString(columns));
            table.put("tableDatas", mapper.writeValueAsString(tableDatas));
            return table.toString();
        } catch (JsonProcessingException e) {
            logger.error("输出JSON数据时存在错误，原因：" + e);
            return null;
        }
    }

}
