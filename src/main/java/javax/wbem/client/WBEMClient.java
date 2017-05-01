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
 * @author : Alexander Wolf-Reber, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-11-08  lupusalex    Make SBLIM client JSR48 compliant
 * 1686000    2007-04-19  ebak         modifyInstance() missing from WBEMClient
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 2858933    2009-10-12  raman_arora  JSR48 new APIs: associatorClasses & associatorInstances
 * 2886829    2009-11-18  raman_arora  JSR48 new APIs: referenceClasses & referenceInstances
 * 2959586    2010-03-01  blaschke-oss Sync up WBEMClient javadoc with JSR48 1.0.0
 * 2961592    2010-03-01  blaschke-oss Remove WBEMClient.setLocales() UnsupportedOperationException
 * 3496301    2012-03-02  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final
 * 3496343    2012-03-02  blaschke-oss JSR48 1.0.0: deprecate WBEMClient associators and references
 * 3514537    2012-04-03  blaschke-oss TCK: execQueryInstances requires boolean, not Boolean
 * 3521131    2012-04-24  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final II
 * 3521328    2012-04-25  blaschke-oss JSR48 1.0.0: remove WBEMClient associators and references
 * 3525657    2012-05-10  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final III
 * 3555752    2012-09-13  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final IV
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.wbem.client;

import java.util.Locale;

import javax.cim.CIMArgument;
import javax.cim.CIMClass;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMQualifierType;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger64;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * The <code>WBEMClient</code> interface is used to invoke WBEM operations
 * against a WBEM Server. A <code>WBEMClient</code> implementation can be
 * retrieved from the <code>WBEMClientFactory</code> specifying the protocol to
 * be used.
 * 
 * @see WBEMClientFactory
 */
public interface WBEMClient {

