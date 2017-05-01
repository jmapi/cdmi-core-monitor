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
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 *    2620    2013-02-23  blaschke-oss Chunked output broken
 */
package org.sblim.cimclient.internal.http.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class ChunkedOutputStream implements an output stream for chunked messages
 * 
 */
public class ChunkedOutputStream extends OutputStream {

	DataOutputStream iOs;

	byte[] iBuffer;

	int iUsed;

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The stream to create this one upon
	 * @param pBufferLength
	 *            The buffer length. When this value is exceeded a new chunk
	 *            will be started.
	 */
	public ChunkedOutputStream(OutputStream pStream, int pBufferLength) {
		this.iOs = new DataOutputStream(pStream);
		this.iBuffer = new byte[pBufferLength];
		this.iUsed = 0;
	}

	@Override
	public void close() throws IOException {
		flush();
		this.iOs.writeBytes(Integer.toHexString(0) + "\r\n");
		this.iOs.flush();
	}

	@Override
	public void flush() throws IOException {
		if (this.iUsed > 0) {
			this.iOs.writeBytes(Integer.toHexString(this.iUsed) + "\r\n");
			this.iOs.write(this.iBuffer, 0, this.iUsed);
			this.iOs.writeBytes("\r\n");
			this.iOs.flush();
		}
		this.iUsed = 0;
	}

	/**
	 * @param offset
	 */
	@Override
	public void write(byte source[], int offset, int len) throws IOException {
		int copied = 0;
		while (len > 0) {
			int total = (this.iBuffer.length - this.iUsed < len) ? (this.iBuffer.length - this.iUsed)
					: len;
			if (total > 0) {
				System.arraycopy(source, copied, this.iBuffer, this.iUsed, total);
				len -= total;
				this.iUsed += total;
				copied += total;
			}
			if (this.iUsed == this.iBuffer.length) flush();
		}
	}

	@Override
	public void write(int i) throws IOException {
		if (this.iBuffer.length == this.iUsed) flush();
		this.iBuffer[this.iUsed++] = (byte) (0xFF & i);
	}

}
