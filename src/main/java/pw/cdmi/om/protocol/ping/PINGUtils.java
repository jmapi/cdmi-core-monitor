package pw.cdmi.om.protocol.ping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PINGUtils {
    private final static Logger logger = LoggerFactory.getLogger(PINGUtils.class);

    /**
     * 通过PING的方式确定监控对象所在主机是否可以访问
     * 
     * @param ip
     */
    public boolean patrolByPing(String ip) {
        logger.info("执行PING命令,尝试连接[" + ip + "]");
        // 3. 判断监控对象所在的网络地址是否可达，需要修改监控对象的状态，但不将作为巡检结果保存
        int timeOut = 3000; // 超时应该在3钞以上
        boolean canconnect = false;
        String errorMessage = null;
        try {
            // 3.1当返回值是true时，说明host是可用的，反正则不可。
            canconnect = InetAddress.getByName(ip).isReachable(timeOut);
            if (!canconnect) {
                errorMessage = "设备网络不可达";
                logger.error(errorMessage + ",请检查设备IP是否正确.");
            }
        } catch (UnknownHostException e) {
            errorMessage = "设备名称不可识别";
            logger.error(errorMessage + ",请检查系统.原因：" + e.toString());
        } catch (IOException e) {
            errorMessage = "连接设备失败";
            logger.error("存在网络问题,请检查系统,原因：" + e.toString());
        }
        return canconnect;
    }
}
