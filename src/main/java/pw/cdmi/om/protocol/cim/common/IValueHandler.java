/**
 * 
 */
package pw.cdmi.om.protocol.cim.common;

import pw.cdmi.om.protocol.cim.CIMObjectParam;

/**
 * 值处理器
 * @author super_cc
 *
 */
public interface IValueHandler {
	/**
	 * 值处理接口
	 * @param target 赋值实例
	 * @param value	 值
	 * @param cimop	 酸置属性信息
	 */
	public void handle(Object target, Object value ,CIMObjectParam cimop);
}
