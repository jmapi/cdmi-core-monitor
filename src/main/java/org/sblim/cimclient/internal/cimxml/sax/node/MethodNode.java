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
 * 1848607    2007-12-11  ebak         Strict EmbeddedObject types
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 *    2703    2013-11-08  blaschke-oss MethodNode should not require TYPE attribute
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMDataType;
import javax.cim.CIMMethod;
import javax.cim.CIMParameter;

import org.sblim.cimclient.internal.cimxml.sax.EmbObjHandler;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * 
 * ELEMENT METHOD (QUALIFIER*, (PARAMETER | PARAMETER.REFERENCE | PARAMETER.ARRAY | PARAMETER.REFARRAY)*)
 * ATTLIST METHOD
 *   %CIMName;
 *   %CIMType;              #IMPLIED
 *   %ClassOrigin;
 *   %Propagated;&gt;
 * </pre>
 */
public class MethodNode extends Node {

	private String iName;

	private CIMDataType iType;

	private String iClassOrigin;

	private boolean iPropagated;

	private QualifiedNodeHandler iQualiHandler;

	private EmbObjHandler iEmbObjHandler;

	private SAXSession iSession;

	/**
	 * PARAMETER.XXX*, AbstractParameterNode*
	 */
	private ArrayList<CIMParameter<?>> iCIMParamAL;

	/**
	 * Ctor.
	 */
	public MethodNode() {
		super(METHOD);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iSession = pSession;
		this.iQualiHandler = QualifiedNodeHandler.init(this.iQualiHandler);
		this.iEmbObjHandler = EmbObjHandler.init(this.iEmbObjHandler, getNodeName(), pAttribs,
				this.iSession, this.iQualiHandler, true);

		if (this.iCIMParamAL != null) this.iCIMParamAL.clear();

		this.iName = getCIMName(pAttribs);

		this.iType = getCIMType(pAttribs, true);
		if (this.iType != null && this.iType.isArray()) throw new SAXException(
				"METHOD node's TYPE cannot be an array!");
		this.iClassOrigin = getClassOrigin(pAttribs);
		this.iPropagated = getPropagated(pAttribs);
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { QUALIFIER, PARAMETER, PARAMETER_REFERENCE,
			PARAMETER_ARRAY, PARAMETER_REFARRAY };

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		for (int i = 0; i < ALLOWED_CHILDREN.length; i++)
			if (pNodeNameEnum.equalsIgnoreCase(ALLOWED_CHILDREN[i])) return;
		throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iQualiHandler.addQualifierNode(pChild)) return;
		if (this.iCIMParamAL == null) this.iCIMParamAL = new ArrayList<CIMParameter<?>>();
		this.iCIMParamAL.add(((AbstractParameterNode) pChild).getCIMParameter());
	}

	@Override
	public void testCompletness() throws SAXException {
		this.iType = this.iEmbObjHandler.getType();
		if (this.iType != null && this.iType.isArray()) throw new SAXException(
				"METHOD node's TYPE attribute cannot be an array!");
	}

	/**
	 * getCIMMethod
	 * 
	 * @return CIMMethod
	 */
	public CIMMethod<?> getCIMMethod() {
		/*
		 * CIMMethod( String name, CIMDataType type, CIMQualifier[] qualifiers,
		 * CIMParameter[] parameters, boolean propagated, String originClass );
		 * 
		 * EmbeddedObject qualifier mustn't be removed. Return type cannot be
		 * changed.
		 * 
		 * The situation changed by the introduction of the strict embedded
		 * object type parsing.
		 */
		return new CIMMethod<Object>(this.iName, this.iType, this.iQualiHandler
				.getQualis(!this.iSession.strictEmbObjParsing()), this.iCIMParamAL == null ? null
				: (CIMParameter<?>[]) this.iCIMParamAL.toArray(EMPTY_PA), this.iPropagated,
				this.iClassOrigin);
	}

	private static final CIMParameter<?>[] EMPTY_PA = new CIMParameter[0];

}
