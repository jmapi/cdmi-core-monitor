/**
 * (C) Copyright IBM Corp. 2006, 2009
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
 * 1565892    2006-11-13  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient;

import java.util.logging.Level;

/**
 * The interface LogListener must be implemented if you want to attach your own
 * logging framework to the CIM Client.
 * 
 * @see LogAndTraceManager
 */
public interface LogListener {

	/**
	 * Receive a message.
	 * 
	 * @param pLevel
	 *            One of the message level identifiers, e.g. SEVERE
	 * @param pMessageKey
	 *            The identifier of the message
	 * @param pMessage
	 *            The message text
	 * @param pParameters
	 *            The parameters for the message
	 */
	public void log(Level pLevel, String pMessageKey, String pMessage, Object[] pParameters);
}
