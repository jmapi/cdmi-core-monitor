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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics 
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 *    2672    2013-09-26  blaschke-oss Remove SIMPLEREQACK support
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMArgument;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sblim.cimclient.internal.wbem.CIMError;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT SIMPLERSP (METHODRESPONSE | IMETHODRESPONSE)
 * 
 * ELEMENT METHODRESPONSE (ERROR|(RETURNVALUE?,PARAMVALUE*))
 * ELEMENT IMETHODRESPONSE (ERROR|IRETURNVALUE?) *
 * </pre>
 */
public class SimpleRspNode extends AbstractSimpleRspNode {

	private Node iChildNode;

	/**
	 * Ctor.
	 */
	public SimpleRspNode() {
		super(SIMPLERSP);
	}

	public void addChild(Node pChild) {
		this.iChildNode = pChild;
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iChildNode = null;
		// no attributes
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
		if (this.iChildNode != null) throw new SAXException(getNodeName()
				+ " node can have only one child node!");
		if (pNodeNameEnum != METHODRESPONSE && pNodeNameEnum != IMETHODRESPONSE) throw new SAXException(
				getNodeName() + " node cannot have " + pNodeNameEnum + " child node!");
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iChildNode == null) throw new SAXException(getNodeName()
				+ " node must have a child node!");
	}

	@Override
	public CIMError getCIMError() {
		if (this.iChildNode instanceof ErrorIf) { return ((ErrorIf) this.iChildNode).getCIMError(); }
		return null;
	}

	/**
	 * getCIMArguments : returns the array of parsed parameters and their values
	 * : String name, CIMDataType type, Object value
	 * 
	 * @return CIMArgument<?>[]
	 */
	@Override
	public CIMArgument<?>[] getCIMArguments() {
		if (this.iChildNode instanceof MethodResponseNode) return ((MethodResponseNode) this.iChildNode)
				.getCIMArguments();
		else if (this.iChildNode instanceof IMethodResponseNode) return ((IMethodResponseNode) this.iChildNode)
				.getCIMArguments();
		return null;
	}

	public int getReturnValueCount() {
		if (this.iChildNode instanceof RetValPipeIf) { return ((RetValPipeIf) this.iChildNode)
				.getReturnValueCount(); }
		return 0;
	}

	public Object readReturnValue() {
		return ((RetValPipeIf) this.iChildNode).readReturnValue();
	}

}
