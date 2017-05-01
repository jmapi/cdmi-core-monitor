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
 * 18045      2005-08-10  pineiro5     Some code clean up in multiple points
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2714989    2009-03-26  blaschke-oss Code cleanup from redundant null check et al
 */
package org.sblim.cimclient.internal.http.io;

import java.io.BufferedWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Class ASCIIPrintStream implement a stream with ASCII charset
 * 
 */
public class ASCIIPrintStream extends FilterOutputStream {

	private boolean iAutoFlush = false;

	private Exception iTrouble = null;

	private BufferedWriter iTextOut;

	private ASCIIPrintStream(boolean autoFlush, OutputStream pOut) {
		super(pOut);
		if (pOut == null) throw new NullPointerException("Null output stream");
		this.iAutoFlush = autoFlush;
	}

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The underlying stream
	 */
	public ASCIIPrintStream(OutputStream pStream) {
		this(pStream, false);
		init(new OutputStreamWriter(this));
	}

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The underlying stream
	 * @param pAutoFlush
	 *            If <code>true</code> the stream is automatically flushed after
	 *            write.
	 */
	public ASCIIPrintStream(OutputStream pStream, boolean pAutoFlush) {
		this(pAutoFlush, pStream);
		init(new OutputStreamWriter(this.out));
	}

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The underlying stream
	 * @param pAutoFlush
	 *            If <code>true</code> the stream is automatically flushed after
	 *            write.
	 * @param pEncoding
	 *            Ignored
	 */
	public ASCIIPrintStream(OutputStream pStream, boolean pAutoFlush, String pEncoding) {
		this(pStream, pAutoFlush);
	}

	private void init(OutputStreamWriter osw) {
		this.iTextOut = new BufferedWriter(osw);
	}

	private void write(String str) {
		int stringLength = str.length();
		char charArray[] = new char[stringLength];
		byte asciiArray[] = new byte[stringLength];
		str.getChars(0, stringLength, charArray, 0);
		for (int i = 0; i < stringLength; i++)
			asciiArray[i] = charArray[i] >= '\u0100' ? 63 : (byte) charArray[i];

		write(asciiArray, 0, stringLength);
	}

	/**
	 * Prints a boolean value
	 * 
	 * @param pValue
	 *            The value
	 */
	public void print(boolean pValue) {
		write(pValue ? "true" : "false");
	}

	/**
	 * Prints a single character
	 * 
	 * @param c
	 *            The character
	 */
	public void print(char c) {
		write(String.valueOf(c));
	}

	/**
	 * Prints an integer value
	 * 
	 * @param i
	 *            The value
	 */
	public void print(int i) {
		write(String.valueOf(i));
	}

	/**
	 * Prints a long value
	 * 
	 * @param l
	 *            The value
	 */
	public void print(long l) {
		write(String.valueOf(l));
	}

	/**
	 * Prints a float value
	 * 
	 * @param f
	 *            The value
	 */
	public void print(float f) {
		write(String.valueOf(f));
	}

	/**
	 * Prints a double value
	 * 
	 * @param d
	 *            The value
	 */
	public void print(double d) {
		write(String.valueOf(d));
	}

	/**
	 * Prints a character array
	 * 
	 * @param pArray
	 *            The array
	 */
	public void print(char pArray[]) {
		write(String.valueOf(pArray));
	}

	/**
	 * Prints a string
	 * 
	 * @param s
	 *            The string
	 */
	public void print(String s) {
		write(s == null ? "null" : s);
	}

	/**
	 * Prints an object
	 * 
	 * @param pObj
	 *            The object
	 */
	public void print(Object pObj) {
		write(String.valueOf(pObj));
	}

	/**
	 * Prints a newline
	 */
	public void println() {
		newLine();
	}

	/**
	 * println
	 * 
	 * @param flag
	 */
	public void println(boolean flag) {
		synchronized (this) {
			print(flag);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param c
	 */
	public void println(char c) {
		synchronized (this) {
			print(c);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param i
	 */
	public void println(int i) {
		synchronized (this) {
			print(i);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param l
	 */
	public void println(long l) {
		synchronized (this) {
			print(l);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param f
	 */
	public void println(float f) {
		synchronized (this) {
			print(f);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param d
	 */
	public void println(double d) {
		synchronized (this) {
			print(d);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param ac
	 */
	public void println(char ac[]) {
		synchronized (this) {
			print(ac);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param s
	 */
	public void println(String s) {
		synchronized (this) {
			print(s);
			newLine();
		}
	}

	/**
	 * println
	 * 
	 * @param obj
	 */
	public void println(Object obj) {
		synchronized (this) {
			print(obj);
			newLine();
		}
	}

	private void newLine() {
		try {
			synchronized (this) {
				ensureOpen();
				this.iTextOut.newLine();
				if (this.iAutoFlush) this.out.flush();
			}
		} catch (InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (IOException x) {
			this.iTrouble = x;
		}
	}

	@Override
	public void write(byte buf[], int off, int len) {
		try {
			synchronized (this) {
				ensureOpen();
				this.out.write(buf, off, len);
				// i ++;
				if (this.iAutoFlush) this.out.flush();
			}
			// System.out.println("TOTAL:"+i);
		} catch (InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (IOException x) {
			this.iTrouble = x;
		}
	}

	@Override
	public void write(int b) {
		try {
			synchronized (this) {
				ensureOpen();
				this.out.write(b);
				if ((b == '\n') && this.iAutoFlush) this.out.flush();
			}
		} catch (InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (IOException x) {
			this.iTrouble = x;
		}
	}

	protected void setError() {
	// trouble = x;
	}

	/**
	 * Returns the last exception caught
	 * 
	 * @return The exception
	 */
	public Exception checkError() {
		if (this.out != null) flush();
		return this.iTrouble;
	}

	@Override
	public void close() {
		synchronized (this) {
			if (!this.closing) {
				this.closing = true;
				try {
					this.iTextOut.close();
					this.out.close();
				} catch (IOException x) {
					this.iTrouble = x;
				}
				this.iTextOut = null;
				// iCharOut = null;
				this.out = null;
			}
		}
	}

	@Override
	public void flush() {
		synchronized (this) {
			try {
				ensureOpen();
				this.out.flush();
			} catch (IOException x) {
				this.iTrouble = x;
			}
		}
	}

	private boolean closing = false;

	private void ensureOpen() throws IOException {
		if (this.out == null) throw new IOException("Stream closed");
	}
}
