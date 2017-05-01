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
 * Change History
 * Flag       Date        Prog         Description
 *-------------------------------------------------------------------------------------------------
 *            2006-04-25  ebak         Initial commit
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 */

package javax.cim;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * The NamedElementInterface is used by CIM Elements that are named. For
 * example, CIM Qualifier Types, CIM Classes and CIM Instances are all named and
 * can be retrieved by a WBEM client.
 */
public interface CIMNamedElementInterface {

	/**
	 * Retrieve the ObjectPath that represents the name for this element.
	 * 
	 * @return The Object Path that represents the element.
	 */
	CIMObjectPath getObjectPath();

}
