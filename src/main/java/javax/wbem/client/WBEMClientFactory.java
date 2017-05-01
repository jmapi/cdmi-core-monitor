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
 * @author : Alexander Wolf-Reber, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-11-08  lupusalex    Make SBLIM client JSR48 compliant
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 2959264    2010-02-25  blaschke-oss Sync up javax.client.* javadoc with JSR48 1.0.0
 * 3490355    2012-02-21  blaschke-oss TCK: Cannot instantiate WBEMClientFactory
 */

package javax.wbem.client;

import javax.wbem.WBEMException;

import org.sblim.cimclient.internal.wbem.WBEMClientCIMXML;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * This class defines the functionality of a <code>WBEMClient</code> factory,
 * which is used to retrieve a <code>WBEMClient</code> for a specified protocol.
 * An example of how to use the factory is included below.
 * 
 * <pre>
 *   ...
 *   WBEMClient cc = null;
 *   
 *   try {
 *     cc = WBEMClientFactory.getClient(WBEMClientConstants.PROTOCOL_CIMXML);
 *   } catch (Exception e) {
 *     System.out.println(&quot;Received error when trying to retrieve client handle&quot;);
 *     System.exit(-1);
 *   }
 *   
 *   // initialize the client 
 *   cc.initialize(cns, s, null);
 *   ...
 * </pre>
 */

public class WBEMClientFactory extends Object {

	private static final String[] cProtocols = { WBEMClientConstants.PROTOCOL_CIMXML };

	/**
	 *
	 */
	public WBEMClientFactory() { /**/}

	/**
	 * Get a <code>WBEMClient</code> for a protocol.
	 * 
	 * @param pProtocol
	 *            The protocol name (e.g. "CIM-XML").
	 * @return The <code>WBEMClient</code> implementation for the protocol
	 *         specified.
	 * @throws IllegalArgumentException
	 *             <dl style="margin-left: 40px;"> <dt>If the protocol is
	 *             <code>null</code> or empty.</dt> <dt>If the protocol is not
	 *             supported.</dt> <dt></dl>
	 * @throws WBEMException
	 *             If the protocol implementation could not be loaded.
	 */
	public static WBEMClient getClient(String pProtocol) throws WBEMException {

		if (WBEMClientConstants.PROTOCOL_CIMXML.equalsIgnoreCase(pProtocol)) { return new WBEMClientCIMXML(); }

		throw new IllegalArgumentException("\"" + pProtocol + "\" is not a supported protocol");
	}

	/**
	 * Get the names of the supported protocols.
	 * 
	 * @return A string array of the supported protocols.
	 */
	public static String[] getSupportedProtocols() {
		return cProtocols;
	}

}
