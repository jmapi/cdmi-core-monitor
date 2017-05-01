package pw.cdmi.om.protocol.cim.cmd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pw.cdmi.om.protocol.cim.CIMObjectParam;

public class CIMResultParams {
	
	private String cmdName = null;
	private Map<String, CIMCmdResultParam> params = new HashMap<String, CIMCmdResultParam>();
	/**
	 * 放入字段命令相关信息
	 * @param paramName 命令输出对应参数名
	 * @param param		
	 */
	public void put(String paramName, CIMObjectParam param){
		this.cmdName = param.getObjectName();
		this.params.put(paramName, new CIMCmdResultParam(param));
	}
	/**
	 * 取出字段命令信息
	 * @param paraName
	 * @return
	 */
	public CIMCmdResultParam get(String paraName){
		return this.params.get(paraName);
	}
	/**
	 * 取出所有的属性
	 * @return
	 */
	public Set<String> getAllParams(){
		return this.params.keySet();
	}
	
	/**
	 * 获取命令名
	 * @return
	 */
	public String getCmdName() {
		return cmdName;
	}
	
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	
	public String[] getPOClassNames(){
		Set<String> clsSet= new HashSet<String>();
		for(CIMCmdResultParam rp : params.values()){
			clsSet.add(rp.getParam().getOrObject().trim());
		}
		return (String[])clsSet.toArray();
	}
}
