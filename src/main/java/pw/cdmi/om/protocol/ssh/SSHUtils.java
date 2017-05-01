package pw.cdmi.om.protocol.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHUtils {
    private final static Logger logger = LoggerFactory.getLogger(SSHUtils.class);

    private JSch jsch;

    protected Session session;

    /**
     * 使用SSH方式远程访问主机
     * 
     * @param user
     * @param passwd
     * @param host
     * @return
     * @throws JSchException
     */
    public boolean connecting(SSHTarget target) {
        logger.info("采用SSH连接主机[ " + target.getIp() + " ].");
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        boolean connected = true;
        jsch = new JSch();
        try {
            session = jsch.getSession(target.getUsername(), target.getIp(), target.getPort());
            session.setPassword(target.getPassword());
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            long start = System.currentTimeMillis();
            session.connect();
            long end = System.currentTimeMillis();
            logger.info("jsch连接用时:" + (end - start));
        } catch (Exception e) {
            logger.error("通过SSH访问主机[ " + target.getIp() + " ]失败." + e);
            // e.printStackTrace();
            connected = false;
        }
        return connected;
    }

    public void close() {
        try {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        } catch (Exception ex) {
            // 不处理
            logger.error("关闭SSH连接错误，存在异常." + ex);
        }
    }

    protected void feedPassword(String password, OutputStream os) {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(os));
        // feed in the program
        out.println(password);
        out.flush();
    }

    protected boolean skipLine(String line) {
        return !line.startsWith("--") && !line.contains("password");
    }

    public List<String> execCmd(String OsType, SSHTarget target, String command) throws JSchException {
        logger.info("通过SSH执行Shell命令[ " + command + " ]");

        List<String> result = new ArrayList<String>();
        boolean connectValue = false;
        try {
            connectValue = connecting(target);
            if (!connectValue) {
                // 连接失败
                return null;
            }
            BufferedReader reader = null;
            Channel localechannel = null;
            Channel channel = null;
            InputStream in = null;
            String lang = "UTF-8";
            try {
                // 获得操作系统的语言环境变量
                if (OsType == "LINUX" || OsType == "AIX") {
                    localechannel = session.openChannel("exec");
                    ((ChannelExec) localechannel).setCommand("locale");

                    localechannel.setInputStream(null);
                    ((ChannelExec) localechannel).setErrStream(System.err);

                    localechannel.connect();
                    in = localechannel.getInputStream();
                    String langLine = "UTF-8";
                    reader = new BufferedReader(new InputStreamReader(in));

                    if ((langLine = reader.readLine()) != null) {

                        if (langLine.toUpperCase().contains("GB")) {
                            lang = "GBK";
                        } else if (langLine.toUpperCase().contains("UTF-8")) {
                            lang = "UTF-8";
                        } else if (langLine.toUpperCase().contains("ZH_CN")) {
                            lang = "GBK";
                        }

                    }
                    in.close();
                    reader.close();
                    localechannel.disconnect();
                }
                // 执行Shell Command命令
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);

                // channel.connect(simocommand.getDefaultTimeOut() * 1000);
                logger.info("建立与:［" + target.getIp() + "］命令访问通道，命令为［" + command + "］");
                channel.connect();
                logger.info("开始从［" + target.getIp() + "］通道中获取数据流");
                in = channel.getInputStream();

                reader = new BufferedReader(new InputStreamReader(in, lang));

                String line = null;
                String underLine = null;
                logger.info("逐行读取:" + target.getIp() + "数据流中的数据");
                while ((line = reader.readLine()) != null) {
                    if (skipLine(line)) {
                        if (line.contains(" ")) {
                            if (underLine != null) {
                                line = underLine + " " + line;
                                underLine = null;
                            }
                            result.add(line);
                        } else {
                            underLine = line;
                        }
                    }
                }
                logger.info("读取:" + target.getIp() + "数据的数据结束");
            } catch (IOException e) {
                logger.error("执行SSH Shell命令[ " + command + " ]失败.原因是：" + e);
            } catch (JSchException e) {
                logger.error("执行SSH Shell命令[ " + command + " ]失败.原因是：" + e);
                if (e.getMessage().contains("session is down")) {
                    logger.error("执行SSH Shell命令[ " + command + " ]超时");
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                try {
                    reader.close();
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        } finally {
            if (session != null || session.isConnected()) {
                session.disconnect();
                session = null;
            }
        }

        return result;
    }
}
