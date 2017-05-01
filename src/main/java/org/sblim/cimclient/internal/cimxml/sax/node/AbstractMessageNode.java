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
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

/**
 * Class AbstractMessageNode is the superclass of AbstractResponseNode and
 * AbstractRequestNode classes.
 */
public abstract class AbstractMessageNode extends Node implements NonVolatileIf {

	/**
	 * Ctor.
	 * 
	 * @param pNameEnum
	 */
	public AbstractMessageNode(String pNameEnum) {
		super(pNameEnum);
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
	// not needed
	}

}
