/**
 * TemplateRegistry.java
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
 * 1535756    2006-08-08  lupusalex    Make code warning free
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.slp;

import java.util.Locale;

/**
 * Subclasses of the TemplateRegistry abstract class provide access to service
 * location templates [8]. Classes implementing TemplateRegistry perform a
 * variety of functions. They manage the registration and access of service type
 * template documents. They create attribute verifiers from service templates,
 * for verification of attributes and introspection on template documents. Note
 * that clients of the Advertiser are not required to verify attributes before
 * registering (though they may get a TYPE_ERROR if the implementation supports
 * type checking and there is a mismatch with the template).
 * 
 */
public abstract class TemplateRegistry {

	/**
	 * Returns the distinguished TemplateRegistry object for performing
	 * operations on and with service templates. Returns null if the
	 * implementation doesn't support TemplateRegistry functionality.
	 * 
	 * <b>Not yet implemented</b>
	 * 
	 * @return The template registry
	 */
	public static TemplateRegistry getTemplateRegistry() {
		throw new RuntimeException("not implemented");
	}

	/**
	 * Register the service template with the template registry.
	 * 
	 * @param pType
	 *            The service type.
	 * @param pDocumentURL
	 *            A string containing the URL of the template document. May not
	 *            be the empty string.
	 * @param pLocale
	 *            A Locale object containing the language locale of the
	 *            template.
	 * @param pVersion
	 *            The version number identifier of template document.
	 * @throws ServiceLocationException
	 */
	public abstract void registerServiceTemplate(ServiceType pType, String pDocumentURL,
			Locale pLocale, String pVersion) throws ServiceLocationException;

	/**
	 * Deregister the template for the service type.
	 * 
	 * @param pType
	 *            The service type.
	 * @param pLocale
	 *            A Locale object containing the language locale of the
	 *            template.
	 * @param pVersion
	 *            A String containing the version number. Use null to indicate
	 *            the latest version.
	 * @throws ServiceLocationException
	 */
	public abstract void deregisterServiceTemplate(ServiceType pType, Locale pLocale,
			String pVersion) throws ServiceLocationException;

	/**
	 * Returns the URL for the template document.
	 * 
	 * @param pType
	 *            The service type.
	 * @param pLocale
	 *            A Locale object containing the language locale of the
	 *            template.
	 * @param pVersion
	 *            A String containing the version number. Use null to indicate
	 *            the latest version.
	 * @return The URL
	 * @throws ServiceLocationException
	 */
	public abstract String findTemplateURL(ServiceType pType, Locale pLocale, String pVersion)
			throws ServiceLocationException;

	/**
	 * Reads the template document URL and returns an attribute verifier for the
	 * service type. The attribute verifier can be used for verifying that
	 * registration attributes match the template, and for introspection on the
	 * template definition.
	 * 
	 * @param pDocumentURL
	 *            A String containing the template document's URL. May not be
	 *            the empty string.
	 * @return The verifier
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationAttributeVerifier attributeVerifier(String pDocumentURL)
			throws ServiceLocationException;

}
