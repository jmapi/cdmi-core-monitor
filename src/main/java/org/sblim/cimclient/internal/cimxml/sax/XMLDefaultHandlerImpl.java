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
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 *    2713    2013-11-22  blaschke-oss Enforce loose validation of CIM-XML documents
 */

package org.sblim.cimclient.internal.cimxml.sax;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.cim.CIMObjectPath;

import org.sblim.cimclient.internal.cimxml.sax.node.CIMNode;
import org.sblim.cimclient.internal.cimxml.sax.node.Node;
import org.sblim.cimclient.internal.cimxml.sax.node.NonVolatileIf;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class XMLDefaultHandlerImpl is DefaultHandler implementation which is used
 * for SAX and PULL style XML parsing.
 */
public class XMLDefaultHandlerImpl extends DefaultHandler {

	private Node iRootNode;

	private NodeStack iNodeStack = new NodeStack();

	private NodePool iNodePool = new NodePool();

	private StringBuffer iStrBuf;

	private SAXSession iSession;

	private boolean iAnyRoot;

	/**
	 * NodeStack
	 */
	static class NodeStack {

		private ArrayList<Node> iAL = new ArrayList<Node>();

		/**
		 * push
		 * 
		 * @param pNode
		 */
		public void push(Node pNode) {
			this.iAL.add(pNode);
		}

		/**
		 * pop
		 * 
		 * @return Node
		 */
		public Node pop() {
			if (this.iAL.size() == 0) return null;
			return this.iAL.remove(this.iAL.size() - 1);
		}

		/**
		 * peek
		 * 
		 * @return Node
		 */
		public Node peek() {
			if (this.iAL.size() == 0) return null;
			return this.iAL.get(this.iAL.size() - 1);
		}

	}

	/**
	 * Ctor.
	 * 
	 * @param pSession
	 *            - stores common variables for the whole parsing session
	 * @param pAnyRoot
	 *            - if true any CIM-XML element can be the root element of the
	 *            XML stream
	 */
	public XMLDefaultHandlerImpl(SAXSession pSession, boolean pAnyRoot) {
		this.iSession = pSession;
		this.iAnyRoot = pAnyRoot;
	}

	/**
	 * Ctor.
	 * 
	 * @param pLocalPath
	 *            - CIMObjectPathes without local paths will be extended by this
	 *            value
	 * @param pAnyRoot
	 *            - if true any CIM-XML element can be the root element of the
	 *            XML stream
	 */
	public XMLDefaultHandlerImpl(CIMObjectPath pLocalPath, boolean pAnyRoot) {
		this(new SAXSession(pLocalPath), pAnyRoot);
	}

	/**
	 * Ctor.
	 * 
	 * @param pLocalPath
	 *            - CIMObjectPathes without local paths will be extended by this
	 *            value
	 */
	public XMLDefaultHandlerImpl(CIMObjectPath pLocalPath) {
		this(pLocalPath, false);
	}

	/**
	 * Ctor.
	 */
	public XMLDefaultHandlerImpl() {
		this((CIMObjectPath) null, false);
	}

	/**
	 * @param uri
	 * @param localName
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		this.iStrBuf = null;
		String nodeNameEnum = NodeFactory.getEnum(qName);
		if (nodeNameEnum == null) {
			LogAndTraceBroker.getBroker()
					.trace(
							Level.FINEST,
							"Ignoring unrecognized starting CIM-XML element found during parsing: "
									+ qName);
			return;
		}

		Node parentNode = getPeekNode();
		if (parentNode == null) {
			if (!this.iAnyRoot && nodeNameEnum != NodeConstIf.CIM) throw new SAXException(
					"First node of CIM-XML document must be CIM! " + nodeNameEnum + " is invalid!");
		}
		if (parentNode != null) parentNode.testChild(nodeNameEnum);
		// let's look for a Node instance in the pool
		Node node = this.iNodePool.getNode(nodeNameEnum);
		// create an instance if pool didn't give us one
		if (node == null) {
			node = NodeFactory.getNodeInstance(nodeNameEnum);
		}
		if (parentNode != null) {
			if (parentNode instanceof NonVolatileIf) ((NonVolatileIf) parentNode).addChild(node);
		} else {
			this.iRootNode = node;
		}
		this.iNodeStack.push(node);
		node.init(attributes, this.iSession);
	}

	@Override
	public void characters(char ch[], int start, int length) {
		String str = new String(ch, start, length);
		// System.out.println("str("+str.length()+")="+str);
		if (this.iStrBuf == null) {
			this.iStrBuf = new StringBuffer(str);
		} else {
			this.iStrBuf.append(str);
		}
	}

	/**
	 * @param uri
	 * @param localName
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String nodeNameEnum = NodeFactory.getEnum(qName);
		if (nodeNameEnum == null) {
			LogAndTraceBroker.getBroker().trace(Level.FINEST,
					"Ignoring unrecognized ending CIM-XML element found during parsing: " + qName);
			return;
		}
		Node peekNode = this.iNodeStack.pop();
		try {
			// pass character data
			if (this.iStrBuf != null) {
				peekNode.parseData(this.iStrBuf.toString());
				this.iStrBuf = null;
			}
			// completeness check
			peekNode.testCompletness();

			// call parent's childParsed()
			Node parentNode = this.iNodeStack.peek();
			if (parentNode != null) {
				parentNode.childParsed(peekNode);
			}
		} finally {
			peekNode.setCompleted();
			// if peekNode is reusable place it into the NodePool
			if (!(peekNode instanceof NonVolatileIf)) {
				this.iNodePool.addNode(peekNode);
			}
		}
	}

	@Override
	public void endDocument() {
		String msg = "hits   : " + getNodePoolHits() + "\nmisses : " + getNodePoolMisses();
		LogAndTraceBroker.getBroker().trace(Level.FINE, msg);
	}

	/**
	 * getCIMNode
	 * 
	 * @return CIMNode, the root Element of the parsed CIM-XML document
	 */
	public CIMNode getCIMNode() {
		return this.iRootNode instanceof CIMNode ? (CIMNode) this.iRootNode : null;
	}

	/**
	 * getRootNode
	 * 
	 * @return Node, the root element of the parsed CIM-XML stream
	 */
	public Node getRootNode() {
		return this.iRootNode;
	}

	/**
	 * getNodePoolHits
	 * 
	 * @return int
	 */
	public int getNodePoolHits() {
		return this.iNodePool.getHitCnt();
	}

	/**
	 * getNodePoolMisses
	 * 
	 * @return int
	 */
	public int getNodePoolMisses() {
		return this.iNodePool.getMissCnt();
	}

	private Node getPeekNode() {
		return this.iNodeStack.peek();
	}
}
