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
 * @author : Endre Bak, ebak@de.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 1716991    2006-05-11  lupusalex    FVT: CIMObjectPath.equals() should ignore host name
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 1917321    2008-05-29  rgummada     javadoc update to constructors with 2 and more parms
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2845128    2009-09-24  blaschke-oss CIMObjectPath.toString() misses host
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944824    2010-02-08  blaschke-oss Missing getXmlSchemaName() in CIMObjectPath
 * 2975975    2010-03-24  blaschke-oss TCK: CIMObjectPath(String) does not handle null
 * 3023141    2010-07-01  blaschke-oss CIMObjectPath uses # constructor instead of valueOf
 * 3496349    2012-03-02  blaschke-oss JSR48 1.0.0: add CIMObjectPath getKeyValue
 * 3510090    2012-03-23  blaschke-oss Fix CIMObjectPath.toString() inconsistencies
 * 3513343    2012-03-31  blaschke-oss TCK: CIMObjectPath must validate XML schema name
 * 3513347    2012-03-31  blaschke-oss TCK: CIMObjectPath allows empty string
 * 3521131    2012-04-24  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final II
 * 3521119    2012-04-24  blaschke-oss JSR48 1.0.0: remove CIMObjectPath 2/3/4-parm ctors
 * 3529151    2012-08-22  blaschke-oss TCK: CIMInstance property APIs include keys from COP
 *    2660    2013-09-04  blaschke-oss CIMObjectPath.equalsModelPath same as equals
 *    2716    2013-12-11  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final V
 */

package javax.cim;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.sblim.cimclient.internal.cim.CIMElementSorter;
import org.sblim.cimclient.internal.uri.BooleanValue;
import org.sblim.cimclient.internal.uri.CharValue;
import org.sblim.cimclient.internal.uri.DateTimeValue;
import org.sblim.cimclient.internal.uri.IntegerValue;
import org.sblim.cimclient.internal.uri.KeyValuePair;
import org.sblim.cimclient.internal.uri.KeyValuePairs;
import org.sblim.cimclient.internal.uri.RealValue;
import org.sblim.cimclient.internal.uri.ReferenceValue;
import org.sblim.cimclient.internal.uri.StringValue;
import org.sblim.cimclient.internal.uri.URI;
import org.sblim.cimclient.internal.uri.URIString;
import org.sblim.cimclient.internal.uri.Value;
import org.sblim.cimclient.internal.util.MOF;

//Sync'd against JSR48 1.0.0 javadoc (version 1.7.0_03) on Tue Dec 10 07:02:50 EST 2013
/**
 * This class represents the CIM Object Path as defined by the Distributed
 * Management Task Force (<a href=http://www.dmtf.org>DMTF</a>) CIM
 * Infrastructure Specification (<a href=
 * "http://dmtf.org/sites/default/files/standards/documents/DSP0004_2.7.0.pdf"
 * >DSP004</a>). In order to uniquely identify a given object, a CIM object path
 * includes the host, namespace, object name and keys (if the object is an
 * instance).<br>
 * <br>
 * For example, the object path:<br>
 * <br>
 * <code>
 * http://myserver/root/cimv2:My_ComputerSystem.Name=mycomputer,
 * CreationClassName=My_ComputerSystem
 * </code><br>
 * <br>
 * has two parts:<br>
 * <br>
 * <ul type="disc"> <li>Namespace Path</li>
 * <code>http://myserver/root/cimv2</code><br>
 * JSR48 defines the namespace path to include the scheme, host, port (optional)
 * and namespace<br>
 * The example specifies the <code>"root/cimv2"</code> namespace on the host
 * <code>myserver</code>.</li> <li>Model Path</li>
 * <code>My_ComputerSystem.Name=mycomputer,CreationClassName=My_ComputerSystem
 * 		</code><br>
 * DSP0004 defines the model path for a class or qualifier type as the name of
 * the class/qualifier type<br>
 * DSP0004 defines the model path for an instance as the class
 * name.(key=value),*<br>
 * The example specifies an instance for the class
 * <code>My_ComputerSystem</code> which is uniquely identified by two key
 * properties and values: <ul type="disc"> <li><code>Name=mycomputer</code></li>
 * <li>
 * <code>CreationClassName=My_ComputerSystem</code></li> </ul> </ul>
 */
public class CIMObjectPath extends Object implements Serializable {

	private static final long serialVersionUID = 4593259690658425064L;

	private String iScheme, iHost, iPort, iNamespace, iObjectName, iXmlSchemaName;

	private CIMProperty<?>[] iKeys;

