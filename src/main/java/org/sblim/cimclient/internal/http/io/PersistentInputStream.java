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

/**
 * Class PersistentInputStream implements an input stream for which close() can
 * be disabled.
 * 
 */
public class PersistentInputStream extends FilterInputStream {

	boolean iClosable = false;

	boolean iClosed = false;

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The underlying stream
	 */
	public PersistentInputStream(InputStream pStream) {
		this(pStream, false);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The underlying stream
	 * @param pClosable
	 *            If <code>false</code> this stream will ignore calls to the
	 *            close() method.
	 */
	public PersistentInputStream(InputStream pStream, boolean pClosable) {
		super(pStream);
		this.iClosable = pClosable;
	}

	@Override
	public synchronized void close() throws IOException {
		if (!this.iClosed) {
			this.iClosed = true;
			if (this.iClosable) this.in.close();
		} else throw new IOException("Error while closing the input stream. It was already closed");
	}
}
