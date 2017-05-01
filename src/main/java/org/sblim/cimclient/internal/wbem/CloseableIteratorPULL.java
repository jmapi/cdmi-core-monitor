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
 * @author : Endre Bak, ebak@de.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-06  ebak         Make SBLIM client JSR48 compliant
 * 1688273    2007-04-16  ebak         Full support of HTTP trailers
 * 1708584    2007-04-27  ebak         CloseableIterator might not clean up streams
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2878054    2009-10-25  raman_arora  Pull Enumeration Feature (PULL Parser)
 */

package org.sblim.cimclient.internal.wbem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import javax.cim.CIMArgument;
import javax.cim.CIMObjectPath;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;

import org.sblim.cimclient.internal.cimxml.sax.XMLDefaultHandlerImpl;
import org.sblim.cimclient.internal.cimxml.sax.node.AbstractMessageNode;
import org.sblim.cimclient.internal.cimxml.sax.node.AbstractSimpleRspNode;
import org.sblim.cimclient.internal.cimxml.sax.node.CIMNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MessageNode;
import org.sblim.cimclient.internal.http.io.TrailerException;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.cimclient.internal.pullparser.XMLPullParser;
import org.xml.sax.SAXException;

/**
 * CloseableIterator implementation for PULL parser.
 */
public class CloseableIteratorPULL implements CloseableIterator<Object> {

	private XMLPullParser iParser;

	private XMLDefaultHandlerImpl iHandler;

	private CIMNode iCIMNode;

	private MessageNode iMsgNode;

	private AbstractSimpleRspNode iAbsSimpRspNode;

	private Object iNextValue;

	private WBEMException iWBEMException;

	private static final String EMPTY = "";

	private CIMArgument<?>[] iCIMArgAL;

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 * @param pPath
	 * @throws RuntimeException
	 */
	public CloseableIteratorPULL(InputStreamReader pStream, CIMObjectPath pPath)
			throws RuntimeException {
		this(new XMLPullParser(pStream), new XMLDefaultHandlerImpl(pPath));
	}

	/**
	 * Ctor.
	 * 
	 * @param pParser
	 * @param pHandler
	 */
	public CloseableIteratorPULL(XMLPullParser pParser, XMLDefaultHandlerImpl pHandler) {
		this.iParser = pParser;
		this.iHandler = pHandler;
	}

	public void close() {
		try {
			this.iParser.close();
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred at XMLPullParser.close()!", e);
		}
		this.iNextValue = null;
		this.iWBEMException = null;
		this.iCIMArgAL = null;
	}

	public boolean hasNext() {
		try {
			return hasNextImpl();
		} catch (TrailerException e) {
			this.iWBEMException = e.getWBEMException();
			throw new RuntimeException(this.iWBEMException);
		} catch (Exception e) {
			LogAndTraceBroker.getBroker().trace(Level.SEVERE,
					"Exception occurred during XML parsing!", e);
			throw new RuntimeException(e);
		}
	}

	public Object next() {
		Object value = this.iNextValue;
		this.iNextValue = null;
		return value;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public WBEMException getWBEMException() {
		return this.iWBEMException;
	}

	private boolean hasNextImpl() throws Exception {
		if (this.iNextValue != null) return true;
		while (this.iParser.hasNext()) {
			int event = this.iParser.next();
			switch (event) {
				case XMLPullParser.START_ELEMENT:
					this.iHandler.startElement(EMPTY, EMPTY, this.iParser.getElementName(),
							this.iParser.getAttributes());
					break;
				case XMLPullParser.CHARACTERS:
					char[] buf = this.iParser.getText().toCharArray();
					this.iHandler.characters(buf, 0, buf.length);
					break;
				case XMLPullParser.END_ELEMENT:
					this.iHandler.endElement(EMPTY, EMPTY, this.iParser.getElementName());
					AbstractSimpleRspNode absSimpRspNode = getAbstractSimpleRspNode();
					if (absSimpRspNode != null) {
						if (absSimpRspNode.getReturnValueCount() > 0) {
							this.iNextValue = absSimpRspNode.readReturnValue();
							return true;
						}
						if (absSimpRspNode.isCompleted()) {
							// arguments required for pull operations are parsed
							// only after iResponseNode is complete
							this.iCIMArgAL = absSimpRspNode.getCIMArguments();

							CIMError cimErr = absSimpRspNode.getCIMError();
							if (cimErr != null) throw new WBEMException(cimErr.getCode(), cimErr
									.getDescription(), cimErr.getCIMInstances());
							return false;
						}
					}
					break;
				case XMLPullParser.END_DOCUMENT:
					return false;
			}
		}
		return false;
	}

	private AbstractSimpleRspNode getAbstractSimpleRspNode() throws Exception {
		if (this.iAbsSimpRspNode != null) return this.iAbsSimpRspNode;
		if (this.iCIMNode == null) {
			this.iCIMNode = this.iHandler.getCIMNode();
			if (this.iCIMNode == null) return null;
		}
		if (this.iMsgNode == null) {
			this.iMsgNode = this.iCIMNode.getMessageNode();
			if (this.iMsgNode == null) return null;
		}
		// if (iAbsSimpRspNode==null) {
		AbstractMessageNode absMsgNode = this.iMsgNode.getAbstractMessageNode();
		if (absMsgNode == null) return null;
		if (!(absMsgNode instanceof AbstractSimpleRspNode)) throw new SAXException(
				"Simple response child node expected for MESSAGE node!");
		this.iAbsSimpRspNode = (AbstractSimpleRspNode) absMsgNode;
		this.iMsgNode = null;
		this.iCIMNode = null; // needed no more
		return this.iAbsSimpRspNode;
	}

	/**
	 * 
	 * getCIMArguments : returns the array of parsed parameters and their values
	 * : String name, CIMDataType type, Object value
	 * 
	 * @return CIMArgument<?>[]
	 */

	public CIMArgument<?>[] getCIMArguments() {
		return this.iCIMArgAL;
	}
}
