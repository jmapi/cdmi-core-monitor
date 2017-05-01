/**
 * (C) Copyright IBM Corp. 2006, 2010
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
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 1737123    2007-06-15  ebak         Differences to JSR48 public review draft
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 */

package javax.cim;

import java.io.Serializable;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * This class represents a CIM Scope as defined by the Distributed Management
 * Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure
 * Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). This class is used in a <code>CIMQualifierType</code> to define
 * what elements the qualifier can be applied to.
 */
public class CIMScope extends Object implements Serializable {

	private static final long serialVersionUID = -4563643521754840535l;

	/**
	 * Scope representing a qualifier that can be applied to any element.
	 */
	public static final int ANY = 127;

	/**
	 * Scope representing a qualifier that can be applied to an association.
	 */
	public static final int ASSOCIATION = 2;

	/**
	 * Scope representing a qualifier that can be applied to a CIM class.
	 */
	public static final int CLASS = 1;

	/**
	 * Scope representing a qualifier that can be applied to an indication.
	 */
	public static final int INDICATION = 4;

	/**
	 * Scope representing a qualifier that can be applied to a method.
	 */
	public static final int METHOD = 32;

	/**
	 * Scope representing a qualifier that can be applied to a parameter.
	 */
	public static final int PARAMETER = 64;

	/**
	 * Scope representing a qualifier that can be applied to a property.
	 */
	public static final int PROPERTY = 8;

	/**
	 * Scope representing a qualifier that can be applied to a reference.
	 */
	public static final int REFERENCE = 16;

	private static final int[] SCOPES = { ANY, ASSOCIATION, CLASS, INDICATION, METHOD, PARAMETER,
			PROPERTY, REFERENCE };

	/**
	 * Returns the complete set of possible scopes.
	 * 
	 * @return An <code>int</code> array of all scopes.
	 */
	public static int[] getScopes() {
		return SCOPES;
	}

}