	/**
	 * Enumerates CIM classes that are associated to a specified source CIM
	 * class.
	 * 
	 * @param pObjectName
	 *            <code>CIMObjectPath</code> defining the source CIM Class whose
	 *            associated classes are to be returned. The
	 *            <code>pObjectName</code> shall include the host, namespace and
	 *            object name. The keys shall not be populated.
	 * @param pAssociationClass
	 *            This string shall contain a valid CIM Association class name
	 *            or be <code>null</code>. It filters the classes returned to
	 *            contain only classes associated to the source Object via this
	 *            CIM Association class or one of its subclasses.
	 * @param pResultClass
	 *            This string shall either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the classes returned to contain
	 *            only the classes of this class name or one of its subclasses.
	 * @param pRole
	 *            This string shall either contain a valid Property name or be
	 *            <code>null</code>. It filters the classes returned to contain
	 *            only classes associated to the source class via an Association
	 *            class in which the <i>source class</i> plays the specified
	 *            role. (i.e. the Property name in the Association class that
	 *            refers to the <i>source class</i> matches this value) For
	 *            example, if "Antecedent" is specified, then only Associations
	 *            in which the source class is the "Antecedent" reference are
	 *            examined.
	 * @param pResultRole
	 *            This string shall either contain a valid Property name or be
	 *            <code>null</code>. It filters the classes returned to contain
	 *            only classes associated to the source class via an Association
	 *            class in which the <i>class returned</i> plays the specified
	 *            role. (i.e. the Property name in the Association class that
	 *            refers to the <i>class returned</i> matches this value)
	 * @param pIncludeQualifiers
	 *            If <code>true</code>, all Qualifiers for each class (including
	 *            Qualifiers on the Object and on any returned Properties) MUST
	 *            be included in the classes returned. If <code>false</code>, no
	 *            Qualifiers are present in each class returned.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property or method. If <code>true</code>, the
	 *            class origin attribute shall be present for each property and
	 *            method on all classes returned. If <code>false</code>, the
	 *            class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the classes returned. Each <code>CIMClass</code> returned
	 *            shall only contain elements for the properties of the names
	 *            specified. Duplicate and invalid property names are ignored
	 *            and the request is otherwise processed normally. An empty
	 *            array indicates that no properties should be included in the
	 *            classes returned. A <code>null</code> value indicates that all
	 *            properties should be contained in the classes returned.
	 * @return If successful, a <code>CloseableIterator</code> containing zero
	 *         or more <code>CIMClass</code>es meeting the specified criteria
	 *         are returned.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes shall be
	 *             returned along with zero or more instances of
	 *             <code>CIM_Error</code>. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMClass> associatorClasses(CIMObjectPath pObjectName,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeQualifiers, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException;

	/**
	 * Enumerates CIM Instances associated to a specified source CIM Instance.
	 * 
	 * @param pObjectName
	 *            <code>CIMObjectPath</code> defining the source CIM Instance
	 *            whose associated instances are to be returned. The
	 *            <code>pObjectName</code> must contain the host, namespace,
	 *            object name and keys for the instance.
	 * @param pAssociationClass
	 *            This string shall either contain a valid CIM Association class
	 *            name or be <code>null</code>. It filters the instances
	 *            returned to contain only instances associated to the source
	 *            instance via this CIM Association class or one of its
	 *            subclasses.
	 * @param pResultClass
	 *            This string shall either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the instances returned to
	 *            contain only the instances of this Class name or one of its
	 *            subclasses.
	 * @param pRole
	 *            This string shall either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects associated to the source Object via an
	 *            Association class in which the <i>source Object</i> plays the
	 *            specified role. (i.e. the Property name in the Association
	 *            class that refers to the <i>source Object</i> matches this
	 *            value) If "Antecedent" is specified, then only Associations in
	 *            which the source Object is the "Antecedent" reference are
	 *            examined.
	 * @param pResultRole
	 *            This string shall either contain a valid Property name or be
	 *            <code>null</code>. It filters the instances returned to
	 *            contain only instances associated to the source instance via
	 *            an Association class in which the <i>instance returned</i>
	 *            plays the specified role. (i.e. the Property name in the
	 *            Association class that refers to the <i>instance returned</i>
	 *            matches this value) For example, if "Dependent" is specified,
	 *            then only Associations in which the instance returned is the
	 *            "Dependent" reference are examined.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property. If <code>true</code>, the class origin
	 *            attribute may be present for each property on all instances
	 *            returned, even if requested the server may ignore the request
	 *            and not return the class origin. If <code>false</code>, the
	 *            class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the Objects returned. Each <code>CIMInstance</code> returned
	 *            only contains elements for the properties of the names
	 *            specified. Duplicate and invalid property names are ignored
	 *            and the request is otherwise processed normally. An empty
	 *            array indicates that no properties should be included in the
	 *            instances returned. A <code>null</code> value indicates that
	 *            all properties should be contained in the instances returned.
	 * @return If successful, a <code>CloseableIterator</code> containing zero
	 *         or more <code>CIMInstance</code>s meeting the specified criteria
	 *         is returned.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes shall be
	 *             returned along with zero or more instances of
	 *             <code>CIM_Error</code>. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMInstance> associatorInstances(CIMObjectPath pObjectName,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException;

	/**
	 * Enumerates the <code>CIMObjectPath</code>s of CIM Objects that are
	 * associated to a particular source CIM Object. If the source Object is a
	 * CIM Class, then a <code>CloseableIterator</code> of
	 * <code>CIMObjectPath</code> s of the classes associated to the source
	 * Object is returned. If the source Object is a CIM Instance, then a
	 * <code>CloseableIterator</code> of <code>CIMObjectPath</code>s of the
	 * <code>CIMInstance</code> objects associated to the source Object is
	 * returned.
	 * 
	 * @param pObjectName
	 *            <code>CIMObjectPath</code> defining the source CIM Object
	 *            whose associated Objects are to be returned. This argument may
	 *            contain either a Class name or the modelpath of an Instance.
	 *            (i.e. Keys populated)
	 * @param pAssociationClass
	 *            This string MUST either contain a valid CIM Association class
	 *            name or be <code>null</code>. It filters the Objects returned
	 *            to contain only Objects associated to the source Object via
	 *            this CIM Association class or one of its subclasses.
	 * @param pResultClass
	 *            This string MUST either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only the Objects of this Class name or one of its subclasses.
	 * @param pRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects associated to the source Object via an
	 *            Association class in which the <i>source Object</i> plays the
	 *            specified role. (i.e. the Property name in the Association
	 *            class that refers to the <i>source Object</i> matches this
	 *            value) If "Antecedent" is specified, then only Associations in
	 *            which the source Object is the "Antecedent" reference are
	 *            examined.
	 * @param pResultRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects associated to the source Object via an
	 *            Association class in which the <i>Object returned</i> plays
	 *            the specified role. (i.e. the Property name in the Association
	 *            class that refers to the <i>Object returned</i> matches this
	 *            value) If "Dependent" is specified, then only Associations in
	 *            which the Object returned is the "Dependent" reference are
	 *            examined.
	 * @return If successful, a <code>CloseableIterator</code> containing zero
	 *         or more <code>CIMObjectPath</code> objects of the CIM Classes or
	 *         CIM Instances meeting the specified criteria is returned.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMObjectPath> associatorNames(CIMObjectPath pObjectName,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole)
			throws WBEMException;

	/**
	 * <code>associatorPaths</code> shall start an enumeration session for
	 * traversing associations starting from the instance defined in the
	 * <code>pInstancePath</code> parameter using any specified filtering
	 * criteria and return zero or more <code>CIMObjectPath</code> objects.
	 * 
	 * @param pInstancePath
	 *            The <code>CIMObjectPath</code> for the instance for which the
	 *            enumeration is to be performed.
	 * @param pAssociationClass
	 *            This string MUST either contain a valid CIM Association class
	 *            name or be <code>null</code>. It filters the Objects returned
	 *            to contain only Objects associated to the source Object via
	 *            this CIM Association class or one of its subclasses.
	 * @param pResultClass
	 *            This string MUST either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only the Objects of this Class name or one of its subclasses.
	 * @param pRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects associated to the source Object via an
	 *            Association class in which the <i>source Object</i> plays the
	 *            specified role. (i.e. the Property name in the Association
	 *            class that refers to the <i>source Object</i> matches this
	 *            value) If "Antecedent" is specified, then only Associations in
	 *            which the source Object is the "Antecedent" reference are
	 *            examined.
	 * @param pResultRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects associated to the source Object via an
	 *            Association class in which the <i>Object returned</i> plays
	 *            the specified role. (i.e. the Property name in the Association
	 *            class that refers to the <i>Object returned</i> matches this
	 *            value) If "Dependent" is specified, then only Associations in
	 *            which the Object returned is the "Dependent" reference are
	 *            examined.
	 * @param pFilterQueryLanguage
	 *            The <code>pFilterQueryLanguage</code> represents the query
	 *            language for the <code>pFilterQuery</code> argument. This must
	 *            be left <code>null</code> if a <code>pFilterQuery</code> is
	 *            not supplied. If the implementation does not support the query
	 *            language specified, the
	 *            <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error shall
	 *            be returned. If the implementation does not support filtered
	 *            enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pFilterQuery
	 *            The <code>pFilterQuery</code> specifies a query in the form of
	 *            the query language specified by the
	 *            <code>pFilterQueryLanguage</code> parameter. If this value is
	 *            not <code>null</code>, the <code>pFilterQueryLanguage</code>
	 *            parameter must be non-<code>null</code>. This value shall act
	 *            as an additional filter on the result set. If the
	 *            implementation does not support the query language specified,
	 *            the <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error
	 *            shall be returned. If the implementation does not support
	 *            filtered enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pTimeout
	 *            This input parameter determines the minimum time the CIM
	 *            server shall maintain the open enumeration session after the
	 *            last Open or Pull operation (unless the enumeration session is
	 *            closed). If the operation timeout is exceeded, the
	 *            implementation may close the enumeration session at any time,
	 *            releasing any resources allocated to the enumeration session.
	 *            A <code>pTimeout</code> of 0 means that there is no operation
	 *            timeout. That is, the enumeration session is never closed
	 *            based on time. If <code>pTimeout</code> is <code>null</code>,
	 *            the implementation shall choose an operation timeout. All
	 *            other values for <code>pTimeout</code> specify the operation
	 *            timeout in seconds. A implementation may restrict the set of
	 *            allowable values for <code>pTimeout</code>. Specifically, the
	 *            implementation may not allow 0 (no timeout). If the specified
	 *            value is not an allowable value, the implementation shall
	 *            return failure with the status code
	 *            <code>CIM_ERR_INVALID_OPERATION_TIMEOUT</code>.
	 * @param pContinueOnError
	 *            If <code>true</code>, requests that the operation resume when
	 *            an error is received. If a implementation does not support
	 *            continuation on error and <code>pContinueOnError</code> is
	 *            <code>true</code>, it shall throw a <code>WBEMException</code>
	 *            with the status code
	 *            <code>CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED</code>. If a
	 *            implementation supports continuation on error and
	 *            <code>pContinueOnError</code> is <code>true</code>, the
	 *            enumeration session shall remain open when a Pull operation
	 *            fails, and any subsequent successful Pull operations shall
	 *            return the set of elements that would have been returned if
	 *            the failing Pull operations were successful. This behavior is
	 *            subject to the consistency rules defined for pulled
	 *            enumerations. If <code>pContinueOnError</code> is
	 *            <code>false</code>, the enumeration session shall be closed
	 *            when either the operation completes successfully or when a
	 *            <code>WBEMExcetpion</code> is thrown.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @return The return value of a successful Open operation is an array of
	 *         enumerated elements with a number of entries from 0 up to a
	 *         maximum defined by <code>pMaxObjects</code>. These entries meet
	 *         the criteria defined in the Open operation. Note that returning
	 *         no entries in the array does not imply that the enumeration
	 *         session is exhausted. Client must evaluate the
	 *         <code>EnumerateResponse.isEnd()</code> to determine if there are
	 *         more elements.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_OPERATION_TIMEOUT
	 *      CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_NOT_FOUND (the source instance was not found)
	 *      CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_QUERY
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMObjectPath> associatorPaths(CIMObjectPath pInstancePath,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * <code>associators</code> shall start an enumeration session for
	 * traversing associations starting from the instance defined in the
	 * <code>pInstancePath</code> parameter using any specified filtering
	 * criteria and return zero or more <code>CIMInstance</code> objects.
	 * 
	 * @param pInstancePath
	 *            The <code>CIMObjectPath</code> for the instance for which the
	 *            enumeration is to be performed.
	 * @param pAssociationClass
	 *            This string MUST either contain a valid CIM Association class
	 *            name or be <code>null</code>. It filters the Objects returned
	 *            to contain only Objects associated to the source Object via
	 *            this CIM Association class or one of its subclasses.
	 * @param pResultClass
	 *            This string MUST either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only the Objects of this Class name or one of its subclasses.
	 * @param pRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects associated to the source Object via an
	 *            Association class in which the <i>source Object</i> plays the
	 *            specified role. (i.e. the Property name in the Association
	 *            class that refers to the <i>source Object</i> matches this
	 *            value) If "Antecedent" is specified, then only Associations in
	 *            which the source Object is the "Antecedent" reference are
	 *            examined.
	 * @param pResultRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects associated to the source Object via an
	 *            Association class in which the <i>Object returned</i> plays
	 *            the specified role. (i.e. the Property name in the Association
	 *            class that refers to the <i>Object returned</i> matches this
	 *            value) If "Dependent" is specified, then only Associations in
	 *            which the Object returned is the "Dependent" reference are
	 *            examined.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property. If <code>true</code>, the class origin
	 *            attribute may be present for each property on all instances
	 *            returned, even if requested the server may ignore the request
	 *            and not return the class origin. If <code>false</code>, the
	 *            class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the Objects returned. Each <code>CIMClass</code> or
	 *            <code>CIMInstance</code> returned only contains elements for
	 *            the properties of the names specified. Duplicate and invalid
	 *            property names are ignored and the request is otherwise
	 *            processed normally. An empty array indicates that no
	 *            properties should be included in the Objects returned. A
	 *            <code>null</code> value indicates that all properties should
	 *            be contained in the Objects returned. <b>NOTE:</b> Properties
	 *            should not be specified in this parameter unless a non-
	 *            <code>null</code> value is specified in the
	 *            <code>pResultClass</code> parameter.
	 * @param pFilterQueryLanguage
	 *            The <code>pFilterQueryLanguage</code> represents the query
	 *            language for the <code>pFilterQuery</code> argument. This must
	 *            be left <code>null</code> if a filterQuery is not supplied. If
	 *            the implementation does not support the query language
	 *            specified, the
	 *            <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error shall
	 *            be returned. If the implementation does not support filtered
	 *            enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pFilterQuery
	 *            The <code>pFilterQuery</code> specifies a query in the form of
	 *            the query language specified by the
	 *            <code>pFilterQueryLanguage</code> parameter. If this value is
	 *            not <code>null</code>, the <code>pFilterQueryLanguage</code>
	 *            parameter must be non-<code>null</code>. This value shall act
	 *            as an additional filter on the result set. If the
	 *            implementation does not support the query language specified,
	 *            the <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error
	 *            shall be returned. If the implementation does not support
	 *            filtered enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pTimeout
	 *            This input parameter determines the minimum time the CIM
	 *            server shall maintain the open enumeration session after the
	 *            last Open or Pull operation (unless the enumeration session is
	 *            closed). If the operation timeout is exceeded, the
	 *            implementation may close the enumeration session at any time,
	 *            releasing any resources allocated to the enumeration session.
	 *            A <code>pTimeout</code> of 0 means that there is no operation
	 *            timeout. That is, the enumeration session is never closed
	 *            based on time. If <code>pTimeout</code> is <code>null</code>,
	 *            the implementation shall choose an operation timeout. All
	 *            other values for <code>pTimeout</code> specify the operation
	 *            timeout in seconds. A implementation may restrict the set of
	 *            allowable values for <code>pTimeout</code>. Specifically, the
	 *            implementation may not allow 0 (no timeout). If the specified
	 *            value is not an allowable value, the implementation shall
	 *            return failure with the status code
	 *            <code>CIM_ERR_INVALID_OPERATION_TIMEOUT</code>.
	 * @param pContinueOnError
	 *            If <code>true</code>, requests that the operation resume when
	 *            an error is received. If a implementation does not support
	 *            continuation on error and <code>pContinueOnError</code> is
	 *            <code>true</code>, it shall throw a <code>WBEMException</code>
	 *            with the status code
	 *            <code>CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED</code>. If a
	 *            implementation supports continuation on error and
	 *            <code>pContinueOnError</code> is <code>true</code>, the
	 *            enumeration session shall remain open when a Pull operation
	 *            fails, and any subsequent successful Pull operations shall
	 *            return the set of elements that would have been returned if
	 *            the failing Pull operations were successful. This behavior is
	 *            subject to the consistency rules defined for pulled
	 *            enumerations. If <code>pContinueOnError</code> is
	 *            <code>false</code>, the enumeration session shall be closed
	 *            when either the operation completes successfully or when a
	 *            <code>WBEMExcetpion</code> is thrown.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @return The return value of a successful Open operation is an array of
	 *         enumerated elements with a number of entries from 0 up to a
	 *         maximum defined by <code>pMaxObjects</code>. These entries meet
	 *         the criteria defined in the Open operation. Note that returning
	 *         no entries in the array does not imply that the enumeration
	 *         session is exhausted. Client must evaluate the
	 *         <code>EnumerateResponse.isEnd()</code> to determine if there are
	 *         more elements.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_OPERATION_TIMEOUT
	 *      CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_NOT_FOUND (The source instance was not found)
	 *      CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_QUERY
	 *      CIM_ERR_FAILED (Some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMInstance> associators(CIMObjectPath pInstancePath,
			String pAssociationClass, String pResultClass, String pRole, String pResultRole,
			boolean pIncludeClassOrigin, String[] pPropertyList, String pFilterQueryLanguage,
			String pFilterQuery, UnsignedInteger32 pTimeout, boolean pContinueOnError,
			UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * Closes the <code>WBEMClient</code> session.
	 */
	public void close();

