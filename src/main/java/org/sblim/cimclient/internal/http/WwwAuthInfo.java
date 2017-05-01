/**
 * (C) Copyright IBM Corp. 2006, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com 
 * 
 * Change History
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1536711    2006-08-15  lupusalex    NullPointerException causes client call to never return 
 * 1516242    2006-11-27  lupusalex    Support of OpenPegasus local authentication
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1627832    2007-01-08  lupuslaex    Incorrect retry behaviour on HTTP 401
 * 1892046    2008-02-13  blaschke-oss Basic/digest authentication problem for Japanese users
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2714989    2009-03-26  blaschke-oss Code cleanup from redundant null check et al
 */
package org.sblim.cimclient.internal.http;

import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

/**
 * Implements HTTP basic and digest authentication
 */
public class WwwAuthInfo extends AuthorizationInfo {

	/**
	 * Default ctor.
	 */
	public WwwAuthInfo() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		String _nc = Long.toHexString(this.iNc);

		if (this.iScheme.equalsIgnoreCase("Digest")) {

			if (this.iRealm == null) { // support for some ICAT CIMOMs buggy
				// Digest authentication
				try {
					MessageDigest messageDigest = null;
					messageDigest = MessageDigest.getInstance("MD5");

					messageDigest.update(getBytes((this.iCredentials != null && this.iCredentials
							.getUserName() != null) ? String.valueOf(this.iCredentials
							.getPassword()) : "null", "UTF-8"));
					String pass = HttpClient.convertToHexString(messageDigest.digest());
					return "Digest username="
							+ ((this.iCredentials != null && this.iCredentials.getUserName() != null) ? this.iCredentials
									.getUserName()
									: "null") + ", response=" + pass;
				} catch (NoSuchAlgorithmException e) {
					// TODO log
					e.printStackTrace();
				}
			}

			result.append(this.iScheme);
			result.append(" username=\"");
			result
					.append((this.iCredentials != null && this.iCredentials.getUserName() != null) ? this.iCredentials
							.getUserName()
							: "null");
			result.append("\"");
			if (this.iRealm != null) {
				result.append(", realm=\"");
				result.append(this.iRealm);
				result.append("\"");
			}
			if (this.iNonce != null) {
				result.append(", nonce=\"");
				result.append(this.iNonce);
				result.append("\"");
			}
			result.append(", uri=\"");
			result.append(this.iUri);
			result.append("\", response=\"");
			result.append(this.iResponse);
			result.append("\"");
			// if (algorithm != null) {
			// result.append(", algorithm=");
			// result.append(algorithm);
			// }
			if (this.iCnonce != null) {
				result.append(", cnonce=\"");
				result.append(this.iCnonce);
				result.append("\"");
			}
			if (this.iOpaque != null) {
				result.append(", opaque=\"");
				result.append(this.iOpaque);
				result.append("\"");
			}
			if (this.iQop != null) {
				result.append(", qop=");
				result.append(this.iQop);
			}
			if (this.iNc > -1) {
				result.append(", nc=");
				result.append("00000000".substring(_nc.length()));
				result.append(_nc);
			}
		} else if (this.iScheme.equalsIgnoreCase("Basic")) {

			result.append("Basic ");

			StringBuffer tmp = new StringBuffer();
			tmp
					.append((this.iCredentials != null && this.iCredentials.getUserName() != null) ? this.iCredentials
							.getUserName()
							: "null");
			tmp.append(':');
			tmp
					.append((this.iCredentials != null && this.iCredentials.getPassword() != null) ? String
							.valueOf(this.iCredentials.getPassword())
							: "null");

			result.append(BASE64Encoder.encode(getBytes(tmp.toString(), "UTF-8")));
		}
		return result.toString();
	}

	/**
	 * Splits a comma-separated string into multiple substrings
	 * 
	 * @param pLine
	 *            The comma-separated string
	 * @return The array of substrings (excluding commas)
	 */
	public static String[] split(String pLine) {
		Vector<String> elem = new Vector<String>();
		int start = 0;
		int end;
		while ((end = pLine.indexOf(',')) > -1) {
			elem.add(pLine.substring(start, end));
			start = end + 1;
		}
		if (end < pLine.length()) elem.add(pLine.substring(start));
		String[] result = new String[elem.size()];
		elem.toArray(result);
		return result;
	}

	private static byte[] getBytes(String str, String encoding) {
		byte[] bytes;
		try {
			bytes = str.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			bytes = str.getBytes();
		}
		return bytes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.internal.http.AuthorizationInfo#updateAuthenticationInfo
	 * (org.sblim.cimclient.internal.http.Challenge, java.net.URI,
	 * java.lang.String)
	 */
	/**
	 * @param authenticate
	 */
	@Override
	public void updateAuthenticationInfo(Challenge challenge, String authenticate, URI url,
			String requestMethod) throws NoSuchAlgorithmException {

		setURI(url.getPath());
		HttpHeader params = challenge.getParams();

		this.iScheme = challenge.getScheme();

		if (!this.iScheme.equalsIgnoreCase("Digest")) { return; }

		this.iRealm = params.getField("realm");

		String algorithm = params.getField("algorithm");
		String opaque = params.getField("opaque");
		String nonce = params.getField("nonce");
		String qop = params.getField("qop");

		this.iAlgorithm = (algorithm != null) ? algorithm : this.iAlgorithm;
		this.iOpaque = (opaque != null) ? opaque : this.iOpaque;
		this.iNonce = (nonce != null) ? nonce : this.iNonce;
		this.iQop = (qop != null) ? qop : this.iQop;

		MessageDigest messageDigest = null;

		messageDigest = MessageDigest.getInstance("MD5");

		if (qop != null || ("md5-sess".equalsIgnoreCase(algorithm))) {
			long time = System.currentTimeMillis();
			byte[] b = new byte[8];

			b[0] = (byte) ((time >> 0) & 0xFF);
			b[1] = (byte) ((time >> 8) & 0xFF);
			b[2] = (byte) ((time >> 16) & 0xFF);
			b[3] = (byte) ((time >> 24) & 0xFF);
			b[4] = (byte) ((time >> 32) & 0xFF);
			b[5] = (byte) ((time >> 40) & 0xFF);
			b[6] = (byte) ((time >> 48) & 0xFF);
			b[7] = (byte) ((time >> 56) & 0xFF);

			messageDigest.reset();
			messageDigest.update(b);

			this.iCnonce = HttpClient.convertToHexString(messageDigest.digest());
		}
		if (qop != null) {
			String[] list_qop = split(qop);
			for (int i = 0; i < list_qop.length; i++) {
				if (list_qop[i].equalsIgnoreCase("auth-int")) {
					qop = "auth-int"; // this one has higher priority
					break;
				}
				if ((list_qop[i].equalsIgnoreCase("auth"))) qop = "auth";
			}
			setQop(qop);
		}
		String nc1 = params.getField("nc");
		long challengeNc = 1;
		if (nc1 != null) {
			try {
				challengeNc = Long.parseLong(nc1, 16);
			} catch (Exception e) {
				// Logger logger = SessionProperties.getGlobalProperties()
				// .getLogger();
				// if (logger.isLoggable(Level.WARNING)) {
				// logger.log(Level.WARNING,
				// "exception while parsing challenge NC", e);
				// }
			}
			if (getNc() == challengeNc) {
				setNc(++challengeNc);
			}
		} else {
			setNc(challengeNc = 1);
		}

		messageDigest.reset();
		PasswordAuthentication credentials1 = getCredentials();
		this.iA1 = credentials1.getUserName() + ":" + getRealm() + ":"
				+ String.valueOf(credentials1.getPassword());
		// System.out.println("Base:"+A1);
		messageDigest.update(getBytes(this.iA1, "UTF-8"));

		if ("md5-sess".equalsIgnoreCase(algorithm)) {
			messageDigest.update(getBytes((":" + getNonce() + ":" + getCnonce()), "UTF-8"));

		}

		byte[] digest1 = messageDigest.digest();
		String sessionKey = HttpClient.convertToHexString(digest1);

		// System.out.println("A1:"+sessionKey);
		String method = requestMethod;

		// if (con instanceof HttpURLConnection)
		// method = ((HttpURLConnection) con).getRequestMethod();

		String A2 = method + ":" + getURI();

		if ("auth-int".equalsIgnoreCase(qop)) {
			messageDigest.reset();
			// messageDigest.update(readFully(con)); //TODO
			messageDigest.update(new byte[] {}); // TODO this must be the
			// entity body, not the
			// message body
			A2 = A2 + ":" + HttpClient.convertToHexString(messageDigest.digest());
		}
		messageDigest.reset();
		messageDigest.update(getBytes(A2, "UTF-8"));
		A2 = HttpClient.convertToHexString(messageDigest.digest());

		messageDigest.reset();
		if (qop == null) {
			messageDigest.update(getBytes((sessionKey + ":" + nonce + ":" + A2), "UTF-8"));
		} else {
			String _nc = Long.toHexString(challengeNc);
			messageDigest.update(getBytes((sessionKey + ":" + nonce + ":"
					+ "00000000".substring(_nc.length()) + _nc + ":" + getCnonce() + ":" + qop
					+ ":" + A2), "UTF-8"));
		}
		this.iResponse = HttpClient.convertToHexString(messageDigest.digest());

		// TODO handle digest-required header
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.internal.http.AuthorizationInfo#getHeaderFieldName()
	 */
	@Override
	public String getHeaderFieldName() {
		return "Authorization";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.internal.http.AuthorizationInfo#isSentOnFirstRequest
	 * ()
	 */
	@Override
	public boolean isSentOnFirstRequest() {
		return false;
	}

	@Override
	public boolean isKeptAlive() {
		return true;
	}
}
