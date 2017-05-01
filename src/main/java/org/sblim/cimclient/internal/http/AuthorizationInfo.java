/**
 * (C) Copyright IBM Corp. 2005, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 *   17970    2005-08-11  pineiro5     Logon from z/OS not possible
 * 1516242    2006-07-05  lupusalex    Support of OpenPegasus local authentication
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.cimclient.internal.http;

import java.net.PasswordAuthentication;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.logging.Messages;
import org.sblim.cimclient.internal.util.WBEMConfiguration;

/**
 * Abstract superclass for HTTP authorization information.
 * 
 * @see org.sblim.cimclient.internal.http.WwwAuthInfo
 * @see org.sblim.cimclient.internal.http.PegasusLocalAuthInfo
 */
public abstract class AuthorizationInfo {

	protected String iAddr;

	protected int iPort;

	protected String iProtocol;

	protected String iRealm;

	protected String iScheme;

	protected PasswordAuthentication iCredentials;

	protected long iNc = 1;

	protected String iCnonce;

	protected String iOpaque;

	protected String iAlgorithm;

	protected String iUri;

	protected String iNonce;

	protected String iQop;

	protected String iA1;

	protected String iResponse;

	// protected byte[] iDigest;

	/**
	 * Initialize
	 * 
	 * @param pProxy
	 *            Proxy authentication ?
	 * @param pAddress
	 *            Server address
	 * @param pPort
	 *            Server port
	 * @param pProtocol
	 *            Protocol
	 * @param pRealm
	 *            Realm
	 * @param pScheme
	 *            Scheme
	 */
	public void init(Boolean pProxy, String pAddress, int pPort, String pProtocol, String pRealm,
			String pScheme) {
		this.iAddr = pAddress;
		this.iPort = pPort;
		this.iProtocol = pProtocol;
		this.iRealm = pRealm;
		this.iScheme = pScheme;
	}

	/**
	 * Sets opaque
	 * 
	 * @param opaque
	 *            new Value
	 */
	public void setOpaque(String opaque) {
		this.iOpaque = opaque;
	}

	/**
	 * Returns opaque
	 * 
	 * @return Opaque
	 */
	public String getOpaque() {
		return this.iOpaque;
	}

	/**
	 * Returns Qop
	 * 
	 * @return Qop
	 */
	public String getQop() {
		return this.iQop;
	}

	/**
	 * Sets Qop
	 * 
	 * @param qop
	 *            New value
	 */
	public void setQop(String qop) {
		this.iQop = qop;
	}

	/**
	 * Returns nc
	 * 
	 * @return nc
	 */
	public long getNc() {
		return this.iNc;
	}

	/**
	 * Sets nc
	 * 
	 * @param nc
	 *            New value
	 */
	public void setNc(long nc) {
		this.iNc = nc;
	}

	/**
	 * Sets nonce
	 * 
	 * @param nonce
	 *            New Value
	 */
	public void setNonce(String nonce) {
		this.iNonce = nonce;
	}

	/**
	 * Returns nonce
	 * 
	 * @return nonce
	 */
	public String getNonce() {
		return this.iNonce;
	}

	/**
	 * Set cnonce
	 * 
	 * @param cnonce
	 *            New value
	 */
	public void setCnonce(String cnonce) {
		this.iCnonce = cnonce;
	}

	/**
	 * Returns cnonce
	 * 
	 * @return cnonce
	 */
	public String getCnonce() {
		return this.iCnonce;
	}

	/**
	 * Set algorithm
	 * 
	 * @param algorithm
	 *            New value
	 */
	public void setAlgorithm(String algorithm) {
		this.iAlgorithm = algorithm;
	}

	/**
	 * Returns algorithm
	 * 
	 * @return algorithm
	 */
	public String getAlgorithm() {
		return this.iAlgorithm;
	}

	/**
	 * Returns A1
	 * 
	 * @return A1
	 */
	public String getA1() {
		return this.iA1;
	}

	/**
	 * Sets A1
	 * 
	 * @param A1
	 *            New value
	 */
	public void setA1(String A1) {
		this.iA1 = A1;
	}

	/**
	 * Sets response
	 * 
	 * @param response
	 *            New value
	 */
	public void setResponse(String response) {
		this.iResponse = response;
	}

	/**
	 * Returns response
	 * 
	 * @return New value
	 */
	public String getResponse() {
		return this.iResponse;
	}

	/**
	 * Returns URI
	 * 
	 * @return URI
	 */
	public String getURI() {
		return this.iUri;
	}

	/**
	 * Sets URI
	 * 
	 * @param uri
	 *            New value
	 */
	public void setURI(String uri) {
		this.iUri = uri;
	}

	/**
	 * Sets credentials
	 * 
	 * @param credentials
	 *            New value
	 */
	public void setCredentials(PasswordAuthentication credentials) {
		this.iCredentials = credentials;
	}

	/**
	 * Returns the address
	 * 
	 * @return The server address
	 */
	public String getAddr() {
		return this.iAddr;
	}

	/**
	 * Returns the port
	 * 
	 * @return The server port
	 */
	public int getPort() {
		return this.iPort;
	}

	/**
	 * Returns the protocol
	 * 
	 * @return The protocol
	 */
	public String getProtocol() {
		return this.iProtocol;
	}

	/**
	 * Returns the realm
	 * 
	 * @return The realm
	 */
	public String getRealm() {
		return this.iRealm;
	}

