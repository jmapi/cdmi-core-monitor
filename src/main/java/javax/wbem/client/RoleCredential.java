/**
 * (C) Copyright IBM Corp. 2006, 2010
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, ebak@de.ibm.com  
 * 
 * Change History
 * Flag       Date        Prog         Description
 *-------------------------------------------------------------------------------------------------
 *            2006-04-25  ebak         Initial commit
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2899859    2009-11-18  blaschke-oss javax.wbem.client missing JSR48 credential/principal APIs
 * 2959264    2010-02-25  blaschke-oss Sync up javax.client.* javadoc with JSR48 1.0.0
 * 2978722    2010-03-29  blaschke-oss PasswordCredential(char[]) constructor is wrong
 */

package javax.wbem.client;

//Sync'd against JSR48 1.0.0 (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * <code>RoleCredential</code> implements a password based credential for a
 * role. <code>RoleCredential</code> includes the credential (e.g. password) and
 * optionally the host information for which the password is used to
 * authenticate the <code>RolePrincipal</code>. <code>RoleCredential</code>
 * should be used in conjunction with the <code>RolePrincipal</code> instance.
 * This should only be used when a client is assuming a role on a WBEM Server
 * that requires a password.
 */
public class RoleCredential extends Object {

	private String iCredential;

	private String iHostname;

	/**
	 * Creates a role credential using the supplied credential.
	 * 
	 * @param pCredential
	 *            The role credential in clear text.
	 * @throws IllegalArgumentException
	 *             If the credential is <code>null</code>.
	 */
	public RoleCredential(char[] pCredential) throws IllegalArgumentException {
		if (pCredential == null) throw new IllegalArgumentException("credential cannot be null!");
		this.iCredential = new String(pCredential);
	}

	/**
	 * Creates a role credential using the supplied credential.
	 * 
	 * @param pCredential
	 *            The role credential in clear text.
	 * @throws IllegalArgumentException
	 *             If the credential is <code>null</code>.
	 */
	public RoleCredential(String pCredential) throws IllegalArgumentException {
		if (pCredential == null) throw new IllegalArgumentException("credential cannot be null!");
		this.iCredential = pCredential;
	}

	/**
	 * Creates a role credential using the supplied credential and host name.
	 * 
	 * @param pCredential
	 *            The role credential in clear text.
	 * @param pHostname
	 *            - The host name for this credential.
	 * @throws IllegalArgumentException
	 *             If the credential is <code>null</code>.
	 */
	public RoleCredential(String pCredential, String pHostname) throws IllegalArgumentException {
		if (pCredential == null) throw new IllegalArgumentException("credential cannot be null!");
		this.iCredential = pCredential;
		this.iHostname = pHostname;
	}

	/**
	 * Return the role credential in clear text.
	 * 
	 * @return The role credential.
	 */
	public char[] getCredential() {
		return this.iCredential.toCharArray();
	}

	/**
	 * Get the host name for which the password is used to authenticate.
	 * 
	 * @return The host name.
	 */
	public String getHostName() {
		return this.iHostname;
	}

}
