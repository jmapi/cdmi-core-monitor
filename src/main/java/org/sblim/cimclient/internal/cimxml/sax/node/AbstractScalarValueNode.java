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
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 1719991    2007-05-16  ebak         FVT: regression ClassCastException in EmbObjHandler
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

/**
 * Class AbstractScalarValueNode is superclass of KeyValueNode,
 * ValueNamedInstanceNode, ValueNode, ValueNullNode, ValueObjectNode,
 * ValueObjectWithLocalPathNode, ValueObjectWithPathNode and ValueReferenceNode
 * classes.
 */
public abstract class AbstractScalarValueNode extends AbstractValueNode {

	/**
	 * Ctor.
	 * 
	 * @param pNameEnum
	 */
	public AbstractScalarValueNode(String pNameEnum) {
		super(pNameEnum);
	}

}
