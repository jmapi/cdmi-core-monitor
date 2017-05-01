/**
 * (C) Copyright IBM Corp. 2007, 2010
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1649779    2007-02-01  lupusalex    Indication listener threads freeze
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 3022541    2010-06-30  blaschke-oss File descriptor leak in sample/unittest
 */

package org.sblim.cimclient.samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.cim.CIMInstance;
import javax.cim.CIMProperty;
import javax.net.SocketFactory;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.listener.IndicationListener;
import javax.wbem.listener.WBEMListener;
import javax.wbem.listener.WBEMListenerFactory;

import org.sblim.cimclient.WBEMConfigurationProperties;

/**
 * Class Jsr48IndicationStresser is a test program to perform a stress test for
 * the indication processing code.
 * 
 */
public class Jsr48IndicationStresser {

	/**
	 * The indication port
	 */
	private static final int PORT = 6000;

	/**
	 * The alternate indication port. Set equal to port if you want to test with
	 * one port only.
	 */
	private static final int ALTERNATE_PORT = 6000;

	/**
	 * The total count of indication to send. COUNT must be dividable by
	 * THREADS.
	 */
	private static final int COUNT = 100000;

	/**
	 * The number of sender threads.
	 */
	private static final int THREADS = 4;

	/**
	 * The number of disquieter threads. Set to zero to disable disquieting
	 */
	private static final int DISQUIETERS = 4;

	/**
	 * The delay between startup of succeeding disquieters
	 */
	private static final long DISQUIETER_DELAY = 6666;

	/**
	 * The time a disquieter will sleep between to attacks
	 */
	private static final long DISQUIETER_BREAK_MS = 10000;

	private String iHeader;

	private String iTrailer;

	private int iThreadFinishCount = 0;

	private Listener iListener;

	private long iStartTime;

	private long iBestTime = Long.MAX_VALUE;

	private long iWorstTime = 0;

	private long iTotal = 0;

	private Disquieter[] iDisquiters = new Disquieter[DISQUIETERS];

