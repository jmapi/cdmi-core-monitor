package pw.cdmi.om.protocol.snmp;


/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月14日
 ************************************************************/
public class SnmpResponse {
	private String key;
	private String value;
	private Exception error;
	private int status = 0;
	private String errorMessage;
	
	public SnmpResponse(){
		
	}
	
	public SnmpResponse(String key, String value){
		this.key = key;
		this.value = value;
	}


	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}

