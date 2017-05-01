package pw.cdmi.om.protocol.cim.common;

import java.util.HashMap;
import java.util.Map;

import javax.cim.CIMInstance;
import javax.cim.CIMProperty;
import javax.wbem.CloseableIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pw.cdmi.om.protocol.cim.cmd.CIMCmdResultParam;
import pw.cdmi.om.protocol.cim.cmd.CIMResultParams;

public class CmplxMapCmdHandler implements ICommandHandler {
	Log log = LogFactory.getLog(CmplxMapCmdHandler.class);
	@Override
	public void handler(CloseableIterator<CIMInstance> ei, String[] outParams, CIMResultParams rp, Object target) {
		log.debug("值处理<CmplxMapCmdHandler>，值处理开始！   命令："+rp.getCmdName());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		while(ei.hasNext()){
			CIMInstance temp = (CIMInstance)ei.next();
			String name = null;
			Object value = null;
			for(CIMProperty<?> a : temp.getProperties()){
				if(a.getName().equals(outParams[0]))
					name = a.getValue().toString();
				else if(a.getName().equals(outParams[1]))
					value = a.getValue();
			}
			if((name!=null)&&(value!=null)){
				map.put(name, value);
			}
		}
		log.debug("值处理<CmplxMapCmdHandler>，值处理中，完成MAP赋值！ 命令名："+rp.getCmdName());
		try {
			for(String param: rp.getAllParams()){
				CIMCmdResultParam cmdInfo = rp.get(param);			
				cmdInfo.getValueHandler().handle(target, map.get(param), cmdInfo.getParam());	
			}
		} catch (Exception e) {
			log.error("值处理<CmplxMapCmdHandler>，向目录实体赋值时，出错！ <名字>："+rp.getCmdName());
		}
		log.debug("值处理<CmplxMapCmdHandler>，值处理完成！  命令："+rp.getCmdName());	
	}

}
