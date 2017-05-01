/**
 * (C) Copyright IBM Corp. 2006, 2013
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
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 *    2697    2012-10-30  blaschke-oss (I)MethodResponseNode allows ERROR with PARAMVALUE
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMArgument;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT IMETHODRESPONSE (ERROR|IRETURNVALUE?)
 * ATTLIST IMETHODRESPONSE
 *   %CIMName; 
 * </pre>
 */
/**
 * Class IMethodResponseNode is responsible for
 * 
 */
public class IMethodResponseNode extends Node implements ErrorIf, RetValPipeIf, NonVolatileIf {

	private String iName;

	private ErrorNode iErrorNode;

	private IReturnValueNode iRetValNode;

	private ArrayList<CIMArgument<Object>> iCIMArgAL;

	private static final CIMArgument<?>[] EMPTY_ARG_A = new CIMArgument[0];

	private boolean iHasError;

	private boolean iHasRetVal;

	/**
	 * Ctor.
	 */
	public IMethodResponseNode() {
		super(IMETHODRESPONSE);
	}

	public void addChild(Node pChild) {
		if (pChild instanceof IReturnValueNode) {
			this.iRetValNode = (IReturnValueNode) pChild;
		} else if (pChild instanceof ErrorNode) {
			this.iErrorNode = (ErrorNode) pChild;
		}
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iName = getCIMName(pAttribs);
		this.iErrorNode = null;
		this.iRetValNode = null;
		if (this.iCIMArgAL != null) this.iCIMArgAL.clear();
		this.iHasError = false;
		this.iHasRetVal = false;
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (pNodeNameEnum == ERROR) {
			String ownedNodeName;
			if (this.iHasRetVal) ownedNodeName = IRETURNVALUE;
			else if (this.iHasError) ownedNodeName = ERROR;
			else if (this.iCIMArgAL != null && this.iCIMArgAL.size() > 0) ownedNodeName = PARAMVALUE;
			else ownedNodeName = null;
			if (ownedNodeName != null) throw new SAXException(pNodeNameEnum
					+ " child node is invalid for " + getNodeName()
					+ " node, since it already has a " + ownedNodeName + " child node!");
		} else if (pNodeNameEnum == IRETURNVALUE) {
			String ownedNodeName;
			if (this.iHasRetVal) ownedNodeName = IRETURNVALUE;
			else if (this.iHasError) ownedNodeName = ERROR;
			else ownedNodeName = null;
			if (ownedNodeName != null) throw new SAXException(pNodeNameEnum
					+ " child node is invalid for " + getNodeName()
					+ " node, since it already has a " + ownedNodeName + " child node!");
		} else if (pNodeNameEnum == PARAMVALUE) {
			if (this.iHasError) throw new SAXException(pNodeNameEnum
					+ " child node is invalid for " + getNodeName()
					+ " node, since it already has an ERROR child node!");
		} else throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof ErrorNode) {
			this.iHasError = true;
			this.iErrorNode = (ErrorNode) pChild;
		} else if (pChild instanceof IReturnValueNode) {
			this.iHasRetVal = true;
			this.iRetValNode = (IReturnValueNode) pChild;
		} else
		// Values of parameters should be stored in array
		if (pChild instanceof ParamValueNode) {
			if (this.iCIMArgAL == null) this.iCIMArgAL = new ArrayList<CIMArgument<Object>>();
			this.iCIMArgAL.add(((ParamValueNode) pChild).getCIMArgument());
		}
	}

	@Override
	public void testCompletness() {
	// no mandatory child nodes
	}

	public CIMError getCIMError() {
		return this.iErrorNode == null ? null : this.iErrorNode.getCIMError();
	}

	public int getReturnValueCount() {
		return this.iRetValNode == null ? 0 : this.iRetValNode.getReturnValueCount();
	}

	public Object readReturnValue() {
		return this.iRetValNode.readReturnValue();
	}

	/**
	 * getName
	 * 
	 * @return String
	 */
	public String getName() {
		return this.iName;
	}

	/**
	 * getCIMArguments : returns the array of parsed parameters and their values
	 * : String name, CIMDataType type, Object value
	 * 
	 * @return CIMArgument<?>[]
	 */
	public CIMArgument<?>[] getCIMArguments() {
		if (this.iCIMArgAL == null || this.iCIMArgAL.size() == 0) return null;
		return this.iCIMArgAL.toArray(EMPTY_ARG_A);
	}

}
