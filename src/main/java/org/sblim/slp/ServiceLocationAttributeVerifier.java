/**
 * ServiceLocationAttributeVerifier.java
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
import java.util.Locale;
import java.util.Vector;

/**
 * The ServiceLocationAttributeVerifier provides access to service templates.
 * Classes implementing this interface parse SLP template definitions, provide
 * information on attribute definitions for service types, and verify whether a
 * ServiceLocationAttribute object matches a template for a particular service
 * type. Clients obtain ServiceLocationAttributeVerifier objects for specific
 * SLP service types through the TemplateRegistry.
 * 
 * 
 */
public interface ServiceLocationAttributeVerifier {

	/**
	 * Returns the SLP service type for which this is the verifier.
	 * 
	 * @return The service type
	 */
	public abstract ServiceType getServiceType();

	/**
	 * Return the language locale of the template.
	 * 
	 * @return The locale
	 */
	public abstract Locale getLocale();

	/**
	 * Return the template version number identifier.
	 * 
	 * @return The version
	 */
	public abstract String getVersion();

	/**
	 * Return the URL syntax expression for the service: URL.
	 * 
	 * @return The url syntax
	 */
	public abstract String getURLSyntax();

	/**
	 * Return the descriptive help text for the template.
	 * 
	 * @return The description
	 */
	public abstract String getDescription();

	/**
	 * Return the ServiceLocationAttributeDescriptor for the attribute having
	 * the named id. If no such attribute exists in this template, return null.
	 * This method is primarily for GUI tools to display attribute information.
	 * Programmatic verification of attributes should use the verifyAttribute()
	 * method.
	 * 
	 * @param pAttributeId
	 *            The attribute id
	 * @return The descriptor
	 */
	public abstract ServiceLocationAttributeDescriptor getAttributeDescriptor(String pAttributeId);

	/**
	 * Returns an Enumeration allowing introspection on the attribute definition
	 * in the service template. The Enumeration returns
	 * ServiceLocationAttributeDescriptor objects for the attributes. This
	 * method is primarily for GUI tools to display attribute information.
	 * Programmatic verification of attributes should use the verifyAttribute()
	 * method.
	 * 
	 * @return Enumeration of attribute descriptors
	 */
	public abstract Enumeration<?> getAttributeDescriptors();

	/**
	 * Verify that the attribute matches the template definition. If the
	 * attribute doesn't match, ServiceLocationException is thrown with the
	 * error code as ServiceLocationException.PARSE_ERROR.
	 * 
	 * @param pAttribute
	 *            The ServiceLocationAttribute object to be verified.
	 * @throws ServiceLocationException
	 *             if validation failed
	 * 
	 */
	public abstract void verifyAttribute(ServiceLocationAttribute pAttribute)
			throws ServiceLocationException;

	/**
	 * Verify that the Vector of ServiceLocationAttribute objects matches the
	 * template for this service type. The vector must contain all the required
	 * attributes, and all attributes must match their template definitions. If
	 * the attributes don't match, ServiceLocationException is thrown with the
	 * error code as ServiceLocationException.PARSE_ERROR
	 * 
	 * @param pAttributeVector
	 *            A Vector of ServiceLocationAttribute objects for the
	 *            registration.
	 * @throws ServiceLocationException
	 *             if attributes don't match
	 */
	public abstract void verifyRegistration(Vector<?> pAttributeVector)
			throws ServiceLocationException;

}
