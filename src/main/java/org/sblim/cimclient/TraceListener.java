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
 * 1565892    2006-11-14  lupusalex    Make SBLIM client JSR48 compliant
 * 1711092    2006-05-02  lupusalex    Some fixes/additions of log&trace messages
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient;

import java.util.logging.Level;

/**
 * The interface TraceListener must be implemented if you want to attach your
 * own logging framework to the CIM Client.
 * 
 * @see LogAndTraceManager
 */
public interface TraceListener {

	/**
	 * Receive a trace message.
	 * 
	 * @param pLevel
	 *            One of the message level identifiers, e.g. FINE
	 * @param pOrigin
	 *            The java class/method/line-of-code sending the message. Might
	 *            be <code>null</code> if algorithm failed to determine origin.
	 * @param pMessage
	 *            The message text
	 */
	public void trace(Level pLevel, StackTraceElement pOrigin, String pMessage);

	/**
	 * Receive a trace message.
	 * 
	 * @param pLevel
	 *            One of the message level identifiers, e.g. SEVERE
	 * @param pOrigin
	 *            The java class/method/line-of-code sending the message
	 * @param pMessage
	 *            The message text
	 * @param pThrown
	 *            The throwable associated with the message
	 */
	public void trace(Level pLevel, StackTraceElement pOrigin, String pMessage, Throwable pThrown);

}
