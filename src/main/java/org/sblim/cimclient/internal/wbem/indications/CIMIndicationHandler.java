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
 * 1438152    2006-05-15  lupusalex    Wrong message ID in ExportResponseMessage
 * 1498938    2006-06-01  lupusalex    Multiple events in single cim-xml request are not handled
 * 1498130    2006-05-31  lupusalex    Selection of xml parser on a per connection basis
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-16  lupusalex    Make SBLIM client JSR48 compliant
 * 1656285    2007-02-12  ebak         IndicationHandler does not accept non-Integer message ID
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2714989    2009-03-26  blaschke-oss Code cleanup from redundant null check et al
 * 2901216    2009-12-01  blaschke-oss lost IndicationURL for IndcationListener.indicationOccured
 * 3185818    2011-02-18  blaschke-oss indicationOccured URL incorrect
 * 3186176    2011-02-18  blaschke-oss XML response for indication not traced
 * 3185763    2011-02-25  blaschke-oss Reliable indication support - Phase 1
 * 3288721    2011-05-20  blaschke-oss Need the function of indication reordering
 * 3304058    2011-05-20  blaschke-oss Use same date format in change history
 * 3304953    2011-05-20  blaschke-oss Indication URL mapped to lower case
 * 3374206    2011-07-22  blaschke-oss NullPointerException caused by Indication
 * 3376657    2011-07-24  blaschke-oss Get reliable indication properties once
 * 3390724    2011-08-12  blaschke-oss Problem with Reliable Indication support in the Listener
 * 3459036    2011-12-13  blaschke-oss Linked list for RI queue not efficient for many LDs
 * 3485074    2012-02-06  blaschke-oss An Indication trace request
 * 3484022    2012-02-08  blaschke-oss Turn reliable indication mode on and off based on SC/SN
 * 3492214    2012-02-23  blaschke-oss Add a SenderIPAddress property indications
 * 3513228    2012-04-23  blaschke-oss Reliable Indications support can create lots of threads
 * 3553858    2012-08-06  blaschke-oss Append duplicate HTTP header fields instead of replace
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
 * 3601894    2013-01-23  blaschke-oss Enhance HTTP and CIM-XML tracing
 */

package org.sblim.cimclient.internal.wbem.indications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;

import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMProperty;
import javax.wbem.WBEMException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.sblim.cimclient.internal.cimxml.CIMClientXML_HelperImpl;
import org.sblim.cimclient.internal.cimxml.CIMRequest;
import org.sblim.cimclient.internal.cimxml.CIMXMLBuilderImpl;
import org.sblim.cimclient.internal.cimxml.CIMXMLParserImpl;
import org.sblim.cimclient.internal.http.HttpContentHandler;
import org.sblim.cimclient.internal.http.HttpException;
import org.sblim.cimclient.internal.http.HttpHeader;
import org.sblim.cimclient.internal.http.HttpHeader.HeaderEntry;
import org.sblim.cimclient.internal.http.HttpHeaderParser;
import org.sblim.cimclient.internal.http.MessageReader;
import org.sblim.cimclient.internal.http.MessageWriter;
import org.sblim.cimclient.internal.http.io.DebugInputStream;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.sblim.cimclient.internal.util.WBEMConfigurationDefaults;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * Handles the HTTP connections, providing the necessary interfaces for
 * CIMListener server.
 */
public class CIMIndicationHandler extends HttpContentHandler {

	/**
	 * <code>DataManager</code> is responsible for background processing of the
	 * reliable indication queue and cache.
	 */
	private class DataManager extends Thread {

		private boolean iAlive = true;

		private LinkedList<ServerListEntry> iLinkedList;

		private Hashtable<ServerTableEntry, IndicationServer> iHashTable;

		public DataManager(LinkedList<ServerListEntry> pServerList) {
			this.iLinkedList = pServerList;
			this.iHashTable = null;
			setDaemon(true);
		}

		public DataManager(Hashtable<ServerTableEntry, IndicationServer> pServerTable) {
			this.iLinkedList = null;
			this.iHashTable = pServerTable;
			setDaemon(true);
		}

