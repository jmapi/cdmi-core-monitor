package pw.cdmi.om.protocol.cim.common;

public class ValueHandlerFactory {
	public static IValueHandler getValueHandler(String flag) throws Exception{
		IValueHandler handler = null;
		if(flag == null)
			handler =  new DefaultValueHandler();
		else{
			handler = (IValueHandler) Class.forName(flag).newInstance();
		}
		return handler;
	}
}
