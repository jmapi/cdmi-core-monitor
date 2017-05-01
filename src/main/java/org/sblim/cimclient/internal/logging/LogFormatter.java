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
 * 1565892    2006-11-15  lupusalex    Make SBLIM client JSR48 compliant
 * 1745282    2007-06-29  ebak         Uniform time stamps for log files
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.logging;

import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class LogFormatter implements the formatting algorithm for our console log.
 * 
 */
public class LogFormatter extends Formatter {

	private final String iLineSeparator = System.getProperty("line.separator");

	/**
	 * Ctor.
	 */
	public LogFormatter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord pRecord) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(TimeStamp.format(pRecord.getMillis()));
		buffer.append(" >");
		buffer.append(String.valueOf(pRecord.getThreadID()));
		buffer.append("< ");
		// buffer.append(iLineSeparator);
		buffer.append(pRecord.getLevel().getName());
		buffer.append(": ");
		buffer.append(MessageFormat.format(pRecord.getMessage(), pRecord.getParameters()));
		buffer.append(this.iLineSeparator);
		return buffer.toString();
	}
}
