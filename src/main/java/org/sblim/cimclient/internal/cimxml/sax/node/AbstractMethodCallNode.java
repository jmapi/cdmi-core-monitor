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
 * 1686000    2007-04-20  ebak         modifyInstance() missing from WBEMClient
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 *    2682    2013-10-02  blaschke-oss (I)MethodCallNode allows no LOCAL*PATH
 *    2690    2013-10-11  blaschke-oss Remove RESPONSEDESTINATION support
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMArgument;
import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cim.CIMElementSorter;
import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT IMETHODCALL (LOCALNAMESPACEPATH, IPARAMVALUE*)
 * ATTLIST IMETHODCALL
 *   %CIMName;
 *   
 * ELEMENT METHODCALL ((LOCALINSTANCEPATH | LOCALCLASSPATH), PARAMVALUE*)
 * ATTLIST METHODCALL
 *   %CIMName;
 * </pre>
 */
public abstract class AbstractMethodCallNode extends Node implements NonVolatileIf, ObjectPathIf {

	private String iName;

	protected CIMObjectPath iPath;

	/**
	 * (IPARAMVALUE | PARAMVALUE)* -> ArrayList of CIMArgument
	 */
	private ArrayList<CIMArgument<?>> iArgAL;

	/**
	 * Sorted array of CIMArguments
	 */
	private CIMArgument<?>[] iArgA;

	/**
	 * Ctor.
	 * 
	 * @param pNameEnum
	 */
	public AbstractMethodCallNode(String pNameEnum) {
		super(pNameEnum);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iName = getCIMName(pAttribs);
		this.iPath = null;
		this.iArgAL = null;
		this.iArgA = null;
	}

	/**
	 * @param pChild
	 */
	public void addChild(Node pChild) {
	// nothing to do
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof AbstractPathNode) {
			this.iPath = ((AbstractPathNode) pChild).getCIMObjectPath();
		} else if (pChild instanceof AbstractParamValueNode) {
			if (this.iArgAL == null) this.iArgAL = new ArrayList<CIMArgument<?>>();
			this.iArgAL.add(((AbstractParamValueNode) pChild).getCIMArgument());
		}
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	protected abstract void testSpecChild(String pNodeNameEnum) throws SAXException;

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		testSpecChild(pNodeNameEnum);
	}

	/**
	 * getName
	 * 
	 * @return String, name of the called method
	 */
	public String getName() {
		return this.iName;
	}

	public CIMObjectPath getCIMObjectPath() {
		return this.iPath;
	}

	private static final CIMArgument<?>[] EMPTY_ARG_A = new CIMArgument[0];

	/**
	 * getCIMArguments
	 * 
	 * @return CIMArgument[]
	 */
	public CIMArgument<?>[] getCIMArguments() {
		if (this.iArgA != null) return this.iArgA;
		this.iArgA = this.iArgAL == null ? null : (CIMArgument[]) this.iArgAL.toArray(EMPTY_ARG_A);
		return (CIMArgument[]) CIMElementSorter.sort(this.iArgA);
	}

	/**
	 * getArgumentCount
	 * 
	 * @return int
	 */
	public int getArgumentCount() {
		getCIMArguments();
		return this.iArgA == null ? 0 : this.iArgA.length;
	}

	/**
	 * getArgument
	 * 
	 * @param pName
	 * @return CIMArgument
	 */
	public CIMArgument<?> getArgument(String pName) {
		getCIMArguments();
		return (CIMArgument<?>) CIMElementSorter.find(this.iArgA, pName);
	}

	/**
	 * getArgument
	 * 
	 * @param pIdx
	 * @return CIMArgument
	 */
	public CIMArgument<?> getArgument(int pIdx) {
		getCIMArguments();
		return this.iArgA == null ? null : this.iArgA[pIdx];
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("Name: " + getName() + "\nPath: " + getCIMObjectPath()
				+ "\nParamValues:\n");
		for (int i = 0; i < getArgumentCount(); i++) {
			buf.append("  " + getArgument(i) + "\n");
		}
		return buf.toString();
	}

}
