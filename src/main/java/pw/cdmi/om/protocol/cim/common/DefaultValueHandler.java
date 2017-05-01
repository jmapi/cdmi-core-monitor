package pw.cdmi.om.protocol.cim.common;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pw.cdmi.om.protocol.cim.CIMObjectParam;

public class DefaultValueHandler implements IValueHandler {
    Log log = LogFactory.getLog(DefaultValueHandler.class);

    @Override
    public void handle(Object target, Object value, CIMObjectParam cimop) {
        try {
            Class t = Class.forName(cimop.getOrObject());
            if (t.isInstance(target)) {
                Class cls = t.getDeclaredField(cimop.getOrParam()).getType();

                // 取得对应属性的Set方法
                Method method = t.getDeclaredMethod(getSetMethodName(cimop.getOrParam()), cls);
                String v = null;
                if (!cls.isInstance(value)) {
                    if (value.getClass().isArray()) {
                        Object[] as = (Object[]) value;
                        for (Object a : as) {
                            if (v == null)
                                v = a.toString();
                            else
                                v = (v + "," + a.toString());
                        }
                    } else
                        v = value.toString();
                    value = cls.getConstructor(String.class).newInstance(v);
                }

                method.invoke(target, value);
                log.debug("值处理器<默认>--完成: " + cimop.getParamName() + " , 值： " + value.toString());
            }
        } catch (Exception e) {
            log.error("值处理器<默认>--出错: " + cimop.getParamName() + " , 值： " + value.toString() + " , 内容： "
                    + e.getMessage());
        }
    }

    private static String getSetMethodName(String name) {
        StringBuffer sb = new StringBuffer();
        sb.append("set");
        sb.append(name.substring(0, 1).toUpperCase());
        sb.append(name.substring(1));
        return sb.toString();
    }
}
