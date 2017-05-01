package pw.cdmi.om.protocol.snmp.mib;


/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public interface MibOID {
	public String getOID();
	
//	public int getVariableNumber();
	
//	public HashSet<String> getFieldOIDs();
	
	public int getMode();
	
	public String getParent();
	
	public int size();

}
