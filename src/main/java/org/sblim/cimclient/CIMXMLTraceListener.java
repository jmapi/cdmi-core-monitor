/**
 * (C) Copyright IBM Corp. 2012
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 *           Dave Blaschke, IBM, blaschke@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
 */

package org.sblim.cimclient;

import java.util.logging.Level;

/**
 * The interface CIMXMLTraceListener must be implemented if you want to attach
 * your own CIM-XML logging framework to the CIM Client.
 * 
 * @see LogAndTraceManager
 */
public interface CIMXMLTraceListener {

	/**
	 * Receive a CIM-XML trace message.
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
	public void traceCIMXML(Level pLevel, String pMessage, boolean pOutgoing);

}
