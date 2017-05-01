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
 *            2006-04-27  wolfalex     Full implementation 
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2899859    2009-11-18  blaschke-oss javax.wbem.client missing JSR48 credential/principal APIs
 * 2959264    2010-02-25  blaschke-oss Sync up javax.client.* javadoc with JSR48 1.0.0
 */
package javax.wbem.client;

import java.security.Principal;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * <code>UserPrincipal</code> implements a Principal identity for a client user
 * identity that authenticates with a username and password. In other words, it
 * represents the user's login identity on the remote system.
 * <code>UserPrincipal</code> includes the username and optionally the host
 * information for which the username is used to authenticate.
 */
public class UserPrincipal extends Object implements Principal {

	private String iUserName;

	private String iHostName;

	/**
	 * This constructor accepts the user name.
	 * 
	 * @param pUserName
	 *            The user login name.
	 * @throws IllegalArgumentException
	 *             If the <codepUuserName</code> is <code>null</code>.
	 */
	public UserPrincipal(String pUserName) throws IllegalArgumentException {
		if (pUserName == null) throw new IllegalArgumentException("userName is null!");
		this.iUserName = pUserName;
	}

	/**
	 * This constructor accepts the user name and host name.
	 * 
	 * @param pUserName
	 *            The user login name.
	 * @param pHostName
	 *            The host name for this principal.
	 * @throws IllegalArgumentException
	 *             If the <code>pUserName</code> is <code>null</code>.
	 */
	public UserPrincipal(String pUserName, String pHostName) throws IllegalArgumentException {
		if (pUserName == null) throw new IllegalArgumentException("userName is null!");
		this.iUserName = pUserName;
		this.iHostName = pHostName;
	}

	/**
	 * The <code>equals</code> method checks if the specified object is the same
	 * principal as this object. The principals are equal if the specified
	 * object is an instance of <code>UserPrincipal</code> and the user name and
	 * authentication host name are the same.
	 * 
	 * @param pOtherPrincipal
	 *            <code>Principal</code> instance to compare for equality.
	 * @return <code>true</code> if the object are equal; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pOtherPrincipal) {
		if (!(pOtherPrincipal instanceof UserPrincipal)) return false;
		UserPrincipal that = (UserPrincipal) pOtherPrincipal;
		return (this.iUserName == null ? that.iUserName == null : this.iUserName
				.equalsIgnoreCase(that.iUserName))
				&& (this.iHostName == null ? that.iHostName == null : this.iHostName
						.equalsIgnoreCase(that.iHostName));
	}

	/**
	 * Return the host name associated with this principal.
	 * 
	 * @return The host name.
	 */
	public String getHostName() {
		return this.iHostName;
	}

	/**
	 * Return the name of this principal identity; that is, return the login
	 * name.
	 * 
	 * @return The name of this principal identity.
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return this.iUserName;
	}

	/**
	 * Return the principal's login user name.
	 * 
	 * @return The user login name.
	 */
	public String getUserName() {
		return this.iUserName;
	}

	/**
	 * The <code>hashCode</code> method returns an integer hash code to
	 * represent this principal. It can be used to test for non-equality, or as
	 * an index key in a hash table.
	 * 
	 * @return An integer hash code representing the principal.
	 */
	@Override
	public int hashCode() {
		return this.iUserName != null ? this.iUserName.hashCode() : 0;
	}

	/**
	 * The <code>toString</code> method returns a string representation of the
	 * principal suitable for displaying in messages. It should not be used for
	 * making authorization checks, however.
	 * 
	 * @return A printable string form of the principal identity.
	 */
	@Override
	public String toString() {
		return this.iUserName;
	}

}
