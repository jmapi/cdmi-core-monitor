/**
 * 
 */
package pw.cdmi.om.protocol.cim;

import javax.cim.CIMObjectPath;
import javax.security.auth.Subject;
import javax.wbem.WBEMException;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.client.WBEMClientFactory;

import org.springframework.stereotype.Component;

/**
 * @author super_cc
 *
 */
@Component
public class CIMClientService {

    public WBEMClient getClient(CIMTarget target) throws WBEMException {
        // String host = "http://7.71.51.191:9988/";
        StringBuffer sb = new StringBuffer();
        sb.append(target.getTelprot());
        sb.append("://");
        sb.append(target.getHostIp());
        sb.append(":");
        sb.append(target.getPort());
        sb.append("/");

        return this.getClient(sb.toString(), target.getUserName(), target.getPassword());
    }

    /**
     * 获得CIM客户端
     * @param host 		eg:"http://7.71.51.191:9988/";
     * @param username
     * @param password
     * @return
     * @throws WBEMException
     */
    public WBEMClient getClient(String host, String username, String password) throws WBEMException {
        WBEMClient clientObj = null;
        /*
        * Create an object path using the host variable.
        */
        CIMObjectPath cns = new CIMObjectPath(host);

        /*
        * Create the principal - used for authentication/authorization
        */
        UserPrincipal up = new UserPrincipal(username);

        /*
        * Create the credential - used for authentication/authorization
        */
        PasswordCredential pc = new PasswordCredential(password);

        /*
        * Add the principal and credential to the subject.
        */
        Subject s = new Subject();
        s.getPrincipals().add(up);
        s.getPrivateCredentials().add(pc);

        /*
        * Create a CIM client connection using the either CIM-XML or
        * WS-Management protocol
        */
        clientObj = WBEMClientFactory.getClient(WBEMClientConstants.PROTOCOL_CIMXML);
        clientObj.initialize(cns, s, null);

        return clientObj;
    }

}
