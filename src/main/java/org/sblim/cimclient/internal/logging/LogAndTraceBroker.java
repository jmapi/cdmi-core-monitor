/**
 * (C) Copyright IBM Corp. 2006, 2013
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
 *------------------------------------------------------------------------------- 
 * 1565892    2006-11-14  lupusalex    Make SBLIM client JSR48 compliant
 * 1711092    2006-05-02  lupusalex    Some fixes/additions of log&trace messages
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3001345    2010-05-18  blaschke-oss File handle leaks in HttpSocketFactory and LogAndTraceBroker
 * 3027618    2010-07-14  blaschke-oss Close files/readers in finally blocks
 * 3154232    2011-01-13  blaschke-oss EmbeddedObject misspelled in javadoc
 * 3252669    2011-03-28  blaschke-oss setXmlTraceStream blindly closes previous stream
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3469018    2012-01-03  blaschke-oss Properties not passed to CIMIndicationHandler
 * 3484014    2012-02-03  blaschke-oss Add LogAndTraceBroker.isLoggable for message/trace
 * 3489638    2012-02-28  blaschke-oss PERF: Bottleneck in LogAndTraceBroker.java - getCaller()
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
 * 3576396    2012-10-11  blaschke-oss Improve logging of config file name
 * 3596303    2013-01-04  blaschke-oss windows http response WWW-Authenticate: Negotiate fails
 *    2652    2013-07-26  blaschke-oss LogAndTraceBroker.setXmlTraceStream should not close previous stream
 *    2651    2013-07-31  blaschke-oss IOException when tracing the cimxml
 */

package org.sblim.cimclient.internal.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.sblim.cimclient.CIMXMLTraceListener;
import org.sblim.cimclient.LogAndTraceManager;
import org.sblim.cimclient.LogListener;
import org.sblim.cimclient.TraceListener;
import org.sblim.cimclient.WBEMConfigurationProperties;
import org.sblim.cimclient.internal.util.WBEMConfiguration;
import org.sblim.cimclient.internal.util.WBEMConstants;

/**
 * Class LogAndTraceBroker is the central class that implements the logging and
 * tracing of the CIM Client. It manages the collections of the internal log and
 * trace listeners. It sets up the application independent logging. It provides
 * the API to send log and trace messages and forwards them to the appropriate
 * listeners.
 * 
 */
public class LogAndTraceBroker {

	private static final String TRACE_LOGGER = "org.sblim.cimclient.trace";

	private static final String FILE_LOGGER = "org.sblim.cimclient.file";

	private static final String CONSOLE_LOGGER = "org.sblim.cimclient.console";

	private static LogAndTraceBroker cBroker = new LogAndTraceBroker();

	/**
	 * Returns the singleton instance of the broker
	 * 
	 * @return The broker instance
	 */
	public static LogAndTraceBroker getBroker() {
		return cBroker;
	}

	/**
	 * Returns if the logging framework has been initialized. This method is
	 * used by the <code>WBEMConfiguration</code> class to determine if the
	 * logging is already up. The <code>WBEMConfiguration</code> is initialized
	 * before the logging, so methods in this class cannot assume the logging to
	 * be up and running.
	 * 
	 * @return <code>true</code> if the logging is up, <code>false</code>
	 *         otherwise
	 */
	public static boolean isLoggingStarted() {
		return cBroker != null;
	}

	private volatile ArrayList<LogListener> iLogListeners;

	private volatile ArrayList<TraceListener> iTraceListeners;

	private volatile ArrayList<CIMXMLTraceListener> iCIMXMLTraceListeners;

	private String iProductName = "SBLIM CIM Client for Java";

	private String iCopyright = "COPYRIGHT (C) 2006, 2013 IBM Corp.";

	private String iVersion = "?";

	private String iBuildDate = "?";

	private String iBuildTime = "?";

	private OutputStream iXmlTraceStream = null;

	private OutputStream iXmlTraceFile = null;

	private final String iTHIS_CLASS = this.getClass().getName();

	private final String iTHROWABLE = Throwable.class.getName();

	private int iInternalLevelConsole = Level.OFF.intValue();

	private int iInternalLevelLogFile = Level.OFF.intValue();

	private int iInternalLevelTraceFile = Level.OFF.intValue();

	private LogListener iInternalListenerLogConsole = null;

	private LogListener iInternalListenerLogFile = null;

