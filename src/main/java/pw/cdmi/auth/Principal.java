package pw.cdmi.auth;

public class Principal {
	
	public static final Principal AllUsers = new Principal("*");
	private final String id;
	
    public Principal(String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("Null account ID specified");
        }
        this.id = identifier.replaceAll("-", "");
    }
    
    public String getProvider() {
        return "AWS";
    }
    
	public String getIdentifier(){
		return this.id;
	}
}
