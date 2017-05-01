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
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 3498482    2012-03-09  blaschke-oss Red Hat: Possible XML Hash DoS in sblim
 * 3535383    2012-08-01  blaschke-oss HashDoS fix 3498482
 *    2672    2013-09-26  blaschke-oss Remove SIMPLEREQACK support
 *    2690    2013-10-11  blaschke-oss Remove RESPONSEDESTINATION support
 *    2538    2013-11-28  blaschke-oss CR14: Support new CORRELATOR element
 */

package org.sblim.cimclient.internal.cimxml.sax;

import java.util.HashMap;

import org.sblim.cimclient.internal.cimxml.sax.node.CIMNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ClassNameNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ClassNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ClassPathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.CorrelatorNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ErrorNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ExpMethodCallNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ExpMethodResponseNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ExpParamValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.HostNode;
import org.sblim.cimclient.internal.cimxml.sax.node.IMethodCallNode;
import org.sblim.cimclient.internal.cimxml.sax.node.IMethodResponseNode;
import org.sblim.cimclient.internal.cimxml.sax.node.IParamValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.IReturnValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.InstanceNameNode;
import org.sblim.cimclient.internal.cimxml.sax.node.InstanceNode;
import org.sblim.cimclient.internal.cimxml.sax.node.InstancePathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.KeyBindingNode;
import org.sblim.cimclient.internal.cimxml.sax.node.KeyValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.LocalClassPathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.LocalInstancePathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.LocalNameSpacePathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MessageNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MethodCallNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MethodNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MethodResponseNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MultiExpReqNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MultiExpRspNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MultiReqNode;
import org.sblim.cimclient.internal.cimxml.sax.node.MultiRspNode;
import org.sblim.cimclient.internal.cimxml.sax.node.NameSpaceNode;
import org.sblim.cimclient.internal.cimxml.sax.node.NameSpacePathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.Node;
import org.sblim.cimclient.internal.cimxml.sax.node.ObjectPathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ParamValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ParameterArrayNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ParameterNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ParameterRefArrayNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ParameterReferenceNode;
import org.sblim.cimclient.internal.cimxml.sax.node.PropertyArrayNode;
import org.sblim.cimclient.internal.cimxml.sax.node.PropertyNode;
import org.sblim.cimclient.internal.cimxml.sax.node.PropertyReferenceNode;
import org.sblim.cimclient.internal.cimxml.sax.node.QualiDeclNode;
import org.sblim.cimclient.internal.cimxml.sax.node.QualifierNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ReturnValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ScopeNode;
import org.sblim.cimclient.internal.cimxml.sax.node.SimpleExpReqNode;
import org.sblim.cimclient.internal.cimxml.sax.node.SimpleExpRspNode;
import org.sblim.cimclient.internal.cimxml.sax.node.SimpleReqNode;
import org.sblim.cimclient.internal.cimxml.sax.node.SimpleRspNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueArrayNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueInstanceWithPathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueNamedInstanceNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueNullNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueObjectNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueObjectWithLocalPathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueObjectWithPathNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueRefArrayNode;
import org.sblim.cimclient.internal.cimxml.sax.node.ValueReferenceNode;

/**
 * Class NodeFactory is responsible for Node instance construction.
 * 
 */
public class NodeFactory implements NodeConstIf {

	/**
	 * getNodeInstance
	 * 
	 * @param pNodeName
	 *            Should be an XML element name constant which is defined in
	 *            NodeConstIf
	 * @return the Node subclass which implements the parsing of pNodeName named
	 *         XML Element.
	 */
	public static Node getNodeInstance(String pNodeName) {
		createParserMap();
		return cParserMap.get(pNodeName).create();
	}

	/**
	 * getEnum
	 * 
	 * @param pNodeName
	 * @return The corresponding String constant for an XML element name. It
	 *         must be used, because the Node subclasses use reference based
	 *         equals comparisons (==).
	 */
	public static String getEnum(String pNodeName) {
		return NODENAME_HASH.get(pNodeName);
	}

	private static HashMap<String, FactoryEntry> cParserMap;

	private interface FactoryEntry {

		/**
		 * create
		 * 
		 * @return Node
		 */
		public Node create();
	}

