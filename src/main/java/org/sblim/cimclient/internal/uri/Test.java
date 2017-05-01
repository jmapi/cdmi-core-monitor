/**
 * (C) Copyright IBM Corp. 2006, 2011
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
 * 1565892    2006-11-05  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3194680    2011-02-28  blaschke-oss Error in numeric keys
 */

package org.sblim.cimclient.internal.uri;

import org.sblim.cimclient.internal.util.Util;

/**
 * Class Test is responsible for testing the WBEM-URI String parsing.
 */
public class Test {

	private static String cUriArray[] = {

			"//www.acme.com/root/cimv2",

			"//www.acme.com/root/cimv2:CIM_RegisteredProfile",

			"https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile",

			"https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile."
					+ "Cool=\"Hey! It's a string\"",

			"https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile.ThisShouldBeAnInt=0",

			"https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile."
					+ "Cool=\"Hey! It's a string\","
					+ "Yeah=\"Gee! \\\"It's an embedded string\\\"\"",

			"https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile."
			// +
					// "SelfRef=\"https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile\"",
					+ "InstRef=\"CIM_RegisteredProfile.IntKey=10\"",
			"https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile."
					+ "Yeah="
					+ Util.quote("Gee! " + Util.quote("It's an embedded string"))
					+ ",Ref="
					+ Util.quote(// "https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile."
							"root/cimv2:CIM_RegisteredProfile." + "Yeah="
									+ Util.quote("Gee! " + Util.quote("It's an embedded string"))),

			"https://jdd:test@64.202.189.170:5959/cimv2:CIM_RegisteredProfile."
					+ "Yeah="
					+ Util.quote("Gee! " + Util.quote("It's an embedded string"))
					+ ",Ref="
					+ Util.quote(// "https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile."
							"CIM_RegisteredProfile." + "Yeah="
									+ Util.quote("Gee! " + Util.quote("It's an embedded string"))),

			"https://64.202.189.170:5959/cimv2:CIM_RegisteredProfile."
					+ "Yeah="
					+ Util.quote("Gee! " + Util.quote("It's an embedded string"))
					+ ",Ref="
					+ Util.quote(// "https://jdd:test@acme.com:5959/cimv2:CIM_RegisteredProfile."
							"CIM_RegisteredProfile." + "Yeah="
									+ Util.quote("Gee! " + Util.quote("It's an embedded string"))),

			"https://64.202.189.170:5959/cimv2:CIM_RegisteredProfile."
					+ "Yeah="
					+ Util.quote("Gee! " + Util.quote("It's an embedded string"))
					+ ",Ref="
					+ Util.quote("CIM_RegisteredProfile." + "Yeah="
							+ Util.quote("Gee! " + Util.quote("It's an embedded string")))
					+ ",CharKey0='a'" + ",CharKey1='\\x32'" + ",boolKeyT=True" + ",boolKeyF=False"
					+ ",binValP=01011010b" + ",binValN=-01011010b" + ",octValP=0644"
					+ ",octValN=-0644" + ",decValP=1848" + ",decValN=-1848" + ",hexValP=0x55aa"
					+ ",hexValN=-0x55aa" + ",realValP=+23.566e-23" + ",realValN=-23.566e+23"
					+ ",dtAbs=" + Util.quote("20061027112135.600123+001") + ",dtInv="
					+ Util.quote("00000042123625.600123:000"),

			/*
			 * Typed URI strings
			 */
			"https://64.202.189.170:5959/cimv2/(instance)CIM_RegisteredProfile."
					+ "Yeah=(string)"
					+ Util.quote("Gee! " + Util.quote("It's an embedded string"))
					+ ",uInt32=(uint32)100000"
					+ ",Real32=(real32)3.14"
					+ ",dtAbs=(datetime)"
					+ Util.quote("20061027112135.600123+001")
					+ ",dtInv=(datetime)"
					+ Util.quote("00000042123625.600123:000")
					+ ",Bool=(boolean)true"
					+ ",Char=(char16)'x'"
					+ ",Ref=(reference)"
					+ Util.quote("/(instance)CIM_RegisteredProfile." + "Yeah=(string)"
							+ Util.quote("Gee! " + Util.quote("It's an embedded string"))
							+ ",ClassRef=(reference)"
							+ Util.quote("/(instance)CIM_Gyurcsany.Key=(uint8)10"))
					+ ",RefWithNS=(reference)"
					+ Util.quote("root/cimv2/(instance)CIM_Orban.Key=(uint8)42")

	};

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		for (int i = 0; i < cUriArray.length; i++) {
			System.out.println("\nparsing:\n" + cUriArray[i]);
			URI uri = URI.parse(cUriArray[i]);
			if (uri == null) {
				System.out.println("parsing failed!");
				continue;
			}
			System.out.println("class:\n" + uri.getClass().getName());
			System.out.println("refactored string:\n" + uri.toString());

			System.out.println("namespace name : " + uri.getNamespaceName());
			System.out.println("namespace type : " + uri.getNamespaceType());
			System.out.println("user info   : " + uri.getUserInfo());
			System.out.println("host name   : " + uri.getHost());
			System.out.println("port        : " + uri.getPort());
			System.out.println("class name  : " + uri.getClassName());
			KeyValuePairs pairs = uri.getKeyValuePairs();
			if (pairs == null) continue;
			for (int j = 0; j < pairs.size(); j++) {
				KeyValuePair pair = (KeyValuePair) pairs.elementAt(j);
				Value value = pair.getValue();
				System.out.println("key : " + pair.getKey() + "\n  " + getTypeStr(value) + " : "
						+ pair.getValue().toString() + " " + getIntProps(value));
			}
		}
	}

	private static String getTypeStr(Value pVal) {
		if (pVal instanceof ReferenceValue) return "refValue";
		else if (pVal instanceof StringValue) return "strValue";
		else if (pVal instanceof CharValue) return "chrValue";
		else if (pVal instanceof BooleanValue) return "boolValue";
		else if (pVal instanceof DateTimeValue) return "dateTimeVal";
		else if (pVal instanceof IntegerValue) return "intValue";
		else if (pVal instanceof RealValue) return "realValue";
		return pVal.getClass().getName();
	}

	private static String getIntProps(Value pVal) {
		if (!(pVal instanceof IntegerValue)) return "";
		IntegerValue intVal = (IntegerValue) pVal;
		return "sigend=" + intVal.isSigned() + ", bitWidth=" + intVal.getBitWidth();
	}

}
