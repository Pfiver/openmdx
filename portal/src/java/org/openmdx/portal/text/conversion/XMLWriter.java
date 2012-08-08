/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Name:        $Id: XMLWriter.java,v 1.2 2007/01/21 20:47:27 wfro Exp $
 * Description: XMLWriter
 * Revision:    $Revision: 1.2 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2007/01/21 20:47:27 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004-2007, OMEX AG, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * 
 * * Neither the name of CRIXP Corp. nor the names of the contributors
 * to openCRX may be used to endorse or promote products derived
 * from this software without specific prior written permission
 * 
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
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 * 
 * This product includes yui, the Yahoo! UI Library
 * (License - based on BSD).
 *
 * This product includes yui-ext, the yui extension
 * developed by Jack Slocum (License - based on BSD).
 * 
 */
package org.openmdx.portal.text.conversion;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * A writer that scrubs certain ASCII characters into XML entities.
 */
public class XMLWriter extends FilterWriter {

  public XMLWriter(Writer writer) {
    super(writer);
  }

  public void write(char cbuf[], int off, int len) throws IOException {
    for(int i = off; i < off+len; i++) {
      this.write(cbuf[i]);
    }
  }

  public void write(String s, int off, int len) throws IOException {
      for(int i = off; i < off+len; i++) {
        this.write(s.charAt(i));
      }
    }

  public void write(char c) throws IOException {
    switch(c) {
      case '<':   out.write("&lt;"); break;
      case '>':   out.write("&gt;"); break;
      case '"':   out.write("&quot;"); break;
      case '\'':  out.write("&apos;"); break;
      case '&':   out.write("&amp;"); break;
      default:    out.write(c);
    }
  }

}

//--- End of File -----------------------------------------------------------
