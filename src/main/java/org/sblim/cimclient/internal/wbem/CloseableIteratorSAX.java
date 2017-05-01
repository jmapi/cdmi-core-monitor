/**
 * (C) Copyright IBM Corp. 2006, 2011
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
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 3311279    2011-06-04  blaschke-oss Repeated Instantiation of SAXParserFactory
 */

package org.sblim.cimclient.internal.wbem;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.cim.CIMArgument;
import javax.cim.CIMObjectPath;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sblim.cimclient.internal.cimxml.sax.XMLDefaultHandlerImpl;
import org.sblim.cimclient.internal.cimxml.sax.node.AbstractMessageNode;
import org.sblim.cimclient.internal.cimxml.sax.node.AbstractSimpleRspNode;
import org.sblim.cimclient.internal.cimxml.sax.node.CIMNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MessageNode;
import org.sblim.cimclient.internal.http.io.TrailerException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * CloseableIterator implementation for SAX parser.
 */
public class CloseableIteratorSAX implements CloseableIterator<Object> {

	private AbstractSimpleRspNode iAbsSimpleRspNode;

	private CIMError iErr;

	private WBEMException iWBEMException;

	private CIMArgument<?>[] iCIMArgAL;

	private static SAXParserFactory iFactory = SAXParserFactory.newInstance();

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 * @param pPath
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws WBEMException
	 */
	public CloseableIteratorSAX(InputStreamReader pStream, CIMObjectPath pPath) throws IOException,
			SAXException, ParserConfigurationException, WBEMException {
		XMLDefaultHandlerImpl handler = new XMLDefaultHandlerImpl(pPath);
		SAXParser saxParser = iFactory.newSAXParser();
		try {
			saxParser.parse(new InputSource(pStream), handler);
		} catch (TrailerException e) {
			this.iWBEMException = e.getWBEMException();
			return;
		} finally {
			pStream.close();
		}
		init(handler.getCIMNode());
	}

	/**
	 * Ctor.
	 * 
	 * @param pCIMNode
	 * @throws WBEMException
	 */
	public CloseableIteratorSAX(CIMNode pCIMNode) throws WBEMException {
		init(pCIMNode);
	}

	private void init(CIMNode pCIMNode) throws WBEMException {
		MessageNode msgNode = pCIMNode.getMessageNode();
		if (msgNode == null) throw new WBEMException(
				"CIMNode parameter must contain a message node!");
		AbstractMessageNode absMsgNode = msgNode.getAbstractMessageNode();
		if (!(absMsgNode instanceof AbstractSimpleRspNode)) throw new WBEMException(
				"Currently only AbstractSimpleRspNodes are handled!");
		this.iAbsSimpleRspNode = (AbstractSimpleRspNode) absMsgNode;
		this.iErr = this.iAbsSimpleRspNode.getCIMError();
		this.iCIMArgAL = this.iAbsSimpleRspNode.getCIMArguments();
	}

	public void close() {
		// release things
		this.iAbsSimpleRspNode = null;
		this.iErr = null;
		this.iWBEMException = null;
		this.iCIMArgAL = null;
	}

	public boolean hasNext() {
		if (this.iWBEMException != null) { throw new RuntimeException(this.iWBEMException); }
		if (this.iErr != null) {
			this.iWBEMException = new WBEMException(this.iErr.getCode(),
					this.iErr.getDescription(), this.iErr.getCIMInstances());
			throw new RuntimeException(this.iWBEMException);
		}
		return this.iAbsSimpleRspNode == null ? false : this.iAbsSimpleRspNode
				.getReturnValueCount() > 0;
	}

	public Object next() {
		return this.iAbsSimpleRspNode.readReturnValue();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public WBEMException getWBEMException() {
		return this.iWBEMException;
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
