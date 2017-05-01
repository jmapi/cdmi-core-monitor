/**
 * XMLPullParser.java
 *
 * (C) Copyright IBM Corp. 2005, 2013
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
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 18274      2005-09-12  fiuczy       String values get corrupted by CIM client
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 1708584    2007-04-27  ebak         CloseableIterator might not clean up streams
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2714989    2009-03-26  blaschke-oss Code cleanup from redundant null check et al
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3001359    2010-05-18  blaschke-oss XMLPullParser.CharString equals() method broken
 * 3019252    2010-06-21  blaschke-oss Methods concatenate strings using + in a loop
 * 3026316    2010-07-07  blaschke-oss XMLPullParser unused fields
 * 3026417    2010-07-07  blaschke-oss XMLAttributeValue does not use iHash field
 * 3028518    2010-07-14  blaschke-oss Additional StringBuilder use
 * 3048749    2010-08-20  blaschke-oss Hex digit parsing logic error in XMLPullParser
 * 3304058    2011-05-20  blaschke-oss Use same date format in change history
 *    2639    2013-05-11  blaschke-oss CDATA parsing broken in PULL parser
 */

package org.sblim.cimclient.internal.pullparser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;

import org.sblim.cimclient.internal.cimxml.sax.XMLDefaultHandlerImpl;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.xml.sax.Attributes;

/**
 * Class XMLPullParser is responsible for XML parsing.
 * 
 */
public class XMLPullParser {

	/**
	 * Class XMLAttributes is an Attributes implementation.
	 */
	class XMLAttributes implements Attributes {

		public int getIndex(String qName) {
			/*
			 * ebak: this is not good, since this Vector contains
			 * XMLAttributeValues return iAttributeNames.indexOf(qName);
			 */
			for (int i = 0; i < XMLPullParser.this.iAttributeNames.size(); i++) {
				XMLAttributeValue xmlAttrValue = XMLPullParser.this.iAttributeNames.get(i);
				if (qName.equals(xmlAttrValue.toString())) return i;
			}
			return -1;
		}

		/**
		 * @param uri
		 * @param localName
		 * @return int getIndex
		 */
		public int getIndex(String uri, String localName) {
			return 0;
		}

		public int getLength() {
			return XMLPullParser.this.iTotalAttributes;
		}

		/**
		 * @param index
		 * @return String getLocalName
		 */
		public String getLocalName(int index) {
			return EMPTY;
		}

		public String getQName(int index) {
			return XMLPullParser.this.iAttributeNames.get(index).getText();
		}

		/**
		 * @param index
		 * @return String getType
		 */
		public String getType(int index) {
			return EMPTY;
		}

		/**
		 * @param qName
		 * @return String getType
		 */
		public String getType(String qName) {
			return EMPTY;
		}

		/**
		 * @param uri
		 * @param localName
		 * @return String getType
		 */
		public String getType(String uri, String localName) {
			return EMPTY;
		}

		/**
		 * @param index
		 * @return String getURI
		 */
		public String getURI(int index) {
			return EMPTY;
		}

		public String getValue(int index) {
			return XMLPullParser.this.iAttributeValues.get(index).getText();
		}

		public String getValue(String qName) {
			/*
			 * ebak: the implementation have to return null if the attribute not
			 * found ebak: indexOf not good here, because iAttributeNames
			 * doesn't contain Strings
			 */
			int idx = getIndex(qName);
			if (idx < 0) return null;
			return getValue(idx);
		}

		/**
		 * @param uri
		 * @param localName
		 * @return String getType
		 */
		public String getValue(String uri, String localName) {
			return EMPTY;
		}
	}

	class XMLAttributeValue {

		int iCurrentPos;

		int iBegin, iLen;

		String iText;

		private boolean iTranslate;

		/**
		 * Ctor.
		 * 
		 * @param begin
		 * @param len
		 * @param translate
		 */
		public XMLAttributeValue(int begin, int len, boolean translate) {
			this.iBegin = begin;
			this.iLen = len;
			this.iTranslate = translate;
		}

		/**
		 * Ctor.
		 * 
		 * @param begin
		 * @param len
		 */
		public XMLAttributeValue(int begin, int len) {
			this.iBegin = begin;
			this.iLen = len;
			this.iTranslate = true;
		}

