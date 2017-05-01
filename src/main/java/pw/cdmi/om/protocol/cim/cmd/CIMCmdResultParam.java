package pw.cdmi.om.protocol.cim.cmd;


import pw.cdmi.om.protocol.cim.CIMObjectParam;
import pw.cdmi.om.protocol.cim.common.IValueHandler;
import pw.cdmi.om.protocol.cim.common.ValueHandlerFactory;
/**
 * 命令信息
 * @author super_cc
 *
 */
public class CIMCmdResultParam {

	private CIMObjectParam param;
	
	public CIMObjectParam getParam() {
		return param;
	}

	public void setParam(CIMObjectParam param) {
		this.param = param;
	}

	public CIMCmdResultParam(CIMObjectParam param){
		this.param = param;
	}
	
	public IValueHandler getValueHandler() throws Exception{
		return ValueHandlerFactory.getValueHandler(param.getHandler());
	}
}
