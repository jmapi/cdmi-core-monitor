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
 * 2798931	  2009-06-01  raman_arora  Fix spelling of getPROTOCOLS()
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 2959240    2010-02-25  blaschke-oss Sync up javax.listener.* javadoc with JSR48 1.0.0
 * 3490009    2012-02-21  blaschke-oss TCK: Too many WBEMListenerFactory class methods
 * 3529062    2012-05-23  blaschke-oss WBEMListenerFactory should return new instance
 */

package javax.wbem.listener;

import javax.wbem.client.WBEMClientConstants;

import org.sblim.cimclient.WBEMListenerSBLIM;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * This class is a factory for getting a <code>WBEMListener</code>
 * implementation for a specified protocol. An example of how to use the factory
 * is included below.
 * 
 * <pre>
 * ...
 * class MyListener implements IndicationListener {
 * 
 *   public void indicationOccured(String pIndicationURL, CIMInstance pIndication) {
 *     System.out.println(pIndication);
 * 	 }
 * }
 * 
 * String protocol = WBEMClientConstants.PROTOCOL_CIMXML;
 * WBEMListener api = WBEMListenerFactory.getListener(protocol);
 * int port = api.addListener(MyListener, 1234, protocol);
 * </pre>
 */
public class WBEMListenerFactory extends Object {

	private static final String[] PROTOCOLS = { WBEMClientConstants.PROTOCOL_CIMXML };

	/**
	 *
	 */
	public WBEMListenerFactory() { /**/}

	/**
	 * Get a WBEM Listener implementation for the specified protocol.
	 * 
	 * @param pProtocol
	 *            The protocol name.
	 * @return Implementation of <code>WBEMListener</code>.
	 * @throws IllegalArgumentException
	 *             Could not load protocol implementation.
	 */
	public static WBEMListener getListener(String pProtocol) throws IllegalArgumentException {

		if (WBEMClientConstants.PROTOCOL_CIMXML.equalsIgnoreCase(pProtocol)) { return new WBEMListenerSBLIM(); }
		throw new IllegalArgumentException("Protocol: " + pProtocol
				+ " is not supported! Invoke getProtocols() for the list of "
				+ "supported protocols.");
	}

	/**
	 * Get the names of the supported protocols.
	 * 
	 * @return A string array of the protocol names supported.
	 */
	public static String[] getProtocols() {
		return PROTOCOLS;
	}
}
