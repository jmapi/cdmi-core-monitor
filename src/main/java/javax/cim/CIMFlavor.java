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
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 */

package javax.cim;

import java.io.Serializable;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:58 EST 2010
/**
 * This class represents a CIM Flavor as defined by the Distributed Management
 * Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM Infrastructure
 * Specification (<a
 * href=http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf
 * >DSP004</a>). CIM flavors specify overriding and inheritance rules. These
 * rules specify how qualifiers are transmitted from classes to derived classes.
 */
public class CIMFlavor extends Object implements Serializable {

	private static final long serialVersionUID = -4177389103635687939l;

	/**
	 * The qualifier cannot be overridden.
	 */
	public static final int DISABLEOVERRIDE = 1;

	/**
	 * The qualifier applies only to the class in which it is declared.
	 */
	public static final int RESTRICTED = 2;

	/**
	 * The qualifier can be specified in multiple locales (language and country
	 * combination).
	 */
	public static final int TRANSLATE = 4;

	private static final int[] flavors = { DISABLEOVERRIDE, RESTRICTED, TRANSLATE };

	/**
	 * Returns an array of possible flavors.
	 * 
	 * @return All possible flavors.
	 */
	public static int[] getFlavors() {
		return flavors;
	}

}
