/**
 * ServiceType.java
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
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp;

import java.net.InetAddress;
import java.util.Locale;
import java.util.Vector;

/**
 * The Locator is the UA interface, allowing clients to query the SLP framework
 * about existing service types, services instances, and about the attributes of
 * an existing service instance or service type. Queries for services and
 * attributes are made in the locale with which the Locator was created, queries
 * for service types are independent of locale.
 */
public interface Locator {
	

	/**
	 * Return the language locale with which this object was created.
	 * 
	 * @return The locale
	 */
	public abstract Locale getLocale();

	/**
	 * Returns an enumeration of ServiceType objects giving known service types
	 * for the given scopes and given naming authority. If no service types are
	 * found, an empty enumeration is returned.
	 * 
	 * @param pNamingAuthority
	 *            The naming authority. Use "" for the default naming authority
	 *            and "*" for all naming authorities.
	 * @param pScopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @return The enumeration
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationEnumeration findServiceTypes(String pNamingAuthority,
			Vector<String> pScopes) throws ServiceLocationException;

	/**
	 * Returns an enumeration of ServiceType objects giving known service types
	 * for the given scopes and given naming authority. If no service types are
	 * found, an empty enumeration is returned. <br />
	 * <br />
	 * <em>This method is not part of the RFC 2614 interface definition.</em>
	 * 
	 * @param pNamingAuthority
	 *            The naming authority. Use "" for the default naming authority
	 *            and "*" for all naming authorities.
	 * @param pScopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @param pDirectoryAgent
	 *            A vector of InetAddress that specify the directory agents to
	 *            look for.
	 * 
	 * @return The enumeration
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationEnumeration findServiceTypes(String pNamingAuthority,
			Vector<String> pScopes, Vector<InetAddress> pDirectoryAgent)
			throws ServiceLocationException;

	/**
	 * Returns a vector of ServiceURL objects for services matching the query,
	 * and having a matching type in the given scopes. If no services are found,
	 * an empty enumeration is returned.
	 * 
	 * @param pType
	 *            The SLP service type of the service.
	 * @param pScopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @param pSearchFilter
	 *            An LDAPv3 [4] string encoded query. If the filter is empty,
	 *            i.e. "", all services of the requested type in the specified
	 *            scopes are returned. SLP reserved characters must be escaped
	 *            in the query. Use ServiceLocationAttribute.escapeId() and
	 *            ServiceLocationAttribute.escapeValue() to construct the query.
	 * 
	 * @return The enumeration
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationEnumeration findServices(ServiceType pType,
			Vector<String> pScopes, String pSearchFilter) throws ServiceLocationException;

	/**
	 * Returns a vector of ServiceURL objects for services matching the query,
	 * and having a matching type in the given scopes. If no services are found,
	 * an empty enumeration is returned. <br />
	 * <br />
	 * <em>This method is not part of the RFC 2614 interface definition.</em>
	 * 
	 * @param pType
	 *            The SLP service type of the service.
	 * @param pScopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @param pSearchFilter
	 *            An LDAPv3 [4] string encoded query. If the filter is empty,
	 *            i.e. "", all services of the requested type in the specified
	 *            scopes are returned. SLP reserved characters must be escaped
	 *            in the query. Use ServiceLocationAttribute.escapeId() and
	 *            ServiceLocationAttribute.escapeValue() to construct the query.
	 * @param pDirectoryAgents
	 *            A vector of InetAddress that specify the directory agents to
	 *            look for.
	 * 
	 * @return The enumeration
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationEnumeration findServices(ServiceType pType,
			Vector<String> pScopes, String pSearchFilter, Vector<InetAddress> pDirectoryAgents)
			throws ServiceLocationException;

	/**
	 * For the URL and scope, return a Vector of ServiceLocationAttribute
	 * objects whose ids match the String patterns in the attributeIds Vector.
	 * The request is made in the language locale of the Locator. If no
	 * attributes match, an empty enumeration is returned.
	 * 
	 * @param URL
	 *            The URL for which the attributes are desired.
	 * @param scopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @param attributeIds
	 *            A Vector of String patterns identifying the desired
	 *            attributes. An empty vector means return all attributes. As
	 *            described in [7], the patterns may include wildcard to match
	 *            substrings. The strings may include SLP reserved characters,
	 *            they will be escaped by the API before transmission.
	 * @return The enumeration
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationEnumeration findAttributes(ServiceURL URL,
			Vector<String> scopes, Vector<String> attributeIds) throws ServiceLocationException;

	/**
	 * For the URL and scope, return a Vector of ServiceLocationAttribute
	 * objects whose ids match the String patterns in the attributeIds Vector.
	 * The request is made in the language locale of the Locator. If no
	 * attributes match, an empty enumeration is returned. <br />
	 * <br />
	 * <em>This method is not part of the RFC 2614 interface definition.</em>
	 * 
	 * @param pURL
	 *            The URL for which the attributes are desired.
	 * @param pScopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @param pAttributeIds
	 *            A Vector of String patterns identifying the desired
	 *            attributes. An empty vector means return all attributes. As
	 *            described in [7], the patterns may include wildcard to match
	 *            substrings. The strings may include SLP reserved characters,
	 *            they will be escaped by the API before transmission.
	 * @param pDirectoryAgents
	 *            A vector of InetAddress that specify the directory agents to
	 *            look for.
	 * @return The enumeration
	 * @throws ServiceLocationException
	 * 
	 */
	public abstract ServiceLocationEnumeration findAttributes(ServiceURL pURL,
			Vector<String> pScopes, Vector<String> pAttributeIds,
			Vector<InetAddress> pDirectoryAgents) throws ServiceLocationException;

