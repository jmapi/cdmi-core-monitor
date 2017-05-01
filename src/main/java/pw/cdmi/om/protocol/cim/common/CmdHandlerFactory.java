package pw.cdmi.om.protocol.cim.common;

public class CmdHandlerFactory {
	/**
	 * 根据处理类型生成命令处理器
	 * @param procType
	 * @return
	 */
	public static ICommandHandler getHandler(int procType){
		if(procType == 1){
			return new SimpleCmdHandler();
		}
//		else if()
		return null;
	}
}
