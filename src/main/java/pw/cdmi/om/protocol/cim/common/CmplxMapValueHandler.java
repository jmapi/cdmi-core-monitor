package pw.cdmi.om.protocol.cim.common;

import java.util.List;
import java.util.Map;

import pw.cdmi.om.protocol.cim.CIMObjectParam;
/**
 * 用于
 * @author super_cc
 *
 */
public class CmplxMapValueHandler extends DefaultValueHandler{
//implements IValueHandler {
	@Override
	public void handle(Object target, Object value, CIMObjectParam cimop) {
		if(value instanceof List){
			Map<String, Object> vs = (Map<String, Object>) value;
			for(String key : vs.keySet()){
				Object v = vs.get(key);
				super.handle(target, v, cimop);
			}
		}
	}
}
