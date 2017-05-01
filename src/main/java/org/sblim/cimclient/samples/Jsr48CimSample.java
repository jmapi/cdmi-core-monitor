/**
 * (C) Copyright IBM Corp. 2006, 2009
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
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-14  lupusalex    Make SBLIM client JSR48 compliant
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 */
package org.sblim.cimclient.samples;

import java.util.Arrays;

import javax.cim.CIMClass;
import javax.cim.CIMClassProperty;
import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.CIMQualifier;

/**
 * This class is an example how to extract the data from the javax.wbem.cim.*
 * classes. The string representations are similar to the MOF format, but not
 * strictly compliant. It is really only "cheap" example code.
 */
public abstract class Jsr48CimSample {

	/**
	 * toMof
	 * 
	 * @param pInstance
	 * @return String pInstance
	 */
	public static String toMof(CIMInstance pInstance) {
		final StringBuffer result = new StringBuffer();
		result.append("// path=");
		result.append(toMof(pInstance.getObjectPath()));
		result.append("\n");
		result.append("Instance of ");
		result.append(pInstance.getClassName());
		result.append(" {\n");
		final CIMProperty<?>[] properties = pInstance.getProperties();
		result.append(toMof(properties));
		result.append("}\n");
		return result.toString();
	}

	/**
	 * toMof
	 * 
	 * @param pProperties
	 * @return String pProperties
	 */
	public static String toMof(CIMProperty<?>[] pProperties) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < pProperties.length; ++i) {
			result.append(toMof(pProperties[i]));
			result.append(";\n");
		}
		return result.toString();
	}

	/**
	 * toMof
	 * 
	 * @param pObjectPath
	 * @return String CIMObjectPath
	 */
	public static String toMof(CIMObjectPath pObjectPath) {
		final StringBuffer result = new StringBuffer();
		if (pObjectPath.getScheme() != null) {
			result.append(pObjectPath.getScheme());
			result.append("://");
		}
		if (pObjectPath.getHost() != null) {
			result.append(pObjectPath.getHost());
			if (pObjectPath.getPort() != null) {
				result.append(":");
				result.append(pObjectPath.getPort());
			}
			result.append("/");
		}
		if (pObjectPath.getNamespace() != null) {
			result.append(pObjectPath.getNamespace());
			result.append(":");
		}
		result.append(pObjectPath.getObjectName());
		final CIMProperty<?>[] keys = pObjectPath.getKeys();
		for (int i = 0; i < keys.length; ++i) {
			result.append(i == 0 ? "." : ",");
			result.append(keys[i].getName());
			result.append("=\"");
			result.append(keys[i].getValue());
			result.append("\"");
		}
		return result.toString();
	}

	/**
	 * toMof
	 * 
	 * @param pProperty
	 * @return String CIMProperty
	 */
	public static String toMof(CIMProperty<?> pProperty) {
		final StringBuffer result = new StringBuffer();
		if (pProperty instanceof CIMClassProperty) {
			result.append(toMof(((CIMClassProperty<?>) pProperty).getQualifiers()));
		}
		result.append(pProperty.getDataType().toString());
		result.append(" ");
		result.append(pProperty.getName());
		if (pProperty.getValue() != null) {
			result.append(" = ");
			if (pProperty.getDataType().isArray()) {
				result.append(Arrays.asList((Object[]) pProperty.getValue()));
			} else {
				result.append(String.valueOf(pProperty.getValue()));
			}
		}
		return result.toString();
	}

	/**
	 * toMof
	 * 
	 * @param pClass
	 * @return String CIMClass
	 */
	public static String toMof(CIMClass pClass) {
		final StringBuffer result = new StringBuffer();
		final CIMQualifier<?>[] qualifiers = pClass.getQualifiers();
		result.append(toMof(qualifiers));
		result.append("class ");
		result.append(pClass.getName());
		if (pClass.getSuperClassName() != null) {
			result.append(" : ");
			result.append(pClass.getSuperClassName());
		}
		result.append(" {\n");
		final CIMClassProperty<?>[] properties = pClass.getProperties();
		result.append(toMof(properties));
		result.append("}\n");
		return result.toString();
	}

	/**
	 * toMof
	 * 
	 * @param qualifiers
	 * @return String CIMQualifier[]
	 */
	public static String toMof(CIMQualifier<?>[] qualifiers) {
		final StringBuffer result = new StringBuffer();
		result.append("[");
		for (int i = 0; i < qualifiers.length; ++i) {
			final CIMQualifier<?> qualifier = qualifiers[i];
			if (i > 0) {
				result.append(",");
			}
			result.append(toMof(qualifier));
		}
		result.append("]\n");
		return result.toString();
	}

	/**
	 * toMof
	 * 
	 * @param pQualifier
	 * @return String CIMQualifier
	 */
	public static String toMof(CIMQualifier<?> pQualifier) {
		final StringBuffer result = new StringBuffer();
		result.append(pQualifier.getName());
		result.append("(");
		if (pQualifier.getDataType().equals(CIMDataType.STRING_T)) {
			result.append('"');
			String value = (String) pQualifier.getValue();
			value = value.replaceAll("\"", "\\\"");
			result.append(value.length() < 15 ? value : value.substring(0, 15) + "...");
			result.append('"');
		} else if (pQualifier.getDataType().isArray()) {
			result.append(Arrays.asList((Object[]) pQualifier.getValue()));
		} else {
			result.append(pQualifier.getValue());
		}
		result.append(")");
		return result.toString();
	}
}
