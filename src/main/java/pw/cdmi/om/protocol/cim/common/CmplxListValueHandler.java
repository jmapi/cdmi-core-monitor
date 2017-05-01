package pw.cdmi.om.protocol.cim.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pw.cdmi.om.protocol.cim.CIMObjectParam;
/**
 * 用于
 * @author super_cc
 *
 */
public class CmplxListValueHandler extends DefaultValueHandler{
//implements IValueHandler {
	@Override
	public void handle(Object target, Object value, CIMObjectParam cimop) {
		if(value instanceof List){
			Set<String> s = new HashSet<String>();
			List<Object> list = (List<Object>)value;
			for(Object o : list){
				s.add(o.toString().split(":")[3]);
			}
			if(s.size()>1)
				super.handle(target, "MIXED", cimop);
			else
				super.handle(target, "SINGLE", cimop);
		}
	}
}