	/**
	 * Sets the realm
	 * 
	 * @param realm
	 *            New value
	 */
	public void setRealm(String realm) {
		this.iRealm = realm;
	}

	/**
	 * Returns the scheme
	 * 
	 * @return The scheme
	 */
	public String getScheme() {
		return this.iScheme;
	}

	/**
	 * Sets the scheme
	 * 
	 * @param scheme
	 *            New value
	 */
	public void setScheme(String scheme) {
		this.iScheme = scheme;
	}

	/**
	 * Returns the credentials
	 * 
	 * @return The credentials
	 */
	public PasswordAuthentication getCredentials() {
		return this.iCredentials;
	}

	/**
	 * Compares two authorization informations.
	 * 
	 * @param obj
	 *            The other authorization information
	 * @return <code>true</code> if type, realm, scheme, address, protocol and
	 *         port of both authorization informations are equal,
	 *         <code>false</code> otherwise.
	 */
	public boolean match(Object obj) {
		if (obj == null || !(obj instanceof AuthorizationInfo)) return false;
		AuthorizationInfo that = (AuthorizationInfo) obj;

		boolean type = getClass().equals(that.getClass());
		// boolean prxt = (iProxy == null || that.iProxy == null)?
		// true:iProxy.equals(that.iProxy);
		boolean prmpt = (this.iRealm == null || that.iRealm == null) ? true : this.iRealm
				.equals(that.iRealm);
		boolean schm = (this.iScheme == null || that.iScheme == null) ? true : this.iScheme
				.equals(that.iScheme);
		boolean adr = (this.iAddr == null || that.iAddr == null) ? true : this.iAddr
				.equals(that.iAddr);
		boolean prot = (this.iProtocol == null || that.iProtocol == null) ? true : this.iProtocol
				.equals(that.iProtocol);
		boolean prt = (this.iPort <= 0 || that.iPort <= 0) ? true : (this.iPort == that.iPort);
		return (type && prmpt && schm && adr && prot && prt);
	}

	/**
	 * Updates the authorization information according to a received challenge.
	 * 
	 * @param challenge
	 *            The received challenge
	 * @param authenticate
	 *            The authenticate header field
	 * @param url
	 *            The url of the CIM server
	 * @param requestMethod
	 *            The HTTP request method (POST or MPOST)
	 * @throws NoSuchAlgorithmException
	 */
	public abstract void updateAuthenticationInfo(Challenge challenge, String authenticate,
			URI url, String requestMethod) throws NoSuchAlgorithmException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();

	/**
	 * Gets the HTTP header field name for this authentication information
	 * 
	 * @return The field name
	 */
	public abstract String getHeaderFieldName();

	/**
	 * Determines if the authorization information is already sent on the very
	 * first http request or after the "401 Unauthorized" response
	 * 
	 * @return <code>true</code> or <code>false</code>
	 */
	public abstract boolean isSentOnFirstRequest();

	/**
	 * Determines if the connection is kept alive after the "401 Unauthorized"
	 * response
	 * 
	 * @return <code>true</code> or <code>false</code>
	 */
	public abstract boolean isKeptAlive();

	/**
	 * Factory method for AuthorizationInfo objects. Returns an instance of a
	 * subclass according to the requested type.
	 * 
	 * @param pModule
	 *            The authorization info type to be constructed
	 * @param pProxy
	 *            Proxy authentication ?
	 * @param pAddress
	 *            Server address
	 * @param pPort
	 *            Server port
	 * @param pProtocol
	 *            Protocol (http/https)
	 * @param pRealm
	 *            Realm
	 * @param pScheme
	 *            Scheme (e.g. Basic, Digest)
	 * @return An instance of a AuthorizationInfo subclass or <code>null</code>
	 * @see WBEMConfiguration#getHttpAuthenticationModule()
	 * @see WwwAuthInfo
	 * @see PegasusLocalAuthInfo
	 */
	public static AuthorizationInfo createAuthorizationInfo(String pModule, Boolean pProxy,
			String pAddress, int pPort, String pProtocol, String pRealm, String pScheme) {

		AuthorizationInfo info = createAuthorizationInfo(pModule);

		if (info != null) {
			info.init(pProxy, pAddress, pPort, pProtocol, pRealm, pScheme);
		}

		return info;

	}

	/**
	 * Factory method for AuthorizationInfo objects. Returns an instance of a
	 * subclass according to the requested type.
	 * 
	 * @param pModule
	 *            The authorization info type to be constructed
	 * @return An instance of a AuthorizationInfo subclass or <code>null</code>
	 */
	public static AuthorizationInfo createAuthorizationInfo(String pModule) {

		if (WwwAuthInfo.class.getName().equals(pModule)) { return new WwwAuthInfo(); }
		if (PegasusLocalAuthInfo.class.getName().equals(pModule)) { return new PegasusLocalAuthInfo(); }

		try {
			Class<?> module = Class.forName(pModule);
			AuthorizationInfo info = (AuthorizationInfo) module.newInstance();
			return info;
		} catch (Exception e) {
			LogAndTraceBroker.getBroker().trace(Level.FINER,
					"Exception while loading authentication module", e);
			LogAndTraceBroker.getBroker().message(Messages.HTTP_AUTH_MODULE_INVALID, pModule);
		}
		return null;
	}
}