	/**
	 * <code>closeEnumeration</code> shall close an enumeration session that has
	 * been previously started but not yet completed. Clients should always use
	 * this method when an enumeration session has been started and the client
	 * does not retrieve all the results. If a client has started an enumeration
	 * session and retrieves all the results until the
	 * <code>EnumerationResponse.isEnd()</code> is true, this method shall not
	 * be called.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> representing the namespace to
	 *            be used.
	 * @param pContext
	 *            The enumeration context to close.
	 * @throws WBEMException
	 */
	public void closeEnumeration(CIMObjectPath pPath, String pContext) throws WBEMException;

	/**
	 * Create a CIM class. The namespace from the
	 * <code>CIMClass.getObjectPath()</code> shall be used.
	 * 
	 * @param pClass
	 *            The <code>CIMClass</code> to be created.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_ALREADY_EXISTS (the CIM Class already exists)
	 *      CIM_ERR_INVALID_SUPERCLASS (the putative CIM Class declares a
	 *            non-existent superclass)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public void createClass(CIMClass pClass) throws WBEMException;

	/**
	 * Create a CIM Instance. The namespace from the
	 * <code>CIMInstance.getObjectPath()</code> shall be used. The keys of the
	 * <code>CIMInstance</code> may be modified by the implementation and the
	 * client must use the returned object path to determine the name of the
	 * instance. It is possible for a client to leave keys of instances empty/
	 * <code>null</code> and the provider can fill them in. This is
	 * implementation specific unless specified by a CIM Schema or in a DMTF
	 * Profile.
	 * 
	 * @param pInstance
	 *            The <code>CIMInstance</code> to be created.
	 * @return <code>CIMObjectPath</code> of the instance created.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED (provider does not support this method)
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_INVALID_CLASS (in this namespace)
	 *      CIM_ERR_ALREADY_EXISTS
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CIMObjectPath createInstance(CIMInstance pInstance) throws WBEMException;

	/**
	 * Deletes the CIM class for the object specified by the CIM object path.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> identifying the namespace and
	 *            class name to delete.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_NOT_FOUND (the CIM Class to be deleted does not exist)
	 *      CIM_ERR_CLASS_HAS_CHILDREN (the CIM Class has one or more subclasses
	 *            which cannot be deleted)
	 *      CIM_ERR_CLASS_HAS_INSTANCES (the CIM Class has one or more instances
	 *            which cannot be deleted)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public void deleteClass(CIMObjectPath pPath) throws WBEMException;

	/**
	 * Delete the CIM instance specified by the CIM object path.
	 * 
	 * @param pPath
	 *            The object path of the instance to be deleted. It must include
	 *            all of the keys.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED (provider does not support this method)
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_INVALID_CLASS (in this namespace)
	 *      CIM_ERR_NOT_FOUND (if the instance does not exist)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public void deleteInstance(CIMObjectPath pPath) throws WBEMException;

	/**
	 * Delete a CIM Qualifier type.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> identifying the name and
	 *            namespace of the CIM qualifier type to delete.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_NOT_FOUND (the Qualifier did not exist)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public void deleteQualifierType(CIMObjectPath pPath) throws WBEMException;

	/**
	 * Enumerate CIM Classes.
	 * 
	 * @param pPath
	 *            The object path of the class to be enumerated. Only the
	 *            namespace and object name should be populated. If the object
	 *            name is set to an empty string (i.e. ""), then all base
	 *            classes in the target namespace are returned.
	 * @param pDeep
	 *            If <code>true</code>, the classes returned shall include
	 *            subclasses. If <code>false</code>, the classes returned shall
	 *            not include subclasses.
	 * @param pLocalOnly
	 *            If <code>true</code>, only elements (properties, methods and
	 *            qualifiers) defined in, or overridden in the class are
	 *            included in the response. If <code>false</code>, all elements
	 *            of the class definition are returned.
	 * @param pIncludeQualifiers
	 *            If <code>true</code>, all Qualifiers for each Class and its
	 *            elements (properties, methods, references) are included. If
	 *            <code>false</code>, no Qualifiers are present in the classes
	 *            returned.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property or method. If <code>true</code>, the
	 *            class origin attribute shall be present for each property and
	 *            method on all returned <code>CIMClass</code>es. If
	 *            <code>false</code>, the class origin shall not be present.
	 * @return A <code>CloseableIterator</code> of <code>CIMClass</code>es.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_INVALID_CLASS (the CIM Class that is the basis for this
	 *            enumeration does not exist)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMClass> enumerateClasses(CIMObjectPath pPath, boolean pDeep,
			boolean pLocalOnly, boolean pIncludeQualifiers, boolean pIncludeClassOrigin)
			throws WBEMException;

	/**
	 * Enumerate the names of CIM Classes.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> identifying the class to be
	 *            enumerated. If the class name in the object path specified is
	 *            an empty string (i.e. ""), all base classes in the target
	 *            namespace are returned. Note that only the namespace and the
	 *            name components should be populated.
	 * @param pDeep
	 *            If <code>true</code>, the enumeration returned shall contain
	 *            the names of all classes derived from the class being
	 *            enumerated. If <code>false</code>, the enumeration returned
	 *            contains only the names of the first level children of the
	 *            class.
	 * @return A <code>CloseableIterator</code> of <code>CIMObjectPath</code>s.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_INVALID_CLASS (the CIM Class that is the basis for this
	 *            enumeration does not exist)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMObjectPath> enumerateClassNames(CIMObjectPath pPath, boolean pDeep)
			throws WBEMException;

	/**
	 * Enumerate the names of the instances for a specified class. The names of
	 * all subclass instances are returned.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> identifying the class whose
	 *            instances are to be enumerated. Only the namespace and class
	 *            name components are used. All other information (e.g. Keys) is
	 *            ignored.
	 * @return <code>CloseabelIterator</code> of <code>CIMObjectPath</code>s.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED (provider does not support this method)
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_INVALID_CLASS (in this namespace)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMObjectPath> enumerateInstanceNames(CIMObjectPath pPath)
			throws WBEMException;

	/**
	 * <code>enumerateInstancePaths</code> shall enumerate the instances of the
	 * specified class in <code>pClassPath</code> and return zero or more
	 * <code>CIMObjectPath</code>s.
	 * 
	 * @param pClassPath
	 *            The <code>CIMObjectPath</code> for the class for which the
	 *            enumeration is to be performed.
	 * @param pFilterQueryLanguage
	 *            The <code>pFilterQueryLanguage</code> represents the query
	 *            language for the <code>pFilterQuery</code> argument. This must
	 *            be left <code>null</code> if a <code>pFilterQuery</code> is
	 *            not supplied. If the implementation does not support the query
	 *            language specified, the
	 *            <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error shall
	 *            be returned. If the implementation does not support filtered
	 *            enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pFilterQuery
	 *            The <code>pFilterQuery</code> specifies a query in the form of
	 *            the query language specified by the
	 *            <code>pFilterQueryLanguage</code> parameter. If this value is
	 *            not <code>null</code>, the <code>pFilterQueryLanguage</code>
	 *            parameter must be non-<code>null</code>. This value shall act
	 *            as an additional filter on the result set. If the
	 *            implementation does not support the query language specified,
	 *            the <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error
	 *            shall be returned. If the implementation does not support
	 *            filtered enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pTimeout
	 *            This input parameter determines the minimum time the CIM
	 *            server shall maintain the open enumeration session after the
	 *            last Open or Pull operation (unless the enumeration session is
	 *            closed). If the operation timeout is exceeded, the
	 *            implementation may close the enumeration session at any time,
	 *            releasing any resources allocated to the enumeration session.
	 *            A <code>pTimeout</code> of 0 means that there is no operation
	 *            timeout. That is, the enumeration session is never closed
	 *            based on time. If <code>pTimeout</code> is <code>null</code>,
	 *            the implementation shall choose an operation timeout. All
	 *            other values for <code>pTimeout</code> specify the operation
	 *            timeout in seconds. A implementation may restrict the set of
	 *            allowable values for <code>pTimeout</code>. Specifically, the
	 *            implementation may not allow 0 (no timeout). If the specified
	 *            value is not an allowable value, the implementation shall
	 *            return failure with the status code
	 *            <code>CIM_ERR_INVALID_OPERATION_TIMEOUT</code>.
	 * @param pContinueOnError
	 *            If <code>true</code>, requests that the operation resume when
	 *            an error is received. If a implementation does not support
	 *            continuation on error and <code>pContinueOnError</code> is
	 *            <code>true</code>, it shall throw a <code>WBEMException</code>
	 *            with the status code
	 *            <code>CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED</code>. If a
	 *            implementation supports continuation on error and
	 *            <code>pContinueOnError</code> is <code>true</code>, the
	 *            enumeration session shall remain open when a Pull operation
	 *            fails, and any subsequent successful Pull operations shall
	 *            return the set of elements that would have been returned if
	 *            the failing Pull operations were successful. This behavior is
	 *            subject to the consistency rules defined for pulled
	 *            enumerations. If <code>pContinueOnError</code> is
	 *            <code>false</code>, the enumeration session shall be closed
	 *            when either the operation completes successfully or when a
	 *            <code>WBEMExcetpion</code> is thrown.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @return The return value of a successful Open operation is an array of
	 *         enumerated elements with a number of entries from 0 up to a
	 *         maximum defined by <code>pMaxObjects</code>. These entries meet
	 *         the criteria defined in the Open operation. Note that returning
	 *         no entries in the array does not imply that the enumeration
	 *         session is exhausted. Client must evaluate the
	 *         <code>EnumerateResponse.isEnd()</code> to determine if there are
	 *         more elements.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_OPERATION_TIMEOUT
	 *      CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_INVALID_CLASS (the source class does not exist)
	 *      CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_QUERY
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMObjectPath> enumerateInstancePaths(CIMObjectPath pClassPath,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * Enumerate the instances of a class. The instances of all subclasses are
	 * also returned.
	 * 
	 * @param pPath
	 *            The object path of the class to be enumerated. Only the
	 *            namespace and class name components are used. Any other
	 *            information (e.g. Keys) is ignored.
	 * @param pDeep
	 *            If <code>true</code>, this specifies that, for each returned
	 *            Instance of the Class, all properties of the Instance must be
	 *            present (subject to constraints imposed by the other
	 *            parameters), including any which were added by subclassing the
	 *            specified Class. If <code>false</code>, each returned Instance
	 *            includes only properties defined for the specified Class in
	 *            path.
	 * @param pLocalOnly
	 *            If <code>true</code>, only properties that were instantiated
	 *            in the instance are returned. WBEM Servers may ignore this
	 *            parameter and return all properties.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property. If <code>true</code>, the class origin
	 *            attribute may be present for each property on all returned
	 *            CIMInstances, even if requested the server may ignore the
	 *            request and not return the class origin. If <code>false</code>
	 *            , the class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the instances returned. Each instance returned only contains
	 *            elements for the properties of the names specified. Duplicate
	 *            and invalid property names are ignored and the request is
	 *            otherwise processed normally. An empty array indicates that no
	 *            properties should be returned. A <b>null</b> value indicates
	 *            that all properties should be returned.
	 * @return A <code>CloseableIterator</code> of <code>CIMInstance</code>s.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_INVALID_CLASS (in this namespace)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMInstance> enumerateInstances(CIMObjectPath pPath, boolean pDeep,
			boolean pLocalOnly, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException;

	/**
	 * <code>enumerateInstances</code> shall enumerate the instances of the
	 * specified class in <code>pClassPath</code> and return zero or more
	 * <code>CIMInstance</code>s.
	 * 
	 * @param pClassPath
	 *            The <code>CIMObjectPath</code> for the class for which the
	 *            enumeration is to be performed.
	 * @param pDeepInheritance
	 *            If <code>true</code>, this specifies that, for each returned
	 *            Instance of the Class, all properties of the Instance must be
	 *            present (subject to constraints imposed by the other
	 *            parameters), including any which were added by subclassing the
	 *            specified Class. If <code>false</code>, each returned Instance
	 *            includes only properties defined for the specified Class in
	 *            path.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property. If <code>true</code>, the class origin
	 *            attribute may be present for each property on all instances
	 *            returned, even if requested the server may ignore the request
	 *            and not return the class origin. If <code>false</code>, the
	 *            class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the Objects returned. Each <code>CIMInstance</code> returned
	 *            <b>only</b> contains elements for the properties of the names
	 *            specified. Duplicate and invalid property names are ignored
	 *            and the request is otherwise processed normally. An empty
	 *            array indicates that no properties should be included in the
	 *            Objects returned. A <code>null</code> value indicates that all
	 *            properties should be contained in the Objects returned.
	 * @param pFilterQueryLanguage
	 *            The <code>pFilterQueryLanguage</code> represents the query
	 *            language for the <code>pFilterQuery</code> argument. This must
	 *            be left <code>null</code> if a <code>pFilterQuery</code> is
	 *            not supplied. If the implementation does not support the query
	 *            language specified, the
	 *            <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error shall
	 *            be returned. If the implementation does not support filtered
	 *            enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pFilterQuery
	 *            The <code>pFilterQuery</code> specifies a query in the form of
	 *            the query language specified by the
	 *            <code>pFilterQueryLanguage</code> parameter. If this value is
	 *            not <code>null</code>, the <code>pFilterQueryLanguage</code>
	 *            parameter must be non-<code>null</code>. This value shall act
	 *            as an additional filter on the result set. If the
	 *            implementation does not support the query language specified,
	 *            the <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error
	 *            shall be returned. If the implementation does not support
	 *            filtered enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pTimeout
	 *            This input parameter determines the minimum time the CIM
	 *            server shall maintain the open enumeration session after the
	 *            last Open or Pull operation (unless the enumeration session is
	 *            closed). If the operation timeout is exceeded, the
	 *            implementation may close the enumeration session at any time,
	 *            releasing any resources allocated to the enumeration session.
	 *            A <code>pTimeout</code> of 0 means that there is no operation
	 *            timeout. That is, the enumeration session is never closed
	 *            based on time. If <code>pTimeout</code> is <code>null</code>,
	 *            the implementation shall choose an operation timeout. All
	 *            other values for <code>pTimeout</code> specify the operation
	 *            timeout in seconds. A implementation may restrict the set of
	 *            allowable values for <code>pTimeout</code>. Specifically, the
	 *            implementation may not allow 0 (no timeout). If the specified
	 *            value is not an allowable value, the implementation shall
	 *            return failure with the status code
	 *            <code>CIM_ERR_INVALID_OPERATION_TIMEOUT</code>.
	 * @param pContinueOnError
	 *            If <code>true</code>, requests that the operation resume when
	 *            an error is received. If a implementation does not support
	 *            continuation on error and <code>pContinueOnError</code> is
	 *            <code>true</code>, it shall throw a <code>WBEMException</code>
	 *            with the status code
	 *            <code>CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED</code>. If a
	 *            implementation supports continuation on error and
	 *            <code>pContinueOnError</code> is <code>true</code>, the
	 *            enumeration session shall remain open when a Pull operation
	 *            fails, and any subsequent successful Pull operations shall
	 *            return the set of elements that would have been returned if
	 *            the failing Pull operations were successful. This behavior is
	 *            subject to the consistency rules defined for pulled
	 *            enumerations. If <code>pContinueOnError</code> is
	 *            <code>false</code>, the enumeration session shall be closed
	 *            when either the operation completes successfully or when a
	 *            <code>WBEMExcetpion</code> is thrown.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @return The return value of a successful Open operation is an array of
	 *         enumerated elements with a number of entries from 0 up to a
	 *         maximum defined by <code>pMaxObjects</code>. These entries meet
	 *         the criteria defined in the Open operation. Note that returning
	 *         no entries in the array does not imply that the enumeration
	 *         session is exhausted. Client must evaluate the
	 *         <code>EnumerateResponse.isEnd()</code> to determine if there are
	 *         more elements.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 * 
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_OPERATION_TIMEOUT
	 *      CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_INVALID_CLASS (the source class does not exist)
	 *      CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_QUERY
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMInstance> enumerateInstances(CIMObjectPath pClassPath,
			boolean pDeepInheritance, boolean pIncludeClassOrigin, String[] pPropertyList,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * Enumerates the CIM Qualifier types for a specific namespace.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> identifying the namespace whose
	 *            qualifier types are to be enumerated.
	 * @return A <code>CloseableIterator</code> of <code>CIMQualifierType</code>
	 *         s.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMQualifierType<?>> enumerateQualifierTypes(CIMObjectPath pPath)
			throws WBEMException;

	/**
	 * <code>enumerationCount</code> provides an estimated count of the total
	 * number of objects in an open enumeration session represented by an
	 * enumeration context.
	 * 
	 * @param pPath
	 *            The namespace for the enumeration context.
	 * @param pEnumerationContext
	 *            The enumeration context to count.
	 * @return The estimated number of objects.
	 * @throws UnsupportedOperationException
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 * 
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_INVALID_ENUMERATION_CONTEXT
	 *      CIM_ERR_SERVER_LIMITS_EXCEEDED
	 *      CIM_ERR_FAILED
	 * </pre>
	 * 
	 */
	public UnsignedInteger64 enumerationCount(CIMObjectPath pPath, String pEnumerationContext)
			throws WBEMException;