		/**
		 * getText
		 * 
		 * @return String
		 */
		public String getText() {
			if (this.iText == null) {
				if (this.iTranslate) {
					try {
						// Integer hashKey = new Integer(hash);
						// text = (String)stringTable.get(hashKey);
						// text = null;
						// if (text == null) {
						this.iText = _getChars();
						// stringTable.put(hashKey, text);
						// } else {
						// // System.out.println("Found:"+text);
						// cnt++;
						// }
					} catch (Exception e) {
						LogAndTraceBroker.getBroker().trace(Level.WARNING,
								"exception while decoding CHARACTERS XML", e);
						this.iText = new String(XMLPullParser.this.iBufferChar, this.iBegin,
								this.iLen);
					}
				} else {
					// Integer hashKey = new Integer(hash);
					// text = (String)stringTable.get(hashKey);
					// text = null;
					// if (text == null) {
					this.iText = new String(XMLPullParser.this.iBufferChar, this.iBegin, this.iLen);
					// stringTable.put(hashKey, text);
					// } else {
					// // System.out.println("Found:"+text);
					// cnt++;
					// }
				}
			}
			return this.iText;
		}

		// public void setTranslate(boolean translate) {
		// this.translate = translate;
		// }

		/**
		 * init
		 * 
		 * @param begin
		 * @param len
		 */
		public void init(int begin, int len) {
			this.iBegin = begin;
			this.iLen = len;
			this.iText = null;
		}

		/**
		 * setTranslate
		 * 
		 * @param translate
		 */
		public void setTranslate(boolean translate) {
			this.iTranslate = translate;
		}

		@Override
		public String toString() {
			return getText();
		}

		protected String _getChars() throws XMLPullParserException {
			StringBuffer attributeValue = new StringBuffer();
			int last = this.iBegin + this.iLen;
			char ch;
			// char prevCh = '\0'; //18274
			for (this.iCurrentPos = this.iBegin; this.iCurrentPos < last; this.iCurrentPos++) {
				ch = XMLPullParser.this.iBufferChar[this.iCurrentPos];
				if (ch == '&') {
					// try {
					// System.out.println(_currentPos);
					int ref = parseReference();

					if (ref > -1) attributeValue.append((char) ref);
					// }
					// catch (Exception e) {
					// e.printStackTrace();
					// }
					// } else if (ch == '\t'
					// || ch == '\r'
					// || ch == '\n') {
					//
					// if (ch != '\n' || prevCh != '\r'){
					// attributeValue.append(' ');
					// }
				} else {
					attributeValue.append(ch);
				}
				// prevCh = ch; //18274
			}
			return attributeValue.toString();
		}

		protected int parseReference() throws XMLPullParserException {
			this.iCurrentPos++;
			char ch1 = XMLPullParser.this.iBufferChar[this.iCurrentPos++];
			if (ch1 == '#') {
				ch1 = XMLPullParser.this.iBufferChar[this.iCurrentPos++];
				if (ch1 == 'x') {
					int value = 0;
					do {
						ch1 = XMLPullParser.this.iBufferChar[this.iCurrentPos++];
						if (ch1 >= '0' && ch1 <= '9') value = value * 16 + (ch1 - '0');
						else if (ch1 >= 'A' && ch1 <= 'F' || ch1 >= 'a' && ch1 <= 'f') value = value
								* 16 + (Character.toUpperCase(ch1) - 'A' + 10);
						else if (ch1 == ';') break;
						else throw new XMLPullParserException(
								"invalid character while parsing hex encoded number " + escape(ch1));
					} while (true);
					this.iCurrentPos--; // 18274
					return (char) value;
				}
				int value = 0;
				if (ch1 >= '0' && ch1 <= '9') {
					do {
						if (ch1 >= '0' && ch1 <= '9') {
							value = value * 10 + (ch1 - '0');
							ch1 = XMLPullParser.this.iBufferChar[this.iCurrentPos++];
						} else if (ch1 == ';') break;
						else throw new XMLPullParserException(
								"invalid character while parsing decimal encoded number: "
										+ escape(ch1));
					} while (true);
					this.iCurrentPos--; // 18274
					return (char) value;
				}
				throw new XMLPullParserException("invalid number format");
			}
			int startPos = this.iCurrentPos - 1;
			if (isValidStartElementNameChar(ch1)) {
				do {
					ch1 = XMLPullParser.this.iBufferChar[this.iCurrentPos++];
					if (ch1 == ';') break;
					if (!isValidElementNameChar(ch1)) throw new XMLPullParserException(
							"invalid reference character " + escape(ch1));
				} while (true);
			} else {
				throw new XMLPullParserException(
						"expected valid name start character for value reference");
			}
			this.iCurrentPos--;
			ch1 = XMLPullParser.this.iBufferChar[startPos];
			char ch2 = XMLPullParser.this.iBufferChar[startPos + 1];
			char ch3 = XMLPullParser.this.iBufferChar[startPos + 2];

			if (ch1 == 'l' && ch2 == 't' && ch3 == ';') {
				return '<';
			} else if (ch1 == 'g' && ch2 == 't' && ch3 == ';') {
				return '>';
			} else {
				char ch4 = XMLPullParser.this.iBufferChar[startPos + 3];
				if (ch1 == 'a' && ch2 == 'm' && ch3 == 'p' && ch4 == ';') { return '&'; }
				char ch5 = XMLPullParser.this.iBufferChar[startPos + 4];
				if (ch1 == 'a' && ch2 == 'p' && ch3 == 'o' && ch4 == 's' && ch5 == ';') {
					return '\'';
				} else if (ch1 == 'q' && ch2 == 'u' && ch3 == 'o' && ch4 == 't' && ch5 == ';') {
					return '\"';
				} else {
					// TODO return reference
				}
			}
			return -1;
		}
	}

