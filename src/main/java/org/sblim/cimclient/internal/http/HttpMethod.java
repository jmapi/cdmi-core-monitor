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
 * 12917      2004-11-11  thschaef     HTTP Header parsing does not work for UNIX line delimitor
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 3557283    2012-11-05  blaschke-oss Print full response when get EOF from CIMOM
 *    2709    2013-11-13  blaschke-oss Lower the level of the EOF message to FINE
 */

package org.sblim.cimclient.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * Class HttpMethod provides a method to read a line from a given input stream
 * 
 */
public abstract class HttpMethod {

	/**
	 * Reads a line from a given input stream
	 * 
	 * @param pStream
	 *            The input stream
	 * @return The line
	 * @throws IOException
	 */
	public static String readLine(InputStream pStream) throws IOException {

		if (pStream == null) return null;

		char buffer[] = new char[16];
		int used = 0;
		int prevChar;
		boolean flag = true;

		for (; (prevChar = pStream.read()) >= 0; buffer[used++] = (char) prevChar) {

			flag = false;

			// @12917 - thschaef
			// see http://www.w3.org/Protocols/HTTP/AsImplemented.html for
			// details
			// Lines shall be delimited by an optional carriage return followed
			// by a mandatory line feed character.
			// The client should not assume that the carriage return will be
			// present. Lines may be of any length.
			// Well-behaved servers should restrict line length to 80 characters
			// excluding the CR LF pair.

			// if (prevChar == 13) continue;
			if (prevChar == 10) break;

			if (used >= buffer.length) {
				char tmp[] = new char[buffer.length << 1];
				System.arraycopy(buffer, 0, tmp, 0, used);
				buffer = tmp;
			}
		}
		if (flag) {
			LogAndTraceBroker
					.getBroker()
					.trace(
							Level.FINE,
							"Unexpected EOF trying to read line from input stream - CIMOM closed its end of socket, check it for connection issues");
			throw new IOException("Unexpected EOF");
		}

		for (; used > 0 && buffer[used - 1] <= ' '; used--) {
			// back up over blanks and non-printables at end of line
		}

		return String.copyValueOf(buffer, 0, used);
	}
}