	/**
	 * <code>execQuery</code> shall execute a query to retrieve objects.
	 * 
	 * @param pPath
	 *            <code>CIMObjectPath</code> identifying the class to query.
	 *            Only the namespace and class name components are used. All
	 *            other information (e.g. Keys) is ignored.
	 * @param pQuery
	 *            A string containing the text of the query.
	 * @param pQueryLanguage
	 *            A string that identifies the query language to use to parse
	 *            the query string specified.
	 * @return A <code>CloseableIterator</code> of <code>CIMInstance</code>s.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED (provider does not support this method)
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED (the requested query language
	 *            is not recognized)
	 *      CIM_ERR_INVALID_QUERY (the query is not a valid query in the specified
	 *            query language)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMInstance> execQuery(CIMObjectPath pPath, String pQuery,
			String pQueryLanguage) throws WBEMException;

	/**
	 * <code>execQueryInstances</code> shall execute a query to retrieve
	 * instances.
	 * 
	 * @param pObjectName
	 *            The <code>CIMObjectPath</code> representing the namespace to
	 *            be used.
	 * @param pFilterQuery
	 *            The <code>pFilterQuery</code> specifies a query in the form of
	 *            the query language specified by the
	 *            <code>pFilterQueryLanguage</code> parameter. If this value is
	 *            not <code>null</code>, the <code>pFilterQueryLanguage</code>
	 *            parameter must be non-<code>null</code>. This value shall act
	 *            as an additional filter on the result set. If the
	 *            implementation does not support the query language specified,
	 *            the <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error
	 *            shall be returned. If the implementation does not support
	 *            filtered enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pFilterQueryLanguage
	 *            The <code>pFilterQueryLanguage</code> represents the query
	 *            language for the <code>pFilterQuery</code> argument. This must
	 *            be left <code>null</code> if a <code>pFilterQuery</code> is
	 *            not supplied. If the implementation does not support the query
	 *            language specified, the
	 *            <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error shall
	 *            be returned. If the implementation does not support filtered
	 *            enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pReturnQueryResultClass
	 *            The <code>pReturnQueryResultClass</code> controls whether a
	 *            class definition is returned in <code>pQueryResultClass</code>
	 *            . If it is set to <code>false</code>,
	 *            <code>pQueryResultClass</code> shall be set to
	 *            <code>null</code> on output. If it is set to <code>true</code>
	 *            , the value of the <code>pQueryResultClass</code> on output
	 *            shall be a class definition that defines the properties
	 *            (columns) of each row of the query result.
	 * @param pTimeout
	 *            This input parameter determines the minimum time the CIM
	 *            server shall maintain the open enumeration session after the
	 *            last Open or Pull operation (unless the enumeration session is
	 *            closed). If the operation timeout is exceeded, the
	 *            implementation may close the enumeration session at any time,
	 *            releasing any resources allocated to the enumeration session.
	 *            A <code>pTimeout</code> of 0 means that there is no operation
	 *            timeout. That is, the enumeration session is never closed
	 *            based on time. If <code>pTimeout</code> is <code>null</code>,
	 *            the implementation shall choose an operation timeout. All
	 *            other values for <code>pTimeout</code> specify the operation
	 *            timeout in seconds. A implementation may restrict the set of
	 *            allowable values for <code>pTimeout</code>. Specifically, the
	 *            implementation may not allow 0 (no timeout). If the specified
	 *            value is not an allowable value, the implementation shall
	 *            return failure with the status code
	 *            <code>CIM_ERR_INVALID_OPERATION_TIMEOUT</code>.
	 * @param pContinueOnError
	 *            If <code>true</code>, requests that the operation resume when
	 *            an error is received. If a implementation does not support
	 *            continuation on error and <code>pContinueOnError</code> is
	 *            <code>true</code>, it shall throw a <code>WBEMException</code>
	 *            with the status code
	 *            <code>CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED</code>. If a
	 *            implementation supports continuation on error and
	 *            <code>pContinueOnError</code> is <code>true</code>, the
	 *            enumeration session shall remain open when a Pull operation
	 *            fails, and any subsequent successful Pull operations shall
	 *            return the set of elements that would have been returned if
	 *            the failing Pull operations were successful. This behavior is
	 *            subject to the consistency rules defined for pulled
	 *            enumerations. If <code>pContinueOnError</code> is
	 *            <code>false</code>, the enumeration session shall be closed
	 *            when either the operation completes successfully or when a
	 *            <code>WBEMExcetpion</code> is thrown.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @param pQueryResultClass
	 *            The <code>pQueryResultClass</code> is an output argument. It
	 *            shall be <code>null</code> if the
	 *            <code>pReturnQueryResultClass</code> input parameter is set to
	 *            <code>false</code>. Otherwise, it shall return a class
	 *            definition where each property of the class corresponds to one
	 *            entry of the query select list. The class definition
	 *            corresponds to one row of the query result. The class name of
	 *            this returned class shall be "CIM_QueryResult". This class
	 *            definition is valid only in the context of this enumeration.
	 * @return The return value of a successful Open operation is an array of
	 *         enumerated elements with a number of entries from 0 up to a
	 *         maximum defined by <code>pMaxObjects</code>. These entries meet
	 *         the criteria defined in the Open operation. Note that returning
	 *         no entries in the array does not imply that the enumeration
	 *         session is exhausted. Client must evaluate the
	 *         <code>EnumerateResponse.isEnd()</code> to determine if there are
	 *         more elements.
	 * @throws UnsupportedOperationException
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_OPERATION_TIMEOUT
	 *      CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED (the requested filter query
	 *            language is not recognized)
	 *      CIM_ERR_INVALID_QUERY (the filter query is not a valid query in the 
	 *            specified filter query language)
	 *      CIM_ERR_QUERY_FEATURE_NOT_SUPPORTED (the query requires support for 
	 *            features that are not supported)
	 *      CIM_ERR_FAILED (Some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMInstance> execQueryInstances(CIMObjectPath pObjectName,
			String pFilterQuery, String pFilterQueryLanguage, boolean pReturnQueryResultClass,
			UnsignedInteger32 pTimeout, boolean pContinueOnError, UnsignedInteger32 pMaxObjects,
			CIMClass pQueryResultClass) throws WBEMException;

	/**
	 * Returns the <code>CIMClass</code> for the specified
	 * <code>CIMObjectPath</code>.
	 * 
	 * @param pName
	 *            The object path of the class to be returned. Only the name
	 *            space and class name components are used. All other
	 *            information (e.g. keys) is ignored.
	 * @param pLocalOnly
	 *            If <code>true</code>, only elements (properties, methods,
	 *            references) overridden or defined in the class are included in
	 *            the <code>CIMClass</code>returned. If <code>false</code>, all
	 *            elements of the class definition are returned.
	 * @param pIncludeQualifiers
	 *            If <code>true</code>, all Qualifiers for the class and its
	 *            elements are included in the <code>CIMClass</code> returned.
	 *            If <code>false</code>, no Qualifier information is contained
	 *            in the <code>CIMClass</code> returned.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property or method. If <code>true</code>, the
	 *            class origin attribute shall be present for each property and
	 *            method on all returned <code>CIMClass</code>es. If
	 *            <code>false</code>, the class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the <code>CIMClass</code> returned. The <code>CIMClass</code>
	 *            returned only contains elements for the properties of the
	 *            names specified. Duplicate and invalid property names are
	 *            ignored and the request is otherwise processed normally. An
	 *            empty array indicates that no properties should be returned. A
	 *            <code>null</code> value indicates that all properties should
	 *            be returned.
	 * @return <code>CIMClass</code> meeting the criteria specified.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_NOT_FOUND (the requested CIM Class does not exist in the
	 *            specified namespace)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CIMClass getClass(CIMObjectPath pName, boolean pLocalOnly, boolean pIncludeQualifiers,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException;

	/**
	 * Get a <code>CIMInstance</code>.
	 * 
	 * @param pName
	 *            The object path of the instance to be returned. The Keys in
	 *            this <code>CIMObjectPath</code> must be populated.
	 * @param pLocalOnly
	 *            If <code>true</code>, only properties overridden or defined in
	 *            the class are included in the <code>CIMInstance</code>
	 *            returned. If <code>false</code>, all properties of the class
	 *            definition are returned. WBEM Servers may ignore this
	 *            parameter and return all properties.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property. If <code>true</code>, the class origin
	 *            attribute may be present for each property of the
	 *            <code>CIMInstance</code>, even if requested the server may
	 *            ignore the request and not return the class origin. If
	 *            <code>false</code>, the class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the <code>CIMClass</code> returned. The <code>CIMClass</code>
	 *            returned only contains elements for the properties of the
	 *            names specified. Duplicate and invalid property names are
	 *            ignored and the request is otherwise processed normally. An
	 *            empty array indicates that no properties should be returned. A
	 *            <code>null</code> value indicates that all properties should
	 *            be returned.
	 * @return <code>CIMInstance</code> identified by the
	 *         <code>CIMObjectPath</code> specified.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED (provider does not support this method)
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_INVALID_CLASS (in this namespace)
	 *      CIM_ERR_NOT_FOUND (if instance does not exist)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CIMInstance getInstance(CIMObjectPath pName, boolean pLocalOnly,
			boolean pIncludeClassOrigin, String[] pPropertyList) throws WBEMException;

	/**
	 * <code>getInstancePaths</code> shall get the <code>CIMObjectPath</code>s
	 * using an enumeration context.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> representing the namespace to
	 *            be used.
	 * @param pContext
	 *            The enumeration context value for the enumeration session to
	 *            be used.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>.
	 * @return <code>EnumerateResponse</code> that includes zero or more
	 *         <code>CIMObjectPath</code> objects.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 * 
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_INVALID_ENUMERATION_CONTEXT
	 *      CIM_ERR_SERVER_LIMITS_EXCEEDED
	 *      CIM_ERR_PULL_HAS_BEEN_ABANDONED
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMObjectPath> getInstancePaths(CIMObjectPath pPath, String pContext,
			UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * <code>getInstances</code> shall get the instances from an enumeration
	 * session started by <code>execQueryInstances</code>.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> representing the namespace to
	 *            be used.
	 * @param pContext
	 *            The enumeration context value for the enumeration session to
	 *            be used.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @return <code>EnumerateResponse</code> that includes zero or more
	 *         <code>CIMObjectPath</code> objects.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 * 
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_INVALID_ENUMERATION_CONTEXT
	 *      CIM_ERR_SERVER_LIMITS_EXCEEDED
	 *      CIM_ERR_PULL_HAS_BEEN_ABANDONED
	 *      CIM_ERR_FAILED
	 * </pre>
	 */
	public EnumerateResponse<CIMInstance> getInstances(CIMObjectPath pPath, String pContext,
			UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * <code>getInstancesWithPath</code> shall use the enumeration context
	 * provided to get the next set of instances for the enumeration session.
	 * 
	 * @param pPath
	 *            The <code>CIMObjectPath</code> representing the namespace to
	 *            be used.
	 * @param pContext
	 *            The enumeration context value for the enumeration session to
	 *            be used.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>.
	 * @return <code>EnumerateResponse</code> that includes zero or more
	 *         <code>CIMObjectPath</code>. objects.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 * 
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_INVALID_ENUMERATION_CONTEXT
	 *      CIM_ERR_SERVER_LIMITS_EXCEEDED
	 *      CIM_ERR_PULL_HAS_BEEN_ABANDONED
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMInstance> getInstancesWithPath(CIMObjectPath pPath,
			String pContext, UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * Get property values. See <code>WBEMClientConstants</code> for a list of
	 * standard properties.
	 * 
	 * @param pKey
	 *            The name of the property.
	 * @return The value of the property.
	 * @see WBEMClientConstants
	 */
	public String getProperty(String pKey);

	/**
	 * Get a <code>CIMQualifierType</code>.
	 * 
	 * @param pName
	 *            <code>CIMObjectPath</code> that identifies the
	 *            <code>CIMQualifierType</code> to return.
	 * @return The <code>CIMQualifierType</code> object.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_NOT_FOUND (the requested Qualifier declaration did not exist)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CIMQualifierType<?> getQualifierType(CIMObjectPath pName) throws WBEMException;

	/**
	 * Initialize the client connection. This must be called before any
	 * operations. This must only be called once.
	 * 
	 * @param pName
	 *            The protocol and host to use (e.g. http://192.168.1.128/). Any
	 *            other fields shall be ignored.
	 * @param pSubject
	 *            The principal/credential pairs for this connection.
	 * @param pLocales
	 *            An array of locales in order of priority of preference.
	 * @throws IllegalArgumentException
	 *             If the host or scheme portion of the object path is null, or
	 *             if the protocol is not supported.
	 * @throws WBEMException
	 *             If the protocol adapter or security cannot be initialized.
	 */
	public void initialize(CIMObjectPath pName, Subject pSubject, Locale[] pLocales)
			throws IllegalArgumentException, WBEMException;

	/**
	 * Executes the specified method on the specified object.
	 * 
	 * @param pName
	 *            CIM object path of the object whose method must be invoked. It
	 *            must include all of the keys.
	 * @param pMethodName
	 *            The name of the method to be invoked.
	 * @param pInputArguments
	 *            The <code>CIMArgument</code> array of method input parameters.
	 *            </p>
	 * @param pOutputArguments
	 *            The <code>CIMArgument</code> array of method output
	 *            parameters. The array should be allocated large enough to hold
	 *            all returned parameters.
	 * @return The return value of the specified method.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED (implementation DOES NOT support ANY 
	 *            Extrinsic Method Invocation)
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_NOT_FOUND (if instance does not exist)
	 *      CIM_ERR_METHOD_NOT_FOUND
	 *      CIM_ERR_METHOD_NOT_AVAILABLE
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public Object invokeMethod(CIMObjectPath pName, String pMethodName,
			CIMArgument<?>[] pInputArguments, CIMArgument<?>[] pOutputArguments)
			throws WBEMException;

	/**
	 * Modify the <code>CIMClass</code>.
	 * 
	 * @param pClass
	 *            <code>CIMClass</code> to be modified.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_INVALID_SUPERCLASS (the putative CIM Class declares a
	 *            non-existent superclass)
	 *      CIM_ERR_CLASS_HAS_CHILDREN (the modification could not be performed
	 *            because it was not possible to update the subclasses of the Class
	 *            in a consistent fashion)
	 *      CIM_ERR_CLASS_HAS_INSTANCES (the modification could not be performed
	 *            because it was not possible to update the instances of the Class in
	 *            a consistent fashion)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public void modifyClass(CIMClass pClass) throws WBEMException;

	/**
	 * Modify some or all of the properties of the specified
	 * <code>CIMInstance</code>.
	 * 
	 * @param pInstance
	 *            <code>CIMInstance</code> to be modified. All Keys must be
	 *            populated.
	 * @param pPropertyList
	 *            An array of property names used to specify which values from
	 *            the <code>CIMInstance</code> specified to set. Properties not
	 *            specified in this list but set in the <code>CIMInstance</code>
	 *            specified are not modified. Duplicate property names are
	 *            ignored and the request is otherwise processed normally. If
	 *            the <code>pPropertyList</code> contains invalid property names
	 *            for the instance to be modified, the server shall reject the
	 *            request. An empty array indicates that no properties should be
	 *            modified. A <code>null</code> value indicates that all
	 *            properties should be modified.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED (provider does not support this method)
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (for this method)
	 *      CIM_ERR_INVALID_CLASS (in this namespace)
	 *      CIM_ERR_NOT_FOUND (if instance does not exist)
	 *      CIM_ERR_NO_SUCH_PROPERTY (in this instance)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public void modifyInstance(CIMInstance pInstance, String[] pPropertyList) throws WBEMException;

	/**
	 * Enumerates the Association classes that refer to a specified source CIM
	 * Class.
	 * 
	 * @param pObjectName
	 *            <code>CIMObjectPath</code> defining the source CIM class whose
	 *            referring classes are to be returned. <code>pObjectName</code>
	 *            shall contain the scheme, host, namespace and object name
	 *            (class name).
	 * @param pResultClass
	 *            This string shall either contain a valid CIM Class name or be
	 *            <code>null</code>. The <code>pResultClass</code> filters the
	 *            classes returned to contain only the classes of this Class
	 *            name or one of its subclasses.
	 * @param pRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects referring to the source Object via a Property
	 *            with the specified name. If "Antecedent" is specified, then
	 *            only Associations in which the source Object is the
	 *            "Antecedent" reference are returned.
	 * @param pIncludeQualifiers
	 *            If <code>true</code>, all Qualifiers for each Object
	 *            (including Qualifiers on the Object and on any returned
	 *            Properties) shall be included in the classes returned. If
	 *            <code>false</code>, no Qualifiers shall be present in each
	 *            class returned.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property or method. If <code>true</code>, the
	 *            class Origin attribute shall be present for each property and
	 *            method on all classes returned. If <code>false</code>, the
	 *            class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the Objects returned. Each <code>CIMClass</code> returned
	 *            shall only contains elements for the properties of the names
	 *            specified. Duplicate and invalid property names are ignored
	 *            and the request is otherwise processed normally. An empty
	 *            array indicates that no properties should be included in the
	 *            classes returned. A <code>null</code> value indicates that all
	 *            properties should be contained in the classes returned.
	 * @return If successful, a <code>CloseableIterator</code> referencing zero
	 *         or more <code>CIMClass</code>es meeting the specified criteria.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes shall be
	 *             returned along with zero or more instance of
	 *             <code>CIM_Error</code>. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */

	CloseableIterator<CIMClass> referenceClasses(CIMObjectPath pObjectName, String pResultClass,
			String pRole, boolean pIncludeQualifiers, boolean pIncludeClassOrigin,
			String[] pPropertyList) throws WBEMException;

	/**
	 * Enumerates the Association instances that refer to a specified source CIM
	 * Instance.
	 * 
	 * @param pObjectName
	 *            <code>CIMObjectPath</code> defining the source CIM Instance
	 *            whose referring instances are to be returned. The
	 *            <code>pObjectName</code> shall include the host, object name
	 *            and keys.
	 * @param pResultClass
	 *            This string shall either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the instances returned to
	 *            contain only the instances of this Class name or one of its
	 *            subclasses.
	 * @param pRole
	 *            This string shall either contain a valid Property name or be
	 *            <code>null</code>. The role filters the instances returned to
	 *            contain only instances referring to the source instance via a
	 *            property with the specified name. For example, If "Antecedent"
	 *            is specified, then only Associations in which the source
	 *            instance is the "Antecedent" reference are returned.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property. If <code>true</code>, the class origin
	 *            attribute may be present for each property and on all
	 *            instances returned, even if requested the server may ignore
	 *            the request and not return the class origin. If
	 *            <code>false</code>, the class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the instances returned. Each <code>CIMInstance</code> returned
	 *            shall only contain elements for the properties of the names
	 *            specified. Duplicate and invalid property names are ignored
	 *            and the request is otherwise processed normally. An empty
	 *            array indicates that no properties should be included in the
	 *            instances returned. A <code>null</code> value indicates that
	 *            all properties supported shall be contained in the instance
	 *            returned.
	 * @return If successful, a <code>CloseableIterator</code> referencing zero
	 *         or more <code>CIMInstance</code>s meeting the specified criteria.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes shall be
	 *             returned along with zero or more <code>CIM_Error</code>
	 *             instances. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	CloseableIterator<CIMInstance> referenceInstances(CIMObjectPath pObjectName,
			String pResultClass, String pRole, boolean pIncludeClassOrigin, String[] pPropertyList)
			throws WBEMException;

	/**
	 * Enumerates the <code>CIMObjectPath</code>s of Association Objects that
	 * refer to a particular source CIM Object. If the source Object is a CIM
	 * Class, then a <code>CloseableIterator</code> of
	 * <code>CIMObjectPath</code>s of the Association classes that refer to the
	 * source Object is returned. If the source Object is a CIM Instance, then a
	 * <code>CloseableIterator</code> of <code>CIMObjectPath</code>s of the
	 * <code>CIMInstance</code> objects that refer to the source Object is
	 * returned.
	 * 
	 * @param pObjectName
	 *            <code>CIMObjectPath</code> defining the source CIM Object
	 *            whose referring Objects are to be returned. This argument may
	 *            contain either a Class name or the modelpath of an Instance.
	 *            (i.e. Keys populated)
	 * @param pResultClass
	 *            This string MUST either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only the Objects of this Class name or one of its subclasses.
	 * @param pRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects referring to the source Object via a Property
	 *            with the specified name. If "Antecedent" is specified, then
	 *            only Associations in which the source Object is the
	 *            "Antecedent" reference are returned.
	 * @return If successful, a <code>CloseableIterator</code> referencing zero
	 *         or more <code>CIMObjectPath</code>s of <code>CIMClass</code>es or
	 *         <code>CIMInstance</code>s meeting the specified criteria.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public CloseableIterator<CIMObjectPath> referenceNames(CIMObjectPath pObjectName,
			String pResultClass, String pRole) throws WBEMException;

	/**
	 * <code>referencePaths</code> shall start an enumeration session for
	 * association instances that have references that refer to the instance
	 * defined in the <code>pInstancePath</code> parameter and return zero or
	 * more <code>CIMObjectPath</code> objects.
	 * 
	 * @param pInstancePath
	 *            The <code>CIMObjectPath</code> for the instance for which the
	 *            enumeration is to be performed.
	 * @param pResultClass
	 *            This string MUST either contain a valid CIM Class name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only the Objects of this Class name or one of its subclasses.
	 * @param pRole
	 *            This string MUST either contain a valid Property name or be
	 *            <code>null</code>. It filters the Objects returned to contain
	 *            only Objects referring to the source Object via a Property
	 *            with the specified name. If "Antecedent" is specified, then
	 *            only Associations in which the source Object is the
	 *            "Antecedent" reference are returned.
	 * @param pFilterQueryLanguage
	 *            The <code>pFilterQueryLanguage</code> represents the query
	 *            language for the <code>pFilterQuery</code> argument. This must
	 *            be left <code>null</code> if a <code>pFilterQuery</code> is
	 *            not supplied. If the implementation does not support the query
	 *            language specified, the
	 *            <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error shall
	 *            be returned. If the implementation does not support filtered
	 *            enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pFilterQuery
	 *            The <code>pFilterQuery</code> specifies a query in the form of
	 *            the query language specified by the
	 *            <code>pFilterQueryLanguage</code> parameter. If this value is
	 *            not <code>null</code>, the <code>pFilterQueryLanguage</code>
	 *            parameter must be non-<code>null</code>. This value shall act
	 *            as an additional filter on the result set. If the
	 *            implementation does not support the query language specified,
	 *            the <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error
	 *            shall be returned. If the implementation does not support
	 *            filtered enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pTimeout
	 *            This input parameter determines the minimum time the CIM
	 *            server shall maintain the open enumeration session after the
	 *            last Open or Pull operation (unless the enumeration session is
	 *            closed). If the operation timeout is exceeded, the
	 *            implementation may close the enumeration session at any time,
	 *            releasing any resources allocated to the enumeration session.
	 *            A <code>pTimeout</code> of 0 means that there is no operation
	 *            timeout. That is, the enumeration session is never closed
	 *            based on time. If <code>pTimeout</code> is <code>null</code>,
	 *            the implementation shall choose an operation timeout. All
	 *            other values for <code>pTimeout</code> specify the operation
	 *            timeout in seconds. A implementation may restrict the set of
	 *            allowable values for <code>pTimeout</code>. Specifically, the
	 *            implementation may not allow 0 (no timeout). If the specified
	 *            value is not an allowable value, the implementation shall
	 *            return failure with the status code
	 *            <code>CIM_ERR_INVALID_OPERATION_TIMEOUT</code>.
	 * @param pContinueOnError
	 *            If <code>true</code>, requests that the operation resume when
	 *            an error is received. If a implementation does not support
	 *            continuation on error and <code>pContinueOnError</code> is
	 *            <code>true</code>, it shall throw a <code>WBEMException</code>
	 *            with the status code
	 *            <code>CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED</code>. If a
	 *            implementation supports continuation on error and
	 *            <code>pContinueOnError</code> is <code>true</code>, the
	 *            enumeration session shall remain open when a Pull operation
	 *            fails, and any subsequent successful Pull operations shall
	 *            return the set of elements that would have been returned if
	 *            the failing Pull operations were successful. This behavior is
	 *            subject to the consistency rules defined for pulled
	 *            enumerations. If <code>pContinueOnError</code> is
	 *            <code>false</code>, the enumeration session shall be closed
	 *            when either the operation completes successfully or when a
	 *            <code>WBEMExcetpion</code> is thrown.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @return The return value of a successful Open operation is an array of
	 *         enumerated elements with a number of entries from 0 up to a
	 *         maximum defined by <code>pMaxObjects</code>. These entries meet
	 *         the criteria defined in the Open operation. Note that returning
	 *         no entries in the array does not imply that the enumeration
	 *         session is exhausted. Client must evaluate the
	 *         <code>EnumerateResponse.isEnd()</code> to determine if there are
	 *         more elements.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 * 
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_OPERATION_TIMEOUT
	 *      CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_NOT_FOUND (the source instance was not found)
	 *      CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_QUERY
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMObjectPath> referencePaths(CIMObjectPath pInstancePath,
			String pResultClass, String pRole, String pFilterQueryLanguage, String pFilterQuery,
			UnsignedInteger32 pTimeout, boolean pContinueOnError, UnsignedInteger32 pMaxObjects)
			throws WBEMException;

	/**
	 * <code>references</code> shall start an enumeration session for
	 * association instances that have references that refer to the instance
	 * defined in the <code>pInstancePath</code> parameter and return zero or
	 * more <code>CIMInstance</code> objects.
	 * 
	 * @param pInstancePath
	 *            The <code>CIMObjectPath</code> for the instance for which the
	 *            enumeration is to be performed.
	 * @param pResultClass
	 *            This string shall either contain a CIM Class name or be
	 *            <code>null</code>. It filters the instances returned to
	 *            contain only the instances of this Class name or one of its
	 *            subclasses.
	 * @param pRole
	 *            This string shall either contain a Property name or be
	 *            <code>null</code>. It filters the instances returned to
	 *            contain only instances referring to the source instance via a
	 *            Property with the specified name. If "Antecedent" is
	 *            specified, then only Associations in which the source instance
	 *            is the "Antecedent" reference are returned.
	 * @param pIncludeClassOrigin
	 *            The class origin attribute is the name of the class that first
	 *            defined the property. If <code>true</code>, the class origin
	 *            attribute may be present for each property on all instances
	 *            returned, even if requested the server may ignore the request
	 *            and not return the class origin. If <code>false</code>, the
	 *            class origin shall not be present.
	 * @param pPropertyList
	 *            An array of property names used to filter what is contained in
	 *            the instances returned. Each <code>CIMInstance</code> returned
	 *            only contains elements for the properties of the names
	 *            specified. Duplicate and invalid property names are ignored
	 *            and the request is otherwise processed normally. An empty
	 *            array indicates that no properties should be included in the
	 *            Objects returned. A <code>null</code> value indicates that all
	 *            non-null properties of the instance are included.
	 * @param pFilterQueryLanguage
	 *            The <code>pFilterQueryLanguage</code> represents the query
	 *            language for the <code>pFilterQuery</code> argument. This must
	 *            be left <code>null</code> if a <code>pFilterQuery</code> is
	 *            not supplied. If the implementation does not support the query
	 *            language specified, the
	 *            <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error shall
	 *            be returned. If the implementation does not support filtered
	 *            enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pFilterQuery
	 *            The <code>pFilterQuery</code> specifies a query in the form of
	 *            the query language specified by the
	 *            <code>pFilterQueryLanguage</code> parameter. If this value is
	 *            not <code>null</code>, the <code>pFilterQueryLanguage</code>
	 *            parameter must be non-<code>null</code>. This value shall act
	 *            as an additional filter on the result set. If the
	 *            implementation does not support the query language specified,
	 *            the <code>CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED</code> error
	 *            shall be returned. If the implementation does not support
	 *            filtered enumerations, the
	 *            <code>CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED</code> error
	 *            shall be returned.
	 * @param pTimeout
	 *            This input parameter determines the minimum time the CIM
	 *            server shall maintain the open enumeration session after the
	 *            last Open or Pull operation (unless the enumeration session is
	 *            closed). If the operation timeout is exceeded, the
	 *            implementation may close the enumeration session at any time,
	 *            releasing any resources allocated to the enumeration session.
	 *            A <code>pTimeout</code> of 0 means that there is no operation
	 *            timeout. That is, the enumeration session is never closed
	 *            based on time. If <code>pTimeout</code> is <code>null</code>,
	 *            the implementation shall choose an operation timeout. All
	 *            other values for <code>pTimeout</code> specify the operation
	 *            timeout in seconds. A implementation may restrict the set of
	 *            allowable values for <code>pTimeout</code>. Specifically, the
	 *            implementation may not allow 0 (no timeout). If the specified
	 *            value is not an allowable value, the implementation shall
	 *            return failure with the status code
	 *            <code>CIM_ERR_INVALID_OPERATION_TIMEOUT</code>.
	 * @param pContinueOnError
	 *            If <code>true</code>, requests that the operation resume when
	 *            an error is received. If a implementation does not support
	 *            continuation on error and <code>pContinueOnError</code> is
	 *            <code>true</code>, it shall throw a <code>WBEMException</code>
	 *            with the status code
	 *            <code>CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED</code>. If a
	 *            implementation supports continuation on error and
	 *            <code>pContinueOnError</code> is <code>true</code>, the
	 *            enumeration session shall remain open when a Pull operation
	 *            fails, and any subsequent successful Pull operations shall
	 *            return the set of elements that would have been returned if
	 *            the failing Pull operations were successful. This behavior is
	 *            subject to the consistency rules defined for pulled
	 *            enumerations. If <code>pContinueOnError</code> is
	 *            <code>false</code>, the enumeration session shall be closed
	 *            when either the operation completes successfully or when a
	 *            <code>WBEMExcetpion</code> is thrown.
	 * @param pMaxObjects
	 *            Defines the maximum number of elements that this Open
	 *            operation can return. The implementation may deliver any
	 *            number of elements up to <code>pMaxObjects</code> but shall
	 *            not deliver more than <code>pMaxObjects</code> elements. An
	 *            implementation may choose to never return any elements during
	 *            an Open operation, regardless of the value of
	 *            <code>pMaxObjects</code>. Note that a CIM client can use a
	 *            <code>pMaxObjects</code> value of 0 to specify that it does
	 *            not want to retrieve any instances in the Open operation.
	 * @return The return value of a successful Open operation is an array of
	 *         enumerated elements with a number of entries from 0 up to a
	 *         maximum defined by <code>pMaxObjects</code>. These entries meet
	 *         the criteria defined in the Open operation. Note that returning
	 *         no entries in the array does not imply that the enumeration
	 *         session is exhausted. Client must evaluate the
	 *         <code>EnumerateResponse.isEnd()</code> to determine if there are
	 *         more elements.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_SERVER_IS_SHUTTING_DOWN
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_OPERATION_TIMEOUT
	 *      CIM_ERR_CONTINUATION_ON_ERROR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_PARAMETER
	 *      CIM_ERR_NOT_FOUND (the source instance was not found)
	 *      CIM_ERR_FILTERED_ENUMERATION_NOT_SUPPORTED
	 *      CIM_ERR_QUERY_LANGUAGE_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_QUERY
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public EnumerateResponse<CIMInstance> references(CIMObjectPath pInstancePath,
			String pResultClass, String pRole, boolean pIncludeClassOrigin, String[] pPropertyList,
			String pFilterQueryLanguage, String pFilterQuery, UnsignedInteger32 pTimeout,
			boolean pContinueOnError, UnsignedInteger32 pMaxObjects) throws WBEMException;

	/**
	 * Change the locales that were provided during initialization.
	 * 
	 * @param pLocales
	 *            An array of locales in order of priority of preference.
	 */
	public void setLocales(Locale[] pLocales);

	/**
	 * Set properties that enable options or protocol specific properties. See
	 * <code>WBEMClientConstants</code> for a list of standard properties.
	 * 
	 * @param pKey
	 *            The name of the property.
	 * @param pValue
	 *            The value of the property.
	 * @throws IllegalArgumentException
	 *             If the name is not a supported property name.
	 * @see WBEMClientConstants
	 */
	public void setProperty(String pKey, String pValue);

	/**
	 * Add a <code>CIMQualifierType</code> to the specified namespace if it does
	 * not already exist. Otherwise, it modifies the qualifier type to the value
	 * specified.
	 * 
	 * @param pQualifierType
	 *            The CIM qualifier type to be added.
	 * @throws UnsupportedOperationException
	 *             If the client implementation (or protocol) does not support
	 *             the operation.
	 * @throws WBEMException
	 *             If unsuccessful, one of the following status codes must be
	 *             returned. The ORDERED list is:
	 * 
	 *             <pre>
	 *      CIM_ERR_ACCESS_DENIED
	 *      CIM_ERR_NOT_SUPPORTED
	 *      CIM_ERR_INVALID_NAMESPACE
	 *      CIM_ERR_INVALID_PARAMETER (including missing, duplicate, unrecognized 
	 *            or otherwise incorrect parameters)
	 *      CIM_ERR_FAILED (some other unspecified error occurred)
	 * </pre>
	 */
	public void setQualifierType(CIMQualifierType<?> pQualifierType) throws WBEMException;
}
