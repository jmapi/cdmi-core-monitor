/**
 * WBEMClientSBLIM.java
 *
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
 * 1565892    2006-11-08  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2942520    2010-03-08  blaschke-oss IPv6 link local address with scope_id including a dot not supported
 * 3516848    2012-04-11  blaschke-oss enumerateNamespaces() method to WBEMClient
 * 3522904    2012-05-02  blaschke-oss Add new API WBEMClientSBLIM.isActive()
 *    2616    2013-02-23  blaschke-oss Add new API WBEMClientSBLIM.sendIndication()
 */

package org.sblim.cimclient;

import java.net.URI;
import java.util.Locale;
import java.util.Properties;

import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.net.SocketFactory;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.WBEMClient;

/**
 * Class WBEMClientSBLIM contains the SBLIM CIM Client specific extensions to
 * the WBEMClient interface.
 * 
 * @see WBEMClient
 * 
 */
public interface WBEMClientSBLIM extends WBEMClient {

	/**
	 * Initialize the client connection. This must be called before any
	 * operations. This must only be called once.
	 * 
	 * @param pUri
	 *            The protocol and host to use. Any other fields will be
	 *            ignored.
	 * @param pSubject
	 *            The principal/credential pairs for this connection.
	 * @param pLocales
	 *            An array of locales in order of priority of preference.
	 * @throws IllegalArgumentException
	 *             If the host or scheme portion of the object path is null, or
	 *             if the protocol is not supported.
	 * @throws WBEMException
	 *             If the protocol adapter or security cannot be initialized.
	 */
	public void initialize(URI pUri, Subject pSubject, Locale[] pLocales)
			throws IllegalArgumentException, WBEMException;

	/**
	 * Returns the client specific configuration properties. Note that only
	 * these properties are returned that override the global settings. The
	 * global settings can be accessed via the <code>java.lang.System</code>
	 * class.<br />
	 * If the no client specific configuration is set, this method returns
	 * <code>null</code>
	 * 
	 * @return The configuration properties
	 * @see System#getProperties()
	 */
	public Properties getProperties();

	/**
	 * Sets the client specific configuration properties. Any previously set
	 * client specific properties are overwritten. The given properties are
	 * handled as an overlay on the global settings. That means that properties
	 * specified here override the corresponding global properties whereas
	 * properties not specified here are taken from the global properties. The
	 * global settings can be accessed via the <code>java.lang.System</code>
	 * class.
	 * 
	 * @param pProperties
	 *            The session specific properties. <code>null</code> resets this
	 *            client to the global settings.
	 * 
	 * @see System#setProperties(Properties)
	 */
	public void setProperties(Properties pProperties);

	/**
	 * Returns the effective value of a given configuration property. The method
	 * will return the local value of the current thread if one was set or
	 * otherwise client specific value if one was set or otherwise the global
	 * value if one was set or otherwise the default value. Valid property names
	 * can be found in the <code>WBEMConfigurationProperties</code> interface.
	 * 
	 * @param pKey
	 *            The name of the configuration property
	 * 
	 * @return The value of the given configuration property
	 * @see WBEMConfigurationProperties
	 */
	public String getProperty(String pKey);

	/**
	 * Sets a client specific configuration property. This property will
	 * override the corresponding global property for this client instance. The
	 * global settings can be accessed via the <code>java.lang.System</code>
	 * class. Valid property names can be found in the
	 * <code>WBEMConfigurationProperties</code> interface. Unknown properties
	 * are ignored.
	 * 
	 * @param pKey
	 *            The name of the configuration property
	 * @param pValue
	 *            The value of the configuration property. <code>null</code>
	 *            resets to the global setting.
	 * @see WBEMConfigurationProperties
	 * @see System#setProperty(String, String)
	 */
	public void setProperty(String pKey, String pValue);

	/**
	 * Returns the configuration properties that are local to the current
	 * thread. Note that only these properties are returned that override the
	 * global and the client settings. The global settings can be accessed via
	 * the <code>java.lang.System</code> class, the client setting via
	 * <code>getProperties()</code>.<br />
	 * If the no client specific configuration is set, this method returns
	 * <code>null</code>
	 * 
	 * @return The configuration properties
	 * @see System#getProperties()
	 * @see #getProperties()
	 */
	public Properties getLocalProperties();

	/**
	 * Sets the configuration properties that are local to the current thread.
	 * Any previously set local properties (of the current thread) are
	 * overwritten. The given properties are handled as an overlay on the global
	 * settings and the client settings. That means that properties specified
	 * here override the corresponding properties whereas properties not
	 * specified here are taken from the client or global properties. The global
	 * settings can be accessed via the <code>java.lang.System</code> class, the
	 * client setting via <code>getProperties()</code>.
	 * 
	 * @param pProperties
	 *            The thread specific properties. <code>null</code> remove the
	 *            local setting of the current thread.
	 * 
	 * @see System#setProperties(Properties)
	 * @see #setProperties(Properties)
	 */
	public void setLocalProperties(Properties pProperties);

	/**
	 * Sets a local configuration property for the current thread. This property
	 * will override the corresponding global and client property for this
	 * client instance. The global settings can be accessed via the
	 * <code>java.lang.System</code> class, the client setting via
	 * <code>getProperties()</code>. Valid property names can be found in the
	 * <code>WBEMConfigurationProperties</code> interface. Unknown properties
	 * are ignored.
	 * 
	 * @param pKey
	 *            The name of the configuration property
	 * @param pValue
	 *            The value of the configuration property. <code>null</code>
	 *            resets to the global setting.
	 * @see WBEMConfigurationProperties
	 * @see System#setProperty(String, String)
	 * @see #setProperty(String, String)
	 */
	public void setLocalProperty(String pKey, String pValue);

	/**
	 * Gets the custom socket factory if one is set. The client uses this
	 * factory for socket creation instead of the default one.
	 * 
	 * @return The custom factory used for socket creation. <code>null</code>
	 *         indicates set the JRE default factory is used.
	 */
	public SocketFactory getCustomSocketFactory();

	/**
	 * Sets a custom socket factory. The client will use this factory for socket
	 * creation instead of the JRE default.
	 * 
	 * @param pFactory
	 *            The factory to use for socket creation. <code>null</code>
	 *            resets to the JRE default factory.
	 * @throws UnsupportedOperationException
	 *             Some protocols might not communicate via TCP sockets
	 */
	public void setCustomSocketFactory(SocketFactory pFactory) throws UnsupportedOperationException;

	/**
	 * Enumerate the names of the instances of CIM namespaces.
	 * 
	 * @param pNamespace
	 *            The Interop Namespace, if known. If null, the default Interop
	 *            Namespace names defined by DSP1033 are used.
	 * @return A <code>CloseableIterator</code> of <code>CIMObjectPath</code>s.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMObjectPath> enumerateNamespaces(String pNamespace)
			throws WBEMException;

	/**
	 * Returns an indication of whether the client is active (initialized and
	 * not closed) or inactive (not initialized or closed).
	 * 
	 * @return <code>true</code> if client is active, <code>false</code>
	 *         otherwise.
	 */
	public boolean isActive();

	/**
	 * Sends the indication to the specified recipient.
	 * 
	 * @param pRecipient
	 *            URI of indication recipient.
	 * @param pIndication
	 *            Indication.
	 * @return <code>true</code> if indication received successfully,
	 *         <code>false</code> otherwise.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public boolean sendIndication(URI pRecipient, CIMInstance pIndication) throws WBEMException;
}
