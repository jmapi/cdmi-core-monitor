package pw.cdmi.om.protocol.cim.common;

import javax.cim.CIMInstance;
import javax.wbem.CloseableIterator;

import pw.cdmi.om.protocol.cim.cmd.CIMResultParams;

public interface ICommandHandler {
	/**
	 * 命令处理
	 * 
	 * @param ei
	 *            CIM反回实体流
	 * @param outParams
	 *            命令执行后对应的参数
	 * @param op
	 *            结果处理信息
	 * @param target
	 *            结果对应实例
	 */
	public void handler(CloseableIterator<CIMInstance> ei, String[] outParams, CIMResultParams op, Object target);
}