	/**
	 * ATTRIBUTE
	 */
	public static final int ATTRIBUTE = 10;

	/**
	 * CDATA
	 */
	public static final int CDATA = 12;

	/**
	 * CHARACTERS
	 */
	public static final int CHARACTERS = 4;

	/**
	 * COMMENT
	 */
	public static final int COMMENT = 5;

	/**
	 * DTD
	 */
	public static final int DTD = 11;

	/**
	 * EMPTY
	 */
	public static final String EMPTY = "";

	/**
	 * END_DOCUMENT
	 */
	public static final int END_DOCUMENT = 8;

	/**
	 * END_ELEMENT
	 */
	public static final int END_ELEMENT = 2;

	/**
	 * ENTITY_DECLARATION
	 */
	public static final int ENTITY_DECLARATION = 15;

	/**
	 * ENTITY_REFERENCE
	 */
	public static final int ENTITY_REFERENCE = 9;

	/**
	 * NAMESPACE
	 */
	public static final int NAMESPACE = 13;

	/**
	 * NOTATION_DECLARATION
	 */
	public static final int NOTATION_DECLARATION = 14;

	/**
	 * PROCESSING_INSTRUCTION
	 */
	public static final int PROCESSING_INSTRUCTION = 3;

	/**
	 * SPACE
	 */
	public static final int SPACE = 6;

	/**
	 * START_DOCUMENT
	 */
	public static final int START_DOCUMENT = 7;