	private synchronized static void createParserMap() {
		if (cParserMap != null) return;
		cParserMap = new HashMap<String, FactoryEntry>();
		cParserMap.put(CIM, new FactoryEntry() {

			public Node create() {
				return new CIMNode();
			}
		});
		// DECLARATION - not implemented
		// DECLGROUP - not implemented
		// DECLGROUP.WITHNAME - not implemented
		// DECLGROUP.WITHPATH - not implemented
		cParserMap.put(QUALIFIER_DECLARATION, new FactoryEntry() {

			public Node create() {
				return new QualiDeclNode();
			}
		});
		cParserMap.put(SCOPE, new FactoryEntry() {

			public Node create() {
				return new ScopeNode();
			}
		});
		cParserMap.put(VALUE, new FactoryEntry() {

			public Node create() {
				return new ValueNode();
			}
		});
		cParserMap.put(VALUE_ARRAY, new FactoryEntry() {

			public Node create() {
				return new ValueArrayNode();
			}
		});
		cParserMap.put(VALUE_REFERENCE, new FactoryEntry() {

			public Node create() {
				return new ValueReferenceNode();
			}
		});
		cParserMap.put(VALUE_REFARRAY, new FactoryEntry() {

			public Node create() {
				return new ValueRefArrayNode();
			}
		});
		cParserMap.put(VALUE_OBJECT, new FactoryEntry() {

			public Node create() {
				return new ValueObjectNode();
			}
		});
		cParserMap.put(VALUE_NAMEDINSTANCE, new FactoryEntry() {

			public Node create() {
				return new ValueNamedInstanceNode();
			}
		});
		cParserMap.put(VALUE_OBJECTWITHLOCALPATH, new FactoryEntry() {

			public Node create() {
				return new ValueObjectWithLocalPathNode();
			}
		});
		cParserMap.put(VALUE_OBJECTWITHPATH, new FactoryEntry() {

			public Node create() {
				return new ValueObjectWithPathNode();
			}
		});
		cParserMap.put(VALUE_NULL, new FactoryEntry() {

			public Node create() {
				return new ValueNullNode();
			}
		});
		cParserMap.put(VALUE_INSTANCEWITHPATH, new FactoryEntry() {

			public Node create() {
				return new ValueInstanceWithPathNode();
			}
		});
		cParserMap.put(NAMESPACEPATH, new FactoryEntry() {

			public Node create() {
				return new NameSpacePathNode();
			}
		});
		cParserMap.put(LOCALNAMESPACEPATH, new FactoryEntry() {

			public Node create() {
				return new LocalNameSpacePathNode();
			}
		});
		cParserMap.put(HOST, new FactoryEntry() {

			public Node create() {
				return new HostNode();
			}
		});
		cParserMap.put(NAMESPACE, new FactoryEntry() {

			public Node create() {
				return new NameSpaceNode();
			}
		});
		cParserMap.put(CLASSPATH, new FactoryEntry() {

			public Node create() {
				return new ClassPathNode();
			}
		});
		cParserMap.put(LOCALCLASSPATH, new FactoryEntry() {

			public Node create() {
				return new LocalClassPathNode();
			}
		});
		cParserMap.put(CLASSNAME, new FactoryEntry() {

			public Node create() {
				return new ClassNameNode();
			}
		});
		cParserMap.put(INSTANCEPATH, new FactoryEntry() {

			public Node create() {
				return new InstancePathNode();
			}
		});
		cParserMap.put(LOCALINSTANCEPATH, new FactoryEntry() {

			public Node create() {
				return new LocalInstancePathNode();
			}
		});
		cParserMap.put(INSTANCENAME, new FactoryEntry() {

			public Node create() {
				return new InstanceNameNode();
			}
		});
		cParserMap.put(OBJECTPATH, new FactoryEntry() {

			public Node create() {
				return new ObjectPathNode();
			}
		});
		cParserMap.put(KEYBINDING, new FactoryEntry() {

			public Node create() {
				return new KeyBindingNode();
			}
		});
		cParserMap.put(KEYVALUE, new FactoryEntry() {

			public Node create() {
				return new KeyValueNode();
			}
		});
		cParserMap.put(CLASS, new FactoryEntry() {

			public Node create() {
				return new ClassNode();
			}
		});
		cParserMap.put(INSTANCE, new FactoryEntry() {

			public Node create() {
				return new InstanceNode();
			}
		});
		cParserMap.put(QUALIFIER, new FactoryEntry() {

			public Node create() {
				return new QualifierNode();
			}
		});
		cParserMap.put(PROPERTY, new FactoryEntry() {

			public Node create() {
				return new PropertyNode();
			}
		});
		cParserMap.put(PROPERTY_ARRAY, new FactoryEntry() {

			public Node create() {
				return new PropertyArrayNode();
			}
		});
		cParserMap.put(PROPERTY_REFERENCE, new FactoryEntry() {

			public Node create() {
				return new PropertyReferenceNode();
			}
		});
		cParserMap.put(METHOD, new FactoryEntry() {

			public Node create() {
				return new MethodNode();
			}
		});
		cParserMap.put(PARAMETER, new FactoryEntry() {

			public Node create() {
				return new ParameterNode();
			}
		});
		cParserMap.put(PARAMETER_REFERENCE, new FactoryEntry() {

			public Node create() {
				return new ParameterReferenceNode();
			}
		});
		cParserMap.put(PARAMETER_ARRAY, new FactoryEntry() {

			public Node create() {
				return new ParameterArrayNode();
			}
		});
		cParserMap.put(PARAMETER_REFARRAY, new FactoryEntry() {

			public Node create() {
				return new ParameterRefArrayNode();
			}
		});
		cParserMap.put(MESSAGE, new FactoryEntry() {

			public Node create() {
				return new MessageNode();
			}
		});
		cParserMap.put(MULTIREQ, new FactoryEntry() {

			public Node create() {
				return new MultiReqNode();
			}
		});
		cParserMap.put(MULTIEXPREQ, new FactoryEntry() {

			public Node create() {
				return new MultiExpReqNode();
			}
		});
		cParserMap.put(SIMPLEREQ, new FactoryEntry() {

			public Node create() {
				return new SimpleReqNode();
			}
		});
		cParserMap.put(SIMPLEEXPREQ, new FactoryEntry() {

			public Node create() {
				return new SimpleExpReqNode();
			}
		});
		cParserMap.put(IMETHODCALL, new FactoryEntry() {

			public Node create() {
				return new IMethodCallNode();
			}
		});
		cParserMap.put(METHODCALL, new FactoryEntry() {

			public Node create() {
				return new MethodCallNode();
			}
		});
		cParserMap.put(EXPMETHODCALL, new FactoryEntry() {

			public Node create() {
				return new ExpMethodCallNode();
			}
		});
		cParserMap.put(PARAMVALUE, new FactoryEntry() {

			public Node create() {
				return new ParamValueNode();
			}
		});
		cParserMap.put(IPARAMVALUE, new FactoryEntry() {

			public Node create() {
				return new IParamValueNode();
			}
		});
		cParserMap.put(EXPPARAMVALUE, new FactoryEntry() {

			public Node create() {
				return new ExpParamValueNode();
			}
		});
		cParserMap.put(MULTIRSP, new FactoryEntry() {

			public Node create() {
				return new MultiRspNode();
			}
		});
		cParserMap.put(MULTIEXPRSP, new FactoryEntry() {

			public Node create() {
				return new MultiExpRspNode();
			}
		});
		cParserMap.put(SIMPLERSP, new FactoryEntry() {

			public Node create() {
				return new SimpleRspNode();
			}
		});
		cParserMap.put(SIMPLEEXPRSP, new FactoryEntry() {

			public Node create() {
				return new SimpleExpRspNode();
			}
		});
		cParserMap.put(METHODRESPONSE, new FactoryEntry() {

			public Node create() {
				return new MethodResponseNode();
			}
		});
		cParserMap.put(EXPMETHODRESPONSE, new FactoryEntry() {

			public Node create() {
				return new ExpMethodResponseNode();
			}
		});
		cParserMap.put(IMETHODRESPONSE, new FactoryEntry() {

			public Node create() {
				return new IMethodResponseNode();
			}
		});
		cParserMap.put(ERROR, new FactoryEntry() {

			public Node create() {
				return new ErrorNode();
			}
		});
		cParserMap.put(RETURNVALUE, new FactoryEntry() {

			public Node create() {
				return new ReturnValueNode();
			}
		});
		cParserMap.put(IRETURNVALUE, new FactoryEntry() {

			public Node create() {
				return new IReturnValueNode();
			}
		});
		cParserMap.put(CORRELATOR, new FactoryEntry() {

			public Node create() {
				return new CorrelatorNode();
			}
		});
	}

