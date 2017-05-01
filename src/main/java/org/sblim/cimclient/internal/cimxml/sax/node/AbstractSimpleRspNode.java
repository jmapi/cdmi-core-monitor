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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMArgument;

import org.sblim.cimclient.internal.wbem.CIMError;

/**
 * Class AbstractSimpleRspNode is the superclass of SimpleExpRspNode and
 * SimpleRspNode classes.
 */
public abstract class AbstractSimpleRspNode extends AbstractResponseNode implements ErrorIf,
		RetValPipeIf {

	/**
	 * Ctor.
	 * 
	 * @param pNameEnum
	 */
	public AbstractSimpleRspNode(String pNameEnum) {
		super(pNameEnum);
	}

	public abstract CIMError getCIMError();

	/**
	 * getCIMArguments
	 * 
	 * @return CIMArgument[]
	 */
	public abstract CIMArgument<?>[] getCIMArguments();

}
