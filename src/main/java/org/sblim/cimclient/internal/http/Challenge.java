/**
 * (C) Copyright IBM Corp. 2005, 2013
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
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3596303    2013-01-04  blaschke-oss windows http response WWW-Authenticate: Negotiate fails
 */

package org.sblim.cimclient.internal.http;

import java.util.Vector;

/**
 * Class Challenge holds a http authentication challenge
 * 
 */
public class Challenge {

	private String iScheme;

	private HttpHeader iParams;

	private Challenge() {
	// hidden ctor
	}

	/**
	 * Returns the parameters
	 * 
	 * @return The parameters
	 */
	public HttpHeader getParams() {
		return this.iParams;
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
	 * Returns the realm
	 * 
	 * @return The realm
	 */
	public String getRealm() {
		return this.iParams.getField("realm");
	}

	/**
	 * Parses the challenge as received from the host. RFC 2617 defines the
	 * following syntax for a challenge:
	 * 
	 * <pre>
	 * challenge = auth-scheme 1*SP 1#auth-param 
	 * auth-scheme = token 
	 * auth-param = token &quot;=&quot; ( token | quoted-string )
	 * </pre>
	 * 
	 * @param pLine
	 *            The challenge string
	 * @return The parsed challenge
	 * @throws HttpParseException
	 *             If the challenge string is ill-formed
	 */
	public static Challenge[] parseChallenge(String pLine) throws HttpParseException {
		if (pLine == null || pLine.length() == 0) throw new IllegalArgumentException(
				"Invalid challenge, empty");
		pLine = pLine.trim();
		if (pLine.length() == 0) throw new IllegalArgumentException("Invalid challenge, empty");

		Vector<Challenge> challenges = new Vector<Challenge>();

		try {
			int start = 0, end = 0;

			// Break up comma-separated list into tokens
			Vector<String> tokensBetweenCommas = new Vector<String>();
			while ((end = indexOfOutsideQuotedString(pLine, ',', start)) > 0) {
				tokensBetweenCommas.add(removeWhitespace(pLine.substring(start, end)));
				start = end + 1;
			}
			if (start < pLine.length()) tokensBetweenCommas.add(removeWhitespace(pLine
					.substring(start)));

			// Break up tokens into auth-scheme and auth-param
			Vector<String> tokens = new Vector<String>();
			for (int i = 0; i < tokensBetweenCommas.size(); i++) {
				String token = tokensBetweenCommas.elementAt(i);

				if (token.length() == 0) continue;

				start = 0;
				end = indexOfOutsideQuotedString(token, ' ', start);

				if (end == -1) {
					tokens.add(token);
				} else {
					tokens.add(token.substring(0, end));
					start = end + 1;
					end = indexOfOutsideQuotedString(token, ' ', start);

					if (end == -1) {
						// RFC 2617 indicates this token should be of the
						// name=value format, but at least one old CIMOM (SVC
						// ICAT) does not follow this rule. The Client
						// effectively ignores this token, and so must continue
						// to do so.
						if (indexOfOutsideQuotedString(token, '=', start) == -1) {
							// throw new
							// HttpParseException("Invalid challenge, second token must be name=value in "
							// + token);
						} else {
							tokens.add(token.substring(start));
						}
					} else {
						throw new HttpParseException("Invalid challenge, too many tokens in "
								+ token);
					}
				}
			}

			Challenge challenge = new Challenge();
			challenge.iScheme = null;
			challenge.iParams = new HttpHeader();

			for (int i = 0; i < tokens.size(); i++) {
				String token = tokens.elementAt(i);

				int equalSign = indexOfOutsideQuotedString(token, '=', 0);
				if (equalSign == 0) {
					throw new HttpParseException(
							"Invalid challenge, no token before equal sign in " + token);
				} else if (equalSign > 0) {
					// param
					if (challenge.iScheme == null) throw new HttpParseException(
							"Invalid challenge, param without scheme");
					String name = token.substring(0, equalSign);
					String value = token.substring(equalSign + 1);
					if (value.length() == 0) {
						throw new HttpParseException(
								"Invalid challenge, no token after equal sign in " + token);
					} else if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 1) {
						challenge.iParams.addField(name, value.substring(1, value.length() - 1));
					} else {
						challenge.iParams.addField(name, value);
					}
				} else {
					// scheme
					if (challenge.iScheme != null) {
						// new scheme
						challenges.add(challenge);

						challenge = new Challenge();
						challenge.iParams = new HttpHeader();
					}
					challenge.iScheme = new String(token);
				}
			}

			if (challenge.iScheme != null) {
				challenges.add(challenge);
			}
		} catch (HttpParseException e) {
			throw e;
		} catch (Exception e) {
			throw new HttpParseException("Invalid challenge, " + e.getMessage());
		}

		return challenges.toArray(new Challenge[challenges.size()]);

	}

	/*
	 * Removes unnecessary whitespace from a challenge such that
	 * "  scheme   name  =  value  " becomes "scheme name=value"
	 */
	private static String removeWhitespace(String str) throws HttpParseException {
		char inBuf[] = str.trim().toCharArray();
		StringBuilder outStr = new StringBuilder();
		boolean inQuotes = false;

		for (int inIdx = 0; inIdx < inBuf.length; inIdx++) {
			if (inQuotes || !Character.isSpaceChar(inBuf[inIdx])) {
				if (inBuf[inIdx] == '=' && outStr.length() == 0) throw new HttpParseException(
						"Invalid challenge, no token before equal sign in " + str);

				outStr.append(inBuf[inIdx]);
			} else {
				// Whitespace not within quoted string
				int i = skipSpaces(inBuf, inIdx + 1);
				if (i >= inBuf.length) throw new HttpParseException(
						"Invalid challenge, no token after space in " + str);

				if (inBuf[i] == '=') {
					// auth-param, remove all whitespace up to next token
					i = skipSpaces(inBuf, i + 1);
					if (i >= inBuf.length) throw new HttpParseException(
							"Invalid challenge, no token after equal sign in " + str);
					outStr.append('=');
				} else {
					// another token, combine all whitespace up to next token
					// into single space
					outStr.append(' ');
				}
				outStr.append(inBuf[i]);
				inIdx = i;
			}
			if (inBuf[inIdx] == '\"') inQuotes = !inQuotes;
		}
		if (inQuotes) throw new HttpParseException(
				"Invalid challenge, quoted string not terminated in " + str);
		return outStr.toString();
	}

	private static int skipSpaces(char[] buf, int pos) {
		while (pos < buf.length && Character.isSpaceChar(buf[pos]))
			pos++;
		return pos;
	}

	private static int indexOfOutsideQuotedString(String str, int ch, int pos)
			throws HttpParseException {
		int len = str.length();
		boolean inQuotes = false;

		while (pos < len) {
			if (str.charAt(pos) == '\"') {
				inQuotes = !inQuotes;
			} else if (str.charAt(pos) == ch) {
				if (!inQuotes) return pos;
			}
			pos++;
		}
		if (inQuotes) throw new HttpParseException(
				"Invalid callenge, quoted string not terminated in " + str);
		return -1;
	}

}