	/**
	 * Class TypeValuePair represents a type-value pair with special
	 * identification functionality for integer and real numbers.
	 * 
	 */
	private static class TypeValuePair {

		private CIMDataType iType;

		private Object iValue;

		/**
		 * Constructs a type-value pair with the specified type and value.
		 * 
		 * @param pType
		 *            Type.
		 * @param pValue
		 *            Value.
		 */
		public TypeValuePair(CIMDataType pType, Object pValue) {
			this.iType = pType;
			this.iValue = pValue;
		}

		/**
		 * Constructs a type-value pair with the specified integer value.
		 * 
		 * @param intVal
		 *            Integer value.
		 */
		public TypeValuePair(IntegerValue intVal) {
			if (intVal.isSigned()) {
				switch (intVal.getBitWidth()) {
					case 8:
						this.iType = CIMDataType.SINT8_T;
						this.iValue = Byte.valueOf(intVal.byteValue());
						break;
					case 16:
						this.iType = CIMDataType.SINT16_T;
						this.iValue = Short.valueOf(intVal.shortValue());
						break;
					case 32:
						this.iType = CIMDataType.SINT32_T;
						this.iValue = Integer.valueOf(intVal.intValue());
						break;
					default:
						this.iType = CIMDataType.SINT64_T;
						this.iValue = Long.valueOf(intVal.longValue());
				}

			} else { // unsigned integers
				switch (intVal.getBitWidth()) {
					case 8:
						this.iType = CIMDataType.UINT8_T;
						this.iValue = new UnsignedInteger8(intVal.shortValue());
						break;
					case 16:
						this.iType = CIMDataType.UINT16_T;
						this.iValue = new UnsignedInteger16(intVal.intValue());
						break;
					case 32:
						this.iType = CIMDataType.UINT32_T;
						this.iValue = new UnsignedInteger32(intVal.longValue());
						break;
					default:
						this.iType = CIMDataType.UINT64_T;
						this.iValue = new UnsignedInteger64(intVal.bigIntValue());
				}
			}
		}

		/**
		 * Constructs a type-value pair with the specified real value.
		 * 
		 * @param pRealVal
		 *            Real value.
		 */
		public TypeValuePair(RealValue pRealVal) {
			// TODO: handle precision
			if (pRealVal.isDouble()) {
				this.iType = CIMDataType.REAL64_T;
				this.iValue = new Double(pRealVal.doubleValue());
			} else {
				this.iType = CIMDataType.REAL32_T;
				this.iValue = new Float(pRealVal.floatValue());
			}
		}

		/**
		 * Returns the type of the type-value pair.
		 * 
		 * @return Type of type-value pair.
		 */
		public CIMDataType getType() {
			return this.iType;
		}

		/**
		 * Returns the value of the type-value pair.
		 * 
		 * @return Value of type-value pair.
		 */
		public Object getValue() {
			return this.iValue;
		}

	}

	/**
	 * Extracts and returns sorted list of key-value pairs from the URI.
	 * 
	 * @param pURI
	 *            The Uniform Resource Identifier.
	 * @return Sorted array of keys in URI.
	 */
	private CIMProperty<?>[] getKeysFromURI(URI pURI) {
		KeyValuePairs pairs = pURI.getKeyValuePairs();
		if (pairs == null) return null;
		CIMProperty<?>[] keys = new CIMProperty[pairs.size()];
		for (int i = 0; i < pairs.size(); i++) {
			KeyValuePair pair = (KeyValuePair) pairs.elementAt(i);
			String name = pair.getKey();
			Value uriVal = pair.getValue();
			TypeValuePair typeValue;
			if (uriVal instanceof StringValue) {
				typeValue = new TypeValuePair(CIMDataType.STRING_T, uriVal.toString());
			} else if (uriVal instanceof ReferenceValue) {
				ReferenceValue refVal = (ReferenceValue) uriVal;
				CIMObjectPath op = new CIMObjectPath(refVal.getRef());
				typeValue = new TypeValuePair(new CIMDataType(op.getObjectName()), op);
			} else if (uriVal instanceof BooleanValue) {
				typeValue = new TypeValuePair(CIMDataType.BOOLEAN_T, ((BooleanValue) uriVal)
						.getBoolean());
			} else if (uriVal instanceof CharValue) {
				typeValue = new TypeValuePair(CIMDataType.CHAR16_T, ((CharValue) uriVal)
						.getCharacter());
			} else if (uriVal instanceof IntegerValue) {
				typeValue = new TypeValuePair((IntegerValue) uriVal);
			} else if (uriVal instanceof RealValue) {
				typeValue = new TypeValuePair((RealValue) uriVal);
			} else if (uriVal instanceof DateTimeValue) {
				typeValue = new TypeValuePair(CIMDataType.DATETIME_T, ((DateTimeValue) uriVal)
						.getDateTime());
			} else {
				// TODO: error or warning tracing
				typeValue = new TypeValuePair(CIMDataType.INVALID_T, null);
			}
			keys[i] = new CIMProperty<Object>(name, typeValue.getType(), typeValue.getValue(),
					true, false, null);
		}
		return (CIMProperty[]) CIMElementSorter.sort(keys);
	}

