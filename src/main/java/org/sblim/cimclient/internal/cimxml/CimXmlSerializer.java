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
 * 1671502    2007-02-08  lupusalex    Remove dependency from Xerces
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3510321    2012-03-23  blaschke-oss Handle CDATA in CimXmlSerializer
 * 3513357    2012-04-01  blaschke-oss Handle multiple CDATAs in CimXmlSerializer
 */

package org.sblim.cimclient.internal.cimxml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.sblim.cimclient.internal.util.WBEMConstants;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Class CimXmlSerializer implements a XML serializer for DOM documents that is
 * specialized for CIM-XML. It might not be used as a general purpose serializer
 * since it doesn't support any DOM or XML features not required by CIM-XML.
 * 
 */
public class CimXmlSerializer {

	/**
	 * Class XmlWriter implements a writer on an output stream that escapes XML
	 * values according to the CIM-XML requirements.
	 * 
	 */
	private static class XmlWriter {

		private BufferedWriter iWriter;

		/**
		 * Ctor.
		 * 
		 * @param pOut
		 *            The output stream the serialized document is written to
		 * @param pCharsetName
		 *            The encoding the use for the output stream
		 */
		public XmlWriter(OutputStream pOut, String pCharsetName) {
			this.iWriter = new BufferedWriter(new OutputStreamWriter(pOut, Charset.forName(
					pCharsetName).newEncoder()));
		}

		/**
		 * Writes text to the stream
		 * 
		 * @param pText
		 *            The text
		 * @throws IOException
		 */
		public void write(String pText) throws IOException {
			if (pText != null) this.iWriter.write(pText);
		}

		/**
		 * Closes the stream
		 * 
		 * @throws IOException
		 */
		public void close() throws IOException {
			this.iWriter.close();
		}

		/**
		 * Flushes the buffer to the stream
		 * 
		 * @throws IOException
		 */
		public void flush() throws IOException {
			this.iWriter.flush();
		}

		/**
		 * Writes a XML value (either attribute or text node). The value is
		 * escaped as follows:<br />
		 * <br />
		 * <table border="1">
		 * <tr>
		 * <th>char</th>
		 * <th>result</th>
		 * </tr>
		 * <tr>
		 * <td align="center">&lt; space</td>
		 * <td>&amp;#xnn;</td>
		 * </tr>
		 * <tr>
		 * <td align="center">&gt; ~</td>
		 * <td>unchanged (UTF-8)</td>
		 * </tr>
		 * <tr>
		 * <td align="center">space</td>
		 * <td>unchanged or &amp;#x20;<br />
		 * (First leading, last trailing and every other space are escaped)</td>
		 * </tr>
		 * <tr>
		 * <td align="center">&lt;</td>
		 * <td>&amp;lt;</td>
		 * </tr>
		 * <tr>
		 * <td align="center">&gt;</td>
		 * <td>&amp;gt;</td>
		 * </tr>
		 * <tr>
		 * <td align="center">&amp;</td>
		 * <td>&amp;amp;</td>
		 * </tr>
		 * <tr>
		 * <td align="center">"</td>
		 * <td>&amp;quot;</td>
		 * </tr>
		 * <tr>
		 * <td align="center">'</td>
		 * <td>&amp;apos;</td>
		 * <tr>
		 * <td align="center">other</td>
		 * <td>unchanged</td>
		 * </tr>
		 * </tr>
		 * </table>
		 * 
		 * @param pText
		 *            The text
		 * @throws IOException
		 */
		public void writeValue(final String pText) throws IOException {
			if (pText == null) { return; }
			boolean escapeSpace = true;
			final int oneBeforeLast = pText.length() - 2;
			for (int i = 0; i < pText.length(); ++i) {

				char currentChar = pText.charAt(i);
				boolean isSpace = false;

				if (isHighSurrogate(currentChar)) {
					if (i > oneBeforeLast || !isLowSurrogate(pText.charAt(i + 1))) { throw new IOException(
							"Illegal Unicode character"); }
					this.iWriter.write(pText, i++, 2);
				} else if (currentChar < ' ') {
					writeAsHex(currentChar);
				} else if (currentChar > '~') {
					this.iWriter.write(currentChar);
				} else {
					switch (currentChar) {
						case ' ':
							isSpace = true;
							if (escapeSpace) {
								writeAsHex(currentChar);
							} else {
								this.iWriter.write(currentChar);
							}
							break;
						case '<':
							this.iWriter.write("&lt;");
							break;
						case '>':
							this.iWriter.write("&gt;");
							break;
						case '&':
							this.iWriter.write("&amp;");
							break;
						case '"':
							this.iWriter.write("&quot;");
							break;
						case '\'':
							this.iWriter.write("&apos;");
							break;
						default:
							this.iWriter.write(currentChar);
					}
				}
				escapeSpace = (isSpace && !escapeSpace) || (i == oneBeforeLast);
			}
		}

