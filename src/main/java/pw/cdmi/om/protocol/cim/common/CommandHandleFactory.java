package pw.cdmi.om.protocol.cim.common;

import java.util.HashMap;
import java.util.Map;

public class CommandHandleFactory {
	private static Map<String, ICommandHandler> handlers = new HashMap<String, ICommandHandler>();
	static{
		handlers.put("[", new SimpleCmdHandler()); // [abcd,abcd,abcd]
		handlers.put("m", new CmplxMapCmdHandler());// map<ab,ag>
		handlers.put("l", new CmplxListCmdHandler());//list<name>
	}
	public static ICommandHandler getCmdHandler(String flag) throws Exception {
		ICommandHandler handler = handlers.get(flag);
		if(handler!=null)
			return handler;
		else
			throw new Exception("没有找到对应的命令处理器，标志为："+flag);
	}
}