	/**
	 * Initializes the elements of the <code>CIMObjectPath</code> from the given
	 * URI.
	 * 
	 * @param pURI
	 *            The Uniform Resource Identifier.
	 */
	private void setURI(URI pURI) {
		this.iNamespace = pURI.getNamespaceName();
		this.iScheme = pURI.getNamespaceType();
		this.iHost = pURI.getHost();
		this.iPort = pURI.getPort();
		this.iObjectName = pURI.getClassName();
		this.iKeys = getKeysFromURI(pURI);
	}

	/**
	 * Constructs a CIM Object Path referencing an instance of the specified CIM
	 * element in the given URI.
	 * 
	 * @param pURI
	 *            The Uniform Resource Identifier.
	 */
	private CIMObjectPath(URI pURI) {
		setURI(pURI);
	}

	/**
	 * Constructs a CIM Object Path referencing a CIM element. The name can
	 * refer to a class name or a qualifier type name, depending on the
	 * particular CIM element identified. In order to refer to an instance, the
	 * key properties and their corresponding values must be set.<br>
	 * <br>
	 * Should be able to handle strings, like:<br>
	 * <code>
	 * http://myserver.org:5066/root/cimv2:My_ComputerSystem.Name="mycmp",CreationClassName="My_ComputerSystem"
	 * <br>
	 * http://myserver.org/root/cimv2:My_ComputerSystem.Name="mycmp",CreationClassName="My_ComputerSystem"
	 * <br>
	 * //myserver.org/root/cimv2:My_ComputerSystem<br>
	 * /root/cimv2:My_ComputerSystem
	 * </code>
	 * 
	 * @param pObjectPath
	 *            The string representation of an object path for a CIM element
	 *            that will be parsed and used to initialize the object.
	 * @throws IllegalArgumentException
	 *             If the <code>pObjectPath</code> is <code>null</code> or an
	 *             empty string.
	 */
	public CIMObjectPath(String pObjectPath) {
		URI uri;

		if (pObjectPath == null) throw new IllegalArgumentException("ObjectPath is null!");
		if (pObjectPath.trim().length() == 0) throw new IllegalArgumentException(
				"ObjectPath is empty!");

		try {
			uri = URI.parse(pObjectPath);
		} catch (IllegalArgumentException asURI) {
			try {
				uri = URI.parseRef(new URIString(pObjectPath), false);
			} catch (IllegalArgumentException asUntypedRef) {
				try {
					uri = URI.parseRef(new URIString(pObjectPath), true);
				} catch (IllegalArgumentException asTypedRef) {
					String msg = "Parsing of ObjectPath string has failed!\n"
							+ "Nested error messages:\n" + "When parsing as normal URI string:\n"
							+ asURI.getMessage() + "When parsing as untyped reference:\n"
							+ asUntypedRef.getMessage() + "When parsing as typed reference:\n"
							+ asTypedRef.getMessage();
					// TODO: tracing
					throw new IllegalArgumentException(msg);
				}
			}
		}
		setURI(uri);
	}

	/**
	 * Constructs a CIM Object Path referencing an instance of the specified CIM
	 * element as defined in the specified namespace on the specified host and
	 * identified by the given key properties and their corresponding values.
	 * Note that the connection mechanism and the port number to which a client
	 * connection is established are also specified.<br>
	 * <br>
	 * NOTE: When using this API against OpenPegasus CIMOM, do not provide the
	 * preceding '/' in the namespace parameter. For example, OpenPegasus will
	 * accept <code>"root/cimv2"</code> as a namespace but will not accept
	 * <code>"/root/cimv2"</code>.
	 * 
	 * @param pScheme
	 *            The connection scheme to the host (e.g. http, https, ...)
	 * @param pHost
	 *            The host name or IP Address.
	 * @param pPort
	 *            The port on the host to which the connection was established.
	 * @param pNamespace
	 *            The namepace in which the CIM element is defined.
	 * @param pObjectName
	 *            The name of the CIM element referenced.
	 * @param pKeys
	 *            The keys and their corresponding values that identify an
	 *            instance of the CIM element.
	 */
	public CIMObjectPath(String pScheme, String pHost, String pPort, String pNamespace,
			String pObjectName, CIMProperty<?>[] pKeys) {
		this.iScheme = pScheme;
		this.iHost = pHost;
		this.iPort = pPort;
		this.iNamespace = pNamespace;
		this.iObjectName = pObjectName;
		if (pKeys != null) {
			for (int i = 0; i < pKeys.length; i++)
				if (!pKeys[i].isKey()) throw new IllegalArgumentException(
						"All CIMObjectPath properties must be keys!");
		}
		this.iKeys = (CIMProperty[]) CIMElementSorter.sort(pKeys);
	}