	/**
	 * START_ELEMENT
	 */
	public static final int START_ELEMENT = 1;

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	// this did testing
	}

	// TODO: ebak: this function seems to be wrong, because "synchronizes" to
	// IRETURNVALUE
	/**
	 * next
	 * 
	 * @param reader
	 * @param parserHdlr
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean next(XMLPullParser reader, XMLDefaultHandlerImpl parserHdlr)
			throws Exception {
		while (reader.hasNext()) {
			int event = reader.next();
			switch (event) {
				case START_ELEMENT:
					parserHdlr.startElement(EMPTY, EMPTY, reader.getElementName(), reader
							.getAttributes());
					break;
				case END_ELEMENT:
					parserHdlr.endElement(EMPTY, EMPTY, reader.getElementName());

					String lastElementName = null;
					if (reader.getElementNames().size() > 0) {
						ArrayList<String> elementNames = reader.getElementNames();
						lastElementName = elementNames.get(elementNames.size() - 1);
					}

					if (lastElementName != null && lastElementName.equalsIgnoreCase("IRETURNVALUE")) { return true; }
					break;
				case CHARACTERS:
					char[] buf = reader.getText().toCharArray();
					parserHdlr.characters(buf, 0, buf.length);
					break;
				case END_DOCUMENT:
					return false;
			}
		}
		return false;
	}

	ArrayList<XMLAttributeValue> iAttributeNames = new ArrayList<XMLAttributeValue>();

	Attributes iAttributes;

	ArrayList<XMLAttributeValue> iAttributeValues = new ArrayList<XMLAttributeValue>();

	char[] iBufferChar = null;

	XMLAttributeValue iCharacters;

	boolean iClosingElementNamePending;

	int iColNumber = 1;

	int iCurrentPosition = 0;

	int iCurrentState = 0;

	String iElementName;

	ArrayList<String> iElementNames = new ArrayList<String>();

	int iEndCharacters;

	int iFinishChar = 0;

	Reader iInstream;

	/*
	 * ebak: implement close
	 */
	boolean iClosed;

	int iLineNumber = 1;

	boolean iSeenEpilog;

	boolean iSeenProlog;

	int iStartCharacters;

	int iTotalAttributes;

	/**
	 * Ctor.
	 * 
	 * @param in
	 */
	public XMLPullParser(Reader in) {
		this.iInstream = in;
		reset();
	}

	/**
	 * close
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (this.iClosed) return;
		this.iClosed = true;
		this.iInstream.close();
	}

	/**
	 * getAttributes
	 * 
	 * @return Attributes
	 */
	public Attributes getAttributes() {
		if (this.iCurrentState != START_ELEMENT) return null;

		if (this.iAttributes == null) {
			this.iAttributes = new XMLAttributes();
		}
		return this.iAttributes;
	}

	/**
	 * getElementName
	 * 
	 * @return String
	 */
	public String getElementName() {
		return this.iElementName;
	}

	/**
	 * getElementNames
	 * 
	 * @return Vector
	 */
	public ArrayList<String> getElementNames() {
		return this.iElementNames;
	}

	/**
	 * getLevel
	 * 
	 * @return int
	 */
	public int getLevel() {
		return this.iElementNames.size();
	}

	/**
	 * getText
	 * 
	 * @return String
	 */
	public String getText() {
		String result = null;
		if (this.iCurrentState == CHARACTERS && this.iCharacters != null) { return this.iCharacters
				.getText(); }
		return result;
	}

	/**
	 * hasNext
	 * 
	 * @return boolean
	 */
	public boolean hasNext() {
		return !this.iClosed && this.iCurrentState != END_DOCUMENT;
	}

	/**
	 * next
	 * 
	 * @return int
	 * @throws IOException
	 */
	public int next() throws IOException {
		char ch;
		resetAttributes();
		ensureCapacity();
		if (this.iClosingElementNamePending) {
			this.iClosingElementNamePending = false;
			this.iElementNames.remove(this.iElementNames.size() - 1);
			this.iCurrentState = END_ELEMENT;
			return this.iCurrentState;
		}
		do {
			ch = (char) getNextCharCheckingEOF();
			if (ch == '<') {
				ch = (char) getNextChar();
				if (ch == '?') {
					if (this.iSeenProlog) { throw new XMLPullParserException(
							"The processing instruction target matching \"[xX][mM][lL]\" is not allowed."); }
					this.iSeenProlog = true;
					parsePI();
					ch = (char) getNextChar();
					ch = skipOptionalSpaces(ch);
					if (ch != '<') throw new XMLPullParserException(this,
							"Content is not allowed in prolog.");
					goBack();

					this.iCurrentState = START_DOCUMENT;
					return this.iCurrentState;
				} else if (ch == '!') {
					ch = (char) getNextChar();
					if (ch == '-') {
						parseComment();

						this.iCurrentState = COMMENT;
						return this.iCurrentState;
					} else if (ch == '[') {
						parseCDATA();
						this.iCurrentState = CHARACTERS;
						return this.iCurrentState;
					} else throw new XMLPullParserException(this, "unexpected char " + escape(ch));
				} else if (ch == '/') {
					parseEndElement();
					this.iCurrentState = END_ELEMENT;
					return this.iCurrentState;
				} else if (ch == '&') {
					parseUnknown();
				} else if (isValidStartElementNameChar(ch)) {
					if (!this.iSeenProlog) {
						this.iSeenProlog = true;
						this.iCurrentState = START_DOCUMENT;
						goBack();
						goBack();
						return this.iCurrentState;
					}
					parseStartElement(ch);
					this.iCurrentState = START_ELEMENT;
					return this.iCurrentState;
				} else {
					throw new XMLPullParserException(this, "unexpected char " + escape(ch));
				}
			} else {
				this.iStartCharacters = this.iCurrentPosition - 1;
				boolean amp = false;
				// int hash = 0;
				// int n = 0;
				do {
					// n = (n+1)&15;
					// hash = hash*primes[n]+ch;

					ch = (char) getNextCharCheckingEOF();
					if (ch == (char) -1) {
						if (this.iElementNames.size() != 0) throw new XMLPullParserException(this,
								"unexpected EOF ");

						this.iCurrentState = END_DOCUMENT;
						return this.iCurrentState;
					} else if (ch == '\r' || ch == '\n') { /* :) */} else {
						if (!isSpace(ch) && ch != '<' && this.iElementNames.size() == 0) {
							if (!this.iSeenProlog) throw new XMLPullParserException(this,
									"Content is not allowed in trailing section.");
							throw new XMLPullParserException(this,
									"Content is not allowed in trailing section.");
						}
					}
					amp = false;
					if (ch == '&') {
						amp = true;
						int i = parseReference();
						ch = (char) (i & 0xFFFF);
					}
				} while (ch != '<' || amp);
				this.iEndCharacters = this.iCurrentPosition;
				goBack();
				if (this.iElementNames.size() > 0) {
					if (this.iCharacters == null) this.iCharacters = new XMLAttributeValue(
							this.iStartCharacters, this.iEndCharacters - this.iStartCharacters - 1);
					else {
						this.iCharacters.init(this.iStartCharacters, this.iEndCharacters
								- this.iStartCharacters - 1);
						this.iCharacters.setTranslate(true);
					}

					this.iCurrentState = CHARACTERS;
					return this.iCurrentState;
				}
			}
		} while (true);
	}

	/**
	 * reset
	 */
	public void reset() {
		this.iSeenProlog = true;
		this.iCurrentState = 0;
		this.iClosingElementNamePending = false;
		this.iColNumber = 1;
		this.iLineNumber = 1;
		this.iElementName = null;
		this.iElementNames.clear();
		this.iAttributeNames.clear();
		this.iAttributeValues.clear();
		this.iAttributes = null;

		this.iStartCharacters = 0;
		this.iEndCharacters = 0;

		this.iTotalAttributes = 0;
		this.iSeenProlog = false;
		this.iSeenEpilog = false;
	}

	@Override
	public String toString() {
		switch (this.iCurrentState) {
			case START_ELEMENT: {
				StringBuilder sb = new StringBuilder("START ELEM: <");
				sb.append(this.iElementName);
				if (this.iAttributeNames.size() > 0) {
					sb.append(" ");
					for (int i = 0; i < this.iAttributeNames.size(); i++) {
						sb.append(this.iAttributeNames.get(i));
						sb.append("=\"");
						sb.append(this.iAttributeValues.get(i));
						sb.append("\" ");
					}
				}
				sb.append(">");
				return sb.toString();
			}
			case END_ELEMENT: {
				String s = "END ELEM: </" + this.iElementName + ">";
				return s;
			}
			case CHARACTERS: {
				return "CHARACTERS: \"" + getText(); // .replaceAll("\n",
				// "\\\\n").replaceAll("\r",
				// "\\\\r").replaceAll("\t",
				// "\\\\t")+"\"";
			}
		}
		return "UNKOWN";
	}

	protected char _getNextChar() {
		return (char) -1;
	}

	protected void addAttribute(int begName, int lenName, int begValue, int lenValue) {
		if (this.iAttributeNames.size() > this.iTotalAttributes) {
			XMLAttributeValue attribute = this.iAttributeValues.get(this.iTotalAttributes);
			XMLAttributeValue name = this.iAttributeNames.get(this.iTotalAttributes);
			this.iTotalAttributes++;
			attribute.init(begValue, lenValue);
			attribute.setTranslate(true);
			name.init(begName, lenName);
			name.setTranslate(false);
		} else {
			XMLAttributeValue attribute = new XMLAttributeValue(begValue, lenValue);
			XMLAttributeValue name = new XMLAttributeValue(begName, lenName, false);
			this.iTotalAttributes++;
			this.iAttributeNames.add(name);
			this.iAttributeValues.add(attribute);
		}
	}

	protected void ensureCapacity() {
		if (this.iBufferChar == null) this.iBufferChar = new char[1024];

		if (this.iCurrentPosition >= (8 * this.iBufferChar.length) / 10) {
			System.arraycopy(this.iBufferChar, this.iCurrentPosition, this.iBufferChar, 0,
					this.iFinishChar - this.iCurrentPosition);
			this.iFinishChar -= this.iCurrentPosition;
			this.iCurrentPosition = 0;
		}
	}

	protected String escape(char ch) {
		String result;
		if (ch == '\n') result = "\'\\n\'";
		if (ch == '\r') result = "\'\\r\'";
		if (ch == '\t') result = "\'\\t\'";
		if (ch == '\'') result = "\'\\'\'";
		if (ch > '\177' || ch < ' ') result = "\'\\u" + Integer.toHexString(ch) + "\'";
		else result = "\'" + ch + "\'";
		return result;
	}

	protected int getChar() throws IOException {
		if (this.iFinishChar <= this.iCurrentPosition) {
			if (this.iBufferChar == null) {
				this.iBufferChar = new char[1024];
			} else if (this.iFinishChar >= this.iBufferChar.length) {
				char[] tmp = this.iBufferChar;
				this.iBufferChar = new char[this.iBufferChar.length << 1];
				System.arraycopy(tmp, 0, this.iBufferChar, 0, tmp.length);
			}

			int total = this.iInstream.read(this.iBufferChar, this.iFinishChar,
					this.iBufferChar.length - this.iFinishChar);

			if (total <= 0) { return -1; }
			this.iFinishChar += total;
		}
		return this.iBufferChar[this.iCurrentPosition++];
	}

	protected int getNextChar() throws IOException {
		int ch;

		if (this.iFinishChar <= this.iCurrentPosition) {
			ch = getChar();
		} else ch = this.iBufferChar[this.iCurrentPosition++];

		if (ch == -1) throw new XMLPullParserException(this, "unexpected end of document");
		if (ch == '\n') {
			this.iLineNumber++;
			this.iColNumber = 1;
		} else this.iColNumber++;

		return (char) ch;
	}

	protected int getNextCharCheckingEOF() throws IOException {
		int ch;

		if (this.iFinishChar <= this.iCurrentPosition) {
			ch = getChar();
		} else ch = this.iBufferChar[this.iCurrentPosition++];

		if (ch == '\n') {
			this.iLineNumber++;
			this.iColNumber = 1;
		} else this.iColNumber++;

		return (char) ch;
	}

	protected void goBack() {
		this.iCurrentPosition--;
		if (this.iColNumber > 1) {
			this.iColNumber--;
		}
	}

	protected boolean isSpace(char ch) {
		return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t';
	}

	protected boolean isValidElementNameChar(char ch) {
		return (ch < 256 && (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch == '_'
				|| ch == ':' || ch == '-' || ch == '.' || ch >= '0' && ch <= '9' || ch == '\267'))
				|| ch >= '\300'
				&& ch <= '\u02FF'
				|| ch >= '\u0370'
				&& ch <= '\u037D'
				|| ch >= '\u0300'
				&& ch <= '\u036F'
				|| ch >= '\u037F'
				&& ch <= '\u2027'
				|| ch >= '\u202A' && ch <= '\u218F' || ch >= '\u2800' && ch <= '\uFFEF';
		// return isValidStartElementNameChar(ch)
		// || (ch < 256 && (ch == '-'
		// || ch == '.'
		// || ch >= '0' && ch <= '9'
		// || ch == '\267'))
		// || ch >= '\u0300' && ch <= '\u036F'
		// || ch >= '\u0400' && ch <= '\u2027'
		// || ch >= '\u202A' && ch <= '\u218F'
		// || ch >= '\u2800' && ch <= '\uFFEF';
	}

	protected boolean isValidStartElementNameChar(char ch) {
		return (ch < 256 && (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch == '_' || ch == ':'))
				|| (ch >= '\300' && ch <= '\u02FF' || ch >= '\u0370' && ch <= '\u037D'
						|| ch >= '\u037F' && ch <= '\u0400' || ch >= '\u0400' && ch <= '\u2027'
						|| ch >= '\u202A' && ch <= '\u218F' || ch >= '\u2800' && ch <= '\uFFEF');
	}

	protected void parseAttribute(char ch) throws IOException {
		int startAttributeName = this.iCurrentPosition - 1;
		int endAttributeName;
		// int hash = 0;
		// int n = 0;
		do {
			// n = (n+1)&15;
			// hash = hash*primes[n]+ch;

			ch = (char) getNextChar();
		} while (isValidElementNameChar(ch));
		endAttributeName = this.iCurrentPosition;
		// String attributeName = new String(bufferChar, startAttributeName,
		// endAttributeName - startAttributeName-1);
		// attributeNames.add(attributeName);

		ch = skipOptionalSpaces(ch);

		if (ch != '=') throw new XMLPullParserException(this, "missing character \'=\'instead "
				+ escape(ch) + " was found ");

		ch = (char) getNextChar();
		ch = skipOptionalSpaces(ch);

		char delimiter;
		if (ch != '\"' && ch != '\'') throw new XMLPullParserException(this,
				"missing character \'\"\' or \'\'\' instead " + escape(ch) + " was found ");
		delimiter = ch;

		// StringBuffer attributeValue = new StringBuffer();
		int startAttributeValue = this.iCurrentPosition;
		char prevCh = '\0';
		// int hashvalue = 0;
		// n = 0;
		do {
			// n = (n+1)&15;
			// hashvalue = hashvalue *primes[n] +ch;

			ch = (char) getNextChar();
			if (ch == delimiter) break;
			else if (ch == '<' || ch == '>') throw new XMLPullParserException(this,
					"illegal character " + escape(ch));
			else if (ch == '&') {
				int ref = parseReference();
				ch = (char) (ref & 0xffff);
				// attributeValue.append((char)ref);
			} else if (ch == '\t' || ch == '\r' || ch == '\n') {

				if (ch != '\n' || prevCh != '\r') {
					// attributeValue.append(' ');
				}
			} else {
				// attributeValue.append(ch);
			}
			prevCh = ch;
		} while (true);
		int endAttributeValue = this.iCurrentPosition;
		// attributeValues.add(attributeValue.toString());
		addAttribute(startAttributeName, endAttributeName - startAttributeName - 1,
				startAttributeValue, endAttributeValue - startAttributeValue - 1);

		return;
	}

	protected int parseCDATA() throws IOException {
		char ch;

		if ((char) getNextChar() != 'C' || (char) getNextChar() != 'D'
				|| (char) getNextChar() != 'A' || (char) getNextChar() != 'T'
				|| (char) getNextChar() != 'A' || (char) getNextChar() != '[') throw new XMLPullParserException(
				"CDATA must start with \"<![CDATA[\".");
		boolean braketFound = false;
		boolean doubleBraket = false;
		int startCharacter = this.iCurrentPosition;
		do {
			ch = (char) getNextCharCheckingEOF();
			if (ch == ']') {
				if (braketFound) doubleBraket = true;
				braketFound = true;
			} else if (ch == '>' && doubleBraket) {
				break;
			} else {
				braketFound = false;
				doubleBraket = false;
			}
			if (ch == (char) -1) throw new XMLPullParserException(
					"XML document structures must start and end within the same entity.");
		} while (true);

		int endCharacter = this.iCurrentPosition - 3;

		this.iCharacters.setTranslate(false);
		this.iCharacters.init(startCharacter, endCharacter - startCharacter);

		return -1;
	}

	protected int parseComment() throws IOException {
		char ch;
		ch = (char) getNextChar();
		if (ch != '-') throw new XMLPullParserException("Comment must start with \"<!--\".");
		boolean dashFound = false;
		boolean doubleDash = false;
		do {
			ch = (char) getNextCharCheckingEOF();
			if (ch == '-') {
				if (dashFound) doubleDash = true;
				dashFound = true;
			} else if (ch == '>' && doubleDash) {
				break;
			} else {
				dashFound = false;
				doubleDash = false;
			}
			if (ch == (char) -1) throw new XMLPullParserException(
					"XML document structures must start and end within the same entity.");
		} while (true);

		// if (!seenProlog) {
		// ch = (char)getNextChar();
		// skipOptionalSpaces(ch);
		// goBack();
		// }
		return -1;
	}

	protected void parseEndElement() throws IOException {
		int startElementName;
		int endElementName;

		char ch;

		startElementName = this.iCurrentPosition;
		do {
			ch = (char) getNextChar();
		} while (isValidElementNameChar(ch));

		endElementName = this.iCurrentPosition;
		this.iElementName = new String(this.iBufferChar, startElementName, endElementName
				- startElementName - 1);

		if (!this.iElementNames.get(this.iElementNames.size() - 1).equals(
				this.iElementName.toUpperCase())) throw new XMLPullParserException(this,
				"The content of elements must consist of well-formed character data or markup.");

		this.iElementNames.remove(this.iElementNames.size() - 1);

		ch = skipOptionalSpaces(ch);
		if (ch != '>') throw new XMLPullParserException(this, "\'=\' was expected, but \'"
				+ escape(ch) + "\' was found instead");

		if (this.iElementNames.size() == 0) this.iSeenEpilog = true;
	}

	protected int parsePI() throws IOException {
		char ch;
		ch = (char) getNextChar();
		boolean dashFound = false;
		do {
			ch = (char) getNextCharCheckingEOF();
			if (ch == '?') {
				dashFound = true;
			} else if (ch == '>' && dashFound) {
				break;
			} else {
				dashFound = false;
			}
			if (ch == (char) -1) throw new XMLPullParserException(
					"XML document structures must start and end within the same entity.");
		} while (true);

		return -1;
	}

	protected int parseReference() throws IOException {
		char ch1 = (char) getNextChar();
		if (ch1 == '#') {
			ch1 = (char) getNextChar();
			if (ch1 == 'x') {
				int value = 0;
				do {
					ch1 = (char) getNextChar();
					if (ch1 >= '0' && ch1 <= '9') value = value * 16 + (ch1 - '0');
					else if (ch1 >= 'A' && ch1 <= 'F' || ch1 >= 'a' && ch1 <= 'f') value = value
							* 16 + (Character.toUpperCase(ch1) - 'A' + 10);
					else if (ch1 == ';') break;
					else throw new XMLPullParserException(this,
							"invalid character while parsing hex encoded number " + escape(ch1));
				} while (true);
				return (char) value;
			}
			int value = 0;
			if (ch1 >= '0' && ch1 <= '9') {
				do {
					if (ch1 >= '0' && ch1 <= '9') {
						value = value * 10 + (ch1 - '0');
						ch1 = (char) getNextChar();
					} else if (ch1 == ';') break;
					else throw new XMLPullParserException(this,
							"invalid character while parsing decimal encoded number: "
									+ escape(ch1));
				} while (true);
				return (char) value;
			}
			throw new XMLPullParserException(this, "invalid number format");
		}
		int startPos = this.iCurrentPosition - 1;
		if (isValidStartElementNameChar(ch1)) {
			do {
				ch1 = (char) getNextChar();
				if (ch1 == ';') break;
				if (!isValidElementNameChar(ch1)) throw new XMLPullParserException(
						"invalid reference character " + escape(ch1));
			} while (true);
		} else {
			throw new XMLPullParserException(this,
					"expected valid name start character for value reference");
		}
		goBack();
		ch1 = this.iBufferChar[startPos];
		char ch2 = this.iBufferChar[startPos + 1];
		char ch3 = this.iBufferChar[startPos + 2];

		if (ch1 == 'l' && ch2 == 't' && ch3 == ';') {
			return '<';
		} else if (ch1 == 'g' && ch2 == 't' && ch3 == ';') {
			return '>';
		} else {
			char ch4 = this.iBufferChar[startPos + 3];
			if (ch1 == 'a' && ch2 == 'm' && ch3 == 'p' && ch4 == ';') { return '&'; }
			char ch5 = this.iBufferChar[startPos + 4];
			if (ch1 == 'a' && ch2 == 'p' && ch3 == 'o' && ch4 == 's' && ch5 == ';') {
				return '\'';
			} else if (ch1 == 'q' && ch2 == 'u' && ch3 == 'o' && ch4 == 't' && ch5 == ';') {
				return '\"';
			} else {
				// TODO return reference
			}
		}
		return -1;
	}

	protected int parseStartElement(char ch) throws IOException {
		int startElementName;
		int endElementName;

		resetAttributes();

		startElementName = this.iCurrentPosition - 1;

		do {
			// n = (n+1)&15;
			// hash = hash*primes[n]+ch;

			ch = (char) getNextChar();
		} while (isValidElementNameChar(ch));

		endElementName = this.iCurrentPosition;
		// Integer hashKey = new Integer(hash);
		// elementName = (String)stringTable.get(hashKey);
		// elementName = null;
		// if (elementName == null) {
		this.iElementName = new String(this.iBufferChar, startElementName, endElementName
				- startElementName - 1);
		// stringTable.put(hashKey, elementName);
		// } else {
		// // System.out.println("Found: "+elementName);
		// cnt++;
		// }
		this.iElementNames.add(this.iElementName.toUpperCase());

		do {
			ch = skipOptionalSpaces(ch);
			if (ch == '>') {
				break;
			} else if (ch == '/') {
				ch = (char) getNextChar();
				if (ch != '>') { throw new XMLPullParserException(this,
						"\'=\' was expected, but \'" + escape(ch) + "\' was found instead"); }
				this.iClosingElementNamePending = true;
			} else if (isValidStartElementNameChar(ch)) {
				parseAttribute(ch);
				ch = (char) getNextChar();
			} else throw new XMLPullParserException(this,
					"Element type \"CIM\" must be followed by either attribute specifications, \">\" or \"/>\".");

		} while (true);
		return -1;
	}

	protected void parseUnknown() throws IOException {
		char ch;
		do {
			ch = (char) getNextChar();
			if (ch == '<') throw new XMLPullParserException(
					"\'>\' was expected, but \'<\' was found instead.");
		} while (ch != '>');
	}

	protected void resetAttributes() {
		this.iTotalAttributes = 0;
		// attributeNames.setSize(0);
		// ebak: clear attributes
		this.iAttributeNames.clear();
		this.iAttributeValues.clear();
		// /clear attributes
	}

	protected char skipOptionalSpaces(char ch) throws IOException {
		while (isSpace(ch)) {
			ch = (char) getNextChar();
		}
		return ch;
	}

	protected char skipRequiredSpaces(char ch) throws IOException {
		if (!isSpace(ch)) throw new XMLPullParserException(this, "space expected");
		do {
			ch = (char) getNextChar();
		} while (isSpace(ch));
		return ch;
	}

}
