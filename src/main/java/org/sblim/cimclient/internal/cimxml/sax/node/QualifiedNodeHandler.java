/**
 * (C) Copyright IBM Corp. 2006, 2012
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
 * 1783288    2007-09-10  ebak         CIMClass.isAssociation() not working for retrieved classes.
 * 1820763    2007-10-29  ebak         Supporting the EmbeddedInstance qualifier
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2013628    2008-07-30  rgummada     SAXException when listing classes
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 2957387    2010-03-03  blaschke-oss EmbededObject XML attribute must not be all uppercases
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import java.util.ArrayList;

import javax.cim.CIMDataType;
import javax.cim.CIMQualifier;

import org.sblim.cimclient.GenericExts;
import org.sblim.cimclient.internal.cim.CIMQualifiedElementInterfaceImpl;

/**
 * Class QualifiedNodeHandler helps parsing XML elements with QUALIFIER child
 * elements.
 */
public class QualifiedNodeHandler {

	private static final Boolean TRUE = Boolean.TRUE;

	private ArrayList<CIMQualifier<?>> iQualiAL;

	private CIMQualifiedElementInterfaceImpl iQualiImpl;

	private boolean iHasEmbObjQuali, iHasEmbInstQuali;

	private boolean iHasKeyQuali, iHasAssocQuali;

	/**
	 * init
	 * 
	 * @param pQNH
	 *            - if it's null it returns with a new instance otherwise it
	 *            inits and returns pQNH
	 * @return a QualifiedNodeHandler instance
	 */
	public static QualifiedNodeHandler init(QualifiedNodeHandler pQNH) {
		if (pQNH == null) pQNH = new QualifiedNodeHandler();
		pQNH.init();
		return pQNH;
	}

	/**
	 * init - for reusing an existing QualifiedNodeHandler instance
	 */
	public void init() {
		this.iQualiAL = GenericExts.initClearArrayList(this.iQualiAL);
		this.iHasEmbObjQuali = this.iHasEmbInstQuali = this.iHasKeyQuali = this.iHasAssocQuali = false;
		this.iQualiImpl = null;
	}

	/**
	 * addQualifierNode
	 * 
	 * @param pNode
	 * @return false if pNode is not instance of QualifierNode
	 */
	public boolean addQualifierNode(Node pNode) {
		if (!(pNode instanceof QualifierNode)) return false;
		if (this.iQualiAL == null) this.iQualiAL = new ArrayList<CIMQualifier<?>>();
		CIMQualifier<Object> quali = ((QualifierNode) pNode).getQualifier();
		if (quali != null) {
			// check for null,
			// because it can be null if child node is not populated
			if (CIMDataType.BOOLEAN_T.equals(quali.getDataType()) && TRUE.equals(quali.getValue())) {
				if ("EmbeddedObject".equalsIgnoreCase(quali.getName())) {
					this.iHasEmbObjQuali = true;
				} else if ("KEY".equalsIgnoreCase(quali.getName())) {
					this.iHasKeyQuali = true;
				} else if ("ASSOCIATION".equalsIgnoreCase(quali.getName())) {
					this.iHasAssocQuali = true;
				}
			} else if (CIMDataType.STRING_T.equals(quali.getDataType())
					&& "EMBEDDEDINSTANCE".equalsIgnoreCase(quali.getName())) {
				this.iHasEmbInstQuali = true;
			}
			this.iQualiAL.add(quali);
		}
		return true;
	}

	/**
	 * getQualis
	 * 
	 * @return CIMQualifier[]
	 */
	public CIMQualifier<?>[] getQualis() {
		return getQualis(false);
	}

	/**
	 * getQualis
	 * 
	 * @param pIncludeEmbObj
	 * @return CIMQualifier[]
	 */
	public CIMQualifier<?>[] getQualis(boolean pIncludeEmbObj) {
		makeQualiImpl(pIncludeEmbObj);
		return this.iQualiImpl.getQualifiers();
	}

	/**
	 * isKeyed
	 * 
	 * @return boolean
	 */
	public boolean isKeyed() {
		return this.iHasKeyQuali;
	}

	/**
	 * isAssociation
	 * 
	 * @return boolean
	 */
	public boolean isAssociation() {
		return this.iHasAssocQuali;
	}

	/**
	 * isEmbeddedObject
	 * 
	 * @return boolean
	 */
	public boolean isEmbeddedObject() {
		return this.iHasEmbObjQuali;
	}

	/**
	 * isEmbeddedInstance
	 * 
	 * @return boolean
	 */
	public boolean isEmbeddedInstance() {
		return this.iHasEmbInstQuali;
	}

	private static final CIMQualifier<?>[] EMPTY_QA = new CIMQualifier[0];

	private void makeQualiImpl(boolean pIncludeEmbObj) {
		if (this.iQualiImpl != null) return;
		if (this.iQualiAL == null) {
			this.iQualiImpl = new CIMQualifiedElementInterfaceImpl(null);
			return;
		}
		this.iQualiImpl = new CIMQualifiedElementInterfaceImpl(this.iQualiAL.toArray(EMPTY_QA),
				this.iHasKeyQuali, pIncludeEmbObj);
		this.iQualiAL = null;
	}

}