	private static final HashMap<String, String> NODENAME_HASH = new HashMap<String, String>();

	private static void initNodeNameHash(String[] pEnumA) {
		for (int i = 0; i < pEnumA.length; i++)
			NODENAME_HASH.put(pEnumA[i], pEnumA[i]);
	}

	static {
		initNodeNameHash(new String[] { CIM, DECLARATION, DECLGROUP, DECLGROUP_WITHNAME,
				DECLGROUP_WITHPATH, QUALIFIER_DECLARATION, SCOPE, VALUE, VALUE_ARRAY,
				VALUE_REFERENCE, VALUE_REFARRAY, VALUE_OBJECT, VALUE_NAMEDINSTANCE,
				VALUE_NAMEDOBJECT, VALUE_OBJECTWITHLOCALPATH, VALUE_OBJECTWITHPATH, VALUE_NULL,
				VALUE_INSTANCEWITHPATH,

				NAMESPACEPATH, LOCALNAMESPACEPATH, HOST, NAMESPACE, CLASSPATH, LOCALCLASSPATH,
				CLASSNAME, INSTANCEPATH, LOCALINSTANCEPATH, INSTANCENAME, OBJECTPATH, KEYBINDING,
				KEYVALUE,

				CLASS, INSTANCE, QUALIFIER, PROPERTY, PROPERTY_ARRAY, PROPERTY_REFERENCE, METHOD,
				PARAMETER, PARAMETER_REFERENCE, PARAMETER_ARRAY, PARAMETER_REFARRAY,

				/*
				 * TABLE stuff is missing yet
				 */
				MESSAGE, MULTIREQ, MULTIEXPREQ, SIMPLEREQ, SIMPLEEXPREQ, IMETHODCALL, METHODCALL,
				EXPMETHODCALL, PARAMVALUE, IPARAMVALUE, EXPPARAMVALUE, MULTIRSP, MULTIEXPRSP,
				SIMPLERSP, SIMPLEEXPRSP, METHODRESPONSE, EXPMETHODRESPONSE, IMETHODRESPONSE, ERROR,
				RETURNVALUE, IRETURNVALUE, CORRELATOR });
	}

}
