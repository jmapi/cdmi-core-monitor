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
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

/**
 * This interface have to be implemented by those Node classes which instances
 * cannot be reused. (e.g. CIM, MESSAGE, SIMPLERSP, IMETHODRESPONSE,
 * IRETURNVALUE)
 */
public interface NonVolatileIf {

	/**
	 * Have to be called by SAX's DefaultHandler.startElement() after
	 * testChild() call.
	 * 
	 * @param pChild
	 */
	public abstract void addChild(Node pChild);

}