	/**
	 * Constructs a CIM Object Path referencing an instance of the specified CIM
	 * element as defined in the specified namespace on the specified host and
	 * identified by the given key properties and their corresponding values.
	 * Note that the connection mechanism and the port number to which a client
	 * connection is established are also specified.<br>
	 * <br>
	 * NOTE: When using this API against OpenPegasus CIMOM, do not provide the
	 * preceding '/' in the namespace parameter. For example, OpenPegasus will
	 * accept <code>"root/cimv2"</code> as a namespace but will not accept
	 * <code>"/root/cimv2"</code>.
	 * 
	 * @param pScheme
	 *            The connection scheme to the host (e.g. http, https, ...)
	 * @param pHost
	 *            The host name or IP Address.
	 * @param pPort
	 *            The port on the host to which the connection was established.
	 * @param pNamespace
	 *            The namepace in which the CIM element is defined.
	 * @param pObjectName
	 *            The name of the CIM element referenced.
	 * @param pKeys
	 *            The keys and their corresponding values that identify an
	 *            instance of the CIM element.
	 * @param pXmlSchemaName
	 *            The name of the XML Schema for this object. This is only
	 *            needed for protocols that require this information.
	 */
	public CIMObjectPath(String pScheme, String pHost, String pPort, String pNamespace,
			String pObjectName, CIMProperty<?>[] pKeys, String pXmlSchemaName) {
		this(pScheme, pHost, pPort, pNamespace, pObjectName, pKeys);
		if (pXmlSchemaName != null) {
			try {
				new URL(pXmlSchemaName);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e);
			}
		}
		this.iXmlSchemaName = pXmlSchemaName;
	}

	/**
	 * Compares this CIM object path with the specified CIM object path for
	 * equality.
	 * 
	 * @param pObj
	 *            The object to compare to this CIM object path. Only the model
	 *            paths are compared.
	 * @return <code>true</code> if the specified path references the same
	 *         object, otherwise <code>false</code> is returned.
	 */
	@Override
	public boolean equals(Object pObj) {
		return equalsWorker(pObj, true);
	}

	private boolean equalsWorker(Object pObj, boolean pIncludeNamespacePath) {
		if (!(pObj instanceof CIMObjectPath)) return false;
		CIMObjectPath that = (CIMObjectPath) pObj;
		// hostname information is not any longer part of the comparison, since
		// there is no reliable way to attach hostnames
		if (pIncludeNamespacePath) {
			boolean namespaceEqual = (this.iNamespace == null ? that.iNamespace == null
					: this.iNamespace.equalsIgnoreCase(that.iNamespace));
			if (!namespaceEqual) return false;
		}
		return (this.iObjectName == null ? that.iObjectName == null : this.iObjectName
				.equalsIgnoreCase(that.iObjectName))
				&& keysEqual(that);
	}

	/**
	 * Compares this CIM object path's keys with the specified CIM object path's
	 * keys for equality.<br>
	 * <br>
	 * NOTE: <code>CIMProperty.equals()</code> shouldn't be used for keys,
	 * because the XML doesn't contain originClass and propagated information.
	 * 
	 * @param pThat
	 *            The object path whose keys are to be compared to this CIM
	 *            object path's keys.
	 * @return <code>true</code> if the keys are the same, otherwise
	 *         <code>false</code> is returned.
	 */
	private boolean keysEqual(CIMObjectPath pThat) {
		if (pThat == this) return true;
		if (this.iKeys == null) return pThat.iKeys == null;
		if (pThat.iKeys == null) return false;
		if (this.iKeys.length != pThat.iKeys.length) return false;
		for (int i = 0; i < this.iKeys.length; i++) {
			CIMProperty<?> thisKey = this.iKeys[i], thatKey = pThat.iKeys[i];
			if (!equals(thisKey, thatKey)) { return false; }
		}
		return true;
	}

	/**
	 * Compares two properties for equality.
	 * 
	 * @param pThis
	 *            First property.
	 * @param pThat
	 *            Second property.
	 * @return <code>true</code> if the properties are equal, <code>false</code>
	 *         otherwise.
	 */
	private boolean equals(CIMProperty<?> pThis, CIMProperty<?> pThat) {
		if (pThis.getDataType() != null && pThis.getDataType().isArray()) { return ncEqualsIC(pThis
				.getName(), pThat.getName())
				&& ncEquals(pThis.getDataType(), pThat.getDataType())
				&& Arrays.equals((Object[]) pThis.getValue(), (Object[]) pThat.getValue());

		}
		return ncEqualsIC(pThis.getName(), pThat.getName())
				&& ncEquals(pThis.getDataType(), pThat.getDataType())
				&& ncEquals(pThis.getValue(), pThat.getValue());
	}

	/**
	 * Compares two objects for equality.
	 * 
	 * @param pThis
	 *            First object.
	 * @param pThat
	 *            Second object.
	 * @return <code>true</code> if the objects are equal, <code>false</code>
	 *         otherwise.
	 */
	private boolean ncEquals(Object pThis, Object pThat) {
		return pThis == null ? pThat == null : pThis.equals(pThat);
	}

	/**
	 * Compares two strings for equality, ignoring case.
	 * 
	 * @param pThis
	 *            First string.
	 * @param pThat
	 *            Second string.
	 * @return <code>true</code> if the strings are equal, <code>false</code>
	 *         otherwise.
	 */
	private boolean ncEqualsIC(String pThis, String pThat) {
		return pThis == null ? pThat == null : pThis.equalsIgnoreCase(pThat);
	}

	/**
	 * Compares this model path with the specified model path for equality. If
	 * the model path includes references, then the references will also be
	 * compared for the model path (i.e. the namespace part of the object path
	 * will be ignored).
	 * 
	 * @param pModelPath
	 *            The object to compare.
	 * @return <code>true</code> if the specified path references the same
	 *         object, otherwise <code>false</code>.
	 */
	public boolean equalsModelPath(CIMObjectPath pModelPath) {
		return equalsWorker(pModelPath, false);
	}

	/**
	 * Gets the host.
	 * 
	 * @return The name of the host.
	 */
	public String getHost() {
		return this.iHost;
	}

	/**
	 * Gets a key property by name.
	 * 
	 * @param pName
	 *            The name of the key property to retrieve.
	 * @return The <code>CIMProperty</code> with the given name, or
	 *         <code>null</code> if it is not found.
	 */
	public CIMProperty<?> getKey(String pName) {
		return (CIMProperty<?>) CIMElementSorter.find(this.iKeys, pName);
	}

	/**
	 * Gets all key properties.
	 * 
	 * @return The container of key properties.
	 */
	public CIMProperty<?>[] getKeys() {
		return this.iKeys == null ? new CIMProperty[0] : this.iKeys;
	}

	/**
	 * @param pName
	 *            The name of the key property to retrieve.
	 * @return The value of the key property.
	 */
	public Object getKeyValue(String pName) {
		CIMProperty<?> prop = getKey(pName);
		return prop == null ? prop : prop.getValue();
	}

	/**
	 * Gets the namespace.
	 * 
	 * @return The name of the namespace.
	 */
	public String getNamespace() {
		return this.iNamespace;
	}

	/**
	 * Gets the object name. Depending on the type of CIM element referenced,
	 * this may be either a class name or a qualifier type name.
	 * 
	 * @return The name of this CIM element.
	 */
	public String getObjectName() {
		return this.iObjectName;
	}

	/**
	 * Gets the the port on the host to which the connection was established.
	 * 
	 * @return The port on the host.
	 */
	public String getPort() {
		return this.iPort;
	}

	/**
	 * Get the connection scheme.
	 * 
	 * @return The connection scheme (e.g. http, https,...)
	 */
	public String getScheme() {
		return this.iScheme;
	}

	/**
	 * Get the XML Schema for this object (optional).
	 * 
	 * @return The XML Schema name.
	 */
	public String getXmlSchemaName() {
		return this.iXmlSchemaName;
	}

	/**
	 * Computes the hash code for this object path.
	 * 
	 * @return The integer representing the hash code for this object path.
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Returns a <code>String</code> representation of the CIM object path. This
	 * method is intended to be used only for debugging purposes. The format of
	 * the value returned may vary between implementations. The string returned
	 * may be empty but may not be <code>null</code>.
	 * 
	 * @return A string representation of this CIM object path.
	 */
	@Override
	public String toString() {
		return MOF.objectHandle(this, false, false);
	}

}