	/**
	 * Executes the indication stresser. This sample will send a given number of
	 * indications as fast as possible to the indication listener. The code will
	 * use a given number of threads. Optionally another given number of thread
	 * can simulate ill behavior and hang after sending an incomplete request.
	 * The indication are send to up to two ports. The code checks if each
	 * indication that was sent is also received by the indication listener.<br />
	 * <br />
	 * <em>This is more test than sample, but since it doesn't fit too well in our unit tests I've added it here.</em><br />
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String args[]) {
		Listener listener = new Listener();
		listener.start();
		System.out.println("Listener started");
		System.out.println("Sending " + COUNT + " indications in " + THREADS + " thread(s).");
		System.out.println("Disquieting with " + DISQUIETERS + " thread(s) that take(s) a "
				+ DISQUIETER_BREAK_MS + " ms break in between.");
		new Jsr48IndicationStresser(listener).run();
	}

	/**
	 * Ctor.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public Jsr48IndicationStresser(Listener pListener) {
		this.iListener = pListener;
	}

	/**
	 * Starts the senders and processes the indications
	 */
	public void run() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass()
				.getResourceAsStream("indication.xml")));
		StringBuffer rawIndication = new StringBuffer();
		try {
			while (reader.ready()) {
				rawIndication.append(reader.readLine());
				rawIndication.append('\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int idPosition = rawIndication.indexOf("%_ID_%");
		this.iHeader = rawIndication.substring(0, idPosition);
		this.iTrailer = rawIndication.substring(idPosition + 6, rawIndication.length());

		// if (COUNT % THREADS > 0) { throw new IllegalArgumentException(
		// "COUNT doesn't divide by THREADS"); }

		int countPerThread = COUNT / THREADS;

		this.iStartTime = System.currentTimeMillis();

		for (int i = 0; i < THREADS; ++i) {
			Thread thread = new Thread(new Sender(i * countPerThread, i * countPerThread
					+ countPerThread, this), "S" + String.valueOf(i));
			thread.start();
		}

		for (int i = 0; i < DISQUIETERS; ++i) {
			this.iDisquiters[i] = new Disquieter(DISQUIETER_BREAK_MS, getHeader() + "FFFFFF"
					+ getTrailer());
			Thread thread = new Thread(this.iDisquiters[i], "D" + String.valueOf(i));
			thread.start();
			try {
				Thread.sleep(DISQUIETER_DELAY);
			} catch (InterruptedException e) {
				// nothing
			}
		}
	}

	/**
	 * Called by a sender when it is finished with sending indications. When all
	 * senders are finished it prints the results.
	 * 
	 * @param pBest
	 *            The shortest response time
	 * @param pWorst
	 *            The longest response time
	 * @param pTotal
	 *            The accumulated response times
	 */
	public void threadFinished(long pBest, long pWorst, long pTotal) {

		this.iBestTime = pBest > this.iBestTime ? this.iBestTime : pBest;
		this.iWorstTime = pWorst > this.iWorstTime ? pWorst : this.iWorstTime;
		this.iTotal += pTotal;

		if (++this.iThreadFinishCount < THREADS) { return; }

		long EndTime = System.currentTimeMillis();
		System.out.println("Completed");

		for (int i = 0; i < DISQUIETERS; ++i) {
			this.iDisquiters[i].stop();
		}

		// try {
		// Thread.sleep(10000);
		// } catch (InterruptedException e) {
		// // nothing
		// }
		this.iListener.stop();

		boolean success = true;
		List<Boolean> hits = this.iListener.getHits();
		for (int i = 0; i < COUNT; ++i) {
			if (!hits.get(i).booleanValue()) {
				System.err.println("Id " + i + " not hit");
				success = false;
			}
		}
		System.out.println("Test was " + (success ? "" : "not") + " successful");
		System.out.println("Test took " + ((EndTime - this.iStartTime) / 1000) + " s, median "
				+ ((EndTime - this.iStartTime) / COUNT) + " ms per indication");
		System.out.println("Per thread: median " + this.iTotal / COUNT + " ms, best "
				+ this.iBestTime + " ms, worst " + this.iWorstTime + " ms.");
	}

	/**
	 * Class Sender is responsible for sending a given amount of indications
	 * 
	 */
	private static class Sender implements Runnable {

		private static final int SENDER_TIMEOUT = 30000;

		private final int iStartId;

		private final int iEndId;

		private Jsr48IndicationStresser iParent;

		private long iBestTime = Long.MAX_VALUE;

		private long iWorstTime = 0;

		private long iTotal = 0;

		/**
		 * Ctor.
		 * 
		 * @param pStartId
		 *            The id of the first indication to send
		 * @param pEndId
		 *            The id of the final indication to send
		 * @param pParent
		 *            The owning stresser
		 */
		public Sender(int pStartId, int pEndId, Jsr48IndicationStresser pParent) {
			this.iStartId = pStartId;
			this.iEndId = pEndId;
			this.iParent = pParent;
		}

		public void run() {
			final String threadId = Thread.currentThread().getName();
			final SocketFactory sf = SocketFactory.getDefault();
			final InetAddress localhost;
			try {
				localhost = InetAddress.getByName("localhost");
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return;
			}
			for (int i = this.iStartId; i < this.iEndId; ++i) {
				if ((i % 100) == 0) {
					if ((i % 1000) == 0 && threadId.equals("S0")) {
						System.out.println();
					}
					System.out.print(threadId + ".");
				}
				try {
					String indication = this.iParent.getHeader() + fill2six(Integer.toHexString(i))
							+ this.iParent.getTrailer();
					Socket socket = null;
					while (socket == null) {
						try {
							socket = sf.createSocket(localhost, Math.random() > 0.5 ? PORT
									: ALTERNATE_PORT);
						} catch (BindException e) {
							System.err.print("B[" + threadId + "].");
							continue;
						}
						if (socket == null) {
							System.err.print("N[" + threadId + "].");
						}
					}
					try {
						long start = System.currentTimeMillis();
						socket.setSoTimeout(10000);
						OutputStream os = socket.getOutputStream();
						InputStream is = socket.getInputStream();
						os.write(indication.getBytes());
						os.flush();
						long timeout = System.currentTimeMillis() + SENDER_TIMEOUT;
						StringBuffer response = new StringBuffer();
						boolean complete = false;
						boolean success = false;
						while (System.currentTimeMillis() < timeout && !complete) {
							while (is.available() > 0) {
								response.append((char) is.read());
							}
							if (!success && response.length() >= 15
									&& !response.substring(0, 15).equals("HTTP/1.1 200 OK")) {
								System.err.println("Delivery failed");
								break;
							}
							complete = response.indexOf("</CIM>") > 0;
							if (!complete) {
								try {
									Thread.sleep(5);
								} catch (InterruptedException e) {/**/}
							}
						}
						if (!complete) {
							System.err.println("T[" + threadId + "].");
							continue;
						}
						long elapsed = System.currentTimeMillis() - start;
						this.iBestTime = elapsed > this.iBestTime ? this.iBestTime : elapsed;
						this.iWorstTime = elapsed > this.iWorstTime ? elapsed : this.iWorstTime;
						this.iTotal += elapsed;
					} finally {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {/**/}
			this.iParent.threadFinished(this.iBestTime, this.iWorstTime, this.iTotal);
		}

		/**
		 * Fills a string with leading zeros to a length of six characters
		 * 
		 * @param pInput
		 *            The original string
		 * @return The filled string
		 */
		private String fill2six(String pInput) {
			switch (pInput.length()) {
				case 0:
					return "000000";
				case 1:
					return "00000" + pInput;
				case 2:
					return "0000" + pInput;
				case 3:
					return "000" + pInput;
				case 4:
					return "00" + pInput;
				case 5:
					return "0" + pInput;
				default:
					return pInput;
			}
		}

	}

	/**
	 * Class Listener is responsible for setting up a WBEM listener and
	 * listening for indications
	 * 
	 */
	private static class Listener implements IndicationListener {

		private List<Boolean> iHits = new ArrayList<Boolean>(COUNT);

		private WBEMListener iListener;

		/**
		 * Ctor.
		 */
		public Listener() {
			for (int i = 0; i < COUNT; ++i) {
				this.iHits.add(Boolean.FALSE);
			}
		}

		/**
		 * Starts the WBEM listener
		 */
		public void start() {
			try {
				System.setProperty(WBEMConfigurationProperties.LISTENER_HTTP_TIMEOUT, "10000");
				System.setProperty(WBEMConfigurationProperties.LISTENER_MIN_POOL_SIZE, "2");
				System.setProperty(WBEMConfigurationProperties.LISTENER_MAX_POOL_SIZE, "8");
				System.setProperty(WBEMConfigurationProperties.LISTENER_BACKLOG, "2");
				System.setProperty(WBEMConfigurationProperties.LISTENER_HANDLER_MAX_IDLE, "10000");
				this.iListener = WBEMListenerFactory
						.getListener(WBEMClientConstants.PROTOCOL_CIMXML);
				this.iListener.addListener(this, PORT, "HTTP");
				if (PORT != ALTERNATE_PORT) {
					this.iListener.addListener(this, ALTERNATE_PORT, "HTTP");
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.iListener.removeListener(PORT);
			}
		}

		/**
		 * Stops the WBEM listener
		 */
		public void stop() {
			this.iListener.removeListener(PORT);
			if (PORT != ALTERNATE_PORT) {
				this.iListener.removeListener(ALTERNATE_PORT);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.wbem.listener.IndicationListener#indicationOccured(java.lang
		 * .String, javax.cim.CIMInstance)
		 */
		public void indicationOccured(String pUrl, CIMInstance pIndication) {
			if (!pUrl.equals("stresser")) {
				System.err.println("Wrong URL: " + pUrl);
			}
			CIMProperty<?> property = pIndication.getProperty("IndicationIdentifier");
			if (property != null && property.getValue() != null) {
				try {
					int id = Integer.parseInt(property.getValue().toString(), 16);
					if (0 <= id && COUNT > id) {
						// synchronized (iHits) {
						if (this.iHits.get(id).booleanValue()) {
							System.err.println("Got " + id + " again.");
						}
						this.iHits.set(id, Boolean.TRUE);
						// }
					} else {
						System.err.println("Invalid id: " + id);
					}
				} catch (NumberFormatException e) {
					System.err.println("Invalid id: " + property.getValue().toString());
				}
			} else {
				System.err.println("Id not set");
			}
		}

		/**
		 * Return the list of ids that have been &quot;hit&quot; by an
		 * indication
		 * 
		 * @return The list of &quot;hits&quot;
		 */
		public List<Boolean> getHits() {
			return this.iHits;
		}
	}

	/**
	 * Class Disquieter is responsible for sending ill behaved requests to the
	 * indication listener. It send an incomplete document and leaves the
	 * connection open, forcing the corresponding handler to block until
	 * timeout.
	 * 
	 */
	private static class Disquieter implements Runnable {

		private static final int DISQUIETER_TIMEOUT = 30000;

		private long iBreak;

		private boolean iStop = false;

		private String iDocument;

		/**
		 * Ctor.
		 * 
		 * @param pFrequency
		 *            The frequency in ms for disquieting
		 * @param pDocument
		 *            The (complete) document
		 */
		public Disquieter(long pFrequency, String pDocument) {
			this.iBreak = pFrequency;
			this.iDocument = pDocument;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			final String threadId = Thread.currentThread().getName();
			final SocketFactory sf = SocketFactory.getDefault();
			final InetAddress localhost;
			try {
				localhost = InetAddress.getByName("localhost");
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return;
			}
			while (!this.iStop) {
				try {
					String indication = this.iDocument.substring(0, this.iDocument.length() - 100);
					Socket socket = null;
					while (socket == null) {
						try {
							socket = sf.createSocket(localhost, Math.random() > 0.5 ? PORT
									: ALTERNATE_PORT);
						} catch (BindException e) {
							System.err.print("B[" + threadId + "].");
							continue;
						}
						if (socket == null) {
							System.err.print("N[" + threadId + "].");
						}
					}
					try {
						OutputStream os = socket.getOutputStream();
						InputStream is = socket.getInputStream();
						os.write(indication.getBytes());
						os.flush();
						long timeout = System.currentTimeMillis() + DISQUIETER_TIMEOUT;
						StringBuffer response = new StringBuffer();
						boolean responseReceived = false;
						while (System.currentTimeMillis() < timeout && !responseReceived) {
							while (is.available() > 0) {
								response.append((char) is.read());
							}
							if (response.length() >= 12) {
								if (response.substring(0, 8).equals("HTTP/1.1")) {
									long elapsed = System.currentTimeMillis() + DISQUIETER_TIMEOUT
											- timeout;
									String result = response.substring(9, 12);
									if (!result.equals("400") || elapsed > 60000) {
										System.out.println("DISQUIETER: Got response after "
												+ elapsed / 1000 + " s. Response code: " + result);
									}
									responseReceived = true;
									break;
								}
							}
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (!responseReceived) {
							System.err.println("T[" + threadId + "].");
						}
					} finally {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.print(threadId + ".");
				try {
					Thread.sleep(this.iBreak);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/**
		 * Stop the disquieter thread
		 */
		public void stop() {
			this.iStop = true;
		}
	}

	/**
	 * Returns the document header. That is the document op to the id.
	 * 
	 * @return The header
	 */
	public String getHeader() {
		return this.iHeader;
	}

	/**
	 * Return the document trailer. That is the document after the id.
	 * 
	 * @return The trailer
	 */
	public String getTrailer() {
		return this.iTrailer;
	}
}
