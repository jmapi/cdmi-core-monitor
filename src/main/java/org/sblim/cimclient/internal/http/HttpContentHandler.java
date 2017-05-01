/**
 * (C) Copyright IBM Corp. 2005, 2011
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
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3185818    2011-02-18  blaschke-oss indicationOccured URL incorrect
 */

package org.sblim.cimclient.internal.http;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Class HttpContentHandler is responsible for handling the content of an http
 * connection.
 * 
 */
public abstract class HttpContentHandler {

	/**
	 * Handles the content of a given connection
	 * 
	 * @param pMessageReader
	 *            The reader of the connection
	 * @param pMessageWriter
	 *            The writer of the connection
	 * @param pInetAddress
	 *            The remote network address
	 * @param pLocalAddress
	 *            The local network address
	 * @throws HttpException
	 * @throws IOException
	 */
	public abstract void handleContent(MessageReader pMessageReader, MessageWriter pMessageWriter,
			InetAddress pInetAddress, String pLocalAddress) throws HttpException, IOException;

	/**
	 * Closes the handler
	 */
	public abstract void close();
}
