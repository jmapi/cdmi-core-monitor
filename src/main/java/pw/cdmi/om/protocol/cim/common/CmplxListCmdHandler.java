package pw.cdmi.om.protocol.cim.common;

import java.util.ArrayList;
import java.util.List;

import javax.cim.CIMInstance;
import javax.cim.CIMProperty;
import javax.wbem.CloseableIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pw.cdmi.om.protocol.cim.cmd.CIMCmdResultParam;
import pw.cdmi.om.protocol.cim.cmd.CIMResultParams;

public class CmplxListCmdHandler implements ICommandHandler {
	Log log = LogFactory.getLog(CmplxListCmdHandler.class);
	@Override
	public void handler(CloseableIterator<CIMInstance> ei, String[] outParams, CIMResultParams rp, Object target) {
		log.debug("值处理<CmplxListCmdHandler>，值处理开始！   命令："+rp.getCmdName());
		
		List<Object> list = new ArrayList<Object>();
		
		while(ei.hasNext()){
			CIMInstance temp = (CIMInstance)ei.next();
			Object value = null;							//目前只支持List一个参数
			for(CIMProperty<?> a : temp.getProperties()){
				if(a.getName().equals(outParams[0]))
					value = a.getValue();
			}			
			list.add(value);
		}
		log.debug("值处理<CmplxListCmdHandler>，值处理中，完成LIST赋值！ 命令名："+rp.getCmdName());
		
		CIMCmdResultParam cmdInfo = rp.get(outParams[0]);			
		try {
			cmdInfo.getValueHandler().handle(target, list, cmdInfo.getParam());			
		} catch (Exception e) {
			log.error("值处理<CmplxListCmdHandler>，向目录实体赋值时，出错！ <名字>："+rp.getCmdName());
		}
		log.debug("值处理<CmplxListCmdHandler>，值处理完成！  命令："+rp.getCmdName());	
	}

}
