package pw.cdmi.om.protocol.cim.common;

import javax.cim.CIMInstance;
import javax.cim.CIMProperty;
import javax.wbem.CloseableIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pw.cdmi.om.protocol.cim.cmd.CIMCmdResultParam;
import pw.cdmi.om.protocol.cim.cmd.CIMResultParams;
/**
 * 用于处理简单命令结果数据
 * @author super_cc
 *
 */
public class SimpleCmdHandler implements ICommandHandler {
	Log log = LogFactory.getLog(SimpleCmdHandler.class);
	@Override
	public void handler(CloseableIterator<CIMInstance> ei, String[] outParams, CIMResultParams rp, Object target) {
		log.debug("值处理<SimpleCommandHandler>，值处理开始！   命令："+rp.getCmdName());
		while(ei.hasNext()){
			CIMInstance temp = (CIMInstance)ei.next();
			
			for(CIMProperty<?> a : temp.getProperties()){
				String name = a.getName();
				try {
					CIMCmdResultParam cmdInfo = rp.get(name);
					if(cmdInfo!=null)
						cmdInfo.getValueHandler().handle(target, a.getValue(), cmdInfo.getParam());	
				} catch (Exception e) {
					log.error("值处理<SimpleCommandHandler>，出错！ <名字>："+name+" <值>："+a.getValue().toString());
				}
			}
		}
		log.debug("值处理<SimpleCommandHandler>，值处理完成！  命令："+rp.getCmdName());
	}

}
