/**
 * ServiceLocationAttributeDescriptor.java
 *
 * (C) Copyright IBM Corp. 2005, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp;

import java.util.Enumeration;

/**
 * The ServiceLocationAttributeDescriptor interface provides introspection on a
 * template attribute definition (see RFC 2609). Classes implementing the
 * ServiceLocationAttributeDescriptor interface return information on a
 * particular service location attribute definition from the service template.
 * This information is primarily for GUI tools. Programmatic attribute
 * verification should be done through the ServiceLocationAttributeVerifier.
 * 
 */
public interface ServiceLocationAttributeDescriptor {

	/**
	 * Return a String containing the attribute's id.
	 * 
	 * @return The id
	 */
	public abstract String getId();

	/**
	 * Returns a String containing the fully package-qualified Java type of the
	 * attribute. SLP types are translated into Java types as follows: <br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <th>SLP</th>
	 * <th>Java</th>
	 * </tr>
	 * <tr>
	 * <td>STRING</td>
	 * <td>"java.lang.String"</td>
	 * </tr>
	 * <tr>
	 * <td>INTEGER</td>
	 * <td>"java.lang.Integer"</td>
	 * </tr>
	 * <tr>
	 * <td>BOOLEAN</td>
	 * <td>"java.lang.Boolean"</td>
	 * </tr>
	 * <tr>
	 * <td>OPAQUE</td>
	 * <td>"[B" (byte[])</td>
	 * </tr>
	 * <tr>
	 * <td>KEYWORD</td>
	 * <td>"" (empty string)</td>
	 * </tr>
	 * </table>
	 * 
	 * @return The Java type
	 */
	public abstract String getValueType();

	/**
	 * Return a String containing the attribute's help text.
	 * 
	 * @return The description
	 */
	public abstract String getDescription();

	/**
	 * Return an Enumeration of allowed values for the attribute type. For
	 * keyword attributes returns null. For no allowed values (i.e.
	 * unrestricted) returns an empty Enumeration.
	 * 
	 * @return The allowed values
	 */
	public abstract Enumeration<?> getAllowedValues();

	/**
	 * Return an Enumeration of default values for the attribute type. For
	 * keyword attributes returns null. For no allowed values (i.e.
	 * unrestricted) returns an empty Enumeration.
	 * 
	 * @return The default values
	 */
	public abstract Enumeration<?> getDefaultValues();

	/**
	 * Returns true if the "X" flag is set, indicating that the attribute should
	 * be included in an any Locator.findServices() request search filter.
	 * 
	 * @return <code>true</code> if "X" is set, <code>false</code> otherwise
	 */
	public abstract boolean getRequiresExplicitMatch();

	/**
	 * Returns true if the "M" flag is set.
	 * 
	 * @return <code>true</code> if "M" is set, <code>false</code> otherwise
	 */
	public abstract boolean getIsMultivalued();

	/**
	 * Returns true if the "O"" flag is set.
	 * 
	 * @return <code>true</code> if "O" is set, <code>false</code> otherwise
	 */
	public abstract boolean getIsOptional();

	/**
	 * Returns true if the "L" flag is set.
	 * 
	 * @return <code>true</code> if "L" is set, <code>false</code> otherwise
	 */
	public abstract boolean getIsLiteral();

	/**
	 * Returns true if the attribute is a keyword attribute.
	 * 
	 * @return <code>true</code> if the attribute is a keyword,
	 *         <code>false</code> otherwise
	 */
	public abstract boolean getIsKeyword();

}
