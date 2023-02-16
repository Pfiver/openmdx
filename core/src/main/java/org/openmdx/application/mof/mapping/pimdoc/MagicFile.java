/*
 * ==================================================================== 
 * Project: openMDX, http://www.openmdx.org
 * Description: Magic Files
 * Owner: the original authors. 
 * ====================================================================
 * 
 * This software is published under the BSD license as listed below.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
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
package org.openmdx.application.mof.mapping.pimdoc;

import java.net.URL;

public enum MagicFile {


	/**
	 * The PIMDoc main page
	 */
	INDEX(null, "index.html"),

	/**
	 * The PIMDoc banner logo
	 */
	LOGO("logo-url", "logo.png"),

	/**
	 * The PIMDoc welcome page
	 */
	WELCOME("welcome-url", "welcome.html"),
	
	/**
	 * The PIMDoc text style sheet
	 */
	TEXT_STYLE_SHEET("text-style-sheet-url","text-style-sheet.css"),

	/**
	 * The PIMDoc image style sheet
	 */
	IMAGE_STYLE_SHEET("image-style-sheet-url","image-style-sheet.iss");
	
	private MagicFile(
		String propertyName,
		String fileName
	) {
		this.propertyName = propertyName;
		this.fileName = fileName;
	}
	
	private final String propertyName;
	private final String fileName;
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String getPropertyName() {
		return this.propertyName;
	}
	
	public URL getDefault() {
		return getClass().getResource("default-" + fileName);
	}

}
