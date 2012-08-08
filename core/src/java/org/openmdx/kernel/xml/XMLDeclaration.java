/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: XMLDeclaration.java,v 1.3 2007/10/10 16:06:08 hburger Exp $
 * Description: XML Declaration
 * Revision:    $Revision: 1.3 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2007/10/10 16:06:08 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2005-2007, OMEX AG, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 * 
 * * Neither the name of the openMDX team nor the names of its
 *   contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * ------------------
 * 
 * This product includes or is based on software developed by other
 * organizations as listed in the NOTICE file.
 */
package org.openmdx.kernel.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML Declaration
 */
public class XMLDeclaration {

	/**
	 * Constructor
	 */
	public XMLDeclaration(
	){
	    super();
	}
	
	/**
	 * Constructor
	 */
	public XMLDeclaration(
		String version,
		String encoding,
		String standalone
	) {
		this.version = version;
		this.encoding = encoding;
		this.standalone = standalone;
	}

	/**
	 * Constructor
	 */
	public XMLDeclaration(
		XMLDeclaration that
	) {
		this(
			that.version,
			that.encoding,
			that.standalone
		);
	}

	/**
	 * The mandatory XML version attribute
	 */
	private String version;
	
	/**
	 * The optional encoding attribute
	 */
	private String encoding;
	
	/**
	 * The optional standalone attribute
	 */
	private String standalone;

	/**
	 * Maximal number of characters to read ahead.
	 */
	static final int READ_AHEAD_LIMIT = 100;
	
	/**
	 * 
	 */
	private static final Pattern XML_DECLARATION_PATTERN;
	
	/**
	 * @return Returns the encoding.
	 */
	public String getEncoding() {
		return 
			Encodings.ISO_8859_1.equals(this.encoding) ?
			    "ISO-8859-1" :
			Encodings.UTF_16BE.equals(this.encoding) ||
			Encodings.UTF_16BE_WITH_BOM.equals(this.encoding) ?
				"UTF-16BE" :
			Encodings.UTF_16LE.equals(this.encoding) ||
			Encodings.UTF_16LE_WITH_BOM.equals(this.encoding) ?
				"UTF-16LE" :
			Encodings.UTF_8.equals(this.encoding) ?
				"UTF-8" :
			Encodings.WINDOWS_1252.equals(this.encoding) ?
				"windows-1252" :
				this.encoding;
	}

	/**
	 * @param encoding The encoding to set.
	 */
	public void setQuotedEncoding(String encoding) {
		this.encoding = unquote(encoding);
	}

	/**
	 * @return Returns the standalone.
	 */
	public String getStandalone() {
		return standalone;
	}

	/**
	 * @param standalone The standalone to set.
	 */
	public void setQuotedStandalone(String standalone) {
		this.standalone = unquote(standalone);
	}

	/**
	 * @param encoding The version to set.
	 */
	public void setQuotedVersion(String version) {
		this.version = unquote(version);
	}
	
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Remove the suurounding &laquo;'&raquo; respectively &laquo;"&raquo; 
	 * characters.
	 *  
	 * @param quoted the embedded string; may be <code>null</code>
	 * @return the quoted string without its leading or trailing character;
	 * or <code>null</code> if quoted was <code>null</code>. 
	 */
	private static final String unquote(
		String quoted
	){
		return quoted == null || quoted.length() < 2 ? 
			null : 
			quoted.substring(1, quoted.length() - 1);
	}
	
	/**
	 * Consume the XML Declaration and return it or reset the stream otherwise.
	 * 
	 * @param in the stream
	 * @param regexpFactory 
	 * 
	 * @return the XML Declaration; or <code>null</code> if none has been 
	 * specified.
	 * 
	 * @throws IOException  
	 */
	public static XMLDeclaration readXMLDeclaration(
		InputStream in
	) throws IOException {
		return readXMLDeclaration(
			new InputStreamASCIIReader(in)
		);
	}

	/**
	 * Consume the XML Declaration and return it or reset the stream otherwise.
	 * 
	 * @param in the stream
	 * @param regexpFactory TODO
	 * 
	 * @return the XML Declaration; or <code>null</code> if none has been 
	 * specified.
	 * 
	 * @throws IOException  
	 */
	public static XMLDeclaration readXMLDeclaration(
		Reader in
	) throws IOException {
		in.mark(READ_AHEAD_LIMIT);
		try {
			if(
				in.read() == '<' &&
				in.read() == '?' &&
				in.read() == 'x' &&
				in.read() == 'm' &&
				in.read() == 'l'
			){
				StringBuffer b = new StringBuffer();
				xmlDeclaration: for(
					int i = 5, c = in.read();
					c > 0 && i++ < READ_AHEAD_LIMIT;
					c = in.read()
				){
					if(c == '>'){
                        Matcher matcher = XML_DECLARATION_PATTERN.matcher(b.toString());
                        if(!matcher.matches()) break xmlDeclaration;
						XMLDeclaration reply = new XMLDeclaration();
						reply.setQuotedVersion(matcher.group(1));
						reply.setQuotedEncoding(matcher.group(3));
						reply.setQuotedStandalone(matcher.group(5));
						return reply;
					} else {
						b.append((char)c);
					}
				}
			}
			in.reset();
			return null;
		} catch (IOException exception) {
			in.reset();
			throw exception;
		} catch (RuntimeException exception) {
			in.reset();
			throw exception;
		}
	}
	

	//------------------------------------------------------------------------
	// Extends Object
	//------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer b = new StringBuffer(
			"<?xml version=\""
		).append(
			getVersion()
		).append(
			'"'
		);
		if(this.encoding != null) b.append(
			" encoding=\""
		).append(
			getEncoding()
		).append(
			'"'
		);
		if(this.standalone != null) b.append(
			" standalone=\""
		).append(
			getStandalone()
		).append(
			'"'
		);
		return b.append(
			"?>"
		).toString();
	}

	
	//------------------------------------------------------------------------
	// Provide Patterns
	//------------------------------------------------------------------------

	static {
		String whitespace = "[ \n\r\t]";
		String optionalWhitespace = whitespace + '*';
		String mandatoryWhitespace = whitespace + '+';
		String value = optionalWhitespace + "=" + optionalWhitespace + 
			"('[^']*'|\"[^\"]*\")";
		XML_DECLARATION_PATTERN = Pattern.compile(
            "^" + 
			mandatoryWhitespace + "version" + value + "(" + 
			mandatoryWhitespace + "encoding" + value + ")?(" + 
			mandatoryWhitespace + "standalone" + value + ")?" + 
			optionalWhitespace + "\\?$"
        );
	}

}
