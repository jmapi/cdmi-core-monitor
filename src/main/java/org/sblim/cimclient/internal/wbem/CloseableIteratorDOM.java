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
 * 1565892    2006-12-11  ebak         Make SBLIM client JSR48 compliant
 * 1688273    2007-04-16  ebak         Full support of HTTP trailers
 * 1708584    2007-04-27  ebak         CloseableIterator might not clean up streams
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2860081    2009-09-17  raman_arora  Pull Enumeration Feature (DOM Parser)
 */

package org.sblim.cimclient.internal.wbem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.cim.CIMObjectPath;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.sblim.cimclient.internal.cimxml.CIMMessage;
import org.sblim.cimclient.internal.cimxml.CIMResponse;
import org.sblim.cimclient.internal.cimxml.CIMXMLParseException;
import org.sblim.cimclient.internal.cimxml.CIMXMLParserImpl;
import org.sblim.cimclient.internal.http.io.TrailerException;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Class CloseableIteratorDOM is a CloseableIterator implementation for the
 * CIM-XML DOM parser.
 * 
 */
public class CloseableIteratorDOM implements CloseableIterator<Object> {

	private Iterator<Object> iItr;

	private CIMResponse iRsp;

	private WBEMException iWBEMException;

	private List<Object> outParamValues;

	private void trace(Level pLevel, String pMsg, Exception pE) {
		LogAndTraceBroker.getBroker().trace(pLevel, pMsg, pE);
	}

	private void trace(Level pLevel, String pMsg) {
		LogAndTraceBroker.getBroker().trace(pLevel, pMsg, null);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 * @param pPath
	 * @throws WBEMException
	 * @throws IOException
	 */
	public CloseableIteratorDOM(InputStreamReader pStream, CIMObjectPath pPath)
			throws WBEMException, IOException {
		try {
			parse(new InputSource(pStream), pPath);
		} finally {
			pStream.close();
		}
	}

	/**
	 * Ctor.
	 * 
	 * @param pIs
	 * @param pLocalPath
	 * @throws WBEMException
	 */
	public CloseableIteratorDOM(InputSource pIs, CIMObjectPath pLocalPath) throws WBEMException {
		parse(pIs, pLocalPath);
	}

	public void close() {
		// release things
		this.iItr = null;
		this.iRsp = null;
		this.iWBEMException = null;
		this.outParamValues = null;
	}

	public boolean hasNext() {
		if (this.iWBEMException != null) throw new RuntimeException(this.iWBEMException);
		if (this.iRsp == null) return false;
		try {
			this.iRsp.checkError();
		} catch (WBEMException e) {
			throw new RuntimeException(e);
		}
		return this.iItr == null ? false : this.iItr.hasNext();
	}

	public Object next() {
		return this.iItr.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public WBEMException getWBEMException() {
		return this.iWBEMException;
	}

	/**
	 * getParamValues : returns the list of CIMArgument parsed parameters and
	 * their values : String name, CIMDataType type, Object value
	 * 
	 * @return List of CIMArgument
	 */
	public List<Object> getParamValues() {
		return this.outParamValues;
	}

	private void parse(InputSource pIs, CIMObjectPath pLocalPath) throws WBEMException {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom;
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using builder to get DOM representation of the XML file
			dom = db.parse(pIs);
		} catch (TrailerException e) {
			this.iWBEMException = e.getWBEMException();
			return;
		} catch (Exception e) {
			String msg = "Exception occurred during DOM parsing!";
			trace(Level.SEVERE, msg, e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, msg, null, e);
		}
		CIMXMLParserImpl.setLocalObjectPath(pLocalPath);
		CIMMessage cimMsg;
		try {
			cimMsg = CIMXMLParserImpl.parseCIM(dom.getDocumentElement());
		} catch (CIMXMLParseException e) {
			String msg = "Exception occurred during parseCIM!";
			trace(Level.SEVERE, msg, e);
			throw new WBEMException(WBEMException.CIM_ERR_FAILED, msg, null, e);
		}
		if (!(cimMsg instanceof CIMResponse)) {
			String msg = "CIM message must be response!";
			trace(Level.SEVERE, msg);
			throw new WBEMException(msg);
		}
		this.iRsp = (CIMResponse) cimMsg;
		List<Object> retValVec = this.iRsp.getFirstReturnValue();
		this.iItr = retValVec == null ? null : retValVec.iterator();
		this.outParamValues = this.iRsp.getParamValues();
	}

}
