/**
 * (C) Copyright IBM Corp. 2007, 2012
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1678915    2007-03-12  lupusalex    Integrated WBEM service discovery via SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3469427    2012-01-04  blaschke-oss Fix broken HTML links
 */

package org.sblim.cimclient.discovery;

import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import javax.security.auth.Subject;
import javax.wbem.client.WBEMClient;

/**
 * Interface WBEMServiceAdvertisement is encapsulates the information collected
 * about a service during discovery. The DMTF specifies a set a attributes that
 * each service must advertise. These attributes are found as string constants
 * in this interface and the method getAttribute() is offered to get an
 * attribute by name. This design was chosen because the set of attributes might
 * be extended by DMTF and vendor implementations. It's also unclear if upcoming
 * new discovery protocols will have the same set of attributes as SLP.
 * 
 * @pattern Immutable
 * @threading This class is thread-safe
 * @since 2.0.2
 */
public interface WBEMServiceAdvertisement {

	/**
	 * template-type (string): The scheme name of the service scheme. The scheme
	 * name consists of the service type name and an optional naming authority
	 * name, separated from the service type name by a period. See RFC 2609
	 * section 3.2.2 for the conventions governing service type names.
	 */
	public static final String TEMPLATE_TYPE = "template-type";

	/**
	 * template-version (string): The version number of the service type
	 * specification.
	 */
	public static final String TEMPLATE_VERSION = "template-version";

	/**
	 * template-description (string): A description of the service suitable for
	 * inclusion in text read by people.
	 */
	public static final String TEMPLATE_DESCRIPTION = "template-description";

	/**
	 * template-url-syntax (string): The template-url-syntax MUST be the WBEM
	 * URI Mapping of the location of one service access point offered by the
	 * WBEM Server over TCP transport. This attribute must provide sufficient
	 * addressing information so that the WBEM Server can be addressed directly
	 * using the URL. The WBEM URI Mapping is defined in the WBEM URI Mapping
	 * Specification 1.0.0 (DSP0207).<br />
	 * <br  />
	 * Example: <code>(template-url-syntax=https://localhost:5989)</cpde>
	 */
	public static final String TEMPLATE_URL_SYNTAX = "template-url-syntax";

	/**
	 * service-hi-name (string, optional): This string is used as a name of the
	 * CIM service for human interfaces. This attribute MUST be the
	 * CIM_ObjectManager.ElementName property value.
	 */
	public static final String SERVICE_HI_NAME = "service-hi-name";

	/**
	 * service-hi-description (string, optional): This string is used as a
	 * description of the CIM service for human interfaces.This attribute MUST
	 * be the CIM_ObjectManager.Description property value.
	 */
	public static final String SERVICE_HI_DESC = "service-hi-description";

	/**
	 * service-id (string, literal): The ID of this WBEM Server. The value MUST
	 * be the CIM_ObjectManager.Name property value.
	 */
	public static final String SERVICE_ID = "service-id";

	/**
	 * CommunicationMechanism (string, literal): The communication mechanism
	 * (protocol) used by the CIM Object Manager for this service-location-tcp
	 * defined in this advertisement. This information MUST be the
	 * CIM_ObjectManagerCommunicationMechanism.CommunicationMechanism property
	 * value. CIM-XML is defined in the CIM Operations over HTTP specification
	 * which can be found at http://www.dmtf.org/ <br />
	 * <br />
	 * Values: <code>"Unknown", "Other", "cim-xml"</code>
	 */
	public static final String COMM_MECHANISM = "CommunicationMechanism";

	/**
	 * OtherCommunicationMechanismDescription (string, literal, optional): The
	 * other communication mechanism defined for the CIM Server in the case the
	 * "Other" value is set in the CommunicationMechanism string. This attribute
	 * MUST be the
	 * CIM_ObjectManagerCommunicationMechanism.OtherCommunicationMechanism
	 * property value. This attribute is optional because it is only required if
	 * the "other" value is set in CommunicationMechansim. The value returned is
	 * a free-form string.
	 */
	public static final String OTHER_COMM_MECHN_DESC = "OtherCommunicationMechanismDescription";

