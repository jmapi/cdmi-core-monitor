package pw.cdmi.om.protocol.smi;

public enum SMINameSpace {
	LSI("root/lsissi11"),
	ThirdPAR("root/tpd"),
	EMC("root/emc"),
	HP("root/eva"),
	IBM("root/ibm"),//
	HDS58A("root/smis/current"),//versions after 5.8
	HDS58B("root/hitachi/dm");	//versions prior to 5.8
	
	private String ns;
	
	private SMINameSpace(String namespace){
		this.ns = namespace;
	}
	
	public String getNameSpace(){
		return this.ns;
	}
}
