package pw.cdmi.om.protocol.snmp.storage;

public enum FailureStatus {
	Normal(1),
	Acute(2),//Acute failure detected
	Serious(3),//Serious failure detected
	Moderate(4),//Serious failure detected
	Service(5);//Serious failure detected
	
	private int value;
	private FailureStatus(int value){
		this.value = value;
	}
	
	public static FailureStatus fromValue(int value){
		FailureStatus[] list = FailureStatus.values();
		for(FailureStatus status : list){
			if(status.getValue() == value){
				return status;
			}
		}
		return null;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public String toString(){
		return this.name() + " failure detected";
	}
}