	/**
	 * InteropSchemaNamespace (string, literal, multiple): Namespace within the
	 * target WBEM Server where the CIM Interop Schema can be accessed. Multiple
	 * namespaces may be provided. Each namespace provided MUST contain the same
	 * information.
	 */
	public static final String INTEROP_NS = "InteropSchemaNamespace";

	/**
	 * ProtocolVersion (string, literal, optional): The version of the protocol.
	 * It MUST be the CIM_ObjectManagerCommunicationMechanism.Version property
	 * value.
	 */
	public static final String PROTOCOL_VERSION = "ProtocolVersion";

	/**
	 * FunctionalProfilesSupported (string, literal, multiple):
	 * ProfilesSupported defines the CIM Operation profiles supported by the CIM
	 * Object Manager. This attribute MUST be the
	 * CIM_ObjectManagerCommunicationMechansim.FunctionalProfilesSupported
	 * property value.<br />
	 * <br />
	 * Values: <code>
	 "Unknown", "Other", "Basic Read", "Basic Write",
	 "Schema Manipulation", "Instance Manipulation",
	 "Association Traversal", "Query Execution",
	 "Qualifier Declaration", "Indications"</code>
	 */
	public static final String FUNCTIONAL_PROF_SUPP = "FunctionalProfilesSupported";

	/**
	 * FunctionalProfileDescriptions (string, literal, multiple, optional):
	 * Other profile description if the "other" value is set in the
	 * ProfilesSupported attribute. This attribute is optional because it is
	 * returned only if the "other" value is set in the ProfilesSupported
	 * attribute. If provided it MUST be equal to the
	 * CIM_ObjectManagerCommunicationMechanism.FunctionalProfileDescriptions
	 * property value.
	 */
	public static final String FUNCTIONAL_PROF_DESC = "FunctionalProfileDescriptions";

	/**
	 * MultipleOperationsSupported (boolean): Defines whether the CIM Object
	 * Manager supports batch operations. This attribute MUST be the
	 * CIM_ObjectManagerCommunicationMechanism.MultipleOperationsSupported
	 * property value.
	 */
	public static final String MULT_OPERATIONS_SUPP = "MultipleOperationsSupported";

	/**
	 * AuthenticationMechanismsSupported (string, literal, multiple): Defines
	 * the authentication mechanism supported by the CIM Object Manager. This
	 * attributed MUST be the CIM_ObjectManagerCommunicationMechanism.
	 * AuthenticationMechanismsSupported property value. <br />
	 * <br />
	 * Values: <code>"Unknown", "None", "Other", "Basic", "Digest"</code>
	 */
	public static final String AUTH_MECH_SUPP = "AuthenticationMechanismsSupported";

	/**
	 * AuthenticationMechansimDescriptions (string, literal, multiple,
	 * optional): Defines other Authentication mechanisms supported by the CIM
	 * Object Manager in the case where the "Other" value is set in any of the
	 * AuthenticationMechanismSupported attribute values. If provided, this
	 * attribute MUST be the CIM_ObjectManagerCommunicationMechanism.
	 * AuthenticationMechansimDescriptions property value.
	 */
	public static final String AUTH_MECH_DESC = "AuthenticationMechansimDescriptions";

	/**
	 * Namespace (string, literal, multiple, optional): Namespace(s) supported
	 * on the CIM Object Manager. This attribute MUST be the CIM_Namespace.name
	 * property value for each instance of CIM_Namespace that exists. This
	 * attribute is optional. NOTE: This value is literal (L) because the
	 * namespace names MUST not be translated into other languages.
	 */
	public static final String NAMESPACE = "Namespace";