	private TraceListener iInternalListenerTraceConsole = null;

	private TraceListener iInternalListenerTraceFile = null;

	private int iNumInternalLogListeners = 0;

	private int iNumExternalLogListeners = 0;

	private int iNumInternalTraceListeners = 0;

	private int iNumExternalTraceListeners = 0;

	private LogAndTraceBroker() {
		this.iLogListeners = new ArrayList<LogListener>();
		this.iTraceListeners = new ArrayList<TraceListener>();
		this.iCIMXMLTraceListeners = new ArrayList<CIMXMLTraceListener>();
		loadVersionTxt();
		registerInternalListeners();
		initXmlTraceFile();
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			if (this.iXmlTraceFile != null && (!this.iXmlTraceFile.equals(System.out))
					&& (!this.iXmlTraceFile.equals(System.err))) this.iXmlTraceFile.close();
		} catch (IOException e) {
			// bad luck
		} finally {
			this.iXmlTraceFile = null;
			super.finalize();
		}
	}

	/**
	 * Registers the listeners for our internal loggers
	 */
	public void registerInternalListeners() {
		try {
			Level level = WBEMConfiguration.getGlobalConfiguration().getLogConsoleLevel();
			String type = WBEMConfiguration.getGlobalConfiguration().getLogConsoleType();
			if (level.intValue() < Level.OFF.intValue() && WBEMConstants.MESSAGE.equals(type)) {
				final Logger logger = Logger.getLogger(CONSOLE_LOGGER);
				final Handler handler = new ConsoleHandler();
				handler.setFormatter(new LogFormatter());
				handler.setLevel(level);
				logger.addHandler(handler);
				logger.setLevel(level);
				logger.setUseParentHandlers(false);
				this.iInternalLevelConsole = level.intValue();
				if (this.iInternalListenerLogConsole != null) removeLogListener(this.iInternalListenerLogConsole);
				this.iInternalListenerLogConsole = new LogListener() {

					public void log(Level pLevel, String pMessageKey, String pMessage,
							Object[] pParameters) {
						LogRecord record = new LogRecord(pLevel, pMessageKey + " " + pMessage);
						record.setParameters(pParameters);
						logger.log(record);
					}
				};
				addLogListener(this.iInternalListenerLogConsole);
			}
		} catch (Exception e) {
			// Don't crash for logging
		}
		try {
			Level level = WBEMConfiguration.getGlobalConfiguration().getLogFileLevel();
			String location = WBEMConfiguration.getGlobalConfiguration().getLogFileLocation();
			int size = WBEMConfiguration.getGlobalConfiguration().getLogFileSizeLimit();
			int count = WBEMConfiguration.getGlobalConfiguration().getLogFileCount();
			if (level.intValue() < Level.OFF.intValue()) {
				final Logger logger = Logger.getLogger(FILE_LOGGER);
				final Handler handler = new FileHandler(location, size, count);
				handler.setFormatter(new LogFormatter());
				handler.setLevel(level);
				logger.addHandler(handler);
				logger.setLevel(level);
				logger.setUseParentHandlers(false);
				this.iInternalLevelLogFile = level.intValue();
				if (this.iInternalListenerLogFile != null) removeLogListener(this.iInternalListenerLogFile);
				this.iInternalListenerLogFile = new LogListener() {

					public void log(Level pLevel, String pMessageKey, String pMessage,
							Object[] pParameters) {
						LogRecord record = new LogRecord(pLevel, pMessageKey + " " + pMessage);
						record.setParameters(pParameters);
						logger.log(record);
					}
				};
				addLogListener(this.iInternalListenerLogFile);
			}
		} catch (Exception e) {
			// Don't crash for logging
		}
		try {
			Level level = WBEMConfiguration.getGlobalConfiguration().getLogConsoleLevel();
			String type = WBEMConfiguration.getGlobalConfiguration().getLogConsoleType();
			if (level.intValue() < Level.OFF.intValue() && WBEMConstants.TRACE.equals(type)) {
				final Logger logger = Logger.getLogger(CONSOLE_LOGGER);
				final Handler handler = new ConsoleHandler();
				handler.setFormatter(new TraceFormatter());
				handler.setLevel(level);
				logger.addHandler(handler);
				logger.setLevel(level);
				logger.setUseParentHandlers(false);
				this.iInternalLevelConsole = level.intValue();
				if (this.iInternalListenerTraceConsole != null) removeTraceListener(this.iInternalListenerTraceConsole);
				this.iInternalListenerTraceConsole = new TraceListener() {

					public void trace(Level pLevel, StackTraceElement pOrigin, String pMessage) {
						LogRecord record = new LogRecord(pLevel, pMessage);
						record.setSourceMethodName(String.valueOf(pOrigin));
						logger.log(record);
					}

					public void trace(Level pLevel, StackTraceElement pOrigin, String pMessage,
							Throwable pThrown) {
						LogRecord record = new LogRecord(pLevel, pMessage);
						record.setSourceMethodName(String.valueOf(pOrigin));
						record.setThrown(pThrown);
						logger.log(record);
					}
				};
				addTraceListener(this.iInternalListenerTraceConsole);
			}
		} catch (Exception e) {
			// Don't crash for logging
		}
		try {
			Level level = WBEMConfiguration.getGlobalConfiguration().getTraceFileLevel();
			String location = WBEMConfiguration.getGlobalConfiguration().getTraceFileLocation();
			int size = WBEMConfiguration.getGlobalConfiguration().getTraceFileSizeLimit();
			int count = WBEMConfiguration.getGlobalConfiguration().getTraceFileCount();
			if (level.intValue() < Level.OFF.intValue()) {
				final Logger logger = Logger.getLogger(TRACE_LOGGER);
				final Handler handler = new FileHandler(location, size, count);
				handler.setFormatter(new TraceFormatter());
				handler.setLevel(level);
				logger.addHandler(handler);
				logger.setLevel(level);
				logger.setUseParentHandlers(false);
				this.iInternalLevelTraceFile = level.intValue();
				if (this.iInternalListenerTraceFile != null) removeTraceListener(this.iInternalListenerTraceFile);
				this.iInternalListenerTraceFile = new TraceListener() {

					public void trace(Level pLevel, StackTraceElement pOrigin, String pMessage) {
						LogRecord record = new LogRecord(pLevel, pMessage);
						record.setSourceMethodName(String.valueOf(pOrigin));
						logger.log(record);
					}

					public void trace(Level pLevel, StackTraceElement pOrigin, String pMessage,
							Throwable pThrown) {
						LogRecord record = new LogRecord(pLevel, pMessage);
						record.setSourceMethodName(String.valueOf(pOrigin));
						record.setThrown(pThrown);
						logger.log(record);
					}
				};
				addTraceListener(this.iInternalListenerTraceFile);
			}
		} catch (Exception e) {
			// Don't crash for logging
		}
	}

	/**
	 * Adds a listener for log messages. The listener will be notified of any
	 * log event. Uses copy on write to ensure concurrent read access.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public synchronized void addLogListener(LogListener pListener) {
		if (pListener == null) return;
		sendGreetings(pListener);
		ArrayList<LogListener> newListeners = new ArrayList<LogListener>(this.iLogListeners);
		newListeners.add(pListener);
		this.iLogListeners = newListeners;

		if ((this.iInternalListenerLogFile != null && this.iInternalListenerLogFile
				.equals(pListener))
				|| (this.iInternalListenerLogConsole != null && this.iInternalListenerLogConsole
						.equals(pListener))) {
			this.iNumInternalLogListeners++;
		} else {
			this.iNumExternalLogListeners++;
		}
	}

	private void loadVersionTxt() {
		InputStream is = null;
		try {
			Properties version = new Properties();
			is = LogAndTraceManager.class.getResourceAsStream("version.txt");
			version.load(is);
			this.iProductName = version.getProperty("PRODUCTNAME");
			this.iCopyright = version.getProperty("COPYRIGHT");
			this.iVersion = version.getProperty("VERSION");
			this.iBuildDate = version.getProperty("BUILDDATE");
			this.iBuildTime = version.getProperty("BUILDTIME");
		} catch (Exception e) {
			// nothing to do
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// nothing ro do
				}
			}
		}
	}

	private void sendGreetings(LogListener pListener) {

		pListener.log(MessageLoader.getLevel(Messages.GREETING), Messages.GREETING, MessageLoader
				.getLocalizedMessage(Messages.GREETING), new Object[] { this.iProductName,
				this.iCopyright });
		pListener.log(MessageLoader.getLevel(Messages.RELEASE), Messages.RELEASE, MessageLoader
				.getLocalizedMessage(Messages.RELEASE), new Object[] { this.iVersion,
				this.iBuildDate, this.iBuildTime });
		pListener.log(MessageLoader.getLevel(Messages.OS), Messages.OS, MessageLoader
				.getLocalizedMessage(Messages.OS), new Object[] { System.getProperty("os.name"),
				System.getProperty("os.version"), System.getProperty("os.arch") });
		pListener.log(MessageLoader.getLevel(Messages.JRE), Messages.JRE, MessageLoader
				.getLocalizedMessage(Messages.JRE), new Object[] {
				System.getProperty("java.version"), System.getProperty("java.vendor") });
		pListener.log(MessageLoader.getLevel(Messages.JVM), Messages.JVM, MessageLoader
				.getLocalizedMessage(Messages.JVM), new Object[] {
				System.getProperty("java.vm.name"), System.getProperty("java.vm.version"),
				System.getProperty("java.vm.vendor") });
		pListener.log(MessageLoader.getLevel(Messages.CONFIGURATION_URL),
				Messages.CONFIGURATION_URL, MessageLoader
						.getLocalizedMessage(Messages.CONFIGURATION_URL),
				new Object[] { WBEMConfiguration.getActiveConfigFullURL() });
		if (!WBEMConfiguration.isConfigurationLoadSuccessful()) {
			pListener.log(MessageLoader.getLevel(Messages.CONFIGURATION_LOAD_FAILED),
					Messages.CONFIGURATION_LOAD_FAILED, MessageLoader
							.getLocalizedMessage(Messages.CONFIGURATION_LOAD_FAILED),
					(Object[]) null);
			if (WBEMConfiguration.getConfigurationLoadException() != null) {
				pListener.log(MessageLoader.getLevel(Messages.EXCEPTION_DURING_CONFIGURATION_LOAD),
						Messages.EXCEPTION_DURING_CONFIGURATION_LOAD, MessageLoader
								.getLocalizedMessage(Messages.EXCEPTION_DURING_CONFIGURATION_LOAD),
						new Object[] { WBEMConfiguration.getConfigurationLoadException()
								.getMessage() });
			}
		}
	}

	private void sendGreetings(TraceListener pListener) {
		StackTraceElement origin = new Throwable().getStackTrace()[0];
		pListener.trace(MessageLoader.getLevel(Messages.GREETING), origin, MessageFormat.format(
				MessageLoader.getMessage(Messages.GREETING), new Object[] { this.iProductName,
						this.iCopyright }));
		pListener.trace(MessageLoader.getLevel(Messages.RELEASE), origin, MessageFormat.format(
				MessageLoader.getMessage(Messages.RELEASE), new Object[] { this.iVersion,
						this.iBuildDate, this.iBuildTime }));
		pListener.trace(MessageLoader.getLevel(Messages.OS), origin, MessageFormat.format(
				MessageLoader.getMessage(Messages.OS), new Object[] {
						System.getProperty("os.name"), System.getProperty("os.version"),
						System.getProperty("os.arch") }));
		pListener.trace(MessageLoader.getLevel(Messages.JRE), origin, MessageFormat.format(
				MessageLoader.getMessage(Messages.JRE), new Object[] {
						System.getProperty("java.version"), System.getProperty("java.vendor") }));
		pListener.trace(MessageLoader.getLevel(Messages.JVM), origin, MessageFormat.format(
				MessageLoader.getMessage(Messages.JVM), new Object[] {
						System.getProperty("java.vm.name"), System.getProperty("java.vm.version"),
						System.getProperty("java.vm.vendor") }));
		pListener.trace(MessageLoader.getLevel(Messages.CONFIGURATION_URL), origin, MessageFormat
				.format(MessageLoader.getMessage(Messages.CONFIGURATION_URL),
						new Object[] { WBEMConfiguration.getActiveConfigURL() }));
		if (!WBEMConfiguration.isConfigurationLoadSuccessful()) {
			pListener.trace(MessageLoader.getLevel(Messages.CONFIGURATION_LOAD_FAILED), origin,
					MessageLoader.getMessage(Messages.CONFIGURATION_LOAD_FAILED));
			if (WBEMConfiguration.getConfigurationLoadException() != null) {
				pListener.trace(MessageLoader
						.getLevel(Messages.EXCEPTION_DURING_CONFIGURATION_LOAD), origin,
						MessageLoader.getMessage(Messages.EXCEPTION_DURING_CONFIGURATION_LOAD),
						WBEMConfiguration.getConfigurationLoadException());
			}
		}
	}

	/**
	 * Remove a listener. This listener will not be notified of log events
	 * anymore.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public synchronized void removeLogListener(LogListener pListener) {
		ArrayList<LogListener> newListeners = new ArrayList<LogListener>(this.iLogListeners);
		if (!newListeners.remove(pListener)) return;
		this.iLogListeners = newListeners;

		if (this.iInternalListenerLogFile != null
				&& this.iInternalListenerLogFile.equals(pListener)) {
			// Removing internal log listener for file
			this.iInternalListenerLogFile = null;
			this.iNumInternalLogListeners--;
		} else if (this.iInternalListenerLogConsole != null
				&& this.iInternalListenerLogConsole.equals(pListener)) {
			// Removing internal log listener for console
			this.iInternalListenerLogConsole = null;
			this.iNumInternalLogListeners--;
		} else {
			// Removing user log listener
			this.iNumExternalLogListeners--;
		}
	}

	/**
	 * Removes all listeners. Caution this will also remove the internal console
	 * and file loggers.
	 */
	public synchronized void clearLogListeners() {
		this.iLogListeners = new ArrayList<LogListener>();
		removeHandlers(Logger.getLogger(CONSOLE_LOGGER));
		removeHandlers(Logger.getLogger(FILE_LOGGER));

		this.iInternalListenerLogFile = null;
		this.iInternalListenerLogConsole = null;
		this.iNumInternalLogListeners = 0;
		this.iNumExternalLogListeners = 0;
	}

	/**
	 * Gets the registered log listeners including the internal console and file
	 * loggers.
	 * 
	 * @return The list of listeners
	 */
	public List<LogListener> getLogListeners() {
		return this.iLogListeners;
	}

	/**
	 * Adds a listener for log messages. The listener will be notified of any
	 * trace event.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public synchronized void addTraceListener(TraceListener pListener) {
		if (pListener == null) return;
		sendGreetings(pListener);
		ArrayList<TraceListener> newListeners = new ArrayList<TraceListener>(this.iTraceListeners);
		newListeners.add(pListener);
		this.iTraceListeners = newListeners;

		if ((this.iInternalListenerTraceFile != null && this.iInternalListenerTraceFile
				.equals(pListener))
				|| (this.iInternalListenerTraceConsole != null && this.iInternalListenerTraceConsole
						.equals(pListener))) {
			this.iNumInternalTraceListeners++;
		} else {
			this.iNumExternalTraceListeners++;
		}
	}

	/**
	 * Removes a listener. This listener will not be notified of trace events
	 * anymore.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public synchronized void removeTraceListener(TraceListener pListener) {
		ArrayList<TraceListener> newListeners = new ArrayList<TraceListener>(this.iTraceListeners);
		if (!newListeners.remove(pListener)) return;
		this.iTraceListeners = newListeners;

		if (this.iInternalListenerTraceFile != null
				&& this.iInternalListenerTraceFile.equals(pListener)) {
			// Removing internal tracelistener for file
			this.iInternalListenerTraceFile = null;
			this.iNumInternalTraceListeners--;
		} else if (this.iInternalListenerTraceConsole != null
				&& this.iInternalListenerTraceConsole.equals(pListener)) {
			// Removing internal trace listener for console
			this.iInternalListenerTraceConsole = null;
			this.iNumInternalTraceListeners--;
		} else {
			// Removing user trace listener
			this.iNumExternalTraceListeners--;
		}
	}

	/**
	 * Removes all listeners. Caution this will also remove the internal trace
	 * file listener.
	 */
	public synchronized void clearTraceListeners() {
		this.iTraceListeners = new ArrayList<TraceListener>();
		removeHandlers(Logger.getLogger(TRACE_LOGGER));

		this.iInternalListenerTraceFile = null;
		this.iInternalListenerTraceConsole = null;
		this.iNumInternalTraceListeners = 0;
		this.iNumExternalTraceListeners = 0;
	}

	/**
	 * Gets the registered trace listeners including the internal console and
	 * file loggers.
	 * 
	 * @return A list of listeners
	 */
	public List<TraceListener> getTraceListeners() {
		return this.iTraceListeners;
	}

	/**
	 * Adds a listener for CIM-XML trace messages. The listener will be notified
	 * of any CIM-XML trace event.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public synchronized void addCIMXMLTraceListener(CIMXMLTraceListener pListener) {
		if (pListener == null) return;
		ArrayList<CIMXMLTraceListener> newListeners = new ArrayList<CIMXMLTraceListener>(
				this.iCIMXMLTraceListeners);
		newListeners.add(pListener);
		this.iCIMXMLTraceListeners = newListeners;
	}

	/**
	 * Removes a CIM-XML trace listener. This listener will not be notified of
	 * CIM-XML trace events anymore.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public synchronized void removeCIMXMLTraceListener(CIMXMLTraceListener pListener) {
		ArrayList<CIMXMLTraceListener> newListeners = new ArrayList<CIMXMLTraceListener>(
				this.iCIMXMLTraceListeners);
		if (!newListeners.remove(pListener)) return;
		this.iCIMXMLTraceListeners = newListeners;
	}

	/**
	 * Removes all CIM-XML trace listeners.
	 */
	public synchronized void clearCIMXMLTraceListeners() {
		if (this.iCIMXMLTraceListeners.size() > 0) this.iCIMXMLTraceListeners = new ArrayList<CIMXMLTraceListener>();
	}

	/**
	 * Gets the registered CIM-XML trace listeners.
	 * 
	 * @return A list of listeners
	 */
	public List<CIMXMLTraceListener> getCIMXMLTraceListeners() {
		return this.iCIMXMLTraceListeners;
	}

	/**
	 * Forwards a log/trace message to the registered log&trace listeners.
	 * 
	 * @param pKey
	 *            The message identifier.
	 */
	public void message(String pKey) {
		message(pKey, (Object[]) null);
	}

	/**
	 * Forwards a log/trace message to the registered log&trace listeners.
	 * 
	 * @param pKey
	 *            The message identifier.
	 * @param pParameter
	 *            The parameter for the message
	 */
	public void message(String pKey, Object pParameter) {
		message(pKey, new Object[] { pParameter });
	}

	/**
	 * Forwards a log/trace message to the registered log&trace listeners.
	 * 
	 * @param pKey
	 *            The message identifier.
	 * @param pParameters
	 *            The parameters for the message
	 */
	public void message(String pKey, Object[] pParameters) {
		try {
			final String message = MessageLoader.getMessage(pKey);
			final String localMessage = MessageLoader.getLocalizedMessage(pKey);
			final Level level = MessageLoader.getLevel(pKey);
			if (isLoggableTrace(level)) {
				final List<TraceListener> traceListeners = getTraceListeners();
				StackTraceElement caller = getCaller();
				for (int i = 0; i < traceListeners.size(); ++i) {
					traceListeners.get(i).trace(level, caller,
							pKey + " " + MessageFormat.format(message, pParameters));
				}
			}
			final List<LogListener> logListeners = getLogListeners();
			for (int i = 0; i < logListeners.size(); ++i) {
				logListeners.get(i).log(level, pKey, localMessage, pParameters);
			}
		} catch (Exception e) {
			// don't crash for logging
		}
	}

	/**
	 * Forwards a trace message to the registered trace listeners.
	 * 
	 * @param pLevel
	 *            One of the three message level identifiers FINE, FINER and
	 *            FINEST
	 * @param pMessage
	 *            The message text
	 */
	public void trace(Level pLevel, String pMessage) {
		try {
			if (isLoggableTrace(pLevel)) {
				final List<TraceListener> traceListeners = getTraceListeners();
				StackTraceElement caller = getCaller();
				for (int i = 0; i < traceListeners.size(); ++i) {
					traceListeners.get(i).trace(pLevel, caller, pMessage);
				}
			}
		} catch (Exception e) {
			// don't crash for logging
		}
	}

	/**
	 * Forwards a trace message to the registered trace listeners.
	 * 
	 * @param pLevel
	 *            One of the three message level identifiers FINE, FINER and
	 *            FINEST
	 * @param pMessage
	 *            The message text
	 * @param pThrown
	 *            The throwable associated with the message
	 */
	public void trace(Level pLevel, String pMessage, Throwable pThrown) {
		try {
			if (isLoggableTrace(pLevel)) {
				final List<TraceListener> traceListeners = getTraceListeners();
				StackTraceElement caller = getCaller();
				for (int i = 0; i < traceListeners.size(); ++i) {
					traceListeners.get(i).trace(pLevel, caller, pMessage, pThrown);
				}
			}
		} catch (Exception e) {
			// don't crash for logging
		}
	}

	/**
	 * Forwards a CIM-XML trace message to the registered CIM-XML trace
	 * listeners.
	 * 
	 * @param pLevel
	 *            One of the message level identifiers, e.g. FINE
	 * @param pMessage
	 *            The CIM-XML message text
	 * @param pOutgoing
	 *            <code>true</code> if CIM-XML is outgoing (being sent from
	 *            client to server), <code>false</code> if CIM-XML is incoming
	 *            (being sent from server to client)
	 */
	public void traceCIMXML(Level pLevel, String pMessage, boolean pOutgoing) {
		try {
			if (this.iCIMXMLTraceListeners.size() > 0) {
				final List<CIMXMLTraceListener> traceListeners = getCIMXMLTraceListeners();
				for (int i = 0; i < traceListeners.size(); ++i) {
					traceListeners.get(i).traceCIMXML(pLevel, pMessage, pOutgoing);
				}
			}
		} catch (Exception e) {
			// don't crash for logging
		}
	}

	/**
	 * Forwards a method entry message to the registered trace listeners.
	 */
	public void entry() {
		trace(Level.FINEST, "Entering method");
	}

	/**
	 * Forwards a method exit message to the registered trace listeners.
	 */
	public void exit() {
		trace(Level.FINEST, "Exiting method");
	}

	/**
	 * Returns the output stream to which all CIM-XML traffic (outgoing &amp;
	 * incoming) will be copied for debugging purposes.
	 * 
	 * @return The output stream. A <code>null</code> value means that CIM-XML
	 *         debugging is disabled
	 */
	public OutputStream getXmlTraceStream() {
		return this.iXmlTraceStream;
	}

	/**
	 * Sets an output stream to which all CIM-XML traffic (outgoing &amp;
	 * incoming) will be copied for debugging purposes.
	 * 
	 * @param pStream
	 *            The output stream. A <code>null</code> value means that
	 *            CIM-XML debugging is disabled.
	 */
	public void setXmlTraceStream(OutputStream pStream) {
		this.iXmlTraceStream = pStream;
	}

	/*
	 * Initializes the CIM-XML trace file (sblim.wbem.cimxmlTraceStream) during
	 * initialization of LogAndTraceBroker - the trace file is used if CIM-XML
	 * tracing is enabled (sblim.wbem.cimxmlTracing=true) but there is no
	 * CIM-XML trace stream set via setXmlTraceStream()
	 */
	private void initXmlTraceFile() {
		try {
			if (WBEMConfiguration.getGlobalConfiguration().isCimXmlTracingEnabled()) {
				String filename = WBEMConfiguration.getGlobalConfiguration().getCimXmlTraceStream();
				if (filename != null && filename.length() > 0 && this.iXmlTraceFile == null
						&& getXmlTraceStream() == null) {
					if (filename.equalsIgnoreCase("System.out")) {
						this.iXmlTraceFile = System.out;
					} else if (filename.equalsIgnoreCase("System.err")) {
						this.iXmlTraceFile = System.err;
					} else {
						try {
							this.iXmlTraceFile = new FileOutputStream(filename);
						} catch (IOException e) {
							trace(Level.FINE, "Unable to open "
									+ WBEMConfigurationProperties.CIMXML_TRACE_STREAM + "="
									+ filename, e);
						}
					}
					setXmlTraceStream(this.iXmlTraceFile);
				}
			}
		} catch (Exception e) {
			// Don't crash for logging
		}
	}

	/**
	 * Analyzes the stack trace and determines from where the
	 * <code>LogAndTraceBroker</code> was called.
	 * 
	 * @return First <code>StackTraceElement</code> outside the
	 *         <code>LogAndTraceBroker</code>
	 */
	private StackTraceElement getCaller() {
		StackTraceElement[] stack = (new Throwable()).getStackTrace();
		for (int i = 0; i < stack.length; ++i) {
			StackTraceElement frame = stack[i];
			String cname = frame.getClassName();
			if (!this.iTHIS_CLASS.equals(cname) && !this.iTHROWABLE.equals(cname)) { return frame; }
		}
		return null;
	}

	/**
	 * Removes all handlers from a logger
	 * 
	 * @param pLogger
	 *            The logger
	 */
	private void removeHandlers(Logger pLogger) {
		Handler[] handlers = pLogger.getHandlers();
		for (int i = 0; i < handlers.length; ++i) {
			pLogger.removeHandler(handlers[i]);
			handlers[i].close();
		}
	}

	/**
	 * Checks whether there are trace listeners installed that will log a trace
	 * message with the specified level. Use this method to determine if a
	 * trace() method call could result in logging before preparing the
	 * information to be logged. For example:
	 * 
	 * <pre>
	 *     if (logger.isLoggableTrace(Level.WARNING) {
	 *         // Prepare info for logging
	 *         logger.trace(Level.WARNING, ...
	 * </pre>
	 * 
	 * @param pLevel
	 *            The <code>Level</code> of the trace message.
	 * @return <code>true</code> if trace message could be logged,
	 *         <code>false</code> otherwise.
	 */
	public boolean isLoggableTrace(Level pLevel) {
		// If there are no trace listeners or specified level is OFF, message
		// will not be logged
		if (this.iTraceListeners.size() == 0 || pLevel.intValue() == Level.OFF.intValue()) return false;

		// If there are external trace listeners, message could be logged (user
		// can alter level at will, so we do not know what it is)
		if (this.iNumExternalTraceListeners > 0) return true;

		// If there are internal trace listeners, determine if message will be
		// logged
		if (this.iNumInternalTraceListeners > 0) {
			int level = Level.OFF.intValue();
			if (this.iInternalListenerTraceFile != null) {
				level = this.iInternalLevelTraceFile;
			}
			if (this.iInternalListenerTraceConsole != null && level > this.iInternalLevelConsole) {
				level = this.iInternalLevelConsole;
			}
			return level <= pLevel.intValue();
		}

		return true;
	}

	/**
	 * Checks whether there are log listeners installed that will log a message
	 * with the specified level. Use this method to determine if a message()
	 * method call could result in logging before preparing the information to
	 * be logged. For example:
	 * 
	 * <pre>
	 *     if (logger.isLoggableMessage(Level.WARNING) {
	 *         // Prepare info for logging
	 *         logger.message(Level.WARNING, ...
	 * </pre>
	 * 
	 * @param pLevel
	 *            The <code>Level</code> of the message.
	 * @return <code>true</code> if message could be logged, <code>false</code>
	 *         otherwise.
	 */
	public boolean isLoggableMessage(Level pLevel) {
		// If message is traceable, message could be logged
		if (isLoggableTrace(pLevel)) return true;

		// If there are no log listeners or specified level is OFF, message
		// will not be logged
		if (this.iLogListeners.size() == 0 || pLevel.intValue() == Level.OFF.intValue()) return false;

		// If there are external log listeners, message could be logged (user
		// can alter level at will, so we do not know what it is)
		if (this.iNumExternalLogListeners > 0) return true;

		// If there are internal log listeners, determine if message will be
		// logged
		if (this.iNumInternalLogListeners > 0) {
			int level = Level.OFF.intValue();
			if (this.iInternalListenerLogFile != null) {
				level = this.iInternalLevelLogFile;
			}
			if (this.iInternalListenerLogConsole != null && level > this.iInternalLevelConsole) {
				level = this.iInternalLevelConsole;
			}
			return level <= pLevel.intValue();
		}

		return true;
	}

	/**
	 * Checks whether there are CIM-XML trace listeners installed that will log
	 * a CIM-XML trace message. Use this method to determine if a trace() method
	 * call could result in logging before preparing the information to be
	 * logged. For example:
	 * 
	 * <pre>
	 *     if (logger.isLoggableCIMXMLTrace(Level.FINEST) {
	 *         // Prepare info for logging
	 *         logger.traceCIMXML(Level.FINEST, ...
	 * </pre>
	 * 
	 * @param pLevel
	 *            The <code>Level</code> of the trace message.
	 * @return <code>true</code> if CIM-XML trace message could be logged,
	 *         <code>false</code> otherwise.
	 */
	public boolean isLoggableCIMXMLTrace(Level pLevel) {
		// If there are no CIM-XML trace listeners or specified level is OFF,
		// message will not be logged
		if (this.iCIMXMLTraceListeners.size() == 0 || pLevel.intValue() == Level.OFF.intValue()) return false;

		return true;
	}
}
