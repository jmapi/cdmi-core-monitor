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
 * 17931      2005-07-28  thschaef     Add InetAddress to CIM Event
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1649779    2007-02-01  lupusalex    Indication listener threads freeze
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3185818    2011-02-18  blaschke-oss indicationOccured URL incorrect
 * 3495662    2012-02-29  blaschke-oss Invalid HTML from HttpConnectionHandler.writeError
 *    2635    2013-05-16  blaschke-oss Slowloris DoS attack for CIM indication listener port
 *    2657    2013-08-20  blaschke-oss Potential null pointer exception in handleConnection
 */
package org.sblim.cimclient.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.net.ssl.SSLSocket;

import org.sblim.cimclient.internal.http.io.ASCIIPrintStream;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.sblim.cimclient.internal.util.WBEMConstants;

/**
 * Class HttpConnectionHandler is responsible for handling an incoming
 * connection
 * 
 */
public class HttpConnectionHandler {

	/**
	 * MAJOR_VERSION
	 */
	public static final int MAJOR_VERSION = 1;

	/**
	 * MINOR_VERSION
	 */
	public static final int MINOR_VERSION = 1;

	HttpContentHandler iHandler;

	private int iHeaderTimeout;

	private int iMaxAllowedTimeouts;

	private Map<String, Integer> iSuspectIPs = new HashMap<String, Integer>();

	private StringBuilder iBlockedIPs = null;

	/**
	 * Ctor.
	 * 
	 * @param pHandler
	 *            The content handler
	 * @param pProperties
	 *            The configuration properties
	 */
	public HttpConnectionHandler(HttpContentHandler pHandler, WBEMConfiguration pProperties) {
		this.iHandler = pHandler;
		this.iHeaderTimeout = pProperties.getListenerHttpHeaderTimeout();
		this.iMaxAllowedTimeouts = pProperties.getListenerMaxAllowedTimeouts();
	}

	private synchronized void addSuspectIP(String pSuspectIP) {
		if (this.iMaxAllowedTimeouts == 0) return;

		Integer times = this.iSuspectIPs.get(pSuspectIP);
		if (times == null) {
			this.iSuspectIPs.put(pSuspectIP, Integer.valueOf(1));
		} else {
			int timesVal = times.intValue() + 1;
			this.iSuspectIPs.put(pSuspectIP, Integer.valueOf(timesVal));
			if (timesVal >= this.iMaxAllowedTimeouts) {
				boolean newIP = false;
				if (this.iBlockedIPs == null) {
					this.iBlockedIPs = new StringBuilder(pSuspectIP);
					newIP = true;
				} else if (this.iBlockedIPs.indexOf(pSuspectIP) == -1) {
					this.iBlockedIPs.append(',');
					this.iBlockedIPs.append(pSuspectIP);
					newIP = true;
				}
				if (newIP) LogAndTraceBroker.getBroker().trace(
						Level.WARNING,
						"Maximum allowable timeouts exceeded, all future connections ignored from "
								+ pSuspectIP);
			}
		}
	}

	private synchronized boolean isBlockedIP(Socket socket) {
		return (this.iBlockedIPs != null && this.iBlockedIPs.indexOf(socket.getInetAddress()
				.getHostAddress()) != -1);
	}