	/**
	 * For the type and scope, return a Vector of all ServiceLocationAttribute
	 * objects whose ids match the String patterns in the attributeIds Vector
	 * regardless of the Locator's locale. The request is made independent of
	 * language locale. If no attributes are found, an empty vector is returned.
	 * 
	 * @param pType
	 *            The service type.
	 * @param pScopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @param pAttributeIds
	 *            A Vector of String patterns identifying the desired
	 *            attributes. An empty vector means return all attributes. As
	 *            described in [7], the patterns may include wildcard to match
	 *            all prefixes or suffixes. The patterns may include SLP
	 *            reserved characters, they will be escaped by the API before
	 *            transmission.
	 * @return The enumeration
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationEnumeration findAttributes(ServiceType pType,
			Vector<String> pScopes, Vector<String> pAttributeIds) throws ServiceLocationException;

	/**
	 * For the type and scope, return a Vector of all ServiceLocationAttribute
	 * objects whose ids match the String patterns in the attributeIds Vector
	 * regardless of the Locator's locale. The request is made independent of
	 * language locale. If no attributes are found, an empty vector is returned. <br />
	 * <br />
	 * <em>This method is not part of the RFC 2614 interface definition.</em>
	 * 
	 * @param pType
	 *            The service type.
	 * @param pScopes
	 *            A Vector of scope names. The vector should be selected from
	 *            the results of a findScopes() API invocation. Use "DEFAULT"
	 *            for the default scope.
	 * @param pAttributeIds
	 *            A Vector of String patterns identifying the desired
	 *            attributes. An empty vector means return all attributes. As
	 *            described in [7], the patterns may include wildcard to match
	 *            all prefixes or suffixes. The patterns may include SLP
	 *            reserved characters, they will be escaped by the API before
	 *            transmission.
	 * @param pDirectoryAgents
	 *            A vector of InetAddress that specify the directory agents to
	 *            look for.
	 * @return The enumeration
	 * @throws ServiceLocationException
	 */
	public abstract ServiceLocationEnumeration findAttributes(ServiceType pType,
			Vector<String> pScopes, Vector<String> pAttributeIds,
			Vector<InetAddress> pDirectoryAgents) throws ServiceLocationException;

}
