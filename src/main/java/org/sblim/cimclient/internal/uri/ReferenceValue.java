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
 * 1565892    2006-11-05  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.uri;

import org.sblim.cimclient.internal.util.MOF;
import org.sblim.cimclient.internal.util.Util;

/**
 * Class UntypedReferenceValue encapsulates an untyped reference value.
 */
public class ReferenceValue extends Value implements QuotedValue {

	private URI iRef;

	/**
	 * Ctor.
	 * 
	 * @param pRef
	 */
	public ReferenceValue(URI pRef) {
		this.iRef = pRef;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.iRef.toString();
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.QuotedValue#toQuotedString()
	 */
	public String toQuotedString() {
		return Util.quote(this.iRef.toString());
	}

	/**
	 * getRef
	 * 
	 * @return <code>URI</code>
	 */
	public URI getRef() {
		return this.iRef;
	}

	/**
	 * @see org.sblim.cimclient.internal.uri.Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		return MOF.REFERENCE;
	}

}
