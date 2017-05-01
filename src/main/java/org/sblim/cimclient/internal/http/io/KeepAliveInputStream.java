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
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */
package org.sblim.cimclient.internal.http.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sblim.cimclient.internal.http.HttpClient;

/**
 * Class KeepAliveInputStream implements an input stream for connections that
 * keep iAlive after a request is completed
 * 
 */
public class KeepAliveInputStream extends FilterInputStream {

	private HttpClient iClient;

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The underlying stream
	 * @param pClient
	 *            The associated client.
	 */
	public KeepAliveInputStream(InputStream pStream, HttpClient pClient) {
		super(pStream);
		this.iClient = pClient;
	}

	@Override
	public int read() throws IOException {
		int i = super.read();
		if (i == -1 && this.iClient != null) {
			this.iClient.streamFinished();
			this.iClient = null;
		}
		return i;
	}

	@Override
	public int read(byte buf[]) throws IOException {
		return read(buf, 0, buf.length);
	}

	@Override
	public int read(byte buf[], int off, int len) throws IOException {
		int i = super.read(buf, off, len);
		if (i == -1 && this.iClient != null) {
			this.iClient.streamFinished();
			this.iClient = null;
		}
		return i;
	}

	@Override
	public long skip(long len) throws IOException {
		long i = super.skip(len);

		if (i == -1 && this.iClient != null) {
			this.iClient.streamFinished();
			this.iClient = null;
		}
		return i;
	}

	@Override
	public void close() throws IOException {
		super.close();
		if (this.iClient != null) {
			this.iClient.streamFinished();
			this.iClient = null;
		}
	}
}
