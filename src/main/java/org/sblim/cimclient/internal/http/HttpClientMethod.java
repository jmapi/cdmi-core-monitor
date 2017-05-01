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
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.cimclient.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.sblim.cimclient.internal.http.io.ASCIIPrintStream;

/**
 * Class HttpClientMethod encapsulates a http client method
 * 
 */
public class HttpClientMethod extends HttpMethod {

	private boolean iIncomming = true;

	private String iHttpHeader;

	private int iMinor, iMajor;

	private int iStatus;

	private String iMethod;

	private String iRequest;

	private String iResponse;

	/**
	 * Ctor. Creates an outgoing http method
	 * 
	 * @param pMethod
	 *            The method
	 * @param pRequest
	 *            The request
	 * @param pMajor
	 *            The major version
	 * @param pMinor
	 *            The minor version
	 */
	public HttpClientMethod(String pMethod, String pRequest, int pMajor, int pMinor) {
		this.iMinor = pMinor;
		this.iMajor = pMajor;
		this.iRequest = pRequest;
		this.iMethod = pMethod;
		this.iIncomming = false;
	}

	/**
	 * Ctor. Parses an incoming http method from a given input stream
	 * 
	 * @param pReader
	 *            The input stream
	 * @throws IOException
	 */
	public HttpClientMethod(InputStream pReader) throws IOException {
		String line = null;

		do {
			line = readLine(pReader);
		} while (line == null || line.length() == 0);
		int rqt = line.indexOf(' ');
		int prev = 0;
		if (rqt > -1) { // Parse the header
			int next = rqt;
			this.iHttpHeader = line.substring(prev, next).toUpperCase();

			prev = this.iHttpHeader.indexOf('/');
			if (prev > 0 && this.iHttpHeader.substring(0, prev).equalsIgnoreCase("HTTP")) {
				next = this.iHttpHeader.indexOf('.', prev + 1);
				try {
					this.iMajor = Integer.parseInt(this.iHttpHeader.substring(prev + 1, next));
					this.iMinor = Integer.parseInt(this.iHttpHeader.substring(next + 1));
				} catch (Exception e) {
					throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
				}
				prev = rqt;
				rqt = line.indexOf(' ', prev + 1);
				if (rqt > -1) {
					try {
						this.iStatus = Integer.parseInt(line.substring(prev + 1, rqt));
					} catch (Exception e) {
						throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
					}
					this.iResponse = line.substring(rqt + 1);
					return;
				}
			} else throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
		}
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
	}

	/**
	 * Returns the major version
	 * 
	 * @return The major version
	 */
	public int getMajorVersion() {
		return this.iMajor;
	}

	/**
	 * Returns the minor version
	 * 
	 * @return The minor version
	 */
	public int getMinorVersion() {
		return this.iMinor;
	}

	/**
	 * Returns the method name
	 * 
	 * @return The method name
	 */
	public String getMethodName() {
		return this.iMethod;
	}

	/**
	 * Return the status
	 * 
	 * @return The status
	 */
	public int getStatus() {
		return this.iStatus;
	}

	/**
	 * Writes the method to a given stream
	 * 
	 * @param pStream
	 *            The stream
	 */
	public void write(ASCIIPrintStream pStream) {
		pStream.print(this.iMethod + " " + this.iRequest + " HTTP/" + this.iMajor + "."
				+ this.iMinor + "\r\n");
	}

	/**
	 * Returns the response message
	 * 
	 * @return The response message
	 */
	public String getResponseMessage() {
		return this.iResponse;
	}

	@Override
	public String toString() {
		if (this.iIncomming) { return this.iHttpHeader + " " + this.iStatus + " " + this.iResponse; }
		return this.iMethod + " " + this.iRequest + " HTTP/" + this.iMajor + "." + this.iMinor;
	}
}
