/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 * 1565892    2006-12-14  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2959240    2010-02-25  blaschke-oss Sync up javax.listener.* javadoc with JSR48 1.0.0
 * 3496385    2012-03-02  blaschke-oss JSR48 1.0.0: add WBEMListener get/setProperty
 */

package javax.wbem.listener;

import java.io.IOException;

//Sync'd against JSR48 1.0.0 javadoc (build 1.6.0_18) on Thu Mar 01 12:21:26 EST 2012
/**
 * The <code>WBEMListener</code> interface is used to add/remove WBEM Indication
 * Listeners. The implementation of a <code>WBEMListener</code> can be retrieved
 * from the <code>WBEMListenerFactor</code> by specifying the protocol to use to
 * listen for indications.
 */
public interface WBEMListener {

	/**
	 * Add a new listener using the specified port.
	 * 
	 * @param pListener
	 *            The Indication Listener that will be called when an indication
	 *            is received.
	 * @param pPort
	 *            The port to listen on. Use 0 to specify any available port.
	 * @param pTransport
	 *            The transport to use (e.g. HTTP or HTTPS).
	 * @return The port that was used.
	 * @throws IOException
	 *             If the port is already in use.
	 */
	public int addListener(IndicationListener pListener, int pPort, String pTransport)
			throws IOException;

	/**
	 * Add a new listener using the specified port.
	 * 
	 * @param pListener
	 *            The Indication Listener that will be called when an indication
	 *            is received.
	 * @param pPort
	 *            The port to listen on. Use 0 to specify any available port.
	 * @param pTransport
	 *            The transport to use (e.g. HTTP or HTTPS).
	 * @param localAddr
	 *            The local IP address to bind to. This is only needed in
	 *            multi-homed systems.
	 * @return The port that was used.
	 * @throws IOException
	 *             If the port is already in use.
	 */
	public int addListener(IndicationListener pListener, int pPort, String pTransport,
			String localAddr) throws IOException;

	/**
	 * Get a property value.
	 * 
	 * @param pName
	 *            The name of the property.
	 * @return The value of the property.
	 */
	public String getProperty(String pName);

	/**
	 * Remove the listener associated with the specified port.
	 * 
	 * @param pPort
	 *            The port.
	 */
	public void removeListener(int pPort);

	/**
	 * Set a property for the WBEM Listener.
	 * 
	 * @param pName
	 *            The name of the property.
	 * @param pValue
	 *            The value of the property.
	 * @throws IllegalArgumentException
	 *             If the name is not a supported property name.
	 */
	public void setProperty(String pName, String pValue);

}
