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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2899859    2009-11-18  blaschke-oss javax.wbem.client missing JSR48 credential/principal APIs
 * 2959264    2010-02-25  blaschke-oss Sync up javax.client.* javadoc with JSR48 1.0.0
 */

package javax.wbem.client;

import java.security.Principal;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * <code>RolePrincipal</code> implements a Principal identity for a role. That
 * is, it represents the role name to be assumed on a system.
 * <code>RolePrincipal</code> includes the role and optionally the host
 * information for which the role is used to authenticate.
 */
public class RolePrincipal extends Object implements Principal {

	private String iRole;

	private String iHost;

	/**
	 * This constructor accepts the role name.
	 * 
	 * @param pRole
	 *            The role name.
	 */
	public RolePrincipal(String pRole) {
		this.iRole = pRole;
	}

	/**
	 * This constructor accepts the role name and host name.
	 * 
	 * @param pRole
	 *            The role name.
	 * @param pHost
	 *            The host name.
	 */
	public RolePrincipal(String pRole, String pHost) {
		this.iRole = pRole;
		this.iHost = pHost;
	}

	/**
	 * The <code>equals</code> method checks if the specified principal is the
	 * same principal as this object. The principals are equal if the specified
	 * object is an instance of <code>RolePrincipal</code> and the user name and
	 * host name are the same.
	 * 
	 * @param pObj
	 *            The Principal to compare for equality.
	 * @return <code>true</code> if they are equal, otherwise <code>false</code>
	 *         .
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof RolePrincipal)) return false;
		RolePrincipal that = (RolePrincipal) pObj;
		return (this.iRole == null ? that.iRole == null : this.iRole.equalsIgnoreCase(that.iRole))
				&& (this.iHost == null ? that.iHost == null : this.iHost
						.equalsIgnoreCase(that.iHost));
	}

	/**
	 * Get the host for which the principal uses to authenticate.
	 * 
	 * @return The host name.
	 */
	public String getHostName() {
		return this.iHost;
	}

	/**
	 * Return the name of this principal identity; that is, return the login
	 * name.
	 * 
	 * @return The name of this principal identity.
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return this.iRole;
	}

	/**
	 * The <code>hashCode()</code> method returns an integer hash code to
	 * represent this principal.
	 * 
	 * @return An integer hash code representing the principal.
	 */
	@Override
	public int hashCode() {
		return this.iRole == null ? 0 : this.iRole.hashCode();
	}

	/**
	 * The <code>toString()</code> method returns a string representation of the
	 * principal suitable for displaying in messages. It should not be used for
	 * making authorization checks.
	 * 
	 * @return A printable string form of the role principal.
	 */
	@Override
	public String toString() {
		return this.iRole;
	}

}