		@Override
		public void run() {
			while (this.iAlive) {
				try {
					if (this.iLinkedList != null) {
						ServerListEntry entry;
						Iterator<ServerListEntry> iterator;

						iterator = this.iLinkedList.iterator();
						while (iterator.hasNext()) {
							entry = iterator.next();
							IndicationServer server = entry.getIndicationServer();
							if (server != null) {
								ReliableIndicationHandler handler = server.getRIHandler();
								if (handler != null) {
									handler.processAll();
								}
							}
						}
					} else if (this.iHashTable != null) {
						Map.Entry<ServerTableEntry, IndicationServer> entry;
						Iterator<Map.Entry<ServerTableEntry, IndicationServer>> iterator;

						Set<Map.Entry<ServerTableEntry, IndicationServer>> set = this.iHashTable
								.entrySet();
						if (set != null) {
							iterator = set.iterator();
							while (iterator.hasNext()) {
								entry = iterator.next();
								IndicationServer server = entry.getValue();
								if (server != null) {
									ReliableIndicationHandler handler = server.getRIHandler();
									if (handler != null) {
										handler.processAll();
									}
								}
							}
						}
					}
					sleep(1000);
				} catch (Throwable t) {
					// Expected, ignore and try again
				}
			}
		}

		public void stopRun() {
			this.iAlive = false;
		}
	}

	/**
	 * <code>IndicationServer</code> represents an entry in the linked list or
	 * hash table of servers handled by this <code>CIMIndicationHandler</code>
	 * instance. Each entry in the list/table will have a unique
	 * serverIP/destinationURL pair along with its own
	 * <code>ReliableIndicationHandler</code>. This is done to handle multiple
	 * contexts from the same server.
	 * 
	 * NOTE: Multiple contexts from the same server will not be handled
	 * correctly if the user creates multiple
	 * <code>CIM_ListenerDestination</code> instances with the same
	 * <code>Destination</code> property. While this is poor programming
	 * practice (two different instances with same pertinent information), the
	 * real issue is that there is no way for the Client to differentiate
	 * between a context switch and two different contexts if the
	 * serverIP/destinationURL are the same. This issue it is not covered by
	 * DSP1054 1.2.
	 */
	private class IndicationServer {

		private boolean iRIInitialized = false;

		private ReliableIndicationHandler iRIHandler;

		public IndicationServer() {
		// initialize() does the work
		}

		public void initialize(ReliableIndicationHandler pRIHandler) {
			this.iRIInitialized = true;
			this.iRIHandler = pRIHandler;
		}

		public boolean isInitialized() {
			return this.iRIInitialized;
		}

		public ReliableIndicationHandler getRIHandler() {
			return this.iRIHandler;
		}
	}

	/**
	 * <code>ServerListEntry</code> represents a physical entry within the
	 * linked list. An <code>IndicationServer</code> instance is extracted from
	 * the linked list by proceeding through each entry in the linked list
	 * looking for the corresponding server IP and destination URL.
	 */
	private class ServerListEntry {

		private InetAddress iInetAddress;

		private String iDestinationUrl;

		private IndicationServer iIndicationServer;

		public ServerListEntry(InetAddress pInetAddress, String pDestinationUrl) {
			this.iInetAddress = pInetAddress;
			this.iDestinationUrl = pDestinationUrl;
			this.iIndicationServer = new IndicationServer();
		}

		public InetAddress getInetAddress() {
			return this.iInetAddress;
		}

		public String getDestinationUrl() {
			return this.iDestinationUrl;
		}

		public IndicationServer getIndicationServer() {
			return this.iIndicationServer;
		}
	}

	/**
	 * <code>ServerTableEntry</code> represents a physical key within the hash
	 * table while the corresponding <code>IndicationServer</code> is the value
	 * associated with the key. The hash table key is comprised of a server IP
	 * and destination URL.
	 */
	private class ServerTableEntry {

		private InetAddress iInetAddress;

		private String iDestinationUrl;