	/**
	 * Classinfo (string, multiple, optional): This attributes is optional but
	 * if used, the values MUST be the CIM_Namespace.Classinfo property value.
	 * The values represent the classinfo (CIM Schema version, etc.) for the
	 * namespaces defined in the corresponding namespace listed in the Namespace
	 * attribute. Each entry in this attribute MUST correspond to the namespace
	 * defined in the same position of the namespace attribute. There must be
	 * one entry in this attribute for each entry in the namespace attribute.
	 */
	public static final String CLASSINFO = "Classinfo";

	/**
	 * RegisteredProfilesSupported (string, literal, multiple):
	 * RegisteredProfilesSupported defines the Profiles that this WBEM Server
	 * has support for. Each entry in this attribute MUST be in the form of
	 * Organization:Profile Name{:Subprofile Name}<br />
	 * <br />
	 * Examples: <br />
	 * <br />
	 * <code>
	 * DMTF:CIM Server<br />
	 * DMTF:CIM Server:Protocol Adapter<br />
	 * DMTF:CIM Server:Provider Registration <br />
	 * </code><br />
	 * The Organization MUST be the CIM_RegisteredProfile.RegisteredOrganization
	 * property value. The Profile Name MUST be the
	 * CIM_RegisteredProfile.RegisteredName property value. The subprofile Name
	 * MUST be the CIM_RegisteredProfile.RegisteredName property value when it
	 * is used as a Dependent in the CIM_SubProfileRequiresProfile association
	 * for the specified Profile Name (used as the antecedent).
	 * 
	 */
	public static final String REG_PROF_SUPP = "RegisteredProfilesSupported";

	/**
	 * Gets the URL of the directory from which this advertisement was received
	 * 
	 * @return The directory URL
	 */
	public abstract String getDirectory();

	/**
	 * Returns the concrete service type. E.g. for the SLP advertised service
	 * <code>service:wbem:https</code> this method would return
	 * <code>https</code>.
	 * 
	 * @return The concrete service type
	 */
	public abstract String getConcreteServiceType();

	/**
	 * Returns the interop namespaces
	 * 
	 * @return The interop namespaces
	 */
	public abstract String[] getInteropNamespaces();

	/**
	 * Returns the service url, e.g. http://9.155.62.79:5988
	 * 
	 * @return The service url
	 */
	public abstract String getServiceUrl();

	/**
	 * Return the attribute value for a given attribute name
	 * 
	 * @param pAttributeName
	 *            The attribute name
	 * @return The value
	 */
	public abstract String getAttribute(String pAttributeName);

	/**
	 * Return the set of attributes of this advertisement
	 * 
	 * @return A Set&lt;Map.Entry&lt;String, String&gt;&gt; containing the name
	 *         value pairs of the attributes.
	 */
	public abstract Set<Entry<String, String>> getAttributes();

	/**
	 * Returns the service id
	 * 
	 * @return The service id
	 */
	public abstract String getServiceId();

	/**
	 * Returns the expiration state of the advertisement.
	 * 
	 * @return <code>true</code> when advertisement is expired.
	 */
	public abstract boolean isExpired();

	/**
	 * Sets the expirations state of the advertisement. Might be used by the
	 * application to mark an advertisement as expired, e.g. when it's no longer
	 * reported by the corresponding directory. Used for this purpose by
	 * AdvertisementCatalog.
	 * 
	 * @param pExpired
	 *            The new value
	 */
	public abstract void setExpired(boolean pExpired);

	/**
	 * Creates a fully-initialized WBEMClient instance connected to the service
	 * that is subject of this advertisement. On every call to this method a new
	 * client will be created. The client is not stored or cached anywhere in
	 * this class.
	 * 
	 * @param pSubject
	 *            The credential for authenticating with the service
	 * @param pLocales
	 *            An array of locales ordered by preference
	 * @return The WBEM client
	 * @throws Exception
	 * @pattern Factory Methods
	 */
	public abstract WBEMClient createClient(Subject pSubject, Locale[] pLocales) throws Exception;
}