	/**
	 * Handles the incoming connection and forwards to the content handler
	 * 
	 * @param socket
	 *            The socket of the connection
	 */
	public void handleConnection(Socket socket) {
		InputStream is = null;
		OutputStream os = null;
		if (isBlockedIP(socket)) {
			LogAndTraceBroker.getBroker().trace(
					Level.FINEST,
					"Incoming connection ignored from blocked IP "
							+ socket.getInetAddress().getHostAddress());
		} else try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			do { // handle persistent connections
				MessageReader reader = new MessageReader(is, this.iHeaderTimeout);
				boolean persistent = reader.isPersistentConnectionSupported();
				persistent = false;
				boolean chunk = reader.isChunkSupported();

				HttpServerMethod readerMethod = reader.getMethod();
				if (readerMethod.getMethodName().equals("POST")
						|| readerMethod.getMethodName().equals("M-POST")) {
					// TODO: validate authorization

					MessageWriter writer = new MessageWriter(os, persistent, chunk);
					try {
						StringBuilder localURL = new StringBuilder(
								socket instanceof SSLSocket ? "https://" : "http://");
						InetAddress localAddress = socket.getLocalAddress();
						if (localAddress != null) {
							localURL.append(localAddress.getHostAddress());
							int port = socket.getLocalPort();
							if (port > 0) {
								localURL.append(":");
								localURL.append(port);
							}
						}

						// 17931
						this.iHandler.handleContent(reader, writer, socket.getInetAddress(),
								localURL.toString());
						writer.setMethod(new HttpServerMethod(readerMethod.getMajorVersion(),
								readerMethod.getMinorVersion(), 200, "OK"));
					} catch (HttpException e) {
						writer.setMethod(new HttpServerMethod(readerMethod.getMajorVersion(),
								readerMethod.getMinorVersion(), e.getStatus(), e.getMessage()));
					} catch (Throwable t) {
						writer.setMethod(new HttpServerMethod(readerMethod.getMajorVersion(),
								readerMethod.getMinorVersion(), 501, "Not Implemented"));
						// TODO: define the correct error description
						writer.reset();
						// TODO: report an specific error
						writeError(writer.getOutputStream(), "error", "error");
					} finally {
						try {
							writer.close();
						} catch (IOException e) {
							LogAndTraceBroker.getBroker().trace(Level.FINER,
									"Exception while closing output stream from server socket", e);
						}
					}
				} else {
					persistent = false;
					MessageWriter writer = new MessageWriter(os, false, false);
					HttpHeader header = new HttpHeader();
					writer.setHeader(header);
					// header.addField("Connection", persistent?
					// "Keep-Alive","close");
					writer.setMethod(new HttpServerMethod(readerMethod.getMajorVersion(),
							readerMethod.getMinorVersion(), 501, "Not Implemented"));
					writeError(writer.getOutputStream(), "", "");
					try {
						writer.close();
					} catch (IOException e) {
						LogAndTraceBroker.getBroker().trace(Level.FINER,
								"Exception while closing output stream from server socket", e);
					}
				}

				if (!persistent) break;
				try {
					reader.close();
				} catch (IOException e) {
					LogAndTraceBroker.getBroker().trace(Level.FINER,
							"Exception while closing input stream from server socket", e);
				}
			} while (true);
		} catch (IOException e) {
			LogAndTraceBroker.getBroker().trace(Level.FINER,
					"Exception while reading from server socket", e);

			if (e instanceof SocketTimeoutException
					|| WBEMConstants.INDICATION_DOS_EXCEPTION_MESSAGE.equalsIgnoreCase(e
							.getMessage())) {
				addSuspectIP(socket.getInetAddress().getHostAddress());
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			LogAndTraceBroker.getBroker().trace(Level.FINER,
					"Exception while closing server socket", e);
		}
	}

	/**
	 * Closes the handler. Will also close the content handler.
	 */
	public void close() {
		this.iHandler.close();
	}

	private void writeError(ASCIIPrintStream dos, String title, String body) {
		dos.print("<HTML> <HEAD> <TITLE>" + title + "</TITLE></HEAD><BODY>" + body
				+ "</BODY></HTML>");

	}

	/**
	 * Get the IPs blocked by the listener associated with the specified port.
	 * 
	 * @return The comma-separated list of blocked IPs.
	 */
	public synchronized String getBlockedIPs() {
		return (this.iBlockedIPs == null) ? null : this.iBlockedIPs.toString();
	}

	/**
	 * Set the IPs to be blocked by the listener associated with the specified
	 * port.
	 * 
	 * @param pIPs
	 *            The comma-separated list of blocked IPs.
	 */
	public synchronized void setBlockedIPs(String pIPs) {
		this.iBlockedIPs = (pIPs == null || pIPs.trim().length() == 0) ? null : new StringBuilder(
				pIPs);
	}
}
