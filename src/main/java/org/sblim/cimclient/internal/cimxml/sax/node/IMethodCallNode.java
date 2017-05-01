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
 * 1686000    2007-04-20  ebak         modifyInstance() missing from WBEMClient
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 *    2682    2013-10-02  blaschke-oss (I)MethodCallNode allows no LOCAL*PATH
 *    2690    2013-10-11  blaschke-oss Remove RESPONSEDESTINATION support
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import org.xml.sax.SAXException;

/**
 * ELEMENT IMETHODCALL (LOCALNAMESPACEPATH, IPARAMVALUE*) ATTLIST IMETHODCALL
 * %CIMName;
 */
public class IMethodCallNode extends AbstractMethodCallNode {

	/**
	 * Ctor.
	 */
	public IMethodCallNode() {
		super(IMETHODCALL);
	}

	@Override
	protected void testSpecChild(String pNodeNameEnum) throws SAXException {
		if (pNodeNameEnum == LOCALNAMESPACEPATH) {
			if (this.iPath != null) throw new SAXException(getNodeName()
					+ " node can have only one " + pNodeNameEnum + " child node!");
		} else if (pNodeNameEnum != IPARAMVALUE) throw new SAXException(getNodeName()
				+ " node cannot have " + pNodeNameEnum + " child node!");
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iPath == null) throw new SAXException(
				"IMETHODCALL node must have a LOCALNAMESPACEPATH child node!");
	}

}
