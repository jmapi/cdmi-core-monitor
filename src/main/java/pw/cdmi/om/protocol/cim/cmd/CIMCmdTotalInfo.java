package pw.cdmi.om.protocol.cim.cmd;

import pw.cdmi.om.protocol.cim.CIMCmdConfig;
import pw.cdmi.om.protocol.cim.CIMObjectParam;
import pw.cdmi.om.protocol.cim.common.CommandHandleFactory;
import pw.cdmi.om.protocol.cim.common.ICommandHandler;

/**
 * CIM命令信息
 * @author super_cc
 *
 */
public class CIMCmdTotalInfo {
	
	private String cmdName;						//命令
	
	private CIMCmdConfig cmdConfig;				//命令及返回结果参数

	private CIMResultParams resultConfig = new CIMResultParams();		//结查处理参数集

	/**
	 * 命令名字
	 */
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	/**  
	 * 命令配置信息
	 *
	 * @return
	 */		
	public CIMCmdConfig getCmdConfig() {
		return cmdConfig;
	}
	public void setCmdConfig(CIMCmdConfig cmdConfig) {
		this.cmdConfig = cmdConfig;
	}
	/**  
	 * 命令结果配置信息
	 *
	 * @return
	 */	
	public CIMResultParams getResultConfig() {
		return resultConfig;
	}

	public void setResultConfig(CIMResultParams resultConfig) {
		this.resultConfig = resultConfig;
	}

	/**
	 * 新增结果配置信息
	 * @param o
	 */
	public void addResultParam(CIMObjectParam o){
		this.resultConfig.put(o.getParamName(), o);
	}
	/**
	 * 获取所以的命令执行的属性
	 * @param o
	 */
	public String[] getCmdPropertys(){
		String outp = this.cmdConfig.getOutParams();
		return this.formatOutParams(outp).split(",");
	}	
	
	/**
	 * 获取命令处理值处理器
	 * @return
	 * @throws Exception
	 */
	public ICommandHandler getHander() throws Exception{
		return CommandHandleFactory.getCmdHandler(this.cmdConfig.getOutParams().trim().substring(0, 1));
	}
	
	private String formatOutParams(String outp){
		int lenth = outp.length();
		int i = outp.indexOf("<");
		if(i>1)
			return outp.substring(i+1, lenth-1);
		else
			return outp.substring(1, lenth-1);
	}
}