		private void writeAsHex(char pChar) throws IOException {
			this.iWriter.write("&#x" + Integer.toHexString(pChar) + ";");
		}

		private boolean isHighSurrogate(char pChar) {
			return pChar >= WBEMConstants.UTF16_MIN_HIGH_SURROGATE
					&& pChar <= WBEMConstants.UTF16_MAX_HIGH_SURROGATE;
		}

		private boolean isLowSurrogate(char pChar) {
			return pChar >= WBEMConstants.UTF16_MIN_LOW_SURROGATE
					&& pChar <= WBEMConstants.UTF16_MAX_LOW_SURROGATE;
		}

	}

	private boolean iPretty;

	private int iIndent = 0;

	private boolean iLastClosed = false;

	private final String CDATA_START = "<![CDATA[";

	private final String CDATA_END = "]]>";

	private CimXmlSerializer(boolean pPretty) {
		this.iPretty = pPretty;
	}

	/**
	 * Serializes a given DOM document as (CIM-)XML to a given output stream.
	 * The method writes first
	 * <code>&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</code>
	 * and then serializes the document node. If you want to suppress this
	 * header just call {@link #serialize(OutputStream, Node, boolean)} on the
	 * document node.
	 * 
	 * @param pOS
	 *            The output stream
	 * @param pDoc
	 *            The document
	 * @param pPretty
	 *            If <code>true</code> the XML is nicely wrapped and indented,
	 *            otherwise it's all in one line
	 * @throws IOException
	 *             Whenever something goes wrong
	 */
	public static void serialize(OutputStream pOS, Document pDoc, boolean pPretty)
			throws IOException {

		try {
			XmlWriter writer = new XmlWriter(pOS, WBEMConstants.UTF8);
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			new CimXmlSerializer(pPretty).serializeNode(writer, pDoc.getDocumentElement());
			writer.flush();
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Serializes a given DOM node as (CIM-)XML to a given output stream
	 * 
	 * @param pOS
	 *            The output stream
	 * @param pNode
	 *            The node
	 * @param pPretty
	 *            If <code>true</code> the XML is nicely wrapped and indented,
	 *            otherwise it's all in one line
	 * @throws IOException
	 *             Whenever something goes wrong
	 */
	public static void serialize(OutputStream pOS, Node pNode, boolean pPretty) throws IOException {

		try {
			XmlWriter writer = new XmlWriter(pOS, WBEMConstants.UTF8);
			new CimXmlSerializer(pPretty).serializeNode(writer, pNode);
			writer.flush();
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	private void serializeNode(XmlWriter pWriter, Node pNode) throws IOException {
		switch (pNode.getNodeType()) {
			case Node.ELEMENT_NODE:
				pWriter.write(indent());
				pWriter.write("<");
				pWriter.write(pNode.getNodeName());
				NamedNodeMap attributes = pNode.getAttributes();
				if (attributes != null) {
					for (int i = 0; i < attributes.getLength(); ++i) {
						pWriter.write(" ");
						serializeNode(pWriter, attributes.item(i));
					}
				}
				Node child = pNode.getFirstChild();
				if (child == null) {
					pWriter.write("/>");
					this.iLastClosed = true;
					break;
				}
				pWriter.write(">");
				++this.iIndent;
				this.iLastClosed = false;
				while (child != null) {
					serializeNode(pWriter, child);
					child = child.getNextSibling();
				}
				--this.iIndent;
				if (this.iLastClosed) {
					pWriter.write(indent());
				}
				pWriter.write("</");
				pWriter.write(pNode.getNodeName());
				pWriter.write(">");
				this.iLastClosed = true;
				break;
			case Node.ATTRIBUTE_NODE:
				pWriter.write(pNode.getNodeName());
				pWriter.write("=\"");
				pWriter.writeValue(pNode.getNodeValue());
				pWriter.write("\"");
				break;
			case Node.TEXT_NODE:
				String value = pNode.getNodeValue();
				if (value != null) {
					int idx = 0;
					int len = value.length();

					while (idx < len) {
						int cdata = value.indexOf(this.CDATA_START, idx);

						// rest of string not CDATA, write all (escaped)
						if (cdata == -1) {
							pWriter.writeValue(value.substring(idx));
							break;
						}

						// write characters before CDATA (escaped)
						if (idx < cdata) {
							pWriter.writeValue(value.substring(idx, cdata));
							idx = cdata;
						}

						int end = value.indexOf(this.CDATA_END, idx);

						// invalid CDATA
						if (end == -1) { throw new IOException("CDATA section not closed: " + value); }

						// write CDATA (not escaped)
						pWriter.write(value.substring(idx, end + this.CDATA_END.length()));
						idx = end + this.CDATA_END.length();
					}
				}
		}
	}

	private String indent() {
		if (!this.iPretty) { return ""; }
		StringBuffer result = new StringBuffer();
		result.append('\n');
		for (int i = 0; i < this.iIndent; ++i) {
			result.append(' ');
		}
		return result.toString();
	}
}