		public ServerTableEntry(InetAddress pInetAddress, String pDestinationUrl) {
			this.iInetAddress = pInetAddress;
			this.iDestinationUrl = pDestinationUrl;
		}

		public InetAddress getInetAddress() {
			return this.iInetAddress;
		}

		public String getDestinationUrl() {
			return this.iDestinationUrl;
		}

		@Override
		public boolean equals(Object pObj) {
			if (!(pObj instanceof ServerTableEntry)) return false;
			ServerTableEntry that = (ServerTableEntry) pObj;
			if (this.iInetAddress.equals(that.getInetAddress())
					&& this.iDestinationUrl.equalsIgnoreCase(that.getDestinationUrl())) return true;
			return false;
		}

		@Override
		public int hashCode() {
			return (this.iInetAddress.hashCode()) ^ (this.iDestinationUrl.hashCode());
		}
	}

	private CIMEventDispatcher iDispatcher = null;

	private int iMessageId = 0;

	private LogAndTraceBroker iLogger = LogAndTraceBroker.getBroker();

	private WBEMConfiguration iSessionProperties;

	private boolean iReliableIndicationsDisabled = true;

	private int iHashtableCapacity = 0; // 0 -> linked list, >0 -> hash table

	private LinkedList<ServerListEntry> iServerList;

	private Hashtable<ServerTableEntry, IndicationServer> iServerTable;

	private String iIndicationTraceClass;

	private String iIndicationTraceProperties[];

	private boolean iAddSenderIPAddress;

	private DataManager iDataManagerThread;

	/**
	 * Ctor.
	 * 
	 * @param pDispatcher
	 */
	public CIMIndicationHandler(CIMEventDispatcher pDispatcher) {
		this(pDispatcher, null);
	}

