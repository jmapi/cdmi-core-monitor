/**
 * (C) Copyright IBM Corp. 2009, 2013
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Ramandeep S Arora, IBM, arorar@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * ---------------------------------------------------------------------------
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 2860081    2009-09-17  raman_arora  Pull Enumeration Feature (DOM Parser)
 * 2878054    2009-10-25  raman_arora  Pull Enumeration Feature (PULL Parser)
 *    2666    2013-09-19  blaschke-oss CR12: Remove ENUMERATIONCONTEXT
 */

package org.sblim.cimclient.internal.wbem;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.cim.CIMArgument;
import javax.cim.CIMObjectPath;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.EnumerateResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Class EnumerateResponseSAX is responsible for all helper functions of SAX
 * parser related with EnumerateResponse.
 * 
 * @param <T>
 */
public class EnumerateResponseSAX<T> {

	private EnumerateResponse<T> enumResponse;

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            Input stream to be parsed
	 * @param pPath
	 *            CIMObject path
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws WBEMException
	 */
	@SuppressWarnings("unchecked")
	public EnumerateResponseSAX(InputStreamReader pStream, CIMObjectPath pPath) throws IOException,
			SAXException, ParserConfigurationException, WBEMException {

		String enumContext = null;

		Boolean endOfSequence = null;

		CloseableIterator<?> iter = new CloseableIteratorSAX(pStream, pPath);

		// check for error and CIMExceptions
		try {
			iter.hasNext();
		} catch (RuntimeException e) {
			iter.close();
			if (e.getCause() != null && e.getCause() instanceof WBEMException) { throw (WBEMException) e
					.getCause(); }
			throw e;
		}

		// get an array of output CIMArguments i.e. enumContext & endOfSequence
		CIMArgument<?>[] pOutArgA = ((CloseableIteratorSAX) iter).getCIMArguments();

		// pOutArgA can never be null
		if (pOutArgA == null) { throw new IllegalArgumentException(
				"Output auguments not found during CIM-XML SAX parser"); }

		// we can use this too but we need not sort array just for 2 elements
		// enumContext = ((CIMArgument<String>) CIMElementSorter.find(pOutArgA,
		// "EnumerationContext")).getValue();
		// endOfSequence = ((CIMArgument<Boolean>)
		// CIMElementSorter.find(pOutArgA, "EndOfSequence")).getValue();

		for (int i = 0; i < pOutArgA.length; i++) {
			if (pOutArgA[i].getName().equals("EnumerationContext")) enumContext = (String) pOutArgA[i]
					.getValue();
			else if (pOutArgA[i].getName().equals("EndOfSequence")) endOfSequence = (Boolean) pOutArgA[i]
					.getValue();
			else throw new IllegalArgumentException(
					"Invalid argument : only EnumerationContext and EndOfSequence are allowed");
		}
		// EndOfSequence can never be null
		if (endOfSequence == null) { throw new IllegalArgumentException(
				"Invalid argument : EndOfSequence can never be null"); }

		// EnumerationContext can't be null if there is more data available
		if ((endOfSequence.booleanValue() == false) && (enumContext == null)) { throw new IllegalArgumentException(
				"Invalid argument : EnumerationContext cannot be null if there is more data available"); }

		this.enumResponse = new EnumerateResponse<T>(enumContext, (CloseableIterator<T>) iter,
				endOfSequence.booleanValue());
	}

	/**
	 * Returns enumResponse
	 * 
	 * @return The value of enumResponse.
	 */
	public EnumerateResponse<T> getEnumResponse() {
		return this.enumResponse;
	}

}