	/**
	 * Ctor.
	 * 
	 * @param pDispatcher
	 * @param pProperties
	 */
	public CIMIndicationHandler(CIMEventDispatcher pDispatcher, WBEMConfiguration pProperties) {
		this.iDispatcher = pDispatcher;
		this.iSessionProperties = (pProperties != null) ? pProperties : WBEMConfiguration
				.getGlobalConfiguration();
		this.iReliableIndicationsDisabled = !this.iSessionProperties.isReliableIndicationEnabled();

		// Initialize reliable indication support
		if (!this.iReliableIndicationsDisabled) {
			this.iHashtableCapacity = this.iSessionProperties
					.getReliableIndicationHashtableCapacity();

			// Validate ReliableIndicationHashtableCapacity property
			if (this.iHashtableCapacity < 0 || this.iHashtableCapacity > 25000) {
				this.iLogger.trace(Level.FINE, "ReliableIndicationHashtableCapacity of "
						+ this.iHashtableCapacity + " outside range, using default value");

				this.iHashtableCapacity = Integer.valueOf(
						WBEMConfigurationDefaults.LISTENER_RELIABLE_INDICATION_HASHTABLE_CAPACITY)
						.intValue();
			}

			if (this.iHashtableCapacity == 0) {
				this.iServerList = new LinkedList<ServerListEntry>();
				this.iDataManagerThread = new DataManager(this.iServerList);
			} else {
				this.iServerTable = new Hashtable<ServerTableEntry, IndicationServer>(
						this.iHashtableCapacity);
				this.iDataManagerThread = new DataManager(this.iServerTable);
			}

			this.iDataManagerThread.start();
		}

		// Initialize indication trace, if any
		String filter = this.iSessionProperties.getListenerIndicationTraceFilter();
		if (filter != null && filter.trim().length() > 0) {
			// Format: [class:]property[,property]*
			String properties;

			int colon = filter.indexOf(':');
			if (colon == -1) {
				this.iIndicationTraceClass = null;
				properties = filter;
			} else {
				this.iIndicationTraceClass = filter.substring(0, colon).trim().toLowerCase();
				properties = filter.substring(colon + 1).trim();
			}

			String[] props = properties.split(",");
			if (props != null && props.length > 0) {
				ArrayList<String> a = new ArrayList<String>();

				for (int i = 0; i < props.length; i++) {
					String prop = props[i].trim().toLowerCase();
					if (prop != null && prop.length() > 0) {
						a.add(prop);
					}
				}

				if (a.size() > 0) {
					this.iIndicationTraceProperties = a.toArray(new String[0]);

					if (this.iLogger.isLoggableTrace(Level.INFO)) {
						StringBuilder msg = new StringBuilder(
								"Indication trace enabled: class filter=");

						if (this.iIndicationTraceClass != null) {
							msg.append("\"");
							msg.append(this.iIndicationTraceClass);
							msg.append("\"");
						} else {
							msg.append("<none>");
						}
						msg.append(", properties=");
						for (int i = 0; i < this.iIndicationTraceProperties.length; i++) {
							if (i > 0) msg.append(",");
							msg.append("\"");
							msg.append(this.iIndicationTraceProperties[i]);
							msg.append("\"");
						}
						this.iLogger.trace(Level.INFO, msg.toString());
					}
				} else {
					this.iIndicationTraceProperties = null;
				}
			}
		}

		this.iAddSenderIPAddress = this.iSessionProperties.getListenerAddSenderIPAddress();
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			close();
		} finally {
			super.finalize();
		}
	}

	@Override
	public void close() {
		if (this.iDataManagerThread != null) {
			this.iDataManagerThread.stopRun();
			this.iDataManagerThread = null;
		}
		if (this.iDispatcher != null) {
			this.iDispatcher.close();
			this.iDispatcher = null;
		}
	}

	/**
	 * getMsgID
	 * 
	 * @return int
	 */
	public synchronized int getMsgID() {
		this.iMessageId++;
		if (this.iMessageId > 1000000) this.iMessageId = 0;
		return this.iMessageId;
	}

	private static HttpHeader processHeader(HttpHeader pHeader, MessageWriter pWriter)
			throws HttpException {

		HttpHeader header = processHttpExtensions(pHeader);
		String cimExport = header.getField("CIMExport");
		String cimOperation = header.getField("CIMOperation");

		if (cimOperation != null && !"METHODCALL".equalsIgnoreCase(cimOperation)) {
			pWriter.getHeader().addField("CIMError", "unsupported-operation");
			throw new HttpException(400, "Bad Request");
		}
		if (cimExport != null && !"METHODREQUEST".equalsIgnoreCase(cimExport)
				&& !"EXPORTMETHODCALL".equalsIgnoreCase(cimExport)) {
			pWriter.getHeader().addField("CIMError", "unsupported-operation");
			throw new HttpException(400, "Bad Request");
		}
		if (cimOperation == null && cimExport == null) {
			// TODO: verify the status returned by the server for this
			// situation, is not defined on the spec
			pWriter.getHeader().addField("CIMError", "unsupported-operation");
			throw new HttpException(400, "Bad Request");
		}

		return header;
	}

	@Override
	public void handleContent(MessageReader pReader, MessageWriter pWriter,
			InetAddress pInetAddress, String pLocalAddress) throws HttpException, IOException {

		CIMError error = null;

		// TODO validate CIMHeaders!
		HttpHeader inputHeader = pReader.getHeader();
		inputHeader = processHeader(inputHeader, pWriter);
		LogAndTraceBroker.getBroker().trace(Level.FINER,
				"Indication Request HTTP Headers= " + inputHeader.toString());
		String cimMethod = inputHeader.getField("CIMMethod");
		String cimExport = inputHeader.getField("CIMExport");

		CIMRequest request = null;
		CIMClientXML_HelperImpl xmlHelper = null;

		try {
			xmlHelper = new CIMClientXML_HelperImpl();
		} catch (ParserConfigurationException e1) {
			IOException e = new IOException("ParserConfigurationException");
			e.initCause(e1);
			throw e;
		}

		InputStream inputstream = null;
		if ((this.iSessionProperties.isCimXmlTracingEnabled() && LogAndTraceBroker.getBroker()
				.getXmlTraceStream() != null)
				|| LogAndTraceBroker.getBroker().isLoggableCIMXMLTrace(Level.FINEST)) {
			inputstream = new DebugInputStream(pReader.getInputStream(), LogAndTraceBroker
					.getBroker().getXmlTraceStream(), "indication request");
		} else {
			inputstream = pReader.getInputStream();
		}

		request = parseDocument(xmlHelper, pReader, inputstream);

		if (request == null) throw new HttpException(400, "Bad Request");

		if ((cimExport == null && !cimMethod.equalsIgnoreCase("Indication"))
				|| !request.isCIMExport()) { throw new HttpException(400, "Bad Request"); }

		error = dispatchIndications(pReader, pInetAddress, pLocalAddress, request);

		buildResponse(xmlHelper, pWriter, request, error);
	}

	private CIMRequest parseDocument(CIMClientXML_HelperImpl xmlHelper, MessageReader pReader,
			InputStream inputstream) throws HttpException {

		// TODO: integrate SAX parser and DOM parser

		Document doc = null;
		try {

			doc = xmlHelper.parse(new InputSource(new InputStreamReader(inputstream, pReader
					.getCharacterEncoding())));
			Element rootE = doc.getDocumentElement();
			return (CIMRequest) CIMXMLParserImpl.parseCIM(rootE);
		} catch (Exception e) {
			this.iLogger.trace(Level.WARNING, "exception while parsing the XML with DOM parser", e);
			throw new HttpException(400, "Bad Request");
		}
	}

	/**
	 * Returns the corresponding indication server if the server is already
	 * present in the linked list, otherwise appends the server to the linked
	 * list and returns the new entry.
	 * 
	 * @param pInetAddress
	 *            Address of indication server.
	 * @param pDestinationUrl
	 *            Destination URL of indication.
	 * @return <code>IndicationServer</code> with given address.
	 */
	private IndicationServer getIndicationServerFromList(InetAddress pInetAddress,
			String pDestinationUrl) {
		ServerListEntry entry;
		Iterator<ServerListEntry> iterator = this.iServerList.iterator();
		while (iterator.hasNext()) {
			entry = iterator.next();
			if (entry.getInetAddress().equals(pInetAddress)
					&& entry.getDestinationUrl().equalsIgnoreCase(pDestinationUrl)) return entry
					.getIndicationServer();
		}
		entry = new ServerListEntry(pInetAddress, pDestinationUrl);
		this.iServerList.add(entry);
		this.iLogger.trace(Level.FINE, "Creating reliable indication handler for server IP "
				+ entry.getInetAddress().toString() + ", destination URL "
				+ entry.getDestinationUrl() + " in linked list");

		return entry.getIndicationServer();
	}

	/**
	 * Returns the corresponding indication server if the server is already
	 * present in the hash table, otherwise adds the server to the hash table
	 * and returns the new entry.
	 * 
	 * @param pInetAddress
	 *            Address of indication server.
	 * @param pDestinationUrl
	 *            Destination URL of indication.
	 * @return <code>IndicationServer</code> with given address.
	 */
	private IndicationServer getIndicationServerFromTable(InetAddress pInetAddress,
			String pDestinationUrl) {
		ServerTableEntry key;
		IndicationServer server;

		key = new ServerTableEntry(pInetAddress, pDestinationUrl);
		server = this.iServerTable.get(key);
		if (server == null) {
			server = new IndicationServer();
			this.iServerTable.put(key, server);
			this.iLogger.trace(Level.FINE, "Creating reliable indication handler for server IP "
					+ key.getInetAddress().toString() + ", destination URL "
					+ key.getDestinationUrl() + " in hash table");
		}
		return server;
	}

	/**
	 * deliverIndication handles reliable indications and returns a boolean
	 * indicating whether the indication should be delivered or not
	 * 
	 * @param pIndication
	 *            Indication.
	 * @param pId
	 *            Indication destination URL.
	 * @param pInetAddress
	 *            Indication server IP.
	 * @return <code>true</code> if indication should be delivered,
	 *         <code>false</code> if <code>ReliableIndicationHandler</code> will
	 *         deliver indication
	 */
	private synchronized boolean deliverIndication(CIMInstance pIndication, String pId,
			InetAddress pInetAddress) {
		IndicationServer server;

		// Each serverIP/destinationURL pair needs its own
		// ReliableIndicationHandler in order to handle multiple contexts
		if (this.iHashtableCapacity == 0) {
			server = getIndicationServerFromList(pInetAddress, pId);
		} else {
			server = getIndicationServerFromTable(pInetAddress, pId);
		}

		// Initialize if not yet done so
		if (!server.isInitialized()) {
			// Validate DeliveryRetryAttempts property
			long attempts = this.iSessionProperties.getListenerDeliveryRetryAttempts();
			if (attempts <= 0 || attempts > 1000) {
				this.iLogger.trace(Level.FINE, "DeliveryRetryAttempts of " + attempts
						+ " outside range, using default value");

				attempts = Long.valueOf(WBEMConfigurationDefaults.LISTENER_DELIVERY_RETRY_ATTEMPTS)
						.longValue();
			}

			// Validate DeliveryRetryInterval property
			long interval = this.iSessionProperties.getListenerDeliveryRetryInterval();
			if (interval <= 0 || interval > 86400) {
				this.iLogger.trace(Level.FINE, "DeliveryRetryInterval of " + interval
						+ " outside range, using default value");

				interval = Long.valueOf(WBEMConfigurationDefaults.LISTENER_DELIVERY_RETRY_INTERVAL)
						.longValue();
			}

			// Create new ReliableIndicationHandler for this
			// CIMIndicationHandler
			server.initialize(new ReliableIndicationHandler(this.iDispatcher, attempts * interval
					* 10 * 1000));

			this.iLogger
					.trace(Level.FINE, "Reliable indication support enabled for IP "
							+ pInetAddress.getHostAddress() + " and URL " + pId
							+ ", DeliveryRetryAttempts=" + attempts + ", DeliveryRetryInterval="
							+ interval);
		}

		// Let ReliableIndicationHandler deliver it
		server.getRIHandler().handleIndication(pIndication, pId, pInetAddress);
		return false;
	}

	private CIMError dispatchIndications(MessageReader pReader, InetAddress pInetAddress,
			String pLocalAddress, CIMRequest request) {
		try {
			Vector<Object> paramValue = request.getParamValue();
			Iterator<Object> iter = paramValue.iterator();
			while (iter.hasNext()) {
				Object cimEvent = iter.next();
				if (cimEvent instanceof CIMInstance) {
					CIMInstance indicationInst = (CIMInstance) cimEvent;

					String path = pReader.getMethod().getFile();
					String id;

					if (path == null) {
						id = pLocalAddress + "/";
					} else if (path.equalsIgnoreCase("/cimom")) {
						id = path;
					} else if (path.length() < 4 || !path.regionMatches(true, 0, "http", 0, 4)) {
						if (path.startsWith("/")) {
							id = pLocalAddress + path;
						} else {
							id = pLocalAddress + "/" + path;
						}
					} else /* path.startsWith("http") */{
						id = path;
					}

					// Add SenderIPAddress property if enabled
					if (this.iAddSenderIPAddress) {
						int size = indicationInst.getPropertyCount();
						CIMProperty<?> props[] = new CIMProperty<?>[size + 1];
						for (int i = 0; i < size; i++)
							props[i] = indicationInst.getProperty(i);
						props[size] = new CIMProperty<String>("SBLIMJCC_SenderIPAddress",
								CIMDataType.STRING_T, pInetAddress.getHostAddress());
						indicationInst = new CIMInstance(indicationInst.getObjectPath(), props);
					}

					// Do any user-requested indication tracing here
					if (this.iIndicationTraceProperties != null
							&& this.iLogger.isLoggableTrace(Level.FINE)
							&& (this.iIndicationTraceClass == null || indicationInst.getClassName()
									.toLowerCase().indexOf(this.iIndicationTraceClass) != -1)) {
						StringBuilder msg = new StringBuilder("Received indication ");
						msg.append(indicationInst.getClassName());
						msg.append(" from IP=");
						String inet = pInetAddress.getHostAddress();
						msg.append(inet != null ? inet : "<null>");
						msg.append(" to URL=");
						msg.append(id);
						msg.append(" with properties:");
						for (int i = 0; i < this.iIndicationTraceProperties.length; i++) {
							msg.append(" ");
							CIMProperty<?> prop = indicationInst
									.getProperty(this.iIndicationTraceProperties[i]);
							if (prop == null) {
								msg.append(this.iIndicationTraceProperties[i]);
								msg.append(" not a property;");
							} else {
								Object value = prop.getValue();
								if (value == null) {
									msg.append(prop.getName());
									msg.append(" = <null>;");
								} else {
									msg.append(prop.toString().trim());
								}
							}
						}
						this.iLogger.trace(Level.FINE, msg.toString());
					}

					if (this.iReliableIndicationsDisabled
							|| deliverIndication(indicationInst, id, pInetAddress)) {
						this.iDispatcher.dispatchEvent(new CIMEvent(indicationInst, id,
								pInetAddress));
					}
				}
			}
			return null;
		} catch (Exception e) {
			return new CIMError(new WBEMException(WBEMException.CIM_ERR_FAILED, "CIM_ERR_FAILED",
					null, e));
		}
	}

	private void buildResponse(CIMClientXML_HelperImpl xmlHelper, MessageWriter pWriter,
			CIMRequest request, CIMError error) throws HttpException, IOException {
		Document responseDoc = null;
		try {
			DocumentBuilder docBuilder = xmlHelper.getDocumentBuilder();
			responseDoc = docBuilder.newDocument();
			CIMXMLBuilderImpl.createIndication_response(responseDoc, request.getId(), error);

		} catch (Exception e) {
			// TODO: check this error code, may not be appropriate
			throw new HttpException(400, "Bad Request");
		}
		if (this.iSessionProperties.isCimXmlTracingEnabled()
				|| this.iLogger.isLoggableCIMXMLTrace(Level.FINEST)) {
			OutputStream pos = new ByteArrayOutputStream();
			CIMClientXML_HelperImpl.dumpDocument(pos, responseDoc, "indication response");
			OutputStream debugStream = this.iLogger.getXmlTraceStream();
			if (this.iSessionProperties.isCimXmlTracingEnabled() && debugStream != null) debugStream
					.write(pos.toString().getBytes());
			if (this.iLogger.isLoggableCIMXMLTrace(Level.FINEST)) this.iLogger.traceCIMXML(
					Level.FINEST, pos.toString(), true);
		}
		CIMClientXML_HelperImpl.serialize(pWriter.getOutputStream(), responseDoc);
		pWriter.getHeader().addField("CIMExport", "MethodResponse");
	}

	private static HttpHeader processHttpExtensions(HttpHeader pOriginalHeader) {
		String man = pOriginalHeader.getField("Man");
		String opt = pOriginalHeader.getField("Opt");
		HttpHeader headers = new HttpHeader();
		String ns = null;

		HttpHeaderParser manOptHeader = null;
		if (man != null && man.length() > 0) manOptHeader = new HttpHeaderParser(man);
		else if (opt != null && opt.length() > 0) manOptHeader = new HttpHeaderParser(opt);
		if (manOptHeader != null) ns = manOptHeader.getValue("ns");

		if (ns != null) {

			Iterator<Entry<HeaderEntry, String>> iter = pOriginalHeader.iterator();
			String key;
			while (iter.hasNext()) {
				Entry<HeaderEntry, String> entry = iter.next();
				if (entry != null) {
					key = entry.getKey().toString();
					if (key.startsWith(ns + "-")) headers.addParsedField(key.substring(3), entry
							.getValue().toString());
					else headers.addParsedField(key, entry.getValue().toString());
				}
			}
		} else {
			headers = pOriginalHeader;
		}

		return headers;
	}

}
